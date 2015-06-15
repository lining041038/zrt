package antelope.wbd.servlet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import antelope.wbd.util.Data;
import antelope.wbd.util.DataUtil;

public class SaveTotal extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		resp.setCharacterEncoding("utf-8");

		String str = req.getParameter("data");
		JSONObject jo = JSONObject.fromObject(str);
		String roomId = jo.getString("roomId");
		String userId = jo.getString("userId");

		try {
			userId = URLDecoder.decode(userId, "utf-8");
			roomId = URLDecoder.decode(roomId, "utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		int pageNo = jo.getInt("pageNo");

		Data data = DataUtil.getData(roomId + userId, pageNo);
		if (data == null) {
			data = new Data();
			DataUtil.setData(roomId + userId, pageNo, data);
		}

		data.setMapData(jo.getString("saveId"), jo.getString("totalData"));
		data.setTotalData(jo.getString("totalData"));
		resp.getWriter().println(true);
	}

}
