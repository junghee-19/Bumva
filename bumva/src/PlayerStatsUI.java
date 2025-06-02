import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;

public class PlayerStatsUI extends JFrame {

    private static final String FONT_PATH = "/Users/choejeonghui/Documents/GitHub/Bumva/bumva/resource/fonts/The Jamsil 5 Bold.ttf";
    private CardLayout cardLayout;
    private JPanel mainContentPanel;

    public PlayerStatsUI() {
        setTitle("선수 상세 프레임");
        setSize(1200, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel contentPane = new JPanel(new BorderLayout());
        setContentPane(contentPane);

        // ───── 상단 바 ─────
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setPreferredSize(new Dimension(1200, 60));
        topBar.setBackground(new Color(30, 30, 60));

        JLabel titleLabel = new JLabel("선수 상세 프레임");
        titleLabel.setFont(loadCustomFont(28f, true));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));
        topBar.add(titleLabel, BorderLayout.WEST);
        contentPane.add(topBar, BorderLayout.NORTH);

        // ───── 메인 패널 ─────
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        JPanel playerInfoPanel = new JPanel(new BorderLayout());
        playerInfoPanel.setPreferredSize(new Dimension(1200, 300));
        playerInfoPanel.setBackground(new Color(40, 40, 50));

        JLabel playerImage = new JLabel(new ImageIcon("ponce.png"));
        playerImage.setPreferredSize(new Dimension(250, 300));
        playerImage.setHorizontalAlignment(SwingConstants.CENTER);
        playerInfoPanel.add(playerImage, BorderLayout.WEST);

        JPanel centerInfo = new JPanel();
        centerInfo.setOpaque(false);
        centerInfo.setLayout(new BoxLayout(centerInfo, BoxLayout.Y_AXIS));
        centerInfo.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 10));
        centerInfo.add(new JLabelWithStyle("코디폰세", 24, true));
        centerInfo.add(new JLabelWithStyle("승률: 52.12%", 16, false));
        centerInfo.add(Box.createVerticalStrut(10));
        centerInfo.add(new JLabelWithStyle("🔥파워피처🔥", 18, true));
        centerInfo.add(new JLabelWithStyle("신체 : 198cm, 116kg", 16, false));
        centerInfo.add(new JLabelWithStyle("출생 : 1994.04.25", 16, false));
        playerInfoPanel.add(centerInfo, BorderLayout.CENTER);

        JPanel rightInfo = new JPanel();
        rightInfo.setOpaque(false);
        rightInfo.setLayout(new BoxLayout(rightInfo, BoxLayout.Y_AXIS));
        rightInfo.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 30));
        rightInfo.add(new JLabelWithStyle("경력사항", 14, true));
        rightInfo.add(new JLabelWithStyle("2024.12 한화이글스", 14, false));
        rightInfo.add(new JLabelWithStyle("2019 WBSC국가대표", 14, false));
        rightInfo.add(Box.createVerticalStrut(10));
        rightInfo.add(new JLabelWithStyle("탈삼진 1위 🥇", 16, false));
        rightInfo.add(new JLabelWithStyle("평균자책 1위 🥇", 16, false));
        rightInfo.add(new JLabelWithStyle("다승 공동1위 🥇", 16, false));
        playerInfoPanel.add(rightInfo, BorderLayout.EAST);

        mainPanel.add(playerInfoPanel);

        // ───── 버튼 패널 ─────
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        buttonPanel.setPreferredSize(new Dimension(1200, 50));
        buttonPanel.setBackground(new Color(230, 230, 230));

        String[] btnTexts = {"정보창", "댓글", "랭킹갱신"};
        for (String text : btnTexts) {
            JButton btn = new JButton(text);
            btn.setPreferredSize(new Dimension(140, 40));
            btn.setFocusPainted(false);
            btn.setFont(loadCustomFont(18f, false));
            btn.addActionListener(e -> {
                switch (text) {
                    case "정보창" -> cardLayout.show(mainContentPanel, "info");
                    case "댓글" -> cardLayout.show(mainContentPanel, "comment");
                    case "랭킹갱신" -> JOptionPane.showMessageDialog(this, "랭킹을 갱신했습니다.");
                }
            });
            buttonPanel.add(btn);
        }
        mainPanel.add(buttonPanel);

        // ───── 카드 전환 패널 (표/댓글) ─────
        cardLayout = new CardLayout();
        mainContentPanel = new JPanel(cardLayout);
        mainContentPanel.setPreferredSize(new Dimension(1200, 300));

        // ▶ 정보창: 표
        String[] columns = {"평균자책", "승-패", "이닝", "삼진", "피안타", "피홈런", "볼넷", "WHIP"};
        Object[][] rowData = {{"1.48", "8-0", "67", "93", "39", "2", "18", "0.85"}};
        JTable table = new JTable(new DefaultTableModel(rowData, columns));
        table.setFont(loadCustomFont(14f, false));
        table.setRowHeight(28);
        JScrollPane tableScroll = new JScrollPane(table);
        mainContentPanel.add(tableScroll, "info");

        // ▶ 댓글창
        JPanel commentPanel = new JPanel(new BorderLayout());
        JTextArea commentArea = new JTextArea();
        commentArea.setFont(loadCustomFont(14f, false));
        commentArea.setEditable(false);
        JScrollPane commentScroll = new JScrollPane(commentArea);

        JPanel commentInputPanel = new JPanel(new BorderLayout());
        JTextField commentInput = new JTextField();
        JButton postBtn = new JButton("등록");
        postBtn.setFont(loadCustomFont(14f, false));
        commentInputPanel.add(commentInput, BorderLayout.CENTER);
        commentInputPanel.add(postBtn, BorderLayout.EAST);

        postBtn.addActionListener(e -> {
            String text = commentInput.getText().trim();
            if (!text.isEmpty()) {
                commentArea.append("- " + text + "\n");
                commentInput.setText("");
            }
        });

        commentPanel.add(commentScroll, BorderLayout.CENTER);
        commentPanel.add(commentInputPanel, BorderLayout.SOUTH);
        mainContentPanel.add(commentPanel, "comment");

        mainPanel.add(mainContentPanel);
        contentPane.add(mainPanel, BorderLayout.CENTER);
    }

    private static class JLabelWithStyle extends JLabel {
        public JLabelWithStyle(String text, int size, boolean bold) {
            super(text);
            setForeground(Color.WHITE);
            setFont(loadCustomFont((float) size, bold));
        }
    }

    private static Font loadCustomFont(float size, boolean bold) {
        try {
            File fontFile = new File(FONT_PATH);
            if (!fontFile.exists()) {
                System.err.println("❌ 폰트 파일 없음: " + FONT_PATH);
                return new Font("맑은 고딕", bold ? Font.BOLD : Font.PLAIN, (int) size);
            }
            Font font = Font.createFont(Font.TRUETYPE_FONT, fontFile);
            font = font.deriveFont(bold ? Font.BOLD : Font.PLAIN, size);
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
            return font;
        } catch (Exception e) {
            e.printStackTrace();
            return new Font("맑은 고딕", bold ? Font.BOLD : Font.PLAIN, (int) size);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PlayerStatsUI().setVisible(true));
    }
}
