package cn.panshihao.pos.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
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

import cn.panshihao.pos.dao.LogDAO;
import cn.panshihao.pos.handler.AsyncHandler;
import cn.panshihao.pos.handler.ImageHandler;
import cn.panshihao.pos.model.Log;
import cn.panshihao.pos.model.User;
import cn.panshihao.pos.tools.TransDate;

public class logWindow extends superWindow {

	
	public String log_title = "工作日志";
	public Shell log_shell = null;
	
	/*
	 * 按钮区域 
	 */
	public Composite log_button = null;
	public Button log_button_add = null;
	
	/*
	 * 选项卡区域
	 */
	public TabFolder log_tab = null;
	public TabItem log_tab_all = null;
	public TabItem log_tab_add = null;
	public TabItem log_tab_modify = null;
	
	/*
	 * 表格
	 */
	public Table log_tab_all_table = null;
	
	
	private boolean deleting = false;
	
	
	public logWindow(superWindow parent) {
		super(parent);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub
		initBase();
		
		initButton();
		
		initTab();
		
		initTable();
		
		log_shell.open();
		log_shell.layout();
		
	}
	/**
	 * 初始化选项卡区域
	 */
	private void initTab(){
		log_tab = new TabFolder(log_shell, SWT.NONE);
		log_tab.setBounds(marginWidthValue, marginHeightValue * 2, getShellWidth() - marginWidthValue * 2, getShellHeight() - marginHeightValue * 4);
		
		log_tab_all = new TabItem(log_tab, SWT.NONE);
		log_tab_all.setText("全部日志");
		
	}
	/**
	 * 加载Tab数据
	 */
	private void initTable(){
		
		log_tab_all_table = new Table(log_tab, SWT.FULL_SELECTION);
		log_tab_all_table.setHeaderVisible(true);
		log_tab_all_table.setLinesVisible(true);
//		log_tab_all_table.addMouseListener(new MouseAdapter() {
//			
//			@Override
//			public void mouseDown(MouseEvent e) {
//				// TODO Auto-generated method stub
//				if(e.button == 3){
//					// item右键事件
//					final int selectionIndex = log_tab_all_table.getSelectionIndex();
//					
//					//如果selectionIndex为-1则代表没有选中任何元素
//					if(selectionIndex == -1){
//						return;
//					}
//					TableItem setectionItem = log_tab_all_table.getSelection()[0];
//					final Log Log = (Log) setectionItem.getData();
//					
//					
//					Menu menu = new Menu(log_tab_all_table);
//					log_tab_all_table.setMenu(menu);
//					
//					MenuItem item_Info = new MenuItem(menu, SWT.PUSH);  
//					item_Info.setText("ID:"+Log.getLog_id()+" 日志名称："+Log.getLog_content());  
//					
//					MenuItem item_modify = new MenuItem(menu, SWT.PUSH);  
//					item_modify.setText("编辑该日志");  
//					item_modify.addSelectionListener(new SelectionAdapter() {
//						@Override
//						public void widgetSelected(SelectionEvent arg0) {
//							// TODO Auto-generated method stub
//							modifyTableItem(Log, selectionIndex);
//							
//						}
//					});
//					
//					
//					MenuItem item_delete = new MenuItem(menu, SWT.PUSH);  
//					item_delete.setText("删除该日志");  
//					item_delete.addSelectionListener(new SelectionAdapter() {
//						@Override
//						public void widgetSelected(SelectionEvent arg0) {
//							// TODO Auto-generated method stub
//							if(log_tab_modify != null){
//								alert(getShell(), "删除错误", "正在进行编辑操作，请完成编辑之后再执行删除！");
//								setTabFocus("Log_tab_modify");
//								return;
//							}
//							
//							if(!deleting){
//								deleteTableItem(Log, selectionIndex);
//							}
//						}
//					});
//				}
//				
//			}
//		});
		
		
		TableColumn idColumn = new TableColumn(log_tab_all_table, SWT.CENTER); 
		idColumn.setText("ID");
		idColumn.setWidth(marginWidthValue * 3);
		
		TableColumn contentColumn = new TableColumn(log_tab_all_table, SWT.CENTER); 
		contentColumn.setText("日志内容");
		contentColumn.setWidth(marginWidthValue * 20);
		
		TableColumn userColumn = new TableColumn(log_tab_all_table, SWT.CENTER); 
		userColumn.setText("操作人员");
		userColumn.setWidth(marginWidthValue * 8);
		
		TableColumn timeColumn = new TableColumn(log_tab_all_table, SWT.CENTER); 
		timeColumn.setText("日志时间");
		timeColumn.setWidth(marginWidthValue * 15);
		
		log_tab_all.setControl(log_tab_all_table);
		
		new loadLogListAsyncHandler(This()).start("");
	}
	/**
	 * 删除表格元素
	 * @param Log
	 * @param index
	 */
	private void deleteTableItem(Log Log, int index){
		new deleteLogAsyncHandler(This()).start(Log);
	}
	/**
	 * 编辑表格元素
	 * @param Log
	 * @param index
	 */
	private void modifyTableItem(final Log Log, int index){
		if(log_tab_modify == null){
			// 创建编辑日志选项卡
			log_tab_modify = new TabItem(log_tab, SWT.NONE);
			log_tab_modify.setText("编辑日志 ID:"+Log.getLog_id()+" 名称："+Log.getLog_content());
			log_tab_modify.setData("Log_tab_modify");
			setTabFocus("Log_tab_modify");
			// 为选项卡增加内容
			final Composite add_content = new Composite(log_tab, SWT.NONE | SWT.WRAP);
			
			Label name_label = new Label(add_content, SWT.NONE);
			name_label.setText("日志名称");
			name_label.setBounds(marginWidthValue, marginHeightValue * 3, marginWidthValue * 5, marginHeightValue * 2);
			
			Text name_text = new Text(add_content, SWT.BORDER);
			name_text.setBounds(name_label.getBounds().x+name_label.getBounds().width, name_label.getBounds().y - (int)(marginHeightValue * 0.5), marginWidthValue * 15, marginHeightValue * 3);
			name_text.setData("name_text");
			
			
			Label desc_label = new Label(add_content, SWT.NONE);
			desc_label.setText("日志描述");
			desc_label.setBounds(name_label.getBounds().x, name_label.getBounds().y+name_label.getBounds().height+marginHeightValue * 3, marginWidthValue * 5, marginHeightValue * 2);
			
			Text desc_text = new Text(add_content, SWT.BORDER | SWT.WRAP);
			desc_text.setBounds(desc_label.getBounds().x+desc_label.getBounds().width, desc_label.getBounds().y - (int)(marginHeightValue * 0.5), marginWidthValue * 15, marginHeightValue * 13);
			desc_text.setData("desc_text");
			
			Button modify_button = new Button(add_content, SWT.NONE);
			modify_button.setText("确认修改");
			modify_button.setBounds(marginWidthValue * 25, marginHeightValue * 2, marginWidthValue * 10, marginHeightValue * 5);
			
			modify_button.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent arg0) {
					// TODO Auto-generated method stub
					
					validateModify(add_content, (Button) arg0.getSource(), Log.getLog_id());
				}
				
			});
			
			Button cancel_button = new Button(add_content, SWT.NONE);
			cancel_button.setText("取消修改");
			cancel_button.setBounds(marginWidthValue * 25, modify_button.getBounds().y + modify_button.getBounds().height + marginHeightValue * 4, marginWidthValue * 10, marginHeightValue * 5);
			
			cancel_button.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent arg0) {
					// TODO Auto-generated method stub
					log_tab_modify.dispose();
					log_tab_modify = null;
				}
			});
			
			log_tab_modify.setControl(add_content);
		}else{
			
			if(confirm(getShell(), "确认操作", "当前已经有一个日志正在被编辑，之前未保存的数据将会丢失，要开始编辑吗？")){
				
				log_tab_modify.dispose();
				log_tab_modify = null;
				modifyTableItem(Log, index);
			}else{
				setTabFocus("Log_tab_modify");
			}
			
			
			
		}
	}
	/**
	 * 验证修改操作
	 * @param content
	 * @param button
	 */
	private void validateModify(Composite content, Button button, int id){
		Control[] children = content.getChildren();
		
		String nameValue = null;
		String descValue = null;
		
		for(int i = 0 ; i < children.length ; i ++){
			Control c = children[i];
			
			if(c instanceof Text){
				Text text = (Text) c;
				
				switch (text.getData().toString()) {
				case "name_text":
					nameValue = text.getText();
					break;
				case "desc_text":
					descValue = text.getText();
					break;
				default:
					break;
				}
				
			}
		}
		
		if(nameValue == null || nameValue.equals("") || nameValue.length() > 32){
			alert(getShell(), "修改错误", "日志名称不能为空，并且不能超过32个字符");
			return;
		}
		
		if(descValue == null || descValue.equals("")){
			alert(getShell(), "修改错误", "日志描述不能为空");
			return;
		}
		
		String[] values = {nameValue, descValue};
		new modifyLogAsyncHandler(This(), button, id).start(values);
	}
	
	/**
	 * 修改日志信息的AsyncHandler
	 * @author shihao
	 *
	 */
	private class modifyLogAsyncHandler extends AsyncHandler<String, Integer, Boolean>{

		private Button button;
		private int id;
		
		public modifyLogAsyncHandler(superWindow window, Button button, int id) {
			super(window);
			// TODO Auto-generated constructor stub
			this.button = button;
			this.id = id;
		}

		@Override
		public Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			LogDAO dao = new LogDAO();
			
			Log Log = new Log();
			Log.setLog_id(id);
			
			User user = (User) cacheMap.get("curUser");
			
			return dao.updateLog(Log, user.getUser_id());
		}
		
		@Override
		public void onComplete(Boolean result) {
			// TODO Auto-generated method stub
			super.onComplete(result);
			
			if(result){
				alert(getShell(), "成功", "修改成功！");
				log_tab_modify.dispose();
				log_tab_modify = null;
				new loadLogListAsyncHandler(This()).start("");
			}else{
				alert(getShell(), "错误", "修改错误，请稍后再试！");
			}
			button.setText("确认修改");
			button.setEnabled(true);
			
		}
		
	}
	/**
	 * 根据tab，设置tab的选中项
	 * @param tab
	 */
	private void setTabFocus(String tab){
		
		TabItem[] items = log_tab.getItems();
		int modify_index = -1;
		for(int i = 0 ; i < items.length ; i ++){
			TabItem item = items[i];
			if(item.getData() != null && item.getData().equals(tab)){
				modify_index = i;
				break;
			}
		}
		
		if(modify_index != -1){
			
			log_tab.setSelection(modify_index);
		}
	}
	
	
	/**
	 * 删除日志信息的AsyncHandler
	 * @author shihao
	 *
	 */
	private class deleteLogAsyncHandler extends AsyncHandler<Log, Integer, Boolean>{

		public deleteLogAsyncHandler(superWindow window) {
			super(window);
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public void onBefore() {
			// TODO Auto-generated method stub
			super.onBefore();
			
			deleting = true;
		}
		
		
		@Override
		public Boolean doInBackground(Log... params) {
			// TODO Auto-generated method stub
			LogDAO dao = new LogDAO();
			User user = (User) cacheMap.get("curUser");
			
			return dao.deleteLog(params[0].getLog_id(), user.getUser_id());
		}
		
		@Override
		public void onComplete(Boolean result) {
			// TODO Auto-generated method stub
			super.onComplete(result);
			deleting = false;
			if(result){
				ClearTableData();
				new loadLogListAsyncHandler(This()).start("");
				alert(getShell(), "成功", "删除日志信息成功！");
			}else{
				alert(getShell(), "失败", "删除日志信息失败，请稍后再试！");
			}
			
		}
		
	}
	/**
	 * 初始化按钮区域
	 */
	private void initButton(){
		log_button = new Composite(log_shell, SWT.NONE);
		log_button.setBounds(marginWidthValue, marginHeightValue, getShellWidth() - marginWidthValue * 2, marginHeightValue * 5);
		log_button.setVisible(false);
		
		log_button_add = new Button(log_button, SWT.NONE);
		log_button_add.setText("添加日志");
		log_button_add.setImage(new ImageHandler(getDisplay(), "res/Add.png", 0.8f).getImage());
		log_button_add.setBounds(0, 0, marginWidthValue * 8, marginHeightValue * 5);
		
		log_button_add.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				addLog();
			}
		});
		
		
	}
	/**
	 * 初始化界面基本数据
	 */
	private void initBase(){
		log_shell = new Shell(getParent().getShell(), SWT.CLOSE | SWT.APPLICATION_MODAL);
		log_shell.setText(log_title);
		log_shell.setSize(marginWidthValue * 50, marginHeightValue * 50);
		log_shell.setLocation(getCenterX(log_shell), getCenterY(log_shell));
	}
	/**
	 * 添加日志按钮的点击事件
	 */
	private void addLog(){
		if(log_tab_add == null){
			// 创建添加日志选项卡
			log_tab_add = new TabItem(log_tab, SWT.NONE);
			log_tab_add.setText("添加日志");
			log_tab_add.setData("Log_tab_add");
			setTabFocus("Log_tab_add");
			// 为选项卡增加内容
			final Composite add_content = new Composite(log_tab, SWT.NONE | SWT.WRAP);
			
			Label name_label = new Label(add_content, SWT.NONE);
			name_label.setText("日志名称");
			name_label.setBounds(marginWidthValue, marginHeightValue * 3, marginWidthValue * 5, marginHeightValue * 2);
			
			Text name_text = new Text(add_content, SWT.BORDER);
			name_text.setBounds(name_label.getBounds().x+name_label.getBounds().width, name_label.getBounds().y - (int)(marginHeightValue * 0.5), marginWidthValue * 15, marginHeightValue * 3);
			name_text.setData("name_text");
			
			Label desc_label = new Label(add_content, SWT.NONE);
			desc_label.setText("日志描述");
			desc_label.setBounds(name_label.getBounds().x, name_label.getBounds().y+name_label.getBounds().height+marginHeightValue * 3, marginWidthValue * 5, marginHeightValue * 2);
			
			Text desc_text = new Text(add_content, SWT.BORDER | SWT.WRAP);
			desc_text.setBounds(desc_label.getBounds().x+desc_label.getBounds().width, desc_label.getBounds().y - (int)(marginHeightValue * 0.5), marginWidthValue * 15, marginHeightValue * 13);
			desc_text.setData("desc_text");
			
			Button add_button = new Button(add_content, SWT.NONE);
			add_button.setText("确认添加");
			add_button.setBounds(marginWidthValue * 25, marginHeightValue * 2, marginWidthValue * 10, marginHeightValue * 5);
			
			add_button.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent arg0) {
					// TODO Auto-generated method stub
					
					validateAdd(add_content, (Button) arg0.getSource());
				}
				
			});
			
			Button cancel_button = new Button(add_content, SWT.NONE);
			cancel_button.setText("取消添加");
			cancel_button.setBounds(marginWidthValue * 25, add_button.getBounds().y + add_button.getBounds().height + marginHeightValue * 4, marginWidthValue * 10, marginHeightValue * 5);
			
			cancel_button.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent arg0) {
					// TODO Auto-generated method stub
					log_tab_add.dispose();
					log_tab_add = null;
				}
			});
			
			log_tab_add.setControl(add_content);
		}else{
			setTabFocus("Log_tab_add");
		}
		
		
	}
	/**
	 * 验证添加日志
	 */
	private void validateAdd(Composite content, Button button){
		Control[] children = content.getChildren();
		
		String nameValue = null;
		String descValue = null;
		for(int i = 0 ; i < children.length ; i ++){
			Control c = children[i];
			
			if(c instanceof Text){
				Text text = (Text) c;
				
				switch (text.getData().toString()) {
				case "name_text":
					nameValue = text.getText();
					break;
				case "desc_text":
					descValue = text.getText();
					break;
				default:
					break;
				}
				
			}
		}
		
		if(nameValue == null || nameValue.equals("") || nameValue.length() > 32){
			alert(getShell(), "添加错误", "日志名称不能为空，并且不能超过32个字符");
			return;
		}
		
		if(descValue == null || descValue.equals("")){
			alert(getShell(), "添加错误", "日志描述不能为空");
			return;
		}
		
		
		String[] values = {nameValue, descValue};
		new checkLogNameExistAsyncHandler(This(), button).start(values);
		
	}
	
	
	@Override
	protected Shell getShell() {
		// TODO Auto-generated method stub
		return log_shell;
	}
	/**
	 * 加载日志列表的asynchandler
	 * @author shihao
	 *
	 */
	private class loadLogListAsyncHandler extends AsyncHandler<String, Integer, Log[]>{

		public loadLogListAsyncHandler(superWindow window) {
			super(window);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onBefore() {
			// TODO Auto-generated method stub
			ClearTableData();
			
		}
		
		
		@Override
		public Log[] doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			LogDAO dao = new LogDAO();
			JSONObject response = dao.getAllLog(0, 10000);
			try {
				JSONArray list = response.getJSONArray("list");
				Log[] Logs = new Log[list.length()];
				
				for(int i = 0 ; i < list.length() ; i ++){
					Log f = new Log();
					JSONObject item = list.getJSONObject(i);
					f.setLog_content(item.getString("con"));
					f.setLog_id(item.getInt("lid"));
					f.setLog_time(item.getLong("tim"));
					f.setUser_id(item.getInt("uid"));
					f.putValue("log_username", item.getString("name"));
					Logs[i] = f;
				}
				
				return Logs;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return null;
		}
		
		@Override
		public void onComplete(Log[] result) {
			// TODO Auto-generated method stub
			super.onComplete(result);
			
			if(result != null && result.length > 0){
				loadLogALL(result);
			}
			
		}
		
	}
	/**
	 * 清除table中的所有数据
	 */
	private void ClearTableData(){
		log_tab_all_table.removeAll();
	}
	/**
	 * 加载日志数据到界面
	 * @param Logs
	 */
	private void loadLogALL(Log[] Logs){
		
		for(Log f : Logs){
			TableItem item = new TableItem(log_tab_all_table, SWT.NONE);
			item.setData(f);
			item.setText(new String[]{f.getLog_id()+"",f.getLog_content(),f.getValue("log_username").toString(),TransDate.convertTime(f.getLog_time())});
		}
		
	}
	/**
	 * 检查日志名称是否已存在
	 * @author shihao
	 *
	 */
	private class checkLogNameExistAsyncHandler extends AsyncHandler<String, Integer, Boolean>{

		private String[] values = null;
		private Button button = null;
		public checkLogNameExistAsyncHandler(superWindow window, Button button) {
			super(window);
			// TODO Auto-generated constructor stub
			this.button = button;
		}

		@Override
		public void onBefore() {
			// TODO Auto-generated method stub
			button.setText("正在添加...");
			button.setEnabled(false);
			
		}
		@Override
		public Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			values = params;
			
			LogDAO dao = new LogDAO();
			
			return null;
		}
		@Override
		public void onComplete(Boolean result) {
			// TODO Auto-generated method stub
			super.onComplete(result);
			
			if(result){
				new AddAsyncHandler(This(), button).start(values);
			}else{
				button.setText("确认添加");
				button.setEnabled(true);
				alert(getShell(), "添加错误", "日志名称已经存在！");
			}
			
		}
	}
	/**
	 * 执行添加日志操作的AsyncHandler
	 * @author shihao
	 *
	 */
	private class AddAsyncHandler extends AsyncHandler<String, Integer, Boolean>{

		private Button button;
		
		public AddAsyncHandler(superWindow window, Button button) {
			super(window);
			// TODO Auto-generated constructor stub
			this.button = button;
		}

		@Override
		public Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			LogDAO dao = new LogDAO();
			
			Log Log = new Log();
			
			User user = (User) cacheMap.get("curUser");
			return null;
//			return dao.addLog(Log, user.getUser_id());
		}
		
		@Override
		public void onComplete(Boolean result) {
			// TODO Auto-generated method stub
			super.onComplete(result);
			
			if(result){
				alert(getShell(), "添加成功", "添加成功！");
				log_tab_add.dispose();
				log_tab_add = null;
				new loadLogListAsyncHandler(This()).start("");
			}else{
				alert(getShell(), "添加错误", "添加错误！请稍后再试！");
			}
			button.setText("确认添加");
			button.setEnabled(true);
			
		}
	}

}
