package cn.panshihao.pos.view;

import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.json.JSONException;
import org.json.JSONObject;

import cn.panshihao.pos.dao.CategoryDAO;
import cn.panshihao.pos.dao.FirmDAO;
import cn.panshihao.pos.dao.KeyDAO;
import cn.panshihao.pos.dao.TuanDAO;
import cn.panshihao.pos.handler.AsyncHandler;
import cn.panshihao.pos.model.Category;
import cn.panshihao.pos.model.Firm;
import cn.panshihao.pos.model.Tuan;
import cn.panshihao.pos.view.superWindow.onResultListener;

public class modifyTuanWindow extends superWindow {

	
	private Tuan tuan;
	
	public String modifyTuan_title = "编辑团购";
	public Shell modifyTuan_shell;
	
	/*
	 * 表单
	 */
	public Label modifyTuan_form_name_label = null;
	public Label modifyTuan_form_desc_label = null;
	public Label modifyTuan_form_frim_label = null;
	public Label modifyTuan_form_category_label = null;
	public Label modifyTuan_form_starttime_label = null;
	public Label modifyTuan_form_endtime_label = null;
	public Label modifyTuan_form_key_label = null;
	
	public Text modifyTuan_form_name_text = null;
	public Text modifyTuan_form_desc_text = null;
	public DateTime modifyTuan_form_starttime_datetime = null;
	public DateTime modifyTuan_form_endtime_datetime = null;
	public Text modifyTuan_form_key_numbertext = null;
	public Label modifyTuan_form_key_numberLabel = null;
	
	public Label modifyTuan_form_key_overLabel = null;
	public Label modifyTuan_form_key_overValueLabel = null;
	
	public Text modifyTuan_form_frim_text = null;
	public Button modifyTuan_form_frim_button = null;
	
	public Text modifyTuan_form_category_text = null;
	public Button modifyTuan_form_category_button = null;
	
	public Button modifyTuan_form_button_create = null;
	
	public onResultListener<Tuan> listener = null;
	
	
	public modifyTuanWindow(superWindow parent, Tuan tuan, onResultListener<Tuan> listener) {
		super(parent);
		// TODO Auto-generated constructor stub
		this.tuan = tuan;
		this.listener = listener;
	}
	
	@Override
	protected void init() {
		// TODO Auto-generated method stub
		initBase();
		
		initForm();
		
		initData();
		
		modifyTuan_shell.open();
		modifyTuan_shell.layout();
		
	}
	/**
	 * 初始化数据
	 */
	private void initData(){
		
		new loadTuanInfoAsyncHandler(This()).start(tuan);
		
	}
	/**
	 * 加载团购详细信息
	 * @author shihao
	 *
	 */
	private class loadTuanInfoAsyncHandler extends AsyncHandler<Tuan, Integer, Tuan>{

		
		private Tuan tuan;
		private Firm firm;
		private Category category;
		private int count;
		private int remain;
		
