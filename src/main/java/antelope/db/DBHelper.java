package antelope.db;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.tomcat.dbcp.dbcp.BasicDataSource;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.springframework.util.Assert;

import antelope.springmvc.SqlWhere;
import antelope.utils.ClasspathResourceUtil;

public class DBHelper {
	private static String configText;
	private static String type; // SQLServer Oracle MySQL
	private static DBHelper dbhelper;
	
	private DBHelper() {
	}
	
	public static String getDbType() throws IOException, DocumentException {
		if (dbhelper == null || type == null) {
			dbhelper = new DBHelper();
			configText = dbhelper.gainConfigText("/persistence.xml");
			Document doc = DocumentHelper.parseText(configText);
			List props = doc.getRootElement().element("persistence-unit").element("properties").elements("property");
			String dialectName = "";
			for (int i = 0; i < props.size(); i++) {
				String propname = ((Element) props.get(i)).attributeValue("name");
				if ("hibernate.dialect".equals(propname)) {
					dialectName = ((Element) props.get(i)).attributeValue("value");
					break;
				}
			}
			type = dialectName;
		}
		
		return type;
	}
	
	public static boolean isOracle() throws IOException, DocumentException {
		getDbType();
		return type.indexOf("Oracle")!= -1;
	}
	
	public static boolean isSQLServer() throws IOException, DocumentException {
		getDbType();
		return type.indexOf("SQLServer") != -1;
	}
	
	public static boolean isMySQL() throws IOException, DocumentException {
		getDbType();
		return type.indexOf("MySQL") != -1;
	}
	
	public static boolean isPostgreSQL() throws IOException, DocumentException {
		getDbType();
		return type.indexOf("PostgreSQL") != -1;
	}
	
	/**
	 * 此方法已不推荐使用，此方法不再支持MySQL, 若有查询条件需求，请直接使用Java连接百分号 拼写字符串 如下<br/>
	 * SqlWhere sqlwhere = tidySqlWhere(new String[]{"name like ?"}, new Object[]{"%" + decodeAndTrim(name) + "%"});
	 * @return
	 * @throws Exception
	 */
	@Deprecated
	public static String getLikePart() throws Exception {
		getDbType();
		if (type.indexOf("SQLServer") != -1)
			return " like '%'+?+'%' ";
		if (type.indexOf("Oracle") != -1) {
			return " like '%'||?||'%' ";
		}
		throw new Exception("对应数据库尚未Like部分实现！");
	}
	
	/**
	 * 根据数据库类型返回日期字符串转换值，格式为 yyyy-MM格式
	 * @param colname
	 * @return 
	 * @throws Exception
	 */
	public static String getYearMonthPart(String colname) throws Exception {
		if (isOracle()) {
			return " to_char("+colname+", 'yyyy-MM') ";
		} 
		
		if (isSQLServer()) {
			return " convert(varchar(7), "+colname+", 20) ";
		}
		
		if (isMySQL()) {
			return " date_format(" + colname + ",'%Y-%m') ";
		}
		
		if (isPostgreSQL()) {
			return " to_char("+colname+", 'YYYY-MM') ";
		} 
		
		throw new Exception("对应数据库尚未YearMonth部分实现！");
	}
	/**@add by xcc
	 * 根据数据库类型返回日期字符串转换值，格式为 yyyy-MM-dd格式
	 * @param colname
	 * @return 
	 * @throws Exception
	 */
	public static String getYearMonthDayPart(String colname) throws Exception {
		if (isOracle()) {
			return " to_char("+colname+", 'yyyy-MM-dd') ";
		} 
		
		if (isSQLServer()) {
			return " convert(varchar(10), "+colname+", 20) ";
		}
		
		if (isMySQL()) {
			return " date_format(" + colname + ",'%Y-%m-%d') ";
		}
		
		if (isPostgreSQL()) {
			return " to_char("+colname+", 'YYYY-MM-DD') ";
		} 
		
		throw new Exception("对应数据库尚未YearMonthDay部分实现！");
	}
	
