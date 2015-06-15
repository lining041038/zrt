package antelope.interfaces;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import antelope.springmvc.BaseComponent;

/**
 * 系统信息删除验证器接口，用于在系统信息删除时
 * 子模块判断是否允许删除。
 * @author lining
 * @since 2012-7-10
 */
public abstract class SystemInfoDeleteValidator extends BaseComponent {
	
	/**
	 * 对用户进行删除操作时触发验证 若返回null 或空字符串，则表示允许删除
	 * 否则 不允许删除，并使用所返回的字符串作为禁止删除的原因。
	 * @param sysuser 将要删除的用户
	 * @param req 前台的删除请求
	 * @return 返回删除原因
	 */
	public abstract String validateDeleteSysUser(String sid, HttpServletRequest req) throws SQLException, Exception;
}
