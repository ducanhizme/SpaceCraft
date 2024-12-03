package com.example.spacecraft.activities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.spacecraft.R;
import com.example.spacecraft.adapters.StudentAdapter;
import com.example.spacecraft.databinding.ActivityListStudentsBinding;
import com.example.spacecraft.models.app.Student;
import com.example.spacecraft.services.FirebaseService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListStudentsActivity extends AppCompatActivity {
    private ActivityListStudentsBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityListStudentsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
    }

    private void initView() {
        new FirebaseService().getStudentsFromFirebase(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Student> students = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Student student = snapshot.getValue(Student.class);
                    students.add(student);
                }
                binding.recycleView.setLayoutManager(new LinearLayoutManager(ListStudentsActivity.this));
                binding.recycleView.setAdapter(new StudentAdapter(students));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}