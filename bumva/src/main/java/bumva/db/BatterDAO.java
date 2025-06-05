package bumva.db;

import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class BatterDAO {
    public static DefaultTableModel getBatterData() {
        String[] columns = { "순위", "이름", "팀", "타율", "홈런", "타점", "도루", "출루율", "장타율" };
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM batter_rank_v2")) {

            while (rs.next()) {
                Object[] row = {
                    rs.getInt("batter_rank"),
                    rs.getString("name"),
                    rs.getString("team"),
                    rs.getFloat("avg"),
                    rs.getInt("hr"),
                    rs.getInt("rbi"),
                    rs.getInt("sb"),
                    rs.getFloat("obp"),
                    rs.getFloat("slg")
                };
                model.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return model;
    }
}