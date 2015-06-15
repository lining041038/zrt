package antelope.controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import antelope.consts.GlobalConsts;
import antelope.db.DBUtil;
import antelope.entities.SysRole;
import antelope.entities.SysUser;
import antelope.events.OrgUserAddOrUpdateEvent;
import antelope.interfaces.components.TreeDatagrid;
import antelope.interfaces.components.supportclasses.Button;
import antelope.interfaces.components.supportclasses.GridColumn;
import antelope.interfaces.components.supportclasses.TreeDatagridOptions;
import antelope.listeners.OrgUserAddOrUpdateListener;
import antelope.springmvc.SpringUtils;
import antelope.springmvc.SqlWhere;
import antelope.utils.I18n;
import antelope.utils.JSONArray;
import antelope.utils.JSONObject;
import antelope.utils.PageItem;

@Controller("usermanage")
@RequestMapping("/system/usermanage/usermanage/UserManageController")
public class UserManageController extends TreeDatagrid {

	@Override
	public TreeDatagridOptions getOptions(HttpServletRequest req) {
		I18n i18n = getI18n(req);
		TreeDatagridOptions opts = new TreeDatagridOptions(this);
		opts.columns.put("name", new GridColumn(i18n.get("antelope.user"), null));
		opts.columns.put("username", new GridColumn(i18n.get("antelope.username"), null));
		opts.columns.put("rolenames", new GridColumn(i18n.get("antelope.role"), null));
		
		opts.buttons.get("del").visibleFunction = "checkDelVisible";

		Button button = new Button("tosetAuthoritys");
		button.toolTip = "配置权限";
		button.visibleFunction = "configVisibleFunc";
		opts.buttons.put("i_config", button);
		
		opts.queryfields = new String[]{"name"};
		opts.showTreeNodeCreateBtn = false;
		opts.showTreeNodeDeleteBtn = false;
		opts.showTreeNodeUpdateBtn = false;
		opts.alertNeedSelectTreeNode = false;
		opts.locatePath = new String[]{GlobalConsts.TREE_ROOT};
		
		opts.selectionMode = "multipleRows";
		
		opts.showBatchDeleteBtn = true;
		
		return opts;
	}
	
	@Override
	public void getSingleGridList(String name, HttpServletRequest req, HttpServletResponse res) throws SQLException, Exception {
		SqlWhere sqlwhere = tidySqlWhere(new String[]{"name like ?"}, new Object[]{"%" + d(name) + "%"}, true);
		
		PageItem<JSONObject> pageItem = null;
		
		if (GlobalConsts.TREE_ROOT.equals(req.getParameter("treenode_sid"))) {
			pageItem = DBUtil.queryJSON("select t.* from SYS_USER t where 1=1 " + sqlwhere.wherePart, sqlwhere.outParams, getPageParams(req));
		} else {
			sqlwhere.outParams.add(0, req.getParameter("treenode_sid"));
			pageItem = DBUtil.queryJSON("select * from SYS_USER t inner join SYS_USER_ROLE_RELATE t2 on t.sid = t2.usersid and t2.rolesid=?" + sqlwhere.wherePart, sqlwhere.outParams, getPageParams(req));
		}
		
		// 查询单页用户所拥有的角色
		List<JSONObject> currList = pageItem.getCurrList();
		for (JSONObject jsonobj : currList) {
			List<String> rolesids = DBUtil.queryStrings("select rolesid from SYS_USER_ROLE_RELATE where usersid=?", jsonobj.get("sid"));
			jsonobj.put("rolesids", rolesids);
			List<SysRole> roles = userroleorg.getRolesByUsersid(jsonobj.getString("sid"));
			List<String> rolenames = new ArrayList<String>();
			for (SysRole sysRole : roles) {
				rolenames.add(sysRole.name);
			}
			jsonobj.put("rolenames", join(",", rolenames));
		}
		
		print(pageItem, res);
	}
	
	/**
	 * 获取根据父节点sid子节点信息
	 * @param sid 父节点sid
	 */
	public void getChildren(String sid, HttpServletRequest req, HttpServletResponse res) throws SQLException, Exception {
		I18n i18n = getI18n(req);
		if (!stringSet(sid)) {
			print("[{id:'" + GlobalConsts.TREE_ROOT + "', open:true, sid:'" + GlobalConsts.TREE_ROOT + "', name:\"" + i18n.get("antelope.allpeople") + "\", isParent:true}]", res);
		} else {
			JSONArray arr = toJSONArray(DBUtil.queryJSON("select * from SYS_ROLE"));
			print(arr, res);
		}
	}
	
