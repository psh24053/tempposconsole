package cn.panshihao.pos.handler;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.SimpleDoc;
import javax.print.attribute.DocAttributeSet;
import javax.print.attribute.HashDocAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.event.PrintJobEvent;
import javax.print.event.PrintJobListener;

import com.swetake.util.Qrcode;

import cn.panshihao.pos.dao.SuperDAO.DAOResponseListener;
import cn.panshihao.pos.tools.InitPrintable;
import cn.panshihao.pos.tools.PosLogger;

public class TestPrint implements Printable{
	
	private int PAGES = 0;
	
	public String content = "";
	public String imageContent = "";
	public String keyCode = "";
	public String address = "";
	public String phone = "";
	public String name = "";
	
	public boolean PrintPos(int userID,String servicesName,String content,String keyCode,String address,
			String phone,String name,DAOResponseListener listener){
		
				boolean isSuccess = true;
				PAGES = 1; // 获取打印总页数
//				Book book = new Book();
//
//				PageFormat pageFormat = new PageFormat();
//				pageFormat.setOrientation(PageFormat.PORTRAIT); 
//
//				Paper paper = new Paper();
//				paper.setSize(590, 840); 
//				paper.setImageableArea(10, 10, 260, 343); 
//
//				pageFormat.setPaper(paper);

//				InitPrintable printable = new InitPrintable();
				this.keyCode = keyCode;
				this.imageContent = keyCode;
				this.content = content;
				this.address = address;
				this.phone = phone;
				this.name = name;
				
//				book.append(printable, pageFormat);

				PrinterJob job = PrinterJob.getPrinterJob();
				
				PrintService[] printServices = PrinterJob.lookupPrintServices();
				
				boolean isFind = false;
				
				DocPrintJob dpj = null;
				
				//查找指定的PrintService
				for(int i = 0 ; i < printServices.length ; i++){

					if((printServices[i].getName()).equals(servicesName)){
						
						isFind = true;
						
						try {
							job.setPrintService(printServices[i]);
							dpj = printServices[i].createPrintJob();
						} catch (PrinterException e) {
							isSuccess = false;
							PosLogger.log.error(e.getMessage());
						}
						
					}
					
				}
				
				if(!isFind){
					
					PosLogger.log.error("Can't find PrintService,PrintServiceName = " + servicesName);
					if(listener != null){
						listener.onError(1);
					}
					return false;
					
				}
				
//				// 设置打印类
//				job.setPageable(book);
//				
//				try {
//					job.print();
//				} catch (PrinterException e) {
//					if(listener != null){
//						listener.onError(3);
//					}
//					isSuccess = false;
//					PosLogger.log.error(e.getMessage());
//				}
				DocFlavor flavor = DocFlavor.SERVICE_FORMATTED.PRINTABLE;
				PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
				pras.add(MediaSizeName.ISO_A4);
	            DocAttributeSet das = new HashDocAttributeSet();
				Doc doc = new SimpleDoc(this, flavor, das);
				
				dpj.addPrintJobListener(new PrintJobListener() {
		  			
		  			@Override
		  			public void printJobRequiresAttention(PrintJobEvent pje) {
		  				// TODO Auto-generated method stub
		  				System.out.println(pje.getPrintEventType());
		  				System.out.println("RequiresAttention");
		  			}
		  			
		  			@Override
		  			public void printJobNoMoreEvents(PrintJobEvent pje) {
		  				// TODO Auto-generated method stub
		  				System.out.println(pje.getPrintEventType());
		  				System.out.println("NoMoreEvents");
		  			}
		  			
		  			@Override
		  			public void printJobFailed(PrintJobEvent pje) {
		  				// TODO Auto-generated method stub
		  				System.out.println(pje.getPrintEventType());
		  				System.out.println("Failed");
		  			}
		  			
		  			@Override
		  			public void printJobCompleted(PrintJobEvent pje) {
		  				// TODO Auto-generated method stub
		  				System.out.println(pje.getPrintEventType());
		  				System.out.println("completed");
		  			}
		  			
		  			@Override
		  			public void printJobCanceled(PrintJobEvent pje) {
		  				// TODO Auto-generated method stub
		  				System.out.println(pje.getPrintEventType());
		  				System.out.println("Canceled");
		  			}
		  			
		  			@Override
		  			public void printDataTransferCompleted(PrintJobEvent pje) {
		  				// TODO Auto-generated method stub
		  				System.out.println(pje.getPrintEventType());
		  				System.out.println("TransferCompleted");
		  			}
		  		});
				
				System.out.println("sdff" + PrintJobEvent.DATA_TRANSFER_COMPLETE);
				System.out.println("sdff" + PrintJobEvent.JOB_CANCELED);
				System.out.println("sdff" + PrintJobEvent.JOB_COMPLETE);
				System.out.println("sdff" + PrintJobEvent.JOB_FAILED);
				System.out.println("sdff" + PrintJobEvent.NO_MORE_EVENTS);
				System.out.println("sdff" + PrintJobEvent.REQUIRES_ATTENTION);
				
				try {
					dpj.print(doc, pras);
				} catch (PrintException e) {
					e.printStackTrace();
				}
				
				return isSuccess;
				
	}
	

