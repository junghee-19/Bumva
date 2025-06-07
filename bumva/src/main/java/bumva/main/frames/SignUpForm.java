package bumva.main.frames;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
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

import com.formdev.flatlaf.FlatLightLaf;

import bumva.main.components.HeaderPanel;
import bumva.main.components.RoundedPanel;

import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.ImageIcon;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class SignUpForm extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textField;
	private JTextField tfID;
	private JTextField tfPS;
	private JTextField tfCheckPS;
	private JTextField tfNickname;
	protected SignInForm signInForm;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SignUpForm frame = new SignUpForm();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public SignUpForm() {

		try {
			UIManager.setLookAndFeel(new FlatLightLaf());
		} catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
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

		// Add 7 images to the panel
		for (int i = 1; i <= 7; i++) {
			JLabel imageLabel = new JLabel(new ImageIcon("image" + i + ".png")); // Replace with your image paths
			//imagePanel.add(imageLabel);
		}

		RoundedPanel panel = new RoundedPanel(40); // 20 is the corner radius
		panel.setBackground(new Color(247, 250, 255));
		panel.setBounds(379, 127, 341, 525);
		contentPane.add(panel);
		panel.setLayout(null);

		JButton btnSignUp = new JButton("회원가입");
		btnSignUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(tfCheckPS.getText().equals("비밀번호 확인") || tfPS.getText().equals("비밀번호") || 
						tfNickname.getText().equals("닉네임") || tfID.getText().equals("아이디")) {
					javax.swing.JOptionPane.showMessageDialog(contentPane, "모든 필드를 올바르게 입력해주세요.", "오류", javax.swing.JOptionPane.ERROR_MESSAGE);
				} else if (!tfPS.getText().equals(tfCheckPS.getText())) {
					javax.swing.JOptionPane.showMessageDialog(contentPane, "비밀번호가 일치하지 않습니다.", "오류", javax.swing.JOptionPane.ERROR_MESSAGE);
				} else {
					javax.swing.JOptionPane.showMessageDialog(contentPane, "회원가입이 완료되었습니다!", "성공", javax.swing.JOptionPane.INFORMATION_MESSAGE);
					if (signInForm == null) {
						signInForm = new SignInForm();
					}
					
					// DB연결시 여기다 로직 추가
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
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (signInForm == null) {
					signInForm = new SignInForm();
				}
				Point currentLocation = getLocation();
				signInForm.setLocation(currentLocation);
				signInForm.setVisible(true);
				dispose(); // 현재 창을 닫음
			}
		});
		btnBack.setFont(new Font("Lucida Grande", Font.PLAIN, 16));

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
            return lower.endsWith(".jpg") || lower.endsWith(".jpeg")
                || lower.endsWith(".png") || lower.endsWith(".gif") || lower.endsWith(".bmp");
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
						@Override
						public void mousePressed(java.awt.event.MouseEvent e) {
						    if (e.getButton() == java.awt.event.MouseEvent.BUTTON1) { // 왼쪽 버튼
						        javax.swing.JOptionPane.showMessageDialog(contentPane, "이미지를 왼쪽 버튼으로 눌렀습니다." + 
						            "\n이미지 경로: " + imgFile.getAbsolutePath(), "이미지 클릭", javax.swing.JOptionPane.INFORMATION_MESSAGE);
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

		JLabel singUpLabel = new JLabel("KBO.GG 회원가입");
		singUpLabel.setBounds(0, 6, 341, 53);
		panel.add(singUpLabel);
		singUpLabel.setHorizontalAlignment(SwingConstants.CENTER);
		singUpLabel.setFont(new Font("Lucida Grande", Font.BOLD | Font.ITALIC, 30));

		// Add FocusListener to textField_3
		tfCheckPS.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				if (tfCheckPS.getText().equals("비밀번호 확인")) {
					tfCheckPS.setText("");
					tfCheckPS.setForeground(Color.BLACK);
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (tfCheckPS.getText().isEmpty()) {
					tfCheckPS.setText("비밀번호 확인");
					tfCheckPS.setForeground(Color.GRAY);
				}
			}
		});

		// Add FocusListener to textField_1
		tfPS.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				if (tfPS.getText().equals("비밀번호")) {
					tfPS.setText("");
					tfPS.setForeground(Color.BLACK);
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (tfPS.getText().isEmpty()) {
					tfPS.setText("비밀번호");
					tfPS.setForeground(Color.GRAY);
				}
			}
		});

		// Add FocusListener to textField_4
		tfNickname.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				if (tfNickname.getText().equals("닉네임")) {
					tfNickname.setText("");
					tfNickname.setForeground(Color.BLACK);
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (tfNickname.getText().isEmpty()) {
					tfNickname.setText("닉네임");
					tfNickname.setForeground(Color.GRAY);
				}
			}
		});

		// Add FocusListener to textField_2
		tfID.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				if (tfID.getText().equals("아이디")) {
					tfID.setText("");
					tfID.setForeground(Color.BLACK);
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (tfID.getText().isEmpty()) {
					tfID.setText("아이디");
					tfID.setForeground(Color.GRAY);
				}
			}
		});
	}
}
