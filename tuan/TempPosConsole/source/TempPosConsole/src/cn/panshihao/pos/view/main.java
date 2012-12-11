package cn.panshihao.pos.view;


import java.io.ObjectInputStream.GetField;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TrayItem;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class main {

	public static final String ApplicationTitle = "TempPosConsole";
	private static Table table;
	private static Table table_1;
	
	
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		final Display display = Display.getDefault();
		
		
		/*
		 * 初始化Shell
		 */
		Shell shell = new Shell(display, SWT.MIN | SWT.CLOSE | SWT.MAX);
		Rectangle rectangle = Display.getDefault().getPrimaryMonitor().getBounds();
		rectangle.height -= rectangle.height / 22.5;
		shell.setSize(rectangle.width, rectangle.height);
		shell.setBounds(rectangle);
		shell.setText(ApplicationTitle);
		shell.setMaximized(true);
		
		
		
		/*
		 * 为各种单位赋值
		 */
		int shellContentWidth = shell.getBounds().width;
		int shellContentHeight = shell.getBounds().height;
		
		// 边距单位
		int marginValue = shellContentHeight / 90;
		
		
		// 按钮区域的大小
		int buttonContentWidth = shellContentWidth - marginValue * 2;
		int buttonContentHeight = shellContentHeight / 14;
		
		// tab区域的大小
		int tabContentWidth = shellContentWidth - marginValue * 2;
		int tabContentHeight = shellContentHeight - buttonContentHeight - marginValue * 6 ;
		
		/*
		 * 创建menu
		 */
		Menu menu = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menu);
		
		MenuItem mntmNewItem = new MenuItem(menu, SWT.NONE);
		mntmNewItem.setText("设置");
		
		
		Composite composite = new Composite(shell, SWT.NONE);
		composite.setBounds(marginValue, 0, buttonContentWidth, buttonContentHeight);
		
		Button btnNewButton = new Button(composite, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				
				Rectangle displayBounds = display.getPrimaryMonitor().getBounds();
				Shell shell = new Shell(display);
				shell.setSize(300, 200);
				Rectangle shellBounds = shell.getBounds();
				int x = displayBounds.x + (displayBounds.width - shellBounds.width)>>1;
				int y = displayBounds.y + (displayBounds.height - shellBounds.height)>>1;
				shell.setLocation(x, y);
				shell.setText("open shell");
				shell.open();
				shell.layout();
				
			}
		});
		btnNewButton.setBounds(0, 10, 100, 40);
		btnNewButton.setText("创建团购");
		
		Button button = new Button(composite, SWT.NONE);
		button.setText("商家管理");
		button.setBounds(120, 10, 100, 40);
		
		Button button_1 = new Button(composite, SWT.NONE);
		button_1.setText("类别管理");
		button_1.setBounds(240, 10, 100, 40);
		
		Button button_2 = new Button(composite, SWT.NONE);
		button_2.setText("用户管理");
		button_2.setBounds(360, 10, 100, 40);
		
		Button button_3 = new Button(composite, SWT.NONE);
		button_3.setText("工作日志");
		button_3.setBounds(480, 10, 100, 40);
		
		
		
		TabFolder tabFolder = new TabFolder(shell, SWT.NONE);
		tabFolder.setBounds(marginValue, buttonContentHeight + marginValue, tabContentWidth, tabContentHeight);
		
		TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
		tabItem.setText("全部团购");
		
		table = new Table(tabFolder, SWT.BORDER | SWT.FULL_SELECTION);
		tabItem.setControl(table);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		TabItem tabItem_1 = new TabItem(tabFolder, SWT.NONE);
		tabItem_1.setText("New Item");
		
		table_1 = new Table(tabFolder, SWT.BORDER | SWT.FULL_SELECTION);
		tabItem_1.setControl(table_1);
		table_1.setHeaderVisible(true);
		table_1.setLinesVisible(true);
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		shell.open();
		shell.layout();
		
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
}
