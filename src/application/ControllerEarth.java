package application;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.interactivemesh.jfx.importer.ImportException;
import com.interactivemesh.jfx.importer.obj.ObjModelImporter;
import com.ludovic.vimont.GeoHashHelper;
import com.ludovic.vimont.Location;

import Donnees.Donne;
import Donnees.ListSignalement;
import Donnees.Signalement;
import javafx.animation.AnimationTimer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.AmbientLight;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.PickResult;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.MeshView;

public class ControllerEarth implements Initializable {
	@FXML
	private Pane pane3D;
	
	@FXML
	private TextField txtEspece;
	
	@FXML
	private Label txtLocaGeo;
	
	@FXML
	private TextField txtPreciGeo;
	
	@FXML
	private DatePicker dateDebut;
	
	@FXML
	private DatePicker dateFin;
	
	@FXML
	private Button btnValider;
	
	@FXML
	private Button btnLecture;
	
	@FXML
	private Button btnPause;
	
	@FXML
	private Button btnStop;
	
	@FXML
	private TreeView<String> treeSignalement;
	
	@FXML
	private ListView<String> listEspece;
	
	@FXML
	private Label labelEspece;
	
	@FXML
	private Label label1;
	
	@FXML
	private Label label2;
	
	@FXML
	private Label label3;
	
	@FXML
	private Label label4;
	
	@FXML
	private Label label5;
	
	@FXML
	private Label label6;
	
	@FXML
	private Label label7;
	
	private static int pasActuel;
	private static boolean stop = false;
	private static boolean pause = false;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
        //Create a Pane et graph scene root for the 3D content
        Group root3D = new Group();
        
        // Load geometry
        ObjModelImporter objImporter = new ObjModelImporter();
        try {
        	URL modelURL = this.getClass().getResource("Earth/earth.obj");
        	objImporter.read(modelURL);
        } catch(ImportException e) {
        	System.out.println(e.getMessage());
        }
        MeshView[] meshViews = objImporter.getImport();
        Group earth = new Group(meshViews);
        
        root3D.getChildren().add(earth);

        // Add a camera group
        PerspectiveCamera camera = new PerspectiveCamera(true);
        new CameraManager(camera, pane3D, root3D);

        // Add point light
        PointLight light = new PointLight(Color.WHITE);
        light.setTranslateX(-180);
        light.setTranslateY(-90);
        light.setTranslateZ(-120);
        light.getScope().addAll(root3D);
        root3D.getChildren().add(light);
        
        // Add ambient light
        AmbientLight ambientLight = new AmbientLight(Color.WHITE);
        ambientLight.getScope().addAll(root3D);
        root3D.getChildren().add(ambientLight);
        
        //Drawing from file, either Delphinidae.json or Selachii.json with a 50/50% chance
        Donne d = Donne.init();
        Label[] labelLegende = {label1,label2,label3,label4,label5,label6,label7};
        txtPreciGeo.setText("3");
        Model.firstDraw(earth,d,labelLegende);
        txtEspece.setText(d.get_list().get(0).get_nom());
        
        // Create scene
        // ...
        SubScene subscene = new SubScene(root3D,500,580,true,SceneAntialiasing.BALANCED);
        subscene.setCamera(camera);
        subscene.setFill(Color.GREY);
        pane3D.getChildren().addAll(subscene);
        
        //Text species input
        txtEspece.setOnKeyReleased(new EventHandler<KeyEvent>() {
        	@Override
        	public void handle(KeyEvent event) {
        		stop=false;
        		if(txtEspece.getLength()>3) {
        			ObservableList<String> items = FXCollections.observableArrayList(Donne.completeSpecies(txtEspece.getText()));
        			labelEspece.setText("");
        		
        			if (items.size() == 0 && txtEspece.getLength() > 0 ) {
        				labelEspece.setTextFill(Color.RED);
        				labelEspece.setText("Aucune espèce trouvée.");
        			}
        		}
        	}
        });
        
