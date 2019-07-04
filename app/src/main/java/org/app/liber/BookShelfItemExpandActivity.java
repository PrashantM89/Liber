package org.app.liber;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;

import org.app.liber.helper.DateUtil;
import org.app.liber.pojo.BookshelfPojo;
import org.app.liber.pojo.UserReview;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookShelfItemExpandActivity extends AppCompatActivity {

    private ImageView img;
    private TextView bookname, bookAvailable, BookDuedate;
    private Intent i;
    private RatingBar ratingBar;
    private TextView ratingTxt;
    private Button returnBookBtn;
    private Button submitReview;
    private EditText bookReviewStr;
    private String ratingBarCount;
    private LiberEndpointInterface service;
    private ProgressDialog progressDialog;
    private SharedPreferences pref;
    private BookshelfPojo returnBookDetail;
    private BookshelfPojo b;
    private LinearLayout returnBtnLLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_shelf_item_expand);

        ratingBar = (RatingBar)findViewById(R.id.ratingBar);
        ratingTxt = (TextView)findViewById(R.id.rating_text_id);
        img = (ImageView)findViewById(R.id.rent_book_img_cover);
        bookname = (TextView)findViewById(R.id.expand_book_title_id);
        returnBookBtn = (Button)findViewById(R.id.return_btn_id);
        submitReview = (Button)findViewById(R.id.submit_review_btn_id);
        bookReviewStr = (EditText)findViewById(R.id.book_review_text);
        returnBtnLLayout = (LinearLayout)findViewById(R.id.return_btn_layout_id);
        returnBookDetail = new BookshelfPojo();
        ratingBarCount = "";
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        service = LiberApiBase.getRetrofitInstance().create(LiberEndpointInterface.class);
        progressDialog = new ProgressDialog(BookShelfItemExpandActivity.this);
        progressDialog.setMessage(getApplicationContext().getString(R.string.processing_label));
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);

        i = getIntent();

        b = (BookshelfPojo) i.getSerializableExtra("bookshelfBooks");

        Picasso.with(getApplicationContext()).load(b.getCoverImgUrl()).into(img);
        bookname.setText(b.getTitle());
        ratingTxt.setText(R.string.rating_default);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    float ratingInt =  rating;
                    ratingBarCount = String.valueOf(ratingInt);

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

        showReturnBtn();

       submitReview.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
            progressDialog.show();

            String today = DateUtil.getDate(new Date());

            UserReview review = new UserReview();
            review.setUbook(bookname.getText().toString());
            review.setUid(pref.getString("USER_NAME","unknown"));
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

       returnBookBtn.setOnClickListener(new View.OnClickListener() {

           @Override
           public void onClick(View view) {
               progressDialog.show();
               returnBookDetail = b;
               Call<ResponseBody> call = LiberApiBase.getRetrofitInstance().create(LiberEndpointInterface.class).returnBook(b);

               call.enqueue(new Callback<ResponseBody>() {
                   @Override
                   public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                       progressDialog.dismiss();
                       System.out.println("-------- "+call.request().url());
                       Toast.makeText(getApplicationContext(),"Book return requested.",Toast.LENGTH_SHORT).show();
                   }

                   @Override
                   public void onFailure(Call<ResponseBody> call, Throwable t) {
                       progressDialog.dismiss();
                       Toast.makeText(getApplicationContext(),"Book return request failed : "+t.getMessage(),Toast.LENGTH_SHORT).show();
                   }
               });

           }
       });
    }

    private void showReturnBtn() {
        
        if(pref.getString("USER_NAME","unknown").equals(b.getU_id())){
            returnBtnLLayout.setVisibility(View.GONE);
        }else{
            returnBtnLLayout.setVisibility(View.VISIBLE);
        }
    }
}
