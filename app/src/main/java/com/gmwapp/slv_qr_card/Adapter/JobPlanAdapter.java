package com.gmwapp.slv_qr_card.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.gmwapp.slv_qr_card.R;
import com.gmwapp.slv_qr_card.activities.MainActivity;
import com.gmwapp.slv_qr_card.fragment.JobsFragment;
import com.gmwapp.slv_qr_card.helper.ApiConfig;
import com.gmwapp.slv_qr_card.helper.Constant;
import com.gmwapp.slv_qr_card.helper.Session;
import com.gmwapp.slv_qr_card.model.PlanListModel;
import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.material.button.MaterialButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JobPlanAdapter extends RecyclerView.Adapter<JobPlanAdapter.PlanViewHolder> {
    private final List<PlanListModel> planListModels;
    private final Session session;
    private final Activity activity;
    private final JobsFragment jobsFragment;

    public JobPlanAdapter(Activity activity, List<PlanListModel> planListModels, JobsFragment jobsFragment) {
        this.activity = activity;
        this.planListModels = planListModels;
        this.session = new Session(activity);
        this.jobsFragment = jobsFragment;
    }

    @NonNull
    @Override
    public PlanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.job_plan_layout, parent, false);
        return new PlanViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull PlanViewHolder holder, int position) {
        PlanListModel plan = planListModels.get(position);

        holder.tvPlanPrice.setText(plan.getName());

        if (plan.getDescription().isEmpty()) {
            holder.tvDescription.setVisibility(View.GONE);
        } else {
            holder.tvDescription.setVisibility(View.VISIBLE);
            holder.tvDescription.setText(plan.getDescription());
        }

        if (plan.getInvite_bonus() != null && !plan.getInvite_bonus().isEmpty()) {
            holder.tvInviteBonus.setVisibility(View.VISIBLE);
            holder.tvInviteBonus.setText("Invite Bonus: ₹" + plan.getInvite_bonus());
        } else {
            holder.tvInviteBonus.setVisibility(View.GONE);
        }

        Glide.with(activity)
                .load(plan.getImage())
                .placeholder(R.drawable.image_icon)
                .error(R.drawable.image_icon)
                .into(holder.ivImage);

        // Enable/disable button based on 'enable' field
        if (plan.getEnable() == 1) {
            holder.btnStart.setEnabled(true);
            holder.btnStart.setBackgroundTintList(ContextCompat.getColorStateList(activity, R.color.teal_700));
        } else {
            holder.btnStart.setEnabled(false);
            holder.btnStart.setBackgroundTintList(ContextCompat.getColorStateList(activity, R.color.gray));
        }

        holder.btnStart.setOnClickListener(view -> {
            if (plan.getEnable() == 1) {
                activatedPlan(plan.getId());
            }
        });

        holder.ivImage.setOnClickListener(view -> dialogZoomable(plan.getImage()));
    }

    private void dialogZoomable(String imageUrl) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_zoomable_image, null);

        PhotoView photoView = dialogView.findViewById(R.id.photoView);

        Glide.with(activity)
                .load(imageUrl)
                .placeholder(R.drawable.image_icon)
                .error(R.drawable.image_icon)
                .into(photoView);

        MaterialButton btnZoomIn = dialogView.findViewById(R.id.btnZoomIn);
        MaterialButton btnZoomOut = dialogView.findViewById(R.id.btnZoomOut);
        ImageButton btnClose = dialogView.findViewById(R.id.btnClose);

        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        btnZoomIn.setOnClickListener(v -> {
            float currentScale = photoView.getScale();
            float maxScale = photoView.getMaximumScale();
            float newScale = Math.min(currentScale * 1.2f, maxScale);
            photoView.setScale(newScale, true);
        });

        btnZoomOut.setOnClickListener(v -> {
            float currentScale = photoView.getScale();
            float minScale = photoView.getMinimumScale();
            float newScale = Math.max(currentScale / 1.2f, minScale);
            photoView.setScale(newScale, true);
        });

        btnClose.setOnClickListener(v -> {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        });

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);
        }

        dialog.show();
    }

    private void activatedPlan(String planId) {
        Map<String, String> params = new HashMap<>();
        params.put(Constant.USER_ID, session.getData(Constant.USER_ID));
        params.put(Constant.PLAN_ID, planId);

        ApiConfig.RequestToVolley((result, response) -> {
            if (result) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString(Constant.MESSAGE);
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
                        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                        jobsFragment.loadPlans(true);
                        if (activity instanceof MainActivity) {
                            ((MainActivity) activity).userDetails();
                        }
                    } else {
                        Toast.makeText(activity, "Please upload your course certificate to activate the plan", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(activity, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, activity, Constant.ACTIVATE_PLAN, params, true);

        Log.d("ACTIVATE_PLAN", "ACTIVATE_PLAN: " + Constant.ACTIVATE_PLAN);
        Log.d("ACTIVATE_PLAN", "ACTIVATE_PLAN params: " + params);
    }

    @Override
    public int getItemCount() {
        return planListModels.size();
    }

    public static class PlanViewHolder extends RecyclerView.ViewHolder {
        TextView tvPlanPrice, tvDescription, tvInviteBonus;
        MaterialButton btnStart;
        PhotoView ivImage;

        public PlanViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPlanPrice = itemView.findViewById(R.id.tvPlanPrice);
            tvDescription = itemView.findViewById(R.id.tvPlanDescription);
            tvInviteBonus = itemView.findViewById(R.id.tvInviteBonus);
            btnStart = itemView.findViewById(R.id.btnStart);
            ivImage = itemView.findViewById(R.id.ivPlanImage);
        }
    }
}