package bumva.main.components;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Font;
import javax.swing.SwingConstants;

public class PlayerInfoBox extends RoundedPanel {
	private String title;
	private String data;
	
	public PlayerInfoBox(String title, String data) {
		super(20); // 20px corner radius
		this.title = title;
		this.data = data;

		
		setBackground(new Color(223, 234, 255));
		setBounds(6, 6, 77, 76);
		setLayout(null);
		
		JLabel lblNewLabel = new JLabel(title);
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Lucida Grande", Font.BOLD, 14));
		lblNewLabel.setBounds(0, 0, 100, 22);
		
		add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel(data);
		lblNewLabel_1.setFont(new Font("Lucida Grande", Font.ITALIC, 13));
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setBounds(0, 25, 100, 28);
		add(lblNewLabel_1);
	}
	
	
	

}
