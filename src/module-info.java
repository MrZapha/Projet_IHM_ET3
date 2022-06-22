module Projet_IHM_Obis3d{
	requires javafx.controls;
	requires javafx.fxml;
	requires java.net.http;
	requires jimObjModelImporterJFX;
	requires org.json;
	
	opens application to javafx.graphics, javafx.fxml;
}
