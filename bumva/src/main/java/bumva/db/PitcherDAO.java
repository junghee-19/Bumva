package bumva.db;

import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class PitcherDAO {

    public static DefaultTableModel getPitcherData() {
        String[] columns = { "순위", "이름", "팀", "ERA", "WHIP", "이닝", "피안타", "피홈런", "볼넷", "사구", "삼진", "실점", "자책점" };
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseManager.getConnection();
            if (conn == null) {
                System.err.println("[ERROR] DB 연결 실패: null 리턴됨");
                return model;
            }

            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM pitcher_rank_v2");

            while (rs.next()) {
                Object[] row = {
                    rs.getInt("pitcher_rank"),
                    rs.getString("name"),
                    rs.getString("team"),
                    rs.getFloat("era"),
                    rs.getFloat("whip"),
                    rs.getString("ip"),
                    rs.getInt("h"),
                    rs.getInt("hr"),
                    rs.getInt("bb"),
                    rs.getInt("hbp"),
                    rs.getInt("so"),
                    rs.getInt("r"),
                    rs.getInt("er")
                };
                model.addRow(row);
            }

        } catch (SQLException e) {
            System.err.println("[SQLException] DB 조회 중 오류 발생:");
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("[SQLException] 자원 해제 중 오류:");
                e.printStackTrace();
            }
        }

        return model;
    }
}