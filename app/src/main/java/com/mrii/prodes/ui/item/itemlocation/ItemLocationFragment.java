package com.mrii.prodes.ui.item.itemlocation;


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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mrii.prodes.Config;
import com.mrii.prodes.R;
import com.mrii.prodes.binding.FragmentDataBindingComponent;
import com.mrii.prodes.databinding.FragmentItemLocationBinding;
import com.mrii.prodes.ui.common.DataBoundListAdapter;
import com.mrii.prodes.ui.common.PSFragment;
import com.mrii.prodes.utils.AutoClearedValue;
import com.mrii.prodes.utils.Constants;
import com.mrii.prodes.utils.Utils;
import com.mrii.prodes.viewmodel.itemlocation.ItemLocationViewModel;
import com.mrii.prodes.viewobject.ItemLocation;
import com.mrii.prodes.viewobject.common.Resource;
import com.mrii.prodes.viewobject.common.Status;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ItemLocationFragment extends PSFragment implements DataBoundListAdapter.DiffUtilDispatchedInterface{

    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    private ItemLocationViewModel itemLocationViewModel;
    public String locationId;

    @VisibleForTesting
    private AutoClearedValue<FragmentItemLocationBinding> binding;
    private AutoClearedValue<ItemLocationAdapter> adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        FragmentItemLocationBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_item_location, container, false, dataBindingComponent);

        binding = new AutoClearedValue<>(this, dataBinding);

        if (getActivity() != null) {
            Intent intent = getActivity().getIntent();
            this.locationId = intent.getStringExtra(Constants.ITEM_LOCATION_TYPE_ID);

        }

        return binding.get().getRoot();
    }

    @Override
    protected void initUIAndActions() {

        if(getActivity() != null){
            if (getActivity().getIntent().getStringExtra(Constants.LOCATION_FLAG).equals(Constants.LOCATION_NOT_CLEAR_ICON) || getActivity().getIntent().getStringExtra(Constants.LOCATION_FLAG).equals(Constants.SELECT_LOCATION_FROM_HOME)) {
                setHasOptionsMenu(false);
                binding.get().selectTitleConstraintLayout.setVisibility(View.VISIBLE);
            } else {

                setHasOptionsMenu(true);
                binding.get().selectTitleConstraintLayout.setVisibility(View.GONE);

            }
        }

        binding.get().locationRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager layoutManager = (LinearLayoutManager)
                        recyclerView.getLayoutManager();

                if (layoutManager != null) {

                    int lastPosition = layoutManager
                            .findLastVisibleItemPosition();

                    if (lastPosition == adapter.get().getItemCount() - 1) {

                        if (!binding.get().getLoadingMore() && !itemLocationViewModel.forceEndLoading) {

                            itemLocationViewModel.loadingDirection = Utils.LoadingDirection.bottom;

                            int limit = Config.LIST_LOCATION_COUNT;

                            itemLocationViewModel.offset = itemLocationViewModel.offset + limit;

                            itemLocationViewModel.setNextPageLoadingStateObj( String.valueOf(limit), String.valueOf(itemLocationViewModel.offset));

                            itemLocationViewModel.setLoadingState(true);
                        }
                    }
                }
            }
        });

        binding.get().swipeRefresh.setColorSchemeColors(getResources().getColor(R.color.view__primary_line));
        binding.get().swipeRefresh.setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.global__primary));
        binding.get().swipeRefresh.setOnRefreshListener(() -> {

            itemLocationViewModel.loadingDirection = Utils.LoadingDirection.top;

            // reset productViewModel.offset
            itemLocationViewModel.offset = 0;

            // reset productViewModel.forceEndLoading
            itemLocationViewModel.forceEndLoading = false;

            // update live data
            itemLocationViewModel.setItemLocationListObj(String.valueOf(Config.LIST_LOCATION_COUNT), String.valueOf(itemLocationViewModel.offset));

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
            this.locationId = "";

            initAdapters();

            initData();

            if(this.getActivity() != null)
            navigationController.navigateBackToItemLocationFragment(this.getActivity(), this.locationId, "", "", "");
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void initViewModels() {

        itemLocationViewModel = new ViewModelProvider(this, viewModelFactory).get(ItemLocationViewModel.class);
    }

    @Override
    protected void initAdapters() {

        ItemLocationAdapter nvadapter = new ItemLocationAdapter(dataBindingComponent,
                (itemLocation, id) -> {

                    if (getActivity() != null) {

                        if (getActivity().getIntent().getStringExtra(Constants.LOCATION_FLAG).equals(Constants.LOCATION_NOT_CLEAR_ICON)) {
                            navigationController.navigateToMainActivity(ItemLocationFragment.this.getActivity(), itemLocation.id, itemLocation.name, itemLocation.lat, itemLocation.lng);

                        } else if (getActivity().getIntent().getStringExtra(Constants.LOCATION_FLAG).equals(Constants.SELECT_LOCATION_FROM_HOME)) {
                            navigationController.navigateBackToMainActivity(ItemLocationFragment.this.getActivity(), itemLocation.id, itemLocation.name, itemLocation.lat, itemLocation.lng);

                        } else {
                            navigationController.navigateBackToItemLocationFragment(ItemLocationFragment.this.getActivity(), itemLocation.id, itemLocation.name, itemLocation.lat, itemLocation.lng);
                        }

                        ItemLocationFragment.this.getActivity().finish();

                    }
                }, this.locationId);
        this.adapter = new AutoClearedValue<>(this, nvadapter);
        binding.get().locationRecyclerView.setAdapter(nvadapter);

    }

    @Override
    protected void initData() {
        loadCategory();
    }

    private void loadCategory() {

        // Load Category List
        itemLocationViewModel.categoryParameterHolder.cityId = selectedCityId;

        if (connectivity.isConnected()) {
            itemLocationViewModel.setItemLocationListObj("", String.valueOf(itemLocationViewModel.offset));
        }

        LiveData<Resource<List<ItemLocation>>> news = itemLocationViewModel.getItemLocationListData();

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

                            itemLocationViewModel.setLoadingState(false);

                            break;

                        case ERROR:
                            // Error State

                            itemLocationViewModel.setLoadingState(false);

                            break;
                        default:
                            // Default

                            break;
                    }

                } else {

                    // Init Object or Empty Data
                    Utils.psLog("Empty Data");

                    if (itemLocationViewModel.offset > 1) {
                        // No more data for this list
                        // So, Block all future loading
                        itemLocationViewModel.forceEndLoading = true;
                    }

                }

            });
        }

        itemLocationViewModel.getNextPageLoadingStateData().observe(this, state -> {
            if (state != null) {
                if (state.status == Status.ERROR) {
                    Utils.psLog("Next Page State : " + state.data);

                    itemLocationViewModel.setLoadingState(false);
                    itemLocationViewModel.forceEndLoading = true;
                }
            }
        });

        itemLocationViewModel.getLoadingState().observe(this, loadingState -> {
            binding.get().setLoadingMore(itemLocationViewModel.isLoading);

            if (loadingState != null && !loadingState) {
                binding.get().swipeRefresh.setRefreshing(false);
            }
        });

    }

    private void replaceData(List<ItemLocation> categoryList) {

        adapter.get().replace(categoryList);
        binding.get().executePendingBindings();

    }

    @Override
    public void onDispatched() {
        if (itemLocationViewModel.loadingDirection == Utils.LoadingDirection.top) {
            binding.get();
            LinearLayoutManager layoutManager = (LinearLayoutManager)
                    binding.get().locationRecyclerView.getLayoutManager();
            if (layoutManager != null) {
                layoutManager.scrollToPosition(0);
            }
        }
    }
}