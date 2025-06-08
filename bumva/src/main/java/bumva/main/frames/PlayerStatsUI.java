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

import bumva.db.BatterDetailDAO;
import bumva.main.components.HeaderPanel;
import bumva.main.components.PlayerInfoBox;

public class PlayerStatsUI extends JFrame {
	private static final String FONT_PATH = "/Users/choejeonghui/Documents/GitHub/Bumva/bumva/resource/fonts/The Jamsil 5 Bold.ttf";
	private CardLayout cardLayout;
	private JPanel mainContentPanel;
	private DefaultCategoryDataset dataset;
	private JFreeChart lineChart;
	private ChartPanel chartPanel;
	private String playerName;
	private String[][] playerData;

	public PlayerStatsUI(String playerName) {
		this.playerName = playerName;
		this.playerData = BatterDetailDAO.getBatterData(playerName);
		
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

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
		buttonPanel.setPreferredSize(new Dimension(1200, 50));
		buttonPanel.setBackground(new Color(230, 230, 230));

		String[] btnTexts = { "정보창", "댓글", "랭킹갱신" };
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

		JPanel playerInfoPanel = new JPanel();
		playerInfoPanel.setPreferredSize(new Dimension(1200, 200));
		playerInfoPanel.setBackground(new Color(0, 32, 98));
		playerInfoPanel.setLayout(null);

		JLabel playerImage = new JLabel(new ImageIcon("ponce.png"));
		playerImage.setBounds(0, 0, 250, 156);
		playerImage.setPreferredSize(new Dimension(250, 200));
		playerImage.setHorizontalAlignment(SwingConstants.CENTER);
		playerInfoPanel.add(playerImage);
		mainPanel.add(playerInfoPanel);

		JLabel playerNameLabel = new JLabel("New label");
		playerNameLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 34));
		playerNameLabel.setForeground(new Color(255, 255, 255));
		playerNameLabel.setBounds(230, 17, 463, 41);
		playerNameLabel.setText(playerName);
		playerInfoPanel.add(playerNameLabel);
		mainPanel.add(buttonPanel);

		cardLayout = new CardLayout();
		mainContentPanel = new JPanel(cardLayout);
		mainContentPanel.setPreferredSize(new Dimension(1200, 450));

		JPanel infoPanel = new JPanel();


		dataset = new DefaultCategoryDataset();
		updateDataset("평균자책");

		lineChart = ChartFactory.createLineChart(null, "날짜", "", dataset, PlotOrientation.VERTICAL, false, false,
				false);
		CategoryPlot plot = lineChart.getCategoryPlot();
		LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();
		renderer.setSeriesStroke(0, new BasicStroke(3f));
		renderer.setSeriesShapesVisible(0, true);
		infoPanel.setLayout(null);
		chartPanel = new ChartPanel(lineChart);
		chartPanel.setBounds(0, 107, 1100, 256);
		chartPanel.setPreferredSize(new Dimension(1200, 250));
		infoPanel.add(chartPanel);

		mainContentPanel.add(infoPanel, "info");
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 0, 1100, 110);
		infoPanel.add(scrollPane);
		
		// 가로로 스크롤 패널 생성 
		JPanel panel = new JPanel();
		scrollPane.setViewportView(panel);
		panel.setLayout(null);
		
		//PlayerInfoBox를 사용해서 100 간격으로 playerData를 표시
		for (int i = 0; i < playerData[0].length; i++) {
			String title = playerData[0][i];
			String data = playerData[1][i];
			
			PlayerInfoBox infoBox = new PlayerInfoBox(title, data);
			infoBox.setBounds(6 + (i * 100), 6, 77, 76);
			panel.add(infoBox);
			
			// panel 가로 크기 조정해서 스크롤 되게 
			Dimension preferredSize = panel.getPreferredSize();
			preferredSize.width = (i + 1) * 100 + 12; // 100 간격으로 크기 조정
			panel.setPreferredSize(preferredSize);
				
			}
		
		

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
		case "승-패" -> values = new double[] { 1, 2, 3, 4, 5 };
		case "이닝" -> values = new double[] { 5, 6, 7, 6, 8 };
		case "삼진" -> values = new double[] { 7, 8, 9, 8, 10 };
		case "피안타" -> values = new double[] { 4, 5, 3, 6, 7 };
		case "피홈런" -> values = new double[] { 1, 1, 0, 2, 1 };
		case "볼넷" -> values = new double[] { 2, 3, 1, 2, 3 };
		case "WHIP" -> values = new double[] { 1.1, 1.0, 0.9, 1.2, 1.1 };
		default -> values = new double[] { 3.2, 4.1, 2.5, 3.6, 4.8 };
		}
		String[] dates = { "05.04", "05.10", "05.17", "05.22", "05.28" };
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
			if (!fontFile.exists())
				return new Font("맑은 고딕", bold ? Font.BOLD : Font.PLAIN, (int) size);
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
		SwingUtilities.invokeLater(() -> new PlayerStatsUI("Kim Seong-yoon").setVisible(true));
	}
}