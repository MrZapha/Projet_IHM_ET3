package Json;

import java.io.IOException;
import java.io.Reader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpClient.Version;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONObject;

public class Json {
	private static String readAll(Reader rd) throws IOException{
    	StringBuilder sb=new StringBuilder();
    	int cp;
    	while ((cp=rd.read()) != -1) {
    		sb.append((char) cp);
    	}
    	return sb.toString();
    }
	
	static JSONObject readJsonFromUrl(String url) {
		String json="";
		HttpClient client = HttpClient.newBuilder()
				.version(Version.HTTP_1_1)
				.followRedirects(Redirect.NORMAL)
				.connectTimeout(Duration.ofSeconds(20))
				.build();
		
		HttpRequest request =HttpRequest.newBuilder()
				.uri(URI.create(url))
				.timeout(Duration.ofMinutes(2))
				.header("Content-Type", "application/json")
			.GET()
			.build();
		
		try {
			json=client.sendAsync(request, BodyHandlers.ofString())
					.thenApply(HttpResponse::body).get(10,TimeUnit.SECONDS);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return new JSONObject(json);
	}
	
	public static JSONObject readJsonWithGeoHash(String nom,int nb_caractere) {
		String s="https://api.obis.org/v3/occurrence/grid/"+nb_caractere+"?scientificname="+nom;
		return readJsonFromUrl(s);
	}
	
	public static void main(String args[]) {
		JSONObject jsonRoot= readJsonWithGeoHash("Delphinidae",3);
		//JSONObject resultatRecherche = jsonRoot.getJSONObject("geometry");
	   	/*JSONObject article = resultatRecherche.getJSONObject(0);
	   	System.out.println(article.getString("title"));
	   	System.out.println(article.getString("snippet"));
	   	System.out.println(article.getInt("wordcount"));
	   	article = resultatRecherche.getJSONObject(1);
	   	System.out.println(article.getString("title"));
	   	System.out.println(article.getString("snippet"));*/
		System.out.println(jsonRoot);
	}
}
