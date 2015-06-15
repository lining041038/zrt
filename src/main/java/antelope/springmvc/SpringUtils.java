package antelope.springmvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.ApplicationContext;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.context.support.XmlWebApplicationContext;

import antelope.system.SystemCache;
import antelope.utils.SpeedIDUtil;


/**
 * 提供spring相关常见方法。如getBeans
 * @author lining
 */
@Component
public class SpringUtils {
	
	public static ApplicationContext appcontext;
	
	/**
	 * 根据类型获取系统中所有标记的bean对象
	 * @param clazz
	 * @param req
	 * @return
	 */
	public static <A> List<A> getBeans(Class<A> clazz) {
		ApplicationContext att = getApplicationContext();
		if (att == null)
			return new ArrayList<A>();
		Map<String, A> beans = att.getBeansOfType(clazz);
		List<A> providers = new ArrayList<A>();
		Set<Entry<String, A>> entries = beans.entrySet();
		for (Entry<String, A> entry : entries) {
			providers.add(entry.getValue());
		}
		return providers;
	}
	
	/**
	 * 根据类型获取系统中第一个标记的bean对象, 若未找到则返回null
	 * @param clazz
	 * @return
	 */
	public static <A> A getBean(Class<A> clazz) {
	
		ApplicationContext context = getApplicationContext();
		if (context == null)
			return null;
		
		Map<String, A> beans = context.getBeansOfType(clazz);
		List<A> providers = new ArrayList<A>();
		Set<Entry<String, A>> entries = beans.entrySet();
		for (Entry<String, A> entry : entries) {
			providers.add(entry.getValue());
		}
		if (providers.size() > 0 )
			return providers.get(0);
		return null;
	}
	
	/**
	 * 根据父类型，bean名称获取bean
	 * @param clazz
	 * @param beanName
	 * @return
	 */
	public static <A> A getBean(Class<A> clazz, String name) {
		ApplicationContext context = getApplicationContext();
		if (context == null)
			return null;
		return context.getBean(name, clazz);
	}
	
	/**
	 * 手动开始某个事务，只在非页面请求之外调用。
	 * 
	 * @param servletContext
	 */
	public static TransactionStatus beginTransaction() {
		JpaTransactionManager txManager = getBean(JpaTransactionManager.class, "txManager");
		if (txManager == null)
			return null;
		
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		String transname = "trans" + SpeedIDUtil.getId();
		def.setName(transname);
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		TransactionStatus trans = txManager.getTransaction(def);
		return trans;
	}
	
	/**
	 * 手动提交某个事务，只在非页面请求之外调用。
	 * @param servletContext
	 */
	public static void commitTransaction(TransactionStatus status) {
		if (status == null)
			return;
		JpaTransactionManager txManager = getBean(JpaTransactionManager.class, "txManager");
		txManager.commit(status);
	}
	
	/**
	 * 手动提交某个事务，只在非页面请求之外调用。
	 * @param servletContext
	 */
	public static void rollbackTransaction(TransactionStatus status) {
		if (status == null)
			return;
		JpaTransactionManager txManager = getBean(JpaTransactionManager.class, "txManager");
		txManager.rollback(status);
	}
	
	/**
	 * 获取spring应用上下文
	 * @return
	 */
	public static ApplicationContext getApplicationContext() {
		if (SystemCache.servletContext != null) {
			return (XmlWebApplicationContext) SystemCache.servletContext.getAttribute("org.springframework.web.servlet.FrameworkServlet.CONTEXT.springmvc");
		}
		return appcontext;
	}
}
