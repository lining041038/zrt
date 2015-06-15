package antelope.wcm.beans;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import junit.framework.Assert;

import org.springframework.transaction.TransactionStatus;

import antelope.db.DBUtil;
import antelope.springmvc.SpringUtils;
import antelope.springmvc.SqlWhere;
import antelope.utils.JSONArray;
import antelope.utils.JSONException;
import antelope.utils.JSONObject;
import antelope.utils.PageItem;
import antelope.utils.PageParams;
import antelope.utils.TextUtils;
import antelope.wcm.consts.WCMSiteSettingConsts;
import antelope.wcm.services.ArticleQueryService;
import antelope.wcm.services.ProductQueryService;

/**
 * 页面数据Bean,方便获取页面相关数据
 * @author pc
 * @since 2014-3-17
 */
public class WebPageDataBean {
	
	private JSONObject tmplSettings;
	
	private String sitesid;
	
	private String templatepath;
	
	private HttpServletRequest req;
	
	
	
	public WebPageDataBean(JSONObject tmplSettings, String sitesid, String templatepath, HttpServletRequest req) throws Exception {
		Assert.assertNotNull("初始化WebPageDataBean时tmplSetting不能为空！", tmplSettings);
		Assert.assertNotNull("初始化WebPageDataBean时sitesid不能为空！", sitesid);
		this.tmplSettings = tmplSettings;
		this.sitesid = sitesid;
		this.templatepath = templatepath;
		this.req = req;
	}
	
	public JSONObject getTmplSettings() {
		return tmplSettings;
	}
	
	public String getSitesid() {
		return sitesid;
	}
	
	public String getTemplatepath() {
		return templatepath;
	}
	
	public String getLogoImgSid() throws JSONException {
		return tmplSettings.getString(WCMSiteSettingConsts.LOGO_IMG_SID);
	}
	
	public JSONArray<JSONObject> getNavigationTree() throws JSONException {
		// 不同类型菜单设置不同超链接
		JSONArray<JSONObject> menus = new JSONArray(tmplSettings.getString(WCMSiteSettingConsts.NAVIGATION_TREE)).getJSONObject(0).getJSONArray("nodes");
		setMenuHref(menus);
		return menus;
	}
	
	/**
	 * 根据webpagepos获取下级NavigationTree数据
	 * @return
	 */
	public JSONArray<JSONObject> getSubNavigationTree() throws JSONException {
		return exploreNavigationTree(getNavigationTree());
	}
	
	/**
	 * 根据level查询webpagesid所属菜单某级菜单
	 * level 根据菜单级别
	 * @return
	 */
	public JSONArray<JSONObject> getSubNavigationTree(int level) throws SQLException, Exception {
		
		
		if (level == 1)
			return getNavigationTree();
		
		List<JSONObject> breadcrumbs = getBreadcrumbs();
		
		if (breadcrumbs.size() > (level - 2) && breadcrumbs.get(level - 2).has("nodes"))
			return breadcrumbs.get(level - 2).getJSONArray("nodes");
		
		return null;
	}
	
