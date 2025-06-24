package com.example.spacecraft.state;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;

import com.example.spacecraft.base.GameState;
import com.example.spacecraft.models.game.Bullet;
import com.example.spacecraft.models.game.EnemyShip;
import com.example.spacecraft.models.game.Explosion;
import com.example.spacecraft.notifier.DeadNotifier;
import com.example.spacecraft.services.DifficultyManager;
import com.example.spacecraft.utils.BackgroundManager;
import com.example.spacecraft.models.game.PlayerShip;
import com.example.spacecraft.services.GameCharacterService;
import com.example.spacecraft.utils.Constants;
import com.example.spacecraft.utils.Difficulty;

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
    private final DifficultyManager difficultyManager;
    private final Difficulty currentDifficulty;

    public GamePlayingState(Context context) {
        this.gameCharacterService = new GameCharacterService(context);
        this.context = context;
        this.difficultyManager = new DifficultyManager(context);
        this.currentDifficulty = difficultyManager.loadDifficulty();
        this.backgroundManager = gameCharacterService.defaultBackgroundManager();
        this.playerShip = gameCharacterService.defaultPlayerShip();
        this.enemies = new ArrayList<>();
        this.explosions = new ArrayList<>();
        this.enemiesDestroyed = 0;
        //this.enemyCount = Constants.DEFAULT_GENERATE_ENEMY; // Will be set based on difficulty
        this.random = new Random();

        DifficultyManager difficultyManager = new DifficultyManager(context);
        Difficulty currentDifficulty = difficultyManager.loadDifficulty();

        switch (currentDifficulty) {
            case EASY:
                this.enemyCount = Math.max(1, Constants.DEFAULT_GENERATE_ENEMY - 1);
                break;
            case MEDIUM:
                this.enemyCount = Constants.DEFAULT_GENERATE_ENEMY;
                break;
            case HARD:
                this.enemyCount = Constants.DEFAULT_GENERATE_ENEMY + 2;
                break;
            default:
                this.enemyCount = Constants.DEFAULT_GENERATE_ENEMY;
                break;
        }

        DeadNotifier deadNotifier = new DeadNotifier(playerShip, context);
        Log.d("GamePlayingState", "PlayerShipHealth: " + playerShip.getHealth() + ", Initial EnemyCount: " + this.enemyCount);
        initializeEnemies();
    }

    private void initializeEnemies() {
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

        int baseHealth;
        int baseSpeed; // Default speed is 20 from GameObject
        int score;
        int drawable;

        switch (enemyType) {
            case 0: // NORMAL
                baseHealth = Constants.ENEMY_SHIP_NORMAL_HEALTH;
                baseSpeed = 20; // Default GameObject speed
                score = 10;
                drawable = Constants.ENEMY_SHIP_NORMAL;
                break;
            case 1: // FAST
                baseHealth = Constants.ENEMY_SHIP_FAST_HEALTH;
                baseSpeed = 30; // Specific speed for fast ship
                score = 20;
                drawable = Constants.ENEMY_SHIP_FAST;
                break;
            case 2: // TANK
            default: // Should not happen with random.nextInt(3) but good practice
                baseHealth = Constants.ENEMY_SHIP_TANK_HEALTH;
                baseSpeed = 20; // Default GameObject speed
                score = 30;
                drawable = Constants.ENEMY_SHIP_TANK;
                break;
        }

        // Adjust stats based on difficulty
        float healthModifier = 1.0f;
        float speedModifier = 1.0f;

        switch (currentDifficulty) {
            case EASY:
                healthModifier = 0.75f;
                speedModifier = 0.8f;
                break;
            case MEDIUM:
                // No change from base
                break;
            case HARD:
                healthModifier = 1.25f;
                speedModifier = 1.2f;
                break;
        }

        int finalHealth = Math.max(1, (int) Math.ceil(baseHealth * healthModifier));
        int finalSpeed = Math.round(baseSpeed * speedModifier);

        enemyShip = gameCharacterService.createEnemyShip(new Point(x, y), drawable);
        enemyShip.setHealth(finalHealth);
        enemyShip.setSpeed(finalSpeed);
        enemyShip.setScore(score); // Score could also be adjusted by difficulty if desired

        // enemyShip.setPoint(new Point(x, y)); // setPoint is already called in createEnemyShip from service
        enemies.add(enemyShip);
        DeadNotifier deadNotifier = new DeadNotifier(enemyShip, context);
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
                Log.d("GamePlayingState", "PlayerShipHealth desc: " + playerShip.getHealth());
                if (playerShip.getHealth() <= 0) {
                    Log.d("GamePlayingState", "PlayerShipHealth dead: " + playerShip.getHealth());
                    playerShip.setExplosion(new Explosion(backgroundManager.getResources(), Constants.EXPLOSION, playerShip.getPoint(), 128, 4));
                    explosions.add(playerShip.getExplosion());
                }
                destroyedEnemies.add(enemy);
            }
            for (Bullet bullet : playerShip.getBullets()) {
                if (Rect.intersects(bullet.getBounds(), enemy.getBounds())) {
                    enemy.setHealth(enemy.getHealth() - 1);
                    destroyedBullets.add(bullet);
                    if(enemy.getHealth() <= 0) {
                        enemy.setExplosion(new Explosion(backgroundManager.getResources(), Constants.EXPLOSION, enemy.getPoint(), 128, 4));
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