package com.mrii.prodes.ui.user;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.mrii.prodes.Config;
import com.mrii.prodes.R;
import com.mrii.prodes.databinding.ActivityUserFbRegisterBinding;
import com.mrii.prodes.ui.common.PSAppCompactActivity;
import com.mrii.prodes.utils.Constants;
import com.mrii.prodes.utils.MyContextWrapper;
import com.mrii.prodes.utils.Utils;

public class UserFBRegisterActivity extends PSAppCompactActivity {


    //region Override Methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityUserFbRegisterBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_user_fb_register);

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Utils.psLog("Inside Result UserFBRegisterActivity");
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if(fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }

    }

    //endregion


    //region Private Methods

    private void initUI(ActivityUserFbRegisterBinding binding) {

        // Toolbar
        initToolbar(binding.toolbar, getResources().getString(R.string.register__register));

        // setup Fragment
        setupFragment(new UserFBRegisterFragment());
        // Or you can call like this
        //setupFragment(new NewsListFragment(), R.id.content_frame);

    }

    //endregion


}