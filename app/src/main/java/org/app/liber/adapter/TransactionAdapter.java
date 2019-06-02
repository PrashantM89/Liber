package org.app.liber.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import org.app.liber.R;
import org.app.liber.pojo.TransactionPojo;
import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactioViewHolder> {

    private Context context;
    private List<TransactionPojo> lstUsrTx;

    public TransactionAdapter(Context context, List<TransactionPojo> lstUsrTx) {
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

        TransactionPojo model = lstUsrTx.get(position);

        holder.txnId.setText(model.getTxId());
        holder.txnDate.setText(model.getTxDate());
        holder.txReturndate.setText(model.getTxReturnDate());
        holder.txnMode.setText(model.getTxPaymentMode());
        holder.txnDeliveryStatus.setText(model.getTxDeliverySts());
        holder.txAmnt.setText(model.getTxAmount());
        holder.txMob.setText(model.getTxMob());
        holder.txBookName.setText(model.getTxBook());
    }

    @Override
    public int getItemCount() {
        return lstUsrTx.size();
    }

    class TransactioViewHolder extends RecyclerView.ViewHolder{

        TextView txnId, txnDate, txnStatus, txnMode, txnDeliveryStatus, txAmnt, txMob, txReturndate, txBookName;

        public TransactioViewHolder(View itemView) {
            super(itemView);
            txnId  = (TextView)itemView.findViewById(R.id.textViewTitle);
            txnDate = (TextView)itemView.findViewById(R.id.textViewShortDesc);
            txReturndate = (TextView)itemView.findViewById(R.id.txnStatus);
            txnMode = (TextView)itemView.findViewById(R.id.textViewPrice);
            txnDeliveryStatus = (TextView)itemView.findViewById(R.id.textViewRating);
            txAmnt = (TextView)itemView.findViewById(R.id.txnAmount1);
            txMob = (TextView)itemView.findViewById(R.id.txnMobId1);
            txBookName = (TextView)itemView.findViewById(R.id.textBookName);
        }
    }
}
