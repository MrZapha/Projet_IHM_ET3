module Projet_IHM_ET3 {
	requires javafx.controls;
	requires java.net.http;
	requires org.json;
	
	opens application to javafx.graphics, javafx.fxml;
}
