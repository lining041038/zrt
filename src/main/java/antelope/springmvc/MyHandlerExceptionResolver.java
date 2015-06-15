package antelope.springmvc;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

/**
 * 系统统一处理异常位置
 * @author pc
 */
public class MyHandlerExceptionResolver implements HandlerExceptionResolver {
	private Logger log = Logger.getLogger(MyHandlerExceptionResolver.class);

	public ModelAndView resolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {
		log.fatal(ex.getMessage());
		ex.printStackTrace();
		return new ModelAndView("/error", null);
	}
}
