package cn.panshihao.pos.view;

import org.eclipse.swt.widgets.Display;


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
