package com.example.spacecraft.adapters;

import android.app.AlertDialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spacecraft.R;
import com.example.spacecraft.activities.DetailActivity;
import com.example.spacecraft.activities.InformationActivity;
import com.example.spacecraft.databinding.ItemHighestScoreBinding;
import com.example.spacecraft.models.app.Profile;

import java.util.List;

public class HighestScoreAdapter extends RecyclerView.Adapter<HighestScoreAdapter.ViewHolder> {
    private final List<Profile> profiles;

    public HighestScoreAdapter(List<Profile> profiles) {
        this.profiles = profiles;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemHighestScoreBinding binding = ItemHighestScoreBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Profile profile = profiles.get(position);
        holder.binding.positionTv.setText(String.valueOf(position + 1));
        holder.binding.nameTv.setText(profile.getUsername());
        holder.binding.scoreTv.setText(String.valueOf(profile.getHighestScore()));
        int color;
        switch (position) {
            case 0:
                color = holder.itemView.getContext().getResources().getColor(R.color.gold, null);
                break;
            case 1:
                color = holder.itemView.getContext().getResources().getColor(R.color.silver, null);
                break;
            case 2:
                color = holder.itemView.getContext().getResources().getColor(R.color.bronze, null);
                break;
            default:
                color = holder.itemView.getContext().getResources().getColor(R.color.white, null);
                break;
        }
        holder.binding.positionTv.setTextColor(color);
        holder.binding.getRoot().setOnClickListener(v -> {
            new AlertDialog.Builder(holder.itemView.getContext())
                    .setTitle("Choose an action")
                    .setMessage("Would you like to view details or edit?")
                    .setPositiveButton("Detail", (dialog, which) -> {
                        Intent intent = new Intent(holder.itemView.getContext(), InformationActivity.class);
                        intent.putExtra("profile", profile);
                        holder.itemView.getContext().startActivity(intent);
                    })
                    .setNegativeButton("Edit", (dialog, which) -> {
                        Intent intent = new Intent(holder.itemView.getContext(), DetailActivity.class);
                        intent.putExtra("profile", profile);
                        holder.itemView.getContext().startActivity(intent);
                    })
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return profiles.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final ItemHighestScoreBinding binding;

        public ViewHolder(@NonNull ItemHighestScoreBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


}
