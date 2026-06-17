import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.google.gson.*;

public class Handler implements RequestHandler<Object, String> {

    static final String LINE_TOKEN = "GAHPcSab50REDD5PY3BzqXPtIT7+MYzuKuwVAX1dVqmgGA+LlbN47daUAZRbadQ2lpR4/+WDV3n90hqB6OE4+3tyWEUh/3S9vWSpa2EvxTd7ut4yGijls2TfDaA2EyHUrw3QPmZkhUmDqsoWj2rvTgdB04t89/1O/w1cDnyilFU=";
    static final String USER_ID = "U03f7318bb7a503b64f8c0a895d65b9a5";

    @Override
    public String handleRequest(Object input, Context context) {
        try {
            String isbn = "9784088825830";
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

            sendLineMessage(client, title + "の発売日は" + pubdate + "です！");

            return "成功";
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

        client.send(request, HttpResponse.BodyHandlers.ofString());
    }
}