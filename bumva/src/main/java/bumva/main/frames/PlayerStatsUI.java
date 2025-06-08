package bumva.main.frames;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import bumva.db.UserDAO;
import bumva.db.BatterDetailDAO;
import bumva.main.components.HeaderPanel;
import bumva.main.components.PlayerInfoBox;

@SuppressWarnings("serial")
public class PlayerStatsUI extends JFrame {
    private static final String FONT_PATH = "/Users/choejeonghui/Documents/GitHub/Bumva/bumva/resource/fonts/The Jamsil 5 Bold.ttf";
    private static final String TEAM_IMG_DIR = System.getProperty("user.dir") + "/resource/imgs/teams/";

    private final String playerName;
    private final String[][] playerData;

    private CardLayout cardLayout;
    private JPanel mainContentPanel;
    private DefaultCategoryDataset dataset;
    private ChartPanel chartPanel;

    // 댓글 관련
    private JPanel commentDisplayPanel;
    private JTextField commentInput;

    public PlayerStatsUI(String playerName) {
        this.playerName = playerName;
        this.playerData = BatterDetailDAO.getBatterData(playerName);

        
        
        setTitle("선수 상세 프레임 - " + playerName);
        setSize(1100, 700);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel contentPane = new JPanel();
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // 헤더
        HeaderPanel header = new HeaderPanel(this);
        contentPane.add(header);

        // 메인 수직 레이아웃
        JPanel mainPanel = new JPanel();
        mainPanel.setBounds(0, 1, 1100, 671);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // 선수 정보 영역
        JPanel playerInfo = new JPanel(null);
        playerInfo.setPreferredSize(new Dimension(1100, 200));
        playerInfo.setBackground(new Color(0, 32, 98));
        JLabel img = new JLabel(new ImageIcon(TEAM_IMG_DIR + playerName + ".png"));
        img.setBounds(20, 20, 160, 160);
        playerInfo.add(img);
        JLabel name = new JLabel(playerName);
        name.setForeground(Color.WHITE);
        name.setFont(loadCustomFont(28f, true));
        name.setBounds(200, 60, 400, 40);
        playerInfo.add(name);
        mainPanel.add(playerInfo);

        // 탭 버튼 패널
        JPanel tabButtons = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        tabButtons.setBackground(new Color(230, 230, 230));
        String[] tabs = {"정보창", "댓글", "랭킹갱신"};
        cardLayout = new CardLayout();
        mainContentPanel = new JPanel(cardLayout);
        for (String t : tabs) {
            JButton b = new JButton(t);
            b.setFont(loadCustomFont(16f, false));
            b.setPreferredSize(new Dimension(120, 40));
            b.addActionListener(e -> {
                if (t.equals("랭킹갱신")) {
                    JOptionPane.showMessageDialog(this, "랭킹이 갱신되었습니다.");
                } else {
                    cardLayout.show(mainContentPanel, t);
                }
            });
            tabButtons.add(b);
        }
        mainPanel.add(tabButtons);

        // 정보창 탭
        JPanel infoTab = new JPanel();
        infoTab.setPreferredSize(new Dimension(1100, 450));

        // PlayerInfoBox 스크롤 영역
        JPanel boxContainer = new JPanel(null);
        JScrollPane boxScroll = new JScrollPane(boxContainer,
                JScrollPane.VERTICAL_SCROLLBAR_NEVER,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        boxScroll.setBounds(0, 116, 1100, 135);
        boxScroll.setPreferredSize(new Dimension(1100, 100));
        for (int i = 0; i < playerData[0].length && playerData[0][i] != null; i++) {
            PlayerInfoBox pib = new PlayerInfoBox(playerData[0][i], playerData[1][i]);
            pib.setBounds(10 + i * 120, 10, 100, 80);
            boxContainer.add(pib);
        }
        infoTab.setLayout(null);
        boxContainer.setPreferredSize(new Dimension(playerData[0].length * 120 + 20, 100));
        infoTab.add(boxScroll);

        // 통계 테이블
        String[] cols = playerData[0];
        Object[][] rows = {playerData[1]};

        // 차트
        dataset = new DefaultCategoryDataset();
        JFreeChart chart = ChartFactory.createLineChart("", "항목", "값", dataset,
                PlotOrientation.VERTICAL, false, false, false);
        CategoryPlot cp = chart.getCategoryPlot();
        LineAndShapeRenderer lr = (LineAndShapeRenderer)cp.getRenderer();
        lr.setSeriesStroke(0, new BasicStroke(3f));
        lr.setSeriesShapesVisible(0, true);
        chartPanel = new ChartPanel(chart);
        chartPanel.setBounds(0, 250, 1100, 200);
        chartPanel.setPreferredSize(new Dimension(1100, 200));
        infoTab.add(chartPanel);

        mainContentPanel.add(infoTab, "정보창");

        // 댓글 탭
        JPanel commentTab = new JPanel(new BorderLayout());
        commentDisplayPanel = new JPanel();
        commentDisplayPanel.setLayout(new BoxLayout(commentDisplayPanel, BoxLayout.Y_AXIS));
        JScrollPane commentScroll = new JScrollPane(commentDisplayPanel);
        commentTab.add(commentScroll, BorderLayout.CENTER);
        JPanel inputPanel = new JPanel(new BorderLayout(5,5));
        commentInput = new JTextField();
        commentInput.addActionListener(e -> postComment());
        JButton post = new JButton("등록");
        post.addActionListener(e -> postComment());
        inputPanel.add(commentInput, BorderLayout.CENTER);
        inputPanel.add(post, BorderLayout.EAST);
        inputPanel.setBorder(BorderFactory.createEmptyBorder(5,10,5,10));
        commentTab.add(inputPanel, BorderLayout.SOUTH);
        mainContentPanel.add(commentTab, "댓글");

        mainPanel.add(mainContentPanel);
        contentPane.add(mainPanel);
        cardLayout.show(mainContentPanel, "정보창");
    }

    private void postComment() {
        String msg = commentInput.getText().trim();
        if (msg.isEmpty()) return;
        String uid = SignInForm.getCurrentUserId();
        String nick = UserDAO.getNickname(uid);
        String team = UserDAO.getTeam(uid);
        if (nick == null) nick = "익명";
        if (team == null) team = "default";
        ImageIcon icon = loadTeamIcon(team);
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT,10,5));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.add(new JLabel(icon));
        JLabel lbl = new JLabel(nick + ": " + msg);
        lbl.setFont(loadCustomFont(14f,false));
        row.add(lbl);
        commentDisplayPanel.add(row);
        commentDisplayPanel.revalidate();
        commentInput.setText("");
        SwingUtilities.invokeLater(() -> {
            JScrollBar bar = ((JScrollPane)commentDisplayPanel.getParent().getParent()).getVerticalScrollBar();
            bar.setValue(bar.getMaximum());
        });
    }

    private void updateDataset(String metric) {
        dataset.clear();
        for (int i = 0; i < playerData[0].length; i++) {
            String col = playerData[0][i];
            if (col != null && col.equals(metric)) {
                try {
                    double v = Double.parseDouble(playerData[1][i]);
                    dataset.addValue(v, metric, metric);
                } catch (NumberFormatException ex) {}
                break;
            }
        }
    }

    private ImageIcon loadTeamIcon(String team) {
        try {
            BufferedImage img = javax.imageio.ImageIO.read(new File(TEAM_IMG_DIR + team + ".png"));
            Image sc = img.getScaledInstance(32,32,Image.SCALE_SMOOTH);
            return new ImageIcon(sc);
        } catch (Exception e) {
            return new ImageIcon(new BufferedImage(32,32,BufferedImage.TYPE_INT_ARGB));
        }
    }

    private static Font loadCustomFont(float size, boolean bold) {
        try {
            File f = new File(FONT_PATH);
            if (!f.exists()) throw new Exception();
            Font font = Font.createFont(Font.TRUETYPE_FONT, f);
            font = font.deriveFont(bold?Font.BOLD:Font.PLAIN, size);
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
            return font;
        } catch (Exception e) {
            return new Font("맑은 고딕", bold?Font.BOLD:Font.PLAIN, (int)size);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PlayerStatsUI("Oh Ji-hwan").setVisible(true));
    }
}