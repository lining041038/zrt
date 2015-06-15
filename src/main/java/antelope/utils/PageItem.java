package antelope.utils;


import java.util.ArrayList;
import java.util.List;

public  class  PageItem<A> {
	/** 当前页面总页数 */
	private int totalPage = 1;
	/** 当前页号 */
	private int currPage = 1;
	/** 上一页页号 */
	private int pre = 1;
	/** 下一页页号 */
	private int next = 1;
	/** 页面数据列表 */
	private List<A> currList = new ArrayList<A>();
	/** 总记录数*/
	private int count;
	/** 每页显示条数*/
	private int numPerPage;
	
	/**
	 * 获取每页显示条数
	 * @return
	 */
	public int getNumPerPage() {
		return numPerPage;
	}

	/**
	 * 得到每页显示条数
	 * @param numPerPage
	 */
	public void setNumPerPage(int numPerPage) {
		this.numPerPage = numPerPage;
	}

	/**
	 * 获取总记录数
	 * @return 总记录数
	 */
	public int getCount() {
		return count;
	}

	/**
	 * 设置总记录数
	 * @param count 总记录数
	 */
	public void setCount(int count) {
		this.count = count;
	}

	/**
	 * 获取数组形式的当前页数据
	 * @return
	 */
	public A[] getCurrArray() {
		Object[] objs = new Object[currList.size()];
		for (int i = 0; i < objs.length; i++) {
			objs[i] = currList.get(i);
		}
		return (A[]) objs;
	}

	/**
	 * 若含有上一页则返回true
	 * @return 若含有上一页则返回true
	 */
	public boolean hasPre() {
		return currPage != 1;
	}

	/**
	 * 若含有下一页则返回true
	 * @return 若含有下一页则返回true
	 */
	public boolean hasNext() {
		return currPage != totalPage;
	}

	/**
	 * 获取总页数
	 * @return 总页数
	 */
	public int getTotalPage() {
		return totalPage;
	}

	/**
	 * 设置总页数
	 * @param totalPage 总页数
	 */
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	/**
	 * 获取当前页号
	 * @return 当前页号
	 */
	public int getCurrPage() {
		return currPage;
	}

	/**
	 * 设置当前页号
	 * @param currPage 当前页号
	 */
	public void setCurrPage(int currPage) {
		this.currPage = currPage;
	}

	/**
	 * 获取上一页号，若本页为1，则此方法仍然返回1
	 * @return 上一页号
	 */
	public int getPre() {
		return pre;
	}

	/**
	 * 设置上一页号
	 * @param pre 上一页号
	 */
	public void setPre(int pre) {
		this.pre = pre;
	}

	/**
	 * 获取下一页号，若本页为总页数，则返回总页数
	 * @return 下一页号
	 */
	public int getNext() {
		return next;
	}

	/**
	 * 设置下一页号
	 * @param next 下一页号
	 */
	public void setNext(int next) {
		this.next = next;
	}

	/**
	 * 获取当前页数据列表
	 * @return 当前页数据列表
	 */
	public List<A> getCurrList() {
		return currList;
	}

	/**
	 * 设置当前页数据列表
	 * @param currList 当前页数据列表
	 */
	public void setCurrList(List<A> currList) {
		this.currList = currList;
	}
	
	/**
	 * 将翻页信息输出到JSONObject当中
	 * prev
	 * next
	 * total
	 * count
	 * currpage
	 * numperpage
	 * @param obj
	 * @throws JSONException 
	 */
	public void outputPageInfoTo(JSONObject obj) throws JSONException {
		if (obj != null) {
			obj.put("prev", getPre());
			obj.put("next", getNext());
			obj.put("total", getTotalPage());
			obj.put("count", getCount());
			obj.put("currpage", getCurrPage());
			obj.put("numperpage", getNumPerPage());
			obj.put("data", toJSONArray(currList));
		}
	}
	
	private JSONArray toJSONArray(List<A> list) {
		JSONArray jsonArr = new JSONArray ();
		for (int i = 0; i < list.size(); ++i)
			jsonArr.put(list.get(i));
		return jsonArr;
	}
}









