package cn.panshihao.pos.view;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.panshihao.pos.component.NumberText;
import cn.panshihao.pos.dao.CategoryDAO;
import cn.panshihao.pos.dao.TuanDAO;
import cn.panshihao.pos.handler.AsyncHandler;
import cn.panshihao.pos.model.Category;
import cn.panshihao.pos.model.Firm;
import cn.panshihao.pos.model.Tuan;

public class createTuanWindow extends superWindow {

	public String createTuan_title = "创建团购";
	public Shell createTuan_shell;
	
	/*
	 * 表单
	 */
	public Label createTuan_form_name_label = null;
	public Label createTuan_form_desc_label = null;
	public Label createTuan_form_frim_label = null;
	public Label createTuan_form_category_label = null;
	public Label createTuan_form_starttime_label = null;
	public Label createTuan_form_endtime_label = null;
	public Label createTuan_form_key_label = null;
	
	public Text createTuan_form_name_text = null;
	public Text createTuan_form_desc_text = null;
	public DateTime createTuan_form_starttime_datetime = null;
	public DateTime createTuan_form_endtime_datetime = null;
	public Text createTuan_form_key_numbertext = null;
	public Text createTuan_form_frim_text = null;
	public Button createTuan_form_frim_button = null;
	
	public Text createTuan_form_category_text = null;
	public Button createTuan_form_category_button = null;
	
	public Button createTuan_form_button_create = null;
	
	public onResultListener<Tuan> listener = null;
	
