package service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import controller.Controller;
import util.ScanUtil;
import util.View;
import dao.FacilityReservationDao;
import dao.FacilityViewDao;

public class FacilityViewService {
	
	private FacilityViewService(){}
	private static FacilityViewService instance;
	public static FacilityViewService getInstance(){
		if(instance == null){
			instance = new FacilityViewService();
		}
		return instance;
	}
	
	private FacilityViewDao facilityViewDao = FacilityViewDao.getInstance();
	private FacilityReservationDao facilityReservationDao = FacilityReservationDao.getInstance();
	private FacilityReservationService facilityReservationService = FacilityReservationService.getInstance();
	private SmemMyPageService smemMyPageService = SmemMyPageService.getInstance();
	
	
	// 선택할 값을 저장할 변수들 - 쿼리에 넣을 코드 
	private String facilityLargeCode = null;
	private String facilityCode = null;
	private int regionLargeCode = 0;
	private String regionSmallCode = null;
	// 선택할 값을 저장할 변수들 - 리스트와 출력할 문장에 넣을 이름
	private String facilityCodeName = null;
	private String facilityLargeName = null;
	private String regionLargeCodeName = null;
	private String regionSmallCodeName = null;
	
	public int facilityViewList(){		
		
		// 첫 선택지
		System.out.println("=================================================================");
		System.out.println("1.체육시설\t2.공간시설\t0.뒤로가기");
		System.out.println("=================================================================");
		int input = ScanUtil.nextInt();	
		
		facilityCode = null; // 첫 선택지로 돌아오면 전체 초기화
		regionLargeCode = 0;
		regionSmallCode = null;
		
		// 첫 선택지 값이 1, 2일 경우 다음 선택 및 조회가 시작된다. 
		if(input != 0){
			
			while(true){
				
				//문장출력용이름에 선택한 시설 대분류명 저장하기
				if(input == 1){
					facilityLargeName = "체육시설";
					facilityLargeCode = "P";
				} else if(input == 2){
					facilityLargeName = "공간시설";
					facilityLargeCode = "C";
				} else{
					System.out.println("알맞게 입력해주세요");
					return View.FACILITY_VIEW;
				}
				
				//두번째 선택지
				System.out.println("=================================================================");
				System.out.println("1.세부유형\t2.지역선택\t3.조회\t0.뒤로가기");					
				System.out.println("=================================================================");
				int input2 = ScanUtil.nextInt();										
				
				switch(input2){
				//세부유형 선택
				case 1 : facilityCodeList(facilityLargeName); break;
				//지역 선택 (광역시를 선택하는 메서드 안에 구를 선택하는 메서드가 있음)
				case 2 : regionLargeCodeList(); break;
				//시설 리스트 출력
				case 3 : selectedFacilityList(facilityCode, regionLargeCode, regionSmallCode); break;
				//체육공간시설 선택하는 첫 선택지로 돌아가기
				case 0 : return View.FACILITY_VIEW;
				}
			}
		}
		// 첫선택지에서 0을 선택했을 때 홈으로 돌아가기 - 사용자별로 다르다.
		if (Controller.loginUser == null && Controller.loginManager != null) {
			if (Controller.loginManager.get("MANAGER_ID").equals("MANAGER")) {
				return View.ManagerHome;
			}	
		}
		return View.HOME;

	}
			
	// 시설 세부유형 리스트 출력 및 선택 유형 저장
	private int facilityCodeList(String facCodeName){
		List<Map<String, Object>> list =  facilityViewDao.facilityCodeList(facCodeName);
		System.out.println("=================================================================");
		for(int i = 0; i < list.size(); i++){
			Map<String, Object> row = list.get(i);
			System.out.println(i + 1 + "." + row.get("FACILITY_SMALL_NAME"));
		} 
		System.out.println("0.뒤로가기");
		System.out.println("=================================================================");
		System.out.println("시설 유형 선택 >");
		int no = ScanUtil.nextInt();
		if(no > 0){
			Map<String, Object> row2 = list.get(no - 1);
			facilityCode = (String)row2.get("FACILITY_CODE");
			facilityCodeName = (String)row2.get("FACILITY_SMALL_NAME");
		}
		return no;
	}
	
