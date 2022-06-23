package Donnees;

import java.util.ArrayList;
import java.time.LocalDate;
import org.json.JSONArray;
import javafx.geometry.Point2D;

public class Enregistrement {
	private ArrayList<Point2D> region;
	private String nom;
	private int nb;
	private LocalDate date_debut;
	private LocalDate date_fin;
	
	public Enregistrement(JSONArray region,String nom, int nb,LocalDate date_debut,LocalDate date_fin) {
		this.region=new ArrayList<Point2D>();
		int taille=region.length();
		for (int i=0;i<taille;i++) {
			this.region.add(new Point2D(region.getJSONArray(i).getDouble(0),region.getJSONArray(i).getDouble(1)));
		}
		this.nom=nom;
		this.nb=nb;
		this.date_debut=date_debut;
		this.date_fin=date_fin;
	}
	
	public Enregistrement(JSONArray region,String nom, int nb) {
		this(region,nom,nb,LocalDate.now(),LocalDate.now());
	}
	
	public ArrayList<Point2D> get_region() {
		return region;
	}
	
	public String get_nom() {
		return nom;
	}
	
	public int get_nombre() {
		return nb;
	}
	
	public LocalDate get_date_debut() {
		return date_debut;
	}
	
	public LocalDate get_date_fin() {
		return date_fin;
	}
	
	@Override
	public String toString() {
		return "C'est "+nb +" "+nom+", qui a été enregistré dans la region: "+region.toString()+"\n";
	}
}
