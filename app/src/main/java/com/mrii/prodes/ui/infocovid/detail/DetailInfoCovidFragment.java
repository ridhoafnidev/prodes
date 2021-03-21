package com.mrii.prodes.ui.infocovid.detail;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.ads.AdRequest;
import com.mrii.prodes.Config;
import com.mrii.prodes.R;
import com.mrii.prodes.binding.FragmentDataBindingComponent;
import com.mrii.prodes.databinding.FragmentBlogDetailBinding;
import com.mrii.prodes.databinding.FragmentInfoCovidDetailBinding;
import com.mrii.prodes.ui.common.PSFragment;
import com.mrii.prodes.utils.AutoClearedValue;
import com.mrii.prodes.utils.Constants;
import com.mrii.prodes.utils.PSDialogMsg;
import com.mrii.prodes.utils.Utils;
import com.mrii.prodes.viewmodel.blog.BlogViewModel;
import com.mrii.prodes.viewmodel.infocovid.InfoCovidViewModel;

public class DetailInfoCovidFragment extends PSFragment {

    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);
    private InfoCovidViewModel infoCovidViewModel;
    private String infoCovidId;
    private PSDialogMsg psDialogMsg;

    @VisibleForTesting
    private AutoClearedValue<FragmentInfoCovidDetailBinding> binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        FragmentInfoCovidDetailBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_info_covid_detail, container, false, dataBindingComponent);

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

        infoCovidViewModel = new ViewModelProvider(this, viewModelFactory).get(InfoCovidViewModel.class);

    }

    @Override
    protected void initAdapters() {

    }

    @Override
    protected void initData() {

        if (getActivity() != null) {
            infoCovidId = getActivity().getIntent().getStringExtra(Constants.INFO_COVID_ID);
        }

        if (infoCovidId != null && !infoCovidId.isEmpty()) {
            infoCovidViewModel.setInfoCovidByIdObj(infoCovidId);

            infoCovidViewModel.getInfoCovidByIdData().observe(this, result -> {

                if (result != null) {
                    if (result.data != null) {
                        switch (result.status) {
                            case SUCCESS:
                                binding.get().setInfoCovid(result.data);

                                String infoCovidDesc = result.data.description;
                                String infoCovidDate = result.data.addedDate;
                                binding.get().descriptionTextView.setText(Html.fromHtml(infoCovidDesc));
                                binding.get().dateTextView.setText(Utils.setDateTime(infoCovidDate));
                                infoCovidViewModel.setLoadingState(false);
                                break;

                            case ERROR:
                                infoCovidViewModel.setLoadingState(false);
                                psDialogMsg.showErrorDialog(getString(R.string.info_covid_detail__error_message), getString(R.string.app__ok));
                                psDialogMsg.show();
                                break;

                            case LOADING:
                                binding.get().setInfoCovid(result.data);
                                break;
                        }
                    }
                }
            });
        }

    }
}
