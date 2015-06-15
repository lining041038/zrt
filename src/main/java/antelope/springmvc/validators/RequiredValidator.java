package antelope.springmvc.validators;

import antelope.utils.I18n;
import antelope.utils.TextUtils;


public class RequiredValidator implements Validator {
	
	@Override
	public String validate(FormField formField, I18n i18n) {
		
		if (TextUtils.stringSet(formField.value)) {
			return null;
		}
		if (i18n == null)
			return "请填写" + formField.label;
		else
			return i18n.get("antelope.validate.pleaseinputit") + formField.label;
	}
	
}
