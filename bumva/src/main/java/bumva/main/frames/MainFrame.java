package bumva.main.frames;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableModel;

import com.formdev.flatlaf.FlatLightLaf;

import bumva.db.BatterDAO;
import bumva.db.DatabaseManager;
import bumva.db.PitcherDAO;
import bumva.main.components.HeaderPanel;
import bumva.main.components.RoundedButton;
import bumva.main.components.RoundedTextField;
import net.miginfocom.swing.MigLayout;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {
	private SignInForm signInForm;
	private String workingDir;
	private File playerImgDir;
	private JPanel imgPanel;
	private DefaultTableModel model;

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
		
		JScrollPane imgScrollPane = new JScrollPane();
		imgScrollPane.setBounds(6, 6, 349, 423);
		centerPanel.add(imgScrollPane);
		
		imgPanel = new JPanel();
		imgScrollPane.setViewportView(imgPanel);

        workingDir = System.getProperty("user.dir");
        System.out.println("Working Directory = " + workingDir);

        // 4. 이미지 폴더 경로 설정
        playerImgDir = new File(workingDir + "/resource/imgs/players/pitchers");
        System.out.println("실제 이미지 디렉터리 → " + playerImgDir.getAbsolutePath());
        System.out.println("존재 여부 → " + playerImgDir.exists());

		loadImagesToPanel();
		JPanel imagePanel = new JPanel();
		imagePanel.setLayout(new GridBagLayout()); // Use GridBagLayout for dynamic rows
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5, 5, 5, 5); // Add some spacing between images
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		
// Add the image panel to a scroll pane
		JScrollPane tearScrollPane = new JScrollPane();
		tearScrollPane.setBounds(383, 67, 664, 362);
		centerPanel.add(tearScrollPane);
		
		JScrollPane scrollPane = new JScrollPane(imagePanel);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		JTable table = new JTable(model);
		tearScrollPane.setViewportView(table);
		
		DatabaseManager.getConnection(); // Ensure DB connection is established	

		model = PitcherDAO.getPitcherData();
		
		JButton btnPitcher = new JButton("피처 티어");
		btnPitcher.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// 테이블 모델 초기화
				model.setRowCount(0); // Clear existing rows
				model = PitcherDAO.getPitcherData(); // Reload data from the database
				table.setModel(model);
				
				JDialog loadingDialog = createLoadingDialog();
				SwingWorker<Void, Void> worker = new SwingWorker<>() {
					@Override
					protected Void doInBackground() {
						playerImgDir = new File(workingDir + "/resource/imgs/players/pitchers");
						loadImagesToPanel();
						return null;
					}
					@Override
					protected void done() {
						loadingDialog.dispose();
					}
				};
				worker.execute();
				loadingDialog.setVisible(true);
			}
		});
		btnPitcher.setBounds(383, 6, 157, 47);
		centerPanel.add(btnPitcher);
		
		JButton btnBatter = new JButton("타자 티어");
		btnBatter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// 테이블 모델 초기화
				model.setRowCount(0); // Clear existing rows
				model = BatterDAO.getBatterData(); // Reload data from the database
				table.setModel(model);
				
				JDialog loadingDialog = createLoadingDialog();
				SwingWorker<Void, Void> worker = new SwingWorker<>() {
					@Override
					protected Void doInBackground() {
						playerImgDir = new File(workingDir + "/resource/imgs/players/batters");
						loadImagesToPanel();
						return null;
					}
					@Override
					protected void done() {
						loadingDialog.dispose();
					}
				};
				worker.execute();
				loadingDialog.setVisible(true);
			}
		});
		btnBatter.setBounds(552, 6, 157, 47);
		centerPanel.add(btnBatter);
		
		JButton btnteam = new JButton("팀 티어");
		btnteam.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model = PitcherDAO.getPitcherData(); // Reload data from the databasv
				table.setModel(model);
			}
		});
		btnteam.setBounds(721, 6, 157, 47);
		centerPanel.add(btnteam);
		
		JButton btnAll = new JButton("전체 선수");
		btnAll.setBounds(890, 6, 157, 47);
		centerPanel.add(btnAll);
		
		

		// Image grid panel
	}

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
					BufferedImage img = javax.imageio.ImageIO.read(imgFile);
					Image scaledImg = img.getScaledInstance(70, 85, Image.SCALE_SMOOTH);
					JLabel imgLabel = new JLabel(new ImageIcon(scaledImg));
					imgLabel.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255), 2));
					imgLabel.addMouseListener(new java.awt.event.MouseAdapter() {
						@Override
						public void mouseEntered(java.awt.event.MouseEvent e) {
							imgLabel.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 255), 2));
						}

						@Override
						public void mouseExited(java.awt.event.MouseEvent e) {
							imgLabel.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255), 2));
						}

						@Override
						public void mousePressed(java.awt.event.MouseEvent e) {
							if (e.getButton() == java.awt.event.MouseEvent.BUTTON1) {
								javax.swing.JOptionPane.showMessageDialog(imgPanel,
										"이미지를 왼쪽 버튼으로 눌렀습니다.\n이미지 경로: " + imgFile.getAbsolutePath(), "이미지 클릭",
										javax.swing.JOptionPane.INFORMATION_MESSAGE);
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

	private JDialog createLoadingDialog() {
		JDialog dialog = new JDialog(this, "로딩중", true);
		JLabel label = new JLabel("이미지를 불러오는 중입니다...", SwingConstants.CENTER);
		dialog.getContentPane().add(label);
		dialog.setSize(250, 100);
		dialog.setLocationRelativeTo(this);
		dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		dialog.setResizable(false);
		return dialog;
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			new MainFrame().setVisible(true); // Start your main application window
		});
	}
}
