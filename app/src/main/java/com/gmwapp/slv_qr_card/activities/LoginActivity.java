package com.gmwapp.slv_qr_card.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.gmwapp.slv_qr_card.R;
import com.gmwapp.slv_qr_card.helper.ApiConfig;
import com.gmwapp.slv_qr_card.helper.Constant;
import com.gmwapp.slv_qr_card.helper.Session;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.zoho.commons.InitConfig;
import com.zoho.livechat.android.listeners.InitListener;
import com.zoho.salesiqembed.ZohoSalesIQ;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    TextView btnSignUp, btnForgotPassword;
    EditText EtPhoneNumber,EtPassword;
    MaterialButton btnLogin;
    Session session;
    Activity activity;
    String Mobile,Password;
    ImageView imgMenu;
    LinearLayout whatsppjoin;
    FloatingActionButton btnFloatingAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        activity = LoginActivity.this;
        session = new Session(activity);

        btnLogin = findViewById(R.id.btnLogin);
        EtPhoneNumber = findViewById(R.id.EtPhoneNumber);
        EtPassword = findViewById(R.id.EtPassword);
        btnSignUp = findViewById(R.id.btnSignUp);
        btnForgotPassword = findViewById(R.id.btnForgotPassword);
        imgMenu=findViewById(R.id.imgMenu);
        whatsppjoin = findViewById(R.id.whatsppjoin);
        btnFloatingAction = findViewById(R.id.btnFloatingAction);

        imgMenu.setOnClickListener(view -> showpopup(view));
        whatsppjoin.setOnClickListener(view -> openWhatsApp());
        btnSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
        });
        btnForgotPassword.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, ForgoPasswordActivity.class);
            startActivity(intent);
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(EtPhoneNumber.getText().toString().trim().equals("") ){
                    Toast.makeText(LoginActivity.this, "Phone Number is empty", Toast.LENGTH_SHORT).show();
                }
                else if (EtPassword.getText().toString().trim().equals("")){
                    Toast.makeText(LoginActivity.this, "Password is empty", Toast.LENGTH_SHORT).show();
                }
                else{


                    Login();
                }
            }
        });

        initializeZohoSalesIQ();

        btnFloatingAction.setOnClickListener(v -> ZohoSalesIQ.Chat.show());

    }

    private void initializeZohoSalesIQ() {
        InitConfig initConfig = new InitConfig();
        ZohoSalesIQ.init(
                getApplication(),
                Constant.ZOHO_API_KEY,
                Constant.ZOHO_ACCESS_KEY,
                initConfig,
                new InitListener() {
                    @Override
                    public void onInitSuccess() {
                        // Initialization successful
                        Log.d("ZohoSalesIQ", "Initialization successful");
                    }

                    @Override
                    public void onInitError(int errorCode, String errorMessage) {
                        // Handle initialization errors
                        Log.e("ZohoSalesIQ", "Initialization failed: " + errorMessage);
                    }
                }
        );
    }
    private void openWhatsApp() {
        String url = Constant.WHATSAPP_URL;
//        String url = "https://api.whatsapp.com/send?phone="+"91"+session.getData(Constant.WHATSAPP);
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }
    private void Login() {
        Mobile = EtPhoneNumber.getText().toString().trim();
        Password = EtPassword.getText().toString().trim();

        Map<String, String> params = new HashMap<>();
        Constant Constant = null;
        params.put(com.gmwapp.slv_qr_card.helper.Constant.MOBILE, Mobile);
        params.put(com.gmwapp.slv_qr_card.helper.Constant.PASSWORD, Password);
        params.put(com.gmwapp.slv_qr_card.helper.Constant.DEVICE_ID, com.gmwapp.slv_qr_card.helper.Constant.getDeviceId(activity));

        ApiConfig.RequestToVolley((result, response) -> {
            if (result) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean(com.gmwapp.slv_qr_card.helper.Constant.SUCCESS)) {
                        String codegenerate = "0", withdrawal_status = "0";
                        if (jsonObject.getBoolean(com.gmwapp.slv_qr_card.helper.Constant.USER_VERIFY)) {

                            JSONArray userArray = jsonObject.getJSONArray(com.gmwapp.slv_qr_card.helper.Constant.DATA);
                            JSONArray setArray = jsonObject.getJSONArray(com.gmwapp.slv_qr_card.helper.Constant.SETTINGS);

                            JSONObject settings = setArray.getJSONObject(0);
//                                session.setData(com.gmwapp.slv_qr_card.helper.Constant.SYNC_TIME, settings.getString(com.gmwapp.slv_qr_card.helper.Constant.SYNC_TIME));

                            if (settings.getString(com.gmwapp.slv_qr_card.helper.Constant.CODE_GENERATE).equals("1")) {
                                codegenerate = userArray.getJSONObject(0).getString(com.gmwapp.slv_qr_card.helper.Constant.CODE_GENERATE);
                            }
                            if (settings.getString(com.gmwapp.slv_qr_card.helper.Constant.WITHDRAWAL_STATUS).equals("1")) {
                                withdrawal_status = userArray.getJSONObject(0).getString(com.gmwapp.slv_qr_card.helper.Constant.WITHDRAWAL_STATUS);
                            }

                            Toast.makeText(this, jsonObject.getString(com.gmwapp.slv_qr_card.helper.Constant.MESSAGE), Toast.LENGTH_SHORT).show();

                            JSONObject userData = userArray.getJSONObject(0);
                            session.setData(com.gmwapp.slv_qr_card.helper.Constant.USER_ID, userData.getString(com.gmwapp.slv_qr_card.helper.Constant.ID));

                            if (session.getBoolean(com.gmwapp.slv_qr_card.helper.Constant.IMPORT_DATA)) {
                                session.setBoolean("is_logged_in", true);
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                            } else {
                                startActivity(new Intent(LoginActivity.this, ImportDataActivity.class));
                                finish();
                            }

//                            if (jsonObject.getBoolean(com.gmwapp.slv_qr_card.helper.Constant.DEVICE_VERIFY)) {
//                                String codegenerate = "0", withdrawal_status = "0";
//                                JSONArray userArray = jsonObject.getJSONArray(com.gmwapp.slv_qr_card.helper.Constant.DATA);
//                                JSONArray setArray = jsonObject.getJSONArray(com.gmwapp.slv_qr_card.helper.Constant.SETTINGS);
//
//                                JSONObject settings = setArray.getJSONObject(0);
////                                session.setData(com.gmwapp.slv_qr_card.helper.Constant.SYNC_TIME, settings.getString(com.gmwapp.slv_qr_card.helper.Constant.SYNC_TIME));
//
//                                if (settings.getString(com.gmwapp.slv_qr_card.helper.Constant.CODE_GENERATE).equals("1")) {
//                                    codegenerate = userArray.getJSONObject(0).getString(com.gmwapp.slv_qr_card.helper.Constant.CODE_GENERATE);
//                                }
//                                if (settings.getString(com.gmwapp.slv_qr_card.helper.Constant.WITHDRAWAL_STATUS).equals("1")) {
//                                    withdrawal_status = userArray.getJSONObject(0).getString(com.gmwapp.slv_qr_card.helper.Constant.WITHDRAWAL_STATUS);
//                                }
//
//                                Toast.makeText(this, jsonObject.getString(com.gmwapp.slv_qr_card.helper.Constant.MESSAGE), Toast.LENGTH_SHORT).show();
//
//                                JSONObject userData = userArray.getJSONObject(0);
//                                session.setData(com.gmwapp.slv_qr_card.helper.Constant.USER_ID, userData.getString(com.gmwapp.slv_qr_card.helper.Constant.ID));
//
//                                if (session.getBoolean(com.gmwapp.slv_qr_card.helper.Constant.IMPORT_DATA)) {
//                                    session.setBoolean("is_logged_in", true);
//                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
//                                    finish();
//                                } else {
//                                    startActivity(new Intent(LoginActivity.this, ImportDataActivity.class));
//                                    finish();
//                                }
//                            } else {
//                                showAlertdialog();
//                            }
                        } else {
                            ApprovalAlertdialog(jsonObject.getString(com.gmwapp.slv_qr_card.helper.Constant.MESSAGE));
                        }
                    } else {
                        Toast.makeText(this, jsonObject.getString(com.gmwapp.slv_qr_card.helper.Constant.MESSAGE), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(activity, "Error catch " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, response + result, Toast.LENGTH_SHORT).show();
            }
        }, LoginActivity.this, com.gmwapp.slv_qr_card.helper.Constant.LOGIN_URL, params, true);

        Log.d("LOGIN_URL", "LOGIN_URL: " + com.gmwapp.slv_qr_card.helper.Constant.LOGIN_URL);
        Log.d("LOGIN_URL", "LOGIN_URLparams: " + params);
    }



    private void clearFields() {
        EtPhoneNumber.getText().clear();
        EtPassword.getText().clear();
    }
    private void showpopup(View v) {

        PopupMenu popup = new PopupMenu(activity,v);
      popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.login_popup);
        popup.show();
    }
    private void showAlertdialog() {


        // Create the object of AlertDialog Builder class
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage("Would you request to change device ?");
        builder.setTitle("Device verification failed !");
        builder.setCancelable(false);
        builder.setPositiveButton("Send request to admin", (dialog, which) -> {
            changeDeviceApi(dialog);
        });
        builder.setNegativeButton("No", (dialog, which) -> {
            // If user click no then dialog box is canceled.
            dialog.cancel();
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();



    }

    private void changeDeviceApi(DialogInterface dialog)
    {
        Map<String, String> params = new HashMap<>();
        params.put(Constant.MOBILE,Mobile);
        params.put(Constant.PASSWORD,Password);
        params.put(Constant.DEVICE_ID,Constant.getDeviceId(activity));
        ApiConfig.RequestToVolley((result, response) -> {
            if (result) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
                        Toast.makeText(this, jsonObject.getString(Constant.MESSAGE), Toast.LENGTH_SHORT).show();
                        dialog.cancel();

                    }
                    else {
                        Toast.makeText(this, jsonObject.getString(Constant.MESSAGE), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
            else {
                Toast.makeText(this, String.valueOf(response) + result, Toast.LENGTH_SHORT).show();

            }
            //pass url
        }, activity, Constant.CHANGE_DEVICE_LIST_URL, params,true);

        Log.d("CHANGE_DEVICE_LIST_URL", "CHANGE_DEVICE_LIST_URL: " + Constant.CHANGE_DEVICE_LIST_URL);
        Log.d("CHANGE_DEVICE_LIST_URL", "CHANGE_DEVICE_LIST_URLparams: " + params);

    }

    private void ApprovalAlertdialog(String msg) {


        // Create the object of AlertDialog Builder class
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(msg);
        builder.setTitle("Blocked.");
        builder.setCancelable(false);
        builder.setPositiveButton("ok", (dialog, which) -> {
            dialog.cancel();
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();



    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.payment){
            try {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse(session.getData(Constant.PAYMENT_LINK)));
                startActivity(intent);
            }catch (Exception e){

            }
        }

        return false;
    }
}