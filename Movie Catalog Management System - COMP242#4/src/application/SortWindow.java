package application;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.List;

public class SortWindow {

	private static int currentPage = 0;
	private static TableView<Movie> moviesTable;
	private static Button prevButton;
	private static Button nextButton;
	private static MovieCatalog catalog;
	static MyLabel noteL;

	@SuppressWarnings({ "unchecked", "deprecation" })
	public static void show(MovieCatalog movieCatalog) {
		catalog = movieCatalog;
		Stage popupStage = new Stage();
		popupStage.initModality(Modality.APPLICATION_MODAL);
		popupStage.setTitle("Browse Movies");

		moviesTable = new TableView<Movie>();
		TableColumn<Movie, String> titleC = new TableColumn<Movie, String>("Title");
		TableColumn<Movie, String> descriptionC = new TableColumn<Movie, String>("Description");
		TableColumn<Movie, Integer> yearC = new TableColumn<Movie, Integer>("Release Year");
		TableColumn<Movie, Double> ratingC = new TableColumn<Movie, Double>("Rating");
		moviesTable.getColumns().addAll(titleC, descriptionC, yearC, ratingC);

		titleC.setCellValueFactory(new PropertyValueFactory<Movie, String>("title"));
		descriptionC.setCellValueFactory(new PropertyValueFactory<Movie, String>("description"));
		yearC.setCellValueFactory(new PropertyValueFactory<Movie, Integer>("releaseYear"));
		ratingC.setCellValueFactory(new PropertyValueFactory<Movie, Double>("rating"));

		moviesTable.setStyle("  -fx-background-color: white;\r\n" + "    -fx-background-radius: 10;\r\n"
				+ "    -fx-border-radius: 10;\r\n" + "    -fx-border-color: #E5E7EB;\r\n" + "    -fx-border-width: 1;");
		moviesTable.autosize();
		moviesTable.setMaxWidth(900);
		VBox vbox = new VBox();
		vbox.getChildren().add(moviesTable);
		vbox.setAlignment(Pos.CENTER);
		moviesTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		moviesTable.setItems(catalog.moviesList);

		ToggleGroup group = new ToggleGroup();
		RadioButton descRb = new RadioButton("DESC");
		descRb.setToggleGroup(group);
		descRb.setPrefSize(60, 30);
		RadioButton ascRb = new RadioButton("ASC");
		ascRb.setToggleGroup(group);
		ascRb.setPrefSize(150, 30);
		ascRb.setSelected(true);

		HBox rbHb = new HBox();
		MyLabel sortL = new MyLabel("Sort: ");
		sortL.setStyle("-fx-font-size: 10px; -fx-font-style: italic ;-fx-font-weight: bold");
		rbHb.getChildren().addAll(sortL, descRb, ascRb);
		rbHb.setAlignment(Pos.CENTER);

		noteL = new MyLabel("");
		noteL.setStyle("-fx-font-size: 10px; -fx-font-style: italic; -fx-text-fill: #635bff;");

		// Create Buttons and Label
		prevButton = new MyButton();
		prevButton.setText("Previous");
		prevButton.setPrefHeight(30);
		prevButton.setPrefWidth(120);

		nextButton = new MyButton();
		nextButton.setText("Next");
		nextButton.setPrefHeight(30);
		nextButton.setPrefWidth(120);

		VBox bottomVb = new VBox();

		HBox navigation = new HBox(10, prevButton, nextButton, noteL);
		navigation.setPadding(new Insets(10));
		navigation.setStyle("-fx-alignment: center;");

		bottomVb.getChildren().addAll(navigation, noteL);
		bottomVb.setSpacing(10);

		BorderPane root = new BorderPane();
		root.setPadding(new Insets(15));
		root.setTop(rbHb);
		root.setCenter(vbox);
		root.setBottom(bottomVb);
		root.setStyle("-fx-background-color: #FFFFFF");
		loadPage(ascRb.isSelected());

		Scene scene = new Scene(root, 800, 400);
		scene.getStylesheets().add(SortWindow.class.getResource("application.css").toExternalForm());
		popupStage.setScene(scene);
		popupStage.setResizable(false);
		popupStage.showAndWait();

		prevButton.setOnAction(e -> goPrev(ascRb.isSelected()));
		nextButton.setOnAction(e -> goNext(ascRb.isSelected()));

		ascRb.setOnAction(e -> {
			loadPage(ascRb.isSelected());
		});
		descRb.setOnAction(e -> {
			loadPage(ascRb.isSelected());

		});
	}

	public static void goNext(boolean asc) {
		if (currentPage == catalog.table.getSize() - 1)
			currentPage = 0;
		else
			currentPage++;
		loadPage(asc);

	}

	public static void goPrev(boolean asc) {
		if (currentPage == 0)
			currentPage = catalog.table.getSize();
		else
			currentPage--;
		loadPage(asc);

	}

	public static void loadPage(boolean asc) {
		AVLTree<Movie> tree = catalog.table.getCell(currentPage);
		List<Movie> cellMovies = tree.inOrderTraversal();
		if (!asc)
			cellMovies = cellMovies.reversed();
		noteL.setText("AVL Height: " + tree.getHeight() + "  |  Cell index: " + currentPage + "  |  Table Size: "
				+ catalog.table.getSize() + "  |  Total Count: " + catalog.moviesList.size());

		moviesTable.setItems(FXCollections.observableArrayList(cellMovies));
	}

}
