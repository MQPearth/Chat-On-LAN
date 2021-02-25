package selectroom.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import chatroom.ui.ChatRoomUI;
import domain.ClientThread;
import selectroom.ui.SelectRoomUI;
import utils.InputNickNameCheck;

public class EnterRoom implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        String ipGet = (String)SelectRoomUI.getInstance().getComboBox().getSelectedItem();
        if ("无在线聊天室".equals(ipGet)) {
			return;
		}
        System.out.println("进入房间");
        //选择房间窗口关闭
        SelectRoomUI.getInstance().dispose();
        //连接端线程初始化，传入选择的ip
        ClientThread.init(ipGet);
        //向服务端确认id
        InputNickNameCheck.nickCheck(ClientThread.getInstance().getSocket());
        //聊天室初始化
        ChatRoomUI.init();
        //连接端线程启动
        ClientThread.getInstance().start();
    }

}
