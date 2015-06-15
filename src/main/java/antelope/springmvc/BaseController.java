package antelope.springmvc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import oracle.sql.TIMESTAMP;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;

import antelope.beans.SearchParam;
import antelope.services.ExcelExportService;
import antelope.services.FileUploadParams;
import antelope.services.FileUploadService;
import antelope.services.FloatReportService;
import antelope.services.FreeMarkerService;
import antelope.services.HadoopService;
import antelope.services.LogService;
import antelope.services.MapRegionService;
import antelope.services.ReportService;
import antelope.services.SysWorkflowService;
import antelope.services.UserRoleOrgService;
import antelope.services.WordExportService;
import antelope.system.SystemCache;
import antelope.utils.AsyncMessage;
import antelope.utils.JSONArray;
import antelope.utils.JSONException;
import antelope.utils.JSONObject;
import antelope.utils.PageItem;
import antelope.utils.SpeedIDUtil;
import antelope.utils.TextUtils;


/**
 * <p>Title: 系统通用Controller基础类</p>
 * <p>Description: 完成系统常用的Controller相关操作</p>
 * @author lining
 * @version 1.0
 */
@Controller
public class BaseController extends BaseComponent {
	
	/**
	 * 日志对象，子类使用此对象记录系统常见日志`
	 */
	protected Logger log = Logger.getLogger(this.getClass());
	
	/**
	 * 用户操作日志，一般用此日志来记录用户相关操作，比如对某模块数据的增删改等
	 */
	@Resource
	protected LogService logservice;
	
	/**
	 * 系统基础dao,用来执行常用的数据的dao操作。
	 */
	@Resource(name="jpabasedao")
	protected JPABaseDao dao;
	
	/**
	 * 地图区域服务相关，将数据集中于地图展现方式时
	 */
	@Resource
	protected MapRegionService map;
	
	/**
	 * 工作流常用方法集合
	 */
	@Resource
	protected SysWorkflowService workflow;
	
	@Resource
	protected FloatReportService floatreport;
	
	@Resource
	protected ReportService report;
	
	/**
	 * 文件上传下载
	 */
	@Resource
	protected FileUploadService fileupload;
	
	/**
	 * excel导出服务类
	 */
	@Resource
	protected ExcelExportService excel;
	
	/**
	 * word导出服务类
	 */
	@Resource
	protected WordExportService word;
	
	/**
	 * FreeMarker服务类，主要完成静态页面发布等相关功能
	 */
	@Resource
	protected FreeMarkerService freemarker;
	
	/**
	 * 用户角色部门相关信息查询
	 */
	@Resource
	protected UserRoleOrgService userroleorg;
	
	/**
	 * hadoop相关接口
	 */
	@Resource
	protected HadoopService hadoop;
	
	/**
	 * 将返回的数据打印到web
	 */
	protected void print(Object obj, HttpServletResponse res) throws IOException {
		getOut(res).print(obj);
	}
	
	/**
	 * 将返回的数据打印到web
	 */
	protected void printJSON(Object obj, HttpServletResponse res) throws Exception {
		if (!(obj instanceof JSONObject) && !(obj instanceof JSONArray)) {
			if (obj instanceof List) {
				throw new Exception("目前不支持直接输出List！");
			} else {
				getOut(res).print(new JSONObject(obj));
			}
		} else {
			getOut(res).print(obj);
		}
	}

	
	/**
	 * 二进制输出
	 * @param res
	 * @return
	 * @throws IOException
	 */
	protected void printBytes(byte[] bytes, HttpServletResponse res) throws IOException {
		res.setHeader("Pragma", "No-cache");
		res.setHeader("Cache-Control", "no-cache");
		res.setDateHeader("Expires", 0);
		res.setCharacterEncoding("utf-8");
		
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		outputStream.write(bytes);
		ServletOutputStream streamOut = res.getOutputStream();
		try {
			outputStream.writeTo(streamOut);
		} catch( Exception e) { // 用户终止文件的下载操作，异常不进行处理
		}
		streamOut.close();
	}
	
