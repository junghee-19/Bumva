import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import com.formdev.flatlaf.FlatLightLaf;

import net.miginfocom.swing.MigLayout;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class Project extends JFrame {
	public Project() {
		try {
			UIManager.setLookAndFeel(new FlatLightLaf());
		} catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		setTitle("메인 프레임");
		setSize(1200, 900);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		// 전체 레이아웃
		JPanel contentPane = new JPanel();
		setContentPane(contentPane);
		contentPane.setBackground(Color.WHITE);
		contentPane.setLayout(null);
		

		// 상단 제목
		JLabel titleLabel = new JLabel("KBO.GG", SwingConstants.LEFT);
		titleLabel.setBounds(0, -5, 1200, 120);
		titleLabel.setBackground(new Color(0, 32, 99));
		titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 40));
		titleLabel.setPreferredSize(new Dimension(0, 80)); // 세로 높이 80px
		titleLabel.setBorder(BorderFactory.createEmptyBorder(40, 20, 0, 0)); // 위, 좌, 아래, 우 여백

		titleLabel.setForeground(new Color(255, 255, 255)); // 텍스트는 흰색
		titleLabel.setOpaque(true); // 배경색 보이게 함

		contentPane.add(titleLabel);

		// 검색 패널
		JPanel searchPanel = new JPanel();
		searchPanel.setBounds(0, 108, 1200, 99);
		contentPane.add(searchPanel);
		searchPanel.setBackground(new Color(0, 27, 89));
		searchPanel.setMinimumSize(new Dimension(100, 120));
		searchPanel.setLayout(null);

		RoundedTextField searchField = new RoundedTextField("선수 검색창", 40);
		searchField.setBounds(25, 26, 500, 40);
		searchField.setPreferredSize(new Dimension(644, 40));
		searchField.setBackground(Color.WHITE); // 배경 필수 설정

		searchPanel.add(searchField);
		RoundedButton searchBtn = new RoundedButton("검색");
		searchBtn.setBounds(550, 26, 100, 40);
		searchBtn.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		searchBtn.setBackground(new Color(0x002063));
		searchBtn.setForeground(Color.WHITE);
		searchBtn.setPreferredSize(new Dimension(100, 40));
		searchPanel.add(searchBtn, "aligny center");

		searchPanel.add(searchBtn, "aligny center");

		// 중앙 전체 패널
		JPanel centerPanel = new JPanel();
		centerPanel.setBounds(99, 233, 1017, 639);
		centerPanel.setBackground(new Color(255, 255, 255));
		contentPane.add(centerPanel);
		centerPanel.setLayout(null);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(6, 6, 381, 627);
		centerPanel.add(scrollPane_1);

		// Image grid panel
		JPanel imagePanel = new JPanel();
		imagePanel.setLayout(new GridBagLayout()); // Use GridBagLayout for dynamic rows
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5, 5, 5, 5); // Add some spacing between images
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;

// Add the image panel to a scroll pane
		JScrollPane scrollPane = new JScrollPane(imagePanel);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

		// 표
		String[] columnNames = { "순위", "선수명", "팀명", "평균자책점", "경기", "승리", "패배", "팀순위" };
		Object[][] data = new Object[20][columnNames.length];
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			FontUtil.applyGlobalFont(); // Apply the custom font globally
			new Project().setVisible(true); // Start your main application window
		});
	}
}
