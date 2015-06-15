package antelope.services;

import java.io.IOException;

import javax.annotation.Resource;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.springframework.stereotype.Service;

import antelope.springmvc.JPABaseDao;
import antelope.utils.ClasspathResourceUtil;

/**
 * 地图区域相关数据服务
 * @author lining
 */
@Service
public class MapRegionService {
	@Resource(name="jpabasedao")
	private JPABaseDao dao;
	
	
	public Document getMapTmpl(String filename) throws IOException, DocumentException {
		return ClasspathResourceUtil.getXMLDocumentByPath("/maptmpls/"+filename+".xml");
	}
	
	public String getRegionNameBySid(String sid) throws IOException, DocumentException {
		Document regions = ClasspathResourceUtil.getXMLDocumentByPath("/map-regions.xml");
		return ((Element) regions.selectSingleNode("/map/region[@sid="+sid+"]")).attributeValue("name");
	}
}
