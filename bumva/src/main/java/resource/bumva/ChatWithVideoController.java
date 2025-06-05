package resource.bumva;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.web.WebView;

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
        System.out.println("✅ initialize 실행됨");

        new Thread(() -> {
            try {
                Socket socket = new Socket("localhost", 7777);
                System.out.println("✅ 서버 연결됨");
                out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                System.out.println("✅ 스트림 초기화 완료");

                String line;
                while ((line = in.readLine()) != null) {
                    String finalLine = line;
                    javafx.application.Platform.runLater(() -> chatArea.appendText(finalLine + "\n"));
                }

            } catch (IOException e) {
                showError("서버 연결 실패: " + e.getMessage());
            }
        }).start();

        inputField.setOnAction(e -> sendMessage());  // 엔터로 전송
    }

    @FXML
    private void sendMessage() {
        if (out == null) {
            System.out.println("❌ BufferedWriter out이 초기화되지 않았습니다.");
            return;
        }

        try {
            String msg = inputField.getText();
            out.write(msg + "\n");
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