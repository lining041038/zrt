package antelope.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import antelope.springmvc.BaseController;
/**
 * 连接邮件服务器的工具类
 * @author lining  
 */
@Component
public class MailUtil {
	
	private static Log logger = LogFactory.getLog(MailUtil.class);

	private static MailUtil senderMail = new MailUtil();

	private Properties properties = new Properties();
	private Session session = null;

	private boolean needAuthentication;
	private String username = null;
	private String password = null;
	
	final static private String MAIL_HOST = "mail.host";
	final static private String MAIL_FROM = "mail.from";
	final static private String MAIL_USERNAME = "mail.username";
	final static private String MAIL_PASSWORD = "mail.password";
	final static private String MAIL_NEEDAUTH = "mail.needauth";
	
	public static final List<MailSendingItem> mailsendinglist = new ArrayList<MailSendingItem>();
	
	public MailUtil(){
		try {
			properties.setProperty(MAIL_HOST, SystemOpts.getProperty(MAIL_HOST));
			properties.setProperty(MAIL_FROM, SystemOpts.getProperty(MAIL_FROM));
			properties.setProperty("mail.smtp.auth", SystemOpts.getProperty(MAIL_NEEDAUTH));
			
			needAuthentication = "true".equals(SystemOpts.getProperty(MAIL_NEEDAUTH));
			if (needAuthentication) {
				username = SystemOpts.getProperty(MAIL_USERNAME);
				password = SystemOpts.getProperty(MAIL_PASSWORD);
			}
			
		} catch (IOException e) {
		}
	}
	
	public static void reload(){
		try {
			senderMail.properties.setProperty(MAIL_HOST, SystemOpts.getProperty(MAIL_HOST));
			senderMail.properties.setProperty(MAIL_FROM, SystemOpts.getProperty(MAIL_FROM));
			senderMail.properties.setProperty("mail.smtp.auth", SystemOpts.getProperty(MAIL_NEEDAUTH));
		} catch (IOException e) {
		}
    }
	
	public Session getMailSession() {
		if (logger.isDebugEnabled()) {
			if (needAuthentication) {
				logger.debug("now start to get the mail session with authentication");
				logger.debug("the login user name " + username + "  , and password = " + password);
			} else {
				logger.debug("now start to get the mail session without authentication");
			}
		}

		// 增加异常处理，如果连接不到邮箱服务器，session为null
		try {
			if (needAuthentication) {
				MailAuthenticator authenticator = new MailAuthenticator(username, password);
				
				session = Session.getDefaultInstance(properties, authenticator);
			} else {
				session = Session.getDefaultInstance(properties, null);
			}
		} catch (Exception e) {
			logger.error(" Can not connect to mail server . Please check the mail server, userName and password !");
		}
		
		if (logger.isDebugEnabled()) {
			logger.debug(" now return the session : " + session);
		}
		return session;
	}
	
