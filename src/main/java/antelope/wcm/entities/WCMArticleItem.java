package antelope.wcm.entities;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="WCM_ARTICLE")
public class WCMArticleItem {
	
	@Id
	public String sid; 
	public String name; 
	public String content; 
	public String catesid; 
	public String authorname;
	public Timestamp createtime; 
	public String digest;
	public String source;
}
