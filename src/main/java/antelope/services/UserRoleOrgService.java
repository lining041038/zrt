package antelope.services;

import java.lang.reflect.InvocationTargetException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import antelope.consts.GlobalConsts;
import antelope.db.DBUtil;
import antelope.db.DBUtil.DataType;
import antelope.entities.SysRole;
import antelope.entities.SysUnit;
import antelope.entities.SysUser;
import antelope.events.UserRegisterEvent;
import antelope.interfaces.SystemInfoDeleteValidator;
import antelope.james.JamesMgtJMX;
import antelope.listeners.UserRegisterListener;
import antelope.springmvc.BaseComponent;
import antelope.springmvc.SpringUtils;
import antelope.utils.I18n;
import antelope.utils.SecurityUtil;
import antelope.utils.SpeedIDUtil;
import antelope.utils.SystemOpts;

/**
 * 用户角色组织机构信息服务类
 * @author lining
 *
 */
@Service
public class UserRoleOrgService extends BaseComponent{

	public List<SysUnit> getUnitsByParentSid(String parentsid) throws Exception {
		return dao.query("select * from SYS_UNIT t where t.parentsid=?", new Object[]{parentsid}, SysUnit.class);
	}
	
	/**
	 * 根据单位sid获取用户信息
	 * @param unitsid
	 * @return
	 * @throws Exception
	 */
	public List<SysUser> getUsersByUnitSid(String unitsid) throws Exception {
		return dao.query("select * from SYS_USER t where t.sid in (select usersid from SYS_USER_UNIT_RELATE where unitsid=?)"
				, new Object[]{unitsid}, SysUser.class);
	}
	
	/**
	 * 获取属于某单位以及其子单位，并属于某个角色人员
	 * @param unitsid
	 * @param rolesid
	 * @return
	 * @throws Exception 
	 */
	public List<SysUser> getUsersBelongToUnitAndChildUnitsByRolesid(String unitsid, String roleSid) throws Exception {
		List<SysUser> users = new ArrayList<SysUser>();
		getSysUserByUnitsid(users, unitsid, roleSid);
		return users;
	}
	
	private void getSysUserByUnitsid(List<SysUser> users, String unitsid, String roleSid) throws Exception {
		List<SysUser> newusers = getUsersByUnitSid(unitsid);
		for (SysUser sysUser : newusers) {
			if (isRoleByRolesid(roleSid, sysUser.sid)) {
				users.add(sysUser);
			}
		}
		List<SysUnit> units = getUnitsByParentSid(unitsid);
		for (SysUnit sysUnit : units) {
			getSysUserByUnitsid(users,sysUnit.sid, roleSid);
		}
	}
	
	/**
	 * 根据角色名称判断当前登录用户是否为某一个角色
	 * @param roleName 角色名称
	 * @return
	 */
	public boolean isRole(String roleName, String usersid) throws SQLException, Exception {
		return !DBUtil.query("select * from SYS_USER_ROLE_RELATE t where t.usersid=? and" +
				" exists (select sid from SYS_ROLE where name=? " +
				"and sid=t.rolesid)", new Object[]{usersid, roleName}).isEmpty();
	}
	
	/**
	 * 根据角色sid判断当前登录用户是否为某一个角色
	 * @param roleName 角色名称
	 * @return
	 */
	public boolean isRoleByRolesid(String rolesid, String usersid) throws SQLException, Exception {
		return !DBUtil.query("select * from SYS_USER_ROLE_RELATE t where t.usersid=? and t.rolesid=?", new Object[]{usersid, rolesid}).isEmpty();
	}

	/**
	 * 根据部门sid获取部门领导用户，若未找到，返回null;
	 * @param deptsid 部门sid
	 * @return
	 * @throws Exception 
	 */
	public SysUser getDeptLeaderUserByDeptsid(String deptsid) throws Exception {
		List<SysUser> users = dao.query("select * from SYS_USER where sid in (select leaderusersid from SYS_UNIT where sid = ?)"
				, deptsid, SysUser.class);
		if (users.size() > 0)
			return users.get(0);
		return null;
	}
	
	/**
	 * 寻找符合符合其中之一单位类型的单位
	 * @param types
	 * @param unitsid
	 * @return
	 */
	public SysUnit getClosestUnit(Integer[] types, String unitsid) throws Exception {
		SysUnit unit = null;
		while ((unit = dao.getBy(unitsid, SysUnit.class)) != null) {
			for (int i = 0; i < types.length; ++i) {
				if (unit.unittype != null && unit.unittype.equals(types[i]))
					return unit;
			}
			unitsid = unit.parentsid;
		}
		return null;
	}

