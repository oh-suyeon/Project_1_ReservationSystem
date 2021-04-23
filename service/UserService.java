package service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import controller.Controller;
import util.ScanUtil;
import util.View;
import dao.UserDao;

public class UserService {

	private UserService() {}

	private static UserService instance;

	public static UserService getInstance() {
		if (instance == null) {
			instance = new UserService();
		}
		return instance;
	}

	private UserDao userDao = UserDao.getInstance();
	SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
	Date time = new Date();
	private FacilityReservationService facilityReservationService = FacilityReservationService.getInstance();
	private RentalRecordService rentalRecordService = RentalRecordService.getInstance();
	private SmemMyPageService smemMyPageService = SmemMyPageService.getInstance();
	
	public int join() {
		String userId;
		String password;
		String userName;
		String phonecall;
		String postNumber;
		String address;
		String addressDetail;
		int code = 0;

		System.out.println("==============회원가입===================");

		// 회원코드
		System.out.println("회원코드");
		System.out.println("1.회원 \t2.시설주 ");
		System.out.print("입력>");
		code = ScanUtil.nextInt();
		switch (code) {
		case 1:
			System.out.println("회원을 선택하셨습니다.");
			break;
		case 2:
			System.out.println("시설주를 선택하셨습니다.");
			break;

		default:
			System.out.println("잘못입력하셨습니다");
			break;
		}

		// 아이디
			while(true){
			    while(true){
					System.out.print("아이디>");
				    userId = ScanUtil.nextLine(); // 아이디입력
				    String IdRegrex = "^[a-z0-9_-]{3,16}$"; 
				    // 정규표현식입력 //소문자,// 숫자,_-포함,3글자 이상 16글자 이하
				    Matcher matcher = Pattern.compile(IdRegrex).matcher(userId);
					if (matcher.matches() == false) {
					    System.out.println("3~16자의 영문 소문자, 숫자와 특수기호(_),(-)만 사용 가능합니다. ");
					} else {
				    	 break;
				    }
			    }
			    
			    Map<String, Object> user = userDao.selectUser2(userId);
				Map<String, Object> check = userDao.checkId(userId);
				if (check != null && user.get("USER_DELETE").equals("0")) {
					System.out.println("중복되는 아이디입니다.");
				}else if(check != null && user.get("USER_DELETE").equals("1")){
					break;
				}else {
					break;
				}   
		  }

	
	

		// 비밀번호
		while (true) {
			while (true) {
				System.out.print("비밀번호>");
				password = ScanUtil.nextLine(); // 아이디입력
				String passwordRegrex = "^[a-z0-9_-]{6,18}$"; // 정규표현식입력//소문자.숫자.//_-포함
																// ,6글자이상//18글자
																// 이하
				Matcher matcher1 = Pattern.compile(passwordRegrex).matcher(
						password);

				if (matcher1.matches() == false) {
					System.out.println("6~18자 영문 소문자, 숫자, 특수기호(-),(_)를 사용하세요.");
				} else {
					break;
				}
			}
			// 정규표현식과 userId를 matcher라는것을 비교해서 matcher라는 변수에 담음
			// matcher의 matches 라는 메소드를 호출하면 false 라는 값이 나옴 (일치하지않을시에)
			System.out.print("비밀번호확인>");
			String passwordcheck = ScanUtil.nextLine();

			if (!password.equals(passwordcheck)
					&& !password.equals(passwordcheck)) {
				System.out.println("비밀번호가 일치하지 않습니다");
			} else {
				break;
			}
		}

		// 이름
		while (true) {
			System.out.print("이름>");
			userName = ScanUtil.nextLine();
			String nameRegrex = "^[가-힣]*$";
			Matcher matcher2 = Pattern.compile(nameRegrex).matcher(userName);

			if (matcher2.matches() == false) {
				System.out.println("한글 문자만 사용가능합니다");
			} else {
				break;
			}
		}

		// 전화번호
		while (true) {
			System.out.print("전화번호>");
			phonecall = ScanUtil.nextLine();
			String phoneRegrex = "^01([0|1|6|7|8|9])-?([0-9]{3,4})-?([0-9]{4})$";// 시작을숫자 01로 시작하며 그후에 0,1,6,7,8,9 중에 하나가 나올수도 있으며 /하이픈-하나 존재할수도 있으며/ 숫자 3~4개 이어지고
			Matcher matcher3 = Pattern.compile(phoneRegrex).matcher(phonecall);
			if (matcher3.matches() == false) {
				System.out.println("전화번호 형식은 010-1234-5678 입니다.");
			} else {
				break;
			}
		}

		// 우편번호
		while (true) {
			System.out.print("우편번호>");
			postNumber = ScanUtil.nextLine();
			String phoneRegrex = "[0-9]{5}";
			Matcher matcher4 = Pattern.compile(phoneRegrex).matcher(postNumber);
			if (matcher4.matches() == false) {
				System.out.println("우편번호 5자리를 정확히 입력해주세요");
			} else {
				break;
			}
		}

		// 주소
		while (true) {
			System.out.print("주소>");
			address = ScanUtil.nextLine();
			if (address.equals("")) {
				System.out.println("필수항목입니다. 주소를 입력해주세요");
			} else {
				break;
			}
		}

		System.out.print("상세주소>");
		addressDetail = ScanUtil.nextLine();

		// 해시맵에 입력데이터 넣기
		Map<String, Object> param = new HashMap<>();
		
		// 탈퇴된 아이디를 사용하는 회원가입
//		Map<String, Object> check = userDao.checkIdDelete(userId);


		// 일반적인 회원가입
		if (code == 1) {
			String member = "USER";
			param.put("USER_TYPE", member);
		} else if (code == 2) {
			String Smember = "OWNER";
			param.put("USER_TYPE", Smember);
		}
		param.put("USER_ID", userId);
		param.put("USER_PASSWORD", password);
		param.put("USER_NAME", userName);
		param.put("USER_PHONENUMBER", phonecall);
		param.put("USER_POSTCODE", postNumber);
		param.put("USER_ADDRESS", address);
		param.put("USER_ADDRESS_DETAIL", addressDetail);

		int result;

	    Map<String, Object> user = userDao.selectUser2(userId);
		Map<String, Object> check = userDao.checkId(userId);
		if (check != null && user.get("USER_DELETE").equals("1")) {
			result = userDao.updateJoinUser(param);
		}else {
			result = userDao.insertUser(param);
		}
		
		
		
		if (0 < result) {
			System.out.println("회원가입 성공1");
		} else {
			System.out.println("회원가입 실패1");
		}

		return View.HOME;
	}

	
	
