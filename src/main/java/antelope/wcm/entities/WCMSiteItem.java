package antelope.wcm.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="WCM_SITE")
public class WCMSiteItem {
	
	@Id
	public String sid; 
	public String name; 
	public String templatesid; 
	public String templatename;	
	public String activated;
	public String domainname;
}

