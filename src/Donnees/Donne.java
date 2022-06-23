package Donnees;

import java.io.StringReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.function.Consumer;
import org.json.JSONArray;
import org.json.JSONObject;
import com.ludovic.vimont.GeoHashHelper;
import com.ludovic.vimont.Location;


import Json.Json;

/**
 * La classe permettant d'avoir une liste d'Enregistrement et des fonctions pour l'utiliser
 * @author Julian COYNEL
 *
 */
public class Donne {
	/**
	 * La liste des Enregistrements
	 */
	private ArrayList<Enregistrement> list;
	
	/**
	 * La méthode permetant d'initialiser la liste des Enregistrements
	 */
	Donne() {
		list=new ArrayList<Enregistrement>();
	}
	
	/**
	 * La fonction permetant d'ajouter un Enregistrement a la list
	 * @param e L'Enregistrement a ajouté a la list
	 */
	private void add_Enregistrement(Enregistrement e) {
		list.add(e);
	}
	
	
	/**
	 *  La fonction permetant d'ajouter les Enregistrements de'une requête Json entre une date de début et de fin 
	 * @param jsonRoot La requête Json contenant les Enregistrements
	 * @param nom le nom de l'espèce enregistré
	 * @param date_debut la date de début de l'Enregistrement
	 * @param date_fin la date de fin de l'Enregistrement
	 */
	private void add_Enregistrement(JSONObject jsonRoot,String nom,LocalDate date_debut,LocalDate date_fin) {
		if(!jsonRoot.isNull("features")) {
			JSONArray resultatRecherche = jsonRoot.getJSONArray("features");
			int taille=resultatRecherche.length();
			for(int i=0;i<taille;i++) {
				JSONObject article = resultatRecherche.getJSONObject(i);
				int nb=0;
				if(!article.isNull("properties")) {
					nb=article.getJSONObject("properties").getInt("n");
				}
				if(!article.isNull("geometry")) {
					JSONObject geometry =article.getJSONObject("geometry");
					if(!geometry.isNull("coordinates")) {
						Enregistrement e=new Enregistrement(geometry.getJSONArray("coordinates").getJSONArray(0),nom,nb,date_debut,date_fin);
						this.add_Enregistrement(e);
						return;
					}
				}
				System.out.println("L'espèce a été trouvée null-part");
			}
			return;
		}
		System.out.println("Aucune espèce trouvée");
	}
	
	
	/**
	 * La fonction permetant d'ajouter les Enregistrements de'une requête Json
	 * @param jsonRoot La requête Json contenant les Enregistrements
	 * @param nom le nom de l'espèce enregistré
	 */
	private void add_Enregistrement(JSONObject jsonRoot,String nom) {
		this.add_Enregistrement(jsonRoot, nom, LocalDate.now(), LocalDate.now());
	}
	
	/**
	 * La fonction permetant d'initaliser l'application avec soit le fichier Selachii.json soit Delphinidae.json avec 50% de chance pour les deux
	 * @return Donne les Enregistrements de selachii ou de delphinidae avec 50% de chance pour les deux
	 */
	public static Donne init() {
		double nb;
		nb = Math.random();
		if (nb>=0.5) {
			return init_selachii();
		}
		else {
			return init_delphinidae();
		}
	}
	
	/**
	 * La fonction permetant d'initaliser l'application avec le fichier Delphinidae.json
	 * @return Donne les Enregistrements de delphinidae
	 */
	private static Donne init_delphinidae() {
		Donne d=new Donne();
		JSONObject jsonRoot=Json.init_delphinidae();
		d.add_Enregistrement(jsonRoot,"Delphinidae");
		return d;
	}
	
	/**
	 * La fonction permetant d'initaliser l'application avec le fichier Selachii.json
	 * @return Donne les Enregistrements de selachii
	 */
	private static Donne init_selachii() {
		Donne d=new Donne();
		JSONObject jsonRoot=Json.init_selachii();
		d.add_Enregistrement(jsonRoot,"Selachii");
		return d;
	}
	
