package service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import util.ScanUtil;
import controller.Controller;
import dao.FacilityReservationDao;

public class FacilityReservationService {
	
	private FacilityReservationService(){}
	
	private static FacilityReservationService instance;
	
	public static FacilityReservationService getInstance(){
		if(instance == null){
			instance = new FacilityReservationService();
		}
		return instance;
	}
	
	private FacilityReservationDao facilityReservationDao = FacilityReservationDao.getInstance();
	
	String reservationDate = null;
	int feeSelected = 0;
	String timeSelected = null;
	boolean reservationState = false;
	
	
	
	// 예약 &결제
	// 메서드 길이가 길고, USER 회원에게만 가능한 기능이기에 다른 클래스로 분리함
	 
	public void facilityReservation1(String facilityID, String resNo) {
			
			boolean flag = false; // 예약 가능한 날짜, 시간대를 확인하고 조건을 만족한 true인 경우에만 다음 단계로 넘어갈 수 있음
			
			reservationDate = reservationDateCheck(); // 날짜 입력받고 정규표현식 체크

			// 예약일자가 오늘 이전이라면 flag = false. 예약을 진행할 수 없다. (Date 클래스의 compareTo()메서드로 날짜를 비교하는 방식)
			try {
				SimpleDateFormat dateForm = new SimpleDateFormat("yyyy-MM-dd");
				Date today = new Date();
				Date rDate = dateForm.parse(reservationDate);
				if (rDate.compareTo(today) > 0) { 			// 정상 예약 : 오늘 후
					flag = true;
				} else if (rDate.compareTo(today) < 0) { 	// 예약 불가 : 오늘 전 -- 상세 페이지로 돌아가기
					System.out.println("<예약이 불가능한 날짜입니다>");
					return;
				} else if (rDate.compareTo(today) == 0) { 	// 예약 불가 : 오늘 -- 상세 페이지로 돌아가기
					System.out.println("<예약이 불가능한 날짜입니다>");
					return;
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}

			if (flag) {
				// 일자에 따른 스케줄을 가져온다.
				List<Map<String, Object>> list = facilityReservationDao
						.facilityReservationSchedule(reservationDate, facilityID);
				String availabilityAM = "예약 가능";
				String availabilityPM = "예약 가능";
				

				if (list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						Map<String, Object> row = list.get(i);
						if (row.get("RESERVATION_AMPM").equals("AM")) {
							availabilityAM = "예약 불가능";
						}
						if (row.get("RESERVATION_AMPM").equals("PM")) {
							availabilityPM = "예약 불가능";
						}
					}
				}

				Map<String, Object> fee = facilityReservationDao.facilityFee(facilityID); // 요금 불러오기
				int feeAM = Integer.parseInt(String.valueOf(fee.get("FACILITY_AM_PAY"))); 
				int feePM = Integer.parseInt(String.valueOf(fee.get("FACILITY_PM_PAY")));
				
				
				schedule1 : 
				while(true){
					System.out.println("<" + reservationDate + " 예약 스케줄>"); // 사용자가 선택한 일자 표시
					System.out.println("1.오전(" + feeAM + ") : " + availabilityAM); 
					System.out.println("2.오후(" + feePM + ") : " + availabilityPM);
					System.out.println("1.시간 선택\t\t0.뒤로 가기");
					int no = ScanUtil.nextInt();
					
					switch (no) {
					case 1 : int no2 = 0;
							 schedule2 : 
							 while(true){
							 	System.out.println("스케줄 번호 입력>");
								no2 = ScanUtil.nextInt(); // 예약 불가능 시간대를 선택하면 flag = false. 결제 단계로 넘어갈 수 없음
								switch (no2) {
								case 1 : if (availabilityAM.equals("예약 가능")) {
							 				flag = true;
							 				feeSelected = feeAM;
							 				timeSelected = "AM";
										 } else {
											System.out.println("<예약이 불가능한 시간대입니다>");
											flag = false;
										 }
							 	  		   break schedule2;
								case 2 : if (availabilityPM.equals("예약 가능")) {
							 				flag = true;
							 				feeSelected = feePM;
							 				timeSelected = "PM";
							 	  		 } else {
							 	  			 System.out.println("<예약이 불가능한 시간대입니다>");
							 	  			 flag = false;
							 	  		 }
										   break schedule2;
								default : System.out.println("<잘못된 입력 값입니다>"); break;
								}
							 }
							 
							 if (flag) {
								 reservationPay1(facilityID, resNo);
							 }
							 if(reservationState){	// 예약이 완료될 경우 예약 메서드를 완전히 빠져나가고, 예약 중간에 뒤로가기로 빠져나온 경우 다시 스케줄 설정부터 시작한다. 
								 break schedule1; 
							 } else {
								 break;
							 }
							 
					case 0 : break schedule1;
					default: System.out.println("<올바르지 않은 입력 값입니다>");
							 break;
					}
				}
				
			}
		}	 
	 
	
	private void reservationPay1(String facilityID, String resNo){
		 payTypeSelect : 
		 while(true){
			 String payTypeCode = null;
			 System.out.println("<결제 수단을 선택해주세요>");
			 System.out.println("1.카카오 페이\n2.무통장 입금\n3.신용카드\n0.뒤로가기");	// (추가) 0.뒤로가기 - 예약 스케줄
			 int payType = ScanUtil.nextInt();
			 switch (payType) { // 여기서 모은 결제 정보는 테이블에 저장하지 않음. 추후에 시간 여유가 있으면 테이블 생성.
				 case 1: payTypeCode = "PT1";
						 phoneNumberCheck(); 	// 핸드폰 번호를 입력받고 정규표현식 체크
						 break;
				 case 2: payTypeCode = "PT2";
						 System.out.println("입금 은행 : 하나은행");
						 System.out.println("계좌 번호 : 777-7777-7777");
						 break;
				 case 3: payTypeCode = "PT3";
						 System.out.println("1.KB국민카드\n2.신한카드\n3.하나카드\n4.현대카드\n5.삼성카드"); 
						 System.out.println("카드사 선택 >");
						 int cardCompany = ScanUtil.nextInt();
						 cardNumberCheck(); 		// 카드 번호를 입력받고 정규표현식 체크
						 cardExpiryDateCheck();		// 카드 유효기간 입력받고 정규표현식 체크
						 cardCVCCheck();			// 카드 CVC코드 입력받고 정규표현식 체크
						 cardPasswordCheck(); 		// 카드 비밀번호 입력받고 정규표현식 체크
						 break;
				 case 0: break payTypeSelect;
				 default: System.out.println("<올바르지 않은 입력 값입니다>");
				 		  break; 
			 }
			 
			 // 결제타입 공통 사항 - 환불계좌
			 String refundAccount = null;
			 String refundBankName = null;
			 refund : 
			 while(true){
				 System.out.println("<환불계좌를 입력해주세요>");
				 System.out.println("1.KB국민은행\n2.신한은행\n3.하나은행\n4.우리은행\n5.농협\n0.뒤로가기"); // (추가) 0.뒤로가기 - 결제정보
				 System.out.println("은행 선택>");
				 int refundBank = ScanUtil.nextInt();
				 
				 switch(refundBank){
				 	case 1 : refundBankName = "KB국민은행";
				 			 break;
				 	case 2 : refundBankName = "신한은행";
				 			 break;
				 	case 3 : refundBankName = "하나은행";
				 			 break;
				 	case 4 : refundBankName = "우리은행";
				 			 break;
				 	case 5 : refundBankName = "농협";
				 		  	 break;
				 	case 0 : break refund;
				 	default : System.out.println("<올바르지 않은 입력 값입니다>");
				 			  break; 
				 }
				 refundAccount = refundAccountCheck(); // 계좌번호 입력받고 정규표현식 체크
				 
				// 모든 예약 정보를 Dao의 업데이트 메서드에 보내기
				 Map<String, Object> p = new HashMap<>();
				 p.put("RESERVATION_DATE", reservationDate);
				 p.put("USER_ID", Controller.loginUser.get("USER_ID"));
				 p.put("FACILITY_ID", facilityID);
				 p.put("RESERVATION_REFUND_ACCOUNT", refundBankName + " " + refundAccount);
				 p.put("RESERVATION_PAY_PEE", feeSelected);
				 p.put("PAYTYPE_CODE", payTypeCode);
				 p.put("RESERVATION_AMPM", timeSelected);
				 facilityReservationDao.facilityReservationUpdate(p);
				 facilityReservationDao.reservationDie(resNo);
				 
				 
				 System.out.println("<예약 요청을 완료하였습니다>");
				 reservationState = true;
				 return;
			 }
		 }
	} 
	 
