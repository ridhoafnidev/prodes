package com.mrii.prodes.viewmodel.itemlocation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.mrii.prodes.repository.itemlocation.ItemLocationRepository;
import com.mrii.prodes.utils.AbsentLiveData;
import com.mrii.prodes.utils.Utils;
import com.mrii.prodes.viewmodel.common.PSViewModel;
import com.mrii.prodes.viewobject.ItemLocation;
import com.mrii.prodes.viewobject.common.Resource;
import com.mrii.prodes.viewobject.holder.CategoryParameterHolder;

import java.util.List;

import javax.inject.Inject;

public class ItemLocationViewModel extends PSViewModel {


    //region Variables

    private final LiveData<Resource<List<ItemLocation>>> itemTypeListData;
    private MutableLiveData<ItemLocationViewModel.TmpDataHolder> itemTypeListObj = new MutableLiveData<>();

    private final LiveData<Resource<Boolean>> nextPageLoadingStateData;
    private MutableLiveData<ItemLocationViewModel.TmpDataHolder> nextPageLoadingStateObj = new MutableLiveData<>();

    public CategoryParameterHolder categoryParameterHolder = new CategoryParameterHolder();

    //endregion

    //region Constructors

    @Inject
    ItemLocationViewModel(ItemLocationRepository repository) {

        Utils.psLog("ItemLocationViewModel");

        itemTypeListData = Transformations.switchMap(itemTypeListObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }

            Utils.psLog("ItemLocationViewModel : categories");
            return repository.getAllItemLocationList(obj.limit, obj.offset);
        });

        nextPageLoadingStateData = Transformations.switchMap(nextPageLoadingStateObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }

            Utils.psLog("Category List.");
            return repository.getNextPageLocationList(obj.limit, obj.offset);
        });

    }

    //endregion

    public void setItemLocationListObj(String limit, String offset) {
        if (!isLoading) {
            TmpDataHolder tmpDataHolder = new TmpDataHolder();
            tmpDataHolder.offset = offset;
            tmpDataHolder.limit = limit;
            itemTypeListObj.setValue(tmpDataHolder);

            // start loading
            setLoadingState(true);
        }
    }

    public LiveData<Resource<List<ItemLocation>>> getItemLocationListData() {
        return itemTypeListData;
    }

    public void setNextPageLoadingStateObj(String limit, String offset) {

        if (!isLoading) {
            ItemLocationViewModel.TmpDataHolder tmpDataHolder = new ItemLocationViewModel.TmpDataHolder();
            tmpDataHolder.offset = offset;
            tmpDataHolder.limit = limit;
            nextPageLoadingStateObj.setValue(tmpDataHolder);

            // start loading
            setLoadingState(true);
        }
    }

    public LiveData<Resource<Boolean>> getNextPageLoadingStateData() {
        return nextPageLoadingStateData;
    }

    class TmpDataHolder {
        public String limit = "";
        public String offset = "";
        public String cityId = "";
    }
}
