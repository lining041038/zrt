package antelope.springmvc;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLDecoder;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.dom4j.DocumentException;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.springframework.stereotype.Service;

import antelope.db.DBHelper;
import antelope.db.DBUtil;
import antelope.db.ResultWrapper;
import antelope.db.Sql;
import antelope.db.SqlLoader;
import antelope.interfaces.components.supportclasses.MoveParams;
import antelope.services.ProcessParams;
import antelope.services.SessionService;
import antelope.springmvc.validators.FormField;
import antelope.springmvc.validators.Validator;
import antelope.utils.I18n;
import antelope.utils.JSONArray;
import antelope.utils.JSONException;
import antelope.utils.JSONObject;
import antelope.utils.PageItem;
import antelope.utils.PageParams;
import antelope.utils.SpeedIDUtil;
import antelope.utils.TextUtils;
import antelope.utils.XmlEnumItem;
import antelope.utils.XmlEnumsUtil;

@Service
public class BaseComponent {
	/**
	 * 日志对象，子类使用此对象记录系统常见日志
	 */
	protected Logger log = Logger.getLogger(this.getClass());
	
	/**
	 * 系统基础dao,用来执行常用的数据的dao操作。
	 */
	@Resource(name="jpabasedao")
	protected JPABaseDao dao;
	
	/**
	 * 提供spring相关常用方法
	 */
	@Resource
	protected SpringUtils spring;
	
	/**
	 * 提供基本的常用格式化器
	 */
	@Resource
	protected FormatterFactory formatters;
	
	/**
	 * 提供基本的常用验证器
	 */
	protected ValidatorFactory validators = new ValidatorFactory();

	/**
	 * 判断字符串是否为null或为""
	 * @param string 要被判断是字符串
	 * @return 若设置了则返回true,否则 false
	 */
    protected final static boolean stringSet(String string) {
    	return TextUtils.stringSet(string);
    }
    
    /**
     * 使用字符串对字符串数组对象进行连接
     * @param glue 用于连接的字符串
     * @param pieces 字符串数组
     * @return
     */
    public final static String join(String glue, String[] pieces) {
        return TextUtils.join(glue, pieces);
    }
    
    /**
     * 使用字符串对集合对象进行连接
     * @param glue 用于连接的字符串
     * @param pieces 字符串数组
     * @return
     */
    public final static String join(String glue, Collection<?> pieces) {
        return TextUtils.join(glue, pieces.iterator());
    }
    
    /**
     * 将可序列化的对象序列化为二进制数组
     * @param obj
     */
    public final static byte[] writeObjectToBytes(Object obj) throws IOException {
    	ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(obj);
		return baos.toByteArray();
    }
    
	// 系统SessionService
	public final static SessionService getService(HttpServletRequest req) {
		return (SessionService) req.getSession().getAttribute("service");
	}
	
	/**
	 * 上下文获取国际化对象
	 * @param req
	 * @return
	 */
	protected I18n getI18n(HttpServletRequest req) {
		return getService(req).getI18n();
	}
	
	public final static String validate(String fieldname, String label, List<Validator> validators, HttpServletRequest req) {
		if (fieldname == null || validators == null)
			return null;
		
		FormField formField = new FormField();
		formField.label = TextUtils.noNull(label);
		formField.value = (req.getParameter(fieldname));
		I18n i18n = getService(req).getI18n();
		
		for (int i = 0; i < validators.size(); ++i) {
			String validateresult = validators.get(i).validate(formField, i18n);
			if (stringSet(validateresult)) {
				return validateresult;
			}
		}
		
		return null;
	}
	
