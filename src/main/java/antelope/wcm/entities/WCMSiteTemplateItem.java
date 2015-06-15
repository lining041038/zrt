package antelope.wcm.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="WCM_SITE_TEMPLATE")
public class WCMSiteTemplateItem {
	
	@Id
	public String sid; 
	public String name; 
	public String tmplpath; 
}
