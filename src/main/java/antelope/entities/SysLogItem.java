package antelope.entities;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 系统日志表
 * @author lining
 * @since 2013-6-18
 */
@Entity
@Table(name="SYS_LOG")
public class SysLogItem {
	@Id
	public String sid; 
	public String loglevel; 
	public String message; 
	public String logclass; 
	public Timestamp createtime; 
}
