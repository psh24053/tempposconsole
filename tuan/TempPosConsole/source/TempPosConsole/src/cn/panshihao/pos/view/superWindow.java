package cn.panshihao.pos.view;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import cn.panshihao.pos.handler.CacheHandler;

public abstract class superWindow {

	private Display display;
	private superWindow parent;
	private boolean root;
	public int marginWidthValue;
	public int marginHeightValue;
	public Rectangle superRectangle;
	public static Map<String, Object> cacheMap = new HashMap<String, Object>();
	public static CacheHandler cacheHandler = CacheHandler.getInstance();
	
	public superWindow(superWindow parent){
		this.parent = parent;
		this.display = parent.getDisplay();
		this.root = false;
		loadValue();
		
	}
	
	public superWindow(Display display){
		this.parent = null;
		this.display = display;
		if(this.display == null){
			this.display = Display.getDefault();
		}
		this.root = true;
		loadValue();
	}
	/**
	 * 根据shell，计算该shell如果出现在屏幕中央，那么需要的X坐标
	 * @param shell
	 * @return
	 */
	public int getCenterX(Shell shell){
		if(shell == null){
			return -1;
		}
		Rectangle shellBounds = shell.getBounds();
		int x = superRectangle.x + (superRectangle.width - shellBounds.width)>>1;
		return x;
	}
	/**
	 * 根据shell，计算该shell如果出现在屏幕中央，那么需要的Y坐标
	 * @param shell
	 * @return
	 */
	public int getCenterY(Shell shell){
		if(shell == null){
			return -1;
		}
		Rectangle shellBounds = shell.getBounds();
		int y = superRectangle.y + (superRectangle.height - shellBounds.height)>>1;
		return y;
	}
	
	/**
	 * 载入各种值
	 */
	private void loadValue(){
		superRectangle = display.getPrimaryMonitor().getBounds();
		
		
		marginWidthValue = superRectangle.width / 100;
		marginHeightValue = superRectangle.height / 100;
		
		
	}
	/**
	 * 打开alert对话框
	 * @param shell
	 * @param title
	 * @param msg
	 */
	public void alert(Shell shell, String title, String msg){
		if(shell == null || shell.isDisposed()){
			return;
		}
		MessageBox box = new MessageBox(shell);
		box.setText(title);
		box.setMessage(msg);
		box.open();
	}
	/**
	 * 初始化方法
	 */
	protected abstract void init();
	protected abstract Shell getShell();
	/**
	 * 显示窗口
	 */
	public void show(){
		init();
	}
	
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
	public boolean isDisposed(){
		return getShell().isDisposed();
	}
	
	public superWindow This(){
		return this;
	}
	
	public int getShellWidth(){
		// 减去左右边框区域
		return getShell().getBounds().width - getShell().getBorderWidth();
	}
	
	public int getShellHeight(){
		// 减去Shell的头部和底部区域
		return getShell().getBounds().height - getShell().getBorderWidth() - marginHeightValue * 3;
	}
	
}
