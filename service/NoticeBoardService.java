package service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import controller.Controller;
import dao.NoticeDao;
import util.ScanUtil;
import util.View;

public class NoticeBoardService {

	private NoticeBoardService(){};
	private static NoticeBoardService instance;
	public static NoticeBoardService getInstance(){
		if (instance == null) {
			instance = new NoticeBoardService();
		}
		return instance;
	}
	NoticeDao noticeDao = NoticeDao.getInstance();
	SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
	Date time = new Date();
	
	public int NoticeList(){
		
		while(true){
		System.out.println("========================================");
		System.out.println("NO 제목\t\t작성일");
		//상단고정일 이전
		List<Map<String, Object>> notice1 = noticeDao.selectNoticeTop();
		for (Map<String, Object> notice11 : notice1) {
			String time1 = format.format(notice11.get("NOTICE_REG_DATE"));
			System.out.print  (notice11.get("NOTICE_BOARD_NO")+"    ");
			System.out.print  (notice11.get("NOTICE_TITLE")+"\t");
			System.out.println(time1);
		}
		System.out.println("----------------------------------------");
		// 상단고정일 이후
		List<Map<String, Object>> notice2 = noticeDao.selectNotice();
		for (Map<String, Object> notice22 : notice2) {
			String time1 = format.format(notice22.get("NOTICE_REG_DATE"));
			System.out.print  (notice22.get("NOTICE_BOARD_NO")+"    ");
			System.out.print  (notice22.get("NOTICE_TITLE")+"\t");
			System.out.println(time1);
		}
		System.out.println("========================================");
		
		// 비회원
		if ((Controller.loginUser == null ||Controller.loginUser.size() == 0)
				&& (Controller.loginManager == null || Controller.loginManager.size() ==0) ) {
			System.out.println("1.조회 0.뒤로가기");
			int input = ScanUtil.nextInt();
			switch (input) {
				case 1:
					noticeRead002();
					break;
				case 0:
					return View.HOME;
			}
		}else{
			System.out.println("1.조회 2.등록 0.뒤로가기");
			int input = ScanUtil.nextInt();
				switch (input) {
					case 1:
						noticeRead001();
						break;
					case 2: //등록
						if (Controller.loginUser == null && Controller.loginManager != null) {
							if (Controller.loginManager.get("MANAGER_ID").equals("MANAGER")) {
								noticeinsert001();
							}	
						}else{
							System.out.println("관리자만 공지사항을 등록할 수 있습니다.");
						}

						break;
					case 0:
						if (Controller.loginUser == null && Controller.loginManager != null) {
							if (Controller.loginManager.get("MANAGER_ID").equals("MANAGER")) {
								return View.ManagerHome;
							}	
						}
						return View.HOME;
				}
			}
		}
	}

		
	private void noticeinsert001() {
		System.out.println("공지사항을 등록합니다");
		System.out.print  ("제목 :");
			String write01 = ScanUtil.nextLine();
		System.out.print  ("내용 :");
			String write02 = ScanUtil.nextLine();
		System.out.print  ("상단고정일수 :");
			int write03 = ScanUtil.nextInt();

		System.out.println("=================작성한글=================");
		System.out.println("제   목 : "+ write01);
		System.out.println("내   용 : "+ write02);
		System.out.println("상단고정일수 : 작성일 + "+ write03+" 일");
		System.out.println("========================================");
		System.out.println("등록하시겠습니까?");
		System.out.println("1.확인\t0.뒤로가기");
		int insert = ScanUtil.nextInt();
		switch (insert) {
		case 1:
			int result = noticeDao.insertNotice(write01,write02,write03);
				if (result == 1) {
					System.out.println("등록되었습니다.");
					
				}else {
					System.out.println("오류가 있군요 어서고치세요");
				}
			break;
		case 0:
			System.out.println("등록하지 않았습니다.");
			break ;
		}
		
		
	}


