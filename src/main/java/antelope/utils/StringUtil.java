package antelope.utils;

import java.io.Serializable;
import java.util.StringTokenizer;

public class StringUtil implements Serializable
{

	private static final long serialVersionUID = -3130292455499117268L;
	public static final String endl = System.getProperty("line.separator", "\n");
	
	public static synchronized String convert(String str, String encodeCharset,
								 String decodeCharset)
	{
		if (str != null)
		{
			try
			{
				byte[] bt = str.getBytes(decodeCharset);
				str = new String(bt, encodeCharset);
			}
			catch (Exception e)
			{}
		}
		return str;
	}

	public static synchronized String convert(String str)
	{
		if (str == null)
		{
			return null;
		}
		int len = str.length();
		byte[] bt = new byte[len];
		for (int i = 0; i < len; i++)
		{
			bt[i] = (byte)str.charAt(i);
		}
		String retn = new String(bt);
		return retn;
	}

	/**
	  @param string
	  @param o
	  @param n
	  @return String
	 */
	public static synchronized String replace(String string, String o, String n)
	{
		if (string == null || o == null || n == null)
		{
			return null;
		}
		if (string.length() == 0 || o.length() == 0)
		{
			return string;
		}

		String result = null;
		StringBuffer buff = new StringBuffer("");
		int newIndex = 0, oldIndex = 0;
		while ( (newIndex = string.indexOf(o, oldIndex)) != -1)
		{
			buff.append(string.substring(oldIndex, newIndex));
			buff.append(n);
			oldIndex = newIndex + o.length();
		}
		if (oldIndex != 0)
		{
			buff.append(string.substring(oldIndex, string.length()));
			result = buff.toString();
		}
		else
		{
			result = string;
		}
		return result;
	}
	
	/**
	  @param string
	  @param o
	  @param n
	  @return String
	 */
	public static synchronized String replace(String string, char o, String n)
	{
		if (string == null || o == '\0' || n == null)
		{
			return null;
		}
		if (string.length() == 0)
		{
			return string;
		}

		return StringUtil.replace(string, String.valueOf(o), n);
	}

	/**
	  @param string
	  @param o
	  @param n
	  @return String
	 */
	public static synchronized String replace(String string, String o, char n)
	{
		if (string == null || n == '\0' || o == null)
		{
			return null;
		}
		if (string.length() == 0)
		{
			return string;
		}

		return StringUtil.replace(string, o, n);
	}

	/**
	  @param string
	  @param o
	  @param n
	  @return String
	 */
	public static synchronized String replace(String string, char o, char n)
	{
		if (string == null || o == '\0' || n == '\0')
		{
			return null;
		}
		if (string.length() == 0)
		{
			return string;
		}

		return string.replace(o, n);
	}

	/**
	  @param string
	  @param o
	  @return String
	 */
	public static synchronized String remove(String string, char o)
	{
		return StringUtil.replace(string, o, "");
	}

	/**
	  @param string
	  @param o
	  @return String
	 */
	public static synchronized String remove(String string, String o)
	{
		return StringUtil.replace(string, o, "");
	}

	
	public static synchronized String html2Txt(String str)
	{
		return str;
	}

	public static synchronized String txt2Html(String s)
	{
		StringBuffer sb = new StringBuffer();
		return txt2Html(sb, s);
	}

