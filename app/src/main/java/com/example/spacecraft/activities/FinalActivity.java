package com.example.spacecraft.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.spacecraft.R;
import com.example.spacecraft.adapters.GyroParameterAdapter;
import com.example.spacecraft.databinding.ActivityFinalBinding;
import com.example.spacecraft.models.app.GyroParameter;
import com.example.spacecraft.services.FirebaseService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FinalActivity extends AppCompatActivity {
    private ActivityFinalBinding binding;
    private GyroParameter gyroParameter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityFinalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getIntentExtra();
        initUI();
        setEventListener();
    }

    private void setEventListener(){
        binding.saveButton.setOnClickListener(v -> {
            if(gyroParameter != null){
                try{
                    gyroParameter.setTitle(Objects.requireNonNull(binding.titleEditText.getText()).toString());
                    new FirebaseService().storeGyroParameter(gyroParameter).addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            Toast.makeText(this, "Data saved", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(this, "Data not saved", Toast.LENGTH_SHORT).show();
                        }
                    });
                }catch (NullPointerException e){
                    Toast.makeText(this, "Please enter a title", Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    Toast.makeText(this,"Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.backBtn.setOnClickListener(v -> {
            finish();
        });
    }

    private void initUI() {
        bindToCard();
        bindToList();
    }

    private void bindToList() {
        List<GyroParameter> gyroParameterList = new ArrayList<>();
        binding.progressBar.setVisibility(View.VISIBLE);
        new FirebaseService().getGyroParametersFromFirebase(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    GyroParameter gyroParameter = dataSnapshot.getValue(GyroParameter.class);
                    gyroParameterList.add(gyroParameter);
                }
                binding.recyleView.setLayoutManager(new LinearLayoutManager(FinalActivity.this));
                binding.recyleView.setAdapter(new GyroParameterAdapter(gyroParameterList));
                binding.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

    }

    private void bindToCard() {
        if(gyroParameter!=null){
            binding.xParameterTextView.setText(String.valueOf(gyroParameter.getRotationX()));
            binding.yParameterTextView.setText(String.valueOf(gyroParameter.getRotationY()));
            binding.zParameterTextView.setText(String.valueOf(gyroParameter.getRotationZ()));
        }else{
            Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
        }
    }


    private void getIntentExtra() {
        gyroParameter = (GyroParameter) getIntent().getSerializableExtra(GyroParameter.TAG);
    }
}