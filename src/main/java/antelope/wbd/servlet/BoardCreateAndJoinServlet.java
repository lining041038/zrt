package antelope.wbd.servlet;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.websocket.StreamInbound;
import org.apache.catalina.websocket.WebSocketServlet;


import antelope.wbd.account.Account;
import antelope.wbd.room.Room;
import antelope.wbd.room.RoomManager;
import antelope.wbd.room.RoomMessageInbound;


public class BoardCreateAndJoinServlet extends WebSocketServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected StreamInbound createWebSocketInbound(String message,
			HttpServletRequest req) {
		String userId = req.getParameter(Account.USER_ID);
		String roomId = req.getParameter(Room.KEY_ROOM_ID);

		try {
			userId = URLDecoder.decode(userId, "utf-8");
			roomId = URLDecoder.decode(roomId, "utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}

		String model = RoomMessageInbound.UNICAST;
		System.out.println("BoardCreateAndJoinServlet" + "---" + roomId
				+ "----" + userId);

		RoomMessageInbound mi = null;

		Room room = RoomManager.getRoom(roomId);
		if (room == null) {
			mi = new RoomMessageInbound();
			mi.setType(RoomMessageInbound.CREATE);
			mi.setModel(model);
			mi.setUserId(userId);
			room = new Room(userId, roomId, mi);
			RoomManager.addRoom(room);
			mi.setRoom(room);
		} else {
			mi = new RoomMessageInbound(room, RoomMessageInbound.JOIN);
			mi.setModel(model);
			mi.setUserId(userId);
			room.addMI(userId, mi);
		}
		return mi;
	}

}
