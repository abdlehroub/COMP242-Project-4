package application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class UpdateMovie {

	public static void showUpdateMoviePopup(MovieCatalog catalog, Movie movieToEdit) {
		Stage popupStage = new Stage();
		popupStage.initModality(Modality.APPLICATION_MODAL);
		popupStage.setTitle("Update Movie");

		// Fields with filled from movieToEdit data
		TextField titleField = new TextField(movieToEdit.getTitle());
		titleField.setPrefHeight(30);

		TextArea descriptionArea = new TextArea(movieToEdit.getDescription());

		TextField yearField = new TextField(String.valueOf(movieToEdit.getReleaseYear()));
		yearField.setPrefHeight(30);
		yearField.textProperty().addListener((obs, oldVal, newVal) -> {
			if (!newVal.matches("\\d*")) {
				yearField.setText(newVal.replaceAll("[^\\d]", ""));
			}
		});

		TextField ratingField = new TextField(String.valueOf(movieToEdit.getRating()));
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
		ratingLabel.setStyle("-fx-font-size: 10px; -fx-font-style: italic; -fx-text-fill: #635bff;");
		// Buttons
		HBox buttonsHb = new HBox();
		Button saveButton = new MyButton();
		saveButton.setText("Update");
		saveButton.setPrefSize(200, 25);
		Button cancelButton = new MyButton();
		cancelButton.setText("Cancel");
		cancelButton.setPrefSize(200, 25);

		buttonsHb.getChildren().addAll(saveButton, cancelButton);
		buttonsHb.setSpacing(10);
		buttonsHb.setAlignment(Pos.CENTER);

		GridPane grid = new GridPane();
		grid.setPadding(new Insets(15));
		grid.setHgap(10);
		grid.setVgap(10);

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
		scene.getStylesheets().add(UpdateMovie.class.getResource("application.css").toExternalForm());
		popupStage.setScene(scene);
		popupStage.setResizable(false);
		popupStage.showAndWait();

		cancelButton.setOnAction(e -> popupStage.close());

		saveButton.setOnAction(e -> {
			try {
				String newTitle = titleField.getText().trim();
				String description = descriptionArea.getText().trim();
				int year = Integer.parseInt(yearField.getText().trim());
				double rating = Double.parseDouble(ratingField.getText().trim());

				// Check if new title already exists and it is not the current movie
				Movie temp = new Movie(newTitle, "", 0, 0);
				if (!movieToEdit.getTitle().equals(newTitle) && catalog.table.contains(temp)) {
					throw new IllegalArgumentException("Error: A movie with this title already exists!");
				}

				// Remove old movie from hash table and list
				catalog.table.delete(movieToEdit);
				catalog.moviesList.remove(movieToEdit);

				// Create updated movie and insert it again
				Movie updatedMovie = new Movie(newTitle, description, year, rating);
				catalog.add(updatedMovie);
				catalog.moviesList.add(updatedMovie);

				popupStage.close();
			} catch (IllegalArgumentException ex) {
				new ErrorAlert("Invalid input: " + ex.getMessage());
			}
		});

	}
}
