package antelope.quartz;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.TransactionStatus;

import antelope.db.DBUtil;
import antelope.springmvc.SpringUtils;


@Repository("quartzDao")
@SuppressWarnings("unchecked")
public class QuartzDao {

	public List<Map<String, Object>> getQrtzTriggers(String group) {
		TransactionStatus status = SpringUtils.beginTransaction();
		String sqlString="SELECT * FROM QRTZ_TRIGGERS a left join QRTZ_CRON_TRIGGERS b on a.TRIGGER_NAME=b.TRIGGER_NAME and a.trigger_group=b.TRIGGER_GROUP" +
				" where a.trigger_group='"+group+"' and b.TRIGGER_GROUP='"+group+"' ORDER BY START_TIME";
		List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();
		try {
			results = DBUtil.query(sqlString);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		long val = 0;
		String temp = null;
		for (Map<String, Object> map : results) {
			temp = MapUtils.getString(map, "trigger_name");
			
			String description = null;
			try {
				description = DBUtil.querySingleString("select description from QRTZ_JOB_DETAILS where job_name=?", new Object[]{temp});
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			if(StringUtils.indexOf(temp, "&") != -1){
				map.put("display_name", StringUtils.substringBefore(temp, "&"));
			}else{
				map.put("display_name", temp);
			}
			
			map.put("description", description);
			
			val = MapUtils.getLongValue(map, "next_fire_time");
			if (val > 0) {
				map.put("next_fire_time", DateFormatUtils.format(val, "yyyy-MM-dd HH:mm:ss"));
			}

			val = MapUtils.getLongValue(map, "prev_fire_time");
			if (val > 0) {
				map.put("prev_fire_time", DateFormatUtils.format(val, "yyyy-MM-dd HH:mm:ss"));
			}

			val = MapUtils.getLongValue(map, "start_time");
			if (val > 0) {
				map.put("start_time", DateFormatUtils.format(val, "yyyy-MM-dd HH:mm:ss"));
			}
			
			val = MapUtils.getLongValue(map, "end_time");
			if (val > 0) {
				map.put("end_time", DateFormatUtils.format(val, "yyyy-MM-dd HH:mm:ss"));
			}
			 
			map.put("CRON_EXPRESSION", MapUtils.getString(map, "CRON_EXPRESSION"));
			 
			
			map.put("statu",Constant.status.get(MapUtils.getString(map, "trigger_state")));
			
			
			
		}
		SpringUtils.commitTransaction(status);
		return results;
	}

}
