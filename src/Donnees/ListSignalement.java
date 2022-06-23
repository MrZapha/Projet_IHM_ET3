package Donnees;

import java.io.StringReader;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;
import Json.Json;

public class ListSignalement {
	private ArrayList<Signalement> list;
	
	ListSignalement() {
		list=new ArrayList<Signalement>();
	}
	
	public void add_Signalement(Signalement s) {
		list.add(s);
	}
	
	
	private void add_Signalement(JSONObject jsonRoot) {
		JSONArray resultatRecherche = jsonRoot.getJSONArray("results");
		int taille=resultatRecherche.length();
		for(int i=0;i<taille;i++) {
			JSONObject article = resultatRecherche.getJSONObject(i);
			String scientificName;
			try(StringReader test_nomscientific=new StringReader(article.getString("scientificName"))) {
				scientificName=article.getString("scientificName");
			}
			catch(org.json.JSONException e) {
				scientificName="Cette espèce n'as pas de nom scientifique";
			};
			String order;
			try(StringReader test_order=new StringReader(article.getString("order"))) {
				order=article.getString("order");
			}
			catch(org.json.JSONException e) {
				order="Cette espèce n'as pas d'ordre";
			};
			String superclass;
			try(StringReader test_superclass=new StringReader(article.getString("parvphylum"))) {
				superclass=article.getString("parvphylum");
			}
			catch(org.json.JSONException e) {
				superclass="Cette espèce n'as pas de super class";
			};
			String recordedBy;
			try(StringReader test_recordeBy=new StringReader(article.getString("scientificNameAuthorship"))) {
				recordedBy=article.getString("scientificNameAuthorship");
			}
			catch(org.json.JSONException e) {
				recordedBy="Ce signalement n'as pas d'auteur";
			};
			String species;
			try(StringReader test_species=new StringReader(article.getString("species"))) {
				species=article.getString("species");
			}
			catch(org.json.JSONException e) {
				species="Cette espèce n'as pas de nom d'espèce";
			};
	   		Signalement s=new Signalement(scientificName,order,superclass
	   				,recordedBy,species);
	   		this.add_Signalement(s);
		}
	}
	
	
	public static ListSignalement set_Liste_Espece(String GeoHash) {
		ListSignalement l=new ListSignalement();
		JSONObject jsonRoot=Json.liste_Espece_GeoHash(GeoHash);
		l.add_Signalement(jsonRoot);
		return l;
	}
	
	public ArrayList<String> get_Liste_Espece(){
		ArrayList<String> ls=new ArrayList<String>();
		for(Signalement s : list) {
			ls.add(s.get_scientificName());
		}
		return ls;
	}
	
	public static ListSignalement set_Liste_information_Espece(String nom,String GeoHash) {
		ListSignalement l=new ListSignalement();
		JSONObject jsonRoot=Json.details_Enregistrement_GeoHash(nom, GeoHash);
		l.add_Signalement(jsonRoot);
		return l;
	}
	
	public ArrayList<Signalement> get_List_Signalement(){
		return list;
	}
	
}
