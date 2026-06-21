import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.google.gson.*;

public class Handler implements RequestHandler<Object, String> {

    static final String LINE_TOKEN = System.getenv("LINE_TOKEN");
    static final String USER_ID = System.getenv("USER_ID");

    @Override
    public String handleRequest(Object input, Context context) {
        try {
            String isbn = "9784041174807";
            String url = "https://api.openbd.jp/v1/get?isbn=" + isbn;

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();

            HttpResponse<String> response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());

            JsonArray jsonArray = JsonParser.parseString(response.body()).getAsJsonArray();
            JsonObject summary = jsonArray.get(0).getAsJsonObject()
                    .getAsJsonObject("summary");

            String title = summary.get("title").getAsString();
            String pubdate = summary.get("pubdate").getAsString();

            // 今日の年月を取得（例：202606）
            java.time.LocalDate today = java.time.LocalDate.now();
            String thisMonth = String.format("%04d%02d", today.getYear(), today.getMonthValue());

            if (pubdate.startsWith(thisMonth)) {
                sendLineMessage(client, title + "は今月発売予定です！");
                return "今月発売：通知しました";
            } else {
                return "今月発売ではありません（発売日情報：" + pubdate + "）";
            }

        } catch (Exception e) {
            context.getLogger().log("エラー: " + e.getMessage());
            return "失敗";
        }
    }

    static void sendLineMessage(HttpClient client, String message) throws Exception {
    String body = "{\"to\":\"" + USER_ID + "\",\"messages\":[{\"type\":\"text\",\"text\":\"" + message + "\"}]}";

    HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("https://api.line.me/v2/bot/message/push"))
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer " + LINE_TOKEN)
            .POST(HttpRequest.BodyPublishers.ofString(body))
            .build();

    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    
    System.out.println("LINE APIレスポンス: " + response.statusCode() + " " + response.body());
    }
}
