package antelope.controllers.components;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import antelope.entities.SysUser;
import antelope.springmvc.BaseController;
import antelope.utils.JSONException;
import antelope.utils.JSONObject;
import antelope.utils.ListUtils;
import antelope.utils.SystemOpts;



@Controller
public class AmsCompController  extends BaseController {
	
	
	
	/** 视频交互聊天区域模块 begin******->*/
	/**
	 * 视频交互聊天区域用户信息缓冲
	 */
	private final Map<String, List<SysUser>> userCacheMap = new HashMap<String, List<SysUser>>();
	
	private final Map<String, Map<String, AudioRequest>> audioReqCacheMap = new HashMap<String, Map<String, AudioRequest>>();
	
	/**
	 * 聊天室新增聊天人
	 */
	@RequestMapping("/common/components/AmsCompController/addNewChatUser")
	public synchronized void addNewChatUser(String sid, HttpServletRequest req, HttpServletResponse res) throws Exception {
		List<SysUser> amsChatUsers = userCacheMap.get(sid);
		if (amsChatUsers == null) {
			amsChatUsers = new ArrayList<SysUser>();
			userCacheMap.put(sid, amsChatUsers);
		}
		
		amsChatUsers.add(dao.getBy(getService(req).getUsersid(), SysUser.class));
	}
	
	/**
	 * 聊天室聊天人离开
	 */
	@RequestMapping("/common/components/AmsCompController/removeChatUser")
	public synchronized void removeChatUser(String sid, HttpServletRequest req, HttpServletResponse res) {
		
		List<SysUser> amsChatUsers = userCacheMap.get(sid);
		if (amsChatUsers == null)
			return;
		
		for (SysUser sysUser : amsChatUsers) {
			if (getService(req).getUsersid().equals(sysUser.sid)) {
				amsChatUsers.remove(sysUser);
				break;
			}
		}
	}
	
	/**
	 * 获取当前正在聊天的人员信息
	 */
	@RequestMapping("/common/components/AmsCompController/getCurrentUsers")
	public void getCurrentUsers(String sid, HttpServletRequest req, HttpServletResponse res) throws IOException {
		List<SysUser> amsChatUsers = userCacheMap.get(sid);
		if (amsChatUsers == null) {
			amsChatUsers = new ArrayList<SysUser>();
			userCacheMap.put(sid, amsChatUsers);
		} 
		List<SysUser> newuserlist = new ArrayList<SysUser>();
		newuserlist.addAll(amsChatUsers);
		ListUtils.unique(newuserlist, new Comparator<SysUser>(){
			@Override
			public int compare(SysUser o1, SysUser o2) {
				return o1.sid.equals(o2.sid) ? 0 : 1;
			}}
		);
		print(toJSONArrayBy(newuserlist), res);
	}
	
	/**
	 * 宾客向主持申请发言
	 */
	@RequestMapping("/common/components/AmsCompController/requestAudio")
	public void requestAudio(String sid, HttpServletRequest req, HttpServletResponse res) {
		Map<String, AudioRequest> amsAuditReqs = audioReqCacheMap.get(sid);
		if (amsAuditReqs == null) {
			amsAuditReqs = new HashMap<String, AudioRequest>();
			audioReqCacheMap.put(sid, amsAuditReqs);
		} 
		
		AudioRequest audioRequest = amsAuditReqs.get(getService(req).getUsersid());
		if (audioRequest != null) {
			return;
		}
		
		AudioRequest auditreq = new AudioRequest();
		amsAuditReqs.put(getService(req).getUsersid(), auditreq);
	}
	
	/**
	 * 获取所有宾客当前发言申请信息
	 */
	@RequestMapping("/common/components/AmsCompController/getAllAudioRequests")
	public void getAllAudioRequests(String sid, HttpServletRequest req, HttpServletResponse res) throws IOException {
		Map<String, AudioRequest> amsAuditReqs = audioReqCacheMap.get(sid);
		if (amsAuditReqs == null) {
			amsAuditReqs = new HashMap<String, AudioRequest>();
			audioReqCacheMap.put(sid, amsAuditReqs);
		}
		print(new JSONObject(amsAuditReqs, true), res);
	}
	
