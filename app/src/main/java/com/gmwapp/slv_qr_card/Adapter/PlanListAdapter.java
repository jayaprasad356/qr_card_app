package com.gmwapp.slv_qr_card.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gmwapp.slv_qr_card.R;
import com.gmwapp.slv_qr_card.fragment.HomeFragment;
import com.gmwapp.slv_qr_card.helper.Session;
import com.gmwapp.slv_qr_card.model.PlanListModel;
import com.google.android.material.button.MaterialButton;

import java.util.List;


public class PlanListAdapter extends RecyclerView.Adapter<PlanListAdapter.PlanViewHolder> {
    private final List<PlanListModel> planListModels;
    private final Session session;
    private final Activity activity;
    private final HomeFragment homeFragment;

    // Constructor
    public PlanListAdapter(Activity activity, List<PlanListModel> planListModels, HomeFragment homeFragment1) {
        this.activity = activity;
        this.planListModels = planListModels;
        this.session = new Session(activity);
        this.homeFragment = homeFragment1;
    }

    @NonNull
    @Override
    public PlanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.plan_list_item, parent, false);
        return new PlanViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull PlanViewHolder holder, int position) {
        PlanListModel plan = planListModels.get(position);

        // Hide the first item
        if (position == 0) {
            holder.itemView.setVisibility(View.GONE);
            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0)); // Collapses the height
        } else {
            holder.itemView.setVisibility(View.VISIBLE);
            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(
                    RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));

            holder.tvPlanName.setText(plan.getName());

            holder.tvLink.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                activity.startActivity(intent);
            });

        }
    }


    @Override
    public int getItemCount() {
        return planListModels.size();
    }


    public static class PlanViewHolder extends RecyclerView.ViewHolder {
        TextView tvPlanName, tvDescription;
        MaterialButton tvLink;

        public PlanViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPlanName = itemView.findViewById(R.id.tvPlanName);
            tvLink = itemView.findViewById(R.id.btLink);
            tvDescription = itemView.findViewById(R.id.tvDescription);
        }
    }
}
