package selectroom.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import chatroom.ui.ChatRoomUI;
import domain.ServerThread;
import selectroom.ui.SelectRoomUI;


public class CreateRoom implements ActionListener
{

	@Override
	public void actionPerformed(ActionEvent e)
	{
		System.out.println("创建房间");
		SelectRoomUI.getInstance().dispose();
		String serverName;
		
		while(true)
		{
			serverName=JOptionPane.showInputDialog(SelectRoomUI.getInstance(), "请输入昵称", "提示", 1);
			if(serverName==null)
				System.exit(0);
			else if(serverName.equals(""))
				continue;
			else
				break;
		}
		
		System.out.println("服务端昵称"+serverName);
		ServerThread.getIntance().setNickName(serverName);
		ChatRoomUI.init(); //初始化聊天室
		ServerThread.getIntance().start();//服务端线程启动
	}

}
