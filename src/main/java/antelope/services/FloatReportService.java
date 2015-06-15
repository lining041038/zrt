package antelope.services;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.stereotype.Service;

import antelope.db.DBUtil;
import antelope.db.DBUtil.DataType;
import antelope.entities.SysFloatRepColumnInfo;
import antelope.entities.SysFloatRepTmpl;
import antelope.springmvc.BaseComponent;
import antelope.utils.JSONException;
import antelope.utils.JSONObject;
import antelope.utils.SpeedIDUtil;

/**
 * 浮动行报表服务类
 * @author lining
 */
@Service 
public class FloatReportService extends BaseComponent {

	/**
	 * 获取浮动行报表模板的模板名称
	 * @param sid
	 */
	public SysFloatRepTmpl getReportTmplBySid(String sid) throws Exception {
		return dao.getBy(sid, SysFloatRepTmpl.class);
	}
	
	
	/**
	 * 核实某模板和其对应的某数据组，对应的统计类型是否有给定的统计值
	 * @param tmplsid
	 * @param groupsid
	 * @param sumtype
	 * @param value
	 * @return
	 */
	public boolean checkSumtypeHasValue(String tmplsid, String groupsid, String sumtype, String value) throws Exception {
		SysFloatRepTmpl tmpl = dao.getBy(tmplsid, SysFloatRepTmpl.class);
		String sumcolname = getSumcolname(tmplsid, sumtype);
		if (stringSet(sumcolname)) {
			Integer ct = (Integer) DBUtil.querySingleVal("select count(" + sumcolname + ") from " + tmpl.dbtablename + " where group_sid = ? and "+sumcolname + "=?", new Object[]{groupsid, value}, DataType.Integer);
			if (ct != null && ct > 0)
				return true;
			else
				return false;
		}
		return false;
	}
	
	/**
	 * 核实某模板和其对应的某数据组里面对应初始化sid，对应的统计类型是否有给定的统计值
	 * @param tmplsid
	 * @param groupsid
	 * @param sumtype
	 * @param value
	 * @return
	 */
	public boolean checkSumtypeHasValue(String tmplsid, String groupsid, String initsid, String sumtype, String value) throws Exception {
		SysFloatRepTmpl tmpl = dao.getBy(tmplsid, SysFloatRepTmpl.class);
		String sumcolname = getSumcolname(tmplsid, sumtype);
		if (stringSet(sumcolname)) {
			Integer ct = (Integer) DBUtil.querySingleVal("select count(" + sumcolname + ") from " + tmpl.dbtablename + " where group_sid = ? and "+sumcolname + "=? and initsid=?", new Object[]{groupsid, value, initsid}, DataType.Integer);
			if (ct != null && ct > 0)
				return true;
			else
				return false;
		}
		return false;
	}


	private String getSumcolname(String tmplsid, String sumtype)
			throws Exception, JSONException {
		String sumcolname = "";
		List<SysFloatRepColumnInfo> colinfos = dao.query("select * from SYS_FLOATREP_DBCOLINFO where tmplsid=?"
				, tmplsid, SysFloatRepColumnInfo.class);
		for (SysFloatRepColumnInfo sysFloatRepColumnInfo : colinfos) {
			if (stringSet(sysFloatRepColumnInfo.sumtype)) {
				JSONObject obj = new JSONObject(sysFloatRepColumnInfo.sumtype);
				if (sumtype.equals(obj.getString("value"))) {
					sumcolname = sysFloatRepColumnInfo.colname;
					break;
				}
			}
		}
		return sumcolname;
	}
	
	/**
	 * 根据数据组sid以及模板sid获取对应数据量
	 * @param tmplsid
	 * @param groupsid
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 */
	public int countDataByGroupsid(String tmplsid, String groupsid) throws SQLException, Exception {
		SysFloatRepTmpl tmpl = dao.getBy(tmplsid, SysFloatRepTmpl.class);
		Integer ct = (Integer) DBUtil.querySingleVal("select count(1) from " + tmpl.dbtablename + " where group_sid=?", groupsid, DataType.Integer);
		return ct;
	}
	
	public int countByInitGroupsidAndGroupsidAndSumtypeHasVal(String tmplsid, String initGroupsid, List<Object> groupsids, String sumtype, String value) throws Exception {
		SysFloatRepTmpl tmpl = dao.getBy(tmplsid, SysFloatRepTmpl.class);
		List<Object> initsids = DBUtil.querySingleValList("select sid from " + tmpl.dbtablename + " where group_sid=?", initGroupsid, DataType.String);
		
		String sumcolname = getSumcolname(tmplsid, sumtype);
		if (stringSet(sumcolname)) {
			Integer ct = (Integer) DBUtil.querySingleVal(
					"select count(distinct(initsid)) from " + 
							tmpl.dbtablename + " where initsid in ('"+join("','", initsids)+"') and group_sid in ('"+join("','", groupsids)+"') and "+sumcolname+"=?", 
							new Object[]{value}, DataType.Integer);
			return ct;
		}
		return 0;
	}
	
