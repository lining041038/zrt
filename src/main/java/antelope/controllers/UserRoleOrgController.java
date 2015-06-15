package antelope.controllers;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.DocumentException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import antelope.db.DBUtil;
import antelope.db.DBUtil.DataType;
import antelope.db.Sql;
import antelope.entities.SysRole;
import antelope.entities.SysUnit;
import antelope.entities.SysUser;
import antelope.events.OrgUserAddOrUpdateEvent;
import antelope.interfaces.SystemInfoDeleteValidator;
import antelope.listeners.OrgUserAddOrUpdateListener;
import antelope.services.UserRoleOrgService;
import antelope.springmvc.BaseController;
import antelope.springmvc.SpringUtils;
import antelope.system.SystemCache;
import antelope.utils.I18n;
import antelope.utils.JSONArray;
import antelope.utils.JSONException;
import antelope.utils.JSONObject;
import antelope.utils.MenuUtil;
import antelope.utils.PageItem;
import antelope.utils.SpeedIDUtil;
import antelope.utils.TextUtils;

/**
 * 用户角色组织机构
 * @author lining
 *
 */
@Controller
public class UserRoleOrgController extends BaseController {
	
	@Resource
	private UserRoleOrgService service;
	
	@Resource
	private SystemCache syscache;
	
	/**
	 * modified by xcc
	   added opts{filterdept：false }根据登录用户所属部门过滤组织机构,即只显示本部门人员及子部门
	   added opts{deptheader:false}向上找部门负责人，如果没有，则整个组织机构人员
	 */
	/**
	 * 获取组织机构树相关数据
	 * @param res
	 * @throws Exception 
	 */
	@RequestMapping("/common/getOrgnizationTreedata")
	public void getOrgnizationTreedata(HttpServletRequest req,HttpServletResponse res) throws Exception {
	     boolean filterdept=Boolean.valueOf(decodeAndTrim(req.getParameter("filterdept"))).booleanValue();
	     boolean deptheader=Boolean.valueOf(decodeAndTrim(req.getParameter("deptheader"))).booleanValue();
	     String deptSid=getService(req).getDeptsid();
		if(filterdept){
			String	unitjsoncache = appendChildNodes(deptSid).toString(); 
			getOut(res).print(unitjsoncache);
		}else if(deptheader){
		//部门负责人作为根节点-->role
		}else{
			getOut(res).print(syscache.getUnitsJson());
		}
	}
	/**
	 * 异步获取组织机构树相关数据
	 * @param res
	 * @throws Exception 
	 */
	@RequestMapping("/common/UserRoleOrgController/getAsyncOrgnizationTreedata")
	public void getAsyncOrgnizationTreedata(String sid, HttpServletRequest req,HttpServletResponse res) throws Exception {
		I18n i18n = getI18n(req);
		
		if (!stringSet(sid)) {
			print("[{id:'root', open:true, sid:'orgroot', name:\"" + i18n.get("antelope.orgnizationroot") + "\", isParent:true}]", res);
		} else {
			JSONArray arr = getDirectChildrendata(sid);
			print(arr, res);
		}
	}

	
	/**
	 * 异步获取组织机构树相关数据(过滤本部门问根节点)
	 * @param res
	 * @throws Exception 
	 */
	@RequestMapping("/common/UserRoleOrgController/getAsyncOrgnizationOrDeptTreedata")
	public void getAsyncOrgnizationOrDeptTreedata(String sid,boolean filterdept, HttpServletRequest req,HttpServletResponse res) throws Exception {
		I18n i18n = getI18n(req);
		if (!stringSet(sid)) {
			if(!filterdept){
				print("[{id:'root', open:true, sid:'orgroot', name:\"" + i18n.get("antelope.orgnizationroot") + "\", isParent:true}]", res);
			}else{
				String deptSidStr=getService(req).getDeptsid();
				String deptNameStr=getService(req).getDeptname();
				print("[{id:'root', open:true, sid:'"+deptSidStr+"', name:\""+deptNameStr+"\", isParent:true}]", res);
			}
		} else {
			JSONArray arr = getDirectChildrendata(sid);
			print(arr, res);
		}
	}
	
