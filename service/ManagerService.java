package service;

import java.util.List;
import java.util.Map;

import dao.ManagerDao;
import dao.ManagerFACSetDao;
import util.ScanUtil;
import util.View;

public class ManagerService {

	private ManagerService(){};
	private static ManagerService instance;
	public static ManagerService getInstance(){
		if (instance == null) {
			instance = new ManagerService();
		}
		return instance;
	}
	private ManagerDao managerDao = ManagerDao.getInstance();
	private static ManagerFACSetDao managerFACSetDao = ManagerFACSetDao.getInstance();
	
	public int Manager() {
		allowFaclist();
//		while (true) {
			
//			System.out.println("관리자로 로그인했구나 너");
//			System.out.println("-----------------------------");
//			System.out.println("");
//			
//			System.out.println("-----------------------------");
			
//		}
		return View.ManagerHome;
	}
	SmemMyPageService smemMyPageService = SmemMyPageService.getInstance();
	


	
	
	
	public void allowFaclist() {
		a : while (true) {
			List<Map<String,Object>> allowFacList = managerFACSetDao.AllowFacList();
			System.out.println("[시설 승인]");
			System.out.println("-------------------------------------");
			System.out.println("번호\t시설명\t시설상태");
			System.out.println("-------------------------------------");
			for(Map<String, Object> allowFac : allowFacList ){
				System.out.print(allowFac.get("ROWNUM") + "\t"); 
				System.out.print(allowFac.get("FACILITY_NAME") + "\t"); 
				System.out.print(allowFac.get("FACILITY_ALLOW") + "\t"); 
				System.out.println();
			}
			System.out.println("=====================================");
			System.out.println("1.조회\t2.등록\t0.뒤로가기");
			int input = ScanUtil.nextInt();
			switch (input) {
			case 1:
				System.out.println("조회할 행을 입력해주세요");
				int allowfacNo = ScanUtil.nextInt();
				if (allowfacNo > allowFacList.size() ) {
					break;
				}
				view(allowFacList, allowfacNo );
				break;
			case 2:
				System.out.println("시설을 등록합니다");
				smemMyPageService.insertManager();
				break;
			case 0:
				break a;
			}
		}
	}





	private void view(List<Map<String, Object>> allowFacList, int allowfacNo) {
		a : while (true) {

			Map<String, Object> row = allowFacList.get(allowfacNo-1);
			String allowfacId = (String)row.get("FACILITY_ID");
			
			Map<String,Object> allowFacList2 = managerFACSetDao.selectAllowViewList(allowfacId);
//			System.out.println( allowFacList.get(allowfacNo-1));
//			System.out.println(allowfacId);
			
			System.out.println("-------------------------------------");
			System.out.println("1.시설명\t:" + allowFacList2.get("FACILITY_NAME") );
			System.out.println("2.시설내용:" + allowFacList2.get("FACILITY_CONTENT"));
			System.out.println("3.오픈시간:" + allowFacList2.get("FACILITY_START_TIME"));
			System.out.println("4.마감시간:" + allowFacList2.get("FACILITY_END_TIME"));
			System.out.println("5.사업자등록증:" + allowFacList2.get("FACILITY_COMPANY_NUMBER"));
			System.out.println("6.오전요금:" + allowFacList2.get("FACILITY_AM_PAY"));
			System.out.println("7.오후요금:" + allowFacList2.get("FACILITY_PM_PAY"));
			System.out.println("8.우편번호:" + allowFacList2.get("FACILITY_POSTCODE"));
			System.out.println("9.시설주소:" + allowFacList2.get("FACILITY_ADDRESS"));
			System.out.println("10.상세주소:" + allowFacList2.get("FACILITY_ADDRESS_DETAIL"));
			System.out.println("11.휴대폰번호:" + allowFacList2.get("FACILITY_PHONE_NUMBER"));
			System.out.println("12.승인여부:" + allowFacList2.get("FACILITY_ALLOW") );
			System.out.println("-------------------------------------");
			
			System.out.println("1.승인\t2.비승인\t0.뒤로가기");
			int allow = ScanUtil.nextInt();
			switch (allow) {
			case 1:
				managerFACSetDao.viewAllowUpdate(allowfacId);
				System.out.println("승인되었습니다.");
				break a;
			case 2:
				managerFACSetDao.viewNonAllowUpdate(allowfacId);
				System.out.println("비승인되었습니다.");
				break a;
			case 0: break a;
			}
		}

		
	}
	
	
}
