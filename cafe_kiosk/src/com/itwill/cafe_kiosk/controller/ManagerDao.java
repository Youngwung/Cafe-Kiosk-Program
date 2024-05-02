package com.itwill.cafe_kiosk.controller;

import static com.itwill.cafe_kiosk.OracleJdbc.*;
import static com.itwill.cafe_kiosk.model.Manager.ManageColumn.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.itwill.cafe_kiosk.model.Kiosk;
import com.itwill.cafe_kiosk.model.Manager;

import oracle.jdbc.OracleDriver;

public class ManagerDao {
	private static ManagerDao instance = null;
	public static ManagerDao getInstance() {
		if(instance == null) {
			instance = new ManagerDao();
		}
		return instance;
	}
	private ManagerDao() {
		try {
			// Oracle 드라이버(라이브러리)를 등록
			DriverManager.registerDriver(new OracleDriver());
			// 단 한 번만 실행하면 됨. 그래서 생성자 안에서 만듦.
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
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
	
	private final String SQL_SELECT_ALL = String.format("select * from %s", TBL_MANAGER);
	
	public List<Manager> read() {
		List<Manager> result = new ArrayList<Manager>();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			conn = DriverManager.getConnection(URL, USER, PASSWORD);
			stmt = conn.prepareStatement(SQL_SELECT_ALL);
			rs = stmt.executeQuery();
			
			while (rs.next()) {
				String name = rs.getString(COL_NAME);
				String id = rs.getString(COL_ID);
				String password = rs.getString(COL_PASSWORD);
				Manager manager = new Manager(name, id, password);
				result.add(manager);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeResources(conn, stmt, rs);
		}
		return result;
	}
	// insert into manager values ('김영웅', 'hero', '1234');
	private final String SQL_INSERT = String.format("insert into %s values (?, ?, ?)", TBL_MANAGER);
	
	public int create(Manager manager) {
		int result = 0;
		
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			conn = DriverManager.getConnection(URL, USER, PASSWORD);
			stmt = conn.prepareStatement(SQL_INSERT);
			stmt.setString(1, manager.getName());
			stmt.setString(2, manager.getId());
			stmt.setString(3, manager.getPassword());
			result = stmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeResources(conn, stmt);
		}
		
		return result;
	}
	
	
	private final String SQL_SELECT_FOR_LOGIN = String.format("select %s, %s from %s where %s = ?", COL_ID, COL_PASSWORD, TBL_MANAGER, COL_ID);
	
	public Manager read(String id) {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Manager manager = null;
		
		try {
			conn = DriverManager.getConnection(URL, USER, PASSWORD);
			stmt = conn.prepareStatement(SQL_SELECT_FOR_LOGIN);
			stmt.setString(1, id);
			rs = stmt.executeQuery();
			if (rs.next()) {
				String id1 = rs.getString(COL_ID);
				String password = rs.getString(COL_PASSWORD);
				manager = new Manager(null, id1, password);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeResources(conn, stmt, rs);
		}
		return manager;
	}
	
	
	
}
