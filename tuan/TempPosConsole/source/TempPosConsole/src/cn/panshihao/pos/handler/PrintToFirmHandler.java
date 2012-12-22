package cn.panshihao.pos.handler;

import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.print.PrintService;

import cn.panshihao.pos.tools.PosLogger;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComFailException;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

public class PrintToFirmHandler {
	
	private Dispatch range;
	
    private Dispatch tbCell;
    
    private ActiveXComponent objWord;
    
    private Dispatch documents;
    
    private Dispatch document;
    
    /** 所有表格对象 */
    private Dispatch wdTables;
    
    /** 当前表格对象 */
    private Dispatch wdTable;
    
    private Dispatch selection;
    
	private static String nowSeparator = File.separator;
	
	public PrintToFirmHandler(){
		
	    ComThread.InitSTA();

	    objWord = new ActiveXComponent("Word.Application"); 
	    Dispatch wordObject = (Dispatch) objWord.getObject(); 
	    Dispatch.put((Dispatch) wordObject, "Visible", new Variant(false));
	    
	    documents = objWord.getProperty("Documents").toDispatch(); 
	    //得到项目路径
	    String projectPath = System.getProperty("user.dir");
	    String docPath = projectPath + nowSeparator + "src" + nowSeparator + "cn" + nowSeparator +
				"panshihao" + nowSeparator + "pos" + nowSeparator + "profile" + nowSeparator + "pirntToFirmModel.doc";
	    document = Dispatch.call(documents, "Open",docPath).toDispatch(); 
	    
	    wdTables = ActiveXComponent.call(document, "Tables").getDispatch();
	    selection = Dispatch.get(objWord, "Selection").toDispatch();
		
	}
	
	/**
	 * @author penglang
	 * @return servicesName(打印设备名字)
	 * @return boolean(打印是否成功)
	 * @param firmName(商家名字)
	 * @param tuanName(团购名字)
	 * @param beginTime(开始时间)
	 * @param endTime(结束时间)
	 * @param keyCodeList(兑换码List,大小不能超过48,若大于48,请分批调用两次打印两张)
	 * 打印实现类
	 */
	public boolean printBegin(String servicesName,String firmName,String tuanName,long beginTime,long endTime
			,List<String> keyCodeList){
		
		boolean isSuccess = true;

		if(keyCodeList == null || keyCodeList.size() <= 0 || keyCodeList.size() > 48){
			PosLogger.log.error("keyCodeList error");
			return false;
		}
		
		
		try {
			
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd日 HH:mm");
			
			//添加兑换码
			if(keyCodeList.size() <= 24){
				
				//小于24个兑换码
				for(int i = 0 ; i < keyCodeList.size() ; i++){
					
					putTextToCell(2, i + 2, 1, keyCodeList.get(i));
					
				}
				
			}else{
				
				//大于24个兑换码
				for(int i = 0 ; i < 24 ; i++){
					
					putTextToCell(2, i + 2, 1, keyCodeList.get(i));
					
				}
				
				int index = 2;
				for(int j = keyCodeList.size() - 1; j >= 24 ; j--){
					
					putTextToCell(1, index, 1, keyCodeList.get(j));
					index++;
					
				}
				
			}
			
			if(!replaceText("商家:", "商家:" + firmName)){
				//操作失败
				PosLogger.log.error("Print error,firmName=" + firmName);
				return false;
			}
			if(!replaceText("团购名称:", "团购名称:" + tuanName)){
				//操作失败
				PosLogger.log.error("Print error,tuanName=" + tuanName);
				return false;
			}
			if(!replaceText("团购开始时间:", "团购开始时间:" + format.format(beginTime))){
				//操作失败
				PosLogger.log.error("Print error,beginTime=" + beginTime);
				return false;
			}
			if(!replaceText("团购结束时间:", "团购结束时间:" + format.format(endTime))){
				//操作失败
				PosLogger.log.error("Print error,beginTime=" + beginTime);
				return false;
			}
			
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
			
			if(isFind){
				
				Dispatch.call(document, "PrintOut");

			}

		} catch (Exception e) {
			
			PosLogger.log.error("Print fail");
			PosLogger.log.error(e.getMessage());

		} finally {

			Dispatch.call(document, "Close", new Variant(false));
			objWord.invoke("Quit", new Variant[] {});
			ComThread.Release();
		}
		
		return isSuccess;
		
	}
	