	public void facilityReservation(String facilityID) {
		
		boolean flag = false; // 예약 가능한 날짜, 시간대를 확인하고 조건을 만족한 true인 경우에만 다음 단계로 넘어갈 수 있음
		
		reservationDate = reservationDateCheck(); // 날짜 입력받고 정규표현식 체크

		// 예약일자가 오늘 이전이라면 flag = false. 예약을 진행할 수 없다. (Date 클래스의 compareTo()메서드로 날짜를 비교하는 방식)
		try {
			SimpleDateFormat dateForm = new SimpleDateFormat("yyyy-MM-dd");
			Date today = new Date();
			Date rDate = dateForm.parse(reservationDate);
			if (rDate.compareTo(today) > 0) { 			// 정상 예약 : 오늘 후
				flag = true;
			} else if (rDate.compareTo(today) < 0) { 	// 예약 불가 : 오늘 전 -- 상세 페이지로 돌아가기
				System.out.println("<예약이 불가능한 날짜입니다>");
				return;
			} else if (rDate.compareTo(today) == 0) { 	// 예약 불가 : 오늘 -- 상세 페이지로 돌아가기
				System.out.println("<예약이 불가능한 날짜입니다>");
				return;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		if (flag) {
			// 일자에 따른 스케줄을 가져온다.
			List<Map<String, Object>> list = facilityReservationDao
					.facilityReservationSchedule(reservationDate, facilityID);
			String availabilityAM = "예약 가능";
			String availabilityPM = "예약 가능";
			

			if (list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					Map<String, Object> row = list.get(i);
					if (row.get("RESERVATION_AMPM").equals("AM")) {
						availabilityAM = "예약 불가능";
					}
					if (row.get("RESERVATION_AMPM").equals("PM")) {
						availabilityPM = "예약 불가능";
					}
				}
			}

			Map<String, Object> fee = facilityReservationDao.facilityFee(facilityID); // 요금 불러오기
			int feeAM = Integer.parseInt(String.valueOf(fee.get("FACILITY_AM_PAY"))); 
			int feePM = Integer.parseInt(String.valueOf(fee.get("FACILITY_PM_PAY")));
			
			
			schedule1 : 
			while(true){
				System.out.println("<" + reservationDate + " 예약 스케줄>"); // 사용자가 선택한 일자 표시
				System.out.println("1.오전(" + feeAM + ") : " + availabilityAM); 
				System.out.println("2.오후(" + feePM + ") : " + availabilityPM);
				System.out.println("1.시간 선택\t\t0.뒤로 가기");
				int no = ScanUtil.nextInt();
				
				switch (no) {
				case 1 : int no2 = 0;
						 schedule2 : 
						 while(true){
						 	System.out.println("스케줄 번호 입력>");
							no2 = ScanUtil.nextInt(); // 예약 불가능 시간대를 선택하면 flag = false. 결제 단계로 넘어갈 수 없음
							switch (no2) {
							case 1 : if (availabilityAM.equals("예약 가능")) {
						 				flag = true;
						 				feeSelected = feeAM;
						 				timeSelected = "AM";
									 } else {
										System.out.println("<예약이 불가능한 시간대입니다>");
										flag = false;
									 }
						 	  		   break schedule2;
							case 2 : if (availabilityPM.equals("예약 가능")) {
						 				flag = true;
						 				feeSelected = feePM;
						 				timeSelected = "PM";
						 	  		 } else {
						 	  			 System.out.println("<예약이 불가능한 시간대입니다>");
						 	  			 flag = false;
						 	  		 }
									   break schedule2;
							default : System.out.println("<잘못된 입력 값입니다>"); break;
							}
						 }
						 
						 if (flag) {
							 reservationPay(facilityID);
						 }
						 if(reservationState){	// 예약이 완료될 경우 예약 메서드를 완전히 빠져나가고, 예약 중간에 뒤로가기로 빠져나온 경우 다시 스케줄 설정부터 시작한다. 
							 break schedule1; 
						 } else {
							 break;
						 }
						 
				case 0 : break schedule1;
				default: System.out.println("<올바르지 않은 입력 값입니다>");
						 break;
				}
			}
			
		}
	}
	
	
	// 결제
	private void reservationPay(String facilityID){
		 payTypeSelect : 
		 while(true){
			 String payTypeCode = null;
			 System.out.println("<결제 수단을 선택해주세요>");
			 System.out.println("1.카카오 페이\n2.무통장 입금\n3.신용카드\n0.뒤로가기");	// (추가) 0.뒤로가기 - 예약 스케줄
			 int payType = ScanUtil.nextInt();
			 switch (payType) { // 여기서 모은 결제 정보는 테이블에 저장하지 않음. 추후에 시간 여유가 있으면 테이블 생성.
				 case 1: payTypeCode = "PT1";
						 phoneNumberCheck(); 	// 핸드폰 번호를 입력받고 정규표현식 체크
						 break;
				 case 2: payTypeCode = "PT2";
						 System.out.println("입금 은행 : 하나은행");
						 System.out.println("계좌 번호 : 777-7777-7777");
						 break;
				 case 3: payTypeCode = "PT3";
						 System.out.println("1.KB국민카드\n2.신한카드\n3.하나카드\n4.현대카드\n5.삼성카드"); 
						 System.out.println("카드사 선택 >");
						 int cardCompany = ScanUtil.nextInt();
						 cardNumberCheck(); 		// 카드 번호를 입력받고 정규표현식 체크
						 cardExpiryDateCheck();		// 카드 유효기간 입력받고 정규표현식 체크
						 cardCVCCheck();			// 카드 CVC코드 입력받고 정규표현식 체크
						 cardPasswordCheck(); 		// 카드 비밀번호 입력받고 정규표현식 체크
						 break;
				 case 0: break payTypeSelect;
				 default: System.out.println("<올바르지 않은 입력 값입니다>");
				 		  break; 
			 }
			 
			 // 결제타입 공통 사항 - 환불계좌
			 String refundAccount = null;
			 String refundBankName = null;
			 refund : 
			 while(true){
				 System.out.println("<환불계좌를 입력해주세요>");
				 System.out.println("1.KB국민은행\n2.신한은행\n3.하나은행\n4.우리은행\n5.농협\n0.뒤로가기"); // (추가) 0.뒤로가기 - 결제정보
				 System.out.println("은행 선택>");
				 int refundBank = ScanUtil.nextInt();
				 
				 switch(refundBank){
				 	case 1 : refundBankName = "KB국민은행";
				 			 break;
				 	case 2 : refundBankName = "신한은행";
				 			 break;
				 	case 3 : refundBankName = "하나은행";
				 			 break;
				 	case 4 : refundBankName = "우리은행";
				 			 break;
				 	case 5 : refundBankName = "농협";
				 		  	 break;
				 	case 0 : break refund;
				 	default : System.out.println("<올바르지 않은 입력 값입니다>");
				 			  break; 
				 }
				 refundAccount = refundAccountCheck(); // 계좌번호 입력받고 정규표현식 체크
				 
				// 모든 예약 정보를 Dao의 업데이트 메서드에 보내기
				 Map<String, Object> p = new HashMap<>();
				 p.put("RESERVATION_DATE", reservationDate);
				 p.put("USER_ID", Controller.loginUser.get("USER_ID"));
				 p.put("FACILITY_ID", facilityID);
				 p.put("RESERVATION_REFUND_ACCOUNT", refundBankName + " " + refundAccount);
				 p.put("RESERVATION_PAY_PEE", feeSelected);
				 p.put("PAYTYPE_CODE", payTypeCode);
				 p.put("RESERVATION_AMPM", timeSelected);
				 facilityReservationDao.facilityReservationUpdate(p);

				 
				 
				 System.out.println("<예약 요청을 완료하였습니다>");
				 reservationState = true;
				 return;
			 }
		 }
	}
	
	// 날짜 정규표현식 체크
	private String reservationDateCheck(){
		while (true) {
			System.out.println("일자를 입력해주세요(YYYY-MM-DD)>"); 
			String reservationDate = ScanUtil.nextLine();
			String phoneRegrex = "^[0-9][0-9][0-9][0-9]\\-[0-9][0-9]\\-[0-9][0-9]$";
			Matcher matcher = Pattern.compile(phoneRegrex).matcher(reservationDate);
			if (matcher.matches() == false) {
				System.out.println("일자 형식에 맞춰 입력해주세요.");
			} else {
				return reservationDate;
			}
		}
	}

	// 휴대폰 번호 정규표현식 체크
	private String phoneNumberCheck(){
		while (true) {
			System.out.print("휴대폰 번호 입력(010-1234-5678)>");
			String phoneNumber = ScanUtil.nextLine();
			String phoneRegrex = "^01([0|1|6|7|8|9])-?([0-9]{3,4})-?([0-9]{4})$";
			Matcher matcher = Pattern.compile(phoneRegrex).matcher(phoneNumber);
			if (matcher.matches() == false) {
				System.out.println("전화번호 형식은 010-1234-5678 입니다.");
			} else {
				return phoneNumber;
			}
		}
	}
		
	// 신용카드 번호 정규표현식 체크 (14~16자리 숫자 입력) 
	private String cardNumberCheck(){
		while (true) {
			System.out.println("신용카드 번호 입력(14~16자리 숫자)>");
			String cardNumber = ScanUtil.nextLine();
			String cardRegrex = "^[0-9]{14,16}$";
			Matcher matcher = Pattern.compile(cardRegrex).matcher(cardNumber);
			if (matcher.matches() == false) {
				System.out.println("14~16 자리 숫자만 입력해주세요.");
			} else {
				return cardNumber;
			}
		}
	}

	// 유효기간 정규표현식 체크
	private String cardExpiryDateCheck() {
		while (true) {
			System.out.println("신용카드 유효기간 입력(MMYY)>");
			String cardExpiryDate = ScanUtil.nextLine();
			String cardRegrex = "^[0-9]{4}$";
			Matcher matcher = Pattern.compile(cardRegrex).matcher(
					cardExpiryDate);
			if (matcher.matches() == false) {
				System.out.println("월, 년도를 총 4자리 숫자로 입력해주세요.");
			} else {
				return cardExpiryDate;
			}
		}
	}

	// CVC코드 정규표현식 체크
	private String cardCVCCheck() {
		while (true) {
			System.out.println("신용카드 CVC코드 입력>");
			String cardCVC = ScanUtil.nextLine();
			String cardRegrex = "^[0-9]{3}$";
			Matcher matcher = Pattern.compile(cardRegrex).matcher(cardCVC);
			if (matcher.matches() == false) {
				System.out.println("CVC코드는 카드 뒷면의 3자리 숫자입니다.");
			} else {
				return cardCVC;
			}
		}
	}

	// 비밀번호 정규표현식 체크
	private String cardPasswordCheck() {
		while (true) {
			System.out.println("신용카드 비밀번호 입력>");
			String cardPassword = ScanUtil.nextLine();
			String cardRegrex = "^[0-9]{6}$";
			Matcher matcher = Pattern.compile(cardRegrex).matcher(cardPassword);
			if (matcher.matches() == false) {
				System.out.println("신용카드 비밀번호는 6자리 숫자입니다.");
			} else {
				return cardPassword;
			}
		}
	}

	// 계좌번호 정규표현식 체크
	private String refundAccountCheck() {
		while (true) {
			System.out.println("계좌번호 번호 입력(11~14자리 숫자)>");
			String refundAccount = ScanUtil.nextLine();
			String accountRegrex = "^[0-9]{11,14}$";
			Matcher matcher = Pattern.compile(accountRegrex).matcher(
					refundAccount);
			if (matcher.matches() == false) {
				System.out.println("11~14 자리 숫자만 입력해주세요.");
			} else {
				return refundAccount;
			}
		}
	}
		
	
	
}