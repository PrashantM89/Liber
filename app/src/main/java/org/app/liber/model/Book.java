package org.app.liber.model;

/**
 * Book
 * A custom class to store Book information
 */
public class Book {

    public String title = "";
    public String authors = "";
    public String smallThumbnailLink = "";
    public String description = "";
    public String genre = "";
    public String avgRating = "";

    public Book(
            String title,
            String authors,
            String smallThumbnailLink,
            String description,
            String genre,
            String avgRating
    ) {

        this.title = title;
        this.authors = authors;
        this.smallThumbnailLink = smallThumbnailLink;
        this.description = description;
        this.genre = genre;
        this.avgRating = avgRating;
    }

    public String getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(String avgRating) {
        this.avgRating = avgRating;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthors() {
        return authors;
    }

    public String getDescription() {
        return description;
    }

    public String getSmallThumbnailLink() {
        return smallThumbnailLink;
    }
}
