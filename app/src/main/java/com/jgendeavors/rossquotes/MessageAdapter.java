package com.jgendeavors.rossquotes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    // Instance variables
    private List<Message> mMessages;

    /**
     * Interface for item clicks
     */
    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    private OnItemClickListener mOnItemClickListener;
    public void setOnItemClickListener(OnItemClickListener listener) { mOnItemClickListener = listener; }


    /**
     * The ViewHolder that will hold the view for a Message item
     */
    public class MessageViewHolder extends RecyclerView.ViewHolder {
        // Widgets
        public TextView tvMessage;

        // Constructor
        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);

            // TODO get references to widgets
            tvMessage = itemView.findViewById(R.id.item_message_tv_message);

            // Handle item clicks
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // notify listener
                    if (mOnItemClickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mOnItemClickListener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }


    // Setter

    /**
     * This is how we update our data.
     * @param messages
     */
    public void setMessages(List<Message> messages) {
        mMessages = messages;
        notifyDataSetChanged();
    }


    // Overrides

    /**
     * Inflates the ViewHolder's layout, and creates and returns a new ViewHolder.
     *
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
        return new MessageViewHolder(itemView);
    }

    /**
     * Populate item view (MessageViewHolder) based on corresponding object.
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = mMessages.get(position);

        // Set holder's widgets based on message
        holder.tvMessage.setText(message.getText());
    }

    @Override
    public int getItemCount() {
        return mMessages == null ? 0 : mMessages.size();
    }
}
