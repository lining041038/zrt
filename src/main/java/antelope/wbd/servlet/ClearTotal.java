package antelope.wbd.servlet;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import antelope.wbd.util.DataUtil;


import net.sf.json.JSONObject;

@WebServlet(name = "ClearTotal", urlPatterns = { "/cleartotal.vot" })
public class ClearTotal extends HttpServlet {

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
		if (jo.containsKey("flag")) {
			int flag = jo.getInt("flag");
			try {
				userId = URLDecoder.decode(userId, "utf-8");
				roomId = URLDecoder.decode(roomId, "utf-8");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}

			String key = roomId + userId;
			DataUtil.removeData(key,flag);
		} else {

			try {
				userId = URLDecoder.decode(userId, "utf-8");
				roomId = URLDecoder.decode(roomId, "utf-8");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}

			String key = roomId + userId;
			DataUtil.removeData(key);
		}
	}
}
