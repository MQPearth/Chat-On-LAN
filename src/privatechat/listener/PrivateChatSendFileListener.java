package privatechat.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.filechooser.FileFilter;

import javax.swing.JFileChooser;


import domain.ClientThread;
import domain.ServerThread;
import privatechat.ui.PrivateChatUI;

public class PrivateChatSendFileListener implements ActionListener
{

	public static File file;
	private String title;// 对方id
	private PrivateChatUI privateChat;

	public PrivateChatSendFileListener(String title, PrivateChatUI privateChat)
	{
		this.title = title;
		this.privateChat = privateChat;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		System.out.println("发送文件");
		JFileChooser fc = new JFileChooser();
		fc.setFileFilter(new FileFilter()
		{
			@Override
			public String getDescription()
			{
				// 显示哪种类型的
				return ".*";
			}

			@Override
			public boolean accept(File f)
			{
				return true;
			}
		});
		int returnVal = fc.showOpenDialog(privateChat);	//返回的状态
		file = fc.getSelectedFile();
		
		
		if (returnVal == JFileChooser.APPROVE_OPTION)
		{
			if (ServerThread.getIntance().getServerNickName() == null) // 服务端昵称为空
			{
				System.out.println("连接端发送文件提醒");
				ClientThread.getInstance().send(
						"@FileToOne@\r\n" + title + "\r\n" + ClientThread.getInstance().getClientName() + "\r\n",
						file.getName() + "\r\n" + file.length());
				System.out.println("FileUtils.sendFile 将要发送的文件"+PrivateChatSendFileListener.file);
			} else
			{
				System.out.println("服务端发送文件提醒");
				// ServerThread.getIntance().sendToOne(title,ServerThread.getIntance().getServerNickName(),
				// message);
				ServerThread.getIntance().sendFileMessage(title, ServerThread.getIntance().getServerNickName(),
						file.getName(), new Double(file.length()).toString());
				System.out.println("FileUtils.sendFile 将要发送的文件"+PrivateChatSendFileListener.file);
			}
		}

	}

}
