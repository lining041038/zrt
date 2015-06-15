package antelope.wbd.servlet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import antelope.wbd.room.RoomManager;
import antelope.wbd.util.Data;
import antelope.wbd.util.DataUtil;

public class GetTotal extends HttpServlet {

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
		String showId = jo.getString("showId");

		try {
			userId = URLDecoder.decode(userId, "utf-8");
			roomId = URLDecoder.decode(roomId, "utf-8");
			showId = URLDecoder.decode(showId, "utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}

		String key = roomId + userId;
		JSONArray ja = new JSONArray();
		for (int i = 0; i < DataUtil.size(key); i++) {
			Data data = DataUtil.getData(key, i);
			if (data == null) {
				continue;
			}
			// System.out.println(data.getTotalData());
			JSONObject temp_jo = new JSONObject();
			temp_jo.put("pageNo", i);
			// temp_jo.put("dataTotal", data.getTotalData());
			if (RoomManager.getRoom(roomId).getManager()
					.equalsIgnoreCase(showId)) {
				temp_jo.put("dataTotal", data.getMapData());
			}
			temp_jo.put("imgUrl", data.getImgUrl());
			ja.add(temp_jo);
		}
		resp.getWriter().println(ja.toString());
	}
}