	/**
	 * 测试某单位对应某个角色有没有相应人员
	 * @param deptsid
	 * @param roleName
	 * @return
	 * @throws Exception 
	 * @throws SQLException 
	 */
	public boolean deptHasRole(String deptsid, String roleName) throws SQLException, Exception {
		Integer val = (Integer) DBUtil.querySingleVal("select count(*)  "+
								"  from sys_user t2  "+
								" where t2.sid in  "+
								"       (select usersid  "+
								"          from SYS_USER_ROLE_RELATE  "+
								"         where rolesid in  "+
								"               (select sid from SYS_ROLE where name = ?))  "+
								"   and t2.sid in  "+
								"       (select usersid  "+
								"          from SYS_USER_UNIT_RELATE  "+
								"         where unitsid = ? )", new Object[]{roleName, deptsid}, DataType.Integer);
		return val > 0;
	}
	
	
	/**
	 * 根据部门sid以及角色名称获取用户 
	 * @param deptsid
	 * @param roleName
	 * @return
	 */
	public List<SysUser> getUserByDeptsidAndRoleName(String deptsid, String roleName) throws Exception {
		List<SysUser> users = dao.query("select *  "+
								"  from sys_user t2  "+
								" where t2.sid in  "+
								"       (select usersid  "+
								"          from SYS_USER_ROLE_RELATE  "+
								"         where rolesid in  "+
								"               (select sid from SYS_ROLE where name = ?))  "+
								"   and t2.sid in  "+
								"       (select usersid  "+
								"          from SYS_USER_UNIT_RELATE  "+
								"         where unitsid = ? )"
								, new Object[]{roleName, deptsid}, SysUser.class);
		return users;
	}
	
	/**
	 * 是否对某功能有权限
	 * @param usersid
	 */
	public Set<String> getAuthorities(String usersid) throws SQLException, Exception {
		List<Map<String, Object>> results = DBUtil.query("select distinct(functionid) functionid from SYS_AUTHORITY where roleorusersid=? or roleorusersid in (select rolesid from SYS_USER_ROLE_RELATE where usersid=?)", new Object[]{usersid, usersid});
		Set<String> funcs = new HashSet<String>();
		for (int i = 0; i < results.size(); i++) {
			funcs.add(results.get(i).get("functionid").toString());
		}
		return funcs;
	}
	
	/**
	 * 用于单人是否对某功能有权限
	 * @param usersid
	 */
	public Set<String> getUserAuthorities(String usersid) throws SQLException, Exception {
		List<Map<String, Object>> results = DBUtil.query("select * from SYS_AUTHORITY where roleorusersid=?", new Object[]{usersid});
		Set<String> funcs = new HashSet<String>();
		for (int i = 0; i < results.size(); i++) {
			funcs.add(results.get(i).get("functionid").toString());
		}
		return funcs;
	}
	
	/**
	 * 用于角色是否对某功能有权限
	 * @param usersid
	 */
	public Set<String> getRoleAuthorities(String rolesid) throws SQLException, Exception {
		List<Map<String, Object>> results = DBUtil.query("select * from SYS_AUTHORITY where roleorusersid=?", new Object[]{rolesid});
		Set<String> funcs = new HashSet<String>();
		for (int i = 0; i < results.size(); i++) {
			funcs.add(results.get(i).get("functionid").toString());
		}
		return funcs;
	}
	
	/**
	 * 根据角色sid获取用户信息
	 * @param unitsid
	 * @return
	 * @throws Exception
	 */
	public List<SysUser> getUsersByRoleSid(String rolesid) throws Exception {
		return dao.query("select * from SYS_USER t where t.sid in (select usersid from SYS_USER_ROLE_RELATE where rolesid=?)"
				, new Object[]{rolesid}, SysUser.class);
	}
	
	/**
	 * 添加用户组织机构（单位）关联关系
	 * @param usersid
	 * @param unitsid
	 */
	public void addUserUnitRelate(String usersid, String unitsid) {
		dao.updateBySQL("insert into SYS_USER_UNIT_RELATE(usersid, unitsid) values(?,?)", new Object[]{usersid, unitsid});
	}
	
