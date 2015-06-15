package antelope.entities;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Smartdot Corporation Copyright (c) 2011</p>
 * <p>Company: BEIJING Smartdot SOFTWARE CO.,LTD</p>
 * @author lining
 * @version 1.0
 */
@Entity
@Table(name="SYS_FILES")
public class SysFile {
	@Id
	public String sid;
	public String filegroupsid;
	public String filename;
	public Integer filesize;
	public byte[] filedata;
	public Integer ispermanent;
	public String uploadtimeid;
	public Timestamp uploaddate;
}
