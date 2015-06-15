package antelope.wbd.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Data {
	private Map<String, String> map = new HashMap<String, String>();
	private String totalData = "";
	private String imgUrl = "";
	private String backUrl = "";
	private List<String> list = new ArrayList<String>();

	public String getTotalData() {
		return totalData;
	}

	public void setTotalData(String totalData) {
		this.totalData = totalData;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getBackUrl() {
		return backUrl;
	}

	public void setBackUrl(String backUrl) {
		this.backUrl = backUrl;
	}

	public Map<String, String> getMap() {
		return map;
	}

	public void setMap(Map<String, String> map) {
		this.map = map;
	}

	public void setMapData(String key, String val) {
		if(!map.containsKey(key)){
			list.add(key);
		}
		map.put(key, val);
	}

	public String getMapData() {
		if (list.size() < 1) {
			return null;
		}
		String val ="[";
		for (int i = 0; i < list.size(); i++) {
			String temp_val = map.get(list.get(i));
			val+= temp_val.substring(1,temp_val.length()-1);
			if(i != list.size() - 1){
				val+=",";
			}
		}
		val += "]";
		return val;
	}
}
