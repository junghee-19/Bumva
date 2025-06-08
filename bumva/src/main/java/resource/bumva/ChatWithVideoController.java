package resource.bumva;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.web.WebView;

import bumva.main.frames.SignInForm;
import bumva.db.UserDAO;

import java.io.*;
import java.net.Socket;

public class ChatWithVideoController {
    @FXML private WebView webView;
    @FXML private TextArea chatArea;
    @FXML private TextField inputField;

    private BufferedWriter out;

    @FXML
    public void initialize() {
        webView.getEngine().load("https://www.youtube.com/embed/T7BLbJqVxRA");

        // 서버 연결
        new Thread(() -> {
            try {
                Socket socket = new Socket("localhost", 7777);
                out = new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream())
                );
                BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream())
                );
                String line;
                while ((line = in.readLine()) != null) {
                    String finalLine = line;
                    Platform.runLater(() ->
                        chatArea.appendText(finalLine + "\n")
                    );
                }
            } catch (IOException e) {
                showError("서버 연결 실패: " + e.getMessage());
            }
        }).start();
    }

    @FXML
    private void sendMessage() {
        if (out == null) {
            System.err.println("❌ 서버 연결이 아직 준비되지 않았습니다.");
            return;
        }
        String raw = inputField.getText().trim();
        if (raw.isEmpty()) return;

        // 로그인된 userId → nickname
        String userId = SignInForm.getCurrentUserId();
        System.out.println("DEBUG: currentUserId = " + userId);
        String nickname = UserDAO.getNickname(userId);
        if (nickname == null) nickname = "익명";

        String msgToSend = nickname + ": " + raw;
        try {
            chatArea.appendText(msgToSend + "\n");
            out.write(msgToSend + "\n");
            out.flush();
            inputField.clear();
        } catch (IOException e) {
            showError("메시지 전송 실패");
        }
    }

    private void showError(String msg) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
            alert.showAndWait();
        });
    }
}