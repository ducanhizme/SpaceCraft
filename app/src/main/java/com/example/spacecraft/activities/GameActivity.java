package com.example.spacecraft.activities;

import android.os.Bundle;
import android.app.AlertDialog;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.spacecraft.R;
import com.example.spacecraft.components.GameView;
import com.example.spacecraft.databinding.ActivityGameBinding;
import com.example.spacecraft.state.GamePlayingState;

public class GameActivity extends AppCompatActivity {
    private GameView gameView;
    private ActivityGameBinding binding;
    private boolean isPaused = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGameBinding.inflate(getLayoutInflater());
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        setContentView(binding.getRoot());
        setView();
        setInputEvent();
    }

    private void setInputEvent() {
        binding.pauseBtn.setOnClickListener(v -> togglePause());

        binding.menuBtn.setOnClickListener(v -> {
            gameView.pause();
            isPaused = true;
            new AlertDialog.Builder(this)
                    .setTitle("Game Paused")
                    .setMessage("Do you want to resume the game?")
                    .setPositiveButton("Resume", (dialog, which) -> resumeGame())
                    .show();
        });

        binding.replayBtn.setOnClickListener(v -> replayGame());
    }

    private void togglePause() {
        if (isPaused) {
            gameView.resume();
        } else {
            gameView.pause();
        }
        isPaused = !isPaused;
        binding.pauseBtn.setImageResource(isPaused ? R.drawable.play : R.drawable.pause);
    }

    private void resumeGame() {
        gameView.resume();
        isPaused = false;
        binding.menuBtn.setImageResource(R.drawable.main_menu);
    }

    private void replayGame() {
        gameView.getGameContext().setState(new GamePlayingState(this));
        isPaused = false;
        binding.pauseBtn.setImageResource(R.drawable.pause);
        binding.menuBtn.setImageResource(R.drawable.main_menu);
    }

    private void setView() {
        gameView = new GameView(this);
        gameView.getGameContext().setState(new GamePlayingState(this));
        binding.game.addView(gameView, 0);
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


    public ActivityGameBinding getBinding() {
        return this.binding;
    }
}