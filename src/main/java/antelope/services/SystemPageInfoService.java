package antelope.services;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;

import antelope.entities.SysRole;
import antelope.springmvc.BaseComponent;
import antelope.springmvc.SpringUtils;
import antelope.utils.SystemOpts;

/**
 * 系统页面信息服务类，用于获取系统中关于页面配置的相关信息
 * @author lining
 * @since 2012-8-21
 */
@Service
public class SystemPageInfoService extends BaseComponent{

	
	/**
	 * 根据用户登录信息，获取用户所应该登录的首页面
	 * 若用户存在于多个角色当中，则将不确定地取其中一个角色对应的首页面，
	 * 若用户所在所有角色均未包含角色首页面，则取system-opts中的indexpagepos属性值
	 */
	public String getIndexPagePos(HttpServletRequest req) throws Exception {
		String usersid = getService(req).getUsersid();
		UserRoleOrgService userroleorg = SpringUtils.getBean(UserRoleOrgService.class);
		List<SysRole> roles = userroleorg.getRolesByUsersid(usersid);
		for (int i = 0; i < roles.size(); ++i) {
			if (stringSet(roles.get(i).indexpath))
				return roles.get(i).indexpath;
		}
		return SystemOpts.getProperty("indexpagepos");
	}
	
	/**
	 * 对不同的请求后，当session过期时进行不同的处理
	 */
	public void dealWithSessionExpired(HttpServletRequest req, HttpServletResponse res, boolean isforward) throws IOException, ServletException {
		if (stringSet(req.getParameter("stopcache"))) {
			getOut(res).print(getI18n(req).get("antelope.pleaserelogin"));
		} else {
			if (isforward) {
				req.getRequestDispatcher(SystemOpts.getProperty("loginpagepos")).forward(req, res);
			} else {
				res.sendRedirect(((HttpServletRequest) req).getContextPath() + SystemOpts.getProperty("loginpagepos"));
			}
		}
	}
}


