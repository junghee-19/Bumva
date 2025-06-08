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
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;

import bumva.db.UserDAO;
import bumva.main.components.HeaderPanel;
import bumva.main.components.RoundedPanel;
import com.formdev.flatlaf.FlatLightLaf;

public class SignUpForm extends JFrame {
    private static final long serialVersionUID = 1L;

    private JPanel contentPane;
    private JTextField tfID;
    private JTextField tfPS;
    private JTextField tfCheckPS;
    private JTextField tfNickname;
    private SignInForm signInForm;
    // 클릭한 팀 이름을 저장합니다.
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
        // FlatLaf 테마 설정
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("KBO.GG 회원가입");
        setSize(1100, 700);
        setResizable(false);
        setLocationRelativeTo(null);

        contentPane = new JPanel();
        contentPane.setBackground(Color.WHITE);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(null);
        setContentPane(contentPane);

        // 헤더
        HeaderPanel headerPanel = new HeaderPanel(this);
        headerPanel.setBounds(0, 0, 1100, 109);
        contentPane.add(headerPanel);

        // 중앙 Rounded 패널
        RoundedPanel panel = new RoundedPanel(40);
        panel.setBackground(new Color(247, 250, 255));
        panel.setBounds(379, 127, 341, 525);
        panel.setLayout(null);
        contentPane.add(panel);

