package application;

import java.util.ArrayList;

import Donnees.Donne;
import Donnees.Enregistrement;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Sphere;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

public class Model {
	
    private static final float TEXTURE_LAT_OFFSET = -0.2f;
    private static final float TEXTURE_LON_OFFSET = 2.8f;
    private static final float TEXTURE_OFFSET = 1.01f;
    
    public static void firstDraw(Group parent,TextField txtEspece,Donne d,Label[] labelLegende) {
    	//Draw from Json Delphinidae file
        ArrayList<Enregistrement> registeredList = d.get_list();
        Group group = new Group();
        double diameter = 0.01f;
        String nom = registeredList.get(0).get_nom();
        txtEspece.setText(nom);
        
        final PhongMaterial material1 = new PhongMaterial();
        material1.setDiffuseColor(Color.rgb(255,255,204,0));
        
        final PhongMaterial material2= new PhongMaterial();
        material2.setDiffuseColor(Color.rgb(255,237,160,0));
        
        final PhongMaterial material3 = new PhongMaterial();
        material3.setDiffuseColor(Color.rgb(254,217,118,0));
        
        final PhongMaterial material4 = new PhongMaterial();
        material4.setDiffuseColor(Color.rgb(254,178,76,0));
        
        final PhongMaterial material5 = new PhongMaterial();
        material5.setDiffuseColor(Color.rgb(253,141,60,0));
      
        final PhongMaterial material6 = new PhongMaterial();
        material6.setDiffuseColor(Color.rgb(252,78,42,0));
        
        final PhongMaterial material7 = new PhongMaterial();
        material7.setDiffuseColor(Color.rgb(227,26,28,0));
        
        final PhongMaterial material8 = new PhongMaterial();
        material8.setDiffuseColor(Color.rgb(177,0,38,0));
        
        int max = registeredList.get(0).get_nombre();
        int[] tableauEchelle = new int[8];
        for(int i=0;i<8;i++) {
        	tableauEchelle[i] = (i+1)*max/8;
        	if(i>0 && i<8) {
        		labelLegende[i-1].setText(""+tableauEchelle[i]);
        	}
        }
        for(int i=0;i<registeredList.size();i++) {
        	ArrayList<Point2D> region = registeredList.get(i).get_region();
        	
        	Point3D spaceCoord0 = geoCoordTo3dCoord((float)region.get(0).getY(), (float)region.get(0).getX());
        	Point3D spaceCoord1 = geoCoordTo3dCoord((float)region.get(1).getY(), (float)region.get(1).getX());
        	Point3D spaceCoord2 = geoCoordTo3dCoord((float)region.get(2).getY(), (float)region.get(2).getX());
        	Point3D spaceCoord3 = geoCoordTo3dCoord((float)region.get(3).getY(), (float)region.get(3).getX());
        	
        	if(registeredList.get(i).get_nombre()<=tableauEchelle[0]) {
        		AddQuadrilateral(group,spaceCoord2,spaceCoord1,spaceCoord0,spaceCoord3,material1);
        	}else if(registeredList.get(i).get_nombre()<=tableauEchelle[1]) {
        		AddQuadrilateral(group,spaceCoord2,spaceCoord1,spaceCoord0,spaceCoord3,material2);
        	}else if(registeredList.get(i).get_nombre()<=tableauEchelle[2]) {
        		AddQuadrilateral(group,spaceCoord2,spaceCoord1,spaceCoord0,spaceCoord3,material3);
        	}else if(registeredList.get(i).get_nombre()<=tableauEchelle[3]) {
        		AddQuadrilateral(group,spaceCoord2,spaceCoord1,spaceCoord0,spaceCoord3,material4);
        	}else if(registeredList.get(i).get_nombre()<=tableauEchelle[4]) {
        		AddQuadrilateral(group,spaceCoord2,spaceCoord1,spaceCoord0,spaceCoord3,material5);
        	}else if(registeredList.get(i).get_nombre()<=tableauEchelle[5]) {
        		AddQuadrilateral(group,spaceCoord2,spaceCoord1,spaceCoord0,spaceCoord3,material6);
        	}else if(registeredList.get(i).get_nombre()<=tableauEchelle[6]) {
        		AddQuadrilateral(group,spaceCoord2,spaceCoord1,spaceCoord0,spaceCoord3,material7);
        	}else if(registeredList.get(i).get_nombre()<=tableauEchelle[7]) {
        		AddQuadrilateral(group,spaceCoord2,spaceCoord1,spaceCoord0,spaceCoord3,material8);
        	}	
            
        }
        parent.getChildren().addAll(group);
        
    }

