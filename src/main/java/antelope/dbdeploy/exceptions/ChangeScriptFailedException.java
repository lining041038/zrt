package antelope.dbdeploy.exceptions;


import java.sql.SQLException;

import antelope.dbdeploy.scripts.ChangeScript;

public class ChangeScriptFailedException extends DbDeployException {
	private final ChangeScript script;
	private final int statement;
	private final String executedSql;

	public ChangeScriptFailedException(SQLException cause, ChangeScript script,
	                                   int statement, String executedSql) {
		super(cause);
		this.script = script;
		this.statement = statement;
		this.executedSql = executedSql;
	}

	public ChangeScript getScript() {
		return script;
	}

	public String getExecutedSql() {
		return executedSql;
	}

	public int getStatement() {
		return statement;
	}

	@Override
	public String getMessage() {
		return "change script " + script +
				" failed while executing statement " + statement + ":\n"
				+ executedSql + "\n -> " + getCause().getMessage();
	}
}