        //Recherche et dessin des signalements de l'espèce
        btnValider.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent ae) {
            	//On veut que l'espèce existe donc label d'alerte vide
                if (labelEspece.getText().length()==0) {
                	//Précision GeoHash par défaut, on n'empêche pas l'utilisateur d'agir
                	if(txtPreciGeo.getText().length()==0) {
                		txtPreciGeo.setText("3");
                	}
                	Donne d;
                	if(dateDebut.getValue()!=null && dateFin.getValue()!=null) {
                		d = Donne.donne_From_URL_With_Date(txtEspece.getText(),Integer.valueOf(txtPreciGeo.getText()),dateDebut.getValue(),dateFin.getValue());
                	}else {
                		d = Donne.donne_From_URL(txtEspece.getText(),Integer.valueOf(txtPreciGeo.getText()));
                	}
                	
                	if(d.get_list().size()>0) {
                		earth.getChildren().subList(1, earth.getChildren().size()).clear();
                		Model.drawHistogram(earth,d,Integer.valueOf(txtPreciGeo.getText()),labelLegende);
                	}else {
                		labelEspece.setTextFill(Color.RED);
        				labelEspece.setText("Aucune/Plusieurs espèce(s) trouvée(s).");
                	}
                }
            }
        });
        
        //Lecture animation si les dates sont précisées
        btnLecture.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent ae) {
            	if(dateDebut.getValue()!=null && dateFin.getValue()!=null && labelEspece.getText().length()==0 && txtEspece.getLength()!=0) {
            		//Valeur par défaut pas de blocage
            		if(txtPreciGeo.getText().length()==0) {
                		txtPreciGeo.setText("3");
                	}
            		pause=false;
            		stop=false;
            		earth.getChildren().subList(1, earth.getChildren().size()).clear();
            		int nbPas = (dateFin.getValue().getYear()-dateDebut.getValue().getYear()) / 5; 
            		pasActuel = 0;
            		Donne leg = Donne.donne_From_URL_With_Date(txtEspece.getText(), Integer.valueOf(txtPreciGeo.getText()), dateDebut.getValue(), dateFin.getValue());
            		int[] tableauEchelle = Model.updateLegend(leg.get_list(),labelLegende);
            		
            		Donne d = Donne.donne_From_URL_With_Time_Interval(txtEspece.getText(),Integer.valueOf(txtPreciGeo.getText()),dateDebut.getValue(),5,nbPas);
            		
            		final long startNanoTime = System.nanoTime();
            		AnimationTimer timer = new AnimationTimer() {
	                	
            			int annee1 = dateDebut.getValue().getYear()+5*pasActuel;
	        			int annee2 = annee1+5;
	        			
	        			LocalDate y1 = LocalDate.of(annee1,dateDebut.getValue().getMonthValue(),dateDebut.getValue().getDayOfMonth());
	        			LocalDate y2 = LocalDate.of(annee2,dateDebut.getValue().getMonthValue(),dateDebut.getValue().getDayOfMonth());        			
	                	
	                	public void handle(long currentNanoTime) {
	                		double t = (currentNanoTime - startNanoTime) / 1000000000.0;
	                		System.out.println(t);

	    	        		if(annee2>dateFin.getValue().getYear() | pause==true | stop==true) {
	    	        			if(annee2>dateFin.getValue().getYear()) {
	    	        				pasActuel=0;
	    	        			}
	    	        			this.stop();
	    	        		}else if(t%6<=0.015) {
	    	        			Donne dIntervalle = d.get_donne_with_this_intervalle(y1,y2);
		                		Model.drawHistogram(earth, dIntervalle, Integer.valueOf(txtPreciGeo.getText()), labelLegende,tableauEchelle);
		                		annee1+=5;
		    	        		annee2+=5;
		    	        		y1 = LocalDate.of(annee1,dateDebut.getValue().getMonthValue(),dateDebut.getValue().getDayOfMonth());
		    	        		y2 = LocalDate.of(annee2,dateDebut.getValue().getMonthValue(),dateDebut.getValue().getDayOfMonth());
		    	        		pasActuel+=1;
	    	        		}
	    	        		
	                	}
            		};
            		timer.start();
            	}
            }     	
        });
        
        //Pause animation
        btnPause.setOnAction(new EventHandler<ActionEvent>() {
        	@Override
        	public void handle(ActionEvent event) {	
        		pause=true;
        	}
        });
        
        //Arrêt animation
        btnStop.setOnAction(new EventHandler<ActionEvent>() {
        	@Override
        	public void handle(ActionEvent event) {	
        		stop=true;
        		pasActuel=0;
        		earth.getChildren().subList(1, earth.getChildren().size()).clear();
        		txtEspece.setText("");
        		dateDebut.getEditor().clear();
        		dateFin.getEditor().clear();
        	}
        });
        
        //Interaction liste d'espèces présentes sur un geohash
        listEspece.setOnMouseClicked(new EventHandler<MouseEvent>() {
		    @Override
		    public void handle(MouseEvent mouseEvent) {
		        if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
		            if(mouseEvent.getClickCount() == 2){
		            	String species = listEspece.getSelectionModel().getSelectedItem();
		        		txtEspece.setText(species);
		        		labelEspece.setText("");
		            }
		        }
		    }
		});
        
        //Clickable earth to get species
        subscene.addEventHandler(MouseEvent.ANY, event-> {
        	if(event.getEventType() == MouseEvent.MOUSE_PRESSED && event.getClickCount()==2) {
        		PickResult pickResult = event.getPickResult();
        		Point3D spaceCoord = pickResult.getIntersectedPoint();
        		
        		Point2D latLon = Model.SpaceCoordToGeoCoord(spaceCoord);
        		double latCursor = latLon.getX();
        		double lonCursor = latLon.getY();
        		Location loc = new Location("selectedGeoHash",latCursor,lonCursor);
        		
        		if(txtPreciGeo.getText().length()==0) {
        			txtPreciGeo.setText("3");
        		}
        		String txt = GeoHashHelper.getGeohash(loc,Integer.valueOf(txtPreciGeo.getText()));
        		txtLocaGeo.setText(txt);
        		
        		ListSignalement signalements = ListSignalement.set_Liste_Espece(txtLocaGeo.getText());
        		
        		//ListView pour les espèces avec lesquelles on peut intéragir
        		ArrayList<String> listEspSignal = signalements.get_Liste_Espece();
        		ObservableList<String> itemsListView = FXCollections.observableArrayList(listEspSignal);
        		listEspece.setItems(itemsListView);
        		
        		ArrayList<Signalement> listSignal = signalements.get_List_Signalement();
        		
        		//TreeView pour les champs
        		TreeItem<String> root = new TreeItem<String>("root");
        		root.setExpanded(true);
        		treeSignalement.setRoot(root);
        		treeSignalement.setShowRoot(false);
        		
        		for(int i=0;i<listSignal.size();i++) {
        			Signalement listSignalIndex = listSignal.get(i);
        			TreeItem<String> name = new TreeItem<String>("scientificName: "+listSignalIndex.get_scientificName());
        			TreeItem<String> order = new TreeItem<String>("order: "+listSignalIndex.get_order());
        			TreeItem<String> superclass = new TreeItem<String>("superclass: "+listSignalIndex.get_superclass());
        			TreeItem<String> recordedBy = new TreeItem<String>("recordedBy: "+listSignalIndex.get_recordedBy());
        			TreeItem<String> species = new TreeItem<String>("species: "+listSignalIndex.get_species());
        			name.getChildren().addAll(order,superclass,recordedBy,species);
        			root.getChildren().addAll(name);
        		}
        		
        	}
        });
        
        new CameraManager(camera,pane3D,root3D);
        
	}
	

}
