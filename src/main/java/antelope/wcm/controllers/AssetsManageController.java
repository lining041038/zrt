package antelope.wcm.controllers;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import antelope.db.DBUtil;
import antelope.interfaces.components.SingleDatagrid;
import antelope.interfaces.components.supportclasses.GridColumn;
import antelope.interfaces.components.supportclasses.SingleDatagridOptions;
import antelope.springmvc.SpringUtils;
import antelope.springmvc.SqlWhere;
import antelope.utils.JSONObject;
import antelope.utils.PageItem;
import antelope.wcm.assets.BaseAsset;
import antelope.wcm.entities.WCMAssetItem;

/**
 * 单列表增删改全功能
 * @author lining
 * @since 2012-7-14
 */
@Controller("assetsmanage")
@RequestMapping("/wcm/management/assetsmanage/AssetsManageController")
public class AssetsManageController extends SingleDatagrid {
	
	@Override
	public SingleDatagridOptions getOptions(HttpServletRequest req) {
		SingleDatagridOptions opts = new SingleDatagridOptions(this);
		opts.queryfields = new String[]{"name"};
		opts.columns.put("name", new GridColumn("资源名称", "30%"));
		opts.columns.put("assettype", new GridColumn("资源类型", "30%"));
		return opts;
	}
	
	public void getSingleGridList(String name, HttpServletRequest req, HttpServletResponse res) throws SQLException, Exception {
		SqlWhere sqlwhere = tidySqlWhere(new String[]{"name like ?"}, new Object[]{"%" + decodeAndTrim(name) + "%"});
		PageItem pageItem = DBUtil.queryJSON("select sid, name, assettype from WCM_ASSET where 1=1 " + sqlwhere.wherePart, sqlwhere.outParams, getPageParams(req));
		
		Map<String, String> assettypeNameMap = new HashMap<String, String>();
		List<BaseAsset> beans = SpringUtils.getBeans(BaseAsset.class);
		for (BaseAsset baseAsset : beans) {
			assettypeNameMap.put(baseAsset.getAssetType(), baseAsset.getAssetTypeName());
		}
		List<JSONObject> currList = pageItem.getCurrList();
		for (JSONObject object : currList) {
			object.put("assettype", assettypeNameMap.get(object.getString("assettype")));
		}
		
		print(pageItem, res);
	}
	
	public void deleteOneLine(String sid, HttpServletRequest req, HttpServletResponse res) throws InstantiationException, IllegalAccessException {
		dao.deleteBy(sid, WCMAssetItem.class);
	}
	
	@Override
	public void batchDeleteLines(String sids, HttpServletRequest req, HttpServletResponse res) throws InstantiationException, IllegalAccessException {
		dao.updateBySQL("delete from WCM_ASSET where sid in (" + createQuestionMarksStr(sids.split(",").length) + ")", sids.split(","));
	}
	
	public void addOrUpdateOne(String sid, HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		WCMAssetItem item = dao.getBy(sid, WCMAssetItem.class);
		if (item == null)
			item = newInstanceWithCreateInfo(WCMAssetItem.class, req);
		wrapToEntity(req, item);
		List<BaseAsset> beans = SpringUtils.getBeans(BaseAsset.class);
		BaseAsset theBaseAsset = null;
		for (BaseAsset baseAsset : beans) {
			if (baseAsset.getAssetType().equals(item.assettype)) {
				theBaseAsset = baseAsset;
				break;
			}
		}
		
		theBaseAsset.saveAsset(item, req.getParameter("assetdatasymbol"), req, res);
		dao.insertOrUpdate(item);
	}
	
	@RequestMapping("/getImageData")
	public void getImageData(String sid, HttpServletRequest req, HttpServletResponse res) throws Exception {
		WCMAssetItem item = dao.getBy(sid, WCMAssetItem.class);
		printBytes(item.assetdata, res);
	}
	
}
