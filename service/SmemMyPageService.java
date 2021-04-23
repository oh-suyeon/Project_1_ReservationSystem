package service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import util.ScanUtil;
import util.View;
import dao.SmemMyPageDao;

public class SmemMyPageService {
	
	private SmemMyPageService(){
		
	}
	
	private static SmemMyPageService instance;
	
	public static SmemMyPageService getInstance() {
		if(instance == null){
			instance = new SmemMyPageService();
		}
		return instance;
	}
	
	private static SmemMyPageDao smemMyPageDao = SmemMyPageDao.getInstance();



//	public static int myPagelist() {
//		System.out.println("1.내정보조회/수정 \t 2.나의 시설 정보 조회 \t 3.예약정보 \t 4.월간 매출 관리");
//		System.out.println("입력>");
//		int input = 0;
//		input = ScanUtil.nextInt();
//		switch (input) {
//		case 1:
//			System.out.println("1번 ");
//			break;
//		case 2:
//			MyFacInfo();
//			break;
//
//		default:
//			break;
//		}
//		
//		return View.SMEM_MYPAGE_;
//	}


   
	
	//나의 시설정보 - 시설목록 리스트
	public static void MyFacInfo() {
		
		a:while (true) {
			List<Map<String, Object>> myFacList = smemMyPageDao.selectMyFacList();
			if(myFacList.size() < 1){
				System.out.println("보유 시설이 존재하지 않습니다");
				break a;
			} else {
				System.out.println("-------------------------------------");
				System.out.println("번호\t시설명");
				System.out.println("-------------------------------------");
				for(Map<String, Object> myFac : myFacList ){
					System.out.print(myFac.get("ROWNUM") + "\t"); 
					System.out.print(myFac.get("FACILITY_NAME") + "\t"); 
					System.out.println();
				}
				System.out.println("=====================================");
				System.out.println("1.조회\t2.등록\t0.뒤로가기");
				int input = ScanUtil.nextInt();
				switch (input) {
				case 1:
					view(myFacList);
					break;
				case 2:
					insert();
					break;
				case 0:
					break a;
				}
			}
		}


//		default:
//			System.out.println("번호를 다시 입력해주세요");
//			return MyFacInfo();
//			
//		}
//		return View.SMEM_MYPAGE_;

	}



	
	
	//나의 시설 정보 - 상세정보 조회 
	private static void view(List<Map<String, Object>> myFacList) {
		
		System.out.println("조회할 행을 입력해주세요");
		int facNo = ScanUtil.nextInt();
		Map<String, Object> row = myFacList.get(facNo-1);
		String facId = (String)row.get("FACILITY_ID");
		A:while(true){
			Map<String, Object> MyFacList2 = smemMyPageDao.selectMyFacViewList(facId);
			System.out.println("----------------------------------------------------------------");
				System.out.println("1.시설명          :" + MyFacList2.get("FACILITY_NAME") );
				System.out.println("2.시설 내용      :" + MyFacList2.get("FACILITY_CONTENT"));
				System.out.println("3.오픈 시간      :" + MyFacList2.get("FACILITY_START_TIME"));
				System.out.println("4.마감 시간      :" + MyFacList2.get("FACILITY_END_TIME"));
				System.out.println("5.사업자등록증 :" + MyFacList2.get("FACILITY_COMPANY_NUMBER"));
				System.out.println("6.오전 요금      :" + MyFacList2.get("FACILITY_AM_PAY"));
				System.out.println("7.오후 요금      :" + MyFacList2.get("FACILITY_PM_PAY"));
				System.out.println("8.우편번호       :" + MyFacList2.get("FACILITY_POSTCODE"));
				System.out.println("9.시설 주소      :" + MyFacList2.get("FACILITY_ADDRESS"));
				System.out.println("10.상세 주소    :" + MyFacList2.get("FACILITY_ADDRESS_DETAIL"));
				System.out.println("11.휴대폰 번호 :" + MyFacList2.get("FACILITY_PHONE_NUMBER"));
				System.out.println("12.승인 여부    :" + MyFacList2.get("FACILITY_ALLOW") );
			System.out.println("----------------------------------------------------------------");
			System.out.println("1.수정\t2.삭제\t0.뒤로가기");
			int input = ScanUtil.nextInt();
			switch (input) {
			case 1:
				update(facId);
				break ;
				
			case 2:
				delete(facId);
				break A;
				
			case 0:
				break A;
			}//switch
	  }//while
	}//viewList 
		

	
	
