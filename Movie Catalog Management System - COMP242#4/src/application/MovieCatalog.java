package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

import javafx.collections.ObservableList;

public class MovieCatalog {
	HashTable<Movie> table;
	ObservableList<Movie> moviesList;

	public MovieCatalog() {
		table = new HashTable<Movie>();
	}

	public void allocate(int size) {
		table.setSize(size);
	}

	public boolean add(Movie movie) {
		table.insert(movie);
		return true;
	}

	public Movie get(String title) {
		return table.search(new Movie(title, null, 1, 1));
	}

	public void erase(String title) {
		table.delete(new Movie(title, null, 1, 1));
	}

	public void saveToFile(File file) throws FileNotFoundException {
		PrintWriter out = new PrintWriter(file);
		for (Movie movie : table) {
			out.println("Title: " + movie.getTitle());
			out.println("Description: " + movie.getDescription());
			out.println("Release Year: " + movie.getReleaseYear());
			out.println("Rating: " + movie.getRating() + "\n");
		}
		out.close();
	}

	public void loadFromFile(File file) {
		boolean skipAllErrors = false;
		try (Scanner in = new Scanner(file)) {
			int i = 1;
			while (in.hasNextLine()) {
				try {
					String titleLine = "";
					while (in.hasNextLine()) {
						titleLine = in.nextLine();
						if (!titleLine.trim().isEmpty())
							break;
					}
					if (titleLine == null || titleLine.trim().isEmpty())
						break;
					String descriptionLine = in.hasNextLine() ? in.nextLine() : "";
					String yearLine = in.hasNextLine() ? in.nextLine() : "";
					String ratingLine = in.hasNextLine() ? in.nextLine() : "";
					String title = titleLine.replaceFirst("Title: ", "").trim();
					String description = descriptionLine.replaceFirst("Description: ", "").trim();
					int releaseYear = Integer.parseInt(yearLine.replaceFirst("Release Year: ", "").trim());
					double rating = Double.parseDouble(ratingLine.replaceFirst("Rating: ", "").trim());
					Movie movie = new Movie(title, description, releaseYear, rating);
					if (table.insert(movie)) {
						moviesList.add(movie);
					} else {
						for (int j = 0; j < moviesList.size(); j++) {
							if (moviesList.get(j).compareTo(movie) == 0) {
								moviesList.set(j, movie);
								break;
							}
						}
					}
					i++;
				} catch (Exception e) {
					if (!skipAllErrors) {
						ErrorAlert alert = new ErrorAlert("Error while loading movie #" + i, false);
						skipAllErrors = alert.shouldSkipAll();
					}
				}
			}
		} catch (FileNotFoundException e) {
			new ErrorAlert("Error: File not found!");
		}
	}

	public void deallocate() {
		table.clear();
	}

}
