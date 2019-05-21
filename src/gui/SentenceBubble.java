package gui;

import javafx.scene.control.TextArea;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

// Datastruktur for "bobblene" (tenk Messenger på Facebook)
// Disse bruker vi i samtalevisningen i resultatvinduet
public class SentenceBubble extends TextArea {
	
	private String[] colors = { 
		"#14CF13", // Grønn
		"#139FCD", // Blå
		"#EE8C2B", // Oransje
		"#9F13CD", // Lilla
		"#13CD9F", // Turkis?
		"#77B300", // Mørkegul/grønn?
		"#0047B3", // Mørkeblå
		"#FF3385", // Rosa
		"#804000", // Brun
		"#161D16" // Svart
	};
	
	public SentenceBubble(String sentence, int type, int colorIndex) {

		String color = "";
		
		if(colorIndex > (colors.length - 1)) colorIndex = 0;
		
		color = colors[colorIndex];
		
		if(type == 0) {
			this.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 14));
			this.setText(sentence);
			this.setMinHeight(70);
			this.setStyle("-fx-text-fill: white; -fx-background-color: transparent; -fx-control-inner-background: " + color + ";");
			
		} else {
			this.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 12));
			this.setText(sentence);
			this.setStyle("-fx-text-fill: gray; -fx-background-color: transparent; -fx-control-inner-background: transparent; "
					+ "-fx-focus-color: transparent; -fx-faint-focus-color: transparent; -fx-highlight-fill: transparent; -fx-highlight-text-fill: gray;");
		}
		
		this.setWrapText(true);
	}
	
}
