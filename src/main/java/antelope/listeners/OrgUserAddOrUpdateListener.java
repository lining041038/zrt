package antelope.listeners;

import antelope.events.OrgUserAddOrUpdateEvent;

/**
 * 组织机构管理中用户添加或修改事件
 * @author lining
 * @since 2011-10-20
 */
public interface OrgUserAddOrUpdateListener {
	public void afterAddOrUpdated(OrgUserAddOrUpdateEvent event);
}
