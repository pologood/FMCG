package net.wit.util;



import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.SecretKeySpec;

import com.sun.org.apache.xml.internal.security.utils.Base64;

/**
 * 
 * @author think
 *
 */
public class DesUtils {

	private final static String DES = "DES";
	//private final static String CIPHER_ALGORITHM = "DES";
	private final static String CIPHER_ALGORITHM = "DES";
	
	/**
	 * ����String��������,String�������
	 * 
	 * @param strMing
	 *            String����
	 * @return String����
	 * @throws DesException
	 */
	public static String getEncString(String strMing, byte[] byteKey) {
		byte[] byteMi = null;
		byte[] byteMing = null;
		byte[] buf=null;
		try {
			buf = strMing.getBytes("utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int len = 8 - buf.length % 8;
		byteMing = new byte[buf.length + len];
		System.arraycopy(buf, 0, byteMing, 0, buf.length);
		byteMi = encryptDes(byteMing, byteKey);
		return Base64.encode(byteMi);
	}
	

	/**
	 * ���� ��String��������,String�������
	 * 
	 * @param strMi
	 *            String����
	 * @return String����
	 */
	public static String getDesString(String strMi, byte[] byteKey) {
		byte[] bytebase64 = null;
		byte[] byteMi = null;
		String strMing = null;
		try {
			byteMi = strMi.getBytes();
			bytebase64 = Base64.decode(byteMi);
			strMing = new String(decryptDes(bytebase64, byteKey));
		} catch (Exception e) {
		}
		return strMing.trim();
	}

	/**
	 * ����
	 * 
	 * @param src
	 *            ���Դ
	 * @param key
	 *            ��Կ�����ȱ�����8�ı���
	 * @return ���ؼ��ܺ�����
	 * @throws DesException
	 */
	public static byte[] encryptDes(byte[] src, byte[] key) {
		// DES�㷨Ҫ����һ�������ε������Դ
		SecureRandom sr = new SecureRandom();
		try {
			// ��ԭʼ�ܳ���ݴ���DESKeySpec����
			DESKeySpec dks = new DESKeySpec(key);
			// ����һ���ܳ׹�����Ȼ�������DESKeySpecת����
			// һ��SecretKey����
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
			SecretKey securekey = keyFactory.generateSecret(dks);
			// Cipher����ʵ����ɼ��ܲ���,NoPaddingΪ��䷽ʽ Ĭ��ΪPKCS5Padding
			Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
			// ���ܳ׳�ʼ��Cipher����
			cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);
			// ���ڣ���ȡ��ݲ�����
			// ��ʽִ�м��ܲ���
			return cipher.doFinal(src);
		} catch (NoSuchAlgorithmException e) {
			// LOG.error("�������", e);
		} catch (InvalidKeyException e) {
			// LOG.error("��Чkey����", e);
		} catch (InvalidKeySpecException e) {
			// LOG.error("��Чkey����", e);
		} catch (NoSuchPaddingException e) {
			// LOG.error("������", e);
		} catch (IllegalBlockSizeException e) {
			// LOG.error("�Ƿ���ݿ�", e);
		} catch (BadPaddingException e) {
			// LOG.error("��������", e);
		}
		return null;
	}

	/**
	 * �����Կ
	 * 
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static byte[] initKey() throws NoSuchAlgorithmException {
		KeyGenerator kg = KeyGenerator.getInstance(DES);
		kg.init(56);
		SecretKey secretKey = kg.generateKey();
		return secretKey.getEncoded();
	}

	/**
	 * ����
	 * 
	 * @param src
	 *            ���Դ
	 * @param key
	 *            ��Կ�����ȱ�����8�ı���
	 * @return ���ؽ��ܺ��ԭʼ���
	 * @throws DesException
	 * @throws Exception
	 */
	public static byte[] decryptDes(byte[] src, byte[] key) {
		// DES�㷨Ҫ����һ�������ε������Դ
		SecureRandom sr = new SecureRandom();
		try {
			// ��ԭʼ�ܳ���ݴ���һ��DESKeySpec����
			DESKeySpec dks = new DESKeySpec(key);
			// ����һ���ܳ׹�����Ȼ�������DESKeySpec����ת����
			// һ��SecretKey����
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
			SecretKey securekey = keyFactory.generateSecret(dks);
			// Cipher����ʵ����ɽ��ܲ���
			Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
			// ���ܳ׳�ʼ��Cipher����
			cipher.init(Cipher.DECRYPT_MODE, securekey, sr);
			// ���ڣ���ȡ��ݲ�����
			// ��ʽִ�н��ܲ���
			return cipher.doFinal(src);
		} catch (NoSuchAlgorithmException e) {
		} catch (InvalidKeyException e) {
		} catch (InvalidKeySpecException e) {
		} catch (NoSuchPaddingException e) {
		} catch (IllegalBlockSizeException e) {
		} catch (BadPaddingException e) {
		}
		return null;
	}

	public static byte[] encrypt(byte[] src, byte[] key) {
		if (key.length == 8) {
			//DES
			return encryptDes(src, key);
		} else if (key.length == 16) {
			//2DES
			byte[] key1= new byte[8];
			System.arraycopy(key, 0, key1, 0, 8);
			byte[] key2= new byte[8];
			System.arraycopy(key, 8, key2, 0, 8);
			src = encryptDes(src, key1);
			src = decryptDes(src, key2);
			return encryptDes(src, key1);
		} else if (key.length == 24){
			// 3DES
			byte[] key1= new byte[8];
			System.arraycopy(key, 0, key1, 0, 8);
			byte[] key2= new byte[8];
			System.arraycopy(key, 8, key2, 0, 8);
			byte[] key3= new byte[8];
			System.arraycopy(key, 16, key3, 0, 8);
			src = encryptDes(src, key1);
			src = decryptDes(src, key2);
			return encryptDes(src, key3);
		} else {
		}
		return null;		
	}
	
	public static byte[] decrypt(byte[] src, byte[] key) {
		if (key.length == 8) {
			//DES
			return decryptDes(src, key);
		} else if (key.length == 16) {
			//2DES
			byte[] key1= new byte[8];
			System.arraycopy(key, 0, key1, 0, 8);
			byte[] key2= new byte[8];
			System.arraycopy(key, 8, key2, 0, 8);
			src = decryptDes(src, key1);
			src = encryptDes(src, key2);
			return decryptDes(src, key1);
		} else if (key.length == 24){
			// 3DES
			byte[] key1= new byte[8];
			System.arraycopy(key, 0, key1, 0, 8);
			byte[] key2= new byte[8];
			System.arraycopy(key, 8, key2, 0, 8);
			byte[] key3= new byte[8];
			System.arraycopy(key, 16, key3, 0, 8);
			src = decryptDes(src, key3);
			src = encryptDes(src, key2);
			return decryptDes(src, key1);
		} else {
		}
		return null;
	}
	
	public static void main(String[] args) throws Exception {
		
		String data = "1ZEKHoBnhHp7sKI2WHLSSl0gYEkYsjUsqqcEJWBnddBoAjvfRjqTNDJp0R+0C1T/iMIq13HKynT2nNoD3vR1GMgCYfX+PRsndAryDfdsdkZQvUCOUspb9pa5agh7jtxiOE37ng+QbjNLtVTwRuCDvwt8mESVyzhIDvRmE9Y0pKo=";
		System.out.println(DesUtils.getDesString(data,"cloudskycloudskycloudskycloudsky".getBytes()));
		/*TestBean tbean = new TestBean();
		tbean.setMobileTime("153333");
		tbean.setCardSerNo("001");
		String test=ApiUtil.getJsonStrFromNest(tbean);*/
		//System.out.println(DesUtils.getEncString(test, "32kldjfdfsfr53534dfd54gbfdhsdfrt".getBytes()));
		//System.out.println(DesUtils.getDesString("levK82Zl+V73kfcchtzNndIAAB4AWBt7Xtb7nZFWGqUKjIQ+E3cdwcgr9BbSD3xCpPJG/WarqDDHGYkuuAYiSeg1EVl4R/85CTlDqE3HVrCZBZ50w7Qppw==", "8ba960f115124b2abb4bd70bb7a31356".getBytes()));
		//System.out.println(MD5.GetMD5Code("1873333333320150330"));
		//String curPwd = MD5.GetMD5Code("123456");
		//String newPwd = MD5.GetMD5Code("111111");
		//System.out.println(MD5.GetMD5Code("18912771633"+curPwd+newPwd));
		//System.out.println(MD5.GetMD5Code("18912771633123456111111"));
		/*
		BankAccBindingBean bean = new BankAccBindingBean();
		bean.setUserId("800000001");
		bean.setBankAccName(URLEncoder.encode("�й�������԰�����","gb2312"));
		bean.setBankAcc("622325721458932123");
		bean.setBankName(URLEncoder.encode("����","gb2312"));
		bean.setBankType("1");
		bean.setVerifyCode("39a01f7a3ace06b1ee580ca2f340040b");
		String msgJson =ApiUtil.getJsonStrFromNest(bean);
		System.out.println(msgJson);
	//	String enc = DesUtils.getEncString(msgJson, "39a69ac177b44fd7ae80ea655144f7d2".getBytes());
		
		System.out.println("md5:"+MD5.GetMD5Code("1"));
		
		System.out.println("20150318:"+DesUtils.getEncString("", "14c46c4c61f046c78f828e9b4d670415".getBytes()));
		
		String des = DesUtils.getDesString("HUR4BTBBUTLfqE3291UXg4xMJWc/n0ntRiyuu8jbFgEUm4PzAk/H6uscwfoBAqDo", "14c46c4c61f046c78f828e9b4d670415".getBytes());
		System.out.println("2015/3/24:"+des);
		
		System.out.println(DesUtils.getDesString(enc, "6594ea156fab4ee9a6ead31512a53237".getBytes()));
		BankAccBindingBean withdrawBean=(BankAccBindingBean)JSONObject.toBean(JSONObject.fromObject(des),BankAccBindingBean.class);
		System.out.println(URLDecoder.decode(withdrawBean.getBankAccName(),"gb2312"));
		//System.out.println(MD5.GetMD5Code("123456"));
		System.out.println("------------------------");
		String tel = ApiUtil.getPropertyValue("bankAccBinding.info");
		BankAccBindingBean bankAccBindingBean =(BankAccBindingBean) JSONObject.toBean(JSONObject.fromObject(tel),BankAccBindingBean.class);
		System.out.println(MD5.GetMD5Code(bankAccBindingBean.getAllInfo()));
		
		System.out.println();
		String tel = ApiUtil.getPropertyValue("transInfoSearch.info");
		TransInfoSearchBean assb =(TransInfoSearchBean) JSONObject.toBean(JSONObject.fromObject(tel),TransInfoSearchBean.class);
		System.out.println(MD5.GetMD5Code(assb.getAllInfo()));
		
		System.out.println(MD5.GetMD5Code(""));
		System.out.println("*****************************");
		
		String str = ApiUtil.getPropertyValue("accountInfo.info");
		String enString = getEncString(str,"45e7a946bd6b4485a2a6ec872e312300".getBytes());
		System.out.println(enString);
		System.out.println("**************account***************");
		//String str = ApiUtil.getPropertyValue("login.info.req");
		//���룬����Ҫ��8�ı���
		//��̨��ݿ��ȡ��Կ��ÿ�ε�¼��Ҫ���£���Կ��ɹ���32λuuid��
		//String uuid = UUID.randomUUID().toString().trim().replaceAll("-", "");    
		//String password = "12123412311982128121291212125406"; 
		String entryString = getEncString(str,"cloudsky".getBytes());
		System.out.println(entryString);
		System.out.println("-------------login----------");
		System.out.println(URLDecoder.decode("%E4%B8%AD%E5%9B%BD%E5%B7%A5%E5%95%86%E9%93%B6%E8%A1%8C%E5%9B%AD%E5%8C%BA%E5%88%86%E8%A1%8C","utf-8"));
		System.out.println(MD5.GetMD5Code("8000000016223257214589321235byg5LiJ\n5Lit5Zu95bel5ZWG6ZO26KGM5Zut5Yy65YiG6KGM\n1"));
		
		*//**
		 * ���Է��ر���
		 * *//*
		String desStr="qiKep0k6mmmgCrea8M8TbbdE5wonWCLxwfh+WzkWMAM9WbdwDa5oRfS89yLlznC6qw6Dg1aMFQ1Pq+pUrRh+aq9R2hGx89eiMJRkeZZJvsOWM02tATwqYYtw9G+FoY2TvyXZauMy8is4IAcIf0lqIh7R/R/zLF4Uw7usldyP3RTq9yVNdb4H8KNw4+JF6JtCX0tNz3JJZXwhHZez+IwQDpTj2vi9rXtqfhFpx8G5lOezXE4MDghj0kEsiehidGhWGgp+nKWjF0lhaSZXD2Jqq+JnEdi/EBv4ugWt6qM1N28J3MYg5FTMTsK+nz5fmEs8OmUERrAImd9fQLsEQ5WOlctQCJeHX6+yMJRkeZZJvsNO5O+jKpKkxGvFuLifGGJa>";
		
		System.out.println(getDesString(desStr, "cloudsky".getBytes()));
		
		//String desStr = "pMMSNaqAIry1Us/amzC9pd9eZI/b6+DfyYharPC4V1dU2VL968fTzoaor3Dc7NjQNRjBGL7C+QvhxBPCVQwgIPfWBui+CYRjKS1OTZfHjsYKg46gm7z1dX/EYssVE04V34uJPxos6Oaviy6aQrtSrd5ujwEpm5TJGgH0wQ0240vr1Fzv5QVt7lKrStU4uRDOdMCy6T8Q0yzXppLpJC+MnomZ4Df4wIld31Ka4yUYQMEX8TRUO94KIyKWRzxc88sVzUNgSJIKCIKgd4U8e6nftzSc7xWuTFiWzW85trxRGPZlXqYoz2JYX7wY5t7cmj4iT/2zoKKrHeF4zItpa+6W1kCCalgAYIyHrWqItPo3gz0=";
		System.out.println(getDesString(entryString,"cloudsky".getBytes()));
		System.out.println(getEncString(entryString, "cloudsky".getBytes()));*/
		//{"userId":"123456}
	}
	
	/// <summary>  
	  
    /// 3des����  
  
    /// </summary>  
  
    /// <param name="value">������ַ�</param>  
  
    /// <param name="key">ԭʼ��Կ�ַ�</param>  
  
    /// <returns></returns>  
  
    public static String Decrypt3DES(String value, String key) throws Exception {  
  
        byte[] b = decryptMode(GetKeyBytes(key), Base64.decode(value));  
  
        return new String(b);  
  
    }  
  
   
  
    /// <summary>  
  
    /// 3des����  
  
    /// </summary>  
  
    /// <param name="value">������ַ�</param>  
  
    /// <param name="strKey">ԭʼ��Կ�ַ�</param>  
  
    /// <returns></returns>  
  
    public static String Encrypt3DES(String value, String key) throws Exception {  
  
        String str = byte2Base64(encryptMode(GetKeyBytes(key), value.getBytes()));  
  
        return str;  
  
    }  
  
   
  
    //����24λ��������byteֵ,���ȶ�ԭʼ��Կ��MD5��hashֵ������ǰ8λ��ݶ�Ӧ��ȫ��8λ  
  
    public static byte[] GetKeyBytes(String strKey) throws Exception {  
  
        if (null == strKey || strKey.length() < 1)  
  
            throw new Exception("key is null or empty!");  
  
   
  
        java.security.MessageDigest alg = java.security.MessageDigest.getInstance("MD5");  
  
        alg.update(strKey.getBytes());  
  
        byte[] bkey = alg.digest();  
  
        System.out.println("md5key.length=" + bkey.length);  
  
        System.out.println("md5key=" + byte2hex(bkey));  
  
        int start = bkey.length;  
  
        byte[] bkey24 = new byte[24];  
  
        for (int i = 0; i < start; i++) {  
  
            bkey24[i] = bkey[i];  
  
        }  
  
        for (int i = start; i < 24; i++) {//Ϊ����.net16λkey����  
  
            bkey24[i] = bkey[i - start];  
  
        }  
  
   
  
        System.out.println("byte24key.length=" + bkey24.length);  
  
        System.out.println("byte24key=" + byte2hex(bkey24));  
  
        return bkey24;  
  
    }  
  
   
  
    private static final String Algorithm = "DESede"; //���� �����㷨,���� DES,DESede,Blowfish         
  
   
  
    //keybyteΪ������Կ������Ϊ24�ֽ�  
  
    //srcΪ�����ܵ���ݻ�����Դ��    
  
    public static byte[] encryptMode(byte[] keybyte, byte[] src) {  
  
        try {  
  
            //�����Կ  
  
            SecretKey deskey = new SecretKeySpec(keybyte, Algorithm); //����   
  
            Cipher c1 = Cipher.getInstance(Algorithm);  
  
            c1.init(Cipher.ENCRYPT_MODE, deskey);  
  
            return c1.doFinal(src);  
  
       } catch (java.security.NoSuchAlgorithmException e1) {  
  
            e1.printStackTrace();  
  
        } catch (javax.crypto.NoSuchPaddingException e2) {  
  
            e2.printStackTrace();  
  
        } catch (java.lang.Exception e3) {  
  
            e3.printStackTrace();  
  
        }  
  
        return null;  
  
    }  
  
   
  
    //keybyteΪ������Կ������Ϊ24�ֽ�    
  
    //srcΪ���ܺ�Ļ�����  
  
    public static byte[] decryptMode(byte[] keybyte, byte[] src) {  
  
        try { //�����Կ     
  
            SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);  
  
            //����       
  
            Cipher c1 = Cipher.getInstance(Algorithm);  
  
            c1.init(Cipher.DECRYPT_MODE, deskey);  
  
            return c1.doFinal(src);  
  
        } catch (java.security.NoSuchAlgorithmException e1) {  
  
            e1.printStackTrace();  
  
        } catch (javax.crypto.NoSuchPaddingException e2) {  
  
            e2.printStackTrace();  
  
        } catch (java.lang.Exception e3) {  
  
            e3.printStackTrace();  
  
        }  
  
        return null;  
  
    }  
  
   
  
    //ת����base64����  
  
    public static String byte2Base64(byte[] b) {  
  
        return Base64.encode(b);  
  
    }  
  
   
  
    //ת����ʮ������ַ�    
  
    public static String byte2hex(byte[] b) {  
  
        String hs = "";  
  
        String stmp = "";  
  
        for (int n = 0; n < b.length; n++) {  
  
            stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));  
  
            if (stmp.length() == 1)  
  
                hs = hs + "0" + stmp;  
  
            else  
  
                hs = hs + stmp;  
  
            if (n < b.length - 1)  
  
                hs = hs + ":";  
  
        }  
  
        return hs.toUpperCase();  
  
    }  
	
	
}
