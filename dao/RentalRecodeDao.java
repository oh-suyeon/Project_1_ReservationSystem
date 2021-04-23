package dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import util.JDBCUtil;

	public class RentalRecodeDao {

	private RentalRecodeDao(){}
	
	private static RentalRecodeDao instance;
	
	public static RentalRecodeDao getInstance(){
		if(instance == null){
			instance = new RentalRecodeDao();
		}
		return instance;
	}
	
	private JDBCUtil jdbc = JDBCUtil.getInstance();
	
	// 대관완료한 시설 목록
	public List<Map<String, Object>> recodeListO(String userID){		 
		String sql = "SELECT ROWNUM RN, D.FACILITY_ID, D.FACILITY_NAME, D.RESERVATION_DATE, D.RESERVATION_STATE, D.COMMENTSCONTENT "
				   + "FROM (SELECT B.FACILITY_ID, B.FACILITY_NAME, B.RESERVATION_DATE, B.RESERVATION_STATE, NVL(B.COMMENTS_CONTENT, '-') COMMENTSCONTENT "
				   + "		FROM (SELECT A.FACILITY_ID, A.FACILITY_NAME, A.RESERVATION_DATE, A.RESERVATION_STATE, C.COMMENTS_CONTENT "
				   + "			  FROM (SELECT F.FACILITY_ID, F.FACILITY_NAME, R.RESERVATION_DATE, R.RESERVATION_STATE, R.USER_ID "
				   + "					FROM P1_RESERVATION R, P1_FACILITY F "
				   + "					WHERE R.FACILITY_ID = F.FACILITY_ID AND "
				   + "					R.USER_ID = ? AND "
				   + "					R.RESERVATION_STATE = '대관완료') A, P1_COMMENTS C "
				   + "			  WHERE A.FACILITY_ID = C.FACILITY_ID(+) AND A.USER_ID = C.USER_ID(+)) B "
				   + "		ORDER BY B.RESERVATION_DATE DESC) D ";
		List<Object> param = new ArrayList<>();
		param.add(userID);
		return jdbc.selectList(sql, param);
	}
	
	// 예약취소한 시설 목록
	public List<Map<String, Object>> recodeListX(String userID){		 
		String sql = "SELECT ROWNUM RN, D.FACILITY_ID, D.FACILITY_NAME, D.RESERVATION_DATE, D.RESERVATION_STATE, D.COMMENTSCONTENT "
				   + "FROM (SELECT B.FACILITY_ID, B.FACILITY_NAME, B.RESERVATION_DATE, B.RESERVATION_STATE, NVL(B.COMMENTS_CONTENT, '-') COMMENTSCONTENT "
				   + "		FROM (SELECT A.FACILITY_ID, A.FACILITY_NAME, A.RESERVATION_DATE, A.RESERVATION_STATE, C.COMMENTS_CONTENT "
				   + "			  FROM (SELECT F.FACILITY_ID, F.FACILITY_NAME, R.RESERVATION_DATE, R.RESERVATION_STATE, R.USER_ID "
				   + "					FROM P1_RESERVATION R, P1_FACILITY F "
				   + "					WHERE R.FACILITY_ID = F.FACILITY_ID AND "
				   + "					R.USER_ID = ? AND "
				   + "					R.RESERVATION_STATE = '예약취소') A, P1_COMMENTS C "
				   + "			  WHERE A.FACILITY_ID = C.FACILITY_ID(+) AND A.USER_ID = C.USER_ID(+)) B "
				   + "		ORDER BY B.RESERVATION_DATE DESC) D ";
		List<Object> param = new ArrayList<>();
		param.add(userID);
		return jdbc.selectList(sql, param);
	}
	
	// 코멘트 입력하기
	public int insertComment(String commentsContent, String userID, String facilityID){
		String sql = "INSERT INTO P1_COMMENTS VALUES ('COM' || SEQ_P1_COMMENTS.NEXTVAL, ?, ?, ?)";
		List<Object> param = new ArrayList<>();
		param.add(commentsContent);
		param.add(userID);
		param.add(facilityID);
		return jdbc.update(sql, param);
	}
	
}