	public int login() {
		while (true) {
			System.out.println();
			System.out
					.println("====================로그인=======================");
			System.out.print("아이디>");
			String userId = ScanUtil.nextLine();
			System.out.print("비밀번호>");
			String password = ScanUtil.nextLine();
			if (userId.equals("MANAGER") && password.equals("0000")) {
				Map<String, Object> manager = userDao.manager();	
				System.out.println("관리자 계정으로 로그인하였습니다.");
				Controller.loginManager = manager;
				return View.ManagerHome;
			}else{
				Map<String, Object> user = userDao.loginuser(userId, password);	
				if (user == null ) {
					System.out.println("아이디 혹은 비밀번호를 잘못 입력하셨습니다");
				} 
				else if (user.get("USER_DELETE").equals("1")) {
					System.out.println("탈퇴된 회원입니다.");
				} 
				else {
					System.out.println("로그인 성공");
					Controller.loginUser = user; // 우리가 조회한 user를 저장한다
					return View.HOME;
				}
			}
			return View.LOGIN;
		}

	}

	
	// 내정보 확인
	public int myinfo() {
		while (true) {
			if (Controller.loginUser.size() == 0) {
				return View.HOME;
			}
			if (Controller.loginUser.get("USER_TYPE").equals("USER")) {
				System.out.println("--------------------------------------------------------");
				System.out.println("1.내정보\t\t2.예약\t3.대관내역\t0.뒤로가기");
				System.out.println("--------------------------------------------------------");
				System.out.print  (">");
				int input = ScanUtil.nextInt();
				switch (input) {
				case 1:
					String userId = (String) Controller.loginUser.get("USER_ID");
					String password = " ";
					while (true) {
						System.out.println("비밀번호를 입력해주세요");
						String pass = ScanUtil.nextLine();
						if (Controller.loginUser.get("USER_PASSWORD").equals(pass)) {
							password  = pass;
							break;
						}else{
							System.out.println("비밀번호를 잘못 입력하셨습니다.");
						}
					}
					Map<String, Object> user = userDao.selectUserInfo(userId, password);
					showInfo(user);
					break;
				case 2: 
					myReservation();
					break; // 예약
				case 3: 
					rentalRecordService.rentalRecodeList();
//					reservationList();
					break; // 대관내역 / 코멘트
					
				case 0 : return View.HOME;
				}	
			}else if (Controller.loginUser.get("USER_TYPE").equals("OWNER")) {
				System.out.println("----------------------------------------------");
				System.out.println(" 1.내정보   2.내 시설 3.예약정보 4.월간매출 0.뒤로가기");
				System.out.println("----------------------------------------------");
				System.out.print  (">");
				int input = ScanUtil.nextInt();
				switch (input) {
					case 1:
						String userId = (String) Controller.loginUser.get("USER_ID");
						String password = " ";
						while (true) {
							System.out.println("비밀번호를 입력해주세요");
							String pass = ScanUtil.nextLine();
							if (Controller.loginUser.get("USER_PASSWORD").equals(pass)) {
								password  = pass;
								break;
							}else{
								System.out.println("비밀번호를 잘못 입력하셨습니다.");
							}
						}
						Map<String, Object> user = userDao.selectUserInfo(userId, password);
						showInfo(user);
						break;
					case 2 : 
						smemMyPageService.MyFacInfo();
						break; //내 시설
					case 3 : //예약 승인대기 승인 녀석들이 들어와야함
						changeReervation();
							break;
					case 4 : 
						ownerMoney();
						break; //월간매출
					case 0 : return View.HOME;
				}
			}
			return View.MYINFO;
		}	
	}

