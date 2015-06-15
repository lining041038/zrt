package antelope.demos;

import java.util.List;

import org.springframework.stereotype.Component;

import antelope.consts.GlobalConsts;
import antelope.demos.entites.LeftTreeItem;
import antelope.demos.entites.SingleDataGridItem;
import antelope.springmvc.BaseComponent;
import antelope.utils.ListUtils;
import antelope.wcm.beans.ArticleBean;
import antelope.wcm.beans.ChannelProgramBean;
import antelope.wcm.interfaces.ChannelProgramAndArticleDataProvider;

/**
 * 使用演示用表中的数据作为演示用频道栏目数据提供者
 * @author lining
 * @since 2013-3-29
 */
@Component("demochannelprogramdataprovider")
public class DemoChannelProgramDataProvider extends BaseComponent implements ChannelProgramAndArticleDataProvider {

	@Override
	public List<ChannelProgramBean> getChannelProgramsByParentsid( String parentsid) throws Exception {
		if (!stringSet(parentsid)) 
			parentsid = GlobalConsts.TREE_ROOT;
		List<LeftTreeItem> treeinfos = dao.getBy("parentsid", parentsid, LeftTreeItem.class);
		List<ChannelProgramBean> cpbeans = ListUtils.copyList(
				new String[]{"sid", "name", "parentsid"}, new String[]{"sid", "name", "parentsid"}, treeinfos, ChannelProgramBean.class);
		return cpbeans;
	}

	@Override
	public List<ArticleBean> getArticlesByParentsid(String parentsid) throws Exception {
		List<SingleDataGridItem> singlebeans = dao.getBy("treenode_sid", parentsid, SingleDataGridItem.class);
		List<ArticleBean> articles = ListUtils.copyList(new String[]{"sid", "name", "parentsid"}, new String[]{"sid", "name", "parentsid"}, singlebeans, ArticleBean.class);
		return articles;
	}
	
	@Override
	public ChannelProgramBean getChannelProgramBySid(String sid) throws Exception {
		LeftTreeItem tree = dao.getBy(sid, LeftTreeItem.class);
		ChannelProgramBean cpbean = new ChannelProgramBean();
		cpbean.name = tree.name;
		cpbean.sid = tree.sid;
		cpbean.parentsid = tree.parentsid;
		return cpbean;
	}

}
