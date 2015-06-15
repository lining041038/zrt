package antelope.services;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import oracle.sql.TIMESTAMP;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.stereotype.Service;

import antelope.beans.SysReportData;
import antelope.db.DBUtil;
import antelope.db.DBUtil.DataType;
import antelope.db.Sql;
import antelope.entities.SysRepTmpl;
import antelope.springmvc.BaseComponent;
import antelope.utils.ResultSetHandler;

/**
 * 固定表报表服务
 * @author lining
 */
@Service
public class ReportService extends BaseComponent {
	
	/**
	 * 获取固定表报表数据
	 * @param tmplsid 对应报表模板sid
	 * @param datasid 对应报表数据sid
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 */
	public List<SysReportData> getReportDataByTmplSidAndDataSid(String tmplsid, String datasid) throws SQLException, Exception {
		
		final List<SysReportData> sysreport = new ArrayList<SysReportData>();
		Object dbtablename = DBUtil.querySingleVal("select dbtablename from sys_rep_tmpl where sid=?", tmplsid, DataType.String);
		Object html = DBUtil.querySingleVal("select htmltable from sys_rep_tmpl where sid=?", tmplsid, DataType.String);
		final Document dom = DocumentHelper.parseText(html.toString());
		
		if (dbtablename == null)
			return sysreport;
		
		Sql sql = new Sql("select * from "+dbtablename+" where sid=?");
		
		sql.query(new ResultSetHandler() {
			public Object handle(ResultSet rs) throws Exception {
				if (rs.next()) {
					ResultSetMetaData metaData = rs.getMetaData();
					for(int i = 1; i <= metaData.getColumnCount(); i++) {
						SysReportData srd = new SysReportData();
						srd.dbcolname = metaData.getColumnLabel(i);
						Element elem = (Element) dom.selectSingleNode("//*[@dbcol='"+srd.dbcolname+"']");
						if (elem != null) {
							srd.edittype = elem.attributeValue("edittype");
						}
						if (rs.getObject(i) instanceof Date || rs.getObject(i) instanceof TIMESTAMP) {
							srd.data = rs.getTimestamp(i);
						} else {
							srd.data = rs.getObject(i);
						}
						sysreport.add(srd);
					//	}
					}
				}
				return null;
			}
		}, new Object[]{datasid});
		
		return sysreport;
	}
	
	/**
	 * 获取表头相关数据
	 * @param tmplsid
	 * @return
	 */
	public List<SysReportData> getReportHeaderDatas(String tmplsid) throws Exception {
		List<SysReportData> headerdata = new ArrayList<SysReportData>();
		if (stringSet(tmplsid)) {
			SysRepTmpl tmpl = dao.getBy(tmplsid, SysRepTmpl.class);
			if (tmpl != null) {
				Document doc = DocumentHelper.parseText(tmpl.htmltable);
				List<Element> nodes = doc.selectNodes("//TD");
				for (Element element : nodes) {
					SysReportData reportdata = new SysReportData();
					reportdata.data = element.getText();
					headerdata.add(reportdata);
				}
			}
		}
		return headerdata;
	}
}



