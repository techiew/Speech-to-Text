package gui;

import java.io.File;
import java.util.ArrayList;
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

    @FXML
    public void chooseFiles(ActionEvent event) {

 	   Stage s = Gui.primaryStage;

 	   FileChooser fileChooser = new FileChooser();
 	   fileChooser.setTitle("Velg en lydfil");
 	   fileChooser.getExtensionFilters().addAll(
 	           new ExtensionFilter("Audio Files", "*.wav", "*.mp3", "*.aac")
 	           );
 	   
 	   File selectedFile = fileChooser.showOpenDialog(s);
 	   ArrayList<File> selectedFiles = new ArrayList<File>();
 	   
 	   selectedFiles.add(selectedFile);
 	   
 	   if (selectedFile != null) {
 		   System.out.println(selectedFile);
 	   }

 	   labelFeedbackMsg.setText("Du har valgt filen:\n " + selectedFile.getName());

       Gui.parentObject.setGuiSelectedFiles(selectedFiles);
       buttonStartProcessing.setDisable(false);
    }

    @FXML
    void startFileAnalysis(MouseEvent event) {
    	Gui.parentObject.startProcess();
    }

}

