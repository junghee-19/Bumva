import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class LoginForm extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginForm frame = new LoginForm();
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
	public LoginForm() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		setTitle("메인 프레임");
		setSize(1200, 900);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		
		
		JLabel titleLabel = new JLabel("KBO.GG", SwingConstants.LEFT);
		titleLabel.setBounds(0, -5, 1200, 120);
		titleLabel.setBackground(new Color(0, 32, 99));
		titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 40));
		titleLabel.setPreferredSize(new Dimension(0, 80)); // 세로 높이 80px
		titleLabel.setBorder(BorderFactory.createEmptyBorder(40, 20, 0, 0)); // 위, 좌, 아래, 우 여백

		titleLabel.setForeground(new Color(255, 255, 255)); // 텍스트는 흰색
		titleLabel.setOpaque(true); // 배경색 보이게 함

		contentPane.add(titleLabel);
	}

}
