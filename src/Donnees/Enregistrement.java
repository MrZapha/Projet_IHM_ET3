package Donnees;

import java.time.LocalDate;
import java.util.ArrayList;

import org.json.JSONArray;

import com.ludovic.vimont.GeoHashHelper;
import com.ludovic.vimont.Location;

public class Enregistrement {
	private ArrayList<Double> region;
	private String nom;
	private LocalDate date_enregistrement;
	private int nb;
	
	public Enregistrement(JSONArray region,String nom, LocalDate date_enregistrement,int nb) {
		this.region=new ArrayList();
		int taille=region.length();
		for (int i=0;i<taille;i++) {
			this.region.add(region.getDouble(i));
		}
		this.nom=nom;
		this.date_enregistrement=date_enregistrement;
		this.nb=nb;
	}
	
	
	public ArrayList<Double> get_region() {
		return region;
	}
	
	public String get_nom() {
		return nom;
	}
	
	public LocalDate get_date() {
		return date_enregistrement;
	}
	
	@Override
	public String toString() {
		return "C'est un "+nom+", qui a été enregistré le "+date_enregistrement.toString()+", dans la region: "+region.toString()+"\n";
	}
}
