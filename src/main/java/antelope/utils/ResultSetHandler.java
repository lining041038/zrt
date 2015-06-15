package antelope.utils;


import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 结果集处理接口 
 * @author lining
 */
public interface ResultSetHandler {

    public Object handle(ResultSet rs) throws Exception;

}