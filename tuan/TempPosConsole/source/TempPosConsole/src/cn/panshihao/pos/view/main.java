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
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.ExpandBar;

public class main {

	public static final String ApplicationTitle = "TempPosConsole";
	private static Table table;
	private static Table table_1;
	private static Table table_2;
	private static Table table_3;
	private static Table table_4;
	private static Table table_5;
	private static Text text;
	private static Table table_6;
	
	
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
		int buttonContentHeight = shellContentHeight / 15;
		
		// 搜索框大小
		int searchContentWidth = shellContentWidth / 7;
		int searchContentHeight = marginValue * 3;
		
		// tab区域的大小
		int tabContentWidth = shellContentWidth - marginValue * 2;
		int tabContentHeight = shellContentHeight - buttonContentHeight - marginValue * 10 - searchContentHeight ;
		
		// footer content size
		int footerContentWidth = shellContentWidth - marginValue * 2;
		int footerContentHeight = shellContentHeight - tabContentHeight - buttonContentHeight - searchContentHeight - marginValue * 7;
		
		
		/*
		 * 创建menu
		 */
		Menu menu = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menu);
		
		MenuItem mntmNewItem = new MenuItem(menu, SWT.CASCADE);
		mntmNewItem.setText("系统");
		
		
		Composite composite = new Composite(shell, SWT.NONE);
		composite.setBounds(marginValue, 0, buttonContentWidth, buttonContentHeight);
		Menu filemenu = new Menu(shell, SWT.DROP_DOWN);  
		mntmNewItem.setMenu(filemenu);  
        // 在下拉框上创建菜单项Open  
        final MenuItem openItem = new MenuItem(filemenu, SWT.CASCADE);  
        openItem.setText("&Open");  
        // 在shell上创建一个下拉框,并把它加到open菜单上  
        Menu childmenu = new Menu(shell, SWT.DROP_DOWN);  
        openItem.setMenu(childmenu);  
        // 在open菜单上创建一个子菜单child  
        final MenuItem childItem = new MenuItem(childmenu, SWT.PUSH);  
        childItem.setText("&Child");  
        // 在open菜单上创建一个子菜单dialog  
        final MenuItem dialogItem = new MenuItem(childmenu, SWT.PUSH);  
        dialogItem.setText("&Dialog");  
        // 在菜单项之间创建一个分隔符  
        new MenuItem(filemenu, SWT.SEPARATOR);  
        // 在下拉框上创建菜单项Exit  
        MenuItem exitItem = new MenuItem(filemenu, SWT.PUSH);  
        exitItem.setText("&Exit");  
        
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
		tabFolder.setBounds(marginValue, buttonContentHeight + marginValue + searchContentHeight, tabContentWidth, tabContentHeight);
		
		TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
		tabItem.setText("全部团购(56314)");
		
		table = new Table(tabFolder, SWT.BORDER | SWT.FULL_SELECTION);
		tabItem.setControl(table);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		TableColumn tblclmnNewColumn = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn.setWidth(58);
		tblclmnNewColumn.setText("ID");
		
		TableColumn tableColumn_61 = new TableColumn(table, SWT.NONE);
		tableColumn_61.setWidth(100);
		tableColumn_61.setText("团购状态");
		
		TableColumn tableColumn = new TableColumn(table, SWT.NONE);
		tableColumn.setWidth(135);
		tableColumn.setText("团购名称");
		
		TableColumn tableColumn_1 = new TableColumn(table, SWT.NONE);
		tableColumn_1.setWidth(100);
		tableColumn_1.setText("团购类别");
		
		TableColumn tableColumn_2 = new TableColumn(table, SWT.NONE);
		tableColumn_2.setWidth(209);
		tableColumn_2.setText("团购描述");
		
		TableColumn tableColumn_3 = new TableColumn(table, SWT.NONE);
		tableColumn_3.setWidth(161);
		tableColumn_3.setText("商家信息");
		
		TableColumn tableColumn_4 = new TableColumn(table, SWT.NONE);
		tableColumn_4.setWidth(151);
		tableColumn_4.setText("开始时间");
		
		TableColumn tableColumn_6 = new TableColumn(table, SWT.NONE);
		tableColumn_6.setWidth(155);
		tableColumn_6.setText("结束时间");
		
		TableColumn tblclmnNewColumn_1 = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn_1.setWidth(115);
		tblclmnNewColumn_1.setText("团购名额");
		
		TableColumn tableColumn_5 = new TableColumn(table, SWT.NONE);
		tableColumn_5.setWidth(100);
		tableColumn_5.setText("剩余名额");
		
		TableColumn tblclmnNewColumn_2 = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn_2.setWidth(130);
		tblclmnNewColumn_2.setText("可用操作");
		
		TabItem tabItem_1 = new TabItem(tabFolder, SWT.NONE);
		tabItem_1.setText("餐饮团购(10242)");
		
		table_1 = new Table(tabFolder, SWT.BORDER | SWT.FULL_SELECTION);
		table_1.setLinesVisible(true);
		table_1.setHeaderVisible(true);
		tabItem_1.setControl(table_1);
		
		TableColumn tableColumn_7 = new TableColumn(table_1, SWT.NONE);
		tableColumn_7.setWidth(58);
		tableColumn_7.setText("ID");
		
		TableColumn tableColumn_62 = new TableColumn(table_1, SWT.NONE);
		tableColumn_62.setWidth(100);
		tableColumn_62.setText("团购状态");
		
		TableColumn tableColumn_8 = new TableColumn(table_1, SWT.NONE);
		tableColumn_8.setWidth(135);
		tableColumn_8.setText("团购名称");
		
		TableColumn tableColumn_9 = new TableColumn(table_1, SWT.NONE);
		tableColumn_9.setWidth(100);
		tableColumn_9.setText("团购类别");
		
		TableColumn tableColumn_10 = new TableColumn(table_1, SWT.NONE);
		tableColumn_10.setWidth(209);
		tableColumn_10.setText("团购描述");
		
		TableColumn tableColumn_11 = new TableColumn(table_1, SWT.NONE);
		tableColumn_11.setWidth(161);
		tableColumn_11.setText("商家信息");
		
		TableColumn tableColumn_12 = new TableColumn(table_1, SWT.NONE);
		tableColumn_12.setWidth(151);
		tableColumn_12.setText("开始时间");
		
		TableColumn tableColumn_13 = new TableColumn(table_1, SWT.NONE);
		tableColumn_13.setWidth(155);
		tableColumn_13.setText("结束时间");
		
		TableColumn tableColumn_14 = new TableColumn(table_1, SWT.NONE);
		tableColumn_14.setWidth(115);
		tableColumn_14.setText("团购名额");
		
		TableColumn tableColumn_15 = new TableColumn(table_1, SWT.NONE);
		tableColumn_15.setWidth(100);
		tableColumn_15.setText("剩余名额");
		
		TableColumn tableColumn_68 = new TableColumn(table_1, SWT.NONE);
		tableColumn_68.setWidth(130);
		tableColumn_68.setText("可用操作");
		
		
		TabItem tabItem_2 = new TabItem(tabFolder, SWT.NONE);
		tabItem_2.setText("旅游团购(3521)");
		
		table_2 = new Table(tabFolder, SWT.BORDER | SWT.FULL_SELECTION);
		table_2.setLinesVisible(true);
		table_2.setHeaderVisible(true);
		tabItem_2.setControl(table_2);
		
		TableColumn tableColumn_16 = new TableColumn(table_2, SWT.NONE);
		tableColumn_16.setWidth(58);
		tableColumn_16.setText("ID");
		
		TableColumn tableColumn_63 = new TableColumn(table_2, SWT.NONE);
		tableColumn_63.setWidth(100);
		tableColumn_63.setText("团购状态");
		
		TableColumn tableColumn_17 = new TableColumn(table_2, SWT.NONE);
		tableColumn_17.setWidth(135);
		tableColumn_17.setText("团购名称");
		
		TableColumn tableColumn_18 = new TableColumn(table_2, SWT.NONE);
		tableColumn_18.setWidth(100);
		tableColumn_18.setText("团购类别");
		
		TableColumn tableColumn_19 = new TableColumn(table_2, SWT.NONE);
		tableColumn_19.setWidth(209);
		tableColumn_19.setText("团购描述");
		
		TableColumn tableColumn_20 = new TableColumn(table_2, SWT.NONE);
		tableColumn_20.setWidth(161);
		tableColumn_20.setText("商家信息");
		
		TableColumn tableColumn_21 = new TableColumn(table_2, SWT.NONE);
		tableColumn_21.setWidth(151);
		tableColumn_21.setText("开始时间");
		
		TableColumn tableColumn_22 = new TableColumn(table_2, SWT.NONE);
		tableColumn_22.setWidth(155);
		tableColumn_22.setText("结束时间");
		
		TableColumn tableColumn_23 = new TableColumn(table_2, SWT.NONE);
		tableColumn_23.setWidth(115);
		tableColumn_23.setText("团购名额");
		
		TableColumn tableColumn_24 = new TableColumn(table_2, SWT.NONE);
		tableColumn_24.setWidth(100);
		tableColumn_24.setText("剩余名额");
		
		TableColumn tableColumn_69 = new TableColumn(table_2, SWT.NONE);
		tableColumn_69.setWidth(130);
		tableColumn_69.setText("可用操作");
		
		TabItem tabItem_3 = new TabItem(tabFolder, SWT.NONE);
		tabItem_3.setText("酒店团购(24154)");
		
		table_3 = new Table(tabFolder, SWT.BORDER | SWT.FULL_SELECTION);
		table_3.setLinesVisible(true);
		table_3.setHeaderVisible(true);
		tabItem_3.setControl(table_3);
		
		TableColumn tableColumn_25 = new TableColumn(table_3, SWT.NONE);
		tableColumn_25.setWidth(58);
		tableColumn_25.setText("ID");
		
		TableColumn tableColumn_64 = new TableColumn(table_3, SWT.NONE);
		tableColumn_64.setWidth(100);
		tableColumn_64.setText("团购状态");
		
		TableColumn tableColumn_26 = new TableColumn(table_3, SWT.NONE);
		tableColumn_26.setWidth(135);
		tableColumn_26.setText("团购名称");
		
		TableColumn tableColumn_27 = new TableColumn(table_3, SWT.NONE);
		tableColumn_27.setWidth(100);
		tableColumn_27.setText("团购类别");
		
		TableColumn tableColumn_28 = new TableColumn(table_3, SWT.NONE);
		tableColumn_28.setWidth(209);
		tableColumn_28.setText("团购描述");
		
		TableColumn tableColumn_29 = new TableColumn(table_3, SWT.NONE);
		tableColumn_29.setWidth(161);
		tableColumn_29.setText("商家信息");
		
		TableColumn tableColumn_30 = new TableColumn(table_3, SWT.NONE);
		tableColumn_30.setWidth(151);
		tableColumn_30.setText("开始时间");
		
		TableColumn tableColumn_31 = new TableColumn(table_3, SWT.NONE);
		tableColumn_31.setWidth(155);
		tableColumn_31.setText("结束时间");
		
		TableColumn tableColumn_32 = new TableColumn(table_3, SWT.NONE);
		tableColumn_32.setWidth(115);
		tableColumn_32.setText("团购名额");
		
		TableColumn tableColumn_33 = new TableColumn(table_3, SWT.NONE);
		tableColumn_33.setWidth(100);
		tableColumn_33.setText("剩余名额");
		
		TableColumn tableColumn_70 = new TableColumn(table_3, SWT.NONE);
		tableColumn_70.setWidth(130);
		tableColumn_70.setText("可用操作");
		
		TabItem tabItem_4 = new TabItem(tabFolder, SWT.NONE);
		tabItem_4.setText("娱乐团购(15201)");
		
		table_4 = new Table(tabFolder, SWT.BORDER | SWT.FULL_SELECTION);
		table_4.setLinesVisible(true);
		table_4.setHeaderVisible(true);
		tabItem_4.setControl(table_4);
		
		TableColumn tableColumn_34 = new TableColumn(table_4, SWT.NONE);
		tableColumn_34.setWidth(58);
		tableColumn_34.setText("ID");
		
		TableColumn tableColumn_65 = new TableColumn(table_4, SWT.NONE);
		tableColumn_65.setWidth(100);
		tableColumn_65.setText("团购状态");
		
		TableColumn tableColumn_35 = new TableColumn(table_4, SWT.NONE);
		tableColumn_35.setWidth(135);
		tableColumn_35.setText("团购名称");
		
		TableColumn tableColumn_36 = new TableColumn(table_4, SWT.NONE);
		tableColumn_36.setWidth(100);
		tableColumn_36.setText("团购类别");
		
		TableColumn tableColumn_37 = new TableColumn(table_4, SWT.NONE);
		tableColumn_37.setWidth(209);
		tableColumn_37.setText("团购描述");
		
		TableColumn tableColumn_38 = new TableColumn(table_4, SWT.NONE);
		tableColumn_38.setWidth(161);
		tableColumn_38.setText("商家信息");
		
		TableColumn tableColumn_39 = new TableColumn(table_4, SWT.NONE);
		tableColumn_39.setWidth(151);
		tableColumn_39.setText("开始时间");
		
		TableColumn tableColumn_40 = new TableColumn(table_4, SWT.NONE);
		tableColumn_40.setWidth(155);
		tableColumn_40.setText("结束时间");
		
		TableColumn tableColumn_41 = new TableColumn(table_4, SWT.NONE);
		tableColumn_41.setWidth(115);
		tableColumn_41.setText("团购名额");
		
		TableColumn tableColumn_42 = new TableColumn(table_4, SWT.NONE);
		tableColumn_42.setWidth(100);
		tableColumn_42.setText("剩余名额");
		
		TableColumn tableColumn_71 = new TableColumn(table_4, SWT.NONE);
		tableColumn_71.setWidth(130);
		tableColumn_71.setText("可用操作");
		
		TabItem tabItem_5 = new TabItem(tabFolder, SWT.NONE);
		tabItem_5.setText("特产团购(2154)");
		
		table_5 = new Table(tabFolder, SWT.BORDER | SWT.FULL_SELECTION);
		table_5.setLinesVisible(true);
		table_5.setHeaderVisible(true);
		tabItem_5.setControl(table_5);
		
		TableColumn tableColumn_43 = new TableColumn(table_5, SWT.NONE);
		tableColumn_43.setWidth(58);
		tableColumn_43.setText("ID");
		
		TableColumn tableColumn_66 = new TableColumn(table_5, SWT.NONE);
		tableColumn_66.setWidth(100);
		tableColumn_66.setText("团购状态");
		
		TableColumn tableColumn_44 = new TableColumn(table_5, SWT.NONE);
		tableColumn_44.setWidth(135);
		tableColumn_44.setText("团购名称");
		
		TableColumn tableColumn_45 = new TableColumn(table_5, SWT.NONE);
		tableColumn_45.setWidth(100);
		tableColumn_45.setText("团购类别");
		
		TableColumn tableColumn_46 = new TableColumn(table_5, SWT.NONE);
		tableColumn_46.setWidth(209);
		tableColumn_46.setText("团购描述");
		
		TableColumn tableColumn_47 = new TableColumn(table_5, SWT.NONE);
		tableColumn_47.setWidth(161);
		tableColumn_47.setText("商家信息");
		
		TableColumn tableColumn_48 = new TableColumn(table_5, SWT.NONE);
		tableColumn_48.setWidth(151);
		tableColumn_48.setText("开始时间");
		
		TableColumn tableColumn_49 = new TableColumn(table_5, SWT.NONE);
		tableColumn_49.setWidth(155);
		tableColumn_49.setText("结束时间");
		
		TableColumn tableColumn_50 = new TableColumn(table_5, SWT.NONE);
		tableColumn_50.setWidth(115);
		tableColumn_50.setText("团购名额");
		
		TableColumn tableColumn_51 = new TableColumn(table_5, SWT.NONE);
		tableColumn_51.setWidth(100);
		tableColumn_51.setText("剩余名额");
		
		TableColumn tableColumn_72 = new TableColumn(table_5, SWT.NONE);
		tableColumn_72.setWidth(130);
		tableColumn_72.setText("可用操作");
		
		TabItem tabItem_6 = new TabItem(tabFolder, 0);
		tabItem_6.setText("搜索结果(24)");
		
		table_6 = new Table(tabFolder, SWT.BORDER | SWT.FULL_SELECTION);
		table_6.setLinesVisible(true);
		table_6.setHeaderVisible(true);
		tabItem_6.setControl(table_6);
		
		TableColumn tableColumn_52 = new TableColumn(table_6, SWT.NONE);
		tableColumn_52.setWidth(58);
		tableColumn_52.setText("ID");
		
		TableColumn tableColumn_67 = new TableColumn(table_6, SWT.NONE);
		tableColumn_67.setWidth(100);
		tableColumn_67.setText("团购状态");
		
		TableColumn tableColumn_53 = new TableColumn(table_6, SWT.NONE);
		tableColumn_53.setWidth(135);
		tableColumn_53.setText("团购名称");
		
		TableColumn tableColumn_54 = new TableColumn(table_6, SWT.NONE);
		tableColumn_54.setWidth(100);
		tableColumn_54.setText("团购类别");
		
		TableColumn tableColumn_55 = new TableColumn(table_6, SWT.NONE);
		tableColumn_55.setWidth(209);
		tableColumn_55.setText("团购描述");
		
		TableColumn tableColumn_56 = new TableColumn(table_6, SWT.NONE);
		tableColumn_56.setWidth(161);
		tableColumn_56.setText("商家信息");
		
		TableColumn tableColumn_57 = new TableColumn(table_6, SWT.NONE);
		tableColumn_57.setWidth(151);
		tableColumn_57.setText("开始时间");
		
		TableColumn tableColumn_58 = new TableColumn(table_6, SWT.NONE);
		tableColumn_58.setWidth(155);
		tableColumn_58.setText("结束时间");
		
		TableColumn tableColumn_59 = new TableColumn(table_6, SWT.NONE);
		tableColumn_59.setWidth(115);
		tableColumn_59.setText("团购名额");
		
		TableColumn tableColumn_60 = new TableColumn(table_6, SWT.NONE);
		tableColumn_60.setWidth(100);
		tableColumn_60.setText("剩余名额");
		
		TableColumn tableColumn_73 = new TableColumn(table_6, SWT.NONE);
		tableColumn_73.setWidth(130);
		tableColumn_73.setText("可用操作");
		
		text = new Text(shell, SWT.BORDER);
		text.setBounds(63, 60, 241, 23);
		
		Label lblNewLabel = new Label(shell, SWT.NONE);
		lblNewLabel.setBounds(10, 63, 53, 17);
		lblNewLabel.setText("搜索内容");
		
		Button btnNewButton_1 = new Button(shell, SWT.NONE);
		btnNewButton_1.setBounds(326, 60, 80, 27);
		btnNewButton_1.setText("搜索");
		
		Composite composite_1 = new Composite(shell, SWT.NONE);
		composite_1.setBounds(10, buttonContentHeight + marginValue + searchContentHeight + tabContentHeight + marginValue * 1, footerContentWidth, footerContentHeight);
		
		Label label = new Label(composite_1, SWT.NONE);
		label.setText("当前版本 Ver1.0.0");
		label.setBounds(0, 0, 124, 17);
		
		Label label_1 = new Label(composite_1, SWT.NONE);
		label_1.setText("当前角色 操作员(1)");
		label_1.setBounds(155, 0, 124, 17);
		
		Label label_2 = new Label(composite_1, SWT.NONE);
		label_2.setText("系统时间 2012-12-11 15:22");
		label_2.setBounds(310, 0, 166, 17);
		
		
		
		
		
		
		
		
		
		shell.open();
		shell.layout();
		
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
}
