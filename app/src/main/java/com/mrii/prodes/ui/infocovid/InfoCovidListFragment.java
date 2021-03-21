package com.mrii.prodes.ui.infocovid;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.mrii.prodes.databinding.FragmentInfoCovidListBinding;
import com.mrii.prodes.ui.blog.list.adapter.BlogListAdapter;
import com.mrii.prodes.ui.blog.list.adapter.InfoCovidListAdapter;
import com.mrii.prodes.ui.common.DataBoundListAdapter;
import com.mrii.prodes.ui.common.PSFragment;
import com.mrii.prodes.utils.AutoClearedValue;
import com.mrii.prodes.utils.Constants;
import com.mrii.prodes.utils.Utils;
import com.mrii.prodes.viewmodel.infocovid.InfoCovidViewModel;
import com.mrii.prodes.viewobject.Blog;
import com.mrii.prodes.viewobject.InfoCovid;
import com.mrii.prodes.viewobject.common.Status;

import java.util.List;

public class InfoCovidListFragment extends PSFragment implements DataBoundListAdapter.DiffUtilDispatchedInterface {

    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    private InfoCovidViewModel infoCovidViewModel;

    @VisibleForTesting
    private AutoClearedValue<FragmentInfoCovidListBinding> binding;
    private AutoClearedValue<InfoCovidAllListAdapter> adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        FragmentInfoCovidListBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_info_covid_list, container, false, dataBindingComponent);

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

                        if (!binding.get().getLoadingMore() && !infoCovidViewModel.forceEndLoading) {

                            infoCovidViewModel.loadingDirection = Utils.LoadingDirection.bottom;

                            int limit = Config.LIST_NEW_FEED_COUNT;

                            infoCovidViewModel.offset = infoCovidViewModel.offset + limit;

                            infoCovidViewModel.setLoadingState(true);

                            infoCovidViewModel.setNextPageNewsInfoCovidObj(String.valueOf(Config.LIST_NEW_FEED_COUNT), String.valueOf(infoCovidViewModel.offset));
                        }
                    }
                }
            }
        });


        binding.get().swipeRefresh.setColorSchemeColors(getResources().getColor(R.color.view__primary_line));
        binding.get().swipeRefresh.setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.global__primary));
        binding.get().swipeRefresh.setOnRefreshListener(() -> {

            infoCovidViewModel.loadingDirection = Utils.LoadingDirection.top;

            // reset productViewModel.offset

            infoCovidViewModel.offset = 0;

            // reset productViewModel.forceEndLoading
            infoCovidViewModel.forceEndLoading = false;

            infoCovidViewModel.setNewsInfoCovidObj(String.valueOf(Config.LIST_NEW_FEED_COUNT), String.valueOf(infoCovidViewModel.offset));

            // update live data

        });

    }

    @Override
    protected void initViewModels() {
        infoCovidViewModel = new ViewModelProvider(this, viewModelFactory).get(InfoCovidViewModel.class);
    }

    @Override
    protected void initAdapters() {

        InfoCovidAllListAdapter nvAdapter = new InfoCovidAllListAdapter(dataBindingComponent, newsInfoCovid -> navigationController.navigateToInfoCovidDetailActivity(InfoCovidListFragment.this.getActivity(), newsInfoCovid.id), this);

        this.adapter = new AutoClearedValue<>(this, nvAdapter);
        binding.get().blogListRecyclerView.setAdapter(adapter.get());

    }

    @Override
    protected void initData() {

        infoCovidViewModel.setNewsInfoCovidObj(String.valueOf(Config.LIST_NEW_FEED_COUNT), String.valueOf(infoCovidViewModel.offset));

        infoCovidViewModel.getNewsInfoCovidData().observe(this, result -> {

            if (result != null) {
                switch (result.status) {
                    case SUCCESS:
                        replaceNewsInfoCovidList(result.data);
                        infoCovidViewModel.setLoadingState(false);
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

        infoCovidViewModel.getNextPageNewsInfoCovidData().observe(this, state -> {
            if (state != null) {
                if (state.status == Status.ERROR) {

                    infoCovidViewModel.setLoadingState(false);
                    infoCovidViewModel.forceEndLoading = true;
                }
            }
        });


        infoCovidViewModel.getLoadingState().observe(this, loadingState -> {

            binding.get().setLoadingMore(infoCovidViewModel.isLoading);

            if (loadingState != null && !loadingState) {
                binding.get().swipeRefresh.setRefreshing(false);
            }

        });

    }

    private void replaceNewsInfoCovidList(List<InfoCovid> infoCovids) {
        this.adapter.get().replace(infoCovids);
        binding.get().executePendingBindings();
    }


    @Override
    public void onDispatched() {
        if (infoCovidViewModel.loadingDirection == Utils.LoadingDirection.top) {

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
