package Donnees;

import java.util.ArrayList;
import java.time.LocalDate;
import org.json.JSONArray;
import javafx.geometry.Point2D;

/**
 * La classe d'un enregistrement d'une esp�ce
 * @author julia
 *
 */
public class Enregistrement {
	/**
	 * La r�gion ou l'enregistrement a �t� �ffectu�
	 */
	private ArrayList<Point2D> region;
	/**
	 * Le nom de l'esp�ce enregistr�e
	 */
	private String nom;
	/**
	 * Le nombre d'individu de l'esp�ce
	 */
	private int nb;
	/**
	 * La date de d�but de l'enregistrement si il y en a une sinon la date ou utilise l'application
	 */
	private LocalDate date_debut;
	/**
	 * La date de fin de l'enregistrement si il y en a une sinon la date ou utilise l'application
	 */
	private LocalDate date_fin;
	
	/**
	 * La m�thode permetant de cr�er un enregistrement avec tous les informations de celui-ci
	 * @param region La r�gion ou l'enregistrement a �t� �ffectu�
	 * @param nom Le nom de l'esp�ce enregistr�e
	 * @param nb Le nombre d'individu de l'esp�ce
	 * @param date_debut La date de d�but de l'enregistrement
	 * @param date_fin La date de fin de l'enregistrement
	 */
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
	
	/**
	 * La m�thode permetant de cr�er un enregistrement si on a pas de date de d�but et de fin pour l'enregistrement
	 * @param region La r�gion ou l'enregistrement a �t� �ffectu�
	 * @param nom Le nom de l'esp�ce enregistr�e
	 * @param nb Le nombre d'individu de l'esp�ce
	 */
	public Enregistrement(JSONArray region,String nom, int nb) {
		this(region,nom,nb,LocalDate.now(),LocalDate.now());
	}
	
	/**
	 * La fonction qui permet d'avoir la r�gion ou l'esp�ce a �t� enregistr�e
	 * @return ArrayList<Point2D> La r�gion ou l'enregistrement a �t� �ffectu�
	 */
	public ArrayList<Point2D> get_region() {
		return region;
	}
	
	/**
	 * La fonction qui permet d'avoir le nom de l'esp�ce enregistr�e
	 * @return String Le nom de l'esp�ce enregistr�e
	 */
	public String get_nom() {
		return nom;
	}
	
	/**
	 * La fonction qui permet d'avoir le nombre d'individu de l'esp�ce
	 * @return int Le nombre d'individu de l'esp�ce
	 */
	public int get_nombre() {
		return nb;
	}
	
	/**
	 * La fonction qui permet d'avoir la date de d�but de l'enregistrement
	 * @return LocalDate La date de d�but de l'enregistrement
	 */
	public LocalDate get_date_debut() {
		return date_debut;
	}
	
	/**
	 * La fonction qui permet d'avoir la date de fin de l'enregistrement
	 * @return LocalDate La date de fin de l'enregistrement
	 */
	public LocalDate get_date_fin() {
		return date_fin;
	}
	
	@Override
	public String toString() {
		return "C'est "+nb +" "+nom+", qui a �t� enregistr� dans la region: "+region.toString()+"\n";
	}
}