	private JSONArray<JSONObject> exploreNavigationTree(JSONArray<JSONObject> parentmenus) throws JSONException {
		for (JSONObject menu : parentmenus) {
			if (menu.getString("pagesid").equals(req.getParameter("webpagepos"))) {
				
				return menu.has("nodes")? menu.getJSONArray("nodes") : null;
			}
			if (menu.has("nodes") && menu.getJSONArray("nodes").length() > 0) {
				JSONArray<JSONObject> submenus = exploreNavigationTree(menu.getJSONArray("nodes"));
				if (submenus != null)
					return submenus;
			}
		}
		return null;
	}
	
	
	public void setMenuHref(JSONArray<JSONObject> menus) throws JSONException {
		for (JSONObject jsonObject : menus) {
			String string = jsonObject.getString("webpageproperty");
			JSONArray<JSONObject> jsonobj = new JSONArray<JSONObject>(string);
			if (jsonobj.length() > 0) {
				
				// 首页
				if ("indexpage".equals(jsonobj.getJSONObject(0).getString("type"))) {
					jsonObject.put("href", "index.jsp?sitesid=" + sitesid + "&webpagepos=index");
					jsonObject.put("pagesid", "index");
				}
				
				// 文章列表
				if ("article_cate".equals(jsonobj.getJSONObject(0).getString("type"))) {
					jsonObject.put("href", "articlelist.jsp?sitesid=" + sitesid + 
							"&sid=" + jsonobj.getJSONObject(0).getString("sid") + 
							"&type=" + jsonobj.getJSONObject(0).getString("type") + 
							"&webpagepos=" + jsonObject.getString("sid"));
					
					jsonObject.put("pagesid", jsonObject.getString("sid"));
				}
				
				// 文章
				if ("article".equals(jsonobj.getJSONObject(0).getString("type"))) {
					jsonObject.put("href", "article.jsp?sitesid=" + sitesid + 
							 "&type=" + jsonobj.getJSONObject(0).getString("type") + 
							 "&sid=" + jsonobj.getJSONObject(0).getString("sid") +  
							 "&webpagepos=" + jsonObject.getString("sid"));
					jsonObject.put("pagesid", jsonObject.getString("sid"));
				}
				
				// 产品列表
				if ("product_cate".equals(jsonobj.getJSONObject(0).getString("type"))) {
					jsonObject.put("href", "productlist.jsp?sitesid=" + sitesid + 
							"&sid=" + jsonobj.getJSONObject(0).getString("sid") + 
							"&type=" + jsonobj.getJSONObject(0).getString("type") + 
							"&webpagepos=" + jsonObject.getString("sid"));
					
					jsonObject.put("pagesid", jsonObject.getString("sid"));
				}
				
				// 网站留言
				if ("peoplemsg".equals(jsonobj.getJSONObject(0).getString("type"))) {
					jsonObject.put("href", "peoplemsg.jsp?sitesid=" + sitesid + 
							"&sid=" + jsonobj.getJSONObject(0).getString("sid") + 
							"&type=" + jsonobj.getJSONObject(0).getString("type") + 
							"&webpagepos=" + jsonObject.getString("sid"));
					
					jsonObject.put("pagesid", jsonObject.getString("sid"));
				}
				
				// 自定义页面
				if ("custompage".equals(jsonobj.getJSONObject(0).getString("type"))) {
					jsonObject.put("href", "custompage.jsp?sitesid=" + sitesid + 
							"&sid=" + jsonobj.getJSONObject(0).getString("sid") + 
							"&type=" + jsonobj.getJSONObject(0).getString("type") + 
							"&webpagepos=" + jsonObject.getString("sid"));
					
					jsonObject.put("pagesid", jsonObject.getString("sid"));
				}
			}
			
			if (jsonObject.has("nodes")) {
				setMenuHref(jsonObject.getJSONArray("nodes"));
			}
		}
	}
	
	public String getBannerSid() throws JSONException {
		return tmplSettings.getString(WCMSiteSettingConsts.WEB_BANNERSID);
	}
	
	public String getHeaderInfo() throws JSONException {
		return TextUtils.noNull(tmplSettings.getString(WCMSiteSettingConsts.WEB_HEADERINFO));
	}
	
	public String getFooterInfo() throws JSONException {
		return TextUtils.noNull(tmplSettings.getString(WCMSiteSettingConsts.WEB_FOOTERINFO));
	}
	
	/**
	 * 获取面包屑导航条数据
	 */
	public List<JSONObject> getBreadcrumbs() throws SQLException, Exception {
		JSONArray<JSONObject> navigationTree = getNavigationTree();
		List<JSONObject> menulist = new ArrayList<JSONObject>();
		List<JSONObject> breadcrumbmenus = exploreNavigationTree(menulist, navigationTree, req.getParameter("webpagepos"));
		
		if (breadcrumbmenus == null) {
			breadcrumbmenus = new ArrayList<JSONObject>();
		}
		
		return  breadcrumbmenus;
	}
	
	/**
	 * 获取选中的菜单样式，当为选中的菜单时，则返回checkedStyleStr，否则返回空字符串
	 * @param checkedStyleStr 
	 * @return
	 * @throws JSONException 
	 */
	public String getCheckedMenuStyle(String currMenuPageSid, String checkedStyleStr) throws JSONException {
		
		List<JSONObject> menulist = new ArrayList<JSONObject>();
		List<JSONObject> breadcrumbmenus = exploreNavigationTree(menulist, getNavigationTree(), currMenuPageSid);
		
		if (breadcrumbmenus == null || breadcrumbmenus.isEmpty())
			return "";
			
		JSONObject jsonObject = breadcrumbmenus.get(breadcrumbmenus.size() - 1);
		
		if (req.getParameter("webpagepos") != null && req.getParameter("webpagepos").equals(jsonObject.getString("pagesid"))) {
			return checkedStyleStr;
		}
		
		if (jsonObject.has("nodes")) {
			List<JSONObject> submenus = exploreNavigationTree(menulist, jsonObject.getJSONArray("nodes"), TextUtils.noNull(req.getParameter("webpagepos")));
			if (submenus == null || submenus.isEmpty())
				return "";
			
			return checkedStyleStr;
		}
		return "";
	}
	
