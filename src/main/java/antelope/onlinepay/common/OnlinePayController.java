package antelope.onlinepay.common;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import antelope.db.DBUtil;
import antelope.demos.entites.MyOrderItem;
import antelope.onlinepay.alipay.AlipayConfig;
import antelope.onlinepay.alipay.AlipayNotify;
import antelope.onlinepay.alipay.AlipaySubmit;
import antelope.springmvc.BaseController;
import antelope.springmvc.SpringUtils;
import antelope.utils.SpeedIDUtil;


@Controller
public class OnlinePayController extends BaseController {

	@RequestMapping("/gotoOnlinepaypage/{ordersid}/{componentname}/{payOnlineBank}/1")
	public void gotoOnlinepaypage(@PathVariable("ordersid") String ordersid, @PathVariable("payOnlineBank") String payOnlineBank,
			@PathVariable("componentname") String componentname, HttpServletRequest req, HttpServletResponse res) throws SQLException, Exception {
		
		OnlinePay bean = SpringUtils.getBean(OnlinePay.class, componentname);
		CommodityBean commodityInfo = bean.getCommodityInfo(ordersid);
		
		////////////////////////////////////请求参数//////////////////////////////////////

		//支付类型
		String payment_type = "1";
		//必填，不能修改
		//服务器异步通知页面路径
		String notify_url = "http://" + req.getLocalAddr() +  ":" + req.getLocalPort() + req.getContextPath() + "/notifypaycallback/" + ordersid + "/" + componentname + "/1.vot";
		//需http://格式的完整路径，不能加?id=123这类自定义参数

		//页面跳转同步通知页面路径
		String return_url = "http://" + req.getLocalAddr() +  ":" + req.getLocalPort() + req.getContextPath() + "/returncallback/" + ordersid + "/" + componentname + "/1.vot";
		//需http://格式的完整路径，不能加?id=123这类自定义参数，不能写成http://localhost/

		//卖家支付宝帐户
		String seller_email = AlipayConfig.seller_email;
		//必填

		//商户订单号
		String out_trade_no = ordersid;
		//商户网站订单系统中唯一订单号，必填

		//订单名称
		String subject = commodityInfo.subject;
		//必填

		//付款金额
		String total_fee = commodityInfo.totalFee;
		//必填

		//订单描述

		//默认支付方式
		String paymethod = "bankPay";
		//必填
		//默认网银
		String defaultbank = payOnlineBank;
		//必填，银行简码请参考接口技术文档

		//需以http://开头的完整路径，例如：http://www.xxx.com/myorder.html

		//客户端的IP地址
		String exter_invoke_ip = req.getRemoteAddr();
		//非局域网的外网IP地址，如：221.0.0.1
		
		//把请求参数打包成数组
		Map<String, String> sParaTemp = new HashMap<String, String>();
		sParaTemp.put("service", "create_direct_pay_by_user");
        sParaTemp.put("partner", AlipayConfig.partner);
        sParaTemp.put("_input_charset", AlipayConfig.input_charset);
		sParaTemp.put("payment_type", payment_type);
		sParaTemp.put("notify_url", notify_url);
		sParaTemp.put("return_url", return_url);
		sParaTemp.put("seller_email", seller_email);
		sParaTemp.put("out_trade_no", out_trade_no);
		sParaTemp.put("subject", subject);
		sParaTemp.put("total_fee", total_fee);
		sParaTemp.put("paymethod", paymethod);
		sParaTemp.put("defaultbank", defaultbank);
		sParaTemp.put("exter_invoke_ip", exter_invoke_ip);
		sParaTemp.put("it_b_pay", "4320m"); // 72小时过期
		
		//建立请求
		String sHtmlText = AlipaySubmit.buildRequest(sParaTemp,"post","确认");
		print(sHtmlText, res);
	}
	
