package antelope.utils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.sql.SQLException;
import java.util.Properties;

import javax.xml.bind.JAXBElement.GlobalScope;

import org.apache.tomcat.jni.Global;
import org.springframework.transaction.TransactionStatus;

import antelope.consts.GlobalConsts;
import antelope.db.DBUtil;
import antelope.springmvc.SpringUtils;


public class SystemOpts {
	
	private static Properties props = null;
	private static SystemOpts getter = new SystemOpts();
	
	/**
	 * 获取属性 系统会从两个地方同时获取，system-opts.properties文件中，或从SYS_OPTS数据库表中
	 * 若system-opts.properties存在某属性则直接用，否则尝试从SYS_OPTS获取，若再次获取失败，则返回null值
	 * @param key
	 * @return
	 * @throws IOException 
	 */
	private String getPropertyInner(String key) throws IOException {
		if (key == null)
			return null;
		if (props == null) {
			props = new Properties();
			
			props.load(new InputStreamReader(this.getClass().getResourceAsStream("/system-opts.properties"), "utf-8"));
		}
		
		// 开发者模式，选项属性时时读取。
		if (GlobalConsts.isDevelopMode) {
			StringReader sr = new StringReader(ClasspathResourceUtil.getTextByPathNoCached("/system-opts.properties"));
			props.load(sr);
		}
		
		String propval = props.getProperty(key);
		if (TextUtils.stringSet(propval))
			return props.getProperty(key);
		
		try {
			TransactionStatus status = SpringUtils.beginTransaction();
			String val = "";
			try {
				val = DBUtil.querySingleString("select value from sys_opts where key_=?", new Object[]{key});
			} finally {
				SpringUtils.commitTransaction(status);
			}
			return val;
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String getProperty(String key) throws IOException {
		return getter.getPropertyInner(key);
	}
}
