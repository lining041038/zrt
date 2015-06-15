package antelope.jni;

import java.io.IOException;

import antelope.utils.ClasspathResourceUtil;
import antelope.utils.SystemOpts;

public class KNetFluxCacheReader {
	static {
		try {
			if ("true".equals(SystemOpts.getProperty("antelope_usewinpcapfluxstats")))
				ClasspathResourceUtil.load_WEB_INF_lib_File("antelope_jni_KNetFluxCacheReader.dll");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 启动流量监控守护线程
	 * @return
	 */
	public native long get_nRecvSpeedStart();
	
	/**
	 * 停止流量监控守护线程
	 * @return
	 */
	public native long get_nRecvSpeedStop();
	
	/**
	 * 获取最近10内网络包数据
	 * @return
	 */
	//public native PackInfo[] get_nRecvSpeedAllPackInfo();
	public native String get_nRecvSpeedAllPackInfo();
	
	// 注意以下方法已经弃用
	public native long get_nRecvSpeed();
	public native long get_nSendSpeed();
	public native long get_nTotalRecv();
	public native long get_nTotalSend();
	public native long get_nRecvSpeedLocal();
	public native long get_nSendSpeedLocal();
	public native long get_nTotalRecvLocal();
	public native long get_nTotalSendLocal();
}
