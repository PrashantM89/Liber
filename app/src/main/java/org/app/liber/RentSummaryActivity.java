package org.app.liber;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import org.app.liber.activity.SelectTenureActivity;
import org.app.liber.adapter.MoreByAuthorAdapter;
import org.app.liber.adapter.ReaderReviewAdapter;
import org.app.liber.model.BookReviewModel;
import org.app.liber.model.LibraryDataModel;
import java.util.ArrayList;

@SuppressWarnings("unchecked")
public class RentSummaryActivity extends AppCompatActivity {

    ImageView rentBookCover;
    TextView rentBookTitle;
    TextView rentBookAuthor;
    TextView rentBookDesc;
    TextView rentBookGenre;
    TextView rentBookRating;
    Button orderSummaryButton;
    MoreByAuthorAdapter recyclerViewAdapter;
    ReaderReviewAdapter readerReviewAdapter;
    private RecyclerView recyclerView;
    private RecyclerView reviewRecyclerView;
    ArrayList<LibraryDataModel> lstOfMoreBooksByThisAuthor;
    ArrayList<BookReviewModel> lstOfReaderReviews;
    DatabaseHelper db;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rent_summary_page);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_backspace);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        lstOfMoreBooksByThisAuthor = new ArrayList<>();
        lstOfReaderReviews =  new ArrayList<>();
        recyclerView = (RecyclerView)findViewById(R.id.more_author_book_recyclerview);
        reviewRecyclerView = (RecyclerView)findViewById(R.id.reader_review_recyclerview);

        rentBookCover=(ImageView)findViewById(R.id.rent_summary_img_cover);
        rentBookTitle=(TextView)findViewById(R.id.rent_summary_book_title);
        rentBookAuthor=(TextView)findViewById(R.id.rent_summary_book_author);
        rentBookDesc = (TextView) findViewById(R.id.bookDescription);
        rentBookGenre = (TextView) findViewById(R.id.rent_summary_book_genre_id);
        rentBookRating = (TextView)findViewById(R.id.rating_id);
        orderSummaryButton = (Button)findViewById(R.id.butSubscribe);
        rentBookDesc.setMovementMethod(new ScrollingMovementMethod());


        Intent i = getIntent();
        final LibraryDataModel l = (LibraryDataModel)i.getSerializableExtra("LibraryBookDetail");
        final ArrayList<LibraryDataModel> ll = (ArrayList<LibraryDataModel>) i.getSerializableExtra("LibraryObject");
        for(LibraryDataModel ldm:ll){
            if(ldm.getAuthor().contains(l.getAuthor()) && !ldm.getBookTitle().equals(l.getBookTitle())) {
                lstOfMoreBooksByThisAuthor.add(ldm);
            }
        }

        recyclerViewAdapter = new MoreByAuthorAdapter(getApplicationContext(),lstOfMoreBooksByThisAuthor);

        loadReviews(l.getBookTitle());

        GridLayoutManager glm = new GridLayoutManager(this,1);
        glm.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(glm);
        recyclerView.setAdapter(recyclerViewAdapter);


        rentBookTitle.setText(l.getBookTitle());
        rentBookAuthor.setText(l.getAuthor());
        rentBookDesc.setText(l.getDescription());
        rentBookGenre.setText(l.getGenre());
        rentBookRating.setText(l.getAvgRating());
        Picasso.with(getApplicationContext()).load(l.getSmallThumbnailLink()).resize(150,200).into(rentBookCover);
        makeTextViewResizable(rentBookDesc, 3, "Read More", true);
        orderSummaryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), SelectTenureActivity.class);
                i.putExtra("order_book",l);
                startActivity(i);
            }
        });


    }


    public ArrayList<BookReviewModel> getReaderReviewData(String bookName){
        db = new DatabaseHelper(getApplicationContext());
        ArrayList<BookReviewModel> lst = new ArrayList<>();
        BookReviewModel bookReviewModel;
        Cursor result = db.getReview();
        while (result.moveToNext()){
            bookReviewModel = new BookReviewModel(result.getString(result.getColumnIndex(result.getColumnName(2).toString())),result.getString(result.getColumnIndex(result.getColumnName(3).toString())),result.getString(result.getColumnIndex(result.getColumnName(1).toString())),result.getString(result.getColumnIndex(result.getColumnName(0).toString())));
            if(bookReviewModel.getBookName().trim().equals(bookName.trim())){
                lst.add(bookReviewModel);
            }

        }
        return lst;
    }

    public static void makeTextViewResizable(final TextView rentBookDesc, final int maxLine, final String expandText, final boolean viewMore) {

        if (rentBookDesc.getTag() == null) {
            rentBookDesc.setTag(rentBookDesc.getText());
        }
        ViewTreeObserver vto = rentBookDesc.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {
                String text;
                int lineEndIndex;
                ViewTreeObserver obs = rentBookDesc.getViewTreeObserver();
                obs.removeGlobalOnLayoutListener(this);
                if (maxLine == 0) {
                    lineEndIndex = rentBookDesc.getLayout().getLineEnd(0);
                    text = rentBookDesc.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                } else if (maxLine > 0 && rentBookDesc.getLineCount() >= maxLine) {
                    lineEndIndex = rentBookDesc.getLayout().getLineEnd(maxLine - 1);
                    text = rentBookDesc.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                } else {
                    lineEndIndex = rentBookDesc.getLayout().getLineEnd(rentBookDesc.getLayout().getLineCount() - 1);
                    text = rentBookDesc.getText().subSequence(0, lineEndIndex) + " " + expandText;
                }
                rentBookDesc.setText(text);
                rentBookDesc.setMovementMethod(LinkMovementMethod.getInstance());
                rentBookDesc.setText(
                        addClickablePartTextViewResizable(Html.fromHtml(rentBookDesc.getText().toString()), rentBookDesc, lineEndIndex, expandText,
                                viewMore), TextView.BufferType.SPANNABLE);
            }
        });
    }

    private static SpannableStringBuilder addClickablePartTextViewResizable(final Spanned strSpanned, final TextView rentBookDesc,
                                                                            final int maxLine, final String spanableText, final boolean viewMore) {
        String str = strSpanned.toString();
        SpannableStringBuilder ssb = new SpannableStringBuilder(strSpanned);

        if (str.contains(spanableText)) {
            ssb.setSpan(new ClickableSpan() {

                @Override
                public void onClick(View widget) {
                    rentBookDesc.setLayoutParams(rentBookDesc.getLayoutParams());
                    rentBookDesc.setText(rentBookDesc.getTag().toString(), TextView.BufferType.SPANNABLE);
                    rentBookDesc.invalidate();
                    if (viewMore) {
                        makeTextViewResizable(rentBookDesc, -1, "Read Less", false);
                    } else {
                        makeTextViewResizable(rentBookDesc, 3, "Read More", true);
                    }

                }
            }, str.indexOf(spanableText), str.indexOf(spanableText) + spanableText.length(), 0);

        }
        return ssb;

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadReviews(rentBookTitle.getText().toString());

    }

    private void loadReviews(String bookName){
        db = new DatabaseHelper(getApplicationContext());
        ArrayList<BookReviewModel> lst = new ArrayList<>();
        BookReviewModel bookReviewModel;
        Cursor result = db.getReview();
        while (result.moveToNext()){
            bookReviewModel = new BookReviewModel(result.getString(result.getColumnIndex(result.getColumnName(2).toString())),result.getString(result.getColumnIndex(result.getColumnName(3).toString())),result.getString(result.getColumnIndex(result.getColumnName(1).toString())),result.getString(result.getColumnIndex(result.getColumnName(0).toString())));
            if(bookReviewModel.getBookName().trim().equals(bookName.trim())){
                lst.add(bookReviewModel);
            }else{
                lst.add(bookReviewModel);
            }
        }
        if(!lst.isEmpty()){
            readerReviewAdapter = new ReaderReviewAdapter(getApplicationContext(),lst);
            reviewRecyclerView.setAdapter(readerReviewAdapter);
            readerReviewAdapter.notifyDataSetChanged();
        }
    }
}
