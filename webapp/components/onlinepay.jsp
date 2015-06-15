<%--
 系统在线支付演示
 @author lining
 @since 2014-05-04
--%>
<%@page import="antelope.onlinepay.common.CommodityBean"%>
<%@page import="antelope.onlinepay.common.OnlinePay"%>
<%@page import="antelope.utils.RegExpUtil"%>
<%@page import="antelope.springmvc.FormatterFactory"%>
<%@page import="antelope.springmvc.BaseController"%>
<%@page import="antelope.demos.entites.MyOrderItem"%>
<%@page import="antelope.springmvc.JPABaseDao"%>
<%@page import="org.springframework.transaction.TransactionStatus"%>
<%@page import="antelope.springmvc.SpringUtils"%>
<%@ page language="java" pageEncoding="utf-8"%>
<%



TransactionStatus status = SpringUtils.beginTransaction();
JPABaseDao dao = SpringUtils.getBean(JPABaseDao.class, "jpabasedao");

OnlinePay onlinepay = SpringUtils.getBean(OnlinePay.class, request.getParameter("component"));

CommodityBean orderitem = onlinepay.getCommodityInfo(request.getParameter("ordersid"));
FormatterFactory formaterfactory = new FormatterFactory();
SpringUtils.commitTransaction(status);
%>
<link rel="stylesheet" href="<%=request.getContextPath() %>/components/onlinepay.css" />
<script src="${pageContext.request.contextPath}/js/jquery-pay.min.js"></script>
<script>
$(function(){
	//$.get(ctx + "/demos/index/DemoIndexController/getDemoData.vot", function(data){
	//	alert(data);
	//});
	$.post(ctx + "/gotoOnlinepaypage/<%=request.getParameter("ordersid") %>/<%=request.getParameter("component")%>/1.vot", {"online":"online222"}, function(){
		alert("fdsfsddfs");
	});
});


var actionmainurl = ctx + "/gotoOnlinepaypage/<%=request.getParameter("ordersid") %>/<%=request.getParameter("component")%>";

</script>

