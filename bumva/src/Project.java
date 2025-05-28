import java.awt.*;
import java.sql.ResultSet;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

@SuppressWarnings("serial")
public class Project extends JFrame {
    public Project() {
    	DB.init();
        setTitle("메인 프레임");
        setSize(1200, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 전체 레이아웃
        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());
        setContentPane(contentPane);

        // 상단 제목
        JLabel titleLabel = new JLabel("메인 프레임", SwingConstants.CENTER);
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 40));
        contentPane.add(titleLabel, BorderLayout.NORTH);

        // 중앙 전체 패널
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());
        contentPane.add(centerPanel, BorderLayout.CENTER);

        // 검색 패널
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JTextField searchField = new JTextField("선수 검색창", 40);  // 약 644px
        JButton searchBtn = new JButton("🔍");
        searchPanel.add(searchField);
        searchPanel.add(searchBtn);
        centerPanel.add(searchPanel, BorderLayout.NORTH);

        // 중앙 하단 콘텐츠
        JPanel bodyPanel = new JPanel();
        bodyPanel.setLayout(new GridLayout(1, 2));
        centerPanel.add(bodyPanel, BorderLayout.CENTER);

        // 좌측: 이미지 그리드
        JPanel imagePanel = new JPanel();
        imagePanel.setLayout(new GridLayout(4, 6, 5, 5));
        for (int i = 1; i <= 24; i++) {
            imagePanel.add(new JLabel(new ImageIcon("placeholder.png"))); // 이미지 교체 필요
        }
        bodyPanel.add(new JScrollPane(imagePanel));

        // 우측: 피처 버튼 + 표
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());

        // 버튼 패널
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 3));
        buttonPanel.add(new JButton("파워피처🔥"));
        buttonPanel.add(new JButton("정밀피처"));
        buttonPanel.add(new JButton("표준피처"));
        rightPanel.add(buttonPanel, BorderLayout.NORTH);

        
        DefaultTableModel model = new DefaultTableModel();
     // 1. 컬럼 이름 추가
        model.addColumn("ID");
        model.addColumn("순위");
        model.addColumn("이름");
        model.addColumn("팀");
        model.addColumn("ERA");
        model.addColumn("경기");
        model.addColumn("승");
        model.addColumn("패");
        model.addColumn("승률");
        model.addColumn("이닝");
        model.addColumn("피안타");
        model.addColumn("피홈런");
        model.addColumn("볼넷");
        model.addColumn("사구");
        model.addColumn("삼진");
        model.addColumn("실점");
        model.addColumn("자책");
        model.addColumn("WHIP");

        // 2. DB에서 데이터 가져와 테이블에 넣기
        try {
            String sql = "SELECT id, `rank`, name, team, era, g, w, l, wpct, ip, h, hr, bb, hbp, so, r, er, whip FROM Bumva.pitcher";
            ResultSet rs = DB.executeQuery(sql);

            while (rs.next()) {
                Object[] row = {
                    rs.getInt("id"),
                    rs.getInt("rank"),
                    rs.getString("name"),
                    rs.getString("team"),
                    rs.getDouble("era"),
                    rs.getInt("g"),
                    rs.getInt("w"),
                    rs.getInt("l"),
                    rs.getDouble("wpct"),
                    rs.getDouble("ip"),
                    rs.getInt("h"),
                    rs.getInt("hr"),
                    rs.getInt("bb"),
                    rs.getInt("hbp"),
                    rs.getInt("so"),
                    rs.getInt("r"),
                    rs.getInt("er"),
                    rs.getDouble("whip")
                };
                model.addRow(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        JTable table = new JTable(model);
     // 열 너비 수동 조정
        table.getColumnModel().getColumn(0).setPreferredWidth(30);  // ID
        table.getColumnModel().getColumn(1).setPreferredWidth(40);  // 순위
        table.getColumnModel().getColumn(2).setPreferredWidth(80);  // 이름
        table.getColumnModel().getColumn(3).setPreferredWidth(80);  // 팀
        table.getColumnModel().getColumn(4).setPreferredWidth(100);  // ERA
        table.getColumnModel().getColumn(5).setPreferredWidth(40);  // 경기
        table.getColumnModel().getColumn(6).setPreferredWidth(40);  // 승
        table.getColumnModel().getColumn(7).setPreferredWidth(40);  // 패
        table.getColumnModel().getColumn(8).setPreferredWidth(100);  // 승률
        table.getColumnModel().getColumn(9).setPreferredWidth(60);  // 이닝
        table.getColumnModel().getColumn(10).setPreferredWidth(60); // 피안타
        table.getColumnModel().getColumn(11).setPreferredWidth(60); // 피홈런
        table.getColumnModel().getColumn(12).setPreferredWidth(60); // 볼넷
        table.getColumnModel().getColumn(13).setPreferredWidth(60); // 사구
        table.getColumnModel().getColumn(14).setPreferredWidth(60); // 삼진
        table.getColumnModel().getColumn(15).setPreferredWidth(60); // 실점
        table.getColumnModel().getColumn(16).setPreferredWidth(60); // 자책
        table.getColumnModel().getColumn(17).setPreferredWidth(60); // WHIP
        JScrollPane tableScroll = new JScrollPane(table);
        rightPanel.add(tableScroll, BorderLayout.CENTER);

        bodyPanel.add(rightPanel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Project().setVisible(true);
        });
    }
}