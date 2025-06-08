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
    private final String position;
    private final String[][] playerData;

    private CardLayout cardLayout;
    private JPanel mainContentPanel;
    private DefaultCategoryDataset dataset;
    private ChartPanel chartPanel;

    // 댓글 관련
    private JPanel commentDisplayPanel;
    private JTextField commentInput;

    public PlayerStatsUI(String playerName,String position) {
        this.playerName = playerName;
        this.position = position;
        
        this.playerData = BatterDetailDAO.getBatterData(playerName, position);

        
        
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
        mainPanel.setBounds(0, 108, 1100, 564);
        mainPanel.setLayout(null);

        // 선수 정보 영역
        JPanel playerInfo = new JPanel(null);
        playerInfo.setBounds(0, 0, 1100, 200);
        playerInfo.setPreferredSize(new Dimension(1100, 200));
        playerInfo.setBackground(new Color(0, 32, 98));
        ImageIcon icon = new ImageIcon(System.getProperty("user.dir") + "/resource/imgs/players/" + position + "/" + playerName + ".jpeg");
        Image imgRaw = icon.getImage().getScaledInstance(160, 160, Image.SCALE_SMOOTH);
        JLabel img = new JLabel(new ImageIcon(imgRaw));
        img.setBounds(20, 20, 160, 160);
        playerInfo.add(img);
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
        tabButtons.setBounds(0, 200, 1100, 51);
        tabButtons.setBackground(new Color(255, 255, 255));
        String[] tabs = {"정보창", "댓글", "랭킹갱신"};
        cardLayout = new CardLayout();
        mainContentPanel = new JPanel(cardLayout);
        mainContentPanel.setBounds(0, 251, 1100, 313);
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
        boxContainer.setBackground(new Color(255, 255, 255));
        JScrollPane boxScroll = new JScrollPane(boxContainer,
                JScrollPane.VERTICAL_SCROLLBAR_NEVER,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        boxScroll.setBounds(0, 0, 1100, 114);
        boxScroll.setPreferredSize(new Dimension(1100, 100));
        for (int i = 0; i < playerData[0].length && playerData[0][i] != null; i++) {
            PlayerInfoBox pib = new PlayerInfoBox(playerData[0][i], playerData[1][i]);
            pib.setBounds(10 + i * 120, 10, 100, 80);
            boxContainer.add(pib);
        }
        infoTab.setLayout(null);
        boxContainer.setPreferredSize(new Dimension(playerData[0].length * 120 + 20, 100));
        infoTab.add(boxScroll);

       

        // 차트
     // 기존 dataset 초기화 아래에 삽입
        dataset = new DefaultCategoryDataset();
        // 초기 차트에 승률, 승, 패, 무 데이터 추가
        String[] initialKeys = {"승률", "승", "패", "무"};
        for (String key : initialKeys) {
            for (int i = 0; i < playerData[0].length; i++) {
                if (playerData[0][i] != null && playerData[0][i].equals(key)) {
                    try {
                        double v = Double.parseDouble(playerData[1][i]);
                        dataset.addValue(v, key, key);
                    } catch (NumberFormatException ignored) {}
                    break;
                }
            }
        }
        // 기존 라인 차트 생성부
        dataset = new DefaultCategoryDataset();
        JFreeChart chart = ChartFactory.createLineChart(
                "선수 주요 지표",      // 제목
                "Metric",            // X축 레이블 (지표명)
                "Value",             // Y축 레이블 (값)
                dataset,             
                PlotOrientation.VERTICAL,
                true,   // 범례 표시 (여러 series 시)
                false,
                false
        );
        CategoryPlot cp = chart.getCategoryPlot();
        LineAndShapeRenderer lr = (LineAndShapeRenderer)cp.getRenderer();
        lr.setSeriesStroke(0, new BasicStroke(3f));
        lr.setSeriesShapesVisible(0, true);

        chartPanel = new ChartPanel(chart);
        chartPanel.setBounds(0, 113, 1100, 200);
        infoTab.add(chartPanel);

        // **한 번에 모든 숫자형 컬럼을 로드**
        populateAllMetrics();
        

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

    private void populateAllMetrics() {
        dataset.clear();
        for (int i = 0; i < playerData[0].length; i++) {
            String metric = playerData[0][i];
            String valStr = playerData[1][i];
            if (metric == null || valStr == null) continue;
            try {
                double v = Double.parseDouble(valStr);
                dataset.addValue(v, "Stats", metric);
            } catch (NumberFormatException ex) {
                // 숫자가 아닌 컬럼은 건너뜀
            }
        }
    }

    /**
     * 단일 컬럼 클릭 시 해당 지표만 다시 그리는 메서드
     */
    private void updateDataset(String metric) {
        dataset.clear();
        for (int i = 0; i < playerData[0].length; i++) {
            if (metric.equals(playerData[0][i])) {
                String valStr = playerData[1][i];
                if (valStr != null) {
                    try {
                        double v = Double.parseDouble(valStr);
                        dataset.addValue(v, "Stats", metric);
                    } catch (NumberFormatException ex) {
                        // 무시
                    }
                }
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
        SwingUtilities.invokeLater(() -> new PlayerStatsUI("Oh Ji-hwan","batters").setVisible(true));
    }
}