package antelope.utils;

import java.io.StringReader;
import java.util.ArrayList;

import org.wltea.analyzer.IKSegmentation;
import org.wltea.analyzer.Lexeme;

/**
 *
 * @author daijun
 * @date 2011-12-27
 * 搜索工具类
 *
 */
public class SearchUtil {

	static private IKSegmentation iks = null;
	static {
		iks = new IKSegmentation(null);
	}

	/**
	 * 分词结果以ArrayList返回
	 *
	 * @param target
	 * @return
	 * @throws Exception
	 */
	public ArrayList<String> analyze(String target) throws Exception{
		ArrayList<String> list = new ArrayList<String>();
		iks.reset(new StringReader(target));
		Lexeme lex = null;
		while ((lex = iks.next())!= null){
			String name = lex.getLexemeText();
			if (name.length() >= 2){
				list.add(name);
			}

		}
		return list;
	}

	/**
	 * 分词结果以字符串数组返回
	 *
	 * @param target
	 * @return
	 * @throws Exception
	 */
	public String[] analyzeToStringArray(String target) throws Exception{
		ArrayList<String> list = analyze(target);
		String[] result = (String [] )list.toArray(new String[list.size()]);
		return result;
	}

	/**
	 * 分词结果以空格分隔返回
	 *
	 * @param target
	 * @return
	 * @throws Exception
	 */
	public String analyzeToString(String target) throws Exception {
		ArrayList<String> list = analyze(target);
		int len = list.size();
		StringBuffer sb = new StringBuffer();
		for (int i=0;i<len;i++){
			if (i != 0){
				sb.append(" ");
			}
			sb.append(list.get(i));
		}
		return sb.toString();
	}


}
