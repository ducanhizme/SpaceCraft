package com.example.spacecraft.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.spacecraft.R;
import com.example.spacecraft.databinding.ActivityAddStudentsBinding;
import com.example.spacecraft.models.app.Student;
import com.example.spacecraft.services.FirebaseService;

import java.util.Objects;

public class AddStudentsActivity extends AppCompatActivity {

    private ActivityAddStudentsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityAddStudentsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setEventListener();
    }

    private void setEventListener() {
        binding.buttonSubmit.setOnClickListener(v -> {
            Student student = getFormData();
            if (student == null) {
                Toast.makeText(this, "Some thing went wrong", Toast.LENGTH_SHORT).show();
                return;
            }
            new FirebaseService().storeStudent(student).addOnSuccessListener(aVoid -> {
                Toast.makeText(this, "Student added", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, ListStudentsActivity.class));
            }).addOnFailureListener(e -> {
                Toast.makeText(this, "Some thing went wrong", Toast.LENGTH_SHORT).show();
            });
        });
        binding.listBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, ListStudentsActivity.class));
        });
    }

    private Student getFormData() {
        try {
            String name = Objects.requireNonNull(binding.editTextName.getText()).toString();
            boolean gender = binding.radioGroupGender.getCheckedRadioButtonId() == R.id.radioButtonMale;
            String day = Objects.requireNonNull(binding.editTextDay.getText()).toString();
            String month = Objects.requireNonNull(binding.editTextMonth.getText()).toString();
            String year = Objects.requireNonNull(binding.editTextYear.getText()).toString();
            String birthday = day + "/" + month + "/" + year;
            String heightStr = Objects.requireNonNull(binding.editTextHeight.getText()).toString();
            int height = Integer.parseInt(heightStr);
            String weightStr = Objects.requireNonNull(binding.editTextWeight.getText()).toString();
            float weight = Float.parseFloat(weightStr);
            return new Student(name, gender, birthday, height, weight);
        } catch (NullPointerException e) {
            Toast.makeText(this, "Please provide every field", Toast.LENGTH_SHORT).show();
            return null;
        }
    }
}