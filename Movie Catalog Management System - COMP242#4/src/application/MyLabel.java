package application;

import javafx.scene.control.Label;

public class MyLabel extends Label {
	public MyLabel(String text) {
		this.setText(text);
		this.setStyle( // Outline (focus effect)
				"-fx-font-family: 'Montserrat';" + "-fx-font-weight: bold;" + "-fx-text-fill: #222222; " + // Text color
																											// (white)
						"-fx-font-size: 20px;");
	}

}
