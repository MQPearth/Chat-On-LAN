package selectroom.ui;

import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;

import javax.swing.JPanel;

import selectroom.listener.CreateRoom;
import selectroom.listener.EnterRoom;
import utils.SendUDP;


public class SelectRoomUI extends JFrame
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static SelectRoomUI selectRoomUI = null;
	private JComboBox<String> cb;
	private SelectRoomUI()
	{
		setTitle("选择房间");
		setBounds(400, 400, 400, 200);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		setContentPane(panel);
		panel.setLayout(new GridLayout(2, 1));
		String[] rooms=SendUDP.sendUDP();
		if(rooms==null||rooms.length<=0)
			rooms=new String[] {"无在线聊天室"};
		cb = new JComboBox<>(rooms);
		
		JPanel upPanel = new JPanel();
		upPanel.setLayout(new FlowLayout());
	
		JPanel downPanel = new JPanel();
		downPanel.setLayout(new FlowLayout());
		
		JButton buttonEnterRoom = new JButton("进入房间");
		
		upPanel.add(cb);
		upPanel.add(buttonEnterRoom);
		
		JButton buttonCreateRoom = new JButton("创建房间");
		downPanel.add(buttonCreateRoom);
		
		panel.add(upPanel);
		panel.add(downPanel);
		
		buttonEnterRoom.addActionListener(new EnterRoom());
		buttonCreateRoom.addActionListener(new CreateRoom());
		
		
		setVisible(true);
		
	}
	
	public static SelectRoomUI getInstance()
	{
		return SelectRoomUI.selectRoomUI;
	}
	
	public JComboBox<String> getComboBox()
	{
		return cb;
	}
	
	public static void main(String[] args)
	{
		try
		{
			javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e)
		{
		}
		selectRoomUI = new SelectRoomUI();
	}
}
