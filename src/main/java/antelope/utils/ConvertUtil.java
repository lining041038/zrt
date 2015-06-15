package antelope.utils;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.lang.reflect.Array;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;


public class ConvertUtil
{
  protected static final BASE64Encoder base64Encoder = new BASE64Encoder();
  protected static final BASE64Decoder base64Decoder = new BASE64Decoder();
  protected static final String HEXINDEX = "0123456789abcdef0123456789ABCDEF";
  protected static final char[] HEXCHAR = { 
    '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 
    'E', 'F' };

  public static synchronized String convert(Object value)
  {
    if (value == null)
    {
      return null;
    }
    if (value.getClass().isArray())
    {
      if (Array.getLength(value) < 1)
      {
        return null;
      }
      value = Array.get(value, 0);
      if (value == null)
      {
        return null;
      }
      if (value instanceof String)
      {
        return ((String)value);
      }

      return value.toString();
    }

    return value.toString();
  }

  public static synchronized String convert(Object value, String defVal)
  {
    if (value == null)
    {
      return defVal;
    }
    if (value.getClass().isArray())
    {
      if (Array.getLength(value) < 1)
      {
        return defVal;
      }
      value = Array.get(value, 0);
      if (value == null)
      {
        return defVal;
      }

      return value.toString();
    }

    return value.toString();
  }

  public static synchronized String base64Encode(byte[] encodeBytes)
  {
    return base64Encode(encodeBytes, 0, encodeBytes.length);
  }

