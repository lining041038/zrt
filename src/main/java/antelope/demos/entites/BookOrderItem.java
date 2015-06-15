package antelope.demos.entites;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="DEMO_BOOKORDER")
public class BookOrderItem implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	public String sid; 
	public String bookname; 
	public String creatorsid; 
	public String creatorname; 
	public Timestamp createtime; 
	public String proc_inst_id_;
	
	
}
