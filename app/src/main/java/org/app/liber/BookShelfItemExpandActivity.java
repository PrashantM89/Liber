package org.app.liber;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import org.app.liber.model.BookshelveDataModel;
import org.app.liber.pojo.UserReview;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookShelfItemExpandActivity extends AppCompatActivity {

    ImageView img;
    TextView bookname, bookAvailable, BookDuedate;
    Intent i;
    RatingBar ratingBar;
    TextView ratingTxt;
    Button submitReview;
    EditText bookReviewStr;
    String ratingBarCount;
    LiberEndpointInterface service;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_shelf_item_expand);

        ratingBar = (RatingBar)findViewById(R.id.ratingBar);
        ratingTxt = (TextView)findViewById(R.id.rating_text_id);
        img = (ImageView)findViewById(R.id.rent_book_img_cover);
        bookname = (TextView)findViewById(R.id.expand_book_title_id);
        submitReview = (Button)findViewById(R.id.submit_review_btn_id);
        bookReviewStr = (EditText)findViewById(R.id.book_review_text);
        ratingBarCount = "";
        service = LiberApiBase.getRetrofitInstance().create(LiberEndpointInterface.class);

//        bookAvailable = (TextView)findViewById(R.id.expand_book_availability_id);
//        BookDuedate = (TextView)findViewById(R.id.expand_book_duedate_id);
        i = getIntent();

        BookshelveDataModel b = (BookshelveDataModel) i.getSerializableExtra("bookshelfBooks");

        Picasso.with(getApplicationContext()).load(b.getSmallThumbnailLink()).into(img);
        bookname.setText(b.getTitle());
//        if(b.getIsAvailable().equals("Y")){
//            bookAvailable.setText("Available");
//        }else{
//            bookAvailable.setText("You are reading");
//        }
//        BookDuedate.setText(b.getDueDate());

        ratingTxt.setText(R.string.rating_default);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    float ratingInt =  rating;
                    ratingBarCount = String.valueOf(ratingInt);

                    //Toast.makeText(getApplicationContext(), String.valueOf(rating), Toast.LENGTH_SHORT).show();
                    switch ((int) ratingInt){
                        case 1:
                            ratingTxt.setText(R.string.rating_1);
                            ratingTxt.setVisibility(View.VISIBLE);
                            break;
                        case 2:
                            ratingTxt.setText(R.string.rating_2);
                            ratingTxt.setVisibility(View.VISIBLE);
                            break;
                        case 3:
                            ratingTxt.setText(R.string.rating_3);
                            ratingTxt.setVisibility(View.VISIBLE);
                            break;
                        case 4:
                            ratingTxt.setText(R.string.rating_4);
                            ratingTxt.setVisibility(View.VISIBLE);
                            break;
                        case 5:
                            ratingTxt.setText(R.string.rating_5);
                            ratingTxt.setVisibility(View.VISIBLE);
                            break;
                        default:
                            ratingTxt.setText(R.string.rating_default);
                            break;
                    }
                }

        });

       submitReview.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

            progressDialog = new ProgressDialog(BookShelfItemExpandActivity.this);
            progressDialog.setMessage(getApplicationContext().getString(R.string.processing_review_upload_label));
            progressDialog.setCancelable(false);
            progressDialog.setIndeterminate(true);
            progressDialog.show();

            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            String today = formatter.format(new Date());

            UserReview review = new UserReview();
            review.setUbook(bookname.getText().toString());
            //To-Do: Make it dynamic.
            review.setUid("Prashant");
            review.setUreview(bookReviewStr.getText().toString());
            review.setUstar(ratingBarCount);
            review.setUdate(today);

            Call<ResponseBody> call = LiberApiBase.getRetrofitInstance().create(LiberEndpointInterface.class).insertUserReviewOnABook(review);

            call.enqueue(new Callback<ResponseBody>() {
                   @Override
                   public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                       progressDialog.dismiss();
                       Toast.makeText(getApplicationContext(),"Review Saved",Toast.LENGTH_SHORT).show();
                   }

                   @Override
                   public void onFailure(Call<ResponseBody> call, Throwable t) {
                       progressDialog.dismiss();
                       Toast.makeText(getApplicationContext(),"Failed uploading review : "+t.getMessage(),Toast.LENGTH_SHORT).show();
                   }
            });


           }
       });
    }
}
