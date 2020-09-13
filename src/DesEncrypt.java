
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class DesEncrypt {
	// ��ʼ56λ��Կ
	private String key = "";
	//����Կ
	public char[][] keybox = new char[16][48];
	//����
	private String message = "";
	//����
	public String ciphertexts = "";
	
	//��ʼ�û�64->68
	public int[] IP = new int[64];
	// �û�1ʹ�ã�8*7,64->56
	public int[] PC_1 = new int[56];
	// ����Կ������λ��
	static int[] RoundTable = new int[16];
	// �û���2��8*6,56->48
	static int[] PC_2 = new int[48];
	//�����E,32->48
	static int[] E = new int[48];
	//S���û�
	static int[][][] S = new int[8][4][16];
	//P�û� 32->32
	static int[] P = new int[32];
	//���ʼ�û�
	public int[] IP_1 = new int[64];
	
	//�޲������췽��
	public DesEncrypt() {
		
	}
	
	//�вι��췽��
	public DesEncrypt(String message,String keystr){
		this.key = keystr;
		this.message = message;
	}

	//�����亯��
	@SuppressWarnings("resource")
	public static void tableprepare(String filename,int[] table) throws IOException {
		String line = "";
		int len = table.length;
		BufferedReader reader  = new BufferedReader(new FileReader(filename));
		line = reader.readLine();
		String[] words = line.split(", ");
		for(int i=0; i<len;i++) {
			table[i] = Integer.parseInt(words[i]);
		}
	}

	//ʮ����ת������
	public static String decimaltoBinary(int t,int flag) {
		String numstr="";
		while(t>0) {
			 int res = t%2;
			 numstr = res+numstr;
			 t=t/2;
		 }
		//תΪ�����ƺ�����λǰ��0
		 if(numstr.length()<4 && flag == 1) {
			 switch(numstr.length()) {
			 case 0: numstr ="0000"; break;
			 case 1: numstr = "000" + numstr; break;
			 case 2: numstr = "00" + numstr; break;
			 case 3: numstr = "0" +numstr; break;
			 }	 
		 }
		return numstr;
	}
	
	// �û�����
	public static void replacement(char[] oldchar, char[] newchar, int[] table) {
		for (int i = 0; i < newchar.length; i++) {
			newchar[i] = oldchar[table[i] - 1];
		}
	}

	// ����������
	public static void split(char[] left, char[] right, char[] origin) {
		int len = origin.length;
		for (int i = 0; i < len; i++) {
			if (i < len / 2) {
				left[i] = origin[i];
				// System.out.print(left[i]);
			} else {
				right[i - len / 2] = origin[i];
				// System.out.print(right[i-len/2]);
			}
		}
	}

	// ����Կ����ʱ����λ
	public static String desshift(char[] C, char[] D, int round) {
		int rotations = RoundTable[round];
		int len = C.length;
		String tmp = null;
		char[] tmp1 = new char[len];
		char[] tmp2 = new char[len];
		for (int i = 0; i < len; i++) {
			// System.out.printf("��λλ��Ϊ%d,��%d��tmpд��C�е�%dλ\n",rotations,i,(i+rotations)%len);
			tmp1[i] = C[(i + rotations) % len];
			tmp2[i] = D[(i + rotations) % len];
		}
		// ��λ�󱣴���λ�������ѭ���´�ƴ��
		for (int i = 0; i < C.length; i++) {
			C[i] = tmp1[i];
			D[i] = tmp2[i];
		}
		// ��λ��ƴ��Ϊtmp
		tmp = String.valueOf(tmp1) + String.valueOf(tmp2);
		return tmp;
	}

	// ����Կ����,����ֵΪ16*��28+28���Ķ�ά����
	public char[][] createkey(char[] key0) {
		char[][] keytable = new char[16][48];
		char[] key1 = new char[56];
		// �û�1,��PC_1,��56λkey0
		replacement(key0, key1, PC_1);
		// ��Ϊ��������
		char[] C = new char[28];
		char[] D = new char[28];
		split(C, D, key1);
		// �õ�C0��D0��
		String temp = null;
		for (int i = 0; i < 16; i++) {
			// ��λ����
			temp = desshift(C, D, i);
			replacement(temp.toCharArray(), keytable[i], PC_2);
		}
		return keytable;
	}

	//charת��ΪASCII
	public static String chartoascii(char c) {
		String binary = "";
		int decimal = (int)c;
		//ʮ����ת��Ϊ�����ƣ�ǰ�油1��0��2��0
		binary = decimaltoBinary(decimal,0);
		if(decimal >= 32&&decimal <= 63) {
			binary = "00" + binary;
		}else if(decimal >= 64&&decimal <= 128){
			binary = "0" + binary;
		}
		return binary;
	}
	
	//�����и�ֶκ�����ÿ��64λ
	public static String[] messagesplit(String messages) {
		
		int len = messages.length();
		int section = len/8;
		if ((len % 8) != 0) {
			section = section + 1;
		}
		String[] messagebox = new String[section];
		char[] words = messages.toCharArray();
		StringBuilder m = new StringBuilder(64);
		String wordstr = "";
		for(int i=0 ;i<8*section ;i++) {
			
			//�ǲ�λ����
			if(i <= len-1) {
				//��words[i]ת��,���8λ�����ƴ���
				wordstr = chartoascii(words[i]);
				//��8λ�����ƴ���ƴ��
				m.append(wordstr);
				//ÿת��8���ַ�,����һλ������
				if((i+1)%8==0) {
					messagebox[i/8] = m.toString();
					//�����Ҫ��m���
					m.delete(0, 64);
				}
			}else{
				wordstr = "00000000";
				m.append(wordstr);
				if((i+1)%8==0) {
					messagebox[i/8] = m.toString();
					m.delete(0, 64);
					}
			}
		}
		return messagebox;
	}

	//�������
	public static char[] xor(char[] messageRE, char[] k, char[] messageRExor) {
		int len = messageRE.length;
		for(int i=0;i<len;i++) {
			messageRExor[i] = String.valueOf(((int)(messageRE[i])^(int)(k[i]))).toCharArray()[0];
			//System.out.printf("%c^%c=%c\n",messageRE[i],k[i],messageRExor[i]);
		}
		return messageRExor;
	}
	
	//S�д���
	public static void replacementS(char[]oldmessage ,char[]newmessage) {
		
		//һ���и�Ϊ����
		char[] half1 = new char[24];
		char[] half2 = new char[24];
		split(half1,half2,oldmessage);
		
		//�����и�Ϊ�Ķ�
		char[] quarter1 = new char[12];
		char[] quarter2 = new char[12];
		char[] quarter3 = new char[12];
		char[] quarter4 = new char[12];
		split(quarter1,quarter2,half1);
		split(quarter3,quarter4,half2);
		
		//�Ķ��и�Ϊ�˶�
		char[][] messagepiece = new char[8][6];
		split(messagepiece[0],messagepiece[1],quarter1);
		split(messagepiece[2],messagepiece[3],quarter2);
		split(messagepiece[4],messagepiece[5],quarter3);
		split(messagepiece[6],messagepiece[7],quarter4);
		
		String numpiece = "";
		for(int i=0;i<8;i++) {
			 //������תʮ����
			 int x = (messagepiece[i][0]-'0')*2 + (messagepiece[i][5]-'0');
			 int y = (messagepiece[i][1]-'0')*8 + (messagepiece[i][2]-'0')*4 + (messagepiece[i][3]-'0')*2 + (messagepiece[i][4]-'0');
			 int t = S[i][x][y];
			 //ʮ����ת������
			 String numstr="";
			 numstr = decimaltoBinary(t,1);
			 //��þ���S��ת�����Ӵ�
			 numpiece += numstr;
		}
		System.arraycopy(numpiece.toCharArray(), 0, newmessage, 0, newmessage.length);
	}
	
	//F������16��ѭ��
	public static void Feistel(char[] messageL, char[] messageR, char[][] keybox,int flag) {
		char[] temp = new char[32];
		char[] messageRExor = new char[48];
		char[] messageRExorS = new char[32];
		char[] messageRExorSP = new char[32];
		char[] k = new char[48];
		
		//16��ѭ��
		for (int i = 0; i < 16; i++) {
			temp = messageR.clone(); // ֵ���ݷ�ʽ��ֵ
			if(flag==0) {
				k = keybox[i]; // �������Կ
			}else if(flag==1) {
				k = keybox[(15-i)%16];// �������Կ������
			}
			char[] messageRE = new char[48];
			
			// �Ҳ���������32->48,ʹ�ñ�E,���48λmessageRE
			replacement(messageR, messageRE, E);
			
			xor(messageRE,k,messageRExor);// ��k�������48->48
			
			// S�д���48->32
			replacementS(messageRExor,messageRExorS);
			
			// P�û�32->32
			replacement(messageRExorS,messageRExorSP,P);
			
			// �����������32->32,ֵ���ݸ�ֵ���Ҳ�
			xor(messageRExorSP,messageL,messageR);
			
			//���Ҳ���ֵ����
			System.arraycopy(temp, 0, messageL, 0, 32);
		}
	}

	//S����д
	public static void Stableprepare(String filename,int[][][] table) throws IOException {
		String line = "";
		@SuppressWarnings("resource")
		BufferedReader reader  = new BufferedReader(new FileReader(filename));
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 4; j++) {
				int len = table[i][j].length;
				line = reader.readLine();
				String[] words = line.split(", ");
				for(int n=0; n<len; n++) {
					table[i][j][n] = Integer.parseInt(words[n]);
				}
			}
		}
	}
	
	//��ʼ�������
	public void init() throws IOException {
		// �����
		tableprepare("/home/wtc/eclipse-workspace/Vserver/src/E.txt", E);
		tableprepare("/home/wtc/eclipse-workspace/Vserver/src/IP.txt", IP);
		tableprepare("/home/wtc/eclipse-workspace/Vserver/src/IP_1.txt", IP_1);
		tableprepare("/home/wtc/eclipse-workspace/Vserver/src/P.txt", P);
		tableprepare("/home/wtc/eclipse-workspace/Vserver/src/PC_1.txt", PC_1);
		tableprepare("/home/wtc/eclipse-workspace/Vserver/src/PC_2.txt", PC_2);
		tableprepare("/home/wtc/eclipse-workspace/Vserver/src/RoundTable.txt", RoundTable);
		Stableprepare("/home/wtc/eclipse-workspace/Vserver/src/S.txt",S);
	}
	
	//���ܹ��̵��Ⱥ���
	public void encrypt() throws IOException {
		
		char[] ciphertext = new char[64];
		char[] messageIP = new char[64];
		char[] messageL = new char[32];
		char[] messageR = new char[32];
		String[] keys = null;
		String messagef = null;
	
		init();

		//����Կ���ȴ���,���ڳ���64λ�������ֻ����ǰ64λ������ʱ����0
		keys = messagesplit(key);		
		
		// ����16��48λ����Կ
		keybox = createkey(keys[0].toCharArray());
		
		//�������и�ֶ�
		String[] messagebox = messagesplit(message);
		
		//��ÿһ�����ļ���
		for (String m : messagebox) {
			//��ʼ�û� 64->64
			replacement(m.toCharArray(),messageIP,IP);
			
			//��Ϊ��������
			split(messageL,messageR,messageIP);
			
			//F����16��ѭ������
			Feistel(messageL,messageR, keybox, 0);
			
			//������ƴ�Ӻ����ʼ�û�IP_1 64->64
			messagef = String.valueOf(messageR) + String.valueOf(messageL);
			replacement(messagef.toCharArray(),ciphertext,IP_1);
			//����ƴ��
			ciphertexts += String.valueOf(ciphertext);
		}
	}
}