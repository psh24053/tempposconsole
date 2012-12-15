package cn.panshihao.pos.view;


import org.apache.commons.codec.digest.DigestUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.internal.win32.MEASUREITEMSTRUCT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import cn.panshihao.pos.dao.UserDAO;
import cn.panshihao.pos.handler.AsyncHandler;
import cn.panshihao.pos.model.User;

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
		
		initDataFromCache();
		
		login_shell.open();
		login_shell.layout();
		
		while (!login_shell.isDisposed()) {
			if (!login_shell.getDisplay().readAndDispatch()) {
				login_shell.getDisplay().sleep();
			}
		}
	}
	/**
	 * 从缓存加载数据
	 */
	private void initDataFromCache(){
		
		login_username_text.setText(cacheHandler.getBaseString("lastLoginUserName", ""));
		
	}
	
	/**
	 * 初始化表单
	 */
	private void initForm(){
		
		LoginKeyListener listener = new LoginKeyListener();
		
		login_username_label = new Label(login_shell, SWT.NONE);
		login_username_label.setText("账　户");
		login_username_label.setBounds(marginWidthValue * 4, marginHeightValue * 3, marginWidthValue * 4, marginHeightValue * 2);
		
		login_username_text = new Text(login_shell, SWT.BORDER);
		login_username_text.setBounds(login_username_label.getBounds().x + login_username_label.getBounds().width,(int) (marginHeightValue * 2.8), marginWidthValue * 12, (int) (marginHeightValue * 3));
		login_username_text.addKeyListener(listener);

		login_password_label = new Label(login_shell, SWT.NONE);
		login_password_label.setText("密　码");
		login_password_label.setBounds(marginWidthValue * 4, marginHeightValue * 10, marginWidthValue * 4, marginHeightValue * 2);
		
		login_password_text = new Text(login_shell, SWT.BORDER | SWT.PASSWORD);
		login_password_text.setBounds(login_password_label.getBounds().x + login_password_label.getBounds().width,(int) (marginHeightValue * 9.8), marginWidthValue * 12, (int) (marginHeightValue * 3));
		login_password_text.addKeyListener(listener);
		
		login_button = new Button(login_shell, SWT.NONE);
		login_button.setText("登陆");
		login_button.setBounds((int)(marginWidthValue * 6.5), login_password_text.getBounds().y + marginHeightValue * 8, marginWidthValue * 12, marginHeightValue * 5);
		
		login_button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				
				validateLogin();
			}
		});
		
	}
	/**
	 * 登陆键盘按键的监听器
	 * @author shihao
	 *
	 */
	private class LoginKeyListener extends KeyAdapter{
		@Override
		public void keyPressed(KeyEvent e) {
			// TODO Auto-generated method stub
			if(e.keyCode == 13){
				validateLogin();
			}
		}
	}
	/**
	 * 初始化界面基本数据
	 */
	private void initBase(){
		
		login_shell = new Shell(getDisplay(), SWT.CLOSE);
		
		login_shell.setSize(marginWidthValue * 25, marginHeightValue * 30);
		login_shell.setText(login_title);
		login_shell.setLocation(getCenterX(login_shell), getCenterY(login_shell));
		
	}
	/**
	 * 检查登陆
	 */
	private void validateLogin(){
		String username = login_username_text.getText();
		String password = login_password_text.getText();
		
		if(username == null || username.length() < 4 || username.length() > 20){
			alert(login_shell, "登陆错误", "账户不能为空，并且不能少于4位以及不能大于20位");
			return;
		}
		
		if(password == null || password.length() < 4 || password.length() > 20){
			
			alert(login_shell, "登陆错误", "密码不能为空，并且不能少于4位以及不能大于20位");
			return;
		}
		
		String[] params = {username, password};
		new validationLoginAsyncHandler(this).start(params);
	}

	@Override
	public boolean isDisposed() {
		// TODO Auto-generated method stub
		return false;
	}
	
	/**
	 * 验证登陆的AsyncHandler
	 * @author shihao
	 *
	 */
	private class validationLoginAsyncHandler extends AsyncHandler<String, Integer, User>{

		public validationLoginAsyncHandler(superWindow window) {
			super(window);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onBefore() {
			// TODO Auto-generated method stub
			super.onBefore();
			
			login_button.setText("正在登陆...");
			login_button.setEnabled(false);
		}
		
		@Override
		public User doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			String username = params[0];
			String password = params[1];
			UserDAO userdao = new UserDAO();
			
			return userdao.checkUserLogin(username, DigestUtils.md5Hex(password));
		}
		
		@Override
		public void onComplete(User result) {
			// TODO Auto-generated method stub
			super.onComplete(result);
			
			if(result != null){
				
				cacheMap.put("curUser", result);
				login_shell.setVisible(false);
				cacheHandler.putString("lastLoginUserName", result.getUser_name()).CommitBaseCache();
				login_button.setText("登陆");
				login_button.setEnabled(true);
				
				new mainWindow(loginWindow.this).show();
				
			}else{
				login_button.setText("登陆");
				login_button.setEnabled(true);
				alert(login_shell, "登陆错误", "账户或密码错误");
			}
			
			
			
		}
		
	}

}
