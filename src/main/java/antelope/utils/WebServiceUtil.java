package antelope.utils;

import java.rmi.RemoteException;

import javax.xml.namespace.QName;
import javax.xml.rpc.Call;
import javax.xml.rpc.ParameterMode;
import javax.xml.rpc.ServiceException;

import org.apache.axis.client.Service;

/**
 * WebService工具类
 * @author lining
 * @since 2013-9-13
 */
public class WebServiceUtil {
	
	/**
	 * 纯字符串参数，并且含有字符串返回值的WebService调用工具方法，所有数据均走字符串
	 * @param url url地址
	 * @param namespace 命名空间
	 * @param methodName 远程调用方法名称
	 * @param paramNames 字符串参数名称数组
	 * @param params 字符串参数具体值
	 * @throws RemoteException
	 * @throws ServiceException
	 */
	public static String invoke(String url, String namespace, String methodName, String[] paramNames, String[] params) throws RemoteException, ServiceException {
		Service serv = new org.apache.axis.client.Service();
		Call call = null;
		call = (Call) serv.createCall();
		call.setTargetEndpointAddress(url);
		call.setOperationName(new QName(namespace, methodName));
		call.setReturnType(org.apache.axis.Constants.XSD_STRING);
		for (int i = 0; i < paramNames.length; ++i) {
			call.addParameter(paramNames[i], org.apache.axis.Constants.XSD_STRING,ParameterMode.IN);
		}
		return call.invoke(params) + "";
	}
	
	
}
