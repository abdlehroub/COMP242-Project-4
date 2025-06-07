package application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AddMovie {

	public static void showAddMoviePopup(MovieCatalog catalog) {
		Stage popupStage = new Stage();
		popupStage.initModality(Modality.APPLICATION_MODAL);
		popupStage.setTitle("Add Movie");

		TextField titleField = new TextField();
		titleField.setPrefHeight(30);
		TextArea descriptionArea = new TextArea();
		TextField yearField = new TextField();
		yearField.setPrefHeight(30);
		yearField.setPrefHeight(30);
		yearField.textProperty().addListener((obs, oldVal, newVal) -> {
			if (!newVal.matches("\\d*")) {
				yearField.setText(newVal.replaceAll("[^\\d]", ""));
			}
		});
		TextField ratingField = new TextField();
		ratingField.setPrefHeight(30);
		ratingField.textProperty().addListener((obs, oldVal, newVal) -> {
			if (!newVal.matches("\\d*(\\.\\d*)?")) {
				ratingField.setText(oldVal);
			}
		});

		Label titleLabel = new MyLabel("Title:");
		titleLabel.setStyle("-fx-font-size: 10px; -fx-font-style: italic; -fx-text-fill: #635bff; ");
		Label descLabel = new MyLabel("Description:");
		descLabel.setStyle("-fx-font-size: 10px; -fx-font-style: italic; -fx-text-fill: #635bff; ");
		Label yearLabel = new MyLabel("Release Year:");
		yearLabel.setStyle("-fx-font-size: 10px; -fx-font-style: italic; -fx-text-fill: #635bff;");
		Label ratingLabel = new MyLabel("Rating:");
		ratingLabel.setStyle("-fx-font-size: 10px; -fx-font-style: italic; -fx-text-fill: #635bff; ");

		HBox buttonsHb = new HBox();
		Button saveButton = new MyButton();
		saveButton.setPrefSize(200, 25);
		saveButton.setText("Save");
		Button cancelB = new MyButton();
		cancelB.setText("Cancel");
		cancelB.setPrefSize(200, 25);
		buttonsHb.getChildren().addAll(saveButton, cancelB);
		buttonsHb.setSpacing(10);

		// GridPane for all text fields
		GridPane grid = new GridPane();
		grid.setPadding(new Insets(15));
		grid.setHgap(10);
		grid.setVgap(10);

		buttonsHb.setAlignment(Pos.CENTER);

		grid.add(titleLabel, 0, 0);
		grid.add(titleField, 1, 0);
		grid.add(descLabel, 0, 1);
		grid.add(descriptionArea, 1, 1);
		grid.add(yearLabel, 0, 2);
		grid.add(yearField, 1, 2);
		grid.add(ratingLabel, 0, 3);
		grid.add(ratingField, 1, 3);
		grid.add(buttonsHb, 1, 4);

		Scene scene = new Scene(grid, 720, 400);
		scene.getStylesheets().add(AddMovie.class.getResource("application.css").toExternalForm());
		popupStage.setScene(scene);
		popupStage.setResizable(false);
		popupStage.showAndWait();

		saveButton.setOnAction(e -> {
			try {
				String title = titleField.getText().trim();
				String description = descriptionArea.getText().trim();
				int year = Integer.parseInt(yearField.getText().trim());
				double rating = Double.parseDouble(ratingField.getText().trim());

				Movie newMovie = new Movie(title, description, year, rating);
				if (!catalog.table.contains(newMovie)) {
					catalog.add(newMovie);
					catalog.moviesList.add(newMovie);
				} else {
					throw new IllegalArgumentException("Error: Title is already exist!");
				}

				popupStage.close();
			} catch (IllegalArgumentException ex) {
				new ErrorAlert(ex.getMessage());
			}
		});

		cancelB.setOnAction(e -> {
			// Close the window
			Stage stage = (Stage) cancelB.getScene().getWindow();
			stage.close();
		});
	}
}
