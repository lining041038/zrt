package antelope.wcm.entities;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="WCM_PEOPLE_MSG")
public class WCMPeopleMsgItem {
	
	@Id
	public String sid; 
	public String name; 
	public String email;
	public String phonenum;
	public String content; 
	public Timestamp createtime; 
	public String sitesid;
	
	public String username;
	public String identityCard;
	public String topic;
	public String profession;
	public String address;
	public String title;
}
