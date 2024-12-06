package com.example.spacecraft.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.spacecraft.adapters.HighestScoreAdapter;
import com.example.spacecraft.databinding.LocalHighestScoreFragmentBinding;
import com.example.spacecraft.models.app.Profile;
import com.example.spacecraft.services.FirebaseService;
import com.example.spacecraft.services.ProfileService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LocalHighestScoreFragment extends Fragment {
    public static final String TAG = "Local";
    private LocalHighestScoreFragmentBinding binding;
    private ProfileService profileService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = LocalHighestScoreFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        profileService = new ProfileService(this.getContext());
        initializeUI();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void initializeUI() {
        new FirebaseService().getProfileOnFirebase().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Profile> profiles = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Profile profile = dataSnapshot.getValue(Profile.class);
                    profiles.add(profile);
                }
                binding.localHighestScoreRv.setLayoutManager(new LinearLayoutManager(getContext()));
                binding.localHighestScoreRv.setAdapter(new HighestScoreAdapter(profiles));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "onCancelled: ", error.toException());
            }
        });
    }
}
