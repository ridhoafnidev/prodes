package com.mrii.prodes.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.mrii.prodes.Config;
import com.mrii.prodes.R;
import com.mrii.prodes.binding.FragmentDataBindingComponent;
import com.mrii.prodes.databinding.FragmentSearchCategoryBinding;
import com.mrii.prodes.ui.category.adapter.SearchCategoryAdapter;
import com.mrii.prodes.ui.common.DataBoundListAdapter;
import com.mrii.prodes.ui.common.PSFragment;
import com.mrii.prodes.utils.AutoClearedValue;
import com.mrii.prodes.utils.Constants;
import com.mrii.prodes.utils.Utils;
import com.mrii.prodes.viewmodel.itemcategory.ItemCategoryViewModel;
import com.mrii.prodes.viewobject.ItemCategory;
import com.mrii.prodes.viewobject.common.Resource;
import com.mrii.prodes.viewobject.common.Status;

import java.util.List;

public class DashBoardSearchCategoryFragment extends PSFragment implements DataBoundListAdapter.DiffUtilDispatchedInterface{

    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    private ItemCategoryViewModel itemCategoryViewModel;
    public String catId;

    @VisibleForTesting
    private AutoClearedValue<FragmentSearchCategoryBinding> binding;
    private AutoClearedValue<SearchCategoryAdapter> adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        FragmentSearchCategoryBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_category, container, false, dataBindingComponent);

        binding = new AutoClearedValue<>(this, dataBinding);

        setHasOptionsMenu(true);

        if (getActivity() != null) {
            Intent intent = getActivity().getIntent();
            this.catId = intent.getStringExtra(Constants.CATEGORY_ID);
        }

        if (Config.SHOW_ADMOB && connectivity.isConnected()) {
            AdRequest adRequest = new AdRequest.Builder()
                    .build();
            binding.get().adView.loadAd(adRequest);
        } else {
            binding.get().adView.setVisibility(View.GONE);
        }
        return binding.get().getRoot();
    }

    @Override
    protected void initUIAndActions() {
        binding.get().subCategoryRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager layoutManager = (LinearLayoutManager)
                        recyclerView.getLayoutManager();

                if (layoutManager != null) {

                    int lastPosition = layoutManager
                            .findLastVisibleItemPosition();

                    if (lastPosition == adapter.get().getItemCount() - 1) {

                        if (!binding.get().getLoadingMore() && !itemCategoryViewModel.forceEndLoading) {

                            itemCategoryViewModel.loadingDirection = Utils.LoadingDirection.bottom;

                            int limit = Config.LIST_SEARCH_CATEGORY_COUNT;

                            itemCategoryViewModel.offset = itemCategoryViewModel.offset + limit;

                            itemCategoryViewModel.setNextPageLoadingStateObj( String.valueOf(limit), String.valueOf(itemCategoryViewModel.offset));

                            itemCategoryViewModel.setLoadingState(true);
                        }
                    }
                }
            }
        });

        binding.get().swipeRefresh.setColorSchemeColors(getResources().getColor(R.color.view__primary_line));
        binding.get().swipeRefresh.setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.global__primary));
        binding.get().swipeRefresh.setOnRefreshListener(() -> {

            itemCategoryViewModel.loadingDirection = Utils.LoadingDirection.top;

            // reset productViewModel.offset
            itemCategoryViewModel.offset = 0;

            // reset productViewModel.forceEndLoading
            itemCategoryViewModel.forceEndLoading = false;

            // update live data
            itemCategoryViewModel.setCategoryListObj(String.valueOf(Config.LIST_SEARCH_CATEGORY_COUNT), String.valueOf(itemCategoryViewModel.offset));

        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {

        inflater.inflate(R.menu.clear_button, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.clear) {
            this.catId = "";

            initAdapters();

            initData();

            navigationController.navigateBackToSearchFragment(this.getActivity(), this.catId, "");
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void initViewModels() {

        itemCategoryViewModel = new ViewModelProvider(this, viewModelFactory).get(ItemCategoryViewModel.class);
    }

    @Override
    protected void initAdapters() {

        SearchCategoryAdapter nvadapter = new SearchCategoryAdapter(dataBindingComponent,
                (category, id) -> {

                    navigationController.navigateBackToSearchFragment(this.getActivity(), category.id, category.name);

                    if (getActivity() != null) {
                        this.getActivity().finish();
                    }
                }, this.catId);
        this.adapter = new AutoClearedValue<>(this, nvadapter);
        binding.get().subCategoryRecyclerView.setAdapter(nvadapter);

    }

    @Override
    protected void initData() {
        loadCategory();
    }

    private void loadCategory() {

        // Load Category List
        itemCategoryViewModel.categoryParameterHolder.cityId = selectedCityId;

        itemCategoryViewModel.setCategoryListObj("", String.valueOf(itemCategoryViewModel.offset));

        LiveData<Resource<List<ItemCategory>>> news = itemCategoryViewModel.getCategoryListData();

        if (news != null) {

            news.observe(this, listResource -> {
                if (listResource != null) {

                    Utils.psLog("Got Data" + listResource.message + listResource.toString());

                    switch (listResource.status) {
                        case LOADING:
                            // Loading State
                            // Data are from Local DB

                            if (listResource.data != null) {
                                //fadeIn Animation
                                fadeIn(binding.get().getRoot());

                                // Update the data
                                replaceData(listResource.data);

                            }

                            break;

                        case SUCCESS:
                            // Success State
                            // Data are from Server

                            if (listResource.data != null) {
                                // Update the data
                                replaceData(listResource.data);
                            }

                            itemCategoryViewModel.setLoadingState(false);

                            break;

                        case ERROR:
                            // Error State

                            itemCategoryViewModel.setLoadingState(false);

                            break;
                        default:
                            // Default

                            break;
                    }

                } else {

                    // Init Object or Empty Data
                    Utils.psLog("Empty Data");

                    if (itemCategoryViewModel.offset > 1) {
                        // No more data for this list
                        // So, Block all future loading
                        itemCategoryViewModel.forceEndLoading = true;
                    }

                }

            });
        }

        itemCategoryViewModel.getNextPageLoadingStateData().observe(this, state -> {
            if (state != null) {
                if (state.status == Status.ERROR) {
                    Utils.psLog("Next Page State : " + state.data);

                    itemCategoryViewModel.setLoadingState(false);
                    itemCategoryViewModel.forceEndLoading = true;
                }
            }
        });

        itemCategoryViewModel.getLoadingState().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean loadingState) {
                binding.get().setLoadingMore(itemCategoryViewModel.isLoading);

                if (loadingState != null && !loadingState) {
                    binding.get().swipeRefresh.setRefreshing(false);
                }
            }
        });

    }

    private void replaceData(List<ItemCategory> categoryList) {

        adapter.get().replace(categoryList);
        binding.get().executePendingBindings();

    }

    @Override
    public void onDispatched() {
        if (itemCategoryViewModel.loadingDirection == Utils.LoadingDirection.top) {
            binding.get();
            LinearLayoutManager layoutManager = (LinearLayoutManager)
                    binding.get().subCategoryRecyclerView.getLayoutManager();
            if (layoutManager != null) {
                layoutManager.scrollToPosition(0);
            }
        }
    }
}

