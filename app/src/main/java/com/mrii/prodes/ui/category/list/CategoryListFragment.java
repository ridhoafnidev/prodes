package com.mrii.prodes.ui.category.list;

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
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.mrii.prodes.Config;
import com.mrii.prodes.MainActivity;
import com.mrii.prodes.R;
import com.mrii.prodes.binding.FragmentDataBindingComponent;
import com.mrii.prodes.databinding.FragmentCategoryListBinding;
import com.mrii.prodes.ui.category.adapter.CategoryAdapter;
import com.mrii.prodes.ui.city.selectedcity.SelectedCityFragment;
import com.mrii.prodes.ui.common.DataBoundListAdapter;
import com.mrii.prodes.ui.common.PSFragment;
import com.mrii.prodes.utils.AutoClearedValue;
import com.mrii.prodes.utils.Constants;
import com.mrii.prodes.utils.Utils;
import com.mrii.prodes.viewmodel.item.TouchCountViewModel;
import com.mrii.prodes.viewmodel.itemcategory.ItemCategoryViewModel;
import com.mrii.prodes.viewobject.ItemCategory;
import com.mrii.prodes.viewobject.common.Resource;
import com.mrii.prodes.viewobject.common.Status;
import com.mrii.prodes.viewobject.holder.ItemParameterHolder;

import java.util.List;

public class CategoryListFragment extends PSFragment implements DataBoundListAdapter.DiffUtilDispatchedInterface {

    //region Variables

    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    private ItemCategoryViewModel itemCategoryViewModel;
    private TouchCountViewModel touchCountViewModel;
    private ItemParameterHolder itemParameterHolder = new ItemParameterHolder();

    @VisibleForTesting
    private AutoClearedValue<FragmentCategoryListBinding> binding;
    private AutoClearedValue<CategoryAdapter> adapter;

