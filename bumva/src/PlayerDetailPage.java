import javax.swing.*;
import java.awt.*;

public class PlayerDetailPage extends JFrame {
    public PlayerDetailPage() {
        setTitle("선수 상세 정보 - 코디 폰세");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 🔷 상단 패널
        JPanel topPanel = new GradientPanel(new Color(30, 30, 60), new Color(49, 49, 60));
        topPanel.setLayout(new BorderLayout());
        topPanel.setPreferredSize(new Dimension(1000, 300));
        add(topPanel, BorderLayout.NORTH);

        // 🖼️ 왼쪽 이미지
        JLabel imageLabel = new JLabel(new ImageIcon("ponce.png"));
        imageLabel.setPreferredSize(new Dimension(250, 300));
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        topPanel.add(imageLabel, BorderLayout.WEST);

        // 🔤 중앙 정보
        JPanel centerInfo = new JPanel();
        centerInfo.setLayout(new BoxLayout(centerInfo, BoxLayout.Y_AXIS));
        centerInfo.setOpaque(false);
        centerInfo.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 20));
        centerInfo.add(createStyledLabel("코디폰세", 28, Font.BOLD));
        centerInfo.add(Box.createVerticalStrut(5));
        centerInfo.add(createStyledLabel("승률 52.12%", 16, Font.PLAIN));
        centerInfo.add(Box.createVerticalStrut(10));
        centerInfo.add(createStyledLabel("🔥파워피처🔥", 18, Font.BOLD));
        centerInfo.add(Box.createVerticalStrut(5));
        centerInfo.add(createStyledLabel("신체: 198cm, 116kg", 16, Font.PLAIN));
        centerInfo.add(createStyledLabel("출생: 1994.04.25", 16, Font.PLAIN));

        // 📚 경력사항
        JPanel rightInfo = new JPanel();
        rightInfo.setLayout(new BoxLayout(rightInfo, BoxLayout.Y_AXIS));
        rightInfo.setOpaque(false);
        rightInfo.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        rightInfo.add(createStyledLabel("경력사항", 14, Font.BOLD));
        rightInfo.add(Box.createVerticalStrut(5));
        rightInfo.add(createStyledLabel("2024.12 한화이글스", 14, Font.PLAIN));
        rightInfo.add(createStyledLabel("2019 WBSC국가대표", 14, Font.PLAIN));
        rightInfo.add(createStyledLabel("등등", 14, Font.PLAIN));

        // 🏆 수상 정보
        JPanel awardPanel = new JPanel();
        awardPanel.setLayout(new BoxLayout(awardPanel, BoxLayout.Y_AXIS));
        awardPanel.setOpaque(false);
        awardPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        awardPanel.add(createStyledLabel("탈삼진 1위 🥇", 16, Font.PLAIN));
        awardPanel.add(Box.createVerticalStrut(10));
        awardPanel.add(createStyledLabel("평균자책 1위 🥇", 16, Font.PLAIN));
        awardPanel.add(Box.createVerticalStrut(10));
        awardPanel.add(createStyledLabel("다승 공동1위 🥇", 16, Font.PLAIN));

        // 중앙 패널 묶기
        JPanel centerWrap = new JPanel();
        centerWrap.setLayout(new BoxLayout(centerWrap, BoxLayout.X_AXIS));
        centerWrap.setOpaque(false);
        centerWrap.add(centerInfo);
        centerWrap.add(Box.createHorizontalStrut(40));
        centerWrap.add(rightInfo);
        centerWrap.add(Box.createHorizontalStrut(40));
        centerWrap.add(awardPanel);
        topPanel.add(centerWrap, BorderLayout.CENTER);

        // 📊 하단 통계 패널
        JPanel statsPanel = new JPanel(new GridLayout(2, 4, 10, 10));
        statsPanel.setBackground(Color.decode("#1C1C1F"));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 30, 50));
        statsPanel.add(createStatBox("평균자책", "1.48"));
        statsPanel.add(createStatBox("승-패", "8-0"));
        statsPanel.add(createStatBox("이닝", "67"));
        statsPanel.add(createStatBox("삼진", "93"));
        statsPanel.add(createStatBox("피안타", "39"));
        statsPanel.add(createStatBox("피홈런", "2"));
        statsPanel.add(createStatBox("볼넷", "18"));
        statsPanel.add(createStatBox("WHIP", "0.85"));
        add(statsPanel, BorderLayout.CENTER);
    }

    private JLabel createStyledLabel(String text, int size, int style) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("맑은 고딕", style, size));
        lbl.setForeground(Color.WHITE);
        return lbl;
    }

    private JPanel createStatBox(String label, String value) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(50, 50, 50));
        panel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        JLabel title = createStyledLabel(label, 14, Font.PLAIN);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        JLabel val = createStyledLabel(value, 20, Font.BOLD);
        val.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(title, BorderLayout.NORTH);
        panel.add(val, BorderLayout.CENTER);
        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PlayerDetailPage ui = new PlayerDetailPage();
            ui.setVisible(true);
        });
    }
}

class GradientPanel extends JPanel {
    private final Color startColor;
    private final Color endColor;

    public GradientPanel(Color startColor, Color endColor) {
        this.startColor = startColor;
        this.endColor = endColor;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        int w = getWidth();
        int h = getHeight();
        GradientPaint gp = new GradientPaint(0, 0, startColor, 0, h, endColor);
        g2.setPaint(gp);
        g2.fillRect(0, 0, w, h);
        g2.dispose();
        super.paintComponent(g);
    }
}