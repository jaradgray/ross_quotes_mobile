package com.jgendeavors.rossquotes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ReceivedMessagesFragment extends Fragment {

    // Instance variables
    ReceivedMessagesFragmentViewModel mViewModel;


    // Overridden methods

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Indicate we'll have an options menu
        setHasOptionsMenu(true);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recent_messages, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // put any usages of findViewById() here

        // Get references to widgets
        RecyclerView recyclerView = view.findViewById(R.id.fragment_recent_messages_recycler_view);

        // Initialize RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        final ReceivedMessageAdapter adapter = new ReceivedMessageAdapter();
        recyclerView.setAdapter(adapter);

        // Request a ViewModel from the Android system
        mViewModel = ViewModelProviders.of(this).get(ReceivedMessagesFragmentViewModel.class);

        // Observe the ViewModel's LiveData
        mViewModel.getAllReceivedMessages().observe(getViewLifecycleOwner(), new Observer<List<ReceivedMessage>>() {
            @Override
            public void onChanged(List<ReceivedMessage> receivedMessages) {
                // update recyclerView's data
                adapter.setReceivedMessages(receivedMessages);
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        // Inflate the Fragment's options menu
        inflater.inflate(R.menu.fragment_received_messages_options_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // clear messages
        if (item.getItemId() == R.id.menu_item_clear_received_messages) {
            mViewModel.deleteAllReceivedMessages();
            Toast.makeText(getContext(), getString(R.string.toast_received_messages_cleared), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}
