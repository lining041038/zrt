package antelope.utils;



import java.util.ArrayList;
import java.util.List;

public class PageUtil {
	/**
	 * 获得当前页的信息
	 * @param sPage 需要转到的页号
	 * @param allList 所有数据列表
	 * @param numPerPage 每页显示数量
	 * @return 分页信息Item对象
	 */
	public static PageItem getPage(String sPage, List allList, int numPerPage) {
		// 创建分页信息Item
		PageItem pageItem = new PageItem();
		// 设置每页显示条数
		pageItem.setNumPerPage(numPerPage);
		if (allList == null)
			return pageItem;
		// 当每页显示数设置成0时则证明不对当前页面数据进行分页
		if (numPerPage == 0) {
			pageItem.setCurrList(allList);
			return pageItem;
		}
		// 默认页数为1
		int pageNum = 1;
		try {
			pageNum = Integer.parseInt(sPage);
		} catch (NumberFormatException e) {
		}
		// 小于1时设置成1
		if (pageNum <= 0)
			pageNum = 1;
		// 设置最大页数
		int totalpage = (allList.size() - 1) / numPerPage + 1; 
		if (totalpage == 0) // 若总页数，最少为1.
			totalpage = 1;
		pageItem.setTotalPage(totalpage);
		// 大于最大页数时设置为最大页数
		if (pageNum > pageItem.getTotalPage())
			pageNum = pageItem.getTotalPage();
		pageItem.setCurrPage(pageNum);
		pageItem.setPre(pageNum == 1 ? 1 : pageNum - 1);
		// 设置设置长度
		int len = pageNum * numPerPage;

		if (len >= allList.size()) {
			pageItem.setNext(pageNum);
			len = allList.size();
		} else {
			pageItem.setNext(pageNum + 1);
		}
		// 添加当前页所需要的页面列表数据项
		for (int i = (pageNum - 1) * numPerPage; i < len; i++) {
			pageItem.getCurrList().add(allList.get(i));
		}
		
		pageItem.setCount(allList.size());
		return pageItem;
	}
	
	public static PageItem initPageItem(PageParams pageParam, int count) {
		PageItem pageItem = new PageItem();
		pageItem.setCount(count);
		
		// 当每页显示数设置成0时则证明不对当前页面数据进行分页
		if (pageParam.numPerPage == 0) {
			pageParam.numPerPage = Integer.MAX_VALUE;
		}
		
		// 设置每页显示条数
		pageItem.setNumPerPage(pageParam.numPerPage);
		
		// 默认页数为1
		int pageNum = 1;
		try {
			pageNum = Integer.parseInt(pageParam.page);
		} catch (NumberFormatException e) {
		}
		// 小于1时设置成1
		if (pageNum <= 0)
			pageNum = 1;
		// 设置最大页数
		int totalpage = (count - 1) / pageParam.numPerPage + 1; 
		if (totalpage == 0) // 若总页数，最少为1.
			totalpage = 1;
		pageItem.setTotalPage(totalpage);
		// 大于最大页数时设置为最大页数
		if (pageNum > pageItem.getTotalPage())
			pageNum = pageItem.getTotalPage();
		pageItem.setCurrPage(pageNum);
		pageItem.setPre(pageNum == 1 ? 1 : pageNum - 1);
		// 设置设置长度
		int len = pageNum * pageParam.numPerPage;

		if (len >= count) {
			pageItem.setNext(pageNum);
			len = count;
		} else {
			pageItem.setNext(pageNum + 1);
		}
		
		return pageItem;
	}

	/**
	 * 获取存在某个对象的那一页(存在指的是指向同一个对象)
	 * @param obj 所存在的对象
	 * @param allList 列表
	 * @param numPerPage 每页显示数
	 * @return
	 */
	public static PageItem getPageExistObj(Object obj, List allList, int numPerPage) {
		int index = -1;
		for (int i = 0; i < allList.size(); i++)
			if (allList.get(i) == obj) {
				index = i;
				break;
			}
		if (index == -1)
			return new PageItem();
		int pageNum = (index / numPerPage) + 1;

		return getPage(pageNum + "", allList, numPerPage);
	}

	/**
	 * 获得当前页的信息
	 * @param sPage 需要转到的页号
	 * @param array 数组
	 * @param numPerPage 每页显示数量
	 * @return
	 */
	public static PageItem getPage(String sPage, Object[] array, int numPerPage) {
		List arrList = new ArrayList();
		for (int i = 0; array != null && i < array.length; i++) {
			arrList.add(array[i]);
		}

		return getPage(sPage, arrList, numPerPage);
	}
	
	/**
	 * 获得当前页的信息
	 * @param sPage 需要转到的页号
	 * @param array 数组
	 * @param numPerPage 每页显示数量
	 * @return
	 */
	public static PageItem getPage(PageParams pageParams, List allList) {
		return getPage(pageParams.page, allList, pageParams.numPerPage);
	}

}
