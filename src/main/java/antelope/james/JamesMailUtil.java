package antelope.james;


import java.util.ArrayList;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.UIDFolder;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 *
 * James邮件操作工具类
 * @author dai jun
 *
 */
public class JamesMailUtil extends Authenticator{

	protected String from;
	protected Session session;
	protected Store store;
	protected Folder inbox;
	protected PasswordAuthentication authentication;


	public JamesMailUtil(String user, String password,String host) {
		from = user + '@' + host;
		if (null == password)password = user;
		authentication = new PasswordAuthentication(user, password);
		Properties props = new Properties();
		props.put("mail.user", user);
		props.put("mail.host", host);
		props.put("mail.smtp.host", host);
		props.put("mail.store.protocol", "pop3");
		props.put("mail.transport.protocol", "smtp");
		session = Session.getInstance(props, this);
	}

	/**
	 *
	 * send Message
	 *
	 * @param to
	 * @param subject
	 * @param content
	 * @throws Exception
	 */
	public void sendMessage(String to, String subject, String content,String [] files)
			throws Exception {
		//System.out.println("SENDING message from " + from + " to " + to);
		MimeMessage msg = new MimeMessage(session);
		msg.addRecipients(Message.RecipientType.TO, to);
		msg.setSubject(subject);
		msg.setText(content);
	   //String address == session.getProperties(from);
		Address address = new InternetAddress(from);
		msg.setFrom(address);

		// deal with attachment
		if (null != files && files.length>1){ // has attachment
			int fileLen = files.length;
			Multipart mp = new MimeMultipart();
			for (int k=0;k<fileLen;k++){
				MimeBodyPart mbp = new MimeBodyPart();
				String fileName = files[k];
				mbp.attachFile(fileName);
				mp.addBodyPart(mbp);
			}
			msg.setContent(mp);
		}

		Transport.send(msg);
	}

	/**
	 * retrieve mail list
	 *
	 * @return
	 * @throws Exception
	 */
	public Message[] getMailList(boolean isCloseResource) throws Exception{
		 store = session.getStore();
		 store.connect();
		 Folder root = store.getDefaultFolder();
		 inbox = root.getFolder("inbox");
		 inbox.open(Folder.READ_WRITE);
		 if (inbox instanceof UIDFolder){
			 System.out.println(11);
			 UIDFolder uidFolder;

		 }
		 Message[] msgs = inbox.getMessages();
		 if (isCloseResource){
			 close();
		 }
		 return msgs;
	}

	/**
	 * delete mails
	 *
	 * @param messages
	 * @throws Exception
	 */
	public void deleteMails(ArrayList<String> messageIds) throws Exception {
		//遍历邮件
		Message[] msgs = getMailList(false);
	    int msgLen = msgs.length;
	    for (int i = 0; i < msgLen; i++){
	    	Message msg = msgs[i];

	    	//msg.getMessageNumber();
	    	// 确认在删除列表中
	    	if (messageIds.contains(String.valueOf(msg.getMessageNumber()))){
	    	//	msg.setFlag(Flags.Flag.DELETED, true);
	    	}
	    }
	    close();
	}

	private void close()throws Exception {
		inbox.close(true);
	    store.close();
	}

	/**
	 * delete single mail
	 *
	 * @param message
	 * @throws Exception
	 */
	public void deleteMail(String messageId) throws Exception {
		ArrayList<String> messageIds = new ArrayList<String>();
		messageIds.add(messageId);
		deleteMails(messageIds);
	}

	public PasswordAuthentication getPasswordAuthentication() {
		return authentication;
	}


}
