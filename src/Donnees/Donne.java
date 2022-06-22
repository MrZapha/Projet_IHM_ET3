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
	private int s=0;
	private ArrayList<Enregistrement> list;
	
	public Donne() {
		list=new ArrayList<Enregistrement>();
	}
	
	public void add_Enregistrement(Enregistrement e) {
		list.add(e);
	}
	
	int nb_signalement_region(String nom,String region) {
		s=0;
		Consumer<Enregistrement> conter_espece = new Consumer<Enregistrement>() {
			@Override
			public void accept(Enregistrement t) {
				if (t.get_nom().equals(nom) && t.get_region().equals(region)) {
					s++;
				}
				
			};
        };
		
		list.forEach(conter_espece);
		return s;
	}
	
	public int nb_signalement_region(String nom,Double longitude,Double latitude) {
		return nb_signalement_region(nom,GeoHashHelper.getGeohash(new Location("",longitude,latitude)));
	}
	
	public static Donne initialize() {
		Donne d=new Donne();
		JSONObject jsonRoot=Json.initialize();
		JSONArray resultatRecherche = jsonRoot.getJSONArray("features");
		int taille=resultatRecherche.length();
		for(int i=0;i<taille;i++) {
			JSONObject article = resultatRecherche.getJSONObject(i);
	   		int nb=article.getJSONObject("properties").getInt("n");
	   		JSONObject geometry =article.getJSONObject("geometry");
	   		Enregistrement e=new Enregistrement(geometry.getJSONArray("coordinates"),"Delphinidae",LocalDate.now(),nb);
	   		d.add_Enregistrement(e);
		}
		return d;
	}
	
	public static void main(String args[]) {
		Donne d=initialize();
		//System.out.println(d.nb_signalement_region("Dolphin",20.0,30.0));
		System.out.println(d.list);
	}
}
