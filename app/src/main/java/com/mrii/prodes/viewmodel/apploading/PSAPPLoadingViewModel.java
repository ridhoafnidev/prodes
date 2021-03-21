package com.mrii.prodes.viewmodel.apploading;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.mrii.prodes.repository.apploading.AppLoadingRepository;
import com.mrii.prodes.utils.AbsentLiveData;
import com.mrii.prodes.utils.Utils;
import com.mrii.prodes.viewmodel.common.PSViewModel;
import com.mrii.prodes.viewobject.PSAppInfo;
import com.mrii.prodes.viewobject.common.Resource;

import javax.inject.Inject;

public class PSAPPLoadingViewModel extends PSViewModel {

    private final LiveData<Resource<PSAppInfo>> deleteHistoryData;
    private MutableLiveData<TmpDataHolder> deleteHistoryObj = new MutableLiveData<>();
    public PSAppInfo psAppInfo;
    public String stripePublishableKey;
    public String currencyShortForm;
    public String appSettingLat;
    public String appSettingLng;

    @Inject
    PSAPPLoadingViewModel(AppLoadingRepository repository) {
        deleteHistoryData = Transformations.switchMap(deleteHistoryObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            Utils.psLog("PSAppInfoViewModel");
            return repository.deleteTheSpecificObjects(obj.startDate, obj.endDate);
        });
    }

    public void setDeleteHistoryObj(String startDate, String endDate) {

        TmpDataHolder tmpDataHolder = new TmpDataHolder(startDate, endDate);

        this.deleteHistoryObj.setValue(tmpDataHolder);
    }

    public LiveData<Resource<PSAppInfo>> getDeleteHistoryData() {
        return deleteHistoryData;
    }

    class TmpDataHolder {
        String startDate, endDate;

        private TmpDataHolder(String startDate, String endDate) {
            this.startDate = startDate;
            this.endDate = endDate;
        }
    }

}