<div class="container payment-con">
    <form  target="_blank" action="<%=request.getContextPath() %>/gotoOnlinepaypage/<%=request.getParameter("ordersid") %>/<%=request.getParameter("component")%>/1.vot" id="pay-form" method="post">
    		<!-- 
            <div class="order-info">
                <div class="msg">
					<h3>您的订单已提交成功！付款咯～</h3>
					<p>
						请在<span class="pay-time-tip">1小时24分</span>内完成支付, 超时后将取消订单  -->
						<!--请在<span class="pay-time-tip">??小时内</span>完成支付，超时后将取消订单-->
				<!-- 	</p>
				</div>
                <div class="info">
                    <p>
                        金额：<span class="pay-total">1699.00元</span>
                    </p>
                    <p>
                        订单：1140504810002933                    </p>
                    <p>
                        配送：李宁                                    <span class="line">/</span>
                                    15810719799                                    <span class="line">/</span>
                                    北京,北京市,海淀区 苏州街18号院长远天地大厦A2座308室                                                                <span class="line">/</span>
                                                                    不限送货时间                                    <span class="line">/</span>
                                    个人                                                    </p>
                </div>
                <div class="icon-box">
                    <i class="iconfont">&#xe613;</i>
                </div>
            </div>
            -->
			<div class="order-info">
                <div class="msg">
					<h3>在线付款</h3>
				</div>
                <div class="info">
                    <p>
                        	金额：<span class="pay-total"><%=orderitem.totalFee  %>元</span>
                    </p>
                    <p>
                        	订单：<%=request.getParameter("ordersid") %>                    </p>
                </div>
                <div class="icon-box">
                    <i class="iconfont">&#xe613;</i>
                </div>
            </div>
            <div class="xm-plain-box">
                <!-- 选择支付方式 -->
                <div class="box-hd bank-title">
                    <div id="titleWrap" class="title-wrap">
                        <h2 class="title">选择支付方式</h2>
                        <h2 class="title hide " >你还需要继续支付 <em> <%=orderitem.totalFee  %></em> 元</h2>
                    </div>
                </div>
                <div class="box-bd" id="bankList">
                    <div class="payment-bd">
                                                <dl class="clearfix payment-box" >
                            <dt>
                                <strong>支付平台</strong>
                                <p>手机等大额支付推荐使用支付宝快捷支付</p>
                            </dt>
                            <dd>
                                <ul class="payment-list clearfix">
                                    <li style="display: none;"><label for="cft"><input type="radio" name="payOnlineBank" id="cft" value="cft" /> <img src="<%=request.getContextPath() %>/antelope/themes/base/assets/cft.gif" alt=""/></label>
                                    </li><li><label for="alipay"><input type="radio" name="payOnlineBank" id="alipay" value="alipay" /> <img src="<%=request.getContextPath() %>/antelope/themes/base/assets/payOnline_zfb.gif" alt=""/></label></li>                                </ul>
                            </dd>
                        </dl>
                                                <dl class="clearfix payment-box"  style="display: none;">
                            <dt>
                                <strong>信用卡支付</strong>
                                <p><span>无需网银快捷支付</span></p>
                            </dt>
                            <dd>
                                <ul class="payment-list clearfix">
                                    <li><label  for="CMB-KQ"><input type="radio" name="payOnlineBank" id="CMB-KQ" value="CMB-KQ" /> <img src="<%=request.getContextPath() %>/antelope/themes/base/assets/payOnline_zsyh.gif" alt=""/></label></li><li><label  for="SPABANK-KQ"><input type="radio" name="payOnlineBank" id="SPABANK-KQ" value="SPABANK-KQ" /> <img src="<%=request.getContextPath() %>/antelope/themes/base/assets/payOnline_payh.gif" alt=""/></label></li><li><label  for="CCB-KQ"><input type="radio" name="payOnlineBank" id="CCB-KQ" value="CCB-KQ" /> <img src="<%=request.getContextPath() %>/antelope/themes/base/assets/payOnline_jsyh.gif" alt=""/></label></li><li><label  for="ICBCB2C-KQ"><input type="radio" name="payOnlineBank" id="ICBCB2C-KQ" value="ICBCB2C-KQ" /> <img src="<%=request.getContextPath() %>/antelope/themes/base/assets/payOnline_gsyh.gif" alt=""/></label></li><li><label  for="CITIC-KQ"><input type="radio" name="payOnlineBank" id="CITIC-KQ" value="CITIC-KQ" /> <img src="<%=request.getContextPath() %>/antelope/themes/base/assets/payOnline_zxyh.gif" alt=""/></label></li><li><label  for="CEBBANK-KQ"><input type="radio" name="payOnlineBank" id="CEBBANK-KQ" value="CEBBANK-KQ" /> <img src="<%=request.getContextPath() %>/antelope/themes/base/assets/payOnline_gdyh.gif" alt=""/></label></li><li><label  for="BOCB2C-KQ"><input type="radio" name="payOnlineBank" id="BOCB2C-KQ" value="BOCB2C-KQ" /> <img src="<%=request.getContextPath() %>/antelope/themes/base/assets/payOnline_zgyh.jpg" alt=""/></label></li><li><label  for="SRCB-KQ"><input type="radio" name="payOnlineBank" id="SRCB-KQ" value="SRCB-KQ" /> <img src="<%=request.getContextPath() %>/antelope/themes/base/assets/payOnline_shncsyyh.jpg" alt=""/></label></li><li><label  for="JSB-KQ"><input type="radio" name="payOnlineBank" id="JSB-KQ" value="JSB-KQ" /> <img src="<%=request.getContextPath() %>/antelope/themes/base/assets/payOnline_jiangsshuyh.gif" alt=""/></label></li><li><label  for="CMBC-KQ"><input type="radio" name="payOnlineBank" id="CMBC-KQ" value="CMBC-KQ" /> <img src="<%=request.getContextPath() %>/antelope/themes/base/assets/payOnline_msyh.gif" alt=""/></label></li><li><label  for="CIB-KQ"><input type="radio" name="payOnlineBank" id="CIB-KQ" value="CIB-KQ" /> <img src="<%=request.getContextPath() %>/antelope/themes/base/assets/payOnline_xyyh.gif" alt=""/></label></li><li><label  for="ABC-KQ"><input type="radio" name="payOnlineBank" id="ABC-KQ" value="ABC-KQ" /> <img src="<%=request.getContextPath() %>/antelope/themes/base/assets/payOnline_nyyh.gif" alt=""/></label></li><li><label  for="SDB-KQ"><input type="radio" name="payOnlineBank" id="SDB-KQ" value="SDB-KQ" /> <img src="<%=request.getContextPath() %>/antelope/themes/base/assets/payOnline_sfyh.gif" alt=""/></label></li><li><label  for="HXB-KQ"><input type="radio" name="payOnlineBank" id="HXB-KQ" value="HXB-KQ" /> <img src="<%=request.getContextPath() %>/antelope/themes/base/assets/payOnline_hyyh.png" alt=""/></label></li><li><label  for="GDB-KQ"><input type="radio" name="payOnlineBank" id="GDB-KQ" value="GDB-KQ" /> <img src="<%=request.getContextPath() %>/antelope/themes/base/assets/payOnline_gfyh.gif" alt=""/></label></li>                                </ul>
                            </dd>
                        </dl>
                             <dl class="clearfix payment-box" >
                            <dt>
                                <strong>银行网银</strong>
                                <p>支持以下各银r行借记卡及信用卡</p>
                            </dt>
                            <dd>
                                <ul class="payment-list clearfix">
                                    <li><label  for="CMB"><input type="radio" name="payOnlineBank" id="CMB" value="CMB" /> <img src="<%=request.getContextPath() %>/antelope/themes/base/assets/payOnline_zsyh.gif" alt=""/></label></li><li><label  for="ICBCB2C"><input type="radio" name="payOnlineBank" id="ICBCB2C" value="ICBCB2C" /> <img src="<%=request.getContextPath() %>/antelope/themes/base/assets/payOnline_gsyh.gif" alt=""/></label></li><li><label  for="CCB"><input type="radio" name="payOnlineBank" id="CCB" value="CCB" /> <img src="<%=request.getContextPath() %>/antelope/themes/base/assets/payOnline_jsyh.gif" alt=""/></label></li><li><label  for="ABC"><input type="radio" name="payOnlineBank" id="ABC" value="ABC" /> <img src="<%=request.getContextPath() %>/antelope/themes/base/assets/payOnline_nyyh.gif" alt=""/></label></li><li><label  for="BOC-DEBIT"><input type="radio" name="payOnlineBank" id="BOC-DEBIT" value="BOC-DEBIT" /> <img src="<%=request.getContextPath() %>/antelope/themes/base/assets/payOnline_zgyh.gif" alt=""/></label></li><li><label  for="COMM"><input type="radio" name="payOnlineBank" id="COMM" value="COMM" /> <img src="<%=request.getContextPath() %>/antelope/themes/base/assets/payOnline_jtyh.gif" alt=""/></label></li><li><label  for="PSBC-DEBIT"><input type="radio" name="payOnlineBank" id="PSBC-DEBIT" value="PSBC-DEBIT" /> <img src="<%=request.getContextPath() %>/antelope/themes/base/assets/payOnline_youzheng.gif" alt=""/></label></li><li><label  for="GDB"><input type="radio" name="payOnlineBank" id="GDB" value="GDB" /> <img src="<%=request.getContextPath() %>/antelope/themes/base/assets/payOnline_gfyh.gif" alt=""/></label></li><li><label  for="SPDB"><input type="radio" name="payOnlineBank" id="SPDB" value="SPDB" /> <img src="<%=request.getContextPath() %>/antelope/themes/base/assets/payOnline_pufa.gif" alt=""/></label></li><li><label  for="CEBBANK"><input type="radio" name="payOnlineBank" id="CEBBANK" value="CEBBANK" /> <img src="<%=request.getContextPath() %>/antelope/themes/base/assets/payOnline_gdyh.gif" alt=""/></label></li><li><label  for="SPABANK"><input type="radio" name="payOnlineBank" id="SPABANK" value="SPABANK" /> <img src="<%=request.getContextPath() %>/antelope/themes/base/assets/payOnline_payh.gif" alt=""/></label></li><li><label  for="CIB"><input type="radio" name="payOnlineBank" id="CIB" value="CIB" /> <img src="<%=request.getContextPath() %>/antelope/themes/base/assets/payOnline_xyyh.gif" alt=""/></label></li><li><label  for="CMBC"><input type="radio" name="payOnlineBank" id="CMBC" value="CMBC" /> <img src="<%=request.getContextPath() %>/antelope/themes/base/assets/payOnline_msyh.gif" alt=""/></label></li><li><label  for="CITIC"><input type="radio" name="payOnlineBank" id="CITIC" value="CITIC" /> <img src="<%=request.getContextPath() %>/antelope/themes/base/assets/payOnline_zxyh.gif" alt=""/></label></li><li><label  for="SDB"><input type="radio" name="payOnlineBank" id="SDB" value="SDB" /> <img src="<%=request.getContextPath() %>/antelope/themes/base/assets/payOnline_sfyh.gif" alt=""/></label></li><li><label  for="SHBANK"><input type="radio" name="payOnlineBank" id="SHBANK" value="SHBANK" /> <img src="<%=request.getContextPath() %>/antelope/themes/base/assets/payOnline_shyh.gif" alt=""/></label></li><li><label  for="BJRCB"><input type="radio" name="payOnlineBank" id="BJRCB" value="BJRCB" /> <img src="<%=request.getContextPath() %>/antelope/themes/base/assets/payOnline_bjnsyh.gif" alt=""/></label></li><li><label  for="NBBANK"><input type="radio" name="payOnlineBank" id="NBBANK" value="NBBANK" /> <img src="<%=request.getContextPath() %>/antelope/themes/base/assets/payOnline_nbyh.gif" alt=""/></label></li><li><label  for="HZCBB2C"><input type="radio" name="payOnlineBank" id="HZCBB2C" value="HZCBB2C" /> <img src="<%=request.getContextPath() %>/antelope/themes/base/assets/payOnline_hzyh.gif" alt=""/></label></li><li><label  for="SHRCB"><input type="radio" name="payOnlineBank" id="SHRCB" value="SHRCB" /> <img src="<%=request.getContextPath() %>/antelope/themes/base/assets/payOnline_shnsyh.gif" alt=""/></label></li><li><label  for="FDB"><input type="radio" name="payOnlineBank" id="FDB" value="FDB" /> <img src="<%=request.getContextPath() %>/antelope/themes/base/assets/payOnline_fcyh.gif" alt=""/></label></li>                                </ul>
                            </dd>
                        </dl>
                    </div>
                            </div>
            	<div class="box-ft clearfix">
                    <input type="submit" class="btn btn-primary" value="下一步" id="payBtn">
                    <span class="tip">
                       	 请在<span class="pay-time-tip">1小时24分</span>内完成支付, 超时后将取消订单                    </span>
                </div>
            </div>
</form>  
</div>


<!-- 支付弹框 -->
<div class="modal hide to-pay-tip" id="toPayTip">
    <div class="modal-header">
        <span class="close" id="toPayTipClose">
            <i class="iconfont">&#xe617;</i>
        </span>
        <h3>正在支付...</h3>
    </div>
    <div class="modal-body">
        <div class="pay-tip clearfix">
            <div class="fail">
                <h4>如果支付失败...</h4>
                <p>额度问题，推荐<a href="#" id="alipayTrigger">支付宝快捷支付 &gt;</a></p>
            </div>
            <div class="success">
                <h4>支付成功了</h4>
                <p>立即查看刷新并查看您订单的最新状态</p>
            </div>
        </div>
    </div>
</div>

<div style="height: 200px;"></div>
