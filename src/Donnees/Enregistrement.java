package Donnees;

import java.util.ArrayList;

import org.json.JSONArray;
import javafx.geometry.Point2D;

public class Enregistrement {
	private ArrayList<Point2D> region;
	private String nom;
	private int nb;
	
	public Enregistrement(JSONArray region,String nom, int nb) {
		this.region=new ArrayList<Point2D>();
		int taille=region.length();
		for (int i=0;i<taille;i++) {
			this.region.add(new Point2D(region.getJSONArray(i).getDouble(0),region.getJSONArray(i).getDouble(1)));
		}
		this.nom=nom;
		this.nb=nb;
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
	
	@Override
	public String toString() {
		return "C'est "+nb +" "+nom+", qui a été enregistré dans la region: "+region.toString()+"\n";
	}
}
