package antelope.wcm.interfaces;

import java.util.List;

import antelope.wcm.beans.ArticleBean;
import antelope.wcm.beans.ChannelProgramBean;

/**
 * 频道栏目与文档的数据提供者接口，用于获取频道栏目与文章等信息
 * @author lining
 * @since 2013-3-29
 */
public interface ChannelProgramAndArticleDataProvider {
	
	/**
	 * 根据父频道或栏目sid获取频道栏目信息列表,若父频道栏目为null或者空字符串，则获取顶级频道栏目相关数据
	 * @param parentsid 父频道或栏目sid 
	 * @return 子频道或栏目信息类
	 */
	public List<ChannelProgramBean> getChannelProgramsByParentsid(String parentsid) throws Exception;
	
	/**
	 * 根据频道或栏目sid获取单个频道栏目信息
	 * @param sid 频道或栏目sid 
	 * @return 频道或栏目信息类
	 */
	public ChannelProgramBean getChannelProgramBySid(String sid) throws Exception;
	
	/**
	 * 根据频道或栏目sid获取文章信息
	 * @param parentsid
	 * @return 文章信息
	 */
	public List<ArticleBean> getArticlesByParentsid(String parentsid) throws InstantiationException, IllegalAccessException, Exception;
}
