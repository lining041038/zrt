package antelope.demos;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import antelope.db.DBUtil;
import antelope.springmvc.BaseController;
import antelope.utils.PageItem;

@Controller
public class TileGridController extends BaseController {
	
	@RequestMapping("/demos/tilegrid/TileGridController/getTileGridPageItem")
	public void getTileGridPageItem(HttpServletRequest req, HttpServletResponse res) throws SQLException, Exception {
		PageItem pageItem = DBUtil.queryJSON("select * from DEMO_SINGLE_DATAGRID where imgpath is not null", new Object[0], getPageParams(req));
		print(pageItem, res);
	}
}




