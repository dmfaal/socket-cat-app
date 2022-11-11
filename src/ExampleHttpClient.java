import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@SuppressWarnings({"resource", "CommentedOutCode"})
public class ExampleHttpClient {

    static final String requestPayload = "GET / HTTP/1.0\r\nHost: www.google.com\r\n\r\n";
//    static final String requestPayload = "GET / HTTP/1.0\r\nHost: google.com\r\n\r\n";
//    static final String requestPayload = "ASDFAKDFASDFJKASDLFJASDFLKASJDF / HTTP/1.0\r\nHost: heyheyhey\r\n\r\n";
//    static final String requestPayload = "ASDFAKDFASDFJKASDLFJASDFLKASJDF / HTTP/1.0\r\nHost: www.google.com\r\n\r\n";

    static final HttpClient httpClient = HttpClient.newHttpClient();

    /**
     * Main function
     */
    public static void main(String[] args) throws Exception {
//        final Socket socket = new Socket("64.233.165.101", 80);
//        System.out.print(requestPayload);
//        socket.getOutputStream().write(requestPayload.getBytes());
//        final byte[] bytes = socket.getInputStream().readAllBytes();
//        System.out.println(new String(bytes, StandardCharsets.UTF_8));

        HttpRequest request =
                HttpRequest.newBuilder()
                        .uri(URI.create("https://www.google.com/"))
                        .timeout(Duration.ofMinutes(2))
//                        .header("Content-Type", "application/json")
//                        .POST(HttpRequest.BodyPublishers.ofFile(Paths.get("file.json"))).build();
                        .GET()
                        .build();

        final String body = httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();
        System.out.println(body);
    }
}
