
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.io.InputStreamReader;

/**
 * 服务端
 */
public class socket {
	private static ServerSocket server = null;
	private static Socket ss = null;
	private static String key = null; // Kcv秘钥
	public static 	String data = null;
	
	/**
	 * 客户端集合
	 */
	private static HashMap<String, ServerThread> serverThreadMap = new HashMap<String, ServerThread>();

	static class ReceiveThread extends Thread{
		public volatile boolean exit = false; 
		
		@Override
		public void run() {
			int port = 8888;
			ServerSocket server1=null;
			try {
				server1 = new ServerSocket(port);
			} catch (IOException e3) {
				// TODO Auto-generated catch block
				e3.printStackTrace();
			}
			System.out.println("server building");
			while(!exit) {
				Socket socket1=null;
				try {
					socket1 = server1.accept();
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				System.out.println("Socket building");
				System.out.println("New user in");
				DataInputStream dis1 = null;
				try {
					dis1 = new DataInputStream(socket1.getInputStream());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				DataOutputStream dos1 = null;
				try {
					dos1 = new DataOutputStream(socket1.getOutputStream());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// boolean isrunning=true;
				// while(isrunning) {
				try {
					data = dis1.readUTF();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				System.out.println("Has Received Data:" + data);

				try {
					if (data.charAt(0) == 'b') {
						key = data.substring(1);
						System.out.println("Key:"+key);
						exit=true;
						data = "s1";
						try {
							dos1.writeUTF(data);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

				} catch (Exception e) {
					e.printStackTrace();
				}

				try {
					dos1.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				try {
					dis1.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					socket1.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	public static void ReceiveKcv()  {
		/*int port = 8888;
		ServerSocket server1 = new ServerSocket(port);
		System.out.println("server building");
		// final Integer[] isGet = { 0 };
		while (!exit) {
			Socket socket1 = server1.accept();
			System.out.println("Socket building");
			new Thread(() -> {
				System.out.println("New user in");
				DataInputStream dis1 = null;
				String data = null;
				try {
					dis1 = new DataInputStream(socket1.getInputStream());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				DataOutputStream dos1 = null;
				try {
					dos1 = new DataOutputStream(socket1.getOutputStream());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// boolean isrunning=true;
				// while(isrunning) {
				try {
					data = dis1.readUTF();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				System.out.println("Has Received Data:" + data);

				try {
					if (data.charAt(0) == 'b') {
						key = data.substring(1);
						System.out.println(key);
						flag=true;
						data = "s1";
						try {
							dos1.writeUTF(data);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

				} catch (Exception e) {
					e.printStackTrace();
				}

				try {
					dos1.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				try {
					dis1.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					socket1.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}).start();
		}
		System.out.println("Receive Successful!");*/
		ReceiveThread t=new ReceiveThread();
		t.start();
		if(data!=null) {
			t.exit=true;
			System.out.println("Receive Successful!");
		}
		
	}
	

	public static void main(String[] args) throws IOException {

		// 服务器打开后，首先接收TGS服务器发送来的秘钥


		ReceiveKcv();
		Thread t=new Thread();
		try {
			t.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		server();

	}

	/**
	 * 普通服务器连接
	 */
	private static void server() {
		try {
			// 建立服务端
			server = new ServerSocket(55533);
			System.out.println("server端已启动！");
			while (true) {
				// 创建接收接口
				ss = server.accept();
				// 启动新客户监听线程
				new ServerThread(server, ss).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				ss.close();
				server.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 内部类线程，每连接一个新的客户端就启动一个对应的监听线程
	 */
	@SuppressWarnings("Duplicates")
	private static class ServerThread extends Thread {
		ServerSocket server = null;
		Socket socket = null;
		DataInputStream dis = null;
		DataOutputStream dos = null;
		String clientName = null;
		boolean alive = true;

		public ServerThread() {
		}

		ServerThread(ServerSocket server, Socket socket) {
			this.socket = socket;
			this.server = server;
		}

		@Override
		public void run() {
			// 接收数据
			try {
				// 输入输出流定义
				try {
					dis = new DataInputStream(socket.getInputStream());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					dos = new DataOutputStream(socket.getOutputStream());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println(serverThreadMap);
				while (alive) {
					// 接收从客户端发送的消息
					// 文本消息
					String message = null;
					try {
						message = dis.readUTF();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					System.out.println("message:" + message);
					Vserver v = new Vserver();
					String flag = message.substring(0, 1);
					System.out.println("Flag:" + flag);
					/*if (flag != "4" && flag != "5") {
						// 标志位不是4也不是5，只能是经过加密的认证密文
						message = v.DesDecrypt(message, key);
						flag = message.substring(0, 1);
						System.out.println(flag);
					}*/
					
					//String data=message.substring(1);
					//message = v.DesDecrypt(message, key);
					//flag = message.substring(0, 1);
					if (flag.equals("4")) {
						message = v.DesDecrypt(message, key);
						System.out.println(flag);
						// 获得时间戳
						// Long startTs = System.currentTimeMillis();
						// System.out.println(startTs);

						// 检验票据合法性
						v.SplitMessage(message);
						System.out.println("截取成功!");
						String result = v.Feedback(v.Authenticate(), key);
						System.out.println(result);
						try {
							dos.writeUTF(result);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}

					if (flag.equals("5")) {
						// 群聊模式
						// message=null;
						System.out.println("连接成功！");
						// String idcinAuthen=v.Sites.get("IDcinAuthen");
						String userid = message.substring(1, 10);
						String data1 = message.substring(10);
						String sendback = "c" + data1;
						String idcinAuthen = userid;
						this.clientName = idcinAuthen;
						serverThreadMap.put(idcinAuthen, this);
						System.out.println(idcinAuthen + "连接成功！");
						System.out.println("当前客户端数量：" + serverThreadMap.size());
						// message=dis.readUTF();

						for (ServerThread st : serverThreadMap.values()) {
							// 向其他客户端转发消息
							if (st != this) {
								st.dos.writeUTF(sendback);
							}
						}
						// 后台打印
						System.out.println(idcinAuthen + "向所有人说：" + data1);
					}
					message = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
				System.err.println("与" + clientName + "连接中断，被迫关闭监听线程！");
				this.alive = false;
			} finally {
				try {
					serverThreadMap.remove(clientName);
					System.out.println("当前客户端数量：" + serverThreadMap.size());
					dos.close();
					dis.close();
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}
}