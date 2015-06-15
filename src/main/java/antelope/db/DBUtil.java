package antelope.db;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import oracle.sql.TIMESTAMP;

import org.dom4j.DocumentException;
import org.hibernate.Session;

import antelope.springmvc.BaseController;
import antelope.springmvc.JPABaseDao;
import antelope.springmvc.SpringUtils;
import antelope.utils.JSONObject;
import antelope.utils.PageItem;
import antelope.utils.PageParams;
import antelope.utils.PageUtil;
import antelope.utils.ResultSetHandler;

import com.opensymphony.xwork2.util.TextUtils;

/**
 * 数据库查询工具类
 * @author lining
 */
public class DBUtil {
	private static final String SELECT = "select";
	private static final String FROM = "from";
	private static final String DISTINCT = "distinct";
	
	public static List<Map<String, Object>>	query(String sql, Object param) throws SQLException, Exception {
		return query(sql, new Object[]{param});
	}
	public static boolean executeUpdate(String sql)throws SQLException, Exception {
		Sql psql = new Sql(sql);
		return psql.update()>0;
	}
	public static boolean executeUpdate(Connection conn, String sql)throws SQLException, Exception {
		Sql psql = new Sql(sql);
		return psql.update(conn, new Object[0])>0;
	}
	
	public static boolean executeUpdate(Connection conn, String sql, Object[] params)throws SQLException, Exception {
		Sql psql = new Sql(sql);
		return psql.update(conn, params)>0;
	}
	
	public static List<Map<String, Object>>	query(String sql, Object[] params) throws SQLException, Exception {
		
		Sql psql = new Sql(sql);
		final List<Map<String, Object>> valuemaps = new ArrayList<Map<String, Object>>();
		psql.query(new ResultSetHandler() {
			@Override
			public Object handle(ResultSet rs) throws Exception {
				ResultSetMetaData metaData = rs.getMetaData();
				while(rs.next()) {
					Map<String, Object> valuemap = new HashMap<String, Object>();
					for (int i = 1; i <= metaData.getColumnCount(); ++i) {
						if (rs.getObject(i) instanceof Date || rs.getObject(i) instanceof TIMESTAMP) {
							valuemap.put(metaData.getColumnLabel(i).toLowerCase(), rs.getTimestamp(i));
						} else if (rs.getObject(i) instanceof BigDecimal){
							valuemap.put(metaData.getColumnLabel(i).toLowerCase(), rs.getDouble(i));
						} else if (rs.getObject(i) instanceof BigInteger){
							valuemap.put(metaData.getColumnLabel(i).toLowerCase(), rs.getInt(i));
						} else {
							valuemap.put(metaData.getColumnLabel(i).toLowerCase(), rs.getObject(i));
						}
					}
					valuemaps.add(valuemap);
				}
				return null;
			}
		}, params);
		
		return valuemaps; 
	}
	
	public static List<Map<String, Object>>	query(Connection conn, String sql, Object[] params) throws SQLException, Exception {
			
			Sql psql = new Sql(sql);
			final List<Map<String, Object>> valuemaps = new ArrayList<Map<String, Object>>();
			psql.query(conn, new ResultSetHandler() {
				@Override
				public Object handle(ResultSet rs) throws Exception {
					ResultSetMetaData metaData = rs.getMetaData();
					while(rs.next()) {
						Map<String, Object> valuemap = new HashMap<String, Object>();
						for (int i = 1; i <= metaData.getColumnCount(); ++i) {
							if (rs.getObject(i) instanceof Date || rs.getObject(i) instanceof TIMESTAMP) {
								valuemap.put(metaData.getColumnLabel(i).toLowerCase(), rs.getTimestamp(i));
							} else if (rs.getObject(i) instanceof BigDecimal){
								valuemap.put(metaData.getColumnLabel(i).toLowerCase(), rs.getDouble(i));
							} else if (rs.getObject(i) instanceof BigInteger){
								valuemap.put(metaData.getColumnLabel(i).toLowerCase(), rs.getInt(i));
							} else {
								valuemap.put(metaData.getColumnLabel(i).toLowerCase(), rs.getObject(i));
							}
						}
						valuemaps.add(valuemap);
					}
					return null;
				}
			}, params);
			
			return valuemaps; 
		}
	
