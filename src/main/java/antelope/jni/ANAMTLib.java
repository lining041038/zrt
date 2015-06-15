package antelope.jni;

import com.ibm.icu.util.Calendar;

import antelope.utils.ClasspathResourceUtil;



public class ANAMTLib {
	
	static {
		ClasspathResourceUtil.load_WEB_INF_lib_File("anamtlib.dll");
	}

	public static final native int AMTValidateProductLicense();
	
	public static final native String AMTGetMachineCode();
	
	public class AMTProductLicenseStatus {
		public static final int kAMTProductLicenseStatus_unlicensed = 0;
		public static final int kAMTProductLicenseStatus_activated = 3;
	}
	
	public static void main(String[] args) {
		
		System.out.println(ANAMTLib.AMTValidateProductLicense());
	}
}
