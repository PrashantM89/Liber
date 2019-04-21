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

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    Context context;
    ArrayList<LibraryDataModel> lstLibraryBooks;

    public void setLstLibraryBooks(ArrayList<LibraryDataModel> lstLibraryBooks) {
        this.lstLibraryBooks = lstLibraryBooks;
    }

    public RecyclerViewAdapter(Context context, ArrayList<LibraryDataModel> lstLibraryBooks) {
        this.context = context;
        this.lstLibraryBooks = lstLibraryBooks;
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
        myViewHolder.tvBookName.setText(lstLibraryBooks.get(i).getBookTitle());
        //myViewHolder.tvAuthor.setText(lstLibraryBooks.get(i).getAuthor());
        Picasso.with(context).load(lstLibraryBooks.get(i).getSmallThumbnailLink()).into(myViewHolder.ivCover);
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

        public MyViewHolder(View itemView) {
            super(itemView);

            tvBookName = (TextView)itemView.findViewById(R.id.cardview_book_title_id);
            //tvAuthor = (TextView)itemView.findViewById(R.id.library_authors);
            ivCover = (ImageView)itemView.findViewById(R.id.cardview_book_img_id);
            rentItBttn = (CardView) itemView.findViewById(R.id.card_view_main_id);
            rentItBttn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), RentSummaryActivity.class);
                    LibraryDataModel lib = new LibraryDataModel(lstLibraryBooks.get(getPosition()).getBookTitle().toString(),lstLibraryBooks.get(getPosition()).getAuthor().toString(),lstLibraryBooks.get(getPosition()).getSmallThumbnailLink().toString(),lstLibraryBooks.get(getPosition()).getDescription(),lstLibraryBooks.get(getPosition()).getGenre(),lstLibraryBooks.get(getPosition()).getAvgRating());
                    intent.putExtra("LibraryBookDetail",lib);
                    intent.putExtra("LibraryObject",lstLibraryBooks);
                    view.getContext().startActivity(intent);
                }
            });
        }
    }

    public LibraryDataModel removeItem(int position) {
        final LibraryDataModel model = lstLibraryBooks.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    public void addItem(int position, LibraryDataModel model) {
        lstLibraryBooks.add(position, model);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final LibraryDataModel model = lstLibraryBooks.remove(fromPosition);
        lstLibraryBooks.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }

    public void animateTo(List<LibraryDataModel> models) {
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
    }

    private void applyAndAnimateRemovals(List<LibraryDataModel> newModels) {
        for (int i = lstLibraryBooks.size() - 1; i >= 0; i--) {
            final LibraryDataModel model = lstLibraryBooks.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<LibraryDataModel> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final LibraryDataModel model = newModels.get(i);
            if (!lstLibraryBooks.contains(model)) {
                addItem(i, model);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<LibraryDataModel> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final LibraryDataModel model = newModels.get(toPosition);
            final int fromPosition = lstLibraryBooks.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

}