    //나의 시설 정보 -수정 
	private static void update(String facId) {
		System.out.println("변경할 항목 번호를 입력해주세요>");
		int updateNo = ScanUtil.nextInt();
			
		switch (updateNo) {
		case 1:
			System.out.println("시설명>");
			String name = ScanUtil.nextLine();
			Map<String,Object> n_param = new HashMap<>();
			n_param.put("FACILITY_NAME", name);
			int n_result = smemMyPageDao.viewNameupdate(facId, name);
			break;
		case 2:
			System.out.println("시설내용>");
			String content= ScanUtil.nextLine();
			Map<String,Object> c_param = new HashMap<>();
			c_param.put("FACILITY_CONTENT", content);
			int c_result = smemMyPageDao.viewContentupdate(facId, content);
			break;
		case 3:
			System.out.println("시설오픈시간");
			String openTime = ScanUtil.nextLine();
			Map<String,Object> ot_param = new HashMap<>();
			ot_param.put("FACILITY_START_TIME", openTime);
			int ot_result = smemMyPageDao.viewOpenTimeupdate(facId, openTime);
			break;
		case 4:
			System.out.println("시설마감시간");
			String closeTime = ScanUtil.nextLine();
			Map<String,Object> ct_param = new HashMap<>();
			ct_param.put("FACILITY_END_TIME", closeTime);
			int ct_result = smemMyPageDao.viewCloseTimeupdate(facId, closeTime);
			break;
		case 5:
            System.out.println("사업자등록증은 수정할 수 없습니다.");
            break;
		case 6:
			System.out.println("오전요금");
			int AmPay = ScanUtil.nextInt();
			Map<String,Object> ap_param = new HashMap<>();
			ap_param.put("FACILITY_AM_PAY", AmPay);
			int ap_result = smemMyPageDao.viewAMPayupdate(facId, AmPay);
			break;
		case 7:
			System.out.println("오후요금");
			int PmPay = ScanUtil.nextInt();
			Map<String,Object> pp_param = new HashMap<>();
			pp_param.put("FACILITY_PM_PAY", PmPay);
			int pp_result = smemMyPageDao.viewPMPayupdate(facId, PmPay);
			break;
		case 8:
			System.out.println("우편번호");
			int postNumber = ScanUtil.nextInt();
			Map<String,Object> pn_param = new HashMap<>();
			pn_param.put("FACILITY_POSTCODE", postNumber);
			int pn_result = smemMyPageDao.viewPostCodeupdate(facId, postNumber);
			break;
		case 9:
			System.out.println("주소");
			String address = ScanUtil.nextLine();
			Map<String,Object> add_param = new HashMap<>();
			add_param.put("FACILITY_ADDRESS", address);
			int add_result = smemMyPageDao.viewAddressupdate(facId, address);
			break;
		case 10:
			System.out.println("상세주소");
			String address_detail = ScanUtil.nextLine();
			Map<String,Object> add2_param = new HashMap<>();
			add2_param.put("FACILITY_ADDRESS_Detail", address_detail);
			int add2_result = smemMyPageDao.viewAddressDetailupdate(facId, address_detail);
			break;
		case 11:
			System.out.println("휴대폰번호");
			String phoneNumber = ScanUtil.nextLine();
			Map<String,Object> phoneNum_param = new HashMap<>();
			phoneNum_param.put("FACILITY_PHONE_NUMBER", phoneNumber);
			int phoneNum_result = smemMyPageDao.viewPhoneNumberupdate(facId, phoneNumber);
			break;
		case 12:
			System.out.println("승인여부는 수정할수 없는 항목입니다.");
			break;

		default:
			System.out.println("번호를 다시 입력해주세요");
			break;
		}
	}

	
	
	
	

