package antelope.wcm.assets.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mvel2.ast.ForEachNode;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import antelope.db.DBUtil;
import antelope.springmvc.BaseController;
import antelope.utils.JSONObject;
import antelope.utils.PageItem;
import antelope.wcm.entities.WCMAssetItem;

@Controller
public class HyperLinkAssetController extends BaseController {
	
	@RequestMapping("/assets/imgasset/HyperLinkAssetController/getSitePagesInfo")
	public void getSitePagesInfo(HttpServletRequest req, HttpServletResponse res) throws Exception {
		PageItem pageItem = DBUtil.queryJSON("select * from WCM_PAGE", new Object[0], getPageParams(req));
		print(pageItem, res);
	}
	
	@RequestMapping("/assets/imgasset/HyperLinkAssetController/getPageName")
	public void getPageName(String pagesid, HttpServletRequest req, HttpServletResponse res) throws Exception {
		WCMAssetItem asset = dao.getBy(pagesid, WCMAssetItem.class);
		String pagename = DBUtil.querySingleString("select name from WCM_PAGE where sid=?", new Object[]{new String(asset.assetdata, "utf-8")});
		print(pagename, res);
	}
	
	@RequestMapping("/assets/imgasset/HyperLinkAssetController/getHyperLinkAssets")
	public void getHyperLinkAssets(String pagesid, HttpServletRequest req, HttpServletResponse res) throws Exception {
		PageItem pageItem = dao.query("select * from WCM_ASSET where assettype='1'", WCMAssetItem.class, getPageParams(req));
		List<WCMAssetItem> currList = pageItem.getCurrList();
		List<JSONObject> objs = new ArrayList<JSONObject>();
		for (WCMAssetItem wcmAssetItem : currList) {
			JSONObject obj = new JSONObject(wcmAssetItem);
			String pagename = DBUtil.querySingleString("select name from WCM_PAGE where sid=?", new Object[]{new String(wcmAssetItem.assetdata, "utf-8")});
			obj.put("assetdata", pagename);
			objs.add(obj);
		}
		pageItem.setCurrList(objs);
		print(pageItem, res);
	}
}
