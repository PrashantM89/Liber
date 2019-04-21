package org.app.liber.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.app.liber.R;
import org.app.liber.model.UserTransactionModel;

import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactioViewHolder> {

    private Context context;
    private List<UserTransactionModel> lstUsrTx;

    public TransactionAdapter(Context context, List<UserTransactionModel> lstUsrTx) {
        this.context = context;
        this.lstUsrTx = lstUsrTx;
    }

    @Override
    public TransactioViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.transaction_item, null);
        return new TransactioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TransactioViewHolder holder, int position) {

        UserTransactionModel model = lstUsrTx.get(position);

        holder.txnId.setText(model.getTxId());
        holder.txnDate.setText(model.getTxDate());
        holder.txnStatus.setText(model.getTxStatus());
        holder.txnMode.setText(model.getTxMode());
        holder.txnDeliveryStatus.setText(model.getDeliveryStatus());

    }

    @Override
    public int getItemCount() {
        return lstUsrTx.size();
    }

    class TransactioViewHolder extends RecyclerView.ViewHolder{

        TextView txnId, txnDate, txnStatus, txnMode, txnDeliveryStatus;
        ImageView img;

        public TransactioViewHolder(View itemView) {
            super(itemView);
            txnId  = (TextView)itemView.findViewById(R.id.textViewTitle);
            txnDate = (TextView)itemView.findViewById(R.id.textViewShortDesc);
            txnStatus = (TextView)itemView.findViewById(R.id.txnStatus);
            txnMode = (TextView)itemView.findViewById(R.id.textViewPrice);
            txnDeliveryStatus = (TextView)itemView.findViewById(R.id.textViewRating);
        }
    }
}
