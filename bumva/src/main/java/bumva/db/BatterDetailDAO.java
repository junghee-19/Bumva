package bumva.db;

import java.sql.*;

public class BatterDetailDAO {
    private static String tableName;
    private static String tableName2;

    /**
     * playerName: 선수 이름
     * position: "batters" 또는 "pitchers"
     * 반환: [0] = 번역된 컬럼명 배열, [1] = 해당 컬럼값 배열
     */
    public static String[][] getBatterData(String playerName, String position) {
        String[][] batterData = new String[2][14];

        // 테이블 및 검색 컬럼 설정
        if ("batters".equals(position)) {
            tableName = "batter_rank_v2";
            tableName2 = "batter_name";
        } else if ("pitchers".equals(position)) {
            tableName = "pitcher_rank_v2";
            tableName2 = "name";
        } else {
            System.out.println("Invalid position: " + position);
            return batterData; // 빈 배열
        }

        String query = "SELECT * FROM " + tableName + " WHERE " + tableName2 + " = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, playerName);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int columnCount = rs.getMetaData().getColumnCount();
                for (int i = 0; i < columnCount; i++) {
                    String raw = rs.getMetaData().getColumnName(i + 1);
                    String label;
                    // 컬럼명 한글 매핑
                    if ("batters".equals(position)) {
                        switch (raw) {
                            case "id":           label = "ID"; break;
                            case "batter_rar":
                            case "batter_rank":  label = "타자 순위"; break;
                            case "batter_na":
                            case "batter_name":  label = "이름"; break;
                            case "team":         label = "팀"; break;
                            case "avg":          label = "타율"; break;
                            case "g":            label = "경기 수"; break;
                            case "pa":           label = "타석"; break;
                            case "ab":           label = "안타"; break;
                            case "hr":           label = "홈런"; break;
                            case "sb":           label = "도루"; break;
                            case "slg":          label = "장타율"; break;
                            case "ops":          label = "OPS"; break;
                            case "created_at":
                            case "created_a":    label = "생성일"; break;
                            case "updated_at":
                            case "updated_a":    label = "수정일"; break;
                            default:              label = raw;       break;
                        }
                    } else {
                        switch (raw) {
                            case "pitcher_rank":   label = "투수 순위"; break;
                            case "name":         label = "이름"; break;
                            case "team":         label = "팀"; break;
                            case "era":          label = "평균 자책점"; break;
                            case "whip":         label = "WHIP"; break;
                            case "ip":           label = "이닝"; break;
                            case "h":            label = "피안타"; break;
                            case "hr":           label = "피홈런"; break;
                            case "bb":           label = "볼넷"; break;
                            case "hbp":          label = "사구"; break;
                            case "so":           label = "탈삼진"; break;
                            case "r":            label = "실점"; break;
                            case "er":           label = "자책점"; break;
                            default:              label = raw;       break;
                        }
                    }
                    // 레이블과 값 저장
                    batterData[0][i] = label;
                    batterData[1][i] = rs.getString(i + 1);
                }
            } else {
                System.out.println("No data found for player: " + playerName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return batterData;
    }
}