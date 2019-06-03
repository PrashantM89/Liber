package org.app.liber.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.app.liber.BookShelfItemExpandActivity;
import org.app.liber.helper.DatabaseHelper;
import org.app.liber.R;
import org.app.liber.pojo.BookshelfPojo;

import java.util.List;

public class BSRecyclerViewAdapter extends RecyclerView.Adapter<BSRecyclerViewAdapter.MyViewHolder> {

    Context context;
    List<BookshelfPojo> lstBSBooks;
    DatabaseHelper db;

    public BSRecyclerViewAdapter(Context context, List<BookshelfPojo> lstBSBooks) {
        this.context = context;
        this.lstBSBooks = lstBSBooks;
    }

    public void setLstBSBooks(List<BookshelfPojo> lstBSBooks) {
        this.lstBSBooks = lstBSBooks;
    }

    @Override
    public BSRecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v;
        v = LayoutInflater.from(context).inflate(R.layout.item_bookshelve,viewGroup,false);
        BSRecyclerViewAdapter.MyViewHolder viewHolder = new BSRecyclerViewAdapter.MyViewHolder(v);
        db = new DatabaseHelper(context);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final BSRecyclerViewAdapter.MyViewHolder myViewHolder, final int i) {
        myViewHolder.tvBookName.setText(lstBSBooks.get(i).getTitle());
       // myViewHolder.tvAuthor.setText(lstBSBooks.get(i).getAuthor());
        if("Y".equalsIgnoreCase(lstBSBooks.get(i).getAvailable())){
            myViewHolder.tvAvailibility.setText("With you");
        }else{

            myViewHolder.tvAvailibility.setText("Reader:"+lstBSBooks.get(i).getReader());
        }
        Picasso.with(context).load(lstBSBooks.get(i).getCoverImgUrl()).into(myViewHolder.ivCover);


        myViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                int position = myViewHolder.getAdapterPosition();
                if(position!=RecyclerView.NO_POSITION){
                    confirmDeletDialog(position);
                }
                //confirmDeletDialog(myViewHolder.getAdapterPosition());
                return true;
            }
        });

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), BookShelfItemExpandActivity.class);
                int position = myViewHolder.getAdapterPosition();
                BookshelfPojo b = new BookshelfPojo();
                b.setTitle(lstBSBooks.get(position).getTitle());
                b.setCoverImgUrl(lstBSBooks.get(position).getCoverImgUrl());
                b.setAvailable(lstBSBooks.get(position).getAvailable());
//              b.setDueDate(lstBSBooks.get(position).getDueDate());
                intent.putExtra("bookshelfBooks",  b);

                  context.startActivity(intent);
//                ActivityOptions options = (ActivityOptions) ActivityOptions.makeSceneTransitionAnimation((Activity) context,pairs);
//                context.startActivity(intent,options.toBundle());

            }
        });
    }

    public void confirmDeletDialog(final int p){
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setMessage("Removing will make it unavailable for others to rent.");
        dialog.setTitle("Remove this book from bookshelf?");
        dialog.setCancelable(false);
        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int j) {

                //lstBSBooks.remove(p);
                //notifyItemRemoved(p);
                db.removeData(lstBSBooks.get(p).getTitle());
                removeItem(p);

            }
        });

        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        dialog.create().show();
    }

    public void removeItem(int position) {
        final BookshelfPojo model = lstBSBooks.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,lstBSBooks.size());
    }


    private void removeDataFromList(BookshelfPojo data){
        int currentPosition = lstBSBooks.indexOf(data);
        lstBSBooks.remove(currentPosition);
        notifyItemRemoved(currentPosition);
    }

    @Override
    public int getItemCount() {
        return lstBSBooks.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView tvBookName;
       // private TextView tvAuthor;
        private TextView tvAvailibility;
        //private TextView tvDueDate;
        private ImageView ivCover;


        public MyViewHolder(View itemView) {
            super(itemView);

            tvBookName = (TextView)itemView.findViewById(R.id.bookshelve_title);
            //tvAuthor = (TextView)itemView.findViewById(R.id.bookshelve_authors);
            tvAvailibility = (TextView)itemView.findViewById(R.id.bookshelve_availability_txt_id);
          //  tvDueDate = (TextView)itemView.findViewById(R.id.bookshelve_duedate_txt_id);
            ivCover = (ImageView)itemView.findViewById(R.id.bookshelve_book_cover);

        }

    }
}
