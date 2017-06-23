package org.marker.security;

import org.apache.commons.codec.digest.DigestUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5加密(依赖apache的DigestUtils类)
 * @author marker
 * */
public class MD5 {
	
	/**
	 * 私有构造方法
	 * */
    private MD5(){ }   
    
	
    /**
     * 获取MD5加密值
     * @return String md5值
     * 
     * */
	/**
	 * 获取MD5加密值
	 * @return String md5值
	 *
	 * */
	public static String getMD5Code(String plainText) {
		if (plainText == null) return null;

		try {
			//生成实现指定摘要算法的 MessageDigest 对象。
			MessageDigest md = MessageDigest.getInstance("MD5");
			//使用指定的字节数组更新摘要。
			md.update(plainText.getBytes("UTF-8"));
			//通过执行诸如填充之类的最终操作完成哈希计算。
			byte b[] = md.digest();
			//生成具体的md5密码到buf数组
			int i;
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			return buf.toString();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}




	public static void main(String[] args){   
         String passwd =  "123撒倒萨";   
	     System.out.println(passwd + " 加密后为： " + MD5.getMD5Code(passwd));  
    }






	// 全局数组
	private final static String[] strDigits = { "0", "1", "2", "3", "4", "5",
			"6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };



	// 返回形式为数字跟字符串
	private static String byteToArrayString(byte bByte) {
		int iRet = bByte;
		// System.out.println("iRet="+iRet);
		if (iRet < 0) {
			iRet += 256;
		}
		int iD1 = iRet / 16;
		int iD2 = iRet % 16;
		return strDigits[iD1] + strDigits[iD2];
	}

	// 返回形式只为数字
	private static String byteToNum(byte bByte) {
		int iRet = bByte;
//		System.out.println("iRet1=" + iRet);
		if (iRet < 0) {
			iRet += 256;
		}
		return String.valueOf(iRet);
	}

	// 转换字节数组为16进制字串
	private static String byteToString(byte[] bByte) {
		StringBuffer sBuffer = new StringBuffer();
		for (int i = 0; i < bByte.length; i++) {
			sBuffer.append(byteToArrayString(bByte[i]));
		}
		return sBuffer.toString();
	}

	public static String getMD5Code2(String strObj) {
		String resultString = null;
		try {
			resultString = new String(strObj);
			MessageDigest md = MessageDigest.getInstance("MD5");
			// md.digest() 该函数返回值为存放哈希值结果的byte数组
			resultString = byteToString(md.digest(strObj.getBytes()));
		} catch (NoSuchAlgorithmException ex) {
			ex.printStackTrace();
		}
		return resultString;
	}

}
 
