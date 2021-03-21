package com.mrii.prodes.ui.user.verifyumkm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.mrii.prodes.Config;
import com.mrii.prodes.R;
import com.mrii.prodes.databinding.ActivityVerifyMobileBinding;
import com.mrii.prodes.databinding.ActivityVerifyUmkmBinding;
import com.mrii.prodes.ui.common.PSAppCompactActivity;
import com.mrii.prodes.ui.user.verifyphone.VerifyMobileFragment;
import com.mrii.prodes.utils.Constants;
import com.mrii.prodes.utils.MyContextWrapper;

public class VerifyUmkmActivity extends PSAppCompactActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityVerifyUmkmBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_verify_umkm);

        // Init all UI
        initUI(binding);
    }

    @Override
    protected void attachBaseContext(Context newBase) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(newBase);
        String CURRENT_LANG_CODE = preferences.getString(Constants.LANGUAGE_CODE, Config.DEFAULT_LANGUAGE);
        String CURRENT_LANG_COUNTRY_CODE = preferences.getString(Constants.LANGUAGE_COUNTRY_CODE, Config.DEFAULT_LANGUAGE_COUNTRY_CODE);

        super.attachBaseContext(MyContextWrapper.wrap(newBase, CURRENT_LANG_CODE, CURRENT_LANG_COUNTRY_CODE, true));
    }

    private void initUI(ActivityVerifyUmkmBinding binding) {
        // Toolbar
        initToolbarBackHome(binding.toolbar, getResources().getString(R.string.verify_phone__title));

        // setup Fragment
        setupFragment(new VerifyUmkmFragment());
    }
}