	private static class AudioRequest {
		public int state; // 状态 0申请中 1回应允许
	}
	
	/*<- 视频交互聊天区域模块 end*******/
	
	
	
	
	/** 组播视频区域模块 begin******->*/
	
	/**
	 * 获取视频参数
	 */
	@RequestMapping("/common/components/AmsCompController/getVideoParams")
	public void getVideoParams(HttpServletRequest req, HttpServletResponse res) throws JSONException, IOException {
		JSONObject obj = new JSONObject();
		obj.put("publishPassword", SystemOpts.getProperty("antelope_fmspublishpassword"));
		obj.put("ipMulticastAddress", SystemOpts.getProperty("antelope_ipmulticastaddr"));
		print(obj, res);
	}
	
	/*<- 组播视频区域模块 end********/
	
	
	/** 聊天区域模块 begin******->*/
	
	/**
	 * 聊天信息缓存
	 */
	private final Map<String, List<AmsChatMessage>> msgCacheMap = new HashMap<String, List<AmsChatMessage>>();
	
	/**
	 * 聊天信息过期时间
	 */
	private static final int timeoutMilling = 600000;
	
	/**
	 * 接收聊天信息
	 */
	@RequestMapping("/common/components/AmsCompController/addChatMessage")
	public synchronized void addChatMessage(String sid, String message, HttpServletRequest req, HttpServletResponse res) throws IOException {
		List<AmsChatMessage> amsChatMessages = msgCacheMap.get(sid);
		int newid = 0;
		if (amsChatMessages == null) {
			amsChatMessages = new ArrayList<AmsChatMessage>();
			msgCacheMap.put(sid, amsChatMessages);
		} else {
			newid = amsChatMessages.get(amsChatMessages.size() - 1).id + 1;
		}
		AmsChatMessage amsMessage = new AmsChatMessage();
		amsMessage.id = newid;
		amsMessage.createTime = now();
		amsMessage.senderName = d(getService(req).getUser());
		amsMessage.message = d(message);
		amsChatMessages.add(amsMessage);
	}
	
	/**
	 * 获取最近的聊天信息数据
	 */
	@RequestMapping("/common/components/AmsCompController/getRecentMessages")
	public void getRecentMessages(String sid, HttpServletRequest req, HttpServletResponse res) throws IOException {
		List<AmsChatMessage> amsMessages = msgCacheMap.get(sid);
		if (amsMessages == null)
			amsMessages = new ArrayList<AmsChatMessage>();
		
		print(toJSONArrayBy(amsMessages), res);
	}
	
	/**
	 * 定期清理聊天缓存信息
	 */
	@Scheduled(cron="0 * * ? * *")
	public void clearCycle() {
		Set<Entry<String, List<AmsChatMessage>>> entries = msgCacheMap.entrySet();
		List<String> keys = new ArrayList<String>();
		for (Entry<String, List<AmsChatMessage>> entry : entries) {
			keys.add(entry.getKey());
		}
		
		for (String key : keys) {
			List<AmsChatMessage> amsMessages = msgCacheMap.get(key);
			for(int i = 0; i < amsMessages.size();) {
				AmsChatMessage fmsmsag = amsMessages.get(i);
				if (fmsmsag.createTime.getTime() + timeoutMilling < System.currentTimeMillis()) {
					amsMessages.remove(i);
				} else {
					++i;
				}
			}
			if (amsMessages.isEmpty()) {
				msgCacheMap.remove(key);
			}
		}
	}
	
	private static class AmsChatMessage {
		public int id; // 单条信息id，加一递增
		public Timestamp createTime;
		public String senderName;
		public String message;
	}
	/*<- 聊天区域模块 end********/
}