	/**
	 * 获取页面title
	 */
	public String getPageTitle() throws SQLException, Exception {
		List<JSONObject> breadcrumbs = getBreadcrumbs();
		if (breadcrumbs.size() == 0) {
			String catename = (String) doQueryInTrans(new QueryHandler() {
				public Object execute() throws Exception {
					return DBUtil.querySingleString("select * from (select name from WCM_PRODUCT_CATE where sid=?" +
							" or sid in (select catesid from WCM_PRODUCT where sid=?) " +
							" union all select name from WCM_ARTICLE_CATE where sid=?" +
							" or sid in (select catesid from WCM_ARTICLE where sid=?) ) t", 
							new Object[]{req.getParameter("sid"), req.getParameter("sid"), req.getParameter("sid"), req.getParameter("sid")});
				}
			});
			
			if (TextUtils.stringSet(catename))
				return catename;
			
			return "首页";
		}
		return breadcrumbs.get(breadcrumbs.size() - 1).getString("name");
	}
	
	/**
	 * 获取当前页超链，所谓当前页是指面包屑导航中最后一页
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 */
	public String getPageHref() throws SQLException, Exception {
		List<JSONObject> breadcrumbs = getBreadcrumbs();
		if (breadcrumbs.size() > 0)  {
			return breadcrumbs.get(breadcrumbs.size() - 1).getString("href");
		}
		return "#";
	}
	
	public String getPageTitle(int level) throws SQLException, Exception {
		List<JSONObject> breadcrumbs = getBreadcrumbs();
		if (breadcrumbs.size() == 0) {
			String catename = (String) doQueryInTrans(new QueryHandler() {
				public Object execute() throws Exception {
					return DBUtil.querySingleString("select * from (select name from WCM_PRODUCT_CATE where sid=?" +
							" or sid in (select catesid from WCM_PRODUCT where sid=?) " +
							" union all select name from WCM_ARTICLE_CATE where sid=?" +
							" or sid in (select catesid from WCM_ARTICLE where sid=?) ) t", 
							new Object[]{req.getParameter("sid"), req.getParameter("sid"), req.getParameter("sid"), req.getParameter("sid")});
				}
			});
			
			if (TextUtils.stringSet(catename))
				return catename;
			
			return "首页";
		}
		return breadcrumbs.get(level - 1).getString("name");
	}
	
	private List<JSONObject> exploreNavigationTree(List<JSONObject> parentlist, JSONArray<JSONObject> parentmenus, String sid) throws JSONException {
		for (JSONObject menu : parentmenus) {
			List<JSONObject> menulist = new ArrayList<JSONObject>();
			menulist.addAll(parentlist);
			menulist.add(menu);
			if (sid.equals(menu.getString("pagesid"))) {
				return menulist;
			}
			if (menu.has("nodes") && menu.getJSONArray("nodes").length() > 0) {
				List<JSONObject> submenus = exploreNavigationTree(menulist, menu.getJSONArray("nodes"), sid);
				if (submenus != null)
					return submenus;
			}
		}
		return null;
	}
	
	public JSONArray<JSONObject> getLeftPortlets() throws Exception {
		return getPortletsInner("0");
	}
	
	public JSONArray<JSONObject> getCenterPortlets() throws Exception {
		return getPortletsInner("1");
	}
	
	public JSONArray<JSONObject> getRightPortlets() throws Exception {
		return getPortletsInner("2");
	}
	
	public JSONArray<JSONObject> getBottomPortlets() throws Exception {
		return getPortletsInner("3");
	}