	/**
	 * @param to  收件人  数组
	 * @param customFrom  发件人
	 * @param subject  标题
	 * @param message  内容
	 * @param fileNames  附件名称 数组
	 * @param filePaths  附件路径  数组
	 * <br/>
	 * <br/>
	 * 	注： 附件名称要和上传的附件名称一致，并且附件名称要加上后缀名称，否则，邮箱中附件显示不正常
	 * <br/>
	 * <br/>
	 * 	如： 名称：附件.doc 
	 * <br/>
	 * <br/>
	 * 	   路径：D:/attch/附件.doc
	 * @return
	 */
	public boolean sendMail(String[] to, String customFrom, String subject, String message, String[] fileNames, String[] filePaths) {
		
		if (logger.isDebugEnabled()) {
			logger.debug("sending email [" + subject + "] to" + to);
		}
		
		if (session == null) {
			//logger.error("you should connect to the mail server firstly!");
			session = getMailSession();
		} 
		
		try {
			MimeMessage msg = new MimeMessage(session);
			if (customFrom != null) {
				msg.setFrom(new InternetAddress(customFrom));
			} else {
				msg.setFrom(new InternetAddress(properties.getProperty(MAIL_FROM)));
			}

			InternetAddress[] addrs = new InternetAddress[to.length];
			for (int i = 0; i < to.length; i++) {
				if (to[i] != null)
					addrs[i] = new InternetAddress(to[i]);
				else
					addrs[i] = new InternetAddress("admin@localhost");
			}

			if (subject == null || subject.equals(""))
				subject = "无标题";
			if (message == null || message.equals(""))
				message = "无内容";
			msg.setRecipients(Message.RecipientType.TO, addrs);
			msg.setSubject(subject);
			msg.setSentDate(new Date());
			
			/**
			 * 增加邮件附件的功能
			 */
			//   Create   the   message   part 
		    BodyPart   messageBodyPart   =   new   MimeBodyPart(); 

		    messageBodyPart.setContent(message, "text/html;charset=gb2312");
			
			Multipart   multipart   =   new   MimeMultipart(); 
		    multipart.addBodyPart(messageBodyPart); 
		    
		    if(fileNames != null && filePaths != null) {
		    	for(int i = 0; i < fileNames.length; i++) {
		    		if(fileNames[i] != null && !"".equals(fileNames[i])) {
		    			messageBodyPart   =   new   MimeBodyPart(); 
		    			FileDataSource   source   =   new   FileDataSource(filePaths[i]); 
		    			messageBodyPart.setDataHandler(new   DataHandler(source)); 
		    			messageBodyPart.setFileName(MimeUtility.encodeText(fileNames[i])); 
		    			multipart.addBodyPart(messageBodyPart);
		    		}
		    	}
		    }

		    msg.setContent(multipart);
		    
			Transport.send(msg);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("unable to send mail", e);
			return false;
		}
	}
	
	public boolean sendMail(String[] to, String customFrom, String subject, String message) {
		return sendMail(to, customFrom, subject, message, null, null);
	}
	
	public static MailUtil getSenderMail() {
		return senderMail;
	}
	/**
	 * @param mailArry  邮箱
	 * @param subject   标题
	 * @param content   内容
	 * @param url       链接
	 */
	public static void sendMailMsg(String[] mailArry, String subject, String content){
		MailUtil mailUtil =MailUtil.getSenderMail();
		mailUtil.getMailSession();

	    String subString = "";
		try {
			subString = SystemOpts.getProperty("antelope_mailtipprefix") + "：";
		} catch (IOException e) {
			e.printStackTrace();
		}
	    subString += subject;
	    
	    String urlString = "";
		try {
			urlString = "<br>" + SystemOpts.getProperty("antelope_mailtipprefix") + "：<br>";
		} catch (IOException e) {
			e.printStackTrace();
		}
	    urlString += "消息：<br>"+content;
	    urlString += "<br>提交时间:" + new Date() + "<br>";
		 
		if(null != mailArry[0]){
			boolean isSendOk = mailUtil.sendMail(mailArry, null, subString, urlString);
			
			if(isSendOk){
				System.out.println( new Date()+"==================>邮件发送成功！！！");
			}else{
				logger.error("cann't send mail to " + mailArry);
				System.out.println( new Date()+"==================>邮件发送失败！！！");
			    }
		}else{
			logger.error("cann't send mail to " + mailArry);
			System.out.println( new Date()+"==================>邮箱为null发送失败！！！");
		}
	}
	/**
	 * @param mailArry  邮箱
	 * @param subject   标题
	 * @param content   内容
	 * @param url       链接
	 */
	public static void sendMailUtil(String[] mailArry, String subject, String content,String url){
		MailUtil mailUtil =MailUtil.getSenderMail();
		mailUtil.getMailSession();

		//xxx你好，内控合规与风险管理平台提醒您： 请处理XXX提交的来自于XXX模块的待办事项，提交时间：XXX，（链接），请您单击此处进行处理。
	    String subString = "";
		try {
			subString = SystemOpts.getProperty("antelope_mailtipprefix") + "：";
		} catch (IOException e) {
			e.printStackTrace();
		}
	    subString += subject;
	    
	    String urlString = "";
		try {
			urlString = "<br>" + SystemOpts.getProperty("antelope_mailtipprefix") + "：<br>";
		} catch (IOException e) {
			e.printStackTrace();
		}
	    urlString += "有来自于"+content;
	    urlString += "<br>提交时间:" + new Date() + "<br>";
		urlString += "<a href=\""+url+"\">请您单击这里进行处理<<</a>";
		if(null != mailArry[0]){
			boolean isSendOk = mailUtil.sendMail(mailArry, null, subString, urlString);
			
			if(isSendOk){
				System.out.println( new Date()+"==================>邮件发送成功！！！");
			}else{
				logger.error("cann't send mail to " + mailArry);
				System.out.println( new Date()+"==================>邮件发送失败！！！");
			    }
		}else{
			logger.error("cann't send mail to " + mailArry);
			System.out.println( new Date()+"==================>邮箱为null发送失败！！！");
		}
	}
	
	
	/**
	 * 流程引擎中用于发送邮件提醒的方法
	 */
	public static void sendMail(String mailAddr, String mailuser, String submituser, String moduleName, String url) throws IOException{
		
		if (!"1".equals(SystemOpts.getProperty("mail.open")))
			return;
		
		
		MailUtil mailUtil =MailUtil.getSenderMail();
		mailUtil.getMailSession();
	    String content = mailuser + "你好，内控合规与风险管理平台提醒您： 请处理" + submituser + "提交的来自于" + moduleName + "模块的待办事项，提交时间：" 
	    		+ BaseController.getNewTimeSdf().format(BaseController.now()) + "<a href=\""+url+"\">请您单击这里进行处理<<</a>";
		if(null != mailAddr){
			boolean isSendOk = mailUtil.sendMail(new String[]{mailAddr}, submituser, "内控合规与风险管理平台待办提醒", content);
			
			if(isSendOk){
				System.out.println( new Date()+"==================>邮件发送成功！！！");
			}else{
				MailSendingItem mailSendingItem = new MailSendingItem();
				mailSendingItem.mailaddr = mailAddr;
				mailSendingItem.content = content;
				mailSendingItem.submituser = submituser;
				logger.error("cann't send mail to " + mailAddr);
				mailsendinglist.add(mailSendingItem);
				System.out.println( new Date()+"==================>邮件发送失败！！！");
			    }
		}else{
			logger.error("cann't send mail to " + mailAddr);
			System.out.println( new Date()+"==================>邮箱为null发送失败！！！");
		}
	}
	
