package Donnees;

/**
 * La classe qui permet d'avoir les infots d'une espèce signalée
 * @author Julian COYNEL
 *
 */
public class Signalement {
	/**
	 * Le nom scientifique de l'espèce
	 */
	private String scientificName;
	/**
	 * L'ordre de lespèce
	 */
	private String order;
	/**
	 * La superclass de l'espèce
	 */
	private String superclass;
	/**
	 * La personne qui a enregistré le signalement
	 */
	private String recordedBy;
	/*
	 * Le nom de l'espèce
	 */
	private String species;
	
	/**
	 * La méthode pour construire un signalement
	 * @param scientificName Le nom scientifique de l'espèce signalée
	 * @param order L'ordre de lespèce signalée
	 * @param superclass La superclass de l'espèce signalée
	 * @param recordedBy La personne qui a enregistré le signalement
	 * @param species Le nom de l'espèce signalée
	 */
	public Signalement (String scientificName,String order, String superclass,String recordedBy, String species) {
		this.scientificName=scientificName;
		this.order=order;
		this.superclass=superclass;
		this.recordedBy=recordedBy;
		this.species=species;
	}
	
	/**
	 * La fonction renvoyant Le nom scientifique de l'espèce signalée
	 * @return scientificName Le nom scientifique de l'espèce signalée
	 */
	public String get_scientificName() {
		return this.scientificName;
	}
	
	/**
	 * La fonction renvoyant L'ordre de lespèce signalée
	 * @return order L'ordre de lespèce signalée
	 */
	public String get_order() {
		return this.order;
	}
	
	/**
	 * La fonction renvoyant La superclass de l'espèce signalée
	 * @return superclass La superclass de l'espèce signalée
	 */
	public String get_superclass() {
		return this.superclass;
	}
	
	/**
	 * La fonction renvoyant La personne qui a enregistré le signalement
	 * @return recordedBy La personne qui a enregistré le signalement
	 */
	public String get_recordedBy() {
		return this.recordedBy;
	}
	
	/**
	 * La fonction renvoyant Le nom de l'espèce signalée
	 * @return species Le nom de l'espèce signalée
	 */
	public String get_species() {
		return this.species;
	}
}
