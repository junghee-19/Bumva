package bumva.db;

import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class TeamDAO {
    public static DefaultTableModel getTeamData() {
        String[] columns = { "순위", "팀명", "경", "승리", "무", "패", "win_rate", "game_diff" };
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM team_rank_v2")) {

            while (rs.next()) {
                Object[] row = {
                    rs.getInt("team_rank"),
                    rs.getString("team_name"),
                    rs.getInt("games"),
                    rs.getInt("wins"),
                    rs.getInt("draws"),
                    rs.getFloat("losses"),
                    rs.getInt("win_rate"),
                    rs.getInt("game_diff")
                };
                model.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return model;
    }
}