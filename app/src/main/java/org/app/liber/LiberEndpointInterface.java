package org.app.liber;

import org.app.liber.pojo.BookshelfPojo;
import org.app.liber.pojo.UserPojo;
import org.app.liber.pojo.UserReview;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface LiberEndpointInterface {

    @GET("bookshelf/books")
    Call<ArrayList<BookshelfPojo>> getBooks();

    @POST("bookshelf/upload")
    Call<ResponseBody> insertBookInBookshelf(@Body BookshelfPojo bookshelfPojo, @Query("u_id")String u_id);

    //Reviews
    @GET("review/reviews")
    Call<ArrayList<UserReview>> getUserReviews();

    @POST("review/uploadReview")
    Call<ResponseBody> insertUserReviewOnABook(@Body UserReview userReview);


}
