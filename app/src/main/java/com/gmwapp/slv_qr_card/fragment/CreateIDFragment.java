package com.gmwapp.slv_qr_card.fragment;

import static com.gmwapp.slv_qr_card.activities.MainActivity.fm;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.gmwapp.slv_qr_card.R;
import com.gmwapp.slv_qr_card.helper.Session;
import com.google.android.material.button.MaterialButton;


public class CreateIDFragment extends Fragment {


    public CreateIDFragment() {
        // Required empty public constructor
    }

    Session session;
    Activity activity;

    MaterialButton btBack;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_create_id, container, false);
        activity = getActivity();
        session = new Session(activity);

        // Retrieve data from the arguments
        Bundle args = getArguments();
        String companyName = args != null ? args.getString("CompanyName") : "";
        String city = args != null ? args.getString("City") : "";
        String country = args != null ? args.getString("Country") : "";
        String website = args != null ? args.getString("Website") : "";
        String email = args != null ? args.getString("Email") : "";
        String zipCode = args != null ? args.getString("ZipCode") : "";
        String businessId = args != null ? args.getString("BusinessId") : "";

        // Find your TextViews and set the values
        TextView tvCompanyName = root.findViewById(R.id.tvCompanyName);
        TextView tvCity = root.findViewById(R.id.tvCity);
        TextView tvCountry = root.findViewById(R.id.tvCountry);
        TextView tvWebsite = root.findViewById(R.id.tvWebsite);
        TextView tvEmail = root.findViewById(R.id.tvEmail);
        TextView tvZipCode = root.findViewById(R.id.tvZipCode);
        TextView tvBusinessId = root.findViewById(R.id.tvBusinessId);

        tvCompanyName.setText(companyName);
        tvCity.setText(city);
        tvCountry.setText(country);
        tvWebsite.setText(website);
        tvEmail.setText(email);
        tvZipCode.setText(zipCode);
        tvBusinessId.setText(businessId);

        btBack = root.findViewById(R.id.btBack);
        btBack.setVisibility(View.GONE);

        fetchSessionDataAndInitialize();

        return root;
    }

    private void fetchSessionDataAndInitialize() {
        new Handler().postDelayed(() -> {
            fm.beginTransaction().replace(R.id.Container, new HomeFragment()).commitAllowingStateLoss();
        }, 3000);
    }

}
