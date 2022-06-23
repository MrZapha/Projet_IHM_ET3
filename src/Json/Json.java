package Json;

import java.io.BufferedReader;
import java.io.FileReader;
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
import java.time.LocalDate;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * La classe qui gére toute les requêtes Json faite a l'API
 * @author Julian Coynel
 *
 */
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
	
	static JSONArray readJsonFromUrlArray(String url) {
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
		return new JSONArray(json);
	}
	
	private static String testnom(String nom) {
		String s=nom.replace(" ", "%20");
		return s;
	}
	
	public static JSONObject readJsonWithGeoHash(String nom,int nb_caractere) {
		String s="https://api.obis.org/v3/occurrence/grid/"+nb_caractere+"?scientificname="+testnom(nom);
		return readJsonFromUrl(s);
	}
	
	public static JSONObject readJsonWithGeoHashAndTime(String nom,int nb_caractere,LocalDate date_debut,LocalDate date_fin) {
		String s="https://api.obis.org/v3/occurrence/grid/"+nb_caractere+"?scientificname="+testnom(nom)+"&startdate="+date_debut+"&enddate="+date_fin;
		return readJsonFromUrl(s);
	}
	
	public static JSONObject init_delphinidae() {
		try (Reader reader = new FileReader("Delphinidae.json")){
        	BufferedReader rd=new BufferedReader(reader);
        	String jsonText =readAll(rd);
        	JSONObject jsonRoot = new JSONObject(jsonText);
        	return jsonRoot;
        }catch (IOException e) {
        	e.printStackTrace();
        	return null;
        }
	}
	
	public static JSONObject init_selachii() {
		try (Reader reader = new FileReader("Selachii.json")){
        	BufferedReader rd=new BufferedReader(reader);
        	String jsonText =readAll(rd);
        	JSONObject jsonRoot = new JSONObject(jsonText);
        	return jsonRoot;
        }catch (IOException e) {
        	e.printStackTrace();
        	return null;
        }
	}
	
	public static JSONObject liste_Espece_GeoHash(String GeoHash) {
		String s="https://api.obis.org/v3/occurrence?";
		s+="geometry="+GeoHash;
		return readJsonFromUrl(s);
	}
	
	public static JSONObject details_Enregistrement_GeoHash(String nom,String GeoHash) {
		String s="https://api.obis.org/v3/occurrence?";
		s+=testnom(nom)+"&";
		s+="geometry="+GeoHash;
		return readJsonFromUrl(s);
	}
	
	public static JSONArray completeSpecies(String texte) {
		String s="https://api.obis.org/v3/taxon/complete/verbose/"+texte;
		return readJsonFromUrlArray(s);
	}
	
	public static void main(String args[]) {
		JSONObject jsonRoot= readJsonWithGeoHashAndTime("Manta birostris",4,LocalDate.of(2002, 10, 05),LocalDate.now());
		JSONArray resultatRecherche = jsonRoot.getJSONArray("features");
	   	JSONObject article = resultatRecherche.getJSONObject(0);
	   	System.out.println(article);
	   	int nb=article.getJSONObject("properties").getInt("n");
	   	JSONObject geometry =article.getJSONObject("geometry");
	   	System.out.println(geometry.getJSONArray("coordinates").getJSONArray(0).getJSONArray(0).getDouble(0));
	   	System.out.println(nb);
	   	/*System.out.println(article.getString("snippet"));
	   	System.out.println(article.getInt("wordcount"));
	   	article = resultatRecherche.getJSONObject(1);
	   	System.out.println(article.getString("title"));
	   	System.out.println(article.getString("snippet"));*/

	}
}
