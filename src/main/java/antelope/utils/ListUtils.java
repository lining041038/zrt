package antelope.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 列表工具类
 * @author lining
 * @since 2012-3-22
 */
public class ListUtils {
	/**
	 * 根据条件对列表进行去重操作
	 * @param list
	 * @param comparator
	 * @return
	 */
	public static final <T> void unique(List<T> list, Comparator<T> comparator) {
		List<T> added = new ArrayList<T>();
		for(int i = 0; i < list.size();) {
			boolean contained = false;
			for(int j = 0; j < added.size(); j++) {
				if (comparator.compare(added.get(j), list.get(i)) == 0) {
					contained = true;
					break;
				}
			}
			if (contained) {
				list.remove(i);
			} else {
				added.add(list.get(i));
				i++;
			}
		}
		
	}
	
	/**
	 * 根据源beans数据与目标beans数据的数据域映射关系将源数据列表复制为新的映射数据列表
	 * 前两个参数映射关系位置需要确保一致，当前仅支持同类型数据，并且仅支持数据域数据复制
	 * @param sourceFieldsNames
	 * @param targetFieldNames
	 * @param sourceBeans
	 * @param targetBeanClass
	 * @return 复制后的目标数据列表，不返回null值，若无数据则返回含有0个元素的列表
	 */
	public static final <T, A> List<A> copyList(String[] sourceFieldsNames, String[] targetFieldNames, List<T> sourceBeans, Class<A> targetBeanClass) throws InstantiationException, IllegalAccessException {
		List<A> targetbeans = new ArrayList<A>();
		if (sourceBeans == null)
			return targetbeans;
		
		Field[] fields = null;
		Map<String, Field> sourcefieldnameset = new HashMap<String, Field>();
		
		Map<String, Field> targetfieldnameset = new HashMap<String, Field>();
		Field[] targetFields = targetBeanClass.getFields();
		for (Field field : targetFields) {
			targetfieldnameset.put(field.getName(), field);
		}
		
		for (int i = 0; i < sourceBeans.size(); ++i) {
			T sourcebean = sourceBeans.get(i);
			
			if (fields == null) {
				fields = sourcebean.getClass().getFields();
				for (Field field : fields) {
					sourcefieldnameset.put(field.getName(), field);
				}
			}
			
			A targetbean = targetBeanClass.newInstance();
			for (int j = 0; j < sourceFieldsNames.length; ++j) {
				if (sourcefieldnameset.containsKey(sourceFieldsNames[j])
					&& targetfieldnameset.containsKey(targetFieldNames[j])) {
					Field field = sourcefieldnameset.get(sourceFieldsNames[j]);
					targetfieldnameset.get(targetFieldNames[j]).set(targetbean,
							field.get(sourcebean));
				}
			}
			targetbeans.add(targetbean);
		}
		
		return targetbeans;
	}
}







