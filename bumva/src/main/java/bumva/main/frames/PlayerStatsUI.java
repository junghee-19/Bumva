package bumva.main.frames;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import bumva.main.components.HeaderPanel;


public class PlayerStatsUI extends JFrame {
    private static final String FONT_PATH = "/Users/choejeonghui/Documents/GitHub/Bumva/bumva/resource/fonts/The Jamsil 5 Bold.ttf";
    private CardLayout cardLayout;
    private JPanel mainContentPanel;
    private DefaultCategoryDataset dataset;
    private JFreeChart lineChart;
    private ChartPanel chartPanel;

    public PlayerStatsUI() {
        setTitle("선수 상세 프레임");
		setSize(1100, 700);
		setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        
        
        JPanel contentPane = new JPanel(new BorderLayout());
        setContentPane(contentPane);

        HeaderPanel headerPanel = new HeaderPanel(this);
        contentPane.add(headerPanel, BorderLayout.NORTH);
        headerPanel.setPreferredSize(new Dimension(1100, 109));
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        JPanel playerInfoPanel = new JPanel(new BorderLayout());
        playerInfoPanel.setPreferredSize(new Dimension(1200, 200));
        playerInfoPanel.setBackground(new Color(40, 40, 50));

        JLabel playerImage = new JLabel(new ImageIcon("ponce.png"));
        playerImage.setPreferredSize(new Dimension(250, 200));
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

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        buttonPanel.setPreferredSize(new Dimension(1200, 50));
        buttonPanel.setBackground(new Color(230, 230, 230));

        String[] btnTexts = {"정보창", "댓글", "랭킹갱신"};
        for (String text : btnTexts) {
            JButton btn = new JButton(text);
            btn.setPreferredSize(new Dimension(140, 40));
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

        cardLayout = new CardLayout();
        mainContentPanel = new JPanel(cardLayout);
        mainContentPanel.setPreferredSize(new Dimension(1200, 450));

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));

        String[] columns = {"평균자책", "승-패", "이닝", "삼진", "피안타", "피홈런", "볼넷", "WHIP"};
        Object[][] rowData = {{"1.48", "8-0", "67", "93", "39", "2", "18", "0.85"}};
        JTable table = new JTable(new DefaultTableModel(rowData, columns));
        table.setFont(loadCustomFont(14f, false));
        table.setRowHeight(28);
        JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.setPreferredSize(new Dimension(1200, 150));
        infoPanel.add(tableScroll);

        dataset = new DefaultCategoryDataset();
        updateDataset("평균자책");

        lineChart = ChartFactory.createLineChart(null, "날짜", "", dataset, PlotOrientation.VERTICAL, false, false, false);
        CategoryPlot plot = lineChart.getCategoryPlot();
        LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();
        renderer.setSeriesStroke(0, new BasicStroke(3f));
        renderer.setSeriesShapesVisible(0, true);
        chartPanel = new ChartPanel(lineChart);
        chartPanel.setPreferredSize(new Dimension(1200, 250));
        infoPanel.add(chartPanel);

        table.getTableHeader().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int col = table.columnAtPoint(e.getPoint());
                String columnName = table.getColumnName(col);
                updateDataset(columnName);
                lineChart.setTitle("경기별 " + columnName);
            }
        });

        mainContentPanel.add(infoPanel, "info");

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

    private void updateDataset(String metric) {
        dataset.clear();
        double[] values;
        switch (metric) {
            case "승-패" -> values = new double[]{1, 2, 3, 4, 5};
            case "이닝" -> values = new double[]{5, 6, 7, 6, 8};
            case "삼진" -> values = new double[]{7, 8, 9, 8, 10};
            case "피안타" -> values = new double[]{4, 5, 3, 6, 7};
            case "피홈런" -> values = new double[]{1, 1, 0, 2, 1};
            case "볼넷" -> values = new double[]{2, 3, 1, 2, 3};
            case "WHIP" -> values = new double[]{1.1, 1.0, 0.9, 1.2, 1.1};
            default -> values = new double[]{3.2, 4.1, 2.5, 3.6, 4.8};
        }
        String[] dates = {"05.04", "05.10", "05.17", "05.22", "05.28"};
        for (int i = 0; i < dates.length; i++) {
            dataset.addValue(values[i], metric, dates[i]);
        }
    }

    private static class JLabelWithStyle extends JLabel {
        public JLabelWithStyle(String text, int size, boolean bold) {
            super(text);
            setForeground(Color.WHITE);
            setFont(loadCustomFont(size, bold));
        }
    }

    private static Font loadCustomFont(float size, boolean bold) {
        try {
            File fontFile = new File(FONT_PATH);
            if (!fontFile.exists()) return new Font("맑은 고딕", bold ? Font.BOLD : Font.PLAIN, (int) size);
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