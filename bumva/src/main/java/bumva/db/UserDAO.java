	package bumva.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import bumva.db.DatabaseManager;

public class UserDAO {

    // 로그인 기능: 사용자 ID와 비밀번호가 일치하는지 확인
    public static boolean login(String userId, String password) {
        String sql = "SELECT * FROM User WHERE user_id = ? AND password = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, userId);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();

            // 결과가 있으면 로그인 성공
            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    //
    public static boolean registerUser(String userId, String nickname, String password,String teamName) {
        String sql = "INSERT INTO User (user_id, nickname, password, team) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userId);
            pstmt.setString(2, nickname);
            pstmt.setString(3, password);
            pstmt.setString(4, teamName);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    

    // 아이디 중복 검사
    public static boolean isUserIdTaken(String userId) {
        String sql = "SELECT user_id FROM User WHERE user_id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, userId);

            ResultSet rs = pstmt.executeQuery();
            return rs.next(); // 이미 존재하면 true

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 닉네임 중복 검사
    public static boolean isNicknameTaken(String nickname) {
        String sql = "SELECT nickname FROM User WHERE nickname = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nickname);

            ResultSet rs = pstmt.executeQuery();
            return rs.next(); // 이미 존재하면 true

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}