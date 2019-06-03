package org.app.liber.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BookshelfPojo implements Serializable {

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("author")
    @Expose
    private String author;
    @SerializedName("coverImgUrl")
    @Expose
    private String coverImgUrl;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("genre")
    @Expose
    private String genre;
    @SerializedName("rating")
    @Expose
    private String rating;
    @SerializedName("user")
    @Expose
    private String u_id;
    @SerializedName("available")
    @Expose
    private String available;
    @SerializedName("reader")
    @Expose
    private String reader;

    public String getReader() {
        return reader;
    }

    public void setReader(String reader) {
        this.reader = reader;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCoverImgUrl() {
        return coverImgUrl;
    }

    public void setCoverImgUrl(String coverImgUrl) {
        this.coverImgUrl = coverImgUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getU_id() { return u_id; }

    public void setU_id(String u_id) { this.u_id = u_id; }

    public String getAvailable() { return available; }

    public void setAvailable(String available) {
        this.available = available;
    }


}
