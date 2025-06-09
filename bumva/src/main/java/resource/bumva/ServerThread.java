package resource.bumva;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class ServerThread extends Thread {
    private final Socket socket;
    private final BufferedReader in;
    private final BufferedWriter out;
    private final List<ServerThread> clients;

    public ServerThread(Socket socket, List<ServerThread> clients) throws IOException {
        this.socket  = socket;
        this.clients = clients;

        this.in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    @Override
    public void run() {
        try {
            String msg;
            while ((msg = in.readLine()) != null) {
                System.out.println("수신: " + msg);

                synchronized (clients) {
                    for (ServerThread client : clients) {
                        // ← 이 부분을 추가
                        if (client == this) {
                            // 자기 자신으로부터 받은 메시지는 다시 보내지 않음
                            continue;
                        }

                        try {
                            client.out.write(msg);
                            client.out.newLine();
                            client.out.flush();
                        } catch (IOException e) {
                            System.err.println("전송 오류, 클라이언트 제거: " 
                                               + client.socket.getRemoteSocketAddress());
                            client.cleanup();
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("클라이언트 연결 끊김: " 
                               + socket.getRemoteSocketAddress());
        } finally {
            cleanup();
        }
    }

    /** 소켓·스트림 닫고, 클라이언트 리스트에서 자신 제거 */
    private void cleanup() {
        try { in.close();  } catch (IOException ignored) {}
        try { out.close(); } catch (IOException ignored) {}
        try { socket.close(); } catch (IOException ignored) {}

        synchronized (clients) {
            clients.remove(this);
        }
        System.out.println("클라이언트 종료: " 
                           + socket.getRemoteSocketAddress());
    }
}