package utils;

import java.awt.Font;
import java.awt.GridLayout;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import chatroom.ui.ChatRoomUI;
import domain.ClientThread;
import domain.ServerThread;
import privatechat.listener.PrivateChatSendFileListener;

public class FileUtils {
    /**
     * 将接收文件的大小
     */
    private static long recFileSize;

    // 将文件的字节转换为Mb
    public static String sizetoMb(String fileSizeStr) {

        long fileSize = new Double(fileSizeStr).longValue();
        double size = (fileSize / (1024.0 * 1024));
        int temp = (int)(size * 1000);
        size = temp / 1000.0;
        return size + "";
    }

    /**
     * @param fromUser 从用户昵称
     * @param fileName 文件名
     * @param fileSize 文件大小
     */
    public static void confirmRecFile(String fromUser, String fileName, String fileSize) {
        int option = JOptionPane.showConfirmDialog(null,
                "是否 接收来自<" + fromUser + ">发送的<" + fileName + ">,大小为< " + sizetoMb(fileSize) + " Mb>");
        // 是否接收文件
        boolean recFile = false;

        if (JOptionPane.OK_OPTION == option) {
            System.out.println("接收文件");
            recFile = true;
            FileUtils.recFileSize = new Double(fileSize).longValue();
        } else if (JOptionPane.NO_OPTION == option) {
            System.out.println("拒绝接收文件");
        } else if (JOptionPane.CANCEL_OPTION == option) {
            System.out.println("取消对话框");
        }
        // 接收文件
        if (recFile) {
            // 服务端昵称为空
            if (ServerThread.getIntance().getServerNickName() == null) {
                // 连接端
                ClientThread.getInstance().send("@SendFileResponse@\r\n",
                        "true\r\n" + fromUser + "\r\n" + IPUtils.getLocalIP() + "\r\n");
            } else {
                ServerThread.getIntance().sendFileResponse(fromUser, "true", IPUtils.getLocalIP());
            }
            FileUtils.recFile();
        } else {
            // 拒绝接收
            // 服务端昵称为空
            if (ServerThread.getIntance().getServerNickName() == null) {
                // 连接端
                ClientThread.getInstance().send("@SendFileResponse@\r\n", "false\r\n" + fromUser);
            } else {
                ServerThread.getIntance().sendFileResponse(fromUser, "false");
            }
        }

    }

    public static void sendFile(final String ip) {

        new Thread() {
            @Override
            public void run() {

                int length = 0;
                byte[] sendByte = null;
                Socket socket = null;
                DataOutputStream dout = null;
                FileInputStream fin = null;
                try {
                    // 已发送数据的大小
                    long count = 0;
                    JLabel label = showProgress(ChatRoomUI.getInstance());
                    try {
                        socket = new Socket();
                        // 指定超时
                        socket.connect(new InetSocketAddress(ip, 12346), 10 * 1000);
                        socket.setKeepAlive(true);
                        System.out.println("连接到目标ip");
                        dout = new DataOutputStream(socket.getOutputStream());
                        // 获取选择的文件
                        File file = PrivateChatSendFileListener.file;
                        fin = new FileInputStream(file);
                        sendByte = new byte[1024];
                        dout.writeUTF(file.getName());
                        while ((length = fin.read(sendByte, 0, sendByte.length)) > 0) {
                            dout.write(sendByte, 0, length);
                            dout.flush();
                            count = count + length;
                            label.setText("发送进度:" + count + "  /  " + file.length() + "  字节");
                            System.out.println("发送了" + length);
                        }
                    } catch (Exception e) {
                        // java Connection reset by peer: socket write error
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(ChatRoomUI.getInstance(), "发送错误" + e.getMessage(), "错误", 0);
                    } finally {
                        if (dout != null) {
                            dout.close();
                        }
                        if (fin != null) {
                            fin.close();
                        }
                        if (socket != null) {
                            socket.close();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("结束发送");
            }
        }.start();
    }

    public static void recFile() {

        new Thread() {
            @Override
            public void run() {
                try {
                    System.out.println("开始监听。。。");
                    final ServerSocket server = new ServerSocket(12346);
                    Socket socket = server.accept();
                    // 已接收数据的大小
                    long count = 0;
                    JLabel label = showProgress(ChatRoomUI.getInstance());
                    byte[] inputByte = null;
                    int length = 0;
                    DataInputStream din = null;
                    FileOutputStream fout = null;
                    try {
                        din = new DataInputStream(socket.getInputStream());
                        String fileName = din.readUTF();
                        System.out.println("test" + fileName);

                        File file = new File("C:\\ChatRoom_RecFile\\");

                        if (!file.exists())
                            file.mkdir();

                        file = new File("C:\\ChatRoom_RecFile\\" + fileName);
                        int i = 1;
                        while (file.exists()) {
                            file = new File("C:\\ChatRoom_RecFile\\" + "副本" + i + fileName);
                            i++;
                        }
                        fout = new FileOutputStream(file);
                        inputByte = new byte[1024];
                        System.out.println("开始接收文件数据...");
                        while (true) {
                            if (din != null) {
                                length = din.read(inputByte, 0, inputByte.length);
                            }
                            if (length == -1) {
                                break;
                            }
                            System.out.println(length);
                            fout.write(inputByte, 0, length);
                            fout.flush();
                            count = count + length;
                            label.setText("接收进度:" + count + "  /  " + FileUtils.recFileSize + "  字节");
                        }
                        System.out.println("完成接收");
                        JOptionPane.showMessageDialog(null, "文件保存在" + file.getAbsolutePath(), "文件已接收", 1);

                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "接收错误" + ex.getMessage(), "错误", 1);
                    } finally {
                        if (fout != null) {
                            fout.close();
                        }
                        if (din != null) {
                            din.close();
                        }
                        if (socket != null) {
                            socket.close();
                        }
                        server.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public static JLabel showProgress(JFrame frame) {
        JDialog dialog = new JDialog(frame, "进度", false);

        JLabel label = new JLabel();
        label.setFont(new Font("微软雅黑", Font.PLAIN, 20));
        dialog.setSize(400, 100);
        dialog.setLocation(400, 400);
        dialog.setLayout(new GridLayout(1, 1));
        dialog.add(label);

        dialog.setVisible(true);
        return label;
    }

}