        // 회원가입 버튼
        JButton btnSignUp = new JButton("회원가입");
        btnSignUp.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
        btnSignUp.setBounds(16, 457, 143, 47);
        btnSignUp.addActionListener(e -> {
            String userId = tfID.getText().trim();
            String nickname = tfNickname.getText().trim();
            String password = tfPS.getText().trim();
            String confirm  = tfCheckPS.getText().trim();

            // 입력검증
            if (userId.equals("아이디") || nickname.equals("닉네임")
             || password.equals("비밀번호") || confirm.equals("비밀번호 확인")) {
                JOptionPane.showMessageDialog(panel,
                    "모든 필드를 올바르게 입력해주세요.",
                    "오류", JOptionPane.ERROR_MESSAGE);
            }
            else if (!password.equals(confirm)) {
                JOptionPane.showMessageDialog(panel,
                    "비밀번호가 일치하지 않습니다.",
                    "오류", JOptionPane.ERROR_MESSAGE);
            }
            else if (UserDAO.isUserIdTaken(userId)) {
                JOptionPane.showMessageDialog(panel,
                    "이미 존재하는 아이디입니다.",
                    "오류", JOptionPane.ERROR_MESSAGE);
            }
            else if (UserDAO.isNicknameTaken(nickname)) {
                JOptionPane.showMessageDialog(panel,
                    "이미 존재하는 닉네임입니다.",
                    "오류", JOptionPane.ERROR_MESSAGE);
            }
            else {
                // teamName 을 포함한 DAO 호출
                boolean success = UserDAO.registerUser(userId, nickname, password, teamName);
                if (success) {
                    JOptionPane.showMessageDialog(panel,
                        "회원가입이 완료되었습니다!",
                        "성공", JOptionPane.INFORMATION_MESSAGE);
                    if (signInForm == null) {
                        signInForm = new SignInForm();
                    }
                    signInForm.setLocation(getLocation());
                    signInForm.setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(panel,
                        "회원가입에 실패했습니다.",
                        "오류", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        panel.add(btnSignUp);

        // 뒤로 버튼
        JButton btnBack = new JButton("뒤로");
        btnBack.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
        btnBack.setBounds(171, 457, 143, 47);
        btnBack.addActionListener(e -> {
            if (signInForm == null) signInForm = new SignInForm();
            signInForm.setLocation(getLocation());
            signInForm.setVisible(true);
            dispose();
        });
        panel.add(btnBack);

        // 각 입력 필드
        tfID = new JTextField("아이디");
        tfID.setBounds(37, 65, 267, 47);
        tfID.setForeground(Color.GRAY);
        tfID.setColumns(10);
        addFocusBehavior(tfID, "아이디");
        panel.add(tfID);

        tfNickname = new JTextField("닉네임");
        tfNickname.setBounds(37, 124, 267, 47);
        tfNickname.setForeground(Color.GRAY);
        tfNickname.setColumns(10);
        addFocusBehavior(tfNickname, "닉네임");
        panel.add(tfNickname);

        tfPS = new JTextField("비밀번호");
        tfPS.setBounds(37, 183, 267, 47);
        tfPS.setForeground(Color.GRAY);
        tfPS.setColumns(10);
        addFocusBehavior(tfPS, "비밀번호");
        panel.add(tfPS);

        tfCheckPS = new JTextField("비밀번호 확인");
        tfCheckPS.setBounds(37, 242, 267, 47);
        tfCheckPS.setForeground(Color.GRAY);
        tfCheckPS.setColumns(10);
        addFocusBehavior(tfCheckPS, "비밀번호 확인");
        panel.add(tfCheckPS);

        // 제목 라벨
        JLabel lblTitle = new JLabel("KBO.GG 회원가입");
        lblTitle.setFont(new Font("Lucida Grande", Font.BOLD | Font.ITALIC, 30));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setBounds(0, 6, 341, 53);
        panel.add(lblTitle);

        // ─── 팀 선택용 스크롤 패널
        JScrollPane teamScrollPane = new JScrollPane();
        teamScrollPane.setBounds(16, 311, 312, 124);
        panel.add(teamScrollPane);

        JPanel imgPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        imgPanel.setBackground(Color.WHITE);
        teamScrollPane.setViewportView(imgPanel);
        teamScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        teamScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);

        // 실제 이미지 디렉터리
        String workingDir = System.getProperty("user.dir");
        File teamDir = new File(workingDir + "/resource/imgs/teams");
        FilenameFilter imgFilter = (dir, name) -> {
            String l = name.toLowerCase();
            return l.endsWith(".jpg") || l.endsWith(".jpeg")
                || l.endsWith(".png") || l.endsWith(".gif") || l.endsWith(".bmp");
        };

        File[] imgFiles = teamDir.listFiles(imgFilter);
        if (imgFiles != null) {
            for (File imgFile : imgFiles) {
                try {
                    BufferedImage buf = javax.imageio.ImageIO.read(imgFile);
                    Image scaled = buf.getScaledInstance(100, 90, Image.SCALE_SMOOTH);
                    JLabel imgLabel = new JLabel(new ImageIcon(scaled));
                    imgLabel.setOpaque(true);
                    imgLabel.setBackground(Color.WHITE);
                    imgLabel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));

                    imgLabel.addMouseListener(new java.awt.event.MouseAdapter() {
                        @Override
                        public void mouseEntered(java.awt.event.MouseEvent e) {
                            imgLabel.setBorder(BorderFactory.createLineBorder(new Color(230,230,255), 2));
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
                                // '_' → ' ' 로 변환
                                teamName = baseName.replace('_',' ');
                                JOptionPane.showMessageDialog(
                                    panel,
                                    "선택된 팀: " + teamName,
                                    "팀 선택",
                                    JOptionPane.INFORMATION_MESSAGE
                                );
                            }
                        }
                    });

                    imgPanel.add(imgLabel);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }

    }

    /** 플레이스홀더 포커스 처리 */
    private void addFocusBehavior(JTextField tf, String placeholder) {
        tf.addFocusListener(new FocusListener() {
            @Override public void focusGained(FocusEvent e) {
                if (tf.getText().equals(placeholder)) {
                    tf.setText("");
                    tf.setForeground(Color.BLACK);
                }
            }
            @Override public void focusLost(FocusEvent e) {
                if (tf.getText().isEmpty()) {
                    tf.setText(placeholder);
                    tf.setForeground(Color.GRAY);
                }
            }
        });
    }
}