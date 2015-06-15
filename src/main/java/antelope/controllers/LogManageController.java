package antelope.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.read.biff.BiffException;

import org.apache.commons.fileupload.FileItem;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import antelope.db.DBUtil;
import antelope.demos.entites.SingleDataGridItem;
import antelope.interfaces.components.SingleDatagrid;
import antelope.interfaces.components.supportclasses.CreateUpdateWindowMode;
import antelope.interfaces.components.supportclasses.FormField;
import antelope.interfaces.components.supportclasses.GridColumn;
import antelope.interfaces.components.supportclasses.SingleDatagridOptions;
import antelope.services.supportclasses.ExcelTmpl;
import antelope.springmvc.SqlWhere;
import antelope.utils.JSONObject;
import antelope.utils.PageItem;
import antelope.utils.SpeedIDUtil;

/**
 * 系统日志管理
 * @author lining
 * @since 2013-11-12
 */
@Controller("logmanage")
@RequestMapping("/system/logmanage/logmanage/LogManageController")
public class LogManageController extends SingleDatagrid {
	
	@Override
	public SingleDatagridOptions getOptions(HttpServletRequest req) {
		SingleDatagridOptions opts = new SingleDatagridOptions(this);
		GridColumn levelname = new GridColumn("级别", "10%");
		levelname.enumXml = "sys_loglevel";
		opts.columns.put("loglevel", levelname);
		opts.columns.put("message", new GridColumn("详情", "60%"));
		GridColumn logclass = new GridColumn("类型", "10%");
		logclass.enumXml = "sys_logclass";
		opts.columns.put("logclass", logclass);
		opts.columns.put("createtime", new GridColumn("时间", "10%"));
		opts.showCreateBtn = false;
		opts.showUpdateBtn = false;
		opts.showDeleteBtn = false;
		opts.buttons.clear();
		return opts;
	}
	
	public void getSingleGridList(String message, HttpServletRequest req, HttpServletResponse res) throws SQLException, Exception {
		SqlWhere sqlwhere = tidySqlWhere(new String[]{"message like ?"}, new Object[]{"%" + decodeAndTrim(message) + "%"});
		PageItem pageItem = DBUtil.queryJSON("select * from SYS_LOG where 1=1 " + sqlwhere.wherePart, sqlwhere.outParams, getPageParams(req, "createtime"));
		print(pageItem, res);
	}
}



