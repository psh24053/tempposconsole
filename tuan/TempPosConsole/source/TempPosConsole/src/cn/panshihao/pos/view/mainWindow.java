package cn.panshihao.pos.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.HelpEvent;
import org.eclipse.swt.events.HelpListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.panshihao.pos.dao.CategoryDAO;
import cn.panshihao.pos.dao.TuanDAO;
import cn.panshihao.pos.handler.AsyncHandler;
import cn.panshihao.pos.model.Category;
import cn.panshihao.pos.model.Firm;
import cn.panshihao.pos.model.Key;
import cn.panshihao.pos.model.Tuan;
import cn.panshihao.pos.model.User;
import cn.panshihao.pos.tools.PosLogger;
import cn.panshihao.pos.tools.TransDate;

/**
 * 主界面类
 * @author Administrator
 *
 */
public class mainWindow extends superWindow {

	public mainWindow(Display display) {
		super(display);
		// TODO Auto-generated constructor stub
	}
	public mainWindow(superWindow parent){
		super(parent);
	}
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
	public MenuItem main_menu_system_menu_printer = null;
	public MenuItem main_menu_system_menu_logout = null;
	
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
	public Label main_search_label = null;
	public Text main_search_text = null;
	public Button main_search_button = null;
	public Combo main_search_combo = null;
	public TabItem main_tab_search = null;
	
	
	/*
	 * 间隔区域最小单位
	 */
	public int marginWidthValue = 0;
	public int marginHeightValue = 0;
	
	
	/*
	 * Tab区域
	 */
	public TabFolder main_tab = null;
	public TabItem main_tab_all = null;
	
	
	/*
	 * Footer区域 
	 */
	public Composite main_footer = null;
	public Label main_footer_version = null;
	public Label main_footer_user = null;
	public Label main_footer_time = null;
	
	public Table all_table = null;
	
	public superWindow printerSettingsWindow;
	
	
	private boolean logout = false;
	
	private User user;
	
