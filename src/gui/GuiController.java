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
    private ImageView imageLoading;

    @FXML
    private Label labelHelp;

    @FXML
    private Label labelHelp2;

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

 	   } else {
 		   System.out.println("Feil med valgte filer");
 		   Gui.parentObject.setGuiSelectedFiles(null);
 		   buttonStartProcessing.setDisable(true);
 		   return;
 	   }

 	   if(selectedFiles.size() > 1) {
 		   labelFeedbackMsg.setText("Du har valgt " + selectedFiles.size() + " filer");
 	   } else {
 		   labelFeedbackMsg.setText("Du har valgt " + selectedFiles.size() + " fil");
 	   }
 	   
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
        	labelHelp.setText("Vennligst vent mens lydfilene blir analysert");
        	labelHelp2.setVisible(false);
        	imageLoading.setVisible(true);
        	buttonChooseFiles.setVisible(false);
        	buttonStartProcessing.setVisible(false);
        	buttonChooseFiles.setDisable(true);
    	} else {
    		System.out.println("Kunne ikke starte analysen, feil med valgte filer");
    	}

    }
    
    //Nullstiller vinduet når vi er ferdig med en analyse, slik at vi kan bruke vinduet igjen
    public void resetWindowState() {
        buttonStartProcessing.setDisable(true);
    	buttonChooseFiles.setDisable(false);
    	buttonChooseFiles.setVisible(true);
    	buttonStartProcessing.setVisible(true);
    	imageLoading.setVisible(false);
        labelHelp.setText("Velg en eller flere lydfiler med tale");
        labelFeedbackMsg.setText("Lydfilene ble analysert og resultatene lagret.");
        labelHelp.setVisible(true);
        labelHelp2.setVisible(true);
    }

}

