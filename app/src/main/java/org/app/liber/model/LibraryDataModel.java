package org.app.liber.model;

import android.support.annotation.Nullable;

import java.io.Serializable;

public class LibraryDataModel implements Serializable {

    private String bookTitle;
    private String author;
    private String smallThumbnailLink;
    private String description;
    private String genre;
    private String avgRating = "";

    public LibraryDataModel(String bookTitle, String author, String smallThumbnailLink, String description,String genre, @Nullable String avgRating) {
        this.bookTitle = bookTitle;
        this.author = author;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public String getSmallThumbnailLink() {
        return smallThumbnailLink;
    }

    public void setSmallThumbnailLink(String smallThumbnailLink) {
        this.smallThumbnailLink = smallThumbnailLink;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
