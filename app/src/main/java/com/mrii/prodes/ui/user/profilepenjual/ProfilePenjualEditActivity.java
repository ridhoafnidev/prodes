package com.mrii.prodes.ui.user.profilepenjual;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.mrii.prodes.Config;
import com.mrii.prodes.MainActivity;
import com.mrii.prodes.R;
import com.mrii.prodes.databinding.ActivityProfileEditBinding;
import com.mrii.prodes.databinding.ActivityProfilePenjualEditBinding;
import com.mrii.prodes.ui.common.PSAppCompactActivity;
import com.mrii.prodes.ui.user.ProfileEditFragment;
import com.mrii.prodes.utils.Constants;
import com.mrii.prodes.utils.MyContextWrapper;

public class ProfilePenjualEditActivity extends PSAppCompactActivity {


    //region Override Methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityProfilePenjualEditBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_profile_penjual_edit);

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

    private void initUI(ActivityProfilePenjualEditBinding binding) {

        String profileUserType = this.getIntent().getStringExtra("PROFILE_PENJUAL");

        if(profileUserType.equals("profile_penjual_kedua") ){
            // Toolbar
            initToolbarBackHome(binding.toolbar, getResources().getString(R.string.edit_profile__title));
        }else{
            // Toolbar
            initToolbar(binding.toolbar, getResources().getString(R.string.edit_profile__title));
        }

        // setup Fragment
        setupFragment(new ProfilePenjualEditFragment());
        // Or you can call like this
        //setupFragment(new NewsListFragment(), R.id.content_frame);

    }

    //endregion


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("FromProfilePenjualEditFragment", "2");
        startActivity(intent);
    }
}