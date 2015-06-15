package antelope.utils;


import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import net.sourceforge.pinyin4j.PinyinHelper;

public class TextUtils {

    public final static String htmlEncode(String s) {
        return htmlEncode(s, true);
    }

    public final static String htmlEncode(String s, boolean encodeSpecialChars) {
        s = noNull(s);

        StringBuffer str = new StringBuffer();

        for (int j = 0; j < s.length(); j++) {
            char c = s.charAt(j);

            if (c < '\200') {
                switch (c) {
                case '"':
                    str.append("&quot;");

                    break;

                case '&':
                    str.append("&amp;");

                    break;

                case '<':
                    str.append("&lt;");

                    break;

                case '>':
                    str.append("&gt;");

                    break;

                default:
                    str.append(c);
                }
            }
            else if (encodeSpecialChars && (c < '\377')) {
                String hexChars = "0123456789ABCDEF";
                int a = c % 16;
                int b = (c - a) / 16;
                String hex = "" + hexChars.charAt(b) + hexChars.charAt(a);
                str.append("&#x" + hex + ";");
            }
            else {
                str.append(c);
            }
        }

        return str.toString();
    }
    
    public final static String join(String glue, Iterator pieces) {
        StringBuffer s = new StringBuffer();

        while (pieces.hasNext()) {
            s.append(pieces.next().toString());

            if (pieces.hasNext()) {
                s.append(glue);
            }
        }

        return s.toString();
    }
    
    public final static String join(String glue, JSONArray arr) throws JSONException {
    	StringBuffer s = new StringBuffer();
    	for (int i = 0; i < arr.length(); i++) {
    		s.append(arr.getString(i));
    		if (i < arr.length() - 1) {
    			s.append(glue);
    		}
    	}
    	return s.toString();
    }

    public final static String join(String glue, String[] pieces) {
        return join(glue, Arrays.asList(pieces).iterator());
    }
    
    public final static String join(String glue, Object[] pieces) {
        return join(glue, Arrays.asList(pieces).iterator());
    }
    
    public final static String join(String glue, Collection<?> pieces) {
        return join(glue, pieces.iterator());
    }

	/** 将字符串放入Set当中以检查是否已经存在的情况 */
	public static Set<String> putStrsIntoSet (String[] strs) {
		Set<String> set = new HashSet<String>();
		if (strs == null) return set;
		
		for (int i = 0; i < strs.length; i++) 
			set.add(strs[i]);
		return set;
	}
    
    public final static String noNull(String string, String defaultString) {
        return (stringSet(string)) ? string : defaultString;
    }
    
    public final static String noNull(String ...strings) {
    	for (int i = 0; i < strings.length; ++i) {
    		if (stringSet(strings[i]))
    			return strings[i];
    	}
    	return "";
    }

    public final static String noNull(String string) {
        return noNull(string, "");
    }

    public final static boolean stringSet(String string) {
        return (string != null) && !"".equals(string.trim());
    }
    
    public final static String yesNo(int i) {
    	return i == 0?"否":"是";
    }
    /**
     * 将使用ISO-8859-1编码的string 更改成GBK编码的string值
     * @param str
     * @return
     * @throws UnsupportedEncodingException
     */
    public final static String toGBK(String str) throws UnsupportedEncodingException {
    	
    	return new String(str.getBytes("ISO-8859-1"),"GBK");
    }
    
    public final static String GBKtoISO_8859_1(String str) throws UnsupportedEncodingException {
    	if (str != null)
    		return new String(str.getBytes("GBK"),"ISO-8859-1");
    	return str;
    }
    /**
     * 将使用ISO-8859-1编码的string 更改成UTF-8编码的string值
     * @param str
     * @return
     * @throws UnsupportedEncodingException
     */
    public final static String toUTF8(String str) throws UnsupportedEncodingException {
    	
    	return new String(str.getBytes("ISO-8859-1"),"UTF-8");
    }
    
    public final static boolean verifyUrl(String url) {
        if (url == null) {
            return false;
        }

        if (url.startsWith("https://")) {
            url = "http://" + url.substring(8);
        }

        try {
            new URL(url);

            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }
    
    /**
     * 根据中文获取由中文首字母组成的拼音缩写, 非中文字符则将要被忽略
     */
    public final static String getPingyinByChinese(String chinese)  {
    	if (stringSet(chinese)) {
			String pingyt = "";
			for (int j = 0; j < chinese.length(); j++) {
				String[] hanyuPinyinStringArray = PinyinHelper.toHanyuPinyinStringArray(chinese.charAt(j));
				if (hanyuPinyinStringArray == null) {
					// pingyt += content.charAt(j);
				} else {
					pingyt += hanyuPinyinStringArray[0].toLowerCase().charAt(0);
				}
			}
			return pingyt;
		}
    	return "";
    }
    
    /**
     * 根据中文获取由中文首字母组成的拼音缩写
     */
    public final static String getPingyinByChineseNotIgnore(String chinese)  {
    	if (stringSet(chinese)) {
			String pingyt = "";
			for (int j = 0; j < chinese.length(); j++) {
				String[] hanyuPinyinStringArray = PinyinHelper.toHanyuPinyinStringArray(chinese.charAt(j));
				if (hanyuPinyinStringArray == null) {
					pingyt += chinese.charAt(j);
				} else {
					pingyt += hanyuPinyinStringArray[0].toLowerCase().charAt(0);
				}
			}
			return pingyt;
		}
    	return "";
    }
    
    /**
     * 根据中文获取由中文字母组成的全拼
     */
    public final static String getFullPingyinByChineseNotIgnore(String chinese)  {
    	if (stringSet(chinese)) {
			String pingyt = "";
			for (int j = 0; j < chinese.length(); j++) {
				String[] hanyuPinyinStringArray = PinyinHelper.toHanyuPinyinStringArray(chinese.charAt(j));
				if (hanyuPinyinStringArray == null) {
					pingyt += chinese.charAt(j);
				} else {
					pingyt += hanyuPinyinStringArray[0].toLowerCase().replaceFirst("\\d*$", "");
				}
			}
			return pingyt;
		}
    	return "";
    }
    
}
