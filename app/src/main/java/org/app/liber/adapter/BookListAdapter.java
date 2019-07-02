package org.app.liber.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
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
import org.app.liber.helper.DateUtil;
import org.app.liber.helper.WalletTxn;
import org.app.liber.model.Book;
import org.app.liber.model.WalletModel;
import org.app.liber.pojo.BookshelfPojo;
import org.app.liber.pojo.WalletPojo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BookListAdapter extends RecyclerView.Adapter<BookListAdapter.ViewHolder> {
    private static final String LOG_TAG = BookListAdapter.class.getSimpleName();
    private final ArrayList<Book> mValues;
    private final Context mContext;
    private WalletTxn txn;
    private DatabaseHelper databaseHelper;
    private ProgressDialog progressDialog;
    private SharedPreferences pref;
    private String userName;
    private String userMob;

    public BookListAdapter(ArrayList<Book> items, Context context) {
        mValues = items;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_booklist, parent, false);
        pref = PreferenceManager.getDefaultSharedPreferences(mContext);
        userName = pref.getString("USER_NAME","Unknown");
        userMob = pref.getString("USER_MOB","Unknown");
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mTitleView.setText(mValues.get(position).title);
        String authorsString = "by " + mValues.get(position).authors;
        holder.mAuthorsView.setText(authorsString);
        String smallThumbnailLink = mValues.get(position).smallThumbnailLink;
        Picasso.with(mContext).load(smallThumbnailLink).into(holder.mBookCoverView);

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
                    book.setAvailable("Y");
                    book.setDelete("N");
                    book.setU_id(userName);
                    book.setMobile(pref.getString("USER_MOB","Unknown"));

                    Call<ResponseBody> call = LiberApiBase.getRetrofitInstance().create(LiberEndpointInterface.class).insertBookInBookshelf(book,book.getU_id());

                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            progressDialog.dismiss();
                            Toast.makeText(mContext,"Book Uploaded",Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            progressDialog.dismiss();
                            Toast.makeText(mContext,"Failed uploading book : "+t.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });

                    addPointsInWallet(book);
                }
            });
        }

    }

    private void addPointsInWallet(BookshelfPojo b) {

        WalletPojo money = new WalletPojo();
        money.setWuid(b.getU_id());
        money.setWmob(userMob);
        money.setWamntAdded("3");
        money.setWbookDate(DateUtil.getDate(Calendar.getInstance().getTime()));
        money.setWdelete("N");
        money.setWbookName(b.getTitle());


        Call<ResponseBody> call = LiberApiBase.getRetrofitInstance().create(LiberEndpointInterface.class).insertMoneyInToWallet(money);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.dismiss();
                Toast.makeText(mContext,"Wallet Updated",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(mContext,"Failed updating wallet : "+t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }
}
