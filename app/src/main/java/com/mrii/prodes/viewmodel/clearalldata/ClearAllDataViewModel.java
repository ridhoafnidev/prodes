package com.mrii.prodes.viewmodel.clearalldata;

import com.mrii.prodes.repository.clearpackage.ClearPackageRepository;
import com.mrii.prodes.utils.AbsentLiveData;
import com.mrii.prodes.viewmodel.common.PSViewModel;
import com.mrii.prodes.viewobject.common.Resource;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

public class ClearAllDataViewModel extends PSViewModel {

    private final LiveData<Resource<Boolean>> deleteAllDataData;
    private MutableLiveData<Boolean> deleteAllDataObj = new MutableLiveData<>();


    @Inject
    public ClearAllDataViewModel(ClearPackageRepository repository) {

        deleteAllDataData = Transformations.switchMap(deleteAllDataObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            return repository.clearAllTheData();
        });
    }

    public void setDeleteAllDataObj() {

        this.deleteAllDataObj.setValue(true);
    }

    public LiveData<Resource<Boolean>> getDeleteAllDataData() {
        return deleteAllDataData;
    }


}
