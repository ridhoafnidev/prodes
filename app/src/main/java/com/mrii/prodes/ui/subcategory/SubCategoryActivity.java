package com.mrii.prodes.ui.subcategory;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.mrii.prodes.Config;
import com.mrii.prodes.R;
import com.mrii.prodes.databinding.ActivitySubCategoryBinding;
import com.mrii.prodes.ui.common.PSAppCompactActivity;
import com.mrii.prodes.utils.Constants;
import com.mrii.prodes.utils.MyContextWrapper;
import com.mrii.prodes.utils.Utils;

public class SubCategoryActivity extends PSAppCompactActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivitySubCategoryBinding activityFilteringBinding = DataBindingUtil.setContentView(this, R.layout.activity_sub_category);

        initUI(activityFilteringBinding);

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
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void initUI(ActivitySubCategoryBinding binding) {

        initToolbarBackHome(binding.toolbar, getIntent().getStringExtra(Constants.CATEGORY_NAME));

        setupFragment(new SubCategoryFragment());

    }

}
