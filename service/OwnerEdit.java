package service;

import java.util.List;
import java.util.Map;

import dao.OwnerEditDao;
import dao.UserEditDao;
import util.ScanUtil;
import util.View;

public class OwnerEdit {
	
	private OwnerEdit(){};
	private static OwnerEdit instance;
	public static OwnerEdit getInstance(){
		if (instance == null) {
			instance = new OwnerEdit();
		}
		return instance;
	}
	OwnerEditDao ownerEditDao = OwnerEditDao.getInstance();
	
	// 회원 관리 페이지
	public int OwnerEdit(){
		
		while (true) {
			System.out.println("[시설주 관리]");
			System.out.println("---------------------------------------------------------");
			System.out.println("NO\t소속\t아이디");
			List<Map<String, Object>> userEdit = ownerEditDao.selectUser();
			for (Map<String, Object> user : userEdit) {
				System.out.print(user.get("ROWNUM")         	 +"\t");
				System.out.print(user.get("USER_NAME")         	 +"\t");
				System.out.println(user.get("USER_ID")        	 +"");
			}
			System.out.println("---------------------------------------------------------");
			System.out.println("1.상세정보 0.뒤로가기");
//			System.out.println("1.시설주삭제 0.뒤로가기");
			int input = ScanUtil.nextInt();
			switch (input) {
//			case 1:	// 삭제를 하려면 조회를 해야한다.
//				System.out.println("삭제할 번호를 입력해주세요");
//				int deleteNum = ScanUtil.nextInt();
//				System.out.println(deleteNum+"번 시설주를 정말로 삭제하시겠습니까?");
//				System.out.println("1.삭제  0.뒤로가기");
//				int choise = ScanUtil.nextInt();
//				if (choise == 1) {
//					ownerEditDao.deleteUser(deleteNum);
//					System.out.println("삭제되었습니다.");
//				}else {
//					System.out.println("취소되었습니다.");
//				}
//				break;
			case 1:
				System.out.print("번호를 입력해주세요 ");
				int sdfd = ScanUtil.nextInt();
				List<Map<String, Object>> userEdit123 = ownerEditDao.selectUser1(sdfd);
				System.out.println("---------------------------------------------------------");
				for (Map<String, Object> user : userEdit123) {
					System.out.println("시설주이름 : " + user.get("USER_NAME") );
					System.out.println("아  이  디   : " + user.get("USER_ID") );
					System.out.println("비밀 번호   : " + user.get("USER_PASSWORD"));
					System.out.println("휴대 전화   : " + user.get("USER_PHONENUMBER"));
					System.out.println("우편 번호   : " + user.get("USER_POSTCODE"));
					System.out.println("기본 주소   : " + user.get("USER_ADDRESS"));
					System.out.println("상세 주소   : " + user.get("USER_ADDRESS_DETAIL"));
					if (user.get("USER_DELETE").equals("1")) {
						System.out.println("탈퇴 여부 : 탈퇴");
					}else{
						System.out.println("탈퇴 여부 : -");
					}
				}
				System.out.println("---------------------------------------------------------");
				System.out.println(" 0.뒤로가기");
				int input3 = ScanUtil.nextInt();
				break;
			case 0:
				
				return View.ManagerHome;
			}
			
			
		}
	
		
		
		
		
		
		
	}
	
	
	
	
	
	
}
