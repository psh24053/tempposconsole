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
	public boolean PrintPos(int userID,String servicesName,String content,String keyCode,String address,
			String phone,String name){
			
				boolean isSuccess = true;
		
				Book book = new Book();

				PageFormat pageFormat = new PageFormat();
				pageFormat.setOrientation(PageFormat.PORTRAIT); 

				Paper paper = new Paper();
				paper.setSize(590, 840); 
				paper.setImageableArea(10, 10, 260, 343); 

				pageFormat.setPaper(paper);

				InitPrintable printable = new InitPrintable();
				printable.keyCode = keyCode;
				printable.imageContent = keyCode;
				printable.content = content;
				printable.address = address;
				printable.phone = phone;
				printable.name = name;
				
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
							isSuccess = false;
							PosLogger.log.error(e.getMessage());
						}
						
					}
					
				}
				
				if(!isFind){
					
					PosLogger.log.error("Can't find PrintService,PrintServiceName = " + servicesName);
					return false;
					
				}
				
				// 设置打印类
				job.setPageable(book);

				
				try {
					job.print();
				} catch (PrinterException e) {
					isSuccess = false;
					PosLogger.log.error(e.getMessage());
				}
				
				return isSuccess;
				
	}
	
	public static void main(String[] args) {
		
//		System.out.println(PrintHandler.getAllPrintServicesName().toString());
		PrintHandler handler = new PrintHandler();
//		handler.PrintPos("\\\\Pc-20121019mbtd\\pos58", "四川南方高新火锅大酬宾5折随便吃");
		handler.PrintPos(1,"\\\\Pc-20121019mbtd\\pos58", "火锅大酬宾5折随便吃", "123456FJSDLKFSDG", "新北小区新乐中街玲珑蓝宇199号", "15008224403","四川南方高新公司有限有限公司");
		
	
	}
	
}
