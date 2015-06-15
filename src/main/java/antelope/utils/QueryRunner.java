package antelope.utils;



import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import antelope.db.DBUtil;
import antelope.db.ResultWrapper;

public class QueryRunner {
	/** 数据源 */
	protected final DataSource ds;

	/**
	 * 默认构造方法
	 */
	public QueryRunner() {
		ds = null;
	}
	
	/**
	 * 使用数据源创建的构造方法
	 * @param ds 数据源
	 */
	public QueryRunner(DataSource ds) {
		this.ds = ds;
	}
	
	/**
	 * 执行批量SQL时调用的方法
	 * @param conn 数据库连接
	 * @param sql sql语句
	 * @param params sql语句参数
	 * @return 影响的行数
	 * @throws SQLException 
	 */
	public int[] batch(Connection conn, String sql, Object[][] params) throws SQLException {

		PreparedStatement stmt = null;
		int[] rows = null;
		try {
			stmt = this.prepareStatement(conn, sql);

			for (int i = 0; i < params.length; i++) {
				this.fillStatement(stmt, params[i]);
				stmt.addBatch();
			}
			rows = stmt.executeBatch();

		} catch (SQLException e) {
			this.rethrow(e, sql, params);
		} finally {
			close(stmt);
		}

		return rows;
	}
	
