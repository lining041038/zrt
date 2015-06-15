package antelope.listeners;

import antelope.listeners.events.UserLogoutEvent;

public interface UserLogoutListener {
	public void afterLogoutSuccess(UserLogoutEvent event);
}
