package controller;

import java.util.Map;

import dao.FacilityReservationDao;
import service.BoardService;
import service.FacilityViewService;
import service.ManagerService;
import service.NonMember;
import service.NoticeBoardService;
import service.OwnerEdit;
import service.UserEdit;
import service.UserService;
import util.ScanUtil;
import util.View;

public class Controller {
	
	public static void main(String[] args) {
				
		new Controller().start();
	}
	public static Map<String, Object> loginUser;// 로그인한 사람을 저장 , 필요할때마다 꺼내쓴다
	public static Map<String, Object> loginManager;// 로그인한 사람을 저장 , 필요할때마다 꺼내쓴다
	
	private BoardService boardServie = BoardService.getInstance();
	private NoticeBoardService noticeBoardService = NoticeBoardService.getInstance();
	private UserEdit userEdit = UserEdit.getInstance();
	private OwnerEdit ownerEdit = OwnerEdit.getInstance();
	private UserService userService = UserService.getInstance();
	private NonMember nonMember = NonMember.getInstance();
	private ManagerService managerService = ManagerService.getInstance();
	private FacilityViewService facilityViewService = FacilityViewService.getInstance();
	private FacilityReservationDao dao = FacilityReservationDao.getInstance();
	
	private void start() {
//		dao.reservFinish();
		int view = View.HOME;
		
		while(true){
			switch (view) {
			case View.HOME: // 홈화면
					view = home();
					break;
			case View.BOARD: // 게시판 , 공지사항
				System.out.println("1.게시판 2.공지사항 0.뒤로가기");
				int choice1 = ScanUtil.nextInt();
					switch (choice1) {
						case 1: 
						    view = boardServie.BoardList();
						    break;
						case 2:
							view = noticeBoardService.NoticeList();
							break;
						case 0:
							view = View.HOME;
					}
					break;
			case View.uEdit: // 회원관리
				view = userEdit.UserEdit();
				break;
			case View.oEdit: // 시설주관리
				view = ownerEdit.OwnerEdit();
				break;
			case View.LOGIN: // 로그인
				view = userService.login();
				break;
			case View.JOIN: // 회원가입
				view = userService.join();
				break;
			case View.nonM: // 비회원 몰아넣기
				view = nonMember.non();
				break;
				
			case View.ALL: // 관리자 계정
				view = managerService.Manager();
				break;
			case View.ManagerHome:
				view = ManagerHome();
				break;
				
			case View.FACILITY_VIEW : 
				view = facilityViewService.facilityViewList(); 
				break; 
				
			case View.MYINFO:
				view = userService.myinfo();
				break;
			
			case View.NOTICE:
				view = noticeBoardService.NoticeList();
				break;
				
			}
		}
		
	}

	private int home() {
		// 비회원 만 
		if (loginUser == null || loginUser.size() == 0) {
			return View.nonM;
			}
			// 로그인한 사람만
		System.out.println("----------------------------------------------");
		if (loginUser != null ) {
			System.out.print(loginUser.get("USER_TYPE")+ " " );
			System.out.print(loginUser.get("USER_NAME")+"님 환영합니다!");
			System.out.println();
		} 
		    System.out.println();
			System.out.println("1.로그아웃 2.마이페이지");
			System.out.println("3.게시판 4.시설   0.종료 ");
			System.out.println("----------------------------------------------");
		

		System.out.println("메뉴번호를 입력해주세요");
		int input1 = ScanUtil.nextInt();
		
		switch (input1) {
		case 1:	loginUser.clear();
				System.out.println("로그아웃 되었습니다.");
				return View.nonM;
		case 2:	return View.MYINFO;
		case 3: return View.BOARD;
		case 4: return View.FACILITY_VIEW;
//		case 8: return View.uEdit;
//		case 9: return View.oEdit;
		case 99: return View.ALL;
		case 0: 
				System.out.println("종료");
				System.exit(0);
		}
		
		
		return View.HOME;
	}
	
	
	private int ManagerHome(){
		
		System.out.println("----------------------------------------------");
		if (loginManager != null || loginManager.size() == 0) {
//			System.out.println(loginUser);
//			System.out.println(loginManager.get("MANAGER_ID").equals("MANAGER"));
//			System.out.println(loginManager.get("MANAGER_ID"));
//			System.out.println(loginManager.get("MANAGER_PASSWORD"));
			System.out.println("[관리자 Mode]");
		    System.out.println("1.로그아웃 0.종료");
			System.out.println("2.공지사항 3.시설관리 4.시설등록/승인 5.회원관리 6.시설주관리 ");
			System.out.println("----------------------------------------------");
		
		}
		System.out.println("메뉴번호를 입력해주세요");
		int input1 = ScanUtil.nextInt();
		
		switch (input1) {
		case 1: loginManager.clear(); 
				System.out.println("로그아웃 되었습니다.");
				return View.nonM;
		case 2: return View.NOTICE;
		case 3: return View.FACILITY_VIEW;
		case 4: return View.ALL;
		case 5: return View.uEdit;
		case 6: return View.oEdit;
		case 0: 
				System.out.println("종료");
				System.exit(0);
		}
//		if (loginUser != null) {
//			if (loginUser.get("MANAGER_ID").equals("MANAGER")) {
//				return View.ManagerHome;
//			}	
//		}
		
		
		
		return View.ALL;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
