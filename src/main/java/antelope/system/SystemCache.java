package antelope.system;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.ServletContext;

import org.springframework.stereotype.Component;

import antelope.db.DBUtil;
import antelope.db.Sql;
import antelope.entities.SysRole;
import antelope.entities.SysUnit;
import antelope.entities.SysUser;
import antelope.services.UserRoleOrgService;
import antelope.utils.AsyncMessage;
import antelope.utils.JSONArray;
import antelope.utils.JSONException;
import antelope.utils.JSONObject;
import antelope.utils.ResultSetHandler;


/**
 * 系统缓存 
 * @author pc
 */
@Component
public class SystemCache {
	
	@Resource
	private UserRoleOrgService service;
	
	private static final String spliter = "_@_";
	private static boolean isCacheStart = true;
	private static long cleanSleeptime = 60 * 60 * 1000 * 2;
	
	private static CleanThread thread;
	
	private static final Map<String, List<AsyncMessage>> userAsyncMessages = new HashMap<String, List<AsyncMessage>>();
	private static String unitjsoncache = null; 
	private static boolean isunitjsoninvalidate = true;
	
	public static boolean isuserroleinvalidate = true;
	
	/**
	 * 将ServletContext缓存到全局
	 */
	public static ServletContext servletContext;
	
	/**
	 * 缓存单位及其下级单位人员，Key为单位sid
	 */
	private static Map<String, List<SysUser>> unitAndChildUnitsUsers;
	
	/**
	 * 缓存单位人员信息，key为单位sid
	 */
	private static Map<String, List<SysUser>> unitUsers;
	
