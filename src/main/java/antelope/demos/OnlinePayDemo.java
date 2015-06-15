package antelope.demos;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import antelope.demos.entites.MyOrderItem;
import antelope.onlinepay.common.CommodityBean;
import antelope.onlinepay.common.OnlinePay;

@Component("onlinepaydemo")
public class OnlinePayDemo extends OnlinePay {
	
	@Override
	public CommodityBean getCommodityInfo(String ordersid) throws Exception {
		MyOrderItem by = dao.getBy(ordersid, MyOrderItem.class);
		CommodityBean commoditybean = new CommodityBean();
		commoditybean.totalFee = formatters.typicalNumberFormatter().format(by.orderprice);
		commoditybean.subject = "时光漫步" + ordersid + "号订单";
		return commoditybean;
	}

	@Override
	public boolean paySuccessHandler(String ordersid) {
		try {
			dao.updateBySQL("update DEMO_MY_ORDER set paycomplete=1 where sid=?", ordersid);
		} catch(Exception e) {
			return false;
		}
		return true;
	}

	@Override
	public String getReturnUrl(String ordersid, HttpServletRequest req) {
		return "demos/pay_returncallback.jsp";
	}
}
