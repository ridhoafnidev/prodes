package com.mrii.prodes.ui.banner;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdRequest;
import com.mrii.prodes.Config;
import com.mrii.prodes.R;
import com.mrii.prodes.binding.FragmentDataBindingComponent;
import com.mrii.prodes.databinding.FragmentBannerDetailBinding;
import com.mrii.prodes.ui.common.PSFragment;
import com.mrii.prodes.utils.AutoClearedValue;
import com.mrii.prodes.utils.Constants;
import com.mrii.prodes.utils.PSDialogMsg;
import com.mrii.prodes.viewmodel.BannerViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class BannerDetailFragment extends PSFragment {

    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);
    private BannerViewModel bannerViewModel;
    private String bannerId;
    private PSDialogMsg psDialogMsg;

    @VisibleForTesting
    private AutoClearedValue<FragmentBannerDetailBinding> binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        FragmentBannerDetailBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_banner_detail, container, false, dataBindingComponent);

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

        psDialogMsg = new PSDialogMsg(getActivity(), false);


    }

    @Override
    protected void initViewModels() {

        bannerViewModel = new ViewModelProvider(this, viewModelFactory).get(BannerViewModel.class);

    }

    @Override
    protected void initAdapters() {

    }

    @Override
    protected void initData() {

        if (getActivity() != null) {
            bannerId = getActivity().getIntent().getStringExtra(Constants.BANNER_ID);
        }

        if (bannerId != null && !bannerId.isEmpty()) {
            bannerViewModel.setBannerByIdObj(bannerId);

            bannerViewModel.getBannerByIdData().observe(this, result -> {

                if (result != null) {
                    if (result.data != null) {
                        switch (result.status) {
                            case SUCCESS:
                                binding.get().setBanner(result.data);

                                String bannerDesc = result.data.description;
                                binding.get().descriptionTextView.setText(Html.fromHtml(bannerDesc));
                                bannerViewModel.setLoadingState(false);
                                break;

                            case ERROR:
                                bannerViewModel.setLoadingState(false);
                                psDialogMsg.showErrorDialog(getString(R.string.banner_detail__error_message), getString(R.string.app__ok));
                                psDialogMsg.show();
                                break;

                            case LOADING:
                                binding.get().setBanner(result.data);
                                break;
                        }
                    }
                }
            });
        }

    }
}