	/**
	 * 用户角色缓冲
	 */
	private static Map<String, List<SysRole>> userRoleCache;
	
	
	public static Map<String, List<SysRole>> getUserRoleCache() {
		if (isuserroleinvalidate || isunitjsoninvalidate) {
			try {
				refreshUnitAndChildUnitsUsers();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			isuserroleinvalidate = false;
			isunitjsoninvalidate = false;
		}
		return userRoleCache;
	}

	public static Map<String, List<SysUser>> getUnitAndChildUnitsUsers() {
		return unitAndChildUnitsUsers;
	}
	
	public static List<SysUser> getUnitUsers(String unitsid) {
		List<SysUser> users = unitUsers.get(unitsid);
		if (users == null)
			users = new ArrayList<SysUser>();
		return users;
	}
	
	public static void refreshUnitAndChildUnitsUsers() throws SQLException, Exception {
		List<SysUnit> units = DBUtil.queryEntities("select * from SYS_UNIT", SysUnit.class);
		unitAndChildUnitsUsers = new HashMap<String, List<SysUser>>();
		unitUsers = new HashMap<String, List<SysUser>>();
		for (SysUnit sysUnit : units) {
			unitAndChildUnitsUsers.put(sysUnit.sid, new ArrayList<SysUser>());
			unitUsers.put(sysUnit.sid, new ArrayList<SysUser>());
		}
		
		List<SysUser> users = DBUtil.queryEntities("select * from SYS_USER", SysUser.class);
		userRoleCache = new HashMap<String, List<SysRole>>();
		for (SysUser sysUser : users) {
			userRoleCache.put(sysUser.sid, new ArrayList<SysRole>());
		}
		
		final Map<String, List<String>> relates = new HashMap<String, List<String>>();
		final Map<String, String> unitsidparents = new HashMap<String, String>();
		Sql relatesql = new Sql("select * from SYS_USER_UNIT_RELATE");
		Sql relatunitsql = new Sql("select * from SYS_UNIT");
		Sql relateuserrolesql = new Sql("select t.usersid, t2.* from SYS_USER_ROLE_RELATE t left join SYS_ROLE t2 on t.rolesid=t2.sid ");
		try {
			relatesql.query(new ResultSetHandler() {
				@Override
				public Object handle(ResultSet rs) throws Exception {
					while (rs.next()) {
						List<String> list = relates.get(rs.getString("usersid"));
						if (list == null) {
							list = new ArrayList<String>();
							relates.put(rs.getString("usersid"), list);
						}
						list.add(rs.getString("unitsid"));
					}
					return null;
				}
			});
			relatunitsql.query(new ResultSetHandler() {
				@Override
				public Object handle(ResultSet rs) throws Exception {
					while (rs.next()) {
						unitsidparents.put(rs.getString("sid"), rs.getString("parentsid"));
					}
					return null;
				}
			});
			
			
			relateuserrolesql.query(new ResultSetHandler() {
				@Override
				public Object handle(ResultSet rs) throws Exception {
					Set<String> columnset = DBUtil.getColumnNameSet(rs);
					while (rs.next()) {
						List<SysRole> list = userRoleCache.get(rs.getString("usersid"));
						if (list != null) {
							SysRole sr = new SysRole();
							DBUtil.wrapToEntity(rs, sr, columnset);
							list.add(sr);
						}
					}
					return null;
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (SysUser sysUser : users) {
			List<String> unitsids = relates.get(sysUser.sid);
			if (unitsids == null || unitsids.isEmpty())
				continue;
			
			for(int i = 0; i < unitsids.size(); i++) {
				String unitsid = unitsids.get(i);
				List<SysUser> currunitusers = unitUsers.get(unitsid);
				if (currunitusers != null && !currunitusers.contains(sysUser)) 
					currunitusers.add(sysUser);
				while (unitsid != null) {
					List<SysUser> list = unitAndChildUnitsUsers.get(unitsid);
					if (list != null && !list.contains(sysUser))
						list.add(sysUser);
					else
						break;
					unitsid = unitsidparents.get(unitsid);
				}
			}
			
		}
	}
	
	public String getUnitsJson() throws Exception {
		if (isunitjsoninvalidate) {
			refreshUnits();
			isunitjsoninvalidate = false;
		}
		return unitjsoncache;
	}
	
	public void invalidateUnitsJson() {
		isunitjsoninvalidate = true;
	}

	private JSONArray appendChildNodes(String parentsid) throws Exception {
		List<SysUnit> units = service.getUnitsByParentSid(parentsid);
		JSONArray arr = new JSONArray(units, true);
		tidyTreeData(arr);
		for (int i = 0; i < arr.length(); i++) {
			arr.getJSONObject(i).put("nodes", appendChildNodes(arr.getJSONObject(i).getString("sid")));
		}
		
		return arr;
	}
	
	private static void tidyTreeData(JSONArray arr) throws JSONException {
		for (int i = 0; i < arr.length(); i++) {
			JSONObject obj = arr.getJSONObject(i);
			obj.put("id", obj.get("sid"));
		}
	}
	
	public void refreshUnits() throws Exception {
		unitjsoncache = appendChildNodes("orgroot").toString(); 
	}
	
	public static final Object popAsyncMessage(String destination, String clientId) {
		List<AsyncMessage> list = userAsyncMessages.get(destination + spliter + clientId);
		if (list == null || list.size() == 0)
			return null;
		else
			return list.remove(0).body;
	}
	
	public static final void pushAysncMessage(AsyncMessage message) {
		if (!isCacheStart)
			return;
		
		String key = message.destination + spliter + message.clientId;
		
		List<AsyncMessage> list = userAsyncMessages.get(key);
		if (list == null) {
			list = new ArrayList<AsyncMessage>();
			userAsyncMessages.put(key, list);
		}
		
		list.add(message);
	}
	
	public static final void startCache() {
		isCacheStart = true;
		if (thread == null) {
			thread = new CleanThread();
			thread.start();
		}
	}
	
	public void stopCache() {
		isCacheStart = false;
		thread.running = false;
		thread = null;
	}

	public static class CleanThread extends Thread {

		public boolean running = true;

		public void run() {
			while (running) {
				long currtime = System.currentTimeMillis();
				for (Entry<String, List<AsyncMessage>>  entry : userAsyncMessages.entrySet()) {
					List<AsyncMessage> list = entry.getValue();
					for (int i = 0; i < list.size();) {
						AsyncMessage asyncMessage = list.get(i);
						if (currtime > (asyncMessage.timestamp + asyncMessage.timeToLive)) {
							list.remove(i);
						} else {
							i++;
						}
					}
					
					if (list.size() == 0) {
						userAsyncMessages.remove(entry.getKey());
					}
				}
				
				try {
					Thread.sleep(cleanSleeptime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}



