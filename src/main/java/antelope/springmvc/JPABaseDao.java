package antelope.springmvc;


import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.Table;

import org.hibernate.LockMode;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import antelope.beans.SearchColInfo;
import antelope.beans.SearchParam;
import antelope.beans.SearchResult;
import antelope.db.DBHelper;
import antelope.db.DBUtil;
import antelope.interfaces.components.supportclasses.MoveParams;
import antelope.utils.JSONException;
import antelope.utils.JSONObject;
import antelope.utils.PageItem;
import antelope.utils.PageParams;
import antelope.utils.TextUtils;
import antelope.utils.XmlEnumItem;
import antelope.utils.XmlEnumsUtil;

/**
 * <p>Title: 系统通用Dao基础类</p>
 * <p>Description: 完成系统常用的Dao相关操作</p>
 * <p>Copyright: Timewalking Corporation Copyright (c) 2011</p>
 * <p>Company: BEIJING Timewalking SOFTWARE CO.,LTD</p>
 * @author lining
 * @version 1.0
 */
@Repository("jpabasedao")
public class JPABaseDao {
	
	/**
	 * 实体管理对象，通过Spring注入
	 */
	@PersistenceContext
	private EntityManager em;
	
	/**
	 * 用于测试的hibernatesession;
	 */
	private Session session;
	
	public Session getSession() {
		if (session != null)
			return session;
		
		return em.unwrap(Session.class);
	}
	
	/**
	 * 获取实体类所对应表中的所有数据
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	public <A> List<A> getAll(final Class<A> clazz) throws Exception {
		Entity annoenti = clazz.getAnnotation(Entity.class);
		if (annoenti != null) {
			String entiName = annoenti.name();
			if (!stringSet(entiName)) {
				entiName = clazz.getSimpleName();
			}
			
			if (session != null) {
				org.hibernate.Query query = session.createQuery("select t from "+entiName+" t");
				return query.list();
			}
			
			Query query = em.createQuery("select t from "+entiName+" t");
			return query.getResultList();
		}
		
		return new ArrayList<A>();
	}
	
	/**
	 * 根据主键获取实体表中的类
	 * @param key 主键
	 * @param clazz 实体类型
	 * @return 对应实体对象
	 * @throws Exception
	 */
	public <A> A getBy(Object sid, final Class<A> clazz) throws Exception {
		if (sid == null)
			return null;
		if (session != null)
			return (A) session.get(clazz, (Serializable) sid);
		return em.find(clazz, sid);
	}
	
	/**
	 * 根据单个外键获取实体表中的类对象
	 */
	public <A> List<A> getBy(String dbcolname, Object value, final Class<A> clazz) throws Exception {
		return query("select * from " + getDBTableName(clazz) + " where " + dbcolname + "=?", value, clazz);
	}
	
