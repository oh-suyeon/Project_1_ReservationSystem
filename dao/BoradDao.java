package dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;
import java.util.Map;






import controller.Controller;
import util.JDBCUtil;


public class BoradDao {

	private BoradDao(){};
	private static BoradDao instance;
	public static BoradDao getInstance(){
		if (instance == null) {
			instance = new BoradDao();
		}
		return instance;
	}
	JDBCUtil jdbc = JDBCUtil.getInstance();

	//게시판 목록 불러오기
	public List<Map<String, Object>> selectBoardList() {
		String sql = "SELECT BOARD_NO, SUBJECT, TITLE, USER_ID, REG_DATE "
				+ " FROM P1_BOARD"
				+ " ORDER BY BOARD_NO DESC";
		// 작성일 월/일로 출력하기
		return jdbc.selectList(sql);
	}
	
	// 게시판 글 등록하기
	public int insertBoardList(String subject, String title, String content){
		String sql = "INSERT INTO P1_BOARD VALUES"
				+ " (P1_BOARD_NO_SEQ.NEXTVAL,?,?,?,SYSDATE,?)";
		List<Object> param = new ArrayList<>();
		param.add(subject);
		param.add(title);
		param.add(content);
		param.add(Controller.loginUser.get("USER_ID"));	
		
		return jdbc.update(sql, param);
	}
	
	// 게시글 조회
	public List<Map<String, Object>> readBoardList(int readNum) {
		String sql = "SELECT BOARD_NO, SUBJECT, TITLE, CONTENT, USER_ID, REG_DATE "
				+ " FROM P1_BOARD "
				+ " WHERE BOARD_no = ? ";
		List<Object> param = new ArrayList<>();
		param.add(readNum);
		
		return jdbc.selectList(sql, param);
	}
	// 댓글 조회
	public List<Map<String, Object>> selectRe02(int readNum,int deleteNum) {
		String sql = " SELECT A.* "
				+ "      FROM ( SELECT ROWNUM RN ,A.* "
				+ "               FROM ( SELECT B.RE_CONTENT, B.USER_ID, B.RE_DATE "
				+ "                        FROM P1_BOARD A INNER JOIN P1_BOARD_RE B ON(A.BOARD_NO = B.BOARD_NO)"
				+ "                       WHERE A.BOARD_NO = ?"
				+ "                       ORDER BY B.RE_NO)A)A "
				+ "              WHERE RN = ?";
		List<Object> param = new ArrayList<>();
		param.add(readNum);
		param.add(deleteNum);
		
		return jdbc.selectList(sql, param);
	}
	
	public List<Map<String, Object>> selectRe01(int readNum) {
		String sql = " SELECT ROWNUM ,A.RE_CONTENT, A.USER_ID, A.RE_DATE "
				+ "      FROM ( SELECT B.RE_CONTENT, B.USER_ID, B.RE_DATE "
				+ "               FROM P1_BOARD A INNER JOIN P1_BOARD_RE B ON(A.BOARD_NO = B.BOARD_NO) "
				+ "              WHERE A.BOARD_NO = ? "
				+ "              ORDER BY B.RE_NO)A "; 

 


		List<Object> param = new ArrayList<>();
		param.add(readNum);
		
		return jdbc.selectList(sql, param);
	}
				
	
	
	
	
	// 게시글 수정
	public int updateBoardList(String subject, String title, String content,int readNum) {
		String sql = "UPDATE P1_BOARD"
				+ " SET SUBJECT = ?, TITLE = ?, CONTENT = ? "
				+ " WHERE BOARD_NO = ? ";
		List<Object> param = new ArrayList<>();
		param.add(subject);
		param.add(title);
		param.add(content);
		param.add(readNum);
		
		return jdbc.update(sql, param);
		
	}
	
	// 전체 댓글 삭제
	public int deleteRe(int deleteNum) {
		String sql = "DELETE P1_BOARD_RE"
				+ " WHERE RE_NO = ?";
		List<Object> param = new ArrayList<>();
		param.add(deleteNum);
		
		return jdbc.update(sql, param);		
	}
	
	// 부분 댓글 삭제
	public int deleteRe02(int deleteNum, int readNum) {
		String sql = " delete p1_board_re "
			       + "  where re_no = ( SELECT A.re_no "
				                      + " FROM ( SELECT ROWNUM RN ,A.* "
				                              + "  FROM ( SELECT b.re_no, B.RE_CONTENT, B.USER_ID, B.RE_DATE "
				                                        + " FROM P1_BOARD A INNER JOIN P1_BOARD_RE B ON(A.BOARD_NO = B.BOARD_NO) "
				                                       + " WHERE A.BOARD_NO = ? "
				                                      + "  ORDER BY B.RE_NO)A)A "
			                 	             + "  WHERE RN = ?) ";
		List<Object> param = new ArrayList<>();
		param.add(readNum);
		param.add(deleteNum);
		
		return jdbc.update(sql, param);		
	}
	
	// 게시글 삭제
	public int deleteBoardList(int readNum) {
		String sql = "DELETE P1_BOARD"
				+ " WHERE BOARD_NO = ?";
		List<Object> param = new ArrayList<>();
		param.add(readNum);
		
		return jdbc.update(sql, param);		
	}

	public int insertRe(int readNum, String re01) {
		String sql = " INSERT INTO P1_BOARD_RE VALUES "
				+ " (P1_RE_SEQ.NEXTVAL,?,SYSDATE,?,?)";
		List<Object> param = new ArrayList<>();
		param.add(re01);
		param.add(readNum);
//		param.add("MASTER");
		param.add(Controller.loginUser.get("USER_ID"));	
		// 로그인한 유저의 아이디로 바꾸자
		
		return jdbc.update(sql, param);
	}

	
	// 작성자 아이디 입력 -> 게시글 리스트 출력
	public List<Map<String, Object>> searchList(String writer){
		
		String sql = "SELECT BOARD_NO, SUBJECT, TITLE, USER_ID, REG_DATE " 
				+ " FROM P1_BOARD "
				+ " WHERE USER_ID LIKE ? || '%' "
				+ " ORDER BY BOARD_NO DESC " ;
		
		List<Object> param = new ArrayList<>();
		param.add(writer);
		
		return jdbc.selectList(sql, param);
	}
	


	
	
	
	
	
	
	
	
	
	
	
	
}