package com.chen.baselibrary.util;

import android.text.TextUtils;
import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
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
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import static com.google.common.base.Preconditions.checkNotNull;

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

	private static final String AES_CBC_PKCS5_PADDING = "AES/CBC/PKCS5Padding";
	private static final String AES = "AES";

	/**
	 * 生成秘钥
	 * * @return Base64编码的秘钥
	 */
	private static String generateSecretKey() {
		try {
			// 获取Key生成器实例,一般一个实例可以多次用来生成秘钥
			KeyGenerator keyGenerator = KeyGenerator.getInstance(AES);
			// 256位
			keyGenerator.init(256);
			// 生成密钥
			SecretKey secretKey = keyGenerator.generateKey();
			// 获取密钥
			byte[] keyBytes = secretKey.getEncoded();
			// 生成的秘钥转换成Base64编码,加、解密时需要用Base64还原秘钥
			return Base64.encodeToString(keyBytes, Base64.DEFAULT);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 检查明文密钥长度，AES要求密钥长度为16位
	 * 如果不是16位则截取或者重复为16位
	 * @param key
	 * @return 将key进行修复使他的长度=16
	 */
	private static String checkAndRepairKey(String key){
		if(TextUtils.isEmpty(key)){
			return null;
		}
		int length = key.length();
		if(length != 16){
			throw new IllegalArgumentException("密钥长度必须为16位");
		}
		String newKey = key;
		if(newKey.length() < 16){
			while (newKey.length() < 16){
				newKey += newKey;
			}
		}

		return newKey.substring(0,16);
	}
	/**
	 * 加密
	 * * @param plaintext 明文
	 * * @param key 秘钥
	 * * @return Base64编码的密文
	 */
	public static String AESEncrypt(String plaintext, String key) {
		checkNotNull(plaintext);
		checkNotNull(key);
		try {
			// Base64还原秘钥
			byte[] keyBytes = checkAndRepairKey(key).getBytes();
			// 还原密钥对象
			SecretKey secretKey = new SecretKeySpec(keyBytes, AES);
			// 加密初始化实例
			Cipher cipher = Cipher.getInstance(AES_CBC_PKCS5_PADDING);
			// CBC模式需要添加一个参数IvParameterSpec，ECB模式则不需要
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(new byte[cipher.getBlockSize()]));
			byte[] result = cipher.doFinal(plaintext.getBytes("UTF-8"));
			// 生成的密文转换成Base64编码出文本,解密时需要用Base64还原出密文
			return Base64.encodeToString(result, Base64.DEFAULT);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 解密
	 * * @param ciphertext 密文
	 * * @param key  秘钥
	 * * @return 明文
	 */
	public static String AESDecrypt(String ciphertext, String key) {
		checkNotNull(ciphertext);
		checkNotNull(key);
		try {
			// Base64还原秘钥
			byte[] keyBytes = checkAndRepairKey(key).getBytes();
			// 还原密钥对象
			SecretKey secretKey = new SecretKeySpec(keyBytes, AES);
			// 加密初始化实例
			Cipher cipher = Cipher.getInstance(AES_CBC_PKCS5_PADDING);
			cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(new byte[cipher.getBlockSize()]));
			// Base64还原密文
			byte[] cipherBytes = Base64.decode(ciphertext, Base64.DEFAULT);
			byte[] result = cipher.doFinal(cipherBytes);
			return new String(result, "UTF-8");
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
		} catch (InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
}
