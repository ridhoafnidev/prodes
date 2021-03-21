package com.mrii.prodes.ui.blog.list;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mrii.prodes.Config;
import com.mrii.prodes.MainActivity;
import com.mrii.prodes.R;
import com.mrii.prodes.binding.FragmentDataBindingComponent;
import com.mrii.prodes.databinding.FragmentBlogListBinding;
import com.mrii.prodes.databinding.FragmentBlogListMainBinding;
import com.mrii.prodes.ui.blog.list.adapter.BlogListAdapter;
import com.mrii.prodes.ui.blog.list.adapter.InfoCovidListAdapter;
import com.mrii.prodes.ui.common.DataBoundListAdapter;
import com.mrii.prodes.ui.common.PSFragment;
import com.mrii.prodes.utils.AutoClearedValue;
import com.mrii.prodes.utils.Constants;
import com.mrii.prodes.utils.Utils;
import com.mrii.prodes.viewmodel.blog.BlogViewModel;
import com.mrii.prodes.viewmodel.infocovid.InfoCovidViewModel;
import com.mrii.prodes.viewobject.Blog;
import com.mrii.prodes.viewobject.InfoCovid;
import com.mrii.prodes.viewobject.common.Status;

import java.util.List;

public class BlogListMainFragment extends PSFragment implements DataBoundListAdapter.DiffUtilDispatchedInterface {

    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    private InfoCovidViewModel infoCovidViewModel;

    private BlogViewModel blogViewModel;

    @VisibleForTesting
    private AutoClearedValue<FragmentBlogListMainBinding> binding;
    private AutoClearedValue<BlogListAdapter> adapter;

