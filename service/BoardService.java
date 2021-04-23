package service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;




import controller.Controller;
import dao.BoradDao;
import util.ScanUtil;
import util.View;

public class BoardService {
	

	private BoardService(){};
	private static BoardService instance;
	public static BoardService getInstance(){
		if (instance == null) {
			instance = new BoardService();
		}
		return instance;
	}
	
	private BoradDao boardDao = BoradDao.getInstance();
	SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
	Date time = new Date();
	

	
	
	// 게시글 목록
	public int BoardList(){
		while (true) {
			List<Map<String, Object>> boardList = boardDao.selectBoardList();
			System.out.println("========================================");
			System.out.println("NO 말머리  제목\t작성자\t작성일");
			for (Map<String, Object> board : boardList) {
				String time1 = format.format(board.get("REG_DATE"));
				System.out.println(board.get("BOARD_NO")
						+ "    " +board.get("SUBJECT")
						+ "    " +board.get("TITLE")
						+ "\t" +board.get("USER_ID")
						+ "\t" + time1);
			}
			System.out.println("========================================");
			// 비회원
			if ((Controller.loginUser == null ||Controller.loginUser.size() == 0)
				&& (Controller.loginManager == null || Controller.loginManager.size() ==0) ) {
				System.out.println("1.조회\t0.뒤로가기");
				int input = ScanUtil.nextInt();
				switch (input) {
				case 1:
					read001();
					break;
				case 0:	return  View.HOME;
				}
			}else{	// 나머지
				System.out.println("1.조회 2.등록 3.검색 0.뒤로가기");
				int input = ScanUtil.nextInt();
				switch (input) {
				case 1:
					read001();
					break;
				case 2: //등록
					insert001();
					break;
				case 3: //검색(작성자 아이디 검색)
					search001();
					break;
				case 0:
					if (Controller.loginUser == null && Controller.loginManager != null) {
						if (Controller.loginManager.get("MANAGER_ID").equals("MANAGER")) {
							return View.ManagerHome;
						}	
					}
					return  View.HOME;
				}
			}
		}
	}


	// 골라서 조회
	private int read001() {
		System.out.println("조회할 번호를 입력해주세요.");
		int readNum = ScanUtil.nextInt();
		read : while (true) {
			List<Map<String, Object>> readBoard = boardDao.readBoardList(readNum);
			if (readBoard.size() < 1) {
				System.out.println("없는 게시글 입니다.");
				return View.BOARD;
			}
			System.out.println("========================================");
			for (Map<String, Object> read : readBoard) {
				String time1 = format.format(read.get("REG_DATE"));
				System.out.print  ("NO. "+read.get("BOARD_NO"));
				System.out.println(" / 말머리 : "+read.get("SUBJECT"));
				System.out.print  ("제   목 : "+read.get("TITLE"));
				System.out.println(" / 작성자 : "+time1);
				System.out.println("----------------------------------------");
				System.out.println("내   용 :");
				System.out.println(read.get("CONTENT"));	
			}
			System.out.println("----------------------------------------");
			List<Map<String, Object>> re = boardDao.selectRe01(readNum);
			for (Map<String, Object> r : re) {
				String time1 = format.format(r.get("RE_DATE"));
				System.out.print(r.get("ROWNUM")+" | ");
				System.out.print(r.get("RE_CONTENT")+" | ");
				System.out.print(r.get("USER_ID")+" | ");
				System.out.println(time1);
			}
			
			System.out.println("========================================");
//				 바로아래 if는 비회원 걸러내기
				if((Controller.loginUser == null ||Controller.loginUser.size() == 0)
						&& (Controller.loginManager == null || Controller.loginManager.size() ==0) ) {
					System.out.println("0.뒤로가기");
					int input4 = ScanUtil.nextInt();
					switch (input4) {
						case 0:
							break read;
					}
				}
				
			System.out.println("1.게시글 수정 2.게시글 삭제 3.댓글 등록 4.댓글 삭제 0.뒤로가기");
			int input2 = ScanUtil.nextInt();
			switch (input2) {
			case 1:  // 게시글 수정
				System.out.println("게시글을 수정 합니다.");
				List<Map<String, Object>> u = boardDao.readBoardList(readNum);
				for (Map<String, Object> uu : u){
					// 지금은 MASTER지만 로그인한 유저 아디로 바꾸자 Controller.LoginUser.get("USER_ID")
					if (uu.get("USER_ID").equals(Controller.loginUser.get("USER_ID"))) {
						update001(readNum);
					} else{
						System.out.println("본인이 작성한 게시글이 아닙니다.");
					}
				}
				break;
			case 2:	// 게시글 삭제
				System.out.println("게시글을 삭제 합니다.");
				List<Map<String, Object>> d = boardDao.readBoardList(readNum);
				for (Map<String, Object> dd : d){
					// 지금은 MASTER지만 로그인한 유저 아디로 바꾸자 Controller.LoginUser.get("USER_ID")
					if (dd.get("USER_ID").equals(Controller.loginUser.get("USER_ID"))) {
						delete001(readNum);
						
					} else{
						System.out.println("본인이 작성한 게시글이 아닙니다.");
					}
				}
				return View.BOARD;
			case 3: // 댓글 등록
				System.out.println("댓글을 등록합니다");
				System.out.print  ("댓글 : ");
				String re01 = ScanUtil.nextLine();
				boardDao.insertRe(readNum, re01);
				System.out.println("댓글이 등록되었습니다");
				break;
			case 4:	// 댓글 삭제
				System.out.println("댓글을 삭제 합니다.");
				System.out.println("삭제할 댓글 번호를 입력해주세요");
				int deleteNum = ScanUtil.nextInt();
				List<Map<String, Object>> RD = boardDao.selectRe02(readNum,deleteNum);
				for (Map<String, Object> RDRD : RD){
					if (RDRD.get("USER_ID").equals(Controller.loginUser.get("USER_ID"))) {
						delete002(deleteNum,readNum);
					}else{
						System.out.println("본인이 작성한 댓글이 아닙니다.");
					}
				}
				break;
			case 0:
				return View.BOARD;
			}
		}
		return View.BOARD;
		
	}
	
