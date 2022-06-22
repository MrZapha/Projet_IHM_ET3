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
	
	public void add_Enregistrement(Enregistrement e) {
		list.add(e);
	}
	
	public ArrayList<Enregistrement> get_list() {
		return list;
	}
	private void add_Enregistrement(JSONObject jsonRoot) {
		JSONArray resultatRecherche = jsonRoot.getJSONArray("features");
		int taille=resultatRecherche.length();
		for(int i=0;i<taille;i++) {
			JSONObject article = resultatRecherche.getJSONObject(i);
	   		int nb=article.getJSONObject("properties").getInt("n");
	   		JSONObject geometry =article.getJSONObject("geometry");
	   		Enregistrement e=new Enregistrement(geometry.getJSONArray("coordinates").getJSONArray(0),"Delphinidae",nb);
	   		this.add_Enregistrement(e);
		}
	}
	
	public static Donne init() {
		Donne d=new Donne();
		JSONObject jsonRoot=Json.init();
		d.add_Enregistrement(jsonRoot);
		return d;
	}
	
	public static Donne donne_From_URL(String nom,int nb_caractere) {
		Donne d=new Donne();
		JSONObject jsonRoot=Json.readJsonWithGeoHash(nom, nb_caractere);
		d.add_Enregistrement(jsonRoot);
		return d;
	}
	
	public static void main(String args[]) {
		Donne d=donne_From_URL("Delphinidae",3);
		//System.out.println(d.nb_signalement_region("Dolphin",20.0,30.0));
		System.out.println(d.list);
	}
}