	private void noticeRead001() {
		System.out.println("조회할 번호를 입력해주세요");
		int readNum = ScanUtil.nextInt();
		List<Map<String, Object>> read = noticeDao.readNotice(readNum);
		for (Map<String, Object> re : read) {
			String time1 = format.format(re.get("NOTICE_REG_DATE"));
			String time2 = format.format(re.get("NOTICE_LAST_DATE"));
			System.out.println("========================================");
			System.out.print  ("N  O : "+re.get("NOTICE_BOARD_NO"));
			System.out.println("  제목 : "+re.get("NOTICE_TITLE"));
			System.out.println("작성일 : "+time1);
			System.out.println("상단 고정일 : "+time2);
			System.out.println("----------------------------------------");
			System.out.println("내용 >");
			System.out.println(re.get("NOTICE_CONTENT"));
			System.out.println("========================================");
		}
		
		if (Controller.loginUser == null && Controller.loginManager != null) {
			if (Controller.loginManager.get("MANAGER_ID").equals("MANAGER")) {
				System.out.println("1.수정 2.삭제 0.뒤로가기");
				int input2 = ScanUtil.nextInt();
				switch (input2) {
					case 1:
						update01(readNum);			
						break;
					case 2:
						delete01(readNum);
						break;
					case 0:
				
						break;
				}
			}
		}else {
			System.out.println("0.뒤로가기");
			int input2 = ScanUtil.nextInt();
			switch (input2) {
			case 0:	break;
			}
		}

			
	}
	
	private void delete01(int readNum) {
		System.out.println("해당 공지사항을 삭제합니다.");
		System.out.println("정말 삭제 하시겠습니까?");
		System.out.println("1.삭제 0.뒤로가기");
		int input = ScanUtil.nextInt();
		switch (input) {
		case 1:
			int result = noticeDao.deleteNotice(readNum);
				if (result == 1) {
					System.out.println("수정 되었습니다.");
				}else{
					System.out.println("오류있음 빨리 수정하셈 ~~");
				}	
			break;
		case 0:
			break;
		}
	}


	// 공지사항 수정 
	private void update01(int readNum) {
		System.out.println("게시글을 수정합니다");
		System.out.print("제목 :");
		String retitle = ScanUtil.nextLine();
		System.out.print("내용 :");
		String recontent = ScanUtil.nextLine();
		System.out.println("=================수정한글=================");
		System.out.println("제   목 : "+ retitle);
		System.out.println("내   용 : "+ recontent);
		System.out.println("========================================");
		System.out.println("수정하시겠습니까?");
		System.out.println("1.확인\t0.뒤로가기");
		int update = ScanUtil.nextInt();
		switch (update) {
		case 1:
			int result = noticeDao.updateNotice(retitle,recontent,readNum);
				if (result == 1) {
					System.out.println("수정 되었습니다.");
				}else{
					System.out.println("오류있음 빨리 수정하셈 ~~");
				}	
			break;
		case 0:
			System.out.println("취소하셨습니다.");
			break ;
		}
		
		
	}




	// 관리자말고 나머지 사람들 조회 코드
	private void noticeRead002() {
		System.out.println("조회할 번호를 입력해주세요");
		int readNum = ScanUtil.nextInt();
		List<Map<String, Object>> read = noticeDao.readNotice(readNum);
		for (Map<String, Object> re : read) {
			String time1 = format.format(re.get("NOTICE_REG_DATE"));
			String time2 = format.format(re.get("NOTICE_LAST_DATE"));
			System.out.println("========================================");
			System.out.print  ("NO "+re.get("NOTICE_BOARD_NO"));
			System.out.println("  제목 : "+re.get("NOTICE_TITLE"));
			System.out.println("작성일 : "+time1);
			System.out.println("상단 고정일 : "+time2);
			System.out.println("----------------------------------------");
			System.out.println("내용 >");
			System.out.println(re.get("NOTICE_CONTENT"));
			System.out.println("========================================");
		}
		System.out.println("0.뒤로가기");
		int out = ScanUtil.nextInt();
		switch (out) {
			case 0: break;
		}	
	}

}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	