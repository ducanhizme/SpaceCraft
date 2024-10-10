package com.example.spacecraft;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.spacecraft.adapter.ProfileAdapter;
import com.example.spacecraft.component.ProfileDialog;
import com.example.spacecraft.databinding.ActivityMainBinding;
import com.example.spacecraft.model.Profile;
import com.example.spacecraft.service.ProfileService;

import java.util.List;

public class MainActivity extends AppCompatActivity implements ProfileDialog.DialogListener {
    private ActivityMainBinding binding;
    private List<Profile> profiles;
    private ProfileService profileService;
    private ProfileDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        profileService = new ProfileService(this);
        profileService.clearProfileIdInPrefs();
        showCustomDialog();
        binding.playBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, GameActivity.class));
        });
    }

    private void showCustomDialog() {
        if (profileService.getProfileIdInPrefs() < 0){
            dialog = new ProfileDialog(this);
            dialog.show(getSupportFragmentManager(), ProfileDialog.TAG);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        profiles = profileService.getAllProfiles();
        binding.profileRv.setAdapter(new ProfileAdapter(this, profiles));
    }

    @Override
    public void onDialogPositiveClick(String inputText) {
        Profile profile = new Profile(inputText, 0);
        long profileId = profileService.addProfile(profile);
        profileService.saveProfileIdToPrefs(profileId);

    }
}