package antelope.entities;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * activiti扩展审批历史
 * @author lining
 * @since 2012-8-8
 */
@Entity
@Table(name="ACT_AN_AUDIT_HIS")
public class AuditHistory {
	@Id
	public String sid;
	public String proc_inst_id_;
	/**
	 * 系统单位名称
	 */
	public String unitname; 
	/**
	 * 审批人（受让人）用户名
	 */
	public String assignee; 
	/**
	 * 审批人（受让人）真实姓名
	 */
	public String assigneename; 
	/**
	 * 审批结果（如通过或不通过）
	 */
	public String result; 
	/**
	 * 审批时节点任务名称
	 */
	public String taskname;
	/**
	 * 审批意见
	 */
	@Column(name="comment_")
	public String comment;
	/**
	 * 创建时间（审批时间）
	 */
	public Timestamp createtime;
}
