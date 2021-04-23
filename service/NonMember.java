package service;

import java.util.List;
import java.util.Map;

import util.ScanUtil;
import util.View;
import dao.NonMemverDao;


public class NonMember {

	private NonMember(){};
	private static NonMember instance;
	public static NonMember getInstance(){
		if (instance == null) {
			instance = new NonMember();
		}
		return instance;
	}
	
	NonMemverDao nonMemverDao = NonMemverDao.getInstance();
	
	public int non(){
		List<Map<String, Object>> no = nonMemverDao.count();
		System.out.println("----------------------------------------------");
		System.out.println("방문자수 : "+ no.get(0).get("NONUSER_NUMBER"));
		System.out.println();
		System.out.println("1.로그인  2.회원가입");
		System.out.println("3.게시판  4.시설조회  0.종료");

		System.out.println("----------------------------------------------");
		System.out.println("입력");
		int input = ScanUtil.nextInt();
		switch (input) {
		case 1: return View.LOGIN;
		case 2: return View.JOIN;
		case 3: return View.BOARD;
		case 4: return View.FACILITY_VIEW;
		case 0: System.out.println("종료됩니다.");
				System.exit(0);
		}
		
		
		
		
		
		return View.HOME;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
