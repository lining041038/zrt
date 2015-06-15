package antelope.example;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name="myversion")
@Table(name="myversion")
public class Version {
	@Id
	public String sid;
	
	public String version;
	
	public String tmpl_sid;
	
	public String data_sid;
}
