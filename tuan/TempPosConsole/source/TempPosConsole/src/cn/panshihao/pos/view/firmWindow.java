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

import cn.panshihao.pos.dao.FirmDAO;
import cn.panshihao.pos.handler.AsyncHandler;
import cn.panshihao.pos.handler.ImageHandler;
import cn.panshihao.pos.model.Firm;
import cn.panshihao.pos.model.User;

public class firmWindow extends superWindow {

	
	public String firm_title = "商家管理";
	public Shell firm_shell = null;
	
	/*
	 * 按钮区域 
	 */
	public Composite firm_button = null;
	public Button firm_button_add = null;
	
	/*
	 * 选项卡区域
	 */
	public TabFolder firm_tab = null;
	public TabItem firm_tab_all = null;
	public TabItem firm_tab_add = null;
	public TabItem firm_tab_modify = null;
	
	/*
	 * 表格
	 */
	public Table firm_tab_all_table = null;
	
	
	private boolean deleting = false;
	
	
	public firmWindow(superWindow parent) {
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
		
		firm_shell.open();
		firm_shell.layout();
		
	}
	/**
	 * 初始化选项卡区域
	 */
	private void initTab(){
		firm_tab = new TabFolder(firm_shell, SWT.NONE);
		firm_tab.setBounds(marginWidthValue, firm_button.getBounds().height + marginHeightValue * 2, getShellWidth() - marginWidthValue * 2, getShellHeight() - firm_button.getBounds().height - marginHeightValue * 4);
		
		firm_tab_all = new TabItem(firm_tab, SWT.NONE);
		firm_tab_all.setText("全部商家");
		
	}
	/**
	 * 加载Tab数据
	 */
	private void initTable(){
		
		firm_tab_all_table = new Table(firm_tab, SWT.FULL_SELECTION);
		firm_tab_all_table.setHeaderVisible(true);
		firm_tab_all_table.setLinesVisible(true);
		firm_tab_all_table.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseDown(MouseEvent e) {
				// TODO Auto-generated method stub
				if(e.button == 3){
					// item右键事件
					final int selectionIndex = firm_tab_all_table.getSelectionIndex();
					
					//如果selectionIndex为-1则代表没有选中任何元素
					if(selectionIndex == -1){
						return;
					}
					TableItem setectionItem = firm_tab_all_table.getSelection()[0];
					final Firm firm = (Firm) setectionItem.getData();
					
					
					Menu menu = new Menu(firm_tab_all_table);
					firm_tab_all_table.setMenu(menu);
					
					MenuItem item_Info = new MenuItem(menu, SWT.PUSH);  
					item_Info.setText("ID:"+firm.getFirm_id()+" 商家名称："+firm.getFirm_name());  
					
					MenuItem item_modify = new MenuItem(menu, SWT.PUSH);  
					item_modify.setText("编辑该商家");  
					item_modify.addSelectionListener(new SelectionAdapter() {
						@Override
						public void widgetSelected(SelectionEvent arg0) {
							// TODO Auto-generated method stub
							modifyTableItem(firm, selectionIndex);
							
						}
					});
					
					
					MenuItem item_delete = new MenuItem(menu, SWT.PUSH);  
					item_delete.setText("删除该商家");  
					item_delete.addSelectionListener(new SelectionAdapter() {
						@Override
						public void widgetSelected(SelectionEvent arg0) {
							// TODO Auto-generated method stub
							if(firm_tab_modify != null){
								alert(getShell(), "删除错误", "正在进行编辑操作，请完成编辑之后再执行删除！");
								setTabFocus("firm_tab_modify");
								return;
							}
							
							if(!deleting){
								deleteTableItem(firm, selectionIndex);
							}
						}
					});
				}
				
			}
		});
		
		
		TableColumn idColumn = new TableColumn(firm_tab_all_table, SWT.CENTER); 
		idColumn.setText("ID");
		idColumn.setWidth(marginWidthValue * 3);
		
		TableColumn nameColumn = new TableColumn(firm_tab_all_table, SWT.CENTER); 
		nameColumn.setText("商家名称");
		nameColumn.setWidth(marginWidthValue * 10);
		
		TableColumn descColumn = new TableColumn(firm_tab_all_table, SWT.CENTER); 
		descColumn.setText("商家描述");
		descColumn.setWidth(marginWidthValue * 18);
		
		TableColumn addressColumn = new TableColumn(firm_tab_all_table, SWT.CENTER); 
		addressColumn.setText("商家地址");
		addressColumn.setWidth(marginWidthValue * 15);
		
		TableColumn phoneColumn = new TableColumn(firm_tab_all_table, SWT.CENTER); 
		phoneColumn.setText("联系电话");
		phoneColumn.setWidth(marginWidthValue * 10);
		
		TableColumn personColumn = new TableColumn(firm_tab_all_table, SWT.CENTER); 
		personColumn.setText("联系人");
		personColumn.setWidth(marginWidthValue * 5);
		
		firm_tab_all.setControl(firm_tab_all_table);
		
		new loadFirmListAsyncHandler(This()).start("");
	}
	/**
	 * 删除表格元素
	 * @param firm
	 * @param index
	 */
	private void deleteTableItem(Firm firm, int index){
		new deleteFirmAsyncHandler(This()).start(firm);
	}
	/**
	 * 编辑表格元素
	 * @param firm
	 * @param index
	 */
	private void modifyTableItem(final Firm firm, int index){
		if(firm_tab_modify == null){
			// 创建编辑商家选项卡
			firm_tab_modify = new TabItem(firm_tab, SWT.NONE);
			firm_tab_modify.setText("编辑商家 ID:"+firm.getFirm_id()+" 名称："+firm.getFirm_name());
			firm_tab_modify.setData("firm_tab_modify");
			setTabFocus("firm_tab_modify");
			// 为选项卡增加内容
			final Composite add_content = new Composite(firm_tab, SWT.NONE | SWT.WRAP);
			
			Label name_label = new Label(add_content, SWT.NONE);
			name_label.setText("商家名称");
			name_label.setBounds(marginWidthValue, marginHeightValue * 3, marginWidthValue * 5, marginHeightValue * 2);
			
			Text name_text = new Text(add_content, SWT.BORDER);
			name_text.setBounds(name_label.getBounds().x+name_label.getBounds().width, name_label.getBounds().y - (int)(marginHeightValue * 0.5), marginWidthValue * 20, marginHeightValue * 3);
			name_text.setData("name_text");
			name_text.setText(firm.getFirm_name());
			
			
			Label desc_label = new Label(add_content, SWT.NONE);
			desc_label.setText("商家描述");
			desc_label.setBounds(name_label.getBounds().x, name_label.getBounds().y+name_label.getBounds().height+marginHeightValue * 3, marginWidthValue * 5, marginHeightValue * 2);
			
			Text desc_text = new Text(add_content, SWT.BORDER | SWT.WRAP);
			desc_text.setBounds(desc_label.getBounds().x+desc_label.getBounds().width, desc_label.getBounds().y - (int)(marginHeightValue * 0.5), marginWidthValue * 20, marginHeightValue * 13);
			desc_text.setData("desc_text");
			desc_text.setText(firm.getFirm_desc());
			
			Label address_label = new Label(add_content, SWT.NONE);
			address_label.setText("商家地址");
			address_label.setBounds(desc_label.getBounds().x, desc_text.getBounds().y+desc_text.getBounds().height+marginHeightValue * 3, marginWidthValue * 5, marginHeightValue * 2);
			
			Text address_text = new Text(add_content, SWT.BORDER | SWT.WRAP);
			address_text.setBounds(address_label.getBounds().x+address_label.getBounds().width, address_label.getBounds().y - (int)(marginHeightValue * 0.5), marginWidthValue * 20, marginHeightValue * 13);
			address_text.setData("address_text");
			address_text.setText(firm.getFirm_address());
			
			Label phone_label = new Label(add_content, SWT.NONE);
			phone_label.setText("联系电话");
			phone_label.setBounds(address_label.getBounds().x, address_text.getBounds().y+address_text.getBounds().height+marginHeightValue * 3, marginWidthValue * 5, marginHeightValue * 2);
			
			Text phone_text = new Text(add_content, SWT.BORDER);
			phone_text.setBounds(phone_label.getBounds().x+phone_label.getBounds().width, phone_label.getBounds().y - (int)(marginHeightValue * 0.5), marginWidthValue * 20, marginHeightValue * 3);
			phone_text.setData("phone_text");
			phone_text.setText(firm.getFirm_phone());
			
			Label person_label = new Label(add_content, SWT.NONE);
			person_label.setText("联系人");
			person_label.setBounds(phone_label.getBounds().x, phone_label.getBounds().y+phone_label.getBounds().height+marginHeightValue * 3, marginWidthValue * 5, marginHeightValue * 2);
			
			Text person_text = new Text(add_content, SWT.BORDER);
			person_text.setBounds(person_label.getBounds().x+person_label.getBounds().width, person_label.getBounds().y - (int)(marginHeightValue * 0.5), marginWidthValue * 20, marginHeightValue * 3);
			person_text.setData("person_text");
			person_text.setText(firm.getFirm_person());
			
			Button modify_button = new Button(add_content, SWT.NONE);
			modify_button.setText("确认修改");
			modify_button.setBounds(marginWidthValue * 35, marginHeightValue * 2, marginWidthValue * 10, marginHeightValue * 5);
			
			modify_button.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent arg0) {
					// TODO Auto-generated method stub
					
					validateModify(add_content, (Button) arg0.getSource(), firm.getFirm_id());
				}
				
			});
			
			Button cancel_button = new Button(add_content, SWT.NONE);
			cancel_button.setText("取消修改");
			cancel_button.setBounds(marginWidthValue * 35, modify_button.getBounds().y + modify_button.getBounds().height + marginHeightValue * 4, marginWidthValue * 10, marginHeightValue * 5);
			
			cancel_button.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent arg0) {
					// TODO Auto-generated method stub
					firm_tab_modify.dispose();
					firm_tab_modify = null;
				}
			});
			
			firm_tab_modify.setControl(add_content);
		}else{
			
			if(confirm(getShell(), "确认操作", "当前已经有一个商家正在被编辑，之前未保存的数据将会丢失，要开始编辑吗？")){
				
				firm_tab_modify.dispose();
				firm_tab_modify = null;
				modifyTableItem(firm, index);
			}else{
				setTabFocus("firm_tab_modify");
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
		String addressValue = null;
		String phoneValue = null;
		String personValue = null;
		
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
				case "address_text":
					addressValue = text.getText();
					break;
				case "phone_text":
					phoneValue = text.getText();
					break;
				case "person_text":
					personValue = text.getText();
					break;
				default:
					break;
				}
				
			}
		}
		
		if(nameValue == null || nameValue.equals("") || phoneValue.length() > 32){
			alert(getShell(), "修改错误", "商家名称不能为空，并且不能超过32个字符");
			return;
		}
		
		if(descValue == null || descValue.equals("")){
			alert(getShell(), "修改错误", "商家描述不能为空");
			return;
		}
		
		if(addressValue == null || addressValue.equals("") || phoneValue.length() > 255){
			alert(getShell(), "修改错误", "商家地址不能为空，并且不能超过255个字符");
			return;
		}
		
		if(phoneValue == null || phoneValue.equals("") || phoneValue.length() > 15){
			alert(getShell(), "修改错误", "联系电话不能为空，并且不能超过15个数字");
			return;
		}
		
		if(personValue == null || personValue.equals("") || phoneValue.length() > 32){
			alert(getShell(), "修改错误", "联系人不能为空，并且不能超过32个字符");
			return;
		}
		
		String[] values = {nameValue, descValue, addressValue, phoneValue, personValue};
		new modifyFirmAsyncHandler(This(), button, id).start(values);
	}
	
	/**
	 * 修改商家信息的AsyncHandler
	 * @author shihao
	 *
	 */
	private class modifyFirmAsyncHandler extends AsyncHandler<String, Integer, Boolean>{

		private Button button;
		private int id;
		
		public modifyFirmAsyncHandler(superWindow window, Button button, int id) {
			super(window);
			// TODO Auto-generated constructor stub
			this.button = button;
			this.id = id;
		}

		@Override
		public Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			FirmDAO dao = new FirmDAO();
			
			Firm firm = new Firm();
			firm.setFirm_name(params[0]);
			firm.setFirm_desc(params[1]);
			firm.setFirm_address(params[2]);
			firm.setFirm_phone(params[3]);
			firm.setFirm_person(params[4]);
			firm.setFirm_id(id);
			
			User user = (User) cacheMap.get("curUser");
			
			return dao.updateFirm(firm, user.getUser_id());
		}
		
		@Override
		public void onComplete(Boolean result) {
			// TODO Auto-generated method stub
			super.onComplete(result);
			
			if(result){
				alert(getShell(), "成功", "修改成功！");
				firm_tab_modify.dispose();
				firm_tab_modify = null;
				new loadFirmListAsyncHandler(This()).start("");
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
		
		TabItem[] items = firm_tab.getItems();
		int modify_index = -1;
		for(int i = 0 ; i < items.length ; i ++){
			TabItem item = items[i];
			if(item.getData() != null && item.getData().equals(tab)){
				modify_index = i;
				break;
			}
		}
		
		if(modify_index != -1){
			
			firm_tab.setSelection(modify_index);
		}
	}
	
	
	/**
	 * 删除商家信息的AsyncHandler
	 * @author shihao
	 *
	 */
	private class deleteFirmAsyncHandler extends AsyncHandler<Firm, Integer, Boolean>{

		public deleteFirmAsyncHandler(superWindow window) {
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
		public Boolean doInBackground(Firm... params) {
			// TODO Auto-generated method stub
			FirmDAO dao = new FirmDAO();
			User user = (User) cacheMap.get("curUser");
			
			return dao.deleteFirm(params[0].getFirm_id(), user.getUser_id());
		}
		
		@Override
		public void onComplete(Boolean result) {
			// TODO Auto-generated method stub
			super.onComplete(result);
			deleting = false;
			if(result){
				ClearTableData();
				new loadFirmListAsyncHandler(This()).start("");
				alert(getShell(), "成功", "删除商家信息成功！");
			}else{
				alert(getShell(), "失败", "删除商家信息失败，请稍后再试！");
			}
			
		}
		
	}
	/**
	 * 初始化按钮区域
	 */
	private void initButton(){
		firm_button = new Composite(firm_shell, SWT.NONE);
		firm_button.setBounds(marginWidthValue, marginHeightValue, getShellWidth() - marginWidthValue * 2, marginHeightValue * 5);
		
		firm_button_add = new Button(firm_button, SWT.NONE);
		firm_button_add.setText("添加商家");
		firm_button_add.setImage(new ImageHandler(getDisplay(), "res/Add.png", 0.8f).getImage());
		firm_button_add.setBounds(0, 0, marginWidthValue * 8, marginHeightValue * 5);
		
		firm_button_add.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				addFirm();
			}
		});
		
		
	}
	/**
	 * 初始化界面基本数据
	 */
	private void initBase(){
		firm_shell = new Shell(getParent().getShell(), SWT.CLOSE | SWT.APPLICATION_MODAL);
		firm_shell.setText(firm_title);
		firm_shell.setSize(marginWidthValue * 70, marginHeightValue * 70);
		firm_shell.setLocation(getCenterX(firm_shell), getCenterY(firm_shell));
	}
	/**
	 * 添加商家按钮的点击事件
	 */
	private void addFirm(){
		if(firm_tab_add == null){
			// 创建添加商家选项卡
			firm_tab_add = new TabItem(firm_tab, SWT.NONE);
			firm_tab_add.setText("添加商家");
			firm_tab_add.setData("firm_tab_add");
			setTabFocus("firm_tab_add");
			// 为选项卡增加内容
			final Composite add_content = new Composite(firm_tab, SWT.NONE | SWT.WRAP);
			
			Label name_label = new Label(add_content, SWT.NONE);
			name_label.setText("商家名称");
			name_label.setBounds(marginWidthValue, marginHeightValue * 3, marginWidthValue * 5, marginHeightValue * 2);
			
			Text name_text = new Text(add_content, SWT.BORDER);
			name_text.setBounds(name_label.getBounds().x+name_label.getBounds().width, name_label.getBounds().y - (int)(marginHeightValue * 0.5), marginWidthValue * 20, marginHeightValue * 3);
			name_text.setData("name_text");
			
			Label desc_label = new Label(add_content, SWT.NONE);
			desc_label.setText("商家描述");
			desc_label.setBounds(name_label.getBounds().x, name_label.getBounds().y+name_label.getBounds().height+marginHeightValue * 3, marginWidthValue * 5, marginHeightValue * 2);
			
			Text desc_text = new Text(add_content, SWT.BORDER | SWT.WRAP);
			desc_text.setBounds(desc_label.getBounds().x+desc_label.getBounds().width, desc_label.getBounds().y - (int)(marginHeightValue * 0.5), marginWidthValue * 20, marginHeightValue * 13);
			desc_text.setData("desc_text");
			
			Label address_label = new Label(add_content, SWT.NONE);
			address_label.setText("商家地址");
			address_label.setBounds(desc_label.getBounds().x, desc_text.getBounds().y+desc_text.getBounds().height+marginHeightValue * 3, marginWidthValue * 5, marginHeightValue * 2);
			
			Text address_text = new Text(add_content, SWT.BORDER | SWT.WRAP);
			address_text.setBounds(address_label.getBounds().x+address_label.getBounds().width, address_label.getBounds().y - (int)(marginHeightValue * 0.5), marginWidthValue * 20, marginHeightValue * 13);
			address_text.setData("address_text");
			
			Label phone_label = new Label(add_content, SWT.NONE);
			phone_label.setText("联系电话");
			phone_label.setBounds(address_label.getBounds().x, address_text.getBounds().y+address_text.getBounds().height+marginHeightValue * 3, marginWidthValue * 5, marginHeightValue * 2);
			
			Text phone_text = new Text(add_content, SWT.BORDER);
			phone_text.setBounds(phone_label.getBounds().x+phone_label.getBounds().width, phone_label.getBounds().y - (int)(marginHeightValue * 0.5), marginWidthValue * 20, marginHeightValue * 3);
			phone_text.setData("phone_text");
			
			Label person_label = new Label(add_content, SWT.NONE);
			person_label.setText("联系人");
			person_label.setBounds(phone_label.getBounds().x, phone_label.getBounds().y+phone_label.getBounds().height+marginHeightValue * 3, marginWidthValue * 5, marginHeightValue * 2);
			
			Text person_text = new Text(add_content, SWT.BORDER);
			person_text.setBounds(person_label.getBounds().x+person_label.getBounds().width, person_label.getBounds().y - (int)(marginHeightValue * 0.5), marginWidthValue * 20, marginHeightValue * 3);
			person_text.setData("person_text");
			
			Button add_button = new Button(add_content, SWT.NONE);
			add_button.setText("确认添加");
			add_button.setBounds(marginWidthValue * 35, marginHeightValue * 2, marginWidthValue * 10, marginHeightValue * 5);
			
			add_button.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent arg0) {
					// TODO Auto-generated method stub
					
					validateAdd(add_content, (Button) arg0.getSource());
				}
				
			});
			
			Button cancel_button = new Button(add_content, SWT.NONE);
			cancel_button.setText("取消添加");
			cancel_button.setBounds(marginWidthValue * 35, add_button.getBounds().y + add_button.getBounds().height + marginHeightValue * 4, marginWidthValue * 10, marginHeightValue * 5);
			
			cancel_button.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent arg0) {
					// TODO Auto-generated method stub
					firm_tab_add.dispose();
					firm_tab_add = null;
				}
			});
			
			firm_tab_add.setControl(add_content);
		}else{
			setTabFocus("firm_tab_add");
		}
		
		
	}
	/**
	 * 验证添加商家
	 */
	private void validateAdd(Composite content, Button button){
		Control[] children = content.getChildren();
		
		String nameValue = null;
		String descValue = null;
		String addressValue = null;
		String phoneValue = null;
		String personValue = null;
		
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
				case "address_text":
					addressValue = text.getText();
					break;
				case "phone_text":
					phoneValue = text.getText();
					break;
				case "person_text":
					personValue = text.getText();
					break;
				default:
					break;
				}
				
			}
		}
		
		if(nameValue == null || nameValue.equals("") || nameValue.length() > 32){
			alert(getShell(), "添加错误", "商家名称不能为空，并且不能超过32个字符");
			return;
		}
		
		if(descValue == null || descValue.equals("")){
			alert(getShell(), "添加错误", "商家描述不能为空");
			return;
		}
		
		if(addressValue == null || addressValue.equals("") || addressValue.length() > 255){
			alert(getShell(), "添加错误", "商家地址不能为空，并且不能超过255个字符");
			return;
		}
		
		if(phoneValue == null || phoneValue.equals("") || phoneValue.length() > 15){
			alert(getShell(), "添加错误", "联系电话不能为空，并且不能超过15个数字");
			return;
		}
		
		if(personValue == null || personValue.equals("") || personValue.length() > 32){
			alert(getShell(), "添加错误", "联系人不能为空，并且不能超过32个字符");
			return;
		}
		
		String[] values = {nameValue, descValue, addressValue, phoneValue, personValue};
		new checkFirmNameExistAsyncHandler(This(), button).start(values);
		
	}
	
	
	@Override
	protected Shell getShell() {
		// TODO Auto-generated method stub
		return firm_shell;
	}
	/**
	 * 加载商家列表的asynchandler
	 * @author shihao
	 *
	 */
	private class loadFirmListAsyncHandler extends AsyncHandler<String, Integer, Firm[]>{

		public loadFirmListAsyncHandler(superWindow window) {
			super(window);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onBefore() {
			// TODO Auto-generated method stub
			ClearTableData();
			
		}
		
		
		@Override
		public Firm[] doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			FirmDAO dao = new FirmDAO();
			JSONObject response = dao.getAllFirm(0, 1000);
			try {
				JSONArray list = response.getJSONArray("list");
				Firm[] firms = new Firm[list.length()];
				
				for(int i = 0 ; i < list.length() ; i ++){
					Firm f = new Firm();
					JSONObject item = list.getJSONObject(i);
					f.setFirm_address(item.getString("address"));
					f.setFirm_desc(item.getString("desc"));
					f.setFirm_id(item.getInt("fid"));
					f.setFirm_name(item.getString("name"));
					f.setFirm_person(item.getString("person"));
					f.setFirm_phone(item.getString("phone"));
					firms[i] = f;
				}
				
				return firms;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return null;
		}
		
		@Override
		public void onComplete(Firm[] result) {
			// TODO Auto-generated method stub
			super.onComplete(result);
			
			if(result != null && result.length > 0){
				loadFirmALL(result);
			}
			
		}
		
	}
	/**
	 * 清除table中的所有数据
	 */
	private void ClearTableData(){
		firm_tab_all_table.removeAll();
	}
	/**
	 * 加载商家数据到界面
	 * @param firms
	 */
	private void loadFirmALL(Firm[] firms){
		
		for(Firm f : firms){
			TableItem item = new TableItem(firm_tab_all_table, SWT.NONE);
			item.setData(f);
			item.setText(new String[]{f.getFirm_id()+"",f.getFirm_name(),f.getFirm_desc(),f.getFirm_address(),f.getFirm_phone(),f.getFirm_person()});
		}
		
	}
	/**
	 * 检查商家名称是否已存在
	 * @author shihao
	 *
	 */
	private class checkFirmNameExistAsyncHandler extends AsyncHandler<String, Integer, Boolean>{

		private String[] values = null;
		private Button button = null;
		public checkFirmNameExistAsyncHandler(superWindow window, Button button) {
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
			
			FirmDAO dao = new FirmDAO();
			
			return dao.checkFirmNameIsExist(values[0]);
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
				alert(getShell(), "添加错误", "商家名称已经存在！");
			}
			
		}
	}
	/**
	 * 执行添加商家操作的AsyncHandler
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
			
			FirmDAO dao = new FirmDAO();
			
			Firm firm = new Firm();
			firm.setFirm_name(params[0]);
			firm.setFirm_desc(params[1]);
			firm.setFirm_address(params[2]);
			firm.setFirm_phone(params[3]);
			firm.setFirm_person(params[4]);
			
			User user = (User) cacheMap.get("curUser");
			
			return dao.addFirm(firm, user.getUser_id());
		}
		
		@Override
		public void onComplete(Boolean result) {
			// TODO Auto-generated method stub
			super.onComplete(result);
			
			if(result){
				alert(getShell(), "添加成功", "添加成功！");
				firm_tab_add.dispose();
				firm_tab_add = null;
				new loadFirmListAsyncHandler(This()).start("");
			}else{
				alert(getShell(), "添加错误", "添加错误！请稍后再试！");
			}
			button.setText("确认添加");
			button.setEnabled(true);
			
		}
	}

}