	/**
	 * 
	 * @param sql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public int[] batch(final String sql, final Object[][] params) throws SQLException {
		
		Session session = DBUtil.getSession();
		final QueryRunner thisObj = this;
		
		final ResultWrapper resultwrapper = new ResultWrapper();
		
		session.doWork(new Work() {
			@Override
			public void execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				int[] rows = null;
				try {
					stmt = thisObj.prepareStatement(conn, sql);

					for (int i = 0; i < params.length; i++) {
						thisObj.fillStatement(stmt, params[i]);
						stmt.addBatch();
					}
					rows = stmt.executeBatch();

				} catch (SQLException e) {

					thisObj.rethrow(e, sql, params);
				} finally {
					close(stmt);
				}

				resultwrapper.result = rows;
			}
		});

		return (int[]) resultwrapper.result;
	}

	public void fillStatement(PreparedStatement stmt, Object[] params) throws SQLException {

		if (params == null) {
			return;
		}
		
		for (int i = 0; i < params.length; i++) {
			stmt.setObject(i + 1, params[i]);
		}
	}

	public void fillStatementWithBean(PreparedStatement stmt, Object bean, PropertyDescriptor[] properties) throws SQLException {
		Object[] params = new Object[properties.length];
		for (int i = 0; i < properties.length; i++) {
			PropertyDescriptor property = properties[i];
			Object value = null;
			Method method = property.getReadMethod();
			if (method == null) {
				throw new RuntimeException("No read method for bean property " + bean.getClass() + " " + property.getName());
			}
			try {
				value = method.invoke(bean, new Object[0]);
			} catch (InvocationTargetException e) {
				throw new RuntimeException("Couldn't invoke method: " + method, e);
			} catch (IllegalArgumentException e) {
				throw new RuntimeException("Couldn't invoke method with 0 arguments: " + method, e);
			} catch (IllegalAccessException e) {
				throw new RuntimeException("Couldn't invoke method: " + method, e);
			}
			params[i] = value;
		}
		fillStatement(stmt, params);
	}

	public void fillStatementWithBean(PreparedStatement stmt, Object bean, String[] propertyNames) throws SQLException {
		PropertyDescriptor[] descriptors;
		try {
			descriptors = Introspector.getBeanInfo(bean.getClass()).getPropertyDescriptors();
		} catch (IntrospectionException e) {
			throw new RuntimeException("Couldn't introspect bean " + bean.getClass().toString(), e);
		}
		PropertyDescriptor[] sorted = new PropertyDescriptor[propertyNames.length];
		for (int i = 0; i < propertyNames.length; i++) {
			String propertyName = propertyNames[i];
			if (propertyName == null) {
				throw new NullPointerException("propertyName can't be null: " + i);
			}
			boolean found = false;
			for (int j = 0; j < descriptors.length; j++) {
				PropertyDescriptor descriptor = descriptors[j];
				if (propertyName.equals(descriptor.getName())) {
					sorted[i] = descriptor;
					found = true;
					break;
				}
			}
			if (!found) {
				throw new RuntimeException("Couldn't find bean property: " + bean.getClass() + " " + propertyName);
			}
		}
		fillStatementWithBean(stmt, bean, sorted);
	}

	public DataSource getDataSource() {
		return this.ds;
	}

	protected PreparedStatement prepareStatement(Connection conn, String sql) throws SQLException {

		return conn.prepareStatement(sql);
	}

	protected Connection prepareConnection() throws SQLException {
		if (this.getDataSource() == null) {
			throw new SQLException("QueryRunner requires a DataSource to be " + "invoked in this way, or a Connection should be passed in");
		}
		return this.getDataSource().getConnection();
	}

	public Object query(Connection conn, final String sql, final ResultSetHandler rsh, final Object[] params) throws Exception {

		PreparedStatement stmt = null;
		ResultSet rs = null;
		Object result = null;

		try {
			stmt = this.prepareStatement(conn, sql);
			this.fillStatement(stmt, params);
			rs = this.wrap(stmt.executeQuery());
			result = rsh.handle(rs);

		} catch (SQLException e) {
			this.rethrow(e, sql, params);

		} finally {
			try {
				close(rs);
			} finally {
				close(stmt);
			}
		}
		return result;
	}

	public Object query(Connection conn, String sql, ResultSetHandler rsh) throws Exception {

		return this.query(conn, sql, rsh, (Object[]) null);
	}

	public Object query(final String sql, final ResultSetHandler rsh, final Object[] params) throws Exception {
		Session session = DBUtil.getSession();
		final QueryRunner thisObj = this;
		
		final ResultWrapper resultwrapper = new ResultWrapper();
		
		session.doWork(new Work() {
			@Override
			public void execute(Connection connection) throws SQLException {
				PreparedStatement stmt = null;
				ResultSet rs = null;
				
				try {
					stmt = thisObj.prepareStatement(connection, sql);
					thisObj.fillStatement(stmt, params);
					queryRunnerSqlLog(sql, params);
					rs = thisObj.wrap(stmt.executeQuery());
					resultwrapper.result = rsh.handle(rs);
				} catch (SQLException e) {
					thisObj.rethrow(e, sql, params);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					try {
						close(rs);
					} finally {
						close(stmt);
					}
				}
			}
		});

		return resultwrapper.result;
	}

	public Object query(String sql, ResultSetHandler rsh) throws Exception {
		return this.query(sql, rsh, (Object[]) null);
	}

	protected void rethrow(SQLException cause, String sql, Object[] params) throws SQLException {

		String causeMessage = cause.getMessage();
		if (causeMessage == null) {
			causeMessage = "";
		}
		StringBuffer msg = new StringBuffer(causeMessage);

		msg.append(" Query: ");
		msg.append(sql);
		msg.append(" Parameters: ");

		if (params == null) {
			msg.append("[]");
		} else {
			msg.append(Arrays.asList(params));
		}

		SQLException e = new SQLException(msg.toString(), cause.getSQLState(), cause.getErrorCode());
		e.setNextException(cause);

		throw e;
	}
	
	protected void queryRunnerSqlLog(String sql, Object[] params) {
		StringBuffer msg = new StringBuffer();

		msg.append(" Query: ");
		msg.append(sql);
		msg.append(" Parameters: ");

		if (params == null) {
			msg.append("[]");
		} else {
			msg.append(Arrays.asList(params));
		}
		
		Logger log = Logger.getLogger(this.getClass());
		log.info(msg.toString());
	}

	public int update(Connection conn, String sql) throws SQLException {
		return this.update(conn, sql, (Object[]) null);
	}

	public int update(Connection conn, String sql, Object param) throws SQLException {

		return this.update(conn, sql, new Object[] { param });
	}

	public int update(Connection conn, String sql, Object[] params) throws SQLException {

		PreparedStatement stmt = null;
		int rows = 0;

		try {
			stmt = this.prepareStatement(conn, sql);
			this.fillStatement(stmt, params);
			rows = stmt.executeUpdate();

		} catch (SQLException e) {
			this.rethrow(e, sql, params);

		} finally {
			close(stmt);
		}

		return rows;
	}

	public int update(String sql) throws SQLException {
		return this.update(sql, (Object[]) null);
	}

	public int update(String sql, Object param) throws SQLException {
		return this.update(sql, new Object[] { param });
	}

	public int update(final String sql, final Object[] params) throws SQLException {

		Session session = DBUtil.getSession();
		final QueryRunner thisObj = this;
		
		final ResultWrapper resultwrapper = new ResultWrapper();
		
		session.doWork(new Work() {
			@Override
			public void execute(Connection conn) throws SQLException {
				PreparedStatement stmt = null;
				int rows = 0;

				try {
					stmt = thisObj.prepareStatement(conn, sql);
					thisObj.fillStatement(stmt, params);
					rows = stmt.executeUpdate();

				} catch (SQLException e) {
					thisObj.rethrow(e, sql, params);

				} finally {
					close(stmt);
				}
				resultwrapper.result = rows;
			}
		});

		return (Integer) resultwrapper.result;
	}

	protected ResultSet wrap(ResultSet rs) {
		return rs;
	}

	protected void close(Connection conn) throws SQLException {
		if (conn != null)
			conn.close();
	}

	protected void close(Statement stmt) throws SQLException {
		if (stmt != null)
			stmt.close();
	}

	protected void close(ResultSet rs) throws SQLException {
		if (rs != null)
			rs.close();
	}
}