	/**
	 * 二进制输出
	 * @param res
	 * @return
	 * @throws IOException
	 */
	protected void printBytes(byte[] bytes, String filename, HttpServletResponse res) throws IOException {
		res.setHeader("Pragma", "No-cache");
		res.setHeader("Cache-Control", "no-cache");
		res.setDateHeader("Expires", 0);
		res.setHeader("Content-Disposition","attachment; filename="+new String(filename.getBytes("gb2312"), "ISO8859-1" ));
		
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		outputStream.write(bytes);
		ServletOutputStream streamOut = res.getOutputStream();
		try {
			outputStream.writeTo(streamOut);
		} catch( Exception e) { // 用户终止文件的下载操作，异常不进行处理
		}
		streamOut.close();
	}
    
    public final static String noNull(String string, String defaultString) {
        return TextUtils.noNull(string, defaultString);
    }

    public final static String noNull(String string) {
        return noNull(string, "");
    }
    
    /**
     * 文件上传时使用
     * @param req
     * @return
     * @throws FileUploadException
     */
    protected final static List<FileItem> parseRequest(HttpServletRequest req) throws FileUploadException {
    	ServletFileUpload servFileUp = new ServletFileUpload(new DiskFileItemFactory());
    	return servFileUp.parseRequest(req);
    }
    
    /**
     * 获取文件上传信息
     * @param req
     * @return
     */
    public final static List<FileUploadParams> getFileUploadParams(HttpServletRequest req) {
    	List<FileUploadParams> fileparamslist = new ArrayList<FileUploadParams>();
    	
    	Set<Entry<String, String[]>> entries = req.getParameterMap().entrySet();
    	for (Entry<String, String[]> entry : entries) {
			String timeid = req.getParameter(entry.getKey() + "_fluploadtimesid");
			if (stringSet(timeid) && entry.getValue().length > 0) { // 若存在文件上传当次id则取得文件sid
				FileUploadParams params = new FileUploadParams();
				params.filegroupsid = entry.getValue()[0];
				params.uploadtimeid = timeid;
				fileparamslist.add(params);
			}
		}
    	
    	return fileparamslist;
    }
    
    /**
     * 使用字符串对迭代对象中的所有对象进行连接
     * @param glue 用于连接的字符串
     * @param pieces 迭代对象
     * @return
     */
    public final static String join(String glue, Iterator<?> pieces) {
        return TextUtils.join(glue, pieces);
    }
    
    public final static String join(String glue, JSONArray arr) throws JSONException {
    	return TextUtils.join(glue, arr);
    }

    
    /**
     * 使用字符串对对象数组对象进行连接
     * @param glue 用于连接的字符串
     * @param pieces 字符串数组
     * @return
     */
    public final static String join(String glue, Object[] pieces) {
        return join(glue, Arrays.asList(pieces).iterator());
    }
    
    /**
     * 添加弹出区域相关数据
     * @param data 区域相关数据
     * @param tmplId 所使用的客户端模板id
     * @param clientId 对应的人员id(如username)
     * @throws JSONException 
     */
    public final static void addPopoutNotice(JSONObject data, String tmplid, String clientId) throws JSONException {
    	JSONObject obj = new JSONObject();
    	obj.put("data", data);
    	obj.put("tmplid", tmplid);
    	routeMessageToService("notice", clientId, 60 * 60 * 1000, obj);
    }
    
    /**
     * 将信息体按照客户端id推送到目标地等待消费
     * @param destination 目标
     * @param clientId 客户端id（如username)
     * @param timeToLive 信息体存活期  毫秒计算
     * @param msgbody 信息体
     */
    public final static void routeMessageToService(String destination, String clientId, long timeToLive, Object msgbody) {
    	SystemCache.pushAysncMessage(new AsyncMessage(destination, clientId, System.currentTimeMillis(), timeToLive, SpeedIDUtil.getId(), msgbody));
    }
    
    /**
     * 获取搜索相关参数
     * @param req
     */
    public final static SearchParam getSearchParam(HttpServletRequest req) {
    	
    	SearchParam param = new SearchParam();
    	param.keyword = decodeAndTrim(req.getParameter("keyword"));
    	String[] keys = req.getParameterValues("filterkeys");
    	if (keys != null) {
    		for (int i = 0; i < keys.length; i++) {
    			param.filterKeys.add(keys[i]);
    		}
    	}
    	
    	return param;
    }
    
    
    /**
     * 将前台传入的参数封装入传入的对象
     * @param req
     * @param A
     * @return
     */
    public final static <A> A wrapToEntity(HttpServletRequest req, A bean) throws InstantiationException, IllegalAccessException {
    	return wrapToEntity(req, "", 0, bean);
    }
    
