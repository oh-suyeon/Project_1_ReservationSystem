package dao;

public class ManagerDao {

	private ManagerDao(){};
	private static ManagerDao instance;
	public static ManagerDao getInstance(){
		if (instance == null) {
			instance = new ManagerDao();
		}
		return instance;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
