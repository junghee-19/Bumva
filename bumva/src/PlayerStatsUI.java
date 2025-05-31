import javax.swing.*;
import java.awt.*;

public class PlayerDetailPage extends JFrame {

    public PlayerDetailPage() {
        setTitle("선수 상세 정보 - 코디 폰세");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 상단 패널
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.DARK_GRAY);
        topPanel.setPreferredSize(new Dimension(1000, 300));
        add(topPanel, BorderLayout.NORTH);

        // 왼쪽 이미지
        JLabel imageLabel = new JLabel(new ImageIcon("ponce.png")); // ponce.png가 반드시 있어야 함
        imageLabel.setPreferredSize(new Dimension(250, 300));
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        topPanel.add(imageLabel, BorderLayout.WEST);

        // 중앙 정보
        JPanel centerInfo = new JPanel();
        centerInfo.setLayout(new BoxLayout(centerInfo, BoxLayout.Y_AXIS));
        centerInfo.setBackground(Color.DARK_GRAY);
        centerInfo.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // ❗ 각 라벨 변수화 없이 직접 add로 해결 (중복 방지 핵심)
        centerInfo.add(new JLabelStyled("코디폰세", 30));
        centerInfo.add(Box.createVerticalStrut(10));
        centerInfo.add(new JLabelStyled("승률 52.12%", 18));
        centerInfo.add(Box.createVerticalStrut(10));
        centerInfo.add(new JLabelStyled("🔥파워피처🔥", 18));
        centerInfo.add(Box.createVerticalStrut(10));
        centerInfo.add(new JLabelStyled("신체 : 198cm, 116kg", 16));
        centerInfo.add(new JLabelStyled("출생 : 1994.04.25", 16));
        centerInfo.add(Box.createVerticalStrut(10));
        centerInfo.add(new JLabelStyled("경력사항", 14));
        centerInfo.add(new JLabelStyled("2024.12 한화이글스", 14));
        centerInfo.add(new JLabelStyled("2019 WBSC국가대표", 14));
        topPanel.add(centerInfo, BorderLayout.CENTER);

        // 오른쪽 수상 정보
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(Color.DARK_GRAY);
        rightPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        rightPanel.add(new JLabelStyled("탈삼진 1위 🥇", 16));
        rightPanel.add(Box.createVerticalStrut(10));
        rightPanel.add(new JLabelStyled("평균자책 1위 🥇", 16));
        rightPanel.add(Box.createVerticalStrut(10));
        rightPanel.add(new JLabelStyled("다승 공동 1위 🥇", 16));
        topPanel.add(rightPanel, BorderLayout.EAST);

        // 하단 통계 박스
        JPanel statsPanel = new JPanel(new GridLayout(3, 3, 10, 10));
        statsPanel.setBackground(Color.BLACK);
        statsPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        statsPanel.add(createStatBox("평균자책", "1.48"));
        statsPanel.add(createStatBox("승-패", "8-0"));
        statsPanel.add(createStatBox("이닝", "67"));
        statsPanel.add(createStatBox("삼진", "93"));
        statsPanel.add(createStatBox("피안타", "39"));
        statsPanel.add(createStatBox("피홈런", "2"));
        statsPanel.add(createStatBox("볼넷", "18"));
        statsPanel.add(createStatBox("WHIP", "0.85"));
        statsPanel.add(new JLabel()); // 빈칸

        add(statsPanel, BorderLayout.CENTER);
    }

    // 🧩 스타일된 JLabel 생성 클래스 (중복 방지용)
    static class JLabelStyled extends JLabel {
        public JLabelStyled(String text, int size) {
            super(text);
            setFont(new Font("맑은 고딕", Font.PLAIN, size));
            setForeground(Color.WHITE);
        }
    }

    private JPanel createStatBox(String title, String value) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(50, 50, 50));
        panel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setForeground(Color.LIGHT_GRAY);
        titleLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 16));

        JLabel valueLabel = new JLabel(value, SwingConstants.CENTER);
        valueLabel.setForeground(Color.WHITE);
        valueLabel.setFont(new Font("맑은 고딕", Font.BOLD, 22));

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(valueLabel, BorderLayout.CENTER);
        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new PlayerDetailPage().setVisible(true);
        });
    }
}