	/**
	 * 获取当前登录人单位机构路径 JSON数组形式
	 */
	@RequestMapping("/common/UserRoleOrgController/getCurrentUsersDeptsPath")
	public void getCurrentUsersDeptsPath(HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		List<SysUnit> units = userroleorg.getUnitsByUsersid(getService(req).getUsersid());
		if (units.size() > 0) {
			List<String> list = new ArrayList<String>();
			SysUnit unit = units.get(0);
			if (unit.sid == null) {
				list.add("orgroot");
			} else {
				while(unit != null) {
					if (unit.sid == null) 
						list.add(0, "orgroot");
					else
						list.add(0, unit.sid);
					if (unit.parentsid == null)
						unit = null;
					else
						unit = dao.getBy(unit.parentsid, SysUnit.class);
				}
				list.add(0, "orgroot");
			}
			print(new JSONArray(list), res);
		} else {
			print("null", res);
		}
	}

	/**
	 * 获取单位直接下级单位信息json数据
	 */
	private JSONArray getDirectChildrendata(String sid) throws Exception,
			JSONException {
		List<SysUnit> units = service.getUnitsByParentSid(sid);
		JSONArray arr = new JSONArray(units, true);
		for(int i = 0; i < arr.length(); i++) {
			List<SysUnit> childunits = service.getUnitsByParentSid(units.get(i).sid);
			if (!childunits.isEmpty())
				arr.getJSONObject(i).put("isParent", true);
		}
		tidyTreeData(arr);
		return arr;
	}
	
	/**
	 * 获取单位直接下级数据
	 */
	@RequestMapping("/common/UserRoleOrgController/getDirectUnitChildren")
	public void getDirectUnitChildren(String sid, HttpServletResponse res) throws JSONException, Exception {
		JSONArray arr = getDirectChildrendata(sid);
		print(arr, res);
	}
	
	/**
	 * 新增或修改单位（组织机构)
	 */
	@RequestMapping("/common/insertOrUpdateUnit")
	public void insertOrUpdateUnit(String sid, HttpServletRequest req, HttpServletResponse res) throws Exception {
		SysUnit unit = dao.getBy(sid, SysUnit.class);
		if (unit == null) {
			unit = new SysUnit();
			wrapToEntity(req, unit);
			unit.sid = SpeedIDUtil.getId();
			String code = TextUtils.getFullPingyinByChineseNotIgnore(unit.name);
			
			// 去重复
			int num = 0;
			String finalcode = code;
			while(((Integer) DBUtil.querySingleVal("select count(1) ct from SYS_UNIT where code=?", finalcode, DataType.Integer)) != 0) {
				finalcode = code + (num++);
			}
			unit.code = finalcode;
		} else {
			wrapToEntity(req, unit);
		}
		dao.insertOrUpdate(unit);
		syscache.invalidateUnitsJson();
	}
	
	/**
	 * 新增或修改用户,此新增修改非自注册用户
	 * @throws Exception 
	 */
	@RequestMapping("/common/insertOrUpdateUser")
	public void insertOrUpdateUser(String passwordchanged, HttpServletRequest req, HttpServletResponse res) throws Exception {
		I18n i18n = getI18n(req);
		SysUser user = wrapToEntity(req, SysUser.class);
		
		
		boolean isadd = false;
		
		if (user.sid != null) {
			user = dao.getBy(user.sid, SysUser.class);
			if (user == null) {
				isadd = true;
				user = wrapToEntity(req, SysUser.class);
				putCreateInfo(user, req);
			} else {
				wrapToEntity(req, user);
			}
		}
		
		String unitsid = decodeAndTrim(req.getParameter("unitsid"));
		String retval = service.addSysUser(passwordchanged, user, unitsid);
		retval = i18n.get(retval);
		if (stringSet(retval)) {
			print(retval, res);
		}
		
		// 用户添加完成之后触发添加完成事件
		List<OrgUserAddOrUpdateListener> listeners = SpringUtils.getBeans(OrgUserAddOrUpdateListener.class);
		for (OrgUserAddOrUpdateListener listener : listeners) {
			OrgUserAddOrUpdateEvent event = new OrgUserAddOrUpdateEvent();
			event.req = req;
			listener.afterAddOrUpdated(event);
		}
		
	}

