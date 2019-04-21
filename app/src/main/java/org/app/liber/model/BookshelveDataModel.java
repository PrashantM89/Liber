package org.app.liber.model;

import java.io.Serializable;

public class BookshelveDataModel implements Serializable {
     String title;
     String authors;
     String smallThumbnailLink;
     boolean isSelected;
     String genre;

    public BookshelveDataModel(){

    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public void setSmallThumbnailLink(String smallThumbnailLink) {
        this.smallThumbnailLink = smallThumbnailLink;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public BookshelveDataModel(
            String title,
            String authors,
            String smallThumbnailLink
    ) {

        this.title = title;
        this.authors = authors;
        this.smallThumbnailLink = smallThumbnailLink;

    }

    public String getTitle() {
        return title;
    }

    public String getAuthors() {
        return authors;
    }

    public String getSmallThumbnailLink() {
        return smallThumbnailLink;
    }
}