		public loadTuanInfoAsyncHandler(superWindow window) {
			super(window);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onBefore() {
			// TODO Auto-generated method stub
			super.onBefore();
			
			modifyTuan_form_button_create.setEnabled(false);
			
		}
		
		@Override
		public Tuan doInBackground(Tuan... params) {
			// TODO Auto-generated method stub
			
			TuanDAO tuandao = new TuanDAO();
			FirmDAO firmdao = new FirmDAO();
			CategoryDAO categorydao = new CategoryDAO();
			tuan = tuandao.getTuanFromDatabase(params[0].getTuan_id());
			firm = firmdao.getFirmFromDatabase(tuan.getFirm_id());
			category = categorydao.getCategoryFromDatabase(tuan.getCategory_id());
			
			JSONObject keycount = tuandao.getTuanKeyCodeCount(tuan.getTuan_id());
			if(keycount != null){
				try {
					count = keycount.getInt("count");
					remain = keycount.getInt("remain");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
			
			return tuan;
		}
		
		@Override
		public void onComplete(Tuan result) {
			// TODO Auto-generated method stub
			super.onComplete(result);
			
			if(result != null){
				loadModifyData(tuan, firm, category, count, remain);
				modifyTuan_form_button_create.setEnabled(true);
				
			}else{
				alert(getShell(), "加载失败", "加载团购信息失败");
				getShell().dispose();
			}
			
		}
		
		
	}
	/**
	 * 加载编辑数据
	 * @param tuan
	 * @param firm
	 * @param category
	 * @param count
	 */
	private void loadModifyData(Tuan tuan, Firm firm, Category category, int count, int remain){
		
		modifyTuan_form_name_text.setText(tuan.getTuan_name());
		modifyTuan_form_desc_text.setText(tuan.getTuan_desc());
		
		modifyTuan_form_category_text.setText(category.getCategory_name());
		modifyTuan_form_category_text.setData(category);
		
		modifyTuan_form_frim_text.setData(firm);
		modifyTuan_form_frim_text.setText(firm.getFirm_name());
		
		Calendar startc = Calendar.getInstance();
		startc.setTimeInMillis(tuan.getTuan_starttime());
		
		modifyTuan_form_starttime_datetime.setYear(startc.get(Calendar.YEAR));
		modifyTuan_form_starttime_datetime.setMonth(startc.get(Calendar.MONTH));
		modifyTuan_form_starttime_datetime.setDay(startc.get(Calendar.DAY_OF_MONTH));
		
		Calendar endc = Calendar.getInstance();
		endc.setTimeInMillis(tuan.getTuan_endtime());
		modifyTuan_form_endtime_datetime.setYear(endc.get(Calendar.YEAR));
		modifyTuan_form_endtime_datetime.setMonth(endc.get(Calendar.MONTH));
		modifyTuan_form_endtime_datetime.setDay(endc.get(Calendar.DAY_OF_MONTH));
		
		
		modifyTuan_form_key_numberLabel.setText(count+"");
		modifyTuan_form_key_overValueLabel.setText(remain+"");
		
		
	}
	
	/**
	 * 初始化编辑表单
	 */
	private void initForm(){
		
		modifyTuan_form_name_label = new Label(getShell(), SWT.NONE);
		modifyTuan_form_name_label.setText("团购名称");
		modifyTuan_form_name_label.setBounds(marginWidthValue, marginHeightValue * 3, marginWidthValue * 5, marginHeightValue * 3);
		
		modifyTuan_form_name_text = new Text(getShell(), SWT.BORDER);
		modifyTuan_form_name_text.setBounds(modifyTuan_form_name_label.getBounds().x + modifyTuan_form_name_label.getBounds().width, modifyTuan_form_name_label.getBounds().y - (int)(marginHeightValue * 0.5), marginWidthValue * 15, marginHeightValue * 3);
		
		modifyTuan_form_desc_label = new Label(getShell(), SWT.NONE);
		modifyTuan_form_desc_label.setText("团购描述");
		modifyTuan_form_desc_label.setBounds(modifyTuan_form_name_label.getBounds().x, modifyTuan_form_name_label.getBounds().y + modifyTuan_form_name_label.getBounds().height + marginHeightValue * 2, marginWidthValue * 5, marginHeightValue * 3);
		
		modifyTuan_form_desc_text = new Text(getShell(), SWT.BORDER | SWT.WRAP);
		modifyTuan_form_desc_text.setBounds(modifyTuan_form_desc_label.getBounds().x + modifyTuan_form_desc_label.getBounds().width, modifyTuan_form_desc_label.getBounds().y - (int)(marginHeightValue * 0.5), marginWidthValue * 15, marginHeightValue * 13);
		
		modifyTuan_form_category_label = new Label(getShell(), SWT.NONE);
		modifyTuan_form_category_label.setText("团购类别");
		modifyTuan_form_category_label.setBounds(modifyTuan_form_desc_label.getBounds().x, modifyTuan_form_desc_text.getBounds().y + modifyTuan_form_desc_text.getBounds().height + marginHeightValue * 2, marginWidthValue * 5, marginHeightValue * 3);
		
		modifyTuan_form_category_button = new Button(getShell(), SWT.NONE);
		modifyTuan_form_category_button.setText("选择类别");
		modifyTuan_form_category_button.setBounds(modifyTuan_form_category_label.getBounds().x + modifyTuan_form_category_label.getBounds().width + marginWidthValue * 15 - marginWidthValue * 5, modifyTuan_form_category_label.getBounds().y - (int)(marginHeightValue * 0.3), marginWidthValue * 5, (int)(marginHeightValue * 2.5));
		modifyTuan_form_category_button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				new categoryWindow(This(), new onResultListener<Category>() {
					
					@Override
					public void onResult(Category result) {
						// TODO Auto-generated method stub
						modifyTuan_form_category_text.setData(result);
						modifyTuan_form_category_text.setText(result.getCategory_name());
					}
				}).show();
				
			}
		});
		
		
		modifyTuan_form_category_text = new Text(getShell(), SWT.READ_ONLY | SWT.BORDER);
		modifyTuan_form_category_text.setBounds(modifyTuan_form_category_label.getBounds().x + modifyTuan_form_category_label.getBounds().width, modifyTuan_form_category_label.getBounds().y - (int)(marginHeightValue * 0.5), marginWidthValue * 15, marginHeightValue * 3);
		
		
		modifyTuan_form_frim_label = new Label(getShell(), SWT.NONE);
		modifyTuan_form_frim_label.setText("发布商家");
		modifyTuan_form_frim_label.setBounds(modifyTuan_form_category_label.getBounds().x, modifyTuan_form_category_label.getBounds().y + modifyTuan_form_category_label.getBounds().height + marginHeightValue * 2, marginWidthValue * 5, marginHeightValue * 3);
		
