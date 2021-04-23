package dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import util.JDBCUtil;

public class ManagerFACSetDao {
	
	private ManagerFACSetDao(){}
	private static ManagerFACSetDao instance;
	public static ManagerFACSetDao getInstance(){
		if(instance == null){
			instance = new ManagerFACSetDao();
		}
		return instance;
	}
	
	private JDBCUtil jdbc = JDBCUtil.getInstance();
	
	
	public List<Map<String, Object>> AllowFacList() {
		String sql = "SELECT ROWNUM, FACILITY_ID, FACILITY_NAME, FACILITY_ALLOW "
				+ " FROM P1_FACILITY "
				+ " WHERE FACILITY_ALLOW = '승인대기' OR FACILITY_ALLOW = '비승인'";
		
		return jdbc.selectList(sql);
	}


	public Map<String, Object> selectAllowViewList(String allowfacId) {
		String sql = "SELECT FACILITY_NAME, FACILITY_CONTENT, FACILITY_START_TIME, FACILITY_END_TIME, "
				+ " FACILITY_COMPANY_NUMBER, FACILITY_AM_PAY, FACILITY_PM_PAY, "
				+ " FACILITY_POSTCODE, FACILITY_ADDRESS, FACILITY_ADDRESS_DETAIL, "
				+ " FACILITY_PHONE_NUMBER, FACILITY_ALLOW "
			    +"  FROM P1_FACILITY "
			    +" WHERE FACILITY_ID = ? ";
		
		List<Object> param = new ArrayList<>();
		param.add(allowfacId);
		
		return jdbc.selectOne(sql, param);
	}
	
	
	public int viewAllowUpdate(String allowfacId){
		String sql = " UPDATE P1_FACILITY SET FACILITY_ALLOW = '승인' "
				+" WHERE FACILITY_ID = ? ";
		
		List<Object> param = new ArrayList<>();
		param.add(allowfacId);
		
		return jdbc.update(sql,param);
	}
	
	public int viewNonAllowUpdate(String allowfacId){
		String sql = " UPDATE P1_FACILITY SET FACILITY_ALLOW = '비승인' "
				+" WHERE FACILITY_ID = ? ";
		
		List<Object> param = new ArrayList<>();
		param.add(allowfacId);
		return jdbc.update(sql,param);
	}

}