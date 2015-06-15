package antelope.wcm.controllers;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import antelope.consts.GlobalConsts;
import antelope.db.DBUtil;
import antelope.interfaces.components.TreeDatagrid;
import antelope.interfaces.components.supportclasses.Button;
import antelope.interfaces.components.supportclasses.GridColumn;
import antelope.interfaces.components.supportclasses.TreeDatagridOptions;
import antelope.springmvc.SqlWhere;
import antelope.utils.JSONArray;
import antelope.utils.JSONException;
import antelope.utils.JSONObject;
import antelope.utils.PageItem;
import antelope.utils.ReturnObject;
import antelope.wcm.beans.WCMTemplateInfoBean;
import antelope.wcm.consts.WCMSiteSettingConsts;
import antelope.wcm.entities.WCMContainerData;
import antelope.wcm.entities.WCMPageItem;
import antelope.wcm.entities.WCMSiteItem;
import antelope.wcm.entities.WCMSiteTemplateSettingItem;
import antelope.wcm.services.TemplateInfoService;

/**
 * 站点管理界面
 * @author lining
 * @since 2013-3-14
 */
@Controller("sitemanage")
@RequestMapping("/wcm/management/sitemanage/SiteManageController")
public class SiteManageController extends TreeDatagrid {
	
	@Resource
	private TemplateInfoService templateService;
	
	@Override
	public TreeDatagridOptions getOptions(HttpServletRequest req) {
		//I18n i18n = getI18n(req);
		TreeDatagridOptions opts = new TreeDatagridOptions(this);
		opts.columns.put("name", new GridColumn("名称", "70%"));
		opts.queryfields = new String[]{"name"};
		opts.treeRootEditable = false;
		
		opts.treeButtons.get("ui-icon-plusthick").click = "createSiteTreeNode";
		opts.treeButtons.get("ui-icon-pencil treeeditbtn").click = "updateSiteTreeNode";
		opts.treeButtons.put("ui-icon-zoomout", new Button("previewSite", "预览站点"));
		opts.treeButtons.put("ui-icon-home", new Button("activatedSite", "激活或取消激活站点"));
		
		return opts;
	}
	
	@Override
	public void getSingleGridList(String name, HttpServletRequest req, HttpServletResponse res) throws SQLException, Exception {
		SqlWhere sqlwhere = tidySqlWhere(new String[]{"name like ?"}, new Object[]{"%" + d(name) + "%"}, true);
		sqlwhere.outParams.add(0, req.getParameter("treenode_sid"));
		PageItem json = DBUtil.queryJSON("select * from WCM_PAGE where sitesid=? " + sqlwhere.wherePart, sqlwhere.outParams, getPageParams(req));
		print(json, res);
	}
	
	/**
	 * 激活网站，若某网站呈现激活状态，则系统登录时将转向激活状态的网站，若没有任何网站呈现激活状态，则转向原始登录页面
	 * @param sitesid 待激活网站的sid
	 */
	@RequestMapping("/activatedSite")
	public void activatedSite(String sitesid, HttpServletRequest req, HttpServletResponse res) throws SQLException, Exception {
		WCMSiteItem siteitem = dao.getBy(sitesid, WCMSiteItem.class);
		if ("0".equals(siteitem.activated)) {
			siteitem.activated = "1";
		} else {
			siteitem.activated = "0";
		}
		dao.insertOrUpdate(siteitem);
		print(new JSONObject(ReturnObject.createSuccessObj(siteitem.activated), true), res);
	}
	
	/**
	 * 获取根据父节点sid子节点信息
	 * @param sid 父节点sid
	 */
	public void getChildren(String sid, HttpServletRequest req, HttpServletResponse res) throws SQLException, Exception {
		if (!stringSet(sid)) {
			print("[{id:'" + GlobalConsts.TREE_ROOT + "', icon: '" + req.getContextPath() + "/themes/defaults/assets/sim/2.png', open:true, sid:'" + GlobalConsts.TREE_ROOT + "', name:\"所有站点\", isParent:true}]", res);
		} else {
			JSONArray arr = toJSONArray(DBUtil.queryJSON("select * from WCM_SITE"));
			for (int i = 0; i < arr.length(); ++i) {
				if ("1".equals(arr.getJSONObject(i).getString("activated")))
					arr.getJSONObject(i).put("icon", req.getContextPath() + "/antelope/themes/base/assets/runningicon.png");
				else
					arr.getJSONObject(i).put("icon", req.getContextPath() + "/antelope/themes/base/assets/stoppedicon.png");
			}
			print(arr, res);
		}
	}
	
	/**
	 * 保存所有该站点设置
	 * @param sitesid 站点sid
	 */
	@RequestMapping("/addOrUpdateSiteSettings")
	public void addOrUpdateSiteSettings(String sid, HttpServletRequest req, HttpServletResponse res) throws SQLException, Exception {
		WCMSiteItem item = dao.getBy(sid, WCMSiteItem.class);
		if (item == null)
			item = newInstanceWithCreateInfo(WCMSiteItem.class, req);
		wrapToEntity(req, item);
		
		Field[] allfields = WCMSiteSettingConsts.class.getFields();
		for (Field field : allfields) {
			if ("java.lang.String".equals(field.getType().getName())) {
				String val = d(req.getParameter(field.get(null) + ""));
				templateService.addOrUpdateSetting(sid, field.get(null) + "", val);
			}
		}
		
		dao.insertOrUpdate(item);
		fileupload.setPermanent(getFileUploadParams(req));
	}
	
