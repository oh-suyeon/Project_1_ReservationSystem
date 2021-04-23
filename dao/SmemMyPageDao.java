package dao;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import controller.Controller;
import util.JDBCUtil;

public class SmemMyPageDao {

	private SmemMyPageDao(){}
	private static SmemMyPageDao instance;
	public static SmemMyPageDao getInstance(){
		if(instance == null){
			instance = new SmemMyPageDao();
		}
		return instance;
	}
	
	private JDBCUtil jdbc = JDBCUtil.getInstance();
	
	
	
	
	
	
	//시설 목록 리스트
	public List<Map<String, Object>> selectMyFacList() {
         String sql = "SELECT ROWNUM, FACILITY_NAME, FACILITY_ID "
        		 + " FROM P1_FACILITY "
        		 + " WHERE FACILITY_ALLOW = '승인' AND "
        		 + "USER_ID = ? ";
         List<Object> param1 = new ArrayList<>();
         param1.add(Controller.loginUser.get("USER_ID"));
         
		return jdbc.selectList(sql, param1);
	}

	
	
	
	
	
	//시설 상세보기 
	public Map<String, Object> selectMyFacViewList(String facId) {
		String sql = "SELECT ROWNUM RN, FACILITY_NAME, FACILITY_CONTENT, FACILITY_START_TIME, FACILITY_END_TIME, FACILITY_COMPANY_NUMBER, FACILITY_AM_PAY, FACILITY_PM_PAY, FACILITY_POSTCODE, FACILITY_ADDRESS, FACILITY_ADDRESS_DETAIL, FACILITY_PHONE_NUMBER, FACILITY_ALLOW "
			    +" FROM P1_FACILITY "
			    +" WHERE FACILITY_ID = ? ";
		

		List<Object> param1 = new ArrayList<>();
		param1.add(facId);
		
		return jdbc.selectOne(sql,param1);
	}
	
	
	
	
	
	
	
     //시설명 업데이트 
	public int viewNameupdate(String facId, String name){
		String sql = " UPDATE P1_FACILITY SET FACILITY_NAME = ?  "
				+" WHERE FACILITY_ID = ? ";
		
		
		List<Object> param = new ArrayList<>();
		param.add(name);
		param.add(facId);
		return jdbc.update(sql,param);
	}
	
	
	//시설내용 업데이트
	public int viewContentupdate(String facId, String content){
		String sql = "UPDATE P1_FACILITY SET FACILITY_CONTENT = ? "
				+"WHERE FACILITY_ID = ?";
		
		
		List<Object> param = new ArrayList<>();
		param.add(content);
		param.add(facId);
		return jdbc.update(sql,param);
	}
	
	//시설오픈시간 업데이트
	public int viewOpenTimeupdate(String facId, String openTime){
		String sql = "UPDATE P1_FACILITY SET FACILITY_START_TIME = ? "
				+"WHERE FACILITY_ID = ?";
		
		
		List<Object> param = new ArrayList<>();
		param.add(openTime);
		param.add(facId);
		return jdbc.update(sql,param);
	}
	
	
	
	//시설마감시간 업데이트
	public int viewCloseTimeupdate(String facId, String closeTime){
		String sql = "UPDATE P1_FACILITY SET FACILITY_END_TIME = ? "
				+"WHERE FACILITY_ID = ?";
		
		
		List<Object> param = new ArrayList<>();
		param.add(closeTime);
		param.add(facId);
		return jdbc.update(sql,param);
	}
	
	
	
	//시설오전요금 업데이트
	public int viewAMPayupdate(String facId, int AmPay){
		String sql = "UPDATE P1_FACILITY SET FACILITY_AM_PAY = ? "
				+"WHERE FACILITY_ID = ?";
		
		
		List<Object> param = new ArrayList<>();
		param.add(AmPay);
		param.add(facId);
		return jdbc.update(sql,param);
	}
	
	
	
	//시설오후요금 업데이트
	public int viewPMPayupdate(String facId, int PmPay){
		String sql = "UPDATE P1_FACILITY SET FACILITY_PM_PAY = ? "
				+"WHERE FACILITY_ID = ?";
		
		
		List<Object> param = new ArrayList<>();
		param.add(PmPay);
		param.add(facId);
		return jdbc.update(sql,param);
	}
	
	
	
	//시설우편번호 업데이트
	public int viewPostCodeupdate(String facId, int postNumber){
		String sql = "UPDATE P1_FACILITY SET FACILITY_POSTCODE = ? "
				+"WHERE FACILITY_ID = ?";
		
		
		List<Object> param = new ArrayList<>();
		param.add(postNumber);
		param.add(facId);
		return jdbc.update(sql,param);
	}
	
	
	
