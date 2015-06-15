package antelope.dbdeploy.database.changelog;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

public class QueryExecuter {
	private final Connection connection;

    public QueryExecuter(String connectionString, String username, String password) throws SQLException {
        connection = DriverManager.getConnection(connectionString, username, password);
	}
    
    public QueryExecuter(DataSource dataSource) throws SQLException {
        connection = dataSource.getConnection();
	}

	public ResultSet executeQuery(String sql) throws SQLException {
		Statement statement = connection.createStatement();
		return statement.executeQuery(sql);
	}

	public void execute(String sql) throws SQLException {
		Statement statement = connection.createStatement();
		try {
			statement.execute(sql);
		} finally {
			statement.close();
		}
	}

    public void execute(String sql, Object... params) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(sql);
        try {
            for (int i = 0; i < params.length; i++) {
                Object param = params[i];
                statement.setObject(i+1, param);
            }
            statement.execute();
        } finally {
            statement.close();
        }
    }

	public void close() throws SQLException {
		connection.close();
	}

	public void setAutoCommit(boolean autoCommitMode) throws SQLException {
		connection.setAutoCommit(autoCommitMode);
	}

	public void commit() throws SQLException {
		connection.commit();
	}

    public String getDatabaseUsername() {
        return "dataSource";
    }
}
