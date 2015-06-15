package antelope.system;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Set;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;
import org.hibernate.ejb.packaging.Entry;
import org.hibernate.ejb.packaging.FileFilter;
import org.hibernate.ejb.packaging.Filter;
import org.hibernate.ejb.packaging.JarVisitor;
import org.hibernate.ejb.packaging.JarVisitorFactory;
import org.springframework.transaction.TransactionStatus;

import antelope.db.DBUtil;
import antelope.db.SqlLoader;
import antelope.entities.SysUser;
import antelope.james.JamesMgtJMX;
import antelope.springmvc.JPABaseDao;
import antelope.springmvc.SpringUtils;
import antelope.utils.SystemOpts;


/**
 * 系统启动时执行
 * @author lining
 */

@WebServlet(urlPatterns={"/initsystemcache"}, loadOnStartup = 10)
public class SystemStartServlet extends HttpServlet {
	
	/**
	 * 日志对象，子类使用此对象记录系统常见日志
	 */
	protected Logger log = Logger.getLogger(this.getClass());
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		System.out.println("系统缓冲开始");
		try {
			SystemCache.servletContext = config.getServletContext();
			TransactionStatus status = SpringUtils.beginTransaction();
			SystemCache.refreshUnitAndChildUnitsUsers();
			SpringUtils.commitTransaction(status);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String filePath = this.getClass().getResource("/").getFile();
		
		File file = new File(filePath.substring(1) + "/");
		
		// linux下，获取目录不能去掉最前面的斜线
		if (!file.isDirectory()) {
			file = new File(filePath);
		}
		
		loadSqlFiles(file);
		
		loadSqlFilesFromAntelopeJar();
		
		try {
			SqlLoader.getInstance().assembleSqls();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("系统缓冲结束");
		
		
		
		try {
			if ("1".equals(SystemOpts.getProperty("antelope_jamesopen"))) {
				TransactionStatus status = SpringUtils.beginTransaction();
				try {
					System.out.println("James邮件初始化开始，请确认James邮件服务器已经启动，并将数据连接到当前数据库！");
					JamesMgtJMX jmx = new JamesMgtJMX();
					int ct = DBUtil.queryCount("select count(*) from JAMES_DOMAIN where domain_name=?", SystemOpts.getProperty("antelope_jamesdomain"));
					if (ct == 0)
						jmx.addDomain(SystemOpts.getProperty("antelope_jamesdomain"));
					// 为系统管理员添加默认账户
					ct = DBUtil.queryCount("select count(*) from users where username=?", "admin");
					if (ct == 0) {
						JPABaseDao dao = SpringUtils.getBean(JPABaseDao.class);
						SysUser user = dao.getBy("1234", SysUser.class);
						jmx.addUser(user.username, user.password);
					}
					SpringUtils.commitTransaction(status);	
					System.out.println("James邮件初始化结束");
				} catch (IOException e) {
					SpringUtils.rollbackTransaction(status);
					e.printStackTrace();
				} catch (Exception e) {
					SpringUtils.rollbackTransaction(status);
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void loadSqlFilesFromAntelopeJar() {
		JarVisitor stateJarVisitor = null;
		try {
			if ( stateJarVisitor == null ) {
				Filter[] filters = new Filter[1];
				filters[0] = new FileFilter( true ) {
					public boolean accept(String javaElementName) {
						return javaElementName.endsWith( ".sql" );
					}
				};
				stateJarVisitor =  JarVisitorFactory.getVisitor(SystemCache.servletContext.getResource("/WEB-INF/lib/antelope.jar"), filters );
			}
			Set<Entry> set = stateJarVisitor.getMatchingEntries()[0];
			for (Entry entry : set) {
				InputStream in = entry.getInputStream();
				byte[] bts = new byte[in.available()];
				in.read(bts);
				in.close();
				String sqlfilestr = new String(bts, "utf-8");
				SqlLoader.getInstance().loadBySqlText(sqlfilestr, entry.getName());
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void loadSqlFiles(File parentfile) {
		if (!parentfile.isDirectory() && parentfile.getName().endsWith(".sql") 
				&& parentfile.getAbsolutePath().indexOf("\\classes\\dbdeploy\\") == -1) {
			try {
				SqlLoader.getInstance().load(parentfile);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			parentfile.isDirectory();
			File[] files = parentfile.listFiles();
			if (files != null) {
				for (File file : files) {
					loadSqlFiles(file);
				}
			}
		}
	}
	
}



