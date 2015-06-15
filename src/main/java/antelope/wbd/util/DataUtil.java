package antelope.wbd.util;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataUtil {
	private static Map<String, List<Data>> dataMap = new HashMap<String, List<Data>>();

	public static Map<String, List<Data>> getDataMap() {
		return dataMap;
	}

	public static List<Data> getTotalData(String key) {
		return dataMap.get(key);
	}

	public static void setTotalData(String key, List<Data> list) {
		dataMap.put(key, list);
	}

	public static Data getData(String key, int no) {
		List<Data> list = dataMap.get(key);
		if (list == null || no < 0 || no >= list.size()) {
			return null;
		}
		return list.get(no);
	}

	public static void setData(String key, int no, Data data) {
		List<Data> list = dataMap.get(key);
		if (list == null) {
			list = new ArrayList<Data>();
			dataMap.put(key, list);
		}

		for (int i = list.size(); i <= no; i++) {
			list.add(null);
		}

		list.set(no, data);
	}

	public static int size(String key) {
		List<Data> list = dataMap.get(key);
		if (list == null) {
			return 0;
		}
		return list.size();
	}
	
	public static void removeData(String key){
		if(dataMap.containsKey(key)){
			dataMap.remove(key);
		}
	}
	
	public static void removeData(String key,int no){
		if(dataMap.containsKey(key)){
			List<Data> list = dataMap.get(key);
			if(list!=null){
				list.set(no,new Data());
			}
		}
	}

	public static void main(String[] args) {
		setData("room1gf", 10, new Data());
		System.out.println(getTotalData("room1gf").size());
		System.out.println(getData("room1gf", 10));
	}

}