	// 지역 리스트 출력 및 선택 지역 저장
	private void regionLargeCodeList(){
		List<Map<String, Object>> list = facilityViewDao.regionLargeCodeList();
		System.out.println("=================================================================");
		for(int i = 0; i < list.size(); i++){
			Map<String, Object> row = list.get(i);
			System.out.println(i + 1 + "." + row.get("REGION_LARGE_NAME"));
		}
		System.out.println("0.뒤로가기");
		System.out.println("=================================================================");
		System.out.println("광역시 선택 >");
		int no = ScanUtil.nextInt();
		if(no > 0){		// 광역시를 선택한 경우 코드를 저장하고 구 선택으로 넘어감
			regionLargeCode = no;
			Map<String, Object> row2 = list.get(no - 1);
			regionLargeCodeName = (String)row2.get("REGION_LARGE_NAME");
			if(no != 4){	// 구가 없는 세종시가 아닌 경우 구 선택으로 넘어감 
				System.out.println("1.구 선택\t\t0.뒤로가기");
				int no2 = ScanUtil.nextInt();
				switch(no2){
					case 1 : regionSmallCodeList(no); break; // 구를 선택하고 저장하는 메서드를 호출함
					case 0 : break;
				}
			}
		}
	}
	
	// 지역-구 리스트 출력 및 선택 지역 저장
	private void regionSmallCodeList(int largeType){
		List<Map<String, Object>> list = facilityViewDao.regionSmallCodeList(largeType);
		System.out.println("=================================================================");
		for(int i = 0; i < list.size(); i++){
			Map<String, Object> row = list.get(i);
			System.out.println(i + 1 + "." + row.get("REGION_SMALL_NAME"));
		}
		System.out.println("0.뒤로가기");
		System.out.println("=================================================================");
		System.out.println("구 선택 >");
		int no = ScanUtil.nextInt();
		if(no > 0){
			Map<String, Object> row2 = list.get(no - 1);
			regionSmallCode = (String)row2.get("REGION_CODE");
			regionSmallCodeName = (String)row2.get("REGION_SMALL_NAME");
		}
	}
	
