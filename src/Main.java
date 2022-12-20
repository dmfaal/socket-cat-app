import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Date;

public class Main {
    private static final int PORT_NUMBER = 8080;
    private static Socket socket;
    private static BufferedReader in;
    private static BufferedWriter ou;

    @SuppressWarnings("InfiniteLoopStatement")
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT_NUMBER);
            System.out.println("Сервер запущен на порту " + PORT_NUMBER);
            while (true) {
                try {
                    socket = serverSocket.accept();
                    System.out.println("Установлено соединение с новым клиентом");

                    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    ou = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

                    String str = in.readLine(); // GET /Page_one HTTP/1.1
                    String contentType = "text/html; charset=utf-8";
                    byte[] content;

                    if (str != null) {
                        String way = str.split(" ")[1].substring(1);
                        System.out.println("Клиент запросил ресурс /" + way);

                        if (way.isEmpty()) {
                            way = "index.html";
                        }

                        // Path traversal
                        // Apache HTTPD: CVE-2021-41773 & CVE-2021-42013
                        File file = new File("resources/", way);
                        if (file.exists()) {
                            content = Files.readAllBytes(file.toPath());

                            if (way.endsWith(".jpg")) {
                                contentType = "image/jpeg";
                            }
                            if (way.endsWith(".html")) {
                                Date date = new Date();
                                String s = new String(content);
                                s = s.replace("{time}", date.toString());
                                content = s.getBytes();
                            }
                        } else {
                            content = "<p>Страница не найдена.</p>".getBytes(StandardCharsets.UTF_8);
                        }
                    } else {
                        content = "<p>Страница не найдена.</p>".getBytes(StandardCharsets.UTF_8);
                    }


                    ou.write("HTTP/1.1 200 OK\n");
                    ou.write("Content-Type: " + contentType + "\n");
                    ou.write("\n");
                    ou.flush();

                    socket.getOutputStream().write(content);
                } finally {
                    socket.close();
                    in.close();
                    ou.close();
                }
            }
        } catch (IOException ex) {
            System.out.println("Сервер упал!");
            ex.printStackTrace();
        }
    }
}
