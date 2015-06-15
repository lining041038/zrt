package antelope.wbd.room;

import java.util.HashMap;
import org.apache.catalina.websocket.MessageInbound;

import antelope.wbd.account.Account;


public class Room {

	public static final String KEY_ROOM_ID = "roomId";

	public final Object roomMisLocker = new Object();

	private String roomId;
	private String manager;
	private HashMap<String, MessageInbound> miMap = new HashMap<String, MessageInbound>();
	private HashMap<String, Account> userMap = new HashMap<String, Account>();

	public Room(String userId, String roomId, MessageInbound mi) {
		this.manager = userId;
		this.roomId = roomId;
		synchronized (roomMisLocker) {
			miMap.put(userId, mi);
		}
		addUser(userId);
	}

	public MessageInbound getMI(String userId) {
		return miMap.get(userId);
	}

	public void addMI(String userId, MessageInbound mi) {
		synchronized (roomMisLocker) {
			miMap.put(userId, mi);
		}
		addUser(userId);
	}

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public HashMap<String, MessageInbound> getMiMap() {
		return miMap;
	}

	public void setMiMap(HashMap<String, MessageInbound> miMap) {
		this.miMap = miMap;
	}

	public String getManager() {
		return manager;
	}

	public void setManager(String manager) {
		this.manager = manager;
	}

	public HashMap<String, Account> getUserMap() {
		return userMap;
	}

	public void setUserMap(HashMap<String, Account> userMap) {
		this.userMap = userMap;
	}

	public void addUser(String userId) {
		if (!userMap.containsKey(userId)) {
			userMap.put(userId, new Account(userId));
		}
	}

	public void removeUser(String userId) {
		if (userMap.containsKey(userId)) {
			userMap.remove(userId);
		}
	}

}
