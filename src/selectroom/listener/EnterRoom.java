package selectroom.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import chatroom.ui.ChatRoomUI;
import domain.ClientThread;
import selectroom.ui.SelectRoomUI;
import utils.InputNickNameCheck;

public class EnterRoom implements ActionListener
{

	@Override
	public void actionPerformed(ActionEvent e)
	{
		String ipGet=(String)SelectRoomUI.getInstance().getComboBox().getSelectedItem();
		if(ipGet.equals("无在线聊天室"))
			return;
		System.out.println("进入房间");
		SelectRoomUI.getInstance().dispose(); //选择房间窗口关闭
		ClientThread.init(ipGet);//连接端线程初始化，传入选择的ip
		InputNickNameCheck.nickCheck(ClientThread.getInstance().getSocket());//向服务端确认id
		ChatRoomUI.init();//聊天室初始化
		ClientThread.getInstance().start();//连接端线程启动
	}

}
