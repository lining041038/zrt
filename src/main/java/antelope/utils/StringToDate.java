package antelope.utils;


import java.sql.Date;
import java.util.Calendar;

public class StringToDate {
	public static Date getDate(String str){
		String date ="";
		if(str == null || str ==""){
			Calendar c = Calendar.getInstance();
			int y = c.get(Calendar.YEAR);
			int m = c.get(Calendar.MONTH) + 1;
			int d = c.get(Calendar.DAY_OF_MONTH);
			if(m<10){
				if(d<10)
				date = y + "-0" + m + "-0" + d;
				else
				date = y + "-0" + m + "-" + d;
			}else{
				if(d<10)
				date = y + "-" + m + "-0" + d;
				else
				date = y + "-" + m + "-" + d;
			}
			Date day = Date.valueOf(date);
			return day;
		}
	Date day = Date.valueOf(str);
	return day;
	}
}
