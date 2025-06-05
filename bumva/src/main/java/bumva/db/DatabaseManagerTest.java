package bumva.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class DatabaseManagerTest {
    public static void main(String[] args) {
        try {
            Connection conn = DatabaseManager.getConnection();
            Statement stmt = conn.createStatement();

            // 예: User 테이블 조회
            ResultSet rs = stmt.executeQuery("SELECT * FROM User");

            while (rs.next()) {
                System.out.println("🧑 ID: " + rs.getInt("id") + ", 이름: " + rs.getString("name"));
            }

            DatabaseManager.close(); // 연결 종료
        } catch (Exception e) {
            System.err.println("❌ DB 작업 중 오류 발생");
            e.printStackTrace();
        }
    }
}