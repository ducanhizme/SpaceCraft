package com.example.spacecraft;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity {
    private GameView gameView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(gameView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        gameView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameView.resume();
    }
}