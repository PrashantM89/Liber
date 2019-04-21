package org.app.liber.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.app.liber.R;
import org.app.liber.model.NotificationModel;


import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>{
    private Context context;
    private List<NotificationModel> lstNotify;

    public NotificationAdapter(Context context, List<NotificationModel> lstNotify) {
        this.context = context;
        this.lstNotify = lstNotify;
    }

    @Override
    public NotificationAdapter.NotificationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_notification, null);
        return new NotificationAdapter.NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NotificationAdapter.NotificationViewHolder holder, int position) {

        NotificationModel model = lstNotify.get(position);

        holder.notifyTxt.setText(model.getNotifyText());
        holder.notifyDate.setText(model.getNotifyTime());

    }

    @Override
    public int getItemCount() {
        return lstNotify.size();
    }

    class NotificationViewHolder extends RecyclerView.ViewHolder{

        TextView notifyTxt, notifyDate;


        public NotificationViewHolder(View itemView) {
            super(itemView);
            notifyTxt  = (TextView)itemView.findViewById(R.id.notification_text_id);
            notifyDate = (TextView)itemView.findViewById(R.id.notification_time_id);
        }
    }
}