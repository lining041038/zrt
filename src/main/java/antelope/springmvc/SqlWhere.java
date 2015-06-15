package antelope.springmvc;

import java.util.ArrayList;
import java.util.List;

/**
 * 使用无参outParams时返回的数据对象
 * @author lining
 * @since 2012-7-5
 */
public class SqlWhere {
	public String wherePart = "";
	public List<Object> outParams = new ArrayList<Object>();
}