	/**
	 * 获取所有该站点设置
	 * @param sitesid
	 */
	@RequestMapping("/getAllSiteSettings")
	public void getAllSiteSettings(String sitesid, HttpServletRequest req, HttpServletResponse res) throws SQLException, Exception {
		WCMSiteItem settingitem = dao.getBy(sitesid, WCMSiteItem.class);
		List<WCMSiteTemplateSettingItem> setting = dao.query("select * from WCM_SITE_TEMPLATE_SETTING where sitesid=?", new Object[]{sitesid}, WCMSiteTemplateSettingItem.class);;
		JSONObject resultobj = new JSONObject(settingitem, true);
		for (WCMSiteTemplateSettingItem wcmSiteTemplateSettingItem : setting) {
			resultobj.put(wcmSiteTemplateSettingItem.settingcode, wcmSiteTemplateSettingItem.settingvalue);
		}
		print(resultobj, res);
	};
	
	/**
	 * 获取模板路径，不包含index.jsp
	 * @param templatesid
	 */
	@RequestMapping("/getTemplatePath")
	public void getTemplatePath(String templatesid, HttpServletRequest req, HttpServletResponse res) throws SQLException, Exception {
		WCMTemplateInfoBean info = templateService.getTemplateInfoBySid(templatesid);
		print(info.path, res);
	}
	
	@Override
	public void batchDeleteLines(String sids, HttpServletRequest req, HttpServletResponse res) throws InstantiationException, IllegalAccessException {
		dao.updateBySQL("delete from WCM_PAGE where sid in (" + createQuestionMarksStr(sids.split(",").length) + ")", sids.split(","));
	}
	
	/**
	 * 树节点添加修改方法
	 * @param sid 父节点sid
	 */
	public void addOrUpdateOneTreeNode(String sid, HttpServletRequest req, HttpServletResponse res) throws Exception {
		WCMSiteItem item = dao.getBy(sid, WCMSiteItem.class);
		if (item == null)
			item = newInstanceWithCreateInfo(WCMSiteItem.class, req);
		wrapToEntity(req, item);
		dao.insertOrUpdate(item);
	}
	
	/**
	 * 树节点添加修改方法
	 * @param sid 父节点sid
	 */
	public void deleteOneTreeNode(String sid, HttpServletRequest req, HttpServletResponse res) throws Exception {
		if (DBUtil.queryCount("select count(*) from WCM_PAGE where sitesid=?", sid) > 0) {
			print("此站点还有页面，不允许删除！", res);
			return;
		}
		dao.deleteBy(sid, WCMSiteItem.class);
	}
	
	/**
	 * 删除列表项 
	 * @param sid
	 */
	public void deleteOneLine(String sid, HttpServletRequest req, HttpServletResponse res) throws InstantiationException, IllegalAccessException {
		String[] sids = req.getParameterValues("sid");
		if (sids != null)
			dao.updateBySQL("delete from WCM_PAGE where sid in(" + createQuestionMarksStr(sids.length) + ")", sids);
	}
	
	/**
	 * 添加或修改
	 * @param treenode_sid 左侧树节点sid
	 * @param req
	 * @param res
	 */
	@Override
	public void addOrUpdateOne(String sid, HttpServletRequest req, HttpServletResponse res) throws Exception {
		WCMPageItem item = dao.getBy(sid, WCMPageItem.class);
		if (item == null)
			item = newInstanceWithCreateInfo(WCMPageItem.class, req);
		item.sitesid = req.getParameter("treenode_sid");
		wrapToEntity(req, item);
		dao.insertOrUpdate(item);
	}
	
	
	/**
	 * 保存插入到页面中的数据
	 */
	@RequestMapping("/saveInsertedData")
	public void saveInsertedData(String sid, HttpServletRequest req, HttpServletResponse res) throws JSONException, IllegalArgumentException, InstantiationException, IllegalAccessException, SQLException, InvocationTargetException {
		String inserteddatas = req.getParameter("inserteddatas");
		JSONArray arr = new JSONArray(d(inserteddatas));
		dao.updateBySQL("delete from WCM_CONTAINER_DATA where pagesid=?", req.getParameter("pagesid"));
		List<WCMContainerData> beans = arr.toBeans(WCMContainerData.class);
		dao.batchInsertOrUpdate(beans);
	}
	
	/**
	 * 获取插入到模板中的数据
	 */
	@RequestMapping("/getInsertedData")
	public void getInsertedData(String pagesid, HttpServletRequest req, HttpServletResponse res) throws Exception {
		JSONObject containersidassetmap = new JSONObject();
		List<JSONObject> pagedatas = DBUtil.queryJSON("select t.*, t2.assettype from WCM_CONTAINER_DATA t left join WCM_ASSET t2 on t.assetsid = t2.sid where t.pagesid=?", new Object[]{pagesid});
		for (JSONObject wcmContainerData : pagedatas) {
			containersidassetmap.put(wcmContainerData.getString("containersid"), wcmContainerData);
		}
		print(containersidassetmap, res);
	}
	
	/******
	 * 持久化幻灯片图片
	 * @param req
	 * @param res
	 */
	@RequestMapping("/permanentfiles")
	public void setFilePermanent(HttpServletRequest req, HttpServletResponse res) {
		fileupload.setPermanent(getFileUploadParams(req));
	}
}






