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
import antelope.wcm.entities.WCMAnnouncementItem;

/**
 * 公告管理
 * @author lining
 * @since 2014-3-10
 */
@Controller("announcementmanage")
@RequestMapping("/wcm/announcement/AnnouncementManageController")
public class AnnouncementManageController extends SingleDatagrid {
	
	@Override
	public SingleDatagridOptions getOptions(HttpServletRequest req) {
		SingleDatagridOptions opts = new SingleDatagridOptions(this);
		opts.queryfields = new String[]{"name"};
		opts.columns.put("name", new GridColumn("标题", "30%"));
		return opts;
	}
	
	public void getSingleGridList(String name, HttpServletRequest req, HttpServletResponse res) throws SQLException, Exception {
		SqlWhere sqlwhere = tidySqlWhere(new String[]{"name like ?"}, new Object[]{"%" + decodeAndTrim(name) + "%"});
		PageItem pageItem = DBUtil.queryJSON("select sid, name, content from WCM_ANNOUNCEMENT where 1=1 " + sqlwhere.wherePart, sqlwhere.outParams, getPageParams(req));
		print(pageItem, res);
	}
	
	public void deleteOneLine(String sid, HttpServletRequest req, HttpServletResponse res) throws InstantiationException, IllegalAccessException {
		dao.deleteBy(sid, WCMAnnouncementItem.class);
	}
	
	public void addOrUpdateOne(String sid, HttpServletRequest req, HttpServletResponse res) throws Exception {
		WCMAnnouncementItem item = dao.getBy(sid, WCMAnnouncementItem.class);
		if (item == null)
			item = newInstanceWithCreateInfo(WCMAnnouncementItem.class, req);
		wrapToEntity(req, item);
		dao.insertOrUpdate(item);
	}
}
