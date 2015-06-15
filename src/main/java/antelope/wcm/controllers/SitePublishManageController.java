package antelope.wcm.controllers;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import antelope.db.DBUtil;
import antelope.interfaces.components.SingleDatagrid;
import antelope.interfaces.components.supportclasses.Button;
import antelope.interfaces.components.supportclasses.GridColumn;
import antelope.interfaces.components.supportclasses.SingleDatagridOptions;
import antelope.springmvc.SpringUtils;
import antelope.springmvc.SqlWhere;
import antelope.utils.ClasspathResourceUtil;
import antelope.utils.JSONObject;
import antelope.utils.PageItem;
import antelope.utils.SpeedIDUtil;
import antelope.wcm.assets.BaseAsset;
import antelope.wcm.beans.WCMTemplateInfoBean;
import antelope.wcm.entities.WCMAssetItem;
import antelope.wcm.entities.WCMContainerData;
import antelope.wcm.entities.WCMPageItem;
import antelope.wcm.entities.WCMPublishHistory;
import antelope.wcm.services.TemplateInfoService;

/**
 * 站点发布管理
 * @author lining
 * @since 2013-3-15
 */
@Controller("sitepublishmanage")
@RequestMapping("/wcm/management/sitepublishmanage/SitePublishManageController")
public class SitePublishManageController extends SingleDatagrid {
	
	@Resource
	private TemplateInfoService tmpinfoservice;

	@Override
	public SingleDatagridOptions getOptions(HttpServletRequest req) {
		SingleDatagridOptions opts = new SingleDatagridOptions(this);
		opts.showCreateBtn = false;
		opts.queryfields = new String[]{"name"};
		opts.columns.put("name", new GridColumn("站点名称", "30%"));
		opts.columns.put("createtime", new GridColumn("最后发布时间", "30%"));
		opts.buttons.clear();
		opts.buttons.put("i_release", new Button("publishSite"));
		opts.buttons.put("i_gate", new Button("viewTheSite", "进入站点"));
		return opts;
	}

	@Override
	public void getSingleGridList(String name, HttpServletRequest req, HttpServletResponse res) throws SQLException, Exception {
		SqlWhere sqlwhere = tidySqlWhere(new String[]{"name like ?"}, new Object[]{"%" + d(name) + "%"});
		PageItem pageItem = DBUtil.queryJSON(getSql("querysitepublish").toString() + sqlwhere.wherePart, sqlwhere.outParams, getPageParams(req));
		List<JSONObject> currList = pageItem.getCurrList();
		for (JSONObject jsonObject : currList) {
			if (!stringSet(jsonObject.getString("createtime"))) {
				jsonObject.put("createtime", "未发布");
			}
		}
		print(pageItem, res);
	}
	
	@RequestMapping("/getHomepagesid")
	public void getHomepagesid(String sitesid, HttpServletRequest req, HttpServletResponse res) throws Exception {
		String homepagesid = DBUtil.querySingleString("select sid from WCM_PAGE where pagetype='1' and sitesid=?", new Object[]{sitesid});
		print(noNull(homepagesid), res);
	}

	/**
	 * 将站点发布为静态页面
	 * @param sitesid 站点sid
	 */
	@RequestMapping("/doPublishTheSite")
	public void doPublishTheSite(String sitesid, HttpServletRequest req, HttpServletResponse res) throws Exception {
		File folder = ClasspathResourceUtil.getWebappFolderFile("/wcm/published/" + sitesid);
		if (!folder.exists()) {
			folder.mkdirs();
		}
		List<WCMPageItem> pageItems = dao.getBy("sitesid", sitesid, WCMPageItem.class);
		
		copyAssets(folder, pageItems);
		
		List<BaseAsset> beans = SpringUtils.getBeans(BaseAsset.class);
		
		Map<String, BaseAsset> beansmap = new HashMap<String, BaseAsset>();
		
		for (BaseAsset baseAsset : beans) {
			beansmap.put(baseAsset.getAssetType(), baseAsset);
		}
		
//		for (WCMPageItem wcmPageItem : pageItems) {
//			WCMTemplateInfoBean tmpinfo = tmpinfoservice.getTemplateInfoBySid(wcmPageItem.templatesid);
//			File tmpinfofile = new File(tmpinfo.path);
//			
//			FileInputStream fis = new FileInputStream(tmpinfofile.getAbsoluteFile() + "/template.html");
//			
//			byte[] byts = new byte[fis.available()];
//			fis.read(byts);
//			fis.close();
//			
//			String htmlstr = new String(byts, "utf-8");
//			List<WCMContainerData> containerDatas = dao.getBy("pagesid", wcmPageItem.sid, WCMContainerData.class);
//			
//			for (WCMContainerData wcmContainerData : containerDatas) {
//				WCMAssetItem asset = dao.getBy(wcmContainerData.assetsid, WCMAssetItem.class);
//				BaseAsset baseAsset = beansmap.get(asset.assettype);
//				String assetText = ClasspathResourceUtil.getTextByWebappPath("/wcm/assets/" + baseAsset.getAssetRelativePath() + ".html");
//				String assetflag = baseAsset.extractAssetData(asset, sitesid);
//				assetText = assetText.replaceAll("\\$\\{[\\s]*asset[\\s]*\\}", assetflag);
//				htmlstr = htmlstr.replaceFirst("(?<=containersid=\"" + wcmContainerData.containersid + "\">)[^<]*(?=\\<)", assetText);
//			}
//			
//			FileOutputStream fos = new FileOutputStream(folder.getAbsolutePath() + "/" + wcmPageItem.sid + ".html");
//			fos.write(htmlstr.getBytes("utf-8"));
//			fos.close();
//		}
		
		// 记录发布历史
		WCMPublishHistory his = newInstanceWithCreateInfo(WCMPublishHistory.class, req);
		his.sid = SpeedIDUtil.getId();
		his.sitesid = sitesid;
		dao.insertOrUpdate(his);
	}

	/**
	 * 循环站点所有页面拷贝资源文件
	 * @param folder
	 * @param pageItem
	 */
	private void copyAssets(File folder, List<WCMPageItem> pageItem)
			throws IOException {
//		for (WCMPageItem wcmPageItem : pageItem) {
//			WCMTemplateInfoBean tmpinfo = tmpinfoservice.getTemplateInfoBySid(wcmPageItem.templatesid);
//			File tmpinfofile = new File(tmpinfo.path);
//			FileUtils.copyDirectory(tmpinfofile, folder, new FileFilter() {
//				public boolean accept(File pathname) {
//					return !pathname.getName().matches("^(template.html|screenshot.png|template.properties)$");
//				}
//			});
//		}
	}
}







