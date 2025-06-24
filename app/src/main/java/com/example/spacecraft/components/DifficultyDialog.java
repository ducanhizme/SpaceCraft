package com.example.spacecraft.components;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import com.example.spacecraft.R;
import com.example.spacecraft.services.DifficultyManager;
import com.example.spacecraft.utils.Difficulty;

public class DifficultyDialog extends DialogFragment {

    public static final String TAG = "DifficultyDialog";
    private RadioGroup difficultyRadioGroup;
    private RadioButton easyRadioButton, mediumRadioButton, hardRadioButton;
    private DifficultyManager difficultyManager;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_difficulty_select, null);

        difficultyManager = new DifficultyManager(requireContext());

        difficultyRadioGroup = view.findViewById(R.id.difficultyRadioGroup);
        easyRadioButton = view.findViewById(R.id.easyRadioButton);
        mediumRadioButton = view.findViewById(R.id.mediumRadioButton);
        hardRadioButton = view.findViewById(R.id.hardRadioButton);
        Button okButton = view.findViewById(R.id.okButton);
        Button cancelButton = view.findViewById(R.id.cancelButton);

        setCurrentDifficulty();

        okButton.setOnClickListener(v -> {
            Difficulty selectedDifficulty = Difficulty.MEDIUM; // Default
            int selectedId = difficultyRadioGroup.getCheckedRadioButtonId();
            if (selectedId == R.id.easyRadioButton) {
                selectedDifficulty = Difficulty.EASY;
            } else if (selectedId == R.id.mediumRadioButton) {
                selectedDifficulty = Difficulty.MEDIUM;
            } else if (selectedId == R.id.hardRadioButton) {
                selectedDifficulty = Difficulty.HARD;
            }
            difficultyManager.saveDifficulty(selectedDifficulty);
            dismiss();
        });

        cancelButton.setOnClickListener(v -> dismiss());

        builder.setView(view);
        // We are using custom buttons, so no need for builder.setPositive/NegativeButton
        // builder.setTitle("Select Difficulty"); // Title is in the XML

        return builder.create();
    }

    private void setCurrentDifficulty() {
        Difficulty currentDifficulty = difficultyManager.loadDifficulty();
        switch (currentDifficulty) {
            case EASY:
                easyRadioButton.setChecked(true);
                break;
            case MEDIUM:
                mediumRadioButton.setChecked(true);
                break;
            case HARD:
                hardRadioButton.setChecked(true);
                break;
        }
    }
}