	public static void drawHistogram(Group parent,Donne d,int precisionGeoHash,Label[] labelLegende) {
        ArrayList<Enregistrement> registeredList = d.get_list();
        Group group = new Group();
        double diameter = 0.03f/precisionGeoHash;
        
        final PhongMaterial material1 = new PhongMaterial();
        material1.setDiffuseColor(Color.rgb(255,255,204,0));
        
        final PhongMaterial material2= new PhongMaterial();
        material2.setDiffuseColor(Color.rgb(255,237,160,0));
        
        final PhongMaterial material3 = new PhongMaterial();
        material3.setDiffuseColor(Color.rgb(254,217,118,0));
        
        final PhongMaterial material4 = new PhongMaterial();
        material4.setDiffuseColor(Color.rgb(254,178,76,0));
        
        final PhongMaterial material5 = new PhongMaterial();
        material5.setDiffuseColor(Color.rgb(253,141,60,0));
      
        final PhongMaterial material6 = new PhongMaterial();
        material6.setDiffuseColor(Color.rgb(252,78,42,0));
        
        final PhongMaterial material7 = new PhongMaterial();
        material7.setDiffuseColor(Color.rgb(227,26,28,0));
        
        final PhongMaterial material8 = new PhongMaterial();
        material8.setDiffuseColor(Color.rgb(177,0,38,0));
        
        int max = registeredList.get(0).get_nombre();
        int[] tableauEchelle = new int[8];
        for(int i=0;i<8;i++) {
        	tableauEchelle[i] = (i+1)*max/8;
        	if(i>0 && i<8) {
        		labelLegende[i-1].setText(""+tableauEchelle[i]);
        	}
        }
        for(int i=0;i<registeredList.size();i++) {
        	ArrayList<Point2D> region = registeredList.get(i).get_region();
        	
        	Point3D spaceCoord0 = geoCoordTo3dCoord((float)region.get(0).getY(), (float)region.get(0).getX());
        	Point3D spaceCoord2 = geoCoordTo3dCoord((float)region.get(2).getY(), (float)region.get(2).getX());
        	Point3D milieu = spaceCoord0.midpoint(spaceCoord2);
        	
        	if(registeredList.get(i).get_nombre()<=tableauEchelle[0]) {
        		Cylinder cyl = createLine(milieu, milieu.multiply(1.03),diameter);
                cyl.setMaterial(material1);
                group.getChildren().add(cyl);
        	}else if(registeredList.get(i).get_nombre()<=tableauEchelle[1]) {
        		Cylinder cyl = createLine(milieu, milieu.multiply(1.06),diameter);
                cyl.setMaterial(material2);
                group.getChildren().add(cyl);
        	}else if(registeredList.get(i).get_nombre()<=tableauEchelle[2]) {
        		Cylinder cyl = createLine(milieu, milieu.multiply(1.09),diameter);
                cyl.setMaterial(material3);
                group.getChildren().add(cyl);
        	}else if(registeredList.get(i).get_nombre()<=tableauEchelle[3]) {
        		Cylinder cyl = createLine(milieu, milieu.multiply(1.12),diameter);
                cyl.setMaterial(material4);
                group.getChildren().add(cyl);
        	}else if(registeredList.get(i).get_nombre()<=tableauEchelle[4]) {
        		Cylinder cyl = createLine(milieu, milieu.multiply(1.15),diameter);
                cyl.setMaterial(material5);
                group.getChildren().add(cyl);
        	}else if(registeredList.get(i).get_nombre()<=tableauEchelle[5]) {
        		Cylinder cyl = createLine(milieu, milieu.multiply(1.18),diameter);
                cyl.setMaterial(material6);
                group.getChildren().add(cyl);
        	}else if(registeredList.get(i).get_nombre()<=tableauEchelle[6]) {
        		Cylinder cyl = createLine(milieu, milieu.multiply(1.21),diameter);
                cyl.setMaterial(material7);
                group.getChildren().add(cyl);
        	}else if(registeredList.get(i).get_nombre()<=tableauEchelle[7]) {
        		Cylinder cyl = createLine(milieu, milieu.multiply(1.24),diameter);
                cyl.setMaterial(material8);
                group.getChildren().add(cyl);
        	}	
            
        }
        parent.getChildren().addAll(group);
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
    public static Cylinder createLine(Point3D origin, Point3D target,double diameter) {
        Point3D yAxis = new Point3D(0, 1, 0);
        Point3D diff = target.subtract(origin);
        double height = diff.magnitude();

        Point3D mid = target.midpoint(origin);
        Translate moveToMidpoint = new Translate(mid.getX(), mid.getY(), mid.getZ());

        Point3D axisOfRotation = diff.crossProduct(yAxis);
        double angle = Math.acos(diff.normalize().dotProduct(yAxis));
        Rotate rotateAroundCenter = new Rotate(-Math.toDegrees(angle), axisOfRotation);

        Cylinder line = new Cylinder(diameter, height);

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
