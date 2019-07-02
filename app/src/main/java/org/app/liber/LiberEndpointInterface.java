package org.app.liber;

import org.app.liber.pojo.BookshelfPojo;
import org.app.liber.pojo.TransactionPojo;
import org.app.liber.pojo.UserPojo;
import org.app.liber.pojo.UserReview;
import org.app.liber.pojo.WalletPojo;
import java.util.ArrayList;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface LiberEndpointInterface {

    //notifications
//    @GET("bookshelf/userNotifications")
//    Call<ArrayList<>> getUserNotification();

    //wallet
    @GET("wallet/userWaller")
    Call<ArrayList<WalletPojo>> getUserWalletData(@Query("wmob")String wmob);

    @POST("wallet/addMoney")
    Call<ResponseBody> insertMoneyInToWallet(@Body WalletPojo walletPojo);

    //bookshelf
    @GET("bookshelf/books")
    Call<ArrayList<BookshelfPojo>> getBooks();

    @POST("bookshelf/upload")
    Call<ResponseBody> insertBookInBookshelf(@Body BookshelfPojo bookshelfPojo, @Query("u_id")String u_id);

    @GET("bookshelf/userBooks")
    Call<ArrayList<BookshelfPojo>> getUserBooks(@Query("u_id")String u_id);

    @POST("bookshelf/deleteBook")
    Call<ArrayList<BookshelfPojo>> deleteBook(@Body BookshelfPojo bookshelfPojo);

    //Reviews
    @GET("review/reviews")
    Call<ArrayList<UserReview>> getUserReviews();

    @POST("review/uploadReview")
    Call<ResponseBody> insertUserReviewOnABook(@Body UserReview userReview);

    //Txn
    @GET("txn/transactions")
    Call<ArrayList<TransactionPojo>> getAllUserTxns(@Query("txMob")String txMob);

    @POST("txn/createUsrTxn")
    Call<ResponseBody> insertUserTxn(@Body TransactionPojo txn);

    //User
    @POST("user/createUser")
    Call<ResponseBody> createNewUser(@Body UserPojo userPojo);

    @GET("user/user/{umob}")
    Call<UserPojo> getUserDetails(@Path("umob") String umob);
}
