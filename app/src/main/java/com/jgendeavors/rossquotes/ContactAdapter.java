package com.jgendeavors.rossquotes;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {
    // Instance variables
    private List<Contact> mContacts;

    /**
     * Interface for item clicks
     */
    public interface OnItemClickListener {
        void onItemClick(Contact contact);
    }
    private OnItemClickListener mOnItemClickListener;
    public void setOnItemClickListener(OnItemClickListener listener) { mOnItemClickListener = listener; }


    /**
     * The ViewHolder that will hold the view for a Contact item
     */
    public class ContactViewHolder extends RecyclerView.ViewHolder {
        // Widgets
        public ImageView ivPicture;
        public TextView tvName;

        // Constructor
        public ContactViewHolder(View itemView) {
            super(itemView);

            // Get references to widgets
            ivPicture = itemView.findViewById(R.id.item_contact_iv_picture);
            tvName = itemView.findViewById(R.id.item_contact_tv_name);

            // Handle item clicks
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // notify listener
                    if (mOnItemClickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mOnItemClickListener.onItemClick(mContacts.get(position));
                        }
                    }
                }
            });
        }
    }


    // Setter

    /**
     * This is how we update our data.
     * @param contacts
     */
    public void setContacts(List<Contact> contacts) {
        mContacts = contacts;
        notifyDataSetChanged();
    }


    // Overrides

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact, parent, false);
        return new ContactViewHolder(itemView);
    }

    /**
     * Populate item view (ContactViewHolder) based on corresponding Contact object.
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        Contact contact = mContacts.get(position);

        // Set holder's data based on contact
        holder.tvName.setText(contact.getName());
        // picture
        Uri imgUri = Uri.parse(contact.getImageAbsolutePath());
        holder.ivPicture.setImageURI(imgUri);
    }

    @Override
    public int getItemCount() {
        return (mContacts == null) ? 0 : mContacts.size();
    }
}