	/**
	 * 新用户进行系统自注册，以使用系统的一些功能
	 */
	@RequestMapping("/common/addRegisterUser")
	public void addRegisterUser(HttpServletRequest req, HttpServletResponse res) throws Exception {
		I18n i18n = getI18n(req);
		SysUser user = wrapToEntity(req, SysUser.class);
		putCreateInfo(user, req);
		String retval = service.addSelfRegisterSysUser(user, i18n.get("antelope.sysmanage.userregisterunit"), i18n.get("antelope.sysmanage.userregisterrole"));
		retval = i18n.get(retval);
		if (stringSet(retval)) {
			print(retval, res);
		} 
	}
	
	/**
	 * 删除用户
	 */
	@RequestMapping("/common/deleteuser")
	public void deleteUser(HttpServletRequest req, HttpServletResponse res) throws Exception {
		JSONArray arr = new JSONArray(decodeAndTrim(req.getParameter("users")));
		
		// 系统管理员不允许删除
		for (int i = 0; i < arr.length(); ++i) {
			if ("1234".equals(arr.getJSONObject(i).getString("sid"))) {
				print("系统管理员不允许删除！", res);
				return;
			}
		}
		
		// 对删除用户进行子模块全局验证
		List<String> usersids = new ArrayList<String>();
		for (int i = 0; i < arr.length(); i++) {
			usersids.add(arr.getJSONObject(i).getString("sid"));
		}
		
		// 提前验证
		List<SystemInfoDeleteValidator> validators = SpringUtils.getBeans(SystemInfoDeleteValidator.class);
		for (SystemInfoDeleteValidator systemInfoDeleteValidator : validators) {
			for (int i = 0; i < usersids.size(); i++) {
				String usersid = usersids.get(i);
				List<SysUnit> units = service.getUnitsByUsersid(usersid);
				if (units.size() > 1)
					continue;
				String retval = systemInfoDeleteValidator.validateDeleteSysUser(usersid, req);
				if (stringSet(retval)) {
					print(retval, res);
					return;
				}
			}
		}
		
		for (int i = 0; i < usersids.size(); i++) {
			String usersid = usersids.get(i);
			
			List<SysUnit> units = service.getUnitsByUsersid(usersid);
			
			if (units.size() > 1) {
				dao.updateBySQL("delete from SYS_USER_UNIT_RELATE where usersid=? and unitsid=?", new Object[]{usersid,arr.getJSONObject(i).getString("unitsid")});
			} else {
				service.deleteUserCompletely(usersid, req);
			}
		}
	}
	
	/**
	 * 删除组织机构
	 * @throws Exception 
	 */
	@RequestMapping("/common/deleteunit")
	public void deleteorganization(String unitsid, HttpServletRequest req, HttpServletResponse res) throws Exception {
		I18n i18n = getI18n(req);
		List<SysUnit> units = service.getUnitsByParentSid(unitsid);
		if (!units.isEmpty()) {
			getOut(res).print(i18n.get("antelope.cannotdeletehassub"));
			return;
		}
		List<SysUser> users = service.getUsersByUnitSid(unitsid);
		if (!users.isEmpty()) {
			getOut(res).print(i18n.get("antelope.cannotdelforuserinfo"));
			return;
		}
		
		dao.deleteBy(unitsid, SysUnit.class);
	}
	
	/**
	 * 获取所有角色
	 * @throws Exception 
	 */
	@RequestMapping("/common/getAllRoles")
	public void getAllRoles(HttpServletResponse res) throws Exception {
		List<SysRole> allroles = dao.getAll(SysRole.class);
		getOut(res).print(toJSONArrayBy(allroles));
	}
	