	// 시설주 월별 매출 확인
	private void ownerMoney() {
		Q :while (true) {
			System.out.println("월별 매출을 확인합니다.");
			System.out.println("----------------------------------------------");
			// 여기에 수연이가 만들어준거 넣자
			List<Map<String, Object>> money = userDao.selectOwnerMoney();
			System.out.println("년/월\t시설이름\t\t\t매출액");
			for (Map<String, Object> map : money) {
				System.out.print  (map.get("MONTH2") +"\t" );
				System.out.print  (map.get("FACILITY_NAME") +"\t\t\t");
				System.out.println(map.get("SALES"));
			}
			System.out.println("----------------------------------------------");
			System.out.println("1.년/월 입력 0. 뒤로가기");
			int input = ScanUtil.nextInt();
			switch (input) {
			case 1:
				a : while (true) {
//					System.out.println("0000 00 년도 4자리와 월 2자리를 입력해주세요");
					System.out.println("0000 년도를 입력해주세요");
					int input2 = ScanUtil.nextInt();
					input2 = input2 * 100;
					System.out.println("00 월를 입력해주세요");
					int input1 = ScanUtil.nextInt();
					int sum = input2 + input1;
					if (sum >999999) {
						System.out.println("양식에 맞게 넣어주세요");
						break a;
					}else if (sum < 100000) {
						System.out.println("양식에 맞게 넣어주세요");
						break a;
					}
					List<Map<String, Object>> moneyIn = userDao.choiseMonth(sum);
					if (moneyIn.size() < 1) {
						System.out.println("데이터가 존재 하지 않습니다.");
						break a;
					}
					System.out.println("----------------------------------------------");
					for (Map<String, Object> map : moneyIn) {
						System.out.print  (map.get("MONTH2")+"\t");
						System.out.print  (map.get("FACILITY_NAME")+"\t");
						System.out.println(map.get("SALES"));	
					}
					System.out.println("----------------------------------------------");
					System.out.println("0.뒤로가기");
					int input3 = ScanUtil.nextInt();
						break a;					
				}
			case 0:
				break Q;
			}
			
		}
		
	}


