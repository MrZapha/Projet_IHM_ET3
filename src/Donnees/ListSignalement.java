package Donnees;

import java.io.StringReader;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;
import Json.Json;

/**
 * La classe implémentant une liste de signalement
 * @author Julian COYNEL
 *
 */
public class ListSignalement {
	/**
	 * La liste des Signalements
	 */
	private ArrayList<Signalement> list;
	
	/**
	 * La méthode pour innitialisé la liste des Signalements
	 */
	ListSignalement() {
		list=new ArrayList<Signalement>();
	}
	
	/**
	 * La fonction permetant de rajouter un Signalement a la liste
	 * @param s le Signalement a rajouté a la liste
	 */
	private void add_Signalement(Signalement s) {
		list.add(s);
	}
	
	/**
	 * La fonction permetant de rajouter tous les Signalements présents dans une requête Json dans la liste
	 * @param jsonRoot la requête qui contient les signalements
	 */
	private void add_Signalement(JSONObject jsonRoot) {
		if(!jsonRoot.isNull("results")) {
			JSONArray resultatRecherche = jsonRoot.getJSONArray("results");
			int taille=resultatRecherche.length();
			for(int i=0;i<taille;i++) {
				JSONObject article = resultatRecherche.getJSONObject(i);
				String scientificName;
				//Certains signalement ne comportant pas tous les champs on a été obligés de faire un try cathc pour ne pas avoir de problémes 
				try(StringReader test_nomscientific=new StringReader(article.getString("scientificName"))) {
					scientificName=article.getString("scientificName");
				}
				catch(org.json.JSONException e) {
					scientificName="N/A";
				};
				String order;
				try(StringReader test_order=new StringReader(article.getString("order"))) {
					order=article.getString("order");
				}
				catch(org.json.JSONException e) {
					order="N/A";
				};
				String superclass;
				try(StringReader test_superclass=new StringReader(article.getString("parvphylum"))) {
					superclass=article.getString("parvphylum");
				}
				catch(org.json.JSONException e) {
					superclass="N/A";
				};
				String recordedBy;
				try(StringReader test_recordeBy=new StringReader(article.getString("scientificNameAuthorship"))) {
					recordedBy=article.getString("scientificNameAuthorship");
				}
				catch(org.json.JSONException e) {
					recordedBy="N/A";
				};
				String species;
				try(StringReader test_species=new StringReader(article.getString("species"))) {
					species=article.getString("species");
				}
				catch(org.json.JSONException e) {
					species="N/A";
				};
				Signalement s=new Signalement(scientificName,order,superclass
						,recordedBy,species);
				this.add_Signalement(s);
				
			}
			return;
			
		}
		System.out.println("Aucune espèce trouvée");
	}
	
	/**
	 * La fonction qui permet d'initialise la liste d'espece étant donnée qu'on ne prend pas en compte un nom d'espèce
	 * @param GeoHash la région de recherche
	 * @return ListSignalement la liste d'espèce
	 */
	public static ListSignalement set_Liste_Espece(String GeoHash) {
		ListSignalement l=new ListSignalement();
		JSONObject jsonRoot=Json.liste_Espece_GeoHash(GeoHash);
		l.add_Signalement(jsonRoot);
		return l;
	}
	
	/**
	 * La fonction permettant de retourner une liste de nom d'espèce
	 * @return ArrayList<String> la liste des noms d'espèces
	 */
	public ArrayList<String> get_Liste_Espece(){
		ArrayList<String> ls=new ArrayList<String>();
		for(Signalement s : list) {
			ls.add(s.get_scientificName());
		}
		return ls;
	}
	
	/**
	 * La fonction qui permet de connaître toutes les informations d'une espèce dans une région donné
	 * @param nom le nom de l'espèce
	 * @param GeoHash la région de recherche
	 * @return ListSignalement la liste des Signalement de l'espèce donné dans la région donné
	 */
	public static ListSignalement set_Liste_information_Espece(String nom,String GeoHash) {
		ListSignalement l=new ListSignalement();
		JSONObject jsonRoot=Json.details_Enregistrement_GeoHash(nom, GeoHash);
		l.add_Signalement(jsonRoot);
		return l;
	}
	
	/**
	 * La fonction qui renvoit la liste des Signalements
	 * @return ArrayList<Signalement> la liste des Signalements
	 */
	public ArrayList<Signalement> get_List_Signalement(){
		return list;
	}
	
}
