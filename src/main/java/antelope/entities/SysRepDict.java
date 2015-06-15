package antelope.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;



@Entity
@Table(name="sys_rep_dict")
public class SysRepDict {
	@Id
	public String sid;
	public String title;
	// 选择模式，1为单选模式，2为复选模式，3为下拉列表模式
	public Integer selectmode;
}