		modifyTuan_form_frim_button = new Button(getShell(), SWT.NONE);
		modifyTuan_form_frim_button.setText("选择商家");
		modifyTuan_form_frim_button.setBounds(modifyTuan_form_frim_label.getBounds().x + modifyTuan_form_frim_label.getBounds().width + marginWidthValue * 15 - marginWidthValue * 5, modifyTuan_form_frim_label.getBounds().y - (int)(marginHeightValue * 0.3), marginWidthValue * 5, (int)(marginHeightValue * 2.5));
		modifyTuan_form_frim_button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				new firmWindow(This(), new onResultListener<Firm>() {
					
					@Override
					public void onResult(Firm result) {
						// TODO Auto-generated method stub
						modifyTuan_form_frim_text.setData(result);
						modifyTuan_form_frim_text.setText(result.getFirm_name());
					}
					
				}).show();
				
			}
		});
		
		
		modifyTuan_form_frim_text = new Text(getShell(), SWT.READ_ONLY | SWT.BORDER);
		modifyTuan_form_frim_text.setBounds(modifyTuan_form_frim_label.getBounds().x + modifyTuan_form_frim_label.getBounds().width, modifyTuan_form_frim_label.getBounds().y - (int)(marginHeightValue * 0.5), marginWidthValue * 15, marginHeightValue * 3);
		
		
		
		modifyTuan_form_starttime_label = new Label(getShell(), SWT.NONE);
		modifyTuan_form_starttime_label.setText("开始时间");
		modifyTuan_form_starttime_label.setBounds(modifyTuan_form_frim_label.getBounds().x, modifyTuan_form_frim_label.getBounds().y + modifyTuan_form_frim_label.getBounds().height + marginHeightValue * 2, marginWidthValue * 5, marginHeightValue * 3);
		
		modifyTuan_form_starttime_datetime = new DateTime(getShell(), SWT.BORDER | SWT.DROP_DOWN | SWT.LONG);
		modifyTuan_form_starttime_datetime.setBounds(modifyTuan_form_starttime_label.getBounds().x + modifyTuan_form_starttime_label.getBounds().width, modifyTuan_form_starttime_label.getBounds().y - (int)(marginHeightValue * 0.5), marginWidthValue * 15, marginHeightValue * 3);
		
		modifyTuan_form_endtime_label = new Label(getShell(), SWT.NONE);
		modifyTuan_form_endtime_label.setText("结束时间");
		modifyTuan_form_endtime_label.setBounds(modifyTuan_form_starttime_label.getBounds().x, modifyTuan_form_starttime_label.getBounds().y + modifyTuan_form_starttime_label.getBounds().height + marginHeightValue * 2, marginWidthValue * 5, marginHeightValue * 3);
		
		modifyTuan_form_endtime_datetime = new DateTime(getShell(), SWT.BORDER | SWT.DROP_DOWN | SWT.LONG);
		modifyTuan_form_endtime_datetime.setBounds(modifyTuan_form_endtime_label.getBounds().x + modifyTuan_form_endtime_label.getBounds().width, modifyTuan_form_endtime_label.getBounds().y - (int)(marginHeightValue * 0.5), marginWidthValue * 15, marginHeightValue * 3);
		
		modifyTuan_form_key_label = new Label(getShell(), SWT.NONE);
		modifyTuan_form_key_label.setText("兑换码数量");
		modifyTuan_form_key_label.setBounds(modifyTuan_form_endtime_label.getBounds().x, modifyTuan_form_endtime_label.getBounds().y + modifyTuan_form_endtime_label.getBounds().height + marginHeightValue * 2, marginWidthValue * 5, marginHeightValue * 3);
		
		modifyTuan_form_key_numberLabel = new Label(getShell(), SWT.NONE);
		modifyTuan_form_key_numberLabel.setText("0");
		modifyTuan_form_key_numberLabel.setBounds(modifyTuan_form_key_label.getBounds().x + modifyTuan_form_key_label.getBounds().width + marginWidthValue, modifyTuan_form_key_label.getBounds().y , marginWidthValue * 5, marginHeightValue * 3);
		
		
		modifyTuan_form_key_overLabel = new Label(getShell(), SWT.NONE);
		modifyTuan_form_key_overLabel.setText("剩余数量");
		modifyTuan_form_key_overLabel.setBounds(modifyTuan_form_key_numberLabel.getBounds().x + modifyTuan_form_key_numberLabel.getBounds().width + marginWidthValue , modifyTuan_form_key_label.getBounds().y , marginWidthValue * 5, marginHeightValue * 3);
		
		modifyTuan_form_key_overValueLabel = new Label(getShell(), SWT.NONE);
		modifyTuan_form_key_overValueLabel.setText("0");
		modifyTuan_form_key_overValueLabel.setBounds(modifyTuan_form_key_overLabel.getBounds().x + modifyTuan_form_key_overLabel.getBounds().width + marginWidthValue , modifyTuan_form_key_label.getBounds().y , marginWidthValue * 5, marginHeightValue * 3);
		
		modifyTuan_form_button_create = new Button(modifyTuan_shell, SWT.NONE);
		modifyTuan_form_button_create.setText("确认编辑");
		modifyTuan_form_button_create.setBounds(marginWidthValue * 25, marginHeightValue * 3, marginWidthValue * 8, marginHeightValue * 4);
		modifyTuan_form_button_create.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				validateModify();
			}
		});
	}
	/**
	 * 验证编辑
	 */
	private void validateModify(){
		String nameValue = modifyTuan_form_name_text.getText();
		String descValue = modifyTuan_form_desc_text.getText();
		Category categoryValue = null;
		Firm firmValue = null;
		long startTime = ExtractDatetimeLong(modifyTuan_form_starttime_datetime);
		long endTime = ExtractDatetimeLong(modifyTuan_form_endtime_datetime);
		
		if(nameValue == null || nameValue.equals("") || nameValue.length() > 32){
			alert(getShell(), "编辑错误", "团购名称不能为空，并且不能大于32个字符");
			return;
		}
		if(descValue == null || descValue.equals("") ){
			alert(getShell(), "编辑错误", "团购描述不能为空");
			return;
		}
		if(startTime > endTime){
			alert(getShell(), "编辑错误", "开始时间不能晚于结束时间");
			return;
		}
		if(modifyTuan_form_category_text.getData() == null){
			alert(getShell(), "编辑错误", "请选择团购类别");
			return;
		}else{
			categoryValue = (Category) modifyTuan_form_category_text.getData();
		}
		if(modifyTuan_form_frim_text.getData() == null){
			alert(getShell(), "编辑错误", "请选择发布商家");
			return;
		}else{
			firmValue = (Firm) modifyTuan_form_frim_text.getData();
		}
		
		Tuan tuan = new Tuan();
		tuan.setCategory_id(categoryValue.getCategory_id());
		tuan.setFirm_id(firmValue.getFirm_id());
		tuan.setTuan_desc(descValue);
		tuan.setTuan_endtime(endTime);
		tuan.setTuan_starttime(startTime);
		tuan.setTuan_name(nameValue);
		tuan.setTuan_id(this.tuan.getTuan_id());
		
		new modifyTuanAsyncHandler(This(), 0).start(tuan);
		
	}
	/**
	 * 编辑团购的Asynchandler
	 * @author Administrator
	 *
	 */
	private class modifyTuanAsyncHandler extends AsyncHandler<Tuan, Integer, Boolean>{

		private int keycount;
		private Tuan tuan;
		
		public modifyTuanAsyncHandler(superWindow window, int keycount) {
			super(window);
			// TODO Auto-generated constructor stub
			this.keycount = keycount;
		}
		
		@Override
		public void onBefore() {
			// TODO Auto-generated method stub
			super.onBefore();
			
			modifyTuan_form_button_create.setText("正在编辑...");
			modifyTuan_form_button_create.setEnabled(false);
			
		}
		
		@Override
		public Boolean doInBackground(Tuan... params) {
			// TODO Auto-generated method stub
			TuanDAO dao = new TuanDAO();
			tuan = params[0];
			
			return dao.updateTuan(tuan, getCurUser().getUser_id());
		}
		@Override
		public void onComplete(Boolean result) {
			// TODO Auto-generated method stub
			super.onComplete(result);
			
			if(result){
				alert(getShell(), "编辑成功", "编辑团购成功！");
				getShell().dispose();
				if(listener != null){
					listener.onResult(tuan);
				}
			}else{
				alert(getShell(), "编辑错误", "编辑错误，请稍候再试！");
				if(!getShell().isDisposed()){
					modifyTuan_form_button_create.setText("确认编辑");
					modifyTuan_form_button_create.setEnabled(true);
					
				}
			}
			
			
		}
	}
	
	/**
	 * 初始化界面基本属性
	 */
	private void initBase(){
		modifyTuan_shell = new Shell(getParent().getShell(), SWT.CLOSE | SWT.APPLICATION_MODAL);
		modifyTuan_shell.setText(modifyTuan_title);
		modifyTuan_shell.setSize(marginWidthValue * 40, marginHeightValue * 50);
		modifyTuan_shell.setLocation(getCenterX(modifyTuan_shell), getCenterY(modifyTuan_shell));
		
		
	}
	
	
	@Override
	protected Shell getShell() {
		// TODO Auto-generated method stub
		return modifyTuan_shell;
	}

}
