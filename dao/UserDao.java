package dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import controller.Controller;
import util.JDBCUtil;

public class UserDao {
	
	private UserDao(){}
	private static UserDao instance;
	public static UserDao getInstance(){
		if(instance == null){
			instance = new UserDao();
		}
		return instance;
	}
	
	private JDBCUtil jdbc = JDBCUtil.getInstance();
	
	public int insertUser(Map<String, Object> param){
		String sql = "INSERT INTO P1_USER VALUES (?, ?, ?, ?, ?, ?, ?, ?,?) ";
		
		List<Object> p = new ArrayList<>();
		p.add(param.get("USER_TYPE"));
		p.add(param.get("USER_ID"));
		p.add(param.get("USER_PASSWORD"));
		p.add(param.get("USER_NAME"));
		p.add(param.get("USER_PHONENUMBER"));
		p.add(param.get("USER_POSTCODE"));
		p.add(param.get("USER_ADDRESS"));
		p.add(param.get("USER_ADDRESS_DETAIL"));
		p.add("0");
		
		return jdbc.update(sql, p);
	}
	
	public int updateJoinUser(Map<String, Object> param){
		String sql = "UPDATE P1_USER "
				+ "		 SET USER_TYPE = ?,"
				+ "          USER_PASSWORD = ?,"
				+ "          USER_NAME = ?,"
				+ "          USER_PHONENUMBER = ?,"
				+ "          USER_POSTCODE = ?, "
				+ "          USER_ADDRESS = ?,"
				+ "          USER_ADDRESS_DETAIL = ?,"
				+ "			 USER_DELETE = ? "
				+ "    WHERE USER_ID = ?";
				
		List<Object> pa = new ArrayList<>();
		pa.add(param.get("USER_TYPE"));
		pa.add(param.get("USER_PASSWORD"));
		pa.add(param.get("USER_NAME"));
		pa.add(param.get("USER_PHONENUMBER"));
		pa.add(param.get("USER_POSTCODE"));
		pa.add(param.get("USER_ADDRESS"));
		pa.add(param.get("USER_ADDRESS_DETAIL"));
		pa.add("0");
		pa.add(param.get("USER_ID"));
		int result = jdbc.update(sql, pa);
		
        return result;
	}
	

	public Map<String, Object> selectUser(String userId, String password) {
		String sql = "SELECT USER_TYPE, USER_ID , USER_PASSWORD, USER_NAME, USER_DELETE"
				+ "     FROM P1_USER "
				+ "    WHERE USER_ID = ? "
				+ "      AND USER_PASSWORD = ? ";
		List<Object> param = new ArrayList<>();
		param.add(userId);
		param.add(password);
		
		return jdbc.selectOne(sql,param);
	}
	
	
	public Map<String, Object> loginuser(String userId, String password) {
		String sql = "SELECT *"
				+ "     FROM P1_USER "
				+ "    WHERE USER_ID = ? "
				+ "      AND USER_PASSWORD = ? ";
		List<Object> param = new ArrayList<>();
		param.add(userId);
		param.add(password);
		
		return jdbc.selectOne(sql,param);
	}
	
	
	public Map<String, Object> selectUser2(String userId) {
		String sql = "SELECT USER_TYPE, USER_ID , USER_PASSWORD, USER_NAME, USER_DELETE"
				+ "     FROM P1_USER "
				+ "    WHERE USER_ID = ? ";
		List<Object> param = new ArrayList<>();
		param.add(userId);
		
		return jdbc.selectOne(sql,param);
	}
	

	
	
	public Map<String, Object> manager() {
		String sql = "SELECT * "
				+ " FROM P1_MANAGER";	
		return jdbc.selectOne(sql);
	}

	public Map<String, Object> checkId(String userId) {
		String sql1 = "SELECT USER_ID, USER_DELETE"
				+ "      FROM P1_USER "
				+ "     WHERE USER_ID = ? ";
		
		List<Object> param1 = new ArrayList<>();
		param1.add(userId);
		
		return jdbc.selectOne(sql1,param1);
	}
	
	public Map<String, Object> checkIdDelete(String userId) {
		String sql1 = "SELECT USER_DELETE"
				+ "      FROM P1_USER "
				+ "     WHERE USER_ID = ? ";
		
		List<Object> param1 = new ArrayList<>();
		param1.add(userId);
		
		return jdbc.selectOne(sql1,param1);
	}


	
	
	public Map<String, Object> selectUserInfo(String userId, String password) {
		String sql = "SELECT *"
				+ "     FROM P1_USER "
				+ "    WHERE USER_ID = ? "
				+ "      AND USER_PASSWORD = ? ";
		List<Object> param = new ArrayList<>();
		param.add(userId);
		param.add(password);
		
		return jdbc.selectOne(sql,param);
	}

