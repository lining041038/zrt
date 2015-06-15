package antelope.wcm.controllers;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import antelope.db.DBUtil;
import antelope.interfaces.components.SingleDatagrid;
import antelope.interfaces.components.supportclasses.GridColumn;
import antelope.interfaces.components.supportclasses.SingleDatagridOptions;
import antelope.springmvc.SqlWhere;
import antelope.utils.PageItem;
import antelope.wcm.entities.WCMAnnouncementItem;
import antelope.wcm.entities.WCMPeopleMsgItem;

/**
 * 公告管理
 * @author lining
 * @since 2014-3-10
 */
@Controller("peoplemsgmanage")
@RequestMapping("/wcm/announcement/peoplemsgmanage")
public class PeopleMsgManageController extends SingleDatagrid {
	
	@Override
	public SingleDatagridOptions getOptions(HttpServletRequest req) {
		SingleDatagridOptions opts = new SingleDatagridOptions(this);
		opts.queryfields = new String[]{"name"};
		opts.columns.put("name", new GridColumn("昵称", "10%"));
		opts.columns.put("email", new GridColumn("电子邮件", "30%"));
		opts.columns.put("phonenum", new GridColumn("电话", "30%"));
		opts.columns.put("createtime", new GridColumn("创建时间", "10%"));
		opts.showCreateBtn = false;
		opts.showUpdateBtn = false;
		return opts;
	}
	
	public void getSingleGridList(String name, HttpServletRequest req, HttpServletResponse res) throws SQLException, Exception {
		SqlWhere sqlwhere = tidySqlWhere(new String[]{"name like ?"}, new Object[]{"%" + decodeAndTrim(name) + "%"});
		PageItem pageItem = DBUtil.queryJSON("select * from WCM_PEOPLE_MSG where 1=1 " + sqlwhere.wherePart, sqlwhere.outParams, getPageParams(req));
		print(pageItem, res);
	}
	
	public void deleteOneLine(String sid, HttpServletRequest req, HttpServletResponse res) throws InstantiationException, IllegalAccessException {
		dao.deleteBy(sid, WCMPeopleMsgItem.class);
	}
}