	//나의 시설 정보 - 시설삭제
	private static void delete(String facId) {
		
		System.out.println("정말 삭제하시겠습니까?");
		System.out.println("1.확인 \t 2.뒤로가기");
	    int input = 0;
	    input = ScanUtil.nextInt();
	    switch (input) {
		case 1:
			int result = smemMyPageDao.viewMyFacDelete(facId);
			System.out.println("삭제가 완료되었습니다.");
			break;
		case 2:
            break;
		default:
			break;
		}
		
			
	}
	
	
	
	
	
	
		
	//나의시설정보- 등록
	 private static void insert(){
			

		   
			String title;
			String content;
			String companyNumber;
			String openTime;
			String closeTime;
			String AmPay;
			String PmPay;
			String postNumber;
			String phoneNumber;
			String address;
			String addressDetail;
			
			
			//시설 이름
			while(true){
			   System.out.println("시설이름: ");
			   title = ScanUtil.nextLine();
			   if (title.equals("")) {
				    System.out.println("필수항목입니다. 이름을 입력해주세요");
			   } else {
				break;
			   }
			}
			
			
			
			//시설 내용
			while(true){
				System.out.println("시설내용: ");
				content = ScanUtil.nextLine();
			    if (content.equals("")) {
				    System.out.println("필수항목입니다. 이름을 입력해주세요");
			    } else {
				break;
			   }
			}
			
			
			
			//사업자등록증
			while(true){  
		      while(true){
			      System.out.println("사업자등록번호: ");
			      companyNumber = ScanUtil.nextLine();
			      String companyNumberRegrex = "[0-9]{10}";
			      Matcher matcher = Pattern.compile(companyNumberRegrex).matcher(companyNumber);
			
				  if (matcher.matches() == false) {
					  System.out.println("사업자등록번호 10글자를 정확히 입력해주세요");
				  }else{
					break;
				  } 
		      }
		    
		     if (companyNumber.equals("")) {
				 System.out.println("필수항목입니다. 사업자등록번호을 입력해주세요");
			 }else{
			   break;
		     }
		  }//while		
			
			
			//여는시간
			while(true){
				while(true){
				    System.out.println("여는시간: ");
				    openTime = ScanUtil.nextLine();
				    String openTimeRegrex = "^([01][0-9]|2[0-3]):([0-5][0-9])$";
				    Matcher matcher = Pattern.compile(openTimeRegrex).matcher(openTime);
					if (matcher.matches() == false) {
						System.out.println("시간 형식은 00:00 입니다.");
					} else {
						break;
					}
				}//while 
				
		        if (openTime.equals("")) {
					     System.out.println("필수항목입니다. 시간을 입력해주세요");
				}else{
			      break;
			    }
			}
			
			
			
			//닫는시간
			while(true){
				while(true){
				    System.out.println("닫는시간: ");
				    closeTime = ScanUtil.nextLine();
				    String closeTimeRegrex = "^([01][0-9]|2[0-3]):([0-5][0-9])$";
				    Matcher matcher = Pattern.compile(closeTimeRegrex).matcher(closeTime);
					if (matcher.matches() == false) {
						System.out.println("시간 형식은 00:00 입니다.");
					} else {
						break;
					}
				}//while 
				
		        if (closeTime.equals("")) {
					     System.out.println("필수항목입니다. 시간을 입력해주세요");
				}else{
			      break;
			    }
			}
			
			
			
			//오전이용료
			while(true){  
		      while(true){
			      System.out.println("오전이용료: ");
			      AmPay = ScanUtil.nextLine();
			      String AmPayRegrex = "^[0-9]*$";
			      Matcher matcher = Pattern.compile(AmPayRegrex).matcher(AmPay);
			
				  if (matcher.matches() == false) {
					  System.out.println("숫자 형식으로 입력해주세요");
				  }else{
					break;
				  } 
		      }//while
		    
		      if (companyNumber.equals("")) {
				  System.out.println("필수항목입니다. 이름을 입력해주세요");
			  }else{
			   break;
		      }
		    }//while
			
			
			//오후이용료
		    while(true){  
				while(true){
				    System.out.println("오후이용료: ");
				    PmPay = ScanUtil.nextLine();
				    String PmPayRegrex = "^[0-9]*$";
				    Matcher matcher = Pattern.compile(PmPayRegrex).matcher(PmPay);
						
					if (matcher.matches() == false) {
					    System.out.println("숫자 형식으로 입력해주세요");
					}else{
					  break;
					} 
				}//while
				    
				if (PmPay.equals("")) {
				    System.out.println("필수항목입니다. 이름을 입력해주세요");
			    }else{
				 break;
				}
			}//while		
			
			
		     //연락처
		     while(true){
		  	    while(true){
		  		   System.out.println("연락처: ");
		  		   phoneNumber = ScanUtil.nextLine();
		  		   String phoneNumberRegrex = "^01([0|1|6|7|8|9]?)-?([0-9]{3,4})-?([0-9]{4})$";
		  		   Matcher matcher = Pattern.compile(phoneNumberRegrex).matcher(phoneNumber);
		  				if (matcher.matches() == false) {
		  					System.out.println("전화번호 형식은 010-1234-5678 입니다.");
		  				} else {
		  					break;
		  				}
		  			}//while 
		  			
		  	        if (phoneNumber.equals("")) {
		  				System.out.println("필수항목입니다. 시간을 입력해주세요");
		  			}else{
		  		      break;
		  		    }
		  		}
			
		     
		    //우편번호
			while(true){  
			    while(true){
					System.out.println("우편주소: ");
					postNumber = ScanUtil.nextLine();
					String postNumberRegrex = "[0-9]{5}";
					Matcher matcher = Pattern.compile(postNumberRegrex).matcher(postNumber);
							
				    if (matcher.matches() == false) {
						System.out.println("우편번호 5자리를 정확히 입력해주세요");
				    }else{
					 break;
				    } 
				}//while
					    
		        if (postNumber.equals("")) {
			     System.out.println("필수항목입니다. 우편번호를 입력해주세요");
		        }else{
			      break;
			    }
			}//while
			
			
		
		     //주소
		     while(true){
		  		 System.out.println("주소: ");
		  		 address = ScanUtil.nextLine();
		  	       
		  		 if (address.equals("")) {
		  			 System.out.println("필수항목입니다. 주소를 입력해주세요");
		  	    }else{
		  		  break;
		  		}
		  	 }
			

			
			System.out.println("상세주소: ");
			addressDetail = ScanUtil.nextLine();
			
			
			String select_facilityCode = selectFacilityList();
			String select_RegionCode = selectRegionList();

			
			 Map<String, Object> param = new HashMap<>();
			 param.put("FACILITY_NAME", title);
			 param.put("FACILITY_CONTENT", content);
			 param.put("FACILITY_COMPANY_NUMBER", Integer.parseInt(companyNumber));
			 param.put("FACILITY START_TIME", openTime);
			 param.put("FACILITY END_TIME", closeTime);
			 param.put("FACILITY_AM_PAY", Integer.parseInt(AmPay));
			 param.put("FACILITY_PM_PAY", Integer.parseInt(PmPay));
			 param.put("FACILITY_POSTCODE", Integer.parseInt(postNumber));
			 param.put("FACILITY_ADDRESS", address);
			 param.put("FACILITY_ADDRESS_Detail", addressDetail);
			 param.put("FACILITY_PHONE_NUMBER", phoneNumber);
			 param.put("FACILITY_CODE", select_facilityCode);
			 param.put("REGION_CODE", select_RegionCode);
			 
			int result = smemMyPageDao.insertMyFac(title, content, companyNumber, openTime, closeTime, AmPay, PmPay, phoneNumber, postNumber, address, addressDetail, select_facilityCode, select_RegionCode);
			System.out.println("등록이 성공되었습니다.");
			
		}
	    

	    
	    
	    
	    