	// 댓글 개별 삭제
	private void delete002(int deleteNum, int readNum) {
		System.out.println("정말로 댓글을 삭제하시겠습니까?");
		System.out.println("1.네 0.아니요");
		int input = ScanUtil.nextInt();
		switch (input) {
		case 1: 
			boardDao.deleteRe02(deleteNum,readNum);
			System.out.println("삭제 되었습니다.");
			break;
		case 0:
			System.out.println("취소하셨습니다.");
			break;
		}
		
	}


	// 게시글 삭제 되묻기
	private void delete001(int readNum) {
		System.out.println("정말로 게시글을 삭제하시겠습니까?");
		System.out.println("1.네 0.아니요");
		int input = ScanUtil.nextInt();
		switch (input) {
		case 1: // 삭제시 댓글 -> 게시글 순서로 삭제
			boardDao.deleteRe(readNum);
			boardDao.deleteBoardList(readNum);
			System.out.println("삭제 되었습니다.");
			break;
		case 0:
			System.out.println("취소하셨습니다.");
			break;
		}
		
	}


	// 게시글 수정
	private void update001(int readNum) {

		System.out.println("말머리를 선택해주세요.");
		System.out.println("1.구인 2.홍보 3.잡담");
		System.out.println(">");
		String write01 = "";
		boolean flag = true;
		do {
			int sub = ScanUtil.nextInt();
			if (sub == 1) {
				write01 = "구인";
				flag = false;
			}else if(sub == 2){
				write01 = "홍보";
				flag = false;
			}else if (sub == 3) {
				write01 = "잡담";
				flag = false;
			}else{
				System.out.println("1,2,3 중에 골라주세요");
			}
		} while (flag);
		
		System.out.print("제목을 수정합니다 :");
		String write02 = ScanUtil.nextLine();
		System.out.print("내용을 수정합니다 :");
		String write03 = ScanUtil.nextLine();
		
		System.out.println("=================작성한글=================");
		System.out.println("말머리 : "+ write01);
		System.out.println("제   목 : "+ write02);
		System.out.println("내   용 : "+ write03);
		System.out.println("========================================");
		System.out.println("수정하시겠습니까?");
		System.out.println("1.확인\t0.뒤로가기");
		int update = ScanUtil.nextInt();
		switch (update) {
		case 1:
			boardDao.updateBoardList(write01,write02,write03,readNum);
			System.out.println("수정 되었습니다.");
			break;
		case 0:
			System.out.println("취소하셨습니다.");
			break ;
		}
		
	}

	// 게시글 등록
	private void insert001() {
		// 바로아래 if는 비회원 걸러내기
		if(Controller.loginUser == null) {
			System.out.println("로그인해야 등록할수있습니다.");
			System.out.println("0.뒤로가기");
			int input4 = ScanUtil.nextInt();
			switch (input4) {
				case 0:
					;
			}
		
		}else{
			System.out.println("게시글을 등록합니다.");
			System.out.println("말머리를 선택해주세요.");
			System.out.println("1.구인 2.홍보 3.잡담");
			System.out.println(">");
			String write01 = "";
			boolean flag = true;
			do {
				int sub = ScanUtil.nextInt();
				if (sub == 1) {
					write01 = "구인";
					flag = false;
				}else if(sub == 2){
					write01 = "홍보";
					flag = false;
				}else if (sub == 3) {
					write01 = "잡담";
					flag = false;
				}else{
					System.out.println("1,2,3 중에 골라주세요");
				}
			} while (flag);
			
			
			System.out.print("제목을 입력해주세요 :");
			String write02 = ScanUtil.nextLine();
			System.out.print("내용을 입력해주세요 :");
			String write03 = ScanUtil.nextLine();
			
			System.out.println("=================작성한글=================");
			System.out.println("말머리 : "+ write01);
			System.out.println("제   목 : "+ write02);
			System.out.println("내   용 : "+ write03);
			System.out.println("========================================");
			System.out.println("등록하시게습니까?");
			System.out.println("1.확인\t0.뒤로가기");
			int insert = ScanUtil.nextInt();
			switch (insert) {
			case 1:
				boardDao.insertBoardList(write01,write02,write03);
				System.out.println("글이 등록되었습니다.");
				break;
			case 0:
				System.out.println("등록하지 않았습니다.");
				break ;
			}
		}	
	}
	
	// 검색 (작성자 아이디)
	private void search001(){
		System.out.println("작성자 아이디 입력 >");
		String writer = ScanUtil.nextLine();
		outer : 	
		while(true){
			List<Map<String, Object>> list = boardDao.searchList(writer);
			System.out.println("========================================");
			System.out.println("NO 말머리  제목\t작성자\t작성일");
			for (Map<String, Object> board : list) {
				String time1 = format.format(board.get("REG_DATE"));
				System.out.println(board.get("BOARD_NO")
						+ "    " +board.get("SUBJECT")
						+ "    " +board.get("TITLE")
						+ "\t" +board.get("USER_ID")
						+ "\t" + time1);
			}
			System.out.println("========================================");
			System.out.println("1.조회 0.뒤로가기");
			int input2 = ScanUtil.nextInt();
			switch(input2){
			case 1: read001(); break;
			case 0: break outer;
			default: break;
			} 
		} 
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}