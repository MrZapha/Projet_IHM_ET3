package Donnees;

import java.util.Date;
import com.ludovic.vimont.GeoHashHelper;
import com.ludovic.vimont.Location;

public class Enregistrement {
	private String region;
	private String nom;
	private Date date_enregistrement;
	
	public Enregistrement(String region,String nom, Date date_enregistrement) {
		this.region=region;
		this.nom=nom;
		this.date_enregistrement=date_enregistrement;
	}
	
	
	public String get_region() {
		return region;
	}
	
	public String get_nom() {
		return nom;
	}
	
	public Date get_date() {
		return date_enregistrement;
	}
	
	@Override
	public String toString() {
		return "C'est un "+nom+", qui a été enregistré le "+date_enregistrement.toString()+", dans la region: "+GeoHashHelper.getLocation(region)+".";
	}
	
	public static void main(String[] args) {
		Location loc=new Location("selectedGeoHash",20,60);
		Enregistrement e=new Enregistrement(GeoHashHelper.getGeohash(loc),"dauphin",new Date());
		System.out.println(e);
		System.out.print(e.get_region());
	}
}