	   //시설 유형 선택
		static String selectFacilityList(){	
	    	String facilityLargeName = null ;
			String facilityCode = null;
			String facilityCodeName = null ;
	    	//시설유형선택
			System.out.println("시설분류");
			System.out.println("[1.체육시설 2.공간시설]");
			int input = 0;
			input = ScanUtil.nextInt();
	        switch (input) {
			case 1:
				facilityLargeName = "체육시설"; 
				break;
			case 2:
				facilityLargeName = "공간시설"; 
				break;

			default:
				break;
			}
	        
	        //시설 소분류 선택
	        List<Map<String,Object>> fac_small_list = smemMyPageDao.facilityCodeList(facilityLargeName);
			
			System.out.println("----------------------------------------");
			
			for(int i=0; i< fac_small_list.size(); i++){
				Map<String, Object> row = fac_small_list.get(i);
				System.out.println(i + 1 + ". " + row.get("FACILITY_SMALL_NAME"));
			}
			System.out.println("----------------------------------------");
			System.out.println("시설유형 선택");
			
			int no = ScanUtil.nextInt();
			
			Map<String, Object> fac_small_code = fac_small_list.get(no-1);
	        facilityCode = (String)fac_small_code.get("FACILITY_CODE");//시설 코드 저장
	        facilityCodeName = (String)fac_small_code.get("FACILITY_SMALL_NAME");
			
			System.out.println("▶" +facilityLargeName+ "," + facilityCodeName + " 을 선택하셨습니다.");
			
			
			return facilityCode;
	    }
		
		
	 
		
		//지역 유형 선택
	    static String selectRegionList(){
	    	int regionLargeCode = 0 ;
	 		String regionLargeCodeName = null;
	 		String regionSmallCode = null;
	 		String regionSmallCodeName = null;
	    	//지역 대분류 리스트 출력 
	    			System.out.println();
	    			System.out.println("지역선택");
	    			List<Map<String, Object>> RegionLarge_list = smemMyPageDao.regionLargeCodeList();
	    			System.out.println("----------------------------------------");
	    			for(int i = 0; i < RegionLarge_list.size(); i++){
	    				Map<String, Object> row = RegionLarge_list.get(i);
	    				System.out.println(i + 1 + "." + row.get("REGION_LARGE_NAME"));
	    			}

	    			System.out.println("----------------------------------------");

	    			//지역 대분류 리스트 선택 
	    			System.out.println("광역시 선택 >");
	    			int regionno = ScanUtil.nextInt();
	    			if(regionno > 0){		
	    				regionLargeCode = regionno; //지역 대분류 코드 저장 
	    				Map<String, Object> region_large_row = RegionLarge_list.get(regionno - 1);
	    				regionLargeCodeName = (String)region_large_row.get("REGION_LARGE_NAME");
	    				System.out.println(regionLargeCodeName);
	    			}	
	    			
				
	    				
	    			  
	    			
	    			//구 리스트 출력 
	    			if(regionno != 4){	// 구가 없는 세종시가 아닌 경우 구 선택으로 넘어감 
	    			   System.out.println("구 선택");
	    			   List<Map<String, Object>> Region_small_list = smemMyPageDao.regionSmallCodeList(regionLargeCode);
	    			   System.out.println("----------------------------------------");
	    			   for(int i = 0; i < Region_small_list.size(); i++){
	    					Map<String, Object> region_small_row = Region_small_list.get(i);
	    					System.out.println(i + 1 + "." + region_small_row.get("REGION_SMALL_NAME"));
	    				}
	    							
	    			    System.out.println("=================================================================");
	    				System.out.println("구 선택 >");
	    			    int regionsmallno = ScanUtil.nextInt();
	    							
	    			     if(regionsmallno > 0){
	    				 Map<String, Object> region_small_code_map = Region_small_list.get(regionsmallno - 1);
	    				 regionSmallCode = (String)region_small_code_map.get("REGION_CODE");
	    				 regionSmallCodeName = (String)region_small_code_map.get("REGION_SMALL_NAME");
	    				  System.out.println("▶"+regionLargeCodeName+","+regionSmallCodeName + " 을 선택하셨습니다.");
	    				}	
	    			}
			
	    	return regionSmallCode;
	    
	    }
	    
	    
	    // 관리자 등록 실행 부분
	    public static void insertManager(){
			String facilityLargeName = null ;
			String facilityCode = null;
			String facilityCodeName = null ;
		    int regionLargeCode = 0 ;
			String regionLargeCodeName = null;
			String regionSmallCode = null;
			String regionSmallCodeName = null;
			String title;
			String content;
			String companyNumber;
			String openTime;
			String closeTime;
			String AmPay;
			String PmPay;
			String postNumber;
			String phoneNumber;
			String address;
			String addressDetail;
			
			
			//시설 이름
			while(true){
			   System.out.println("시설이름: ");
			   title = ScanUtil.nextLine();
			   if (title.equals("")) {
				    System.out.println("필수항목입니다. 이름을 입력해주세요");
			   } else {
				break;
			   }
			}
			
			
			
			//시설 내용
			while(true){
				System.out.println("시설내용: ");
				content = ScanUtil.nextLine();
			    if (content.equals("")) {
				    System.out.println("필수항목입니다. 이름을 입력해주세요");
			    } else {
				break;
			   }
			}
			
			
			
			//사업자등록증
			while(true){  
		      while(true){
			      System.out.println("사업자등록번호: ");
			      companyNumber = ScanUtil.nextLine();
			      String companyNumberRegrex = "[0-9]{10}";
			      Matcher matcher = Pattern.compile(companyNumberRegrex).matcher(companyNumber);
			
				  if (matcher.matches() == false) {
					  System.out.println("사업자등록번호 10글자를 정확히 입력해주세요");
				  }else{
					break;
				  } 
		      }
		    
		     if (companyNumber.equals("")) {
				 System.out.println("필수항목입니다. 사업자등록번호을 입력해주세요");
			 }else{
			   break;
		     }
		  }//while		
			
			
			//여는시간
			while(true){
				while(true){
				    System.out.println("여는시간: ");
				    openTime = ScanUtil.nextLine();
				    String openTimeRegrex = "^([01][0-9]|2[0-3]):([0-5][0-9])$";
				    Matcher matcher = Pattern.compile(openTimeRegrex).matcher(openTime);
					if (matcher.matches() == false) {
						System.out.println("시간 형식은 00:00 입니다.");
					} else {
						break;
					}
				}//while 
				
		        if (openTime.equals("")) {
					     System.out.println("필수항목입니다. 시간을 입력해주세요");
				}else{
			      break;
			    }
			}
			
			
			
			//닫는시간
			while(true){
				while(true){
				    System.out.println("닫는시간: ");
				    closeTime = ScanUtil.nextLine();
				    String closeTimeRegrex = "^([01][0-9]|2[0-3]):([0-5][0-9])$";
				    Matcher matcher = Pattern.compile(closeTimeRegrex).matcher(closeTime);
					if (matcher.matches() == false) {
						System.out.println("시간 형식은 00:00 입니다.");
					} else {
						break;
					}
				}//while 
				
		        if (closeTime.equals("")) {
					     System.out.println("필수항목입니다. 시간을 입력해주세요");
				}else{
			      break;
			    }
			}
			
			
			
			//오전이용료
			while(true){  
		      while(true){
			      System.out.println("오전이용료: ");
			      AmPay = ScanUtil.nextLine();
			      String AmPayRegrex = "^[0-9]*$";
			      Matcher matcher = Pattern.compile(AmPayRegrex).matcher(AmPay);
			
				  if (matcher.matches() == false) {
					  System.out.println("숫자 형식으로 입력해주세요");
				  }else{
					break;
				  } 
		      }//while
		    
		      if (companyNumber.equals("")) {
				  System.out.println("필수항목입니다. 이름을 입력해주세요");
			  }else{
			   break;
		      }
		    }//while
			
			
			//오후이용료
		    while(true){  
				while(true){
				    System.out.println("오후이용료: ");
				    PmPay = ScanUtil.nextLine();
				    String PmPayRegrex = "^[0-9]*$";
				    Matcher matcher = Pattern.compile(PmPayRegrex).matcher(PmPay);
						
					if (matcher.matches() == false) {
					    System.out.println("숫자 형식으로 입력해주세요");
					}else{
					  break;
					} 
				}//while
				    
				if (PmPay.equals("")) {
				    System.out.println("필수항목입니다. 이름을 입력해주세요");
			    }else{
				 break;
				}
			}//while		
			
			
		     //연락처
		     while(true){
		  	    while(true){
		  		   System.out.println("연락처: ");
		  		   phoneNumber = ScanUtil.nextLine();
		  		   String phoneNumberRegrex = "^01([0|1|6|7|8|9]?)-?([0-9]{3,4})-?([0-9]{4})$";
		  		   Matcher matcher = Pattern.compile(phoneNumberRegrex).matcher(phoneNumber);
		  				if (matcher.matches() == false) {
		  					System.out.println("전화번호 형식은 010-1234-5678 입니다.");
		  				} else {
		  					break;
		  				}
		  			}//while 
		  			
		  	        if (phoneNumber.equals("")) {
		  				System.out.println("필수항목입니다. 시간을 입력해주세요");
		  			}else{
		  		      break;
		  		    }
		  		}
			
		     
		    //우편번호
			while(true){  
			    while(true){
					System.out.println("우편주소: ");
					postNumber = ScanUtil.nextLine();
					String postNumberRegrex = "[0-9]{5}";
					Matcher matcher = Pattern.compile(postNumberRegrex).matcher(postNumber);
							
				    if (matcher.matches() == false) {
						System.out.println("우편번호 5자리를 정확히 입력해주세요");
				    }else{
					 break;
				    } 
				}//while
					    
		        if (postNumber.equals("")) {
			     System.out.println("필수항목입니다. 우편번호를 입력해주세요");
		        }else{
			      break;
			    }
			}//while
			
			
		
		     //주소
		     while(true){
		  		 System.out.println("주소: ");
		  		 address = ScanUtil.nextLine();
		  	       
		  		 if (address.equals("")) {
		  			 System.out.println("필수항목입니다. 주소를 입력해주세요");
		  	    }else{
		  		  break;
		  		}
		  	 }
			

			
			System.out.println("상세주소: ");
			addressDetail = ScanUtil.nextLine();
			
			
			
			
			//시설유형선택
			System.out.println("시설분류");
			System.out.println("[1.체육시설 2.공간시설]");
			int input = 0;
			input = ScanUtil.nextInt();
	        switch (input) {
			case 1:
				facilityLargeName = "체육시설"; 
				break;
			case 2:
				facilityLargeName = "공간시설"; 
				break;

			default:
				break;
			}
	        
	        
	        
	        
	        //시설 소분류 선택
	        List<Map<String,Object>> fac_small_list = smemMyPageDao.facilityCodeList(facilityLargeName);
			
			System.out.println("----------------------------------------");
			
			for(int i=0; i< fac_small_list.size(); i++){
				Map<String, Object> row = fac_small_list.get(i);
				System.out.println(i + 1 + ". " + row.get("FACILITY_SMALL_NAME"));
			}
			System.out.println("----------------------------------------");
			System.out.println("시설유형 선택");
			
			int no = ScanUtil.nextInt();
			
			Map<String, Object> fac_small_code = fac_small_list.get(no-1);
	        facilityCode = (String)fac_small_code.get("FACILITY_CODE");//시설 코드 저장
	        facilityCodeName = (String)fac_small_code.get("FACILITY_SMALL_NAME");
			
			System.out.println("▶" +facilityLargeName+ "," + facilityCodeName + " 을 선택하셨습니다.");
			
			
			

			
			
			//지역 대분류 리스트 출력 
			System.out.println();
			System.out.println("지역선택");
			List<Map<String, Object>> RegionLarge_list = smemMyPageDao.regionLargeCodeList();
			System.out.println("----------------------------------------");
			for(int i = 0; i < RegionLarge_list.size(); i++){
				Map<String, Object> row = RegionLarge_list.get(i);
				System.out.println(i + 1 + "." + row.get("REGION_LARGE_NAME"));
			}

			System.out.println("----------------------------------------");

			//지역 대분류 리스트 선택 
			System.out.println("광역시 선택 >");
			int regionno = ScanUtil.nextInt();
			if(regionno > 0){		
				regionLargeCode = regionno; //지역 대분류 코드 저장 
				Map<String, Object> region_large_row = RegionLarge_list.get(regionno - 1);
				regionLargeCodeName = (String)region_large_row.get("REGION_LARGE_NAME");
				System.out.println(regionLargeCodeName);
			}	
			
				
				
			  
			
			//구 리스트 출력 
			if(regionno != 4){	// 구가 없는 세종시가 아닌 경우 구 선택으로 넘어감 
			   System.out.println("구 선택");
			   List<Map<String, Object>> Region_small_list = smemMyPageDao.regionSmallCodeList(regionLargeCode);
			   System.out.println("----------------------------------------");
			   for(int i = 0; i < Region_small_list.size(); i++){
					Map<String, Object> region_small_row = Region_small_list.get(i);
					System.out.println(i + 1 + "." + region_small_row.get("REGION_SMALL_NAME"));
					}
							
					System.out.println("=================================================================");
				    System.out.println("구 선택 >");
					int regionsmallno = ScanUtil.nextInt();
							
					if(regionsmallno > 0){
					  Map<String, Object> region_small_code_map = Region_small_list.get(regionsmallno - 1);
					  regionSmallCode = (String)region_small_code_map.get("REGION_CODE");
					  regionSmallCodeName = (String)region_small_code_map.get("REGION_SMALL_NAME");
					  System.out.println("▶"+regionLargeCodeName+","+regionSmallCodeName + " 을 선택하셨습니다.");
					}	
				}
			

			
			 Map<String, Object> param = new HashMap<>();
			 param.put("FACILITY_NAME", title);
			 param.put("FACILITY_CONTENT", content);
			 param.put("FACILITY_COMPANY_NUMBER", Integer.parseInt(companyNumber));
			 param.put("FACILITY START_TIME", openTime);
			 param.put("FACILITY END_TIME", closeTime);
			 param.put("FACILITY_AM_PAY", Integer.parseInt(AmPay));
			 param.put("FACILITY_PM_PAY", Integer.parseInt(PmPay));
			 param.put("FACILITY_POSTCODE", Integer.parseInt(postNumber));
			 param.put("FACILITY_ADDRESS", address);
			 param.put("FACILITY_ADDRESS_Detail", addressDetail);
			 param.put("FACILITY_PHONE_NUMBER", phoneNumber);
			 param.put("FACILITY_CODE", facilityCode);
			 param.put("REGION_CODE", regionSmallCode);
			 
			int result = smemMyPageDao.insertMyFac1(title, content, companyNumber, openTime, closeTime, AmPay, PmPay, phoneNumber, postNumber, address, addressDetail, facilityCode, regionSmallCode);
			System.out.println("등록이 성공되었습니다.");
			
		}
    
    
    
}
    
   
    
    
        
     

