package domain;


import java.util.LinkedList;



import chatroom.ui.ChatRoomUI;
import utils.ListenPort;
import utils.ReceiveUDP;

/**
 * 服务端线程
 * 
 * @author 22x
 *
 */
public class ServerThread extends Thread
{
	private LinkedList<NameSocket> sockets = new LinkedList<>(); // 连接端列表
	private String serverNickName;// 服务端昵称

	private static ServerThread serverThread = null;

	public static boolean refreshFlag = true;

	private ServerThread()
	{

	}

	public void setNickName(String name)
	{
		this.serverNickName = name;
	}

	public static ServerThread getIntance()
	{
		if (serverThread == null)
			ServerThread.serverThread = new ServerThread();

		return ServerThread.serverThread;
	}

	@Override
	public void run()
	{
		// 监听端口，建立连接
		new Thread()
		{
			@Override
			public void run()
			{
				ListenPort.listen();
			}
		}.start();

//		// 在线列表发送线程
//		new Thread()
//		{
//			@Override
//			public void run()
//			{
//				
//			}
//		}.start();

		// 监听UDP广播
		new Thread()
		{
			@Override
			public void run()
			{
				ReceiveUDP.receiveUDP();
			}
		}.start();
	}

	/**
	 * 群发消息
	 * 
	 * @param message
	 *            消息内容
	 */
	public void sendToAll(String fromUser, String message)
	{
		String forword = "@MessageToAll@\r\n" + fromUser + "\r\n" + message;
		for (NameSocket socket : sockets)
		{
			socket.sendMessage(forword);
		}
		ChatRoomUI.getInstance().toScreen(fromUser, message);
	}

	/**
	 * 私聊消息
	 * 
	 * @param toUser
	 *            至 用户
	 * @param fromUser
	 *            来自用户
	 * @param message
	 *            消息内容
	 */
	public void sendToOne(String toUser, String fromUser, String message)
	{

		String forword = "@MessageToOne@\r\n" + fromUser + "\r\n" + message;
		System.out.println("即将私聊的消息" + forword);
		for (NameSocket socket : sockets)
		{
			if (socket.getName().equals(toUser))
			{
				socket.sendMessage(forword);
				return;
			}
		}

	}

	/**
	 * 发送文件前的提醒
	 * 
	 * @param toUser
	 *            至用户
	 * @param fromUser
	 *            来之用户
	 * @param fileName
	 *            文件名
	 * @param fileSize
	 *            文件大小
	 */
	public void sendFileMessage(String toUser, String fromUser, String fileName, String fileSize)
	{
		String forword = "@FileToOne@\r\n" + toUser + "\r\n" + fromUser + "\r\n" + fileName + "\r\n" + fileSize;
		for (NameSocket socket : sockets)
		{
			if (socket.getName().equals(toUser))
			{
				socket.sendMessage(forword);
				break;
			}
		}
	}

	/**
	 * 拒绝接收文件
	 * 
	 * @param toUser
	 *            至用户
	 * @param isRec
	 *            是否接收
	 */
	public void sendFileResponse(String toUser, String isRec)
	{
		String forword = "@SendFileResponse@\r\n"+isRec+"\r\n" + toUser;
		System.out.println("即将回复文件请求" + forword);
		for (NameSocket socket : sockets)
		{
			if (socket.getName().equals(toUser))
			{
				socket.sendMessage(forword);
				return;
			}
		}
	}

	/**
	 * 同意接收
	 * 
	 * @param toUser
	 *            至用户
	 * @param isRec
	 *            是否接收
	 * @param ip
	 *            本机ip
	 */
	public void sendFileResponse(String toUser, String isRec, String ip)
	{
		String forword = "@SendFileResponse@\r\n"+isRec+"\r\n" + toUser + "\r\n" + ip;
		System.out.println("即将回复文件请求" + forword);
		for (NameSocket socket : sockets)
		{
			if (socket.getName().equals(toUser))
			{
				socket.sendMessage(forword);
				return;
			}
		}
	}

	public void sendOnLineList()
	{

		StringBuffer message = new StringBuffer("@OnLineList@\r\n" + serverNickName);

		// 获取在线列表
		for (NameSocket socket : sockets)
		{
			message.append("&" + socket.getName());
		}

		System.out.println("即将发送的列表" + message.toString());
		// 遍历发送
		for (NameSocket socket : sockets)
		{
			socket.sendMessage(message.toString());
		}

	}

	/**
	 * 将消息显示到屏幕
	 */
//	public static void toScreen(String nickName, String message)
//	{
//		ChatRoomUI.getInstance().getChatRecordTextArea().append(nickName + " ： " + message + "\n");
//		int height= 10999 ;   
//	    Point p = new  Point();   
//	    p.setLocation(0 , ChatRoomUI.getInstance().getChatRecordTextArea().getLineCount()*height);   
//	    ChatRoomUI.getInstance().getSp().getViewport().setViewPosition(p);
//	}  
	

	public LinkedList<NameSocket> getSockets()
	{
		return sockets;
	}

	public String getServerNickName()
	{
		return serverNickName;
	}

	public void setServerNickName(String serverNickName)
	{
		this.serverNickName = serverNickName;
	}

}
