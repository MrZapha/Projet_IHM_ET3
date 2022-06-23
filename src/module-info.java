module Projet_IHM_ET3{
	requires javafx.controls;
	requires javafx.fxml;
	requires java.net.http;
	requires jimObjModelImporterJFX;
	requires org.json;
	requires org.controlsfx.controls;
	requires javafx.graphics;
	requires javafx.base;
	
	opens application to javafx.graphics, javafx.fxml;
}
