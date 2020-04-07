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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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
        DashboardCardView cardMessages = view.findViewById(R.id.fragment_dashboard_card_messages);
        DashboardCardView cardContacts = view.findViewById(R.id.fragment_dashboard_card_contacts);
        DashboardCardView cardSettings = view.findViewById(R.id.fragment_dashboard_card_settings);

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
    }
}
