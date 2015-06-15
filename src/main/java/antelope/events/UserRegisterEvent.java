package antelope.events;

import antelope.entities.SysRole;

/**
 * 用户注册事件对象
 * @author lining
 * @since 2012-8-27
 */
public class UserRegisterEvent {
	
	/**
	 * 当用户注册时，将自动将用户放入一个角色当中
	 * 此属性保存此默认用户自注册角色 
	 */
	public final SysRole role;
	
	public UserRegisterEvent(SysRole role) {
		this.role = role;
	}
}
