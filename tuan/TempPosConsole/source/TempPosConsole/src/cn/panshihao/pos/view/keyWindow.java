package cn.panshihao.pos.view;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.json.JSONException;
import org.json.JSONObject;

import cn.panshihao.pos.dao.KeyDAO;
import cn.panshihao.pos.dao.TuanDAO;
import cn.panshihao.pos.handler.AsyncHandler;
import cn.panshihao.pos.model.Key;
import cn.panshihao.pos.model.Tuan;

public class keyWindow extends superWindow {

	public String key_title = "增加兑换码";
	
	public Shell key_shell = null;
	
	public onResultListener<Key> listener;
	
	private int tuan_id;
	
	
	public Label key_tuanname_label = null;
	public Label key_tuanname_valueLabel = null;
	
	public Label key_tuandesc_label = null;
	public Label key_tuandesc_valueLabel = null;
	
	public Label key_count_label = null;
	public Label key_count_valueLabel = null;
	
	public Label key_remain_label = null;
	public Label key_remain_valueLabel = null;
	
	public Label key_addkey_label = null;
	public Text  key_addkey_text = null;
	public Button key_addkey_button = null;
	
	
	public keyWindow(superWindow parent, onResultListener<Key> listener, int tuan_id) {
		super(parent);
		// TODO Auto-generated constructor stub
		this.listener = listener;
		this.tuan_id = tuan_id;
	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub

		initBase();
		
		initForm();
		
		initData();
		
		key_shell.open();
		key_shell.layout();
	}
	/**
	 * 初始化数据
	 */
	private void initData(){
		new loadTuanAsyncHandler(This()).start(tuan_id);
	}
	/**
	 * 加载团购信息的asynchandler
	 * @author shihao
	 *
	 */
	private class loadTuanAsyncHandler extends AsyncHandler<Integer, Integer, Tuan>{

		private int count;
		private int remain;
		
