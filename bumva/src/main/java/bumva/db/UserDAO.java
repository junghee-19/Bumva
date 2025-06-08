package bumva.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    /** 로그인 체크 */
    public static boolean login(String userId, String password) {
        String sql = "SELECT * FROM User WHERE user_id = ? AND password = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, userId);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /** 아이디 중복 검사 */
    public static boolean isUserIdTaken(String userId) {
        String sql = "SELECT 1 FROM User WHERE user_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, userId);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /** 닉네임 중복 검사 */
    public static boolean isNicknameTaken(String nickname) {
        String sql = "SELECT 1 FROM User WHERE nickname = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nickname);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /** 회원가입 (팀 정보 포함) */
    public static boolean registerUser(String userId, String nickname, String password, String team) {
        String sql = "INSERT INTO User (user_id, nickname, password, team) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, userId);
            pstmt.setString(2, nickname);
            pstmt.setString(3, password);
            pstmt.setString(4, team);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /** 아이디로 닉네임 조회 */
    public static String getNickname(String userId) {
        String sql = "SELECT nickname FROM User WHERE user_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, userId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("nickname");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /** 아이디로 팀 이름 조회 추가 */
    public static String getTeam(String userId) {
        String sql = "SELECT team FROM User WHERE user_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, userId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("team");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}