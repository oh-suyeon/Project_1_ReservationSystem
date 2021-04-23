package service;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import util.ScanUtil;
import util.View;
import controller.Controller;
import dao.RentalRecodeDao;

public class RentalRecordService {
	
	private RentalRecordService(){}
	
	private static RentalRecordService instance;
	
	public static RentalRecordService getInstance(){
		if(instance == null){
			instance = new RentalRecordService();
		}
		return instance;
	}
	
	private RentalRecodeDao rentalRecodeDao = RentalRecodeDao.getInstance();
	private SimpleDateFormat dateForm = new SimpleDateFormat("yyyy-MM-dd");
	
	String facilityID = null;
	List<Map<String, Object>> listO = null;
	List<Map<String, Object>> listX = null;
	
	public int rentalRecodeList(){
		
		while(true){
			listO = rentalRecodeDao.recodeListO((String)Controller.loginUser.get("USER_ID"));
			listX = rentalRecodeDao.recodeListX((String)Controller.loginUser.get("USER_ID"));
			
			System.out.println("=================================================================");
			System.out.println("NO\t시설명  [대관일자] [처리상태]");
			System.out.println("=================================================================");
			
				if(listO.size() > 0){
					for(int i = 0; i < listO.size(); i++){
						Map<String, Object> row = listO.get(i);
						System.out.println(row.get("RN") + "\t" + row.get("FACILITY_NAME") 
									   + " [" + dateForm.format(row.get("RESERVATION_DATE")) 
									   + "] [" + row.get("RESERVATION_STATE") + "]\n\t코멘트 : " + row.get("COMMENTSCONTENT"));
					}
				System.out.println("-----------------------------------------------------------------");
				if(listX.size() > 0){
					for(int i = 0; i < listX.size(); i++){
						Map<String, Object> row = listX.get(i);
						System.out.println("\t" + row.get("FACILITY_NAME") 
									   + " [" + dateForm.format(row.get("RESERVATION_DATE")) 
									   + "] [" + row.get("RESERVATION_STATE") + "]");
					}
				}
					System.out.println("=================================================================");
					System.out.println("1.코멘트 등록\t\t0.뒤로가기");
					System.out.println("=================================================================");
				
					System.out.println("메뉴 선택 > ");
					int input = ScanUtil.nextInt();
					switch(input){
						case 1 : insertComment();
					 			 break;
						case 0 : return View.HOME;
						default: System.out.println("<잘못된 입력입니다>"); 
								 break;
					}
				} else {
					System.out.println("<시설 대관/예약취소 내역이 존재하지 않습니다>");
					System.out.println("=================================================================");
					System.out.println("0.뒤로가기");
					System.out.println("=================================================================");
					System.out.println("메뉴 선택 > ");
					int input = ScanUtil.nextInt();
					switch(input){
						case 0 : return View.HOME;
						default: System.out.println("<잘못된 입력입니다>");
								 break;
					}
			   }
		 }
	}
	

	private void insertComment(){
		System.out.println("번호 입력 > ");
		int facilityNo = ScanUtil.nextInt();
		
		facilityID = (String)listO.get(facilityNo - 1).get("FACILITY_ID");
		
		// 이미 등록한 코멘트가 있을 경우 메서드 종료
		if(!listO.get(facilityNo - 1).get("COMMENTSCONTENT").equals("-")){
			System.out.println("<이미 코멘트를 등록했습니다>");
			return;
		}
		
		// 예약 취소 상태 번호를 골랐을 경우 코멘트 작성 불가
//		if(list.get(facilityNo - 1).get("RESERVATION_STATE").equals("예약취소")){
//			System.out.println("<예약취소 시 코멘트를 등록할 수 없습니다>");
//			return;
//		}
		
		System.out.println("코멘트 내용 > ");
		String commentContent = ScanUtil.nextLine();
		
		rentalRecodeDao.insertComment(commentContent, (String)Controller.loginUser.get("USER_ID"), facilityID);
		
		System.out.println("<코멘트가 등록되었습니다>");
	}
	
}