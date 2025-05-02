package com.gmwapp.slv_qr_card.Adapter;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gmwapp.slv_qr_card.R;
import com.gmwapp.slv_qr_card.model.TypeData;

import java.util.Collections;
import java.util.List;

public class TypeAdapter extends RecyclerView.Adapter<TypeAdapter.LevelTypeViewHolder> {

    private final List<TypeData> typeData;
    private int selectedPosition = 0; // Default to the first item
    private final OnLevelSelectedListener listener;

    public interface OnLevelSelectedListener {
        void onLevelSelected(String levelId);
    }

    public TypeAdapter(List<TypeData> typeData, OnLevelSelectedListener listener) {
        this.typeData = typeData;
        this.listener = listener;
    }

    public static class LevelTypeViewHolder extends RecyclerView.ViewHolder {
        TextView levelItem;

        public LevelTypeViewHolder(@NonNull View itemView) {
            super(itemView);
            levelItem = itemView.findViewById(R.id.level_item);
        }
    }

    @NonNull
    @Override
    public LevelTypeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_type, parent, false);
        return new LevelTypeViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull LevelTypeViewHolder holder, int position) {
        TypeData levelTypeData = typeData.get(position);

        // Set item text
        holder.levelItem.setText(levelTypeData.getName());

        // Check if the item is selected
        boolean isSelected = position == selectedPosition;

        // Smoothly animate text color change
        int startColor = isSelected ? Color.BLACK : Color.WHITE;
        int endColor = isSelected ? Color.WHITE : Color.BLACK;

        ObjectAnimator colorAnim = ObjectAnimator.ofObject(
                holder.levelItem, "textColor", new ArgbEvaluator(), startColor, endColor);
        colorAnim.setDuration(300);
        colorAnim.start();

        // Background animation
        if (position == selectedPosition) {
            holder.levelItem.setBackgroundResource(R.drawable.selected_background);
            holder.levelItem.setTextColor(holder.itemView.getContext().getColor(R.color.white));
        } else {
            holder.levelItem.setBackgroundResource(R.drawable.round_border);
            holder.levelItem.setTextColor(holder.itemView.getContext().getColor(R.color.primaryColor));
        }

        // Handle item click
        holder.itemView.setOnClickListener(v -> {
            int previousPosition = selectedPosition;
            selectedPosition = position;

            if (previousPosition != -1) {
                notifyItemChanged(previousPosition); // Unselect previous
            }
            notifyItemChanged(selectedPosition); // Select new

            // Notify listener
            listener.onLevelSelected(levelTypeData.getId());
        });
    }



    @Override
    public int getItemCount() {
        return typeData.size();
    }
}