		public loadTuanAsyncHandler(superWindow window) {
			super(window);
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public void onBefore() {
			// TODO Auto-generated method stub
			super.onBefore();
			
			key_addkey_button.setEnabled(false);
			
		}
		
		@Override
		public Tuan doInBackground(Integer... params) {
			// TODO Auto-generated method stub
			TuanDAO dao = new TuanDAO();
			JSONObject json = dao.getTuanKeyCodeCount(params[0]);
			
			if(json != null){
				try {
					count = json.getInt("count");
					remain = json.getInt("remain");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			
			return dao.getTuanFromDatabase(params[0]);
		}
		@Override
		public void onComplete(Tuan result) {
			// TODO Auto-generated method stub
			super.onComplete(result);
			
			if(result != null){
				
				changeForm(result, count, remain);
				key_addkey_button.setEnabled(true);
			}else{
				alert(getShell(), "失败", "记载团购信息失败！");
				getShell().dispose();
			}
			
		}
		
	}
	/**
	 * 根据传入的tuan对象来改变界面
	 * @param tuan
	 */
	private void changeForm(Tuan tuan, int count, int remain){
		
		key_tuanname_valueLabel.setText(tuan.getTuan_id()+"");
		key_tuandesc_valueLabel.setText(tuan.getTuan_name());
		key_count_valueLabel.setText(count+"");
		key_remain_valueLabel.setText(remain+"");
	}
	/**
	 * 初始化表单组件
	 */
	private void initForm(){
		key_tuanname_label = new Label(getShell(), SWT.NONE);
		key_tuanname_label.setText("团购ID");
		key_tuanname_label.setBounds(marginWidthValue, marginHeightValue, marginWidthValue * 5, marginHeightValue * 2);
		
		key_tuanname_valueLabel = new Label(getShell(), SWT.NONE);
		key_tuanname_valueLabel.setText("0");
		key_tuanname_valueLabel.setBounds(key_tuanname_label.getBounds().x + key_tuanname_label.getBounds().width + marginWidthValue , key_tuanname_label.getBounds().y, marginWidthValue * 3, marginHeightValue * 2);
		
		key_tuandesc_label = new Label(getShell(), SWT.NONE);
		key_tuandesc_label.setText("团购名称");
		key_tuandesc_label.setBounds(key_tuanname_valueLabel.getBounds().x + key_tuanname_valueLabel.getBounds().width + marginWidthValue , key_tuanname_valueLabel.getBounds().y, marginWidthValue * 4, marginHeightValue * 2);
		
		key_tuandesc_valueLabel = new Label(getShell(), SWT.NONE);
		key_tuandesc_valueLabel.setText("");
		key_tuandesc_valueLabel.setBounds(key_tuandesc_label.getBounds().x + key_tuandesc_label.getBounds().width + marginWidthValue  , key_tuanname_label.getBounds().y, marginWidthValue * 15, marginHeightValue * 2);
		
		
		key_count_label = new Label(getShell(), SWT.NONE);
		key_count_label.setText("兑换码数量");
		key_count_label.setBounds(key_tuanname_label.getBounds().x, key_tuanname_label.getBounds().y + key_tuanname_label.getBounds().height + marginHeightValue, marginWidthValue * 5, marginHeightValue * 2);
		
		key_count_valueLabel = new Label(getShell(), SWT.NONE);
		key_count_valueLabel.setText("0");
		key_count_valueLabel.setBounds(key_count_label.getBounds().x + key_count_label.getBounds().width + marginWidthValue , key_count_label.getBounds().y, marginWidthValue * 3, marginHeightValue * 2);
		
		key_remain_label = new Label(getShell(), SWT.NONE);
		key_remain_label.setText("剩余数量");
		key_remain_label.setBounds(key_count_valueLabel.getBounds().x + key_count_valueLabel.getBounds().width + marginWidthValue , key_count_valueLabel.getBounds().y, marginWidthValue * 4, marginHeightValue * 2);
		
		key_remain_valueLabel = new Label(getShell(), SWT.NONE);
		key_remain_valueLabel.setText("0");
		key_remain_valueLabel.setBounds(key_remain_label.getBounds().x + key_remain_label.getBounds().width + marginWidthValue  , key_remain_label.getBounds().y, marginWidthValue * 15, marginHeightValue * 2);
		
		key_addkey_label = new Label(getShell(), SWT.NONE);
		key_addkey_label.setText("输入增加数量");
		key_addkey_label.setBounds(key_count_label.getBounds().x, key_count_label.getBounds().y + key_count_label.getBounds().height + marginHeightValue * 3, marginWidthValue * 6, marginHeightValue * 2);
		
		key_addkey_text = new Text(getShell(), SWT.BORDER);
		key_addkey_text.setTextLimit(4);
		key_addkey_text.setText("0");
		key_addkey_text.setBounds(key_addkey_label.getBounds().x + key_addkey_label.getBounds().width + marginWidthValue , key_addkey_label.getBounds().y - key_addkey_text.getBorderWidth(), marginWidthValue * 10, marginHeightValue * 3);
		key_addkey_text.addVerifyListener(new VerifyListener() {

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
		
		key_addkey_button = new Button(getShell(), SWT.NONE);
		key_addkey_button.setText("增加兑换码");
		key_addkey_button.setBounds(marginWidthValue * 10, key_addkey_label.getBounds().y +key_addkey_label.getBounds().height + marginHeightValue * 5, marginWidthValue * 7, marginHeightValue * 5);
		key_addkey_button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				validateAddKey();
			}
		});
	}
	/**
	 * 验证增加兑换码
	 */
	private void validateAddKey(){
		int count = Integer.valueOf(key_addkey_text.getText());
		
		new addKeyAsyncHandler(This()).start(count);
		
		
	}
	/**
	 * 增加兑换码的asynchandler
	 * @author shihao
	 *
	 */
	private class addKeyAsyncHandler extends AsyncHandler<Integer, Integer, Boolean>{

		public addKeyAsyncHandler(superWindow window) {
			super(window);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onBefore() {
			// TODO Auto-generated method stub
			super.onBefore();
			alert(getShell(), "提示", "生成兑换码的时间较长，请耐心等待！");
			key_addkey_button.setEnabled(false);
			key_addkey_button.setText("正在生成...");
			
		}
		
		@Override
		public Boolean doInBackground(Integer... params) {
			// TODO Auto-generated method stub
			KeyDAO dao = new KeyDAO();
			
			return dao.addAfterKey(tuan_id, params[0], getCurUser().getUser_id());
		}
		
		@Override
		public void onComplete(Boolean result) {
			// TODO Auto-generated method stub
			super.onComplete(result);
			
			if(result != null && result){
				alert(getShell(), "成功", "增加兑换码成功！");
				getShell().dispose();
				if(listener != null){
					listener.onResult(null);
				}
			}else{
				alert(getShell(), "失败", "增加失败，请稍后再试！");
				if(!getShell().isDisposed()){
					key_addkey_button.setEnabled(true);
					key_addkey_button.setText("增加兑换码");
				}
			}
			
		}
		
	}
	
	/**
	 * 初始化界面基础数据
	 */
	private void initBase(){
		key_shell = new Shell(getParent().getShell(), SWT.CLOSE | SWT.APPLICATION_MODAL);
		key_shell.setText(key_title);
		key_shell.setSize(marginWidthValue * 28, marginHeightValue * 27);
		key_shell.setLocation(getCenterX(key_shell), getCenterY(key_shell));
		
	}

	@Override
	protected Shell getShell() {
		// TODO Auto-generated method stub
		return key_shell;
	}

}
