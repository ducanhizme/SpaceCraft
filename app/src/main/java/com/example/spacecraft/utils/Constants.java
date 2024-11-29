package com.example.spacecraft.utils;

import android.content.Context;

import com.example.spacecraft.R;
import com.example.spacecraft.components.GameView;

public class Constants {
    public static String CURRENT_STATE;
    public static int[] BACKGROUND_GAME = {R.drawable.starscape00, R.drawable.starscape01, R.drawable.starscape02, R.drawable.starscape03};
    public static int PLAYER_SHIP = R.drawable.playership;
    public static int BULLET = R.drawable.laserblue;
    public static int ENEMY_SHIP_NORMAL = R.drawable.enemy_normal;
    public static int ENEMY_SHIP_FAST = R.drawable.enemy_fast;
    public static int ENEMY_SHIP_TANK = R.drawable.enemytank;
    public static int EXPLOSION = R.drawable.explosion;
    public static int HEALTH_ICON = R.drawable.player_life;
    public static int PLAYER_SHIP_HEALTH = 3;
    public static int ENEMY_SHIP_NORMAL_HEALTH = 3;
    public static int ENEMY_SHIP_FAST_HEALTH = 2;
    public static int ENEMY_SHIP_TANK_HEALTH = 5;
    public static int COL_EXPLOSION = 9;
    public static int EXPLOSION_FRAME_TIME = 40000;
    public static int DEFAULT_GENERATE_ENEMY = 3;

}
