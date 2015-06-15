package antelope.system;

import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.log4j.Logger;
import org.springframework.transaction.TransactionStatus;

import antelope.db.DBUtil;
import antelope.entities.SysUser;
import antelope.listeners.UserLogoutListener;
import antelope.listeners.events.UserLogoutEvent;
import antelope.services.SessionService;
import antelope.springmvc.SpringUtils;

public class SystemSessionListener implements HttpSessionListener {

	/**
	 * 日志对象，子类使用此对象记录系统常见日志
	 */
	protected Logger log = Logger.getLogger(this.getClass());
	
	@Override
	public void sessionCreated(HttpSessionEvent arg0) {
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent arg0) {
		List<UserLogoutListener> listeners = SpringUtils.getBeans(UserLogoutListener.class);
		SessionService service = (SessionService) arg0.getSession().getAttribute("service");
		TransactionStatus status = SpringUtils.beginTransaction();
		try {
			if (service != null) {
				List<SysUser> users = DBUtil.queryEntities("select * from SYS_USER where sid=?", service.getUsersid(), SysUser.class);
				if (users.isEmpty())
					return;
				for (UserLogoutListener userLogoutListener : listeners) {
					UserLogoutEvent event = new UserLogoutEvent();
					event.sysuser = users.get(0);
					userLogoutListener.afterLogoutSuccess(event);
				}
			}
		} catch (SQLException e) {
			status.setRollbackOnly();
			e.printStackTrace();
		} catch (Exception e) {
			status.setRollbackOnly();
			e.printStackTrace();
		} finally {
			SpringUtils.commitTransaction(status);
		}
	}

}
