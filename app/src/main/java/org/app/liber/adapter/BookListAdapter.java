package org.app.liber.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.app.liber.LiberApiBase;
import org.app.liber.LiberEndpointInterface;
import org.app.liber.helper.DatabaseHelper;
import org.app.liber.R;
import org.app.liber.helper.WalletTxn;
import org.app.liber.model.Book;
import org.app.liber.model.WalletModel;
import org.app.liber.pojo.BookshelfPojo;

import java.util.ArrayList;
import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BookListAdapter extends RecyclerView.Adapter<BookListAdapter.ViewHolder> {
    private static final String LOG_TAG = BookListAdapter.class.getSimpleName();
    private final ArrayList<Book> mValues;
    private final Context mContext;
    WalletTxn txn;
    DatabaseHelper databaseHelper;
    ProgressDialog progressDialog;

    public BookListAdapter(ArrayList<Book> items, Context context) {
        mValues = items;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_booklist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mTitleView.setText(mValues.get(position).title);
        String authorsString = "by " + mValues.get(position).authors;
        holder.mAuthorsView.setText(authorsString);
        String smallThumbnailLink = mValues.get(position).smallThumbnailLink;
        Picasso.with(mContext).load(smallThumbnailLink).resize(90,90).into(holder.mBookCoverView);

    }

    @Override
    public int getItemCount() {
        if (mValues != null) {
            return mValues.size();
        } else {
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mTitleView;
        public final TextView mAuthorsView;
        public final ImageView mBookCoverView;
        public final Button mPutOnShelfBtnView;
        public Book mItem;

        public ViewHolder(View view) {
            super(view);

            mTitleView = (TextView) view.findViewById(R.id.title);
            mAuthorsView = (TextView) view.findViewById(R.id.authors);
            mBookCoverView = (ImageView) view.findViewById(R.id.book_cover);
            mPutOnShelfBtnView = (Button)view.findViewById(R.id.upload_bttn_id);
            databaseHelper = new DatabaseHelper(mContext);

            mPutOnShelfBtnView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Book b = new Book(mValues.get(getPosition()).title,mValues.get(getPosition()).authors,mValues.get(getPosition()).smallThumbnailLink,mValues.get(getPosition()).description, mValues.get(getPosition()).genre,"");
//                    databaseHelper.addData(b);

                    progressDialog = new ProgressDialog(mContext);
                    progressDialog.setMessage(mContext.getString(R.string.processing_label));
                    progressDialog.setCancelable(false);
                    progressDialog.setIndeterminate(true);
                    progressDialog.show();

                    BookshelfPojo book = new BookshelfPojo();
                    book.setTitle(mValues.get(getPosition()).title);
                    book.setAuthor(mValues.get(getPosition()).authors);
                    book.setCoverImgUrl(mValues.get(getPosition()).smallThumbnailLink);
                    book.setDescription(mValues.get(getPosition()).description);
                    book.setGenre(mValues.get(getPosition()).genre);
                    book.setRating(mValues.get(getPosition()).avgRating);

                    Call<ResponseBody> call = LiberApiBase.getRetrofitInstance().create(LiberEndpointInterface.class).insertBookInBookshelf(book,"Sandwista");

                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            progressDialog.dismiss();
                            System.out.println("---------------------- "+call.request().url());
                            Toast.makeText(mContext,"Book Uploaded",Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            progressDialog.dismiss();
                            System.out.println("---------------------- "+t.getMessage());
                            System.out.println("---------------------- "+call.request().url());
                            Toast.makeText(mContext,"Failed uploading book : "+t.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });


                    addPointsInWallet();
                }
            });
        }
    }

    private void addPointsInWallet() {
        txn = new WalletTxn(databaseHelper);
        WalletModel model = new WalletModel(3,"7338239977",new Date().toString());
        txn.addToWallet(model);
    }
}
