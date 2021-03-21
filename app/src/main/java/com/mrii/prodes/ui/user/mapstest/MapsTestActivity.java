package com.mrii.prodes.ui.user.mapstest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.mrii.prodes.R;
import com.mrii.prodes.databinding.ActivityItemEntryBinding;
import com.mrii.prodes.databinding.ActivityMapsTestBinding;
import com.mrii.prodes.ui.common.PSAppCompactActivity;
import com.mrii.prodes.ui.item.entry.ItemEntryFragment;

public class MapsTestActivity extends PSAppCompactActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_test);

        ActivityMapsTestBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_maps_test);

        // Init all UI
        initUI(binding);
    }

    private void initUI(ActivityMapsTestBinding binding) {

        // Toolbar
        setToolbarText(binding.toolbar, getString(R.string.item_entry_toolbar));

        // setup Fragment
        setupFragment(new MapsTestFragment());
        // Or you can call like this
        //setupFragment(new NewsListFragment(), R.id.content_frame);

    }
}
