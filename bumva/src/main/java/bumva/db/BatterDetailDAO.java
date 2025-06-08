package bumva.db;

import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class BatterDetailDAO {
	
    public static String[][] getBatterData(String playerName) {
		String[][] batterData = new String[2][14];
		String query = "SELECT * FROM batter_rank_v2 WHERE batter_name = " + "'" + playerName + "'";
		
		try (Connection conn = DatabaseManager.getConnection();
			 PreparedStatement pstmt = conn.prepareStatement(query);
			 ResultSet rs = pstmt.executeQuery()) {

			if (rs.next()) {
				// 자동으로 컬럼
				// 자동으로 [0][0]에 순위, [0][1]에 이름, [0][2]에 팀, [0][3]에 타율 등등이 들어감
				// 자동으로 [1][0] 부터 컬럼에 해당하는 데이터가 들어감
				
				int columnCount = rs.getMetaData().getColumnCount();
				for (int i = 0; i < columnCount; i++) {
					batterData[0][i] = rs.getMetaData().getColumnName(i + 1); // 컬럼 이름
					batterData[1][i] = rs.getString(i + 1); // 데이터 값
				}
				
				// 예시: batterData[0][0] = "순위", batterData[1][0] = "1"
				
				
			} else {
				System.out.println("No data found for player: " + playerName);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return batterData;
		
    }
}