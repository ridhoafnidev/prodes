package com.mrii.prodes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mrii.prodes.api.CombineApi;
import com.mrii.prodes.api.PSApiService;
import com.mrii.prodes.model.UmkmResponse;
import com.mrii.prodes.ui.dialog.DialogCodeFoundFragment;
import com.mrii.prodes.ui.user.ProfileEditActivity;
import com.mrii.prodes.ui.user.profilepenjual.ProfilePenjualEditActivity;

import org.w3c.dom.Text;

public class UserCodeCheckActivity extends AppCompatActivity {
    PSApiService psApiService;
    Button btncheck;
    EditText etcode;
    String usertype,phone,user;
    private ProgressBar progress_bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_code_check);
        initToolbar();
        psApiService = CombineApi.getApiService();
        btncheck = (Button)findViewById(R.id.btnCheck);
        etcode = (EditText)findViewById(R.id.etCode);
        progress_bar = (ProgressBar) findViewById(R.id.progress_bar);
        phone = getIntent().getStringExtra("USER_PHONE");
        btncheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progress_bar.setVisibility(View.VISIBLE);
                Call<UmkmResponse> umkmResponseCall = psApiService.checkCode(etcode.getText().toString());
                umkmResponseCall.enqueue(new Callback<UmkmResponse>() {
                    @Override
                    public void onResponse(Call<UmkmResponse> call, Response<UmkmResponse> response) {
                        if (response.isSuccessful()){
                            if (response.body().getStatus() == 200){

                              new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        showDialogPaymentSuccess(response, view);
                                        progress_bar.setVisibility(View.GONE);
                                        btncheck.setAlpha(1f);
                                        doUpdate();
                                    }
                                }, 1000);

                            }
                            else{
                                progress_bar.setVisibility(View.GONE);
                                Toast.makeText(UserCodeCheckActivity.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            progress_bar.setVisibility(View.GONE);
                            Toast.makeText(UserCodeCheckActivity.this, "Response Gagal", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<UmkmResponse> call, Throwable t) {
                        Toast.makeText(UserCodeCheckActivity.this, "Response Failure", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void showDialogPaymentSuccess(Response<UmkmResponse> response, View view) {
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        final View popupView = inflater.inflate(R.layout.detail_code, null);
        FloatingActionButton close = (FloatingActionButton)popupView.findViewById(R.id.fab_close);
        TextView umkm,pemilik,wa,alamat,kades,nokades,lokasi;
        final PopupWindow popupWindow = new PopupWindow(popupView);
        popupWindow.setWidth(width);
        popupWindow.setAnimationStyle(android.R.style.Animation_Translucent);
        popupWindow.setHeight(height);
        popupWindow.setFocusable(true);
        umkm = (TextView)popupView.findViewById(R.id.tvUmkm);
        lokasi =(TextView)popupView.findViewById(R.id.tvLokasi);
        kades = (TextView)popupView.findViewById(R.id.tvKades);
        nokades = (TextView)popupView.findViewById(R.id.tvNoKades);
        wa = (TextView)popupView.findViewById(R.id.tvWa);
        pemilik = (TextView)popupView.findViewById(R.id.tvPemilik);
        user = response.body().getData().getNamaPemilik();
        alamat = (TextView)popupView.findViewById(R.id.tvAlamat);
        lokasi.setText(response.body().getData().getProvinsi()+", "+
                response.body().getData().getKabupaten()+", "+
                response.body().getData().getKecamatan()+", "+
                response.body().getData().getKelurahan());
        kades.setText(""+response.body().getData().getNamaKades());
        nokades.setText(""+response.body().getData().getNoHpKades());
        alamat.setText(""+response.body().getData().getAlamat());
        umkm.setText(""+response.body().getData().getNamaUmkm());
        pemilik.setText(""+response.body().getData().getNamaPemilik());
        wa.setText(""+response.body().getData().getNomorWa());
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                Intent intent = new Intent(getApplicationContext(), ProfilePenjualEditActivity.class);
                intent.putExtra("PROFILE_PENJUAL", "profile_penjual_pertama");
                startActivity(intent);
            }
        });
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Cek Kode");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void doUpdate() {
        usertype = "penjual";
        Call<UmkmResponse> umkmResponseCall = psApiService.updateUserType(phone,usertype,user);
        umkmResponseCall.enqueue(new Callback<UmkmResponse>() {
            @Override
            public void onResponse(Call<UmkmResponse> call, Response<UmkmResponse> response) {
                if (response.isSuccessful()){
                    if (response.body().getStatus()==200){
//                        Intent i = new Intent(getApplicationContext(),MainActivity.class);
//                        startActivity(i);
//                        Intent intent = new Intent(getApplicationContext(), ProfileEditActivity.class);
//                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(getApplicationContext(), ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(), "Gagal", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UmkmResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Harap periksa jaringan anda", Toast.LENGTH_SHORT).show();
            }
        });
//        Intent i = new Intent(getApplicationContext(),MainActivity.class);
//        startActivity(i);
    }
}