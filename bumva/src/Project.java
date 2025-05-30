import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class Project extends JFrame {
    public Project() {
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
        contentPane.add(centerPanel, BorderLayout.CENTER);
        centerPanel.setLayout(new MigLayout("", "[1200px]", "[39px][785px]"));

        // 검색 패널
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JTextField searchField = new JTextField("선수 검색창", 40);  // 약 644px
        JButton searchBtn = new JButton("🔍");
        searchPanel.add(searchField);
        searchPanel.add(searchBtn);
        centerPanel.add(searchPanel, "cell 0 0,growx,aligny top");

        // 중앙 하단 콘텐츠
        JPanel bodyPanel = new JPanel();
        centerPanel.add(bodyPanel, "cell 0 1,grow");
        bodyPanel.setLayout(new MigLayout("insets 0, alignx center",     // align → alignx 로 수정
        	    "[grow, center][grow, center]", // column constraints
        	    "[grow, center]" ));

        // 좌측: 이미지 그리드
        JPanel imagePanel = new JPanel();
        imagePanel.setLayout(new GridLayout(4, 6, 5, 5));
        for (int i = 1; i <= 24; i++) {
            imagePanel.add(new JLabel(new ImageIcon("placeholder.png"))); // 이미지 교체 필요
        }
        bodyPanel.add(new JScrollPane(imagePanel), "cell 0 0,grow");

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

        // 표
        String[] columnNames = { "순위", "선수명", "팀명", "ERA", "G", "W", "L", "SV", "HLD", "WPCT", "IP", "H", "HR", "BB", "HBP", "SO", "R", "ER", "WHIP" };
        Object[][] data = new Object[20][columnNames.length]; // 더미 데이터
        JTable table = new JTable(new DefaultTableModel(data, columnNames));
        JScrollPane tableScroll = new JScrollPane(table);
        rightPanel.add(tableScroll, BorderLayout.CENTER);

        bodyPanel.add(rightPanel, "cell 1 0,grow");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Project().setVisible(true);
        });
    }
}