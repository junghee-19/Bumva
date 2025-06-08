package bumva.main.frames;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import com.formdev.flatlaf.FlatLightLaf;

import bumva.db.BatterDAO;
import bumva.db.PitcherDAO;
import bumva.db.TeamDAO;
import bumva.main.components.HeaderPanel;
import bumva.main.components.RoundedButton;
import bumva.main.components.RoundedTextField;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {
	private SignInForm signInForm;
	private String workingDir;
	private File playerImgDir;
	private JPanel imgPanel;
	private DefaultTableModel model;
	protected PlayerStatsUI playerStatsUI;

	// 캐시된 썸네일 저장 (파일 경로 -> ImageIcon)
	private final Map<String, ImageIcon> thumbnailCache = new HashMap<>();

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
		searchField.setBackground(Color.WHITE);
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

		JScrollPane imgScrollPane = new JScrollPane();
		imgScrollPane.setBounds(6, 6, 349, 423);
		centerPanel.add(imgScrollPane);

		imgPanel = new JPanel();
		imgScrollPane.setViewportView(imgPanel);

		workingDir = System.getProperty("user.dir");
		playerImgDir = new File(workingDir + "/resource/imgs/players/pitchers");

		// 초기 로드
		loadImagesToPanel();

		JScrollPane tearScrollPane = new JScrollPane();
		tearScrollPane.setBounds(383, 67, 664, 362);
		centerPanel.add(tearScrollPane);

		JTable table = new JTable(model);
		tearScrollPane.setViewportView(table);

		// 초기 테이블 데이터
		model = PitcherDAO.getPitcherData();
		table.setModel(model);

		JButton btnPitcher = new JButton("피처 티어");
		btnPitcher.addActionListener(e -> {
			model = PitcherDAO.getPitcherData();
			table.setModel(model);
			playerImgDir = new File(workingDir + "/resource/imgs/players/pitchers");
			loadImagesToPanel(); // 캐시 활용해서 빠르게 갱신
		});
		btnPitcher.setBounds(383, 6, 210, 47);
		centerPanel.add(btnPitcher);

		JButton btnBatter = new JButton("타자 티어");
		btnBatter.addActionListener(e -> {
			model = BatterDAO.getBatterData();
			table.setModel(model);
			playerImgDir = new File(workingDir + "/resource/imgs/players/batters");
			loadImagesToPanel();
		});
		btnBatter.setBounds(610, 8, 210, 47);
		centerPanel.add(btnBatter);

		JButton btnTeam = new JButton("팀 티어");
		btnTeam.addActionListener(e -> {
			model = TeamDAO.getTeamData();
			table.setModel(model);
			// 팀 이미지는 따로 없으므로 패널 초기화
			imgPanel.removeAll();
			imgPanel.revalidate();
			imgPanel.repaint();
		});
		btnTeam.setBounds(837, 6, 210, 47);
		centerPanel.add(btnTeam);
	}

	/**
	 * 이미지를 디스크에서 읽어와 패널에 추가. 캐시에 없으면 읽어서 스케일 후 캐싱, 있으면 메모리에서 즉시 사용.
	 */
	private void loadImagesToPanel() {
		imgPanel.removeAll();
		imgPanel.setLayout(new GridLayout(0, 4, 10, 10));

		FilenameFilter imgFilter = (dir, name) -> {
			String lower = name.toLowerCase();
			return lower.endsWith(".jpg") || lower.endsWith(".jpeg") || lower.endsWith(".png") || lower.endsWith(".gif")
					|| lower.endsWith(".bmp");
		};
		File[] imgFiles = playerImgDir.listFiles(imgFilter);
		if (imgFiles != null && imgFiles.length > 0) {
			for (File imgFile : imgFiles) {
				try {
					String path = imgFile.getAbsolutePath();
					ImageIcon icon = thumbnailCache.get(path);
					if (icon == null) {
						BufferedImage img = ImageIO.read(imgFile);
						Image scaled = img.getScaledInstance(70, 85, Image.SCALE_SMOOTH);
						icon = new ImageIcon(scaled);
						thumbnailCache.put(path, icon);
					}

					JLabel imgLabel = new JLabel(icon);
					imgLabel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
					imgLabel.addMouseListener(new java.awt.event.MouseAdapter() {
						@Override
						public void mouseEntered(java.awt.event.MouseEvent e) {
							imgLabel.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 255), 2));
						}

						@Override
						public void mouseExited(java.awt.event.MouseEvent e) {
							imgLabel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
						}

						@Override
						public void mousePressed(java.awt.event.MouseEvent e) {
							if (e.getButton() == java.awt.event.MouseEvent.BUTTON1) {
								String fileName = imgFile.getName();
								String baseName = fileName.substring(0, fileName.lastIndexOf('.'));

								javax.swing.JOptionPane.showMessageDialog(
										imgLabel, "이미지를 왼쪽 버튼으로 눌렀습니다." + "\n이미지 경로: " + imgFile.getAbsolutePath()
												+ "\n선택된 팀: " + baseName,
										"이미지 클릭", javax.swing.JOptionPane.INFORMATION_MESSAGE);

								playerStatsUI = new PlayerStatsUI(baseName);
								playerStatsUI.setLocation(getLocation());
								playerStatsUI.setVisible(true);
								setVisible(false);
							}
						}
					});
					imgPanel.add(imgLabel);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else {
			JLabel noImgLabel = new JLabel("이미지가 없습니다.");
			noImgLabel.setHorizontalAlignment(SwingConstants.CENTER);
			imgPanel.add(noImgLabel);
		}

		imgPanel.revalidate();
		imgPanel.repaint();
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			HeaderPanel.mainFrame = new MainFrame();
			HeaderPanel.mainFrame.setVisible(true);
		}

		);
	}
}