	private void putText(Dispatch dispatch, String text) throws Exception {
		
		range = getRange(dispatch);
		Dispatch.put(range, "Text", text);
	}

	/**
	 * 在指定的表格中的单元个填充数据
	 * 
	 * @param tbIndex
	 *            　word文件中的第N张表(从1开始)
	 * @param rowIndex
	 *            　指定行的序号(从1开始)
	 * @param colIndex
	 *            　指定列的序号(从1开始)
	 * @param text
	 *            　填充的文本内容
	 */
	private void putTextToCell(int tbIndex, int rowIndex, int colIndex,
			String text) throws Exception {
		putText(getCell(getTable(tbIndex), rowIndex, colIndex), text);
	}
	
    private Dispatch getTable(int tbIndex) {
        wdTable =  Dispatch.call(wdTables, "Item", new Variant(tbIndex)).getDispatch();
        return wdTable;
    }
    
    private Dispatch getRange(Dispatch dispatch){
        return Dispatch.get(dispatch,"Range").getDispatch();
    }
    
    private Dispatch getCell(Dispatch tableDispatch,int tbRowIndex, int tbColumnIndex) throws Exception{
        try{
        	
            tbCell = Dispatch.call(tableDispatch, "Cell",new Variant(tbRowIndex),new Variant(tbColumnIndex)).getDispatch();
        
        }catch(ComFailException cfe){
        
        	throw new Exception(cfe);
        
        }
       
        return tbCell;
    
    }
    
    /**
    * 从选定内容或插入点开始查找文本
    * 
    * @param toFindText
    *            要查找的文本
    * @return boolean true-查找到并选中该文本，false-未查找到文本
    */
    private boolean find(String toFindText) {
    
	    if (toFindText == null || toFindText.equals("")){
	    	
	    	return false;
	    }
	
	    Dispatch find = objWord.call(selection, "Find").toDispatch();
	    Dispatch.call(selection, "HomeKey", new Variant(6));
	
	    Dispatch.put(find, "Text", toFindText);
	    
	    Dispatch.put(find, "Forward", "True");
	
	    Dispatch.put(find, "Format", "True");
	
	    Dispatch.put(find, "MatchCase", "True");
	
	    Dispatch.put(find, "MatchWholeWord", "True");
	
	    return Dispatch.call(find, "Execute").getBoolean();
	    
    }
    
    /**
    * 把选定选定内容设定为替换文本
    * 
    * @param toFindText
    *            查找字符串
    * @param newText
    *            要替换的内容
    * @return
    */
    private boolean replaceText(String toFindText, String newText) {
    	
	    if (!find(toFindText)){
	    	
	    	return false;
	    
	    }
	    
	    Dispatch.put(selection, "Text", newText);
	   
	    return true;
    }
	
    public static void main(String[] args) {
		
    	PrintToFirmHandler ptfh = new PrintToFirmHandler();
    	List<String> list = new ArrayList<String>();
    	for(int i = 0 ; i < 43 ; i++){

    		list.add("123456FJSDLKFSDG");
    		
    	}
    	
    	System.out.println(list.size());
		PrintService[] printServices = PrinterJob.lookupPrintServices();
//    	String print = "\\\192.168.0.2\\HP_LaserJet_P1007";
    	System.out.println(ptfh.printBegin(printServices[3].getName(),"四川南方高新", "四川南方大酬宾5折随便吃", 1355216304532L, 1355216304532L, list));
    	
    	
	}
    
}
