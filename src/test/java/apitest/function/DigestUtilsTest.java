package apitest.function;

import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.Md5Crypt;

public class DigestUtilsTest {
	
	public static void main(String[] args) {
		try {
		  System.out.println(DigestUtils.md5Hex(("123"+System.nanoTime()).getBytes("UTF-8")));
		  System.out.println(DigestUtils.md5Hex(("123"+System.nanoTime()).getBytes("UTF-8")));
		  ShaTest("123");
		  ShaTest("123");
		  
		  //带盐
		  System.out.println(Md5Crypt.md5Crypt("123".getBytes()));
		  System.out.println(Md5Crypt.md5Crypt("123".getBytes()));
//		 String base64= encodeTest("我爱中国");
//		 decodeTest(base64);
		 
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
	}
	
	private static String ShaTest(String str) {
		try {
			System.out.println("SHA 编码后：" + new String(DigestUtils.sha1Hex(str.getBytes("UTF-8"))));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}
	
	private static String encodeTest(String str) {
		Base64 base64 = new Base64();
		try {
			str = base64.encodeToString(str.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		System.out.println("Base64 编码后：" + str);
		return str;
	}
	
	private static void decodeTest(String str) {
		Base64 base64 = new Base64();
		str = new String(Base64.decodeBase64(str));
		System.out.println("Base64 解码后：" + str);
	}

}
