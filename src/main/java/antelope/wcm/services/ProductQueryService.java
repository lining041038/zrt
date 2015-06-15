package antelope.wcm.services;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import antelope.db.DBUtil;
import antelope.springmvc.BaseComponent;
import antelope.springmvc.SqlWhere;
import antelope.utils.PageItem;
import antelope.utils.PageParams;

@Service
public class ProductQueryService extends BaseComponent {

	public PageItem getProductsPageDataByCatesid(String catesid, PageParams pageParams, SqlWhere sqlwhere) throws SQLException, Exception {
		List<String> allcatesids = new ArrayList<String>();
		List<String> parentcatesids = new ArrayList<String>();
		parentcatesids.add(catesid);
		allcatesids.add(catesid);
		
		while (!parentcatesids.isEmpty()) {
			parentcatesids = DBUtil.queryStrings("select sid from WCM_PRODUCT_CATE where parentsid in ('" + join("','", parentcatesids) + "')");
			allcatesids.addAll(parentcatesids);
		}
		
		PageItem json = DBUtil.queryJSON("select t.*, t2.name catename from WCM_PRODUCT t left join WCM_PRODUCT_CATE t2 on t.catesid=t2.sid  where t.catesid in ('" + join("','", allcatesids) + "') order by t.createtime desc" + sqlwhere.wherePart, sqlwhere.outParams, pageParams);
		
		return json;
	}
}
