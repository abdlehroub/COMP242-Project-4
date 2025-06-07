package application;

import java.io.File;
import java.io.FileNotFoundException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class HomeScene extends Scene {

	private static BorderPane mainPane = new BorderPane();
	private TableView<Movie> moviesTable;
	private TextField searchTf;
	private MovieCatalog catalog;
	private File file;

	@SuppressWarnings({ "unchecked", "deprecation" })
	public HomeScene() {
		super(mainPane);

		catalog = new MovieCatalog();
		catalog.moviesList = FXCollections.observableArrayList();

		VBox tableVb = new VBox();
		tableVb.getStyleClass().add("rounded-vbox");

//		Label as title of the page
		MyLabel titleL = new MyLabel("Movies Table");
		titleL.setPadding(new Insets(20, 0, 10, 390));

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

		HBox searchHb = new HBox();
//		Search TextField
		searchTf = new TextField();
		searchTf.setPrefHeight(25);
		searchTf.setPrefWidth(400);
		searchTf.setPromptText("Search:");

//		RadioButtons to select the filter type
		ToggleGroup group = new ToggleGroup();
		RadioButton titleRb = new RadioButton("Title");
		titleRb.setToggleGroup(group);
		titleRb.setPrefSize(60, 30);
		RadioButton yearRb = new RadioButton("Release Year");
		yearRb.setToggleGroup(group);
		yearRb.setPrefSize(150, 30);
		titleRb.setSelected(true);

		searchHb.getChildren().addAll(searchTf, titleRb, yearRb);
		searchHb.setAlignment(Pos.CENTER);

		MenuBar menuBar = new MenuBar();

		Menu fileM = new Menu("File");
		Menu userM = new Menu("Movie");
		Menu exitM = new Menu("Exit");

		MenuItem readMi = new MenuItem("Open");
		readMi.setAccelerator(KeyCombination.keyCombination("Ctrl+O"));
		MenuItem saveMi = new MenuItem("Save");
		saveMi.setAccelerator(KeyCombination.keyCombination("Ctrl+S"));
		fileM.getItems().addAll(readMi, saveMi);

		MenuItem addMi = new MenuItem("Add Movie");
		addMi.setAccelerator(KeyCombination.keyCombination("Ctrl+N"));
		MenuItem updateMi = new MenuItem("Update Movie");
		updateMi.setAccelerator(KeyCombination.keyCombination("Ctrl+U"));
		MenuItem deleteMi = new MenuItem("Delete Movie");
		deleteMi.setAccelerator(KeyCombination.keyCombination("Ctrl+D"));
		MenuItem sortMi = new MenuItem("Sort Movies");
		sortMi.setAccelerator(KeyCombination.keyCombination("Ctrl+R"));

		userM.getItems().addAll(addMi, updateMi, deleteMi, sortMi);

		MenuItem exitDontMi = new MenuItem("Exit");
		exitM.getItems().addAll(exitDontMi);
		exitDontMi.setAccelerator(KeyCombination.keyCombination("Ctrl+Q"));

		menuBar.getMenus().addAll(fileM, userM, exitM);
		menuBar.setMinWidth(960);

		tableVb.getChildren().addAll(menuBar, titleL, searchHb, vbox);
		mainPane.setCenter(tableVb);

		searchTf.setOnKeyTyped(e -> {
			if (titleRb.isSelected()) {
				filtered("Title");
			} else {
				filtered("Release Year");
			}
			if (searchTf.getText().isEmpty())
				moviesTable.setItems(catalog.moviesList);

		});

		readMi.setOnAction(e -> {
			fileChooser();
			try {
				catalog.loadFromFile(file);
				moviesTable.refresh();
				new SuccessAlert("Great: The saving is done!\n" + "Unique movies in HashTable: "
						+ catalog.table.getCount() + "\n" + "Movies in ObservableList: " + catalog.moviesList.size());
			} catch (Exception e1) {
				new ErrorAlert("Error: No File Selected!");
			}
		});
		saveMi.setOnAction(e -> {
			fileChooser();
			try {
				catalog.saveToFile(file);
				new SuccessAlert("Great: The saving is done!");
			} catch (FileNotFoundException e1) {
				new ErrorAlert("Error:File Not Found!");
			}

		});
		addMi.setOnAction(e -> {
			AddMovie.showAddMoviePopup(catalog);

		});
		updateMi.setOnAction(e -> {
			Movie selected = moviesTable.getSelectionModel().getSelectedItem();
			if (selected != null) {
				UpdateMovie.showUpdateMoviePopup(catalog, selected);
				moviesTable.refresh();
			}
		});
		deleteMi.setOnAction(e -> {
			try {
				deleteMovie();
			} catch (IllegalArgumentException ex) {
				new ErrorAlert(ex.getMessage());
			}
		});
		sortMi.setOnAction(e -> {
			SortWindow.show(catalog);

		});
		exitDontMi.setOnAction(e -> {
			((Stage) this.getWindow()).close();
		});

	}

//	method to open file chooser dialog
	public void fileChooser() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
		file = fileChooser.showOpenDialog(new Stage());
	}

//	Display the users depends on the search TextField
	public void filtered(String filterBy) {
		ObservableList<Movie> filterdList = FXCollections.observableArrayList();
		for (Movie movie : catalog.moviesList) {
			if (filterBy.equals("Title")) {
				if (movie.getTitle().toLowerCase().trim().startsWith(searchTf.getText().toLowerCase().trim())) {
					filterdList.add(movie);
				}
			} else {
				if ((movie.getReleaseYear() + "").startsWith(searchTf.getText())) {
					filterdList.add(movie);
				}
			}
		}
		moviesTable.setItems(filterdList);

	}

	public void deleteMovie() {
		Movie p = moviesTable.getSelectionModel().getSelectedItem();
		if (p != null) {
			catalog.table.delete(p);
			catalog.moviesList.remove(p);
		} else {
			throw new IllegalArgumentException("Error: No Product Selected");
		}
	}

}
