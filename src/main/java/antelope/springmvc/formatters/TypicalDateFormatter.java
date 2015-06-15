package antelope.springmvc.formatters;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;

import junit.framework.Assert;

public class TypicalDateFormatter extends Format {

	@Override
	public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
		if (obj == null)
			return new StringBuffer();
		if (obj.toString().length() < 10)
			return new StringBuffer(obj.toString());
		
		return new StringBuffer(obj.toString().substring(0, 10));
	}

	@Override
	public Object parseObject(String source, ParsePosition pos) {
		Assert.fail("typicalDateFormatter 格式化器不支持反向序列化");
		return null;
	}

}
