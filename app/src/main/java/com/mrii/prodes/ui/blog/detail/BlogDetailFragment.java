package com.mrii.prodes.ui.blog.detail;

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
import com.mrii.prodes.ui.common.PSFragment;
import com.mrii.prodes.utils.AutoClearedValue;
import com.mrii.prodes.utils.Constants;
import com.mrii.prodes.utils.PSDialogMsg;
import com.mrii.prodes.utils.Utils;
import com.mrii.prodes.viewmodel.blog.BlogViewModel;

public class BlogDetailFragment extends PSFragment {

    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);
    private BlogViewModel blogViewModel;
    private String blogId;
    private PSDialogMsg psDialogMsg;

    @VisibleForTesting
    private AutoClearedValue<FragmentBlogDetailBinding> binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        FragmentBlogDetailBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_blog_detail, container, false, dataBindingComponent);

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

        blogViewModel = new ViewModelProvider(this, viewModelFactory).get(BlogViewModel.class);

    }

    @Override
    protected void initAdapters() {

    }

    @Override
    protected void initData() {

        if (getActivity() != null) {
            blogId = getActivity().getIntent().getStringExtra(Constants.BLOG_ID);
        }

        if (blogId != null && !blogId.isEmpty()) {
            blogViewModel.setBlogByIdObj(blogId);

            blogViewModel.getBlogByIdData().observe(this, result -> {

                if (result != null) {
                    if (result.data != null) {
                        switch (result.status) {
                            case SUCCESS:
                                binding.get().setBlog(result.data);

                                String blogDesc = result.data.description;
                                String blogAdd = result.data.addedDate;
                                binding.get().descriptionTextView.setText(Html.fromHtml(blogDesc));
                                binding.get().dateTextView.setText(Utils.setDateTime(blogAdd));
                                blogViewModel.setLoadingState(false);
                                break;

                            case ERROR:
                                blogViewModel.setLoadingState(false);
                                psDialogMsg.showErrorDialog(getString(R.string.blog_detail__error_message), getString(R.string.app__ok));
                                psDialogMsg.show();
                                break;

                            case LOADING:
                                binding.get().setBlog(result.data);
                                break;
                        }
                    }
                }
            });
        }

    }
}
