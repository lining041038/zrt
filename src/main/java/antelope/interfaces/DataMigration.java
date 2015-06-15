package antelope.interfaces;

import java.sql.Connection;
import java.sql.SQLException;

import antelope.springmvc.BaseComponent;

/**
 * 数据迁移接口
 * @author lining
 * @since 2012-1-27
 */
public abstract class DataMigration extends BaseComponent{
	
	/**
	 * 获取需要进行数据迁移的数据库表名
	 */
	abstract public String[] getDBTableNames();
	
	
	/**
	 * 获取数据迁移名称
	 * @return
	 */
	abstract public String getTitle();
	
	/**
	 * 手动进行数据迁移，当需要进行手动数据迁移时则实现，否则不需要实现
	 * @return 返回值返回需要再次执行的前台脚本方法名称
	 * @throws Exception
	 */
	abstract public String migratData(final Connection otherSystemConn) throws Exception;
	
	/**
	 * 在进行单个表迁移之前执行，默认实现为空，子类需要进行覆盖操作。
	 * @param tablename
	 */
	public void beforeMigrateTable(String tablename, final Connection ourConnection) throws SQLException, Exception {
	}
}