  public static synchronized String base64Encode(byte[] encodeBytes, int offset, int length)
  {
    ByteArrayInputStream bin = new ByteArrayInputStream(encodeBytes, offset, length);
    ByteArrayOutputStream bout = new ByteArrayOutputStream();
    byte[] buf = (byte[])null;
    try
    {
      base64Encoder.encodeBuffer(bin, bout);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    buf = bout.toByteArray();
    return new String(buf).trim();
  }

  public static synchronized String base64Encode(InputStream in)
  {
    ByteArrayOutputStream bout = new ByteArrayOutputStream();
    byte[] buf = (byte[])null;
    try
    {
      base64Encoder.encodeBuffer(in, bout);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    buf = bout.toByteArray();
    return new String(buf).trim();
  }

  public static synchronized void base64Encode(InputStream in, OutputStream out)
  {
    try
    {
      base64Encoder.encodeBuffer(in, out);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  public static synchronized byte[] base64Decode(String base64Str)
  {
    ByteArrayInputStream bin = new ByteArrayInputStream(base64Str.getBytes());
    ByteArrayOutputStream bout = new ByteArrayOutputStream();
    byte[] buf = (byte[])null;
    try
    {
      base64Decoder.decodeBuffer(bin, bout);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }

    buf = bout.toByteArray();
    return buf;
  }

  public static synchronized void base64Decode(InputStream in, OutputStream out)
  {
    try
    {
      base64Decoder.decodeBuffer(in, out);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  public static synchronized String byteToHex(byte b)
  {
    return Integer.toHexString(0xFF & b);
  }

  public static synchronized String bytesToHex(byte[] encodeBytes)
  {
    StringBuffer result = new StringBuffer();
    int n = encodeBytes.length;
    for (int i = 0; i < n; ++i)
    {
      int c = encodeBytes[i] & 0xFF;
      result.append(HEXCHAR[(c >> 4 & 0xF)]);
      result.append(HEXCHAR[(c & 0xF)]);
    }
    return result.toString();
  }

  public static synchronized byte[] hexToBytes(String s)
  {
    int l = s.length() / 2;
    byte[] data = new byte[l];
    int j = 0;

    for (int i = 0; i < l; ++i)
    {
      char c = s.charAt(j++);

      int n = "0123456789abcdef0123456789ABCDEF".indexOf(c);
      if (n == -1)
      {
        return null;
      }
      int b = (n & 0xF) << 4;
      c = s.charAt(j++);
      n = "0123456789abcdef0123456789ABCDEF".indexOf(c);
      b += (n & 0xF);
      data[i] = (byte)b;
    }
    return data;
  }

  public static synchronized String unicodeToHexString(String s)
  {
    ByteArrayOutputStream bout = new ByteArrayOutputStream();
    DataOutputStream out = new DataOutputStream(bout);
    try
    {
      out.writeUTF(s);
      out.close();
      bout.close();
    }
    catch (IOException e)
    {
      return null;
    }
    return bytesToHex(bout.toByteArray());
  }

  public static synchronized String hexStringToUnicode(String s)
  {
    byte[] b = hexToBytes(s);
    ByteArrayInputStream bin = new ByteArrayInputStream(b);
    DataInputStream in = new DataInputStream(bin);
    try
    {
      return in.readUTF();
    }
    catch (IOException e)
    {
      return null;
    }
  }

  public static synchronized String unicodeToAscii(String s)
  {
    if ((s == null) || (s.equals("")))
    {
      return s;
    }
    int len = s.length();
    StringBuffer b = new StringBuffer(len);
    for (int i = 0; i < len; ++i)
    {
      char c = s.charAt(i);
      if (c == '\\')
      {
        if ((i < len - 1) && (s.charAt(i + 1) == 'u'))
        {
          b.append(c);
          b.append("u005c");
        }
        else
        {
          b.append(c);
        }
      }
      else if ((c >= ' ') && (c <= ''))
      {
        b.append(c);
      }
      else
      {
        b.append("\\u");
        b.append(HEXCHAR[(c >> '\f' & 0xF)]);
        b.append(HEXCHAR[(c >> '\b' & 0xF)]);
        b.append(HEXCHAR[(c >> '\4' & 0xF)]);
        b.append(HEXCHAR[(c & 0xF)]);
      }
    }

    return b.toString();
  }

  public static synchronized String asciiToUnicode(String s)
  {
    if ((s == null) || (s.indexOf("\\u") == -1))
    {
      return s;
    }
    int len = s.length();
    char[] b = new char[len];
    int j = 0;
    for (int i = 0; i < len; ++i)
    {
      char c = s.charAt(i);
      if ((c != '\\') || (i == len - 1))
      {
        b[(j++)] = c;
      }
      else
      {
        c = s.charAt(++i);
        if ((c != 'u') || (i == len - 1))
        {
          b[(j++)] = '\\';
          b[(j++)] = c;
        }
        else
        {
          int k = ("0123456789abcdef0123456789ABCDEF".indexOf(s.charAt(++i)) & 0xF) << 12;
          k += (("0123456789abcdef0123456789ABCDEF".indexOf(s.charAt(++i)) & 0xF) << 8);
          k += (("0123456789abcdef0123456789ABCDEF".indexOf(s.charAt(++i)) & 0xF) << 4);
          k += ("0123456789abcdef0123456789ABCDEF".indexOf(s.charAt(++i)) & 0xF);
          b[(j++)] = (char)k;
        }
      }
    }
    return new String(b, 0, j);
  }

  public static synchronized String InputStreamToString(InputStream x)
    throws IOException
  {
    InputStreamReader in = new InputStreamReader(x);
    StringWriter write = new StringWriter();
    int blocksize = 8192;
    char[] buffer = new char[blocksize];
    while (true)
    {
      int l = in.read(buffer, 0, blocksize);
      if (l == -1)
      {
        break;
      }
      write.write(buffer, 0, l);
    }
    write.close();
    x.close();
    return write.toString();
  }

  public static void main(String[] args)
    throws Exception
  {
    String oraStr = "1234567890";
    byte[] passwd = oraStr.getBytes();
    System.out.println(new String(passwd));
    String base64 = base64Encode(passwd);
    System.out.println(base64);
    passwd = base64Decode(base64);
    System.out.println(new String(passwd));

    String hex = bytesToHex(passwd);
    System.out.println(hex);
    passwd = hexToBytes(hex);
    System.out.println(new String(passwd));
  }
}