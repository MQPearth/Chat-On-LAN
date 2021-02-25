package domain;

import java.io.IOException;
import java.net.Socket;

import javax.swing.JOptionPane;

import chatroom.ui.ChatRoomUI;
import privatechat.ui.PrivateChatUI;
import utils.FileUtils;

/**
 * 连接端消息线程
 */
public class ClientThread extends Thread {

    private NameSocket socket;
    private static ClientThread clientThread = null;
    private String clientName;

    private ClientThread(String ip) {
        try {
            System.out.println("连接到：" + ip);
            socket = new NameSocket(new Socket(ip, 12345));
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "链接错误" + e.getMessage(), "错误", 0);
            System.exit(0);
        }
    }

    // 接收消息
    @Override
    public void run() {
        byte[] buf;
        while (true) {
            try {
                buf = new byte[1024];
                socket.getSocket().getInputStream().read(buf);
                System.out.println("连接端收到消息（" + new String(buf).trim() + ")");
                String[] txts = new String(buf).trim().split("\r\n");
                if ("@MessageToAll@".equals(txts[0])) {
                    ChatRoomUI.getInstance().toScreen(txts[1], txts[2]);
                } else if ("@MessageToOne@".equals(txts[0])) {
                    System.out.println("来自" + txts[1] + "的：" + txts[2]);
                    PrivateChatUI.newInstance(txts[1], txts[2]);
                } else if ("@FileToOne@".equals(txts[0]) && txts[1].equals(clientName)) {
                    System.out.println("来自" + txts[2] + "的发送文件提醒" + txts[3] + "大小" + txts[4]);
                    FileUtils.confirmRecFile(txts[2], txts[3], txts[4]);
                } else if ("@OnLineList@".equals(txts[0])) {
                    System.out.println("在线列表" + txts[1]);
                    updateOnlineList(txts[1]);
                } else if ("@SendFileResponse@".equals(txts[0])) {
                    if (txts[2].equals(clientName)) {
                        if ("true".equals(txts[1])) {
                            System.out.println("对方同意接收文件，返回ip:" + txts[3]);
                            FileUtils.sendFile(txts[3]);
                        } else if ("false".equals(txts[1])) {
                            System.out.println("对方拒绝接收文件");
                            JOptionPane.showMessageDialog(null, "对方拒绝接收文件", "提示", 1);
                        }
                    }

                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(ChatRoomUI.getInstance(), "房主已断开连接,程序即将退出", "提示", 1);
                System.exit(0);
            }
        }
    }

    // 更新在线列表
    public void updateOnlineList(String online) {
        String[] onlineList = online.split("&");
        ChatRoomUI.getInstance().getOnlineListCombo();
        for (String user : onlineList) {
            if (user.equals(clientName) == false) {
                ChatRoomUI.getInstance().addItemToOnLineCombo(user);
            }
        }

        ChatRoomUI.getInstance().getOnlineListCombo().removeAllItems();

        for (String user : onlineList) {
            if (user.equals(clientName) == false) {
                ChatRoomUI.getInstance().addItemToOnLineCombo(user);
            }
        }
    }

    public void send(String title, String message) {
        String txt = title + message;
        try {
            socket.getSocket().getOutputStream().write(txt.getBytes());
            System.out.println("连接端发送了" + message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // public void toScreen(String nickName, String message)
    // {
    // ChatRoomUI.getInstance().getChatRecordTextArea().append(nickName + " ： " +
    // message + "\n");
    // int height= 99999 ;
    // Point p = new Point();
    // p.setLocation(0 ,
    // ChatRoomUI.getInstance().getChatRecordTextArea().getLineCount()*height);
    // ChatRoomUI.getInstance().getSp().getViewport().setViewPosition(p);
    // }

    public static void init(String ip) {
        clientThread = new ClientThread(ip);
    }

    public static ClientThread getInstance() {
        return clientThread;
    }

    public NameSocket getSocket() {
        return socket;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }
}
