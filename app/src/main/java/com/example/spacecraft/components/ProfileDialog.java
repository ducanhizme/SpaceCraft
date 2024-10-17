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
    private static final String ARG_TITLE = "title";
    private static final String ARG_MESSAGE = "message";
    private ProfileDialogFragmentBinding binding;
    private DialogListener listener;

    public ProfileDialog(DialogListener listener) {
        this.listener = listener;
    }

    public static ProfileDialog newInstance(DialogListener listener, String title, String message) {
        ProfileDialog dialog = new ProfileDialog(listener);
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putString(ARG_MESSAGE, message);
        dialog.setArguments(args);
        return dialog;
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

        if (getArguments() != null) {
            String title = getArguments().getString(ARG_TITLE);
            String message = getArguments().getString(ARG_MESSAGE);
            binding.titleTv.setText(title);
            binding.messageTv.setText(message);
        }

        binding.saveProfileBtn.setOnClickListener(v -> {
            String profileName = binding.profileNameEt.getText().toString();
            if (profileName.isEmpty()) {
                AppToast.makeText(getContext(), "Please enter a profile name", Toast.LENGTH_SHORT);
            } else {
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
        void onDialogNegativeClick();
    }
}