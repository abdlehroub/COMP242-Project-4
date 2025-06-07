package application;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class SuccessAlert {

	public SuccessAlert(String message) {
		showSuccAlert(message);
	}

	public void showSuccAlert(String message) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Done");
		alert.setHeaderText(message);
		alert.getDialogPane().getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		alert.showAndWait();
	}

}
