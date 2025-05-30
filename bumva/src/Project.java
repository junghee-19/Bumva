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
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout());

        // 🟦 상단 바 (제목 + 버튼들)
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setPreferredSize(new Dimension(1200, 60));
        topBar.setBackground(new Color(30, 30, 60));

        JLabel titleLabel = new JLabel("메인 프레임");
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));
        topBar.add(titleLabel, BorderLayout.WEST);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        buttonPanel.setOpaque(false);
        
        JButton signupButton = new JButton("회원가입");
        signupButton.addActionListener(e -> showSignupDialog());
        buttonPanel.add(signupButton);

        JButton loginButton = new JButton("로그인");
        loginButton.addActionListener(e -> showLoginDialog());  // 🔹 추가
        buttonPanel.add(loginButton);

        topBar.add(buttonPanel, BorderLayout.EAST);
        contentPane.add(topBar, BorderLayout.NORTH);

        // 🔲 중앙 전체 패널
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new MigLayout("", "[grow]", "[39px][grow]"));
        contentPane.add(centerPanel, BorderLayout.CENTER);

        // 🔍 검색 패널
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JTextField searchField = new JTextField("선수 검색창", 40);
        JButton searchBtn = new JButton("🔍");
        searchPanel.add(searchField);
        searchPanel.add(searchBtn);
        centerPanel.add(searchPanel, "cell 0 0, growx");

        // 📦 콘텐츠 패널
        JPanel bodyPanel = new JPanel(new MigLayout("insets 0, alignx center", "[grow, center][grow, center]", "[grow]"));
        centerPanel.add(bodyPanel, "cell 0 1, grow");

        // 🖼️ 좌측 이미지 그리드
        JPanel imagePanel = new JPanel(new GridLayout(4, 6, 5, 5));
        for (int i = 1; i <= 24; i++) {
            imagePanel.add(new JLabel(new ImageIcon("placeholder.png"))); // 이미지 교체
        }
        bodyPanel.add(new JScrollPane(imagePanel), "cell 0 0, grow");

        // 📊 우측 표와 버튼
        JPanel rightPanel = new JPanel(new BorderLayout());

        JPanel featureButtons = new JPanel(new GridLayout(1, 3));
        featureButtons.add(new JButton("파워피처🔥"));
        featureButtons.add(new JButton("정밀피처"));
        featureButtons.add(new JButton("표준피처"));
        rightPanel.add(featureButtons, BorderLayout.NORTH);

        String[] columnNames = {
            "순위", "선수명", "팀명", "ERA", "G", "W", "L", "SV", "HLD", "WPCT",
            "IP", "H", "HR", "BB", "HBP", "SO", "R", "ER", "WHIP"
        };
        Object[][] data = new Object[20][columnNames.length];
        JTable table = new JTable(new DefaultTableModel(data, columnNames));
        JScrollPane tableScroll = new JScrollPane(table);
        rightPanel.add(tableScroll, BorderLayout.CENTER);

        bodyPanel.add(rightPanel, "cell 1 0, grow");
    }

    // 🔽 회원가입 모달창
    private void showSignupDialog() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        JTextField nameField = new JTextField();
        JTextField idField = new JTextField();
        JPasswordField pwField = new JPasswordField();
        JTextField nickField = new JTextField();

        panel.add(new JLabel("이름:"));
        panel.add(nameField);
        panel.add(new JLabel("아이디:"));
        panel.add(idField);
        panel.add(new JLabel("비밀번호:"));
        panel.add(pwField);
        panel.add(new JLabel("닉네임:"));
        panel.add(nickField);

        int result = JOptionPane.showConfirmDialog(
            this, panel, "회원가입", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            JOptionPane.showMessageDialog(this, "회원가입에 성공하셨습니다!");
        }
    }

    // 🔽 로그인 모달창 (아이디 + 비밀번호만)
    private void showLoginDialog() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        JTextField idField = new JTextField();
        JPasswordField pwField = new JPasswordField();

        panel.add(new JLabel("아이디:"));
        panel.add(idField);
        panel.add(new JLabel("비밀번호:"));
        panel.add(pwField);

        int result = JOptionPane.showConfirmDialog(
            this, panel, "로그인", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            JOptionPane.showMessageDialog(this, "로그인에 성공하셨습니다!");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Project().setVisible(true));
    }
}