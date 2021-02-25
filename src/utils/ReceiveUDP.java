package utils;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * 接收udp广播
 *
 * @author 22x
 */
public class ReceiveUDP {


    public static void receiveUDP() {
        int port = 9999;
        DatagramSocket ds = null;
        DatagramPacket dp = null;
        // 存储发来的消息
        byte[] buf = new byte[1024];
        String sendText = IPUtils.getLocalIP();
        System.out.println("本机局域网ip" + sendText);
        try {
            while (true) {
                // 绑定端口的
                ds = new DatagramSocket(port);
                dp = new DatagramPacket(buf, buf.length);
                System.out.println("监听广播端口打开：");
                // 等待接收，会进入阻塞状态
                ds.receive(dp);
                ds.send(new DatagramPacket(sendText.getBytes(), sendText.length(), dp.getSocketAddress()));
                System.out.println("收到广播消息：" + new String(dp.getData()).trim());
                ds.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}