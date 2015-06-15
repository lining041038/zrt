package antelope.wcm.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="WCM_CONTAINER_DATA")
public class WCMContainerData {
	
	@Id
	public String sid; 
	public String assetsid; 
	public String pagesid; 
	public String containersid; 
}
