package antelope.system;

import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.springframework.transaction.TransactionStatus;

import antelope.db.DBUtil;
import antelope.services.SessionService;
import antelope.services.SystemPageInfoService;
import antelope.services.UserRoleOrgService;
import antelope.springmvc.SpringUtils;
import antelope.utils.ClasspathResourceUtil;
import antelope.utils.MenuUtil;
import antelope.utils.SystemOpts;

import com.opensymphony.xwork2.util.TextUtils;

/**
 * 系统级过滤器
 * 
 * @author lining
 * 
 * @WebFilter({ "*.do", "*.jsp", "*.action", "*.vot" })
 */
public class SystemFilter implements Filter {

	@Override
	public void destroy() {
	}
	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest httpReq = (HttpServletRequest) req;
		//System.out.println(httpReq.getRequestURL() + "?" + httpReq.getQueryString());
		
	
		
		SessionService sessionService = (SessionService) httpReq.getSession()
				.getAttribute("service");
		if (sessionService == null) {
			sessionService = new SessionService(httpReq.getSession()); 
			httpReq.getSession().setAttribute("service", sessionService);
		}
		
		
		
		if (httpReq.getParameter("token") != null) {
			
			
			if (sessionService.getUsername() == null) {
				try {
					TransactionStatus beginTransaction = SpringUtils.beginTransaction();
					String username2 = null;
					try {
						username2 = DBUtil.querySingleString("select usersid username from FJH_TOKEN_APP where tokensid = ? ", new Object[]{req.getParameter("token")});
					} catch(Exception e) {
						SpringUtils.rollbackTransaction(beginTransaction);
					}finally {
						SpringUtils.commitTransaction(beginTransaction);
					}
					putPropVal(sessionService, "username", username2);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			chain.doFilter(req, res);
			return;
		}
		
		// 无论是登录还是没有登录，都需要检测一下当前要访问的页面是否为不需要登录即可访问的页面，若是则直接通过
		try {
			if (isFilteredByNeedOrNotLoginConfig(httpReq, !"false".equals(SystemOpts.getProperty("use_no_need_login")))) {
				chain.doFilter(req, res);
				return;
			}
		} catch (DocumentException e1) {
			e1.printStackTrace();
		}

		List<Element> elems = getRelatedFuncElems(httpReq, sessionService);
		
		if (httpReq.getSession().getAttribute("galaxy.userid") == null) {
			// noneedlogin="true"时，不需要登录状态验证
			for (Element elem : elems) {
				if ("true".equals(elem.attributeValue("noneedlogin"))) {
					if (!res.isCommitted())
						chain.doFilter(req, res);
					return;
				}
			}
			
			try {
				if ("false".equals(SystemOpts.getProperty("use_no_need_login")))
					doFilterByNeedLogin(req, res, chain, httpReq);
				else
					doFilterByNoNeedLogin(req, res, chain, httpReq);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			// 判断是否有权限访问该页面 begin
			
			boolean hasright = false;
			if (elems.isEmpty() ||  "true".equals(elems.get(0).attributeValue("ispublic"))) {
				hasright = true;
			} else {
				TransactionStatus status = SpringUtils.beginTransaction();
				UserRoleOrgService service = SpringUtils.getBean(UserRoleOrgService.class);
				Set<String> authoredfuncs = new HashSet<String>();
				if (sessionService != null && sessionService.isAdmin()) {
					hasright = true;
				} else {
					try {
						authoredfuncs = service.getAuthorities(sessionService.getUsersid());
						for (Element element : elems) {
							if (authoredfuncs.contains(element.attributeValue("id"))) {
								hasright = true;
								break;
							}
						}
					} catch (SQLException e) {
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				SpringUtils.commitTransaction(status);
			}
			
			// 判断是否有权限访问该页面 end		
			SystemPageInfoService pageinfo = SpringUtils.getBean(SystemPageInfoService.class);
			if (hasright) {
				// 角色自定义首页
				String uri = httpReq.getRequestURI();
				if ((httpReq.getContextPath() + "/").equals(uri) || (httpReq.getContextPath()).equals(uri)) {
					try {
						((HttpServletResponse) res).sendRedirect(((HttpServletRequest) req).getContextPath() + pageinfo.getIndexPagePos(httpReq));
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					chain.doFilter(req, res);
				}
			} else {
				pageinfo.dealWithSessionExpired((HttpServletRequest) req, (HttpServletResponse) res, false);
			}
		}
	}
	
	private void putPropVal(SessionService service, String fieldName, Object val) {
		try {
			Field propfield = SessionService.class.getDeclaredField(fieldName);
			propfield.setAccessible(true);
			propfield.set(service, val);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取module-cfg中与当前所访问url相关的节点
	 */
	private List<Element> getRelatedFuncElems(HttpServletRequest httpReq,
			SessionService sessionService) throws IOException {
		Document dom = null;
		try {
			dom = MenuUtil.getModuleXml(sessionService.getLocale());
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		String path = httpReq.getRequestURI().replaceFirst(httpReq.getContextPath(), "");
		
		// 去除uri访问尾部(类名，方法名)
		if (path.endsWith(".vot")) {
			path = path.replaceFirst("(/[^/]*/[^/]*(\\.jsp|\\.vot))$", "") + ".";
		} else {
			path = path.replaceFirst("((\\.jsp|\\.vot))$", "") + ".";
		}
		
		List<Element> elems = null;
		
	
		if (".".equals(path)) {// 防止为两级访问目录，但却是登录后允许访问的路径导致的问题
			elems = new ArrayList<Element>();
		} else {// 限制所有与改功能相关的访问权限
			elems = dom.selectNodes("//*[contains(@path,'"+path+"')]");
		}
		
		return elems;
	}
	
	/**
	 * 根据need-login-url.xml配置判断是否允许访问
	 */
	private void doFilterByNeedLogin(ServletRequest req, ServletResponse res, FilterChain chain, HttpServletRequest httpReq) throws Exception {
		
		// 客户在尝试访问首页
		if (httpReq.getRequestURI().equals(httpReq.getContextPath()) || httpReq.getRequestURI().equals(httpReq.getContextPath() + "/")) {
			SystemPageInfoService pageinfo = SpringUtils.getBean(SystemPageInfoService.class);
			((HttpServletResponse) res).sendRedirect(((HttpServletRequest) req).getContextPath() + pageinfo.getIndexPagePos(httpReq));
			return;
		}
		
		// 未登录时核查必须登录之后才能显示的页面
		boolean fileterd = isFilteredByNeedOrNotLoginConfig(httpReq, false);
	
		// 若发现当前页面是必须登陆后才能查看的页面则跳转到登录页面
		if (!fileterd && !res.isCommitted()) {
			chain.doFilter(req, res);
		} else {
			SystemPageInfoService pageinfo = SpringUtils.getBean(SystemPageInfoService.class);
			pageinfo.dealWithSessionExpired((HttpServletRequest) req, (HttpServletResponse) res, true);
		}
	}
	
	private boolean isFilteredByNeedOrNotLoginConfig(HttpServletRequest httpReq, boolean use_no_need_login) throws IOException, DocumentException {
		if (use_no_need_login) {
			List<Element> elems = ClasspathResourceUtil.getXMLDocumentByPath("/no-need-login-url.xml").selectNodes("//url");
			for (Element element : elems) {
				if (httpReq.getRequestURI().endsWith(element.getText()) || Pattern.matches(element.getText(), httpReq.getRequestURI())
						) {
					return true;
				}
			}
		} else {
			List<Element> elems = ClasspathResourceUtil.getXMLDocumentByPath("/need-login-url.xml").selectNodes("//url");
			for (Element element : elems) {
				if (httpReq.getRequestURI().endsWith(element.getText()) || Pattern.matches(element.getText(), httpReq.getRequestURI())) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * 根据no-need-login-url.xml配置判断是否允许访问
	 * @throws DocumentException 
	 */
	private void doFilterByNoNeedLogin(ServletRequest req, ServletResponse res, FilterChain chain, HttpServletRequest httpReq) throws IOException,
			ServletException, DocumentException {
		boolean fileterd = isFilteredByNeedOrNotLoginConfig(httpReq, true);
		if (fileterd && !res.isCommitted())
			chain.doFilter(req, res);
		else {
			if (TextUtils.stringSet(httpReq.getQueryString())) {
				req.setAttribute("ssourl", httpReq.getRequestURI().replaceFirst(httpReq.getContextPath(), "") + "?" + httpReq.getQueryString());
				((HttpServletRequest) req).getSession().setAttribute("ssourl", httpReq.getRequestURI() + "?" + httpReq.getQueryString());
			} else {
				req.setAttribute("ssourl", httpReq.getRequestURI().replaceFirst(httpReq.getContextPath(), ""));
				((HttpServletRequest) req).getSession().setAttribute("ssourl", httpReq.getRequestURI());
			}
			
			if (!fileterd) {
				SystemPageInfoService pageinfo = SpringUtils.getBean(SystemPageInfoService.class);
				pageinfo.dealWithSessionExpired((HttpServletRequest) req, (HttpServletResponse) res, true);
			}
		}
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}
	

}
