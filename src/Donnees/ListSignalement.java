package Donnees;

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
	   		Signalement s=new Signalement(article.getString("scientificName"),article.getString("order"),
	   				article.getString("superclass"),article.getString("recordedBy"),article.getString("species"));
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
	
}
