package com.gmwapp.slv_qr_card.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gmwapp.slv_qr_card.Adapter.TypeAdapter;
import com.gmwapp.slv_qr_card.R;
import com.gmwapp.slv_qr_card.helper.Constant;
import com.gmwapp.slv_qr_card.helper.Session;
import com.gmwapp.slv_qr_card.model.TypeData;
import com.google.android.material.button.MaterialButton;
import androidx.core.widget.NestedScrollView;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    Session session;
    Activity activity;

    MaterialButton btnNotification;
    TextView tvEarningWallet, tvRefundWallet;
    LinearLayout llWaiting, cardType1;
    NestedScrollView frame;
    RecyclerView rvLevelType;
    ProgressBar progressBar;

    View root;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_home, container, false);
        activity = getActivity();
        session = new Session(activity);

        // Initialize views from XML
        btnNotification = root.findViewById(R.id.btnNotification);
        tvEarningWallet = root.findViewById(R.id.tvEarningWallet);
        tvRefundWallet = root.findViewById(R.id.tvRefundWallet);
        llWaiting = root.findViewById(R.id.llWaiting);
        cardType1 = root.findViewById(R.id.card_type1);
        frame = root.findViewById(R.id.frame);
        rvLevelType = root.findViewById(R.id.rv_level_type);
        progressBar = root.findViewById(R.id.progressBar);

        // Show loading first
        llWaiting.setVisibility(View.VISIBLE);
        frame.setVisibility(View.GONE);

        //setupRecyclerView();

        // Notification button click action
        btnNotification.setOnClickListener(v -> {
            NotificationFragment notificationFragment = new NotificationFragment();
            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.Container, notificationFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });

        // Simulate loading and fetch data
        fetchSessionDataAndInitialize();

        return root;
    }

    private void fetchSessionDataAndInitialize() {
        new Handler().postDelayed(() -> {
            llWaiting.setVisibility(View.GONE);
            frame.setVisibility(View.VISIBLE);
            updateWalletData();
        }, 2000);  // 2 seconds delay
    }

    private void updateWalletData() {
        String earningWallet = session.getData(Constant.EARNING_WALLET);
        String bonusWallet = session.getData(Constant.BONUS_WALLET);

        tvEarningWallet.setText("₹" + (earningWallet != null ? earningWallet : "0"));
        tvRefundWallet.setText("₹" + (bonusWallet != null ? bonusWallet : "0"));
    }

    private void setupRecyclerView() {
        List<TypeData> typeList = new ArrayList<>();
        typeList.add(new TypeData("b", "Dashboard"));
        typeList.add(new TypeData("c", "Work"));

        TypeAdapter typeAdapter = new TypeAdapter(typeList, levelId -> {
            if ("b".equals(levelId)) {
                cardType1.setVisibility(View.VISIBLE);
            } else {
                cardType1.setVisibility(View.GONE);
            }
        });

        rvLevelType.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvLevelType.setAdapter(typeAdapter);
    }
}
