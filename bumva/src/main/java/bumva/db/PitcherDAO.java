package bumva.db;

import java.sql.*;
import java.util.*;
import javax.swing.table.DefaultTableModel;

public class PitcherDAO {
    public static DefaultTableModel getPitcherData() {
        String[] columns = { "순위", "이름", "팀", "ERA", "WHIP", "이닝", "피안타", "피홈런", "볼넷", "사구", "삼진", "실점", "자책점" };
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM pitcher_rank_v2")) {

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
            e.printStackTrace();
        }

        return model;
    }
}