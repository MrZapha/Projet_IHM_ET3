package donnees;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.function.Consumer;

import com.ludovic.vimont.GeoHashHelper;
import com.ludovic.vimont.Location;

public class Donne {
	private int s=0;
	private ArrayList<Enregistrement> list;
	
	public Donne() {
		list=new ArrayList<Enregistrement>();
	}
	
	public void add_Enregistrement(Enregistrement e) {
		list.add(e);
	}
	
	int nb_signalement_region(String nom,String region) {
		s=0;
		Consumer<Enregistrement> conter_espece = new Consumer<Enregistrement>() {
			@Override
			public void accept(Enregistrement t) {
				if (t.get_nom().equals(nom) && t.get_region().equals(region)) {
					s++;
				}
				
			};
        };
		
		list.forEach(conter_espece);
		return s;
	}
	
	public int nb_signalement_region(String nom,Double longitude,Double latitude) {
		return nb_signalement_region(nom,GeoHashHelper.getGeohash(new Location("",longitude,latitude)));
	}
	
	public static Donne initialize() {
		Donne d=new Donne();
		
		return d;
	}
	
	public static void main(String args[]) {
		Donne d=new Donne();
		Enregistrement e=new Enregistrement(20.0,30.0,"Dolphin",LocalDate.now());
		d.add_Enregistrement(e);
		Enregistrement f=new Enregistrement(GeoHashHelper.getGeohash(new Location("dolphin",40,30)),"Dolphin",LocalDate.now());
		d.add_Enregistrement(f);
		Enregistrement g=new Enregistrement(GeoHashHelper.getGeohash(new Location("dolphin",20,30)),"Dolphin",LocalDate.now());
		d.add_Enregistrement(g);
		System.out.println(d.nb_signalement_region("Dolphin",20.0,30.0));
		System.out.println(d.list);
	}
}