    /**
     * 将前台传入的参数封装入传入的对象,参数包含一个固定前缀。
     * @param req
     * @param prefix 固定前缀
     * @param bean
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public final static <A> A wrapToEntity(HttpServletRequest req, String prefix, A bean) throws InstantiationException, IllegalAccessException {
    	return wrapToEntity(req, prefix, 0, bean);
    }
    
    
    /**
     * 将前台传入的参数封装入传入的对象
     * @param req
     * @param A
     * @return
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     */
    public final static <A> A wrapToEntity(HttpServletRequest req, String prefix, int paramindex, A bean) throws InstantiationException, IllegalAccessException {
    	prefix = noNull(prefix);
    
    	Class<?> klass = bean.getClass();
    	Method[] methods = klass.getMethods();
        for (int i = 0; i < methods.length; i += 1) {
            try {
                Method method = methods[i];
                String name = method.getName();
                String key = "";
                if (name.startsWith("set")) {
                    key = name.substring(3);
                }
            	Class<?>[] paramtypes = method.getParameterTypes();
            	
            	if (paramtypes.length != 1)
            		continue;
            	
        		String paramName = paramtypes[0].getName();
        		if (paramName.indexOf(".") != -1 && !paramName.startsWith("java"))
        			continue;
            	
                if (key.length() > 0 &&
                        Character.isUpperCase(key.charAt(0))) {
                    if (key.length() == 1) {
                        key = key.toLowerCase();
                    } else if (!Character.isUpperCase(key.charAt(1))) {
                        key = key.substring(0, 1).toLowerCase() +
                            key.substring(1);
                    }
                    
                	String param = getParamValue(req, prefix + key, paramindex);
                    if (param != null) {
                    	method.invoke(bean, new Object[]{getSimpleValue(param, paramtypes[0])});
                    }
                }
            } catch (Exception e) {
            	throw new RuntimeException(e);
            }
        }
        
       	Field[] fields = klass.getFields();
        for (int i = 0; i < fields.length; i += 1) {
            try {
            	Field field = fields[i];
            	field.setAccessible(true);
                String key = field.getName();
                if (key.length() > 0) {
                	String param = getParamValue(req, prefix + key, paramindex);
                    if (param != null) {
                    	field.set(bean, getSimpleValue(param, field.getType()));
                    }
                }
            } catch (Exception e) {
            	throw new RuntimeException(e);
            }
        }
    	
    	return bean;
    }
    
	private final static String getParamValue(HttpServletRequest req, String key, int paramindex) {
		String param = null;
		String[] values = req.getParameterValues(key);
		if (values != null && values.length > paramindex)
			param = values[paramindex];
		if (param == null) {
			values = req.getParameterValues((key).toLowerCase());
			if (values != null && values.length > paramindex)
				param = values[paramindex];
		}

		return decodeAndTrim(param);
	}
	
	/**
     * 将前台传入的批量参数封装入list当中，并与录入表所对应对象信息进行合并
     * @param req
     * @param klass
     */
    public final <A> List<A> wrapToEntityListMerged(HttpServletRequest req, String prefix, String lengthfield, Class<A> klass) throws InstantiationException, IllegalAccessException {
    	List<A> beanList = new ArrayList<A>();
    	Assert.notNull(lengthfield, "批量表单的长度域不能为空！");
    	
    	int len = 0;
    	if (req.getParameterValues(lengthfield) != null)
    		len = req.getParameterValues(lengthfield).length;
    	
    	for (int i = 0; i < len; i++) {
    		String sid = req.getParameterValues(prefix + "sid")[i];
    		A bean = null;
			try {
				bean = dao.getBy(sid, klass);
				if (bean == null)
					bean = klass.newInstance();
			} catch (Exception e) {
				e.printStackTrace();
			}
    		beanList.add(wrapToEntity(req, prefix, i, bean));
    	}
    	return beanList;
    }
    
    /**
     * 将前台传入的批量参数封装入list当中
     * @param req
     * @param klass
     */
    public final static <A> List<A> wrapToEntityList(HttpServletRequest req, String prefix, String lengthfield, Class<A> klass) throws InstantiationException, IllegalAccessException {
    	List<A> beanList = new ArrayList<A>();
    	Assert.notNull(lengthfield, "批量表单的长度域不能为空！");
    	
    	int len = 0;
    	if (req.getParameterValues(lengthfield) != null)
    		len = req.getParameterValues(lengthfield).length;
    	
    	for (int i = 0; i < len; i++) {
    		A bean = klass.newInstance();
    		beanList.add(wrapToEntity(req, prefix, i, bean));
    	}
    	return beanList;
    }
    
    
    /**
     * 将前台传入的参数封装入对象
     * @param req
     * @param A
     * @return
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     */
    public final static <A> A wrapToEntity(HttpServletRequest req, Class<A> klass) throws InstantiationException, IllegalAccessException {
  	  //If klass.getSuperClass is System class then includeSuperClass = false;
    	A bean = klass.newInstance();
    	return wrapToEntity(req, bean);
    }
    
