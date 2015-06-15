package antelope.db;








import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import antelope.utils.QueryRunner;
import antelope.utils.RegExpUtil;
import antelope.utils.ResultSetHandler;


/**
 * <p>Title: 可以配置参数的sql工具</p>
 * <p>Description: 此工具用来将sql剥离到代码外层</p>
 * @author lining 
 * @version 1.0
 */
public class Sql{
	private String sql;
	private String id;
	private Map<String, String> otherProp = new HashMap<String, String>();
	private static Pattern ptParam = Pattern.compile("\\$\\{[\\s]*[^}]*[\\s]*\\}");
	private Map<String, String> currPropValues = new HashMap<String, String>();
	private QueryRunner qr = new QueryRunner ();
	
	public Sql() {
	}
	
	public Sql(String sql) {
		this.sql = sql;
	}
	
	public Sql setSql(String sql) {
		this.sql = sql;
		return this;
	}
	
	public String getSql() {
		
		Iterator<String> iter = currPropValues.keySet().iterator();
		String tempsql = this.sql;
		while (iter.hasNext()) {
			String key = iter.next();
			tempsql = tempsql.replaceAll ("\\$\\{[\\s]*" + key + "[\\s]*\\}", currPropValues.get(key));
		}
		
		return tempsql;
	}
	
	public String toString() {
		return getSql();
	}
	
	public Sql addOtherProp(String key, String val) {
		otherProp.put(key, val);
		return this;
	}
	
	public String getOtherProp (String key) {
		return otherProp.get(key).toString();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public Sql setParam (String name, String param) {
		currPropValues.put(name, param);
		return this;
	}
	
	public boolean isReadyToUse () {
		if (RegExpUtil.getFirstMatched(ptParam, this.sql) != null) {
			return false;
		}
		return true;
	}
	
	protected Sql clone() throws CloneNotSupportedException {
		Sql clsql= new Sql ();
		clsql.setId(this.getId());
		clsql.setSql(this.getSql());
		clsql.otherProp.putAll (otherProp);
		return clsql;
	}
	
	public Object query (ResultSetHandler rsh, Object[] params) throws Exception {
		return qr.query(getSql(),rsh, params);
	}
	
	public Object query (ResultSetHandler rsh, List<Object> params) throws Exception {
		return qr.query(getSql(),rsh, params.toArray(new Object[0]));
	}
	
	public Object query (ResultSetHandler rsh) throws Exception {
		return qr.query(getSql(),rsh);
	}
	
	public int update (Object[] params) throws SQLException {
		return qr.update( getSql(), params);
	}
	
	public int update () throws SQLException {
		return qr.update( getSql());
	}
	
	public int[] batch (Object[][] params) throws SQLException {
		return qr.batch( getSql(), params);
	}
	
	
	/**
	 * 含有连接传入的查询，除非在多数据源情况下使用，否则禁止使用
	 */
	public Object query (Connection conn, ResultSetHandler rsh, Object[] params) throws Exception {
		return qr.query(conn, getSql(),rsh, params);
	}
	
	/**
	 * 含有连接传入的查询，除非在多数据源情况下使用，否则禁止使用
	 */
	public int update (Connection conn, Object[] params) throws SQLException {
		return qr.update(conn, getSql(), params);
	}
	
}







