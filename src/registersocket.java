
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.io.InputStreamReader;

public class registersocket {
	static String truth="1";
	static String wrong="0";
       public static String register(String host, int port,String data,String account) throws UnknownHostException, IOException {
    	  /* String host = "127.0.0.1"; 
		    int port = 55533;*/
		    // �����˽�������
		    Socket socket = new Socket(host, port);
		    // �������Ӻ�����Ϣ
		     BufferedReader console=new BufferedReader(new InputStreamReader(System.in));
		    DataOutputStream dos=new DataOutputStream(socket.getOutputStream());
		    DataInputStream dis=new DataInputStream(socket.getInputStream());
		   
		   //������Ϣ
		    //	String msg=console.readLine();
			//  dos.writeUTF(msg);
		    //System.out.println("data"+receiver);
		    System.out.println("serve1111r"+data);
	    	dos.writeUTF(data);
	    	System.out.println("serve1111r"+data);
		    dos.flush();
		    //��ȡ��Ϣ
		    String receiver;
		    receiver=dis.readUTF();
			System.out.println("server"+receiver);
			String tag=receiver.substring(0,1);
			if(tag.equals("6")) {
				String sign=receiver.substring(1,2);
				if(sign.equals("1")) {
					dis.close();
				    dos.close();
				    socket.close(); 
					return truth;
				}
				/*if(sign.equals("0")) {
					dis.close();
				    dos.close();
				    socket.close(); 
					return false;
				}*/
			}
			if(tag.equals("7")) {
				String sign=receiver.substring(1,2);
				if(sign.equals("1")) {
					dis.close();
				    dos.close();
				    socket.close(); 
					return truth;
				}
			}
			if(tag.equals("8")) {
				String sign=receiver.substring(1,2);
				if(sign.equals("1")) {
					dis.close();
				    dos.close();
				    socket.close(); 
					return receiver.substring(3);
				}
			}
			if(tag.equals("9")) {
				String sign=receiver.substring(1,2);
				if(sign.equals("1")) {
					dis.close();
				    dos.close();
				    socket.close(); 
					return receiver.substring(3);
				}
			}
			if(tag.equals("a")) {
				String sign=receiver.substring(1,2);
				if(sign.equals("1")) {
					data=5+"hello";
					dos.writeUTF(data);
					
					
					String receiver1=null;
					 receiver1=dis.readUTF();
					 System.out.println("server"+receiver);
					
					
					
					
					dis.close();
				    dos.close();
				    socket.close(); 
					return receiver.substring(3);
				}
			}
			if(tag.equals("c")) {
				String sign=receiver.substring(1,2);
				System.out.println("server"+receiver);
				while(true) {
			    	//发送消息
					data=null;
			    	String msg=console.readLine();
			    	data=5+account+msg;
				    dos.writeUTF(data);
				    dos.flush();
				    //获取消息
					data=dis.readUTF();
					System.out.println("from client message"+data);
					if(data.equals("end")) {
						break;
					}
			    }

			}
			
			dis.close();
		    dos.close();
		    socket.close(); 
    	   
    	   
    	   
    	   return wrong;
    	   
       }
}