	/**
	 * 根据逗号分割的用户sid字符串获取用户信息
	 * @param selectedusers
	 * @param res
	 * @throws Exception 
	 */
	@RequestMapping("/common/getSelectedUserInfos")
	public void getSelectedUserInfos(String isusername, String usersids, HttpServletResponse res) throws Exception {
		usersids = decodeAndTrim(usersids);
		JSONArray arr = new JSONArray(usersids);
		if (!stringSet(usersids)) {
			return;
		}
		List<SysUser> users = dao.query("select * from SYS_USER where "+("true".equals(isusername)?"username":"sid")+" in ('" + arr.join("','").replaceAll("\"", "") + "')", SysUser.class);
		getOut(res).print(toJSONArrayBy(users));
	}
	
	/**
	 * 保存角色用户关联关系
	 * @throws JSONException 
	 */
	@RequestMapping("/common/saveroleusers")
	public void saveroleusers(String rolesid, String usersids, HttpServletResponse res) throws JSONException {
		usersids = decodeAndTrim(usersids);
		JSONArray userids = new JSONArray(usersids);
		service.removeAllUserRoleRelateByRolesid(rolesid);
		for(int i = 0; i < userids.length(); i++) {
			service.addUserRoleRelate(userids.getString(i), rolesid);
		}
		
		SystemCache.isuserroleinvalidate = true;
	}
	
	/**
	 * 删除角色用户关联关系
	 * @throws JSONException 
	 */
	@RequestMapping("/common/delroleusers")
	public void delroleusers(String rolesid, String usersids, HttpServletResponse res) throws JSONException {
		usersids = decodeAndTrim(usersids);
		JSONArray userids = new JSONArray(usersids);
		for(int i = 0; i < userids.length(); i++) {
			service.removeUserRoleRelate(userids.getString(i), rolesid);
		}
	}
	
	/**
	 * 保存用户功能权限对应关系
	 * @param users JSON表示的用户
	 * @param funcs json表示的系统功能
	 * @param res
	 */
	@RequestMapping("/common/saveUserAuthoritys")
	public void saveUserAuthoritys(String users, String funcs, HttpServletResponse res) throws SQLException, Exception {
		users = decodeAndTrim(users);
		funcs = decodeAndTrim(funcs);
		JSONArray usersarr = new JSONArray(users);
		JSONArray funcsarr = new JSONArray(funcs);
		
		Sql sql = new Sql("delete from SYS_AUTHORITY where roleorusersid=?");
		
		for (int i = 0; i < usersarr.length(); i++) {
			sql.update(new Object[]{usersarr.getJSONObject(i).getString("sid")});
		}
		
		Set<String> inserted = new HashSet<String>();
		for (int i = 0; i < usersarr.length(); i++) {
			String usersid = usersarr.getJSONObject(i).getString("sid");
			for (int j = 0; j < funcsarr.length(); j++) {
				
				if (inserted.contains(usersid + "_" + funcsarr.getString(j)))
					continue;
				dao.updateBySQL("insert into SYS_AUTHORITY(roleorusersid,functionid) values(?,?)", new Object[]{usersid, 
					funcsarr.getString(j)});
				
				inserted.add(usersid + "_" + funcsarr.getString(j));
			}
			
		}
	}
	
	/**
	 * 根据单位的sid获取单位相关信息
	 * @param unitsids
	 */
	@RequestMapping("/common/UserRoleOrgController/getUnitInfosByUnitsids")
	public void getUnitInfosByUnitsids(String unitsids, HttpServletResponse res) throws JSONException, Exception {
		unitsids = unitsids.replaceAll("(^\")|(\"$)", "");
		List<SysUnit> units = dao.query("select * from sys_unit where sid in ('"+join("','", new JSONArray(unitsids))+"')", SysUnit.class);
		print(toJSONArrayBy(units), res);
	}
	
