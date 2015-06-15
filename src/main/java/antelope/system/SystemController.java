package antelope.system;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;

import antelope.beans.UserLoginEvent;
import antelope.consts.GlobalConsts;
import antelope.entities.SysUnit;
import antelope.entities.SysUser;
import antelope.listeners.UserLoginListener;
import antelope.services.SessionService;
import antelope.services.SystemPageInfoService;
import antelope.services.UserRoleOrgService;
import antelope.springmvc.BaseController;
import antelope.springmvc.SpringUtils;
import antelope.utils.I18n;
import antelope.utils.MenuUtil;
import antelope.utils.SecurityUtil;
import antelope.utils.SpeedIDUtil;
import antelope.utils.SystemOpts;
import antelope.utils.TextUtils;

/**
 * <p>Title: 系统controller类，完成常见系统信息的获取</p>
 * <p>Copyright: Smartdot Corporation Copyright (c) 2011</p>
 * <p>Company: BEIJING Smartdot SOFTWARE CO.,LTD</p>
 * @author lining
 * @version 1.0
 */
@Controller
public class SystemController extends BaseController{
	
	@Resource
	private UserRoleOrgService service;
	
	/**
	 * 获取系统三级菜单相关信息
	 * @param res
	 * @throws Exception 
	 * @throws SQLException 
	 */
	@RequestMapping("/getMenuInfo")
	public void getMenuInfo(HttpServletRequest req, HttpServletResponse res) throws SQLException, Exception {
		getOut(res).print(MenuUtil.getMenuJSONAndFilter((SessionService)req.getSession().getAttribute("service"), service).toString()); // 本系统方式取得菜单配置
	}
	
	/**
	 * 获取已发布的异步信息
	 * @param res
	 * @throws IOException 
	 */
	@RequestMapping("/getRoutedMessage")
	public void getRoutedMessage(HttpServletRequest req, HttpServletResponse res) throws IOException {
		String dest = req.getParameter("dest");
		Assert.notNull(dest, "参数dest为发布异步信息目标ID，不能为空");
		SessionService theservice = getService(req);
		if (theservice != null)
			getOut(res).print(SystemCache.popAsyncMessage(dest, theservice.getUsername()));
	}
	
