package antelope.wcm.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="WCM_SITE_TEMPLATE_SETTING")
public class WCMSiteTemplateSettingItem {
	
	@Id
	public String sid; 
	public String name; 
	public String settingcode; 
	public String settingvalue; 
	public String sitesid;
}
