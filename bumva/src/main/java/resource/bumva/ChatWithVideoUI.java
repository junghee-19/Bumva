package resource.bumva;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;

public class ChatWithVideoUI extends Application {
    private TextArea chatArea;
    private TextField inputField;
    private Button sendButton;
    private BufferedWriter out;
    private Socket socket;

    public static void main(String[] args) {
        launch(args);  // JavaFX 앱 시작
    }

    @Override
    public void start(Stage primaryStage) {
        System.out.println("[클라이언트] UI 시작");

        // ───── UI 구성 ─────
        VBox root = new VBox(10);
        root.setPrefSize(640, 480);

        WebView webView = new WebView();
        webView.getEngine().load("https://www.youtube.com/embed/T7BLbJqVxRA");

        chatArea = new TextArea();
        chatArea.setEditable(false);
        chatArea.setWrapText(true);

        inputField = new TextField();
        inputField.setPromptText("메시지를 입력하세요...");
        inputField.setOnAction(e -> sendMessage()); // 엔터 전송

        sendButton = new Button("전송");
        sendButton.setOnAction(e -> sendMessage());

        HBox inputBox = new HBox(10, inputField, sendButton);
        HBox.setHgrow(inputField, Priority.ALWAYS); // ← HBox에서 setHgrow는 static 메서드임

        root.getChildren().addAll(webView, chatArea, inputBox);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Chat + Video");
        primaryStage.show();

        // ───── 서버 연결 ─────
        connectToServer();
    }

    private void connectToServer() {
        new Thread(() -> {
            try {
                System.out.println("[클라이언트] 서버에 연결 시도 중...");
                socket = new Socket("localhost", 7777);
                out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                System.out.println("[클라이언트] 서버 연결 성공");

                String line;
                while ((line = in.readLine()) != null) {
                    String finalLine = line;
                    System.out.println("[서버 → 클라이언트 수신] " + finalLine);
                    javafx.application.Platform.runLater(() -> chatArea.appendText(finalLine + "\n"));
                }

            } catch (IOException e) {
                showError("서버 연결 실패: " + e.getMessage());
            }
        }).start();
    }

    private void sendMessage() {
        String msg = inputField.getText().trim();
        System.out.println("[클라이언트] 전송 요청: " + msg);
        if (msg.isEmpty()) return;

        try {
            out.write(msg + "\n");
            out.flush();
            inputField.clear();
        } catch (IOException e) {
            showError("메시지 전송 실패");
        }
    }

    private void showError(String msg) {
        javafx.application.Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
            alert.showAndWait();
        });
    }
}