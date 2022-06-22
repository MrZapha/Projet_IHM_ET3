package application;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpClient.Version;
import java.net.http.HttpResponse.BodyHandlers;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;

import org.controlsfx.control.textfield.TextFields;

import com.interactivemesh.jfx.importer.ImportException;
import com.interactivemesh.jfx.importer.obj.ObjModelImporter;
import com.ludovic.vimont.GeoHashHelper;
import com.ludovic.vimont.Location;

import Donnees.Donne;
import Donnees.Enregistrement;
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
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.PickResult;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Sphere;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

public class ControllerEarth implements Initializable {
	@FXML
	private Pane pane3D;
	
	@FXML
	private TextField txtEspece;
	
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
	private TreeView treeSignalement;
	
	@FXML
	private ListView listEspece;
	
    private static final float TEXTURE_LAT_OFFSET = -0.2f;
    private static final float TEXTURE_LON_OFFSET = 2.8f;
    private static final float TEXTURE_OFFSET = 1.01f;
	
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
        
        //Draw from Json Delphinidae file
        Donne d = Donne.init();
        ArrayList<Enregistrement> registeredList = d.get_list();
        int max = registeredList.get(0).get_nombre();
        int[] tableauEchelle = new int[8];
        for(int i=0;i<8;i++) {
        	tableauEchelle[i] = 1+i*(max-1)/8;
        }
        for(int i=0;i<registeredList.size();i++) {
        	registeredList.get(i).get_region();
        	for(int j=0;j<5;j++) {
        		geoCoordTo3dCoord(float lat, float lon);
        	}
        }

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
        
        //AutoComplete from controlsFX
        //TextFields.bindAutoCompletion(txtEspece,);
        
        // Create scene
        // ...
        SubScene subscene = new SubScene(root3D,500,580,true,SceneAntialiasing.BALANCED);
        subscene.setCamera(camera);
        subscene.setFill(Color.GREY);
        pane3D.getChildren().addAll(subscene);
        
        subscene.addEventHandler(MouseEvent.ANY, event-> {
        	if(event.getEventType() == MouseEvent.MOUSE_PRESSED && event.getClickCount()==2) {
        		PickResult pickResult = event.getPickResult();
        		Point3D spaceCoord = pickResult.getIntersectedPoint();
        		//System.out.print(spaceCoord+"\n");
        		
        		Point2D latLon = SpaceCoordToGeoCoord(spaceCoord);
        		double latCursor = latLon.getX();
        		double lonCursor = latLon.getY();
        		Location loc = new Location("selectedGeoHash",latCursor,lonCursor);
        		System.out.print(GeoHashHelper.getGeohash(loc)+"\n");
        		
                Sphere sphere = new Sphere(0.05);
                final PhongMaterial blackMaterial = new PhongMaterial();
                blackMaterial.setDiffuseColor(Color.BLACK);
                blackMaterial.setSpecularColor(Color.BLACK);
                sphere.setMaterial(blackMaterial);
                            	
                sphere.setTranslateX(spaceCoord.getX());
                sphere.setTranslateY(spaceCoord.getY());
                sphere.setTranslateZ(spaceCoord.getZ());
                root3D.getChildren().add(sphere);
        	}
        });
        
