package antelope.wcm.services;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Service;

import antelope.db.DBUtil;
import antelope.springmvc.BaseComponent;
import antelope.springmvc.SqlWhere;
import antelope.utils.PageItem;
import antelope.utils.PageParams;

@Service
public class SearchQueryService extends BaseComponent{
	public PageItem getSearchPageDataByKey(String key,String t,String mintime,String maxtime, PageParams pageParams, SqlWhere sqlwhere) throws SQLException, Exception {
		String sql = "select * from (select t.sid, t.name, t.digest, t.createtime, 'article.jsp' as pageinfo, 'article' as pagetype from WCM_ARTICLE t  where (t.name like '%"+key+"%' or t.digest like '%"+key+"%') "+ getDateTime(t,mintime,maxtime,"t")
				+"union select p.sid, p.name, p.name as digest, p.createtime, 'product.jsp' as pageinfo,'product' as pagetype from WCM_PRODUCT p where (p.name like '%"+key+"%') "+ getDateTime(t,mintime,maxtime,"p")
				+") t33 order by t33.createtime desc";
		//System.out.println(sql);
		PageItem json = DBUtil.queryJSON(sql + sqlwhere.wherePart, sqlwhere.outParams, pageParams);
		return json;
	}
	
	public String getDateTime(String t,String mintime,String maxtime,String alias){
		if(t==null||t.equals("")){
			return "";
		}
		String sql = "";
		
		if("timecs".equals(t)){
			//用户自定义时间
			sql = " and "+alias+".createtime >='"+mintime+"' and "+alias+".createtime <= '"+maxtime+"' ";
		}else{
			//时间处理
			Calendar cal = Calendar.getInstance();
			Date end = cal.getTime();
			if("timeyt".equals(t)){
				cal.add(Calendar.DATE, -1);
			}else if("timeyz".equals(t)){
				cal.add(Calendar.WEEK_OF_YEAR, -1);
			}else if("timeyy".equals(t)){
				cal.add(Calendar.MONTH, -1);
			}else if("timeyn".equals(t)){
				cal.add(Calendar.YEAR,-1);
			}else{
				return "";
			}
			Date start = cal.getTime();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			sql = " and "+alias+".createtime >='"+sdf.format(start)+"' and "+alias+".createtime <= '"+sdf.format(end)+"' ";
		}
		
		return sql;
	}
	
}
