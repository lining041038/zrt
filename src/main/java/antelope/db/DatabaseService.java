package antelope.db;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.stereotype.Service;

import antelope.dbdeploy.DbDeploy;
import antelope.springmvc.BaseController;
import antelope.utils.ClasspathResourceUtil;

/**
 * 数据库服务对象
 * @author pc
 * @since 2012-3-18
 */
@Service
public class DatabaseService extends BaseController {
	
	/**
	 * 对当前系统所连接到的数据库更新
	 * @param ds
	 * @throws Exception
	 */
	public void updateDatabase(DataSource ds) throws Exception {
		
		
		Connection conn = null;
		
		try {
			conn = ds.getConnection();
			conn.setAutoCommit(true);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		System.out.println("	antelope基础表开始升级");
		updateDatabaseInner("/bapdbdeploy/" + DBHelper.getDbms(), "bapchangelog");
		System.out.println("	antelope基础表升级成功");
		
		File file = new File(this.getClass().getResource("/dbplugins").getFile().replaceAll("^\\/", ""));
		if (file.exists()) {
			File[] pluginsfolders = file.listFiles();
			if (pluginsfolders != null && pluginsfolders.length > 0) {
				System.out.println("	插件表开始升级");
				try {
					for (File folder : pluginsfolders) {
						DBUtil.executeUpdate(conn, "if not exists (select * from sysobjects where id = object_id('"+folder.getName()+"changelog'))  "+
								"	begin CREATE TABLE "+folder.getName()+"changelog (  "+
								"			  change_number BIGINT NOT NULL PRIMARY KEY,  "+
								"			  complete_dt DATETIME NOT NULL,  "+
								"			  applied_by VARCHAR(100) NOT NULL,  "+
								"			  description VARCHAR(500) NOT NULL  "+
								"	 )  "+
								"	end");
						updateDatabaseInner("/dbplugins/" + folder.getName() + "/ " + DBHelper.getDbms() + " /build.xml", folder.getName() + "changelog");
					}
					System.out.println("	插件表升级成功");
				} catch (SQLException e) {
					e.printStackTrace();
					System.out.println("	插件表升级失败");
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("	插件表升级失败");
				} finally {
					try {
						if (conn != null && !conn.isClosed())
							conn.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		System.out.println("	系统表开始升级");
		updateDatabaseInner("/dbdeploy/" + DBHelper.getDbms(), "changelog");
		System.out.println("	系统表升级成功");
	}

	/**
	 * 根据build文件所在路径进行数据库升级
	 * @param buildpath
	 * @param changeLogTableName
	 * @throws DocumentException
	 * @throws IOException
	 */
	public void updateDatabaseInner(String buildpath, String changeLogTableName) throws DocumentException, IOException {
		String filePath= this.getClass().getResource(buildpath + "/build.xml").getFile();
		String tableexistsql = ClasspathResourceUtil.getTextByPathNoCached(buildpath + "/existSchemaVersionTable.sql");
		String tablecreatesql = ClasspathResourceUtil.getTextByPathNoCached(buildpath + "/createSchemaVersionTable.sql");
		Connection conn = null;
		try {
			conn = DBHelper.getDataSource().getConnection();
		} catch (SQLException e1) {
			e1.printStackTrace();
		} catch (NamingException e1) {
			e1.printStackTrace();
		}
		try {
			List<Map<String, Object>> obj = null;
			if ("MySQL".equals(conn.getMetaData().getDatabaseProductName()) || "PostgreSQL".equals(conn.getMetaData().getDatabaseProductName())) {
				obj = DBUtil.query(conn, tableexistsql, new Object[]{conn.getCatalog()});
			} else {
				obj = DBUtil.query(conn, tableexistsql, new Object[0]); 
			}
			Double ct = Double.valueOf(obj.get(0).get("ct").toString());
			if (ct == 0)
				DBUtil.executeUpdate(conn, tablecreatesql);
			if (!conn.getAutoCommit())
				conn.commit();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		DbDeploy dbdeploy = new DbDeploy();
		dbdeploy.setChangeLogTableName(changeLogTableName);
		dbdeploy.setScriptdirectory(new File(filePath).getParentFile());
		try {
			dbdeploy.go();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