	public int deleteUser() {
		String sql = " UPDATE P1_USER SET USER_DELETE = 1 "
				+ "    WHERE USER_ID  = ?";
		List<Object> param = new ArrayList<>();
		param.add(Controller.loginUser.get("USER_ID"));
		
		int result = jdbc.update(sql, param);
		return result;
		
	}

	public int updateuserInfo1(String typeC) {
		String sql = "UPDATE P1_USER"
				+ "      SET USER_TYPE = ?"
				+ "    WHERE USER_ID = ?";
		List<Object> param = new ArrayList<>();
		param.add(typeC);
		param.add(Controller.loginUser.get("USER_ID"));
		
		
		int result = jdbc.update(sql, param);
		return result;
	}
	
	
	public int updateuserInfo2(String password) {
		String sql = "UPDATE P1_USER"
				+ "      SET USER_PASSWORD = ?"
				+ "    WHERE USER_ID = ?";
		List<Object> param = new ArrayList<>();
		param.add(password);
		param.add(Controller.loginUser.get("USER_ID"));
		
		
		int result = jdbc.update(sql, param);
		return result;
	}
	
	public int updateuserInfo3(String NAME) {
		String sql = "UPDATE P1_USER"
				+ "      SET USER_NAME = ?"
				+ "    WHERE USER_ID = ?";
		List<Object> param = new ArrayList<>();
		param.add(NAME);
		param.add(Controller.loginUser.get("USER_ID"));
		
		
		int result = jdbc.update(sql, param);
		return result;
	}
	
	public int updateuserInfo4(String phone) {
		String sql = "UPDATE P1_USER"
				+ "      SET USER_PHONENUMBER = ?"
				+ "    WHERE USER_ID = ?";
		List<Object> param = new ArrayList<>();
		param.add(phone);
		param.add(Controller.loginUser.get("USER_ID"));
		
		
		int result = jdbc.update(sql, param);
		return result;
	}
	
	public int updateuserInfo5(String post, String add, String detail) {
		String sql = "UPDATE P1_USER"
				+ "      SET USER_POSTCOME = ?"
				+ "      SET USER_ADDRESS = ?"
				+ "      SET USER_ADDRESS_DETAIL = ?"
				+ "    WHERE USER_ID = ?";
		List<Object> param = new ArrayList<>();
		param.add(post);
		param.add(add);
		param.add(detail);
		param.add(Controller.loginUser.get("USER_ID"));
		
		
		int result = jdbc.update(sql, param);
		return result;
	}

	public List<Map<String, Object>> selectReservation1() {
		String sql = " SELECT B.FACILITY_NAME Q,"
				+ "           A.RESERVATION_DATE,"
				+ "           A.RESERVATION_AMPM,"
				+ "           A.RESERVATION_PAY_PEE,"	
				+ "           A.RESERVATION_STATE,"
				+ "           RESERVATION_REFUND_ACCOUNT, "
				+ "			  A.RESERVATION_NO "
				+ "      FROM P1_RESERVATION A INNER JOIN P1_FACILITY B ON(A.FACILITY_ID = B.FACILITY_ID)"
				+ "     WHERE SYSDATE < A.RESERVATION_DATE"
				+ "       AND ( RESERVATION_STATE = '승인대기' OR RESERVATION_STATE = '승인') ";
 
		return jdbc.selectList(sql);
	}

	public int changeReservation() {
		String sql = " UPDATE P1_RESERVATION SET RESERVATION_STATE = '예약취소', RESERVATION_AMPM = '0' "
				+ "    WHERE RESERVATION_NO = (SELECT RESERVATION_NO "
				+ "                              FROM P1_RESERVATION "
				+ "                             WHERE USER_ID = ? "
				+ "                               and ( RESERVATION_STATE = '승인대기' OR RESERVATION_STATE = '승인') )";
		List<Object> param = new ArrayList<>();
		param.add(Controller.loginUser.get("USER_ID"));
		
		int result = jdbc.update(sql, param);
		return result;
	}

	public String selectFacID() {
		String sql = "SELECT B.FACILITY_ID Q "
				+ "     FROM P1_RESERVATION A INNER JOIN P1_FACILITY B ON(A.FACILITY_ID = B.FACILITY_ID) "
				+ "    WHERE SYSDATE < A.RESERVATION_DATE";
		
		Map<String, Object> a = jdbc.selectOne(sql);
		String aa = (String) a.get("Q");
		return aa;
	}

