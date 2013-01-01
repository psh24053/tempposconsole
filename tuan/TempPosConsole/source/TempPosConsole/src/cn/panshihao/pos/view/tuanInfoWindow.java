package cn.panshihao.pos.view;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.json.JSONException;
import org.json.JSONObject;

import cn.panshihao.pos.dao.KeyDAO;
import cn.panshihao.pos.dao.SuperDAO.DAOResponseListener;
import cn.panshihao.pos.dao.TuanDAO;
import cn.panshihao.pos.handler.AsyncHandler;
import cn.panshihao.pos.handler.PrintHandler;
import cn.panshihao.pos.handler.PrintToFirmHandler;
import cn.panshihao.pos.model.Tuan;
import cn.panshihao.pos.tools.TransDate;

public class tuanInfoWindow extends superWindow {

	private Tuan tuan;
	
	public String tuanInfo_title = "团购详情";
	public Shell tuanInfo_shell = null;
	
	
	public Label tuanInfo_id_label = null;
	public Text tuanInfo_id_text = null;
	
	public Label tuanInfo_name_label = null;
	public Text tuanInfo_name_text = null;
	
	public Label tuanInfo_category_label = null;
	public Text tuanInfo_category_text = null;
	
	public Label tuanInfo_desc_label = null;
	public Text tuanInfo_desc_text = null;
	
	public Label tuanInfo_firmName_label = null;
	public Text tuanInfo_firmName_text = null;
	
	public Label tuanInfo_firmAddress_label = null;
	public Text tuanInfo_firmAddress_text = null;
	
	public Label tuanInfo_firmPerson_label = null;
	public Text tuanInfo_firmPerson_text = null;
	
	public Label tuanInfo_firmPhone_label = null;
	public Text tuanInfo_firmPhone_text = null;
	
	public Label tuanInfo_startTime_label = null;
	public Text tuanInfo_startTime_text = null;
	
	public Label tuanInfo_endTime_label = null;
	public Text tuanInfo_endTime_text = null;
	
	public Label tuanInfo_count_label = null;
	public Text tuanInfo_count_text = null;
	
	public Label tuanInfo_remain_label = null;
	public Text tuanInfo_remain_text = null;
	
	public Button tuanInfo_getKey_button = null;
	public Button tuanInfo_getFirm_button = null;
	
	private onResultListener<String> listener = null;
	
