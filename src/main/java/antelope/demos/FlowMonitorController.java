package antelope.demos;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import antelope.jni.KNetFluxCacheReader;
import antelope.jni.KPFWFLUX;
import antelope.springmvc.BaseController;
import antelope.utils.JSONArray;
import antelope.utils.JSONException;
import antelope.utils.JSONObject;

@Controller
public class FlowMonitorController extends BaseController {
	
	boolean isstarted = false;

	@RequestMapping("/demos/sysfluxdemo/FlowMonitorController/getKPFWFLUX")
	public synchronized void getKPFWFLUX(HttpServletResponse res) throws IOException, JSONException {
		KNetFluxCacheReader reader = new KNetFluxCacheReader();
		
		if (!isstarted) {
			System.out.println("启动");
			long a=reader.get_nRecvSpeedStart();
			System.out.println(a);
			isstarted = true;
		}
		
		KPFWFLUX sysFlux = new KPFWFLUX();
		String a=reader.get_nRecvSpeedAllPackInfo();;
		//PackInfo[] packes = reader.get_nRecvSpeedAllPackInfo();
		System.out.println(a);//new JSONArray(packes)
		
	//	sysFlux.nRecvSpeed = reader.get_nRecvSpeed();
		//sysFlux.nRecvSpeedLocal = reader.get_nRecvSpeedLocal();
		//sysFlux.nSendSpeed = reader.get_nSendSpeed();
		//sysFlux.nSendSpeedLocal = reader.get_nSendSpeedLocal();
		//sysFlux.nTotalRecv = reader.get_nTotalRecv();
		//sysFlux.nTotalRecvLocal = reader.get_nTotalRecvLocal();
		//sysFlux.nTotalSend = reader.get_nTotalSend();
		//sysFlux.nTotalSendLocal = reader.get_nTotalSendLocal();
		print(new JSONObject(sysFlux, true), res);
	}
}
