import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Scanner;

public class CorpusFiller {
    public CorpusFiller(File file) {
        try {
            for (int i = 0; i < 90; i++) {
                String link = getRandomLink();
                Files.writeString(file.toPath(), link, StandardOpenOption.APPEND);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getRandomLink() {
        try {
            URL url = new URL("https://en.wikipedia.org/w/api.php?action=query&format=json&list=random&rnnamespace=0&rnlimit=1");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            StringBuilder inline = new StringBuilder();
            Scanner scanner = new Scanner(url.openStream());

            while (scanner.hasNext()) {
                inline.append(scanner.nextLine());
            }

            scanner.close();
            JSONParser parse = new JSONParser();

            JSONObject data = (JSONObject) parse.parse(inline.toString());
            JSONObject query = (JSONObject) data.get("query");
            JSONArray random = (JSONArray) query.get("random");
            JSONObject article = (JSONObject) random.get(0);
            String title = (String) article.get("title");

            return "\nhttps://en.wikipedia.org/wiki/" + title.replace(" ", "_");

        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
