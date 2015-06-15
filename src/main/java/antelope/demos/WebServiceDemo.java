package antelope.demos;

import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

import antelope.utils.WebServiceUtil;

/**
 * WebService样例程序
 * @author lining
 * @since 2013-9-13
 */
public class WebServiceDemo {

	/**
	 * webservice定义及调用demo
	 * 若要查看此webservice的相关wsdl定义请在浏览器端访问
	 * http://localhost:8083/antelope/services/WebServiceDemo?wsdl链接（注意对应的ip及端口要根据开发人员所设置的决定)
	 * @param args
	 * @throws RemoteException
	 * @throws ServiceException
	 */
	public static void main(String[] args) throws RemoteException, ServiceException {
		String returnStr = WebServiceUtil.invoke("http://localhost:8083/antelope/services/WebServiceDemo", "http://demos.antelope", "testWebService", 
				new String[]{"param1","param2"}, new String[]{"参数一", "参数二"});
		System.out.println("调用返回数据:" + returnStr);
	}
	
	public String testWebService(String param1, String param2) {
		System.out.println("param1:" + param1);
		System.out.println("param2:" + param2);
		return "成功调用";
	}
}


