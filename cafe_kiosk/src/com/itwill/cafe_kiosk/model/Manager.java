package com.itwill.cafe_kiosk.model;

public class Manager {
	
	public static final class ManageColumn {
		public static final String TBL_MANAGER = "manager";
		
		public static final String COL_NAME = "name";
		public static final String COL_ID = "id";
		public static final String COL_PASSWORD = "password";
	}
	
	private String name;
	private String id;
	private String password;
	
	public Manager(String name, String id, String password) {
		super();
		this.name = name;
		this.id = id;
		this.password = password;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}


}
