package com.mrii.prodes.ui.blog.list;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mrii.prodes.Config;
import com.mrii.prodes.MainActivity;
import com.mrii.prodes.R;
import com.mrii.prodes.binding.FragmentDataBindingComponent;
import com.mrii.prodes.databinding.FragmentBlogListBinding;
import com.mrii.prodes.ui.blog.list.adapter.BlogListAdapter;
import com.mrii.prodes.ui.common.DataBoundListAdapter;
import com.mrii.prodes.ui.common.PSFragment;
import com.mrii.prodes.utils.AutoClearedValue;
import com.mrii.prodes.utils.Utils;
import com.mrii.prodes.viewmodel.blog.BlogViewModel;
import com.mrii.prodes.viewobject.Blog;
import com.mrii.prodes.viewobject.common.Status;

import java.util.List;

public class BlogListFragment extends PSFragment implements DataBoundListAdapter.DiffUtilDispatchedInterface {

    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    private BlogViewModel blogViewModel;

    @VisibleForTesting
    private AutoClearedValue<FragmentBlogListBinding> binding;
    private AutoClearedValue<BlogListAdapter> adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        FragmentBlogListBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_blog_list, container, false, dataBindingComponent);

        binding = new AutoClearedValue<>(this, dataBinding);

        return binding.get().getRoot();

    }

    @Override
    protected void initUIAndActions() {

        if (getActivity() instanceof MainActivity) {
           // ((MainActivity) getActivity()).setToolbarText(((MainActivity) getActivity()).binding.toolbar, getString(R.string.blog_list__title));
           // getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
           // ((MainActivity) this.getActivity()).binding.toolbar.setBackgroundColor(getResources().getColor(R.color.global__primary));
           // ((MainActivity) getActivity()).updateToolbarIconColor(Color.WHITE);
           // ((MainActivity) getActivity()).updateMenuIconWhite();
           // ((MainActivity) getActivity()).refreshPSCount();
        }

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
        blogViewModel = new ViewModelProvider(this, viewModelFactory).get(BlogViewModel.class);
    }

    @Override
    protected void initAdapters() {

        BlogListAdapter nvAdapter = new BlogListAdapter(dataBindingComponent, newsFeed -> navigationController.navigateToBlogDetailActivity(BlogListFragment.this.getActivity(), newsFeed.id), this);

        this.adapter = new AutoClearedValue<>(this, nvAdapter);
        binding.get().blogListRecyclerView.setAdapter(adapter.get());

    }

    @Override
    protected void initData() {

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
