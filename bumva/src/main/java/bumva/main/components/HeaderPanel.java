package bumva.main.components;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import bumva.main.frames.MainFrame;
import bumva.main.frames.PlayerStatsUI;
import bumva.main.frames.SignInForm;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@SuppressWarnings("serial")
public class HeaderPanel extends JPanel {
    private SignInForm signInForm;
    private JFrame parentFrame;
    private MainFrame mainFrame;
    protected PlayerStatsUI playerStatsUI;

    public HeaderPanel(JFrame parentFrame) {
        this.parentFrame = parentFrame;

        setLayout(null);
        setBackground(new Color(0, 32, 98));
        setBounds(0, 0, 1100, 109);

        JLabel lblNewLabel = new JLabel("KBO.GG");
        lblNewLabel.setBounds(59, 48, 179, 55);
        lblNewLabel.setForeground(Color.WHITE);
        lblNewLabel.setFont(new Font("Lucida Grande", Font.BOLD | Font.ITALIC, 46));
        lblNewLabel.setHorizontalAlignment(SwingConstants.LEFT);
        lblNewLabel.setVerticalAlignment(SwingConstants.BOTTOM);
        add(lblNewLabel);

        JPanel topMenuPanel = new JPanel();
        topMenuPanel.setBackground(new Color(47, 63, 98));
        topMenuPanel.setBounds(0, 0, 1100, 35);
        topMenuPanel.setLayout(null);
        add(topMenuPanel);

        JButton btnLogin = new JButton("로그인");
        btnLogin.setBounds(983, 0, 117, 35);
        btnLogin.setForeground(new Color(255, 254, 255));
        btnLogin.setBackground(new Color(47, 62, 98));
        btnLogin.setBorder(new MatteBorder(0, 1, 0, 0, Color.WHITE));
        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (signInForm == null) {
                    signInForm = new SignInForm();
                }
                Point currentLocation = parentFrame.getLocation();
                signInForm.setLocation(currentLocation);
                signInForm.setVisible(true);
                parentFrame.setVisible(false);
            }
        });
        topMenuPanel.add(btnLogin);

        JButton btnMenu1 = new JButton("야구 분석");
        btnMenu1.setBounds(57, 0, 117, 35);
        btnMenu1.setForeground(new Color(255, 254, 255));
        btnMenu1.setBackground(new Color(47, 62, 98));
        btnMenu1.setBorder(new MatteBorder(0, 1, 0, 0, Color.WHITE));
        btnMenu1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (mainFrame == null) {
                    mainFrame = new MainFrame();
                }
                Point currentLocation = parentFrame.getLocation();
                mainFrame.setLocation(currentLocation);
                mainFrame.setVisible(true);
                parentFrame.setVisible(false);
            }
        });
        topMenuPanel.add(btnMenu1);

        JButton btnMenu2 = new JButton("야구 동영상");
        btnMenu2.setBounds(175, 0, 117, 35);
        btnMenu2.setForeground(new Color(255, 254, 255));
        btnMenu2.setBackground(new Color(47, 62, 98));
        btnMenu2.setBorder(new MatteBorder(0, 1, 0, 0, Color.WHITE));
        btnMenu2.addActionListener(e -> {
            try {
                // 이제 "-all.jar" 만 실행하면 됩니다
            	ProcessBuilder pb = new ProcessBuilder(
            		    "java",
            		    "--module-path", "/Users/choejeonghui/Downloads/javafx-sdk-21.0.2-aarch64/lib",
            		    "--add-modules", "javafx.controls,javafx.fxml,javafx.web",
            		    "-cp", "target/bumva-1.0.0-all.jar",
            		    "resource.bumva.ChatWithVideoApp"
            		);
            		pb.inheritIO();
            		pb.start();
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(parentFrame, "ChatWithVideoApp 실행 중 오류 발생");
            }
        });
        topMenuPanel.add(btnMenu2);

        JButton btnMenu3 = new JButton("추가 예정?");
        btnMenu3.setBounds(293, 0, 117, 35);
        btnMenu3.setForeground(new Color(255, 254, 255));
        btnMenu3.setBackground(new Color(47, 62, 98));
        btnMenu3.setBorder(new MatteBorder(0, 1, 0, 1, Color.WHITE));
        btnMenu3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (playerStatsUI == null) {
                    playerStatsUI = new PlayerStatsUI("선수 이름"); // 선수 이름은 실제로는 동적으로 받아와야 합니다.
                }
                Point currentLocation = parentFrame.getLocation();
                playerStatsUI.setLocation(currentLocation);
                playerStatsUI.setVisible(true);
                parentFrame.setVisible(false);
            }
        });
        topMenuPanel.add(btnMenu3);
    }
}