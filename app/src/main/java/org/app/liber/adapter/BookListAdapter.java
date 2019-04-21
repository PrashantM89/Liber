package org.app.liber.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.app.liber.helper.DatabaseHelper;
import org.app.liber.R;
import org.app.liber.helper.WalletTxn;
import org.app.liber.model.Book;
import org.app.liber.model.WalletModel;

import java.util.ArrayList;
import java.util.Date;


public class BookListAdapter extends RecyclerView.Adapter<BookListAdapter.ViewHolder> {
    private static final String LOG_TAG = BookListAdapter.class.getSimpleName();
    private final ArrayList<Book> mValues;
    private final Context mContext;
    WalletTxn txn;
    DatabaseHelper databaseHelper;

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
                    Book b = new Book(mValues.get(getPosition()).title,mValues.get(getPosition()).authors,mValues.get(getPosition()).smallThumbnailLink,mValues.get(getPosition()).description, mValues.get(getPosition()).genre,"");
                    databaseHelper.addData(b);
                    addPointsInWallet();
                }
            });
        }
    }

    private void addPointsInWallet() {
        txn = new WalletTxn(databaseHelper);
        WalletModel model = new WalletModel(1,"7338239977",new Date().toString());
        txn.addToWallet(model);
    }
}
