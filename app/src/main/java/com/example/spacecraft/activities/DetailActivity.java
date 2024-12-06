package com.example.spacecraft.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.spacecraft.R;
import com.example.spacecraft.databinding.ActivityDetailBinding;
import com.example.spacecraft.models.app.Profile;
import com.example.spacecraft.services.FirebaseService;
import com.google.android.gms.tasks.OnSuccessListener;

public class DetailActivity extends AppCompatActivity {
    private ActivityDetailBinding binding;
    private Profile profile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getExtra();
        initializeUI();
        setEventListeners();
    }

    private void initializeUI() {
        bindToCard();
    }

    private void bindToCard() {
        if(profile!=null){
            binding.tvId.setText(profile.getId());
            binding.tvUsername.setText(profile.getUsername());
            binding.tvHighestScore.setText(String.valueOf(profile.getHighestScore()));
        }
    }

    private void getExtra() {
        profile = (Profile) getIntent().getSerializableExtra("profile");
    }


    private void setEventListeners() {
        binding.btnBack.setOnClickListener(v -> finish());
        binding.btnSave.setOnClickListener(v -> {
            try{
                getFormData();
                new FirebaseService().storeProfile(profile).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(DetailActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                    }
                });
            }catch(NullPointerException e){
                Toast.makeText(this, "Please provide full information", Toast.LENGTH_SHORT).show();
            }catch (Exception e){
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }

        });

    }

    private void getFormData() {
        if(profile!=null){
            profile.setDescription(binding.etDescription.getText().toString());
            profile.setQuanity(Integer.parseInt(binding.etQuantity.getText().toString()));
            profile.setTimestamp(System.currentTimeMillis());
        }
    }
}