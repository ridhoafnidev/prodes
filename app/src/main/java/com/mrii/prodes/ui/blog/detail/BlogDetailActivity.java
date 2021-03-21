package com.mrii.prodes.ui.blog.detail;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import com.mrii.prodes.Config;
import com.mrii.prodes.R;
import com.mrii.prodes.databinding.ActivityBlogDetailBinding;
import com.mrii.prodes.ui.common.PSAppCompactActivity;
import com.mrii.prodes.utils.Constants;
import com.mrii.prodes.utils.MyContextWrapper;
import com.mrii.prodes.utils.Utils;

public class BlogDetailActivity extends PSAppCompactActivity {

    String blogId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityBlogDetailBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_blog_detail);

        initUI(binding);

    }

    @Override
    protected void attachBaseContext(Context newBase) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(newBase);
        String CURRENT_LANG_CODE = preferences.getString(Constants.LANGUAGE_CODE, Config.DEFAULT_LANGUAGE);
        String CURRENT_LANG_COUNTRY_CODE = preferences.getString(Constants.LANGUAGE_COUNTRY_CODE, Config.DEFAULT_LANGUAGE_COUNTRY_CODE);

        super.attachBaseContext(MyContextWrapper.wrap(newBase, CURRENT_LANG_CODE, CURRENT_LANG_COUNTRY_CODE, true));
    }

    private void initUI(ActivityBlogDetailBinding binding) {

        blogId = this.getIntent().getStringExtra(Constants.BLOG_ID);

        // Toolbar
        initToolbarBackHome(binding.toolbar, getResources().getString(R.string.blog_detail__title));

        // setup Fragment
        setupFragment(new BlogDetailFragment());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_share, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else {
            try {
                String uri = "http://www.ngebugcode.com/prodes/pronews/"+blogId;
                shareImageUri(uri);
            } catch (Exception e) {
                Utils.psErrorLog("Error ItemFragment 288 shared", e);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void shareImageUri(String uri) {

        new Thread(() -> {
            try {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, uri);
                intent.setType("text/plain");
                Intent shareIntent = Intent.createChooser(intent, null);

                if (intent.resolveActivity(this.getPackageManager()) != null) {
                    startActivity(shareIntent);
                }

                // startActivity(shareIntent);

                // Objects.requireNonNull(getContext()).startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }


}
