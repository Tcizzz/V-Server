
import java.io.IOException;

public class Main {
	public static void main(String[] args) throws IOException {
		//����
		String message = "mdkdusjsjjsd";
		//��Կ��ֻȡǰ8���ֽ�
		String key = "defr";
		DesEncrypt desencrypt = new DesEncrypt(message,key);
		desencrypt.encrypt();
		
		//�������Կ
		System.out.println("����Կ���ɵ�����Կ�ǣ�");
		int i = 1;
		for(char[] KEY:desencrypt.keybox) {
			System.out.printf("����Կkey%d��",i++);
			System.out.println(KEY);
		}
		System.out.println("������:");
		System.out.println(desencrypt.ciphertexts);
		
		
		String ciphertext = desencrypt.ciphertexts;
		DesDecrypt decrypt = new DesDecrypt(ciphertext,key);
		decrypt.decrypt();
		System.out.println("������:");
		System.out.println();
	}
}
/*
 */
