package Donnees;

/**
 * La classe qui permet d'avoir les infots d'une esp�ce signal�e
 * @author Julian COYNEL
 *
 */
public class Signalement {
	/**
	 * Le nom scientifique de l'esp�ce
	 */
	private String scientificName;
	/**
	 * L'ordre de lesp�ce
	 */
	private String order;
	/**
	 * La superclass de l'esp�ce
	 */
	private String superclass;
	/**
	 * La personne qui a enregistr� le signalement
	 */
	private String recordedBy;
	/*
	 * Le nom de l'esp�ce
	 */
	private String species;
	
	/**
	 * La m�thode pour construire un signalement
	 * @param scientificName Le nom scientifique de l'esp�ce signal�e
	 * @param order L'ordre de lesp�ce signal�e
	 * @param superclass La superclass de l'esp�ce signal�e
	 * @param recordedBy La personne qui a enregistr� le signalement
	 * @param species Le nom de l'esp�ce signal�e
	 */
	public Signalement (String scientificName,String order, String superclass,String recordedBy, String species) {
		this.scientificName=scientificName;
		this.order=order;
		this.superclass=superclass;
		this.recordedBy=recordedBy;
		this.species=species;
	}
	
	/**
	 * La fonction renvoyant Le nom scientifique de l'esp�ce signal�e
	 * @return scientificName Le nom scientifique de l'esp�ce signal�e
	 */
	public String get_scientificName() {
		return this.scientificName;
	}
	
	/**
	 * La fonction renvoyant L'ordre de lesp�ce signal�e
	 * @return order L'ordre de lesp�ce signal�e
	 */
	public String get_order() {
		return this.order;
	}
	
	/**
	 * La fonction renvoyant La superclass de l'esp�ce signal�e
	 * @return superclass La superclass de l'esp�ce signal�e
	 */
	public String get_superclass() {
		return this.superclass;
	}
	
	/**
	 * La fonction renvoyant La personne qui a enregistr� le signalement
	 * @return recordedBy La personne qui a enregistr� le signalement
	 */
	public String get_recordedBy() {
		return this.recordedBy;
	}
	
	/**
	 * La fonction renvoyant Le nom de l'esp�ce signal�e
	 * @return species Le nom de l'esp�ce signal�e
	 */
	public String get_species() {
		return this.species;
	}
}