	/**
	 * 彻底删除用户 包括关联单位角色权限
	 * @param usersid
	 */
	public String deleteUserCompletely(String usersid, HttpServletRequest req) throws SQLException, Exception {
		
		List<SystemInfoDeleteValidator> validators = SpringUtils.getBeans(SystemInfoDeleteValidator.class);
		for (SystemInfoDeleteValidator systemInfoDeleteValidator : validators) {
			String retval = systemInfoDeleteValidator.validateDeleteSysUser(usersid, req);
			if (stringSet(retval)) {
				return retval;
			}
		}
		
		dao.deleteBy(usersid, SysUser.class);
		removeUserUnitRelate(usersid); // 单位
		removeAllUserRoleRelate(usersid); // 角色
		removeAllUserAuthoritiesBy(usersid); // 权限
		return null;
	}
	
	/**
	 * 批量彻底删除用户 包括关联单位角色权限
	 * @param usersid
	 */
	public String batchDeleteUserCompletely(String[] usersids, HttpServletRequest req) throws SQLException, Exception {
		List<SystemInfoDeleteValidator> validators = SpringUtils.getBeans(SystemInfoDeleteValidator.class);
		
		for (int i = 0; i < usersids.length; ++i) {
			for (SystemInfoDeleteValidator systemInfoDeleteValidator : validators) {
				String retval = systemInfoDeleteValidator.validateDeleteSysUser(usersids[i], req);
				if (stringSet(retval)) {
					return retval;
				}
			}
		}
		
		for (int i = 0; i < usersids.length; ++i) {
			dao.deleteBy(usersids[i], SysUser.class);
			removeUserUnitRelate(usersids[i]); // 单位
			removeAllUserRoleRelate(usersids[i]); // 角色
			removeAllUserAuthoritiesBy(usersids[i]); // 权限
		}
		
		return null;
	}
	
	/**
	 * 添加用户角色关联关系
	 * @param usersid
	 * @param unitsid
	 */
	public void addUserRoleRelate(String usersid, String rolesid) {
		dao.updateBySQL("insert into SYS_USER_ROLE_RELATE(usersid, rolesid) values(?,?)", new Object[]{usersid, rolesid});
	}
	
	/**
	 * 根据角色id删除所有用户角色关联关系
	 */
	public void removeAllUserRoleRelateByRolesid(String rolesid) {
		dao.updateBySQL("delete from SYS_USER_ROLE_RELATE where rolesid=?", new Object[]{rolesid});
	}
	
	/**
	 * 删除用户组织机构（单位）关联关系
	 * @param usersid
	 */
	public void removeUserUnitRelate(String usersid) {
		dao.updateBySQL("delete from SYS_USER_UNIT_RELATE where usersid=?", new Object[]{usersid});
	}
	
	/**
	 * 删除用户角色关联关系
	 * @param usersid
	 */
	public void removeUserRoleRelate(String usersid, String rolesid) {
		dao.updateBySQL("delete from SYS_USER_ROLE_RELATE where usersid=? and rolesid=?", new Object[]{usersid, rolesid});
	}
	
	/**
	 * 根据用户sid删除所有用户角色关联关系
	 * @param usersid
	 */
	public void removeAllUserRoleRelate(String usersid) {
		dao.updateBySQL("delete from SYS_USER_ROLE_RELATE where usersid=?", new Object[]{usersid});
	}
	
	/**
	 * 根据用户usersid删除用户单独关联权限
	 */
	public void removeAllUserAuthoritiesBy(String usersid) {
		dao.updateBySQL("delete from SYS_AUTHORITY where roleorusersid=?", new Object[]{usersid});
	}
	
	/**
	 * 根据用户名查找用户
	 * @return 
	 * @throws Exception 
	 */
	public List<SysUser> getUserByUserName(String username) throws Exception {
		return dao.query("select * from SYS_USER where username=?", new Object[]{username}, SysUser.class);
	}
	
	/**
	 * 根据用户名查找用户
	 * @return 
	 * @throws Exception 
	 */
	public List<SysUser> getUserBySid(String sid) throws Exception {
		return dao.query("select * from SYS_USER where sid=?", new Object[]{sid}, SysUser.class);
	}
	
	/**
	 * 根据用户sid获取此用户所拥有的所有角色
	 * @param sid
	 * @return 所查询用户所拥有的所有角色
	 */
	public List<SysRole> getRolesByUsersid(String sid) throws Exception {
		return dao.query("select * from SYS_ROLE where sid in (select rolesid from SYS_USER_ROLE_RELATE where usersid=?)", sid, SysRole.class);
	}
	
