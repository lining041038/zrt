package antelope.pdf;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class toolkits {
	public static double[] getpxpy(String content) {
		String regex = "sx(.+?)exsy(.+?)ey";
		Matcher m = Pattern.compile(regex).matcher(content);
		double px = 0;
		double py = 0;
		double[] result = new double[2];
		while (m.find()) {
			String x = m.group(1);
			double xvalue = Double.parseDouble(x);
			String y = m.group(2);
			double yvalue = Double.parseDouble(y);
			px+=xvalue;
			py+=yvalue;
		}
		result[0] = px;
		result[1] = py;
		return result;
	}

	public static void main(String[] args) {
		double[] data = getpxpy("sx123exsy24ey");
		System.out.println(data[0]+"----"+data[1]);
	}
}
