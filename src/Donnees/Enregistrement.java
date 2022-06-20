package Donnees;

import java.time.LocalDate;
import com.ludovic.vimont.GeoHashHelper;
import com.ludovic.vimont.Location;

public class Enregistrement {
	private String region;
	private String nom;
	private LocalDate date_enregistrement;
	
	Enregistrement(String region,String nom, LocalDate date_enregistrement) {
		this.region=region;
		this.nom=nom;
		this.date_enregistrement=date_enregistrement;
	}
	
	public Enregistrement(Double latitude,Double longitude,String nom, LocalDate date_enregistrement) {
		this(GeoHashHelper.getGeohash(new Location("",20,30)),nom,date_enregistrement);
	}
	
	public String get_region() {
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
		return "C'est un "+nom+", qui a été enregistré le "+date_enregistrement.toString()+", dans la region: "+GeoHashHelper.getLocation(region)+"\n";
	}
}