	@Override
	public int print(Graphics graphics, PageFormat pageFormat, int pageIndex)
			throws PrinterException {

		if (pageIndex >= PAGES) {
			// 当打印页号大于需要打印的总页数时，打印工作结束
			return Printable.NO_SUCH_PAGE;
		}

//		pageFormat.setOrientation(PageFormat.PORTRAIT);
//
//		Paper paper = new Paper();
//		paper.setSize(590, 840);
//		paper.setImageableArea(10, 10, 260, 343);
//
//		pageFormat.setPaper(paper);

		double x = pageFormat.getImageableX();
		double y = pageFormat.getImageableY();
		Graphics2D graph = (Graphics2D) graphics;
		graph.setColor(Color.BLACK);
		Font font = new Font("宋体", Font.PLAIN, 9);
		graph.setFont(font); 
		
		/********************************/
		Qrcode qrcode = new Qrcode();
		qrcode.setQrcodeErrorCorrect('M');
		qrcode.setQrcodeEncodeMode('B');
		qrcode.setQrcodeVersion(7);
		String QrcodeString = imageContent;
		
		byte[] byteImage = null;
		
		try {
			byteImage = QrcodeString.getBytes("utf-8");
		} catch (UnsupportedEncodingException e) {
			PosLogger.log.error(e.getMessage());
		}

		BufferedImage bufferedIamge = new BufferedImage(98, 98,BufferedImage.TYPE_BYTE_BINARY);
		
		Graphics2D imageGrap = bufferedIamge.createGraphics();
		
		imageGrap.setBackground(Color.WHITE);
		imageGrap.clearRect(0, 0, 98, 98);
		imageGrap.setColor(Color.BLACK);

		// 限制最大字节数为120
		if (byteImage.length > 0 && byteImage.length < 120) {
			boolean[][] s = qrcode.calQrcode(byteImage);
			for (int i = 0; i < s.length; i++) {
				for (int j = 0; j < s.length; j++) {
					if (s[j][i]) {
						imageGrap.fillRect(j * 2 + 3, i * 2 + 3, 2, 2);
					}
				}
			}
		}
		
		imageGrap.dispose();
		bufferedIamge.flush();

		/********************************/
		graph.drawLine((int) x, (int)y + 2, 200, (int)y + 2);
		graph.drawImage(bufferedIamge,null,(int)x + 20,(int)y + 20);
		graph.drawString("兑换码:",(int)x + 10, (int)y + 140);
		Font fontCode = new Font("宋体", Font.BOLD, 13);
		graph.setFont(fontCode); 
		graph.drawString(keyCode,(int)x + 10, (int)y + 160);
		graph.setFont(font);
		
		int yIndex = 180;
		
		if(content.length() > 14){
			
			graph.drawString(content.substring(0, 14), (int)x + 10, (int)y + yIndex);
			yIndex += 20;
			graph.drawString(content.substring(14, content.length()), (int)x + 10, (int)y + yIndex);
			yIndex += 10;
			
		}else{
			
			graph.drawString(content, (int)x + 10, (int)y + yIndex);
			yIndex += 10;
			
		}
		
		graph.drawLine((int) x, (int)y + yIndex, 200, (int)y + yIndex);
		yIndex += 15;
		
		if(name.length() > 11){
			
			graph.drawString("商家:" + name.substring(0,11), (int)x + 10, (int)y + yIndex);
			yIndex += 20;
			graph.drawString(name.substring(11,name.length()), (int)x + 10, (int)y + yIndex);
			yIndex += 20;
			
		}else{
			
			graph.drawString("商家:" + name, (int)x + 10, (int)y + yIndex);
			yIndex += 20;
			
		}
		
		if(address.length() > 11){
			
			graph.drawString("地址:" + address.substring(0,11), (int)x + 10, (int)y + yIndex);
			yIndex += 20;
			graph.drawString(address.substring(11,address.length()), (int)x + 10, (int)y + yIndex);
			yIndex += 20;
			
		}else{
			
			graph.drawString("地址:" + address, (int)x + 10, (int)y + yIndex);
			yIndex += 20;
			
		}
		
		graph.drawString("电话:" + phone, (int)x + 10, (int)y + yIndex);
		yIndex += 20;
		graph.drawString("--请妥善保管--", (int)x + 40, (int)y + yIndex);
		yIndex += 20;
		graph.drawLine((int) x, (int)y + yIndex, 200, (int)y + yIndex);
		
//		graph.drawLine((int) x, (int)y - 32, 200, (int)y - 32);
//		graph.drawImage(bufferedIamge,null,(int)x + 20,(int)y - 10);
//		graph.drawString(content, (int)x + 60, (int)y + 90);
//		graph.drawLine((int) x, (int)y + 100, 200, (int)y + 100);
		
		return 0;
	}

	public static void main(String[] args) {
		
		TestPrint tp = new TestPrint();
		tp.PrintPos(1,"\\\\Pc-20121019mbtd\\pos58", "火锅大酬宾5折随便吃", "123456FJSDLKFSDG", "新北小区新乐中街玲珑蓝宇199号", "15008224403","四川南方高新公司有限有限公司",null);
		
	}

}
