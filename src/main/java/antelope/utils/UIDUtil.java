package antelope.utils;


import java.util.Date;

/**
 * 36位随机字符串生成器(取自Flex)
 *  
 *
 * @author  李宁
 * @version 1.0, 2009-2-8
 * @since   1.0
 */
public class UIDUtil {
    /**
     *  @private
     *  Char codes for 0123456789ABCDEF
     */
    private static final char[] ALPHA_CHAR_CODES = new char[]{48, 49, 50, 51, 52, 53, 54, 
        55, 56, 57, 65, 66, 67, 68, 69, 70};
	
	public static final String createUID() {
        char[] uid = new char[36];
	    int index = 0;
	    
	    int i;
	    int j;
	    
	    for (i = 0; i < 8; i++)
	    {
	        uid[index++] = ALPHA_CHAR_CODES[(int) Math.floor(Math.random() *  16)];
	    }
	
	    for (i = 0; i < 3; i++)
	    {
	        uid[index++] = 45; // charCode for "-"
	        
	        for (j = 0; j < 4; j++)
	        {
	            uid[index++] = ALPHA_CHAR_CODES[(int)Math.floor(Math.random() *  16)];
	        }
	    }
	    
	    uid[index++] = 45; // charCode for "-"
	
	    long time = new Date().getTime();
	    // Note: time is the number of milliseconds since 1970,
	    // which is currently more than one trillion.
	    // We use the low 8 hex digits of this number in the UID.
	    // Just in case the system clock has been reset to
	    // Jan 1-4, 1970 (in which case this number could have only
	    // 1-7 hex digits), we pad on the left with 7 zeros
	    // before taking the low digits.
	    String timestr = ("0000000" + Long.toString(time, 16).toUpperCase());
	    String timeString = timestr.substring(timestr.length() - 8);
	    
	    for (i = 0; i < 8; i++)
	    {
	        uid[index++] = timeString.charAt(i);
	    }
	    
	    for (i = 0; i < 4; i++)
	    {
	        uid[index++] = ALPHA_CHAR_CODES[(int)Math.floor(Math.random() *  16)];
	    }
	    return String.valueOf(uid);
	}
	
}
