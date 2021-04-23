package dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import util.JDBCUtil;

public class FacilityViewDao {

	private FacilityViewDao(){}
	private static FacilityViewDao instance;
	public static FacilityViewDao getInstance(){
		if(instance == null){
			instance = new FacilityViewDao();
		}
		return instance;
	}
	
	private JDBCUtil jdbc = JDBCUtil.getInstance();
	
	// 시설 세부 유형 리스트 
	public List<Map<String, Object>> facilityCodeList(String facilityType){
		String sql = "SELECT FACILITY_SMALL_NAME, FACILITY_CODE "
				+ "	    FROM P1_FACILITY_CODE "
				+ "    WHERE FACILITY_LARGE_NAME = ?";
		List<Object> param = new ArrayList<>();
		param.add(facilityType);
		return jdbc.selectList(sql, param);
	}
	
	// 지역 리스트 - 광역시 
	public List<Map<String, Object>> regionLargeCodeList(){
		String sql = "SELECT REGION_LARGE_NAME "
				+ "FROM P1_REGION_CODE "
				+ "WHERE REGION_CODE LIKE '%01' ";
		return jdbc.selectList(sql);
	}
	
	// 지역 리스트 - 구
	public List<Map<String, Object>> regionSmallCodeList(int largeType){
		String sql = "SELECT REGION_SMALL_NAME, REGION_CODE "
				+ "FROM P1_REGION_CODE "
				+ "WHERE REGION_LARGE_CODE = ?";
		List<Object> param = new ArrayList<>();
		param.add(largeType);
		return jdbc.selectList(sql, param);
	}

	// 시설 유형  + 지역 조건에 따라 시설 리스트 조회
		public List<Map<String, Object>> facilityViewList(String facilityLargeCode, String facilityCode, int regionLargeCode, String regionCode){		
		
			String sql = null;
			List<Object> param = new ArrayList<>();
			
			if(facilityCode == null && regionLargeCode == 0 && regionCode == null) {		// 시설유형 X 광역시 X 구 X		
				sql = "SELECT ROWNUM RM, FACILITY_ID, FACILITY_NAME, FACILITY_ADDRESS "
						+ "FROM P1_FACILITY "
						+ "WHERE FACILITY_CODE LIKE ? || '%' "
						+ "AND FACILITY_ALLOW = '승인' ";
				param.add(facilityLargeCode);
			} else if(facilityCode == null && regionLargeCode != 0 && regionCode == null) {	// 시설유형 X 광역시 O 구 X
				sql = "SELECT ROWNUM RM, F.FACILITY_ID, F.FACILITY_NAME, F.FACILITY_ADDRESS "
						+ "FROM P1_FACILITY F, P1_REGION_CODE R "
						+ "WHERE F.REGION_CODE = R.REGION_CODE AND "
						+ "FACILITY_CODE LIKE ? || '%' AND "
						+ "R.REGION_LARGE_CODE = ? "
						+ "AND FACILITY_ALLOW = '승인' ";
				param.add(facilityLargeCode);
				param.add(regionLargeCode);
			} else if(facilityCode == null && regionLargeCode != 0 && regionCode != null) {	// 시설유형 X 광역시 O 구 O	
				sql = "SELECT ROWNUM RM, F.FACILITY_ID, F.FACILITY_NAME, F.FACILITY_ADDRESS "
						+ "FROM P1_FACILITY F, P1_REGION_CODE R "
						+ "WHERE F.REGION_CODE = R.REGION_CODE AND "
						+ "FACILITY_CODE LIKE ? || '%' AND "
						+ "R.REGION_LARGE_CODE = ? AND "
						+ "R.REGION_CODE = ? "
						+ "AND FACILITY_ALLOW = '승인' ";
				param.add(facilityLargeCode);
				param.add(regionLargeCode);
				param.add(regionCode);
			} else if(facilityCode != null && regionLargeCode == 0 && regionCode == null) {	// 시설유형 O 광역시 X 구 X	
				sql = "SELECT ROWNUM RM, FACILITY_ID, FACILITY_NAME, FACILITY_ADDRESS "
						+ "FROM P1_FACILITY "
						+ "WHERE FACILITY_CODE = ? "
						+ "AND FACILITY_ALLOW = '승인' ";
				param.add(facilityCode);
			} else if(facilityCode != null && regionLargeCode != 0 && regionCode == null) {	// 시설유형 O 광역시 O 구 X	
				sql = "SELECT ROWNUM RM, F.FACILITY_ID, F.FACILITY_NAME, F.FACILITY_ADDRESS "
						+ "FROM P1_FACILITY F, P1_REGION_CODE R "
						+ "WHERE F.REGION_CODE = R.REGION_CODE AND "
						+ "F.FACILITY_CODE = ? AND "
						+ "R.REGION_LARGE_CODE = ? "
						+ "AND FACILITY_ALLOW = '승인' ";
				param.add(facilityCode);
				param.add(regionLargeCode);
			} else {																		// 시설유형 O 광역시 O 구 O
				sql = "SELECT ROWNUM RM, F.FACILITY_ID, F.FACILITY_NAME, F.FACILITY_ADDRESS " 
						+ "FROM P1_FACILITY F, P1_REGION_CODE R "
						+ "WHERE F.REGION_CODE = R.REGION_CODE AND "
						+ "F.FACILITY_CODE = ? AND "
						+ "R.REGION_LARGE_CODE = ? AND "
						+ "R.REGION_CODE = ? "
						+ "AND FACILITY_ALLOW = '승인' ";
				param.add(facilityCode);
				param.add(regionLargeCode);
				param.add(regionCode);
			}
			return jdbc.selectList(sql, param);
		}
	