	public static List<JSONObject> queryJSON(String sql, Object[] params) throws SQLException, Exception {
		Sql psql = new Sql(sql);
		return (List <JSONObject>)psql.query(new ResultSetHandler() {
			@Override
			public Object handle(ResultSet rs) throws Exception {
				return BaseController.putAllResultIntoList(rs);
			}
		}, params);
	}
	
	public static Session getSession() {
		JPABaseDao dao = SpringUtils.getBean(JPABaseDao.class, "jpabasedao");
		return dao.getSession();
	}
	
	/**
	 * 根据参数查询出对象列表
	 * @param sql
	 * @param params
	 * @param klass
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 */
	public static <A> List<A> queryEntities(String sql, Object[] params, final Class<A> klass) throws SQLException, Exception {
		
		Sql psql = new Sql(sql);
		final List<A> objectlist = new ArrayList<A>();
		psql.query(new ResultSetHandler() {
			@Override
			public Object handle(ResultSet rs) throws Exception {
				Set<String> columnset = getColumnNameSet(rs);
				while(rs.next()) {
					objectlist.add(wrapToEntity(rs, klass.newInstance(), columnset));
				}
				return null;
			}
		}, params);
		return objectlist;
	}
	
	/**
	 * 根据参数查询出对象列表, 使用传入的连接
	 * @param sql
	 * @param params
	 * @param klass
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 */
	public static <A> List<A> queryEntities(String sql, Object[] params, final Class<A> klass, Connection conn) throws SQLException, Exception {
		
		Sql psql = new Sql(sql);
		final List<A> objectlist = new ArrayList<A>();
		psql.query(conn, new ResultSetHandler() {
			@Override
			public Object handle(ResultSet rs) throws Exception {
				Set<String> columnset = getColumnNameSet(rs);
				while(rs.next()) {
					objectlist.add(wrapToEntity(rs, klass.newInstance(), columnset));
				}
				return null;
			}
		}, params);
		return objectlist;
	}
	
	/**
	 * 根据参数查询出对象列表
	 * @param sql
	 * @param params
	 * @param klass
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 */
	public static <A> List<A> queryEntities(String sql, final Class<A> klass) throws SQLException, Exception {
		return queryEntities(sql, new Object[]{}, klass);
	}
	
	/**
	 * 根据参数查询出对象列表
	 * @param sql
	 * @param params
	 * @param klass
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 */
	public static <A> List<A> queryEntities(String sql, List<Object> params, final Class<A> klass) throws SQLException, Exception {
		return queryEntities(sql, params.toArray(new Object[0]), klass);
	}
	
	/**
	 * 根据参数查询出对象列表
	 * @param sql
	 * @param params
	 * @param klass
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 */
	public static <A> List<A> queryEntities(String sql, Object param, final Class<A> klass) throws SQLException, Exception {
		return queryEntities(sql, new Object[]{param}, klass);
	}
	
	public static Set<String> getColumnNameSet(ResultSet rs) throws SQLException {
		Set<String> columnset = new HashSet<String>();
		ResultSetMetaData metaData = rs.getMetaData();
		for(int i = 1; i <= metaData.getColumnCount(); i++)
			columnset.add(metaData.getColumnLabel(i).toLowerCase());
		
		return columnset;
	}
	
