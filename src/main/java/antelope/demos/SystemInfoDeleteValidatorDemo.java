package antelope.demos;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import antelope.interfaces.SystemInfoDeleteValidator;


@Component
public class SystemInfoDeleteValidatorDemo extends SystemInfoDeleteValidator {

	@Override
	public String validateDeleteSysUser(String sid,
			HttpServletRequest req) {
		return null;
	}

}
