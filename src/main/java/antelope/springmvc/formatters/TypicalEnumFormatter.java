package antelope.springmvc.formatters;

import java.io.IOException;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.dom4j.DocumentException;

import antelope.springmvc.BaseComponent;
import antelope.utils.TextUtils;
import antelope.utils.XmlEnumItem;

/**
 * 典型的枚举格式化器
 * @author lining
 * @since 2012-5-31
 */
public class TypicalEnumFormatter extends Format {
	
	private String xmlname;
	private Map<String, XmlEnumItem> enumItems = new HashMap<String, XmlEnumItem>();
	
	public TypicalEnumFormatter(String xmlname, String locale) {
		if (!TextUtils.stringSet(xmlname)) {
			Assert.fail("枚举格式化器必须传入枚举对应的xml文件名（不包含扩展名）");
		}
		this.xmlname = xmlname;
		XmlEnumItem[] enumItemLocals = new XmlEnumItem[0];
		try {
			enumItemLocals = BaseComponent.getXmlEnumItems(xmlname, locale);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		for (XmlEnumItem xmlEnumItem : enumItemLocals) {
			enumItems.put(xmlEnumItem.value, xmlEnumItem);
		}
	}

	private static final long serialVersionUID = 1L;

	@Override
	public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
		if (obj == null)
			return new StringBuffer();
		String label = "";
		if (enumItems.get(obj.toString()) != null)
			label = enumItems.get(obj.toString()).label;
		return label == null ? new StringBuffer() : new StringBuffer(label);
	}

	@Override
	public Object parseObject(String source, ParsePosition pos) {
		Assert.fail("TypicalEnumFormatter 格式化器不支持反向序列化");
		return null;
	}

}
