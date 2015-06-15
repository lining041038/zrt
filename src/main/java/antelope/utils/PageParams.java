package antelope.utils;

public class PageParams {
	
	public String page;
	public int numPerPage;
	/**
	 * 用于定位对应页的参数，若此参数不为空，并且通过查询可以找到对应数据项，则page参数将失效
	 * 此参数仅适用于DBUtil查询，并且具体的查询sql语句中必须包含有 sid列，否则查询将失败
	 */
	public String locateItemSid; 
	public String sortcol;
	public boolean isup;
	
	public boolean isSort() {
		return TextUtils.stringSet(sortcol);
	}
	
	public PageParams() {
	}
	
	public PageParams(String page) {
		this.page = page;
	}
	
	public PageParams(String page, int numPerPage) {
		this.page = page;
		this.numPerPage = numPerPage;
	}
}
