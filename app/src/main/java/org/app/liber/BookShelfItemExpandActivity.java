package org.app.liber;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.app.liber.model.BookReviewModel;
import org.app.liber.model.BookshelveDataModel;
import org.w3c.dom.Text;

public class BookShelfItemExpandActivity extends AppCompatActivity {

    ImageView img;
    TextView bookname, bookAvailable, BookDuedate;
    Intent i;
    RatingBar ratingBar;
    TextView ratingTxt;
    Button submitReview;
    EditText bookReviewStr;
    DatabaseHelper databaseHelper;
    String ratingBarCount;

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
                    int ratingInt = (int) rating;
                    ratingBarCount = String.valueOf(ratingInt);

                    //Toast.makeText(getApplicationContext(), String.valueOf(rating), Toast.LENGTH_SHORT).show();
                    switch (ratingInt){
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
               databaseHelper = new DatabaseHelper(getApplicationContext());
               BookReviewModel bookReviewModel = new BookReviewModel(bookReviewStr.getText().toString(),bookname.getText().toString(),ratingBarCount,"Prashant");
               databaseHelper.addReview(bookReviewModel);
           }
       });
    }
}
