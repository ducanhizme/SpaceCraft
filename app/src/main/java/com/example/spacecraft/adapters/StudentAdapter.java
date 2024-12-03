package com.example.spacecraft.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spacecraft.R;
import com.example.spacecraft.databinding.ItemStudentBinding;
import com.example.spacecraft.models.app.Student;

import java.util.List;


public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {
    private final List<Student> students;

    public StudentAdapter(List<Student> students) {
        this.students = students;
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student, parent, false);
        return new StudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        Student student = students.get(position);
        holder.bind(student);
    }

    @Override
    public int getItemCount() {
        return students.size();
    }

    public static class StudentViewHolder extends RecyclerView.ViewHolder {
        private final ItemStudentBinding binding;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemStudentBinding.bind(itemView);
        }

        public void bind(Student student) {
            binding.textViewName.setText(student.getName());
            binding.chipGender.setText(student.isGender() ? "Nam" : "Nữ");
            binding.textViewBirthday.setText(student.getBirthdate());
            binding.chipHeight.setText(String.valueOf(student.getHeight()));
            binding.chipWeight.setText(String.valueOf(student.getWeight()));
        }
    }
}