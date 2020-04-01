package com.jgendeavors.rossquotes;

import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * The ContactDetailsFragment displays an existing Contact's details.
 */
public class ContactDetailsFragment extends Fragment {
    // Constants
    public static final String ARG_KEY_CONTACT_ID = "ARG_KEY_CONTACT_ID";


    // Lifecycle overrides

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contact_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // put any usages of findViewById() here

        // Get references to widgets
        ImageView ivProfile = view.findViewById(R.id.fragment_contact_details_iv_profile);
        final TextView tvName = view.findViewById(R.id.fragment_contact_details_tv_name);
        RecyclerView recyclerView = view.findViewById(R.id.fragment_contact_details_rv_quotes);

        // Initialize RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        final MessageAdapter adapter = new MessageAdapter();
        recyclerView.setAdapter(adapter);

        // Handle clicks on RecyclerView items by implementing MessageAdapter.OnItemClickListener interface
        adapter.setOnItemClickListener(new MessageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // TODO handle clicks on Message items
            }
        });

        // Get the id of the Contact from args
        int contactId = getArguments().getInt(ARG_KEY_CONTACT_ID);

        // Request a ViewModel from the Android system
        ContactDetailsFragmentViewModel viewModel = ViewModelProviders.of(this).get(ContactDetailsFragmentViewModel.class);

        // Observe the ViewModel's LiveData

        // contact
        viewModel.getContact(contactId).observe(getViewLifecycleOwner(), new Observer<Contact>() {
            @Override
            public void onChanged(Contact contact) {
                // TODO update profile pic

                // update name
                tvName.setText(contact.getName());
            }
        });

        // contact's messages
        viewModel.getMessagesForContact(contactId).observe(getViewLifecycleOwner(), new Observer<List<Message>>() {
            @Override
            public void onChanged(List<Message> messages) {
                // update adapter's data
                adapter.setMessages(messages);
            }
        });
    }
}
