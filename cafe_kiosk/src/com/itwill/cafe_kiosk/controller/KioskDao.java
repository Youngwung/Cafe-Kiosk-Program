package com.itwill.cafe_kiosk.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.itwill.cafe_kiosk.model.Kiosk;

import oracle.jdbc.OracleDriver;

import static com.itwill.cafe_kiosk.OracleJdbc.*;
import static com.itwill.cafe_kiosk.model.Kiosk.Entity.*;

public class KioskDao {
	private static KioskDao instance = null;
	public static KioskDao getInstance(){
		if (instance == null) {
			instance = new KioskDao();
		}
		return instance;
	}
	
	private KioskDao() {
		try {
			// Oracle 드라이버(라이브러리)를 등록
			DriverManager.registerDriver(new OracleDriver());
			// 단 한 번만 실행하면 됨. 그래서 생성자 안에서 만듦.
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// 리소스 해제 메소드
	private void closeResources(Connection conn, Statement stmt, ResultSet rs) {
		try {
			if(rs != null) rs.close();
			if(stmt != null) stmt.close();
			if(conn != null) conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// excuteupdate() 용 아규먼트2개짜리 리소스 해제 메소드
	private void closeResources(Connection conn, Statement stmt) {
		closeResources(conn, stmt, null);
	}
	
	private final String SQL_SELECT_ALL = String.format("select * from %s order by %s", TBL_MENUS, COL_PRICE);
	
	public List<Kiosk> read() {
		List<Kiosk> result = new ArrayList<Kiosk>();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			conn = DriverManager.getConnection(URL, USER, PASSWORD);
			stmt = conn.prepareStatement(SQL_SELECT_ALL);
			rs = stmt.executeQuery();
			
			while (rs.next()) {
				String menuName = rs.getString(COL_MENUNAME);
				int price = rs.getInt(COL_PRICE);
				String type = rs.getString(COL_TYPE);
				String photo = rs.getString(COL_PHOTO);
				
				Kiosk kiosk = new Kiosk(menuName, price, type, photo);
				result.add(kiosk);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeResources(conn, stmt, rs);
		}
		return result;
	}
	// sql = insert into kiosk (메뉴사진, 메뉴이름, 종류, 가격) values (?, ?, ?, ?)
	private final String SQL_INSERT = String.format("insert into %s (%s, %s, %s, %s) values (?, ?, ?, ?)", 
			TBL_MENUS,
			COL_PHOTO,
			COL_MENUNAME,
			COL_TYPE,
			COL_PRICE);
	
	public int create(Kiosk kiosk) {
		int result = 0;
		
		Connection conn = null;
		PreparedStatement stmt = null;
		String s =System.getProperty("user.home") + "\\Desktop\\imgs\\kiosk\\";
		try {
			conn = DriverManager.getConnection(URL, USER, PASSWORD);
			stmt = conn.prepareStatement(SQL_INSERT);
			stmt.setString(1, 
					s +kiosk.getPhoto()+".png");
			stmt.setString(2, kiosk.getMenuName());
			stmt.setString(3, kiosk.getType());
			stmt.setInt(4, kiosk.getPrice());
			result = stmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeResources(conn, stmt);
		}
		
		return result;
	}
	
	private static final String SQL_DELETE = String.format(
			"delete from %s where %s = ?",
			TBL_MENUS, COL_MENUNAME
			); 
	
	public int delete(String menuName) {
		int result = 0;
		
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = DriverManager.getConnection(URL, USER, PASSWORD);
			stmt = conn.prepareStatement(SQL_DELETE);
			stmt.setString(1, menuName);
			result = stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeResources(conn, stmt);
		}
		return result;
	}
	
	private static final String SQL_UPDATE = String.format(
            "update %s set %s = ?, %s = ?, %s = ? where %s = ?", 
            TBL_MENUS, COL_PHOTO, COL_TYPE, COL_PRICE, COL_MENUNAME);
	
	
	 public int update(Kiosk kiosk) {
	        int result = 0;
	        
	        Connection conn = null;
	        PreparedStatement stmt = null;
	        String s =System.getProperty("user.home") + "\\Desktop\\imgs\\kiosk\\"; // 바탕화면 절대경로
	        try {
	            conn = DriverManager.getConnection(URL, USER, PASSWORD);
	            stmt = conn.prepareStatement(SQL_UPDATE);
	            stmt.setString(1, 
	            		s +kiosk.getPhoto()+".png");
	            stmt.setString(2, kiosk.getType());
	            stmt.setInt(3, kiosk.getPrice());
	            stmt.setString(4, kiosk.getMenuName());
	            result = stmt.executeUpdate();
	            
	        } catch (SQLException e) {
	            e.printStackTrace();
	        } finally {
	            closeResources(conn, stmt);
	        }
	        
	        return result;
	    }
	 
	  private static final String SQL_SELECT_BY_MENUNAME = String.format(
	           "select * from %s where %s = ?", 
	            TBL_MENUS, COL_MENUNAME);
	  
	  
	  public Kiosk read(String menuName) {
	        Kiosk kiosk = null;
	        
	        Connection conn = null;
	        PreparedStatement stmt = null;
	        ResultSet rs = null;
	        String menuName1 = null;
	        int price = 0;
	        String type = null;
	        String photo = null;
	        try {
	            conn = DriverManager.getConnection(URL, USER, PASSWORD);
	            stmt = conn.prepareStatement(SQL_SELECT_BY_MENUNAME);
	            stmt.setString(1, menuName);
	            rs = stmt.executeQuery();
	            if (rs.next()) {
					menuName1 = rs.getString(COL_MENUNAME);
					price = rs.getInt(COL_PRICE);
					type = rs.getString(COL_TYPE);
					photo = rs.getString(COL_PHOTO);
					
					kiosk = new Kiosk(menuName1, price, type, photo);
	            }
	            
	        } catch (SQLException e) {
	            e.printStackTrace();
	        } finally {
	            closeResources(conn, stmt, rs);
	        }
	        
	        return kiosk;
	    }
	  
		private static final String SQL_SELECT_BY_TYPE = String.format(
				"select * from %s where lower(%s) like ? order by %s",
				TBL_MENUS, COL_TYPE, COL_PRICE); 
	  
		public List<Kiosk> search(String keyword) {
			List<Kiosk> result = new ArrayList<Kiosk>();
			
			Connection conn = null;
			PreparedStatement stmt = null;
			ResultSet rs = null;
			String searchKeyword = "%" + keyword.toLowerCase()+"%"; // like 검색에서 사용할 파라미터 <<<===이거 잘 모르겠음
	        String menuName1 = null;
	        int price = 0;
	        String type1 = null;
	        String photo = null;
			try {
				conn = DriverManager.getConnection(URL, USER, PASSWORD);
				if (searchKeyword.contains("ice")) {
					stmt = conn.prepareStatement(SQL_SELECT_BY_TYPE);
					stmt.setString(1, "ice");
				} else if (searchKeyword.contains("hot")) {
					stmt = conn.prepareStatement(SQL_SELECT_BY_TYPE);
					stmt.setString(1, "hot");
				} else if (searchKeyword.contains("ade")) {
					stmt = conn.prepareStatement(SQL_SELECT_BY_TYPE);
					stmt.setString(1, "ade");
				} else if (searchKeyword.contains("dessert")) {
					stmt = conn.prepareStatement(SQL_SELECT_BY_TYPE);
					stmt.setString(1, "dessert");
				} 
				rs = stmt.executeQuery();
				while (rs.next()) {
					menuName1 = rs.getString(COL_MENUNAME);
					price = rs.getInt(COL_PRICE);
					type1 = rs.getString(COL_TYPE);
					photo = rs.getString(COL_PHOTO);
					
					Kiosk kiosk = new Kiosk(menuName1, price, type1, photo);
					result.add(kiosk);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				closeResources(conn, stmt, rs);
			}
			
			return result;
		}
		
		private final String SQL_HISTORY_INSERT = String.format("insert into %s (%s, %s) values (?, ?)", 
				TBL_HISTORYS,
				COL_MENUNAME,
				COL_PRICE
				);
		public int addHistory(String menuName, int price) {
			int result = 0;
			
			Connection conn = null;
			PreparedStatement stmt = null;
			
			try {
				conn = DriverManager.getConnection(URL, USER, PASSWORD);
				stmt = conn.prepareStatement(SQL_HISTORY_INSERT);
				stmt.setString(1, menuName);
				stmt.setInt(2, price);
				result = stmt.executeUpdate();
				
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				closeResources(conn, stmt);
			}
			
			return result;
		}
		private final String SQL_HISTORY_DELETE = String.format("delete from %s", 
				TBL_HISTORYS
				);
		
		public int deleteHistory() {
			int result = 0;
			Connection conn = null;
			PreparedStatement stmt = null;
			try {
				conn = DriverManager.getConnection(URL, USER, PASSWORD);
				stmt = conn.prepareStatement(SQL_HISTORY_DELETE);
				result = stmt.executeUpdate();
				
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				closeResources(conn, stmt);
			}
			
			return result;
		}
		
		private final String SQL_SELECT_HISTORY = String.format(
				"select %s, %s, count(%s) as count from %s group by %s, %s",
				COL_MENUNAME, COL_PRICE, COL_MENUNAME, TBL_HISTORYS,COL_MENUNAME, COL_PRICE);
		
		public List<Kiosk> readHistory() {
			List<Kiosk> result = new ArrayList<Kiosk>();
			Connection conn = null;
			PreparedStatement stmt = null;
			ResultSet rs = null;
			
			try {
				conn = DriverManager.getConnection(URL, USER, PASSWORD);
				stmt = conn.prepareStatement(SQL_SELECT_HISTORY);
				rs = stmt.executeQuery();
				
				while (rs.next()) {
					String menuName = rs.getString(COL_MENUNAME);
					int count = rs.getInt("count");
					int price = rs.getInt(COL_PRICE);
					Kiosk kiosk = new Kiosk(menuName, count, price);
					result.add(kiosk);
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				closeResources(conn, stmt, rs);
			}
			return result;
		}
		
		
		// select count(menuName) from historys group by menuName
		private final String SQL_HISTORY_SELECT_COUNT = String.format(
				"select count(?) as counts from %s where %s = ? group by ?", 
				TBL_HISTORYS,
				COL_MENUNAME);
		
		public int getCount(String menuName) {
			  Connection conn = null;
		        PreparedStatement stmt = null;
		        ResultSet rs = null;
		        int count = 0;
		        try {
		            conn = DriverManager.getConnection(URL, USER, PASSWORD);
		            stmt = conn.prepareStatement(SQL_HISTORY_SELECT_COUNT);
		            stmt.setString(1, menuName);
		            stmt.setString(2, menuName);
		            stmt.setString(3, menuName);
		            rs = stmt.executeQuery();
		            if (rs.next()) {
						count = rs.getInt("counts");
						
		            }
		        } catch (SQLException e) {
		            e.printStackTrace();
		        } finally {
		            closeResources(conn, stmt, rs);
		        }
			return count;
		}
		
		
		
	 
}
