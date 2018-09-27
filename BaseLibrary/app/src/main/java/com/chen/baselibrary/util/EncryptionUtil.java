package com.chen.baselibrary.util;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * 加密工具：MD5
 * 
 * @author chenxiuyi
 * 
 */
public class EncryptionUtil {

	/**
	 * =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=MD5加密=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
	 */
	// 全局数组
	private final static String[] strDigits = { "0", "1", "2", "3", "4", "5",
			"6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };
	// 返回形式为数字跟字符串
	private static String byteToArrayString(byte bByte) {
		int iRet = bByte;
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
		//System.out.println("iRet1=" + iRet);
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

	public static String GetMD5Code(String strObj) {
		String resultString = null;
		try {
			resultString = new String(strObj);
			MessageDigest md = MessageDigest.getInstance("MD5");
			// md.digest() 该函数返回值为存放哈希值结果的byte数组
			resultString = byteToString(md.digest(strObj.getBytes()));
		} catch (NoSuchAlgorithmException ex) {
			ex.printStackTrace();
			return "";
		}
		return resultString;
	}
	/**
	 * =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=AES加解密=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
	 */
	/**
	 * AES加密字符串生成base64密文
	 *
	 * @param content 需要被加密的字符串
	 * @param password 加密需要的密码
	 * @return base64密文
	 */
	public static String AESEncrypt(String content, String password) {
		try {
			// 创建AES的Key生产者
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			// 利用用户密码作为随机数初始化出
			kgen.init(256, new SecureRandom(password.getBytes()));
			// 根据用户密码，生成一个密钥
			SecretKey secretKey = kgen.generateKey();
			// 返回基本编码格式的密钥，如果此密钥不支持编码，则返回
			byte[] enCodeFormat = secretKey.getEncoded();
			// 转换为AES专用密钥
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
			// 创建密码器
			Cipher cipher = Cipher.getInstance("AES");

			byte[] byteContent = content.getBytes("utf-8");
			// 初始化为加密模式的密码器
			cipher.init(Cipher.ENCRYPT_MODE, key);
			// 加密
			byte[] result = cipher.doFinal(byteContent);
			String base64Result = Base64.encodeToString(result,Base64.DEFAULT);
			return base64Result;
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 解密AES加密过的字符串
	 *
	 * @param content AES加密过的base64密文
	 * @param password 加密时的密码
	 * @return 明文
	 */
	public static String AESDecrypt(String content, String password) {
		try {
			// 创建AES的Key生产者
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			kgen.init(256, new SecureRandom(password.getBytes()));
			// 根据用户密码，生成一个密钥
			SecretKey secretKey = kgen.generateKey();
			// 返回基本编码格式的密钥
			byte[] enCodeFormat = secretKey.getEncoded();
			// 转换为AES专用密钥
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
			// 创建密码器
			Cipher cipher = Cipher.getInstance("AES");
			// 初始化为解密模式的密码器
			cipher.init(Cipher.DECRYPT_MODE, key);
			byte[] decContent = Base64.decode(content,Base64.DEFAULT);
			byte[] result = cipher.doFinal(decContent);
			return new String(result,"utf-8");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e){
			e.printStackTrace();
		}
		return null;
	}
}
