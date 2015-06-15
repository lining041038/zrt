package antelope.james;


import javax.mail.Message;
import javax.mail.internet.MimeMessage;

public class JamesMailTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		// 测试邮件发送
		/*JamesMailUtil jmu = new JamesMailUtil("test","test","mydomain.com");
		String [] files = new String[1];
		files[0] = "c:/a.txt";
		try {
			jmu.sendMessage("white@mydomain.com", "hello", "hihihi", files);
		}catch (Exception e){
			e.printStackTrace();
		}*/

		// 测试邮件接收
		/*
		JamesMailUtil jmu2 = new JamesMailUtil("test","test","mydomain.com");

		try {
			Message [] msgs = jmu2.getMailList(true);
			for (int i=0;i<msgs.length;i++){
				MimeMessage msg = (MimeMessage)msgs[i];
				System.out.println(msgs[i].getMessageNumber());
				System.out.println(msg.getMessageID());
				//msgs[i].get
			}
			System.out.println();
		}catch(Exception e){
			e.printStackTrace();
		}*/

		// 测试邮件删除
		JamesMailUtil jmu2 = new JamesMailUtil("test","test","mydomain.com");
		try {
			jmu2.deleteMail("1");
			System.out.println();
		}catch(Exception e){
			e.printStackTrace();
		}


	}

}
