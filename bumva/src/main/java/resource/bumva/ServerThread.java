package resource.bumva;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ServerThread extends Thread {
    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;
    private ArrayList<ServerThread> clients;

    public ServerThread(Socket socket, ArrayList<ServerThread> clients) {
        this.socket = socket;
        this.clients = clients;

        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            System.out.println("스트림 에러: " + e.getMessage());
        }
    }

    @Override
    public void run() {
        try {
            String msg;
            while ((msg = in.readLine()) != null) {
            	System.out.println("수신: " + msg); // ✅ 여기 추가
                for (ServerThread client : clients) {
                    client.out.write(msg + "\n");
                    client.out.flush();
                }
            }
        } catch (IOException e) {
            System.out.println("클라이언트 연결 끊김");
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.out.println("소켓 닫기 실패");
            }
        }
    }
}