import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.List;
import java.util.Map;

public class App {

    static InputStream image;
    public static void main(String[] args) throws Exception {

        //make a HTTP Request: you're asking something via Internet using some specific protocol
        String body = makeHttpRequest();

        //parsing data: you're excracting the data you want (title, rating, ranking)
        List<Map<String, String>> movieList = parsingData(body);

        makeDirIfNotExisting();

        //show and manipulate our data
        for (Map<String,String> movie : movieList) {
            String urlImage = movie.get("image");
            String title = movie.get("title");
            String text = textOnSticker(movie);            
            InputStream inputStream = new URL(urlImage).openStream();
            StickerGenerator stickerGenerator = new StickerGenerator();
            stickerGenerator.create(inputStream, title, text, image);
            printingWithEmojis(movie);
        }
        
    }


    private static String textOnSticker(Map<String, String> movie) throws FileNotFoundException {
        Double rating = Double.parseDouble(movie.get("imDbRating"));
        String text;
        if (rating >= 8){
            text = "Awesome";
            image = new FileInputStream(new File("AluraStickers/input/happy_emoji.jpg"));
        }else{
            text = "Nice";
            image = new FileInputStream(new File("AluraStickers/input/sad_emoji.jpg"));
        }
        return text;
    }

    private static List<Map<String, String>> parsingData(String body) {
        JsonParser parser = new JsonParser();
        List<Map<String, String>> movieList = parser.parse(body);
        return movieList;
    }

    private static void printingWithEmojis(Map<String, String> movie) {
        System.out.print("\u001b[90m\u001b[105m\u001b[1mRating: ");
        System.out.println(movie.get("imDbRating") + "\u001b[m");
        AddStarsToPrint(movie);            
        System.out.print("Title: \u001b[1m");
        System.out.println(movie.get("title")+ "\u001b[m");
        System.out.print("Poster: \u001b[1m");
        System.out.println(movie.get("image")+ "\u001b[m");
        System.out.println();
    }

    private static void AddStarsToPrint(Map<String, String> movie) {
        double starsDouble = Double.parseDouble(movie.get("imDbRating"));
        int starsInt = (int) starsDouble;
        for (int n = 1; n <= starsInt; n++){
            System.out.print("⭐");
        }
    }

    private static void makeDirIfNotExisting() {
        var path = new File("output");
        path.mkdir();
    }

    private static String makeHttpRequest() throws IOException, InterruptedException {
        String url = "https://raw.githubusercontent.com/alura-cursos/imersao-java-2-api/main/TopTVs.json";
        URI adress = URI.create(url);
        var client = HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder(adress).GET().build();
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        String body = response.body();
        return body;
    }
}
