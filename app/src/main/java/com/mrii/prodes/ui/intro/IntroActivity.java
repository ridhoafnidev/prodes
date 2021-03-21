package com.mrii.prodes.ui.intro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.mrii.prodes.MainActivity;
import com.mrii.prodes.R;
import com.mrii.prodes.ui.common.NavigationController;
import com.mrii.prodes.ui.user.verifyumkm.VerifyUmkmActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

public class IntroActivity extends AppCompatActivity {
    private ViewPager screenPager;
    IntroViewPagerAdapter introViewPagerAdapter ;
    int position = 0 ;
    Button btn_daftar_sekarang;
    TabLayout tabIndicator;
    Animation btnAnim ;
    @Inject
    NavigationController navigationController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        //Membuat activity menjadi full screen
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // menyimpan data ke shared preference, hanya muncul saat pertama kali install
        if (restorePrefData()) {
            Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class );
            startActivity(mainActivity);
            finish();
        }

        // ini views
        tabIndicator = findViewById(R.id.tab_indicator);
        btn_daftar_sekarang = findViewById(R.id.btn_daftar_sekarang);
        btnAnim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.button_animation);
        btn_daftar_sekarang.setAnimation(btnAnim);

        // fill list screen
        final List<ScreenItem> mList = new ArrayList<>();
        mList.add(new ScreenItem("Selamat Datang di apps prodes","prodes adalah Pasar Rakyat Online - Desa. Sebagai platform etalase digital produk Desa Indonesia",R.drawable.gif_intro_two));
        mList.add(new ScreenItem("Jual Produk Unggulan Desa","Penjual adalah pelaku usaha Desa yang terverifikasi oleh aparatur atau pemerintahan Desa setempat",R.drawable.gif_intro_one));
        mList.add(new ScreenItem("Database Usaha Desa skala Mikro dan Kecil di Indonesia","Temukan database wisata, kuliner, kriya dan jasa Desa lainnya",R.drawable.gif_inro_three));

        // setup viewpager
        screenPager =findViewById(R.id.screen_viewpager);
        introViewPagerAdapter = new IntroViewPagerAdapter(this,mList);
        screenPager.setAdapter(introViewPagerAdapter);

        // setup tablayout with viewpager
        tabIndicator.setupWithViewPager(screenPager);

        // Get Started button click listener
        btn_daftar_sekarang.setOnClickListener(v -> {

            //open main activity
            savePrefsData();
            Intent mainActivity = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(mainActivity);
            finish();
        });

        // tablayout add change listener
        tabIndicator.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == mList.size()-1) {
                    loaddLastScreen();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

    }

    // show the GETSTARTED Button and hide the indicator and the next button
    private void loaddLastScreen() {
        btn_daftar_sekarang.setVisibility(View.VISIBLE);
        //tabIndicator.setVisibility(View.INVISIBLE);
        // setup animation
        btn_daftar_sekarang.setAnimation(btnAnim);

        btn_daftar_sekarang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePrefsData();
                //navigationController.navigateToMainActivity(MainActivity.this);
                Intent explicit = new Intent(IntroActivity.this, MainActivity.class);
                startActivity(explicit);
                finish();
            }
        });

    }

    private boolean restorePrefData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs",MODE_PRIVATE);
        return pref.getBoolean("isIntroOpnend",false);

    }

    private void savePrefsData(){
        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs",MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isIntroOpnend",true);
        editor.apply();
    }
}
