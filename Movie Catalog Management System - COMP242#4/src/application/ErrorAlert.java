package application;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class ErrorAlert {

	private boolean skipAll = false;

	public ErrorAlert(String message, boolean skipAll) {
		this.skipAll = skipAll;
		showErrorAlertWithTwo(message);
	}

	public ErrorAlert(String message) {
		showErrorAlert(message);
	}

	public boolean shouldSkipAll() {
		return skipAll;
	}

	private void showErrorAlertWithTwo(String message) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText(message);
		alert.getDialogPane().getStylesheets().add(getClass().getResource("application.css").toExternalForm());

		ButtonType skip = new ButtonType("Skip this");
		ButtonType skipAllButton = new ButtonType("Skip All");

		alert.getButtonTypes().setAll(skip, skipAllButton);

		ButtonType result = alert.showAndWait().orElse(skip);
		if (result == skipAllButton) {
			skipAll = true;
		}
	}

	private void showErrorAlert(String message) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
        alert.getDialogPane().getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		alert.setHeaderText(message);
		alert.showAndWait();
	}
}
