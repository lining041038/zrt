package antelope.beans;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class SysFileInfo {
	@Id
	public String sid;
	public String filegroupsid;
	public String filename;
	public Integer filesize;
	public Integer ispermanent;
	public String uploadtimeid;
	public Timestamp uploaddate;
	public String willdelete;
}