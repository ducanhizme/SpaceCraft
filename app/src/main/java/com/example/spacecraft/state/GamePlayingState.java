// app/src/main/java/com/example/spacecraft/state/GamePlayingState.java
package com.example.spacecraft.state;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;

import com.example.spacecraft.R;
import com.example.spacecraft.base.GameState;
import com.example.spacecraft.models.game.Bullet;
import com.example.spacecraft.models.game.EnemyShip;
import com.example.spacecraft.notifier.DeadNotifier;
import com.example.spacecraft.utils.BackgroundManager;
import com.example.spacecraft.models.game.PlayerShip;
import com.example.spacecraft.services.GameCharacterService;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GamePlayingState implements GameState {
    public static final String TAG = "GamePlayingState";
    private final BackgroundManager backgroundManager;
    private final PlayerShip playerShip;
    private final List<EnemyShip> enemies;
    private int enemiesDestroyed;
    private int enemyCount;
    private final Random random;
    private final GameCharacterService gameCharacterService;

    public GamePlayingState(Context context) {
        this.gameCharacterService = new GameCharacterService(context);
        this.backgroundManager = gameCharacterService.defaultBackgroundManager();
        this.playerShip = gameCharacterService.defaultPlayerShip();
        this.enemies = new ArrayList<>();
        this.enemiesDestroyed = 0;
        this.enemyCount = 3;
        this.random = new Random();
        DeadNotifier healthNotifier = new DeadNotifier(playerShip, context);
        initializeEnemies();
    }

    private void initializeEnemies() {
        for (int i = 0; i < enemyCount; i++) {
            float x = random.nextFloat() * backgroundManager.getScreenWidth();
            float y = random.nextFloat() * backgroundManager.getScreenHeight() / 2;
            EnemyShip enemyShip = new EnemyShip(backgroundManager.getScreenWidth(), backgroundManager.getScreenHeight(), backgroundManager.getResources(), R.drawable.enemy, 1920f / backgroundManager.getScreenWidth(), 1080f / backgroundManager.getScreenHeight());
            enemyShip.setPoint(new Point((int) x, (int) y));
            enemies.add(enemyShip);
        }
    }

    @Override
    public void update() {
        backgroundManager.update();
        playerShip.update();
        for (EnemyShip enemy : enemies) {
            enemy.update();
        }
        checkCollisions();
    }

    @Override
    public void draw(Canvas canvas) {
        backgroundManager.draw(canvas, new Paint());
        playerShip.draw(canvas, new Paint());
        for (EnemyShip enemy : enemies) {
            enemy.draw(canvas, new Paint());
        }
    }

    private void checkCollisions() {
        List<EnemyShip> destroyedEnemies = new ArrayList<>();
        List<Bullet> destroyedBullets = new ArrayList<>();
        for (EnemyShip enemy : enemies) {
            if (Rect.intersects(enemy.getBounds(), playerShip.getBounds())) {
                if(playerShip.getHealth()<0){
                    playerShip.triggerExplosion(backgroundManager.getResources());
                }
                playerShip.setHealth(playerShip.getHealth() - 1);
                destroyedEnemies.add(enemy);
            }
            for (Bullet bullet : playerShip.getBullets()) {
                if (Rect.intersects(bullet.getBounds(), enemy.getBounds())) {
                    destroyedEnemies.add(enemy);
                    destroyedBullets.add(bullet);
                }
            }
        }
        enemies.removeAll(destroyedEnemies);
        playerShip.getBullets().removeAll(destroyedBullets);
        enemiesDestroyed += destroyedEnemies.size();
        if (enemiesDestroyed >= enemyCount) {
            enemiesDestroyed = 0;
            enemyCount++;
            initializeEnemies();
            Log.d(TAG, "checkCollisions: " + enemyCount);
        }
    }

}