package antelope.db;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.util.Assert;


/**
 * 根据数据类型寻找对应数据库数据类型
 * @author pc
 *
 */
public class DBDataTypeFinder {
	
	/**
	 * 根据数据类型寻找对应数据库表达方式
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public static String find(String type) throws Exception {
	
		
		if (DBHelper.getDbType().indexOf("SQLServer") != -1) {
			return getSqlServerType(type, 8000);
		}
		
		if (DBHelper.getDbType().indexOf("Oracle") != -1) {
			return getOracleType(type, 4000);
		}
		
		throw new Exception("对应数据库报表发布数据类型区分还未实现！");
	}
	/**
	 * 根据数据类型寻找对应数据库表达方式
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public static String find(String type, int length) throws Exception {
		if (DBHelper.getDbType().indexOf("SQLServer") != -1) {
			return getSqlServerType(type, length);
		}
		
		if (DBHelper.getDbType().indexOf("Oracle") != -1) {
			return getOracleType(type, length);
		}
		
		throw new Exception("对应数据库报表发布数据类型区分还未实现！");
	}
	
	
	
	private static String getSqlServerType(String type, int length) {
		Assert.notNull(type);
		
		if ("text".equals(type)) {
			return " varchar("+length+") ";
		} else if ("num".equals(type)) {
			return " float ";
		} else if ("date".equals(type)) {
			return " datetime ";
		} else if ("dict".equals(type)) {
			return " varchar(1000) ";
		} else if ("file".equals(type)) { // 文件的话存文件组对应sid
			return " varchar(1000) ";
		} else if ("image".equals(type)) { // 图片的话存图片对应文件组sid
			return " varchar(1000) ";
		}
		
		return " varchar(8000) ";
	}
	
	private static String getOracleType(String type, int length) {
		if ("text".equals(type)) {
			return " varchar2("+length+") ";
		} else if ("num".equals(type)) {
			return " number ";
		} else if ("date".equals(type)) {
			return " timestamp ";
		} else if ("file".equals(type)) { // 文件的话存文件组对应sid
			return " varchar2(1000) ";
		} else if ("image".equals(type)) { // 图片的话存图片对应文件组sid
			return " varchar2(1000) ";
		}
		return " varchar2(4000) ";
	}
}
