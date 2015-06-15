package antelope.springmvc;

import javax.servlet.http.HttpServletRequest;

public class SimpleRequest {

	private HttpServletRequest req;
	
	SimpleRequest(HttpServletRequest req) {
		this.req = req;
	}
	
	/**
	 * 获取解码过并trim过的字符串
	 * @param key 参数名
	 * @return 解码过并trim过的字符串
	 */
	public String getDecodedStr(String key) {
		if (req.getParameter(key) == null)
			return null;
		return BaseController.d(req.getParameter(key));
	}
	
	
	/**
	 * getDecodedStr的别名，实现相同的功能，调用时代码量更少
	 */
	public String d(String key) {
		return getDecodedStr(key);
	}
}