	// 시설 상세 보기 
	public Map<String, Object> facilityViewRead(String facilityCode){
		String sql = "SELECT FACILITY_ID, FACILITY_CODE, REGION_CODE, FACILITY_START_TIME, "
				+ "FACILITY_END_TIME, FACILITY_NAME, FACILITY_CONTENT, FACILITY_COMPANY_NUMBER, "
				+ "FACILITY_ALLOW, FACILITY_AM_PAY, FACILITY_PM_PAY, FACILITY_PHONE_NUMBER, "
				+ "FACILITY_POSTCODE, FACILITY_ADDRESS, NVL(FACILITY_ADDRESS_DETAIL, '-') FACILITYADDRESSDETAIL, USER_ID "
				+ "FROM P1_FACILITY "
				+ "WHERE FACILITY_ID = ?";
		List<Object> param = new ArrayList<>();
		param.add(facilityCode);
		return jdbc.selectOne(sql, param);
	}
	
	// 시설 상세 보기 - 코멘트
	public List<Map<String, Object>> facilityViewComments(String facilityCode){
		String sql = "SELECT ROWNUM RN, A.COMMENTS_NO, A.COMMENTS_CONTENT, A.USER_ID "
				+ "FROM (SELECT C.COMMENTS_NO, C.COMMENTS_CONTENT, C.USER_ID "
				+ "FROM P1_FACILITY F, P1_COMMENTS C "
				+ "WHERE F.FACILITY_ID = C.FACILITY_ID AND "
				+ "F.FACILITY_ID = ? "
				+ "ORDER BY SUBSTR(COMMENTS_NO, 4)) A";
		List<Object> param = new ArrayList<>();
		param.add(facilityCode);
		return jdbc.selectList(sql, param);
	}
	
	public int viewNameupdate(String facilityCode, String name) {
		String sql = " UPDATE P1_FACILITY SET FACILITY_NAME = ?  "
				+" WHERE FACILITY_ID = ? ";
		
		
		List<Object> param = new ArrayList<>();
		param.add(name);
		param.add(facilityCode);
		return jdbc.update(sql,param);
	}

	public int viewContentupdate(String facilityCode, String content) {
		String sql = "UPDATE P1_FACILITY SET FACILITY_CONTENT = ? "
				+"WHERE FACILITY_ID = ?";
		
		
		List<Object> param = new ArrayList<>();
		param.add(content);
		param.add(facilityCode);
		return jdbc.update(sql,param);
	}

