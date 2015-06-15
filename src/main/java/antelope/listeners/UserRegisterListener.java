package antelope.listeners;

import java.sql.SQLException;

import antelope.events.UserRegisterEvent;

/**
 * 用户注册事件监听器
 * @author lining
 * @since 2012-8-27
 */
public interface UserRegisterListener {
	
	/**
	 * 用户成功注册完成之后调用此方法
	 * @param event
	 */
	public void afterRegistered(UserRegisterEvent event) throws SQLException, Exception;
}
