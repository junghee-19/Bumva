package bumva.main.frames;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import bumva.db.UserDAO;
import bumva.main.components.HeaderPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 선수 상세 프레임: 정보창, 댓글 기능(아이콘+닉네임), 차트
 */
@SuppressWarnings("serial")
public class PlayerStatsUI extends JFrame {
    private static final String FONT_PATH =
        "/Users/choejeonghui/Documents/GitHub/Bumva/bumva/resource/fonts/The Jamsil 5 Bold.ttf";
    private static final String TEAM_IMG_DIR =
        System.getProperty("user.dir") + "/resource/imgs/teams/";

    private CardLayout cardLayout;
    private JPanel mainContentPanel;
    private DefaultCategoryDataset dataset;
    private JFreeChart lineChart;
    private ChartPanel chartPanel;

    // 댓글 전용
    private JPanel commentDisplayPanel;
    private JTextField commentInput;

    public PlayerStatsUI(String playerName) {
        setTitle("선수 상세 프레임");
        setSize(1100, 700);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel contentPane = new JPanel(new BorderLayout());
        setContentPane(contentPane);

        // ─── Header
        HeaderPanel headerPanel = new HeaderPanel(this);
        contentPane.add(headerPanel, BorderLayout.NORTH);

        // ─── Main panel with Info & Comment tabs
        JPanel tabButtons = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        tabButtons.setBackground(new Color(230, 230, 230));
        String[] labels = { "정보창", "댓글", "랭킹갱신" };
        cardLayout = new CardLayout();
        mainContentPanel = new JPanel(cardLayout);
        mainContentPanel.setPreferredSize(new Dimension(1100, 590));

        for (String lbl : labels) {
            JButton b = new JButton(lbl);
            b.setFont(loadCustomFont(16f, false));
            b.addActionListener(e -> {
                switch (lbl) {
                    case "정보창"   -> cardLayout.show(mainContentPanel, "info");
                    case "댓글"     -> cardLayout.show(mainContentPanel, "comment");
                    case "랭킹갱신" -> JOptionPane.showMessageDialog(this, "랭킹을 갱신했습니다.");
                }
            });
            tabButtons.add(b);
        }
        contentPane.add(tabButtons, BorderLayout.WEST);

        // ─── Info 탭
        JPanel infoTab = new JPanel(new BorderLayout());
        infoTab.setBackground(Color.WHITE);

        // Player image and name
        JPanel topInfo = new JPanel(new BorderLayout());
        topInfo.setBackground(new Color(0, 32, 98));
        JLabel img = new JLabel(new ImageIcon("ponce.png"));
        img.setPreferredSize(new Dimension(250, 200));
        img.setHorizontalAlignment(SwingConstants.CENTER);
        topInfo.add(img, BorderLayout.WEST);

        JLabel nameLbl = new JLabel(playerName);
        nameLbl.setForeground(Color.WHITE);
        nameLbl.setFont(loadCustomFont(24f, true));
        nameLbl.setBorder(BorderFactory.createEmptyBorder(0,10,0,0));
        topInfo.add(nameLbl, BorderLayout.CENTER);

        infoTab.add(topInfo, BorderLayout.NORTH);

        // Stats table
        String[] cols = { "평균자책", "승-패", "이닝", "삼진", "피안타", "피홈런", "볼넷", "WHIP" };
        Object[][] data = {{ "1.48","8-0","67","93","39","2","18","0.85" }};
        JTable table = new JTable(new DefaultTableModel(data, cols));
        table.setFont(loadCustomFont(14f, false));
        table.setRowHeight(28);
        JScrollPane tblPane = new JScrollPane(table);
        tblPane.setPreferredSize(new Dimension(1100, 150));
        infoTab.add(tblPane, BorderLayout.CENTER);

        // Chart
        dataset = new DefaultCategoryDataset();
        updateDataset("평균자책");
        lineChart = ChartFactory.createLineChart(
            "경기별 평균자책", "날짜", "값", dataset,
            PlotOrientation.VERTICAL, false, false, false
        );
        CategoryPlot plot = lineChart.getCategoryPlot();
        LineAndShapeRenderer rnd = (LineAndShapeRenderer) plot.getRenderer();
        rnd.setSeriesStroke(0, new BasicStroke(3f));
        rnd.setSeriesShapesVisible(0, true);

        chartPanel = new ChartPanel(lineChart);
        chartPanel.setPreferredSize(new Dimension(1100, 250));
        infoTab.add(chartPanel, BorderLayout.SOUTH);

        // 테이블 헤더 클릭 시 차트 업데이트
        table.getTableHeader().addMouseListener(new MouseAdapter(){
            @Override public void mouseClicked(MouseEvent e) {
                int c = table.columnAtPoint(e.getPoint());
                String key = table.getColumnName(c);
                updateDataset(key);
                lineChart.setTitle("경기별 " + key);
            }
        });

        // ─── Comment 탭
        JPanel commentTab = new JPanel(new BorderLayout());
        commentDisplayPanel = new JPanel();
        commentDisplayPanel.setLayout(new BoxLayout(commentDisplayPanel, BoxLayout.Y_AXIS));
        JScrollPane cmtScroll = new JScrollPane(commentDisplayPanel);
        commentTab.add(cmtScroll, BorderLayout.CENTER);

        JPanel cInputPane = new JPanel(new BorderLayout(5,5));
        commentInput = new JTextField();
        commentInput.addActionListener(e -> postComment());
        JButton send = new JButton("등록");
        send.addActionListener(e -> postComment());
        cInputPane.add(commentInput, BorderLayout.CENTER);
        cInputPane.add(send, BorderLayout.EAST);
        commentTab.add(cInputPane, BorderLayout.SOUTH);

        // ─── Add tabs to card panel
        mainContentPanel.add(infoTab,    "info");
        mainContentPanel.add(commentTab, "comment");
        contentPane.add(mainContentPanel, BorderLayout.CENTER);

        // 기본으로 정보창 보여주기
        cardLayout.show(mainContentPanel, "info");
    }

