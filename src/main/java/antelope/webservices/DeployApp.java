package antelope.webservices;

import java.io.File;

import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.User;
import org.apache.ftpserver.ftplet.UserManager;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.usermanager.PropertiesUserManagerFactory;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;

public class DeployApp {
	
	private static FtpServer server;
	
	/**
	 * 远程部署应用程序
	 * @param antfilename 对应应用程序的ant文件名称
	 */
	public String deployApp(String antfilename) {
		
		if (server != null && !server.isStopped())
			server.stop();
		
		String filePath= this.getClass().getResource("/deploy/build.xml").getFile();
		File buildFile = new File(filePath);
		Project p = new Project();
		p.init();
		ProjectHelper helper = ProjectHelper.getProjectHelper();
		helper.parse(p, buildFile);
		p.executeTarget(p.getDefaultTarget());
		
		return "成功 ！";
	}
	
	public String startFtpServer(String name) {
		
		
		FtpServerFactory serverFactory = new FtpServerFactory();
        
		ListenerFactory factory = new ListenerFactory();
		        
		// set the port of the listener
		factory.setPort(21);

		// replace the default listener
		serverFactory.addListener("default", factory.createListener());
		        
		PropertiesUserManagerFactory userManagerFactory = new PropertiesUserManagerFactory();
		
		UserManager username = userManagerFactory.createUserManager();
		
		String filePath= this.getClass().getResource("/ftp.properties").getFile();
		File buildFile = new File(filePath);
		userManagerFactory.setFile(buildFile);
		serverFactory.setUserManager(userManagerFactory.createUserManager());
		        
		// start the server
		server = serverFactory.createServer(); 
		try {
			server.start();
		} catch (FtpException e) {
			e.printStackTrace();
		}
		
//		// 关闭服务器服务
//		String filePath2= this.getClass().getResource("/deploy/build.xml").getFile();
//		File buildFile2 = new File(filePath2);
//		Project p = new Project();
//		p.init();
//		ProjectHelper helper = ProjectHelper.getProjectHelper();
//		helper.parse(p, buildFile2);
//		p.executeTarget("closeserver");
		return "dsffds";
	}
}
