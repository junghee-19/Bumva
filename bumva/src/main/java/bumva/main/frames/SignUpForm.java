package bumva.main.frames;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import bumva.main.components.HeaderPanel;
import bumva.main.components.RoundedPanel;
import bumva.db.UserDAO;
import com.formdev.flatlaf.FlatLightLaf;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class SignUpForm extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField tfID;
	private JTextField tfPS;
	private JTextField tfCheckPS;
	private JTextField tfNickname;
	protected SignInForm signInForm;
	protected String teamName;

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				SignUpForm frame = new SignUpForm();
				frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	public SignUpForm() {
		try {
			UIManager.setLookAndFeel(new FlatLightLaf());
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		setTitle("메인 프레임");
		setSize(1100, 700);
		setResizable(false);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 255, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		HeaderPanel headerPanel = new HeaderPanel(this);
		headerPanel.setLocation(-1, 0);
		contentPane.add(headerPanel);

		RoundedPanel panel = new RoundedPanel(40);
		panel.setBackground(new Color(247, 250, 255));
		panel.setBounds(379, 127, 341, 525);
		contentPane.add(panel);
		panel.setLayout(null);

		JButton btnSignUp = new JButton("회원가입");
		btnSignUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String userId = tfID.getText();
				String nickname = tfNickname.getText();
				String password = tfPS.getText();

				String confirmPassword = tfCheckPS.getText();

				if (userId.equals("아이디") || nickname.equals("닉네임") || password.equals("비밀번호")
						|| confirmPassword.equals("비밀번호 확인")) {
					JOptionPane.showMessageDialog(contentPane, "모든 필드를 올바르게 입력해주세요.", "오류", JOptionPane.ERROR_MESSAGE);
				} else if (!password.equals(confirmPassword)) {
					JOptionPane.showMessageDialog(contentPane, "비밀번호가 일치하지 않습니다.", "오류", JOptionPane.ERROR_MESSAGE);
				} else if (UserDAO.isUserIdTaken(userId)) {
					JOptionPane.showMessageDialog(contentPane, "이미 존재하는 아이디입니다.", "오류", JOptionPane.ERROR_MESSAGE);
				} else if (UserDAO.isNicknameTaken(nickname)) {
					JOptionPane.showMessageDialog(contentPane, "이미 존재하는 닉네임입니다.", "오류", JOptionPane.ERROR_MESSAGE);
				} else {
					boolean success = UserDAO.registerUser(userId, nickname, password, teamName);
					if (success) {
						JOptionPane.showMessageDialog(contentPane, "회원가입이 완료되었습니다!", "성공",
								JOptionPane.INFORMATION_MESSAGE);
						if (signInForm == null) {
							signInForm = new SignInForm();
						}
						signInForm.setLocation(getLocation());
						signInForm.setVisible(true);
						dispose();
					} else {
						JOptionPane.showMessageDialog(contentPane, "회원가입에 실패했습니다.", "오류", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		btnSignUp.setBounds(16, 457, 143, 47);
		panel.add(btnSignUp);
		btnSignUp.setFont(new Font("Lucida Grande", Font.PLAIN, 16));

		tfID = new JTextField("아이디");
		tfID.setBounds(37, 65, 267, 47);
		panel.add(tfID);
		tfID.setForeground(Color.GRAY);
		tfID.setColumns(10);

		tfNickname = new JTextField("닉네임");
		tfNickname.setBounds(37, 124, 267, 47);
		panel.add(tfNickname);
		tfNickname.setForeground(Color.GRAY);
		tfNickname.setColumns(10);

		tfPS = new JTextField("비밀번호");
		tfPS.setBounds(37, 183, 267, 47);
		panel.add(tfPS);
		tfPS.setForeground(Color.GRAY);
		tfPS.setColumns(10);

		tfCheckPS = new JTextField("비밀번호 확인");
		tfCheckPS.setBounds(37, 242, 267, 47);
		panel.add(tfCheckPS);
		tfCheckPS.setForeground(Color.GRAY);
		tfCheckPS.setColumns(10);

		JButton btnBack = new JButton("뒤로");
		btnBack.setBounds(171, 457, 143, 47);
		panel.add(btnBack);
		btnBack.addActionListener(e -> {
			if (signInForm == null) {
				signInForm = new SignInForm();
			}
			signInForm.setLocation(getLocation());
			signInForm.setVisible(true);
			dispose();
		});
		btnBack.setFont(new Font("Lucida Grande", Font.PLAIN, 16));

		JLabel singUpLabel = new JLabel("KBO.GG 회원가입");
		singUpLabel.setBounds(0, 6, 341, 53);
		panel.add(singUpLabel);
		singUpLabel.setHorizontalAlignment(SwingConstants.CENTER);
		singUpLabel.setFont(new Font("Lucida Grande", Font.BOLD | Font.ITALIC, 30));

		addFocusBehavior(tfID, "아이디");
		addFocusBehavior(tfNickname, "닉네임");
		addFocusBehavior(tfPS, "비밀번호");
		addFocusBehavior(tfCheckPS, "비밀번호 확인");

		JScrollPane teamScrollPane = new JScrollPane();
		teamScrollPane.setBounds(16, 311, 312, 124);
		panel.add(teamScrollPane);

		JPanel imgPanel = new JPanel();
		imgPanel.setBackground(new Color(255, 255, 255));
		imgPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10)); // Horizontal layout with spacing

		// Set the image panel as the viewport of the scroll pane
		teamScrollPane.setViewportView(imgPanel);
		teamScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		teamScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);

		String workingDir = System.getProperty("user.dir");
		System.out.println("Working Directory = " + workingDir);

		// 4. 이미지 폴더 경로 설정
		File playerImgDir = new File(workingDir + "/resource/imgs/teams");
		System.out.println("실제 이미지 디렉터리 → " + playerImgDir.getAbsolutePath());
		System.out.println("존재 여부 → " + playerImgDir.exists());

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

					Image scaledImg = img.getScaledInstance(100, 90, Image.SCALE_SMOOTH);

					// 2) JLabel 에 축소된 아이콘 세팅
					JLabel imgLabel = new JLabel(new ImageIcon(scaledImg));
					imgLabel.setOpaque(true); // Allow background color
					imgLabel.setBackground(Color.WHITE); // Default background
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

						public void mousePressed(java.awt.event.MouseEvent e) {
							if (e.getButton() == java.awt.event.MouseEvent.BUTTON1) {
								String fileName = imgFile.getName(); // ex: "Hanwha_Eagles.png"
								String baseName = fileName.substring(0, fileName.lastIndexOf('.')); // "Hanwha_Eagles"
								teamName = baseName.replace('_', ' '); // "Hanwha Eagles"

								javax.swing.JOptionPane.showMessageDialog(
										contentPane, "이미지를 왼쪽 버튼으로 눌렀습니다." + "\n이미지 경로: " + imgFile.getAbsolutePath()
												+ "\n선택된 팀: " + teamName,
										"이미지 클릭", javax.swing.JOptionPane.INFORMATION_MESSAGE);
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

	}

	private void addFocusBehavior(JTextField textField, String placeholder) {
		textField.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent e) {
				if (textField.getText().equals(placeholder)) {
					textField.setText("");
					textField.setForeground(Color.BLACK);
				}
			}

			public void focusLost(FocusEvent e) {
				if (textField.getText().isEmpty()) {
					textField.setText(placeholder);
					textField.setForeground(Color.GRAY);
				}
			}
		});
	}
}
