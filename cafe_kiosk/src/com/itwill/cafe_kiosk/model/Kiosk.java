package com.itwill.cafe_kiosk.model;

public class Kiosk{
	
	public static final class Entity {
		// 데이터베이스 BLOGS 테이블의 "컬럼 이름"들을 상수로 선언.
		public static final String TBL_MENUS = "menus";
		public static final String COL_MENUNAME = "menuName";
		public static final String COL_PRICE = "price";
		public static final String COL_TYPE = "type";
		public static final String COL_PHOTO = "photo";
		
		public static final String TBL_HISTORYS = "historys";
	}
	
	private String menuName;
	private int price;
	private String type;
	private String photo;
	private int count;
	
	public int getCount() {
		return count;
	}
	
	public void setCount(int count) {
		this.count = count;
		}
	
	public String getMenuName() {
		return menuName;
	}
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	
	public Kiosk() {}
	
	public Kiosk(String menuName, int count, int price) {
		this.menuName = menuName;
		this.count = count;
		this.price = price;
	}
	
	public Kiosk(String menuName, int price, String type, String photo) {
		super();
		this.menuName = menuName;
		this.price = price;
		this.type = type;
		this.photo = photo;
	}
	
	@Override
	public String toString() {
		return "Kiosk [menuName=" + menuName + ", price=" + price + ", type=" + type + ", photo=" + photo + "]";
	}
	

	
	
	
}
