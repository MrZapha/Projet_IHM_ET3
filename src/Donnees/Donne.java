package Donnees;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.function.Consumer;

import org.json.JSONArray;
import org.json.JSONObject;

import com.ludovic.vimont.GeoHashHelper;
import com.ludovic.vimont.Location;

import Json.Json;

public class Donne {
	private ArrayList<Enregistrement> list;
	
	Donne() {
		list=new ArrayList<Enregistrement>();
	}
	
	void add_Enregistrement(Enregistrement e) {
		list.add(e);
	}
	
	private void add_Enregistrement(JSONObject jsonRoot,String nom) {
		JSONArray resultatRecherche = jsonRoot.getJSONArray("features");
		int taille=resultatRecherche.length();
		for(int i=0;i<taille;i++) {
			JSONObject article = resultatRecherche.getJSONObject(i);
	   		int nb=article.getJSONObject("properties").getInt("n");
	   		JSONObject geometry =article.getJSONObject("geometry");
	   		Enregistrement e=new Enregistrement(geometry.getJSONArray("coordinates").getJSONArray(0),nom,nb);
	   		this.add_Enregistrement(e);
		}
	}
	
	public static Donne init() {
		double nb;
		nb = Math.random();
		if (nb>=0.5) {
			System.out.println("requin");
			return init_selachii();
		}
		else {
			return init_delphinidae();
		}
	}
	
	private static Donne init_delphinidae() {
		Donne d=new Donne();
		JSONObject jsonRoot=Json.init_delphinidae();
		d.add_Enregistrement(jsonRoot,"Delphinidae");
		return d;
	}
	
	private static Donne init_selachii() {
		Donne d=new Donne();
		JSONObject jsonRoot=Json.init_selachii();
		d.add_Enregistrement(jsonRoot,"Selachii");
		return d;
	}
	
	
	public static Donne donne_From_URL(String nom,int nb_caractere) {
		Donne d=new Donne();
		JSONObject jsonRoot=Json.readJsonWithGeoHash(nom, nb_caractere);
		d.add_Enregistrement(jsonRoot,nom);
		return d;
	}
	
	public static Donne donne_From_URL_With_Date(String nom,int nb_caractere,LocalDate date_debut,LocalDate date_fin) {
		Donne d=new Donne();
		JSONObject jsonRoot=Json.readJsonWithGeoHashAndTime(nom, nb_caractere, date_debut, date_fin);
		d.add_Enregistrement(jsonRoot,nom);
		return d;
	}
	
	/*public static Donne donne_From_URL_With_Time_Interval() {
		
	}*/
	
	public ArrayList<Enregistrement> get_list() {
		return list;
	}
	
	public static void main(String args[]) {
		Donne d=init();
		//System.out.println(d.nb_signalement_region("Dolphin",20.0,30.0));
		System.out.println(d.list);
	}
}