	/**
	 * 此方法已不推荐使用，此方法不再支持MySQL, 若有查询条件需求，请直接使用Java连接百分号 拼写字符串 如下<br/>
	 * SqlWhere sqlwhere = tidySqlWhere(new String[]{"name like ?"}, new Object[]{"%" + decodeAndTrim(name)});
	 * @return
	 * @throws Exception
	 */
	@Deprecated
	public static String getLeftLikePart() throws Exception {
		getDbType();
		if (type.indexOf("SQLServer") != -1)
			return " like '%'+? ";
		if (type.indexOf("Oracle") != -1) {
			return " like '%'||? ";
		}
		throw new Exception("对应数据库尚未Like部分实现！");
	}
	
	
	/**
	 * 此方法已不推荐使用，此方法不再支持MySQL, 若有查询条件需求，请直接使用Java连接百分号 拼写字符串 如下<br/>
	 * SqlWhere sqlwhere = tidySqlWhere(new String[]{"name like ?"}, new Object[]{ decodeAndTrim(name) +"%"});
	 * @return
	 * @throws Exception
	 */
	@Deprecated
	public static String getRightLikePart() throws Exception {
		getDbType();
		if (type.indexOf("SQLServer") != -1)
			return " like ?+'%' ";
		if (type.indexOf("Oracle") != -1) {
			return " like ?||'%' ";
		}
		throw new Exception("对应数据库尚未Like部分实现！");
	}
	
	public static String getEightLenUidStrSign() throws Exception {
		if (isOracle())
			return " substr(sys_guid(), 0, 8) ";
		
		if (isSQLServer())
			return " substring(cast (NEWID() as varchar(36)), 0, 9) ";
		
		if (isMySQL())
			return " left(uuid(), 8) ";
			
		
		throw new Exception("对应数据库尚未Like部分实现！");
	}
	
	public static DataSource getDataSource() throws IOException, DocumentException, NamingException {
		Document persist = ClasspathResourceUtil.getXMLDocumentByPath("/persistence.xml");
		String txt = persist.getRootElement().element("persistence-unit").element("non-jta-data-source").getText();
		InitialContext ctx = new InitialContext();
		DataSource myds = null;
		try {
			myds = (DataSource) ctx.lookup(txt);
		} catch (Exception e) {
			// e.printStackTrace();
		}
		// 若未获取到数据源则尝试创建测试用sqlite数据源
		if (myds == null) {
			org.apache.tomcat.dbcp.dbcp.BasicDataSource datasource = new BasicDataSource();
			datasource.setUrl("jdbc:sqlite://d:/databasetotest.db");
			datasource.setDriverClassName("org.sqlite.JDBC");
			return datasource;
		}
		
		return myds; 
	}
	
	public static DataSource getDataSource(String jndiname) throws IOException, DocumentException, NamingException {
		InitialContext ctx = new InitialContext();
		DataSource myds = null;
		try {
			myds = (DataSource) ctx.lookup(jndiname);
		} catch (Exception e) {
			// e.printStackTrace();
		}
		return myds; 
	}
	
	/**
	 * 获取数据库类型简短字符串
	 * @return
	 */
	public static String getDbms() throws Exception {
		if (isOracle())
			return "ora";
		
		if (isSQLServer())
			return "mssql";
		
		if (isMySQL())
			return "mysql";
		
		if (isPostgreSQL()) {
			return "postgres";
		}
		
		throw new Exception("对应数据库尚未支持自动升级实现！");
	}
	
	/**
	 * 获取对应数据库字符串连接符
	 * 此函数不支持MySQL
	 * @return 
	 * @throws Exception
	 */
	@Deprecated
	public static String getStrCatSign() throws Exception {
		getDbType();
		if (type.indexOf("SQLServer") != -1)
			return " + ";
		if (type.indexOf("Oracle") != -1) {
			return " || ";
		}
		throw new Exception("对应数据库尚未StrCat部分实现！");
	}
	
	private String gainConfigText(String resource) throws IOException {
		InputStream in = this.getClass().getResourceAsStream(resource);
		if (in == null) {
			throw new FileNotFoundException(resource + "（系统找不到指定的文件。）");
		}
		byte[] chs = new byte[in.available()];
		in.read(chs);
		String text = new String(chs, "utf-8");
		return text;
	}
}
