import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.google.gson.*;

public class Main {
    public static void main(String[] args) throws Exception {
        String isbn = "9784088825830";
        String url = "https://api.openbd.jp/v1/get?isbn=" + isbn;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        // JSONを解析
        JsonArray jsonArray = JsonParser.parseString(response.body()).getAsJsonArray();
        JsonObject summary = jsonArray.get(0).getAsJsonObject()
                .getAsJsonObject("summary");

        String title = summary.get("title").getAsString();
        String pubdate = summary.get("pubdate").getAsString();

        System.out.println("タイトル：" + title);
        System.out.println("発売日：" + pubdate);
    }
}