	/**
	 * 非ldap方式的系统登录
	 * @throws Exception 
	 */
	@RequestMapping("/LoginAction")
	public String loginAction(HttpServletRequest req, HttpServletResponse res) throws Exception {
		I18n i18n = getI18n(req);
		
		
		// 已经登录成功的人，复制URL后不需要再重新登录，直接跳转到对应的首页
		if (SessionService.isLogon(req) && req.getParameter("username") == null) {
			SystemPageInfoService pageinfo = SpringUtils.getBean(SystemPageInfoService.class);
			return pageinfo.getIndexPagePos(req).replaceFirst("\\.jsp$", "");
		}
		
		// 登录验证码校验
		req.setAttribute("loginerror", "");
		if ("true".equals(SystemOpts.getProperty("useverifycode"))) {
			if (req.getSession().getAttribute("rand") == null) {
				req.setAttribute("loginerror", i18n.get("antelope.verifycodeerror"));
				return SystemOpts.getProperty("loginpagepos").replaceFirst("\\.jsp$", "");
			}
			
			if (!req.getSession().getAttribute("rand").equals(req.getParameter("verifycode"))) {
				req.setAttribute("loginerror", i18n.get("antelope.verifycodeerror"));
				return SystemOpts.getProperty("loginpagepos").replaceFirst("\\.jsp$", "");
			}
		}
		
		String password = req.getParameter("password");
		password = noNull(password);
		List<SysUser> users = dao.query("select * from SYS_USER where username=? and password=?", 
				new Object[]{req.getParameter("username"), SecurityUtil.sha1(password.getBytes())}, SysUser.class);
		
		if (users.isEmpty()) {
			req.setAttribute("loginerror", i18n.get("antelope.usernameorpwderror"));
			return SystemOpts.getProperty("loginpagepos").replaceFirst("\\.jsp$", "");
		} else {
			HttpSession session = req.getSession();
			SysUser logonuser = users.get(0);
			
			if (session.getAttribute("service") == null) 
				session.setAttribute("service", new SessionService(session));
			SessionService sessionService = (SessionService) session.getAttribute("service");
			
			putPropVal(sessionService, "username", logonuser.username);
			putPropVal(sessionService, "user", logonuser.name);
			putPropVal(sessionService, "usersid", logonuser.sid);
			
			session.setAttribute("galaxy.userid", logonuser.username);
			
			List<SysUnit> units = service.getUnitsByUsersid(logonuser.sid);
			if (units.size() > 0) {
				putPropVal(sessionService, "deptname", units.get(0).name);
				putPropVal(sessionService, "deptcode", units.get(0).code);
			} else {
				putPropVal(sessionService, "deptname", i18n.get("antelope.orgnizationroot"));
				putPropVal(sessionService, "deptcode", i18n.get("antelope.orgnizationroot"));
			}
			triggerLoginEvent(logonuser);
			SystemPageInfoService pageinfo = SpringUtils.getBean(SystemPageInfoService.class);
			return pageinfo.getIndexPagePos(req).replaceFirst("\\.jsp$", "");
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
	
	private void triggerLoginEvent(SysUser sysuser) {
		List<UserLoginListener> loginLisener = SpringUtils.getBeans(UserLoginListener.class);
		
		UserLoginEvent event = new UserLoginEvent();
		event.sysuser = sysuser;
		for (UserLoginListener userLoginListener : loginLisener) {
			userLoginListener.afterLoginSuccess(event);
		}
		
	}
	
	/**
	 *  
	 */
	@RequestMapping("/system/SystemController/getPinyinByChinese")
	public void getPinyinByChinese(String chinese, HttpServletResponse res) throws IOException {
		chinese = decodeAndTrim(chinese);
		print(TextUtils.getPingyinByChineseNotIgnore(chinese), res) ;
	}
	
	/**
	 * SSO方式的系统登录
	 * @throws Exception 
	 */
	@RequestMapping("/SSOLoginAction")
	public String SSOLoginAction(HttpServletRequest req, HttpServletResponse res) throws Exception {
		I18n i18n = getI18n(req);
		//浏览器cookie信息 ,得到用户登录名
		String userName = "";
		Cookie[] userCookie = req.getCookies();
		if(userCookie!=null ){
			for(int i=0;i<userCookie.length;i++){
				if("UserAccount".equalsIgnoreCase(userCookie[i].getName()))
						userName = userCookie[i].getValue();
			}
			log.debug("Cookie Name="+userName);
		}
		List<SysUser> users = dao.query("select * from SYS_USER where username=?", 
				new Object[]{userName}, SysUser.class);
		
		if (users.isEmpty()) {
			((HttpServletResponse) res).sendRedirect(((HttpServletRequest) req).getContextPath() + SystemOpts.getProperty("loginpagepos"));
			return SystemOpts.getProperty("loginpagepos").replaceFirst("\\.jsp$", "");
		} else {
			HttpSession session = req.getSession();
			SysUser logonuser = users.get(0);
			
			if (session.getAttribute("service") == null) 
				session.setAttribute("service", new SessionService(session));
			SessionService sessionService = (SessionService) session.getAttribute("service");
			
			putPropVal(sessionService, "username", logonuser.username);
			putPropVal(sessionService, "user", logonuser.name);
			putPropVal(sessionService, "usersid", logonuser.sid);
			
			session.setAttribute("galaxy.userid", logonuser.username);
			
			List<SysUnit> units = service.getUnitsByUsersid(logonuser.sid);
			if (units.size() > 0) {
				putPropVal(sessionService, "deptname", units.get(0).name);
				putPropVal(sessionService, "deptcode", units.get(0).code);
			} else {
				putPropVal(sessionService, "deptname", i18n.get("antelope.orgnizationroot"));
				putPropVal(sessionService, "deptcode", i18n.get("antelope.orgnizationroot"));
			}
			
			String ssourl = (String) req.getSession().getAttribute("ssourl");
			
			if (TextUtils.stringSet(ssourl)) {
				req.getSession().removeAttribute("ssourl");
				ssourl = ssourl.replaceFirst("\\/$", "");
				res.sendRedirect(ssourl);
				SystemPageInfoService pageinfo = SpringUtils.getBean(SystemPageInfoService.class);
				return pageinfo.getIndexPagePos(req).replaceFirst("\\.jsp$", "");
			}
			
			if (!stringSet((String) req.getAttribute("ssourl")) || "/".equals(req.getAttribute("ssourl").toString())) {
				SystemPageInfoService pageinfo = SpringUtils.getBean(SystemPageInfoService.class);
				return pageinfo.getIndexPagePos(req).replaceFirst("\\.jsp$", "");
			}
			return req.getAttribute("ssourl").toString();
		}
	}
	/**
	 * 清除cookie
	 * @throws Exception 
	 */
	public String SSOLogoutAction(HttpServletRequest req, HttpServletResponse res) throws Exception {
		Cookie[] userCookie = req.getCookies();
		for(int i=0;i<userCookie.length;i++){
			Cookie cookie = new Cookie(userCookie[i].getName(),null); 
			cookie.setMaxAge(0);
			res.addCookie(cookie); 
		}
		return "/singleLogin";
	}
	
	/**
	 * 从后台获取一个新的sid
	 */
	@RequestMapping("/common/getanewsid")
	public void getanewsid(HttpServletResponse res) throws IOException {
		getOut(res).print(SpeedIDUtil.getId());
	}
	
	/**
	 * 后台判断用户是否已经登录
	 * @param req
	 * @param res
	 * @throws IOException
	 */
	@RequestMapping("/common/isLogon")
	public void isLogon(HttpServletRequest req, HttpServletResponse res) throws IOException {
		print(SessionService.isLogon(req), res);
	}
	
	/**
	 * 获取系统属性参数 来源于 src/main/resource/ecm.properties文件
	 * @param propname
	 * @param res
	 */
	@RequestMapping("/common/getsysprop")
	public void getSysProp(String propname, HttpServletResponse res) throws IOException {
		getOut(res).print(SystemOpts.getProperty(propname));
	}
	
	/**
	 * 获取首页面
	 */
	@RequestMapping("/common/getIndexPagePos")
	public void getIndexPagePos(HttpServletRequest req, HttpServletResponse res) throws IOException, Exception {
		SystemPageInfoService pageInfo = SpringUtils.getBean(SystemPageInfoService.class);
		print(pageInfo.getIndexPagePos(req), res);
	}
	
	
	/**
	 * 获取验证码图片
	 */
	@RequestMapping("/common/SystemController/getVerifycodeImage")
	public void getVerifycodeImage(HttpServletRequest req, HttpServletResponse res) throws IOException {
		Assert.isTrue("true".equals(SystemOpts.getProperty("useverifycode")), "系统未开启登录验证码功能！");
		int width=60, height=20; 
		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		Graphics g = image.getGraphics();
		Random random = new Random();
		g.setColor(getRandColor(200, 250));
		g.fillRect(0, 0, width, height);
		g.setFont(new Font("Times New Roman", Font.PLAIN, 18));
		g.setColor(getRandColor(160, 200));
		for (int i = 0; i < 155; i++) {
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			int xl = random.nextInt(12);
			int yl = random.nextInt(12);
			g.drawLine(x, y, x + xl, y + yl);
		}
		String sRand = "";
		for (int i = 0; i < 4; i++) {
			String rand = String.valueOf(random.nextInt(10));
			sRand += rand;
			g.setColor(new Color(20 + random.nextInt(110), 20 + random
					.nextInt(110), 20 + random.nextInt(110)));
			g.drawString(rand, 13 * i + 6, 16);
		}
		// 将认证码存入SESSION
		req.getSession().setAttribute("rand", sRand);
		g.dispose();
		ImageIO.write(image, "JPEG", res.getOutputStream());
	}
	
	private Color getRandColor(int fc,int bc) { 
		Random random = new Random(); 
		if(fc>255) fc=255; 
		if(bc>255) bc=255; 
		int r=fc+random.nextInt(bc-fc); 
		int g=fc+random.nextInt(bc-fc); 
		int b=fc+random.nextInt(bc-fc); 
		return new Color(r,g,b); 
	} 
	
	/**
	 * 处理机构code，替换掉数字及// 
	 * 例如：国信证券股份有限公司/0006/行政管理部 --> 国信证券股份有限公司/行政管理部
	 * @param code
	 * @return
	 */
	@Deprecated
	public String getDeptAllPath(String code){
		String s = "组织机构";
		if(code!=null){
			Pattern p = Pattern.compile( "[0-9]*");   
			Matcher m = p.matcher( code );   
			s =  m.replaceAll("");
			s = s.replace("//", "/");
			s = s.replace("国信证券股份有限公司", "国信证券");
		}
		return s;
	}
}





