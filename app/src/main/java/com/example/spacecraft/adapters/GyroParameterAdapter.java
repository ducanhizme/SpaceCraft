package com.example.spacecraft.adapters;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spacecraft.databinding.ItemGyroParameterBinding;
import com.example.spacecraft.databinding.ItemHighestScoreBinding;
import com.example.spacecraft.models.app.GyroParameter;
import com.example.spacecraft.models.app.Profile;

import java.util.List;

public class GyroParameterAdapter extends RecyclerView.Adapter<GyroParameterAdapter.ViewHolder> {
    private final List<GyroParameter> gyroParameters;

    public GyroParameterAdapter(List<GyroParameter> gyroParameters) {
        this.gyroParameters = gyroParameters;
    }
    @NonNull
    @Override
    public GyroParameterAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemGyroParameterBinding binding = ItemGyroParameterBinding.inflate(layoutInflater, parent, false);
        return new GyroParameterAdapter.ViewHolder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull GyroParameterAdapter.ViewHolder holder, int position) {
        GyroParameter gyroParameter = gyroParameters.get(position);
        holder.binding.xLabelTextView.setText("X: "+ gyroParameter.getRotationX());
        holder.binding.yLabelTextView.setText("Y: "+ gyroParameter.getRotationY());
        holder.binding.zLabelTextView.setText("Z: "+ gyroParameter.getRotationZ());
        holder.binding.titleTextView.setText("#"+(position+1)+ ": "+gyroParameter.getTitle());
    }

    @Override
    public int getItemCount() {
        return gyroParameters.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final ItemGyroParameterBinding binding;

        public ViewHolder(@NonNull ItemGyroParameterBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
