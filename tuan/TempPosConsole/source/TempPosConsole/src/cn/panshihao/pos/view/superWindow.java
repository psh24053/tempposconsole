package cn.panshihao.pos.view;

import org.eclipse.swt.widgets.Display;

public abstract class superWindow {

	private Display display;
	private superWindow parent;
	private boolean root;
	
	public superWindow(superWindow parent){
		this.parent = parent;
		this.display = parent.getDisplay();
		this.root = false;
	}
	
	public superWindow(Display display){
		this.parent = null;
		this.display = display;
		this.root = true;
	}
	
	/**
	 * 初始化方法
	 */
	public abstract void init();
	
	/**
	 * 获取Display
	 * @return
	 */
	public Display getDisplay() {
		return display;
	}
	/**
	 * 获取父窗口
	 * @return
	 */
	public superWindow getParent() {
		return parent;
	}
	/**
	 * 判断当前窗口是否是主窗口
	 * @return
	 */
	public boolean isRoot() {
		return root;
	}
	/**
	 * 判断当前窗口是否被关闭
	 * @return
	 */
	public abstract boolean isDisposed();
	
	
	
	
	
}