	/**
	 * 内部使用获取相关页
	 * @param sPage
	 * @param numPerPage
	 * @param countjpql
	 * @param queryjpql
	 * @param params
	 * @return
	 */
	private PageItem getPageInnerBySql(String sPage, int numPerPage, String countsql, String querysql, Object[] params, Class<?> clazz) {
		
		if (session != null) {
			
			SQLQuery cntquery = session.createSQLQuery( countsql );
			cntquery.addEntity( "alias1", clazz.getName(), LockMode.READ );
			
			SQLQuery query = session.createSQLQuery( countsql );
			query.addEntity( "alias1", clazz.getName(), LockMode.READ );
			
			for (int i = 0; i < params.length; i++) {
				cntquery.setParameter(i + 1, params[i]);
				query.setParameter(i + 1, params[i]);
			}
			
			PageItem pageItem = new PageItem();
			int count = Integer.parseInt(cntquery.uniqueResult().toString());
			pageItem.setCount(count);
			// 设置每页显示条数
			pageItem.setNumPerPage(numPerPage);
			// 当每页显示数设置成0时则证明不对当前页面数据进行分页
			if (numPerPage == 0) {
				pageItem.setCurrList(query.list());
				return pageItem; 
			}

			// 默认页数为1
			int pageNum = 1;
			try {
				pageNum = Integer.parseInt(sPage);
			} catch (NumberFormatException e) {
			}
			// 小于1时设置成1
			if (pageNum <= 0)
				pageNum = 1;
			// 设置最大页数
			int totalpage = (count - 1) / numPerPage + 1; 
			if (totalpage == 0) // 若总页数，最少为1.
				totalpage = 1;
			pageItem.setTotalPage(totalpage);
			// 大于最大页数时设置为最大页数
			if (pageNum > pageItem.getTotalPage())
				pageNum = pageItem.getTotalPage();
			pageItem.setCurrPage(pageNum);
			pageItem.setPre(pageNum == 1 ? 1 : pageNum - 1);
			// 设置设置长度
			int len = pageNum * numPerPage;

			if (len >= count) {
				pageItem.setNext(pageNum);
				len = count;
			} else {
				pageItem.setNext(pageNum + 1);
			}
			// 添加当前页所需要的页面列表数据项
			query.setFirstResult((pageNum - 1) * numPerPage);
			query.setMaxResults(numPerPage);
			pageItem.setCurrList(query.list());
			pageItem.setCount(count);
			return pageItem;
		}
		
		Query cntquery = em.createNativeQuery(countsql);
		Query query = em.createNativeQuery(querysql, clazz);
		
		for (int i = 0; i < params.length; i++) {
			cntquery.setParameter(i + 1, params[i]);
			query.setParameter(i + 1, params[i]);
		}
		
		PageItem pageItem = new PageItem();
		int count = Integer.parseInt(cntquery.getSingleResult().toString());
		pageItem.setCount(count);
		// 设置每页显示条数
		pageItem.setNumPerPage(numPerPage);
		// 当每页显示数设置成0时则证明不对当前页面数据进行分页
		if (numPerPage == 0) {
			pageItem.setCurrList(query.getResultList());
			return pageItem; 
		}

		// 默认页数为1
		int pageNum = 1;
		try {
			pageNum = Integer.parseInt(sPage);
		} catch (NumberFormatException e) {
		}
		// 小于1时设置成1
		if (pageNum <= 0)
			pageNum = 1;
		// 设置最大页数
		int totalpage = (count - 1) / numPerPage + 1; 
		if (totalpage == 0) // 若总页数，最少为1.
			totalpage = 1;
		pageItem.setTotalPage(totalpage);
		// 大于最大页数时设置为最大页数
		if (pageNum > pageItem.getTotalPage())
			pageNum = pageItem.getTotalPage();
		pageItem.setCurrPage(pageNum);
		pageItem.setPre(pageNum == 1 ? 1 : pageNum - 1);
		// 设置设置长度
		int len = pageNum * numPerPage;

		if (len >= count) {
			pageItem.setNext(pageNum);
			len = count;
		} else {
			pageItem.setNext(pageNum + 1);
		}
		// 添加当前页所需要的页面列表数据项
		query.setFirstResult((pageNum - 1) * numPerPage);
		query.setMaxResults(numPerPage);
		pageItem.setCurrList(query.getResultList());
		pageItem.setCount(count);
		return pageItem;
	}

	private <T> String getDBTableName(Class<T> clazz) {
		Table annoenti = clazz.getAnnotation(Table.class);
		String className = clazz.getSimpleName();
		if (annoenti != null) {
			className = annoenti.name();
			if (!stringSet(className)) {
				className = clazz.getSimpleName();
			}
		}
		return className;
	}
	
	/**
	 * 获取数据表字段名
	 * @param field
	 * @return
	 */
	private String getDBFieldName(Field field) {
		String fieldName = field.getName().toLowerCase();
		Column annoColumn = field.getAnnotation(Column.class);
		if (annoColumn != null) {
			String name = annoColumn.name();
			if (TextUtils.stringSet(name)) {
				fieldName = name;
			}
		}
		
		return fieldName;
	}
	
