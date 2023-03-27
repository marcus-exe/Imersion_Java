import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandler;
import java.net.http.HttpResponse.BodyHandlers;
import java.security.Key;
import java.util.List;
import java.util.Map;

public class App {
    public static void main(String[] args) throws Exception {

        //make a HTTP Request: you're asking something via Internet using some specific protocol
        String url = "https://raw.githubusercontent.com/alura-cursos/imersao-java-2-api/main/TopTVs.json";
        URI adress = URI.create(url);
        var client = HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder(adress).GET().build();
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        String body = response.body();

        //parsing data: you're excracting the data you want (title, rating, ranking)
        JsonParser parser = new JsonParser();
        List<Map<String, String>> movieList = parser.parse(body);

        //show and manipulate our data
        for (Map<String,String> movie : movieList) {
            System.out.print("\u001b[90m\u001b[105m\u001b[1mRating: ");
            System.out.println(movie.get("imDbRating") + "\u001b[m");
            double starsDouble = Double.parseDouble(movie.get("imDbRating"));
            int starsInt = (int) starsDouble;
            for (int n = 1; n <= starsInt; n++){
                System.out.print("⭐");
            }
            
            System.out.print("Title: \u001b[1m");
            System.out.println(movie.get("title")+ "\u001b[m");
            System.out.print("Poster: \u001b[1m");
            System.out.println(movie.get("image")+ "\u001b[m");
            System.out.println();
        }
        
    }
}
