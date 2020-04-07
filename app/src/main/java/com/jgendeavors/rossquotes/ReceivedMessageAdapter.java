package com.jgendeavors.rossquotes;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ReceivedMessageAdapter extends RecyclerView.Adapter<ReceivedMessageAdapter.ReceivedMessageViewHolder> {
    // Instance variables
    List<ReceivedMessage> mReceivedMessages;

    /**
     * The ViewHolder that will hold the view for a ReceivedMessage item
     */
    public class ReceivedMessageViewHolder extends RecyclerView.ViewHolder {
        // Widgets
        public ImageView ivAvatar;
        public TextView tvContactName;
        public TextView tvMessageText;
        public TextView tvTimestamp;

        // Constructor
        public ReceivedMessageViewHolder(@NonNull View itemView) {
            super(itemView);

            // Get references to widgets
            ivAvatar = itemView.findViewById(R.id.item_received_message_iv_avatar);
            tvContactName = itemView.findViewById(R.id.item_received_message_tv_contact_name);
            tvMessageText = itemView.findViewById(R.id.item_received_message_tv_message_text);
            tvTimestamp = itemView.findViewById(R.id.item_received_message_tv_timestamp);
        }
    }


    // Setter

    public void setReceivedMessages(List<ReceivedMessage> receivedMessages) {
        mReceivedMessages = receivedMessages;
        notifyDataSetChanged();
    }


    // Overrides

    @NonNull
    @Override
    public ReceivedMessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_received_message, parent, false);
        return new ReceivedMessageViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReceivedMessageViewHolder holder, int position) {
        ReceivedMessage receivedMessage = mReceivedMessages.get(position);

        // Set holder's data based on receivedMessage
        // picture
        Uri imgUri = Uri.parse(receivedMessage.getContactImageAbsolutePath());
        holder.ivAvatar.setImageURI(imgUri);
        // contact name
        holder.tvContactName.setText(receivedMessage.getContactName());
        // message text
        holder.tvMessageText.setText(receivedMessage.getMessageText());
        // timestamp
        // TODO format timestamp String better
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(receivedMessage.getTimestamp());
        holder.tvTimestamp.setText(c.getTime().toString());
    }

    @Override
    public int getItemCount() {
        return (mReceivedMessages == null) ? 0 : mReceivedMessages.size();
    }
}
