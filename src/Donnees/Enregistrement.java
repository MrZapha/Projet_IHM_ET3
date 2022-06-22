package Donnees;

import org.json.JSONArray;

public class Enregistrement {
	private double[] region;
	private String nom;
	private int nb;
	
	public Enregistrement(JSONArray region,String nom, int nb) {
		int taille=region.length();
		this.region=new double[2*taille];
		for (int i=0;i<taille;i+=2) {
			this.region[i]=region.getJSONArray(i).getDouble(0);
			this.region[i+1]=region.getJSONArray(i).getDouble(1);
		}
		this.nom=nom;
		this.nb=nb;
	}
	
	
	public double[] get_region() {
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
