package com.mrii.prodes.ui.user.userlist.detail;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.databinding.DataBindingUtil;

import com.mrii.prodes.Config;
import com.mrii.prodes.R;
import com.mrii.prodes.databinding.ActivityUserDetailBinding;
import com.mrii.prodes.ui.common.PSAppCompactActivity;
import com.mrii.prodes.utils.Constants;
import com.mrii.prodes.utils.MyContextWrapper;

public class UserDetailActivity extends PSAppCompactActivity {

    //region Override Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityUserDetailBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_user_detail);
        
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

    //endregion


    //region Private Methods

    private void initUI(ActivityUserDetailBinding binding) {

        // Toolbar
        initToolbarBackHome(binding.toolbar, getIntent().getStringExtra(Constants.OTHER_USER_NAME));

        // setup Fragment
        setupFragment(new UserDetailFragment());

    }

}

