package cn.panshihao.pos.execute;

import org.eclipse.swt.widgets.Display;

import cn.panshihao.pos.view.loginWindow;


public class main {

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		
		loginWindow login = new loginWindow(Display.getDefault());
		login.show();
		
	}
}
