package com.mrii.prodes.ui.item.loginUserItem;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.databinding.DataBindingUtil;

import com.mrii.prodes.Config;
import com.mrii.prodes.R;
import com.mrii.prodes.databinding.ActivityLoginUserItemListBinding;
import com.mrii.prodes.ui.common.PSAppCompactActivity;
import com.mrii.prodes.utils.Constants;
import com.mrii.prodes.utils.MyContextWrapper;

public class LoginUserItemListActivity extends PSAppCompactActivity {


    //region Override Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityLoginUserItemListBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_login_user_item_list);
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

    private void initUI(ActivityLoginUserItemListBinding binding) {

        if(getIntent() != null && getIntent().getExtras() != null) {
            String paidOrNot = getIntent().getExtras().getString(Constants.FLAGPAIDORNOT);
            if (paidOrNot.equals(Constants.FLAGPAID)) {
                // Toolbar
                initToolbarBackHome(binding.toolbar, getString(R.string.profile__paid_ad));

                // setup Fragment
                setupFragment(new LoginUserPaidItemFragment());
            } else if (paidOrNot.equals(Constants.FLAGNOPAID)) {
                // Toolbar
                initToolbarBackHome(binding.toolbar, getIntent().getExtras().getString(Constants.ITEM_LIST_TITLE));

                // setup Fragment
                setupFragment(new LoginUserItemFragment());
            }
        }

    }

}
