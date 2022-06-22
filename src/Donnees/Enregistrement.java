package Donnees;

import java.time.LocalDate;
import java.util.ArrayList;

import org.json.JSONArray;
import javafx.geometry.Point2D;
import com.ludovic.vimont.GeoHashHelper;
import com.ludovic.vimont.Location;

public class Enregistrement {
	private ArrayList<Point2D> region;
	private String nom;
	private LocalDate date_enregistrement;
	private int nb;
	
	public Enregistrement(JSONArray region,String nom, LocalDate date_enregistrement,int nb) {
		this.region=new ArrayList<Point2D>();
		int taille=region.length();
		for (int i=0;i<taille;i++) {
			this.region.add(new Point2D(region.getJSONArray(i).getDouble(0),region.getJSONArray(i).getDouble(1)));
		}
		this.nom=nom;
		this.date_enregistrement=date_enregistrement;
		this.nb=nb;
	}
	
	
	public ArrayList<Point2D> get_region() {
		return region;
	}
	
	public String get_nom() {
		return nom;
	}
	
	public LocalDate get_date() {
		return date_enregistrement;
	}
	
	public int get_nombre() {
		return nb;
	}
	
	@Override
	public String toString() {
		return "C'est "+nb +" "+nom+", qui a été enregistré le "+date_enregistrement.toString()+", dans la region: "+region.toString()+"\n";
	}
}
