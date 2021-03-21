package com.mrii.prodes.ui.gallery;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.gms.ads.AdRequest;
import com.mrii.prodes.Config;
import com.mrii.prodes.R;
import com.mrii.prodes.binding.FragmentDataBindingComponent;
import com.mrii.prodes.databinding.FragmentGalleryBinding;
import com.mrii.prodes.ui.common.DataBoundListAdapter;
import com.mrii.prodes.ui.common.PSFragment;
import com.mrii.prodes.ui.gallery.adapter.GalleryAdapter;
import com.mrii.prodes.utils.AutoClearedValue;
import com.mrii.prodes.utils.Constants;
import com.mrii.prodes.utils.Utils;
import com.mrii.prodes.viewmodel.image.ImageViewModel;
import com.mrii.prodes.viewobject.Image;
import com.mrii.prodes.viewobject.common.Resource;

import java.util.List;

/**
 * GalleryFragment
 */
public class GalleryFragment extends PSFragment implements DataBoundListAdapter.DiffUtilDispatchedInterface {


    //region Variables

    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);
    private ImageViewModel imageViewModel;

    @VisibleForTesting
    private AutoClearedValue<FragmentGalleryBinding> binding;
    private AutoClearedValue<GalleryAdapter> adapter;

    //private String imgParentId = "";

    //endregion


    //region Override Methods

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        FragmentGalleryBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_gallery, container, false, dataBindingComponent);

        binding = new AutoClearedValue<>(this, dataBinding);

        return binding.get().getRoot();
    }

    @Override
    protected void initUIAndActions() {

        if (Config.SHOW_ADMOB && connectivity.isConnected()) {
            AdRequest adRequest = new AdRequest.Builder()
                    .build();
            binding.get().adView.loadAd(adRequest);
        } else {
            binding.get().adView.setVisibility(View.GONE);
        }

        binding.get().newsImageList.setHasFixedSize(true);
        binding.get().newsImageList.setNestedScrollingEnabled(false);
        StaggeredGridLayoutManager mLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        binding.get().newsImageList.setLayoutManager(mLayoutManager);
    }

    @Override
    protected void initViewModels() {

        imageViewModel = new ViewModelProvider(this, viewModelFactory).get(ImageViewModel.class);

    }

    private void imageClicked(Image image) {
        navigationController.navigateToDetailGalleryActivity(getActivity(), imageViewModel.imgType, imageViewModel.id, image.imgId);
    }

    @Override
    protected void initAdapters() {
        // ViewModel need to get from ViewModelProviders
        GalleryAdapter nvAdapter = new GalleryAdapter(dataBindingComponent, this::imageClicked, this, getActivity());
        this.adapter = new AutoClearedValue<>(this, nvAdapter);
        binding.get().newsImageList.setAdapter(adapter.get());
    }

    @Override
    public void onDispatched() {

    }

    @Override
    protected void initData() {
        //load category

        try {
            if(getActivity() != null) {
                imageViewModel.imgType = getActivity().getIntent().getStringExtra(Constants.IMAGE_TYPE);
            }
        }catch (Exception e){
            Utils.psErrorLog("Error in getting intent.", e);
        }

        try {
            if(getActivity() != null) {
                imageViewModel.id = getActivity().getIntent().getStringExtra(Constants.IMAGE_PARENT_ID);
            }
        }catch (Exception e){
            Utils.psErrorLog("Error in getting intent.", e);
        }

        LiveData<Resource<List<Image>>> imageListLiveData = imageViewModel.getImageListLiveData();
        imageViewModel.setImageParentId(imageViewModel.imgType, imageViewModel.id);
        imageListLiveData.observe(this, listResource -> {
            // we don't need any null checks here for the adapter since LiveData guarantees that
            // it won't call us if fragment is stopped or not started.
            if (listResource != null && listResource.data != null) {
                Utils.psLog("Got Data");

                //fadeIn Animation
                fadeIn(binding.get().getRoot());

                // Update the data
                this.adapter.get().replace(listResource.data);
                this.binding.get().executePendingBindings();

            } else {
                //noinspection Constant Conditions
                Utils.psLog("Empty Data");
            }
        });

    }

    //endregion


}
