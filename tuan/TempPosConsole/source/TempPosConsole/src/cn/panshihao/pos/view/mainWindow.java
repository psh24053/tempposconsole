package cn.panshihao.pos.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DragDetectEvent;
import org.eclipse.swt.events.DragDetectListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class mainWindow {

	
	public static void main(String[] args) {
		
		Display display = new Display();
		
		Shell shell = new Shell(display, SWT.MIN | SWT.CLOSE | SWT.MAX);
//		shell.setLayout(new FillLayout());
		shell.setBounds(Display.getDefault().getPrimaryMonitor().getBounds());
		shell.setText("Hello World");
		shell.open();
		shell.setMaximized(true);
		
		
		
		while (!shell.isDisposed()) {
		    if (!display.readAndDispatch()){
		    	display.sleep ();
		    }
		}
		
		display.dispose ();
		
	}
	
}
