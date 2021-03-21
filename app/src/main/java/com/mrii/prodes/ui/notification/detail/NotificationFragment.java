package com.mrii.prodes.ui.notification.detail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.ads.AdRequest;
import com.mrii.prodes.Config;
import com.mrii.prodes.R;
import com.mrii.prodes.binding.FragmentDataBindingComponent;
import com.mrii.prodes.databinding.FragmentNotificationBinding;
import com.mrii.prodes.ui.common.DataBoundListAdapter;
import com.mrii.prodes.ui.common.PSFragment;
import com.mrii.prodes.utils.AutoClearedValue;
import com.mrii.prodes.utils.Constants;
import com.mrii.prodes.utils.Utils;
import com.mrii.prodes.viewmodel.notification.NotificationsViewModel;
import com.mrii.prodes.viewobject.Noti;
import com.mrii.prodes.viewobject.common.Resource;
import com.mrii.prodes.viewobject.common.Status;


public class NotificationFragment extends PSFragment implements DataBoundListAdapter.DiffUtilDispatchedInterface {

    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);
    private NotificationsViewModel notificationViewModel;
    private String notiId;

    @VisibleForTesting
    private AutoClearedValue<FragmentNotificationBinding> binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentNotificationBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_notification, container, false, dataBindingComponent);
        binding = new AutoClearedValue<>(this, dataBinding);

        return binding.get().getRoot();
    }

    @Override
    public void onDispatched() {

    }

    @Override
    public void onDestroy() {
        navigationController.navigateBackFromNotiList(this.getActivity());
        super.onDestroy();
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

    }

    @Override
    protected void initViewModels() {
        notificationViewModel = new ViewModelProvider(this, viewModelFactory).get(NotificationsViewModel.class);
    }

    @Override
    protected void initAdapters() {

    }

    @Override
    protected void initData() {

        try {
            if (getActivity() != null) {
                if (getActivity().getIntent().getExtras() != null) {
                    String NOTI_ID_KEY = Constants.NOTI_ID;
                    notiId = getActivity().getIntent().getExtras().getString(NOTI_ID_KEY);
                }
            }
        } catch (Exception e) {
            Utils.psErrorLog("", e);
        }

//        try {
//            if (getActivity() != null) {
//                if (getActivity().getIntent().getExtras() != null) {
//                    String TOKEN_KEY = Constants.PAYMENT_TOKEN;
//                    notificationViewModel.token = getActivity().getIntent().getExtras().getString(TOKEN_KEY);
//                }
//            }
//        } catch (Exception e) {
//            Utils.psErrorLog("", e);
//        }
        notificationViewModel.token = pref.getString(Constants.NOTI_TOKEN, Constants.USER_NO_DEVICE_TOKEN);
        notificationViewModel.setNotificationDetailObj(notiId);
        LiveData<Resource<Noti>> notificationDetail = notificationViewModel.getNotificationDetailData();

        if (notificationDetail != null) {
            notificationDetail.observe(this, listResource -> {
                if (listResource != null) {

                    switch (listResource.status) {
                        case LOADING:
                            // Loading State
                            // Data are from Local DB

                            if (listResource.data != null) {
                                //fadeIn Animation
                                fadeIn(binding.get().getRoot());

                                // Update the data
                                replaceNotificationDetailData(listResource.data);

                                if (listResource.data.isRead.equals(Constants.ZERO)) {
                                    sendNotiReadPostData();
                                }
                            }

                            break;

                        case SUCCESS:
                            // Success State
                            // Data are from Server

                            if (listResource.data != null) {
                                // Update the data
                                replaceNotificationDetailData(listResource.data);

                            }

                            notificationViewModel.setLoadingState(false);

                            break;

                        case ERROR:
                            // Error State

                            notificationViewModel.setLoadingState(false);

                            break;
                        default:
                            // Default

                            break;
                    }

                } else {

                    notificationViewModel.setLoadingState(false);
                    // Init Object or Empty Data
                    Utils.psLog("Empty Data");
                }
            });
        }

        //getter and setter notification post method

        notificationViewModel.getNotiReadData().observe(this, result -> {
            if (result != null) {
                if (result.status == Status.SUCCESS) {
                    if (NotificationFragment.this.getActivity() != null) {
                        Utils.psLog(result.status.toString());
                        notificationViewModel.setLoadingState(false);

                    }

                } else if (result.status == Status.ERROR) {
                    if (NotificationFragment.this.getActivity() != null) {
                        Utils.psLog(result.status.toString());
                        notificationViewModel.setLoadingState(false);
                    }
                }
            }
        });

    }

    //send favourite data
    private void sendNotiReadPostData() {


        // Don't call to server when both loginUserId and Token are blank.
        if (!(loginUserId.equals("") && notificationViewModel.token.trim().equals(""))) {
            notificationViewModel.setNotiReadObj(notiId, loginUserId, notificationViewModel.token);
        } else {
            Utils.psLog("No Call");
        }

    }


    private void replaceNotificationDetailData(Noti noti) {

        binding.get().setNotification(noti);

    }
}