	/**
	 * 保存角色功能权限对应关系
	 * @param users JSON表示的用户
	 * @param funcs json表示的系统功能
	 * @param res
	 * @throws SQLException 
	 * @throws JSONException 
	 */
	@RequestMapping("/common/saveRoleAuthoritys")
	public void saveRoleAuthoritys(String rolesid, String funcs, HttpServletResponse res) throws SQLException, JSONException {
		rolesid = decodeAndTrim(rolesid);
		funcs = decodeAndTrim(funcs);
		JSONArray funcsarr = new JSONArray(funcs);
		
		Sql sql = new Sql("delete from SYS_AUTHORITY where roleorusersid=?");
		sql.update(new Object[]{rolesid});
		
		Set<String> inserted = new HashSet<String>();
		
		for (int j = 0; j < funcsarr.length(); j++) {
			if (inserted.contains(rolesid + "_" + funcsarr.getString(j)))
				continue;
			dao.updateBySQL("insert into SYS_AUTHORITY(roleorusersid, functionid) values(?,?)", new Object[]{rolesid, 
				funcsarr.getString(j)});
			inserted.add(rolesid + "_" + funcsarr.getString(j));
		}
	}
	
	
	/**
	 * 将所有用户添加如某角色
	 */
	@RequestMapping("/common/UserRoleOrgController/addAllUserToRole")
	public void addAllUserToRole(String rolesid, HttpServletResponse res) throws SQLException, JSONException {
		dao.updateBySQL("delete from SYS_USER_ROLE_RELATE where rolesid=?", rolesid);
		dao.updateBySQL("insert into SYS_USER_ROLE_RELATE select sid, '" + rolesid + "' from sys_user");
	}
	
	/**
	 * 获取功能相关数据
	 * @throws Exception 
	 * @throws SQLException 
	 */
	@RequestMapping("/common/getfunctiondatas")
	public void getFunctionDatas(String usersid, HttpServletRequest req, HttpServletResponse res) throws SQLException, Exception {
		Set<String> authoredfuncs = service.getUserAuthorities(usersid);
		getFunctionDataInner(res, authoredfuncs, req);
	}
	
	/**
	 * 获取功能相关数据
	 * @throws Exception 
	 * @throws SQLException 
	 */
	@RequestMapping("/common/getfunctionroledatas")
	public void getFunctionroleDatas(String rolesid, HttpServletRequest req, HttpServletResponse res) throws SQLException, Exception {
		Set<String> authoredfuncs = service.getRoleAuthorities(rolesid);
		getFunctionDataInner(res, authoredfuncs, req);
	}

	private void getFunctionDataInner(HttpServletResponse res,
			Set<String> authoredfuncs, HttpServletRequest req) throws DocumentException, IOException,
			JSONException {
		JSONArray funcs = MenuUtil.getMenuJSON(getService(req));
		for (int i = 0; i < funcs.length(); i++) {
			JSONObject jsonObject = funcs.getJSONObject(i);
			tidyFunctionsData( jsonObject, authoredfuncs );
		}
		getOut(res).print(funcs);
	}
	
	private void tidyFunctionsData(JSONObject jsonobj, Set<String> authoredfuncs) throws JSONException {
		if (authoredfuncs.contains(jsonobj.getString("id"))) {
			jsonobj.put("checked", "checked");
		}
		jsonobj.put("name", jsonobj.get("title"));
		if (jsonobj.has("submenu")) {
			JSONArray jsonArray = jsonobj.getJSONArray("submenu");
			jsonobj.put("nodes", jsonArray);
			for (int j = 0; j < jsonArray.length(); j++) {
				JSONObject jsonObject2 = jsonArray.getJSONObject(j);
				tidyFunctionsData(jsonObject2, authoredfuncs);
			}
		}
	}
	
	
	/**
	 * 新增或修改角色
	 */
	@RequestMapping("/common/insertOrUpdateRole")
	public void insertOrUpdateRole(HttpServletRequest req, HttpServletResponse res) throws InstantiationException, IllegalAccessException, IllegalArgumentException, SQLException, InvocationTargetException {
		SysRole role = wrapToEntity(req, SysRole.class);
		if (!stringSet(role.sid)) {
			role.sid = SpeedIDUtil.getId();
		}
		dao.insertOrUpdate(role);
	}
	
