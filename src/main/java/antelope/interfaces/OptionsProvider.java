package antelope.interfaces;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

/**
 * 选项提供器
 * @author lining
 * @since 2012-7-14
 */
public interface OptionsProvider {
	public abstract Object getOptions(HttpServletRequest req) throws SQLException, Exception;
}