	@RequestMapping("/notifypaycallback/{ordersid}/{componentname}/1")
	public void notifypaycallback(@PathVariable("ordersid") String ordersid, @PathVariable("componentname") String componentname, HttpServletRequest req, HttpServletResponse res) throws SQLException, Exception {
		
		//获取支付宝POST过来反馈信息
		Map<String,String> params = new HashMap<String,String>();
		Map requestParams = req.getParameterMap();
		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i]
						: valueStr + values[i] + ",";
			}
			//乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
			//valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
			params.put(name, valueStr);
		}
		
		//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
		//商户订单号

		String out_trade_no = new String(req.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");

		//支付宝交易号
		String trade_no = new String(req.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");

		//交易状态
		String trade_status = new String(req.getParameter("trade_status").getBytes("ISO-8859-1"),"UTF-8");

		//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//
		if(AlipayNotify.verify(params)){//验证成功
			//////////////////////////////////////////////////////////////////////////////////////////
			//请在这里加上商户的业务逻辑程序代码

			//——请根据您的业务逻辑来编写程序（以下代码仅作参考）——
			
			if(trade_status.equals("TRADE_FINISHED")){
				//判断该笔订单是否在商户网站中已经做过处理
					//如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
					//如果有做过处理，不执行商户的业务程序
				
				//注意：
				//该种交易状态只在两种情况下出现
				//1、开通了普通即时到账，买家付款成功后。
				//2、开通了高级即时到账，从该笔交易成功时间算起，过了签约时的可退款时限（如：三个月以内可退款、一年以内可退款等）后。
				
				if (doUpdateOrder(out_trade_no, componentname)) {
					print("success", res);
				} else {
					print("fail", res);	//订单更新失败
				}
				return;
				
			} else if (trade_status.equals("TRADE_SUCCESS")){
				//判断该笔订单是否在商户网站中已经做过处理
					//如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
					//如果有做过处理，不执行商户的业务程序
					
				//注意：
				//该种交易状态只在一种情况下出现——开通了高级即时到账，买家付款成功后。
				
				if (doUpdateOrder(out_trade_no, componentname)) {
					print("success", res);
				} else {
					print("fail", res);	//订单更新失败
				}
				return;
			}

			//——请根据您的业务逻辑来编写程序（以上代码仅作参考）——
			 print("success", res);
			//////////////////////////////////////////////////////////////////////////////////////////
		}else{//验证失败
			print("fail", res);
		}
	}
	
	/**
	 * 确保多次处理订单时可能存在的错误处理，因此加同步
	 * @param out_trade_no
	 * @throws SQLException
	 * @throws Exception
	 */
	private synchronized boolean doUpdateOrder (String out_trade_no, String componentname) throws SQLException, Exception {
		int queryCount = DBUtil.queryCount("select count(*) from SYS_ORDER_ONLINEPAY_LOG where ordersid=? and orderstatus='1'", out_trade_no);
		if (queryCount == 0) {
			// 更新订单
			OnlinePay bean = SpringUtils.getBean(OnlinePay.class, componentname);
			boolean paySuccessHandler = bean.paySuccessHandler(out_trade_no);
			if (paySuccessHandler) {// 订单更新成功，记录日志
				OrderOnlinePayLogItem paylog = new OrderOnlinePayLogItem();
				paylog.sid = SpeedIDUtil.getId();
				paylog.ordersid = out_trade_no;
				paylog.orderstatus = "1";
				dao.insertOrUpdate(paylog);
				dao.flush();
				return true;
			} else {// 订单更新失败
				return false;
			}
		} else {// 已经处理过订单，不在进行处理,直接返回成功
			return true;	
		}
	}
	
	@RequestMapping("/returncallback/{ordersid}/{componentname}/1")
	public String returncallback(@PathVariable("ordersid") String ordersid, @PathVariable("componentname") String componentname, HttpServletRequest req, HttpServletResponse res) throws SQLException, Exception {
		
		//获取支付宝GET过来反馈信息
		Map<String,String> params = new HashMap<String,String>();
		Map requestParams = req.getParameterMap();
		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i]
						: valueStr + values[i] + ",";
			}
			//乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
			valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
			params.put(name, valueStr);
		}
		
		//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
		//商户订单号

		String out_trade_no = new String(req.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");

		OnlinePay bean = SpringUtils.getBean(OnlinePay.class, componentname);
		//支付宝交易号

		String trade_no = new String(req.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");

		//交易状态
		String trade_status = new String(req.getParameter("trade_status").getBytes("ISO-8859-1"),"UTF-8");

		//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//
		
		//计算得出通知验证结果
		boolean verify_result = AlipayNotify.verify(params);
		
		if(verify_result){//验证成功
			//////////////////////////////////////////////////////////////////////////////////////////
			//请在这里加上商户的业务逻辑程序代码

			//——请根据您的业务逻辑来编写程序（以下代码仅作参考）——
			req.setAttribute("ordersid", out_trade_no);
			if(trade_status.equals("TRADE_FINISHED") || trade_status.equals("TRADE_SUCCESS")){
				//判断该笔订单是否在商户网站中已经做过处理
					//如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
					//如果有做过处理，不执行商户的业务程序
				if (doUpdateOrder(out_trade_no, componentname)) {
					return bean.getReturnUrl(out_trade_no, req).replaceFirst("\\.jsp$", "");
				} else {
					return "components/onlinepay_error";
				}
				//该页面可做页面美工编辑
			}
			
			//——请根据您的业务逻辑来编写程序（以上代码仅作参考）——
			return bean.getReturnUrl(out_trade_no, req).replaceFirst("\\.jsp$", "");
			//////////////////////////////////////////////////////////////////////////////////////////
		}else{
			//该页面可做页面美工编辑
			return "components/onlinepay_error";
		//	println("验证失败");
		}
	}
}



