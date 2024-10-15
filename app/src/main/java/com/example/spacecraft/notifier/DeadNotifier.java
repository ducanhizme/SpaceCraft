package com.example.spacecraft.notifier;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.example.spacecraft.activities.GameActivity;
import com.example.spacecraft.activities.MainActivity;
import com.example.spacecraft.base.GameObject;
import com.example.spacecraft.base.Observer;
import com.example.spacecraft.models.game.EnemyShip;
import com.example.spacecraft.models.game.PlayerShip;

public class DeadNotifier extends Observer {
    private final Context context;

    public DeadNotifier(GameObject gameObject, Context context) {
        this.context = context;
        this.gameObject = gameObject;
        this.gameObject.attachObserver(this);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void notify(GameObject gameObject, Object arg) {
            if (context instanceof GameActivity) {
            GameActivity gameActivity = (GameActivity) context;
            gameActivity.runOnUiThread(() -> {
                    if (gameObject instanceof PlayerShip) {
                    gameActivity.updateHealthBar(gameObject.getHealth());
                    if (gameObject.getHealth() <= 0) {
                        gameActivity.showGameOverDialog();
                    }
                } else if (gameObject instanceof EnemyShip && gameObject.getHealth() <= 0) {
                    int currentScore = Integer.parseInt(gameActivity.getBinding().score.getText().toString());
                    gameActivity.getBinding().score.setText(String.valueOf(currentScore + gameObject.getScore()));
            }
            });
        }
    }
}
