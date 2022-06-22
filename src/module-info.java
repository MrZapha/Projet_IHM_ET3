module Projet_IHM_Obis3D {
	requires javafx.controls;
	requires javafx.fxml;
	requires java.net.http;
	requires jimObjModelImporterJFX;
	requires org.json;
	requires org.controlsfx.controls;
	
	opens application to javafx.graphics, javafx.fxml;
}
