package antelope.wcm.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="WCM_PAGE")
public class WCMPageItem {
	@Id
	public String sid; 
	public String name; 
	public String content; 
	public String sitesid; 
}
