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
import com.example.spacecraft.models.game.Explosion;
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
    private final List<Explosion> explosions;
    private int enemiesDestroyed;
    private int enemyCount;
    private final Random random;
    private final GameCharacterService gameCharacterService;
    private final Context context;

    public GamePlayingState(Context context) {
        this.gameCharacterService = new GameCharacterService(context);
        this.context = context;
        this.backgroundManager = gameCharacterService.defaultBackgroundManager();
        this.playerShip = gameCharacterService.defaultPlayerShip();
        this.enemies = new ArrayList<>();
        this.explosions = new ArrayList<>();
        this.enemiesDestroyed = 0;
        this.enemyCount = 3;
        this.random = new Random();
        DeadNotifier healthNotifier = new DeadNotifier(playerShip, context);
        initializeEnemies();
    }

    private void initializeEnemies() {

        final int TYPE_NORMAL =0;
        final int TYPE_FAST =1;
        final int TYPE_TANK =2;
        switch (random.nextInt(3)) {
            case 0:
                spawnEnemyCircularFormation();
                break;
            case 1:
                spawnEnemyGridFormation();
                break;
            case 2:
                spawnEnemyZigzagFormation();
                break;
        }
    }

    private void spawnEnemyGridFormation() {
        int spacingX = (int) (backgroundManager.getScreenWidth() / enemyCount);
        int spacingY = (int) (backgroundManager.getScreenHeight() / (enemyCount*2));

        int totalEnemies = 0;
        for (int row = 0; row < enemyCount && totalEnemies < enemyCount; row++) {
            for (int col = 0; col < enemyCount && totalEnemies < enemyCount; col++) {
                int x = col * spacingX + spacingX / 2;
                int y = row * spacingY + spacingY / 2;
                createEnemyShip(x, y);
                totalEnemies++;
            }
        }
    }

    private void spawnEnemyCircularFormation() {
        int centerX = (int) (backgroundManager.getScreenWidth() / 2);
        int centerY = (int) (backgroundManager.getScreenHeight() / 4);
        float radius = backgroundManager.getScreenWidth() / 6;

        for (int i = 0; i < enemyCount; i++) {
            double angle = 2 * Math.PI * i / enemyCount;
            int x = (int) (centerX + radius * Math.cos(angle));
            int y = (int) (centerY + radius * Math.sin(angle));
            createEnemyShip(x, y);
        }
    }

    private void spawnEnemyZigzagFormation() {
        int spacingX = (int) (backgroundManager.getScreenWidth() / enemyCount);
        int spacingY = (int) (backgroundManager.getScreenHeight() / 4);

        for (int i = 0; i < enemyCount; i++) {
            int x = i * spacingX + spacingX / 2;
            int y = (i % 2 == 0) ? spacingY : spacingY * 2;
            createEnemyShip(x, y);
        }
    }


    private void createEnemyShip(int x, int y) {
        EnemyShip enemyShip = null;
        int enemyType = random.nextInt(3);
        switch (enemyType) {
            case 0:
                enemyShip = gameCharacterService.createEnemyShip(new Point(x,y),R.drawable.enemy_normal);
                enemyShip.setHealth(3);
                break;
            case 1:
                enemyShip = gameCharacterService.createEnemyShip(new Point(x,y),R.drawable.enemy_fast);
                enemyShip.setHealth(4);
                break;
            case 2:
                enemyShip = gameCharacterService.createEnemyShip(new Point(x,y),R.drawable.enemytank);
                enemyShip.setHealth(5);
                break;
        }
        enemyShip.setPoint(new Point(x, y));
        enemies.add(enemyShip);
        DeadNotifier healthNotifier = new DeadNotifier(enemyShip, context);
    }


    @Override
    public void update() {
        backgroundManager.update();
        playerShip.update();
        for (EnemyShip enemy : enemies) {
            enemy.update();
        }
        List<Explosion> finishedExplosions = new ArrayList<>();
        for (Explosion explosion : explosions) {
            explosion.update();
            if (explosion.isFinished()) {
                finishedExplosions.add(explosion);
            }
        }
        explosions.removeAll(finishedExplosions);
        checkCollisions();
    }

    @Override
    public void draw(Canvas canvas) {
        backgroundManager.draw(canvas, new Paint());
        playerShip.draw(canvas, new Paint());
        for (EnemyShip enemy : enemies) {
            enemy.draw(canvas, new Paint());
        }
        for (Explosion explosion : explosions) {
            explosion.draw(canvas, new Paint());
        }
    }

    private void checkCollisions() {
        List<EnemyShip> destroyedEnemies = new ArrayList<>();
        List<Bullet> destroyedBullets = new ArrayList<>();
        for (EnemyShip enemy : enemies) {
            if (Rect.intersects(enemy.getBounds(), playerShip.getBounds())) {
                playerShip.setHealth(playerShip.getHealth() - 1);
                if (playerShip.getHealth() <= 0) {
                    playerShip.setExplosion(new Explosion(backgroundManager.getResources(), R.drawable.explosion, playerShip.getPoint(), 128, 4));
                    explosions.add(playerShip.getExplosion());
                }
                destroyedEnemies.add(enemy);
            }
            for (Bullet bullet : playerShip.getBullets()) {
                if (Rect.intersects(bullet.getBounds(), enemy.getBounds())) {
                    enemy.setHealth(enemy.getHealth() - 1);
                    destroyedBullets.add(bullet);
                    if(enemy.getHealth() <= 0) {
                        enemy.setExplosion(new Explosion(backgroundManager.getResources(), R.drawable.explosion, enemy.getPoint(), 128, 4));
                        explosions.add(enemy.getExplosion());
                        destroyedEnemies.add(enemy);
                    }
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
        }
    }

}