	public int viewOpenTimeupdate(String facilityCode, String openTime) {
		String sql = "UPDATE P1_FACILITY SET FACILITY_START_TIME = ? "
				+"WHERE FACILITY_ID = ?";
		
		
		List<Object> param = new ArrayList<>();
		param.add(openTime);
		param.add(facilityCode);
		return jdbc.update(sql,param);
	}
	public int viewCloseTimeupdate(String facilityCode, String closeTime) {
		String sql = "UPDATE P1_FACILITY SET FACILITY_END_TIME = ? "
				+"WHERE FACILITY_ID = ?";
		
		
		List<Object> param = new ArrayList<>();
		param.add(closeTime);
		param.add(facilityCode);
		return jdbc.update(sql,param);
	}

	public int viewAMPayupdate(String facilityCode, int AmPay) {
		String sql = "UPDATE P1_FACILITY SET FACILITY_AM_PAY = ? "
				+"WHERE FACILITY_ID = ?";
		
		
		List<Object> param = new ArrayList<>();
		param.add(AmPay);
		param.add(facilityCode);
		return jdbc.update(sql,param);
	}

	public int viewPMPayupdate(String facilityCode, int PmPay) {
		String sql = "UPDATE P1_FACILITY SET FACILITY_PM_PAY = ? "
				+"WHERE FACILITY_ID = ?";
		
		
		List<Object> param = new ArrayList<>();
		param.add(PmPay);
		param.add(facilityCode);
		return jdbc.update(sql,param);
	}

	public int viewPostCodeupdate(String facilityCode, int postNumber) {
		String sql = "UPDATE P1_FACILITY SET FACILITY_POSTCODE = ? "
				+"WHERE FACILITY_ID = ?";
		
		
		List<Object> param = new ArrayList<>();
		param.add(postNumber);
		param.add(facilityCode);
		return jdbc.update(sql,param);
	}

	public int viewAddressupdate(String facilityCode, String address) {
		String sql = "UPDATE P1_FACILITY SET FACILITY_ADDRESS = ? "
				+"WHERE FACILITY_ID = ?";
		
		
		List<Object> param = new ArrayList<>();
		param.add(address);
		param.add(facilityCode);
		return jdbc.update(sql,param);
	}

	public int viewAddressDetailupdate(String facilityCode,String address_detail) {
		String sql = "UPDATE P1_FACILITY SET FACILITY_ADDRESS_Detail = ?  "
				+"WHERE FACILITY_ID = ?";
		
		
		List<Object> param = new ArrayList<>();
		param.add(address_detail);
		param.add(facilityCode);
		return jdbc.update(sql,param);
	}
	
	public int viewPhoneNumberupdate(String facilityCode, String phoneNumber) {
		String sql = "UPDATE P1_FACILITY SET FACILITY_PHONE_NUMBER = ? "
				+"WHERE FACILITY_ID = ?";

		List<Object> param = new ArrayList<>();
		param.add(phoneNumber);
		param.add(facilityCode);
		return jdbc.update(sql,param);
	}


	public int viewfacilityCodeUpdate(String facilityCode, String facility_select_code) {
			String sql = "UPDATE P1_FACILITY SET FACILITY_CODE = ?  "
					+"WHERE FACILITY_ID = ?";
			
			
			List<Object> param = new ArrayList<>();
			param.add(facility_select_code);
			param.add(facilityCode);
			return jdbc.update(sql,param);
	}
	
	
	public int viewRegionCodeUpdate(String facilityCode, String facility_region_code) {
		String sql = "UPDATE P1_FACILITY SET REGION_CODE = ?  "
				+"WHERE FACILITY_ID = ?";
		
		
		List<Object> param = new ArrayList<>();
		param.add(facility_region_code);
		param.add(facilityCode);
		return jdbc.update(sql,param);
}

	public int viewMyFacDelete(String facilityCode) {
		String sql = "DELETE FROM P1_FACILITY "
				+" WHERE FACILITY_ID = ?";
		List<Object> param = new ArrayList<>();
		param.add(facilityCode);
		
		return jdbc.update(sql,param);
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}