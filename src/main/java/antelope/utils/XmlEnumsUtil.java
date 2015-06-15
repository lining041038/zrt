package antelope.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * XML系统级枚举获取工具类
 * @author pc
 *
 */
public class XmlEnumsUtil {
	
	private static XmlEnumsUtil xmlEnumsUtil;
	
	private static Map<String, XmlEnumItem[]> cache = new HashMap<String, XmlEnumItem[]>();
	
	private XmlEnumsUtil() {
	}
	
	public static XmlEnumsUtil getInstance() {
		if (xmlEnumsUtil == null)
			xmlEnumsUtil = new XmlEnumsUtil(); 
		return xmlEnumsUtil;
	}
	
	public static XmlEnumItem[] getXmlEnumItems(String xmlname, String locale) throws IOException, DocumentException {
		
		String text = ClasspathResourceUtil.getTextByPathNoCached("/enums/" + xmlname + "_" + locale + ".xml");
		int k = 0;
		while(text.charAt(k) != '<'){
			++k;
		}
		List<Element> elems = DocumentHelper.parseText(text.substring(k)).getRootElement().elements();
		List<XmlEnumItem> xmlenums = new ArrayList<XmlEnumItem>();
		
		for (int i = 0; i < elems.size(); i++) {
			XmlEnumItem xmlenum = new XmlEnumItem();
			Element elem = elems.get(i);
			xmlenum.value = elem.attributeValue("value");
			xmlenum.label = elem.attributeValue("label");
			xmlenum.selected = "true".equals(elem.attributeValue("selected"));
			xmlenums.add(xmlenum);
		}
		XmlEnumItem[] enumsarr = xmlenums.toArray(new XmlEnumItem[0]);
		
		return enumsarr;
	}
}