	// 시설주 예약 정보 확인하는곳
	private void changeReervation() {
		a : 
		while (true) {
			System.out.println("예약상태를 선택해주세요");
			System.out.println("1.승인대기/승인 2.예약취소 3.대관완료 0.뒤로가기");
			int input1 = ScanUtil.nextInt();
			switch (input1) {
			case 1:
				System.out.println("-------------------------승인 정보 확인-------------------------");
				System.out.println("NO\t시설명\t예약일자\t시간\t결제수단\t결제일\t예약상태");
				List<Map<String, Object>> q = userDao.selectReservationList1();
				for (Map<String, Object> qq : q) {
					String time1 = format.format(qq.get("W"));
					String time2 = format.format(qq.get("T"));
					System.out.print  (qq.get("RN")+"\t");
					System.out.print  (qq.get("Q") +"\t");
					System.out.print  (time1       +"\t");
					System.out.print  (qq.get("E") +"\t");
					System.out.print  (qq.get("R") +"\t");
					System.out.print  (time2       +"\t");
					System.out.println(qq.get("Y"));
				}
				System.out.println("------------------------------------------------------------");
				System.out.println("1.승인하기 2.예약취소 0.뒤로가기");
				int input2 = ScanUtil.nextInt();
				switch (input2) {
				case 1:// 승인대기 -> 승인으로
					System.out.println("승인할 번호를 입력해주세요");
					int input3 = ScanUtil.nextInt();
					userDao.changeReservationState1(input3);
					System.out.println(input3+ "번이 승인으로 변경되었습니다.");
					break;
				case 2:// 승인대기,승인 -> 예약취소
					System.out.println("예약취소할 번호를 입력해주세요");
					int input4 = ScanUtil.nextInt();
					System.out.println("취소한 예약은 되돌리수 없습니다.");
					System.out.println("선택한 번호가 "+input4+"번 맞습니까?");
					System.out.println("1.예약취소 0. 뒤로가기");
					int input5 = ScanUtil.nextInt();
					if (input5 == 1) {
						userDao.changeReservationState2(input4);
						System.out.println("예약이 취소되었습니다.");
						break;
					}else if(input5 == 0){
						break;
					}
					break;
				case 0:
					break;

				}
				break;
			case 2: // 예약취소만 나온다 확인용
				System.out.println("-------------------------예약 취소-------------------------");
				System.out.println("NO\t시설명\t예약일자\t시간\t결제수단\t결제일\t예약상태");
				List<Map<String, Object>> w = userDao.selectReservationList2();
				for (Map<String, Object> ww : w) {
					String time1 = format.format(ww.get("W"));
					String time2 = format.format(ww.get("T"));
					System.out.print  (ww.get("RN")+"\t");
					System.out.print  (ww.get("Q")+"\t");
					System.out.print  (time1      +"\t");
					System.out.print  (ww.get("E")+"\t");
					System.out.print  (ww.get("R")+"\t");
					System.out.print  (time2      +"\t");
					System.out.println(ww.get("Y"));
				}
				System.out.println("--------------------------------------------------------");
				System.out.println("0.뒤로가기");
				int input6 = ScanUtil.nextInt();
				break;
			case 3:
				System.out.println("-------------------------대관 완료-------------------------");
				System.out.println("NO\t시설명\t\t예약일자\t시간\t결제수단\t결제일\t예약상태");
				List<Map<String, Object>> r = userDao.selectReservationList3();
				for (Map<String, Object> rr : r) {
					String time1 = format.format(rr.get("W"));
					String time2 = format.format(rr.get("T"));
					System.out.print  (rr.get("RN")+"\t");
					System.out.print  (rr.get("Q")+"\t\t");
					System.out.print  (time1      +"\t");
					System.out.print  (rr.get("E")+"\t");
					System.out.print  (rr.get("R")+"\t");
					System.out.print  (time2      +"\t");
					System.out.println(rr.get("Y"));
				}
				System.out.println("--------------------------------------------------------");
				System.out.println("0.뒤로가기");
				int input7 = ScanUtil.nextInt();
				break;
			case 0:
				break;
			}
			break a; //예약정보
			
		}
		
	}



