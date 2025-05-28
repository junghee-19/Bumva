


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DB {

	private static Connection conn;

	public static void init() {
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/Bumva?serverTimezone=UTC&useUniCode=yes&characterEncoding=UTF-8", 
					"root", "rootroot");
			System.out.println("DB 연결 성공");
		} catch (ClassNotFoundException e) {
			System.err.println("해당 드라이버를 찾지 못했습니다 : ");
			e.printStackTrace();
		} catch (SQLException e) {
			System.err.println("SQL 오류 발생 : ");
			e.printStackTrace();
		}		
	}
	
	// SELECT
	public static ResultSet executeQuery(String sql, Object... params ) {
		try{
			PreparedStatement pstmt = conn.prepareStatement(sql);
			setParams(pstmt, params);
			return pstmt.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} 
	}
	
	
	// INSERT, UPDATE, DELETE 
	public static int executeUpdate(String sql, Object... params) {
		try(PreparedStatement pstmt = conn.prepareStatement(sql)){
			setParams(pstmt, params);
			return pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}

	private static void setParams(PreparedStatement pstmt, Object... params) throws SQLException {
		for (int i = 0; i < params.length; i++) {
			pstmt.setObject(i+1, params[i]);
		}
		
	}
}
