package cn.panshihao.pos.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;

import cn.panshihao.pos.handler.AsyncHandler;
import cn.panshihao.pos.handler.PrintHandler;

public class selectPrintWindow extends superWindow {

	private onResultListener<String> listener;
	
	public String selectPrint_title = "选择打印机";
	public Shell selectPrint_shell = null;
	
	public Label selectPrint_select_label = null;
	public List selectPrint_list = null;
	public Button selectPrint_button = null;
	
	
	
	public selectPrintWindow(superWindow parent, onResultListener<String> listener) {
		super(parent);
		// TODO Auto-generated constructor stub
		this.listener = listener;
	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub

		initBase();
		
		initList();
		
		initData();
		
		getShell().open();
		getShell().layout();
	}
	
	/**
	 * 加载数据
	 */
	private void initData(){
		new loadPrintServiceAsyncHandler(This()).start("");
	}
	/**
	 * 加载打印机列表的asynchandler
	 * @author Administrator
	 *
	 */
	private class loadPrintServiceAsyncHandler extends AsyncHandler<String, Integer, java.util.List<String>>{

		public loadPrintServiceAsyncHandler(superWindow window) {
			super(window);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onBefore() {
			// TODO Auto-generated method stub
			super.onBefore();
				
			selectPrint_button.setEnabled(false);
			
		}
		
		@Override
		public java.util.List<String> doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			return PrintHandler.getAllPrintServicesName();
		}
		@Override
		public void onComplete(java.util.List<String> result) {
			// TODO Auto-generated method stub
			super.onComplete(result);
			
			if(result != null){
				selectPrint_button.setEnabled(true);
				String[] names = new String[result.size()];
				
				for(int i = 0 ; i < result.size(); i ++){
					names[i] = result.get(i);
				}
				selectPrint_list.setItems(names);
				
			}else{
				alert(getShell(), "失败", "加载打印机列表失败！");
				getShell().dispose();
			}
		}
		
	}
	
	/**
	 * 初始化打印机列表
	 */
	private void initList(){
		
		selectPrint_select_label = new Label(getShell(), SWT.NONE);
		selectPrint_select_label.setText("请选择打印机");
		selectPrint_select_label.setBounds(marginWidthValue, marginHeightValue, marginWidthValue * 7, marginHeightValue * 3);
		
		selectPrint_list = new List(getShell(), SWT.BORDER | SWT.V_SCROLL);
		selectPrint_list.setBounds(selectPrint_select_label.getBounds().x, selectPrint_select_label.getBounds().y + selectPrint_select_label.getBounds().height + marginHeightValue, getShellWidth() - marginWidthValue * 2, marginHeightValue * 15);
		
		selectPrint_button = new Button(getShell(), SWT.NONE);
		selectPrint_button.setText("确认选择");
		selectPrint_button.setBounds(getShellWidth() / 2 - marginWidthValue * 3, selectPrint_list.getBounds().y + selectPrint_list.getBounds().height + marginHeightValue * 2, marginWidthValue * 6, marginHeightValue * 3);
		selectPrint_button.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				ValidatePrint();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	/**
	 * 验证选择
	 */
	private void ValidatePrint(){
		if(selectPrint_list.getSelectionCount() == 0){
			alert(getShell(), "错误", "请选择打印机！");
			return;
		}
		
		getShell().dispose();
	}
	
	
	/**
	 * 初始化界面基本属性
	 */
	private void initBase(){
		
		selectPrint_shell = new Shell(getParent().getShell(), SWT.CLOSE | SWT.APPLICATION_MODAL);
		selectPrint_shell.setText(selectPrint_title);
		selectPrint_shell.setSize(marginWidthValue * 20, marginHeightValue * 30);
		selectPrint_shell.setLocation(getCenterX(selectPrint_shell), getCenterY(selectPrint_shell));
		
		selectPrint_shell.addDisposeListener(new DisposeListener() {
			
			@Override
			public void widgetDisposed(DisposeEvent arg0) {
				// TODO Auto-generated method stub
				if(listener != null){
					listener.onResult(selectPrint_list.getSelection()[0]);
				}
			}
		});
	}
	

	@Override
	protected Shell getShell() {
		// TODO Auto-generated method stub
		return selectPrint_shell;
	}

}
