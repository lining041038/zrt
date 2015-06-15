package antelope.consts;


public class GlobalConsts {
	
	/**
	 * 系统管理员角色id
	 */
	public static final String ADMIN_ROLE_SID = "153";
	
	/**
	 * 系统管理员角色名称
	 */
	public static final String ADMIN_ROLE_NAME = "系统管理员";
	
	/**
	 * 全局标志位，是否为开发者模式
	 */
	public static final boolean isDevelopMode = System.getProperty("catalina.base") != null && System.getProperty("catalina.base").indexOf("\\.metadata\\.plugins\\") != -1;
	
	/**
	 * 表单暂存自动保存间隔，工作流部分使用，单位为毫秒
	 */
	public static final long tempAutoSaveInterval = 1000 * 60 * 5;
	
	/**
	 * 顶级单位sid, 为固定单位，在单位表中不存在此单位
	 */
	public static final String TOP_UNIT_SID = "orgroot";
	
	/**
	 * 遇到含有树结构的表关系（一般含有parentsid）由于oracle或潜在的其他数据库无法区分null与""，所以统一使用treeroot为根节点的parentsid值，
	 */
	public static final String TREE_ROOT = "treeroot";
	
}
