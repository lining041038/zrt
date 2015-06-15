package antelope.springmvc.validators;

import antelope.utils.I18n;

/**
 * 基验证器
 * @author lining
 * @since 2012-11-27
 */
public interface Validator {
	public String validate(FormField formField, I18n i18n);
}
