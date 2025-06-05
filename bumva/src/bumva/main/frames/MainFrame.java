package bumva.main.frames;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableModel;

import com.formdev.flatlaf.FlatLightLaf;

import bumva.main.compoments.HeaderPanel;
import bumva.main.compoments.RoundedButton;
import bumva.main.compoments.RoundedTextField;
import net.miginfocom.swing.MigLayout;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {
	private SignInForm signInForm;
	
	public MainFrame() {
		try {
			UIManager.setLookAndFeel(new FlatLightLaf());
		} catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		setTitle("메인 프레임");
		setSize(1100, 700);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		// 전체 레이아웃
		JPanel contentPane = new JPanel();
		setContentPane(contentPane);
		contentPane.setBackground(Color.WHITE);
		contentPane.setLayout(null);
		
		HeaderPanel headerPanel = new HeaderPanel(this);
		contentPane.add(headerPanel);

		// 검색 패널
		JPanel searchPanel = new JPanel();
		searchPanel.setBounds(0, 108, 1200, 99);
		contentPane.add(searchPanel);
		searchPanel.setBackground(new Color(0, 32, 98));
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
		searchBtn.setBackground(new Color(255, 255, 255));
		searchBtn.setForeground(new Color(0, 32, 99));
		searchBtn.setPreferredSize(new Dimension(100, 40));
		searchPanel.add(searchBtn, "aligny center");

		searchPanel.add(searchBtn, "aligny center");

		// 중앙 전체 패널
		JPanel centerPanel = new JPanel();
		centerPanel.setBounds(23, 231, 1053, 435);
		centerPanel.setBackground(new Color(255, 255, 255));
		contentPane.add(centerPanel);
		centerPanel.setLayout(null);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(6, 6, 349, 423);
		centerPanel.add(scrollPane_1);

		// 표 컬럼 및 데이터
		String[] columnNames = { "순위", "선수명", "팀명", "평균자책점", "경기", "승리", "패배", "팀순위" };
		Object[][] data = {
			{ "1", "류현진", "한화", "2.34", "25", "15", "3", "2" },
			{ "2", "김광현", "SSG", "2.67", "24", "14", "4", "3" }
		};

		DefaultTableModel model = new DefaultTableModel(data, columnNames);
		
		JButton btnNewButton = new JButton("피처 티어");
		btnNewButton.setBounds(383, 6, 157, 47);
		centerPanel.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("타자 티어");
		btnNewButton_1.setBounds(552, 6, 157, 47);
		centerPanel.add(btnNewButton_1);
		
		JButton btnNewButton_2 = new JButton("팀 티어");
		btnNewButton_2.setBounds(721, 6, 157, 47);
		centerPanel.add(btnNewButton_2);
		
		JButton btnNewButton_3 = new JButton("전체 선수");
		btnNewButton_3.setBounds(890, 6, 157, 47);
		centerPanel.add(btnNewButton_3);
		
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(383, 67, 664, 362);
		centerPanel.add(scrollPane_2);
		
		JTable table = new JTable(model);
		scrollPane_2.setViewportView(table);

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


	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			new MainFrame().setVisible(true); // Start your main application window
		});
	}
}
