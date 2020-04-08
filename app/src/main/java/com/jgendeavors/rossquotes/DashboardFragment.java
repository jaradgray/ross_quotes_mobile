package com.jgendeavors.rossquotes;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class DashboardFragment extends Fragment {

    /*
    public DashboardFragment() {

    }

    public static DashboardFragment newInstance() {
        DashboardFragment fragment = new DashboardFragment();
        return fragment;
    }
    */


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // put any usages of findViewById() here

        // Get references to cards
        final DashboardCardView cardMessages = view.findViewById(R.id.fragment_dashboard_card_messages);
        final DashboardCardView cardContacts = view.findViewById(R.id.fragment_dashboard_card_contacts);
        final DashboardCardView cardSettings = view.findViewById(R.id.fragment_dashboard_card_settings);

        // Get MainActivity's NavController
        final NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);

        // Handle clicks on cards
        cardMessages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navigate to RecentMessagesFragment
                navController.navigate(R.id.action_dashboardFragment_to_recentMessagesFragment);
            }
        });
        cardContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navigate to ContactsFragment
                navController.navigate(R.id.action_dashboardFragment_to_contactsFragment);
            }
        });
        cardSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navigate to SettingsFragment
                navController.navigate(R.id.action_dashboardFragment_to_settingsFragment);
            }
        });

        // Bind UI to the ViewModel

        // request a ViewModel from the Android system
        DashboardFragmentViewModel viewModel = ViewModelProviders.of(this).get(DashboardFragmentViewModel.class);
        // observe the ViewModel's LiveData
        // received messages
        viewModel.getAllReceivedMessages().observe(getViewLifecycleOwner(), new Observer<List<ReceivedMessage>>() {
            @Override
            public void onChanged(List<ReceivedMessage> receivedMessages) {
                // update messages card's details text
                String detailsText = getString(
                        R.string.dashboard_card_received_messages_details,
                        receivedMessages.size());
                cardMessages.setDetailText(detailsText);
            }
        });
        // contacts
        viewModel.getAllContacts().observe(getViewLifecycleOwner(), new Observer<List<Contact>>() {
            @Override
            public void onChanged(List<Contact> contacts) {
                // update contacts card's details text
                String detailsText = getString(
                        R.string.dashboard_card_contacts_details,
                        contacts.size());
                cardContacts.setDetailText(detailsText);
            }
        });
        // app enabled/disabled
        viewModel.getIsAppEnabled().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isEnabled) {
                // update settings card's alert
                DashboardCardView.AlertMode alertMode = isEnabled ? DashboardCardView.AlertMode.CONFIRM : DashboardCardView.AlertMode.WARN;
                String alertText = isEnabled ? getString(R.string.dashboard_card_settings_alert_text_confirm) : getString(R.string.dashboard_card_settings_alert_text_warn);
                cardSettings.setAlert(alertMode, alertText);
            }
        });
    }
}
