package com.example.spacecraft.notifier;

import android.content.Context;

import com.example.spacecraft.activities.MainActivity;
import com.example.spacecraft.base.GameObject;
import com.example.spacecraft.base.Observer;

public class DeadNotifier extends Observer {
    private final Context context;

    public DeadNotifier(GameObject gameObject, Context context) {
        this.context = context;
        this.gameObject = gameObject;
        this.gameObject.attachObserver(this);
    }

    @Override
    public void notify(GameObject gameObject, Object arg) {
        if (context instanceof MainActivity) {
            ((MainActivity) context).getGameView().pause();
            ((MainActivity) context).runOnUiThread(() -> ((MainActivity) context).showGameOverDialog());
        }
    }
}
