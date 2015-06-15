package antelope.wcm.entities;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="WCM_SITE_PUBLISH_HIS")
public class WCMPublishHistory {

	@Id
	public String sid; 
	public String sitesid; 
	public Timestamp createtime;
	
}