	@RequestMapping("/getAllRoles")
	public void getAllRoles(HttpServletRequest req, HttpServletResponse res) throws IOException, SQLException, Exception {
		List<JSONObject> roles = DBUtil.queryJSON("select * from SYS_ROLE");
		for (JSONObject jsonObject : roles) {
			jsonObject.put("label", jsonObject.getString("name"));
			jsonObject.put("value", jsonObject.getString("sid"));
		}
		print(roles, res);
	}
	
	/**
	 * 删除列表项 
	 * @param sid
	 */
	public void deleteOneLine(String sid, HttpServletRequest req, HttpServletResponse res) throws SQLException, Exception {
		I18n i18n = getI18n(req);
		String[] sids = req.getParameterValues("sid");
		if (sids != null) {
			for (String string : sids) {
				if ("1234".equals(string)) {
					try {
						print(i18n.get("antelope.admincannotdel"), res);
					} catch (IOException e) {
					}
					return;
				}
			}
			
			String errinfo = userroleorg.batchDeleteUserCompletely(sids, req);
			if (stringSet(errinfo)) {
				print(errinfo, res);
			}
		}
	}
	
	@RequestMapping("/setRoleSidForUsers")
	public void setRoleSidForUsers(String usersids, String userrolesid, HttpServletRequest req, HttpServletResponse res) throws SQLException, Exception {
		if (!stringSet(usersids))
			return;
		usersids = d(usersids);
		String[] usersidarr = usersids.split(",");
		for (String usersid : usersidarr) {
			userroleorg.removeAllUserRoleRelate(usersid);
			userroleorg.addUserRoleRelate(usersid, userrolesid);
		}
	}
	
	@Override
	public void batchDeleteLines(String sids, HttpServletRequest req, HttpServletResponse res) throws SQLException, Exception {
		if (!stringSet(sids))
			return;
		
		String[] sidarr = sids.split(",");
		for (String sid : sidarr) {
			if ("1234".equals(sid)) {
				print(getI18n(req).get("antelope.admincannotdel"), res);
				return;
			}
		}
		
		String errinfo = userroleorg.batchDeleteUserCompletely(sidarr, req);
		if (stringSet(errinfo)) {
			print(errinfo, res);
		}
	}
	
	/**
	 * 添加或修改右侧列表项
	 * @param treenode_sid 左侧树节点sid
	 * @param req
	 * @param res
	 */
	@Override
	public void addOrUpdateOne(String sid, HttpServletRequest req, HttpServletResponse res) throws Exception {
		List<SysUser> items = null;
		try {
			items = DBUtil.queryEntities("select * from SYS_USER", new Object[0], SysUser.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		SysUser item = items.get(0);
		if (item == null) {
			item = newInstanceWithCreateInfo(SysUser.class, req);
		}
			
		if ("1234".equals(sid)) {// 超级管理员admin的系统管理员角色不允许删除
			dao.updateBySQL("delete from SYS_USER_ROLE_RELATE where usersid=? and rolesid <> '153'", new Object[]{sid});
		} else {
			dao.updateBySQL("delete from SYS_USER_ROLE_RELATE where usersid=?", new Object[]{sid});
		}
		
		String[] rolesids = req.getParameterValues("rolesid");
		if (rolesids != null) {
			for (String rolesid : rolesids) {
				dao.updateBySQL("insert into SYS_USER_ROLE_RELATE(usersid, rolesid) values(?,?)", new Object[]{sid, rolesid});
			}
		}
			
		wrapToEntity(req, item);
		String i18nstr = userroleorg.addSysUser(req.getParameter("passwordchanged"), item, "orgroot");
		if (stringSet(i18nstr))
			print(getI18n(req).get(i18nstr), res);
		
		
		// 用户添加完成之后触发添加完成事件
		List<OrgUserAddOrUpdateListener> listeners = SpringUtils.getBeans(OrgUserAddOrUpdateListener.class);
		for (OrgUserAddOrUpdateListener listener : listeners) {
			OrgUserAddOrUpdateEvent event = new OrgUserAddOrUpdateEvent();
			event.req = req;
			listener.afterAddOrUpdated(event);
		}
	}
}





