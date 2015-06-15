package antelope.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.security.DigestInputStream;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class SecurityUtil
{
  private static byte[] keys = { -84, -19, 0, 5, 
    115, 114, 0, 34, 
    115, 117, 110, 46, 
    115, 101, 99, 117, 
    114, 105, 116, 121, 
    46, 112, 114, 111, 
    118, 105, 100, 101, 
    114, 46, 68, 83, 
    65, 80, 117, 98, 
    108, 105, 99, 75, 
    101, 121, -42, 114, 
    125, 13, 4, 25, 
    -21, 123, 2, 
    0, 1, 76, 0, 1, 
    121, 116, 0, 22, 
    76, 106, 97, 118, 
    97, 47, 109, 97, 
    116, 104, 47, 66, 
    105, 103, 73, 110, 
    116, 101, 103, 101, 
    114, 59, 120, 114, 
    0, 25, 115, 117, 
    110, 46, 115, 101, 
    99, 117, 114, 105, 
    116, 121, 46, 120, 
    53, 48, 57, 46, 
    88, 53, 48, 57, 
    75, 101, 121, -75, 
    -96, 29, -66, 100, 
    -102, 114, -90, 3, 
    0, 5, 73, 
    0, 10, 117, 110, 117, 
    115, 101, 100, 66, 
    105, 116, 115, 76, 
    0, 5, 97, 108, 
    103, 105, 100, 116, 
    0, 31, 76, 115, 
    117, 110, 47, 115, 
    101, 99, 117, 114, 
    105, 116, 121, 47, 
    120, 53, 48, 57, 
    47, 65, 108, 103, 
    111, 114, 105, 116, 
    104, 109, 73, 100, 
    59, 76, 0, 12, 
    98, 105, 116, 83, 
    116, 114, 105, 110, 
    103, 75, 101, 121, 
    116, 0, 28, 76, 
    115, 117, 110, 47, 
    115, 101, 99, 117, 
    114, 105, 116, 121, 
    47, 117, 116, 105, 
    108, 47, 66, 105, 
    116, 65, 114, 114, 
    97, 121, 59, 91, 
    0, 10, 101, 110, 
    99, 111, 100, 101, 
    100, 75, 101, 121, 
    116, 0, 2, 91, 
    66, 91, 0, 3, 
    107, 101, 121, 113, 
    0, 126, 0, 5, 
    120, 112, 119, -12, 
    48, -127, -15, 48, 
    -127, -88, 6, 7, 
    42, -122, 72, -50, 
    56, 4, 1, 48, 
    -127, -100, 2, 65, 
    0, -4, -90, -126, 
    -50, -114, 18, -54, 
    -70, 38, -17, -52, 
    -9, 17, 14, 82, 
    109, -80, 120, -80, 
    94, -34, -53, -51, 
    30, -76, -94, 8, 
    -13, -82, 22, 23, 
    -82, 1, -13, 91, 
    -111, -92, 126, 109, 
    -10, 52, 19, -59, 
    -31, 46, -48, -119, 
    -101, -51, 19, 42, 
    -51, 80, -39, -111, 
    81, -67, -60, 62, 
    -25, 55, 89, 46, 
    23, 2, 21, 
    0, -106, 46, -35, -52, 
    54, -100, -70, -114, 
    -69, 38, 14, -26, 
    -74, -95, 38, -39, 
    52, 110, 56, -59, 
    2, 64, 103, -124, 
    113, -78, 122, -100, 
    -12, 78, -23, 26, 
    73, -59, 20, 125, 
    -79, -87, -86, -14, 
    68, -16, 90, 67, 
    77, 100, -122, -109, 
    29, 45, 20, 39, 
    27, -98, 53, 3, 
    11, 113, -3, 115, 
    -38, 23, -112, 105, 
    -77, 46, 41, 53, 
    99, 14, 28, 32, 
    98, 53, 77, 13, 
    -94, 10, 108, 65, 
    110, 80, -66, 121, 
    76, -92, 3, 68, 
    0, 2, 65, 
    0, -74, 47, 93, 86, 
    75, 110, 72, -47, 
    69, 71, 36, -6, 
    95, 67, 98, -102, 
    -125, 3, 2, 75, 
    75, 30, 116, 93, 
    -2, -68, -80, -48, 
    -88, -12, 104, -39, 
    88, -69, -101, -46, 
    -53, 16, 113, 2, 
    -115, 13, 86, 111, 
    -61, 31, 57, -79, 
    72, -80, -37, 111, 
    19, 97, 4, 106, 
    58, -126, 18, 7, 
    44, -35, -59, 34, 
    120, 115, 114, 
    0, 20, 106, 97, 118, 
    97, 46, 109, 97, 
    116, 104, 46, 66, 
    105, 103, 73, 110, 
    116, 101, 103, 101, 
    114, -116, -4, -97, 
    31, -87, 59, -5, 
    29, 3, 0, 6, 
    73, 0, 8, 98, 
    105, 116, 67, 111, 
    117, 110, 116, 73, 
    0, 9, 98, 105, 
    116, 76, 101, 110, 
    103, 116, 104, 73, 
    0, 19, 102, 105, 
    114, 115, 116, 78, 
    111, 110, 122, 101, 
    114, 111, 66, 121, 
    116, 101, 78, 117, 
    109, 73, 0, 12, 
    108, 111, 119, 101, 
    115, 116, 83, 101, 
    116, 66, 105, 116, 
    73, 0, 6, 115, 
    105, 103, 110, 117, 
    109, 91, 0, 9, 
    109, 97, 103, 110, 
    105, 116, 117, 100, 
    101, 113, 0, 126, 
    0, 5, 120, 114, 
    0, 16, 106, 97, 
    118, 97, 46, 108, 
    97, 110, 103, 46, 
    78, 117, 109, 98, 
    101, 114, -122, -84, 
    -107, 29, 11, -108, 
    -32, -117, 2, 
    0, 0, 120, 112, -1, 
    -1, -1, -1, -1, 
    -1, -1, -1, -1, 
    -1, -1, -2, -1, 
    -1, -1, -2, 
    0, 0, 0, 1, 117, 
    114, 0, 2, 91, 
    66, -84, -13, 23, 
    -8, 6, 8, 84, 
    -32, 2, 
    0, 0, 120, 112, 
    0, 0, 0, 64, -74, 47, 
    93, 86, 75, 110, 
    72, -47, 69, 71, 
    36, -6, 95, 67, 
    98, -102, -125, 3, 
    2, 75, 75, 30, 
    116, 93, -2, -68, 
    -80, -48, -88, -12, 
    104, -39, 88, -69, 
    -101, -46, -53, 16, 
    113, 2, -115, 13, 
    86, 111, -61, 31, 
    57, -79, 72, -80, 
    -37, 111, 19, 97, 
    4, 106, 58, -126, 
    18, 7, 44, -35, 
    -59, 34, 120 };
  private static PublicKey pubkey = null;

  public static void main(String[] args)
    throws Exception 
  {
    String wid = "���ܲ���";

    System.out.println("3DES");
    System.out.println(wid);
    byte[] bt = des3Encrypt(wid.getBytes());
    System.out.println(ConvertUtil.base64Encode(bt));
    bt = des3Decrypt(bt);
    System.out.println(new String(bt));
    System.out.println("\nBlowFish");
    System.out.println(wid);
    bt = blowFishEncrypt(wid.getBytes());
    System.out.println(ConvertUtil.base64Encode(bt));
    bt = blowFishDecrypt(bt);
    System.out.println(new String(bt));
  }

  public static synchronized String digest(byte[] msg, String algorithm)
    throws NoSuchAlgorithmException
  {
    MessageDigest md;
    String digestString = null;
    try
    {
      md = MessageDigest.getInstance(algorithm);
      md.update(msg);
      byte[] bt = md.digest();
      digestString = ConvertUtil.base64Encode(bt);
    }
    catch (NoSuchAlgorithmException ex)
    {
      throw ex;
    }
    return digestString;
  }

  public static synchronized String digest(InputStream is, String algorithm)
    throws NoSuchAlgorithmException, IOException
  {
    MessageDigest md;
    DigestInputStream in;
    int BUFFER_SIZE = 32768;

    String digestString = null;
    try
    {
      md = MessageDigest.getInstance(algorithm);
      in = new DigestInputStream(is, md);

      byte[] buffer = new byte[BUFFER_SIZE];
      int i = in.read(buffer, 0, BUFFER_SIZE);
      while (i == BUFFER_SIZE)
      {
        i = in.read(buffer, 0, BUFFER_SIZE);
      }
      md = in.getMessageDigest();
      in.close();
      byte[] bt = md.digest();
      digestString = ConvertUtil.base64Encode(bt);
    }
    catch (NoSuchAlgorithmException ex)
    {
      throw ex;
    }
    catch (IOException ex)
    {
      throw ex;
    }
    return digestString;
  }

  public static synchronized String md5(byte[] msg)
    throws NoSuchAlgorithmException
  {
    return digest(msg, "MD5");
  }

  public static synchronized String md5(InputStream is)
    throws NoSuchAlgorithmException, IOException
  {
    return digest(is, "MD5");
  }

  public static synchronized String sha1(byte[] msg)
    throws NoSuchAlgorithmException
  {
    return digest(msg, "SHA1");
  }

  public static synchronized String sha1(InputStream is)
    throws NoSuchAlgorithmException, IOException
  {
    return digest(is, "SHA1");
  }

  public static synchronized byte[] des3Encrypt(byte[] msg)
    throws Exception
  {
    return des3Encrypt(msg, getPasswd());
  }

  public static synchronized byte[] des3Decrypt(byte[] msg)
    throws Exception
  {
    return des3Decrypt(msg, getPasswd());
  }

  public static synchronized byte[] des3Encrypt(byte[] msg, String key)
    throws Exception
  {
    return dataCrypt(msg, key, 168, 1, "DESede");
  }

  public static synchronized byte[] des3Decrypt(byte[] msg, String key)
    throws Exception
  {
    return dataCrypt(msg, key, 168, 2, "DESede");
  }

  public static synchronized byte[] blowFishEncrypt(byte[] msg)
    throws Exception
  {
    return blowFishEncrypt(msg, getPasswd());
  }

  public static synchronized byte[] blowFishDecrypt(byte[] msg)
    throws Exception
  {
    return blowFishDecrypt(msg, getPasswd());
  }

  public static synchronized byte[] blowFishEncrypt(byte[] msg, Key key)
    throws Exception
  {
    return dataCrypt(msg, key, 1, "Blowfish");
  }

  public static synchronized byte[] blowFishEncrypt(byte[] msg, String key)
    throws Exception
  {
    return dataCrypt(msg, key, 128, 1, "Blowfish");
  }

  public static synchronized byte[] blowFishDecrypt(byte[] msg, String key)
    throws Exception
  {
    return dataCrypt(msg, key, 128, 2, "Blowfish");
  }

  public static synchronized byte[] blowFishDecrypt(byte[] msg, Key key)
    throws Exception
  {
    return dataCrypt(msg, key, 2, "Blowfish");
  }

  public static synchronized byte[] rc6Encrypt(byte[] msg)
    throws Exception
  {
    return dataCrypt(msg, getPasswd(), 1, "RC6");
  }

  public static synchronized byte[] rc6Decrypt(byte[] msg)
    throws Exception
  {
    return rc6Encrypt(msg, getPasswd());
  }

  public static synchronized byte[] rc6Encrypt(byte[] msg, String key)
    throws Exception
  {
    return dataCrypt(msg, key, 1, "RC6");
  }

  public static synchronized byte[] rc6Encrypt(byte[] msg, Key key)
    throws Exception
  {
    return rc6Decrypt(msg, getPasswd());
  }

  public static synchronized byte[] rc6Decrypt(byte[] msg, String key)
    throws Exception
  {
    return dataCrypt(msg, key, 2, "RC6");
  }

  public static synchronized byte[] rc6Decrypt(byte[] msg, Key key)
    throws Exception
  {
    return dataCrypt(msg, key, 2, "RC6");
  }

  public static synchronized byte[] dataCrypt(byte[] msg, String passwd, int mode, String algorithm)
    throws Exception
  {
    return dataCrypt(msg, passwd, -1, mode, algorithm);
  }

  public static synchronized byte[] dataCrypt(byte[] msg, String passwd, int len, int mode, String algorithm) throws Exception
  {
    KeyGenerator keygen = KeyGenerator.getInstance(algorithm);
    if (len <= 0)
    {
      keygen.init(new SecureRandom(passwd.getBytes("ISO-8859-1")));
    }
    else
    {
      keygen.init(len, new SecureRandom(passwd.getBytes("ISO-8859-1")));
    }
    SecretKey deskey = keygen.generateKey();
    Cipher c1 = Cipher.getInstance(algorithm);
    c1.init(mode, deskey);
    byte[] cipherByte = c1.doFinal(msg);
    return cipherByte;
  }

  public static synchronized byte[] dataCrypt(byte[] msg, Key key, int mode, String algorithm)
    throws Exception
  {
    Cipher c1 = Cipher.getInstance(algorithm);
    c1.init(mode, key);
    byte[] cipherByte = c1.doFinal(msg);
    return cipherByte;
  }

  public static PublicKey getPublicKey()
  {
    if (pubkey == null)
    {
      try
      {
        ObjectInputStream in = new ObjectInputStream(
          new ByteArrayInputStream(keys));
        pubkey = (PublicKey)in.readObject();
        in.close();
      }
      catch (Exception ex)
      {
        ex.printStackTrace();
      }
    }
    return pubkey;
  }

  private static String getPasswd()
  {
    return "elingke1999";
  }
}