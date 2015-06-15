package antelope.interfaces.components.supportclasses;

/**
 * 审批窗口中内容显示模式
 * @author lining
 * @since 2014-08-13
 */
public class AuditFormDisplayMode {
	/**
	 * 三页签模式，基本信息一个页签，审批一个页签，流程图一个页签
	 */
	public static final String NORMAL_THREE_TAB = "threetab";
	
	/**
	 * 三页签，审批页签,流程历史页签及流程图页签，其中审批页签中将包含审批基本信息页面相关信息
	 */
	public static final String AUDIT_HIS_PROCESSCHART_TAB= "auditprocesscharttab";
}
