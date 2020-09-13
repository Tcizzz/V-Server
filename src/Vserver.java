import java.util.HashMap;
import java.io.IOException;
import java.lang.String;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class Vserver {

	static HashMap<String, String> Sites = new HashMap<String, String>(); // 存放报文中各类信息的hashmap
	static String IDv = "1";
	public String Kcv=null;

	// 解密用户发送来的密文
	public String DesDecrypt(String ciphertexts, String key) throws IOException {
		String Message = null;
		String Flag = ciphertexts.substring(0, 1); // 截取出操作指令;
		String secondTicketv = ciphertexts.substring(1, 897); // 截取出加密的Ticketv
		String secondAuthen = ciphertexts.substring(897); // 截取出加密的Authen
		// 第一次解密用户报文，得到第一位为操作指令，后续内容为加密后的Ticketv以及Authencation
		//DesDecrypt decrypt = new DesDecrypt(ciphertexts, key);
		//decrypt.decrypt();
		//Message = decrypt.message;
		//String secondTicketv = Message.substring(1, 897); // 截取出加密的Ticketv
		//String secondAuthen = Message.substring(897); // 截取出加密的Authen
		// String secondciphertexts = Message.substring(1); //
		// 截取出加密后的Ticketv以及Authencation
		// 二次解密Ticketv
		DesDecrypt decryptTicketv = new DesDecrypt(secondTicketv, key);
		decryptTicketv.decrypt();
		String Ticketv = decryptTicketv.message;
		System.out.println("Ticketv:"+Ticketv);
		Kcv=Ticketv.substring(0,64);
		// 二次解密Authen
		DesDecrypt decryptAuthen = new DesDecrypt(secondAuthen, Kcv);
		decryptAuthen.decrypt();
		String Authen = decryptAuthen.message;
		System.out.println("密文:" + ciphertexts.substring(1));
		Message = null;
		Message = Flag + Ticketv + Authen;
		System.out.println("解密结果:" + Message);
		return Message;

	}

	public void SplitMessage(String Message) { // 分割报文中的各类信息

		String Flag = Message.substring(0, 1);
		String Kcv = Message.substring(1, 65);
		String IDcinTicketv = Message.substring(65, 74);
		String ADsinTicketv = Message.substring(74, 86);
		String IDvinTicketv = Message.substring(86, 87);
		String TS4 = Message.substring(87, 100);
		String LT4 = Message.substring(100, 113);
		String IDcinAuthen = Message.substring(113, 122);
		String ADsinAuthen = Message.substring(122, 134);
		String TS5 = Message.substring(134, 147);
		Sites.put("Flag", Flag);
		Sites.put("Kcv", Kcv);
		Sites.put("IDcinTicketv", IDcinTicketv);
		Sites.put("ADsinTicketv", ADsinTicketv);
		Sites.put("IDvinTicketv", IDvinTicketv);
		Sites.put("TS4", TS4);
		Sites.put("LT4", LT4);
		Sites.put("IDcinAuthen", IDcinAuthen);
		Sites.put("ADsinAuthen", ADsinAuthen);
		Sites.put("TS5", TS5);

		// return Sites;
	}

	public boolean Authenticate() { // 验证信息
		String flag = Sites.get("Flag");
		if (flag.equals("4")) {
			System.out.println("认证模式...");
			String idvinTicketv = Sites.get("IDvinTicketv");
			if (idvinTicketv.equals(IDv)) {
				System.out.println("应用服务器确认成功...");
				String idcinTicketv = Sites.get("IDcinTicketv");
				String idcinAuthen = Sites.get("IDcinAuthen");
				String adsinTicketv = Sites.get("ADsinTicketv");
				String adsinAuthen = Sites.get("ADsinAuthen");
				if (idcinTicketv.equals(idcinAuthen) && adsinTicketv.equals(adsinAuthen)) {
					System.out.println("账号信息,用户网络地址确认成功...");
					String TS4 = Sites.get("TS4");
					String LT4 = Sites.get("LT4");
					String TS5 = Sites.get("TS5");
					int f1 = TS4.compareTo(LT4);
					int f2 = TS5.compareTo(LT4);
					if (f1 <= 0 && f2 <= 0) {
						System.out.println("票据信息审核成功!");
						return true;
					} else
						System.out.println("时间戳有误!");
				} else
					System.out.println("用户账号信息或网络地址有误!");
			} else
				System.out.println("应用服务器确认失败!");
		} else
			System.out.println("操作代码错误!");

		System.out.println("票据信息审核失败!");
		return false;

	}

	public String Feedback(boolean flag,String key) {
		String feedback = null;
		if (flag) {
			String TS5 = Sites.get("TS5");
			BigInteger ts5 = new BigInteger(TS5);
			ts5 = ts5.add(new BigInteger("1"));
			System.out.println(ts5);
			String ts = ts5.toString();
			feedback = ts;
			System.out.println(feedback);
			System.out.println(Kcv);
			DesEncrypt desencrypt = new DesEncrypt(feedback,Kcv);
			try {
				desencrypt.encrypt();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			feedback=desencrypt.ciphertexts;
			feedback="a1"+feedback;
		} else {
			feedback = "a0";
		}
		return feedback;
	}

	public void GroupChat() {
	};

	public static void main(String[] args) throws IOException {
		// 预设message中：第一位为操作标识符；后9+12+1+13+13分别为客户端账号IDC、网络地址ADS、IDv、ts4、LifeTime4；后面9+12+13为客户端账号IDC、网络地址ADS、时间戳TS5
		//System.out.println(IDv);
		/*String miwen = new String(
				"01000010011111110110001010110011110001100001101000001010100000000100001001101001001111000101001100010101110001100101000010100111100110101111011000000111000101000100110010101110011111000110101010011111111101000101100011111101001011010000011001100010100001111100110000100110101100001000001111100110111110000010101010101110111000001101001001001101111111001111100111010011011010010011011110001000011101100010011010111100110010111000011011011001110100000111001100001010000100000100100000101110000010011100100110110101001000011100011000001101101001010011010000100100100010111100111000010100000111010110001010001000101100111101010000011001010000111010110110110100101100011001100101001100000000010111000010111111011010100010111011000101100011010110110100110111000111010101010111101010010001001011011000000011001001111100001010111110101100101011101100000010000010100101100010001110000000111011100100010110\n"
						+ "");
		String key = "abcdefgh";
		Vserver v = new Vserver();
		String Message = null;
		Message = v.DesDecrypt(miwen, key);
		v.SplitMessage(Message);*/
		
		// 获得时间戳
		Long startTs = System.currentTimeMillis();
		System.out.println(startTs);
		
		// v.Authenticate();
		System.out.println("Sites" + Sites);
	}
}
