package dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import util.JDBCUtil;

public class NoticeDao {

	private NoticeDao(){};
	private static NoticeDao instance;
	public static NoticeDao getInstance(){
		if (instance == null) {
			instance = new NoticeDao();
		}
		return instance;
	}

	JDBCUtil jdbc = JDBCUtil.getInstance();
	
	// 모든 공지사항 조회
	public List<Map<String, Object>> selectNotice() {
		String sql = "SELECT NOTICE_BOARD_NO, NOTICE_TITLE, NOTICE_REG_DATE"
				+ " FROM P1_NOTICE"
				+ " WHERE SYSDATE >= NOTICE_LAST_DATE "
				+ " ORDER BY NOTICE_BOARD_NO DESC";
		
		return jdbc.selectList(sql);
	}
	// 상단 고정조회
	public List<Map<String, Object>> selectNoticeTop() {
		String sql = "SELECT NOTICE_BOARD_NO, NOTICE_TITLE, NOTICE_REG_DATE"
				+ " FROM P1_NOTICE"
				+ " WHERE SYSDATE < NOTICE_LAST_DATE "
				+ " ORDER BY NOTICE_BOARD_NO DESC";
		
		return jdbc.selectList(sql);
	}

	public List<Map<String, Object>> readNotice(int readNum) {
		String sql = "SELECT *"
				+ " FROM P1_NOTICE "
				+ " WHERE NOTICE_BOARD_NO = ? ";
		List<Object> param = new ArrayList<>();
		param.add(readNum);
		
		return jdbc.selectList(sql, param);		
	}
	
	// 게시글 등록
	public int insertNotice(String title, String content, int top) {
		String sql = "INSERT INTO P1_NOTICE "
				+ " VALUES (P1_NOTICE_SEQ.NEXTVAL,?,?,SYSDATE,SYSDATE+?)";
		List<Object> param = new ArrayList<>();
		param.add(title);
		param.add(content);
		param.add(top);
		int result = jdbc.update(sql, param);
		return result; 
	}
	
	// 공지사항 수정
	public int updateNotice(String retitle, String recontent, int readNum) {
		String sql = "UPDATE P1_NOTICE "
				+ " SET NOTICE_TITLE = ?, NOTICE_CONTENT = ? "
				+ " WHERE NOTICE_BOARD_NO = ? ";
		List<Object> param = new ArrayList<>();
		param.add(retitle);
		param.add(recontent);
		param.add(readNum);
		
		int result = jdbc.update(sql, param);
		return result; 
	}
	
	// 공지사항 삭제
	public int deleteNotice(int readNum) {
		String sql = "DELETE P1_NOTICE "
				+ " WHERE NOTICE_BOARD_NO = ? ";
		List<Object> param = new ArrayList<>();
		param.add(readNum);
		
		int result = jdbc.update(sql, param);
		return result;
	}


	
	
	
	
	
	
	
	
}















