package privatechat.ui;

import java.awt.Color;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;


import javax.swing.BorderFactory;
import javax.swing.JButton;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import chatroom.ui.ChatRoomUI;
import privatechat.listener.PrivateChatExitListener;
import privatechat.listener.PrivateChatSendFileListener;
import privatechat.listener.PrivateChatSendListener;

public class PrivateChatUI extends JFrame {
    private static final long serialVersionUID = 1L;
    /**
     * 聊天记录
     */
    private JTextArea chatRecordTextArea = new JTextArea();
    /**
     * 消息输入框
     */
    private JTextArea inputMessageTextArea = new JTextArea();
    /**
     * 聊天记录滚动面板
     */
    private JScrollPane sp = null;
    /**
     * 退出按钮
     */
    private JButton exitButton = new JButton("关闭私聊");
    /**
     * 发送按钮
     */
    private JButton sendButton = new JButton("发送");
    /**
     * 发送按钮
     */
    private JButton sendFileButton = new JButton("发送文件");
    /**
     * 窗口标题
     */
    private String title;
    /**
     * 私聊窗口
     */
    public static HashMap<String, PrivateChatUI> privateChatUIs = new HashMap<>();

    public PrivateChatUI(String title) {
        this.title = title;
        setTitle("与  " + title + "  的对话");
        setBounds(490, 150, 605, 650);
        setResizable(false);

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
        //不可编辑
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
        functionPanel.setBounds(0, 550, 600, 150);

        functionPanel.add(sendFileButton);
        functionPanel.add(exitButton);
        functionPanel.add(sendButton);

        sendFileButton.addActionListener(new PrivateChatSendFileListener(title, this));
        exitButton.addActionListener(new PrivateChatExitListener(title));
        sendButton.addActionListener(new PrivateChatSendListener(title, inputMessageTextArea, chatRecordTextArea, this));

        panel.add(functionPanel);

        final String titleTemp = title;
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                PrivateChatUI.removeChatUI(titleTemp);
                System.out.println("关闭了私聊窗口");
            }
        });
        setVisible(true);
    }

    public void toScreen(String fromUser, String message) {
        chatRecordTextArea.append(fromUser + " ： " + message + "\r\n");
        //尽量大
        int height = 99999;
        Point p = new Point();
        p.setLocation(0, chatRecordTextArea.getLineCount() * height);
        this.getSp().getViewport().setViewPosition(p);
    }

    // 从窗口列表中移除，以指定的title
    public static void removeChatUI(String title) {

        // Iterator<PrivateChatUI> iterator=privateChatUIs.iterator();
        // while(iterator.hasNext())
        // {
        // PrivateChatUI chat=iterator.next();
        // if(chat.getTitleUser().equals(title))
        // privateChatUIs.remove(chat);
        // }
        privateChatUIs.remove(title).dispose();
        ;
    }

    public String getTitleUser() {
        return title;
    }

    public void setTitleUser(String title) {
        this.title = title;
    }

    // new 新窗口并将接收的消息显示
    public static void newInstance(String user, String message) {
        // LinkedList<PrivateChatUI> chats = PrivateChatUI.privateChatUIs;
        // boolean flag = false; // 原有窗口是否存在，默认不存在
        // for (PrivateChatUI chat : chats)
        // {
        // if (chat.getTitleUser().equals(user))
        // {
        // chat.showMessage(user, message);
        // flag = true; // 存在
        // }
        // }
        // if (!flag) // 不存在则new新窗口
        // {
        // PrivateChatUI privateChatUI = new PrivateChatUI(user);
        // chats.add(privateChatUI);
        // privateChatUI.showMessage(user, message);
        // }
        HashMap<String, PrivateChatUI> uis = privateChatUIs;
        // 存在此窗口
        if (uis.containsKey(user)) {
            uis.get(user).toScreen(user, message);
        } else {
            // 不存在
            PrivateChatUI privateChatUI = new PrivateChatUI(user);
            uis.put(user, privateChatUI);
            privateChatUI.toScreen(user, message);
        }

    }

    // new 新窗口
    public static void newInstance(String user) {
//		LinkedList<PrivateChatUI> chats = PrivateChatUI.privateChatUIs;
//		boolean flag = false; // 原有窗口是否存在，默认不存在
//		for (PrivateChatUI chat : chats)
//		{
//			if (chat.getTitleUser().equals(user))
//			{
//				flag = true; // 存在
//				JOptionPane.showMessageDialog(ChatRoomUI.getInstance(), "窗口已存在", "提示", 1);
//			}
//		}
//		if (!flag) // 不存在则new新窗口
//		{
//			PrivateChatUI privateChatUI = new PrivateChatUI(user);
//			chats.add(privateChatUI);
//		}
        HashMap<String, PrivateChatUI> uis = privateChatUIs;
        // 存在此窗口
        if (uis.containsKey(user)) {
            JOptionPane.showMessageDialog(ChatRoomUI.getInstance(), "窗口已存在", "提示", 1);
        } else {
            // 不存在
            PrivateChatUI ui = new PrivateChatUI(user);
            uis.put(user, ui);
        }

    }

    public JScrollPane getSp() {
        return sp;
    }

    public void setSp(JScrollPane sp) {
        this.sp = sp;
    }

    public JTextArea getChatRecordTextArea() {
        return chatRecordTextArea;
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
}
