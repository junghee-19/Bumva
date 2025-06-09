package resource.bumva;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import bumva.db.UserDAO;              // <-- 여기 추가
import java.io.*;
import java.net.Socket;
import java.util.List;

public class ChatWithVideoUI extends Application {
    private TextArea chatArea;
    private TextField inputField;
    private BufferedWriter out;
    private String nickname;        // ← 로그인 닉네임을 저장할 필드

    public static void main(String[] args) {
        launch(args);  // JavaFX 앱 시작, args[0]에 userId가 넘어옵니다.
    }

    @Override
    public void start(Stage primaryStage) {
        // 1) 런칭 인자에서 userId 꺼내기
        List<String> raw = getParameters().getRaw();
        String userId = raw.isEmpty() ? null : raw.get(0);
        // 2) DAO로 닉네임 조회 (없으면 익명)
        nickname = (userId != null) 
                 ? UserDAO.getNickname(userId) 
                 : null;
        if (nickname == null || nickname.isBlank()) {
            nickname = "익명";
        }
        System.out.println("[DEBUG] 채팅 닉네임: " + nickname);

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
        inputField.setOnAction(e -> sendMessage());

        Button sendButton = new Button("전송");
        sendButton.setOnAction(e -> sendMessage());

        HBox inputBox = new HBox(10, inputField, sendButton);
        HBox.setHgrow(inputField, Priority.ALWAYS);

        root.getChildren().addAll(webView, chatArea, inputBox);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Chat + Video (" + nickname + ")");
        primaryStage.show();

        // ───── 서버 연결 ─────
        connectToServer();
    }

    private void connectToServer() {
        new Thread(() -> {
            try {
                Socket socket = new Socket("localhost", 7777);
                out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                String line;
                while ((line = in.readLine()) != null) {
                    String finalLine = line;
                    javafx.application.Platform.runLater(() ->
                        chatArea.appendText(finalLine + "\n")
                    );
                }
            } catch (IOException e) {
                showError("서버 연결 실패: " + e.getMessage());
            }
        }, "Chat-Thread").start();
    }

    private void sendMessage() {
        String raw = inputField.getText().trim();
        if (raw.isEmpty()) return;

        // 여기서 닉네임을 붙입니다.
        String msg = nickname + ": " + raw;
        try {
            // 화면 출력
            chatArea.appendText(msg + "\n");
            // 서버 전송
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