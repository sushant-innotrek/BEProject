package www.SRTS.in.Encrypt;

import java.util.Date;


public class Decryption {
	private String keys[];
	public Decryption() {
		keys = new String[]{
				"MUMSRTS","YOYKIMK","WCWUQYU","SESOGWO","KIKCMSC","UQUEYKE","OGOIWUI",
				"CMCQSOQ","EYEGKCG","IWIMUEM","QSQYOIY","GKGWCQW"
		};
	}
	public String decrypt(String Cipher){
		String key = keys[new Date().getMinutes() /5];
		//System.out.println(key);
		return VigenereCipher.decrypt(Cipher, key);
	}
}
