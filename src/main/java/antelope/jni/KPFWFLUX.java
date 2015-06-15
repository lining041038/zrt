package antelope.jni;

/**
 * 保存网络流量相关数据
 * @author lining
 * @since 2012-9-13
 */
public class KPFWFLUX {
	// 接收速度
	public long	nRecvSpeed;

	// 发送速度
	public long	nSendSpeed;

	// 总计接受的流量
	public long	nTotalRecv;

	// 总计发送的流量
	public long	nTotalSend;

	// 本地接收速度
	public	long nRecvSpeedLocal;

	// 本地发送速度
	public long	nSendSpeedLocal;

	// 本地总计接受的流量
	public long	nTotalRecvLocal;

	// 本地总计发送的流量
	public long	nTotalSendLocal;
}
