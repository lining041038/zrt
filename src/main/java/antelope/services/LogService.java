package antelope.services;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;


import antelope.springmvc.BaseComponent;
import antelope.utils.TextUtils;


/**
 * 日志服务类
 * @author lining
 * @since 2013-11-12
 */
@Service
public class LogService extends BaseComponent {
	
	
	/**
	 * 添加或更新操作日志
	 * @param itemMetaName 对象元数据名称
	 * @param update 是否为更新操作
	 * @param req http请求对象
	 */
	public <A> void addOrUpdateOne(String itemMetaName, boolean update, String name, HttpServletRequest req) {
		String username = TextUtils.noNull(getService(req).getUsername(), "Anonymous");
		String operat = "添加了";
		if (update) {
			operat = "更新了";
		}
		name = TextUtils.noNull(name);
		if (name.length() > 20) {
			name = name.substring(0, 20) + "...";
		}
		log.info("[" + username + "]" + operat + "[" + itemMetaName + "]名称为[" + name + "]");
	}
	
	
	public void info(String message, HttpServletRequest req) {
		String username = TextUtils.noNull(getService(req).getUsername(), "Anonymous");
		log.info("[" + username + "]" + message);
	}
	
}