	public tuanInfoWindow(superWindow parent, Tuan tuan, onResultListener<String> listener) {
		super(parent);
		// TODO Auto-generated constructor stub
		this.tuan = tuan;   
		this.listener = listener;
	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub
		initBase();
		
		initTuanInfo();
		
		initData();
		
		tuanInfo_shell.open();
		tuanInfo_shell.layout();
		
	}
	/**
	 * 初始化团购信息
	 */
	private void initTuanInfo(){
		tuanInfo_id_label = new Label(getShell(), SWT.NONE);
		tuanInfo_id_label.setText("团购ID");
		tuanInfo_id_label.setBounds(marginWidthValue * 2, marginHeightValue * 2, marginWidthValue * 4, marginHeightValue * 3);
		
		tuanInfo_id_text = new Text(getShell(), SWT.BORDER);
		tuanInfo_id_text.setEditable(false);
		tuanInfo_id_text.setBounds(tuanInfo_id_label.getBounds().x + tuanInfo_id_label.getBounds().width + marginWidthValue * 2, tuanInfo_id_label.getBounds().y - (int)(marginHeightValue * 0.5), marginWidthValue * 17, marginHeightValue * 3);
		
		tuanInfo_name_label = new Label(getShell(), SWT.NONE);
		tuanInfo_name_label.setText("团购名称");
		tuanInfo_name_label.setBounds(tuanInfo_id_label.getBounds().x, tuanInfo_id_label.getBounds().y + tuanInfo_id_label.getBounds().height + marginHeightValue, marginWidthValue * 4, marginHeightValue * 3);
		
		tuanInfo_name_text = new Text(getShell(), SWT.BORDER);
		tuanInfo_name_text.setEditable(false);
		tuanInfo_name_text.setBounds(tuanInfo_name_label.getBounds().x + tuanInfo_name_label.getBounds().width + marginWidthValue * 2, tuanInfo_name_label.getBounds().y - (int)(marginHeightValue * 0.5), marginWidthValue * 17, marginHeightValue * 3);
		
		tuanInfo_category_label = new Label(getShell(), SWT.NONE);
		tuanInfo_category_label.setText("团购类别");
		tuanInfo_category_label.setBounds(tuanInfo_name_label.getBounds().x, tuanInfo_name_label.getBounds().y + tuanInfo_name_label.getBounds().height + marginHeightValue, marginWidthValue * 4, marginHeightValue * 3);
		
		tuanInfo_category_text = new Text(getShell(), SWT.BORDER);
		tuanInfo_category_text.setEditable(false);
		tuanInfo_category_text.setBounds(tuanInfo_category_label.getBounds().x + tuanInfo_category_label.getBounds().width + marginWidthValue * 2, tuanInfo_category_label.getBounds().y - (int)(marginHeightValue * 0.5), marginWidthValue * 17, marginHeightValue * 3);
		
		tuanInfo_desc_label = new Label(getShell(), SWT.NONE);
		tuanInfo_desc_label.setText("团购描述");
		tuanInfo_desc_label.setBounds(tuanInfo_category_label.getBounds().x, tuanInfo_category_label.getBounds().y + tuanInfo_category_label.getBounds().height + marginHeightValue, marginWidthValue * 4, marginHeightValue * 3);
		
		tuanInfo_desc_text = new Text(getShell(), SWT.BORDER | SWT.WRAP);
		tuanInfo_desc_text.setEditable(false);
		tuanInfo_desc_text.setBounds(tuanInfo_desc_label.getBounds().x + tuanInfo_desc_label.getBounds().width + marginWidthValue * 2, tuanInfo_desc_label.getBounds().y - (int)(marginHeightValue * 0.5), marginWidthValue * 17, marginHeightValue * 13);
		
		tuanInfo_firmName_label = new Label(getShell(), SWT.NONE);
		tuanInfo_firmName_label.setText("商家名称");
		tuanInfo_firmName_label.setBounds(tuanInfo_desc_label.getBounds().x, tuanInfo_desc_text.getBounds().y + tuanInfo_desc_text.getBounds().height + marginHeightValue, marginWidthValue * 4, marginHeightValue * 3);
		
		tuanInfo_firmName_text = new Text(getShell(), SWT.BORDER | SWT.WRAP);
		tuanInfo_firmName_text.setEditable(false);
		tuanInfo_firmName_text.setBounds(tuanInfo_firmName_label.getBounds().x + tuanInfo_firmName_label.getBounds().width + marginWidthValue * 2, tuanInfo_firmName_label.getBounds().y - (int)(marginHeightValue * 0.5), marginWidthValue * 17, marginHeightValue * 3);
		
		tuanInfo_firmPerson_label = new Label(getShell(), SWT.NONE);
		tuanInfo_firmPerson_label.setText("负责人");
		tuanInfo_firmPerson_label.setBounds(tuanInfo_firmName_label.getBounds().x, tuanInfo_firmName_label.getBounds().y + tuanInfo_firmName_label.getBounds().height + marginHeightValue, marginWidthValue * 4, marginHeightValue * 3);
		
		tuanInfo_firmPerson_text = new Text(getShell(), SWT.BORDER | SWT.WRAP);
		tuanInfo_firmPerson_text.setEditable(false);
		tuanInfo_firmPerson_text.setBounds(tuanInfo_firmPerson_label.getBounds().x + tuanInfo_firmPerson_label.getBounds().width + marginWidthValue * 2, tuanInfo_firmPerson_label.getBounds().y - (int)(marginHeightValue * 0.5), marginWidthValue * 17, marginHeightValue * 3);
		
		tuanInfo_firmPhone_label = new Label(getShell(), SWT.NONE);
		tuanInfo_firmPhone_label.setText("商家电话");
		tuanInfo_firmPhone_label.setBounds(tuanInfo_firmPerson_label.getBounds().x, tuanInfo_firmPerson_label.getBounds().y + tuanInfo_firmPerson_label.getBounds().height + marginHeightValue, marginWidthValue * 4, marginHeightValue * 3);
			
		tuanInfo_firmPhone_text = new Text(getShell(), SWT.BORDER | SWT.WRAP);
		tuanInfo_firmPhone_text.setEditable(false);
		tuanInfo_firmPhone_text.setBounds(tuanInfo_firmPhone_label.getBounds().x + tuanInfo_firmPhone_label.getBounds().width + marginWidthValue * 2, tuanInfo_firmPhone_label.getBounds().y - (int)(marginHeightValue * 0.5), marginWidthValue * 17, marginHeightValue * 3);
		
		tuanInfo_firmAddress_label = new Label(getShell(), SWT.NONE);
		tuanInfo_firmAddress_label.setText("商家地址");
		tuanInfo_firmAddress_label.setBounds(tuanInfo_firmPhone_label.getBounds().x, tuanInfo_firmPhone_label.getBounds().y + tuanInfo_firmPhone_label.getBounds().height + marginHeightValue, marginWidthValue * 4, marginHeightValue * 3);
		
		tuanInfo_firmAddress_text = new Text(getShell(), SWT.BORDER | SWT.WRAP);
		tuanInfo_firmAddress_text.setEditable(false);
		tuanInfo_firmAddress_text.setBounds(tuanInfo_firmAddress_label.getBounds().x + tuanInfo_firmAddress_label.getBounds().width + marginWidthValue * 2, tuanInfo_firmAddress_label.getBounds().y - (int)(marginHeightValue * 0.5), marginWidthValue * 17, marginHeightValue * 3);
		
		tuanInfo_startTime_label = new Label(getShell(), SWT.NONE);
		tuanInfo_startTime_label.setText("开始时间");
		tuanInfo_startTime_label.setBounds(tuanInfo_firmAddress_label.getBounds().x, tuanInfo_firmAddress_label.getBounds().y + tuanInfo_firmAddress_label.getBounds().height + marginHeightValue, marginWidthValue * 4, marginHeightValue * 3);
		
		tuanInfo_startTime_text = new Text(getShell(), SWT.BORDER | SWT.WRAP);
		tuanInfo_startTime_text.setEditable(false);
		tuanInfo_startTime_text.setBounds(tuanInfo_startTime_label.getBounds().x + tuanInfo_startTime_label.getBounds().width + marginWidthValue * 2, tuanInfo_startTime_label.getBounds().y - (int)(marginHeightValue * 0.5), marginWidthValue * 17, marginHeightValue * 3);
		
		tuanInfo_endTime_label = new Label(getShell(), SWT.NONE);
		tuanInfo_endTime_label.setText("结束时间");
		tuanInfo_endTime_label.setBounds(tuanInfo_startTime_label.getBounds().x, tuanInfo_startTime_label.getBounds().y + tuanInfo_startTime_label.getBounds().height + marginHeightValue, marginWidthValue * 4, marginHeightValue * 3);
		
		tuanInfo_endTime_text = new Text(getShell(), SWT.BORDER | SWT.WRAP);
		tuanInfo_endTime_text.setEditable(false);
		tuanInfo_endTime_text.setBounds(tuanInfo_endTime_label.getBounds().x + tuanInfo_endTime_label.getBounds().width + marginWidthValue * 2, tuanInfo_endTime_label.getBounds().y - (int)(marginHeightValue * 0.5), marginWidthValue * 17, marginHeightValue * 3);
		
		tuanInfo_count_label = new Label(getShell(), SWT.NONE);
		tuanInfo_count_label.setText("兑换码总数");
		tuanInfo_count_label.setBounds(tuanInfo_endTime_label.getBounds().x, tuanInfo_endTime_label.getBounds().y + tuanInfo_endTime_label.getBounds().height + marginHeightValue, (int)(marginWidthValue * 4.5), marginHeightValue * 3);
		
		tuanInfo_count_text = new Text(getShell(), SWT.BORDER | SWT.WRAP);
		tuanInfo_count_text.setEditable(false);
		tuanInfo_count_text.setBounds(tuanInfo_count_label.getBounds().x + tuanInfo_count_label.getBounds().width + (int)(marginWidthValue * 1.5), tuanInfo_count_label.getBounds().y - (int)(marginHeightValue * 0.5), marginWidthValue * 17, marginHeightValue * 3);
		
		tuanInfo_remain_label = new Label(getShell(), SWT.NONE);
		tuanInfo_remain_label.setText("剩余兑换码");
		tuanInfo_remain_label.setBounds(tuanInfo_count_label.getBounds().x, tuanInfo_count_label.getBounds().y + tuanInfo_count_label.getBounds().height + marginHeightValue, (int)(marginWidthValue * 4.5), marginHeightValue * 3);
		
		tuanInfo_remain_text = new Text(getShell(), SWT.BORDER | SWT.WRAP);
		tuanInfo_remain_text.setEditable(false);
		tuanInfo_remain_text.setBounds(tuanInfo_remain_label.getBounds().x + tuanInfo_remain_label.getBounds().width + (int)(marginWidthValue * 1.5), tuanInfo_remain_label.getBounds().y - (int)(marginHeightValue * 0.5), marginWidthValue * 17, marginHeightValue * 3);
		
		
		tuanInfo_getKey_button = new Button(getShell(), SWT.NONE);
		tuanInfo_getKey_button.setText("提取兑换码");
		tuanInfo_getKey_button.setBounds(tuanInfo_remain_label.getBounds().x, tuanInfo_remain_label.getBounds().y + tuanInfo_remain_label.getBounds().height + marginHeightValue * 3, marginWidthValue * 8, marginHeightValue * 5);
		tuanInfo_getKey_button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				getKey();
			}
		});
		
		tuanInfo_getFirm_button = new Button(getShell(), SWT.NONE);
		tuanInfo_getFirm_button.setText("打印商家版");
		tuanInfo_getFirm_button.setBounds(tuanInfo_getKey_button.getBounds().x + tuanInfo_getKey_button.getBounds().width + marginWidthValue * 7, tuanInfo_remain_label.getBounds().y + tuanInfo_remain_label.getBounds().height + marginHeightValue * 3, marginWidthValue * 8, marginHeightValue * 5);
		tuanInfo_getFirm_button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				getFirm();
			}
		});
		
	}
	/**
	 * 提取兑换码的操作
	 */
	private void getKey(){
		int remain = (int) tuan.getValue("remain");
		
		if(remain < 1){
			alert(getShell(), "错误", "该团购已无兑换码！");
			return;
		}
		
		
		// 如果打印机名称被选择过，
		if(cacheHandler.hasBaseCache("PrinterName")){
			String printername = cacheHandler.getBaseString("PrinterName", "");
			
			KeyDAO dao = new KeyDAO();
			
			dao.updateKeyStatusAndPrint(getCurUser().getUser_id(), printername, tuan.getTuan_name(), dao.getNotUsedOneKeyCode(tuan.getTuan_id()), tuan.getValue("address").toString(), tuan.getValue("phone").toString(), tuan.getValue("firm").toString(), new DAOResponseListener() {
				
				@Override
				public void onSuccess() {
					// TODO Auto-generated method stub
					alert(getShell(), "成功", "提取成功！");
					initData();
					if(listener != null){
						listener.onResult("Re");
					}
				}
				
				@Override
				public void onError(int errorcode) {
					// TODO Auto-generated method stub
					switch (errorcode) {
					case 1:
						alert(getShell(), "失败", "找不到指定打印机！");
						cacheHandler.removeBaseCache("PrinterName").CommitBaseCache();
						break;
					case 2:
						alert(getShell(), "失败", "数据库连接失败！请重启计算机");
						break;
					case 3:
						alert(getShell(), "失败", "打印失败，请检查打印机！");
						cacheHandler.removeBaseCache("PrinterName").CommitBaseCache();
						break;
					default:
						break;
					}
				}
			});
			
		}else{
			new selectPrintWindow(This(), new onResultListener<String>() {
				
				@Override
				public void onResult(String result) {
					// TODO Auto-generated method stub
					if(result != null && !result.equals("")){
						
						cacheHandler.putString("PrinterName", result).CommitBaseCache();
						getKey();
					}
					
				}
			}).show();
		}
		
		
	}
	/**
	 * 打印商家版的操作
	 */
	private void getFirm(){
		
		// 如果打印机名称被选择过，
		if(cacheHandler.hasBaseCache("FirmPrinterName")){
			String printername = cacheHandler.getBaseString("FirmPrinterName", "");
			
			
//			PrintToFirmHandler handler = new PrintToFirmHandler();
//			handler.printBegin(servicesName, firmName, tuanName, beginTime, endTime, keyCodeList)
			
			
//			KeyDAO dao = new KeyDAO();
//			
//			dao.updateKeyStatusAndPrint(getCurUser().getUser_id(), printername, tuan.getTuan_name(), dao.getNotUsedOneKeyCode(tuan.getTuan_id()), tuan.getValue("address").toString(), tuan.getValue("phone").toString(), tuan.getValue("firm").toString(), new DAOResponseListener() {
//				
//				@Override
//				public void onSuccess() {
//					// TODO Auto-generated method stub
//					alert(getShell(), "成功", "提取成功！");
//					initData();
//					if(listener != null){
//						listener.onResult("Re");
//					}
//				}
//				
//				@Override
//				public void onError(int errorcode) {
//					// TODO Auto-generated method stub
//					switch (errorcode) {
//					case 1:
//						alert(getShell(), "失败", "找不到指定打印机！");
//						cacheHandler.removeBaseCache("FirmPrinterName").CommitBaseCache();
//						break;
//					case 2:
//						alert(getShell(), "失败", "数据库连接失败！请重启计算机");
//						break;
//					case 3:
//						alert(getShell(), "失败", "打印失败，请检查打印机！");
//						cacheHandler.removeBaseCache("FirmPrinterName").CommitBaseCache();
//						break;
//					default:
//						break;
//					}
//				}
//			});
			
		}else{
			new selectPrintWindow(This(), new onResultListener<String>() {
				
				@Override
				public void onResult(String result) {
					// TODO Auto-generated method stub
					if(result != null && !result.equals("")){
						
						cacheHandler.putString("FirmPrinterName", result).CommitBaseCache();
						getKey();
					}
					
				}
			}).show();
		}
		
		
	}
	
	/**
	 * 加载数据
	 */
	private void initData(){
		
		new loadTuanInfoAsyncHandler(This()).start(tuan.getTuan_id());
		
	}
	/**
	 * 记载团购信息的asynchandler
	 * @author Administrator
	 *
	 */
	private class loadTuanInfoAsyncHandler extends AsyncHandler<Integer, Integer, JSONObject>{

		public loadTuanInfoAsyncHandler(superWindow window) {
			super(window);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onBefore() {
			// TODO Auto-generated method stub
			super.onBefore();
			
			tuanInfo_getFirm_button.setEnabled(false);
			tuanInfo_getKey_button.setEnabled(false);
			
		}
		
		@Override
		public JSONObject doInBackground(Integer... params) {
			// TODO Auto-generated method stub
			TuanDAO dao = new TuanDAO();
			
			return dao.getQueryTuan(params[0]);
		}

		@Override
		public void onComplete(JSONObject result) {
			// TODO Auto-generated method stub
			super.onComplete(result);
			
			if(result != null){
				
				try {
					changeData(result);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				tuanInfo_getFirm_button.setEnabled(true);
				tuanInfo_getKey_button.setEnabled(true);
			}else{
				alert(getShell(), "失败", "获取团购详情失败!");
				getShell().dispose();
				
			}
			
		}
		
	}
	/**
	 * 根据传入的数据改变界面
	 *  json说明:
	 *  "cat"-团购类别名称,
	 *  "tid"-团购ID 
	 *  "name"-团购名字,
	 *  "desc"-团购描述,
	 *  "firm"-团购商家名称,
	 *  "sta"-团购开始时间,
	 *  "end"-团购结束时间, 
	 *  "count"-该团购总兑换码数量,
	 *  "remain"-该团购剩余兑换码数量,
	 *  "person"-负责人名称,
	 *  "address"-地址 ,
	 *  "phone"-商家电话 
	 *  json例:{cat:"餐饮",tid:1,name:"南方高新火锅城团购券",desc:"南方高新火锅城70一人随便吃", firm:"南方高新火锅城",sta:1234567891011,end:1234567891012,count:10,remain:5}
	 * @param data
	 * @throws JSONException 
	 */
	private void changeData(JSONObject data) throws JSONException{
		
		tuan.setTuan_desc(data.getString("desc"));
		tuan.setTuan_name(data.getString("name"));
		tuan.setTuan_starttime(data.getLong("sta"));
		tuan.setTuan_endtime(data.getLong("end"));
		tuan.putValue("firm", data.getString("firm"));
		tuan.putValue("address", data.getString("address"));
		tuan.putValue("person", data.getString("person"));
		tuan.putValue("phone", data.getString("phone"));
		tuan.putValue("count", data.getInt("count"));
		tuan.putValue("remain", data.getInt("remain"));
		
		tuanInfo_id_text.setText(data.getInt("tid") + "");
		
		tuanInfo_name_text.setText(data.getString("name"));
		
		tuanInfo_desc_text.setText(data.getString("desc"));
		
		tuanInfo_firmName_text.setText(data.getString("firm"));
		
		tuanInfo_firmAddress_text.setText(data.getString("address"));
		
		tuanInfo_firmPerson_text.setText(data.getString("person"));
		
		tuanInfo_firmPhone_text.setText(data.getString("phone"));
		
		tuanInfo_startTime_text.setText(TransDate.convertTime(data.getLong("sta")));
		
		tuanInfo_endTime_text.setText(TransDate.convertTime(data.getLong("end")));
		
		tuanInfo_count_text.setText(data.getInt("count")+"");
		
		tuanInfo_remain_text.setText(data.getInt("remain")+"");
		
		tuanInfo_category_text.setText(data.getString("cat"));
	}
	
	/**
	 * 初始化界面基本数据
	 */
	private void initBase(){
		tuanInfo_shell = new Shell(getParent().getShell(), SWT.CLOSE | SWT.APPLICATION_MODAL);
		tuanInfo_shell.setText(tuanInfo_title);
		tuanInfo_shell.setSize(marginWidthValue * 30, marginHeightValue * 75);
		tuanInfo_shell.setLocation(getCenterX(tuanInfo_shell), getCenterY(tuanInfo_shell));
		
		
	}

	@Override
	protected Shell getShell() {
		// TODO Auto-generated method stub
		return tuanInfo_shell;
	}

}
