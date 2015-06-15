package antelope.springmvc.formatters;

import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;

import junit.framework.Assert;

/**
 * 典型的百分数格式化器 123,456.20%
 * @author lining
 * @since 2012-5-31
 */
public class TypicalPercentageFormatter extends Format {

	private static final long serialVersionUID = 1L;

	@Override
	public StringBuffer format(Object number, StringBuffer toAppendTo, FieldPosition pos) {
		if (number == null)
			return new StringBuffer();
		
		Number num = (Number)number;
		num = num.doubleValue();
		DecimalFormat dcformat = new DecimalFormat("###,##0.00%");
		return dcformat.format(num, new StringBuffer(), new FieldPosition(0));
	}

	@Override
	public Object parseObject(String source, ParsePosition pos) {
		Assert.fail("TypicalPercentageFormatter 格式化器不支持反向序列化");
		return null;
	}

}
