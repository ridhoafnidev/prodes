package com.mrii.prodes.viewmodel.paypal;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import com.mrii.prodes.repository.paypal.PaypalRepository;
import com.mrii.prodes.utils.AbsentLiveData;
import com.mrii.prodes.utils.Utils;
import com.mrii.prodes.viewmodel.common.PSViewModel;
import com.mrii.prodes.viewobject.common.Resource;

import javax.inject.Inject;

public class PaypalViewModel extends PSViewModel {

    private final LiveData<Resource<Boolean>> paypalTokenData;
    private MutableLiveData<Boolean> paypalTokenObj = new MutableLiveData<>();


    @Inject
    PaypalViewModel(PaypalRepository repository) {
        paypalTokenData = Transformations.switchMap(paypalTokenObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            Utils.psLog("paypalTokenData");
            return repository.getPaypalToken();
        });
    }

    public void setPaypalTokenObj() {
        this.paypalTokenObj.setValue(true);
    }

    public LiveData<Resource<Boolean>> getPaypalTokenData() {
        return paypalTokenData;
    }

}
