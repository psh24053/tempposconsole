package cn.panshihao.pos.view;

import org.apache.commons.codec.digest.DigestUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
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

import cn.panshihao.pos.dao.UserDAO;
import cn.panshihao.pos.handler.AsyncHandler;
import cn.panshihao.pos.handler.ImageHandler;
import cn.panshihao.pos.model.Firm;
import cn.panshihao.pos.model.User;

public class userWindow extends superWindow {

	public String user_title = "用户管理";
	public Shell user_shell;
	
	/*
	 * 按钮区域
	 */
	public Composite user_button = null;
	public Button user_button_add = null;
	
	/*
	 * 选项卡区域
	 */
	public TabFolder user_tab = null;
	public TabItem user_tab_all = null;
	public TabItem user_tab_add = null;
	public TabItem user_tab_modify = null;
	
	
	/*
	 * 表格
	 */
	public Table user_tab_all_table = null;
	
	private boolean deleting = false;
	
	public userWindow(superWindow parent) {
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
		
		user_shell.open();
		user_shell.layout();
		
	}
	/**
	 * 初始化表格内容区域
	 */
	private void initTable(){
		user_tab_all_table = new Table(user_tab, SWT.FULL_SELECTION);
		user_tab_all_table.setHeaderVisible(true);
		user_tab_all_table.setLinesVisible(true);
		
		user_tab_all_table.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseDown(MouseEvent e) {
				// TODO Auto-generated method stub
				if(e.button == 3){
					// item右键事件
					final int selectionIndex = user_tab_all_table.getSelectionIndex();
					
					//如果selectionIndex为-1则代表没有选中任何元素
					if(selectionIndex == -1){
						return;
					}
					TableItem setectionItem = user_tab_all_table.getSelection()[0];
					final User user = (User) setectionItem.getData();
					
					
					Menu menu = new Menu(user_tab_all_table);
					user_tab_all_table.setMenu(menu);
					
					MenuItem item_Info = new MenuItem(menu, SWT.PUSH);  
					item_Info.setText("ID:"+user.getUser_id()+" 用户账号："+user.getUser_name());  
					
					MenuItem item_modify = new MenuItem(menu, SWT.PUSH);  
					item_modify.setText("编辑该用户");  
					item_modify.addSelectionListener(new SelectionAdapter() {
						@Override
						public void widgetSelected(SelectionEvent arg0) {
							// TODO Auto-generated method stub
							modifyTableItem(user, selectionIndex);
							
						}
					});
					
					
					MenuItem item_delete = new MenuItem(menu, SWT.PUSH);  
					item_delete.setText("删除该用户");  
					item_delete.addSelectionListener(new SelectionAdapter() {
						@Override
						public void widgetSelected(SelectionEvent arg0) {
							// TODO Auto-generated method stub
							if(user_tab_modify != null){
								alert(getShell(), "删除错误", "正在进行编辑操作，请完成编辑之后再执行删除！");
								setTabFocus("user_tab_modify");
								return;
							}
							
							if(!deleting){
								deleteTableItem(user, selectionIndex);
							}
						}
					});
				}
			}
		});
		
		TableColumn idColumn = new TableColumn(user_tab_all_table, SWT.CENTER); 
		idColumn.setText("ID");
		idColumn.setWidth(marginWidthValue * 3);
		
		TableColumn nameColumn = new TableColumn(user_tab_all_table, SWT.CENTER); 
		nameColumn.setText("用户账号");
		nameColumn.setWidth(marginWidthValue * 15);
		
		TableColumn descColumn = new TableColumn(user_tab_all_table, SWT.CENTER); 
		descColumn.setText("用户权限");
		descColumn.setWidth(marginWidthValue * 10);
		
		user_tab_all.setControl(user_tab_all_table);
		
		new loadUserAllAsyncHandler(This()).start("");
	}
	/**
	 * 编辑表格元素
	 * @param firm
	 * @param index
	 */
	private void modifyTableItem(final User user, int index){
		if(user_tab_modify == null){
			// 创建编辑商家选项卡
			user_tab_modify = new TabItem(user_tab, SWT.NONE);
			user_tab_modify.setText("编辑用户 ID:"+user.getUser_id()+" 名称："+user.getUser_name());
			user_tab_modify.setData("user_tab_modify");
			setTabFocus("user_tab_modify");
			// 为选项卡增加内容
			final Composite add_content = new Composite(user_tab, SWT.NONE | SWT.WRAP);
			
			Label name_label = new Label(add_content, SWT.NONE);
			name_label.setText("用户账号");
			name_label.setBounds(marginWidthValue, marginHeightValue * 3, marginWidthValue * 5, marginHeightValue * 2);
			
			Text name_text = new Text(add_content, SWT.BORDER);
			name_text.setBounds(name_label.getBounds().x+name_label.getBounds().width, name_label.getBounds().y - (int)(marginHeightValue * 0.5), marginWidthValue * 15, marginHeightValue * 3);
			name_text.setData("name_text");
			name_text.setText(user.getUser_name());
			name_text.setEnabled(false);
			
			Label pass_label = new Label(add_content, SWT.NONE);
			pass_label.setText("用户密码");
			pass_label.setBounds(name_label.getBounds().x, name_label.getBounds().y+name_label.getBounds().height+marginHeightValue * 3, marginWidthValue * 5, marginHeightValue * 2);
			
			Text pass_text = new Text(add_content, SWT.BORDER);
			pass_text.setBounds(pass_label.getBounds().x+pass_label.getBounds().width, pass_label.getBounds().y - (int)(marginHeightValue * 0.5), marginWidthValue * 15, marginHeightValue * 3);
			pass_text.setData("pass_text");
			pass_text.setText("输入新密码，留空则表示不修改密码");
			pass_text.addFocusListener(new FocusListener() {
				
				@Override
				public void focusLost(FocusEvent e) {
					// TODO Auto-generated method stub
					Text text = (Text) e.getSource();
					String pass = text.getText();
					
					if(pass == null || pass.equals("")){
						text.setText("输入新密码，留空则表示不修改密码");
					}
					
				}
				
				@Override
				public void focusGained(FocusEvent e) {
					// TODO Auto-generated method stub
					Text text = (Text) e.getSource();
					String pass = text.getText();
					
					if(pass.equals("输入新密码，留空则表示不修改密码")){
						text.setText("");
					}
					
				}
			});
			
			
			Label grade_label = new Label(add_content, SWT.NONE);
			grade_label.setText("用户权限");
			grade_label.setBounds(pass_label.getBounds().x, pass_label.getBounds().y+pass_label.getBounds().height+marginHeightValue * 3, marginWidthValue * 5, marginHeightValue * 2);
			
			Combo grade_combo = new Combo(add_content, SWT.READ_ONLY);
			grade_combo.setBounds(grade_label.getBounds().x+grade_label.getBounds().width, grade_label.getBounds().y - (int)(marginHeightValue * 0.5), marginWidthValue * 15, marginHeightValue * 3);
			grade_combo.setData("grade_combo");
			grade_combo.setItems(new String[]{"操作员","管理员"});
			switch (user.getUser_grade()) {
			case 1:
				grade_combo.select(1);
				break;
			case 2:
				grade_combo.select(0);
				break;
			default:
				break;
			}
			
			
			Button modify_button = new Button(add_content, SWT.NONE);
			modify_button.setText("确认修改");
			modify_button.setBounds(marginWidthValue * 25, marginHeightValue * 2, marginWidthValue * 10, marginHeightValue * 5);
			
			modify_button.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent arg0) {
					// TODO Auto-generated method stub
					
					validateModify(add_content, (Button) arg0.getSource(), user.getUser_id());
				}
				
			});
			
			Button cancel_button = new Button(add_content, SWT.NONE);
			cancel_button.setText("取消修改");
			cancel_button.setBounds(marginWidthValue * 25, modify_button.getBounds().y + modify_button.getBounds().height + marginHeightValue * 4, marginWidthValue * 10, marginHeightValue * 5);
			
			cancel_button.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent arg0) {
					// TODO Auto-generated method stub
					user_tab_modify.dispose();
					user_tab_modify = null;
				}
			});
			
			user_tab_modify.setControl(add_content);
		}else{
			
			if(confirm(getShell(), "确认操作", "当前已经有一个用户正在被编辑，之前未保存的数据将会丢失，要开始编辑吗？")){
				
				user_tab_modify.dispose();
				user_tab_modify = null;
				modifyTableItem(user, index);
			}else{
				setTabFocus("user_tab_modify");
			}
			
			
			
		}
	}
	/**
	 * 验证修改用户信息
	 * @param content
	 * @param button
	 * @param id
	 */
	private void validateModify(Composite content, Button button, int id){
		
		Control[] children = content.getChildren();
		
		String nameValue = null;
		String passValue = null;
		int gradeValue = 2;
		
		for(int i = 0 ; i < children.length ; i ++){
			Control c = children[i];
			
			if(c instanceof Text){
				Text text = (Text) c;
				
				switch (text.getData().toString()) {
				case "name_text":
					nameValue = text.getText();
					break;
				case "pass_text":
					passValue = text.getText();
					break;
				default:
					break;
				}
				
			}
			
			if(c instanceof Combo){
				Combo combo = (Combo) c;
				
				String value = combo.getText();
				if(value.equals("管理员")){
					gradeValue = 1;
				}else if(value.equals("操作员")){
					gradeValue = 2;
				}else{
					gradeValue = -1;
				}
				
			}
			
		}
		
		if(nameValue == null || nameValue.equals("") || nameValue.length() > 32){
			alert(getShell(), "修改错误", "用户账号不能为空，并且不能超过32个字符");
			return;
		}
		if(passValue == null || passValue.equals("") || passValue.length() > 32){
			alert(getShell(), "修改错误", "用户密码不能为空，并且不能超过32个字符");
			return;
		}
		if(gradeValue == -1){
			alert(getShell(), "修改错误", "请选择用户权限");
			return;
		}
		
		String[] values = {nameValue, passValue, gradeValue+""};
		new modifyUserAsyncHandler(This(), button, id).start(values);
		
	}
	/**
	 * 删除表格元素
	 * @param firm
	 * @param index
	 */
	private void deleteTableItem(User user, int index){
		new deleteUserAsyncHandler(This()).start(user);
	}
	/**
	 * 修改用户的asynchandler
	 * @author Administrator
	 *
	 */
	private class modifyUserAsyncHandler extends AsyncHandler<String, Integer, Boolean>{

		private Button button;
		private int id;
		
		
		public modifyUserAsyncHandler(superWindow window, Button button, int id) {
			super(window);
			// TODO Auto-generated constructor stub
			this.button = button;
			this.id = id;
		}
		
		@Override
		public void onBefore() {
			// TODO Auto-generated method stub
			button.setText("正在修改...");
			button.setEnabled(false);
			
		}
		
		@Override
		public Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			UserDAO dao = new UserDAO();
			User user = new User();
			user.setUser_name(params[0]);
			user.setUser_pass(params[1]);
			
			if(user.getUser_pass() != null && !user.getUser_pass().equals("")){
				user.setUser_pass(DigestUtils.md5Hex(user.getUser_pass()));
			}
			
			user.setUser_grade(Integer.valueOf(params[2]));
			user.setUser_id(id);
			
			return dao.updateUser(user, getCurUser().getUser_id());
		}
		
		@Override
		public void onComplete(Boolean result) {
			// TODO Auto-generated method stub
			super.onComplete(result);
			
			if(result){
				alert(getShell(), "成功", "修改用户信息成功！");
				user_tab_modify.dispose();
				user_tab_modify = null;
				new loadUserAllAsyncHandler(This()).start("");
			}else{
				alert(getShell(), "失败", "修改用户信息失败，请稍候再试！");
			}
			button.setText("确认修改");
			button.setEnabled(true);
		}
		
	}
	/**
	 * 删除用户的asynchandler
	 * @author Administrator
	 *
	 */
	private class deleteUserAsyncHandler extends AsyncHandler<User, Integer, Boolean>{

		public deleteUserAsyncHandler(superWindow window) {
			super(window);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onBefore() {
			// TODO Auto-generated method stub
			super.onBefore();
			
			
		}
		
		@Override
		public Boolean doInBackground(User... params) {
			// TODO Auto-generated method stub
			
			UserDAO dao = new UserDAO();
			
			return dao.deleteUser(params[0].getUser_id(), getCurUser().getUser_id());
		}
		
		@Override
		public void onComplete(Boolean result) {
			// TODO Auto-generated method stub
			super.onComplete(result);
			
			if(result){
				alert(getShell(), "成功", "删除用户成功！");
				new loadUserAllAsyncHandler(This()).start("");
				
			}else{
				alert(getShell(), "错误", "删除用户错误，请稍候再试！");
			}
			
		}
		
	}
	/**
	 * 初始化选项卡区域
	 */
	private void initTab(){
		user_tab = new TabFolder(getShell(), SWT.NONE);
		user_tab.setBounds(marginWidthValue, user_button.getBounds().height + marginHeightValue * 2, getShellWidth() - marginWidthValue * 2, getShellHeight() - user_button.getBounds().height - marginHeightValue * 4);
		
		user_tab_all = new TabItem(user_tab, SWT.NONE);
		user_tab_all.setText("全部用户");
	}
	/**
	 * 初始化按钮区域
	 */
	private void initButton(){
		user_button = new Composite(user_shell, SWT.NONE);
		user_button.setBounds(marginWidthValue, marginHeightValue, getShellWidth() - marginWidthValue * 2, marginHeightValue * 5);
		
		user_button_add = new Button(user_button, SWT.NONE);
		user_button_add.setText("添加用户");
		user_button_add.setImage(new ImageHandler(getDisplay(), "res/Add.png", 0.8f).getImage());
		user_button_add.setBounds(0, 0, marginWidthValue * 8, marginHeightValue * 5);
		
		user_button_add.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				addUserTab();
			}
		});
		
	}
	/**
	 * 根据tab，设置tab的选中项
	 * @param tab
	 */
	private void setTabFocus(String tab){
		
		TabItem[] items = user_tab.getItems();
		int modify_index = -1;
		for(int i = 0 ; i < items.length ; i ++){
			TabItem item = items[i];
			if(item.getData() != null && item.getData().equals(tab)){
				modify_index = i;
				break;
			}
		}
		
		if(modify_index != -1){
			
			user_tab.setSelection(modify_index);
		}
	}
	/**
	 * 创建一个添加用户的tab
	 */
	private void addUserTab(){
		if(user_tab_add == null){
			// 创建添加商家选项卡
			user_tab_add = new TabItem(user_tab, SWT.NONE);
			user_tab_add.setText("添加用户");
			user_tab_add.setData("user_tab_add");
			setTabFocus("user_tab_add");
			// 为选项卡增加内容
			final Composite add_content = new Composite(user_tab, SWT.NONE | SWT.WRAP);
			
			Label name_label = new Label(add_content, SWT.NONE);
			name_label.setText("用户账号");
			name_label.setBounds(marginWidthValue, marginHeightValue * 3, marginWidthValue * 5, marginHeightValue * 2);
			
			Text name_text = new Text(add_content, SWT.BORDER);
			name_text.setBounds(name_label.getBounds().x+name_label.getBounds().width, name_label.getBounds().y - (int)(marginHeightValue * 0.5), marginWidthValue * 15, marginHeightValue * 3);
			name_text.setData("name_text");
			
			Label pass_label = new Label(add_content, SWT.NONE);
			pass_label.setText("用户密码");
			pass_label.setBounds(name_label.getBounds().x, name_label.getBounds().y+name_label.getBounds().height+marginHeightValue * 3, marginWidthValue * 5, marginHeightValue * 2);
			
			Text pass_text = new Text(add_content, SWT.BORDER );
			pass_text.setBounds(pass_label.getBounds().x+pass_label.getBounds().width, pass_label.getBounds().y - (int)(marginHeightValue * 0.5), marginWidthValue * 15, marginHeightValue * 3);
			pass_text.setData("pass_text");
			
			Label grade_label = new Label(add_content, SWT.NONE);
			grade_label.setText("用户权限");
			grade_label.setBounds(pass_label.getBounds().x, pass_label.getBounds().y+pass_label.getBounds().height+marginHeightValue * 3, marginWidthValue * 5, marginHeightValue * 2);
			
			Combo grade_combo = new Combo(add_content, SWT.READ_ONLY);
			grade_combo.setBounds(grade_label.getBounds().x+grade_label.getBounds().width, grade_label.getBounds().y - (int)(marginHeightValue * 0.5), marginWidthValue * 15, marginHeightValue * 3);
			grade_combo.setData("grade_combo");
			grade_combo.setItems(new String[]{"操作员","管理员"});
			
			
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
					user_tab_add.dispose();
					user_tab_add = null;
				}
			});
			
			user_tab_add.setControl(add_content);
			
			
		}
		
		
	}
	/**
	 * 验证添加用户
	 */
	private void validateAdd(Composite content, Button button){
		Control[] children = content.getChildren();
		
		String nameValue = null;
		String passValue = null;
		int gradeValue = 2;
		
		for(int i = 0 ; i < children.length ; i ++){
			Control c = children[i];
			
			if(c instanceof Text){
				Text text = (Text) c;
				
				switch (text.getData().toString()) {
				case "name_text":
					nameValue = text.getText();
					break;
				case "pass_text":
					passValue = text.getText();
					break;
				default:
					break;
				}
				
			}
			
			if(c instanceof Combo){
				Combo combo = (Combo) c;
				
				String value = combo.getText();
				if(value.equals("管理员")){
					gradeValue = 1;
				}else if(value.equals("操作员")){
					gradeValue = 2;
				}else{
					gradeValue = -1;
				}
				
			}
			
		}
		
		if(nameValue == null || nameValue.equals("") || nameValue.length() > 32){
			alert(getShell(), "添加错误", "用户账号不能为空，并且不能超过32个字符");
			return;
		}
		if(passValue == null || passValue.equals("") || passValue.length() > 32){
			alert(getShell(), "添加错误", "用户密码不能为空，并且不能超过32个字符");
			return;
		}
		if(gradeValue == -1){
			alert(getShell(), "添加错误", "请选择用户权限");
			return;
		}
		
		String[] values = {nameValue, passValue, gradeValue+""};
		new checkUserNameExistAsyncHandler(This(), button).start(values);
		
	}
	/**
	 * 初始化界面基本属性
	 */
	private void initBase(){
		user_shell = new Shell(getParent().getShell(), SWT.CLOSE | SWT.APPLICATION_MODAL);
		user_shell.setText(user_title);
		user_shell.setSize(marginWidthValue * 40, marginHeightValue * 40);
		user_shell.setLocation(getCenterX(user_shell), getCenterY(user_shell));
		
		
	}
	/**
	 * 清理表格数据
	 */
	public void ClearTableData(){
		user_tab_all_table.removeAll();
	}
	/**
	 * 检查用户名是否存在的AsyncHandler
	 * @author Administrator
	 *
	 */
	private class checkUserNameExistAsyncHandler extends AsyncHandler<String, Integer, Boolean>{

		private Button button;
		private String[] values;
		
		public checkUserNameExistAsyncHandler(superWindow window, Button button) {
			super(window);
			// TODO Auto-generated constructor stub
			this.button = button;
		}

		@Override
		public void onBefore() {
			// TODO Auto-generated method stub
			super.onBefore();
			
			button.setText("正在添加...");
			button.setEnabled(false);
		}
		
		@Override
		public Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			values = params;
			
			UserDAO dao = new UserDAO();
			
			return dao.checkUserNameIsExist(params[0]);
		}
		
		@Override
		public void onComplete(Boolean result) {
			// TODO Auto-generated method stub
			super.onComplete(result);
			
			if(result){
				new AddUserAsyncHandler(This(), button).start(values);
			}else{
				alert(getShell(), "添加错误", "用户名已经存在！");
				button.setText("确认添加");
				button.setEnabled(true);
			}
			
		}
		
	}
	/**
	 * 添加用户的asyncHandler
	 * @author Administrator
	 *
	 */
	private class AddUserAsyncHandler extends AsyncHandler<String, Integer, Boolean>{

		private Button button;
		
		public AddUserAsyncHandler(superWindow window, Button button) {
			super(window);
			// TODO Auto-generated constructor stub
			this.button = button;
		}

		@Override
		public void onBefore() {
			// TODO Auto-generated method stub
			super.onBefore();
		}
		
		@Override
		public Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			UserDAO dao = new UserDAO();

			User user = new User();
			user.setUser_name(params[0]);
			user.setUser_pass(params[1]);
			
			// 将用户密码做md5
			user.setUser_pass(DigestUtils.md5Hex(user.getUser_pass()));
			
			user.setUser_grade(Integer.valueOf(params[2]));
			
			User curUser = (User) cacheMap.get("curUser");
			
			return dao.addUser(user, curUser.getUser_id());
		}
		
		@Override
		public void onComplete(Boolean result) {
			// TODO Auto-generated method stub
			super.onComplete(result);
			
			if(result){
				alert(getShell(), "添加成功", "添加用户成功！");
				user_tab_add.dispose();
				user_tab_add = null;
				new loadUserAllAsyncHandler(This()).start("");
			}else{
				alert(getShell(), "添加错误", "添加用户错误，请稍候再试！");
			}
			button.setText("确认添加");
			button.setEnabled(true);
		}
		
	}
	
	/**
	 * 加载所有用户
	 * @author Administrator
	 *
	 */
	private class loadUserAllAsyncHandler extends AsyncHandler<String, Integer, User[]>{

		public loadUserAllAsyncHandler(superWindow window) {
			super(window);
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public void onBefore() {
			// TODO Auto-generated method stub
			
			ClearTableData();
			
		}
		
		@Override
		public User[] doInBackground(String... params) {
			// TODO Auto-generated method stub
			UserDAO dao = new UserDAO();
			
			JSONObject response = dao.getAllUser(0, 1000);
			
			try {
				JSONArray list = response.getJSONArray("list");
				User[] users = new User[list.length()];
				
				for(int i = 0 ; i < list.length() ; i ++){
					User u = new User();
					JSONObject item = list.getJSONObject(i);
					u.setUser_grade(item.getInt("grade"));
					u.setUser_id(item.getInt("uid"));
					u.setUser_name(item.getString("name"));
					
					users[i] = u;
				}
				return users;
				
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			return null;
		}
		
		@Override
		public void onComplete(User[] result) {
			// TODO Auto-generated method stub
			super.onComplete(result);
			
			if(result != null && result.length > 0){
				loadUserAll(result);
				
				
			}
			
			
			
		}
		
	}
	/**
	 * 将数据加载到界面上
	 * @param users
	 */
	private void loadUserAll(User[] users){
		
		for(User u : users){
			TableItem item = new TableItem(user_tab_all_table, SWT.NONE);
			item.setData(u);
			item.setText(new String[]{u.getUser_id()+"",u.getUser_name(),u.getUser_grade() == 2 ? "操作员":"管理员"});
			
		}
		
		
	}
	

	@Override
	protected Shell getShell() {
		// TODO Auto-generated method stub
		return user_shell;
	}

}