	/**
	 * 根据条件获取所有的initsid
	 * @param tmplsid
	 * @param initGroupsid
	 * @param groupsids
	 * @param sumtype
	 * @param value
	 * @return
	 * @throws Exception
	 */
	public List<String> getByInitGroupsidAndGroupsidAndSumtypeHasVal(String tmplsid, String initGroupsid, List<Object> groupsids, String sumtype, String value) throws Exception {
		SysFloatRepTmpl tmpl = dao.getBy(tmplsid, SysFloatRepTmpl.class);
		List<Object> initsids = DBUtil.querySingleValList("select sid from " + tmpl.dbtablename + " where group_sid=?", initGroupsid, DataType.String);
		List<String> initlist = new ArrayList<String>();
		String sumcolname = getSumcolname(tmplsid, sumtype);
		if (stringSet(sumcolname)) {
			List<Object> vallist = DBUtil.querySingleValList(
					"select distinct(initsid) from " + 
							tmpl.dbtablename + " where initsid in ('"+join("','", initsids)+"') and group_sid in ('"+join("','", groupsids)+"') and "+sumcolname+"=?", 
							new Object[]{value}, DataType.String);
			for (Object object : vallist) {
				initlist.add(object.toString());
			}
		}
		return initlist;
	}
	
	/***
	 * 根据模板id, 初始化数据组id, 数据组id组， 统计值类型， 统计值 获取对应的初始化数据项sid数组
	 * @param tmplsid
	 * @param initGroupsid
	 * @param groupsids
	 * @param sumtype
	 * @param value
	 * @return
	 * @throws Exception
	 */
	public List<String> getInitsidsByInitGroupsidAndGroupsidAndSumtypeHasVal(String tmplsid, String initGroupsid, List<Object> groupsids, String sumtype, String value) throws Exception {
		List<String> theinitsids = new ArrayList<String>();
		
		SysFloatRepTmpl tmpl = dao.getBy(tmplsid, SysFloatRepTmpl.class);
		List<Object> initsids = DBUtil.querySingleValList("select sid from " + tmpl.dbtablename + " where group_sid=?", initGroupsid, DataType.String);
		
		String sumcolname = getSumcolname(tmplsid, sumtype);
		if (stringSet(sumcolname)) {
			List<Object> results= (List<Object>) DBUtil.querySingleValList(
					"select distinct(initsid) from " + 
							tmpl.dbtablename + " where initsid in ('"+join("','", initsids)+"') and group_sid in ('"+join("','", groupsids)+"') and "+sumcolname+"=?", 
							new Object[]{value}, DataType.String);
			for (Object object : results) {
				theinitsids.add(object.toString());
			} 
		}
		return theinitsids;
	}
	
	/**
	 * 根据模板sid, 数据组sid, 统计类型以及对应值，查找对应的数据sid
	 * @param tmplsid
	 * @param groupsid
	 * @param sumtype
	 * @param value
	 * @return
	 * @throws Exception 
	 */
	public List<String> getDatasidsByGroupsidAndSumtypeHasVal(String tmplsid, String groupsid, String sumtype, String value) throws Exception {
		List<String> thedatasids = new ArrayList<String>();
		SysFloatRepTmpl tmpl = dao.getBy(tmplsid, SysFloatRepTmpl.class);
		String sumcolname = getSumcolname(tmplsid, sumtype);
		if (stringSet(sumcolname)) {
			List<Object> objs = DBUtil.querySingleValList(
					"select sid from " + tmpl.dbtablename + " where group_sid=? and "+sumcolname+"=?", new Object[]{groupsid, value}, DataType.String);
			for (Object object : objs) {
				thedatasids.add(object.toString());
			}
		}
		return thedatasids;
	}
	
	/**
	 * 合并对应模板的多个数据组为一个数据组，并返回新的数据组id
	 * @param tmplsid 模板sid
	 * @param groupsids 多个数据组sid
	 * @return
	 */
	public String mergeTmplData(String tmplsid, String[] groupsids) throws Exception {
		SysFloatRepTmpl tmpl = dao.getBy(tmplsid, SysFloatRepTmpl.class);
		
		List<SysFloatRepColumnInfo> colinfos = dao.query("select * from SYS_FLOATREP_DBCOLINFO where tmplsid=? order by sid"
				, tmplsid, SysFloatRepColumnInfo.class);
		
		List<String> colnames = new ArrayList<String>();
		for (SysFloatRepColumnInfo colinfo : colinfos) {
			colnames.add(colinfo.colname);
		}
		String newgroupsid = SpeedIDUtil.getId();
		
		List<Map<String, Object>> results = DBUtil.query("select * from " + tmpl.dbtablename + " where group_sid in ('" + join("','", groupsids) + "') order by group_sid");
		
		for (int i = 0; i < results.size(); i++) {
			StringBuffer sb = new StringBuffer();
			List<Object> params = new ArrayList<Object>();
			Set<Entry<String, Object>> entrys = results.get(i).entrySet();
			results.get(i).put("sid", SpeedIDUtil.getId());
			results.get(i).put("group_sid", newgroupsid);
			sb.append("insert into " + tmpl.dbtablename + "(");
			for (Entry<String, Object> entry : entrys) {
				sb.append(entry.getKey() + ",");
				params.add(entry.getValue());
			}
			sb.replace(sb.length()-1, sb.length(), "");
			sb.append(") values(");
			for (Entry<String, Object> entry : entrys) {
				sb.append("?"+",");
			}
			sb.replace(sb.length()-1, sb.length(), "");
			sb.append(")");
			dao.updateBySQL(sb.toString(), params);
		}
		
		return newgroupsid;
	}
	
}
