package gui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class GuiController {

    @FXML
    private Button buttonChooseFiles;

    @FXML
    private Button buttonStartProcessing;
    
    @FXML
    private ImageView imageLogo;

    @FXML
    private Label labelHelp;

    @FXML
    private Label labelFeedbackMsg;

    //Kode for "velg filer" knappen
    @FXML
    public void chooseFiles(ActionEvent event) {

 	   Stage s = Gui.primaryStage;

 	   FileChooser fileChooser = new FileChooser();
 	   fileChooser.setInitialDirectory(new File("."));
 	   fileChooser.setTitle("Velg lydfiler");
 	   fileChooser.getExtensionFilters().addAll(
 	           new ExtensionFilter("Audio Files", "*.wav", "*.mp3", "*.aac")
 	           );
 	   
 	   List<File> selectedFiles = fileChooser.showOpenMultipleDialog(s);
 	   
 	   if (selectedFiles != null) {
 		   
 		   for(int i = 0; i < selectedFiles.size(); i++) {
 	 		   System.out.println(selectedFiles.get(i).getName());
 		   }

 	   } else {
 		   System.out.println("Feil med valgte filer");
 		   Gui.parentObject.setGuiSelectedFiles(null);
 		   buttonStartProcessing.setDisable(true);
 		   return;
 	   }

 	   labelFeedbackMsg.setText("Du har valgt " + selectedFiles.size() + " filer");

       Gui.parentObject.setGuiSelectedFiles(selectedFiles);
       buttonStartProcessing.setDisable(false);
    }

    //Kode for "start analyse" knappen
    @FXML
    void startFileAnalysis(ActionEvent event) {
    	
    	if(Gui.parentObject.getGuiSelectedFiles().size() > 0) {
        	buttonStartProcessing.setDisable(true);
        	labelFeedbackMsg.setText("Analyserer filene...");
        	
    		System.out.println("Disse filene vil bli analysert: ");
    		
        	for(int i = 0; i < Gui.parentObject.getGuiSelectedFiles().size(); i++) {
        		System.out.println(Gui.parentObject.getGuiSelectedFiles().get(i).getName());
        	}
        	
        	Gui.parentObject.startProcess();
    	} else {
    		System.out.println("Kunne ikke starte analysen, feil med valgte filer");
    	}

    }

}