    //endregion
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentCategoryListBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_category_list, container, false, dataBindingComponent);

        binding = new AutoClearedValue<>(this, dataBinding);

        binding.get().setLoadingMore(connectivity.isConnected());

        return binding.get().getRoot();
    }

    @Override
    protected void initUIAndActions() {
        if (getActivity() instanceof MainActivity) {
            //((MainActivity) getActivity()).setToolbarText(((MainActivity) getActivity()).binding.toolbar, getString(R.string.category__list_title));
            //getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
            //((MainActivity) this.getActivity()).binding.toolbar.setBackgroundColor(getResources().getColor(R.color.global__primary));
            //((MainActivity) getActivity()).updateToolbarIconColor(Color.WHITE);
            //((MainActivity) getActivity()).updateMenuIconWhite();
            //((MainActivity) getActivity()).refreshPSCount();
        }

        if (Config.SHOW_ADMOB && connectivity.isConnected()) {
            AdRequest adRequest = new AdRequest.Builder()
                    .build();
            binding.get().adView.loadAd(adRequest);
        } else {
            binding.get().adView.setVisibility(View.GONE);
        }

        binding.get().idKriya.setOnClickListener(v -> {
            itemParameterHolder.cat_id = "cat5dfc0138547b926dccc5ee269b1cd042";
            itemParameterHolder.isPaid = Constants.PAIDITEMFIRST;
            itemParameterHolder.location_id = selected_location_id;
            itemParameterHolder.lat = selectedLat;
            itemParameterHolder.lng = selectedLng;

            navigationController.navigateToHomeFilteringActivity(CategoryListFragment.this.getActivity(), itemParameterHolder, "Kriya", selectedCityLat, selectedCityLng, Constants.MAP_MILES);

        });

        binding.get().idPronews.setOnClickListener(v -> {
            navigationController.navigateToBlogList(getActivity());
        });

        binding.get().idPromart.setOnClickListener(v -> {
            navigationController.navigateToInfoDevelopmentActivity(getActivity(),  "Promart");
        });

        binding.get().idProbisa.setOnClickListener(v -> {
            navigationController.navigateToInfoDevelopmentActivity(getActivity(),  "Probisa");
        });

        binding.get().idProraga.setOnClickListener(v -> {
            navigationController.navigateToInfoDevelopmentActivity(getActivity(),  "Proraga");
        });

        binding.get().idProjasa.setOnClickListener(v -> {
            navigationController.navigateToInfoDevelopmentActivity(getActivity(),  "Projasa");
        });

        binding.get().idProevent.setOnClickListener(v -> {
            navigationController.navigateToInfoDevelopmentActivity(getActivity(),  "Proevent");
        });

        binding.get().idProjob.setOnClickListener(v -> {
            navigationController.navigateToInfoDevelopmentActivity(getActivity(),  "Prokerja");
        });

        binding.get().idKopiDesa.setOnClickListener(v -> {
            navigationController.navigateToInfoDevelopmentActivity(getActivity(),  "Kopi Desa");
        });

        binding.get().idRuteDesa.setOnClickListener(v -> {
            navigationController.navigateToInfoDevelopmentActivity(getActivity(),  "Rute Desa");
        });

        binding.get().idKaplingDesa.setOnClickListener(v -> {
            navigationController.navigateToInfoDevelopmentActivity(getActivity(),  "Kapling Desa");
        });

        binding.get().idRondaDesa.setOnClickListener(v -> {
            navigationController.navigateToInfoDevelopmentActivity(getActivity(),  "Ronda Desa");
        });

        binding.get().idKopiDesa.setOnClickListener(v -> {
            navigationController.navigateToInfoDevelopmentActivity(getActivity(),  "Kopi Desa");
        });

        binding.get().idKaplingDesa.setOnClickListener(v -> {
            navigationController.navigateToInfoDevelopmentActivity(getActivity(),  "Kapling Desa");
        });

        binding.get().idBalaiDesa.setOnClickListener(v -> {
            navigationController.navigateToInfoDevelopmentActivity(getActivity(),  "Balai Desa");
        });

        binding.get().idModalDesa.setOnClickListener(v -> {
            navigationController.navigateToInfoDevelopmentActivity(getActivity(),  "Modal Desa");
        });

        binding.get().idWisata.setOnClickListener(v -> {
            navigationController.navigateToSubCategoryActivity(getActivity(), "cat445639833db3eff8b6cdb5510aa39faa", "Wisata");
        });

        binding.get().idKuliner.setOnClickListener(v -> {
            itemParameterHolder.cat_id = "catfa070dd5cc2a2c9c6196159f85480ff7";
            itemParameterHolder.isPaid = Constants.PAIDITEMFIRST;
            itemParameterHolder.location_id = selected_location_id;
            itemParameterHolder.lat = selectedLat;
            itemParameterHolder.lng = selectedLng;

            navigationController.navigateToHomeFilteringActivity(CategoryListFragment.this.getActivity(), itemParameterHolder, "Kuliner", selectedCityLat, selectedCityLng, Constants.MAP_MILES);

        });

//        binding.get().categoryList.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                GridLayoutManager layoutManager = (GridLayoutManager)
//                        recyclerView.getLayoutManager();
//
//                if (layoutManager != null) {
//                    int lastPosition = layoutManager
//                            .findLastVisibleItemPosition();
//                    if (lastPosition == adapter.get().getItemCount() - 1) {
//
//                        if (!binding.get().getLoadingMore() && !itemCategoryViewModel.forceEndLoading) {
//
//                            if (connectivity.isConnected()) {
//
//                                itemCategoryViewModel.loadingDirection = Utils.LoadingDirection.bottom;
//
//                                int limit = Config.LIST_CATEGORY_COUNT;
//                                itemCategoryViewModel.offset = itemCategoryViewModel.offset + limit;
//
//                                itemCategoryViewModel.setNextPageLoadingStateObj(String.valueOf(Config.LIST_CATEGORY_COUNT),
//                                        String.valueOf(itemCategoryViewModel.offset));//itemCategoryViewModel.categoryParameterHolder.cityId);
//                            }
//                        }
//                    }
//                }
//            }
//        });

//        binding.get().swipeRefresh.setColorSchemeColors(getResources().getColor(R.color.view__primary_line));
//        binding.get().swipeRefresh.setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.global__primary));
//        binding.get().swipeRefresh.setOnRefreshListener(() -> {
//
//            itemCategoryViewModel.loadingDirection = Utils.LoadingDirection.top;
//
//            // reset itemCategoryViewModel.offset
//            itemCategoryViewModel.offset = 0;
//
//            // reset itemCategoryViewModel.forceEndLoading
//            itemCategoryViewModel.forceEndLoading = false;
//
//            // update live data
//            itemCategoryViewModel.setCategoryListObj(String.valueOf(Config.LIST_CATEGORY_COUNT), String.valueOf(itemCategoryViewModel.offset));
//
//        });
    }

    @Override
    protected void initViewModels() {
//        itemCategoryViewModel = new ViewModelProvider(this, viewModelFactory).get(ItemCategoryViewModel.class);
//        touchCountViewModel = new ViewModelProvider(this, viewModelFactory).get(TouchCountViewModel.class);
    }

    @Override
    protected void initAdapters() {
//        CategoryAdapter nvAdapter = new CategoryAdapter(dataBindingComponent,
//                category -> navigationController.navigateToSubCategoryActivity(CategoryListFragment.this.getActivity(), category.id, category.name), this);
//
//        this.adapter = new AutoClearedValue<>(this, nvAdapter);
//        binding.get().categoryList.setAdapter(nvAdapter);
    }

    @Override
    protected void initData() {
        //loadCategory();
    }

    //region Private Methods

    private void loadCategory() {

        // Load Category List

//        itemCategoryViewModel.categoryParameterHolder.cityId = selectedCityId;
//
//        itemCategoryViewModel.setCategoryListObj(String.valueOf(Config.LIST_CATEGORY_COUNT), String.valueOf(itemCategoryViewModel.offset));
//
//        LiveData<Resource<List<ItemCategory>>> news = itemCategoryViewModel.getCategoryListData();
//
//        if (news != null) {
//
//            news.observe(this, listResource -> {
//                if (listResource != null) {
//
//                    switch (listResource.status) {
//                        case LOADING:
//                            // Loading State
//                            // Data are from Local DB
//
//                            if (listResource.data != null) {
//                                //fadeIn Animation
//                                fadeIn(binding.get().getRoot());
//
//                                // Update the data
//                                replaceData(listResource.data);
//
//                            }
//
//                            break;
//
//                        case SUCCESS:
//                            // Success State
//                            // Data are from Server
//
//                            if (listResource.data != null) {
//                                // Update the data
//                                replaceData(listResource.data);
//                            }
//
//                            itemCategoryViewModel.setLoadingState(false);
//                            break;
//
//                        case ERROR:
//                            // Error State
//                            itemCategoryViewModel.setLoadingState(false);
//                            break;
//                        default:
//                            // Default
//
//                            break;
//                    }
//
//                } else {
//
//                    // Init Object or Empty Data
//
//                    if (itemCategoryViewModel.offset > 1) {
//                        // No more data for this list
//                        // So, Block all future loading
//                        itemCategoryViewModel.forceEndLoading = true;
//                    }
//
//                }
//
//            });
//        }

//        itemCategoryViewModel.getNextPageLoadingStateData().observe(this, state -> {
//            if (state != null) {
//                if (state.status == Status.ERROR) {
//
//                    itemCategoryViewModel.setLoadingState(false);
//                    itemCategoryViewModel.forceEndLoading = true;
//                }
//            }
//        });
//
//        itemCategoryViewModel.getLoadingState().observe(this, loadingState -> {
//
//            binding.get().setLoadingMore(itemCategoryViewModel.isLoading);
//
//            if (loadingState != null && !loadingState) {
//                binding.get().swipeRefresh.setRefreshing(false);
//            }
//
//        });
//
//        //get touch count post method
//        touchCountViewModel.getTouchCountPostData().observe(this, result -> {
//            if (result != null) {
//                if (result.status == Status.SUCCESS) {
//                    if (CategoryListFragment.this.getActivity() != null) {
//                        Utils.psLog(result.status.toString());
//                    }
//
//                } else if (result.status == Status.ERROR) {
//                    if (CategoryListFragment.this.getActivity() != null) {
//                        Utils.psLog(result.status.toString());
//                    }
//                }
//            }
//        });

    }

//    private void replaceData(List<ItemCategory> categoryList) {
//        adapter.get().replace(categoryList);
//        binding.get().executePendingBindings();
//    }

    @Override
    public void onDispatched() {
//        if (itemCategoryViewModel.loadingDirection == Utils.LoadingDirection.top) {
//
//            if (binding.get().categoryList != null) {
//
//                LinearLayoutManager layoutManager = (LinearLayoutManager)
//                        binding.get().categoryList.getLayoutManager();
//
//                if (layoutManager != null) {
//                    layoutManager.scrollToPosition(0);
//                }
//            }
//        }
    }
}
