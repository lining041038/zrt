
package antelope.springmvc;

import java.text.Format;

import org.springframework.stereotype.Component;

import antelope.springmvc.formatters.TypicalDateFormatter;
import antelope.springmvc.formatters.TypicalEnumFormatter;
import antelope.springmvc.formatters.TypicalNumberFormatter;
import antelope.springmvc.formatters.TypicalPercentageFormatter;
import antelope.springmvc.formatters.TypicalTimeFormatter;

/**
 * 格式化器工厂，提供系统标准格式化器
 * @author lining
 * @since 2012-5-31
 */
@Component
public class FormatterFactory {
	private TypicalPercentageFormatter format = new TypicalPercentageFormatter();
	private TypicalDateFormatter dateformatter = new TypicalDateFormatter();
	private TypicalTimeFormatter timeformatter = new TypicalTimeFormatter();
	private TypicalNumberFormatter numformatter = new TypicalNumberFormatter();
	
	public Format typicalDateFormatter() {
		return dateformatter;
	}
	
	public Format typicalTimeFormatter() {
		return timeformatter;
	}
	
	public Format typicalPercentageFormatter() {
		return format;
	}
	
	public Format typicalNumberFormatter() {
		return numformatter;
	}
	
	public Format typicalEnumFormatter(String xmlname, String locale) {
		return new TypicalEnumFormatter(xmlname, locale);
	}
}
