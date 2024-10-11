package com.example.spacecraft.activities;

import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.example.spacecraft.state.GameContext;
import com.example.spacecraft.adapters.ProfileAdapter;
import com.example.spacecraft.components.GameView;
import com.example.spacecraft.components.ProfileDialog;
import com.example.spacecraft.databinding.ActivityMainBinding;
import com.example.spacecraft.models.Profile;
import com.example.spacecraft.services.ProfileService;
import com.example.spacecraft.state.GamePauseState;
import com.example.spacecraft.state.GamePlayingState;
import com.example.spacecraft.utils.Constants;

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
        Point point = new Point();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            point.x = getWindowManager().getCurrentWindowMetrics().getBounds().width();
            point.y = getWindowManager().getCurrentWindowMetrics().getBounds().height();
        } else {
            getWindowManager().getDefaultDisplay().getSize(point);
        }
        gameView = new GameView(this, point.x, point.y);
        setInputUser();
        setView();
        handleOnBackPressed();
//        handleGameState();
    }

//    private void handleGameState() {
//        switch (Constants.CURRENT_STATE) {
//            case GamePlayingState.TAG:
//                gameView.getGameContext().setState(new GamePlayingState(gameView.getBackgroundManager()));
//                break;
//            case GamePauseState.TAG:
//                gameView.getGameContext().setState(new GamePauseState(gameView.getBackgroundManager()));
//                break;
//        }
//    }

    private void setView() {
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
//                gameContext.setState(new GamePauseState(gameView.getBackgroundManager()));
//                Constants.CURRENT_STATE = GamePauseState.TAG;
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
            Constants.CURRENT_STATE = GamePlayingState.TAG;
        });
        binding.logoGame.startAnimation(moveUp);
        binding.layoutButton.startAnimation(moveDown);
    }
}