package com.mrii.prodes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mrii.prodes.api.CombineApi;
import com.mrii.prodes.api.PSApiService;
import com.mrii.prodes.model.UmkmResponse;
import com.mrii.prodes.ui.user.ProfileEditActivity;
import com.mrii.prodes.ui.user.profilepenjual.ProfilePenjualEditActivity;
import com.mrii.prodes.utils.Constants;
import com.mrii.prodes.utils.Utils;
import com.mrii.prodes.viewobject.Image;

import javax.inject.Inject;

public class UserSelectedType extends AppCompatActivity {
    LinearLayout ll_pembeli,ll_penjual;
    PSApiService psApiService;
    String usertype,phone;
    String TAG = "pusing";
    Utils utils;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_selected_type);

        initToolbar();

        utils = new Utils();
        psApiService = CombineApi.getApiService();
        ll_penjual = (LinearLayout) findViewById(R.id.ll_penjual);
        ll_pembeli = (LinearLayout) findViewById(R.id.ll_pembeli);
        phone = getIntent().getStringExtra("USER_PHONE");
        ll_pembeli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usertype = "pembeli";
                Call<UmkmResponse> umkmResponseCall = psApiService.updateUserType(phone,usertype,"");
                umkmResponseCall.enqueue(new Callback<UmkmResponse>() {
                    @Override
                    public void onResponse(Call<UmkmResponse> call, Response<UmkmResponse> response) {
                        if (response.isSuccessful()){
                            if (response.body().getStatus()==200){

                                Intent intent = new Intent(getApplicationContext(), ProfileEditActivity.class);
                                intent.putExtra("PROFILE_PEMBELI", "profile_pembeli_pertama");
                                startActivity(intent);
                                finish();

                            }
                            else{
                                Toast.makeText(UserSelectedType.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            Toast.makeText(UserSelectedType.this, "Gagal", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<UmkmResponse> call, Throwable t) {
                        Toast.makeText(UserSelectedType.this, "Harap periksa jaringan anda", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        ll_penjual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoseller = new Intent(getApplicationContext(),UserCodeCheckActivity.class);
                gotoseller.putExtra("USER_PHONE",phone);
                startActivity(gotoseller);
            }
        });
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Jenis Pengguna");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}