    private AutoClearedValue<InfoCovidListAdapter> adapterCovid;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        FragmentBlogListMainBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_blog_list_main, container, false, dataBindingComponent);

        binding = new AutoClearedValue<>(this, dataBinding);

        return binding.get().getRoot();

    }

    @Override
    protected void initUIAndActions() {

        if (getActivity() instanceof MainActivity) {
            Toolbar toolbar = (Toolbar) binding.get().toolbar;
            //((MainActivity)getActivity()).setSupportActionBar(toolbar);
            ((MainActivity)getActivity()).setToolbarText(toolbar, getString(R.string.blog_list__title));
            setHasOptionsMenu(true);
            ((MainActivity) getActivity()).updateToolbarIconColor(Color.WHITE);
            ((MainActivity) getActivity()).updateMenuIconWhite();
            ((MainActivity) getActivity()).refreshPSCount();
        }

        binding.get().infoCovidViewAllTextView.setOnClickListener(v -> navigationController.navigateToAllInfoCovidActivity(getActivity()));

        binding.get().proNewsViewAllTextView.setOnClickListener(v -> navigationController.navigateToBlogList(getActivity()));

        binding.get().blogListRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager layoutManager = (LinearLayoutManager)
                        recyclerView.getLayoutManager();

                if (layoutManager != null) {

                    int lastPosition = layoutManager
                            .findLastVisibleItemPosition();

                    if (lastPosition == adapter.get().getItemCount() - 1) {

                        if (!binding.get().getLoadingMore() && !blogViewModel.forceEndLoading) {

                            blogViewModel.loadingDirection = Utils.LoadingDirection.bottom;

                            int limit = Config.LIST_NEW_FEED_COUNT;

                            blogViewModel.offset = blogViewModel.offset + limit;

                            blogViewModel.setLoadingState(true);

                            blogViewModel.setNextPageNewsFeedObj(String.valueOf(Config.LIST_NEW_FEED_COUNT), String.valueOf(blogViewModel.offset));
                        }
                    }
                }
            }
        });


        binding.get().swipeRefresh.setColorSchemeColors(getResources().getColor(R.color.view__primary_line));
        binding.get().swipeRefresh.setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.global__primary));
        binding.get().swipeRefresh.setOnRefreshListener(() -> {

            blogViewModel.loadingDirection = Utils.LoadingDirection.top;

            // reset productViewModel.offset

            blogViewModel.offset = 0;

            // reset productViewModel.forceEndLoading
            blogViewModel.forceEndLoading = false;

            blogViewModel.setNewsFeedObj(String.valueOf(Config.LIST_NEW_FEED_COUNT), String.valueOf(blogViewModel.offset));

            // update live data

        });

    }

    @Override
    protected void initViewModels() {
        infoCovidViewModel = new ViewModelProvider(this, viewModelFactory).get(InfoCovidViewModel.class);
        blogViewModel = new ViewModelProvider(this, viewModelFactory).get(BlogViewModel.class);
    }

    @Override
    protected void initAdapters() {

        // Blog
        BlogListAdapter nvAdapter = new BlogListAdapter(dataBindingComponent, newsFeed -> navigationController.navigateToBlogDetailActivity(BlogListMainFragment.this.getActivity(), newsFeed.id), this);

        this.adapter = new AutoClearedValue<>(this, nvAdapter);
        binding.get().blogListRecyclerView.setAdapter(adapter.get());

        // Info Covid
        InfoCovidListAdapter nvInfoCovidAdapter = new InfoCovidListAdapter(dataBindingComponent, newsInfoCovid -> navigationController.navigateToInfoCovidDetailActivity(BlogListMainFragment.this.getActivity(), newsInfoCovid.id), this);

        this.adapterCovid = new AutoClearedValue<>(this, nvInfoCovidAdapter);
        binding.get().infoCovidRecyclerView.setAdapter(adapterCovid.get());

    }

    @Override
    protected void initData() {

        // Blog
        infoCovidViewModel.setNewsInfoCovidObj(String.valueOf(Config.LIST_NEW_FEED_COUNT), String.valueOf(infoCovidViewModel.offset));

        infoCovidViewModel.getNewsInfoCovidData().observe(this, result -> {

            if (result != null) {
                switch (result.status) {
                    case SUCCESS:
                        replaceNewsInfoCovidList(result.data);
                        break;

                    case LOADING:
                        replaceNewsInfoCovidList(result.data);
                        break;

                    case ERROR:
                        infoCovidViewModel.setLoadingState(false);
                        break;
                }
            }

        });

        // Blog
        blogViewModel.setNewsFeedObj(String.valueOf(Config.LIST_NEW_FEED_COUNT), String.valueOf(blogViewModel.offset));

        blogViewModel.getNewsFeedData().observe(this, result -> {

            if (result != null) {
                switch (result.status) {
                    case SUCCESS:
                        replaceNewsFeedList(result.data);
                        blogViewModel.setLoadingState(false);
                        break;

                    case LOADING:
                        replaceNewsFeedList(result.data);
                        break;

                    case ERROR:

                        blogViewModel.setLoadingState(false);
                        break;
                }
            }

        });

        blogViewModel.getNextPageNewsFeedData().observe(this, state -> {
            if (state != null) {
                if (state.status == Status.ERROR) {

                    blogViewModel.setLoadingState(false);
                    blogViewModel.forceEndLoading = true;
                }
            }
        });


        blogViewModel.getLoadingState().observe(this, loadingState -> {

            binding.get().setLoadingMore(blogViewModel.isLoading);

            if (loadingState != null && !loadingState) {
                binding.get().swipeRefresh.setRefreshing(false);
            }

        });

    }

    private void replaceNewsFeedList(List<Blog> blogs) {
        this.adapter.get().replace(blogs);
        binding.get().executePendingBindings();
    }

    // Info Covid
    private void replaceNewsInfoCovidList(List<InfoCovid> infoCovids) {
        this.adapterCovid.get().replace(infoCovids);
        binding.get().executePendingBindings();
    }


    @Override
    public void onDispatched() {
        if (blogViewModel.loadingDirection == Utils.LoadingDirection.top) {

            if (binding.get().blogListRecyclerView != null) {

                LinearLayoutManager layoutManager = (LinearLayoutManager)
                        binding.get().blogListRecyclerView.getLayoutManager();

                if (layoutManager != null) {
                    layoutManager.scrollToPosition(0);
                }
            }
        }
    }
}