        new CameraManager(camera,pane3D,root3D);
        
	}
	
    public static void circleEarth(Group parent) {
    	final PhongMaterial greenMaterial = new PhongMaterial();
        greenMaterial.setDiffuseColor(Color.rgb(0,150,0,0));
        greenMaterial.setSpecularColor(Color.GREEN);
        final PhongMaterial redMaterial = new PhongMaterial();
        redMaterial.setDiffuseColor(Color.rgb(150,0,0,0));
        redMaterial.setSpecularColor(Color.RED);
        boolean redOrGreen = true; //true = vert false = red
        for(float latitude=-90;latitude<90;latitude+=10) {
        	for(float longitude=-180;longitude<180;longitude+=10) {
        		Point3D TopRight = geoCoordTo3dCoord((float)latitude+10,(float)longitude+10).multiply(1.01);
        		Point3D TopLeft = geoCoordTo3dCoord((float)latitude+10,(float)longitude).multiply(1.01);
        		Point3D BotRight = geoCoordTo3dCoord((float)latitude,(float)longitude+10).multiply(1.01);
        		Point3D BotLeft = geoCoordTo3dCoord((float)latitude,(float)longitude).multiply(1.01);
        		if(redOrGreen) {
        			AddQuadrilateral(parent, TopRight, BotRight, BotLeft, TopLeft,greenMaterial);
        			redOrGreen = false;
        		}
        		else {
        			AddQuadrilateral(parent, TopRight, BotRight, BotLeft, TopLeft,redMaterial);
        			redOrGreen = true;
        		}
        	}
        	if(redOrGreen) {
        		redOrGreen = false;
        	}else {
        		redOrGreen = true;
        	}
        }		
    }


    // From Rahel Lüthy : https://netzwerg.ch/blog/2015/03/22/javafx-3d-line/
    public Cylinder createLine(Point3D origin, Point3D target) {
        Point3D yAxis = new Point3D(0, 1, 0);
        Point3D diff = target.subtract(origin);
        double height = diff.magnitude();

        Point3D mid = target.midpoint(origin);
        Translate moveToMidpoint = new Translate(mid.getX(), mid.getY(), mid.getZ());

        Point3D axisOfRotation = diff.crossProduct(yAxis);
        double angle = Math.acos(diff.normalize().dotProduct(yAxis));
        Rotate rotateAroundCenter = new Rotate(-Math.toDegrees(angle), axisOfRotation);

        Cylinder line = new Cylinder(0.01f, height);

        line.getTransforms().addAll(moveToMidpoint, rotateAroundCenter);

        return line;
    }

    public static Point3D geoCoordTo3dCoord(float lat, float lon) {
        float lat_cor = lat + TEXTURE_LAT_OFFSET;
        float lon_cor = lon + TEXTURE_LON_OFFSET;
        return new Point3D(
                -java.lang.Math.sin(java.lang.Math.toRadians(lon_cor))
                        * java.lang.Math.cos(java.lang.Math.toRadians(lat_cor)),
                -java.lang.Math.sin(java.lang.Math.toRadians(lat_cor)),
                java.lang.Math.cos(java.lang.Math.toRadians(lon_cor))
                        * java.lang.Math.cos(java.lang.Math.toRadians(lat_cor)));
    }
    
    public static Point2D SpaceCoordToGeoCoord(Point3D p) {
    	float lat = (float)(Math.asin(-p.getY() / TEXTURE_OFFSET));
    	float lon;
    	
    	if(p.getZ()<0) {
    		lon = 180 - (float)(Math.asin(-p.getX() / (TEXTURE_OFFSET
    				*Math.cos((Math.PI/180)
    				*(lat + TEXTURE_LAT_OFFSET))))*180 / Math.PI - TEXTURE_LON_OFFSET);
    	}else {
    		lon = (float) (Math.asin(-p.getX() / (TEXTURE_OFFSET * Math.cos((Math.PI / 180)
    				 * (lat + TEXTURE_LAT_OFFSET)))) * 180 / Math.PI - TEXTURE_LON_OFFSET);
    	}
    	return new Point2D(lat,lon);
    }
    
    public static void displayTown(Group parent, String name, float latitude, float longitude) {
    	Sphere sphere = new Sphere(0.01);
    	sphere.setId(name);
    	
    	Point3D sphere3D = geoCoordTo3dCoord(latitude,longitude);
    	
    	final PhongMaterial greenMaterial = new PhongMaterial();
        greenMaterial.setDiffuseColor(Color.GREEN);
        greenMaterial.setSpecularColor(Color.GREEN);
        sphere.setMaterial(greenMaterial);
    	
    	sphere.setTranslateX(sphere3D.getX());
    	sphere.setTranslateY(sphere3D.getY());
    	sphere.setTranslateZ(sphere3D.getZ());
    	parent.getChildren().add(sphere);
    }
    
    private static void AddQuadrilateral(Group parent, Point3D topRight, Point3D bottomRight, Point3D bottomLeft, Point3D topLeft,
    		 PhongMaterial material) {
    	final TriangleMesh triangleMesh = new TriangleMesh();
    	
    	final float[] points = {
    		(float)topRight.getX(),(float)topRight.getY(),(float)topRight.getZ(),
    		(float)topLeft.getX(),(float)topLeft.getY(),(float)topLeft.getZ(),
    		(float)bottomLeft.getX(),(float)bottomLeft.getY(),(float)bottomLeft.getZ(),
    		(float)bottomRight.getX(),(float)bottomRight.getY(),(float)bottomRight.getZ()
    	};
    	
    	final float[] texCoords = {
    			1,1,
    			1,0,
    			0,1,
    			0,0		
    	};
    	final int[] faces = {
    			0,1,1,0,2,2,
    			0,1,2,2,3,3
    	};
    	
    	triangleMesh.getPoints().setAll(points);
    	triangleMesh.getTexCoords().setAll(texCoords);
    	triangleMesh.getFaces().setAll(faces);
    	
    	final MeshView meshView = new MeshView(triangleMesh);
    	meshView.setMaterial(material);
    	parent.getChildren().addAll(meshView);
    }
}
