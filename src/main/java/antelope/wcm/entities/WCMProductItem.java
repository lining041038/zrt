package antelope.wcm.entities;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="WCM_PRODUCT")
public class WCMProductItem {
	
	@Id
	public String sid; 
	public String name; 
	public String content; 
	public String catesid; 
	public String productimgsid;
	public Timestamp createtime; 
	public Double productprice;
}