	@Scheduled(cron="1/10 * * ? * *")
	public void sendSchedule() {
		for (int i = 0; i < mailsendinglist.size();) {
			MailSendingItem mailSendingItem = mailsendinglist.get(i);
			if (mailSendingItem.time > 3) {
				mailsendinglist.remove(i);
			} else {
				MailUtil mailUtil =MailUtil.getSenderMail();
				mailUtil.getMailSession();
				boolean isSendOk = mailUtil.sendMail(new String[]{mailSendingItem.mailaddr}, mailSendingItem.submituser, "内控合规与风险管理平台待办提醒", mailSendingItem.content);
				if(isSendOk){
					System.out.println( new Date()+"==================>邮件发送成功！！！");
					mailsendinglist.remove(i);
				} else {
					++i;
					mailSendingItem.time++;
				}
			}
		}
	}
	
	public static class MailSendingItem  {
		public String mailaddr;
		public String submituser;
		public String content;
		public int time = 0; 
	}
	
	
	
	public static void main(String[] args) {
		 
		String subject1 = "请尽快完成" + "监管沟通相关报告";
		String content = "content s";
		String url = "";
		String[] mailAddress = new String[]{"lining041038@163.com","435269135@qq.com"};
		 
		 
		MailUtil.sendMailUtil(mailAddress, subject1, content, url);
		
		/*
		boolean flag = MailUtil.getSenderMail().sendMail(new String[]{"15811273815@163.com"}, 
						"lixl@smartdot.com",
						"测试邮件附件发送", "测试邮件附件发送", 
						new String[]{"一创证券差异需求-daijy.doc"}, 
						new String[]{"C:/Documents and Settings/Administrator/桌面/新建文件夹/一创证券差异需求-daijy.doc"});
		if(flag) {
			System.err.println("发送成功");
		}else{
			System.err.println("发送失败");
		}*/
	}
}


class MailAuthenticator extends Authenticator {
	private String username = null;
	private String password = null;

	public MailAuthenticator(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(username, password);
	}

}