    /**
     * 将前台传入的参数封装入对象
     * @param req
     * @param A
     * @return
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     * @throws NoSuchFieldException 
     * @throws SecurityException 
     */
    public final static <A> A wrapToEntityAndFillIdWhenIdIsEmpty(HttpServletRequest req, A bean, String idFieldName) throws InstantiationException, IllegalAccessException, SecurityException, NoSuchFieldException {
    	A entity = wrapToEntity(req, bean);
    	fillIdWhenIdIsEmpty(entity, idFieldName);
    	return entity;
    }
    
    
    /**
     * 将前台传入的参数封装入对象
     * @param req
     * @param A
     * @return
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     * @throws NoSuchFieldException 
     * @throws SecurityException 
     */
    public final static <A> A wrapToEntityAndFillIdWhenIdIsEmpty(HttpServletRequest req, Class<A> klass, String idFieldName) throws InstantiationException, IllegalAccessException, SecurityException, NoSuchFieldException {
  	  //If klass.getSuperClass is System class then includeSuperClass = false;
    	A bean = klass.newInstance();
    	A entity = wrapToEntity(req, bean);
    	fillIdWhenIdIsEmpty(entity, idFieldName);
    	return entity;
    }
    
    /**
     * 获取简单Request对象，提供封装过的简单方法
     * @param req
     * @return
     */
    public final static SimpleRequest getSimpleRequest(HttpServletRequest req) {
    	return new SimpleRequest(req);
    }
    