	// 검색 조건 맞는 시설 리스트 조회하기 & 상세 페이지 읽기
	private void selectedFacilityList(String facilityCode, int regionLargeCode, String regionSmallCode){
		outer : while(true){
			 List<Map<String, Object>> list = facilityViewDao.facilityViewList(facilityLargeCode, facilityCode, regionLargeCode, regionSmallCode);
			 selectedCondition();
			 System.out.println("=================================================================");
			 System.out.println("NO\t이름 - [위치]");
			 System.out.println("-----------------------------------------------------------------");
			 for(int i = 0; i < list.size(); i++){ 
				 Map<String, Object> row = list.get(i);
				 System.out.println(row.get("RM")
						 + "\t" + row.get("FACILITY_NAME") + " - [" + row.get("FACILITY_ADDRESS") + "]");
			 }
			 System.out.println("=================================================================");
			 System.out.println("1.조회\t0.뒤로가기");
			 System.out.println("=================================================================");
			 System.out.println("입력>");
			 int input3 = ScanUtil.nextInt();
			 switch(input3){
			 	case 1 : System.out.println("번호 입력>");
			 			 int rn = ScanUtil.nextInt();
			 			 Map<String, Object> row2 = list.get(rn - 1);
			 			 String fid = (String)row2.get("FACILITY_ID");
			 			 facilityRead(fid);
			 		  	 break;
			 	case 0 : break outer;
			 } 
		 }
	}
	
//	// 시설 상세 페이지 읽기
//	private void facilityRead(String facilityCode){
//		Map<String, Object> row = facilityViewDao.facilityViewRead(facilityCode);
//		List<Map<String, Object>> list = facilityViewDao.facilityViewComments(facilityCode);
//		System.out.println("=================================================================");
//		System.out.println("시설 이름         : " 	+ row.get("FACILITY_NAME") + 			"\n" + 
//						   "내용  \n" 		+ row.get("FACILITY_CONTENT") + 		"\n" +
//						   "사업자등록번호 : " 	+ row.get("FACILITY_COMPANY_NUMBER") + 	"\n" +
//						   "여는 시간         : " 	+ row.get("FACILITY_START_TIME") + 		"\n" +
//						   "닫는 시간         : "	+ row.get("FACILITY_END_TIME") + 		"\n" +
//						   "오전 이용료      : " 	+ row.get("FACILITY_AM_PAY") + 			"\n" +
//						   "오후 이용료      : " 	+ row.get("FACILITY_PM_PAY") + 			"\n" +
//						   "연락처             : " 	+ row.get("FACILITY_PHONE_NUMBER") + 	"\n" +
//						   "우편주소          : " 	+ row.get("FACILITY_POSTCODE") + 		"\n" +
//						   "기본주소          : " 	+ row.get("FACILITY_ADDRESS") + 		"\n" +
//						   "상세주소          : "	+ row.get("FACILITYADDRESSDETAIL") +	"\n" +
//						   "시설분류          : " 	+ row.get("FACILITY_CODE") + 			"\n" +
//						   "시설관리자       : " 	+ row.get("USER_ID"));	
//		System.out.println("-----------------------------------------------------------------");
//		if(list.size() > 0){
//			for(int i = 0; i < list.size(); i++){
//				Map<String, Object> row2 = list.get(i);
//				System.out.println(row2.get("RN") + " | " + row2.get("COMMENTS_CONTENT") + " | " +  
//								   row2.get("USER_ID"));
////				System.out.println("-----------------------------------------------------------------");
//			}
//		}else{
//			System.out.println("<작성된 코멘트가 없습니다.>");
//		}
//		System.out.println("=================================================================");
//		if ((Controller.loginUser == null || Controller.loginUser.size() == 0)
//				&& (Controller.loginManager == null || Controller.loginManager.size() ==0) ) {
//			System.out.println("0. 뒤로가기");
//			System.out.println("입력 >");
//			int input  = ScanUtil.nextInt();
//			switch (input) {
//				case 0:break;
//			}
//
//		}
//		else if((Controller.loginManager == null || Controller.loginManager.size() == 0) && "USER".equals(Controller.loginUser.get("USER_TYPE"))){
//			System.out.println("1.예약\t\t0.뒤로가기");
//			System.out.println("=================================================================");
//			int input = ScanUtil.nextInt();
//			switch(input){
//				case 1 : Map<String, Object> row3 = new HashMap<>(); // 예약 중복을 막기 위해 로그인한 유저에게 승인,승인대기 상태인 예약이 있는지 확인함
//						 row3 = facilityReservationDao.facilityReservationOverlapCheck(Controller.loginUser.get("USER_ID")); 
//						 if(row3 != null){
//							 System.out.println("<" + row3.get("RESERVATION_STATE") + " 상태인 예약 정보가 존재합니다.>");
//						 } else {
//							 facilityReservationService.facilityReservation((String)row.get("FACILITY_ID")); // 진행중인 예약이 없는 경우에 예약을 진행
//						 }
//						 break;
//				case 0 : break ;
//			}
//		} else if ((Controller.loginUser == null || Controller.loginUser.size() == 0) && "MANAGER".equals(Controller.loginManager.get("MANAGER_ID"))){
//			System.out.println("1.수정\t\t2.삭제\t\t0.뒤로가기");
//			System.out.println("=================================================================");
//			int input = ScanUtil.nextInt();
//			switch(input){
//			case 1 : break;
//			case 2 : break;
//			case 0 : break ;
//			}
//		} else {
//			System.out.println("0.뒤로가기");
//			System.out.println("=================================================================");
//			int input = ScanUtil.nextInt();
//			switch(input){
//			case 0 : break ;
//			}
//		}
//	}
	

