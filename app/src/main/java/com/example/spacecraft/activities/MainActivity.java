package com.example.spacecraft.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.spacecraft.adapters.ProfileAdapter;
import com.example.spacecraft.components.GameView;
import com.example.spacecraft.components.ProfileDialog;
import com.example.spacecraft.databinding.ActivityMainBinding;
import com.example.spacecraft.models.app.Profile;
import com.example.spacecraft.services.ProfileService;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private List<Profile> profiles;
    private ProfileService profileService;
    private ProfileDialog dialog;
    private GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setInputUser();
        setView();
    }

    private void setView() {
        gameView = new GameView(this);
        binding.main.addView(gameView, 0);
    }

    @SuppressLint("SetTextI18n")
    private void setInputUser() {
        binding.playBtn.setOnClickListener(v -> {
            startActivity();
            overridePendingTransition(0, 0);
        });

        binding.profileBtn.setOnClickListener(v -> {
            ProfileDialog dialog = ProfileDialog.newInstance(new ProfileDialog.DialogListener() {
                @Override
                public void onDialogPositiveClick(String inputText) {
                    createProfile(inputText);
                    profiles = profileService.getAllProfiles();
                    setAdapter();
                }

                @Override
                public void onDialogNegativeClick() {

                }
            }, "Profile", "Enter your new profile name to start new game");
            dialog.show(getSupportFragmentManager(), ProfileDialog.TAG);


        });
    }

    private void startActivity() {
        Intent i = new Intent(this, GameActivity.class);
        startActivity(i);
    }

    protected void onStart() {
        super.onStart();
        profileService = new ProfileService(this);
        profiles = profileService.getAllProfiles();
        setAdapter();
        int currentProfileId = binding.profileRv.getCurrentProfileIndex();
        profileService.saveProfileIdToPrefs((int)profiles.get(currentProfileId).getId());
        checkCurrentProfile();
    }

    private void checkCurrentProfile() {
        if (profileService.getProfileIdInPrefs() < 0) {
            dialog = new ProfileDialog(new ProfileDialog.DialogListener() {
                @Override
                public void onDialogPositiveClick(String inputText) {
                    createProfile(inputText);
                }

                @Override
                public void onDialogNegativeClick() {

                }
            });
            dialog.show(getSupportFragmentManager(), ProfileDialog.TAG);
        }
    }

    private void createProfile(String inputText){
        Profile profile = new Profile(inputText, 0);
        long profileId = profileService.addProfile(profile);
        profileService.saveProfileIdToPrefs(profileId);
    }

    private void setAdapter() {
        binding.profileRv.setAdapter(new ProfileAdapter(this, profiles));
        long currentProfileId = profileService.getProfileIdInPrefs();
        if (currentProfileId >= 0) {
            int currentIndex = -1;
            for (int i = 0; i < profiles.size(); i++) {
                if (profiles.get(i).getId() == currentProfileId) {
                    currentIndex = i;
                    break;
                }
            }
            if (currentIndex >= 0) {
                binding.profileRv.getRecyclerView().smoothScrollToPosition(currentIndex);
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        gameView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        gameView.pause();
    }

    public GameView getGameView() {
        return this.gameView;
    }

    public ActivityMainBinding getBinding(){
        return this.binding;
    }
}