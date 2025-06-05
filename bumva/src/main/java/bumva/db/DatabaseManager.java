package bumva.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {

    private static final String URL = "jdbc:mysql://localhost:3306/bumva?serverTimezone=Asia/Seoul";
    private static final String USER = "root";
    private static final String PASSWORD = "rootroot";

    private static Connection connection;

    static {
        try {
            // JDBC 드라이버 로드 (MySQL 8 이상)
            Class.forName("com.mysql.cj.jdbc.Driver");

            // DB 연결 시도
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("✅ DatabaseManager: DB 연결 성공");
        } catch (ClassNotFoundException e) {
            System.err.println("❌ MySQL JDBC 드라이버를 찾을 수 없습니다.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("❌ DB 연결 실패 (SQLException)");
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        return connection;
    }

    public static void close() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("🔒 DB 연결 종료");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}