	public  List<Map<String, Object>> selectReservationList1() {
		String sql = " SELECT A.* "
				+ "      FROM (SELECT ROWNUM RN, A.*"
				+ "              FROM (SELECT  B.FACILITY_NAME Q, "
				+ "	 							A.RESERVATION_DATE W,"
				+ "   							A.RESERVATION_AMPM E,"
				+ "								C.PAYTYPE_NAME R,"
				+ "								A.RESERVATION_PAY_DATE T,"
				+ "								 A.RESERVATION_STATE Y"
				+ "						 FROM P1_RESERVATION A INNER JOIN P1_FACILITY B ON(A.FACILITY_ID = B.FACILITY_ID)"
				+ "                                            INNER JOIN P1_PAYTYPE C ON(A.PAYTYPE_CODE = C.PAYTYPE_CODE)"
				+ "                     WHERE B.USER_ID = ? "
				+ "                       and (a.RESERVATION_STATE = '승인대기' OR a.RESERVATION_STATE = '승인')"
				+ "                     ORDER BY A.RESERVATION_DATE)A)A"; 
				
		List<Object> param = new ArrayList<>();
		param.add(Controller.loginUser.get("USER_ID"));
//		param.add("승인대기");
//		param.add("승인");
		
		return jdbc.selectList(sql, param);
	}
	public  List<Map<String, Object>> selectReservationList2() {
		String sql = " SELECT A.* "
				+ "      FROM (SELECT ROWNUM RN, A.*"
				+ "              FROM (SELECT  B.FACILITY_NAME Q, "
				+ "	 							A.RESERVATION_DATE W,"
				+ "   							A.RESERVATION_AMPM E,"
				+ "								C.PAYTYPE_NAME R,"
				+ "								A.RESERVATION_PAY_DATE T,"
				+ "								 A.RESERVATION_STATE Y"
				+ "						 FROM P1_RESERVATION A INNER JOIN P1_FACILITY B ON(A.FACILITY_ID = B.FACILITY_ID)"
				+ "                                            INNER JOIN P1_PAYTYPE C ON(A.PAYTYPE_CODE = C.PAYTYPE_CODE)"
				+ "                     WHERE B.USER_ID = ? "
				+ "                       and a.RESERVATION_STATE = ? "
				+ "                     ORDER BY A.RESERVATION_DATE)A)A"; 
		List<Object> param = new ArrayList<>();
		param.add(Controller.loginUser.get("USER_ID"));
		param.add("예약취소");
		
		return jdbc.selectList(sql, param);
	}
	public  List<Map<String, Object>> selectReservationList3() {
		String sql = " SELECT A.* "
				+ "      FROM (SELECT ROWNUM RN, A.*"
				+ "              FROM (SELECT  B.FACILITY_NAME Q, "
				+ "	 							A.RESERVATION_DATE W,"
				+ "   							A.RESERVATION_AMPM E,"
				+ "								C.PAYTYPE_NAME R,"
				+ "								A.RESERVATION_PAY_DATE T,"
				+ "								 A.RESERVATION_STATE Y"
				+ "						 FROM P1_RESERVATION A INNER JOIN P1_FACILITY B ON(A.FACILITY_ID = B.FACILITY_ID)"
				+ "                                            INNER JOIN P1_PAYTYPE C ON(A.PAYTYPE_CODE = C.PAYTYPE_CODE)"
				+ "                     WHERE B.USER_ID = ? "
				+ "                       and a.RESERVATION_STATE = ? "
				+ "                     ORDER BY A.RESERVATION_DATE)A)A"; 
		List<Object> param = new ArrayList<>();
		param.add(Controller.loginUser.get("USER_ID"));
		param.add("대관완료");
		
		return jdbc.selectList(sql, param);
	}

	// 승인대기-> 승인
	public int changeReservationState1(int input3) {
		String sql = "UPDATE P1_RESERVATION SET RESERVATION_STATE = '승인' "
				+ " WHERE RESERVATION_NO = (SELECT A.N "
				+ " FROM( SELECT ROWNUM RN, A.* "
				+ " FROM (SELECT  B.FACILITY_NAME Q, "
				+ " A.RESERVATION_DATE W, "
				+ " A.RESERVATION_AMPM E, "
				+ " C.PAYTYPE_NAME R, "
				+ " A.RESERVATION_PAY_DATE T, "
				+ " A.RESERVATION_NO N "
				+ " FROM P1_RESERVATION A INNER JOIN P1_FACILITY B ON(A.FACILITY_ID = B.FACILITY_ID) "
				+ " INNER JOIN P1_PAYTYPE C ON(A.PAYTYPE_CODE = C.PAYTYPE_CODE) "
				+ " WHERE B.USER_ID = ? "
				+ " and( a.RESERVATION_STATE = '승인대기' "
				+ " OR a.RESERVATION_STATE = '승인' ) "
				+ " ORDER BY A.RESERVATION_DATE)A)A "
				+ " WHERE A.RN = ?) ";
		
		List<Object> param = new ArrayList<>();
		param.add(Controller.loginUser.get("USER_ID"));
		param.add(input3);
		
		return jdbc.update(sql, param);
	}
	
