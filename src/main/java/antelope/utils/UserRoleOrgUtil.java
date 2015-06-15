package antelope.utils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.transaction.TransactionStatus;

import antelope.consts.GlobalConsts;
import antelope.db.DBUtil;
import antelope.entities.SysUnit;
import antelope.entities.SysUser;
import antelope.springmvc.SpringUtils;


/**
 * 用户角色机构工具类
 * @author lining
 */
public class UserRoleOrgUtil {
	
	/**
	 * 判断是否为admin
	 * @param username
	 * @return
	 */
	public static final boolean isAdmin(String username) {
		TransactionStatus status = SpringUtils.beginTransaction();
		List<SysUser> user = new ArrayList<SysUser>();
		try {
			user = DBUtil.queryEntities("select * from SYS_USER where username=?", username, SysUser.class);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		SpringUtils.commitTransaction(status);
		if (user.size()  == 0) 
			return false;
		
		return isAdminByUsersid(user.get(0).sid);
	}
	
	/**
	 * 根据用户sid判断是否为admin
	 * @param username
	 * @return
	 */
	public static final boolean isAdminByUsersid(String usersid) {
		try {
			return isRoleBySid(usersid, GlobalConsts.ADMIN_ROLE_SID);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static final boolean isRoleBySid(String usersid, String rolesid) throws SQLException, Exception {
		TransactionStatus status = SpringUtils.beginTransaction();
		boolean result = !DBUtil.query("select * from SYS_USER_ROLE_RELATE t where t.usersid=? and rolesid=?", new Object[]{usersid.trim(), rolesid.trim()}).isEmpty();
		SpringUtils.commitTransaction(status);
		return result;
	}
	
	public static final SysUser getSysUserByUsername(String username) throws SQLException, Exception {
		TransactionStatus status = SpringUtils.beginTransaction();
		List<SysUser> users = DBUtil.queryEntities("select * from SYS_USER where username=?", username, SysUser.class);
		SpringUtils.commitTransaction(status);
		if (users.isEmpty()) {
			return null;
		}
		return users.get(0);
	}
	
	/**
	 * 根据角色sid和单位sid获取用户列表 
	 * @param deptsid
	 * @param rolesid
	 */
	public static final List<SysUser> getSysUsersByRolesidAndDeptSid(String deptsid, String rolesid) throws SQLException, Exception {
		TransactionStatus status = SpringUtils.beginTransaction();
		List<SysUser> results = DBUtil.queryEntities("select *  "+
							"  from SYS_USER  "+
							" where sid in (select usersid from SYS_USER_ROLE_RELATE where rolesid = ?)  "+
							"   and sid in (select usersid from SYS_USER_UNIT_RELATE where unitsid = ?)", 
				new Object[]{rolesid, deptsid}, SysUser.class);
		SpringUtils.commitTransaction(status);
		return results;
	}
	
	/**
	 * 根据角色sid和单位sid获取第一个用户
	 * @param deptsid
	 * @param roleName
	 * @return
	 */
	public static final SysUser getSysUserByRolesidAndDeptSid(String deptsid, String rolesid) throws SQLException, Exception {
		List<SysUser> users = getSysUsersByRolesidAndDeptSid(deptsid, rolesid);
		if (users.size() > 0)
			return users.get(0);
		return  null;
	}
	
	public static final SysUnit getUnitByUsername(String username) throws SQLException, Exception {
		if (isAdmin(username)) { // admin返回暂时为null
			return null;
		}
		TransactionStatus status = SpringUtils.beginTransaction();
		List<SysUnit> units = DBUtil.queryEntities("select * from SYS_UNIT where sid in (select unitsid from SYS_USER_UNIT_RELATE where usersid in (select sid from SYS_USER where username=?))", new Object[]{username}, SysUnit.class);
		SpringUtils.commitTransaction(status);
		if (units.isEmpty()) 
			return null;
		return units.get(0);
	}
	
	/**
	 * 根据角色sid获取用户列表
	 * @param roleName
	 * @return
	 */
	public static final List<SysUser> getSysUsersByRolesid(String rolesid) throws SQLException, Exception {
		TransactionStatus status = SpringUtils.beginTransaction();
		List<SysUser> results = DBUtil.queryEntities("select * from SYS_USER t where exists (select usersid from SYS_USER_ROLE_RELATE where rolesid=? and usersid=t.sid)", rolesid, SysUser.class);
		SpringUtils.commitTransaction(status);
		return results;
	}
	
	/**
	 * 根据角色sid获取第一个用户
	 * @param roleName
	 * @return
	 */
	public static final SysUser getSysUserByRolesid(String rolesid) throws SQLException, Exception {
		List<SysUser> users = getSysUsersByRolesid(rolesid);
		if (users.size() > 0)
			return users.get(0);
		return  null;
	}
	
	/**
	 * 根据角色名称判断当前登录用户是否为某一个角色
	 * @param roleName 角色名称
	 * @return
	 */
	public static boolean isRole(String usersid, String roleName) throws SQLException, Exception {
		TransactionStatus status = SpringUtils.beginTransaction();
		boolean result = !DBUtil.query("select * from SYS_USER_ROLE_RELATE t where t.usersid=? and" +
				" exists (select sid from SYS_ROLE where name=? " +
				"and sid=t.rolesid)", new Object[]{usersid, roleName}).isEmpty();
		SpringUtils.commitTransaction(status);
		return result;
	}
}





