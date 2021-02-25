package utils;


import javax.swing.JOptionPane;

import domain.ClientThread;
import domain.NameSocket;
import selectroom.ui.SelectRoomUI;

public class InputNickNameCheck {

    /**
     * 向服务端确认昵称是否重复
     *
     * @return 昵称
     */
    public static String nickCheck(NameSocket socket) {
        try {
            String nickName;
            while (true) {
                nickName = JOptionPane.showInputDialog(SelectRoomUI.getInstance(), "请输入昵称", "提示", 1);

                if (nickName == null) {
                    System.exit(0);
                } else if (nickName.equals("")) {
                    continue;
                }
                System.out.println("昵称：" + nickName);

                socket.sendMessage("@NickName@\r\n" + nickName);
                byte[] buf = new byte[1024];
                socket.getSocket().getInputStream().read(buf);
                // 向服务端发送数据，服务端返回1则通过
                if (new String(buf).trim().equals("1")) {
                    System.out.println("通过");
                    break;
                } else {
                    System.out.println("该昵称已存在");
                    JOptionPane.showMessageDialog(SelectRoomUI.getInstance(), "该昵称已存在，请重新输入", "提示", 0);
                }
            }
            ClientThread.getInstance().setClientName(nickName);
        } catch (Exception e) {
            // handle exception
        }

        return "";
    }
}
