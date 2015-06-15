package antelope.wbd.room;

import java.io.IOException;
import java.nio.CharBuffer;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.catalina.websocket.MessageInbound;

import antelope.wbd.util.SychroLockers;



public class RoomManager {

	private static HashMap<String,Room> rooms = new HashMap<String,Room>();
	private static final HeartBeat hb = new HeartBeat();
	
	static {
		//hb.start();
	}
	

	public static HashMap<String,Room> getRooms(){
		return rooms;
	}


	public static Room getRoom(String roomId){
		return rooms.get(roomId);
	}

	public static void addRoom(Room room){
		synchronized(SychroLockers.roomMapLocker) {
			rooms.put(room.getRoomId(), room);
		}
	}


	public static boolean isDuplicated(String roomName){
		if (getRooms().containsKey(roomName)){
			return true;
		}else {
			return false;
		}
	}

	public static class HeartBeat extends Thread {
		@Override
		public void run() {
			while(true) {
				try {
					sleep(20 * 1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Set<Entry<String, Room>> entries = rooms.entrySet();
				for (Entry<String, Room> entry : entries) {
					HashMap<String, MessageInbound> miMap = entry.getValue().getMiMap();
					Set<Entry<String, MessageInbound>> mientries = miMap.entrySet();
					for (Entry<String, MessageInbound> entry2 : mientries) {
						try {
							CharBuffer cb = CharBuffer.wrap("[{\"onlineNumber\":\"" + miMap.size() + "\"}]");
							entry2.getValue().getWsOutbound().writeTextMessage(cb);
							entry2.getValue().getWsOutbound().flush();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					
				}
				System.out.println("heartbeat executed!");
			}
		}
	}
	
}
