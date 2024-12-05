package com.example.spacecraft.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.spacecraft.adapters.ProfileAdapter;
import com.example.spacecraft.components.GameView;
import com.example.spacecraft.components.HighestScoreDialog;
import com.example.spacecraft.components.ProfileDialog;
import com.example.spacecraft.databinding.ActivityMainBinding;
import com.example.spacecraft.models.app.GyroParameter;
import com.example.spacecraft.models.app.Profile;
import com.example.spacecraft.services.FirebaseService;
import com.example.spacecraft.services.GoogleAuthService;
import com.example.spacecraft.services.ProfileService;

import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private ActivityMainBinding binding;
    private List<Profile> profiles;
    private ProfileService profileService;
    private GoogleAuthService googleAuthService;
    private GameView gameView;
    private ActivityResultLauncher<Intent> signInLauncher;
    private SensorManager sensorManager;
    private Sensor gyroscopeSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        googleAuthService = new GoogleAuthService(this);
        profileService = new ProfileService(this);
        initializeUI();
        loadProfiles();
        registerSignInLauncher();
    }

    private void initializeUI() {
        setupGameView();
        setupButtonListeners();
    }

    private void setupGameView() {
        gameView = new GameView(this);
        binding.main.addView(gameView, 0);
    }

    @SuppressLint("SetTextI18n")
    private void setupButtonListeners() {
        binding.playBtn.setOnClickListener(v -> startGameActivity());
        binding.profileBtn.setOnClickListener(v -> showProfileDialog(true));
        binding.heightScoreBtn.setOnClickListener(v -> showHeightScoreDialog());
    }

    private void showHeightScoreDialog() {
        HighestScoreDialog dialog = new HighestScoreDialog();
        dialog.show(getSupportFragmentManager(), HighestScoreDialog.TAG);
    }

    private void startGameActivity() {
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    private void showProfileDialog(boolean isCancelable) {
        ProfileDialog dialog = ProfileDialog.newInstance(new ProfileDialog.DialogListener() {
            @Override
            public void onDialogPositiveClick(String inputText) {
                createProfile(inputText);
                updateProfileAdapter();
            }

            @Override
            public void onDialogNegativeClick() {

            }
        }, "Profile", "Enter your new profile name to start new game");
        dialog.setCancelable(isCancelable);
        dialog.show(getSupportFragmentManager(), ProfileDialog.TAG);
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkCurrentProfile();
        updateProfileAdapter();
        updateHeightScoreToFirebase();
        try{
            int currentProfileId = binding.profileRv.getCurrentProfileIndex();
            profileService.saveProfileIdToPrefs((int)profiles.get(currentProfileId).getId()+1);
        }catch (IndexOutOfBoundsException e){
            Log.d("MainActivity", "onStart: "+e.getMessage());
        }
    }

    private void updateHeightScoreToFirebase() {
        if (googleAuthService.getCurrentUser() != null) {
            Profile profile = profileService.getProfileHaveHighestScores();
            if (profile != null) {
                new FirebaseService().storeGoogleAuthUser(googleAuthService.getCurrentUser(), profile.getHighestScore());
            }
        }
    }

    private void checkCurrentProfile() {
        if (profileService.getProfileIdInPrefs() < 0) {
            showProfileDialog(false);
        }
    }

    private void createProfile(String inputText) {
        Profile profile = new Profile(inputText, 0);
        int profileId = profileService.addProfile(profile);
        profileService.saveProfileIdToPrefs(profileId);
    }

    private void updateProfileAdapter() {
        profiles = profileService.getAllProfiles();
        binding.profileRv.setAdapter(new ProfileAdapter(this, profiles));
        scrollToCurrentProfile();
    }

    private void scrollToCurrentProfile() {
        long currentProfileId = profileService.getProfileIdInPrefs();
        if (currentProfileId >= 0) {
            int currentIndex = findProfileIndexById(currentProfileId);
            if (currentIndex >= 0) {
                binding.profileRv.getRecyclerView().scrollToPosition(currentIndex);
            }
        }
    }

    private int findProfileIndexById(long profileId) {
        for (int i = 0; i < profiles.size(); i++) {
            if (profiles.get(i).getId() == profileId) {
                return i;
            }
        }
        return -1;
    }

    private void loadProfiles() {
        profiles = profileService.getAllProfiles();
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameView.resume();
        if (gyroscopeSensor != null) {
            sensorManager.registerListener(this, gyroscopeSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        gameView.pause();
        if (gyroscopeSensor != null) {
            sensorManager.unregisterListener(this);
        }
    }

    private void registerSignInLauncher() {
        signInLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                googleAuthService.handleSignInResult(result.getData(), () -> {
                    Log.d("MainActivity", "handleSignInResult: ");
                });
            }
        });
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            float rotationRateX = event.values[0];
            float rotationRateY = event.values[1];
            float rotationRateZ = event.values[2];
            if(rotationRateX !=0 && rotationRateY !=0&& rotationRateZ !=0){
                GyroParameter gyroParameter = new GyroParameter(rotationRateX, rotationRateY, rotationRateZ);
                Intent intent = new Intent(this, FinalActivity.class);
                intent.putExtra(GyroParameter.TAG, gyroParameter);
                startActivity(intent);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


}