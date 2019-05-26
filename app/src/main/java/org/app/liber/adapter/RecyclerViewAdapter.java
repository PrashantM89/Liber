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
import org.app.liber.model.LibraryDataModel;
import org.app.liber.pojo.BookshelfPojo;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    Context context;
    //ArrayList<LibraryDataModel> lstLibraryBooks;
    ArrayList<BookshelfPojo> lstLibraryBooks2;
    String uploadedBy;

    public void setLstLibraryBooks(ArrayList<BookshelfPojo> lstLibraryBooks2) {
        this.lstLibraryBooks2 = lstLibraryBooks2;
    }

    public RecyclerViewAdapter(Context context, ArrayList<BookshelfPojo> lstLibraryBooks2) {
        this.context = context;
        this.lstLibraryBooks2 = lstLibraryBooks2;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v;

        //v = LayoutInflater.from(context).inflate(R.layout.item_library,viewGroup,false);
        v = LayoutInflater.from(context).inflate(R.layout.cardview_item_book,viewGroup,false);
        MyViewHolder viewHolder = new MyViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {
        myViewHolder.tvBookName.setText(lstLibraryBooks2.get(i).getTitle());
        //myViewHolder.tvAuthor.setText(lstLibraryBooks.get(i).getAuthor());
        myViewHolder.tvUserName.setText(lstLibraryBooks2.get(i).getU_id());
        Picasso.with(context).load(lstLibraryBooks2.get(i).getCoverImgUrl()).into(myViewHolder.ivCover);
    }

    @Override
    public int getItemCount() {
        return lstLibraryBooks2.size();
    }

    public  class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView tvBookName;
        private TextView tvAuthor;
        private TextView tvUserName;
        private ImageView ivCover;
        private CardView rentItBttn;

        public MyViewHolder(View itemView) {
            super(itemView);

            tvBookName = (TextView)itemView.findViewById(R.id.cardview_book_title_id);
            //tvAuthor = (TextView)itemView.findViewById(R.id.library_authors);
            tvUserName = (TextView)itemView.findViewById(R.id.cardview_book_usr_id);
            ivCover = (ImageView)itemView.findViewById(R.id.cardview_book_img_id);
            rentItBttn = (CardView) itemView.findViewById(R.id.card_view_main_id);
            rentItBttn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), RentSummaryActivity.class);
                    BookshelfPojo lib = new BookshelfPojo();
                    lib.setTitle(lstLibraryBooks2.get(getPosition()).getTitle());
                    lib.setAuthor(lstLibraryBooks2.get(getPosition()).getAuthor());
                    lib.setCoverImgUrl(lstLibraryBooks2.get(getPosition()).getCoverImgUrl());
                    lib.setDescription(lstLibraryBooks2.get(getPosition()).getDescription());
                    lib.setGenre(lstLibraryBooks2.get(getPosition()).getGenre());
                    lib.setRating(lstLibraryBooks2.get(getPosition()).getRating());
                    lib.setU_id(lstLibraryBooks2.get(getPosition()).getU_id());

                    intent.putExtra("LibraryBookDetail",lib);
                    intent.putExtra("LibraryObject",lstLibraryBooks2);
                    view.getContext().startActivity(intent);
                }
            });
        }
    }

    public BookshelfPojo removeItem(int position) {
        final BookshelfPojo model = lstLibraryBooks2.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    public void addItem(int position, BookshelfPojo model) {
        lstLibraryBooks2.add(position, model);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final BookshelfPojo model = lstLibraryBooks2.remove(fromPosition);
        lstLibraryBooks2.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }

    public void animateTo(List<BookshelfPojo> models) {
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
    }

    private void applyAndAnimateRemovals(List<BookshelfPojo> newModels) {
        for (int i = lstLibraryBooks2.size() - 1; i >= 0; i--) {
            final BookshelfPojo model = lstLibraryBooks2.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<BookshelfPojo> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final BookshelfPojo model = newModels.get(i);
            if (!lstLibraryBooks2.contains(model)) {
                addItem(i, model);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<BookshelfPojo> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final BookshelfPojo model = newModels.get(toPosition);
            final int fromPosition = lstLibraryBooks2.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

}