    public final static <A> A wrapToEntity(ResultSet rs, A bean, Set<String> columnset) throws InstantiationException, IllegalAccessException {
    
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
        		 //过滤List set modify by huanggc
        		boolean isList="java.util.List".equalsIgnoreCase(paramtypes[0].getName());
        		boolean isSet="java.util.Set".equalsIgnoreCase(paramtypes[0].getName());
        		
        		if (paramName.indexOf(".") != -1 && !paramName.startsWith("java")||isList||isSet)
        			continue;
            	
                if (key.length() > 0 &&
                        Character.isUpperCase(key.charAt(0))) {
                    if (key.length() == 1) {
                        key = key.toLowerCase();
                    } else if (!Character.isUpperCase(key.charAt(1))) {
                        key = key.substring(0, 1).toLowerCase() +
                            key.substring(1);
                    }
                    if (columnset.contains(key.toLowerCase()))
                    	method.invoke(bean, new Object[]{getParamValue(rs, key, paramtypes[0])});
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
                	if (columnset.contains(key.toLowerCase()))
                		field.set(bean, getParamValue(rs, key, field.getType()));
                }
            } catch (Exception e) {
            	throw new RuntimeException(e);
            }
        }
    	
    	return bean;
    }
    
    /**
     * 利用jdbc预编译sql进行批量更新操作
     * @param sql
     * @param batchparams
     * @return
     */
    public static int batchUpdateWithHighSpeed(String sql, List<Object[]> batchparams) throws SQLException {
    	if (batchparams == null)
    		return 0;
    	
    	Sql sqlobj = new Sql(sql);
    	int totalrows = 0;
		int[] rowsarr = sqlobj.batch(batchparams.toArray(new Object[0][]));
		for (int rows : rowsarr) {
			totalrows += rows;
		}
    	return totalrows;
    }
    
	private final static Object getParamValue(ResultSet rs, String key, Class<?> type) throws SQLException {
		String typeName = type.getName();
		
		if (rs.getObject(key) == null)
			return null;
		
		if (typeName.equals("java.lang.String")) {
			return rs.getString(key);
		} else if (typeName.equals("int") || typeName.equals("java.lang.Integer")) {
			return rs.getInt(key);
		} else if (typeName.equals("double") || typeName.equals("java.lang.Double")) {
			return rs.getDouble(key);
		} else if (typeName.equals("java.sql.Date") || typeName.equals("java.util.Date")) {
			return rs.getDate(key);
		} else if (typeName.equals("java.sql.Timestamp")) {
			return rs.getTimestamp(key);
		} else if (typeName.equals("long") || typeName.equals("java.lang.Long")) {
			return rs.getLong(key);
		} else if (typeName.equals("float") || typeName.equals("java.lang.Float")) {
			return rs.getFloat(key);
		} else if (typeName.equals("[B")) {
			return rs.getBytes(key);
		}

		return null;
	}
	
	
	public static List<JSONObject> queryJSON(String sql, Object param) throws SQLException, Exception {
		Sql psql = new Sql(sql);
		return (List <JSONObject>)psql.query(new ResultSetHandler() {
			@Override
			public Object handle(ResultSet rs) throws Exception {
				return BaseController.putAllResultIntoList(rs);
			}
		}, new Object[]{param});
	}
	
	public static List<JSONObject> queryJSON(String sql, List<Object> params) throws SQLException, Exception {
		
		Sql psql = new Sql(sql);
		return (List <JSONObject>)psql.query(new ResultSetHandler() {
			@Override
			public Object handle(ResultSet rs) throws Exception {
				return BaseController.putAllResultIntoList(rs);
			}
		}, params);
	}
	
	public static List<JSONObject> queryJSON(Connection conn, String sql, Object[] params) throws SQLException, Exception {
		
		Sql psql = new Sql(sql);
		return (List <JSONObject>)psql.query(conn, new ResultSetHandler() {
			@Override
			public Object handle(ResultSet rs) throws Exception {
				return BaseController.putAllResultIntoList(rs);
			}
		}, params);
	}
	
	public static List<JSONObject> queryJSON(String sql) throws SQLException, Exception {
		
		Sql psql = new Sql(sql);
		return (List <JSONObject>)psql.query(new ResultSetHandler() {
			@Override
			public Object handle(ResultSet rs) throws Exception {
				return BaseController.putAllResultIntoList(rs);
			}
		});
	}
	
	public static JSONObject querySingleJSON(String sql) throws SQLException, Exception {
		
		Sql psql = new Sql(sql);
		return (JSONObject)psql.query(new ResultSetHandler() {
			@Override
			public Object handle(ResultSet rs) throws Exception {
				if (rs.next())
					return BaseController.putSingleResultIntoJSONObject(rs);
				return null;
			}
		});
	}
	
	public static JSONObject querySingleJSON(String sql, Object[] params) throws SQLException, Exception {
		
		Sql psql = new Sql(sql);
		return (JSONObject)psql.query(new ResultSetHandler() {
			@Override
			public Object handle(ResultSet rs) throws Exception {
				if (rs.next())
					return BaseController.putSingleResultIntoJSONObject(rs);
				return null;
			}
		}, params);
	}
	
	public static JSONObject querySingleJSON(String sql, Object param) throws SQLException, Exception {
		
		Sql psql = new Sql(sql);
		return (JSONObject)psql.query(new ResultSetHandler() {
			@Override
			public Object handle(ResultSet rs) throws Exception {
				if (rs.next())
					return BaseController.putSingleResultIntoJSONObject(rs);
				return null;
			}
		}, new Object[]{param});
	}
	
	public static JSONObject querySingleJSON(String sql, List<Object> params) throws SQLException, Exception {
		
		Sql psql = new Sql(sql);
		return (JSONObject)psql.query(new ResultSetHandler() {
			@Override
			public Object handle(ResultSet rs) throws Exception {
				if (rs.next())
					return BaseController.putSingleResultIntoJSONObject(rs);
				return null;
			}
		}, params);
	}
	
	public static List<Map<String, Object>>	query(String sql, List<Object> params) throws SQLException, Exception {
		return query(sql, params.toArray(new Object[0])); 
	}
	
	public static List<Map<String, Object>>	query(String sql) throws SQLException, Exception {
		return query(sql, new Object[0]); 
	}
	
	public static List<Object> querySingleValList(String sql) throws SQLException, Exception {
		
		Sql psql = new Sql(sql);
		return (List<Object>)psql.query(new ResultSetHandler() {
			public Object handle(ResultSet rs) throws Exception {
				List<Object> list = new ArrayList<Object>();
				while(rs.next()) {
					list.add(rs.getObject(1));
				}
				return list;
			}
		});
	}
	
	public static List<Object> querySingleValList(String sql, Object[] params, final DataType datatype) throws SQLException, Exception {
		
		Sql psql = new Sql(sql);
		return (List<Object>)psql.query(new ResultSetHandler() {
			public Object handle(ResultSet rs) throws Exception {
				List<Object> list = new ArrayList<Object>();
				while(rs.next()) {
					Object obj = null;
					obj = getTypedData(datatype, rs, obj);
					list.add(obj);
				}
				return list;
			}
		}, params);
	}
	
	
	public static List<Object> querySingleValList(String sql, Object param, final DataType datatype) throws SQLException, Exception {
		return querySingleValList(sql, new Object[]{param}, datatype);
	}
	
	public static List<Object> querySingleValList(String sql, List<Object> params, final DataType datatype) throws SQLException, Exception {
		return querySingleValList(sql, params.toArray(new Object[0]), datatype);
	}
	
	public static Object querySingleVal(String sql, final DataType datatype) throws SQLException, Exception {
		
		Sql psql = new Sql(sql);
		return psql.query(new ResultSetHandler() {
			public Object handle(ResultSet rs) throws Exception {
				if (rs.next()) {
					Object obj = null;
					obj = getTypedData(datatype, rs, obj);
					return obj;
				}
				return null;
			}
		});
	}
	
	public static String querySingleString(String sql, Object[] params) throws SQLException, Exception {
		return (String) querySingleVal(sql, params, DataType.String);
	}
	
	public static Object querySingleVal(String sql, Object[] params, final DataType datatype) throws SQLException, Exception {
		
		Sql psql = new Sql(sql);
		return psql.query(new ResultSetHandler() {
			public Object handle(ResultSet rs) throws Exception {
				if (rs.next()) {
					Object obj = null;
					obj = getTypedData(datatype, rs, obj);
					return obj;
				}
				return null;
			}

			
		}, params);
	}
	
	/**
	 * 专门用来根据sql查询数量 
	 */
	public static int queryCount(String sql, Object[] params) throws SQLException, Exception {
		Integer ct = (Integer) querySingleVal(sql, params, DataType.Integer);
		if (ct == null)
			return 0;
		return ct;
	}
	
	/**
	 * 查询单列字符串
	 */
	public static List<String> queryStrings(String sql, Object[] params) throws SQLException, Exception {
		List<Object> results = querySingleValList(sql, params, DataType.String);
		List<String> strlist = new ArrayList<String>();
		for (Object string : results) {
			strlist.add(string.toString());
		}
		return strlist;		
	}
	
	/**
	 * 查询单列字符串
	 */
	public static List<String> queryStrings(String sql) throws SQLException, Exception {
		List<Object> results = querySingleValList(sql, new Object[]{}, DataType.String);
		List<String> strlist = new ArrayList<String>();
		for (Object string : results) {
			strlist.add(string.toString());
		}
		return strlist;		
	}
	
	/**
	 * 查询单列字符串
	 */
	public static List<String> queryStrings(String sql, Object param) throws SQLException, Exception {
		List<Object> results = querySingleValList(sql, param, DataType.String);
		List<String> strlist = new ArrayList<String>();
		for (Object string : results) {
			strlist.add(string.toString());
		}
		return strlist;		
	}
	
	/**
	 * 专门用来根据sql查询数量
	 */
	public static int queryCount(String sql, Object param) throws SQLException, Exception {
		Integer ct = (Integer) querySingleVal(sql, new Object[]{param}, DataType.Integer);
		if (ct == null)
			return 0;
		return ct;
	}
	
	public static Object querySingleVal(String sql, Object param, final DataType datatype) throws SQLException, Exception {
		return querySingleVal(sql, new Object[]{param}, datatype);
	}
	
	public static Object querySingleVal(String sql, List<Object> params, final DataType datatype) throws SQLException, Exception {
		return querySingleVal(sql, params.toArray(new Object[0]), datatype);
	}
	
	private static Object getTypedData(final DataType datatype,
			ResultSet rs, Object obj) throws SQLException {
		switch(datatype) {
		case Integer: obj = rs.getInt(1); break;
		case BigDecimal: obj= rs.getBigDecimal(1); break;
		case Long: obj = rs.getLong(1); break;
		case String: obj =  rs.getString(1); break;
		case Double: obj = rs.getDouble(1); break;
		case Timestamp: obj = rs.getTimestamp(1); break;
		}
		return obj;
	}
	
	/**
	 * 数据类型
	 * @author lining
	 */
	public static enum DataType {
		Integer, BigDecimal, Long, String, Double, Timestamp
	}
	
	public static PageItem queryJSON(String sql, List<Object> params, PageParams pageparams) throws SQLException, Exception {
		return queryJSON(sql, params.toArray(new Object[]{}), pageparams); 
	}
	
	public static PageItem queryJSON(String sql, Object param, PageParams pageparams) throws SQLException, Exception {
		return queryJSON(sql, new Object[]{param}, pageparams);
	}
	
	public static PageItem queryJSON(String sql, Object[] params, PageParams pageparams) throws SQLException, Exception {
		ResultSetHandler jsonHandle = new ResultSetHandler() {
			public Object handle(ResultSet rs) throws Exception {
				return BaseController.putAllResultIntoList(rs);
			}
		};
		return queryPageInner(sql, pageparams, params, jsonHandle);
	}
	private static PageItem queryPageInner(String sql, PageParams pageparams,
			Object[] params, ResultSetHandler jsonHandle) throws Exception,
			IOException, DocumentException {
		PageItem pageItem = null;
		
		Sql psql = new Sql("select count(*) ct from ("+sql.replaceFirst("(?i)order by .*$", "")+") t___");
		int count = (Integer) psql.query(new ResultSetHandler() {
			public Object handle(ResultSet rs) throws Exception {
				if (rs.next()) {
					return rs.getInt(1);
				}
				return null;
			}
		}, params);
		
		pageItem = PageUtil.initPageItem(pageparams, count);
		List<Object> paramlist = new ArrayList<Object>();
		for (int i = 0; i < params.length; i++) {
			paramlist.add(params[i]);
		}
		
		// 排序
		if (pageparams.isSort()) {
			sql = sql.replaceFirst("(?i)order[ ]+by.*$", "");
			sql += " order by " + pageparams.sortcol + " " + (pageparams.isup?"":"desc");
		}
		
		// 若存在数据项定位sid，则根据数据项定位sid定位当前页
		if (TextUtils.stringSet(pageparams.locateItemSid)) {
			final String locatesid = pageparams.locateItemSid;
			Sql locatesql = new Sql(sql);
			int pos = (Integer) locatesql.query(new ResultSetHandler() {
				@Override
				public Object handle(ResultSet rs) throws Exception {
					int pos = 0;
					while(rs.next()) {
						if (locatesid.equals(rs.getString("sid"))) {
							return pos;
						}
						pos++;
					}
					return -1;
				}
			}, params);
			if (pos != -1) {
				pageparams.page = (pos / pageItem.getNumPerPage() + 1) + "";
				pageItem = PageUtil.initPageItem(pageparams, count);
			}
		}
		
		if (DBHelper.isOracle()) { // 实现Oraclesql数据库端分页
			psql = new Sql("select * from ( select row_.*, rownum rownum_ from ("+sql+") row_ where rownum <= ?) where rownum_ > ?");
			paramlist.add((pageItem.getCurrPage()) * pageItem.getNumPerPage());
			paramlist.add((pageItem.getCurrPage() - 1) * pageItem.getNumPerPage());
		} else if (DBHelper.isSQLServer()) { // 实现sqlserver数据库端分页仿照hibernate
			psql = new Sql(getLimitString(sql));
			paramlist.add((pageItem.getCurrPage() - 1) * pageItem.getNumPerPage() + 1);
			paramlist.add((pageItem.getCurrPage()) * pageItem.getNumPerPage());
		} else if (DBHelper.isMySQL()) {
			psql = new Sql(getMySqlLimiteString(sql));
			paramlist.add((pageItem.getCurrPage() - 1) * pageItem.getNumPerPage());
			paramlist.add(pageItem.getNumPerPage());
		} else if (DBHelper.isPostgreSQL()) {
			psql = new Sql(getPostgreSQLLimiteString(sql));
			paramlist.add(pageItem.getNumPerPage());
			paramlist.add((pageItem.getCurrPage() - 1) * pageItem.getNumPerPage());
		}
		
		pageItem.setCurrList((List<JSONObject>)psql.query(jsonHandle, paramlist));
		return pageItem;
	}
	
	public static PageItem queryJSONPageTopN(String sql, Object[] params, int n, PageParams pageparams) throws SQLException, Exception {
		PageItem pageItem = null;
		
		Sql psql = new Sql("select count(*) ct from ("+sql.replaceFirst("(?i)select","(?i)select top "+n).replaceFirst("order by .*$", "")+") t");
		int count = (Integer) psql.query(new ResultSetHandler() {
			public Object handle(ResultSet rs) throws Exception {
				if (rs.next()) {
					return rs.getInt(1);
				}
				return null;
			}
		}, params);
		
		pageItem = PageUtil.initPageItem(pageparams, count);
		List<Object> paramlist = new ArrayList<Object>();
		for (int i = 0; i < params.length; i++) {
			paramlist.add(params[i]);
		}
		
		// 排序
		if (pageparams.isSort()) {
			// 将默认原来sql中的order by 先去掉
			sql = sql.replaceFirst("order[ ]+by.*$", "");
			sql += " order by " + pageparams.sortcol + " " + (pageparams.isup?"":"desc");
		}
		
		if (DBHelper.isOracle()) { // 实现Oraclesql数据库端分页
			 throw new Exception("TopN  暂不支持Oracle");
		} else if (DBHelper.isSQLServer()) { // 实现sqlserver数据库端分页仿照hibernate
			psql = new Sql(getLimitString(sql));
			paramlist.add((pageItem.getCurrPage() - 1) * pageItem.getNumPerPage() + 1);
			paramlist.add((pageItem.getCurrPage()) * pageItem.getNumPerPage());
		}
		
		pageItem.setCurrList((List<JSONObject>)psql.query(new ResultSetHandler() {
			public Object handle(ResultSet rs) throws Exception {
				return BaseController.putAllResultIntoList(rs);
			}
		}, paramlist));
			
			return pageItem;
	}
	
