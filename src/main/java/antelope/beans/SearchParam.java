package antelope.beans;

import java.util.HashSet;
import java.util.Set;

/**
 * 前台传入的搜索相关参数
 * @author lining
 */
public class SearchParam {
	
	public String keyword;
	public Set<String> filterKeys = new HashSet<String>();
	
}
