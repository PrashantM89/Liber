package org.app.liber.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.app.liber.R;
import org.app.liber.RentSummaryActivity;

import org.app.liber.pojo.BookshelfPojo;

import java.util.ArrayList;

public class MoreByAuthorAdapter extends RecyclerView.Adapter<MoreByAuthorAdapter.MyViewHolder>{

    Context context;
    ArrayList<BookshelfPojo> lstLibraryBooks;

    public void setLstLibraryBooks(ArrayList<BookshelfPojo> lstLibraryBooks) {
        this.lstLibraryBooks = lstLibraryBooks;
    }

    public MoreByAuthorAdapter(Context context, ArrayList<BookshelfPojo> lstLibraryBooks) {
        this.context = context;
        this.lstLibraryBooks = lstLibraryBooks;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v;

        v = LayoutInflater.from(context).inflate(R.layout.cardview_item_book,viewGroup,false);
        MoreByAuthorAdapter.MyViewHolder viewHolder = new MoreByAuthorAdapter.MyViewHolder(v);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(MoreByAuthorAdapter.MyViewHolder myViewHolder, int i) {
        myViewHolder.tvBookName.setText(lstLibraryBooks.get(i).getTitle());
        myViewHolder.userIdTxt.setText(lstLibraryBooks.get(i).getU_id());
        Picasso.with(context).load(lstLibraryBooks.get(i).getCoverImgUrl()).into(myViewHolder.ivCover);
    }


    @Override
    public int getItemCount() {
        return lstLibraryBooks.size();
    }

    public  class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView tvBookName;
        private TextView tvAuthor;
        private ImageView ivCover;
        private CardView rentItBttn;
        private TextView userIdTxt;

        public MyViewHolder(View itemView) {
            super(itemView);

            tvBookName = (TextView)itemView.findViewById(R.id.cardview_book_title_id);
            userIdTxt = (TextView)itemView.findViewById(R.id.cardview_book_usr_id);
            ivCover = (ImageView)itemView.findViewById(R.id.cardview_book_img_id);
            rentItBttn = (CardView) itemView.findViewById(R.id.card_view_main_id);
            rentItBttn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent2 = new Intent(view.getContext(), RentSummaryActivity.class);
                    BookshelfPojo lib = new BookshelfPojo();
                    lib.setTitle(lstLibraryBooks.get(getPosition()).getTitle());
                    lib.setAuthor(lstLibraryBooks.get(getPosition()).getAuthor());
                    lib.setCoverImgUrl(lstLibraryBooks.get(getPosition()).getCoverImgUrl());
                    lib.setDescription(lstLibraryBooks.get(getPosition()).getDescription());
                    lib.setGenre(lstLibraryBooks.get(getPosition()).getGenre());
                    lib.setRating(lstLibraryBooks.get(getPosition()).getRating());
                    lib.setU_id(lstLibraryBooks.get(getPosition()).getU_id());
                    lib.setAvailable(lstLibraryBooks.get(getPosition()).getAvailable());

                    intent2.putExtra("LibraryBookDetail",lib);
                    intent2.putExtra("LibraryObject",lstLibraryBooks);
                    intent2.setFlags(intent2.FLAG_ACTIVITY_NEW_TASK);
                    view.getContext().startActivity(intent2);
                }
            });
        }
    }

}
