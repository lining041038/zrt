package antelope.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import antelope.db.DBUtil;
import antelope.entities.SysMapRegionRelate;
import antelope.entities.SysUnit;
import antelope.springmvc.BaseController;
import antelope.utils.ClasspathResourceUtil;
import antelope.utils.JSONArray;
import antelope.utils.JSONException;
import antelope.utils.JSONObject;
import antelope.utils.SpeedIDUtil;


/**
 * 数据地图相关控制器
 * @author lining
 */
@Controller
public class DataMapController extends BaseController {
	
	/**
	 * 获取省级相关信息
	 * @param res
	 * @throws DocumentException 
	 * @throws IOException 
	 * @throws JSONException 
	 */
	@RequestMapping("/common/getprovinceinfos")
	public void getProvinceinfos(HttpServletResponse res) throws IOException, DocumentException, JSONException {
		Document regions = ClasspathResourceUtil.getXMLDocumentByPath("/map-regions.xml");
		List<Element> elems = regions.getRootElement().elements();
		JSONArray provinces = new JSONArray();
		
		for (int i = 0; i < elems.size(); i++) {
			Element elem = elems.get(i);
			if (elem.attributeValue("sid").endsWith("0000")) {
				JSONObject obj = new JSONObject();
				obj.put("name", elem.attributeValue("name"));
				obj.put("sid", elem.attributeValue("sid"));
				provinces.put(obj);
			}
		}
		
		getOut(res).print(provinces);
	}
	
	/**
	 * 获取所有地图对应关系信息
	 */
	@RequestMapping("/common/getAllMapRelateInfos")
	public void getAllMapRelateInfos(HttpServletResponse res) throws IOException, Exception {
		getOut(res).print(toJSONArrayBy(dao.getAll(SysMapRegionRelate.class)));
	}
	
	/**
	 * 根据省sid获取市级信息
	 * @param res
	 * @throws IOException 
	 * @throws DocumentException 
	 * @throws JSONException 
	 */
	@RequestMapping("/common/getCityInProvince")
	public void getCityInProvince(String sid, HttpServletResponse res) throws IOException, DocumentException, JSONException {
		Document regions = ClasspathResourceUtil.getXMLDocumentByPath("/map-regions.xml");
		List<Element> elems = regions.getRootElement().elements();
		JSONArray cities = new JSONArray();
		
		for (int i = 0; i < elems.size(); i++) {
			Element elem = elems.get(i);
			if (elem.attributeValue("sid").startsWith(sid.substring(0,2)) && !elem.attributeValue("sid").endsWith("0000")) {
				JSONObject obj = new JSONObject();
				obj.put("name", elem.attributeValue("name"));
				obj.put("sid", elem.attributeValue("sid"));
				cities.put(obj);
			}
		}
		
		getOut(res).print(cities);
	}

	/**
	 * 保存区域关联信息
	 * @param unitsid
	 * @param regionsid
	 */
	@RequestMapping("/common/saveRegionRelate")
	public void saveRegionRelate(String unitsid, String regionsid, String regionname, HttpServletResponse res) throws Exception {
		regionname = decodeAndTrim(regionname);
		unitsid = decodeAndTrim(unitsid);
		if (regionsid.endsWith("0000"))
			dao.updateBySQL("delete from SYS_MAPREGION_UNIT_RELATE where unitsid=? and regionsid "+leftLikePart(), new Object[]{unitsid, "0000"});
		else
			dao.updateBySQL("delete from SYS_MAPREGION_UNIT_RELATE where unitsid=? and regionsid not "+likePart(), new Object[]{unitsid, "0000"});
		SysUnit unit = dao.getBy(unitsid, SysUnit.class);
		dao.updateBySQL("insert into SYS_MAPREGION_UNIT_RELATE(sid, regionsid, regionname, unitsid, unitname) values(?,?,?,?,?)",
				new Object[]{SpeedIDUtil.getId(), regionsid, regionname, unit.sid, unit.name});
	}
	
	/**
	 * 获取组织机构关联区域信息
	 * @param unitsid
	 */
	@RequestMapping("/common/getCurrOrgRelateRegion")
	public void getCurrOrgRelateRegion(String unitsid, HttpServletResponse res) throws Exception {
		unitsid = decodeAndTrim(unitsid);
		JSONObject obj = new JSONObject();
		List<SysMapRegionRelate> region = null;
		region = dao.query("select * from SYS_MAPREGION_UNIT_RELATE where unitsid=? and regionsid "+leftLikePart(), 
				new Object[]{unitsid, "0000"}, SysMapRegionRelate.class);
		if (!region.isEmpty())
			obj.put("prov", new JSONObject(region.get(0)));
		
		region = dao.query("select * from SYS_MAPREGION_UNIT_RELATE where unitsid=? and regionsid not "+leftLikePart(), 
				new Object[]{unitsid, "0000"}, SysMapRegionRelate.class);
		if (!region.isEmpty())
			obj.put("city", new JSONObject(region.get(0)));
		getOut(res).print(obj);
	}
	
	/**
	 * 删除地图机构关系关联
	 * @param unitsid
	 * @param res
	 */
	@RequestMapping("/common/removeRelateMapData")
	public void removeRelateMapData(String unitsid, HttpServletResponse res) {
		dao.updateBySQL("delete from SYS_MAPREGION_UNIT_RELATE where unitsid=?", new Object[]{unitsid});
	}
	
	/**
	 * 删除地图省关系关联
	 * @param unitsid
	 * @param res
	 */
	@RequestMapping("/common/removeProvinceRelateMapData")
	public void removeProvinceRelateMapData(String unitsid, HttpServletResponse res) {
		dao.updateBySQL("delete from SYS_MAPREGION_UNIT_RELATE where unitsid=? and regionsid like '%0000'", new Object[]{unitsid});
	}
	
	/**
	 * 导出地图当前图片
	 * @param imgname
	 * @param res
	 */
	@RequestMapping("/common/DataMapController/exportFlashImg")
	public void exportFlashImg(String imgname, HttpServletResponse res) throws IOException {
		String parentpath = ClasspathResourceUtil.getWebappFolderFile("/").getParent();
		FileInputStream fis = new FileInputStream(new File(parentpath + "/exportimgs/" + imgname));
		byte[] byts = new byte[fis.available()];
		fis.read(byts);
		fis.close();
		printBytes(byts, imgname, res);
	}
}




















