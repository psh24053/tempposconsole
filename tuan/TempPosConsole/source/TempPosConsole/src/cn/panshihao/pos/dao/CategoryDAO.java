package cn.panshihao.pos.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.panshihao.pos.model.Category;
import cn.panshihao.pos.tools.PosLogger;
import cn.panshihao.pos.tools.SQLConn;

//对category表的操作
public class CategoryDAO extends SuperDAO {
	
	private Connection conn = null;
	
	private PreparedStatement ps = null;
	
	private ResultSet rs = null;
	
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

		HashMap<String,Object> dataMap = this.selectFromDatabase(tablesName,primaryKeyName,primaryKeyVaule);
		
		if(dataMap == null){
			
			PosLogger.log.error("result is null");
			return null;
			
		}
		
		//加入数据
		if(dataMap.get("category_id") != null){
			
			category.setCategory_id((int)dataMap.get("category_id"));
		}
		if(dataMap.get("category_form") != null){
			
			category.setCategory_form((String)dataMap.get("category_form"));
		}
		if(dataMap.get("category_desc") != null){
			
			category.setCategory_desc((String)dataMap.get("category_desc"));
		}
		if(dataMap.get("category_name") != null){
			
			category.setCategory_name((String)dataMap.get("category_name"));
		}
		
		return category;
		
	}
	
	/**
	 * @author penglang
	 * @param start:查询开始位置,count:查询条数
	 * @return JsonObject
	 * 获取所有类别信息
	 * json说明:"list"-包含所有类别的数组名,"count"-实际得到的类别条数,"cid"-类别ID,
	 * "name"-类别名称,"desc"-类别描述,"form"-类别自定义表单
	 * json例:{count:2,list:[{uid:1,name:"娱乐","desc":"K歌,上网,样样都有",form:"暂不知道什么形式"}
	 * ,{uid:2,name:"餐饮",desc:"吃饭喝酒什么都有",form:"暂不知道什么形式"}]}
	 */
	public JSONObject getAllCategory(int start,int count){
		
		JSONObject allCategoryArray = new JSONObject();
		
		PosLogger.log.debug("Get all of category");
		
		conn = SQLConn.getConnection();
		
		try {
			ps = conn.prepareStatement("select * from " + tablesName  + " limit " + start + "," + count);
			
			rs = ps.executeQuery();
			
			if(rs == null){
				PosLogger.log.error("Database have no category");
				return null;
			}
			
			JSONArray array = new JSONArray();
			
			while (rs.next()) {
				
				JSONObject categoryJson = new  JSONObject();
				categoryJson.put("cid", rs.getInt("category_id"));
				categoryJson.put("name", rs.getString("category_name"));
				categoryJson.put("desc", rs.getString("category_desc"));
				categoryJson.put("form", rs.getString("category_form"));
				
				array.put(categoryJson);
				
			}
			
			allCategoryArray.put("list", array);
			
		} catch (SQLException e) {
			PosLogger.log.error(e.getMessage());
		} catch (JSONException e) {
			// TODO: handle exception
			PosLogger.log.error(e.getMessage());
		} finally{
			//关闭资源
			this.closeConnection();
		}
		
		return allCategoryArray;
	}
	
	private void closeConnection(){
		
		if(this.rs != null){
			
			try{
				rs.close();
			}catch(SQLException e){
				PosLogger.log.error(e.getMessage());
			}
			
		}
		
		if (this.ps != null) {

			try {
				ps.close();
			} catch (SQLException e) {
				PosLogger.log.error(e.getMessage());
			}

		}

		if (this.conn != null) {

			try {
				conn.close();
			} catch (SQLException e) {
				PosLogger.log.error(e.getMessage());
			}
		}
		
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