	//시설주소 업데이트
	public int viewAddressupdate(String facId, String address){
		String sql = "UPDATE P1_FACILITY SET FACILITY_ADDRESS = ? "
				+"WHERE FACILITY_ID = ?";
		
		
		List<Object> param = new ArrayList<>();
		param.add(address);
		param.add(facId);
		return jdbc.update(sql,param);
	}
	
	
	
	//시설상세주소 업데이트
	public int viewAddressDetailupdate(String facId, String address_detail){
		String sql = "UPDATE P1_FACILITY SET FACILITY_ADDRESS_Detail = ?  "
				+"WHERE FACILITY_ID = ?";
		
		
		List<Object> param = new ArrayList<>();
		param.add(address_detail);
		param.add(facId);
		return jdbc.update(sql,param);
	}
	
	
	
	//시설휴대번호 업데이트
	public int viewPhoneNumberupdate(String facId, String phoneNumber){
		String sql = "UPDATE P1_FACILITY SET FACILITY_PHONE_NUMBER = ? "
				+"WHERE FACILITY_ID = ?";

		List<Object> param = new ArrayList<>();
		param.add(phoneNumber);
		param.add(facId);
		return jdbc.update(sql,param);
	}
	
	
	//시설 삭제
	public int viewMyFacDelete(String facId){
		String sql = "DELETE FROM P1_FACILITY "
				+" WHERE FACILITY_ID = ?";
		List<Object> param = new ArrayList<>();
		param.add(facId);
		
		return jdbc.update(sql,param);
	}
	
	
	// 시설 세부 유형 리스트 
		public List<Map<String, Object>> facilityCodeList(String facilityLargeName){
			String sql = "SELECT FACILITY_SMALL_NAME, FACILITY_CODE "
					+ "FROM P1_FACILITY_CODE "
					+ "WHERE FACILITY_LARGE_NAME = ?";
			List<Object> param = new ArrayList<>();
			param.add(facilityLargeName);
			return jdbc.selectList(sql, param);
		}
	
		
	//지역 리스트 - 광역시
		public List<Map<String, Object>> regionLargeCodeList(){
			String sql = "SELECT REGION_LARGE_NAME "
					+ "FROM P1_REGION_CODE "
					+ "WHERE REGION_CODE LIKE '%01' ";
			return jdbc.selectList(sql);
		}
	
		
	// 지역 리스트 - 구
		public List<Map<String, Object>> regionSmallCodeList(int regionLargeCode){
			String sql = "SELECT REGION_SMALL_NAME, REGION_CODE "
					+ "FROM P1_REGION_CODE "
					+ "WHERE REGION_LARGE_CODE = ?";
			List<Object> param = new ArrayList<>();
			param.add(regionLargeCode);
			return jdbc.selectList(sql, param);
		}
		
	
		
		
		
	//등록 
		public int insertMyFac(String title, String content, String companyNumber, String openTime, String closeTime,
	               String amPay, String pmPay, String phoneNumber, String postNumber, String address,
	               String addressDetail, String select_facilityCode, String select_RegionCode){
			String sql = " INSERT INTO P1_FACILITY "
					+" VALUES ( 'FAC' || SEQ_FAC_ID.NEXTVAL , ? , ? , ? , ? , ? , ? , ? ,'승인대기', ? , ? , ? , ? , ? , ? , ? ) ";
			
			List<Object> param = new ArrayList<>();
			param.add(select_facilityCode);
			param.add(select_RegionCode);
			param.add(openTime);
			param.add(closeTime);
			param.add(title);
			param.add(content);
			param.add(companyNumber);
			param.add(amPay);
			param.add(pmPay);
			param.add(phoneNumber);
			param.add(postNumber);
			param.add(address);
			param.add(addressDetail);
			param.add(Controller.loginUser.get("USER_ID"));



			return jdbc.update(sql, param);
		
		}
	
	public int insertMyFac1(String title, String content, String companyNumber, String openTime, String closeTime,
            String amPay, String pmPay, String phoneNumber, String postNumber, String address,
            String addressDetail, String facilityCode, String regionSmallCode){
		String sql = " INSERT INTO P1_FACILITY "
			+" VALUES ( 'FAC' || SEQ_FAC_ID.NEXTVAL , ? , ? , ? , ? , ? , ? , ? ,'승인', ? , ? , ? , ? , ? , ? , ? ) ";
		
		List<Object> param = new ArrayList<>();
		param.add(facilityCode);
		param.add(regionSmallCode);
		param.add(openTime);
		param.add(closeTime);
		param.add(title);
		param.add(content);
		param.add(companyNumber);
		param.add(amPay);
		param.add(pmPay);
		param.add(phoneNumber);
		param.add(postNumber);
		param.add(address);
		param.add(addressDetail);
		param.add("MASTER");



		return jdbc.update(sql, param);
	
	}
	
	
	
	//등록 

	
	
	
	
}
