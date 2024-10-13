package com.example.spacecraft.components;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.spacecraft.databinding.ProfileDialogFragmentBinding;

public class ProfileDialog extends DialogFragment {

    public static final String TAG = "ProfileDialog";
    private  ProfileDialogFragmentBinding binding;
    private DialogListener listener;

    public ProfileDialog(DialogListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ProfileDialogFragmentBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.saveProfileBtn.setOnClickListener(v -> {
            String profileName = binding.profileNameEt.getText().toString();
            if (profileName.isEmpty()) {
                AppToast.makeText(getContext(), "Please enter a profile name", Toast.LENGTH_SHORT);
            }else {
                listener.onDialogPositiveClick(profileName);
                dismiss();
            }
        });
        binding.cancelProfileBtn.setOnClickListener(v -> {
            dismiss();
        });
    }

    public interface DialogListener {
        void onDialogPositiveClick(String inputText);
    }
}
