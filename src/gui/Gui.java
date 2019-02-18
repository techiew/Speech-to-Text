package gui;

import core.SpeechToText;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Gui extends Application {
	
	public static Stage primaryStage;
	public static SpeechToText parentObject;
	

    public void startApplication(SpeechToText stt) {
    	Gui.parentObject = stt;
    	launch();
    }
    
    @Override
    public void start(Stage stage) {
    	
        try {
            Parent root = FXMLLoader.load(
            		getClass().getResource("Scene.fxml")
            		);
            
        	primaryStage = stage;
            primaryStage.setTitle("ITX - Tale til tekst");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
        
    }

}