	/**
	 * 根据sql获取List对象
	 * @param sql
	 * @return
	 * @throws Exception 
	 */
	public <A> List<A> query(String sql, Class<A> resultClass) throws Exception {
		
		if (session != null) {
			org.hibernate.SQLQuery query = session.createSQLQuery(sql);
			query.addEntity( "alias1", resultClass.getName(), LockMode.READ );
			return query.list();
		}
		
		Query query = em.createNativeQuery(sql, resultClass);
		return query.getResultList();
	}
	
	public <A> A querySingle(String sql, Class<A> resultClass) throws Exception {
		List<A> list = query(sql, resultClass);
		if (list.isEmpty())
			return null;
		return list.get(0);
	}
	
	/**
	 * 强行提交事务，一般不建议调用，当出现数据库死锁问题时进行调用。
	 */
	public void flush() {
		if (session != null) {
			session.flush();
		} else {
			em.flush();
		}
	}
	
	/**
	 * 根据sql获取List对象
	 * @param sql
	 * @return
	 * @throws Exception 
	 */
	public <A> List<A> query(String sql, Object[] params, Class<A> resultClass) throws Exception {
		if (session != null) {
			org.hibernate.SQLQuery query = session.createSQLQuery(sql);
			query.addEntity( "alias1", resultClass.getName(), LockMode.READ );
			for (int i = 0; i < params.length; i++) {
				query.setParameter(i + 1, params[i]);
			}
			return query.list();
		}
		
		Query query = em.createNativeQuery(sql, resultClass);
		for (int i = 0; i < params.length; i++) {
			query.setParameter(i + 1, params[i]);
		}
		return query.getResultList();
	}
	
	public <A> A querySingle(String sql, Object[] params, Class<A> resultClass) throws Exception {
		List<A> list = query(sql, params, resultClass);
		if (list.isEmpty())
			return null;
		return list.get(0);
	}
	
	/**
	 * 根据sql获取List对象
	 * @param sql
	 * @return
	 * @throws Exception 
	 */
	public <A> List<A> query(String sql, Object param, Class<A> resultClass) throws Exception {
		
		if (session != null) {
			org.hibernate.SQLQuery query = session.createSQLQuery(sql);
			query.addEntity( "alias1", resultClass.getName(), LockMode.READ );
			query.setParameter(0, param);
			return query.list();
		}
		
		Query query = em.createNativeQuery(sql, resultClass);
		query.setParameter(1, param);
		return query.getResultList();
	}
	
	public <A> A querySingle(String sql, Object param, Class<A> resultClass) throws Exception {
		List<A> list = query(sql, param, resultClass);
		if (list.isEmpty())
			return null;
		return list.get(0);
	}
	
	/**
	 * 根据sql获取List对象
	 * @param sql
	 * @return
	 * @throws Exception 
	 */
	public <A> List<A> query(String sql, List<Object> params, Class<A> resultClass) throws Exception {
		return query(sql, params.toArray(new Object[0]), resultClass);
	}
	
	public <A> A querySingle(String sql, List<Object> params, Class<A> resultClass) throws Exception {
		List<A> list = query(sql, params.toArray(new Object[0]), resultClass);
		if (list.isEmpty())
			return null;
		return list.get(0);
	}
	
	public PageItem query(String sql, Object[] params, Class<?> resultClass, PageParams pageParams) {
		Assert.notNull(pageParams);
		String countsql = "select count(*) from ("+sql+") t";
		
		Field[] fields = resultClass.getFields();
		Field colfield = null;
		for(int i = 0; i < fields.length; i++) {
			if (pageParams.sortcol != null && fields[i].getName().toLowerCase().equals(pageParams.sortcol.toLowerCase())) {
				colfield = fields[i];
			}
		}
		
		String dbFieldName = pageParams.sortcol;
		if (colfield != null)
			dbFieldName = getDBFieldName(colfield);
		
		if (pageParams.isSort()) {
			sql += " order by " + dbFieldName + " " + (pageParams.isup?"":"desc");
		}
		return getPageInnerBySql(pageParams.page, pageParams.numPerPage, countsql, sql, params, resultClass);
	}
	
	public PageItem query(String sql, List<Object> params, Class<?> resultClass, PageParams pageParams) {
		return query(sql, params.toArray(new Object[0]), resultClass, pageParams);
	}
	
