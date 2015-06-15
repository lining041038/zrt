package antelope.listeners;

import antelope.beans.UserLoginEvent;


/**
 * 用户登录事件
 * @author pc
 * @since 2011-10-20
 */
public interface UserLoginListener {
	public void afterLoginSuccess(UserLoginEvent event);
}
