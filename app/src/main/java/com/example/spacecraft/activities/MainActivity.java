package com.example.spacecraft.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.example.spacecraft.R;
import com.example.spacecraft.base.GameContext;
import com.example.spacecraft.adapters.ProfileAdapter;
import com.example.spacecraft.components.GameView;
import com.example.spacecraft.components.ProfileDialog;
import com.example.spacecraft.databinding.ActivityMainBinding;
import com.example.spacecraft.models.app.Profile;
import com.example.spacecraft.services.ProfileService;
import com.example.spacecraft.state.GamePlayingState;

import java.util.List;
import java.util.Objects;

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

    private void setInputUser() {
        binding.playBtn.setOnClickListener(v -> {
            startActivity();
            overridePendingTransition(0, 0);
        });
    }

    private void startActivity() {
        Intent i = new Intent(this, GameActivity.class);
        startActivity(i);
    }

    @Override
    protected void onStart() {
        super.onStart();
        profileService = new ProfileService(this);
        profiles = profileService.getAllProfiles();
        SetAdapter();
        int currentProfileId = binding.profileRv.getCurrentProfileIndex();
        profileService.saveProfileIdToPrefs((int)profiles.get(currentProfileId).getId());
        checkCurrentProfile();
    }

    private void checkCurrentProfile() {
        if (profileService.getProfileIdInPrefs() < 0) {
            dialog = new ProfileDialog(new ProfileDialog.DialogListener() {
                @Override
                public void onDialogPositiveClick(String inputText) {
                    Profile profile = new Profile(inputText, 0);
                    long profileId = profileService.addProfile(profile);
                    profileService.saveProfileIdToPrefs(profileId);
                }

                @Override
                public void onDialogNegativeClick() {

                }
            });
            dialog.show(getSupportFragmentManager(), ProfileDialog.TAG);
        }
    }

    private void SetAdapter() {
        binding.profileRv.setAdapter(new ProfileAdapter(this, profiles));
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