	/**
	 * 删除角色
	 * @param res
	 */
	@RequestMapping("/common/deleterole")
	public void deleteRole(String rolesid, HttpServletRequest req, HttpServletResponse res) throws Exception {
		I18n i18n = getI18n(req);
		List<SysUser> users = service.getUsersByRoleSid(rolesid);
		if (!users.isEmpty()) {
			getOut(res).print(i18n.get("antelope.currrolehasuserinfo"));
			return;
		} 
		dao.deleteBy(rolesid, SysRole.class);
	}
	
	/**
	 * 根据角色id获取用户
	 * @param rolesid
	 * @param res
	 * @throws Exception 
	 */
	@RequestMapping("/common/getUsersByRolesid")
	public void getUsersByRolesid(String rolesid, HttpServletRequest req, HttpServletResponse res) throws Exception {
		// 查询人员所在单位相关信息
		PageItem resultsjson = DBUtil.queryJSON("select * from sys_user where sid in (select usersid from SYS_USER_ROLE_RELATE where rolesid=?)", 
				new Object[]{rolesid}, getPageParams(req));
		
		for(int i = 0; i < resultsjson.getCurrList().size(); i++) {
			List<SysUnit> units = userroleorg.getUnitsByUsersid(((JSONObject)resultsjson.getCurrList().get(i)).getString("sid"));
			List<String> belongtoUnits = new ArrayList<String>();
			for (SysUnit sysUnit : units) {
				belongtoUnits.add(sysUnit.name);
			}
			((JSONObject)resultsjson.getCurrList().get(i)).put("unitnames", join("，",belongtoUnits));
		}
		
		getOut(res).print(new JSONObject(resultsjson));
	}
	
	/**
	 * 根据角色id获取用户
	 * @param rolesid
	 * @param res
	 * @throws Exception 
	 */
	@RequestMapping("/common/getAllUsersByRolesid")
	public void getAllUsersByRolesid(String rolesid, HttpServletRequest req, HttpServletResponse res) throws Exception {
		// 查询人员所在单位相关信息
		List<JSONObject> json = DBUtil.queryJSON("select * from sys_user where sid in (select usersid from SYS_USER_ROLE_RELATE where rolesid=?)", new Object[]{rolesid});
		
		for(int i = 0; i < json.size(); i++) {
			List<SysUnit> units = userroleorg.getUnitsByUsersid(((JSONObject)json.get(i)).getString("sid"));
			List<String> belongtoUnits = new ArrayList<String>();
			for (SysUnit sysUnit : units) {
				belongtoUnits.add(sysUnit.name);
			}
			((JSONObject)json.get(i)).put("unitnames", join("，",belongtoUnits));
		}
		
		getOut(res).print(json);
	}
	
	/**
	 * 根据角色id获取用户
	 * @param rolesid
	 * @param res
	 * @throws Exception 
	 */
	@RequestMapping("/common/getAllUserssidsByRolesid")
	public void getAllUserssidsByRolesid(String rolesid, HttpServletRequest req, HttpServletResponse res) throws Exception {
		List<String> strings = DBUtil.queryStrings("select usersid from SYS_USER_ROLE_RELATE where rolesid=?", rolesid);
		getOut(res).print(new JSONArray(strings));
	}
	
