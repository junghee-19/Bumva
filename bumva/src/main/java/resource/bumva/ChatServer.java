package resource.bumva;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ChatServer {
    public static void main(String[] args) {
        ArrayList<ServerThread> clients = new ArrayList<>();

        try (ServerSocket serverSocket = new ServerSocket(7777)) {
            System.out.println("서버 시작됨");

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("클라이언트 연결됨");

                ServerThread client = new ServerThread(socket, clients);
                clients.add(client);
                client.start();
            }

        } catch (IOException e) {
            System.out.println("서버 에러: " + e.getMessage());
        }
    }
}