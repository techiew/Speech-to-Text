package gui;

import java.io.File;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import core.SpeechToText;

public class GuiController {

    @FXML
    private Button buttonChooseFile;

    @FXML
    private Button buttonStartProcessing;
    
    @FXML
    private ImageView imageLogo;

    @FXML
    private Label labelHelp;

    @FXML
    private Label labelFeedbackMsg;

    @FXML
    public void chooseFile(ActionEvent event) {

 	   Stage s = MyApplication.primaryStage;

 	   FileChooser fileChooser = new FileChooser();
 	   fileChooser.setTitle("Velg en lydfil");
 	   fileChooser.getExtensionFilters().addAll(
 	           new ExtensionFilter("Audio Files", "*.wav", "*.mp3", "*.aac")
 	           );
 	   
 	   File selectedFile = fileChooser.showOpenDialog(s);
 	   
 	   if (selectedFile != null) {
 		   System.out.println(selectedFile);
 	   }

 	   labelFeedbackMsg.setText("Du har valgt filen:\n " + selectedFile.getName());

       //System.out.println(selectedFile);
        
       MyApplication.selectedFile = selectedFile;
       buttonStartProcessing.setDisable(false);
    }

    @FXML
    void startFileAnalysis(MouseEvent event) {
    	new Thread(new SpeechToText(MyApplication.selectedFile.getPath())).start();
    }

}

