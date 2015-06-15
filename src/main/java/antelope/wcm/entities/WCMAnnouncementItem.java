package antelope.wcm.entities;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="WCM_ANNOUNCEMENT")
public class WCMAnnouncementItem {
	
	@Id
	public String sid; 
	public String name; 
	public String content; 
	public String creatorsid; 
	public String creatorname; 
	public Timestamp createtime; 
}
