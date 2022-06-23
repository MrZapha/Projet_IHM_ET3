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
	/**
	 * La fonction permettant de lire depuis un fichier .Json
	 * @param rd un Reader 
	 * @return un String au format Json
	 * @throws IOException
	 */
	private static String readAll(Reader rd) throws IOException{
    	StringBuilder sb=new StringBuilder();
    	int cp;
    	while ((cp=rd.read()) != -1) {
    		sb.append((char) cp);
    	}
    	return sb.toString();
    }
	
	/**
	 * La fonction permettant de lire depuis une url
	 * @param url l'url ou on veut récuperer le Json
	 * @return un JSONObject qui correspond au resultat de la requête
	 */
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
	
	/**
	 * La fonction permettant de lire depuis une url
	 * @param url l'url ou on veut récuperer le Json
	 * @return un JSONArray qui correspond au resultat de la requête
	 */
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
	
	/**
	 * Une fonction qui permet de tester si le nom de l'espèce contient un espace et si c'est le cas change l'espace par %20
	 * @param nom le nom de l'espèce
	 * @return le nom de l'espéce modifié si besoin
	 */
	private static String testnom(String nom) {
		String s=nom.replace(" ", "%20");
		return s;
	}
	
	/**
	 * La fonction qui fait une requête a l'API en fonction du nom de l'espèce et de la précision géohash
	 * @param nom le nom de l'espèce
	 * @param nb_caractere la précision geohash
	 * @return un JSONObject qui correspond au resultat de la requête
	 */
	public static JSONObject readJsonWithGeoHash(String nom,int nb_caractere) {
		String s="https://api.obis.org/v3/occurrence/grid/"+nb_caractere+"?scientificname="+testnom(nom);
		return readJsonFromUrl(s);
	}
	
	/**
	 * La fonction qui fait une requête a l'API en fonction du nom de l'espèce, de la précision géohash et un intervalle de temps
	 * @param nom le nom de l'espèce
	 * @param nb_caractere la précision geohash
	 * @param date_debut la date du début de l'intervalle de temps
	 * @param date_fin la date de la fin de l'intervalle de temps
	 * @return un JSONObject qui correspond au resultat de la requête
	 */
	public static JSONObject readJsonWithGeoHashAndTime(String nom,int nb_caractere,LocalDate date_debut,LocalDate date_fin) {
		String s="https://api.obis.org/v3/occurrence/grid/"+nb_caractere+"?scientificname="+testnom(nom)+"&startdate="+date_debut+"&enddate="+date_fin;
		return readJsonFromUrl(s);
	}
	
	/**
	 * La fonction pour initialiser l'application avec le fichier Delphinidae.json
	 * @return un JSONObject qui correspond au fichier
	 */
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
	
	/**
	 * La fonction pour initialiser l'application avec le fichier Selachii.json
	 * @return un JSONObject qui correspond au fichier
	 */
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
	
	/**
	 * La fonction qui permet de récuperer les informations sur les espèces présentes dans un GeoHash
	 * @param GeoHash le GeoHash demander
	 * @return un JSONObject qui correspond au resultat de la requête
	 */
	public static JSONObject liste_Espece_GeoHash(String GeoHash) {
		String s="https://api.obis.org/v3/occurrence?";
		s+="geometry="+GeoHash;
		return readJsonFromUrl(s);
	}
	
	/**
	 * La fonction qui permet de récuperer les informations sur l'espece données en paramétres présentes dans un GeoHash
	 * @param nom le nom de l'espèce
	 * @param GeoHash le GeoHash demander
	 * @return un JSONObject qui correspond au resultat de la requête
	 */
	public static JSONObject details_Enregistrement_GeoHash(String nom,String GeoHash) {
		String s="https://api.obis.org/v3/occurrence?";
		s+=testnom(nom)+"&";
		s+="geometry="+GeoHash;
		return readJsonFromUrl(s);
	}
	
	/**
	 * La fonction qui permet de faire l'autocompletion
	 * @param texte le texte pour faire l'autocompletion
	 * @return un JSONArray qui correspond au resultat de la requête
	 */
	public static JSONArray completeSpecies(String texte) {
		String s="https://api.obis.org/v3/taxon/complete/verbose/"+testnom(texte);
		return readJsonFromUrlArray(s);
	}
	
}