	private void reservationList() {
		a : while (true) {
			System.out.println("[예약 목록]");
			System.out.println("===================================");
			
			
			System.out.println("===================================");
			System.out.println("0.뒤로가기");
			int input = ScanUtil.nextInt();
			switch (input) {
				case 0:	break a;
			}
		}
		
		
		
		
	}


	// 회원 예약 페이지
	private void myReservation() {
		String resNo = null;
		a : while (true) {
			System.out.println("--------------------------------------------------------");
			System.out.println("[예약 현황]");
			List<Map<String, Object>> reList = userDao.selectReservation1();
			if (reList.size() < 1) {
				System.out.println("예약한 시설이 없습니다.");
				System.out.println("--------------------------------------------------------");
				System.out.println("0.뒤로가기");
				int input = ScanUtil.nextInt();
				switch (input) {
					case 0:	break a;
				}
			}else{
				for (Map<String, Object> re : reList) {
					System.out.println("시       설 : "+ re.get("Q")+"\t");
					String time1 = format.format(re.get("RESERVATION_DATE"));
					System.out.println("예  약  일 : "+ time1+"\t");
					System.out.println("타       임 : "+ re.get("RESERVATION_AMPM") + "\t");
					System.out.println("결제 금액 : "+ re.get("RESERVATION_PAY_PEE")+"\t");
					System.out.println("예약 상태 : "+ re.get("RESERVATION_STATE")+"\t");
					System.out.println("환불 계좌 : "+ re.get("RESERVATION_REFUND_ACCOUNT"));
					resNo = (String)re.get("RESERVATION_NO");
				}	
			}
			System.out.println("--------------------------------------------------------");
			System.out.println("1.예약변경 2.예약취소 0.뒤로가기");
			int input = ScanUtil.nextInt();
			switch (input) {
			case 1:
				String facilityID = userDao.selectFacID();
				facilityReservationService.facilityReservation1(facilityID, resNo);
				System.out.println("예약정보가 수정되었습니다.");
				break;
			case 2: // 예약취소 상태변경
				System.out.println("정말 예약을 취소하시겠습니까? 1.취소 0.뒤로가기");
				int a = ScanUtil.nextInt();
				if (a == 1) {
					int aaa = userDao.changeReservation();
					System.out.println(aaa);
					System.out.println("예약이 취소되었습니다.");
					System.out.println("환불 계좌로 이용료가 환불됩니다. (1 ~ 2일 소용)");
					break a;
				}else if (a == 0) {
					break ;
				}
			case 0:
				break a;

			}
			
			
			
		}
		
		
	}