	// 시설 상세 페이지 읽기
	private void facilityRead(String facilityCode){
		a : while(true){
		Map<String, Object> row = facilityViewDao.facilityViewRead(facilityCode);
		List<Map<String, Object>> list = facilityViewDao.facilityViewComments(facilityCode);
		System.out.println("=================================================================");
		System.out.println("1.시설 이름 : " 		+ row.get("FACILITY_NAME") + 			"\n" + 
						   "2.내용  \n" 		+ row.get("FACILITY_CONTENT") + 		"\n" +
						   "3.오픈시간 : " 	+ row.get("FACILITY_START_TIME") + 	"\n" +
						   "4.마감시간 : " 		+ row.get("FACILITY_END_TIME") + 		"\n" +
						   "5.사업자등록증 : "		+ row.get("FACILITY_COMPANY_NUMBER") + 		"\n" +
						   "6.오전요금 : " 	+ row.get("FACILITY_AM_PAY") + 			"\n" +
						   "7.오후요금 : " 	+ row.get("FACILITY_PM_PAY") + 			"\n" +
						   "8.우편번호 : " 		+ row.get("FACILITY_POSTCODE") + 	"\n" +
						   "9.시설주소 : " 		+ row.get("FACILITY_ADDRESS") + 		"\n" +
						   "10.상세주소 : " 		+ row.get("FACILITYADDRESSDETAIL") + 		"\n" +
						   "11.휴대폰번호 : "		+ row.get("FACILITY_PHONE_NUMBER") +	"\n" +
						   "12.승인여부 : " 		+ row.get("FACILITY_ALLOW") +	"\n" + 
						   "13.시설코드 : " 	+ row.get("FACILITY_CODE") +	"\n" +
						   "14.지역코드 : "   + row.get("REGION_CODE"));
		System.out.println("-----------------------------------------------------------------");
		if(list.size() > 0){
			for(int i = 0; i < list.size(); i++){
				Map<String, Object> row2 = list.get(i);
				System.out.println(row2.get("RN") + " | " + row2.get("COMMENTS_CONTENT") + " | " +  
								   row2.get("USER_ID"));
			}
		}else{
			System.out.println("<작성된 코멘트가 없습니다.>");
		}
		System.out.println("=================================================================");
		if ((Controller.loginUser == null || Controller.loginUser.size() == 0)
				&& (Controller.loginManager == null || Controller.loginManager.size() ==0) ) {
			System.out.println("0. 뒤로가기");
			int input  = ScanUtil.nextInt();
			switch (input) {
				case 0:break;
			}

		}
		else if((Controller.loginManager == null || Controller.loginManager.size() == 0) && "USER".equals(Controller.loginUser.get("USER_TYPE"))){
			System.out.println("1.예약\t\t0.뒤로가기");
			System.out.println("=================================================================");
			int input = ScanUtil.nextInt();
			switch(input){
				case 1 : Map<String, Object> row3 = new HashMap<>(); // 예약 중복을 막기 위해 로그인한 유저에게 승인,승인대기 상태인 예약이 있는지 확인함
						 row3 = facilityReservationDao.facilityReservationOverlapCheck(Controller.loginUser.get("USER_ID")); 
						 if(row3 != null){
							 System.out.println("<" + row3.get("RESERVATION_STATE") + " 상태인 예약 정보가 존재합니다.>");
						 } else {
							 facilityReservationService.facilityReservation((String)row.get("FACILITY_ID")); // 진행중인 예약이 없는 경우에 예약을 진행
						 }
						 break;
				case 0 : break a;
			  }
			
		 	} else if ((Controller.loginUser == null || Controller.loginUser.size() == 0) && "MANAGER".equals(Controller.loginManager.get("MANAGER_ID"))){
			System.out.println("1.수정\t\t2.삭제\t\t0.뒤로가기");
			System.out.println("=================================================================");
			int input = ScanUtil.nextInt();
			switch(input){
			case 1 : 
				facilityViewUpdate(facilityCode);
				break ;
			case 2 : // 삭제
				facilityViewDelete(facilityCode);	
				break a;
			case 0 :
				break a;
			}
			
		} else {
			System.out.println("0.뒤로가기");
			System.out.println("=================================================================");
			int input = ScanUtil.nextInt();
			switch(input){
			case 0 : break ;
			}
		}
	  }//while
	}
	


