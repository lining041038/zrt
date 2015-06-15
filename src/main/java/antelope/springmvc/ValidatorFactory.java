package antelope.springmvc;

import org.springframework.stereotype.Component;

import antelope.springmvc.validators.RequiredValidator;
import antelope.springmvc.validators.Validator;

/**
 * 验证器工厂
 * @author lining
 * @since 2012-11-27
 */
@Component
public class ValidatorFactory {
	private RequiredValidator requiredValidator = new RequiredValidator();
	
	public Validator required() {
		return requiredValidator;
	}
	
}
