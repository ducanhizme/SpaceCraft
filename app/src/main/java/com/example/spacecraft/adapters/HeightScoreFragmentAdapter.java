package com.example.spacecraft.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.spacecraft.fragments.GlobalHeightScoreFragment;
import com.example.spacecraft.fragments.LocalHeightScoreFragment;

public class HeightScoreFragmentAdapter extends FragmentStateAdapter {

    public HeightScoreFragmentAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 1) {
            return new GlobalHeightScoreFragment();
        }
        return new LocalHeightScoreFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
