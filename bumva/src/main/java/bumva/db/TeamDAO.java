package bumva.db;

import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class TeamDAO {
    public static DefaultTableModel getTeamData() {
        String[] columns = { "순위", "팀명", "승", "패", "무", "승률", "득점", "실점", "홈런", "팀타율" };
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM team_rank_v2")) {

            while (rs.next()) {
                Object[] row = {
                    rs.getInt("team_rank"),
                    rs.getString("team"),
                    rs.getInt("w"),
                    rs.getInt("l"),
                    rs.getInt("d"),
                    rs.getFloat("win_rate"),
                    rs.getInt("rs"),
                    rs.getInt("ra"),
                    rs.getInt("hr"),
                    rs.getFloat("avg")
                };
                model.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return model;
    }
}