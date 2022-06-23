package Donnees;

import java.io.StringReader;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;
import Json.Json;

/**
 * La classe impl�mentant une liste de signalement
 * @author Julian COYNEL
 *
 */
public class ListSignalement {
	/**
	 * La liste des Signalements
	 */
	private ArrayList<Signalement> list;
	
	/**
	 * La m�thode pour innitialis� la liste des Signalements
	 */
	ListSignalement() {
		list=new ArrayList<Signalement>();
	}
	
	/**
	 * La fonction permetant de rajouter un Signalement a la liste
	 * @param s le Signalement a rajout� a la liste
	 */
	private void add_Signalement(Signalement s) {
		list.add(s);
	}
	
	/**
	 * La fonction permetant de rajouter tous les Signalements pr�sents dans une requ�te Json dans la liste
	 * @param jsonRoot la requ�te qui contient les signalements
	 */
	private void add_Signalement(JSONObject jsonRoot) {
		if(!jsonRoot.isNull("results")) {
			JSONArray resultatRecherche = jsonRoot.getJSONArray("results");
			int taille=resultatRecherche.length();
			for(int i=0;i<taille;i++) {
				JSONObject article = resultatRecherche.getJSONObject(i);
				String scientificName;
				//Certains signalement ne comportant pas tous les champs on a �t� oblig�s de faire un try cathc pour ne pas avoir de probl�mes 
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
		System.out.println("Aucune esp�ce trouv�e");
	}
	
	/**
	 * La fonction qui permet d'initialise la liste d'espece �tant donn�e qu'on ne prend pas en compte un nom d'esp�ce
	 * @param GeoHash la r�gion de recherche
	 * @return ListSignalement la liste d'esp�ce
	 */
	public static ListSignalement set_Liste_Espece(String GeoHash) {
		ListSignalement l=new ListSignalement();
		JSONObject jsonRoot=Json.liste_Espece_GeoHash(GeoHash);
		l.add_Signalement(jsonRoot);
		return l;
	}
	
	/**
	 * La fonction permettant de retourner une liste de nom d'esp�ce
	 * @return ArrayList<String> la liste des noms d'esp�ces
	 */
	public ArrayList<String> get_Liste_Espece(){
		ArrayList<String> ls=new ArrayList<String>();
		for(Signalement s : list) {
			ls.add(s.get_scientificName());
		}
		return ls;
	}
	
	/**
	 * La fonction qui permet de conna�tre toutes les informations d'une esp�ce dans une r�gion donn�
	 * @param nom le nom de l'esp�ce
	 * @param GeoHash la r�gion de recherche
	 * @return ListSignalement la liste des Signalement de l'esp�ce donn� dans la r�gion donn�
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