	public int changeReservationState2(int input4) {
		String sql = "UPDATE P1_RESERVATION SET RESERVATION_STATE = '예약취소', RESERVATION_AMPM = 'X' "
				+ " WHERE RESERVATION_NO = (SELECT A.N "
				+ " FROM( SELECT ROWNUM RN, A.* "
				+ " FROM (SELECT  B.FACILITY_NAME Q, "
				+ " A.RESERVATION_DATE W, "
				+ " A.RESERVATION_AMPM E, "
				+ " C.PAYTYPE_NAME R, "
				+ " A.RESERVATION_PAY_DATE T, "
				+ " A.RESERVATION_NO N "
				+ " FROM P1_RESERVATION A INNER JOIN P1_FACILITY B ON(A.FACILITY_ID = B.FACILITY_ID) "
				+ " INNER JOIN P1_PAYTYPE C ON(A.PAYTYPE_CODE = C.PAYTYPE_CODE) "
				+ " WHERE B.USER_ID = ? "
				+ " and( a.RESERVATION_STATE = '승인대기' "
				+ " OR a.RESERVATION_STATE = '승인' ) "
				+ " ORDER BY A.RESERVATION_DATE)A)A "
				+ " WHERE A.RN = ?) ";
		

		List<Object> param = new ArrayList<>();
		param.add(Controller.loginUser.get("USER_ID"));
		param.add(input4);
		
		return jdbc.update(sql, param);
	}

	public List<Map<String, Object>> selectOwnerMoney() {
		String sql = " SELECT TO_CHAR(TO_DATE(A.MONTH1, 'YYYY/MM'), 'YYYY/MM') MONTH2, A.FACILITY_ID, A.FACILITY_NAME, A.SALES "
				+ "      FROM (SELECT TO_CHAR(R.RESERVATION_DATE, 'YYYYMM') MONTH1, R.FACILITY_ID, F.FACILITY_NAME, SUM(RESERVATION_PAY_PEE) SALES"
				+ "              FROM P1_FACILITY F, P1_RESERVATION R, P1_USER U"
				+ "             WHERE U.USER_ID = F.USER_ID AND"
				+ "                   F.FACILITY_ID = R.FACILITY_ID AND"
				+ "                   U.USER_ID = ? "
				+ "             GROUP BY TO_CHAR(R.RESERVATION_DATE, 'YYYYMM'), R.FACILITY_ID, F.FACILITY_NAME) A"
				+ "     ORDER BY TO_CHAR(TO_DATE(A.MONTH1, 'YYYY/MM'), 'YYYY/MM'), TO_NUMBER(SUBSTR(A.FACILITY_ID, 4))";
		List<Object> param = new ArrayList<>();
		param.add(Controller.loginUser.get("USER_ID"));
		return jdbc.selectList(sql, param);
	}

	public  List<Map<String, Object>> choiseMonth(int input1) {
		String sql = "SELECT TO_CHAR(TO_DATE(A.MONTH1, 'YYYY/MM'), 'YYYY/MM') MONTH2, A.FACILITY_ID, A.FACILITY_NAME, A.SALES "
				+ " FROM (SELECT TO_CHAR(R.RESERVATION_DATE, 'YYYYMM') MONTH1, R.FACILITY_ID, F.FACILITY_NAME, SUM(RESERVATION_PAY_PEE) SALES "
				+ " FROM P1_FACILITY F, P1_RESERVATION R, P1_USER U "
				+ " WHERE U.USER_ID = F.USER_ID AND "
				+ " F.FACILITY_ID = R.FACILITY_ID AND "
				+ " U.USER_ID = ? AND  "
				+ " TO_CHAR(R.RESERVATION_DATE, 'YYYYMM') = ? "
				+ " GROUP BY TO_CHAR(R.RESERVATION_DATE, 'YYYYMM'), R.FACILITY_ID, F.FACILITY_NAME) A "
				+ " ORDER BY TO_NUMBER(SUBSTR(A.FACILITY_ID, 4)) ";
				
					      
		List<Object> param = new ArrayList<>();
		param.add(Controller.loginUser.get("USER_ID"));
		param.add(input1);
		
		return jdbc.selectList(sql, param);			      
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
