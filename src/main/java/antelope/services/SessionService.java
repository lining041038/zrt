package antelope.services;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.transaction.TransactionStatus;
import org.springframework.util.Assert;

import antelope.db.DBUtil;
import antelope.db.DBUtil.DataType;
import antelope.springmvc.BaseController;
import antelope.springmvc.SpringUtils;
import antelope.utils.I18n;
import antelope.utils.SpeedIDUtil;
import antelope.utils.SystemOpts;
import antelope.utils.TextUtils;
import antelope.utils.UserRoleOrgUtil;

/**
 * 系统用户Session数据相关服务类
 * 使用方式如下 EL 表达式 ${sessionScope.service.username} 普通session.getAttribute("service").getUsername(); 
 * @author lining
 *
 */
public class SessionService implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private String username;// 用户的登录用名
	 
	private String user;// 用户的真实姓名
	
	private String deptname;// 用户所在部门（单位）名称
	
	private String deptcode;// 用户所在部门（单位）代码
	
	private String usersid; // 用户sid;
	
	private String locale; // 当前用户所选中的本地化字符串
	
	public SessionService(HttpSession session) {
		Assert.notNull(session); // 传入的session不能为空
	}
	
	/**
	 * 获取主题关键词字符串 如 defaults lightness等
	 */
	public String getTheme() throws SQLException, Exception {
		String sid = getUsersid();
		if (TextUtils.stringSet(sid)) {
			String str = DBUtil.querySingleString("select theme from SYS_USER where sid=?", new Object[]{sid});
			if (TextUtils.stringSet(str))
				return str;
			return SystemOpts.getProperty("default.theme");
		}
		return SystemOpts.getProperty("default.theme");
	}
	
	/**
	 * 获取当前登录用户名
	 */
	public String getUsername() {
		return username;
	}
	
	/**
	 * 获取当前所连接系统用户所选语言本地化字符串
	 * @return
	 */
	public String getLocale() {
		try {
			if (locale == null)
				locale = SystemOpts.getProperty("locale");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return locale;
	}
	
	/**
	 * 获取当前所连接系统用户所选语言对应国际化对象
	 * @return
	 */
	public I18n getI18n() {
		return new I18n(this);
	}
	
	/**
	 * 设置当前所连接系统用户所选语言本地化字符串
	 * @param locale
	 */
	public void setLocale(String locale) {
		this.locale = locale;
	}
	
	/**
	 * 获取当前用户所在单位编码 
	 */
	public String getDeptcode() {
		if (deptcode == null)
			return "organization";
		return deptcode;
	}
	
	public List<String> getDeptUsernamesByRoleName(String roleName) throws SQLException, Exception {
		TransactionStatus status = SpringUtils.beginTransaction();
		List<Object> results = DBUtil.querySingleValList("select username   "+
				"	  from sys_user   "+
				"	 where sid in   "+
				"	       (select usersid from sys_user_unit_relate where unitsid = ?)   "+
				"	   and sid in   "+
				"	       (select usersid   "+
				"	          from sys_user_role_relate   "+
				"	         where rolesid in (select sid from sys_role where name = ?))", new Object[]{getDeptsid(), roleName}, DataType.String);
		SpringUtils.commitTransaction(status);
		List<String> reulstlist = new ArrayList<String>();
		for (Object object : results) {
			reulstlist.add(object.toString());
		}
		return reulstlist;
	}
	
	/**
	 * 获取父级领导sid;
	 * @return
	 * @throws Exception
	 */
	public String getParentusersid() throws Exception {
		TransactionStatus status = SpringUtils.beginTransaction();
		List<Map<String, Object>> usr = DBUtil.query("select parentusersid from sys_user where sid=?", getUsersid());
		SpringUtils.commitTransaction(status);
		if (usr.isEmpty())
			return null;
		
		return (String) usr.get(0).get("parentusersid");
	}
	
	/**
	 * 获取父级领导用户名
	 * @return
	 * @throws Exception
	 */
	public String getParentusername() throws Exception {
		if (TextUtils.stringSet(getParentusersid())) {
			TransactionStatus status = SpringUtils.beginTransaction();
			List<Map<String, Object>> usr = DBUtil.query("select username from sys_user where sid=?", getParentusersid());
			SpringUtils.commitTransaction(status);
			if (usr.isEmpty())
				return null;
			
			return (String) usr.get(0).get("username");
		}
		
		return null;
	}
	
	/**
	 * 获取父级领导姓名
	 * @return
	 * @throws Exception
	 */
	public String getParentuser() throws Exception {
		if (TextUtils.stringSet(getParentusersid())) {
			TransactionStatus status = SpringUtils.beginTransaction();
			List<Map<String, Object>> usr = DBUtil.query("select name from sys_user where sid=?", getParentusersid());
			SpringUtils.commitTransaction(status);
			if (usr.isEmpty())
				return null;
			
			return (String) usr.get(0).get("name");
		}
		
		return null;
	}
	
	/**
	 * 获取当前登录人所在部门领导用户名
	 * @return
	 */
	public String getDeptLeaderusername() throws SQLException, Exception {
		TransactionStatus status = SpringUtils.beginTransaction();
		List<Map<String, Object>> result = DBUtil.query("select username from SYS_USER where sid in (select leaderusersid from SYS_UNIT where sid in (select unitsid from SYS_USER_UNIT_RELATE where usersid=?))", new Object[]{getUsersid()});
		SpringUtils.commitTransaction(status);
		if (result.isEmpty())
			return null;
		else
			return result.get(0).get("username") + "";
	}
	
	/**
	 * 判断当前用户是否为系统管理员用户
	 * @return
	 */
	public boolean isAdmin() {
		return UserRoleOrgUtil.isAdmin(getUsername());
	}
	
	/**
	 * 获取当前登录用户姓名
	 */
	public String getUser() {
		return user;
	}
	
	/**
	 * 获取当前登录用户sid
	 * @return
	 */
	public String getUsersid() {
		return usersid;
	}
	
	/**
	 *  获取当前人员对应部门名称
	 */
	public String getDeptname() {
		return deptname;
	}
	
	public String getDeptsid() throws Exception {
		
		if ("admin".equals(getUsername())) {
			return "orgroot";
		}
		TransactionStatus status = SpringUtils.beginTransaction();
		List<Map<String, Object>> result = DBUtil.query("select sid from SYS_UNIT where sid in (select unitsid from SYS_USER_UNIT_RELATE where usersid=?)", new Object[]{getUsersid()});
		SpringUtils.commitTransaction(status);
		if (result.isEmpty())
			return null;
		else
			return result.get(0).get("sid") + "";
	}
	
	/**
	 * 根据角色名称判断当前登录用户是否为某一个角色
	 * @param roleName 角色名称
	 * @return
	 */
	public boolean isRole(String roleName) throws SQLException, Exception {
		return UserRoleOrgUtil.isRole(getUsersid(), roleName);
	}
	/**
	 * 判断 当前登录部门的部门类型是否为总部 是则返回true add by wy 2012-03-16
	 */
	public boolean isUnitTypeZ() throws SQLException, Exception{
	  Integer num = null;
	  try{
	    num = (Integer) DBUtil.querySingleVal("select unittype from sys_unit where sid='"+getDeptsid()+"'", DataType.Integer);
	  }catch(Exception e){
	    
	  }
	  if (num!=null&&num==3){
	    return true;
	  }else{
	    return false;
	  }
	}
	/**
	 * 获取当前登录部门的部门类型 返回值integer added by wy 2012-03-16
	 * @throws Exception 
	 * @throws SQLException 
	 * 
	 */
	public Integer getUnitType() throws SQLException, Exception{
	  Integer num = null;
	  try {
        num = (Integer) DBUtil.querySingleVal("select unittype from sys_unit where sid='"+getDeptsid()+"'", DataType.Integer);
	  }catch (NullPointerException e){
	  }catch (Exception e){
	    
	  }
      return num;
	}
	/**sessionService的一些常用工具*/
	/**
	 * 方便使用的工具，获取今天字符串
	 * @return
	 */
	public String getToday() throws ParseException {
		return BaseController.getNewSdf().format(new Date());
	}
	
	/**
	 * 方便使用的工具，获取今天字符串
	 * @return
	 */
	public String getNow() throws ParseException {
		return BaseController.getNewTimeSdf().format(new Date());
	}
	
	/**
	 * 获取新的id值
	 * @return
	 */
	public String getNewsid() {
		return SpeedIDUtil.getId();
	}
	
	/**
	 * 静态方法，确认当前请求页面的用户是否已经登录
	 * @param req
	 * @return
	 */
	public static final boolean isLogon(HttpServletRequest req) {
		SessionService service = (SessionService) req.getSession().getAttribute("service");
		return service != null && service.getUsersid() != null;
	}
}
