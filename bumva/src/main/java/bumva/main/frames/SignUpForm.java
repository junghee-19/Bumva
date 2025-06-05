package bumva.main.frames;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Point;
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
import javax.swing.JScrollPane;
import javax.swing.ImageIcon;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class SignUpForm extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_2;
	private JTextField textField_1;
	private JTextField textField_3;
	private JTextField textField_4;
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

		JButton btnNewButton = new JButton("회원가입");
		btnNewButton.setBounds(16, 457, 143, 47);
		panel.add(btnNewButton);
		btnNewButton.setFont(new Font("Lucida Grande", Font.PLAIN, 16));

		textField_2 = new JTextField("아이디");
		textField_2.setBounds(37, 65, 267, 47);
		panel.add(textField_2);
		textField_2.setForeground(Color.GRAY);
		textField_2.setColumns(10);

		textField_4 = new JTextField("닉네임");
		textField_4.setBounds(37, 124, 267, 47);
		panel.add(textField_4);
		textField_4.setForeground(Color.GRAY);
		textField_4.setColumns(10);

		textField_1 = new JTextField("비밀번호");
		textField_1.setBounds(37, 183, 267, 47);
		panel.add(textField_1);
		textField_1.setForeground(Color.GRAY);
		textField_1.setColumns(10);

		textField_3 = new JTextField("비밀번호 확인");
		textField_3.setBounds(37, 242, 267, 47);
		panel.add(textField_3);
		textField_3.setForeground(Color.GRAY);
		textField_3.setColumns(10);

		JButton btnNewButton_1 = new JButton("뒤로");
		btnNewButton_1.setBounds(171, 457, 143, 47);
		panel.add(btnNewButton_1);
		btnNewButton_1.addActionListener(new ActionListener() {
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
		btnNewButton_1.setFont(new Font("Lucida Grande", Font.PLAIN, 16));

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(16, 308, 312, 108);
		panel.add(scrollPane);

		JPanel imagePanel = new JPanel();
		imagePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10)); // Horizontal layout with spacing

		// Set the image panel as the viewport of the scroll pane
		scrollPane.setViewportView(imagePanel);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);

		JLabel lblNewLabel_2 = new JLabel("KBO.GG 회원가입");
		lblNewLabel_2.setBounds(0, 6, 341, 53);
		panel.add(lblNewLabel_2);
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2.setFont(new Font("Lucida Grande", Font.BOLD | Font.ITALIC, 30));

		// Add FocusListener to textField_3
		textField_3.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				if (textField_3.getText().equals("비밀번호 확인")) {
					textField_3.setText("");
					textField_3.setForeground(Color.BLACK);
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (textField_3.getText().isEmpty()) {
					textField_3.setText("비밀번호 확인");
					textField_3.setForeground(Color.GRAY);
				}
			}
		});

		// Add FocusListener to textField_1
		textField_1.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				if (textField_1.getText().equals("비밀번호")) {
					textField_1.setText("");
					textField_1.setForeground(Color.BLACK);
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (textField_1.getText().isEmpty()) {
					textField_1.setText("비밀번호");
					textField_1.setForeground(Color.GRAY);
				}
			}
		});

		// Add FocusListener to textField_4
		textField_4.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				if (textField_4.getText().equals("닉네임")) {
					textField_4.setText("");
					textField_4.setForeground(Color.BLACK);
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (textField_4.getText().isEmpty()) {
					textField_4.setText("닉네임");
					textField_4.setForeground(Color.GRAY);
				}
			}
		});

		// Add FocusListener to textField_2
		textField_2.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				if (textField_2.getText().equals("아이디")) {
					textField_2.setText("");
					textField_2.setForeground(Color.BLACK);
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (textField_2.getText().isEmpty()) {
					textField_2.setText("아이디");
					textField_2.setForeground(Color.GRAY);
				}
			}
		});
	}
}
