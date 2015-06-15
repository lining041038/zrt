package antelope.utils;



public class AsyncMessage {
	
	public String destination;
	public String clientId;
	public long timestamp;
	public long timeToLive;
	public String messageId;
	public Object body; 
	
	public AsyncMessage(String destination, String clientId, long timestamp, long timeToLive, String messageId, Object body) {
		this.destination = destination;
		this.clientId = clientId;
		this.timestamp = timestamp;
		this.timeToLive = timeToLive;
		this.messageId = messageId;
		this.body = body;
	}
}
