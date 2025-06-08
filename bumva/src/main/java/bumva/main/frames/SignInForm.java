package bumva.main.frames;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;

import com.formdev.flatlaf.FlatLightLaf;

import bumva.db.UserDAO;
import bumva.main.components.HeaderPanel;
import bumva.main.components.RoundedPanel;

@SuppressWarnings("serial")
public class SignInForm extends JFrame {
    private JPanel contentPane;
    private SignUpForm signUpForm;
    private JTextField idField;
    private JTextField pwdField;
    private JButton btnLogin;

    // 현재 로그인된 userId를 저장할 static 필드
    private static String currentUserId;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                SignInForm frame = new SignInForm();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public SignInForm() {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("KBO.GG 로그인");
        setSize(1100, 700);
        setResizable(false);
        setLocationRelativeTo(null);

        contentPane = new JPanel();
        contentPane.setBackground(new Color(255, 255, 255));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(null);
        setContentPane(contentPane);

        // Header
        HeaderPanel headerPanel = new HeaderPanel(this);
        headerPanel.setBounds(0, 0, 1100, 109);
        contentPane.add(headerPanel);

        // Rounded login panel
        RoundedPanel panel = new RoundedPanel(40);
        panel.setBackground(new Color(247, 250, 255));
        panel.setBounds(371, 165, 341, 480);
        panel.setLayout(null);
        contentPane.add(panel);

        JLabel lblTitle = new JLabel("KBO.GG 로그인");
        lblTitle.setFont(new Font("Lucida Grande", Font.BOLD | Font.ITALIC, 30));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setBounds(6, 56, 329, 57);
        panel.add(lblTitle);

        // ID field
        idField = new JTextField("아이디");
        idField.setBounds(39, 177, 267, 51);
        idField.setForeground(Color.GRAY);
        idField.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                if (idField.getText().equals("아이디")) {
                    idField.setText("");
                    idField.setForeground(Color.BLACK);
                }
            }
            public void focusLost(FocusEvent e) {
                if (idField.getText().isEmpty()) {
                    idField.setText("아이디");
                    idField.setForeground(Color.GRAY);
                }
            }
        });
        panel.add(idField);

        // Password field
        pwdField = new JTextField("비밀번호");
        pwdField.setBounds(39, 263, 267, 51);
        pwdField.setForeground(Color.GRAY);
        pwdField.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                if (pwdField.getText().equals("비밀번호")) {
                    pwdField.setText("");
                    pwdField.setForeground(Color.BLACK);
                }
            }
            public void focusLost(FocusEvent e) {
                if (pwdField.getText().isEmpty()) {
                    pwdField.setText("비밀번호");
                    pwdField.setForeground(Color.GRAY);
                }
            }
        });
        panel.add(pwdField);

        // Login button
        btnLogin = new JButton("로그인");
        btnLogin.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
        btnLogin.setBounds(39, 344, 267, 51);
        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String userId = idField.getText().trim();
                String password = pwdField.getText().trim();
                if (UserDAO.login(userId, password)) {
                    currentUserId = userId;
                    new MainFrame().setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(SignInForm.this, "로그인 실패");
                }
            }
        });
        panel.add(btnLogin);

        // Sign-up link
        JButton btnSignUp = new JButton("회원가입");
        btnSignUp.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
        btnSignUp.setBounds(206, 416, 100, 29);
        btnSignUp.addActionListener(e -> {
            if (signUpForm == null) signUpForm = new SignUpForm();
            signUpForm.setLocation(getLocation());
            signUpForm.setVisible(true);
            dispose();
        });
        panel.add(btnSignUp);

        JLabel lblQuery = new JLabel("계정이 없으신가요?");
        lblQuery.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
        lblQuery.setHorizontalAlignment(SwingConstants.RIGHT);
        lblQuery.setBounds(57, 419, 124, 20);
        panel.add(lblQuery);
    }

    /** 현재 로그인된 userId 반환 */
    public static String getCurrentUserId() {
        return currentUserId;
    }
}