	/**
	 * 根据角色id和单位id获取用户
	 * @param rolesid
	 * @param includeSubunits 是否包含有所有下级单位相关角色人员
	 * @param res
	 * @throws Exception 
	 */
	@RequestMapping("/common/getUsersByRolesidAndUnitsid")
	public void getUsersByRolesidAndUnitsid(String rolesid, String unitsid, HttpServletResponse res) throws Exception {
		unitsid = decodeAndTrim(unitsid);
		List<SysUser> roleusers = new ArrayList<SysUser>();
		if (stringSet(unitsid)) {
			Map<String, List<SysUser>> users = SystemCache.getUnitAndChildUnitsUsers();
			Map<String, List<SysRole>> userRoleCache = SystemCache.getUserRoleCache();
			List<SysUser> unitusers = users.get(unitsid);
			if (unitusers == null)
				unitusers = new ArrayList<SysUser>();
			for (SysUser sysUser : unitusers) {
				List<SysRole> list = userRoleCache.get(sysUser.sid);
				boolean isrole = false;
				if (list != null) {
					for (SysRole sysRole : list) {
						if (rolesid.equals(sysRole.sid)) {
							isrole = true;
							break;
						}
					}
				}
				
				if (!stringSet(rolesid)) {
					isrole = true;
				}
				
				if (isrole) {
					roleusers.add(sysUser);
				}
			}
		} else {
			roleusers = userroleorg.getUsersByRoleSid(rolesid);
		}
		
		getOut(res).print(toJSONArrayBy(roleusers));
	}
	
	/**
	 * 根据角色id获取角色详细信息
	 * @param rolesid
	 * @param res
	 * @throws Exception 
	 */
	@RequestMapping("/common/getUserByRolesid")
	public void getUserinfoByRolesid(String rolesid, HttpServletResponse res) throws Exception {
		SysRole role = dao.getBy(rolesid, SysRole.class);
		if (role != null)
			getOut(res).print(new JSONObject(role, true));
	}
	
	/**
	 * 根据单位（组织机构）sid获取用户信息
	 * @param unitsid
	 * @param res
	 * @throws Exception 
	 */
	@RequestMapping("/common/getUsersByUnitsid")
	public void getUsersByUnitsid(String unitsid, String filterrole, HttpServletRequest req, HttpServletResponse res) throws Exception {
		unitsid = decodeAndTrim(unitsid);
		String deptSid=getService(req).getDeptsid();
		if (stringSet(filterrole)) {
			filterrole = decodeAndTrim(filterrole);
			List<SysUser> result = dao.query("select t.*, t5.name parentuser "+
					"	  from sys_user t left join sys_user t5 on t.parentusersid=t5.sid"+
					"	 where t.sid in (select t2.usersid "+
					"	                   from SYS_USER_UNIT_RELATE t2 "+
					"	                  where t2.unitsid = ?) "+
					"	   and t.sid in (select t3.usersid "+
					"	                   from SYS_USER_ROLE_RELATE t3 "+
					"	                  inner join SYS_ROLE t4 "+
					"	                     on t3.rolesid = t4.sid "+
					"	                  where t4.name = ?) ", new Object[]{unitsid, filterrole}, SysUser.class);
			getOut(res).print(toJSONArrayBy(result));
		} else {
			getOut(res).print(toJSONArray(DBUtil.queryJSON("select t.*, t5.name parentuser, '" + unitsid + "' unitsid "+
					"	  from sys_user t left join sys_user t5 on t.parentusersid=t5.sid"+
					"	 where t.sid in (select t2.usersid "+
					"	                   from SYS_USER_UNIT_RELATE t2 "+
					"	                  where t2.unitsid = ?)", unitsid)));
		}
	}
	
	/**多部门集体领导管理  begin***************->*/
	
	/**
	 * 获取存在领导单位的领导人员 
	 * @param res
	 */
	@RequestMapping("/common/getMultiLeaderusers")
	public void getMultiLeaderusers(HttpServletResponse res) throws SQLException, Exception {
		List<JSONObject> result = DBUtil.queryJSON("select * "+
						 " from sys_user t "+
						" where exists (select sid from sys_unit t2 where t.sid = t2.leaderusersid)  ");
		getOut(res).print(toJSONArray(result));
	}
	
	@RequestMapping("/common/getMultiOrgLeaderOrgs")
	public void getMultiOrgLeaderOrgs(String usersid, HttpServletResponse res) throws SQLException, Exception {
		List<JSONObject> result = DBUtil.queryJSON("select sid from sys_unit where leaderusersid=?", usersid);
		getOut(res).print(result);
	}
	
