package bumva.main.frames;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import bumva.db.BatterDAO;
import bumva.db.PitcherDAO;
import bumva.db.TeamDAO;
import com.formdev.flatlaf.FlatLightLaf;
import bumva.main.components.HeaderPanel;
import bumva.main.components.RoundedButton;
import bumva.main.components.RoundedTextField;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {
	public MainFrame() {
		try {
			UIManager.setLookAndFeel(new FlatLightLaf());
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		setTitle("메인 프레임");
		setSize(1100, 700);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		JPanel contentPane = new JPanel();
		setContentPane(contentPane);
		contentPane.setBackground(Color.WHITE);
		contentPane.setLayout(null);

		HeaderPanel headerPanel = new HeaderPanel(this);
		contentPane.add(headerPanel);

		JPanel searchPanel = new JPanel();
		searchPanel.setBounds(0, 108, 1200, 99);
		contentPane.add(searchPanel);
		searchPanel.setBackground(new Color(0, 32, 98));
		searchPanel.setLayout(null);

		RoundedTextField searchField = new RoundedTextField("선수 검색창", 40);
		searchField.setBounds(25, 26, 500, 40);
		searchPanel.add(searchField);

		RoundedButton searchBtn = new RoundedButton("검색");
		searchBtn.setBounds(550, 26, 100, 40);
		searchBtn.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		searchBtn.setBackground(Color.WHITE);
		searchBtn.setForeground(new Color(0, 32, 99));
		searchPanel.add(searchBtn);

		JPanel centerPanel = new JPanel();
		centerPanel.setBounds(23, 231, 1053, 435);
		centerPanel.setBackground(Color.WHITE);
		contentPane.add(centerPanel);
		centerPanel.setLayout(null);

		JTable table = new JTable();
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(383, 67, 664, 362);
		centerPanel.add(scrollPane);

		JButton btnPitcher = new JButton("피처 티어");
		btnPitcher.setBounds(383, 6, 157, 47);
		centerPanel.add(btnPitcher);
		btnPitcher.addActionListener(e -> table.setModel(PitcherDAO.getPitcherData()));

		JButton btnBatter = new JButton("타자 티어");
		btnBatter.setBounds(552, 6, 157, 47);
		centerPanel.add(btnBatter);
		btnBatter.addActionListener(e -> table.setModel(BatterDAO.getBatterData()));

		JButton btnTeam = new JButton("팀 티어");
		btnTeam.setBounds(721, 6, 157, 47);
		centerPanel.add(btnTeam);
		btnTeam.addActionListener(e -> table.setModel(TeamDAO.getTeamData()));

		JButton btnAll = new JButton("전체 선수");
		btnAll.setBounds(890, 6, 157, 47);
		centerPanel.add(btnAll);
		btnAll.addActionListener(e -> JOptionPane.showMessageDialog(this, "전체 선수 기능은 준비 중입니다."));
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
	}
}