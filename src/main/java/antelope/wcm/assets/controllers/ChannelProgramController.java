package antelope.wcm.assets.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import antelope.consts.GlobalConsts;
import antelope.springmvc.BaseController;
import antelope.springmvc.SpringUtils;
import antelope.utils.JSONArray;
import antelope.utils.JSONObject;
import antelope.utils.SystemOpts;
import antelope.wcm.beans.ArticleBean;
import antelope.wcm.beans.ChannelProgramBean;
import antelope.wcm.interfaces.ChannelProgramAndArticleDataProvider;


@Controller
public class ChannelProgramController extends BaseController  {

	@RequestMapping("/assets/imgasset/ChannelProgramController/getChannelProgramsByParentsid")
	public void getChannelProgramsByParentsid(String sid, HttpServletRequest req, HttpServletResponse res) throws Exception {
		if (!stringSet(sid)) {
			JSONObject obj = new JSONObject();
			obj.put("sid", GlobalConsts.TREE_ROOT);
			obj.put("name", "频道与栏目");
			obj.put("isParent", true);
			JSONArray arr = new JSONArray();
			arr.put(obj);
			print(arr, res);
			return;
		}
		
		if (GlobalConsts.TREE_ROOT.equals(sid))	
			sid = "";
		
		String providername = SystemOpts.getProperty("antelope_wcm_channeldataprovidername");
		if (!stringSet(providername)) 
			providername = "demochannelprogramdataprovider";
		ChannelProgramAndArticleDataProvider bean = SpringUtils.getBean(ChannelProgramAndArticleDataProvider.class, providername);
		List<ChannelProgramBean> channels = bean.getChannelProgramsByParentsid(sid);
		JSONArray jsonarr = toJSONArrayBy(channels);
		jsonarr.addFieldToAll("isParent", true);
		print(jsonarr, res);
	}
	
	@RequestMapping("/assets/imgasset/ChannelProgramController/getChannelProgramsInfosByParentsid")
	public void getChannelProgramsInfosByParentsid(String sid, HttpServletRequest req, HttpServletResponse res) throws Exception {
		String providername = SystemOpts.getProperty("antelope_wcm_channeldataprovidername");
		if (!stringSet(providername)) 
			providername = "demochannelprogramdataprovider";
		ChannelProgramAndArticleDataProvider bean = SpringUtils.getBean(ChannelProgramAndArticleDataProvider.class, providername);
		JSONObject obj = new JSONObject();
		if (!GlobalConsts.TREE_ROOT.equals(sid)) {
			ChannelProgramBean channelprogram = bean.getChannelProgramBySid(sid);
			obj.put("name", channelprogram.name);
		}
		List<ChannelProgramBean> channels = bean.getChannelProgramsByParentsid(sid);
		obj.put("channels", toJSONArrayBy(channels));
		print(obj, res);
	}
	
	/**
	 * 获取频道文章首页块显示用数据
	 * @param sid 频道栏目sid
	 * @param req
	 * @param res
	 * @throws Exception
	 */
	@RequestMapping("/assets/imgasset/ChannelProgramController/getChannelPortalArticleDatas")
	public void getChannelPortalArticleDatas(String sid, HttpServletRequest req, HttpServletResponse res) throws Exception {
		String providername = SystemOpts.getProperty("antelope_wcm_channeldataprovidername");
		if (!stringSet(providername)) 
			providername = "demochannelprogramdataprovider";
		ChannelProgramAndArticleDataProvider bean = SpringUtils.getBean(ChannelProgramAndArticleDataProvider.class, providername);
		ChannelProgramBean channelprogram = bean.getChannelProgramBySid(sid);
		List<ArticleBean> articles = bean.getArticlesByParentsid(sid);
		JSONObject obj = new JSONObject();
		obj.put("name", channelprogram.name);
		obj.put("morepath", "#");
		obj.put("articles", new JSONArray(articles, true));
		print(obj, res);
	}
	
	
	
	/**
	 * 获取频道文章列表分页显示数据
	 * @param sid 频道栏目sid
	 * @param req
	 * @param res
	 * @throws Exception
	 */
	@RequestMapping("/assets/imgasset/ChannelProgramController/getChannelArticlePageDatas")
	public void getChannelArticlePageDatas(String sid, HttpServletRequest req, HttpServletResponse res) throws Exception {
		String providername = SystemOpts.getProperty("antelope_wcm_channeldataprovidername");
		if (!stringSet(providername)) 
			providername = "demochannelprogramdataprovider";
		ChannelProgramAndArticleDataProvider bean = SpringUtils.getBean(ChannelProgramAndArticleDataProvider.class, providername);
		ChannelProgramBean channelprogram = bean.getChannelProgramBySid(sid);
		List<ArticleBean> articles = bean.getArticlesByParentsid(sid);
		JSONObject obj = new JSONObject();
		obj.put("name", channelprogram.name);
		obj.put("morepath", "#");
		obj.put("articles", new JSONArray(articles, true));
		print(obj, res);
	}
}



