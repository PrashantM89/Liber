package org.app.liber.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import org.app.liber.R;
import org.app.liber.pojo.UserReview;

import java.util.ArrayList;

public class ReaderReviewAdapter extends RecyclerView.Adapter<ReaderReviewAdapter.MyViewHolder>{

    Context context;
    ArrayList<UserReview> lstReaderReview;

    public void setLstReaderReview(ArrayList<UserReview> lstReaderReview) {
        this.lstReaderReview = lstReaderReview;
    }

    public ReaderReviewAdapter(Context context, ArrayList<UserReview> lstReaderReview) {
            this.context = context;
            this.lstReaderReview = lstReaderReview;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v;
            v = LayoutInflater.from(context).inflate(R.layout.cardview_item_review,viewGroup,false);
            ReaderReviewAdapter.MyViewHolder viewHolder = new ReaderReviewAdapter.MyViewHolder(v);
            return viewHolder;
        }
        @Override
        public void onBindViewHolder(ReaderReviewAdapter.MyViewHolder myViewHolder, int i) {
            myViewHolder.reviewerNameTxt.setText(lstReaderReview.get(i).getUid());
            myViewHolder.reviewerSummaryReview.setText(lstReaderReview.get(i).getUreview());
            myViewHolder.starTxt.setText(" "+lstReaderReview.get(i).getUstar());
        }


        @Override
        public int getItemCount() {
            return lstReaderReview.size();
        }

        public  class MyViewHolder extends RecyclerView.ViewHolder{

            private TextView reviewerNameTxt;
            private TextView reviewerSummaryReview;
            private TextView starTxt;

            public MyViewHolder(View itemView) {
                super(itemView);

                reviewerNameTxt = (TextView)itemView.findViewById(R.id.reviewer_name_id);
                reviewerSummaryReview = (TextView)itemView.findViewById(R.id.reviewer_review_txt_id);
                starTxt = (TextView)itemView.findViewById(R.id.reviewer_ratingbar_id);
            }
        }
    }