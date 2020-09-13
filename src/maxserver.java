
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Hashtable;

public class maxserver {
	
	@SuppressWarnings("rawtypes")
	private static HashMap<String,Socket> ServerThreadMap=new HashMap<String,Socket>();
	
	public static void main(String[] args) throws IOException {
		int port=55533;
		ServerSocket server=new ServerSocket(port);
		System.out.println("server 将一直等待连接");
		while(true) {
			Socket socket=server.accept();
			
			new Thread(()->{
				DataInputStream dis = null;
				try {
					dis = new DataInputStream(socket.getInputStream());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				DataOutputStream dos = null;
				try {
					dos = new DataOutputStream(socket.getOutputStream());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			//	boolean isrunning=true;
				//while(isrunning) {
					String data = null;
					try {
						data = dis.readUTF();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}	
					
					System.out.println(data);
					Vserver v=new Vserver();
					String flag=v.Sites.get("Flag");
					if(flag=="4") {
						//检验票据合法性
						v.SplitMessage(data);
						String result=v.Feedback(v.Authenticate(),key);
						System.out.println(data);
						try {
							dos.writeUTF(result);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					if(flag=="5") {
						//处理用户群聊
						String idcinAuthen=v.Sites.get("IDcinAuthen");
						//将连接上的用户发送到
						ServerThreadMap.put(idcinAuthen,socket);
					}
					
					
					
					try {
						dos.writeUTF(result);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						dos.flush();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			
				try {
					dis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}).start();
			
			
			
			
		}
		
	}
}
