package com.mrii.prodes.ui.user.userlist;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.databinding.DataBindingUtil;

import com.mrii.prodes.Config;
import com.mrii.prodes.R;
import com.mrii.prodes.databinding.ActivityUserListBinding;
import com.mrii.prodes.ui.common.PSAppCompactActivity;
import com.mrii.prodes.utils.Constants;
import com.mrii.prodes.utils.MyContextWrapper;
import com.mrii.prodes.viewobject.holder.UserParameterHolder;

public class UserListActivity extends PSAppCompactActivity {


    UserParameterHolder userHolder = new UserParameterHolder();

    //region Override Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityUserListBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_user_list);
        
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

    private void initUI(ActivityUserListBinding binding) {

        userHolder = (UserParameterHolder) getIntent().getSerializableExtra(Constants.USER_PARAM_HOLDER_KEY);

        // Toolbar
        if(userHolder.return_types.equals("follower")) {
            initToolbarBackHome(binding.toolbar, getString(R.string.user_follower_list_toolbar_name));
        }else {
            initToolbarBackHome(binding.toolbar, getString(R.string.user_following_list_toolbar_name));
        }

        // setup Fragment
        setupFragment(new UserListFragment());

    }

}