	/**
	 * 字符串解码并切割
	 * @param str
	 * @return
	 */
	public final static String decodeAndTrim(String str) {
		try {
			if (stringSet(str)) {
				return URLDecoder.decode(str,"utf-8").trim();
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}
	
	/**
	 * 获取已经设置为utf-8编码的输出对
	 * @param res
	 * @return
	 * @throws IOException
	 */
	protected PrintWriter getOut(HttpServletResponse res) throws IOException {
		res.setHeader("Pragma", "No-cache");
		res.setHeader("Cache-Control", "no-cache");
		res.setDateHeader("Expires", 0);
		res.setHeader("Content-Type", "text/html; charset=utf-8");
		res.setCharacterEncoding("utf-8");
		return res.getWriter();
	}
	
	/**
	 * 字符串解码并切割，为decodeAndTrim方法简写形式
	 * @param str
	 */
	public final static String d(String str) {
		return decodeAndTrim(str);
	}
	
    /**
     * 将二进制数组反序列化为对象
     * @param bytes
     */
    public final static Object readObjectFromBytes(byte[] bytes) throws IOException, ClassNotFoundException {
    	ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		ObjectInputStream ois = new ObjectInputStream(bais);
		return ois.readObject();
    }
    
	/**
	 * 
	 * @return
	 */
	public String tidySqlWhere(String[] sqlparams, Object[] params, List<Object> outParams) {
		List<String> tmpsqlparams = new ArrayList<String>();
		for (int i = 0; i < sqlparams.length; i++) {
			if (params[i] instanceof String) {
				if (stringSet((String) params[i])) {
					tmpsqlparams.add(sqlparams[i]);
					outParams.add(params[i]);
				}
			} else if (params[i] != null) {
				tmpsqlparams.add(sqlparams[i]);
				outParams.add(params[i]);
			}
		}
		String paramstr = join(" and ", tmpsqlparams);
		if (stringSet(paramstr)) {
			return " and " + paramstr;
		} else {
			return "";
		}
	}
	
	/**
	 * 无输入参数版
	 * @return
	 */
	public SqlWhere tidySqlWhere(String[] sqlparams, Object[] params) {
		return tidySqlWhere(sqlparams, params, false);
	}
	
	
	/**
	 * 添加创建时相关信息，包括创建人创建人sid创建时间修改时间, 初始创建时创建时间与修改时间相同
	 * 所传入的属性均非必须
	 * 所传入的属性必须为public 属性
	 * creatorsid 创建人sid creatorname 创建人姓名 createtime 创建时间 modifytime修改时间 sortfield排序字段
	 * @param obj 实体对象
	 */
	protected void putCreateInfo(Object obj, HttpServletRequest req) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		Class<? extends Object> clazz = obj.getClass();
		SessionService service = getService(req);
		Field[] fields = clazz.getFields();
		int ct = 0;
		for (Field field : fields) {
			if ("creatorsid".equals(field.getName())) {
				field.set(obj, service.getUsersid());
				++ct;
			}
			if ("creatorname".equals(field.getName())) {
				field.set(obj, service.getUser());
				++ct;
			}
			if ("createtime".equals(field.getName())) {
				field.set(obj, now());
				++ct;
			}
			if ("modifytime".equals(field.getName())) {
				field.set(obj, now());
				++ct;
			}
			if ("sortfield".equals(field.getName())) {
				field.set(obj, SpeedIDUtil.getId() + "0000");
				++ct;
			}
			if (ct == 5)
				break;
		}
	}
	
	/**
	 * 创建包含有创建信息的实例
	 */
	protected <A> A newInstanceWithCreateInfo(Class<A> clazz, HttpServletRequest req) throws InstantiationException, IllegalAccessException, SecurityException, IllegalArgumentException, NoSuchFieldException {
		A a = clazz.newInstance();
		putCreateInfo(a, req);
		return a;
	}
	
	/**
	 * 无输入参数版
	 * @return
	 */
	public SqlWhere tidySqlWhere(String[] sqlparams, Object[] params, boolean isOr) {
		String andorstr = " and ";
		if (isOr)
			andorstr = " or ";
		
		SqlWhere sqlwhere = new SqlWhere();
		List<String> tmpsqlparams = new ArrayList<String>();
		for (int i = 0; i < sqlparams.length; i++) {
			if (params[i] instanceof String) {
				if (stringSet((String) params[i]) && !params[i].toString().matches("^%?(null)?%?$")) {
					tmpsqlparams.add(sqlparams[i]);
					sqlwhere.outParams.add(params[i]);
				}
			} else if (params[i] != null) {
				tmpsqlparams.add(sqlparams[i]);
				sqlwhere.outParams.add(params[i]);
			}
		}
		String paramstr = join(andorstr, tmpsqlparams);
		if (stringSet(paramstr)) {
			sqlwhere.wherePart = " and (" + paramstr + ")"; 
		}
		return sqlwhere;
	}
	
	/**
	 * 获取翻页参数
	 * @param req
	 * @return
	 */
	protected PageParams getPageParams(HttpServletRequest req) {
		return getPageParams(req, null);
	}
	
	/**
	 * 获取翻页参数
	 * @param req
	 * @param defaultSortcol 默认的排序字段
	 * @return
	 */
	protected PageParams getPageParams(HttpServletRequest req, String defaultSortcol) {
		return getPageParams(req, defaultSortcol, false);
		
	}
	
	/**
	 * 获取翻页参数
	 * @param req
	 * @param defaultSortcol 默认的排序字段
	 * @param asc 是否为正向排序，填写true则进行正排序
	 * @return
	 */
	protected PageParams getPageParams(HttpServletRequest req, String defaultSortcol, boolean asc) {
		PageParams params = new PageParams();
		params.page = req.getParameter("page");
		if (req.getParameter("numPerPage") == null) {
			params.numPerPage = 10;
		} else {
			params.numPerPage = Integer.parseInt(req.getParameter("numPerPage"));
		}
		params.sortcol = req.getParameter("sortcol");
		if (!stringSet(params.sortcol)) {
			params.sortcol = defaultSortcol;
			params.isup = asc;
		} else {
			params.isup = "true".equals(req.getParameter("isup") + "");
		}
		
		params.locateItemSid = req.getParameter("locateItemSid");
		
		return params;
	}
	
	
	/**
	 * 将数据转换为JSON数组
	 * @param list
	 * @return
	 */
	protected <A> JSONArray toJSONArrayBy(List<A> list) {
		JSONArray jsonArr = new JSONArray ();
		for (int i = 0; i < list.size(); ++i) {
			jsonArr.put(new JSONObject(list.get(i), true));
		}
		return jsonArr;
	}
	
	/**
	 * 获取前台传入的流程参数
	 * @return 流程参数
	 */
	protected ProcessParams getProcessParams(HttpServletRequest req) {
		return getOneProcessParam(req.getParameter("taskid"), req.getParameter("execution_id_"), req);
	}
	
	/**
	 * 当为批量完成任务时，前台获取的流程参数列表
	 * @return 流程参数列表
	 */
	protected List<ProcessParams> getBatchProcessParams(HttpServletRequest req) {
		String taskids = req.getParameter("taskids");
		String allexecutionids = req.getParameter("allexecutionids");
		List<ProcessParams> paramslist = new ArrayList<ProcessParams>();
		if (!stringSet(taskids))
			return paramslist;
		
		String[] taskidarr = taskids.split(",");
		String[] executeidarr = allexecutionids.split(",");
		int i = 0;
		for (String taskid : taskidarr) {
			paramslist.add(getOneProcessParam(taskid, executeidarr[i++], req));
		}
		
		return paramslist;
	}
	
	protected MoveParams getMoveParams(HttpServletRequest req) {
		MoveParams moveParams = new MoveParams();
		moveParams.isup = "true".equals(req.getParameter("isup") + "");
		moveParams.sid = req.getParameter("sid");
		
		if (stringSet(req.getParameter("updowntimes"))) {
			moveParams.updowntimes = Integer.parseInt(req.getParameter("updowntimes"));
		}
		
		return moveParams;
	}
	
	private ProcessParams getOneProcessParam(String taskid, String execution_id_, HttpServletRequest req) {
		ProcessParams params = new ProcessParams();
		params.taskid = taskid;
		params.result = BaseController.d(req.getParameter("var_result"));
		params.comment = BaseController.d(req.getParameter("var_comment"));
		params.assignee = getService(req).getUsername();
		params.processdefinitionkey = req.getParameter("processdefinitionkey");
		params.task_def_key_ = req.getParameter("task_def_key_");
		params.execution_id_ = execution_id_;
		params.tempsave = req.getParameter("tempsave");
		Map<String, String[]> maps = req.getParameterMap();
		Set<Entry<String, String[]>> entries = maps.entrySet();
		for (Entry<String, String[]> entry : entries) {
			if (entry.getKey().startsWith("var_")) {
				String[] vals = entry.getValue();
				List<String> listvals = new ArrayList<String>();
				for (int i = 0; i < vals.length; i++) {
					vals[i] = BaseController.d(vals[i]);
					if (stringSet(vals[i])) {
						String[] splitedvals = vals[i].split(",");
						for (String string : splitedvals) {
							listvals.add(string);
						}
					}
				}
				params.variables.put(entry.getKey().substring(4), listvals);
			}
		}
		
		if (stringSet(req.getParameter("var_copytousers"))) {
			params.copytousers = BaseController.d(req.getParameter("var_copytousers")).split(",");
		}
		
		params.i18n = getI18n(req);
		return params;
	}
	
	
	/**
	 * 执行存储过程，并返回结果
	 * @param callName
	 * @return 0，执行成功 1 执行失败
	 */
	protected int executeProc(final String callName) {
		return executeProc(callName, null);
	}
	
	/**
	 * 执行存储过程，并返回结果
	 * @param callName
	 * @return 0，执行成功 1 执行失败
	 */
	protected int executeProc(final String callName, final Object[] params) {
		Session session = DBUtil.getSession();
		if (session == null)
			return 1;
		final ResultWrapper resultwrapper = new ResultWrapper();
		session.doWork(new Work() {
			@Override
			public void execute(Connection conn) throws SQLException {
				resultwrapper.result = 1;
				CallableStatement cst = null;
				try {
					if (conn != null) {
						cst = conn.prepareCall(callName);
						int i = 0;
						if (params != null) {
							for (; i < params.length; ++i) {
								cst.setObject(i + 1, params[i]);
							}
						}
						cst.registerOutParameter(i + 1, java.sql.Types.INTEGER);
						cst.execute();
						resultwrapper.result = cst.getInt(i + 1);
					}
				} catch (Exception ex) {
					ex.printStackTrace();
					log.error(ex);
					resultwrapper.fail = true;
				} finally {
					try {
						if (cst != null)
							cst.close();
					} catch (SQLException ex) {
						log.error(ex);
					}
				}
			}
		});
		
		if (resultwrapper.fail)
			return 1;
		
		return (Integer) resultwrapper.result;
	}
	
	
	/**
	 * 使用传入的格式化器格式化对应值域后添加一个新的数据域
	 * 目前仅支持PageItem里面保存JSONObject
	 */
	public static void putFormattedField(String valueField, String newField, Format formatter, PageItem pageItem) throws JSONException {
		putFormattedField(valueField, newField, formatter, pageItem.getCurrList());
	}
	
	
	/**
	 * 使用传入的格式化器格式化对应值域后添加一个新的数据域
	 */
	public static void putFormattedField(String valueField, String newField, Format formatter, List<JSONObject> jsonlist) throws JSONException {
		int listsize = jsonlist.size();
		for(int i = 0; i < listsize; ++i) {
			jsonlist.get(i).put(newField, formatter.format(jsonlist.get(i).get(valueField)));
		}
	}
	
	/**
	 * 获取系统级XML枚举类型值。
	 * @param enumvalue
	 * @param xmlname
	 * @return
	 * @throws IOException
	 * @throws DocumentException
	 */
	public static String getXmlEnumLabel(String enumvalue, String xmlname, String locale) throws IOException, DocumentException {
		XmlEnumItem[] item = getXmlEnumItems(xmlname, locale);
		for (XmlEnumItem xmlEnumItem : item) {
			if (xmlEnumItem.value.equals(enumvalue)) {
				return xmlEnumItem.label;
			}
		}
		return null;
	}
	
	/**
	 * 获取系统级XML枚举类型，注意：此枚举均在 resources 中的 enums 包下定义
	 * @param xmlname 对应xml文件名称，不包含.xml后缀
	 */
	public static XmlEnumItem[] getXmlEnumItems(String xmlname, String locale) throws IOException, DocumentException {
		return XmlEnumsUtil.getXmlEnumItems(xmlname, locale);
	}
	
	@Deprecated
	public static String likePart() throws Exception {
		return DBHelper.getLikePart();
	}
	
	public static String yearMonthPart(String colname) throws Exception {
		return DBHelper.getYearMonthPart(colname);
	}
	
	@Deprecated
	public static String leftLikePart() throws Exception {
		return DBHelper.getLeftLikePart();
	}
	
	@Deprecated
	public static String rightLikePart() throws Exception {
		return DBHelper.getRightLikePart();
	}
	
	@Deprecated
	public static String strCatSign() throws Exception {
		return DBHelper.getStrCatSign();
	}
	
	/**
	 * 创建问号连接的字符串，通常用于拼接sql中的in条件字符串
	 * @param num 需要连接的问号的数量
	 * @return
	 */
	public static String createQuestionMarksStr(int num) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < num; ++i) {
			sb.append(",?");
		}
		return sb.toString().substring(1);
	}
	
	// 日期处理相关方法封装
	
	public static Timestamp yestoday() throws ParseException {
		return removeTime(new Timestamp(System.currentTimeMillis() - 86400000));
	}
	// 明天
	public static Timestamp tomorrow() throws ParseException {
		return removeTime(new Timestamp(System.currentTimeMillis() + 86400000));
	}
	
	public static Timestamp now() {
		return new Timestamp(System.currentTimeMillis());
	}
	
	public static Timestamp toTimestamp(String currday) { 
		try {
			if (stringSet(currday)) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				if (currday.length() > 10) {
					sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				}
				Date parse = sdf.parse(currday);
				long time = parse.getTime();
				return new Timestamp(time);
			}
		} catch (ParseException e) {
		}
		return null;
	}
	
	/**
	 * 根据对应sql文件中对应id值获取sql对象
	 * @param id
	 */
	public static Sql getSql(String id) {
		return SqlLoader.getInstance().getSql(id);
	}
	
	public static SimpleDateFormat getNewSdf() {
		return new SimpleDateFormat("yyyy-MM-dd");
	}
	
	public static SimpleDateFormat getNewTimeSdf() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	}
	
	public static Timestamp today() throws ParseException {
		SimpleDateFormat sdf = getNewSdf();
		return new Timestamp(sdf.parse(sdf.format(new Date())).getTime());
	}
	
	public static Timestamp nextDay(Timestamp currday) {
		return new Timestamp(currday.getTime() + 86400000);
	}
	
	public static Timestamp prevDay(Timestamp currday) {
		return new Timestamp(currday.getTime() - 86400000);
	}
	
	public static Timestamp nextDay(String currday) {
		try {
			if (stringSet(currday))
				return new Timestamp(getNewSdf().parse(currday).getTime() + 86400000);
		} catch (ParseException e) {
		}
		return null;
	}
	
	public static Timestamp removeTime(Timestamp currTimestamp) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String fm = sdf.format(currTimestamp);
			Date parse = sdf.parse(fm);
			return new Timestamp(parse.getTime());
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}
}
