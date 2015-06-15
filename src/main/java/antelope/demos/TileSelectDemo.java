package antelope.demos;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import antelope.db.DBUtil;
import antelope.interfaces.components.TileSelect;
import antelope.interfaces.components.supportclasses.TileSelectOptions;
import antelope.springmvc.SqlWhere;
import antelope.utils.JSONObject;
import antelope.utils.PageItem;

@Component("tileselectdemo")
public class TileSelectDemo extends TileSelect {

	@Override
	public TileSelectOptions getOptions(HttpServletRequest req) {
		TileSelectOptions options = new TileSelectOptions();
		options.title = "演示标题";
		return options;
	}

	@Override
	public PageItem getJSONPage(String queryval, HttpServletRequest req) throws Exception {
		SqlWhere sqlwhere = tidySqlWhere(new String[]{"name like ?", "username like ?", "sid = ?"}, 
				new Object[]{"%" + queryval+ "%", "%" + queryval+ "%", d(req.getParameter("sid"))}, true);
		PageItem pageItem = DBUtil.queryJSON("select * from SYS_USER where 1=1 " + sqlwhere.wherePart, sqlwhere.outParams, getPageParams(req));
		List<JSONObject> users = pageItem.getCurrList();
		for (JSONObject jsonObject : users) {
			jsonObject.put("imgpath", req.getContextPath() + "/demos/assets/a111634u1_160_90.jpg");
		}
		return pageItem;
	}

}