	// 가입정보 
	private int showInfo(Map<String, Object> user) {
		outer : while (true) {
			System.out.println("===================내정보===================");
			System.out.println("회원 분류 :" + user.get("USER_TYPE"));
			System.out.println("아  이  디 :" + user.get("USER_ID"));
			System.out.println("비밀 번호 :" + user.get("USER_PASSWORD"));
			System.out.println("이       름 :" + user.get("USER_NAME"));
			System.out.println("휴대 전화 :" + user.get("USER_PHONENUMBER"));
			System.out.println("우편 번호 :" + user.get("USER_POSTCODE"));
			System.out.println("기본 주소 :" + user.get("USER_POSTCODE"));
			System.out.println("상세 주소 :" + user.get("USER_ADDRESS_DETAIL"));
//			System.out.println("회원 분류 :" + user.get("USER_DELETE"));
			System.out.println("==========================================");
			System.out.println("1.수정 5.탈퇴 0.뒤로가기");
			int input = ScanUtil.nextInt();
			switch (input) {
				case 1:	
					updateInfo(user);
					break;// 시간날때 추가하자
				case 5: // 탈퇴
						System.out.println("비밀번호를 한번 더 입력해주세요");
						String pass = ScanUtil.nextLine();
						if (Controller.loginUser.get("USER_PASSWORD").equals(pass)) {
							int result = userDao.deleteUser();
							if (result == 1) {
								System.out.println("탈퇴되었습니다.");
								Controller.loginUser.clear(); 
								System.out.println("로그아웃 되었습니다.");
								return View.HOME;
							}else{
								System.out.println("오류있음 수정하셈");
							}
							
						}else{
							System.out.println("비밀번호를 잘못 입력하셨습니다.");
						}
					break;
				case 0: break outer;
			}
		}
		return View.HOME;
	}



	private void updateInfo(Map<String, Object> user) {
		u : while (true) {
			System.out.println("내정보를 수정합니다.");
			System.out.println("==========================================");
			System.out.println("1.회원 분류 :"  + Controller.loginUser.get("USER_TYPE"));
			System.out.println("    아이디 :" + Controller.loginUser.get("USER_ID"));
			System.out.println("2.비밀 번호 :"  + Controller.loginUser.get("USER_PASSWORD"));
			System.out.println("3.이       름 :"  + Controller.loginUser.get("USER_NAME"));
			System.out.println("4.휴대 전화 :"  + Controller.loginUser.get("USER_PHONENUMBER"));
			System.out.println("5.우편 번호 :"  + Controller.loginUser.get("USER_POSTCODE"));
			System.out.println("  기본 주소 :" + Controller.loginUser.get("USER_ADDRESS"));
			System.out.println("  상세 주소 :" + Controller.loginUser.get("USER_ADDRESS_DETAIL"));
//			System.out.println("회원 분류 :" + user.get("USER_DELETE"));
			System.out.println("==========================================");
			System.out.println("[1 2 3 4 5].선택\t0.뒤로가기");
			int input = ScanUtil.nextInt();
			switch (input) {
			case 1:	changetype(); break;
			case 2:	changepassword(); break;
			case 3:	changename(); break;
			case 4:	changephone(); break;
			case 5:	changeaddress(); break;
			case 0: break u;
			}
		}
	}



	
	
	
	
	private void changeaddress() {
		a : while (true) {
			System.out.println("현재 주소 : ["+Controller.loginUser.get("USER_POSTCODE")+" | "+
		                        Controller.loginUser.get("USER_ADDRESS")+" | "+
		                  Controller.loginUser.get("USER_ADDRESS_DETAIL") +"] \n 1.수정\t0.뒤로가기");
			System.out.println(" 입력 > ");
			int a = ScanUtil.nextInt();
			if (a == 1) {
				System.out.print("변경할 우편주소 입력>");
				String newpass1 = ScanUtil.nextLine();
				System.out.print("변경할 기본주소 입력>");
				String newpass2 = ScanUtil.nextLine();
				System.out.print("변경할 상세주소 입력>");
				String newpass3 = ScanUtil.nextLine();
				System.out.println("입력한 주소 : "+newpass1+"|"+newpass2+"|"+newpass3);
				System.out.println("1. 입력  0.다시입력");
				int b = ScanUtil.nextInt();
				if (b== 1) {
					userDao.updateuserInfo5(newpass1,newpass2,newpass3);
					System.out.println("주소가 수정되었습니다.");
					Controller.loginUser.put("USER_POSTCODE", newpass1);
					Controller.loginUser.put("USER_ADDRESS", newpass2);
					Controller.loginUser.put("USER_ADDRESS_DETAIL", newpass3);
					break;
				}else{
					break;
				}
			}else if (a == 0) {
				break a;
			}		
		}
		
	}



