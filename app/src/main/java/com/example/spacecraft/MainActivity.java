package com.example.spacecraft;

import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.spacecraft.adapter.ProfileAdapter;
import com.example.spacecraft.component.ProfileDialog;
import com.example.spacecraft.databinding.ActivityMainBinding;
import com.example.spacecraft.model.Profile;
import com.example.spacecraft.service.ProfileService;

import java.util.List;

public class MainActivity extends AppCompatActivity implements ProfileDialog.DialogListener {
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
            Log.d("GameActivity a", "Width: " + point.x + " Height: " + point.y);
        }else{
            getWindowManager().getDefaultDisplay().getSize(point);
            Log.d("GameActivity b", "Width: " + point.x + " Height: " + point.y);
        }
        gameView = new GameView(this, point.x, point.y);
        binding.main.addView(gameView,0);
        profileService = new ProfileService(this);
        profileService.clearProfileIdInPrefs();
        showCustomDialog();
        binding.playBtn.setOnClickListener(v -> {
            startAnimationAndTransition();
            startActivity(new Intent(this, GameActivity.class));
        });
    }

    private void startAnimationAndTransition() {
        Animation moveUp = AnimationUtils.loadAnimation(this, R.anim.move_up);
        Animation moveDown = AnimationUtils.loadAnimation(this, R.anim.move_down);
        moveUp.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                // Optional: actions when animation starts
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                binding.logoGame.setVisibility(View.GONE);
                binding.layoutButton.startAnimation(moveDown);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
        moveDown.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                binding.layoutButton.setVisibility(View.GONE);

                Intent intent = new Intent(MainActivity.this, GameActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
        binding.logoGame.startAnimation(moveUp);
        binding.layoutButton.startAnimation(moveDown);
    }

    private void showCustomDialog() {
        if (profileService.getProfileIdInPrefs() < 0){
            dialog = new ProfileDialog(this);
            dialog.show(getSupportFragmentManager(), ProfileDialog.TAG);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        profiles = profileService.getAllProfiles();
        binding.profileRv.setAdapter(new ProfileAdapter(this, profiles));
    }

    @Override
    public void onDialogPositiveClick(String inputText) {
        Profile profile = new Profile(inputText, 0);
        long profileId = profileService.addProfile(profile);
        profileService.saveProfileIdToPrefs(profileId);

    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.logoGame.setVisibility(View.VISIBLE);
        binding.layoutButton.setVisibility(View.VISIBLE);
        gameView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        gameView.pause();
    }
}