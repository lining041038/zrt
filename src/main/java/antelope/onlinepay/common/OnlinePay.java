package antelope.onlinepay.common;

import javax.servlet.http.HttpServletRequest;

import antelope.springmvc.BaseComponent;

public abstract class OnlinePay extends BaseComponent {
	
	/**
	 * 根据订单号获取商品的一般信息
	 * 参数 ordersid 为订单号
	 */
	public abstract CommodityBean getCommodityInfo(String ordersid) throws Exception;
		
	/**
	 * 订单支付成功后处理函数，完成对应订单支付成功后的一些工作，如将订单打上已支付的标识等
	 * @param ordersid
	 * @return
	 */
	public abstract boolean paySuccessHandler(String ordersid);
	
	/**
	 * 返回到主页面的jsp页面地址。如 demos/xxx.jsp   注意不要添加ctx上下文路径
	 * 此页面地址可以通过request.getAttributer("ordersid")获取到当前所完成的订单相关的订单sid
	 * 注意只有成功验证本次为支付宝发出的订单完成的通知才会转到此页面，否则将转到验证失败的错误页面
	 */
	public abstract String getReturnUrl(String ordersid, HttpServletRequest req);
}