	private void facilityViewUpdate(String facilityCode) {
		String facility_select_code;
		String facility_region_code = null;
		
		System.out.println("변경할 항목 번호를 입력해주세요>");
		int updateNo = ScanUtil.nextInt();
		
		switch (updateNo) {
		case 1:
			System.out.println("시설명>");
			String name = ScanUtil.nextLine();
			Map<String,Object> n_param = new HashMap<>();
			n_param.put("FACILITY_NAME", name);
			int n_result = facilityViewDao.viewNameupdate(facilityCode, name);
			break;
		case 2:
			System.out.println("시설내용>");
			String content= ScanUtil.nextLine();
			Map<String,Object> c_param = new HashMap<>();
			c_param.put("FACILITY_CONTENT", content);
			int c_result = facilityViewDao.viewContentupdate(facilityCode, content);
			break;
		case 3:
			System.out.println("시설오픈시간");
			String openTime = ScanUtil.nextLine();
			Map<String,Object> ot_param = new HashMap<>();
			ot_param.put("FACILITY_START_TIME", openTime);
			int ot_result = facilityViewDao.viewOpenTimeupdate(facilityCode, openTime);
			break;
		case 4:
			System.out.println("시설마감시간");
			String closeTime = ScanUtil.nextLine();
			Map<String,Object> ct_param = new HashMap<>();
			ct_param.put("FACILITY_END_TIME", closeTime);
			int ct_result = facilityViewDao.viewCloseTimeupdate(facilityCode, closeTime);
			break;
		case 5:
            System.out.println("사업자등록증은 수정할 수 없습니다.");
            break;
		case 6:
			System.out.println("오전요금");
			int AmPay = ScanUtil.nextInt();
			Map<String,Object> ap_param = new HashMap<>();
			ap_param.put("FACILITY_AM_PAY", AmPay);
			int ap_result = facilityViewDao.viewAMPayupdate(facilityCode, AmPay);
			break;
		case 7:
			System.out.println("오후요금");
			int PmPay = ScanUtil.nextInt();
			Map<String,Object> pp_param = new HashMap<>();
			pp_param.put("FACILITY_PM_PAY", PmPay);
			int pp_result = facilityViewDao.viewPMPayupdate(facilityCode, PmPay);
			break;
		case 8:
			System.out.println("우편번호");
			int postNumber = ScanUtil.nextInt();
			Map<String,Object> pn_param = new HashMap<>();
			pn_param.put("FACILITY_POSTCODE", postNumber);
			int pn_result = facilityViewDao.viewPostCodeupdate(facilityCode, postNumber);
			break;
		case 9:
			System.out.println("주소");
			String address = ScanUtil.nextLine();
			Map<String,Object> add_param = new HashMap<>();
			add_param.put("FACILITY_ADDRESS", address);
			int add_result = facilityViewDao.viewAddressupdate(facilityCode, address);
			break;
		case 10:
			System.out.println("상세주소");
			String address_detail = ScanUtil.nextLine();
			Map<String,Object> add2_param = new HashMap<>();
			add2_param.put("FACILITY_ADDRESS_Detail", address_detail);
			int add2_result = facilityViewDao.viewAddressDetailupdate(facilityCode, address_detail);
			break;
		case 11:
			System.out.println("휴대폰번호");
			String phoneNumber = ScanUtil.nextLine();
			Map<String,Object> phoneNum_param = new HashMap<>();
			phoneNum_param.put("FACILITY_PHONE_NUMBER", phoneNumber);
			int phoneNum_result = facilityViewDao.viewPhoneNumberupdate(facilityCode, phoneNumber);
			break;
		case 12:
			System.out.println("승인여부는 수정할수 없는 항목입니다.");
			
			break;
		case 13:
			String select_facility_Code = smemMyPageService.selectFacilityList();
			Map<String,Object> facilityCode_param = new HashMap<>();
			facilityCode_param.put("FACILITY_CODE", select_facility_Code);
			int facilityCode_result = facilityViewDao.viewfacilityCodeUpdate(facilityCode, select_facility_Code);
			break;
			
		case 14:
			String select_RegionCode = smemMyPageService.selectRegionList();
			Map<String,Object> RegionCode_param = new HashMap<>();
			RegionCode_param.put("REGION_CODE", select_RegionCode);
			int region_result = facilityViewDao.viewRegionCodeUpdate(facilityCode, select_RegionCode);
			break;

		default:
			System.out.println("번호를 다시 입력해주세요");
			break;
		}
		
	}
	
	
	
	private void facilityViewDelete(String facilityCode) {
		System.out.println("정말 삭제하시겠습니까?");
		System.out.println("1.확인 \t 2.뒤로가기");
	    int input = 0;
	    input = ScanUtil.nextInt();
	    switch (input) {
		case 1:
			int result = facilityViewDao.viewMyFacDelete(facilityCode);
			System.out.println("삭제가 완료되었습니다.");
			break;
		case 2:
            break;
		default:
			break;
		}
		
	}
	
	
	// 검색 조건 문장으로 출력하기
	private void selectedCondition(){
		if (facilityCode == null){
			facilityCodeName = "전체";
		}
		if (regionLargeCode == 0){
			regionLargeCodeName = "전체";
		}
		if (regionSmallCode == null){
			regionSmallCodeName = "전체";
		}
		System.out.println("<" + regionLargeCodeName + " 광역시 " + regionSmallCodeName + " 지역의 " +
						   facilityLargeName + " 중 " + facilityCodeName + " 검색 결과입니다.>" );
	}
	
}	
	
