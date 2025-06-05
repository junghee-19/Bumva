package bumva.main.frames;
import java.awt.Color;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

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
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class SignInForm extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private SignUpForm signUpForm;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SignInForm frame = new SignInForm();
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
	public SignInForm() {

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
		contentPane.add(headerPanel);


		RoundedPanel panel = new RoundedPanel(40); // 20 is the corner radius
		panel.setBackground(new Color(247, 250, 255));
		panel.setBounds(371, 165, 341, 480);
		contentPane.add(panel);
		panel.setLayout(null);

		JLabel lblNewLabel_2 = new JLabel("KBO.GG 로그인");
		lblNewLabel_2.setFont(new Font("Lucida Grande", Font.BOLD | Font.ITALIC, 30));
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2.setBounds(6, 56, 329, 57);
		panel.add(lblNewLabel_2);

		JTextField textField_2 = new JTextField("아이디");
		textField_2.setBounds(39, 177, 267, 51);
		panel.add(textField_2);
		textField_2.setForeground(Color.GRAY); // Set placeholder text color

		textField_2.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				if (textField_2.getText().equals("아이디")) {
					textField_2.setText(""); // Clear the placeholder text
					textField_2.setForeground(Color.BLACK); // Set normal text color
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (textField_2.getText().isEmpty()) {
					textField_2.setText("아이디"); // Restore the placeholder text
					textField_2.setForeground(Color.GRAY); // Set placeholder text color
				}
			}
		});
		textField_2.setColumns(10);

		JTextField textField_1 = new JTextField("비밀번호");
		textField_1.setBounds(39, 263, 267, 51);
		panel.add(textField_1);
		textField_1.setForeground(Color.GRAY); // Set placeholder text color
		textField_1.setColumns(10);
		
		
		textField_1.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				if (textField_1.getText().equals("비밀번호")) {
					textField_1.setText(""); // Clear the placeholder text
					textField_1.setForeground(Color.BLACK); // Set normal text color
				}
			}
			
			@Override
			public void focusLost(FocusEvent e) {
				if (textField_1.getText().isEmpty()) {
					textField_1.setText("비밀번호"); // Restore the placeholder text
					textField_1.setForeground(Color.GRAY); // Set placeholder text color
				}
			}
		});
		
		JButton btnNewButton = new JButton("로그인");
		btnNewButton.setBounds(39, 344, 267, 51);
		panel.add(btnNewButton);
		btnNewButton.setFont(new Font("Lucida Grande", Font.PLAIN, 16));

		JButton btnNewButton_2 = new JButton("회원가입");
		btnNewButton_2.addActionListener(new ActionListener() {
			

			public void actionPerformed(ActionEvent e) {
				if(signUpForm == null) {
					signUpForm = new SignUpForm();
				}
				
				Point currentLocation = getLocation();
				signUpForm.setLocation(currentLocation);
				signUpForm.setVisible(true);
				dispose(); // 현재 창을 닫음
			}
		});
		btnNewButton_2.setBounds(206, 416, 100, 29);
		panel.add(btnNewButton_2);
		btnNewButton_2.setFont(new Font("Lucida Grande", Font.PLAIN, 16));

		JLabel lblNewLabel_1 = new JLabel("계정이 없으신가요?");
		lblNewLabel_1.setBounds(57, 419, 124, 20);
		panel.add(lblNewLabel_1);
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel_1.setFont(new Font("Lucida Grande", Font.PLAIN, 16));

	}
}
