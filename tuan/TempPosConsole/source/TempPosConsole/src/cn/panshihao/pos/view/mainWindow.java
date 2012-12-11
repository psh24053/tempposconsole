package cn.panshihao.pos.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.HelpEvent;
import org.eclipse.swt.events.HelpListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * 主界面类
 * @author Administrator
 *
 */
public class mainWindow {

	/*
	 * 任务栏标题
	 */
	public String main_title = "TempPosConsole";
	
	public Rectangle main_rectangle = null;

	
	/*
	 * shell组件
	 */
	public Shell main_shell = null;
	
	
	/*
	 * 菜单组件
	 */
	public Menu main_menu = null;
	
	/*
	 * 系统菜单项
	 */
	public MenuItem main_menu_system = null;
	public Menu main_menu_system_menu = null;
	public MenuItem main_menu_system_menu_help = null;
	public MenuItem main_menu_system_menu_helpSeperator = null;
	public MenuItem main_menu_system_menu_exit = null;
	
	/*
	 * 按钮区域
	 */
	public Composite main_button = null;
	
	public Button main_button_createTuan = null;
	public Button main_button_categoryManager = null;
	public Button main_button_firmManager = null;
	public Button main_button_userManager = null;
	public Button main_button_log = null;
	
	
	/*
	 * 搜索区域 
	 */
	public Composite main_search = null;
	
	
	/*
	 * 间隔区域最小单位
	 */
	public int marginWidthValue = 0;
	public int marginHeightValue = 0;
	
	
	/**
	 * 构造方法
	 */
	public mainWindow() {
		init();
	}
	/**
	 * 初始化界面
	 */
	private void init(){
		
		initBase();
		
		initMenu();
		
		initButton();
		
		initSearch();
		
		/*
		 * 显示界面
		 */
		main_shell.open();
		main_shell.layout();
		
		while (!main_shell.isDisposed()) {
			if (!main_shell.getDisplay().readAndDispatch()) {
				main_shell.getDisplay().sleep();
			}
		}
		
	}
	/**
	 * 初始化搜索区域
	 */
	private void initSearch(){
		
	}
	
	/**
	 * 初始化按钮区域
	 */
	private void initButton(){
		
		ButtonSelectionListener selectListener = new ButtonSelectionListener();
		
		main_button = new Composite(main_shell, SWT.NONE);
		
		int buttonWidth = main_shell.getBounds().width - marginWidthValue * 2;
		int buttonHeight = marginHeightValue * 10;
		
		main_button.setBounds(marginWidthValue, 0, buttonWidth, buttonHeight);
		
		main_button_createTuan = new Button(main_button, SWT.NONE);
		main_button_createTuan.setText("创建团购");
		main_button_createTuan.setBounds(0, marginHeightValue, marginWidthValue * 10, marginHeightValue * 4);
		main_button_createTuan.addSelectionListener(selectListener);
		
		main_button_firmManager = new Button(main_button, SWT.NONE);
		main_button_firmManager.setText("商家管理");
		main_button_firmManager.setBounds(marginWidthValue * 10 + marginWidthValue * 2, marginHeightValue, marginWidthValue * 10, marginHeightValue * 4);
		main_button_firmManager.addSelectionListener(selectListener);
		
		main_button_categoryManager = new Button(main_button, SWT.NONE);
		main_button_categoryManager.setText("类别管理");
		main_button_categoryManager.setBounds(marginWidthValue * 20 + marginWidthValue * 4, marginHeightValue, marginWidthValue * 10, marginHeightValue * 4);
		main_button_categoryManager.addSelectionListener(selectListener);
		
		main_button_userManager = new Button(main_button, SWT.NONE);
		main_button_userManager.setText("用户管理");
		main_button_userManager.setBounds(marginWidthValue * 30 + marginWidthValue * 6, marginHeightValue, marginWidthValue * 10, marginHeightValue * 4);
		main_button_userManager.addSelectionListener(selectListener);
		
		main_button_log = new Button(main_button, SWT.NONE);
		main_button_log.setText("工作日志");
		main_button_log.setBounds(marginWidthValue * 40 + marginWidthValue * 8, marginHeightValue, marginWidthValue * 10, marginHeightValue * 4);
		main_button_log.addSelectionListener(selectListener);
		
	}
	
	/**
	 * 初始化界面基本信息
	 */
	private void initBase(){
		this.main_shell = new Shell(Display.getDefault(), SWT.MIN | SWT.CLOSE | SWT.MAX);
		
		this.main_rectangle = this.main_shell.getDisplay().getPrimaryMonitor().getBounds();
		/*
		 * 去掉Windows任务栏的高度
		 */
		main_rectangle.height -= main_rectangle.height / 22.5;
		
		
		marginWidthValue = main_rectangle.width / 100;
		marginHeightValue = main_rectangle.height / 100;
		
		main_shell.setSize(main_rectangle.width, main_rectangle.height);
		main_shell.setBounds(main_rectangle);
		main_shell.setText(main_title);
		main_shell.setMaximized(true);
		
		/*
		 * 为当前界面注册键盘事件
		 */
		main_shell.addKeyListener(new KeyListener() {
			
			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				
				if(e.stateMask == SWT.ALT && e.keyCode == 'q'){
					// Alt + Q
					exitApplication();
				}else if(e.stateMask == SWT.ALT && e.keyCode == SWT.F1){
					// Alt + F1
					openHelp();
				}
				
				
			}
		});
	}
	/**
	 * 初始化Menu菜单组件
	 */
	private void initMenu(){
		
		main_menu = new Menu(main_shell, SWT.BAR);
		main_shell.setMenuBar(main_menu);
		
		
		initMenuItem_System();
		
		
		
		
	}
	/**
	 * 初始化系统菜单项
	 */
	private void initMenuItem_System(){
	
		// main_menu_system菜单项本身
		main_menu_system = new MenuItem(main_menu, SWT.CASCADE);
		main_menu_system.setText("系统");
		
		// main_menu_system菜单项的下拉菜单
		main_menu_system_menu = new Menu(main_shell, SWT.DROP_DOWN);
		main_menu_system.setMenu(main_menu_system_menu);
		
		// 关于菜单项
		main_menu_system_menu_help = new MenuItem(main_menu_system_menu, SWT.CASCADE);
		main_menu_system_menu_help.setText("关于　　Alt + F1");
		main_menu_system_menu_help.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				openHelp();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		// 关于菜单项下面的分割线
		main_menu_system_menu_helpSeperator = new MenuItem(main_menu_system_menu, SWT.SEPARATOR);
		
		// 退出菜单项
		main_menu_system_menu_exit = new MenuItem(main_menu_system_menu, SWT.CASCADE);
		main_menu_system_menu_exit.setText("退出　　Alt + Q");
		main_menu_system_menu_exit.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				exitApplication();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
			}
		});
		
	}
	/**
	 * 退出程序的方法
	 */
	private void exitApplication(){
		System.out.println("dispose application");
		main_shell.dispose();
	}
	/**
	 * 打开关于界面的方法
	 */
	private void openHelp(){
		System.out.println("HelpListener");
	}
	/**
	 * 
	 * 按钮区域的事件监听器
	 * @author Administrator
	 *
	 */
	private class ButtonSelectionListener implements SelectionListener{

		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void widgetSelected(SelectionEvent e) {
			// TODO Auto-generated method stub
			if(e.getSource() instanceof Button){
				Button source = (Button) e.getSource();
				
				switch (source.getText()) {
				case "创建团购":
					System.out.println("创建团购");
					break;

				default:
					break;
				}
				
			}
		}
		
	}
}
