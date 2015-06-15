package antelope.wcm.controllers;

import java.io.IOException;
import java.sql.SQLException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import antelope.springmvc.BaseController;
import antelope.springmvc.SqlWhere;
import antelope.utils.PageItem;
import antelope.utils.SpeedIDUtil;
import antelope.wcm.entities.WCMPeopleMsgItem;
import antelope.wcm.services.ArticleQueryService;
import antelope.wcm.services.ProductQueryService;
import antelope.wcm.services.SearchQueryService;

/**
 * 公告管理
 * @author lining
 * @since 2014-3-10
 */
@Controller("webpagedatacontroller")
@RequestMapping("/wcm/webpagedata/WebPageDataController")
public class WebPageDataController extends BaseController {
	
	@Resource
	private ArticleQueryService articleservice;
	
	@Resource
	private ProductQueryService productservice;
	
	@Resource
	private SearchQueryService searchservice;
	
	@RequestMapping("/getArticleListData")
	public void getArticleListData(String catesid, HttpServletRequest req, HttpServletResponse res) throws IOException, SQLException, Exception {
		SqlWhere sqlwhere = tidySqlWhere(new String[]{}, new Object[]{}, true);
		PageItem json = articleservice.getArticlesPageDataByCatesid(catesid, getPageParams(req), sqlwhere);
		print(json, res);
	}
	
	
	@RequestMapping("/getProductListData")
	public void getProductListData(String catesid, HttpServletRequest req, HttpServletResponse res) throws IOException, SQLException, Exception {
		SqlWhere sqlwhere = tidySqlWhere(new String[]{}, new Object[]{}, true);
		PageItem json = productservice.getProductsPageDataByCatesid(catesid, getPageParams(req), sqlwhere);
		print(json, res);
	}
	
	@RequestMapping("/savePeoplemsg")
	public void savePeoplemsg(HttpServletRequest req, HttpServletResponse res) throws IOException, SQLException, Exception {
		WCMPeopleMsgItem wcmpeoplemsgitem = newInstanceWithCreateInfo(WCMPeopleMsgItem.class, req);
		wrapToEntity(req, wcmpeoplemsgitem);
		wcmpeoplemsgitem.sid = SpeedIDUtil.getId();
		dao.insertOrUpdate(wcmpeoplemsgitem);
	}
	
	@RequestMapping("/getSearchContent")
	public void getSearchContent(HttpServletRequest req, HttpServletResponse res) throws IOException, SQLException, Exception {
		SqlWhere sqlwhere = tidySqlWhere(new String[]{}, new Object[]{}, true);
		PageItem json = searchservice.getSearchPageDataByKey(req.getParameter("key"),req.getParameter("t"),req.getParameter("mintime"),req.getParameter("maxtime"),getPageParams(req), sqlwhere);
		print(json, res);
	}
	
}
