package antelope.wbd.room;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import net.sf.json.JSONObject;

import org.apache.catalina.websocket.MessageInbound;
import org.apache.catalina.websocket.WsOutbound;

import antelope.wbd.util.Data;
import antelope.wbd.util.DataUtil;


public class RoomMessageInbound extends MessageInbound {

	public static final String CREATE = "create";
	public static final String JOIN = "join";
	public static final String BOARDCAST = "boardcast";
	public static final String UNICAST = "unicast";

	private Room room;
	private String userId;
	private String type;
	private String model;

	public RoomMessageInbound() {

	}

	public RoomMessageInbound(Room room) {
		this.room = room;
	}

	public RoomMessageInbound(Room room, String type) {
		this.room = room;
		this.type = type;
	}

	@Override
	protected void onBinaryMessage(ByteBuffer arg0) throws IOException {
		System.out.println("RoomMessageInbound  onBinaryMessage");
	}

	@Override
	protected void onTextMessage(CharBuffer msg) throws IOException {
		synchronized (room.roomMisLocker) {

			JSONObject msgObj = JSONObject.fromObject(msg.toString());
			
			//解决中文乱码
			Iterator<String> temp_it = msgObj.keys();
			while(temp_it.hasNext()){
				String temp = temp_it.next();
				if("imgData".equalsIgnoreCase(temp)){
					continue;
				}
				msgObj.put(temp, URLDecoder.decode(msgObj.getString(temp), "utf-8"));
			}
			
			System.out.println("后台接收数据：" + this.userId + "===="
					+ this.room.getRoomId() + "====="
					+ msgObj.getString("pageNo"));
			
			Data data = DataUtil.getData(room.getRoomId()+userId, msgObj.getInt("pageNo"));
			if(data == null){
				data = new Data();
				DataUtil.setData(room.getRoomId()+userId, msgObj.getInt("pageNo"), data);
			}
			data.setImgUrl(msgObj.getString("imgData"));

			HashMap<String, MessageInbound> miMap = room.getMiMap();
			if (miMap == null) {
				return;
			}
			Set<String> set = miMap.keySet();
			Iterator<String> it = set.iterator();
			while (it.hasNext()) {
				String key = it.next();
				if (key.equalsIgnoreCase(userId)) {
					continue;
				}
				MessageInbound mi_temp = miMap.get(key);
				send(mi_temp, msgObj.toString());
			}

		}
	}

	@Override
	protected void onOpen(WsOutbound outbound) {
		if (UNICAST.equalsIgnoreCase(model)) {
			for (int i = 0; i < DataUtil.size(room.getRoomId()+userId); i++) {
				JSONObject jo = new JSONObject();
				jo.put("pageNo", i);
				if(DataUtil.getData(room.getRoomId()+userId, i)==null){
					continue;
				}
				jo.put("imgData", DataUtil.getData(room.getRoomId()+userId, i).getImgUrl());
				jo.put("model", "onopen");
				MessageInbound mi_temp = room.getMiMap().get(userId);
				try {
					send(mi_temp, jo.toString());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	protected void onClose(int status) {
		try {
			System.out.println("数据入库！！！"
					+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
							.format(new Date()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	private void send(MessageInbound mi, String msg) throws IOException {
		CharBuffer bufMsg = CharBuffer.wrap(msg);
		mi.getWsOutbound().writeTextMessage(bufMsg);
	}

}
