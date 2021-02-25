package chatroom.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import chatroom.ui.ChatRoomUI;

import domain.ServerThread;
import privatechat.ui.PrivateChatUI;

public class PrivateChatListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        // 服务端昵称为空
        if (ServerThread.getIntance().getServerNickName() == null) {
            System.out.println("连接端发起私聊窗口");
        } else {
            System.out.println("服务端发起私聊窗口");
        }
        String nickName = ChatRoomUI.getInstance().getComboSelect();
        if (nickName != null) {
            PrivateChatUI.newInstance(nickName);
        }
    }

}
