package antelope.wcm.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="WCM_ASSET")
public class WCMAssetItem {
	
	@Id
	public String sid; 
	public String name; 
	public byte[] assetdata; 
	public String assettype;
}
