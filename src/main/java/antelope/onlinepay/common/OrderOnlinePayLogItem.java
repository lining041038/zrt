package antelope.onlinepay.common;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="SYS_ORDER_ONLINEPAY_LOG")
public class OrderOnlinePayLogItem {
	
	@Id
	public String sid;
	
	/**
	 * 本地交易的订单sid
	 */
	public String ordersid; 
	
	/**
	 * 本次交易状态，是否交易成功 0或空为未成功  1为已成功
	 * 注意此标识仅为支付宝交易接口获取之后的标注，具体业务还需要在支付成功的回调当中进行进一步处理
	 */
	public String orderstatus;
 
}