	/**
	 * 获取portlets
	 * @param position  位置参数  0 左  1中 2右  若不确定请核对sys_wcm_position 的xml枚举
	 */
	private JSONArray<JSONObject> getPortletsInner(String position) throws JSONException,
			Exception {
		JSONArray<JSONObject> portlets = new JSONArray<JSONObject>();
		JSONArray<JSONObject> jsonArray = new JSONArray(tmplSettings.getString(WCMSiteSettingConsts.WEB_INDEXPORTLETS));
		
		for (JSONObject jsonObject : jsonArray) {
			// 
			if (position.equals(jsonObject.getString("position"))) {
				portlets.put(jsonObject);
				JSONArray<JSONObject> jsonArray2 = new JSONArray(jsonObject.getString("portletcontent_values"));
				
				/**
				 * 	<item value="0" label="文章分类列表"/>
	<item value="1" label="最新文章"/>
	<item value="4" label="文章摘要"/>
	<item value="2" label="产品分类列表"/>
	<item value="3" label="最新产品"/>
				 */
				
				String jspfilename = "";
				String pagetype = "";
				switch(Integer.parseInt(jsonObject.getString("portlettype"))) {
				case 0: jspfilename = "articlelist.jsp"; pagetype="article_cate"; break;
				case 1: jspfilename = "article.jsp"; pagetype="article"; break;
				case 2: jspfilename = "productlist.jsp"; pagetype="product_cate"; break;
				case 3: jspfilename = "product.jsp";  pagetype="product"; break;
				case 4: jspfilename = "article.jsp";  pagetype="article"; break;
				}
				
				
				// 文章分类列表
				if ("0".equals(jsonObject.getString("portlettype"))) {
					final List<String> catesids = new ArrayList<String>();
					for (JSONObject jsonobj : jsonArray2) {
						catesids.add(jsonobj.getString("sid"));
					}
					
					final int finalshownum = getPortletShowNum(jsonObject);
					PageItem pagedata = (PageItem) doQueryInTrans(new QueryHandler() {
						public Object execute() throws Exception {
							PageParams pageParams = new PageParams();
							pageParams.numPerPage = finalshownum;
							pageParams.page = "1";
							return DBUtil.queryJSON("select * from WCM_ARTICLE_CATE where sid in ('" + TextUtils.join("','", catesids) + "') ", new Object[0], pageParams);
						}
					});
					
					List<JSONObject> list = pagedata.getCurrList();
					for (JSONObject newestarticle : list) {
						newestarticle.put("href", jspfilename + "?sitesid=" + sitesid + 
								"&sid=" + newestarticle.getString("sid") + 
								"&type=" + pagetype + 
								"&webpagepos=" + req.getParameter("webpagepos"));
					}
					
					jsonObject.put("portletcontent_values", new JSONArray(list));
				}
				
				// 最新文章
				if ("1".equals(jsonObject.getString("portlettype"))) {
					final String catesid = jsonArray2.getJSONObject(0).getString("sid");
					final int finalshownum = getPortletShowNum(jsonObject);
					
					PageItem pagedata = (PageItem) doQueryInTrans(new QueryHandler() {
						public Object execute() throws Exception {
							ArticleQueryService queryservice = SpringUtils.getBean(ArticleQueryService.class);
							PageParams pageParams = new PageParams();
							pageParams.numPerPage = finalshownum;
							pageParams.page = "1";
							return queryservice.getArticlesPageDataByCatesid(catesid, pageParams, new SqlWhere());
						}
					});
					
					List<JSONObject> list = pagedata.getCurrList();
					for (JSONObject newestarticle : list) {
						newestarticle.put("href", jspfilename + "?sitesid=" + sitesid + 
								"&sid=" + newestarticle.getString("sid") + 
								"&type=" + pagetype + 
								"&webpagepos=" + req.getParameter("webpagepos"));
					}
					
					jsonObject.put("portletcontent_values", new JSONArray(list));
				}
				
				// 产品分类列表
				if ("2".equals(jsonObject.getString("portlettype"))) {
					
					final List<String> catesids = new ArrayList<String>();
					for (JSONObject jsonobj : jsonArray2) {
						catesids.add(jsonobj.getString("sid"));
					}
					final int finalshownum = getPortletShowNum(jsonObject);
					PageItem pagedata = (PageItem) doQueryInTrans(new QueryHandler() {
						public Object execute() throws Exception {
							PageParams pageParams = new PageParams();
							pageParams.numPerPage = finalshownum;
							pageParams.page = "1";
							return DBUtil.queryJSON("select * from WCM_PRODUCT_CATE where sid in ('" + TextUtils.join("','", catesids) + "')", new Object[0], pageParams);
						}
					});
					
					List<JSONObject> list = pagedata.getCurrList();
					for (JSONObject newestarticle : list) {
						newestarticle.put("href", jspfilename + "?sitesid=" + sitesid + 
								"&sid=" + newestarticle.getString("sid") + 
								"&type=" + pagetype + 
								"&webpagepos=" + req.getParameter("webpagepos"));
					}
					
					jsonObject.put("portletcontent_values", new JSONArray(list));
				}
				
				// 最新产品
				if ("3".equals(jsonObject.getString("portlettype"))) {
					final String catesid = jsonArray2.getJSONObject(0).getString("sid");
					final int finalshownum = getPortletShowNum(jsonObject);
					PageItem pagedata = (PageItem) doQueryInTrans(new QueryHandler() {
						public Object execute() throws Exception {
							ProductQueryService queryservice = SpringUtils.getBean(ProductQueryService.class);
							PageParams pageParams = new PageParams();
							pageParams.numPerPage = finalshownum;
							pageParams.page = "1";
							return queryservice.getProductsPageDataByCatesid(catesid, pageParams, new SqlWhere());
						}
					});
					
					List<JSONObject> list = pagedata.getCurrList();
					for (JSONObject newestarticle : list) {
						newestarticle.put("href", jspfilename + "?sitesid=" + sitesid + 
								"&sid=" + newestarticle.getString("sid") + 
								"&type=" + pagetype + 
								"&webpagepos=" + req.getParameter("webpagepos"));
						newestarticle.put("productimgsid", newestarticle.getString("productimgsid"));
					}
					
					jsonObject.put("portletcontent_values", new JSONArray(list));
				}
				
				// 类型4为文章摘要
				if ("4".equals(jsonObject.getString("portlettype"))) { // 需要查询摘要
					final String articlesid = new JSONArray(jsonObject.getString("portletcontent_values")).getJSONObject(0).getString("sid");
					String digest = ((String) doQueryInTrans(new QueryHandler() {
						public Object execute() throws Exception {
							return DBUtil.querySingleString("select digest from WCM_ARTICLE where sid=?", new Object[]{articlesid});
						}
					}));
					jsonObject.put("digest", digest);
					jsonObject.put("articlesid", articlesid);
				}
			}
			
		}
		
		//[{"sid":"1395022083210","title":"测试第二个新标题47","portlettype":"1","position":"0","sortfield":"356","portletcontent_values":
		//"[{\"sid\":\"1394420131613\",\"name\":\"二级分类\",\"parentsid\":\"1394420131610\"}]","portletcontent_values_names":"二级分类"}]
		return portlets;
	}

