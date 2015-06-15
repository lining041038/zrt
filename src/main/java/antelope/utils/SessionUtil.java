package antelope.utils;

import javax.servlet.http.HttpSession;

import antelope.services.SessionService;

/**
 * Session工具类
 * @author lining
 * @since 2013-1-13
 */
public class SessionUtil {
	
	/**
	 * 在最大范围内寻找SessionService， 当传入的service 不为空则直接用它，否则尝试从session当中寻找
	 * 若session本身为空则返回空。否则获取或创建新的SessionService,然后返回
	 * @param service
	 * @param session
	 * @return
	 */
	public static SessionService getSessionService(SessionService service, HttpSession session) {
		
		if (service != null)
			return service;
		
		if (session == null)
			return null;
		
		if (session.getAttribute("service") == null)
			session.setAttribute("service", new SessionService(session));
		
		return (SessionService) session.getAttribute("service");
	}
}