	/**
	 * La fonction permetant de faire une requête a partir du nom d'une espèce et d'une précision GeoHash
	 * @param nom le nom dd l'espèce recherché
	 * @param nb_caractere la précision GeoHash
	 * @return Donne les donnes receuillis de la requête
	 */
	public static Donne donne_From_URL(String nom,int nb_caractere) {
		Donne d=new Donne();
		JSONObject jsonRoot=Json.readJsonWithGeoHash(nom, nb_caractere);
		d.add_Enregistrement(jsonRoot,nom);
		return d;
	}
	
	/**
	 * La fonction permetant de faire une requête a partir du nom d'une espèce, d'une précision GeoHash entre deux date
	 * @param nom le nom de l'espèce
	 * @param nb_caractere la précision GeoHash
	 * @param date_debut la date du début 
	 * @param date_fin la date de fin
	 * @return Donne les donnes receuillis de la requête
	 */
	public static Donne donne_From_URL_With_Date(String nom,int nb_caractere,LocalDate date_debut,LocalDate date_fin) {
		Donne d=new Donne();
		JSONObject jsonRoot=Json.readJsonWithGeoHashAndTime(nom, nb_caractere, date_debut, date_fin);
		d.add_Enregistrement(jsonRoot,nom,date_debut,date_fin);
		return d;
	}
	
	/**
	 * La fonction permetant de faire une requête a partir du nom d'une espèce, d'une précision GeoHash et d'un intervalle de temps
	 * @param nom le nom de l'espèce
	 * @param nb_caractere la précision GeoHash
	 * @param date_debut la date du début de l'intervalle de temps
	 * @param intervalleAnnee le nombre d'année qu'on avance 
	 * @param nb_intervalle le nobmre de fois qu'on ajoute intervalleAnnee
	 * @return Donne les donnes receuillis de la requête
	 */
	public static Donne donne_From_URL_With_Time_Interval(String nom,int nb_caractere,LocalDate date_debut,long intervalleAnnee,int nb_intervalle) {
		Donne d=new Donne();
		for(int i=1;i<=nb_intervalle;i++) {
			LocalDate date_fin=date_debut.plusYears(intervalleAnnee);
			JSONObject jsonRoot=Json.readJsonWithGeoHashAndTime(nom, nb_caractere, date_debut, date_fin);
			d.add_Enregistrement(jsonRoot,nom,date_debut,date_fin);
			date_debut=date_debut.plusYears(intervalleAnnee);
		}
		return d;
	}
	
	/**
	 * La fonction qui permet de recupere parmis une instance de Donne quelles sont les Enregistrements faisant partis de l'intervalle de temps passer en parametre
	 * @param date_debut la date du début de l'intervalle
	 * @param date_fin la date de fin de l'intervalle
	 * @return Donne les donnes receuillis par la fonction
	 */
	public Donne get_donne_with_this_intervalle(LocalDate date_debut,LocalDate date_fin) {
		Donne d=new Donne();
		for(Enregistrement e: list) {
			LocalDate d_deb=e.get_date_debut();
			LocalDate d_fin=e.get_date_fin();
			if((d_deb.isAfter(date_debut) || d_deb.isEqual(date_debut)) && (d_fin.isBefore(date_fin) || d_fin.isEqual(date_fin)) ) {
				d.add_Enregistrement(e);
			}
		}
		return d;
	}
	
	/**
	 * La fonction permetant l'autocompletion
	 * @param texte le texte a autocompléter
	 * @return ArrayList<String> la liste des noms d'espèces pouvant être rentrée
	 */
	public static ArrayList<String> completeSpecies(String texte){
		JSONArray jsonRoot=Json.completeSpecies(texte);
		ArrayList<String> listCompletion=new ArrayList<String>();
		int taille=jsonRoot.length();
		for(int i=0;i<taille;i++) {
			JSONObject espece=jsonRoot.getJSONObject(i);
			listCompletion.add(espece.getString("scientificName"));
		}	
		return listCompletion;
	}
	
	/**
	 * La fonction permettant de renvoyer la liste des Enregistrements
	 * @return ArrayList<Enregistrement> la liste des Enregistrements
	 */
	public ArrayList<Enregistrement> get_list() {
		return list;
	}
	
}
