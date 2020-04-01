package com.jgendeavors.rossquotes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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

        // Initialize RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.fragment_contacts_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        final ContactAdapter adapter = new ContactAdapter();
        recyclerView.setAdapter(adapter);

        // Handle clicks on RecyclerView items by implementing ContactAdapter.OnItemClickListener interface
        adapter.setOnItemClickListener(new ContactAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Contact contact) {
                // TODO handle click on Contact item correctly
                //  for now, just navigate to ContactDetailsFragment
                final NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                navController.navigate(R.id.action_contactsFragment_to_contactDetailsFragment);
            }
        });

        // Request a ViewModel from the Android system
        ContactsFragmentViewModel viewModel = ViewModelProviders.of(this).get(ContactsFragmentViewModel.class);

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
