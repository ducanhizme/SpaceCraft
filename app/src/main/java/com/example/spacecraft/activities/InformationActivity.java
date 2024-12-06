package com.example.spacecraft.activities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.spacecraft.R;
import com.example.spacecraft.databinding.ActivityInformationBinding;
import com.example.spacecraft.models.app.Profile;

public class InformationActivity extends AppCompatActivity {
    private ActivityInformationBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInformationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initUi();
    }

    private void initUi() {
        Profile profile = (Profile) getIntent().getSerializableExtra("profile");
        if(profile!=null){
            binding.id.setText(profile.getDescription());
            binding.nameTv.setText(String.valueOf(profile.getTimestamp()));
            binding.score.setText(String.valueOf(profile.getQuanity()));
        }
    }
}