package antelope.interfaces.components.supportclasses;

import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import antelope.utils.JSONObject;

/**
 * 
 * @author lining
 * @since 2013-3-12
 */
public abstract class SelectionTreeFilter extends SelectionFilter {
	
	public final String getSupportjspname() {
		return  "selectiontreefilter.jsp";
	}
	
	public abstract List<JSONObject> getChildren(String sid, HttpServletRequest req, HttpServletResponse res) throws SQLException, Exception;
	
	public int getWidth() {
		return 360;
	}
	
	public int getHeight() {
		return 470;
	}
	
	public String getParamName() {
		return "filtertreesid";
	}
	
	/**
	 * 获取默认的过滤条件参数值
	 */
	public String getDefaultParamValue() {
		return "";
	}
}
