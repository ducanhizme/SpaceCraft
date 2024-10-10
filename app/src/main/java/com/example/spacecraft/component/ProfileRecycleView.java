package com.example.spacecraft.component;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spacecraft.databinding.ProfileRecycleViewBinding;

import java.util.Objects;

public class ProfileRecycleView extends LinearLayout {
    public static final String TAG = "ProfileRecycleView";
    private LinearLayoutManager layoutManager;
    private int currentProfileIndex;
    private ProfileRecycleViewBinding binding;

    public ProfileRecycleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        binding = ProfileRecycleViewBinding.inflate(LayoutInflater.from(context), this);
        layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        binding.recyclerView.setLayoutManager(layoutManager);
        binding.rightChangeProfileBtn.setOnClickListener(v -> scrollToNextItem());
        binding.leftChangeProfileBtn.setOnClickListener(v -> scrollToPreviousItem());
    }

    private void scrollToPreviousItem() {
        if (currentProfileIndex > 0) {
            currentProfileIndex--;
            binding.recyclerView.smoothScrollToPosition(currentProfileIndex);
        } else {
            currentProfileIndex = Objects.requireNonNull(binding.recyclerView.getAdapter()).getItemCount() - 1;
            binding.recyclerView.scrollToPosition(currentProfileIndex);
        }
    }

    private void scrollToNextItem() {
        try {
            if (currentProfileIndex < Objects.requireNonNull(binding.recyclerView.getAdapter()).getItemCount() - 1) {
                currentProfileIndex++;
                binding.recyclerView.smoothScrollToPosition(currentProfileIndex);
            } else {
                currentProfileIndex = 0;
                binding.recyclerView.scrollToPosition(currentProfileIndex);
            }
        } catch (Exception e) {
            Log.e(TAG, "scrollToNextItem: ", e);
        }
    }


    public void setAdapter(RecyclerView.Adapter adapter) {
        binding.recyclerView.setAdapter(adapter);
    }

    public int getCurrentProfileIndex() {
        return currentProfileIndex;
    }

    public void setCurrentProfileIndex(int currentProfileIndex) {
        this.currentProfileIndex = currentProfileIndex;
    }

}
