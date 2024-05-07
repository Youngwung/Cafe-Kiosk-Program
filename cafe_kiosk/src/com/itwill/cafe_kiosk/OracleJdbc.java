package com.itwill.cafe_kiosk;

public interface OracleJdbc {
	// [public static final 생략 가능]
	
	// 오라클 데이터베이스에 접속하기 위한 라이브러리 정보와 서버 주소/포트/SID 정보
	String URL = "jdbc:oracle:thin:@192.168.20.01:1521:xe"; 
//	String URL = "jdbc:oracle:thin:@localhost:1521:xe"; 
	// 프로젝트 진행 시 IP주소를 cmd에서 ipconfig 실행 후 나온 주소로 사용하여 
	// 발표 시 자기 IP 주소로 접속할 수 있게 한다.
	
	// 오라클 데이터베이스에 접속할 수 있는 계정 이름
	String USER = "scott";
	
	// 오라클 데이터베이스에 접속할 때 사용할 비밀번호
	String PASSWORD = "tiger";
	
	

}
