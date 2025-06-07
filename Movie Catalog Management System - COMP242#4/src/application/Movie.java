package application;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class Movie implements Comparable<Movie> {

	private String title;
	private String description;
	private int releaseYear;
	private double rating;

	public Movie(String title, String description, int releaseYear, double rating) {
		setTitle(title);
		setDescription(description);
		setRating(rating);
		setReleaseYear(releaseYear);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getReleaseYear() {
		return releaseYear;
	}

	public void setReleaseYear(int releaseYear) {
		if (releaseYear < 0 || releaseYear > new GregorianCalendar().get(Calendar.YEAR))
			throw new IllegalArgumentException("Error: Invalid release year!");
		this.releaseYear = releaseYear;
	}

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		if (rating < 0 || rating > 10)
			throw new IllegalArgumentException("Error: Invalid rating (It must be >0 and <10)!");
		this.rating = rating;
	}

	@Override
	public int compareTo(Movie o) {
		return this.title.compareTo(o.title);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Movie other = (Movie) obj;
		return this.title.equals(other.title);
	}

	@Override
	public String toString() {
		return "Movie [title=" + title + ", description=" + description + ", releaseYear=" + releaseYear + ", rating="
				+ rating + "]";
	}

	@Override
	public int hashCode() {
		return title.hashCode();
	}

}
