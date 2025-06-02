import java.awt.*;
import javax.swing.*;
import java.io.InputStream;

public class FontUtil {
    public static void applyGlobalFont() {
        try {
            // Load the converted font (e.g., .ttf)
            InputStream fontStream = new java.net.URL(
                "https://fastly.jsdelivr.net/gh/projectnoonnu/noonfonts_2302_01@1.0/TheJamsil5Bold.woff2') format('woff2')" // Replace with your .ttf URL
            ).openStream();
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(16f);

            // Apply the font globally to all Swing components
            UIManager.put("Label.font", customFont);
            UIManager.put("Button.font", customFont);
            UIManager.put("TextField.font", customFont);
            UIManager.put("TextArea.font", customFont);
            UIManager.put("Table.font", customFont);
            UIManager.put("List.font", customFont);
            UIManager.put("ComboBox.font", customFont);
            UIManager.put("Menu.font", customFont);
            UIManager.put("MenuItem.font", customFont);
            UIManager.put("CheckBox.font", customFont);
            UIManager.put("RadioButton.font", customFont);
            UIManager.put("TabbedPane.font", customFont);
            UIManager.put("ToolTip.font", customFont);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
