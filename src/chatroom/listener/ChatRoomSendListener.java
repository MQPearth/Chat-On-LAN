package chatroom.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import chatroom.ui.ChatRoomUI;
import domain.ClientThread;
import domain.ServerThread;

public class ChatRoomSendListener implements ActionListener
{

	@Override
	public void actionPerformed(ActionEvent e)
	{
		System.out.println("发送消息");
		String message = ChatRoomUI.getInstance().getInputMessageTextArea().getText().replaceAll("\r\n", "\n");

		if (message == null || message.equals(""))
		{
			JOptionPane.showMessageDialog(ChatRoomUI.getInstance(), "输入不能为空", "错误", 0);
			return;
		}
		if (ServerThread.getIntance().getServerNickName() == null) // 服务端昵称为空
		{
			System.out.println("连接端发送消息");
			ClientThread.getInstance().send("@MessageToAll@\r\n" + ClientThread.getInstance().getClientName() + "\r\n",
					message);
		} else
		{
			System.out.println("服务端发送消息");
			ServerThread.getIntance().sendToAll(ServerThread.getIntance().getServerNickName(), message);
		}
		ChatRoomUI.getInstance().getInputMessageTextArea().setText("");

	}

}