	public PageItem query(String sql, Object param, Class<?> resultClass, PageParams pageParams) {
		return query(sql, new Object[]{param}, resultClass, pageParams);
	}
	
	public PageItem query(String sql, Class<?> resultClass, PageParams pageParams) {
		Assert.notNull(pageParams);
		String countsql = "select count(*) from ("+sql+") t";
		if (pageParams.isSort()) {
			sql += " order by " + pageParams.sortcol + " " + (pageParams.isup?"":"desc");
		}
		return getPageInnerBySql(pageParams.page, pageParams.numPerPage, countsql, sql, new Object[]{}, resultClass);
	}
	
	/**
	 * 对数据进行批量更新或修改
	 */
	public <A> void batchInsertOrUpdate(List<A> beanList) throws IllegalArgumentException, SQLException, IllegalAccessException, InvocationTargetException {
		for (int i = 0; i < beanList.size(); i++) {
			insertOrUpdate(beanList.get(i));
		}
	}
	
	/**
	 * 查询搜索结果
	 * @param origSql
	 * @param cols
	 * @param coltitles
	 * @param enums // 如果对应列为枚举型，则需要将列枚举对应xml名称传入, 否则传入一个null即可
	 * @param searchParam
	 * @return
	 * @throws Exception 
	 */
	public <A> SearchResult<A> getSearchResult(String origSql, 
			String[] cols, String[] coltitles, String[] enums, SearchParam searchParam, Class<A> clazz, String locale) throws Exception {
		
		List<SearchColInfo> colinfos = new ArrayList<SearchColInfo>();
		for (int i = 0; i < cols.length; i++) {
			SearchColInfo colinfo = new SearchColInfo();
			colinfo.colname = cols[i];
			colinfo.coltitle = coltitles[i];
			if (stringSet(enums[i])) {
				colinfo.enums = XmlEnumsUtil.getXmlEnumItems(enums[i], locale);
			}
			colinfos.add(colinfo);
		}
		
		if (!stringSet(searchParam.keyword)) {
			List<A> results = query(origSql, clazz);
			SearchResult<A> sr = new SearchResult<A>();
			sr.resultData = results;
			sr.colinfos = colinfos;
			return sr;
		}
		
		// 准备
		StringBuilder sb = new StringBuilder(origSql.trim().toLowerCase());
		
		int orderByIndex = sb.indexOf("order by");
		CharSequence orderby = orderByIndex > 0 ? sb.subSequence(orderByIndex, sb.length())
				: "ORDER BY CURRENT_TIMESTAMP";
		
		// Delete the order by clause at the end of the query
		if (orderByIndex > 0) {
			sb.delete(orderByIndex, orderByIndex + orderby.length());
		}
		
		String finalsql = "select * from (" + sb + ") t where ";
		
		
		
		List<String> must = new ArrayList<String>();
		List<String> may = new ArrayList<String>();
		List<String> params = new ArrayList<String>();
	
		List<String> containvalues = new ArrayList<String>();
		for (int i = 0; i < cols.length; i++) {
			String wherepart = cols[i] + DBHelper.getLikePart();
			
			if (stringSet(enums[i])) {
				wherepart = null;
				containvalues.clear();
				XmlEnumItem[] enumitems = XmlEnumsUtil.getXmlEnumItems(enums[i], locale);
				for (int j = 0; j < enumitems.length; j++) {
					if (enumitems[j].label.indexOf(searchParam.keyword) != -1) 
						containvalues.add(enumitems[j].value);
				}
				if (stringSet(TextUtils.join("','", containvalues)))
					wherepart = cols[i] + " in ('" + TextUtils.join("','", containvalues) + "')";
			} else
				params.add(searchParam.keyword);
			
			if (stringSet(wherepart)) {
				if (searchParam.filterKeys.contains(cols[i])) {
					must.add(wherepart);
					if (!stringSet(enums[i])) {
						params.add(searchParam.keyword);
					}
				} 
				may.add(wherepart);
			}
		}
		
		String muststr = TextUtils.join(" and ", must);
		if (!stringSet(muststr))
			muststr = " 1=1 ";
		finalsql += muststr + " and (" + TextUtils.join(" or ", may) + ") " + orderby;
		
		List<A> results = query(finalsql, params.toArray(new Object[0]), clazz);
		SearchResult<A> sr = new SearchResult<A>();
		
		sr.resultData = results;
		sr.colinfos = colinfos;
		
		return sr;
	}
	
