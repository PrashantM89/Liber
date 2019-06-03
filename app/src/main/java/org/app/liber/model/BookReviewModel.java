package org.app.liber.model;

public class BookReviewModel {

    private String reviewStr;
    private String bookName;
    private String ratingStars;
    private String readerName;

    public BookReviewModel(String reviewStr, String bookName, String ratingStars, String readerName) {
        this.reviewStr = reviewStr;
        this.bookName = bookName;
        this.ratingStars = ratingStars;
        this.readerName = readerName;
    }

    public String getReviewStr() {
        return reviewStr;
    }

    public String getBookName() {
        return bookName;
    }

    public String getRatingStars() {
        return ratingStars;
    }

    public String getReaderName() {
        return readerName;
    }
}

