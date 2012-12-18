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

import cn.panshihao.pos.dao.CategoryDAO;
import cn.panshihao.pos.handler.AsyncHandler;
import cn.panshihao.pos.handler.ImageHandler;
import cn.panshihao.pos.model.Category;
import cn.panshihao.pos.model.User;

public class categoryWindow extends superWindow {

	
	public String category_title = "类别管理";
	public Shell category_shell = null;
	
	/*
	 * 按钮区域 
	 */
	public Composite category_button = null;
	public Button category_button_add = null;
	
	/*
	 * 选项卡区域
	 */
	public TabFolder category_tab = null;
	public TabItem category_tab_all = null;
	public TabItem category_tab_add = null;
	public TabItem category_tab_modify = null;
	
	/*
	 * 表格
	 */
	public Table category_tab_all_table = null;
	
	
	private boolean deleting = false;
	
	
	public categoryWindow(superWindow parent) {
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
		
		category_shell.open();
		category_shell.layout();
		
	}
	/**
	 * 初始化选项卡区域
	 */
	private void initTab(){
		category_tab = new TabFolder(category_shell, SWT.NONE);
		category_tab.setBounds(marginWidthValue, category_button.getBounds().height + marginHeightValue * 2, getShellWidth() - marginWidthValue * 2, getShellHeight() - category_button.getBounds().height - marginHeightValue * 4);
		
		category_tab_all = new TabItem(category_tab, SWT.NONE);
		category_tab_all.setText("全部类别");
		
	}
	/**
	 * 加载Tab数据
	 */
	private void initTable(){
		
		category_tab_all_table = new Table(category_tab, SWT.FULL_SELECTION);
		category_tab_all_table.setHeaderVisible(true);
		category_tab_all_table.setLinesVisible(true);
		category_tab_all_table.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseDown(MouseEvent e) {
				// TODO Auto-generated method stub
				if(e.button == 3){
					// item右键事件
					final int selectionIndex = category_tab_all_table.getSelectionIndex();
					
					//如果selectionIndex为-1则代表没有选中任何元素
					if(selectionIndex == -1){
						return;
					}
					TableItem setectionItem = category_tab_all_table.getSelection()[0];
					final Category Category = (Category) setectionItem.getData();
					
					
					Menu menu = new Menu(category_tab_all_table);
					category_tab_all_table.setMenu(menu);
					
					MenuItem item_Info = new MenuItem(menu, SWT.PUSH);  
					item_Info.setText("ID:"+Category.getCategory_id()+" 类别名称："+Category.getCategory_name());  
					
					MenuItem item_modify = new MenuItem(menu, SWT.PUSH);  
					item_modify.setText("编辑该类别");  
					item_modify.addSelectionListener(new SelectionAdapter() {
						@Override
						public void widgetSelected(SelectionEvent arg0) {
							// TODO Auto-generated method stub
							modifyTableItem(Category, selectionIndex);
							
						}
					});
					
					
					MenuItem item_delete = new MenuItem(menu, SWT.PUSH);  
					item_delete.setText("删除该类别");  
					item_delete.addSelectionListener(new SelectionAdapter() {
						@Override
						public void widgetSelected(SelectionEvent arg0) {
							// TODO Auto-generated method stub
							if(category_tab_modify != null){
								alert(getShell(), "删除错误", "正在进行编辑操作，请完成编辑之后再执行删除！");
								setTabFocus("Category_tab_modify");
								return;
							}
							
							if(!deleting){
								deleteTableItem(Category, selectionIndex);
							}
						}
					});
				}
				
			}
		});
		
		
		TableColumn idColumn = new TableColumn(category_tab_all_table, SWT.CENTER); 
		idColumn.setText("ID");
		idColumn.setWidth(marginWidthValue * 3);
		
		TableColumn nameColumn = new TableColumn(category_tab_all_table, SWT.CENTER); 
		nameColumn.setText("类别名称");
		nameColumn.setWidth(marginWidthValue * 10);
		
		TableColumn descColumn = new TableColumn(category_tab_all_table, SWT.CENTER); 
		descColumn.setText("类别描述");
		descColumn.setWidth(marginWidthValue * 18);
		
		category_tab_all.setControl(category_tab_all_table);
		
		new loadCategoryListAsyncHandler(This()).start("");
	}
	/**
	 * 删除表格元素
	 * @param Category
	 * @param index
	 */
	private void deleteTableItem(Category Category, int index){
		new deleteCategoryAsyncHandler(This()).start(Category);
	}
	/**
	 * 编辑表格元素
	 * @param Category
	 * @param index
	 */
	private void modifyTableItem(final Category Category, int index){
		if(category_tab_modify == null){
			// 创建编辑类别选项卡
			category_tab_modify = new TabItem(category_tab, SWT.NONE);
			category_tab_modify.setText("编辑类别 ID:"+Category.getCategory_id()+" 名称："+Category.getCategory_name());
			category_tab_modify.setData("Category_tab_modify");
			setTabFocus("Category_tab_modify");
			// 为选项卡增加内容
			final Composite add_content = new Composite(category_tab, SWT.NONE | SWT.WRAP);
			
			Label name_label = new Label(add_content, SWT.NONE);
			name_label.setText("类别名称");
			name_label.setBounds(marginWidthValue, marginHeightValue * 3, marginWidthValue * 5, marginHeightValue * 2);
			
			Text name_text = new Text(add_content, SWT.BORDER);
			name_text.setBounds(name_label.getBounds().x+name_label.getBounds().width, name_label.getBounds().y - (int)(marginHeightValue * 0.5), marginWidthValue * 15, marginHeightValue * 3);
			name_text.setData("name_text");
			name_text.setText(Category.getCategory_name());
			
			
			Label desc_label = new Label(add_content, SWT.NONE);
			desc_label.setText("类别描述");
			desc_label.setBounds(name_label.getBounds().x, name_label.getBounds().y+name_label.getBounds().height+marginHeightValue * 3, marginWidthValue * 5, marginHeightValue * 2);
			
			Text desc_text = new Text(add_content, SWT.BORDER | SWT.WRAP);
			desc_text.setBounds(desc_label.getBounds().x+desc_label.getBounds().width, desc_label.getBounds().y - (int)(marginHeightValue * 0.5), marginWidthValue * 15, marginHeightValue * 13);
			desc_text.setData("desc_text");
			desc_text.setText(Category.getCategory_desc());
			
			Button modify_button = new Button(add_content, SWT.NONE);
			modify_button.setText("确认修改");
			modify_button.setBounds(marginWidthValue * 25, marginHeightValue * 2, marginWidthValue * 10, marginHeightValue * 5);
			
			modify_button.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent arg0) {
					// TODO Auto-generated method stub
					
					validateModify(add_content, (Button) arg0.getSource(), Category.getCategory_id());
				}
				
			});
			
			Button cancel_button = new Button(add_content, SWT.NONE);
			cancel_button.setText("取消修改");
			cancel_button.setBounds(marginWidthValue * 25, modify_button.getBounds().y + modify_button.getBounds().height + marginHeightValue * 4, marginWidthValue * 10, marginHeightValue * 5);
			
			cancel_button.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent arg0) {
					// TODO Auto-generated method stub
					category_tab_modify.dispose();
					category_tab_modify = null;
				}
			});
			
			category_tab_modify.setControl(add_content);
		}else{
			
			if(confirm(getShell(), "确认操作", "当前已经有一个类别正在被编辑，之前未保存的数据将会丢失，要开始编辑吗？")){
				
				category_tab_modify.dispose();
				category_tab_modify = null;
				modifyTableItem(Category, index);
			}else{
				setTabFocus("Category_tab_modify");
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
			alert(getShell(), "修改错误", "类别名称不能为空，并且不能超过32个字符");
			return;
		}
		
		if(descValue == null || descValue.equals("")){
			alert(getShell(), "修改错误", "类别描述不能为空");
			return;
		}
		
		String[] values = {nameValue, descValue};
		new modifyCategoryAsyncHandler(This(), button, id).start(values);
	}
	
	/**
	 * 修改类别信息的AsyncHandler
	 * @author shihao
	 *
	 */
	private class modifyCategoryAsyncHandler extends AsyncHandler<String, Integer, Boolean>{

		private Button button;
		private int id;
		
		public modifyCategoryAsyncHandler(superWindow window, Button button, int id) {
			super(window);
			// TODO Auto-generated constructor stub
			this.button = button;
			this.id = id;
		}

		@Override
		public Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			CategoryDAO dao = new CategoryDAO();
			
			Category Category = new Category();
			Category.setCategory_name(params[0]);
			Category.setCategory_desc(params[1]);
			Category.setCategory_id(id);
			
			User user = (User) cacheMap.get("curUser");
			
			return dao.updateCategory(Category, user.getUser_id());
		}
		
		@Override
		public void onComplete(Boolean result) {
			// TODO Auto-generated method stub
			super.onComplete(result);
			
			if(result){
				alert(getShell(), "成功", "修改成功！");
				category_tab_modify.dispose();
				category_tab_modify = null;
				new loadCategoryListAsyncHandler(This()).start("");
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
		
		TabItem[] items = category_tab.getItems();
		int modify_index = -1;
		for(int i = 0 ; i < items.length ; i ++){
			TabItem item = items[i];
			if(item.getData() != null && item.getData().equals(tab)){
				modify_index = i;
				break;
			}
		}
		
		if(modify_index != -1){
			
			category_tab.setSelection(modify_index);
		}
	}
	
	
	/**
	 * 删除类别信息的AsyncHandler
	 * @author shihao
	 *
	 */
	private class deleteCategoryAsyncHandler extends AsyncHandler<Category, Integer, Boolean>{

		public deleteCategoryAsyncHandler(superWindow window) {
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
		public Boolean doInBackground(Category... params) {
			// TODO Auto-generated method stub
			CategoryDAO dao = new CategoryDAO();
			User user = (User) cacheMap.get("curUser");
			
			return dao.deleteCategory(params[0].getCategory_id(), user.getUser_id());
		}
		
		@Override
		public void onComplete(Boolean result) {
			// TODO Auto-generated method stub
			super.onComplete(result);
			deleting = false;
			if(result){
				ClearTableData();
				new loadCategoryListAsyncHandler(This()).start("");
				alert(getShell(), "成功", "删除类别信息成功！");
			}else{
				alert(getShell(), "失败", "删除类别信息失败，请稍后再试！");
			}
			
		}
		
	}
	/**
	 * 初始化按钮区域
	 */
	private void initButton(){
		category_button = new Composite(category_shell, SWT.NONE);
		category_button.setBounds(marginWidthValue, marginHeightValue, getShellWidth() - marginWidthValue * 2, marginHeightValue * 5);
		
		category_button_add = new Button(category_button, SWT.NONE);
		category_button_add.setText("添加类别");
		category_button_add.setImage(new ImageHandler(getDisplay(), "res/Add.png", 0.8f).getImage());
		category_button_add.setBounds(0, 0, marginWidthValue * 8, marginHeightValue * 5);
		
		category_button_add.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				addCategory();
			}
		});
		
		
	}
	/**
	 * 初始化界面基本数据
	 */
	private void initBase(){
		category_shell = new Shell(getParent().getShell(), SWT.CLOSE | SWT.APPLICATION_MODAL);
		category_shell.setText(category_title);
		category_shell.setSize(marginWidthValue * 40, marginHeightValue * 50);
		category_shell.setLocation(getCenterX(category_shell), getCenterY(category_shell));
	}
	/**
	 * 添加类别按钮的点击事件
	 */
	private void addCategory(){
		if(category_tab_add == null){
			// 创建添加类别选项卡
			category_tab_add = new TabItem(category_tab, SWT.NONE);
			category_tab_add.setText("添加类别");
			category_tab_add.setData("Category_tab_add");
			setTabFocus("Category_tab_add");
			// 为选项卡增加内容
			final Composite add_content = new Composite(category_tab, SWT.NONE | SWT.WRAP);
			
			Label name_label = new Label(add_content, SWT.NONE);
			name_label.setText("类别名称");
			name_label.setBounds(marginWidthValue, marginHeightValue * 3, marginWidthValue * 5, marginHeightValue * 2);
			
			Text name_text = new Text(add_content, SWT.BORDER);
			name_text.setBounds(name_label.getBounds().x+name_label.getBounds().width, name_label.getBounds().y - (int)(marginHeightValue * 0.5), marginWidthValue * 15, marginHeightValue * 3);
			name_text.setData("name_text");
			
			Label desc_label = new Label(add_content, SWT.NONE);
			desc_label.setText("类别描述");
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
					category_tab_add.dispose();
					category_tab_add = null;
				}
			});
			
			category_tab_add.setControl(add_content);
		}else{
			setTabFocus("Category_tab_add");
		}
		
		
	}
	/**
	 * 验证添加类别
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
			alert(getShell(), "添加错误", "类别名称不能为空，并且不能超过32个字符");
			return;
		}
		
		if(descValue == null || descValue.equals("")){
			alert(getShell(), "添加错误", "类别描述不能为空");
			return;
		}
		
		
		String[] values = {nameValue, descValue};
		new checkCategoryNameExistAsyncHandler(This(), button).start(values);
		
	}
	
	
	@Override
	protected Shell getShell() {
		// TODO Auto-generated method stub
		return category_shell;
	}
	/**
	 * 加载类别列表的asynchandler
	 * @author shihao
	 *
	 */
	private class loadCategoryListAsyncHandler extends AsyncHandler<String, Integer, Category[]>{

		public loadCategoryListAsyncHandler(superWindow window) {
			super(window);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onBefore() {
			// TODO Auto-generated method stub
			ClearTableData();
			
		}
		
		
		@Override
		public Category[] doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			CategoryDAO dao = new CategoryDAO();
			JSONObject response = dao.getAllCategory(0, 1000);
			try {
				JSONArray list = response.getJSONArray("list");
				Category[] Categorys = new Category[list.length()];
				
				for(int i = 0 ; i < list.length() ; i ++){
					Category f = new Category();
					JSONObject item = list.getJSONObject(i);
					f.setCategory_desc(item.getString("desc"));
					f.setCategory_id(item.getInt("cid"));
					f.setCategory_name(item.getString("name"));
					Categorys[i] = f;
				}
				
				return Categorys;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return null;
		}
		
		@Override
		public void onComplete(Category[] result) {
			// TODO Auto-generated method stub
			super.onComplete(result);
			
			if(result != null && result.length > 0){
				loadCategoryALL(result);
			}
			
		}
		
	}
	/**
	 * 清除table中的所有数据
	 */
	private void ClearTableData(){
		category_tab_all_table.removeAll();
	}
	/**
	 * 加载类别数据到界面
	 * @param Categorys
	 */
	private void loadCategoryALL(Category[] Categorys){
		
		for(Category f : Categorys){
			TableItem item = new TableItem(category_tab_all_table, SWT.NONE);
			item.setData(f);
			item.setText(new String[]{f.getCategory_id()+"",f.getCategory_name(),f.getCategory_desc()});
		}
		
	}
	/**
	 * 检查类别名称是否已存在
	 * @author shihao
	 *
	 */
	private class checkCategoryNameExistAsyncHandler extends AsyncHandler<String, Integer, Boolean>{

		private String[] values = null;
		private Button button = null;
		public checkCategoryNameExistAsyncHandler(superWindow window, Button button) {
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
			
			CategoryDAO dao = new CategoryDAO();
			
			return dao.checkCategoryNameIsExist(values[0]);
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
				alert(getShell(), "添加错误", "类别名称已经存在！");
			}
			
		}
	}
	/**
	 * 执行添加类别操作的AsyncHandler
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
			
			CategoryDAO dao = new CategoryDAO();
			
			Category Category = new Category();
			Category.setCategory_name(params[0]);
			Category.setCategory_desc(params[1]);
			
			User user = (User) cacheMap.get("curUser");
			
			return dao.addCategory(Category, user.getUser_id());
		}
		
		@Override
		public void onComplete(Boolean result) {
			// TODO Auto-generated method stub
			super.onComplete(result);
			
			if(result){
				alert(getShell(), "添加成功", "添加成功！");
				category_tab_add.dispose();
				category_tab_add = null;
				new loadCategoryListAsyncHandler(This()).start("");
			}else{
				alert(getShell(), "添加错误", "添加错误！请稍后再试！");
			}
			button.setText("确认添加");
			button.setEnabled(true);
			
		}
	}

}
