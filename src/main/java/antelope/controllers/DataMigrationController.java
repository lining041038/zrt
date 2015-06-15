package antelope.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import antelope.db.DBUtil;
import antelope.db.Sql;
import antelope.interfaces.DataMigration;
import antelope.springmvc.BaseController;
import antelope.springmvc.SpringUtils;
import antelope.utils.JSONObject;
import antelope.utils.RegExpUtil;
import antelope.utils.ResultSetHandler;
import antelope.utils.ReturnObject;
import antelope.utils.TextUtils;

/**
 * 数据迁移控制器
 * @author lining
 * @since 2012-1-27
 */
@Controller
public class DataMigrationController extends BaseController {

	/**
	 * 测试连接
	 */
	@RequestMapping("/common/DataMigrationController/testConnection")
	public void testConnection(String connectionurl, String username, String password, HttpServletResponse res) throws IOException {
		
		Connection oracleconn;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			oracleconn = DriverManager.getConnection(connectionurl, username, password);
			System.out.println(oracleconn);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
			print(e.getMessage(), res);
		}
		
	}
	
	/**
	 * 获取迁移相关信息
	 */
	@RequestMapping("/common/DataMigrationController/getMigrationInfos")
	public void getMigrationInfos(HttpServletResponse res) throws IOException {
		List<DataMigration> beans = SpringUtils.getBeans(DataMigration.class);
		print(toJSONArrayBy(beans), res);
	}
	
	/**
	 * 开始进行数据迁移
	 * @param title
	 * @throws IOException 
	 */
	@RequestMapping("/common/DataMigrationController/startMigratData")
	public void startMigratData(String connectionurl, String username, String password, String title, HttpServletResponse res) throws IOException {
		title = decodeAndTrim(title);
		
		Connection oracleconn = null;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			oracleconn = DriverManager.getConnection(connectionurl, username, password);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
			print(e.getMessage(), res);
		}
		System.out.println("开始迁移数据!");
		String finalscriptFuncName = "";
		try {
			List<DataMigration> beans = SpringUtils.getBeans(DataMigration.class);
			for (DataMigration dataMigration : beans) {
				if (dataMigration.getTitle().equals(title)) {
					String[] tbnames = dataMigration.getDBTableNames();
					if (tbnames!= null) {
						for (int i = 0; i < tbnames.length; i++) {
							dd(oracleconn, tbnames[i], dataMigration);
						}
					}
					finalscriptFuncName = dataMigration.migratData(oracleconn);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			print(e.getMessage(), res);
		} catch (Exception e) {
			e.printStackTrace();
			print(e.getMessage(), res);
		}
		ReturnObject<String> ro = new ReturnObject<String>();
		if (stringSet(finalscriptFuncName)) {
			ro.setMsg(finalscriptFuncName);
		} else {
			System.out.println("迁移数据结束!");
		}
		print(new JSONObject(ro, true), res);
	}
	
	/**
	 * 数据库表对应信息
	 * @author lining
	 * @since 2012-3-28
	 */
	public class TableInfos {
		public String othertablename;
		public String ourtablename;
		public boolean isappend = false;
		public String wherecond = "";
		public List<String> colinfos;
	}
	
	/**
	 * 根据表信息剥离外部连接对应的表名和字段名
	 * @param tbname
	 * @return
	 */
	private TableInfos extractTbnames(String tbname) {
		TableInfos infos = new TableInfos();
		
		
		if (tbname.indexOf(":") == -1) {
			infos.ourtablename = tbname;
			infos.othertablename = tbname;
			return infos;
		}
		
		String likejson = RegExpUtil.getFirstMatched(Pattern.compile("(?<=\\()[^\\)]*(?=\\))"), tbname);
		
		infos.othertablename = RegExpUtil.getFirstMatched(Pattern.compile("(?<=:)[^:]*(?=\\()"), tbname);
		infos.ourtablename = RegExpUtil.getFirstMatched(Pattern.compile("^[^:]*(?=:)"), tbname);
		infos.isappend = tbname.endsWith(":append");
		infos.wherecond = RegExpUtil.getFirstMatched(Pattern.compile("(?<=:)where [^:]*(?=:)"), tbname);
		
		infos.colinfos = new ArrayList<String>();
		String[] colmaps = likejson.split(",");
		for (String string : colmaps) {
			infos.colinfos.add(string);
		}
		
		return infos;
	}

	private void dd(final Connection oracleconn,  final String tbname, final DataMigration dataMigration)
			throws SQLException, Exception {
		
		final TableInfos tableinfo = extractTbnames(tbname);
		
		Session session = DBUtil.getSession();
		session.doWork(new Work() {
			@Override
			public void execute(final Connection connection) throws SQLException {
				connection.setAutoCommit(true);
				
				if (!tableinfo.isappend) { // 若数据迁移模式不是追加模式，则需要把老数据进行删除
					Sql sql = new Sql("delete from "+tableinfo.ourtablename);
					sql.update(connection, new Object[]{});
				}
				try {
					dataMigration.beforeMigrateTable(tableinfo.ourtablename, connection);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				String sql2str = "select * from " + tableinfo.othertablename;
				
				if (tableinfo.colinfos != null) {
					sql2str = "select " + TextUtils.join(",", tableinfo.colinfos) + " from " + tableinfo.othertablename;
				}
				
				if (TextUtils.stringSet(tableinfo.wherecond)) {
					sql2str += " " +  tableinfo.wherecond;
				}
				
				Sql sql2 = new Sql(sql2str);
				
				try {
					sql2.query(oracleconn, new ResultSetHandler() {
						public Object handle(ResultSet rs) throws Exception {
							ResultSetMetaData colname = rs.getMetaData();
							List<String> colquess = new ArrayList<String>();
							List<String> colnames = new ArrayList<String>();
							
							for (int i = 1; i <= colname.getColumnCount(); i++) {
								colquess.add("?");
								colnames.add(colname.getColumnLabel(i));
							}
							int s = 0;
							while (rs.next()) {
								s++;
								List<Object> valus = new ArrayList<Object>();
								for (int i = 1; i <= colnames.size(); i++) {
									if (rs.getObject(i) instanceof Date || rs.getObject(i) instanceof oracle.sql.TIMESTAMP) {
										valus.add(rs.getTimestamp(i));
									} else {
										valus.add(rs.getObject(i));
									}
								}
								String ourcolnames = "("+TextUtils.join(",", colnames)+")";
								if (tableinfo.colinfos != null) {
									ourcolnames = "";
								}
								
								Sql incresql = new Sql("if exists (SELECT 1 FROM sys.columns  "+  
												"	  WHERE object_id=OBJECT_ID('" + tableinfo.ourtablename + "') AND is_identity=1 "+
												"	)   "+
												" begin set IDENTITY_INSERT " + tableinfo.ourtablename + " on insert into " + tableinfo.ourtablename + ourcolnames+" values("+TextUtils.join(",", colquess)+")"+
												" end else insert into " + tableinfo.ourtablename + ourcolnames +" values("+TextUtils.join(",", colquess)+")");
								valus.addAll(valus);
								incresql.update(connection, valus.toArray(new Object[0]));
								
							}
							System.out.println("导数完毕，从表" + tableinfo.othertablename +"到表" + tableinfo.ourtablename + "共导入了" + s + "条");
							return null;
						}
					}, new Object[]{});
				} catch (SQLException e) {
					e.printStackTrace();
					throw e;
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}






