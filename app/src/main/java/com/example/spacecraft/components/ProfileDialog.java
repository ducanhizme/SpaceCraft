package com.example.spacecraft.components;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.spacecraft.R;
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
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.TransparentDialog);
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
    }

    public interface DialogListener {
        void onDialogPositiveClick(String inputText);
        void onDialogNegativeClick();
    }
}