//	public static PageItem queryPage(String sql, Object[] params, PageParams pageparams) {
//		
//	}
	
	
	private static String getLimitString(String querySqlString) {
		StringBuilder sb = new StringBuilder(querySqlString.trim().toLowerCase());

		int orderByIndex = sb.indexOf("order by");
		CharSequence orderby = orderByIndex > 0 ? sb.subSequence(orderByIndex, sb.length())
				: "ORDER BY CURRENT_TIMESTAMP";

		// Delete the order by clause at the end of the query
		if (orderByIndex > 0) {
			sb.delete(orderByIndex, orderByIndex + orderby.length());
		}

		// HHH-5715 bug fix
		replaceDistinctWithGroupBy(sb);

		insertRowNumberFunction(sb, orderby);

		// Wrap the query within a with statement:
		sb.insert(0, "WITH query AS (").append(") SELECT * FROM query ");
		sb.append("WHERE __hibernate_row_nr__ BETWEEN ? AND ?");

		return sb.toString();
	}
	
	private static String getMySqlLimiteString(String querySqlString) {
		return new StringBuffer( querySqlString.length() + 20 )
		.append( querySqlString )
		.append( " limit ?, ?")
		.toString();
	}
	
	private static String getPostgreSQLLimiteString(String querySqlString) {
		return new StringBuffer( querySqlString.length()+20 )
		.append( querySqlString )
		.append(" limit ? offset ?")
		.toString();
	}
	
	/**
	 * Utility method that checks if the given sql query is a select distinct one and if so replaces the distinct select
	 * with an equivalent simple select with a group by clause.
	 *
	 * @param sql an sql query
	 */
	protected static void replaceDistinctWithGroupBy(StringBuilder sql) {
		int distinctIndex = shallowIndexOfWord( sql, DISTINCT, 0 );
		int selectEndIndex = shallowIndexOfWord( sql, FROM, 0 );
		if (distinctIndex > 0 && distinctIndex < selectEndIndex) {
			sql.delete( distinctIndex, distinctIndex + DISTINCT.length() + " ".length());
			sql.append( " group by" ).append( getSelectFieldsWithoutAliases( sql ) );
		}
	}
	
	/**
	 * Returns index of the first case-insensitive match of search term surrounded by spaces
	 * that is not enclosed in parentheses.
	 *
	 * @param sb String to search.
	 * @param search Search term.
	 * @param fromIndex The index from which to start the search.
	 * @return Position of the first match, or {@literal -1} if not found.
	 */
	private static int shallowIndexOfWord(final StringBuilder sb, final String search, int fromIndex) {
		final int index = shallowIndexOf( sb, ' ' + search + ' ', fromIndex );
		return index != -1 ? ( index + 1 ) : -1; // In case of match adding one because of space placed in front of search term.
	}
	
	/**
	 * Returns index of the first case-insensitive match of search term that is not enclosed in parentheses.
	 *
	 * @param sb String to search.
	 * @param search Search term.
	 * @param fromIndex The index from which to start the search.
	 * @return Position of the first match, or {@literal -1} if not found.
	 */
	private static int shallowIndexOf(StringBuilder sb, String search, int fromIndex) {
		final String lowercase = sb.toString().toLowerCase(); // case-insensitive match
		final int len = lowercase.length();
		final int searchlen = search.length();
		int pos = -1, depth = 0, cur = fromIndex;
		do {
			pos = lowercase.indexOf( search, cur );
			if ( pos != -1 ) {
				for ( int iter = cur; iter < pos; iter++ ) {
					char c = sb.charAt( iter );
					if ( c == '(' ) {
						depth = depth + 1;
					}
					else if ( c == ')' ) {
						depth = depth - 1;
					}
				}
				cur = pos + searchlen;
			}
		} while ( cur < len && depth != 0 && pos != -1 );
		return depth == 0 ? pos : -1;
	}
	
	/**
	 * This utility method searches the given sql query for the fields of the select statement and returns them without
	 * the aliases. See {@link SQLServer2005DialectTestCase#testGetSelectFieldsWithoutAliases()}
	 * 
	 * @param an
	 *            sql query
	 * @return the fields of the select statement without their alias
	 */
	protected static CharSequence getSelectFieldsWithoutAliases(StringBuilder sql) {
		String select = sql.substring(sql.indexOf(SELECT) + SELECT.length(), sql.indexOf(FROM));

		// Strip the as clauses
		return stripAliases(select);
	}
	
	/**
	 * Utility method that strips the aliases. See {@link SQLServer2005DialectTestCase#testStripAliases()}
	 * 
	 * @param a
	 *            string to replace the as statements
	 * @return a string without the as statements
	 */
	protected static String stripAliases(String str) {
		return str.replaceAll("\\sas[^,]+(,?)", "$1");
	}
	
	/**
	 * Right after the select statement of a given query we must place the row_number function
	 * 
	 * @param sql
	 *            the initial sql query without the order by clause
	 * @param orderby
	 *            the order by clause of the query
	 */
	protected static void insertRowNumberFunction(StringBuilder sql, CharSequence orderby) {
		// Find the end of the select statement
		int selectEndIndex = sql.indexOf(SELECT) + SELECT.length();

		// Insert after the select statement the row_number() function:
		sql.insert(selectEndIndex, " ROW_NUMBER() OVER (" + orderby + ") as __hibernate_row_nr__,");
	}
	
}









