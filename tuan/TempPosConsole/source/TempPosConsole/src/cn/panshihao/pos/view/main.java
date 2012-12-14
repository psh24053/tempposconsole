package cn.panshihao.pos.view;

import org.eclipse.swt.widgets.Display;


public class main {

	public static final String ApplicationTitle = "TempPosConsole";
	
	
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		
		mainWindow mainWindow = new mainWindow(Display.getDefault());
		mainWindow.init();
	}
}
