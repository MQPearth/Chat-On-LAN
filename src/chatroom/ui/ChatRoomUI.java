package chatroom.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Point;

import javax.swing.*;

import chatroom.listener.ChatRoomExitListener;
import chatroom.listener.ChatRoomSendListener;
import chatroom.listener.PrivateChatListener;

/**
 * 群聊界面
 *
 * @author zzx
 */
public class ChatRoomUI extends JFrame {
    private static final long serialVersionUID = 1L;
    private static ChatRoomUI chatRoomUI = null;
    /**
     * 聊天记录
     */
    private JTextArea chatRecordTextArea = new JTextArea();
    /**
     * 消息输入框
     */
    private JTextArea inputMessageTextArea = new JTextArea("输入消息");
    /**
     * 聊天记录滚动面板
     */
    private JScrollPane sp = null;
    /**
     * 用户列表
     */
    private JComboBox<String> onlineListCombo = new JComboBox<>();
    /**
     * 退出按钮
     */
    private JButton exitButton = new JButton("退出");
    /**
     * 发送按钮
     */
    private JButton sendButton = new JButton("发送");

    private ChatRoomUI() {
        setTitle("聊天室");
        setBounds(490, 150, 605, 650);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        setContentPane(panel);
        panel.setLayout(null);

        JPanel chatRecordPanel = new JPanel();
        chatRecordPanel.setLayout(new GridLayout(1, 1));
        chatRecordPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        chatRecordPanel.setBounds(0, 0, 599, 400);
        sp = new JScrollPane(chatRecordTextArea);
        //文本与文本域的边距
        chatRecordTextArea.setMargin(new Insets(10, 10, 10, 10));
        // 不可编辑
        chatRecordTextArea.setEditable(false);
        chatRecordTextArea.setLineWrap(true);
        chatRecordTextArea.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        chatRecordTextArea.setFocusable(false);
        chatRecordPanel.add(sp);
        panel.add(chatRecordPanel);

        JPanel inputMessagePanel = new JPanel();
        inputMessagePanel.setLayout(new GridLayout(1, 1));
        inputMessagePanel.setBounds(0, 400, 600, 150);
        inputMessageTextArea.setLineWrap(true);
        inputMessageTextArea.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        inputMessagePanel.add(inputMessageTextArea);
        panel.add(inputMessagePanel);

        JPanel functionPanel = new JPanel();
        functionPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 20));
        // functionPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        functionPanel.setBounds(0, 550, 600, 150);
        JButton privateChatButton = new JButton("发起私聊");
        functionPanel.add(onlineListCombo);
        functionPanel.add(privateChatButton);
        onlineListCombo.setPreferredSize(new Dimension(100, 25));
        functionPanel.add(exitButton);
        functionPanel.add(sendButton);
        exitButton.addActionListener(new ChatRoomExitListener());
        sendButton.addActionListener(new ChatRoomSendListener());
        panel.add(functionPanel);

        privateChatButton.addActionListener(new PrivateChatListener());

        setVisible(true);

    }

    public void toScreen(String nickName, String message) {
        ChatRoomUI.getInstance().getChatRecordTextArea().append(nickName + " ： " + message + "\n");
        //滚动条自动跟进
        int height = 99999;
        Point p = new Point();
        p.setLocation(0, ChatRoomUI.getInstance().getChatRecordTextArea().getLineCount() * height);
        ChatRoomUI.getInstance().getSp().getViewport().setViewPosition(p);
    }

    public static ChatRoomUI getInstance() {
        return ChatRoomUI.chatRoomUI;
    }

    public String getComboSelect() {
        return (String)onlineListCombo.getSelectedItem();
    }

    public static void init() {
        ChatRoomUI.chatRoomUI = new ChatRoomUI();
    }

    public JTextArea getChatRecordTextArea() {
        return chatRecordTextArea;
    }

    public void addItemToOnLineCombo(String newName) {
        onlineListCombo.addItem(newName);
    }

    public void removeItemFromOnLineCombo(String newName) {
        onlineListCombo.removeItem(newName);
    }

    public void setChatRecordTextArea(JTextArea chatRecordTextArea) {
        this.chatRecordTextArea = chatRecordTextArea;
    }

    public JTextArea getInputMessageTextArea() {
        return inputMessageTextArea;
    }

    public void setInputMessageTextArea(JTextArea inputMessageTextArea) {
        this.inputMessageTextArea = inputMessageTextArea;
    }

    public JComboBox<String> getOnlineListCombo() {
        return onlineListCombo;
    }

    public void setOnlineListCombo(JComboBox<String> onlineListCombo) {
        this.onlineListCombo = onlineListCombo;
    }

    public JScrollPane getSp() {
        return sp;
    }

    public void setSp(JScrollPane sp) {
        this.sp = sp;
    }
}
