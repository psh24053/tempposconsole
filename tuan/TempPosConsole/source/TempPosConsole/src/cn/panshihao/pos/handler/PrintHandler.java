package cn.panshihao.pos.handler;

import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.ArrayList;
import java.util.List;

import javax.print.PrintService;

import cn.panshihao.pos.tools.InitPrintable;
import cn.panshihao.pos.tools.PosLogger;


//所有打印操作的实现类
public class PrintHandler {
	
	/**
	 * @author penglang
	 * @return 包含此计算机的所有打印设备的名称的List
	 */
	public static List<String> getAllPrintServicesName(){
		
		PosLogger.log.debug("Into getAllPrintServicesName");
		
		List<String> printServicesName = new ArrayList<>();
		
		PrintService[] printServices = PrinterJob.lookupPrintServices();
		
		if(printServices == null || printServices.length == 0){
			
			PosLogger.log.info("This computor have no print device");
			return printServicesName;
			
		}
		
		for(int i = 0 ; i < printServices.length ; i++){
			
			printServicesName.add(printServices[i].getName());
			
		}
		
		return printServicesName;
		
	}
	
	/**
	 * @author penglang
	 * 初始化打印机
	 */
	public void PrintPos(String servicesName,String content){
		
				Book book = new Book();

				PageFormat pageFormat = new PageFormat();
				pageFormat.setOrientation(PageFormat.PORTRAIT); 

				Paper paper = new Paper();
				paper.setSize(590, 840); 
				paper.setImageableArea(10, 10, 260, 343); 

				pageFormat.setPaper(paper);

				InitPrintable printable = new InitPrintable();
				printable.content = content;
				printable.imageContent = "王洁是个哈嘛批";
				
				book.append(printable, pageFormat);

				PrinterJob job = PrinterJob.getPrinterJob();
				
				PrintService[] printServices = PrinterJob.lookupPrintServices();
				
				boolean isFind = false;
				
				//查找指定的PrintService
				for(int i = 0 ; i < printServices.length ; i++){

					if((printServices[i].getName()).equals(servicesName)){
						
						isFind = true;
						
						try {
							job.setPrintService(printServices[i]);
						} catch (PrinterException e) {
							PosLogger.log.error(e.getMessage());
						}
						
					}
					
				}
				
				if(!isFind){
					
					PosLogger.log.error("Can't find PrintService,PrintServiceName = " + servicesName);
					return;
					
				}
				
				// 设置打印类
				job.setPageable(book);

				
				try {
					job.print();
				} catch (PrinterException e) {
					PosLogger.log.error(e.getMessage());
				}
		
	}
	
	public static void main(String[] args) {
		
		System.out.println(PrintHandler.getAllPrintServicesName().toString());
//		PrintHandler handler = new PrintHandler();
//		handler.PrintPos("POS58", "尊敬的王洁");
	
	}
	
}
