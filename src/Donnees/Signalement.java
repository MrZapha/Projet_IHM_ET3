package Donnees;

public class Signalement {
	private String scientificName;
	private String order;
	private String superclass;
	private String recordedBy;
	private String species;
	
	public Signalement (String scientificName,String order, String superclass,String recordedBy, String species) {
		this.scientificName=scientificName;
		this.order=order;
		this.superclass=superclass;
		this.recordedBy=recordedBy;
		this.species=species;
	}
	
	public String get_scientificName() {
		return this.scientificName;
	}
	
	public String get_order() {
		return this.order;
	}
	
	public String get_superclass() {
		return this.superclass;
	}
	
	public String get_recordedBy() {
		return this.recordedBy;
	}
	
	public String get_species() {
		return this.species;
	}
}