    /** 댓글을 작성하여 패널에 추가 */
    private void postComment() {
        String msg = commentInput.getText().trim();
        if (msg.isEmpty()) return;

        // 닉네임, 팀 조회
        String uid = SignInForm.getCurrentUserId();
        String nick = UserDAO.getNickname(uid);
        String team = UserDAO.getTeam(uid);
        if (nick == null) nick = "익명";
        if (team == null) team = "default";

        // 팀 아이콘
        ImageIcon icon = loadTeamIcon(team);

        // 댓글 행
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.add(new JLabel(icon));
        JLabel text = new JLabel(nick + ": " + msg);
        text.setFont(loadCustomFont(14f, false));
        row.add(text);

        commentDisplayPanel.add(row);
        commentDisplayPanel.revalidate();

        commentInput.setText("");
        SwingUtilities.invokeLater(() -> {
            JScrollBar bar = ((JScrollPane)commentDisplayPanel.getParent().getParent())
                                .getVerticalScrollBar();
            bar.setValue(bar.getMaximum());
        });
    }

    /** 데이터셋 업데이트 */
    private void updateDataset(String metric) {
        dataset.clear();
        double[] vals;
        switch (metric) {
            case "승-패":  vals = new double[]{1,2,3,4,5}; break;
            case "이닝":  vals = new double[]{5,6,7,6,8}; break;
            case "삼진":  vals = new double[]{7,8,9,8,10}; break;
            case "피안타": vals = new double[]{4,5,3,6,7}; break;
            case "피홈런": vals = new double[]{1,1,0,2,1}; break;
            case "볼넷":  vals = new double[]{2,3,1,2,3}; break;
            case "WHIP":  vals = new double[]{1.1,1.0,0.9,1.2,1.1}; break;
            default:      vals = new double[]{3.2,4.1,2.5,3.6,4.8}; break;
        }
        String[] dates = {"05.04","05.10","05.17","05.22","05.28"};
        for (int i = 0; i < dates.length; i++) {
            dataset.addValue(vals[i], metric, dates[i]);
        }
    }

    /** 팀 아이콘 로드 */
    private ImageIcon loadTeamIcon(String team) {
        try {
            File f = new File(TEAM_IMG_DIR + team + ".png");
            BufferedImage img = javax.imageio.ImageIO.read(f);
            Image scaled = img.getScaledInstance(32,32,Image.SCALE_SMOOTH);
            return new ImageIcon(scaled);
        } catch (Exception e) {
            return new ImageIcon(
                new BufferedImage(32,32,BufferedImage.TYPE_INT_ARGB)
            );
        }
    }

    /** 사용자 정의 폰트 로드 */
    private static Font loadCustomFont(float size, boolean bold) {
        try {
            File f = new File(FONT_PATH);
            Font font = Font.createFont(Font.TRUETYPE_FONT, f)
                            .deriveFont(bold ? Font.BOLD:Font.PLAIN, size);
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
            return font;
        } catch (Exception e) {
            return new Font("맑은 고딕", bold?Font.BOLD:Font.PLAIN, (int)size);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
            new PlayerStatsUI("테스트선수").setVisible(true)
        );
    }
}