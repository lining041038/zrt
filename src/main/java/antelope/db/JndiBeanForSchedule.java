package antelope.db;

import java.io.IOException;

import javax.activation.DataSource;
import javax.naming.NamingException;

import org.dom4j.DocumentException;
import org.springframework.beans.factory.FactoryBean;

/**
 * 计划任务所使用的数据源工厂
 * @author lining
 * @since 2012-7-17
 */
public class JndiBeanForSchedule implements FactoryBean {
	
	@Override
	public Object getObject() {
		try {
			return DBHelper.getDataSource();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return null;
	} 

	@Override
	public Class getObjectType() {
		return DataSource.class;
	}

	@Override
	public boolean isSingleton() {
		return false;
	}
}
