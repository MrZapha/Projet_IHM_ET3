module Projet_IHM_Obis3D {
	requires javafx.controls;
	requires javafx.fxml;
	requires jimObjModelImporterJFX;
	requires java.net.http;
	requires org.json;
	
	opens application to javafx.graphics, javafx.fxml;
}
