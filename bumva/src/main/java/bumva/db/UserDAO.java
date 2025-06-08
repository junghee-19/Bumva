package bumva.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import bumva.db.DatabaseManager;

public class UserDAO {

    /**
     * 로그인 기능: 사용자 ID와 비밀번호가 일치하는지 확인
     * @param userId   사용자 ID
     * @param password 비밀번호
     * @return 일치하면 true
     */
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

    /**
     * 회원가입 기능 (팀 정보 포함)
     * @param userId   사용자 ID
     * @param nickname 닉네임
     * @param password 비밀번호
     * @param teamName 팀 이름
     * @return 성공하면 true
     */
    public static boolean registerUser(String userId, String nickname, String password, String teamName) {
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

    /**
     * 회원가입 기능 (팀 정보 없이)
     * @param userId   사용자 ID
     * @param nickname 닉네임
     * @param password 비밀번호
     * @return 성공하면 true
     */
    public static boolean registerUser(String userId, String nickname, String password) {
        String sql = "INSERT INTO User (user_id, nickname, password) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, userId);
            pstmt.setString(2, nickname);
            pstmt.setString(3, password);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 아이디 중복 검사
     * @param userId 확인할 ID
     * @return 이미 존재하면 true
     */
    public static boolean isUserIdTaken(String userId) {
        String sql = "SELECT user_id FROM User WHERE user_id = ?";
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

    /**
     * 닉네임 중복 검사
     * @param nickname 확인할 닉네임
     * @return 이미 존재하면 true
     */
    public static boolean isNicknameTaken(String nickname) {
        String sql = "SELECT nickname FROM User WHERE nickname = ?";
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

    /**
     * 사용자 ID로 닉네임 조회
     * @param userId 조회할 사용자 ID
     * @return 닉네임, 없으면 null
     */
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

    /**
     * 사용자 ID로 팀 이름 조회
     * @param userId 조회할 사용자 ID
     * @return 팀 이름, 없으면 null
     */
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