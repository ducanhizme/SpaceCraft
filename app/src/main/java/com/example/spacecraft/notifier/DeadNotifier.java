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
        if (gameObject instanceof PlayerShip) {
            if (context instanceof MainActivity) {
//                ((MainActivity) context).runOnUiThread(() -> ((MainActivity) context).showGameOverDialog());
            }
        }else if(gameObject instanceof EnemyShip){
            if (context instanceof GameActivity) {
                ((GameActivity) context).runOnUiThread(() -> {
                    int currentScore = Integer.parseInt(((GameActivity) context).getBinding().score.getText().toString());
                    ((GameActivity) context).getBinding().score.setText(String.valueOf(currentScore + gameObject.getScore()));
                });
            }
        }
        Log.d("DeadNotifier", "notify: " + gameObject.getHealth());
    }
}
