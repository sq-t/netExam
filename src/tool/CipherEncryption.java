package tool;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


import sun.misc.BASE64Encoder;
 
public class CipherEncryption {
	@SuppressWarnings("static-access")
	public String encryption(String pwd) {
		return String.valueOf(this.getMD5(pwd));
	}
	
	//MD5����
	@SuppressWarnings("unused")
	private static byte[] encrypt(String msg) {
		try {
		    //����MessageDigest����
		    MessageDigest md5 = MessageDigest.getInstance("MD5");
		    
		    /*byte[] srcBytes = msg.getBytes();
		    
		    //����ժҪ
		    md5.update(srcBytes);*/
		    
		    //���hash���㡣�õ�result
		    byte[] resulBytes=md5.digest();
		    
		    return resulBytes;
	    } catch (NoSuchAlgorithmException e) {
	    	//handle exception
	    	e.printStackTrace();
	    }
	    return null;
    }

    public static String getMD5(String str){
        String newStr = null;
        try {
            // ȷ�����㷽��
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            BASE64Encoder base64e = new BASE64Encoder();
            // ���ܺ���ַ���
            newStr = base64e.encode(md5.digest(str.getBytes()));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return newStr;// 16λ�ļ���
    }


	/*
	public static void main(String[] args) {
		String code = String.valueOf(encrypt("123456"));
		System.out.println(code);
	}
	*/
}