package com.example.spacecraft.activities;

import android.app.Activity;
import android.app.AlertDialog;
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
        handleOnBackPressed();
    }

    private void setView() {
        gameView = new GameView(this);
        binding.main.addView(gameView, 0);
    }

    private void setInputUser() {
        binding.playBtn.setOnClickListener(v -> {
            startAnimationAndTransition();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        profileService = new ProfileService(this);
        checkCurrentProfile();
        profiles = profileService.getAllProfiles();
        SetAdapter();
    }

    private void checkCurrentProfile() {
        if (profileService.getProfileIdInPrefs() < 0) {
            dialog = new ProfileDialog(inputText -> {
                Profile profile = new Profile(inputText, 0);
                long profileId = profileService.addProfile(profile);
                profileService.saveProfileIdToPrefs(profileId);
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

    private void handleOnBackPressed() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            final GameContext gameContext = gameView.getGameContext();
            @Override
            public void handleOnBackPressed() {
                gameView.pause();
            }
        });
    }

    private void startAnimationAndTransition() {
        Animation moveUp = com.example.spacecraft.utils.Animation.moveUp(this, () -> {
            binding.logoGame.setVisibility(View.GONE);
        });
        Animation moveDown = com.example.spacecraft.utils.Animation.moveDown(this, () -> {
            binding.layoutButton.setVisibility(View.GONE);
            binding.score.setVisibility(View.VISIBLE);
        });
        binding.logoGame.startAnimation(moveUp);
        binding.layoutButton.startAnimation(moveDown);
        gameView.getGameContext().setState(new GamePlayingState(this));
    }

    public void showGameOverDialog() {
        new android.os.Handler().postDelayed(() -> {
            gameView.pause();
        new AlertDialog.Builder(this)
                .setTitle("Game Over")
                .setMessage("You have lost the game. Would you like to try again?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    gameView.getGameContext().setState(new GamePlayingState(this));
                })
                .setNegativeButton("No", (dialog, which) -> {
                    finish();
                })
                .setCancelable(false)
                .show();
        }, 5000);

    }

    public GameView getGameView() {
        return this.gameView;
    }

    public ActivityMainBinding getBinding(){
        return this.binding;
    }
}