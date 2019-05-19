package gui;

import java.io.IOException;
import java.util.regex.Pattern;

import core.SpeechToText;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class Gui extends Application {
	
	public static Stage primaryStage;
	public static SpeechToText parentObject;
	private static GuiController primaryController;
	
    public void startApplication(SpeechToText stt) {
    	Gui.parentObject = stt;
    	launch();
    }
    
    @Override
    public void start(Stage stage) {
    	
        try {
            FXMLLoader loader = new FXMLLoader(
            		getClass().getResource("Scene.fxml")
            		);
            Parent root = loader.load();
            
            primaryController = (GuiController) loader.getController();
            
        	primaryStage = stage;
            primaryStage.setTitle("ITX - Tale til tekst");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
            
        } catch(Exception e) {
            e.printStackTrace();
        }
        
    }
    
    // Viser resultatene fra analysen i et nytt vindu, dataene leses fra en .json fil
    public void showChatResults(String fileToRead) {
    	System.out.println("Åpner resultatvisning...");
    	
    	Platform.runLater(() -> {
    		resetMainWindow();
    		
			try {
				FXMLLoader loader = new FXMLLoader(
						getClass().getResource("ResultsView.fxml")
						);
				
				Parent root = loader.load();
				
				ResultsController controller = (ResultsController) loader.getController();
				
				controller.createChatLog(fileToRead);
				
				Stage resultsStage = new Stage();
				String[] fileNameSplit = fileToRead.split(Pattern.quote("\\"));
				String fileName = fileNameSplit[fileNameSplit.length-1];
				resultsStage.setTitle(fileName + " - Resultatvisning");
				resultsStage.setScene(new Scene(root));
		    	resultsStage.show();
		    	
			} catch (IOException e) {
				e.printStackTrace();
			}

    	});
    	
    }
    
    // Si ifra til dette vinduet sin controller at vi skal nullstille
    public void resetMainWindow() {
    	primaryController.resetWindowState();
    }

}