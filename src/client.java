
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
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
 * 客户端
 */
@SuppressWarnings("Duplicates")
public class client {
    private static String serverInetAddress = "127.0.0.1";
    private static int serverPort = 55533;
    private static Socket client = null;
    private static OutputStream os = null;
    private static InputStream is = null;
    private static String thisName;
    private static boolean alive = true;

    /**
     * 客户端连接服务器
     */
    @SuppressWarnings("unused")
    public static void open(String name) {
        try {
            thisName = name;
            InetAddress inetAddress = InetAddress.getLocalHost();
            //建立连接
            client = new Socket(serverInetAddress, serverPort);
            //数据流发送数据
            os = client.getOutputStream();
            sendMsg("{\"type\":\"OPEN\",\"clientName\":\"" + name + "\"}");
            //数据流接收数据
            is = client.getInputStream();
            byte[] b = new byte[1024];
            int length = 0;
            while (alive) {
                //接收从服务器发送回来的消息
                length = is.read(b);
                if (length != -1) {
                    onMsg(new String(b, 0, length));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                //关流
                os.close();
                client.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 关闭客户端
     */
    public static void close() {
        sendMsg("{\"type\":\"CLOSE\"}");
        alive = false;
    }

    /**
     * 发送消息
     */
    public static void sendMsg(String msg) {
        try {
            //调用发送
            os.write(msg.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 收到消息的回调
     */
    private static void onMsg(String message) {
        //JSON字符串转 HashMap
        HashMap hashMap = null;
        try {
            hashMap = new ObjectMapper().readValue(message, HashMap.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String msg = (String) hashMap.get("message");

        String chat = (String) hashMap.get("chat");

        String from = (String) hashMap.get("from");

        String to = (String) hashMap.get("to");

        //群聊
        if ("GROUP".equals(chat)) {
            //后台打印
            System.out.println(thisName + "收到（" + to + "）群聊消息：" + msg);
        }
        //私聊
        if ("PRIVATE".equals(chat)) {
            //后台打印
            System.out.println(thisName + "收到（" + from + "）私聊消息：" + msg);
        }
    }

    /**
     * 获取thisName
     */
    public static String getThisName() {
        return thisName;
    }
}