    public final static <A> void fillIdWhenIdIsEmpty(A bean, String idFieldName) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
    	Field idField = bean.getClass().getDeclaredField(idFieldName);
    	idField.setAccessible(true);
    	Object id = idField.get(bean);
    	if (id == null) {
    		if (idField.getName().equals("Long"))
    			idField.set(bean, Long.parseLong(SpeedIDUtil.getId()));
    		else
    			idField.set(bean, SpeedIDUtil.getId());
    	}
    }

    /**
     * 将字符串转换为相应的数据类型
     * @param param
     * @param type
     * @return
     * @throws ParseException 
     */
	private static Object getSimpleValue(String param, Class<?> type)
			throws ClassNotFoundException, NoSuchMethodException,
			IllegalAccessException, InvocationTargetException, ParseException {
		String typeName = type.getName();
		if (!typeName.startsWith("java.")) {
			if (typeName.equals("int")) 
				typeName = "Integer";
			typeName = "java.lang." + Character.toUpperCase(typeName.charAt(0)) + typeName.substring(1);
		}
		if (typeName.equals("java.sql.Date") || typeName.equals("java.util.Date")) {
			return new java.sql.Date(getNewSdf().parse(param).getTime());
		} else if (typeName.equals("java.sql.Timestamp")) {
			if (param.length() > 10) 
				return new java.sql.Timestamp(getNewTimeSdf().parse(param).getTime());
			
			if (param.length() > 0) 
				return new java.sql.Timestamp(getNewSdf().parse(param).getTime());
			
			return null;
		} else {
			Class<?> clazz = Class.forName(typeName);
			Method method2 = null;
			if ("java.lang.String".equals(typeName)) {
				method2 = clazz.getMethod("valueOf", Object.class);
			} else {
				method2 = clazz.getMethod("valueOf", String.class);
				if (!stringSet(param))
					param = null;
			}
			
			if (param == null)
				return null;
			
			Object invoke = method2.invoke(null, param);
			return invoke;
		}
	}
	
	public static void putAllResultIntoList (ResultSet rs, List<JSONObject> daeInfoList) throws SQLException {
		while (rs.next()) {
			daeInfoList.add(putSingleResultIntoJSONObject(rs));
		}
	}
	
	public static List<JSONObject> putAllResultIntoList (ResultSet rs) throws SQLException {
		List<JSONObject> infolists = new ArrayList<JSONObject>();
		ResultSetMetaData metaData = rs.getMetaData();
		int colcount = metaData.getColumnCount();
		List<String> lowercaseNames = new ArrayList<String>();
		lowercaseNames.add("");// 从1开始记列，添加一个无用列；
		for(int i = 1; i <= colcount; ++i) {
			lowercaseNames.add(metaData.getColumnLabel(i).toLowerCase());	
		}
		SimpleDateFormat sdf = getNewTimeSdf();
		while (rs.next()) {
			infolists.add(putSingleResultIntoJSONObject(rs, colcount, lowercaseNames, sdf));
		}
		return infolists;
	}
	
	public static JSONObject putSingleResultIntoJSONObject(ResultSet rs) throws SQLException {
		JSONObject jsonObj = new JSONObject ();
		ResultSetMetaData metaData = rs.getMetaData();
		int colcount = metaData.getColumnCount();
		List<String> lowercaseNames = new ArrayList<String>();
		lowercaseNames.add("");// 从1开始记列，添加一个无用列；
		for(int i = 1; i <= colcount; ++i) {
			lowercaseNames.add(metaData.getColumnLabel(i).toLowerCase());	
		}
		SimpleDateFormat sdf = getNewTimeSdf();
		putSingleResultIntoJSONObject(rs, jsonObj, colcount, lowercaseNames, sdf);
		return jsonObj;
	}
	
	public static JSONObject putSingleResultIntoJSONObject(ResultSet rs, int colcount, List<String> lowercaseNames, SimpleDateFormat sdf) throws SQLException {
		JSONObject jsonObj = new JSONObject ();
		putSingleResultIntoJSONObject(rs, jsonObj, colcount, lowercaseNames, sdf);
		return jsonObj;
	}
	
	public static void putSingleResultIntoJSONObject(ResultSet rs, JSONObject obj, int colcount, List<String> lowercaseNames, SimpleDateFormat sdf) throws SQLException {
		putSingleResultIntoJSONObjectInner(rs, obj, colcount, lowercaseNames, sdf);
	}

	private static void putSingleResultIntoJSONObjectInner(ResultSet rs,
			JSONObject obj, int colcount, List<String> lowercaseNames,
			SimpleDateFormat sdf) throws SQLException {
		for (int i = 1; i <= colcount; ++i) {
			try {
				Object rsobj = rs.getObject(i);
				if (rsobj instanceof Date || rsobj instanceof TIMESTAMP) {
					Timestamp timestamp = rs.getTimestamp(i);
					obj.put(lowercaseNames.get(i), sdf.format(timestamp));
				} else if (rsobj instanceof byte[]){
					// 转16进制字符串
					byte[] bts = rs.getBytes(i);
					StringBuffer sb = new StringBuffer();
					for(int j = 0; j < bts.length; j++) {
						String str16 = Integer.toHexString(Integer.valueOf(bts[j])).toUpperCase();
						if (str16.length() == 1)
							str16 = "0" + str16;
						
						if (str16.length() == 8)
							str16 = str16.substring(6);
						sb.append(str16);
					}
					obj.put(lowercaseNames.get(i), sb.toString());
				} else if (rsobj != null && (rsobj instanceof oracle.sql.CLOB || rsobj.toString().startsWith("weblogic.jdbc.wrapper.Clob_oracle_sql_CLOB"))){ // 处理oracle clob数据
					obj.put(lowercaseNames.get(i), rs.getString(i));
				} else {
					obj.put(lowercaseNames.get(i), rsobj);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	
	public JSONArray toJSONArray(List<JSONObject> list) {
		JSONArray jsonArr = new JSONArray ();
		if (list != null) {
			for (int i = 0; i < list.size(); ++i)
				jsonArr.put(list.get(i));
		}
		return jsonArr;
	}
	
	/**
	 * 判断是否今天
	 */
	public static boolean isToday(String date) {
		SimpleDateFormat today = new SimpleDateFormat("yyyy-MM-dd");
		if (!stringSet(date) || today.format(new Date()) == date
				|| today.format(new Date()).equals(date)) {
			return true;
		}
		return false;
	}

	public void print(PageItem pageItem, HttpServletResponse res) throws IOException {
		print(new JSONObject(pageItem, true), res);
	}

	public void print(List<JSONObject> jsonlist, HttpServletResponse res) throws IOException {
		print(toJSONArray(jsonlist), res);
	}
}