	private int getPortletShowNum(JSONObject jsonObject) throws JSONException {
		int shownum = 50;
		if (TextUtils.stringSet(jsonObject.getString("shownum"))) {
			shownum = Integer.parseInt(jsonObject.getString("shownum"));
		}
		return shownum;
	}
	
	/**
	 * 文章界面适用，获取文章标题
	 * @return
	 * @throws Exception
	 */
	public String getArticleTitle() throws Exception {
		return (String) doQueryInTrans(new QueryHandler() {
			public Object execute() throws Exception {
				return DBUtil.querySingleString("select name from WCM_ARTICLE where sid=?", new Object[]{req.getParameter("sid")});
			}
		});
	}
	
	
	public String getArticleContent() throws Exception {
		return (String) doQueryInTrans(new QueryHandler() {
			public Object execute() throws Exception {
				return DBUtil.querySingleString("select content from WCM_ARTICLE where sid=?", new Object[]{req.getParameter("sid")});
			}
		});
	}
	
	
	public String getArticleAuthorName() throws Exception {
		return (String) doQueryInTrans(new QueryHandler() {
			public Object execute() throws Exception {
				return DBUtil.querySingleString("select authorname from WCM_ARTICLE where sid=?", new Object[]{req.getParameter("sid")});
			}
		});
	}
	
	public String getArticleCreateTime() throws Exception {
		return (String) doQueryInTrans(new QueryHandler() {
			public Object execute() throws Exception {
				return DBUtil.querySingleString("select createtime from WCM_ARTICLE where sid=?", new Object[]{req.getParameter("sid")});
			}
		});
	}
	
	public String getArticleSource()throws Exception {
		return (String) doQueryInTrans(new QueryHandler() {
			public Object execute() throws Exception {
				return DBUtil.querySingleString("select source from WCM_ARTICLE where sid=?", new Object[]{req.getParameter("sid")});
			}
		});
	}
	
