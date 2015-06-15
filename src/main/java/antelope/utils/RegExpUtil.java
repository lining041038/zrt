package antelope.utils;



import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegExpUtil {
	public static String getFirstMatched (Pattern pattern, String str) {
		if (str == null) {
			return null;
		}
		Matcher matcher = pattern.matcher(str);
		if (!matcher.find()) return null;
		else return matcher.group();
	}
	
	public static String getFirstMatched (String patternstr, String str) {
		if (str == null) {
			return null;
		}
		Matcher matcher = Pattern.compile(patternstr).matcher(str);
		if (!matcher.find()) return null;
		else return matcher.group();
	}
	
	public static List<String> getAllMatched(String patternstr, String str) {
		if (str == null) {
			return null;
		}
		Matcher matcher = Pattern.compile(patternstr).matcher(str);
		List<String> list = new ArrayList<String>();
		while(matcher.find()) {
			list.add(matcher.group());
		}
		return list;
	}
	
	public static String getNextMatched (Matcher matcher) {
		if (matcher.find())
			return matcher.group();
		else 
			return null;
	}
}
