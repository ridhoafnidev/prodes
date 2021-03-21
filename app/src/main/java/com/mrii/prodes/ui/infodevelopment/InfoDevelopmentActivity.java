package com.mrii.prodes.ui.infodevelopment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.mrii.prodes.R;
import com.mrii.prodes.databinding.ActivityInfoDevelopmentBinding;
import com.mrii.prodes.ui.common.PSAppCompactActivity;

public class InfoDevelopmentActivity extends PSAppCompactActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityInfoDevelopmentBinding activityInfoDevBinding = DataBindingUtil.setContentView(this, R.layout.activity_info_development);
        initUI(activityInfoDevBinding);
    }

    private void initUI(ActivityInfoDevelopmentBinding binding) {
        initToolbarBackHome(binding.toolbar, "Info Pengembangan");
        setupFragment(new InfoDevelopmentFragment() );
    }
}