	/**
	 * 添加新项目或者更新已有项目
	 * 若根据id或者联合id找到项目则更新，否则添加一个新项目进去。
	 * @param instance 实体实例
	 * @throws SQLException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException 
	 */
	public <A> void insertOrUpdate(A instance) throws SQLException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Field[] fields = instance.getClass().getFields();
		Method[] methods = instance.getClass().getMethods();
		Object keyval = "";
		
		for (int i = 0; i < fields.length; i++) {
			if (fields[i].getAnnotation(Id.class) != null) {
				keyval = fields[i].get(instance);
				break;
			}
		}
		
		for (int i = 0; i < methods.length; i++) {
			if (methods[i].getAnnotation(Id.class) != null) {
				keyval = methods[i].invoke(instance);
				break;
			}
		}
		
		if (session != null) {
			if (keyval != null && session.get(instance.getClass(), (Serializable) keyval) != null) {
				session.merge(instance);
			} else {
				session.persist(instance);
			}
		} else {
			if (keyval != null && em.find(instance.getClass(), keyval) != null) {
				em.merge(instance);
			} else {
				em.persist(instance);
			}
		}
		
	}
	
	public int updateBySQL(String sql, Object[] params) {
		Assert.notNull(params);
		
		if (session != null) {
			org.hibernate.SQLQuery query = session.createSQLQuery(sql);
			for (int i = 0; i < params.length; i++) {
				query.setParameter(i, params[i]);
			}
			return query.executeUpdate();
		}
		
		Query query = em.createNativeQuery(sql);
		for (int i = 0; i < params.length; i++) {
			query.setParameter(i + 1, params[i]);
		}
		return query.executeUpdate();
	}
	
	/**
	 * 数据按照排序字段和移动参数移动，若向上移动并且该数据已经到第一位，或者向下移动该数据已经到最后一位，则调用此方法无效果
	 * 若没有排序字段，则报错
	 * 此方法仅支持以public数据域为数据存储的实体类
	 * @param params
	 * @param clazz
	 * @param reverse 是否颠倒排序调整过程，默认为按照系统列表倒序排列时，向上向下移动，颠倒之后则按系统列表正序排列时，向上向下移动
	 * @throws Exception 
	 * @throws SQLException 
	 */
	public void moveUpOrDown(MoveParams params, Class<?> clazz) throws SQLException, Exception {
		moveUpOrDown(null, new Object[0], params, clazz, false);
	}
	
	/**
	 * 数据按照排序字段和移动参数移动，若向上移动并且该数据已经到第一位，或者向下移动该数据已经到最后一位，则调用此方法无效果
	 * 若没有排序字段，则报错
	 * 此方法仅支持以public数据域为数据存储的实体类
	 * @param datarangesql 数据查询范围sql，用来确定排序时数据查询范围
	 * @param params
	 * @param clazz
	 * @param reverse 是否颠倒排序调整过程，默认为按照系统列表倒序排列时，向上向下移动，颠倒之后则按系统列表正序排列时，向上向下移动
	 * @throws Exception 
	 * @throws SQLException 
	 */
	public void moveUpOrDown(String dataRangeSql, Object[] sqlparams, MoveParams moveparams, Class<?> clazz) throws SQLException, Exception {
		moveUpOrDown(dataRangeSql, sqlparams, moveparams, clazz, false);
	}
	
	/**
	 * 数据按照排序字段和移动参数移动，若向上移动并且该数据已经到第一位，或者向下移动该数据已经到最后一位，则调用此方法无效果
	 * 若没有排序字段，则报错
	 * 此方法仅支持以public数据域为数据存储的实体类
	 * @param datarangesql 数据查询范围sql，用来确定排序时数据查询范围
	 * @param params
	 * @param clazz
	 * @param reverse 是否颠倒排序调整过程，默认为按照系统列表倒序排列时，向上向下移动，颠倒之后则按系统列表正序排列时，向上向下移动
	 * @throws Exception 
	 * @throws SQLException 
	 */
	public void moveUpOrDown(String dataRangeSql, Object[] sqlparams, MoveParams moveparams, Class<?> clazz, boolean reverse) throws SQLException, Exception {
		moveUpOrDownInner(dataRangeSql, sqlparams, moveparams, clazz, reverse);	
	}

	private void moveUpOrDownInner(String dataRangeSql, Object[] sqlparams,
			MoveParams moveparams, Class<?> clazz, boolean reverse)
			throws SQLException, Exception, JSONException {
		Field[] fields = clazz.getFields();
		boolean hassortfield = false;
		for(int i = 0; i < fields.length; i++) {
			if ("sortfield".equals(fields[i].getName())) {
				hassortfield = true;
				break;
			}
		}
		
		if (hassortfield) {
			String tbname = getDBTableName(clazz);
			
			List<JSONObject> sortvals = DBUtil.queryJSON("select sid, sortfield from " + tbname + " where sid in ('" + TextUtils.join("','", moveparams.sid.split(",")) +"') order by sortfield", new Object[]{});
			
			String sortval = DBUtil.querySingleString("select sortfield from " + tbname + " where sid=?", new Object[]{moveparams.sid});
			JSONObject obj = null;
			
			// 注意，使用排序字段排序，为了适合
			String rangesqlortb = tbname;
			if (stringSet(dataRangeSql)) {
				rangesqlortb = "(" + dataRangeSql + ") _t__";
			}
			
			Object[] finalsqlparams = new Object[sqlparams.length + 1];
			for (int i = 0; i < finalsqlparams.length - 1; i++) {
				finalsqlparams[i] = sqlparams[i];
			}
			
			if (reverse ^ moveparams.isup) {
				finalsqlparams[finalsqlparams.length - 1] = sortvals.get(sortvals.size() - 1).get("sortfield");
			} else {
				finalsqlparams[finalsqlparams.length - 1] = sortvals.get(0).get("sortfield");
			}
			
			PageParams pageParams = new PageParams("1", moveparams.updowntimes);
			
			List<JSONObject> moveobjs = null;
			
			
			
			if (reverse ^ moveparams.isup) {
					//obj = DBUtil.querySingleJSON("select sid, sortfield from " + rangesqlortb + " where sortfield < ? order by sortfield desc", finalsqlparams);
				moveobjs = DBUtil.queryJSON("select sid, sortfield from " + rangesqlortb + " where sortfield > ? order by sortfield", finalsqlparams, pageParams).getCurrList();
			} else {
				moveobjs = DBUtil.queryJSON("select sid, sortfield from " + rangesqlortb + " where sortfield < ? order by sortfield desc", finalsqlparams, pageParams).getCurrList();
				//obj = DBUtil.querySingleJSON("select sid, sortfield from " + rangesqlortb + " where sortfield > ? order by sortfield", finalsqlparams);
			}
			if (moveobjs.isEmpty())
				return;
			
			int finalmovetimes = moveobjs.size();
			
			if (reverse ^ moveparams.isup) {
				for (int i = 0; i < sortvals.size(); ++i) {
					moveobjs.add(0, sortvals.get(sortvals.size() - i - 1));
				}
			} else {
				for (int i = 0; i < sortvals.size(); ++i) {
					moveobjs.add(0, sortvals.get(i));
				}
			}
			
			for (int j = 0; j < finalmovetimes; ++j) {
				String prefixobjsortfield = moveobjs.get(moveobjs.size() - 1).getString("sid");
				for (int i  = moveobjs.size() - 1; i > 0; --i) {
					moveobjs.get(i).put("sid", moveobjs.get(i - 1).get("sid"));
				}
				moveobjs.get(0).put("sid", prefixobjsortfield);
			}
			
			for (int i = 0; i < moveobjs.size(); ++i) {
				updateBySQL("update " + tbname + " set sortfield=? where sid=?", new Object[]{moveobjs.get(i).get("sortfield"), moveobjs.get(i).getString("sid")});
			}
			
			//updateBySQL("update " + tbname + " set sortfield=? where sid=?", new Object[]{sortval, obj.getString("sid")});
			//updateBySQL("update " + tbname + " set sortfield=? where sid=?", new Object[]{obj.getString("sortfield"), moveparams.sid});
		} else {
			Assert.state(false, "移动数据位置的数据未包含sortfield排序字段，若确认要移动请添加后再试！");
		}
	}
	
	public int updateBySQL(String sql, Object param) {
		return updateBySQL(sql, new Object[]{param});
	}
	
	public int updateBySQL(String sql) {
		if (session != null) {
			SQLQuery query = session.createSQLQuery(sql);
			return query.executeUpdate();
		}
		Query query = em.createNativeQuery(sql);
		return query.executeUpdate();
	}
	
	public int updateBySQL(String sql, List<Object> params) {
		return updateBySQL(sql, params.toArray(new Object[0]));
	}
	
	/**
	 * 根据实体删除数据
	 * @param key 主键数据
	 * @param clazz 实体对象信息
	 * @return 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws SQLException
	 */
	public <A> int deleteBy(Object key, Class<A> clazz) throws InstantiationException, IllegalAccessException  {
		Entity annoenti = clazz.getAnnotation(Entity.class);
		if (annoenti != null) {
			String entiName = annoenti.name();
			if (!stringSet(entiName)) {
				entiName = clazz.getSimpleName();
			}
			
			Field[] fields = clazz.getFields();
			String keycol = "";
			for (int i = 0; i < fields.length; i++) {
				Id anno = fields[i].getAnnotation(Id.class);
				if (anno != null) {
					keycol = fields[i].getName();
					break;
				}
			}
			
			if (!stringSet(keycol)) {
				Method[] methods = clazz.getMethods();
				for (Method method : methods) {
					if (method.getName().startsWith("get") && method.getAnnotation(Id.class) != null) {
						keycol = Character.toLowerCase(method.getName().charAt(3)) + method.getName().substring(4);  
					}
				}
			}
			String dbtablename = getDBTableName(clazz);
			if (session != null) {
				org.hibernate.Query query = session.createSQLQuery("delete from "+dbtablename+" where " +keycol + "= ?");
				query.setParameter(0, key);
				return query.executeUpdate();
			}
			Query query = em.createNativeQuery("delete from "+dbtablename+" where " +keycol + "= ?" );
			query.setParameter(1, key);
			return query.executeUpdate();
		}
		
		return 0;
	}
	
	public void remove(Object obj) {
		if (session != null) {
			session.delete(obj);
			return;
		}
		em.remove(obj);
	}
	
	private static void putAllResultIntoList (ResultSet rs, List<JSONObject> daeInfoList) throws SQLException {
		while (rs.next()) {
			daeInfoList.add(putSingleResultIntoJSONObject(rs));
		}
	}
	
	private static JSONObject putSingleResultIntoJSONObject(ResultSet rs) throws SQLException {
		JSONObject jsonObj = new JSONObject ();
		putSingleResultIntoJSONObject(rs, jsonObj);
		return jsonObj;
	}
	
	private static void putSingleResultIntoJSONObject(ResultSet rs, JSONObject obj) throws SQLException {
		ResultSetMetaData metaData = rs.getMetaData();
		for (int i = 1; i <= metaData.getColumnCount(); ++i) {
			try {
				if (rs.getObject(i) instanceof Date) {
					Timestamp timestamp = rs.getTimestamp(i);
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					obj.put(metaData.getColumnLabel(i).toLowerCase(), sdf.format(timestamp));
				} else {
					obj.put(metaData.getColumnLabel(i).toLowerCase(), rs.getObject(i));
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 判断字符串是否为空
	 * @param clazz
	 * @return
	 */
    private final static boolean stringSet(String string) {
        return (string != null) && !"".equals(string);
    }
    
}
