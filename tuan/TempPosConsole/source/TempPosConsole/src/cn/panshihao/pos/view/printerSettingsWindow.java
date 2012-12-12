package cn.panshihao.pos.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.DocAttributeSet;
import javax.print.attribute.HashDocAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.printing.Printer;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

public class printerSettingsWindow extends superWindow {

	public String printerSettings_title = "打印设置";
	
	public Shell printerSettings_shell;
	
	public Label printerSettings_label;
	
	public printerSettingsWindow(superWindow parent){
		super(parent);
	}
	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		
		initbase();
	
		try {
			initPrinter();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PrintException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 初始化窗口基本数据
	 */
	private void initbase(){
		
		Rectangle displayBounds = getDisplay().getPrimaryMonitor().getBounds();
		
		printerSettings_shell = new Shell(getDisplay(), SWT.MIN | SWT.CLOSE);
		printerSettings_shell.setText(printerSettings_title);
		printerSettings_shell.setSize(displayBounds.width / 3, displayBounds.height / 2);
		
		Rectangle shellBounds = printerSettings_shell.getBounds();
		int x = displayBounds.x + (displayBounds.width - shellBounds.width)>>1;
		int y = displayBounds.y + (displayBounds.height - shellBounds.height)>>1;
		printerSettings_shell.setLocation(x, y);
		
		
		printerSettings_shell.open();
		printerSettings_shell.layout();
		
	}
	/**
	 * 初始化打印机
	 * @throws PrintException 
	 * @throws FileNotFoundException 
	 */
	private void initPrinter() throws PrintException, FileNotFoundException{
		
//		MessageBox box = new MessageBox(printerSettings_shell, SWT.OK|SWT.ICON_INFORMATION);
//		box.setText("box");
//		box.setMessage("!!!asd");
//		box.open();
		
		printerSettings_label = new Label(printerSettings_shell, SWT.NONE);
		printerSettings_label.setBounds(0, 0, printerSettings_shell.getBounds().width, printerSettings_shell.getBounds().height);
		
		
		
		//ReadData();
		// 通俗理解就是书、文档
		Book book = new Book();
		// 设置成竖打
		PageFormat pf = new PageFormat();
		pf.setOrientation(PageFormat.PORTRAIT); // LANDSCAPE表示竖打;PORTRAIT表示横打;REVERSE_LANDSCAPE表示打印空白
		// 通过Paper设置页面的空白边距和可打印区域。必须与实际打印纸张大小相符。
		Paper p = new Paper();
		p.setSize(590, 840); // 纸张大小(590, 840)表示A4纸
		p.setImageableArea(10, 10, 260, 343); // A4(595 X
		// 842)设置打印区域，其实0，0应该是72，72
		// ，因为A4纸的默认X,Y边距是72

		pf.setPaper(p);
		// 把 PageFormat 和 Printable 添加到书中，组成一个页面
		book.append(new Printable() {
			
			@Override
			public int print(Graphics graphics, PageFormat pageFormat, int pageIndex)
					throws PrinterException {
				// TODO Auto-generated method stub
				double x = pageFormat.getImageableX();
				double y = pageFormat.getImageableY();
				Graphics2D g2 = (Graphics2D) graphics;
				g2.setColor(Color.BLACK);
				Font font = new Font("宋体", Font.PLAIN, 9);
				g2.setFont(font); // 设置字体
				g2.drawString("Hello World !", (int)x+ 5, (int)y + 18);
				g2.drawLine((int) x, 30, 200, 30);
				
				return 0;
			}
		}, pf);
		// 获取打印服务对象
		PrinterJob job = PrinterJob.getPrinterJob();
		// 设置打印类
		job.setPageable(book);
		
		
		try {
			job.print();
		} catch (PrinterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		PrintService[] service = PrintServiceLookup.lookupPrintServices(null, null);
//		String value = "";
//		DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
//		PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();   
//		for(int i = 0 ; i < service.length ; i ++){
//			PrintService p = service[i];
//			value += p.getName();
//			DocPrintJob job = p.createPrintJob();
//				
//		    byte[] buf = "折尼玛的".getBytes();
//		    InputStream stream = new ByteArrayInputStream(buf);
//		    Doc doc = new SimpleDoc(stream, flavor, null);
//
//		   // print
//		    job.print(doc, null);
//
////			
////			
////			FileInputStream fis = new FileInputStream("C:" + File.separator + "111.txt");   
////			DocAttributeSet das = new HashDocAttributeSet();   
////			Doc doc = new SimpleDoc("Hello World !", flavor, das);   
////			job.print(doc, aset);   
//			
//		}
		
//		printerSettings_label.setText(value);
		
		
		
		
	}

	@Override
	public boolean isDisposed() {
		// TODO Auto-generated method stub
		return printerSettings_shell.isDisposed();
	}
	
	
}
