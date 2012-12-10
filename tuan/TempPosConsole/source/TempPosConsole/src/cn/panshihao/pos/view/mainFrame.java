package cn.panshihao.pos.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

import cn.panshihao.pos.dao.UserDAO;

public class mainFrame extends JFrame {

	
	
	
	public mainFrame() {
		// TODO Auto-generated method stub
		this.setLocation(250, 250);
		this.setLayout(null);
		this.setSize(400, 400);
		this.setTitle("Temp Pos Console");
		this.setVisible(true);
		
		
		JButton jb = new JButton("尼玛");
		jb.setBounds(10, 10, 100, 50);
		jb.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				
				UserDAO dao = new UserDAO();
				dao.addUser(null);
				
			}
		});
		
		this.add(jb);
		
	}
	
	public static void main(String[] args) {
		
		new mainFrame();
		
	}
}
