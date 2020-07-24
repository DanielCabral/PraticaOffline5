package controller;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXMLLoader;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			AnchorPane root = (AnchorPane)FXMLLoader.load(getClass().getResource("/view/Tela.fxml"));			
			Scene scene = new Scene(root,378,263);
			primaryStage.setScene(scene);
			primaryStage.resizableProperty().setValue(Boolean.FALSE);
			primaryStage.setTitle("No");
			primaryStage.setResizable(false);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {	
		launch(args);
	}
}
