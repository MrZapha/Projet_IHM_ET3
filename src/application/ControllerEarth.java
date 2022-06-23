package application;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import com.interactivemesh.jfx.importer.ImportException;
import com.interactivemesh.jfx.importer.obj.ObjModelImporter;
import com.ludovic.vimont.GeoHashHelper;
import com.ludovic.vimont.Location;

import Donnees.Donne;
import application.Model;
import Donnees.ListSignalement;
import Donnees.Signalement;
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
	
	private static AutoCompletionBinding<String> auto;
	
	@FXML
	private TextField txtLocaGeo;
	
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
        txtPreciGeo.setText("3");
        Model.firstDraw(earth,txtEspece,d);
        
        // Create scene
        // ...
        SubScene subscene = new SubScene(root3D,500,580,true,SceneAntialiasing.BALANCED);
        subscene.setCamera(camera);
        subscene.setFill(Color.GREY);
        pane3D.getChildren().addAll(subscene);
        
        //Auto complete
        //auto = TextFields.bindAutoCompletion(txtEspece, "");
        txtEspece.setOnKeyReleased(new EventHandler<KeyEvent>() {
        	@Override
        	public void handle(KeyEvent event) {
        		if(txtEspece.getLength()>3) {
        			ObservableList<String> items = FXCollections.observableArrayList(Donne.completeSpecies(txtEspece.getText()));
        			//auto.dispose();
        			//auto = TextFields.bindAutoCompletion(txtEspece, items);	
        			labelEspece.setText("");
        		
        			if (items.size() == 0 && txtEspece.getLength() > 0 ) {
        				labelEspece.setText("Aucune espèce trouvée.");
        			}
        		}
        	}
        });
        
        listEspece.setOnMouseClicked(new EventHandler<MouseEvent>() {
		    @Override
		    public void handle(MouseEvent mouseEvent) {
		        if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
		            if(mouseEvent.getClickCount() == 2){
		            	String espece = listEspece.getSelectionModel().getSelectedItem();
		            	txtEspece.setText(espece);
		            }
		        }
		    }
		});
        
        btnValider.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent ae) {
            	//auto.dispose();
            	//On veut que l'espèce existe donc label d'alerte vide
                if (labelEspece.getText().length()==0) {
                	//Précision GeoHash par défaut, on n'empêche pas l'utilisateur d'agir
                	if(txtPreciGeo.getText().length()==0) {
                		txtPreciGeo.setText("3");
                	}
                	Donne d = Donne.donne_From_URL(txtEspece.getText(),Integer.valueOf(txtPreciGeo.getText()));
                	earth.getChildren().subList(1, earth.getChildren().size()).clear();
                	Model.drawHistogram(earth,d,Integer.valueOf(txtPreciGeo.getText()));
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
        		
                //TODO Mettre espèces du GeoHash dans la listView et dans le TreeView
        		if(txtPreciGeo.getText().length()==0) {
        			txtPreciGeo.setText("3");
        		}
        		String txt = GeoHashHelper.getGeohash(loc,Integer.valueOf(txtPreciGeo.getText()));
        		txtLocaGeo.setText(txt);
        		
        		ListSignalement signalements = ListSignalement.set_Liste_Espece(txtLocaGeo.getText());
        		
        		ArrayList<String> listEspSignal = signalements.get_Liste_Espece();
        		ObservableList<String> itemsListView = FXCollections.observableArrayList(listEspSignal);
        		listEspece.setItems(itemsListView);
        		
        		ArrayList<Signalement> listSignal = signalements.get_List_Signalement();
        		
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
