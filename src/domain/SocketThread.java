package domain;

import java.io.IOException;
import java.io.InputStream;

import javax.swing.JOptionPane;

import chatroom.ui.ChatRoomUI;
import privatechat.ui.PrivateChatUI;
import utils.FileUtils;

/**
 * 处理连接端socket 线程 监听连接端发送的消息
 */
public class SocketThread extends Thread {
    private NameSocket socket;

    public SocketThread(NameSocket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        System.out.println("服务端：已为" + socket.getName() + "开启处理线程");
        ChatRoomUI.getInstance().addItemToOnLineCombo(socket.getName());
        // 新连接到来，发送列表
        ServerThread.getIntance().sendOnLineList();
        try {
            InputStream is = socket.getSocket().getInputStream();
            ServerThread serverThread = ServerThread.getIntance();
            byte[] buf = null;

            while (true) {
                buf = new byte[1024];
                is.read(buf);
                System.out.println("服务端收到消息(" + new String(buf).trim() + ")");
                if (new String(buf).trim().equals("")) {
                    throw new IOException();
                }

                String[] messages = new String(buf).trim().split("\r\n");
                if ("@MessageToAll@".equals(messages[0])) {
                    serverThread.sendToAll(messages[1], messages[2]);
                } else if ("@MessageToOne@".equals(messages[0])) {
                    // 接收到对服务端的私聊消息
                    if (messages[1].equals(serverThread.getServerNickName())) {
                        System.out.println("来自" + messages[2] + "的" + messages[3]);
                        PrivateChatUI.newInstance(messages[2], messages[3]);
                    } else {// 连接端对连接端的私聊消息
                        serverThread.sendToOne(messages[1], messages[2], messages[3]);
                    }
                } else if ("@FileToOne@".equals(messages[0])) {
                    // 接收到对服务端的私聊消息
                    if (messages[1].equals(serverThread.getServerNickName())) {
                        System.out.println("来自" + messages[2] + "的发送文件提醒" + messages[3] + "大小" + messages[4]);
                        FileUtils.confirmRecFile(messages[2], messages[3], messages[4]);
                    } else {
                        System.out.println("来自" + messages[1] + "的发送文件提醒" + messages[3] + "大小" + messages[4]);

                        serverThread.sendFileMessage(messages[1], socket.getName(), messages[3], messages[4]);
                    }
                } else if ("@SendFileResponse@".equals(messages[0])) {
                    // 接收到对服务端的文件回复消息
                    if (messages[2].equals(serverThread.getServerNickName())) {
                        if ("true".equals(messages[1])) {
                            System.out.println("对方同意接收文件，返回ip:" + messages[3]);
                            FileUtils.sendFile(messages[3]);
                        } else if ("false".equals(messages[1])) {
                            System.out.println("对方拒绝接收文件");
                            JOptionPane.showMessageDialog(null, "对方拒绝接收文件", "提示", 1);
                        }
                    } else {
                        if (messages[3] == null) {
                            serverThread.sendFileResponse(messages[2], messages[1]);
                        } else {
                            serverThread.sendFileResponse(messages[2], messages[1], messages[3]);
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("SocketThread.run+连接端连接断开+此socket处理线程结束");

            ChatRoomUI.getInstance().removeItemFromOnLineCombo(socket.getName());
            ServerThread serverThread = ServerThread.getIntance();
            serverThread.getSockets().remove(socket);
            // 该连接断开，发送列表
            serverThread.sendOnLineList();
            // e.printStackTrace();
        }

    }
}