	/**
	 * 初始化界面
	 */
	@Override
	public void init(){
		
		initBase();
		
		initMenu();
		
		initButton();
		
		initSearch();
		
		initTabs();
		
		initFooter();
		
		initData();
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
	 * 加载数据
	 */
	private void initData(){
		
		user = (User) cacheMap.get("curUser");
		
		String userValue = "当前用户 ";
		
		if(user.getUser_grade() == 1){
			userValue += "管理员("+user.getUser_name()+")";
		}else{
			userValue += "操作员("+user.getUser_name()+")";
		}
		
		main_footer_user.setText(userValue);
		
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				
				while(!isDisposed()){
					final String timeValue = "当前时间 "+ TransDate.convertTime(System.currentTimeMillis());;
					
					
					getDisplay().asyncExec(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							main_footer_time.setText(timeValue);
						}
					});
					
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
		}).start();
		
	}
	/**
	 * 初始化footer
	 */
	private void initFooter(){
		
		main_footer = new Composite(main_shell, SWT.NONE);
		
		int footerWidth = main_shell.getBounds().width - marginWidthValue * 2;
		int footerHeight = marginHeightValue * 4;
		
		main_footer.setBounds(marginWidthValue, main_tab.getBounds().y + main_tab.getBounds().height + marginHeightValue, footerWidth, footerHeight);
		
		main_footer_version = new Label(main_footer, SWT.NONE);
		main_footer_version.setText("当前版本 Ver1.0.0");
		main_footer_version.setBounds(0, marginHeightValue, marginWidthValue * 10, marginHeightValue * 2);
		
		main_footer_user = new Label(main_footer, SWT.NONE);
		main_footer_user.setText("当前用户 ");
		main_footer_user.setBounds(main_footer_version.getBounds().x + main_footer_version.getBounds().width + marginWidthValue, marginHeightValue, marginWidthValue * 13, marginHeightValue * 2);
		
		main_footer_time = new Label(main_footer, SWT.NONE);
		main_footer_time.setText("当前时间 ");
		main_footer_time.setBounds(main_footer_user.getBounds().x + main_footer_user.getBounds().width + marginWidthValue, marginHeightValue, marginWidthValue * 14, marginHeightValue * 2);
		
		
		
	}
	
	/**
	 * 初始化tabs区域
	 */
	private void initTabs(){
		
		main_tab = new TabFolder(main_shell, SWT.NONE);
		
		int tabWidth = main_shell.getBounds().width - marginWidthValue * 2;
		int tabHeight = marginHeightValue * 80;
		
		main_tab.setBounds(marginWidthValue, main_search.getBounds().y + main_search.getBounds().height + marginHeightValue, tabWidth, tabHeight);
		
		
		new loadAllTuanAsyncHandler(This()).start("");
		
	}
	/**
	 * 加载所有类别的asynchandler
	 * @author Administrator
	 *
	 */
	private class loadAllCategoryAsyncHandler extends AsyncHandler<String, Integer, JSONObject>{

		private String tab;
		
		public loadAllCategoryAsyncHandler(superWindow window) {
			super(window);
			// TODO Auto-generated constructor stub
		}

		@Override
		public JSONObject doInBackground(String... params) {
			// TODO Auto-generated method stub
			tab = params[0];
			
			CategoryDAO dao = new CategoryDAO();
			
			
			return dao.getAllCategory(0, 1000);
		}
		
		@Override
		public void onComplete(JSONObject result) {
			// TODO Auto-generated method stub
			super.onComplete(result);
			
			if(result != null){
				try {
					changeCategoryTab(result);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			
		}
		
	}
	/**
	 * 改变类别tab
	 * @param data
	 * @throws JSONException 
	 */
	private void changeCategoryTab(JSONObject data) throws JSONException{
		TabItem[] items = main_tab.getItems();
		
		for(int i = 0 ; i < items.length ; i ++){
			TabItem item = items[i];
			
			if(item.isDisposed()){
				continue;
			}
			
			if(item.equals(main_tab_all)){
				continue;
			}else{
				item.dispose();
			}
			
		}
		
		JSONArray list = data.getJSONArray("list");
		
		for(int i = 0 ; i < list.length() ; i ++){
			JSONObject item = list.getJSONObject(i);
			Category c = new Category();
			c.setCategory_desc(item.getString("desc"));
			c.setCategory_id(item.getInt("cid"));
			c.setCategory_name(item.getString("name"));
			new loadCategoryTuanAsyncHandler(This()).start(c);
			
		}
		
		
	}
	/**
	 * 根据类别加载团购信息
	 * @author Administrator
	 *
	 */
	private class loadCategoryTuanAsyncHandler extends AsyncHandler<Category, Integer, JSONObject>{

		private Category category;
		
		public loadCategoryTuanAsyncHandler(superWindow window) {
			super(window);
			// TODO Auto-generated constructor stub
		}

		@Override
		public JSONObject doInBackground(Category... params) {
			// TODO Auto-generated method stub
			TuanDAO dao = new TuanDAO();
			this.category = params[0];
			return dao.getTuanByCategoryID(params[0].getCategory_id(), 0, 100000);
		}
		@Override
		public void onComplete(JSONObject result) {
			// TODO Auto-generated method stub
			super.onComplete(result);
			
			if(result != null){
				try {
					changeCategoryTuanData(result, category);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			
		}
		
		
	}
	/**
	 * 将传入的data设置到table中
	 * @param data
	 * @param table
	 * @throws JSONException 
	 */
	private void changeCategoryTuanData(JSONObject data, Category category) throws JSONException{
		JSONArray list = data.getJSONArray("list");
		
		TabItem tabitem = new TabItem(main_tab, SWT.NONE);
		tabitem.setData(category.getCategory_id()+category.getCategory_name());
		
		Table table = createTable(main_tab, tabitem);
		tabitem.setText(category.getCategory_name()+"("+list.length()+")");
		
		for(int i = 0 ; i < list.length() ; i ++){
			Tuan tuan = new Tuan();
			JSONObject json = list.getJSONObject(i);
			
			
			int id = json.getInt("tid");
			String name = json.getString("name");
			String desc = json.getString("desc");
			String firm = json.getString("firm");
			String cat = json.getString("cat");
			String sta = TransDate.convertTime(json.getLong("sta"));
			String end = TransDate.convertTime(json.getLong("end"));
			int count = json.getInt("count");
			int remain = json.getInt("remain");
			
			tuan.setTuan_id(id);
			tuan.setTuan_desc(desc);
			tuan.setTuan_name(name);
			
			TableItem item = new TableItem(table, SWT.NONE);
			item.setData(tuan);
			item.setText(new String[]{id+"", name, desc, firm, cat, sta, end, count+"", remain+""});
			
			
		}
	}
	/**
	 * 加载全部团购数据的asynchandler
	 * @author Administrator
	 *
	 */
	private class loadAllTuanAsyncHandler extends AsyncHandler<String, Integer, JSONObject>{

		private String tab;
		
		public loadAllTuanAsyncHandler(superWindow window) {
			super(window);
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public void onBefore() {
			// TODO Auto-generated method stub
			super.onBefore();
			
			if(all_table != null){
				all_table.removeAll();
			}
			
		}
		
		@Override
		public JSONObject doInBackground(String... params) {
			// TODO Auto-generated method stub
			tab = params[0];
			
			TuanDAO dao = new TuanDAO();
			
			return dao.getAllTuan(0, 100000);
		}
		
		@Override
		public void onComplete(JSONObject result) {
			// TODO Auto-generated method stub
			super.onComplete(result);
			
			if(result != null){
				
				try {
					changeAllTuanData(result);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				new loadAllCategoryAsyncHandler(This()).start(tab);
			}
			
		}
		
	}
	
	
	/**
	 * 创建一个table对象
	 * @param tab
	 * @return
	 */
	private Table createTable(Composite tab, final TabItem tabitem){
		final Table table = new Table(tab, SWT.FULL_SELECTION);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				// TODO Auto-generated method stub
				if(e.button == 3){
					// item右键事件
					final int selectionIndex = table.getSelectionIndex();
					
					//如果selectionIndex为-1则代表没有选中任何元素
					if(selectionIndex == -1){
						return;
					}
					final TableItem setectionItem = table.getSelection()[0];
					final Tuan tuan = (Tuan) setectionItem.getData();
					
					
					Menu menu = new Menu(table);
					table.setMenu(menu);
					
					MenuItem item_Info = new MenuItem(menu, SWT.PUSH);  
					item_Info.setText("ID:"+tuan.getTuan_id()+" 团购名称："+tuan.getTuan_name());  
					item_Info.addSelectionListener(new SelectionAdapter() {
						@Override
						public void widgetSelected(SelectionEvent e) {
							// TODO Auto-generated method stub
							viewTuanInfo(tuan);
						}
					});
					
					MenuItem item_key = new MenuItem(menu, SWT.PUSH);  
					item_key.setText("提取兑换码");  
					item_key.addSelectionListener(new SelectionAdapter() {
						@Override
						public void widgetSelected(SelectionEvent e) {
							// TODO Auto-generated method stub
							viewTuanInfo(tuan);
						}
					});
					
					MenuItem item_modify = new MenuItem(menu, SWT.PUSH);  
					item_modify.setText("编辑该团购");  
					item_modify.addSelectionListener(new SelectionAdapter() {
						@Override
						public void widgetSelected(SelectionEvent arg0) {
							// TODO Auto-generated method stub
							modifyTableItem(tuan, selectionIndex);
							
						}
					});
					
					
					MenuItem item_delete = new MenuItem(menu, SWT.PUSH);  
					item_delete.setText("删除该团购");  
					item_delete.addSelectionListener(new SelectionAdapter() {
						@Override
						public void widgetSelected(SelectionEvent arg0) {
							// TODO Auto-generated method stub
							
							if(confirm(getShell(), "删除提示", "你确定要删除ID："+tuan.getTuan_id()+" ,名称："+tuan.getTuan_name()+" 的团购信息吗？")){
								deleteTableItem(tuan, tabitem, selectionIndex);
							}
							
						}
					});
					
					MenuItem item_addkey = new MenuItem(menu, SWT.PUSH);
					item_addkey.setText("增加兑换码");
					item_addkey.addSelectionListener(new SelectionAdapter() {
						@Override
						public void widgetSelected(SelectionEvent e) {
							// TODO Auto-generated method stub
							new keyWindow(This(), new onResultListener<Key>() {
								
								@Override
								public void onResult(Key result) {
									// TODO Auto-generated method stub
									new loadAllTuanAsyncHandler(This()).start("");
								}
							}, tuan.getTuan_id()).show();
						}
					});
				}
				
				
			}
			
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				// TODO Auto-generated method stub
				// item右键事件
				final int selectionIndex = table.getSelectionIndex();
				
				//如果selectionIndex为-1则代表没有选中任何元素
				if(selectionIndex == -1){
					return;
				}
				final TableItem setectionItem = table.getSelection()[0];
				final Tuan tuan = (Tuan) setectionItem.getData();
				
				viewTuanInfo(tuan);
			}
		});
		
		TableColumn idColumn = new TableColumn(table, SWT.CENTER); 
		idColumn.setText("ID");
		idColumn.setWidth(marginWidthValue * 3);
		
		TableColumn nameColumn = new TableColumn(table, SWT.CENTER); 
		nameColumn.setText("团购名称");
		nameColumn.setWidth(marginWidthValue * 10);
		
		TableColumn descColumn = new TableColumn(table, SWT.CENTER); 
		descColumn.setText("团购描述");
		descColumn.setWidth(marginWidthValue * 18);
		
		TableColumn firmColumn = new TableColumn(table, SWT.CENTER); 
		firmColumn.setText("商家名称");
		firmColumn.setWidth(marginWidthValue * 10);
		
		TableColumn categoryColumn = new TableColumn(table, SWT.CENTER); 
		categoryColumn.setText("团购类别");
		categoryColumn.setWidth(marginWidthValue * 10);
		
		TableColumn startColumn = new TableColumn(table, SWT.CENTER); 
		startColumn.setText("团购开始时间");
		startColumn.setWidth(marginWidthValue * 10);
		
		TableColumn endColumn = new TableColumn(table, SWT.CENTER); 
		endColumn.setText("团购结束时间");
		endColumn.setWidth(marginWidthValue * 10);
		
		TableColumn keyTotalColumn = new TableColumn(table, SWT.CENTER); 
		keyTotalColumn.setText("兑换码总数");
		keyTotalColumn.setWidth(marginWidthValue * 10);
		
		TableColumn keyOverColumn = new TableColumn(table, SWT.CENTER); 
		keyOverColumn.setText("兑换码剩余");
		keyOverColumn.setWidth(marginWidthValue * 10);
		
		
		tabitem.setControl(table);
		
		return table;
	}
	/**
	 * 打开团购详情界面
	 * @param tuan
	 */
	private void viewTuanInfo(Tuan tuan){
		new tuanInfoWindow(This(), tuan).show();
	}
	/**
	 * 删除表格元素
	 * @param tuan
	 * @param tab
	 * @param index
	 */
	private void deleteTableItem(Tuan tuan, TabItem item, int index){
		
		new deleteTuanAsyncHandler(This(), item, index).start(tuan);
		
	}
	/**
	 * 删除团购的asynchandler
	 * @author shihao
	 *
	 */
	private class deleteTuanAsyncHandler extends AsyncHandler<Tuan, Integer, Boolean>{

		private TabItem item;
		private int index;
		
		public deleteTuanAsyncHandler(superWindow window, TabItem item, int index) {
			super(window);
			// TODO Auto-generated constructor stub
			this.item = item;
			this.index = index;
		}

		@Override
		public Boolean doInBackground(Tuan... params) {
			// TODO Auto-generated method stub
			TuanDAO dao = new TuanDAO();
			
			return dao.deleteTuan(params[0].getTuan_id(), getCurUser().getUser_id());
		}
		@Override
		public void onComplete(Boolean result) {
			// TODO Auto-generated method stub
			super.onComplete(result);
			
			if(result != null && result){
				
				String tab = (String) item.getData();
				
				new loadAllTuanAsyncHandler(This()).start(tab);
			}
			
		}
		
	}
	/**
	 * 编辑表格元素
	 * @param tuan
	 * @param index
	 */
	private void modifyTableItem(Tuan tuan, int index){
		new modifyTuanWindow(This(), tuan, new onResultListener<Tuan>() {
			
			@Override
			public void onResult(Tuan result) {
				// TODO Auto-generated method stub
				new loadAllTuanAsyncHandler(This()).start("");
			}
		}).show();
	}
	
	/**
	 * 根据传入的数据改变界面
	 * @param data
	 * @throws JSONException 
	 */
	private void changeAllTuanData(JSONObject data) throws JSONException{
		
		JSONArray list = data.getJSONArray("list");
		
		
		if(main_tab_all == null){
			main_tab_all = new TabItem(main_tab, SWT.NONE);
			all_table = createTable(main_tab, main_tab_all);
			
		}
		main_tab_all.setText("全部团购("+list.length()+")");
		
		for(int i = 0 ; i < list.length() ; i ++){
			Tuan tuan = new Tuan();
			JSONObject json = list.getJSONObject(i);
			
			int id = json.getInt("tid");
			String name = json.getString("name");
			String desc = json.getString("desc");
			String firm = json.getString("firm");
			String cat = json.getString("cat");
			String sta = TransDate.convertTime(json.getLong("sta"));
			String end = TransDate.convertTime(json.getLong("end"));
			int count = json.getInt("count");
			int remain = json.getInt("remain");
			
			tuan.setTuan_id(id);
			tuan.setTuan_desc(desc);
			tuan.setTuan_name(name);
			
			TableItem item = new TableItem(all_table, SWT.NONE);
			item.setData(tuan);
			item.setText(new String[]{id+"", name, desc, firm, cat, sta, end, count+"", remain+""});
			
			
		}
		
		
		
	} 
	
	
	/**
	 * 初始化搜索区域
	 */
	private void initSearch(){
		
		main_search = new Composite(main_shell, SWT.NONE);
		
		
		int searchWidth = main_shell.getBounds().width - marginWidthValue * 2;
		int searchHeight = marginHeightValue * 5;
		
		main_search.setBounds(marginWidthValue, main_button.getBounds().y + main_button.getBounds().height + marginHeightValue, searchWidth, searchHeight);
//		main_search.setBackground(new Color(main_shell.getDisplay(), new RGB(0xff, 0x00, 0x00)));
		
//		main_search_label = new Label(main_search, SWT.NONE);
//		main_search_label.setText("搜索内容");
//		main_search_label.setBounds(0, (int)(marginHeightValue * 1.5), marginWidthValue * 4, marginHeightValue * 2);
		
		main_search_combo = new Combo(main_search, SWT.READ_ONLY);
		main_search_combo.setBounds(0, marginHeightValue, marginWidthValue * 8, marginHeightValue * 2);
		main_search_combo.setItems(new String[]{"团购名称","团购描述","商家名称"});
		main_search_combo.select(0);
		
		main_search_text = new Text(main_search, SWT.BORDER);
		main_search_text.setBounds(main_search_combo.getBounds().width + marginWidthValue , main_search_combo.getBounds().y, marginWidthValue * 20, (int)(marginHeightValue * 3.5));
		
		main_search_button = new Button(main_search, SWT.NONE);
		main_search_button.setText("搜索");
		main_search_button.setBounds(main_search_text.getBounds().x + main_search_text.getBounds().width + marginWidthValue, (int)(marginHeightValue * 1), marginWidthValue * 5, (int)(marginHeightValue * 3.5));
		main_search_button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				searchTuan();
			}
		});
	}
	/**
	 * 搜索团购信息
	 */
	private void searchTuan(){
		String comboValue = main_search_combo.getText();
		String keywordValue = main_search_text.getText();
		
		if(keywordValue == null || keywordValue.equals("")){
			alert(getShell(), "搜索错误", "请输入关键字！");
			return;
		}
		
		
		
		new searchTuanAsyncHandler(This(), keywordValue).start(comboValue);
		
	}
	/**
	 * 搜索团购信息
	 * @author shihao
	 *
	 */
	private class searchTuanAsyncHandler extends AsyncHandler<String, Integer, JSONObject>{

		private String keyword;
		
		public searchTuanAsyncHandler(superWindow window, String keyword) {
			super(window);
			// TODO Auto-generated constructor stub
			this.keyword = keyword;
		}

		@Override
		public void onBefore() {
			// TODO Auto-generated method stub
			super.onBefore();
			
			
			
			
		}
		
		@Override
		public JSONObject doInBackground(String... params) {
			// TODO Auto-generated method stub
			TuanDAO dao = new TuanDAO();
			
			switch (params[0]) {
			case "团购名称":
				return dao.getQueryTuanByTuanName(keyword, 0, 10000);
			case "团购描述":
				return dao.getQueryTuanByTuanDesc(keyword, 0, 10000);
			case "商家名称":
				return dao.getQueryTuanByFirmName(keyword, 0, 10000);
			default:
				break;
			}
			
			
			return null;
		}
		
		@Override
		public void onComplete(JSONObject result) {
			// TODO Auto-generated method stub
			super.onComplete(result);
			
			if(result != null){
				try {
					changeSearchData(result);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
	}
	/**
	 * 根据tab，设置tab的选中项
	 * @param tab
	 */
	private void setTabFocus(String tab){
		
		TabItem[] items = main_tab.getItems();
		int modify_index = -1;
		for(int i = 0 ; i < items.length ; i ++){
			TabItem item = items[i];
			if(item.getData() != null && item.getData().equals(tab)){
				modify_index = i;
				break;
			}
		}
		
		if(modify_index != -1){
			
			main_tab.setSelection(modify_index);
		}
	}
	/**
	 * 改变搜索数据
	 * @param data
	 * @throws JSONException 
	 */
	private void changeSearchData(JSONObject data) throws JSONException{
		
		JSONArray list = data.getJSONArray("list");
		
		System.out.println(list.toString());
		
		
		if(list.length() < 1){
			alert(getShell(), "提示", "没有搜索到团购信息");
			return;
		}
		
		Table table = null;
		
		if(main_tab_search == null){
			main_tab_search = new TabItem(main_tab, SWT.NONE);
			table = createTable(main_tab, main_tab_search);
			main_tab_search.setData("main_tab_search");
			
		}else{
			table = (Table) main_tab_search.getControl();
			table.removeAll();
		}
		
		main_tab_search.setText("搜索结果("+list.length()+")");
		setTabFocus("main_tab_search");
		
		
		for(int i = 0 ; i < list.length() ; i ++){
			Tuan tuan = new Tuan();
			JSONObject json = list.getJSONObject(i);
			
			int id = json.getInt("tid");
			String name = json.getString("name");
			String desc = json.getString("desc");
			String firm = json.getString("firm");
			String cat = json.getString("cat");
			String sta = TransDate.convertTime(json.getLong("sta"));
			String end = TransDate.convertTime(json.getLong("end"));
			int count = json.getInt("count");
			int remain = json.getInt("remain");
			
			tuan.setTuan_id(id);
			tuan.setTuan_desc(desc);
			tuan.setTuan_name(name);
			
			TableItem item = new TableItem(table, SWT.NONE);
			item.setData(tuan);
			item.setText(new String[]{id+"", name, desc, firm, cat, sta, end, count+"", remain+""});
			
			
		}
		
	}
	
	/**
	 * 初始化按钮区域
	 */
	private void initButton(){
		
		ButtonSelectionListener selectListener = new ButtonSelectionListener();
		
		main_button = new Composite(main_shell, SWT.NONE);
		
		int buttonWidth = main_shell.getBounds().width - marginWidthValue * 2;
		int buttonHeight = marginHeightValue * 5;
		
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
		main_shell.getDisplay().addFilter(SWT.KeyDown, new Listener() {
			
			@Override
			public void handleEvent(Event e) {
				// TODO Auto-generated method stub
				if(e.stateMask == SWT.ALT && e.keyCode == 'q'){
					// Alt + Q
					exitApplication();
				}else if(e.stateMask == SWT.ALT && e.keyCode == 'h'){
					// Alt + F1
					openHelp();
				}else if(e.stateMask == SWT.ALT && e.keyCode == 'p'){
					// Alt + p
					openPinter();
				}else if(e.stateMask == SWT.ALT && e.keyCode == 'z'){
					logout();
				}
			}
		});
		
		main_shell.addDisposeListener(new DisposeListener() {
			
			@Override
			public void widgetDisposed(DisposeEvent arg0) {
				// TODO Auto-generated method stub
				if(!logout){
					getDisplay().dispose();
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
		main_menu_system_menu_help.setText("关于　　　　Alt + H");
		main_menu_system_menu_help.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				openHelp();
			}
			
		});
		
		main_menu_system_menu_printer = new MenuItem(main_menu_system_menu, SWT.CASCADE);
		main_menu_system_menu_printer.setText("打印设置　　Alt + P");
		main_menu_system_menu_printer.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				openPinter();
			}
			
		});
		
		
		main_menu_system_menu_logout = new MenuItem(main_menu_system_menu, SWT.CASCADE);
		main_menu_system_menu_logout.setText("更换登陆　　Alt + Z");
		main_menu_system_menu_logout.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				logout();
			}
		});
		
		// 关于菜单项下面的分割线
		main_menu_system_menu_helpSeperator = new MenuItem(main_menu_system_menu, SWT.SEPARATOR);
		
		// 退出菜单项
		main_menu_system_menu_exit = new MenuItem(main_menu_system_menu, SWT.CASCADE);
		main_menu_system_menu_exit.setText("退出程序　　Alt + Q");
		main_menu_system_menu_exit.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				exitApplication();
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
	 * 打开打印设置界面的方法
	 */
	private void openPinter(){
		
		if(printerSettingsWindow == null || printerSettingsWindow.isDisposed()){
			printerSettingsWindow = new printerSettingsWindow(this);
			printerSettingsWindow.show();
		}
		
	}
	/**
	 * 打开关于界面的方法
	 */
	private void openHelp(){
		System.out.println("HelpListener");
	}
	/**
	 * 更换登陆的方法
	 */
	private void logout(){
		logout = true;
		
		main_shell.dispose();
		getParent().getShell().setVisible(true);
		getParent().getShell().setFocus();
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
					new createTuanWindow(This(), new onResultListener<Tuan>() {
						
						@Override
						public void onResult(Tuan result) {
							// TODO Auto-generated method stub
							new loadAllTuanAsyncHandler(This()).start("");
						}
					}).show();
					break;
				case "商家管理":
					new firmWindow(This()).show();
					break;
				case "用户管理":
					new userWindow(This()).show();
					break;
				case "类别管理":
					new categoryWindow(This()).show();
					break;
				case "工作日志":
					new logWindow(This()).show();
					break;
				default:
					break;
				}
				
			}
		}
		
	}
	
	@Override
	protected Shell getShell() {
		// TODO Auto-generated method stub
		return main_shell;
	}
}
