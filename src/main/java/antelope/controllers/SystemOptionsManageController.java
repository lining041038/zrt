package antelope.controllers;

import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import antelope.db.DBUtil;
import antelope.entities.SysOptItem;
import antelope.interfaces.components.MultipleDatagrids;
import antelope.interfaces.components.supportclasses.FormField;
import antelope.interfaces.components.supportclasses.GridColumn;
import antelope.interfaces.components.supportclasses.MultipleDatagridsOptions;
import antelope.interfaces.components.supportclasses.SingleDatagridOptionsExtended;
import antelope.springmvc.SqlWhere;
import antelope.utils.I18n;
import antelope.utils.JSONObject;
import antelope.utils.PageItem;

/**
 * 系统选项管理
 * @author lining
 * @since 2012-7-14
 */
@Controller("systemoptionsmanage")
@RequestMapping("/system/sysoptsManage/SystemOptionsManageController")
public class SystemOptionsManageController extends MultipleDatagrids {
	
	@Override
	public MultipleDatagridsOptions getOptions(HttpServletRequest req) throws SQLException, Exception {
		MultipleDatagridsOptions opts = new MultipleDatagridsOptions(this);
		I18n i18n = getI18n(req);
		
		List<JSONObject> groupnames = DBUtil.queryJSON("select distinct group_name, group_key from SYS_OPTS where group_name is not null and group_name != ''");
		
		for (JSONObject groupname : groupnames) {
			SingleDatagridOptionsExtended singlopt = new SingleDatagridOptionsExtended(this);
			singlopt.label = groupname.getString("group_name");
			singlopt.showCreateBtn = false;
			singlopt.showDeleteBtn = false;
			singlopt.queryfields = new String[]{"name"};
			singlopt.formKeyField = "formkey";
			singlopt.formfields = new FormField[]{new FormField("value", i18n.get("single_datagrid.systemoptionsmanage.optionvalue") , "required='true' maxlength2='500'", "enumxmlname")};
			singlopt.columns.put("name", new GridColumn(i18n.get("single_datagrid.systemoptionsmanage.optionname"), null));
			singlopt.columns.put("value", getValueGridColumn(i18n));
			opts.singleDatagridOptionMap.put(groupname.getString("group_key"), singlopt);
		}
		
		SingleDatagridOptionsExtended singlopt = new SingleDatagridOptionsExtended(this);
		singlopt.label = i18n.get("antelope.other");
		singlopt.showCreateBtn = false;
		singlopt.formKeyField = "formkey";
		singlopt.formKey = "/system/sysoptsManageform.jsp";
		singlopt.showDeleteBtn = false;
		singlopt.queryfields = new String[]{"name"};
		singlopt.formfields = new FormField[]{new FormField("value", i18n.get("single_datagrid.systemoptionsmanage.optionvalue") , "required='true' maxlength2='500'", "enumxmlname")};
		singlopt.columns.put("name", new GridColumn(i18n.get("single_datagrid.systemoptionsmanage.optionname"), null));
		singlopt.columns.put("value", getValueGridColumn(i18n));
		opts.singleDatagridOptionMap.put("others", singlopt);
		
		return opts;
	}
	
	private GridColumn getValueGridColumn(I18n i18n) {
		GridColumn gridCol = new GridColumn(i18n.get("single_datagrid.systemoptionsmanage.optionvalue"), null, "enumxmlname");
		gridCol.labelFunction = "optvalLabelFunction";
		return gridCol; 
	}

	
	public void getSingleGridList(String gridkey, HttpServletRequest req, HttpServletResponse res) throws SQLException, Exception {
		SqlWhere sqlwhere = tidySqlWhere(new String[]{"group_key=?", "name like ?"}, new Object[]{gridkey, "%" + decodeAndTrim(req.getParameter("name")) + "%"});
		PageItem pageItem = DBUtil.queryJSON("select * from SYS_OPTS where 1=1 " + sqlwhere.wherePart, sqlwhere.outParams, getPageParams(req));
		print(pageItem, res);
	}
	
	public void addOrUpdateOne(String key, String sid, HttpServletRequest req, HttpServletResponse res) throws Exception {
		SysOptItem item = dao.getBy(sid, SysOptItem.class);
		if (item == null)
			return;
		wrapToEntity(req, item);
		dao.insertOrUpdate(item);
	}
}
