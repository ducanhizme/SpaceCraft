package com.example.spacecraft.components;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.example.spacecraft.databinding.GameOverDialogBinding;

public class GameOverDialog extends DialogFragment {
    public static final String TAG = "GameOverDialog";
    private final String score;
    private GameOverDialogBinding binding;
    private final ProfileDialog.DialogListener listener;
    public GameOverDialog(ProfileDialog.DialogListener listener, String score) {
        this.listener = listener;
        this.score = score;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = GameOverDialogBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.scoreTv.setText(score);
        binding.saveHeightScoreBtn.setOnClickListener(v -> {
            listener.onDialogPositiveClick(binding.scoreTv.getText().toString());
        });

        binding.replayGameBtn.setOnClickListener(v -> {
            listener.onDialogNegativeClick();
            dismiss();
        });
    }
}
