package utils;


import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;

import java.util.LinkedList;

import javax.swing.JOptionPane;

import domain.NameSocket;
import domain.ServerThread;
import domain.SocketThread;

public class ListenPort
{
	/**
	 * 检查新连接的昵称是否与已有连接的昵称是否重复
	 * 
	 * @param sockets
	 * @param name
	 * @return false 重复 true 不重复
	 */
	public static boolean checkName(LinkedList<NameSocket> sockets, String name)
	{
		System.out.println(ServerThread.getIntance().getServerNickName() + "待检测昵称" + name);
		if (ServerThread.getIntance().getServerNickName().equals(name))
			return false;
		for (NameSocket socket : sockets)
		{
			if (socket.getName().equals(name))
				return false;
		}
		return true;
	}

	/**
	 * 监听连接
	 * 
	 * @throws IOException
	 */
	public static void listen()
	{
		final LinkedList<NameSocket> sockets = ServerThread.getIntance().getSockets();

		try
		{
			@SuppressWarnings("resource")
			ServerSocket serverSocket = new ServerSocket(12345); //
			while (true)
			{
				final NameSocket socket = new NameSocket(serverSocket.accept());
				System.out.println("有连接");
				new Thread()
				{
					public void run()
					{
						try
						{

							boolean flag = true;

							byte[] buf = new byte[1024];
							InputStream is = socket.getSocket().getInputStream();
							while (flag)
							{
								buf = new byte[1024];
								is.read(buf);
								String[] txts = new String(buf).trim().split("\r\n");
								System.out.println("接收到消息" + new String(buf).trim());
								if (txts[0].equals("@NickName@") && checkName(sockets, txts[1]))
								{
									sockets.add(socket);
									socket.setName(txts[1]);
									new SocketThread(socket).start();
									socket.getSocket().getOutputStream().write("1".getBytes());
									System.out.println("通过验证"+socket.getName()+"进入房间");
									flag = false;
								} else
									socket.getSocket().getOutputStream().write("-1".getBytes());
							}
						} catch (Exception e)
						{
							e.printStackTrace();
							System.out.println("ListenPort.listen");
						}
					}
				}.start();

			}
		} catch (Exception e)
		{
			JOptionPane.showMessageDialog(null, "创建房间发生错误"+e.getMessage(), "错误", 0);
			System.exit(0);
		}
	}
}
