package com.gmwapp.slv_qr_card.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gmwapp.slv_qr_card.R;
import com.gmwapp.slv_qr_card.helper.Session;
import com.gmwapp.slv_qr_card.model.PaymentScreenshot;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class PaymentScreenshotListAdapter extends RecyclerView.Adapter<PaymentScreenshotListAdapter.PaymentScreenshotListViewHolder> {

    private final Context context;
    private final Session session;
    private final Activity activity;
    private final List<PaymentScreenshot> paymentScreenshotList;

    public PaymentScreenshotListAdapter(Activity activity, Context context, List<PaymentScreenshot> paymentScreenshotList) {
        this.activity = activity;
        this.session = new Session(activity);
        this.context = context;
        this.paymentScreenshotList = paymentScreenshotList;
    }

    @NonNull
    @Override
    public PaymentScreenshotListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_payment_screenshot_list, parent, false);
        return new PaymentScreenshotListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentScreenshotListViewHolder holder, int position) {
        PaymentScreenshot paymentScreenshot = paymentScreenshotList.get(position);

        // Bind the data to the views
        Glide.with(activity)
                .load(paymentScreenshot.getImage()) // Assuming `getImage()` returns a URL or URI
                .placeholder(R.drawable.image_icon) // Optional: Add a placeholder
                .error(R.drawable.image_icon) // Optional: Add an error image
                .into(holder.ivImage);

        holder.tvStatus.setText(getStatusTitle(paymentScreenshot.getStatus()));
        holder.tvDateTime.setText(paymentScreenshot.getDatetime());
        holder.tvDateTime.setVisibility(View.VISIBLE);

        // Activate plan button logic
        holder.btnZoom.setOnClickListener(view -> {
            dialogZoomable(paymentScreenshot.getImage());
        });
    }

    @Override
    public int getItemCount() {
        return paymentScreenshotList.size();
    }

    public void dialogZoomable(String imageUrl) {
    AlertDialog.Builder builder = new AlertDialog.Builder(activity); // Context: Activity
    LayoutInflater inflater = activity.getLayoutInflater(); // LayoutInflater
    View dialogView = inflater.inflate(R.layout.dialog_zoomable_image, null);

    com.github.chrisbanes.photoview.PhotoView photoView = dialogView.findViewById(R.id.photoView);

    // Load image with Glide
    Glide.with(activity)
            .load(imageUrl)
            .placeholder(R.drawable.image_icon)
            .error(R.drawable.image_icon)
            .into(photoView);

    // Initialize buttons
    Button btnZoomIn = dialogView.findViewById(R.id.btnZoomIn);
    Button btnZoomOut = dialogView.findViewById(R.id.btnZoomOut);
    ImageButton btnClose = dialogView.findViewById(R.id.btnClose);

    builder.setView(dialogView);
    AlertDialog dialog = builder.create();

    // **Zoom In Button**
    btnZoomIn.setOnClickListener(v -> {
        float currentScale = photoView.getScale();
        float maxScale = photoView.getMaximumScale();
        float newScale = Math.min(currentScale * 1.2f, maxScale); // Clamp to maxScale
        photoView.setScale(newScale, true);
    });

    // **Zoom Out Button**
    btnZoomOut.setOnClickListener(v -> {
        float currentScale = photoView.getScale();
        float minScale = photoView.getMinimumScale();
        float newScale = Math.max(currentScale / 1.2f, minScale); // Clamp to minScale
        photoView.setScale(newScale, true);
    });

    // **Close Button**
    btnClose.setOnClickListener(v -> {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    });

    // Set rounded corner background
    if (dialog.getWindow() != null) {
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);
    }

    // Display the dialog
    dialog.show();
}

    private String getStatusTitle(String status) {
        switch (status) {
            case "1":
                return "Paid";
            case "2":
                return "Cancelled";
            default:
                return "Pending";
        }
    }

    static class PaymentScreenshotListViewHolder extends RecyclerView.ViewHolder {
        final ImageView ivImage;
        final TextView tvStatus;
        MaterialButton btnZoom;
        final TextView tvDateTime;

        public PaymentScreenshotListViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            btnZoom = itemView.findViewById(R.id.btnZoom);
            tvDateTime = itemView.findViewById(R.id.tvDateTime);
        }
    }
}