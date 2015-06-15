package antelope.wcm.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="WCM_PRODUCT_CATE")
public class WCMProductCateItem {
	
	@Id
	public String sid; 
	public String name; 
	public String parentsid; 
}
