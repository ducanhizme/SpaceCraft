package com.example.spacecraft.component;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.content.res.AppCompatResources;

import com.example.spacecraft.R;

import java.util.Objects;

public class AppToast {
    public static void makeText(Context context, String message, int duration) {
        Toast toast = Toast.makeText(context, message, duration);
        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
        View view = toast.getView();
        assert view != null;
//        view.setBackground(AppCompatResources.getDrawable(context, R.drawable.toast));
        view.setBackgroundColor(context.getColor(R.color.toastBackground));
        TextView text = view.findViewById(android.R.id.message);
        text.setTextColor(context.getColor(android.R.color.white));
        text.setTypeface(Typeface.createFromAsset(context.getAssets(), "res/font/barcadenobar.ttf"));
        toast.show();


    }
}
