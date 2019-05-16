package org.app.liber;

import org.app.liber.pojo.BookshelfPojo;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface LiberEndpointInterface {

    @GET("bookshelf/books")
    Call<ArrayList<BookshelfPojo>> getBooks();
}
