package com.example.spacecraft.components;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.spacecraft.R;
import com.example.spacecraft.adapters.HighestScoreFragmentAdapter;
import com.example.spacecraft.databinding.HighestScoreDialogBinding;
import com.example.spacecraft.fragments.GlobalHighestScoreFragment;
import com.example.spacecraft.fragments.LocalHighestScoreFragment;
import com.google.android.material.tabs.TabLayoutMediator;

public class HighestScoreDialog extends DialogFragment {
    public static final String TAG = "HeightScoreDialog";
    private HighestScoreDialogBinding binding;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        if (dialog.getWindow() != null) {
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = HighestScoreDialogBinding.inflate(inflater, container, false);
        return binding.getRoot();


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.TransparentDialog);
        initializeUI();
    }

    private void initializeUI() {
        setViewPagerAdapter();
        setEventListeners();
    }

    private void setEventListeners() {
        binding.backBtn.setOnClickListener(v -> dismiss());
    }

    private void setViewPagerAdapter() {
        HighestScoreFragmentAdapter adapter = new HighestScoreFragmentAdapter(this,getActivity());
        binding.pager.setAdapter(adapter);
        new TabLayoutMediator(binding.tabLayout, binding.pager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText(LocalHighestScoreFragment.TAG);
                    break;
                case 1:
                    tab.setText(GlobalHighestScoreFragment.TAG);
                    break;
            }
        }).attach();
    }
}
