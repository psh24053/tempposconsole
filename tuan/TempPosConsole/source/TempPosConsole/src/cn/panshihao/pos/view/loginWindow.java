package cn.panshihao.pos.view;


import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class loginWindow extends superWindow {

	
	public Shell login_shell;
	public String login_title = "登录";
	
	// Label
	public Label login_username_label = null;
	public Label login_password_label = null;
	
	// Text
	public Text login_username_text = null;
	public Text login_password_text = null;
	
	// Button
	public Button login_button = null;
	
	
	
	public loginWindow(Display display) {
		super(display);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		initBase();
		
		initForm();
		
		login_shell.open();
		login_shell.layout();
		
		while (!login_shell.isDisposed()) {
			if (!login_shell.getDisplay().readAndDispatch()) {
				login_shell.getDisplay().sleep();
			}
		}
	}
	/**
	 * 初始化表单
	 */
	private void initForm(){
		
		login_username_label = new Label(login_shell, SWT.NONE);
		login_username_label.setText("账　户");
		login_username_label.setBounds(marginWidthValue * 3, marginHeightValue * 3, marginWidthValue * 5, marginHeightValue * 2);
		login_username_label.setBackground(Color.win32_new(getDisplay(), 255));
		
		login_username_text = new Text(login_shell, SWT.NONE);
//		login_username_text.setBounds(arg0, arg1, arg2, arg3)
	}
	
	/**
	 * 初始化界面基本数据
	 */
	private void initBase(){
		
		login_shell = new Shell(getDisplay(), SWT.CLOSE);
		
		login_shell.setSize(marginWidthValue * 25, marginHeightValue * 20);
		login_shell.setText(login_title);
		login_shell.setLocation(getCenterX(login_shell), getCenterY(login_shell));
		
	}
	

	@Override
	public boolean isDisposed() {
		// TODO Auto-generated method stub
		return false;
	}
	public static void main(String[] args) {
		loginWindow login = new loginWindow(null);
		login.init();
	}

}
