package antelope.system;

import java.io.File;
import java.io.IOException;

import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;
import org.dom4j.DocumentException;

import antelope.db.DBHelper;
import antelope.db.DatabaseService;
import antelope.utils.ClasspathResourceUtil;


/**
 * 系统启动时执行
 * @author lining
 */

@WebServlet(urlPatterns={"/initsystemcache"}, loadOnStartup = 0)
public class SystemInitDBServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	/**
	 * 日志对象，子类使用此对象记录系统常见日志
	 */
	protected Logger log = Logger.getLogger(this.getClass());
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		System.out.println("开始升级数据库");
		DatabaseService dbservice3 = new DatabaseService();
		updateDatabaseInner(dbservice3);
		
		
		//**** 在升级数据库时顺带初始化一下系统所需文件夹
		// 创建exportimgs用以存放后台导出的临时图片
		String parentpath = ClasspathResourceUtil.getWebappFolderFile("/").getParent();
		File exportimgfile = new File(parentpath + "/exportimgs/");
		exportimgfile.mkdirs();
	}

	private void updateDatabaseInner(DatabaseService dbservice) {
		boolean issuccess = true;
		try {
			dbservice.updateDatabase(DBHelper.getDataSource());
		} catch (DocumentException e) {
			e.printStackTrace();
			issuccess = false;
		} catch (IOException e) {
			e.printStackTrace();
			issuccess = false;
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (issuccess)
			System.out.println("数据库升级成功");
		else
			System.out.println("数据库升级失败，详细信息请查看错误堆栈");
	}
	
}



