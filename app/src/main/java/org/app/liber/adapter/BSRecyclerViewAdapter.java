package org.app.liber.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.app.liber.BookShelfItemExpandActivity;
import org.app.liber.LiberApiBase;
import org.app.liber.LiberEndpointInterface;
import org.app.liber.helper.DatabaseHelper;
import org.app.liber.R;
import org.app.liber.helper.DateUtil;
import org.app.liber.helper.ToastUtil;
import org.app.liber.model.Book;
import org.app.liber.pojo.BookshelfPojo;
import org.app.liber.pojo.WalletPojo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BSRecyclerViewAdapter extends RecyclerView.Adapter<BSRecyclerViewAdapter.MyViewHolder> {

    Context context;
    List<BookshelfPojo> lstBSBooks;
    DatabaseHelper db;
    private ProgressDialog progressDialog;
    private LiberEndpointInterface service;

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
        service = LiberApiBase.getRetrofitInstance().create(LiberEndpointInterface.class);
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(context.getString(R.string.deleting_bookshelf_label));
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final BSRecyclerViewAdapter.MyViewHolder myViewHolder, final int i) {
        myViewHolder.tvBookName.setText(lstBSBooks.get(i).getTitle());
       // myViewHolder.tvAuthor.setText(lstBSBooks.get(i).getAuthor());
        if("Y".equalsIgnoreCase(lstBSBooks.get(i).getAvailable())){
            myViewHolder.tvAvailibility.setText("Your book");
        }else{

            myViewHolder.tvAvailibility.setText("Reader:"+lstBSBooks.get(i).getReader());
        }
        Picasso.with(context).load(lstBSBooks.get(i).getCoverImgUrl()).into(myViewHolder.ivCover);

        if(lstBSBooks.get(i).getDor() !=null ) {
            myViewHolder.tvDueDate.setText("Date of Return: "+lstBSBooks.get(i).getDor());
        }else{
            myViewHolder.tvDueDate.setText("");
        }
        myViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                int position = myViewHolder.getAdapterPosition();
                if(myViewHolder.tvDueDate.getText().equals(null) || myViewHolder.tvDueDate.getText().equals("")) {
                    if (position != RecyclerView.NO_POSITION) {
                        confirmDeletDialog(position);
                    }
                    //confirmDeletDialog(myViewHolder.getAdapterPosition());
                }else{
                            AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                            dialog.setMessage("Book is being read.");
                            dialog.setTitle("Can't remove this book.");
                            dialog.setCancelable(false);
                            dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int j) {
                            return;
                        }
                    });
                    dialog.create().show();
                }
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
                b.setDor(lstBSBooks.get(position).getDor());
                b.setMobile(lstBSBooks.get(position).getMobile());
                b.setReader(lstBSBooks.get(position).getReader());
                b.setU_id(lstBSBooks.get(position).getU_id());
                intent.putExtra("bookshelfBooks",  b);

                  context.startActivity(intent);
            }
        });
    }

    public void confirmDeletDialog(final int p){
            AlertDialog.Builder dialog = new AlertDialog.Builder(context);
            dialog.setMessage("Removing will make it unavailable for others to rent.");
            dialog.setTitle("Remove book "+lstBSBooks.get(p).getTitle()+" from bookshelf?");
            dialog.setCancelable(false);
            dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int j) {
                updateBackend(p);
                removeItem(p);
                updateWalletIfEarlyRemove(lstBSBooks.get(p));
            }
        });

        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        dialog.create().show();
    }

    private void updateWalletIfEarlyRemove(BookshelfPojo bookshelfPojo) {

        WalletPojo money = new WalletPojo();
        money.setWuid(bookshelfPojo.getU_id());
        money.setWmob(bookshelfPojo.getMobile());
        money.setWamntAdded("-3");
        money.setWbookDate(DateUtil.getDate(Calendar.getInstance().getTime()));
        money.setWdelete("N");
        money.setWbookName(bookshelfPojo.getTitle());

        Call<ResponseBody> call = LiberApiBase.getRetrofitInstance().create(LiberEndpointInterface.class).insertMoneyInToWallet(money);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Toast.makeText(context,"Wallet Updated",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context,"Failed updating wallet : "+t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateBackend(final int p){
        BookshelfPojo book = lstBSBooks.get(p);
        book.setDelete("Y");
        Call<ArrayList<BookshelfPojo>> call = service.deleteBook(book);
        call.enqueue(new Callback<ArrayList<BookshelfPojo>>() {
            @Override
            public void onResponse(Call<ArrayList<BookshelfPojo>> call, Response<ArrayList<BookshelfPojo>> response) {

                if (response.code() == 200) {
                    progressDialog.dismiss();
                    ToastUtil.showToast(context,"Book removed from your shelf successfully");
                } else {
                    progressDialog.dismiss();
                    ToastUtil.showToast(context,"Something went wrong while removing this book.");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<BookshelfPojo>> call, Throwable t) {
                progressDialog.dismiss();

            }
        });
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
        private TextView tvDueDate;
        private ImageView ivCover;


        public MyViewHolder(View itemView) {
            super(itemView);

            tvBookName = (TextView)itemView.findViewById(R.id.bookshelve_title);
            //tvAuthor = (TextView)itemView.findViewById(R.id.bookshelve_authors);
            tvAvailibility = (TextView)itemView.findViewById(R.id.bookshelve_availability_txt_id);
            tvDueDate = (TextView)itemView.findViewById(R.id.bookshelve_duedate_txt_id);
            ivCover = (ImageView)itemView.findViewById(R.id.bookshelve_book_cover);

        }

    }
}