	/**
	 * 根据用户查找用户所属单位
	 * @param sid
	 * @return
	 */
	public List<SysUnit> getUnitsByUsersid(String sid) throws Exception {
		List<SysUnit> units = dao.query("select * from SYS_UNIT where sid in (select unitsid from SYS_USER_UNIT_RELATE where usersid=?)"
				, new Object[]{sid},SysUnit.class);
		
		// 若此人不属于任何单位，则目前为admin系统管理员
		if (units.size() == 0) {
			SysUnit unit = new SysUnit();
			unit.name = "组织机构";
			units.add(unit);
		}
		
		return units;
	}
	/**
	 * 根据部门名称获取单位
	 * @param deptName
	 * @return
	 * @throws Exception
	 */
	public List<SysUnit> getUnitsByDeptName(String deptName)throws Exception{
		return dao.getBy("NAME", deptName, SysUnit.class);
	}
	
	/**
	 * 添加自注册用户。若调用则系统将自动创建一个自注册用户的角色，并将此人放入此角色当中。
	 * @param user
	 * @return
	 * @throws Exception 
	 */
	public String addSelfRegisterSysUser(SysUser user, String unitnamecode, String rolename) throws Exception {

		// 自注册用户未划分所属部门单位，将其添加到一个特殊单位下即可.
		List<SysUnit> unit = dao.getBy("flag", "1", SysUnit.class);
		String unitsid = "";
		if (unit.isEmpty()) {
			SysUnit sysunit = new SysUnit();
			sysunit.name = unitnamecode;
			sysunit.code = unitnamecode;
			sysunit.parentsid = GlobalConsts.TOP_UNIT_SID;
			sysunit.flag = "1";
			sysunit.sid = SpeedIDUtil.getId();
			unitsid = sysunit.sid; 
			dao.insertOrUpdate(sysunit);
		} else {
			unitsid = unit.get(0).sid;
		}
		
		// 自注册用户未划分角色，将其添加到一个特殊角色下即可
		List<SysRole> roles = dao.getBy("flag", "1", SysRole.class);
		String rolesid = "";
		SysRole therole = null;
		if (roles.isEmpty()) {
			SysRole role = new SysRole();
			role.flag = "1";
			role.name = rolename;
			role.sid = SpeedIDUtil.getId();
			rolesid = role.sid;
			dao.insertOrUpdate(role);
			therole = role;
		} else {
			rolesid = roles.get(0).sid;
			therole = roles.get(0);
		}
		
		String retval = addSysUser("true", user, unitsid);
		
		if (stringSet(retval)) {
			return retval;
		} else { // 没有返回信息则证明注册成功,触发一下注册完成事件！
			addUserRoleRelate(user.sid, rolesid);
			List<UserRegisterListener> registers = SpringUtils.getBeans(UserRegisterListener.class);
			for (UserRegisterListener userRegisterListener : registers) {
				UserRegisterEvent event = new UserRegisterEvent(therole);
				userRegisterListener.afterRegistered(event);
			}
		}
		
		return null;
	}
	
	public String addSysUser(String passwordchanged, SysUser user, String unitsid)
			throws Exception, NoSuchAlgorithmException, SQLException,
			IllegalAccessException, InvocationTargetException {
		
		List<SysUser>  users = getUserByUserName(user.username);
		List<SysUser>  userss = getUserBySid(user.sid);
		if (!users.isEmpty()) {
			if (!userss.isEmpty()) {
				if( !users.get(0).username.equals(userss.get(0).username)) {
					return "antelope.usernameallreadyhave";
				}
			} else {
				return "antelope.usernameallreadyhave";
			}
		}
		if (userss.isEmpty()) {
			addUserUnitRelate(user.sid, unitsid);
		}
		
		if (stringSet(passwordchanged)) {// 密码发生修改，则将新密码保存
			user.password = SecurityUtil.sha1(user.password.getBytes());
		}
		if (userss.isEmpty()) {
			// 邮件自动添加
			if ("1".equals(SystemOpts.getProperty("antelope_jamesopen"))) {
				int ct = DBUtil.queryCount("select count(*) from users where username=?", user.username);
				if (ct == 0) { // 邮箱未注册的进行注册
					JamesMgtJMX jmx = new JamesMgtJMX();
					jmx.addUser(user.username, user.password);
				}
			}
		}
		dao.insertOrUpdate(user);
		return null;
	}
}



