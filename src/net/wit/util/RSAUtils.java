/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.util;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;
import org.apache.shiro.codec.Hex;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.util.Assert;

/**
 * Utils - RSA加密解密
 * 
 * @author rsico Team
 * @version 3.0
 */
public final class RSAUtils {

	/** 安全服务提供者 */
	private static final Provider PROVIDER = new BouncyCastleProvider();

	/** 密钥大小 */
	private static final int KEY_SIZE = 1024;

	/**
	 * 不可实例化
	 */
	private RSAUtils() {
	}

	/**
	 * 生成密钥对
	 * 
	 * @return 密钥对
	 */
	public static KeyPair generateKeyPair() {
		try {
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA", PROVIDER);
			keyPairGenerator.initialize(KEY_SIZE, new SecureRandom());
			KeyPair keyPair = keyPairGenerator.generateKeyPair();
			return keyPair;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 加密
	 * 
	 * @param publicKey
	 *            公钥
	 * @param data
	 *            数据
	 * @return 加密后的数据
	 */
	public static byte[] encrypt(PublicKey publicKey, byte[] data) {
		Assert.notNull(publicKey);
		Assert.notNull(data);
		try {
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", PROVIDER);
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			return cipher.doFinal(data);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 加密
	 * 
	 * @param publicKey
	 *            公钥
	 * @param text
	 *            字符串
	 * 
	 * @return Base64编码字符串
	 */
	public static String encrypt(PublicKey publicKey, String text) {
		Assert.notNull(publicKey);
		Assert.notNull(text);
		byte[] data = encrypt(publicKey,text.getBytes());
		return data != null ? Base64.encodeBase64String(data) : null;
	}

	/**
	 * 加密
	 * 
	 * @param publicKey
	 *            公钥
	 * @param text
	 *            字符串
	 * 
	 * @return Base64编码字符串
	 */
	public static String hexEncrypt(PublicKey publicKey, String text) {
		Assert.notNull(publicKey);
		Assert.notNull(text);
		byte[] data = encrypt(publicKey,text.getBytes());
		return data != null ? Hex.encodeToString(data): null;
	}

	/**
	 * 解密
	 * 
	 * @param privateKey
	 *            私钥
	 * @param data
	 *            数据
	 * @return 解密后的数据
	 */
	public static byte[] decrypt(PrivateKey privateKey, byte[] data) {
		Assert.notNull(privateKey);
		Assert.notNull(data);
		try {
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", PROVIDER);
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			return cipher.doFinal(data);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 解密
	 * 
	 * @param privateKey
	 *            私钥
	 * @param text
	 *            Base64编码字符串
	 * @return 解密后的数据
	 */
	public static String decrypt(PrivateKey privateKey, String text) {
		Assert.notNull(privateKey);
		Assert.notNull(text);
		byte[] data = decrypt(privateKey, Base64.decodeBase64(text));
		return data != null ? new String(data) : null;
	}
	
	/**
	 * 解密
	 * 
	 * @param privateKey
	 *            私钥
	 * @param text
	 *            Base64编码字符串
	 * @return 解密后的数据
	 */
	public static String hexDecrypt(PrivateKey privateKey, String text) {
		Assert.notNull(privateKey);
		Assert.notNull(text);
		byte[] data = decrypt(privateKey, Hex.decode(text));
		return data != null ? new String(data) : null;
	}
	
    /** 
     * 使用模和指数生成RSA公钥 
     * 注意：【此代码用了默认补位方式，为RSA/None/PKCS1Padding，不同JDK默认的补位方式可能不同，如Android默认是RSA 
     * /None/NoPadding】 
     *  
     * @param modulus 
     *            模 
     * @param exponent 
     *            指数 
     * @return 
     */  
    public static RSAPublicKey getPublicKey(byte[] modulus, byte[] exponent) {
		try {
			BigInteger b1 = new BigInteger(modulus);
			BigInteger b2 = new BigInteger(exponent);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");  
            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(b1, b2);  
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);  
        } catch (Exception e) {  
            e.printStackTrace();  
            return null;  
        }  
    }  
  
    /** 
     * 使用模和指数生成RSA私钥 
     * 注意：【此代码用了默认补位方式，为RSA/None/PKCS1Padding，不同JDK默认的补位方式可能不同，如Android默认是RSA 
     * /None/NoPadding】 
     *  
     * @param modulus 
     *            模 
     * @param exponent 
     *            指数 
     * @return 
     */  
    public static RSAPrivateKey getPrivateKey(byte[] modulus, byte[] exponent) {  
        try {  
            BigInteger b1 = new BigInteger(modulus);  
            BigInteger b2 = new BigInteger(exponent);  
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");  
            RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(b1, b2);  
            return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);  
        } catch (Exception e) {  
            e.printStackTrace();  
            return null;  
        }  
    }  

	static class Tester {
		public static void main(String[] args) throws Exception {
			System.out.println("------------准备公私钥--------------");
			KeyPair keyPir = RSAUtils.generateKeyPair();
			//生成公钥和私钥
			RSAPublicKey publicKey = (RSAPublicKey) keyPir.getPublic();
			RSAPrivateKey privateKey = (RSAPrivateKey) keyPir.getPrivate();
			
			String pm = Hex.encodeToString(publicKey.getModulus().toByteArray());
			String pe = Hex.encodeToString(publicKey.getPublicExponent().toByteArray());
			System.out.println("公钥模："+ pm);
			System.out.println("公钥指数："+ pe);
			String sm = Hex.encodeToString(privateKey.getModulus().toByteArray());
			String se = Hex.encodeToString(privateKey.getPrivateExponent().toByteArray());
			System.out.println("私钥模："+ sm);
			System.out.println("私钥指数："+ se);
			
			System.out.println("------------原始公私钥加解密--------------");
			
			String ming1 = "lixinyu";
			System.out.println("加密前："+ ming1);
			
			//加密后的密文
			String mi1 = RSAUtils.hexEncrypt(publicKey,ming1);
			System.out.println("加密后："+ mi1);
			//解密后的明文
			ming1 = RSAUtils.hexDecrypt(privateKey,mi1);
			System.out.println("解密后："+ ming1);
			
			
			System.out.println("----------- hex后公私钥加解密--------------");
			RSAPublicKey publicKey1 = RSAUtils.getPublicKey(Hex.decode(pm),Hex.decode(pe));
		
			String ming2 = "lixinyu";
			System.out.println("加密前："+ ming2);
			//加密后的密文
			String mi3 = RSAUtils.hexEncrypt(publicKey1,ming2);
			System.out.println("加密后："+ mi3);

			RSAPrivateKey privateKey2 = RSAUtils.getPrivateKey(Hex.decode(sm),Hex.decode(se));
			//mi3 = "9ED8EC09B50324DF228EAEE1367D60D54BBFAD089DD21AEAC2C3D100C6FC395F92DECBD6F7059B65A25ADCB586370907E8B2E1BB8D4E4A0A7E8D804D32AFE219BEB8A2D62A33A7190561491CE17F7C242EA9DA4FAD6A27589E46AF7D0FFC5D7E103307D17C06FE3EA98E0AD6F452638F8DE27A3E11682471014C628CE79187C5";
			ming1 = RSAUtils.hexDecrypt(privateKey2,mi3);
			System.out.println("解密后："+ ming1);
			
			//加密后的密文
			String mi4 = "79D482974AEFF006F63034393E0BB9F0A5632E49A5303D4C369868D0425113B52A754EDF6E58AAD97C1D3BA5AB110E8024649BD5C09241744D9F5E007D2EDEB6CC87D90E712AA674C5C091152EA8B1EC85EA1A45EBBF989FC7FC092CB747411EA965F5215A7F6244FC34E2E7CAF523B4E79CADB32A5FFB88248376FC46BA3766";
			System.out.println("delphi加密后："+ mi4);

			RSAPrivateKey privateKey3 = RSAUtils.getPrivateKey(Hex.decode("53bba470e563a74144b7c3c04374451ecd56e3c8a855a7d49e095c4527d8ebc23905379fcdfdedc39dd5862ba2b0569c532145fb1a268a319089638dd231f5a9d998c932995dea3af9e4ddb1f11e1b85ced00458278d694070c8bbc48e718bfed0375ba1ff1e2bb01042e8bc052377bf86496189259c00cf9a6d746d492d0119"),Hex.decode("010001"));
			ming1 = RSAUtils.hexDecrypt(privateKey3,mi4);
			System.out.println("解密后："+ ming1);
			
			
		}
	}
}