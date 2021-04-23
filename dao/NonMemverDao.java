package dao;

import java.util.List;
import java.util.Map;

import util.JDBCUtil;

public class NonMemverDao {
	
	private NonMemverDao(){};
	private static NonMemverDao instance;
	public static NonMemverDao getInstance(){
		if (instance == null) {
			instance = new NonMemverDao();
		}
		return instance;
	}
	
	JDBCUtil  jdbc = JDBCUtil.getInstance();
	public List<Map<String, Object>> count() {
		String sql = "UPDATE P1_NON_MEMBER"
				+ " SET NONUSER_NUMBER = P1_NON_SEQ.NEXTVAL ";
		jdbc.update(sql);
		String sql1 = "SELECT * FROM P1_NON_MEMBER";
		return	jdbc.selectList(sql1); 
	}
	
	
	
	
	
}