	@RequestMapping("/common/saveCheckedLeaderOrgs")
	public void saveCheckedLeaderOrgs(String orgs, String usersid, HttpServletResponse res) throws JSONException {
		orgs = decodeAndTrim(orgs);
		JSONArray orgss = new JSONArray(orgs);
		dao.updateBySQL("update sys_unit set leaderusersid = '' where leaderusersid = ?", new Object[]{usersid});
		dao.updateBySQL("update sys_unit set leaderusersid=? where sid in ('" + TextUtils.join("','", orgss) + "')", new Object[]{usersid});
	}
	
	/**
	 * 删除某个领导
	 * @param res
	 */
	@RequestMapping("/common/deleteMultiOrgLeader")
	public void deleteMultiOrgLeader(String usersid, HttpServletResponse res) {
		dao.updateBySQL("update sys_unit set leaderusersid = '' where leaderusersid = ?", new Object[]{usersid});
	}
	
	/*<-*多部门集体领导管理 end**********************/
	
	
	/**
	 * 根据角色和部门获取缺省人员
	 * @param res
	 */
	@RequestMapping("/common/getDefaultDeptUserByRoleName")
	public void getDefaultDeptUserByRoleName(String roleName,HttpServletRequest req,HttpServletResponse res) throws SQLException, Exception {
		
	List<SysUser> list=	service.getUserByDeptsidAndRoleName(getService(req).getDeptsid(), decodeAndTrim(roleName));
		getOut(res).print(new JSONArray(list,true)); 
	}
	
	/**
	 * 获取当前登录人相关信息
	 * @param res
	 */
	@RequestMapping("/common/UserRoleOrgController/getLogonUserinfo")
	public void getLogonUserinfo(HttpServletRequest req, HttpServletResponse res) throws Exception {
		SysUser user = dao.getBy(getService(req).getUsersid(), SysUser.class);
		print(new JSONObject(user), res);
	}
	
	private JSONArray appendChildNodes(String parentsid) throws Exception {
		List<SysUnit> units = service.getUnitsByParentSid(parentsid);
		JSONArray arr = new JSONArray(units, true);
		tidyTreeData(arr);
		for (int i = 0; i < arr.length(); i++) {
			arr.getJSONObject(i).put("nodes", appendChildNodes(arr.getJSONObject(i).getString("sid")));
		}
		
		return arr;
	}
	
	private static void tidyTreeData(JSONArray arr) throws JSONException {
		for (int i = 0; i < arr.length(); i++) {
			JSONObject obj = arr.getJSONObject(i);
			obj.put("id", obj.get("sid"));
		}
	}
	
	/**
	 * 根据角色sid获取人员数量
	 */
	@RequestMapping("/common/UserRoleOrgController/getUsersCountByRolesid")
	public void getUsersCountByRolesid(String rolesid, HttpServletResponse res) throws SQLException, Exception {
		int ct = DBUtil.queryCount("select count(1) ct from SYS_USER_ROLE_RELATE where rolesid=?", new Object[]{rolesid});
		print(ct, res);
	}
	
	
	/**
	 * 根据用户名获取用户所关联的父级用户的信息
	 */
	@RequestMapping("/common/UserRoleOrgController/getParentUserByUsername")
	public void getParentUserByUsername(String username, HttpServletResponse res) throws Exception {
		List<SysUser> users = userroleorg.getUserByUserName(username);
		if (users.isEmpty())
			return;
		
		List<SysUser> parentusers = userroleorg.getUserBySid(users.get(0).parentusersid);
		if (parentusers.isEmpty())
			return;
		
		print(new JSONObject(parentusers.get(0)), res);
	}
	
	/**
	 * 根据用户名获取用户所关联的父级用户的信息
	 */
	@RequestMapping("/common/UserRoleOrgController/getUserByUsername")
	public void getUserByUsername(String username, HttpServletResponse res) throws Exception {
		List<SysUser> users = userroleorg.getUserByUserName(username);
		if (users.isEmpty())
			return;
		
		print(new JSONObject(users.get(0)), res);
	}
}






