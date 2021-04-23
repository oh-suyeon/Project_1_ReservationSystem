package dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import util.JDBCUtil;

public class UserEditDao {

	private UserEditDao(){};
	private static UserEditDao instance;
	public static UserEditDao getInstance(){
		if (instance == null) {
			instance = new UserEditDao(); 
		}
		return instance;
	}
	JDBCUtil jdbc = JDBCUtil.getInstance();
	
	// 모든 회원 조회
	public List<Map<String, Object>> selectUser() {
		String sql =  "SELECT ROWNUM ,a.* "
				+ "      FROM (SELECT * "
				+ "              FROM P1_USER"
				+ "             WHERE USER_TYPE ='USER' "
				+ "             ORDER BY USER_NAME) a";
		return jdbc.selectList(sql);
	}
	
	// 상세정보
	public List<Map<String, Object>> selectUser1(int aa) {
		String sql = " SELECT a.* "
				+ "      FROM (SELECT ROWNUM RN ,a.* "
				+ "              FROM (SELECT * "
				+ "                     FROM P1_USER"
				+ "                    WHERE USER_TYPE ='USER' "
				+ "                    ORDER BY USER_NAME) a)a "
				+ "     WHERE RN = ? ";
		
		List<Object> param = new ArrayList<>();
		param.add(aa);
			
		return jdbc.selectList(sql,param);
	}
	
	
	// 삭제할 회원 조회
	public int deleteUser(int deleteNum) {
		String sql =  "DELETE P1_USER "
				+ "		WHERE USER_ID = (SELECT A.USER_ID"
								+ "        FROM (SELECT ROWNUM RN , a.* "
										+ "        FROM (SELECT *"
										+ "                FROM P1_USER"
										+ "               WHERE USER_TYPE ='USER' "
										+ "               ORDER BY USER_NAME) a ) A "
								    + "    WHERE A.RN = ?) ";
		List<Object> param = new ArrayList<>();
		param.add(deleteNum);
		
		return jdbc.update(sql, param);
	}
	
	
	
	
}
