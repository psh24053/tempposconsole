package cn.panshihao.pos.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import cn.panshihao.pos.model.Category;
import cn.panshihao.pos.tools.PosLogger;

//对category表的操作
public class CategoryDAO extends SuperDAO {
	
	private final String tablesName = "temp_category";
	
	private final String primaryKeyName = "category_id";
	
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
	
	//修改类别操作
	public boolean updateCategory(Category category){
		
		boolean isSuccess = false;
		
		if (category == null) {

			PosLogger.log.error("update category fail,category object is not exist");
			return isSuccess;

		}
		
		// 检查categoryid是否为空
		if (category.getCategory_id() <= 0) {

			PosLogger.log.error("update category fail,category id is not exist");
			return isSuccess;

		}
		
		//构造添加数据集合
		HashMap<String,Object> colunmsMap = new HashMap<String,Object>();
		
		
		if (category.getCategory_name() != null) {

			colunmsMap.put("category_name",category.getCategory_name());
		}

		if (category.getCategory_desc() != null) {

			colunmsMap.put("category_desc",category.getCategory_desc());

		}
		
		if (category.getCategory_form() != null) {

			colunmsMap.put("category_form",category.getCategory_form());

		}
		
		isSuccess = this.updateToDatabase(tablesName,primaryKeyName,category.getCategory_id(),colunmsMap);
		
		return isSuccess;
		
	}
	
	//删除类别
	public boolean deleteCategory(int primaryKeyVaule){
		
		boolean isSuccess = false;
		
		// 检查primaryKeyVaule是否为空
		if (primaryKeyVaule <= 0) {

			PosLogger.log.error("delete category fail,category id is not exist");
			return isSuccess;

		}

		isSuccess = this.deleteToDatabase(tablesName,primaryKeyName,primaryKeyVaule);
		
		return isSuccess;
		
	}
	
	//根据主键获得category
	public Category getCategoryFromDatabase(int primaryKeyVaule){
		
		Category category = new Category();
		
		// 检查primaryKeyVaule是否为空
		if (primaryKeyVaule <= 0) {

			PosLogger.log.error("select category fail,category id is not exist");
			return null;

		}

		ResultSet rs = this.selectFromDatabase(tablesName,primaryKeyName,primaryKeyVaule);
		
		if(rs == null){
			
			PosLogger.log.error("result is null");
			return null;
			
		}
		
		try {
			
			if(rs.next()){
				
				category.setCategory_id(rs.getInt("category_id"));
				category.setCategory_form(rs.getString("category_form"));
				category.setCategory_desc(rs.getString("category_desc"));
				category.setCategory_name(rs.getString("category_name"));
				
			}
			
		} catch (SQLException e) {
			PosLogger.log.error(e.getMessage());
		} finally{
			
			if(rs != null){
				try {
					rs.close();
				} catch (SQLException e) {
					PosLogger.log.error(e.getMessage());
				}
			}
			
		}
		
		return category;
		
	}
	
	public static void main(String[] args) {
		
		CategoryDAO dao = new CategoryDAO();
		Category category = new Category();
		category.setCategory_id(1);
		category.setCategory_name("娱乐");
//		category.setCategory_desc("娱乐是很好的东西");
//		category.setCategory_form("我不知道有什么形式");
		dao.updateCategory(category);
		
	}
	
}
