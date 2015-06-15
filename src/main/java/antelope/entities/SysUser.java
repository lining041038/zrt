package antelope.entities;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="SYS_USER")
public class SysUser {
	@Id
	public String sid;
	public String username;
	public String password;
	public String name;
	public String mail;
	public String parentusersid; // 用户的父级领导sid
	public String extno; // 用户分机号
	public String officephone; //用户办公电话
	public String theme;
	public String creatorsid;
	public String creatorname;
	public Timestamp createtime;
	public String pwdquestion; // 密保问题
	public String pwdanswer; // 密保问题答案
	public String gender; // 性别字段 0 为男 1为女
	
	public SysUser() {
	}

	public SysUser(String sid, String username, String password, String name, String mail, String parentusersid,String extno,String officephone) {
		this.sid = sid;
		this.username = username;
		this.password = password;
		this.name = name;
		this.mail = mail;
		this.parentusersid = parentusersid;
		this.extno = extno;
		this.officephone = officephone;
	}
}