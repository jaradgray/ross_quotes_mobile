package com.jgendeavors.rossquotes;

import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * The ContactDetailsFragment displays an existing Contact's details.
 */
public class ContactDetailsFragment extends Fragment {
    // Constants
    public static final String ARG_KEY_CONTACT_ID = "ARG_KEY_CONTACT_ID";


    // Instance variables
    private ContactDetailsFragmentViewModel mViewModel;


    // Lifecycle overrides

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Indicate we'll have an options menu
        setHasOptionsMenu(true);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contact_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // put any usages of findViewById() here

        // Get references to widgets
        final ImageView ivProfile = view.findViewById(R.id.fragment_contact_details_iv_profile);
        final TextView tvName = view.findViewById(R.id.fragment_contact_details_tv_name);
        View contactContainer = view.findViewById(R.id.fragment_contact_details_contact_container);
        final View alertContainer = view.findViewById(R.id.fragment_contact_details_alert_container);
        RecyclerView recyclerView = view.findViewById(R.id.fragment_contact_details_rv_quotes);
        FloatingActionButton fab = view.findViewById(R.id.fragment_contact_details_fab);

        // Get the id of the Contact from args
        final int contactId = getArguments().getInt(ARG_KEY_CONTACT_ID);

        // Handle clicks on FAB
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navigate to EditMessageFragment
                // build the Bundle to pass data
                Bundle bundle = new Bundle();
                bundle.putInt(EditMessageFragment.ARG_KEY_CONTACT_ID, contactId);
                bundle.putInt(EditMessageFragment.ARG_KEY_MESSAGE_ID, EditMessageFragment.ARG_VALUE_NO_MESSAGE_ID);
                // navigate
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment)
                        .navigate(R.id.action_contactDetailsFragment_to_editMessageFragment, bundle);
            }
        });

        // Handle clicks on contact section
        contactContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navigate to EditContactFragment
                // build the Bundle to pass data
                Bundle bundle = new Bundle();
                bundle.putInt(EditContactFragment.ARG_KEY_CONTACT_ID, contactId);
                // navigate
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment)
                        .navigate(R.id.action_contactDetailsFragment_to_editContactFragment, bundle);
            }
        });

        // Initialize RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        final MessageAdapter adapter = new MessageAdapter();
        recyclerView.setAdapter(adapter);

        // Handle clicks on RecyclerView items by implementing MessageAdapter.OnItemClickListener interface
        adapter.setOnItemClickListener(new MessageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Message message) {
                // Navigate to EditMessageFragment
                // build the Bundle to pass data
                Bundle bundle = new Bundle();
                bundle.putInt(EditMessageFragment.ARG_KEY_CONTACT_ID, contactId);
                bundle.putInt(EditMessageFragment.ARG_KEY_MESSAGE_ID, message.getId());
                // navigate
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment)
                        .navigate(R.id.action_contactDetailsFragment_to_editMessageFragment, bundle);
            }
        });

        // Request a ViewModel from the Android system
        mViewModel = ViewModelProviders.of(this).get(ContactDetailsFragmentViewModel.class);

        // Observe the ViewModel's LiveData

        // contact
        mViewModel.getContact(contactId).observe(getViewLifecycleOwner(), new Observer<Contact>() {
            @Override
            public void onChanged(Contact contact) {
                // update profile pic
                Uri imgUri = Uri.parse(contact.getImageAbsolutePath());
                ivProfile.setImageURI(imgUri);

                // update name
                tvName.setText(contact.getName());
            }
        });

        // contact's messages
        mViewModel.getMessagesForContact(contactId).observe(getViewLifecycleOwner(), new Observer<List<Message>>() {
            @Override
            public void onChanged(List<Message> messages) {
                // update adapter's data
                adapter.setMessages(messages);
                // show/hide "no quotes" alert based on if Contact has messages in the db
                int visibility = messages.isEmpty() ? View.VISIBLE : View.GONE;
                alertContainer.setVisibility(visibility);
            }
        });
    }



    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        // Inflate the Fragment's options menu
        inflater.inflate(R.menu.fragment_contact_details_options_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_delete_contact:
                // Delete Contact
                int contactId = getArguments().getInt(ARG_KEY_CONTACT_ID);
                mViewModel.deleteContact(contactId);
                // Navigate back/up
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).popBackStack();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
