package com.mrii.prodes.ui.item.searchlistCategory;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;

import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.mrii.prodes.Config;
import com.mrii.prodes.R;
import com.mrii.prodes.databinding.ActivitySettingBinding;
import com.mrii.prodes.ui.common.PSAppCompactActivity;
import com.mrii.prodes.utils.Constants;
import com.mrii.prodes.utils.MyContextWrapper;
import com.mrii.prodes.utils.Utils;

public class SearchListCategoryActivity extends PSAppCompactActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivitySettingBinding activityFilteringBinding = DataBindingUtil.setContentView(this, R.layout.activity_setting);

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

    private void initUI(ActivitySettingBinding binding) {

        String title = getIntent().getStringExtra(Constants.ITEM_NAME);

        if (title != null) {
            initViewToolbar(binding.toolbar, title);
        } else {
            initViewToolbar(binding.toolbar, getString(R.string.item_list_title));
        }

        setupFragment(new SearchListCategoryFragment());

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item != null) {
            Utils.psLog("Clicked " + item.getItemId());
            switch (item.getItemId()) {
                case android.R.id.home:
                    navigationController.navigateToSubCategoryActivity(this, "cat445639833db3eff8b6cdb5510aa39faa", "Wisata");
                    // mapFilterButtonClicked(mapFilterClicked);
                    finish();
                    return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }


    private void initViewToolbar(Toolbar toolbar, String title) {
        if(toolbar != null) {

            toolbar.setTitle(title);

            try {
                toolbar.setTitleTextColor(getResources().getColor(R.color.text__white));
            }catch (Exception e){
                Utils.psErrorLog("Can't set color.", e);
            }

            try {
                setSupportActionBar(toolbar);
            }catch (Exception e) {
                Utils.psErrorLog("Error in set support action bar.", e);
            }

            try {
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setDisplayShowHomeEnabled(true);
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                }
            }catch (Exception e) {
                Utils.psErrorLog("Error in set display home as up enabled.", e);
            }

            if(!title.equals("")) {
                setToolbarText(toolbar, title);
            }

        }else {
            Utils.psLog("Toolbar is null");
        }
    }

}
