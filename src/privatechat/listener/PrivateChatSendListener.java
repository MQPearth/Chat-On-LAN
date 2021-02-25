package privatechat.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;


import domain.ClientThread;
import domain.ServerThread;
import privatechat.ui.PrivateChatUI;


public class PrivateChatSendListener implements ActionListener {
    private String title;
    private JTextArea inputMessageTextArea;
    private JTextArea chatRecordTextArea;
    private PrivateChatUI privateChatUI;

    public PrivateChatSendListener(String title, JTextArea inputMessageTextArea, JTextArea chatRecordTextArea, PrivateChatUI privateChatUI) {

        this.privateChatUI = privateChatUI;
        this.title = privateChatUI.getTitleUser();
        this.inputMessageTextArea = privateChatUI.getInputMessageTextArea();
        this.chatRecordTextArea = privateChatUI.getChatRecordTextArea();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("发送私聊消息");
        String message = inputMessageTextArea.getText().replaceAll("\r\n", "\n");

        if ("".equals(message)) {
            JOptionPane.showMessageDialog(chatRecordTextArea, "输入不能为空", "错误", 0);
            return;
        }
        // 服务端昵称为空
        if (ServerThread.getIntance().getServerNickName() == null) {
            System.out.println("连接端发送私聊消息");
            ClientThread.getInstance().send("@MessageToOne@\r\n" + title + "\r\n" + ClientThread.getInstance().getClientName() + "\r\n", message);
            //chatRecordTextArea.append(ClientThread.getInstance().getClientName()+":"+message+"\r\n");
            privateChatUI.toScreen(ClientThread.getInstance().getClientName(), message);


        } else {
            //服务度私聊没有将窗口add
            System.out.println("服务端发送私聊消息");
            ServerThread.getIntance().sendToOne(title, ServerThread.getIntance().getServerNickName(), message);
            //chatRecordTextArea.append(ServerThread.getIntance().getServerNickName()+":"+message+"\r\n");
            privateChatUI.toScreen(ServerThread.getIntance().getServerNickName(), message);
        }
        privateChatUI.getInputMessageTextArea().setText("");

    }

}
