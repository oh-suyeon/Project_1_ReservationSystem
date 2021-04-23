package dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import controller.Controller;
import util.JDBCUtil;

	public class FacilityReservationDao {
	
	private FacilityReservationDao(){}
	
	private static FacilityReservationDao instance;
	
	public static FacilityReservationDao getInstance(){
		if(instance == null){
			instance = new FacilityReservationDao();
		}
		return instance;
	}
	
	private JDBCUtil jdbc = JDBCUtil.getInstance();
	
	// 예약 결제 정보 테이블에 업데이트하기  
	public int facilityReservationUpdate(Map<String, Object> p){ // 들어오는 값은 2 3 4 5 8 10
			
		//시설 아이디로 요금 구해 p에 넣기
		Map<String, Object> row = facilityFee((String)p.get("FACILITY_ID"));
		if(p.get("RESERVATION_AMPM") == "AM"){
			p.put("RESERVATION_PAY_PEE", row.get("FACILITY_AM_PAY"));
		} else {
			p.put("RESERVATION_PAY_PEE", row.get("FACILITY_PM_PAY"));
		}
			
		String sql = "INSERT INTO P1_RESERVATION VALUES ('RES' || SEQ_P1_RESERVATION.NEXTVAL, "
														  + "TO_DATE(?, 'YYYY-MM-DD'), ?, ?, ?, ?, SYSDATE, ?, '승인대기', ?)";
		
		List<Object> param = new ArrayList<>();
		param.add(p.get("RESERVATION_DATE"));//2
		param.add(p.get("USER_ID"));//3
		param.add(p.get("FACILITY_ID"));//4
		param.add(p.get("RESERVATION_REFUND_ACCOUNT"));//5
		param.add(p.get("RESERVATION_PAY_PEE"));//6
		param.add(p.get("PAYTYPE_CODE"));//8
		param.add(p.get("RESERVATION_AMPM"));//10
			
		return jdbc.update(sql, param);
	}
		
	
	
	// 예약 결제 정보 테이블에 요금 정보 가져오기
	public Map<String, Object> facilityFee(String facilityID){
		String sql = "SELECT FACILITY_AM_PAY, FACILITY_PM_PAY "
				+ "FROM P1_FACILITY "
				+ "WHERE FACILITY_ID = ? ";
		List<Object> param = new ArrayList<>();
		param.add(facilityID);
		return jdbc.selectOne(sql, param);
	}
		
	
	
	// 예약 스케줄 확인하기(시설아이디와 입력 날짜 입력 -> 오전/오후 예약 정보를 가져오기 - 없을 수도 있을 수도 있음) 
	public List<Map<String, Object>> facilityReservationSchedule(String reservationDate, String facilityID){
		String sql = "SELECT RESERVATION_DATE, RESERVATION_AMPM "
				+ "FROM P1_RESERVATION "
				+ "WHERE RESERVATION_DATE LIKE TO_DATE(?, 'YYYY/MM/DD') || '%' AND "
				+ "FACILITY_ID = ? ";
		List<Object> param = new ArrayList<>();
		param.add(reservationDate);
		param.add(facilityID);
		return jdbc.selectList(sql, param);
	}
		
	
	
	// 예약 중복을 막기 위해 예약 상태 '승인 대기' 또는 '승인' 예약 정보가 있는 회원인지 검사
	public Map<String, Object> facilityReservationOverlapCheck(Object userID){
		String sql = "SELECT USER_ID, RESERVATION_STATE "
				+ "FROM P1_RESERVATION "
				+ "WHERE USER_ID = ? AND "
				+ "(RESERVATION_STATE = '승인대기' OR "
				+ "RESERVATION_STATE = '승인') ";
		List<Object> param = new ArrayList<>();
		param.add(userID);
		return jdbc.selectOne(sql, param);
	}



	public int reservationDie(String resNo) {
		String sql = " UPDATE P1_RESERVATION "
				+ "       SET RESERVATION_STATE = '다이', RESERVATION_AMPM = '0' "
				+ "     WHERE RESERVATION_NO = (SELECT RESERVATION_NO  "
				+ "                               FROM P1_RESERVATION "
				+ "                              WHERE USER_ID = ? AND "
				+ "										RESERVATION_NO = ?) ";
		List<Object> param = new ArrayList<>();
		param.add(Controller.loginUser.get("USER_ID"));
		param.add(resNo);
		
		int result = jdbc.update(sql, param);
		return result;
		
	}



	public int reservFinish() {
		String sql = " UPDATE P1_RESERVATION "
				+ " SET RESERVATION_STATE = '대관완료' "
				+ " WHERE SYSDATE >= RESERVATION_DATE";
		return jdbc.update(sql);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
	
	
	