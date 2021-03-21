package com.mrii.prodes.ui.dashboard.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mrii.prodes.R;
import com.mrii.prodes.databinding.ItemDashboardViewpagerBlogBinding;
import com.mrii.prodes.viewobject.Banner;
import com.mrii.prodes.viewobject.Blog;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class DashBoardViewPagerAdapter extends PagerAdapter {

    public List<Banner> bannerList;

    public final androidx.databinding.DataBindingComponent dataBindingComponent;
    public DashBoardViewPagerAdapter.newsBannerClickCallBack callback;

    public DashBoardViewPagerAdapter(androidx.databinding.DataBindingComponent dataBindingComponent,
                              DashBoardViewPagerAdapter.newsBannerClickCallBack callback) {
        this.dataBindingComponent = dataBindingComponent;
        this.callback = callback;

    }

    @Override
    public int getCount() {
        if (bannerList == null) {
            return 0;
        } else {
            return bannerList.size();
        }
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        ItemDashboardViewpagerBlogBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(container.getContext()),
                        R.layout.item_dashboard_viewpager_blog, container, false,
                        dataBindingComponent);

        binding.setBanner(bannerList.get(position));

        binding.getRoot().setOnClickListener(v -> callback.onPagerClick(binding.getBanner()));

        container.addView(binding.getRoot());

        return binding.getRoot();
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);
    }

    public void replaceNewsBannerList(List<Banner> bannerList) {
        this.bannerList = bannerList;
        this.notifyDataSetChanged();
    }

    public interface newsBannerClickCallBack {
        void onPagerClick(Banner banner);
    }
}

