package cn.panshihao.pos.dao;

import java.util.HashMap;

import cn.panshihao.pos.model.Category;
import cn.panshihao.pos.tools.PosLogger;

//对category表的操作
public class CategoryDAO extends SuperDAO {
	
	private String tablesName = "temp_category";
	
	//添加类别操作
	public boolean addCategory(Category category){
		
		boolean isSuccess = false;
		
		if (category == null) {

			PosLogger.log.error("add category fail,category object is not exist");
			return isSuccess;

		}
		
		// 检查列别名是否为空
		if (category.getCategory_name() == null) {

			PosLogger.log.error("add category fail,category name is not exist");
			return isSuccess;

		}

		// 检查类别描述是否为空
		if (category.getCategory_desc() == null) {

			PosLogger.log.error("add category fail,category description is not exist");
			return isSuccess;

		}
		

		PosLogger.log.debug("add category , categoryname = "  + category.getCategory_name());
		
		//构造添加数据集合
		HashMap<String,Object> colunmsMap = new HashMap<String,Object>();
		
		colunmsMap.put("category_name",category.getCategory_name());
		colunmsMap.put("category_desc",category.getCategory_desc());
		// 检查类别自定义表单是否为空
		if (category.getCategory_form() != null) {
			
			colunmsMap.put("category_form",category.getCategory_form());
			
		}else{
			
			PosLogger.log.info("add category ,category form is null");
		}
		
		isSuccess = this.insertIntoDatabase(tablesName,colunmsMap);
		
		return isSuccess;
		
	}
	
	public static void main(String[] args) {
		
		CategoryDAO dao = new CategoryDAO();
		Category category = new Category();
		category.setCategory_name("娱乐");
		category.setCategory_desc("娱乐是很好的东西");
		category.setCategory_form("我不知道有什么形式");
		dao.addCategory(category);
		
	}
	
}
