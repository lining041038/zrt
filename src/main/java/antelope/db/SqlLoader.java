package antelope.db;







import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import antelope.consts.GlobalConsts;
import antelope.utils.ClasspathResourceUtil;
import antelope.utils.ClasspathResourceUtil.NoCacheReturn;
import antelope.utils.RegExpUtil;


/**
 * <p>Title: 可以配置参数的sql工具,sql文件加载器</p>
 * <p>Description: 此工具用来将sql剥离到代码外层，此类可以将自定义sql代码加载到外围</p>
 * <p>Copyright: Smartdot Corporation Copyright (c) 2011</p>
 * <p>Company: BEIJING Smartdot SOFTWARE CO.,LTD</p> 
 * @author lining
 * @version 1.0 
 */
public class SqlLoader {
	private static SqlLoader sqlLoader = new SqlLoader();
	private static Log log = LogFactory.getLog(SqlLoader.class);
	private static final Object locker = new Object();

	private static Pattern ptprops = Pattern.compile("[\\w]*[ ]*=[ ]*[^\\s=]*");
	private static Pattern ptSqlatoms = Pattern.compile("(?<=---)[^;]*|--[^\r\n]*---");
	private static Pattern ptAutoAssemble = Pattern.compile("(?<=\\@\\{)[\\s]*[^}]*[\\s]*(?=\\})");
	
	private Map<String, Sql> sqls = new HashMap<String, Sql>();
	private Set<String> loadedFileName = new HashSet<String>();

	private SqlLoader() {
	}

	public static SqlLoader getInstance() {
		return sqlLoader;
	}

	public SqlLoader load(String resource) throws IOException, SQLException {
		log.info("reading sql file:" + resource);
		if (sqlLoader.loadedFileName.contains(resource))
			return this;

		Matcher sqlatoms = ptSqlatoms.matcher(gainSqlText(resource).result);
		afterLoading(resource, sqlatoms, false);
		return this;
	}
	
	public SqlLoader loadBySqlText(String sqlText, String resourceFileName) throws SQLException {
		Matcher sqlatoms = ptSqlatoms.matcher(sqlText);
		afterLoading(resourceFileName, sqlatoms, false);
		return this;
	}
	
	public SqlLoader load(File file) throws IOException, SQLException {
		String resource = file.getAbsolutePath();
		if (resource.indexOf("WEB-INF\\classes\\") != -1)
			resource = resource.substring(resource.indexOf("WEB-INF\\classes\\") + 15).replaceAll("\\\\", "/");
		load(resource);
		return this;
	}

	private void afterLoading(String resource, Matcher sqlatoms, boolean isreload)
			throws SQLException {
		
		for (String atom = RegExpUtil.getNextMatched(sqlatoms); atom != null; atom = RegExpUtil.getNextMatched(sqlatoms)) {
			if (Pattern.matches("--[^\r\n]*---", atom)) {
				Sql sql = new Sql();
				Matcher mtprop = ptprops.matcher(atom);

				for (String prop = RegExpUtil.getNextMatched(mtprop); prop != null; prop = RegExpUtil.getNextMatched(mtprop)) {
					String[] keyval = prop.split("=", -1);
					if ("id".equals(keyval[0])) {
						sql.setId(keyval[1]);
					}
					sql.addOtherProp(keyval[0], keyval[1]);
				}
				if (isreload) {// 假如为重新加载，则不进行重复判断
				//	System.out.println(isreload);
				} else {
					Sql existSql = sqlLoader.sqls.get(sql.getId());
					
					if (existSql != null) {
						throw new SQLException("遇到相同id的Sql,其id值为:" + sql.getId() + "当加载以下文件时:" + resource);
					}
				}
				atom = RegExpUtil.getNextMatched(sqlatoms);
				sql.setSql(atom);
				if (isreload) {
					sqls.put(sql.getId(), sql);
				} else {
					sqlLoader.sqls.put(sql.getId(), sql);
				}
			}
		}
		if (!isreload) {
			sqlLoader.loadedFileName.add(resource);
		}
	}
	
	public SqlLoader load(String resource, boolean isreload) throws IOException, SQLException {
		log.info("reading sql file:" + resource);
		if (! isreload && sqlLoader.loadedFileName.contains(resource))
			return this;

		Matcher sqlatoms = ptSqlatoms.matcher(gainSqlText(resource).result);
		afterLoading(resource, sqlatoms, isreload);
		return this;
	}

	public void reloadAll() throws IOException, SQLException {
		sqls.clear();
		Iterator<String> iter = loadedFileName.iterator();
		while (iter.hasNext()) {
			load(iter.next(), true);
		}
	}

	private NoCacheReturn gainSqlText(String resource) throws IOException {
		return ClasspathResourceUtil.getTextByPathNoCachedReturnResult(resource);
	}
	
	private String gainSqlText(File file) throws IOException {
		InputStream in = new FileInputStream(file);
		if (in == null) {
			throw new FileNotFoundException(file.getAbsolutePath() + "（系统找不到指定的文件。）");
		}
		byte[] chs = new byte[in.available()];
		in.read(chs);
		String text = new String(chs, "utf-8");
		return text;
	}

	public int size() {
		return sqls.size();
	}
	
	/**
	 * 对所有已加载的所有sql进行动态组装
	 * 全局寻找id 对标有@{id}符号的进行动态替换
	 */
	public void assembleSqls() throws SQLException  {
		Set<Entry<String, Sql>> sqlentries = sqls.entrySet();
		for (Entry<String, Sql> sqlentry : sqlentries) {
			setAutoAssembleSql(sqlentry.getValue());
		}
	}
	
	private void setAutoAssembleSql(Sql parentsql) throws SQLException {
		String parentSqlStr = parentsql.getSql();
		Matcher subsqlkeyMatcher = ptAutoAssemble.matcher(parentSqlStr);
		for (String subsqlkey = RegExpUtil.getNextMatched(subsqlkeyMatcher); subsqlkey != null; subsqlkey = RegExpUtil.getNextMatched(subsqlkeyMatcher)) {
			Sql sql = sqls.get(subsqlkey);
			if (sql == null) {
				throw new SQLException("组装sql失败，未找到id为"+subsqlkey+"的sql");
			}
			setAutoAssembleSql(sql);
			parentSqlStr = parentSqlStr.replaceAll("\\@\\{[\\s]*"+subsqlkey+"[\\s]*}", sql.getSql());
		}
		parentsql.setSql(parentSqlStr);
	}
	
	public Sql getSql(String id) {
		
		// 开发者模式，时时加载sql
		if (GlobalConsts.isDevelopMode)	 {
			synchronized(locker) {
				boolean haschanged = false;
				for (String resource : loadedFileName) {
					try {
						NoCacheReturn gainSqlText = gainSqlText(resource);
						if (gainSqlText.ismodified) {
							Matcher sqlatoms = ptSqlatoms.matcher(gainSqlText.result);
							afterLoading(resource, sqlatoms, true);
							haschanged = true;
						}
					} catch (IOException e) {
						e.printStackTrace();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				if (haschanged) {
					try {
						assembleSqls();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		try {
			if (!sqls.containsKey(id))
				return null;
			return sqls.get(id).clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}
}
