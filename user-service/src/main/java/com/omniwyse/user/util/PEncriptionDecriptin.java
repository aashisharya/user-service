package com.omniwyse.user.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import com.omniwyse.user.constant.UtilConstant;

public class PEncriptionDecriptin {

	public static String encrypt(String data) {
		byte[] dataToSend = data.getBytes();
		byte[] encryptedData = null;
		try {
			Cipher c = Cipher.getInstance(UtilConstant.ALGORITHM);
			SecretKeySpec secretKey = new SecretKeySpec(UtilConstant.KEY, UtilConstant.AES);
			c.init(Cipher.ENCRYPT_MODE, secretKey);
			encryptedData = c.doFinal(dataToSend);
		} catch (Exception e) {
			e.printStackTrace();
		}
		byte[] encryptedByteValue = new Base64().encode(encryptedData);
		return new String(encryptedByteValue);// .toString();
	}

	public static String decrypt(String data) {

		byte[] encryptedData = new Base64().decode(data);
		byte[] decrypted = null;

		try {
			Cipher c = Cipher.getInstance(UtilConstant.ALGORITHM);
			SecretKeySpec k = new SecretKeySpec(UtilConstant.KEY, UtilConstant.AES);
			c.init(Cipher.DECRYPT_MODE, k);
			decrypted = c.doFinal(encryptedData);
		} catch (Exception e) {
		}
		return new String(decrypted);
	}
	/*
	 * public static void main(String[] args) { String password =
	 * PEncriptionDecriptin.encrypt("root"); System.out.println(password);
	 * System.out.println(PEncriptionDecriptin.decrypt(password)); }
	 */
}
