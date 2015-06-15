package antelope.demos.entites;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 我的订单表，用于演示在线支付接口相关功能
 * @author lining
 * @since 2014-5-4
 */
@Entity
@Table(name="DEMO_MY_ORDER")
public class MyOrderItem {
	@Id
	public String sid; 
	public String name; 
	public String paycomplete; // 0  为未支付，1为已支付 
	public Double orderprice; // 到角
}
