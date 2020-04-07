package com.jgendeavors.rossquotes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class RecentMessagesFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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
        RecentMessagesFragmentViewModel viewModel = ViewModelProviders.of(this).get(RecentMessagesFragmentViewModel.class);

        // Observe the ViewModel's LiveData
        viewModel.getAllReceivedMessages().observe(getViewLifecycleOwner(), new Observer<List<ReceivedMessage>>() {
            @Override
            public void onChanged(List<ReceivedMessage> receivedMessages) {
                // update recyclerView's data
                adapter.setReceivedMessages(receivedMessages);
            }
        });
    }
}
