package antelope.interfaces;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import antelope.beans.ToviewListItem;
import antelope.services.SessionService;

/**
 * 全局待阅事项接口
 * 
 */
public interface ToviewListDataProvider {
	public List<ToviewListItem> getToviewListItems(SessionService service,HttpServletRequest req) throws Exception;
	 
}