	public createTuanWindow(superWindow parent) {
		super(parent);
		// TODO Auto-generated constructor stub
	}
	public createTuanWindow(superWindow parent, onResultListener<Tuan> listener){
		super(parent);
		this.listener = listener;
	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub
		initBase();
		
		initForm();
		
		createTuan_shell.open();
		createTuan_shell.layout();
		
	}
	/**
	 * 初始化创建表单
	 */
	private void initForm(){
		
		createTuan_form_name_label = new Label(getShell(), SWT.NONE);
		createTuan_form_name_label.setText("团购名称");
		createTuan_form_name_label.setBounds(marginWidthValue, marginHeightValue * 3, marginWidthValue * 5, marginHeightValue * 3);
		
		createTuan_form_name_text = new Text(getShell(), SWT.BORDER);
		createTuan_form_name_text.setBounds(createTuan_form_name_label.getBounds().x + createTuan_form_name_label.getBounds().width, createTuan_form_name_label.getBounds().y - (int)(marginHeightValue * 0.5), marginWidthValue * 15, marginHeightValue * 3);
		
		createTuan_form_desc_label = new Label(getShell(), SWT.NONE);
		createTuan_form_desc_label.setText("团购描述");
		createTuan_form_desc_label.setBounds(createTuan_form_name_label.getBounds().x, createTuan_form_name_label.getBounds().y + createTuan_form_name_label.getBounds().height + marginHeightValue * 2, marginWidthValue * 5, marginHeightValue * 3);
		
		createTuan_form_desc_text = new Text(getShell(), SWT.BORDER | SWT.WRAP);
		createTuan_form_desc_text.setBounds(createTuan_form_desc_label.getBounds().x + createTuan_form_desc_label.getBounds().width, createTuan_form_desc_label.getBounds().y - (int)(marginHeightValue * 0.5), marginWidthValue * 15, marginHeightValue * 13);
		
		createTuan_form_category_label = new Label(getShell(), SWT.NONE);
		createTuan_form_category_label.setText("团购类别");
		createTuan_form_category_label.setBounds(createTuan_form_desc_label.getBounds().x, createTuan_form_desc_text.getBounds().y + createTuan_form_desc_text.getBounds().height + marginHeightValue * 2, marginWidthValue * 5, marginHeightValue * 3);
		
//		createTuan_form_category_combo = new Combo(getShell(), SWT.READ_ONLY);
//		createTuan_form_category_combo.setBounds(createTuan_form_category_label.getBounds().x + createTuan_form_category_label.getBounds().width, createTuan_form_category_label.getBounds().y - (int)(marginHeightValue * 0.5), marginWidthValue * 15, marginHeightValue * 3);
		
		
		createTuan_form_category_button = new Button(getShell(), SWT.NONE);
		createTuan_form_category_button.setText("选择类别");
		createTuan_form_category_button.setBounds(createTuan_form_category_label.getBounds().x + createTuan_form_category_label.getBounds().width + marginWidthValue * 15 - marginWidthValue * 5, createTuan_form_category_label.getBounds().y - (int)(marginHeightValue * 0.3), marginWidthValue * 5, (int)(marginHeightValue * 2.5));
		createTuan_form_category_button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				new categoryWindow(This(), new onResultListener<Category>() {
					
					@Override
					public void onResult(Category result) {
						// TODO Auto-generated method stub
						createTuan_form_category_text.setData(result);
						createTuan_form_category_text.setText(result.getCategory_name());
					}
				}).show();
				
			}
		});
		
		
		createTuan_form_category_text = new Text(getShell(), SWT.READ_ONLY | SWT.BORDER);
		createTuan_form_category_text.setBounds(createTuan_form_category_label.getBounds().x + createTuan_form_category_label.getBounds().width, createTuan_form_category_label.getBounds().y - (int)(marginHeightValue * 0.5), marginWidthValue * 15, marginHeightValue * 3);
		
		
		createTuan_form_frim_label = new Label(getShell(), SWT.NONE);
		createTuan_form_frim_label.setText("发布商家");
		createTuan_form_frim_label.setBounds(createTuan_form_category_label.getBounds().x, createTuan_form_category_label.getBounds().y + createTuan_form_category_label.getBounds().height + marginHeightValue * 2, marginWidthValue * 5, marginHeightValue * 3);
		
		createTuan_form_frim_button = new Button(getShell(), SWT.NONE);
		createTuan_form_frim_button.setText("选择商家");
		createTuan_form_frim_button.setBounds(createTuan_form_frim_label.getBounds().x + createTuan_form_frim_label.getBounds().width + marginWidthValue * 15 - marginWidthValue * 5, createTuan_form_frim_label.getBounds().y - (int)(marginHeightValue * 0.3), marginWidthValue * 5, (int)(marginHeightValue * 2.5));
		createTuan_form_frim_button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				new firmWindow(This(), new onResultListener<Firm>() {
					
					@Override
					public void onResult(Firm result) {
						// TODO Auto-generated method stub
						createTuan_form_frim_text.setData(result);
						createTuan_form_frim_text.setText(result.getFirm_name());
					}
					
				}).show();
				
			}
		});
		
		
		createTuan_form_frim_text = new Text(getShell(), SWT.READ_ONLY | SWT.BORDER);
		createTuan_form_frim_text.setBounds(createTuan_form_frim_label.getBounds().x + createTuan_form_frim_label.getBounds().width, createTuan_form_frim_label.getBounds().y - (int)(marginHeightValue * 0.5), marginWidthValue * 15, marginHeightValue * 3);
		
		
		
		createTuan_form_starttime_label = new Label(getShell(), SWT.NONE);
		createTuan_form_starttime_label.setText("开始时间");
		createTuan_form_starttime_label.setBounds(createTuan_form_frim_label.getBounds().x, createTuan_form_frim_label.getBounds().y + createTuan_form_frim_label.getBounds().height + marginHeightValue * 2, marginWidthValue * 5, marginHeightValue * 3);
		
		createTuan_form_starttime_datetime = new DateTime(getShell(), SWT.BORDER | SWT.DROP_DOWN | SWT.LONG);
		createTuan_form_starttime_datetime.setBounds(createTuan_form_starttime_label.getBounds().x + createTuan_form_starttime_label.getBounds().width, createTuan_form_starttime_label.getBounds().y - (int)(marginHeightValue * 0.5), marginWidthValue * 15, marginHeightValue * 3);
		
		createTuan_form_endtime_label = new Label(getShell(), SWT.NONE);
		createTuan_form_endtime_label.setText("结束时间");
		createTuan_form_endtime_label.setBounds(createTuan_form_starttime_label.getBounds().x, createTuan_form_starttime_label.getBounds().y + createTuan_form_starttime_label.getBounds().height + marginHeightValue * 2, marginWidthValue * 5, marginHeightValue * 3);
		
		createTuan_form_endtime_datetime = new DateTime(getShell(), SWT.BORDER | SWT.DROP_DOWN | SWT.LONG);
		createTuan_form_endtime_datetime.setBounds(createTuan_form_endtime_label.getBounds().x + createTuan_form_endtime_label.getBounds().width, createTuan_form_endtime_label.getBounds().y - (int)(marginHeightValue * 0.5), marginWidthValue * 15, marginHeightValue * 3);
		
		createTuan_form_key_label = new Label(getShell(), SWT.NONE);
		createTuan_form_key_label.setText("兑换码数量");
		createTuan_form_key_label.setBounds(createTuan_form_endtime_label.getBounds().x, createTuan_form_endtime_label.getBounds().y + createTuan_form_endtime_label.getBounds().height + marginHeightValue * 2, marginWidthValue * 5, marginHeightValue * 3);
		
		createTuan_form_key_numbertext = new Text(getShell(), SWT.BORDER);
		createTuan_form_key_numbertext.setTextLimit(4);
		createTuan_form_key_numbertext.setText("0");
		createTuan_form_key_numbertext.setBounds(createTuan_form_key_label.getBounds().x + createTuan_form_key_label.getBounds().width, createTuan_form_key_label.getBounds().y - (int)(marginHeightValue * 0.5), marginWidthValue * 15, marginHeightValue * 3);
		createTuan_form_key_numbertext.addVerifyListener(new VerifyListener() {

			@Override
			public void verifyText(VerifyEvent e) {
				// TODO Auto-generated method stub
				// 几种情况，输入控制键，输入中文，输入字符，输入数字   
			    // 正整数验证   
			    Pattern pattern = Pattern.compile("[0-9]\\d*");   
			    Matcher matcher = pattern.matcher(e.text);   
			    if (matcher.matches()){ // 处理数字   
			    	e.doit = true;   	
			    }
			    else if (e.text.length() > 0){ // 有字符情况,包含中文、空格   
			    	e.doit = false;   
			    }
			    else {
			    	// 控制键   
			    	e.doit = true;   
			    }
			}   
			      
		});
		
		createTuan_form_button_create = new Button(createTuan_shell, SWT.NONE);
		createTuan_form_button_create.setText("确认创建");
		createTuan_form_button_create.setBounds(marginWidthValue * 25, marginHeightValue * 3, marginWidthValue * 8, marginHeightValue * 4);
		createTuan_form_button_create.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				validateCreate();
			}
		});
	}
	/**
	 * 验证创建
	 */
	private void validateCreate(){
		String nameValue = createTuan_form_name_text.getText();
		String descValue = createTuan_form_desc_text.getText();
		Category categoryValue = null;
		Firm firmValue = null;
		long startTime = ExtractDatetimeLong(createTuan_form_starttime_datetime);
		long endTime = ExtractDatetimeLong(createTuan_form_endtime_datetime);
		int keyCount = Integer.valueOf(createTuan_form_key_numbertext.getText());
		
		if(nameValue == null || nameValue.equals("") || nameValue.length() > 32){
			alert(getShell(), "创建错误", "团购名称不能为空，并且不能大于32个字符");
			return;
		}
		if(descValue == null || descValue.equals("") ){
			alert(getShell(), "创建错误", "团购描述不能为空");
			return;
		}
		if(startTime > endTime){
			alert(getShell(), "创建错误", "开始时间不能晚于结束时间");
			return;
		}
		if(createTuan_form_category_text.getData() == null){
			alert(getShell(), "创建错误", "请选择团购类别");
			return;
		}else{
			categoryValue = (Category) createTuan_form_category_text.getData();
		}
		if(createTuan_form_frim_text.getData() == null){
			alert(getShell(), "创建错误", "请选择发布商家");
			return;
		}else{
			firmValue = (Firm) createTuan_form_frim_text.getData();
		}
		
		Tuan tuan = new Tuan();
		tuan.setCategory_id(categoryValue.getCategory_id());
		tuan.setFirm_id(firmValue.getFirm_id());
		tuan.setTuan_desc(descValue);
		tuan.setTuan_endtime(endTime);
		tuan.setTuan_starttime(startTime);
		tuan.setTuan_name(nameValue);
		
		new createTuanAsyncHandler(This(), keyCount).start(tuan);
		
	}
	/**
	 * 创建团购的Asynchandler
	 * @author Administrator
	 *
	 */
	private class createTuanAsyncHandler extends AsyncHandler<Tuan, Integer, Boolean>{

		private int keycount;
		private Tuan tuan;
		
		public createTuanAsyncHandler(superWindow window, int keycount) {
			super(window);
			// TODO Auto-generated constructor stub
			this.keycount = keycount;
		}
		
		@Override
		public void onBefore() {
			// TODO Auto-generated method stub
			super.onBefore();
			
			createTuan_form_button_create.setText("正在创建...");
			createTuan_form_button_create.setEnabled(false);
			
		}
		
		@Override
		public Boolean doInBackground(Tuan... params) {
			// TODO Auto-generated method stub
			TuanDAO dao = new TuanDAO();
			tuan = params[0];
			
			return dao.addTuanAndKey(params[0], keycount, getCurUser().getUser_id());
		}
		@Override
		public void onComplete(Boolean result) {
			// TODO Auto-generated method stub
			super.onComplete(result);
			
			if(result){
				alert(getShell(), "创建成功", "创建团购成功！");
				getShell().dispose();
				if(listener != null){
					listener.onResult(tuan);
				}
			}else{
				alert(getShell(), "创建错误", "创建错误，请稍候再试！");
				if(!getShell().isDisposed()){
					createTuan_form_button_create.setText("确认创建");
					createTuan_form_button_create.setEnabled(true);
					
				}
			}
			
			
		}
	}
	
	/**
	 * 初始化界面基本属性
	 */
	private void initBase(){
		createTuan_shell = new Shell(getParent().getShell(), SWT.CLOSE | SWT.APPLICATION_MODAL);
		createTuan_shell.setText(createTuan_title);
		createTuan_shell.setSize(marginWidthValue * 40, marginHeightValue * 50);
		createTuan_shell.setLocation(getCenterX(createTuan_shell), getCenterY(createTuan_shell));
		
		
	}
	
	
	@Override
	protected Shell getShell() {
		// TODO Auto-generated method stub
		return createTuan_shell;
	}

}
