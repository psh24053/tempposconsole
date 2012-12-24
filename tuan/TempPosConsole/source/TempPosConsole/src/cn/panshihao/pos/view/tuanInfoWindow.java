package cn.panshihao.pos.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;

import cn.panshihao.pos.model.Tuan;

public class tuanInfoWindow extends superWindow {

	private Tuan tuan;
	
	public String tuanInfo_title = "团购详情";
	public Shell tuanInfo_shell = null;
	
	public tuanInfoWindow(superWindow parent, Tuan tuan) {
		super(parent);
		// TODO Auto-generated constructor stub
		this.tuan = tuan;
	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub
		initBase();
		
		initTuanInfo();
		
		tuanInfo_shell.open();
		tuanInfo_shell.layout();
		
	}
	/**
	 * 初始化团购信息
	 */
	private void initTuanInfo(){
		
	}
	/**
	 * 初始化界面基本数据
	 */
	private void initBase(){
		tuanInfo_shell = new Shell(getParent().getShell(), SWT.CLOSE | SWT.APPLICATION_MODAL);
		tuanInfo_shell.setText(tuanInfo_title);
		tuanInfo_shell.setSize(marginWidthValue * 40, marginHeightValue * 50);
		tuanInfo_shell.setLocation(getCenterX(tuanInfo_shell), getCenterY(tuanInfo_shell));
		
		
	}

	@Override
	protected Shell getShell() {
		// TODO Auto-generated method stub
		return tuanInfo_shell;
	}

}
