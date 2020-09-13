
import java.io.IOException;

public class DesDecrypt {
	//����
	// ��ʼ56λ��Կ
	private String key = "";
	//����Կ
	public static char[][] keybox = new char[16][48];
	//����
	public String message = "";
	//����
	public String ciphertexts = ""; 
	
	//���캯��
	public DesDecrypt(String ciphertexts,String key){
		this.key = key;
		this.ciphertexts = ciphertexts;
	}
	
	//���ķֶΣ�ÿ�γ���λlength
	public static String[] ciphertextsplit(String ciphertext, int length) {
		//���ķֶ�ÿlengthλλһ��
		int len = ciphertext.length();
		int section = len/length;
		String[] ciphertextbox = new String[section];
		//��ÿ��lengthλ��Ƭ
		for(int i=0,j=0; i<section; i++,j+=length) {
			ciphertextbox[i] = ciphertext.substring(j, j+length);
		}
		return ciphertextbox;
	}
	
	//ASCIIתchar
	public static char asciitochar(String s) {
		char c = 0;
		char[] S = s.toCharArray();
		c = (char)((S[0]-'0')*128+(S[1]-'0')*64+(S[2]-'0')*32+(S[3]-'0')*16+(S[4]-'0')*8+(S[5]-'0')*4+(S[6]-'0')*2+(S[7]-'0')*1);
		return c;	
	}
	
	//���Ⱥ���
	public void decrypt() throws IOException {
		
		char[] ciphertextIP_1 = new char[64];
		char[] massages = new char[64];
		char[] ciphertextL = new char[32];
		char[] ciphertextR = new char[32];
		String[] keys = null;
		String messagestr = "";
		String ciphertextf = null;
		
		//��������ʵ��,�����䷽��
		DesEncrypt desencrypt = new DesEncrypt();
		
		//�Զ����ʼ������Ҫ����д���
		desencrypt.init();
		
		//����Կ���ȴ���,���ڳ���64λ�������ֻ����ǰ64λ������ʱ����0
		keys = DesEncrypt.messagesplit(key);
		
		// ����16��48λ����Կ
		keybox = desencrypt.createkey(keys[0].toCharArray());
		
		//�����ķֶ�
		String[] ciphertextbox = ciphertextsplit(ciphertexts,64);
		
		
		for(String c:ciphertextbox) {
			
			//�����û�
			DesEncrypt.replacement(c.toCharArray(), ciphertextIP_1, desencrypt.IP);
			
			//��Ƭ
			DesEncrypt.split(ciphertextL,ciphertextR,ciphertextIP_1);
			
			//����F����
			DesEncrypt.Feistel(ciphertextL, ciphertextR, keybox, 1);
		
			//������ƴ�Ӻ����ʼ�û�IP_1 64->64
			ciphertextf = String.valueOf(ciphertextR) + String.valueOf(ciphertextL);
			DesEncrypt.replacement(ciphertextf.toCharArray(),massages,desencrypt.IP_1);
			
			messagestr += String.valueOf(massages);
		}
		//תΪ�ַ�,�ȷֳ�8λ������String
		String[] messagebox = ciphertextsplit(messagestr,8);
		StringBuilder m = new StringBuilder(messagebox.length);
		for(String w:messagebox) {
			if(w.equals("00000000")) break;
			m.append(asciitochar(w));
		}
		message = m.toString();
	}

}