	/**
	  @param s
	  @return String
	 */
	public static synchronized String txt2Html(StringBuffer stringbuffer, String s)
	{
		if (s == null || s.length() == 0)
		{
			return "";
		}
		int j = s.length();
		for (int i = 0; i < j; i++)
		{
			char c = s.charAt(i);
			switch (c)
			{
				case 60:
					stringbuffer.append("&lt;");
					break;
				case 62:
					stringbuffer.append("&gt;");
					break;
				case 38:
					stringbuffer.append("&amp;");
					break;
				case 34:
					stringbuffer.append("&quot;");
					break;
				case 169:
					stringbuffer.append("&copy;");
					break;
				case 174:
					stringbuffer.append("&reg;");
					break;
				case 165:
					stringbuffer.append("&yen;");
					break;
				case 8364:
					stringbuffer.append("&euro;");
					break;
				case 8482:
					stringbuffer.append("&#153;");
					break;
				case 10:
					stringbuffer.append("<br>");
					break;
				case 13:
					stringbuffer.append("<br>");
					break;
				case 32:
					stringbuffer.append("&nbsp;");
					break;
				default:
					stringbuffer.append(c);
					break;
			}
		}
		return stringbuffer.toString();
	}

	/**
	  @param s
	  @param count
	  @return String
	 */
	public static synchronized String lineWrap(String s, int count)
	{
		if (s == null || count <= 0)
		{
			return s;
		}

		int l = s.length();
//		String eol = System.getProperty("line.separator", "\n");
		StringBuffer sbRet = new StringBuffer("");
		int i;
		for (i = 0; i < l / count; i++)
		{
			sbRet.append(s.substring(i * count, i * count + count));
			sbRet.append("\n");
		}
		sbRet.append(s.substring(i * count, l));
		return sbRet.toString();
	}
	
	public static synchronized String substring(String str, int begin, int end)
	{
		if( begin < 0 )
			begin = 0;
		if (str == null || begin > end || str.length()<end )
		{
			return str;
		}
		String retn = str.substring(begin, end);
		return retn;
	}


	public static synchronized String getHexStr(String str)
	{
		if (str == null)
		{
			return "";
		}
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < str.length(); i++)
		{
			if (i % 16 == 0)
			{
				sb.append("\n");
			}
			sb.append(Integer.toHexString(str.charAt(i)) + " ");
		}
		return sb.toString();
	}

	public static synchronized boolean isContain(String[] names, String name)
	{
		return isContain( names, name, true );
	}
	
	public static synchronized boolean isContain(String[] names, String name, boolean isIgnoreCase)
	{
		if (names == null || names.length == 0 || name == null)
		{
			return false;
		}
		for (int i = 0; i < names.length; i++)
		{
			if( isIgnoreCase )
			{
				if (name.equalsIgnoreCase(names[i]))
				{
					return true;
				}
			}
			else
			{
				if (name.equals(names[i]))
				{
					return true;
				}
			}
		}
		return false;
	}
	
	public static String getArrayValue(String[] array, int n, String defval)
	{
		String str = null;
		try
		{
			str = array[n];
		}
		catch( Exception ex )
		{
		}
		if( str == null || str.length() == 0 )
		{
			str = defval;
		}
		return str;
	}
	
	public static String getAttributeValue( String str, String attr_name, String delim )
	{
		String value = null;
		StringTokenizer st =new StringTokenizer( str, delim, true );
		while( st.hasMoreElements() )
		{
			String name = ((String)st.nextElement()).trim();
			if( attr_name.equals( name ) )
			{
				if( st.hasMoreElements() )
				{
					String token = ((String)st.nextElement());
					if( delim.indexOf(token) != -1 )
					{
						if( st.hasMoreElements() )
						{
							value = ((String)st.nextElement()).trim();
							break;
						}
					}
				}
			}
		}
		return value;
	}
	
//	public static HashMap getAttributesValue( String str, String delim )
//	{
//		HashMap values = null;
//		StringTokenizer st =new StringTokenizer( str, delim, true );
//		while( st.hasMoreElements() )
//		{
//			Object name = st.nextElement();
//			Object value = null;
//			if( st.hasMoreElements() )
//			{
//				String token = (String)st.nextElement();
//				if( delim.indexOf(token) != -1 )
//				{
//					if( st.hasMoreElements() )
//					{
//						value = st.nextElement();
//					}
//				}
//			}
//			if( values == null )
//			{
//				values = new HashMap();
//			}
//			values.put( name, value );
//		}
//		return values;
//	}
}