	private void changephone() {
		a : while (true) {
			System.out.println("현재 휴대번호 : ["+Controller.loginUser.get("USER_PHONENUMBER")+"] \n 1.수정\t0.뒤로가기");
			System.out.println(" 입력 > ");
			int a = ScanUtil.nextInt();
			if (a == 1) {
				System.out.print("변경할 번호 입력>");
				String newpass1 = ScanUtil.nextLine();
				System.out.print("변경할 번호 확인 입력>");
				String newpass2 = ScanUtil.nextLine();
				if (newpass1.equals(newpass2)) {
					userDao.updateuserInfo4(newpass1);
					System.out.println("휴대번호가 수정되었습니다.");
					Controller.loginUser.put("USER_PHONENUMBER", newpass1);
					break;
				}else{
					System.out.println("입력값이 동일하지 않습니다.");
				}
			}else if (a == 0) {
				break a;
			}		
		}
		
	}



	private void changename() {
		a : while (true) {
			System.out.println("현재 이름 : ["+Controller.loginUser.get("USER_NAME")+"] \n 1.수정\t0.뒤로가기");
			System.out.println(" 입력 > ");
			int a = ScanUtil.nextInt();
			if (a == 1) {
				System.out.print("변경할 이름 입력>");
				String newpass1 = ScanUtil.nextLine();
				System.out.print("변경할 이름 확인 입력>");
				String newpass2 = ScanUtil.nextLine();
				if (newpass1.equals(newpass2)) {
					userDao.updateuserInfo3(newpass1);
					System.out.println("이름이 수정되었습니다.");
					Controller.loginUser.put("USER_NAME", newpass1);
					break;
				}else{
					System.out.println("입력값이 동일하지 않습니다.");
				}
			}else if (a == 0) {
				break a;
			}		
		}
		
	}

	private void changepassword() {
		a : while (true) {
			System.out.println("현재비밀번호 : [" + Controller.loginUser.get("USER_PASSWORD") + "] \n 1.수정\t0.뒤로가기");
			System.out.println(" 입력 > ");
			int a = ScanUtil.nextInt();
			if (a == 1) {
				System.out.print("새 비밀번호 입력>");
				String newpass1 = ScanUtil.nextLine();
				System.out.print("새 비밀번호 확인 입력>");
				String newpass2 = ScanUtil.nextLine();
				if (newpass1.equals(newpass2)) {
					userDao.updateuserInfo2(newpass1);
					System.out.println("비밀번호가 수정되었습니다.");
					Controller.loginUser.put("USER_PASSWORD", newpass1);
					break;
				}else{
					System.out.println("입력값이 동일하지 않습니다.");
				}
			}else if (a == 0) {
				break a;
			}		
		}
		
	}



	private void changetype() {
		while (true) {
			System.out.println("회원 분류  [1.회원  2.시설주]\t0.뒤로가기");
			System.out.print("입력>");
			String typeC = null;
			int code = ScanUtil.nextInt();
			if (code == 1) {
				typeC = "USER";
				userDao.updateuserInfo1(typeC);
				System.out.println("회원으로 수정되었습니다.");
				Controller.loginUser.put("USER_TYPE", "USER");
				break;
			}else if(code == 2){
				typeC = "OWNER";
				userDao.updateuserInfo1(typeC);
				System.out.println("시설주로 수정되었습니다.");
				Controller.loginUser.put("USER_TYPE", "OWNER");
				break;
			}else if (code == 0) {
				break;
			}
			else{
				System.out.println("1,2번을 골라주세요");
			}
		}	
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
