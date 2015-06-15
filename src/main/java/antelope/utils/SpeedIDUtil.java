package antelope.utils;

import java.math.BigInteger;
import java.util.HashMap;

public class SpeedIDUtil
{
  private static HashMap map = new HashMap();
  private static String defPreFix = "";
  private static final long baseid = System.currentTimeMillis() * 1L;

  static
  {
    try
    {
      defPreFix = "";
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
  }

  public static synchronized String getId()
  {
    return getId(defPreFix, null, null);
  }

  public static synchronized String getId(String key)
  {
    return getId(key, null, null);
  }

  public static synchronized String getId(String key, String prefix)
  {
    return getId(defPreFix, prefix, null);
  }

  public static synchronized String getId(String key, String prefix, String postfix)
  {
    if (key == null)
    {
      key = defPreFix;
    }

    String val = getNextID(key);
    String str = "";
    if (prefix != null)
    {
      str = prefix;
    }
    else
    {
      str = defPreFix;
    }
    str = str + val;
    if (postfix != null)
    {
      str = str + postfix;
    }
    return str;
  }

  private static synchronized String getNextID(String key)
  {
    synchronized (key)
    {
      BigInteger val = (BigInteger)map.get(key);
      String id = null;
      if (val != null)
      {
        val = val.add(BigInteger.valueOf(1L));
        id = val.toString();
        map.put(key, val);
      }
      else
      {
        id = String.valueOf(baseid);
        map.put(key, BigInteger.valueOf(baseid));
      }
      return id;
    }
  }
}