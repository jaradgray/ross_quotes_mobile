package com.jgendeavors.rossquotes;

import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

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

public class ContactsFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contacts, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // put any usages of findViewById() here

        // Get references to widgets
        final RecyclerView recyclerView = view.findViewById(R.id.fragment_contacts_recycler_view);
        FloatingActionButton fab = view.findViewById(R.id.fragment_contacts_fab);

        // Initialize RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        final ContactAdapter adapter = new ContactAdapter();
        recyclerView.setAdapter(adapter);

        // Request a ViewModel from the Android system
        final ContactsFragmentViewModel viewModel = ViewModelProviders.of(this).get(ContactsFragmentViewModel.class);

        // Handle interactions on RecyclerView items by implementing ContactAdapter.OnItemInteractionListener interface
        adapter.setOnItemInteractionListener(new ContactAdapter.OnItemInteractionListener() {
            @Override
            public void onItemClick(Contact contact) {
                // Navigate to ContactDetailsFragment
                // Note: we pass data to the destination with a simple Bundle instead of with SafeArgs because it's simpler.
                // See documentation here: https://developer.android.com/guide/navigation/navigation-pass-data#bundle

                // build the Bundle that will hold the clicked Contact's id
                Bundle bundle = new Bundle();
                bundle.putInt(ContactDetailsFragment.ARG_KEY_CONTACT_ID, contact.getId());
                // navigate
                final NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                navController.navigate(R.id.action_contactsFragment_to_contactDetailsFragment, bundle);
            }

            @Override
            public void onSwitchClick(Contact contact, boolean isChecked) {
                // Update contact's isEnabled value in db via ViewModel
                viewModel.updateContactIsEnabled(contact, isChecked);
                // Show a Snackbar
                int snackbarStringResId = isChecked ? R.string.snackbar_contact_enabled : R.string.snackbar_contact_disabled;
                // the string we'll show in the Snackbar contains HTML formatting, so:
                //  encode Contact's name
                //  get the styled string with fromHtml()
                String encodedName = TextUtils.htmlEncode(contact.getName());
                String text = getString(snackbarStringResId, encodedName); // contains HTML markup
                Spanned styledText = Html.fromHtml(text); // the string we present to the UI
                Snackbar.make(recyclerView /* CoordinatorLayout or a child of one */, styledText, Snackbar.LENGTH_LONG).show();
            }
        });

        // Handle clicks on FAB
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navigate to EditContactFragment
                // build the Bundle to pass data
                Bundle bundle = new Bundle();
                bundle.putInt(EditContactFragment.ARG_KEY_CONTACT_ID, EditContactFragment.ARG_VALUE_NO_CONTACT_ID);
                // navigate
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment)
                        .navigate(R.id.action_contactsFragment_to_editContactFragment, bundle);
            }
        });

        // Observe the ViewModel's LiveData
        viewModel.getContacts().observe(getViewLifecycleOwner(), new Observer<List<Contact>>() {
            /**
             * Called each time the data in the LiveData object we're observing changes.
             * @param contacts
             */
            @Override
            public void onChanged(List<Contact> contacts) {
                // update RecyclerView UI
                adapter.setContacts(contacts);
            }
        });
    }


}