	public String getProductName() throws Exception {
		return (String) doQueryInTrans(new QueryHandler() {
			public Object execute() throws Exception {
				return DBUtil.querySingleString("select name from WCM_PRODUCT where sid=?", new Object[]{req.getParameter("sid")});
			}
		});
	}
	
	public String getProductContent() throws Exception {
		return (String) doQueryInTrans(new QueryHandler() {
			public Object execute() throws Exception {
				return DBUtil.querySingleString("select content from WCM_PRODUCT where sid=?", new Object[]{req.getParameter("sid")});
			}
		});
	}
	
	public String getProductCreatetime() throws Exception {
		return (String) doQueryInTrans(new QueryHandler() {
			public Object execute() throws Exception {
				return DBUtil.querySingleString("select createtime from WCM_PRODUCT where sid=?", new Object[]{req.getParameter("sid")});
			}
		});
	}
	
	
	public String getCustomTitle() throws Exception {
		return (String) doQueryInTrans(new QueryHandler() {
			public Object execute() throws Exception {
				return DBUtil.querySingleString("select name from WCM_PAGE where sid=?", new Object[]{req.getParameter("sid")});
			}
		});
	}
	
	public String getCustomContent() throws Exception {
		return (String) doQueryInTrans(new QueryHandler() {
			public Object execute() throws Exception {
				return DBUtil.querySingleString("select content from WCM_PAGE where sid=?", new Object[]{req.getParameter("sid")});
			}
		});
	}
	
	/**
	 * 获取产品img图片sid数组，此sid为SYS_FILES中的sid，非WCM_PRODUCT 中的productimgsid
	 */
	public List<JSONObject> getProductImgSids() throws Exception {
		return (List<JSONObject>) doQueryInTrans(new QueryHandler() {
			public Object execute() throws Exception {
				return DBUtil.queryJSON("select sid from SYS_FILES where ispermanent=1 and filegroupsid in (select productimgsid from WCM_PRODUCT where sid=?)", new Object[]{req.getParameter("sid")});
			}
		});
	}
	
	/**
	 * 获取产品img图片WCM_PRODUCT 中的productimgsid
	 */
	public String getProductImgSid() throws Exception {
		return (String) doQueryInTrans(new QueryHandler() {
			public Object execute() throws Exception {
				return DBUtil.querySingleString("select productimgsid from WCM_PRODUCT where sid=?", new Object[]{req.getParameter("sid")});
			}
		});
	}
	
	private Object doQueryInTrans(QueryHandler handler) throws Exception {
		TransactionStatus status = SpringUtils.beginTransaction();
		Object execute = null;
		try {
			execute = handler.execute();
		} finally {
			SpringUtils.commitTransaction(status);
		}
		return execute;
	}
	
	
	/**
	 * 获取首页幻灯片
	 * @return
	 * @throws JSONException
	 * @throws Exception
	 */
	public JSONArray<JSONObject> getIndexImg() throws JSONException,Exception {
		JSONArray<JSONObject> portlets = new JSONArray<JSONObject>();
		JSONArray<JSONObject> jsonArray = new JSONArray(tmplSettings.getString(WCMSiteSettingConsts.WEB_IMGPORTLETS));
		int num = 0;
		JSONArray<JSONObject> jsonSortArray = new JSONArray();
		for(int i = jsonArray.length()-1;i >= 0;i--){
			num++;
			if(num > 5){
				break;
			}
			
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			
			String jspfilename = "";
			String pagetype = "";
			
			net.sf.json.JSONArray  ja_1 = net.sf.json.JSONArray.fromObject(jsonObject.get("portletcontent_values"));
			net.sf.json.JSONObject jo_1 = net.sf.json.JSONObject.fromObject(ja_1.get(0));

			if("product".equals(jo_1.getString("type"))){
				jspfilename = "product.jsp"; 
				pagetype="product";
			}else if("article".equals(jo_1.getString("type"))){
				jspfilename = "article.jsp"; 
				pagetype="article";
			}else if("article_cate".equals(jo_1.getString("type"))){
				jspfilename = "articlelist.jsp"; 
				pagetype="article_cate";
			}else{
				jspfilename = "productlist.jsp"; 
				pagetype="product_cate";
			}
			
			String href = jspfilename + "?sitesid=" + sitesid + 
					"&sid=" + jo_1.getString("sid") + 
					"&type=" + pagetype + 
					"&webpagepos=" + req.getParameter("webpagepos");
			
			jsonObject.put("href", href);
			jsonSortArray.put(jsonObject);
		}
		return  jsonSortArray;
	}
}



