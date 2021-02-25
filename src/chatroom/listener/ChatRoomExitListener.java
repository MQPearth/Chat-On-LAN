package chatroom.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class ChatRoomExitListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("关闭");
        System.exit(0);
    }

}
