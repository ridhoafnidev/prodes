package com.mrii.prodes.viewmodel.item;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.mrii.prodes.repository.item.ItemRepository;
import com.mrii.prodes.utils.AbsentLiveData;
import com.mrii.prodes.utils.Constants;
import com.mrii.prodes.utils.Utils;
import com.mrii.prodes.viewmodel.common.PSViewModel;
import com.mrii.prodes.viewobject.Item;
import com.mrii.prodes.viewobject.common.Resource;
import com.mrii.prodes.viewobject.holder.ItemParameterHolder;

import java.util.List;

import javax.inject.Inject;

public class RecentItemViewModel extends PSViewModel {

    private final LiveData<Resource<List<Item>>> recentItemListByKeyData;
    private final MutableLiveData<RecentItemViewModel.ItemTmpDataHolder> itemListByKeyObj = new MutableLiveData<>();

    private final LiveData<Resource<Boolean>> nextPagerecentItemListByKeyData;
    private final MutableLiveData<RecentItemViewModel.ItemTmpDataHolder> nextPageItemListByKeyObj = new MutableLiveData<>();

    public ItemParameterHolder recentItemParameterHolder = new ItemParameterHolder().getRecentItem();

    public String locationId;
    public String locationName;
    public String locationLat = Constants.EMPTY_STRING;
    public String locationLng = Constants.EMPTY_STRING;

    @Inject
    RecentItemViewModel(ItemRepository repository)
    {

        recentItemListByKeyData = Transformations.switchMap(itemListByKeyObj, obj -> {

            if (obj == null) {
                return AbsentLiveData.create();
            }

            return repository.getRecentItemListByKey(obj.loginUserId, obj.limit, obj.offset, obj.itemParameterHolder);

        });

        nextPagerecentItemListByKeyData = Transformations.switchMap(nextPageItemListByKeyObj, obj -> {

            if (obj == null) {
                return AbsentLiveData.create();
            }

            return repository.getNextPageProductListByKey(obj.itemParameterHolder, obj.loginUserId, obj.limit, obj.offset);

        });
    }

    //region getItemList

    public void setRecentItemListByKeyObj(String loginUserId, String limit, String offset, ItemParameterHolder parameterHolder) {

        RecentItemViewModel.ItemTmpDataHolder tmpDataHolder = new RecentItemViewModel.ItemTmpDataHolder(limit, offset, loginUserId, parameterHolder);

        this.itemListByKeyObj.setValue(tmpDataHolder);

    }

    public LiveData<Resource<List<Item>>> getRecentItemListByKeyData() {
        return recentItemListByKeyData;
    }

    public void setNextPageRecentItemListByKeyObj(String limit, String offset, String loginUserId, ItemParameterHolder parameterHolder) {

        RecentItemViewModel.ItemTmpDataHolder tmpDataHolder = new RecentItemViewModel.ItemTmpDataHolder(limit, offset, loginUserId, parameterHolder);

        this.nextPageItemListByKeyObj.setValue(tmpDataHolder);
    }

    public LiveData<Resource<Boolean>> getNextPageRecentItemListByKeyData() {
        return nextPagerecentItemListByKeyData;
    }

    //endregion

    class ItemTmpDataHolder {

        private String limit, offset, loginUserId;
        private ItemParameterHolder itemParameterHolder;
        private String defaultLat, defaultLng;

        public ItemTmpDataHolder(String limit, String offset, String loginUserId, ItemParameterHolder itemParameterHolder ) {
            this.limit = limit;
            this.offset = offset;
            this.loginUserId = loginUserId;
            this.defaultLat = defaultLat;
            this.defaultLng = defaultLng;

//            if(defaultLat.equals(itemParameterHolder.lat) && defaultLng.equals(itemParameterHolder.lng) ) {
//                itemParameterHolder.lat = "";
//                itemParameterHolder.lng = "";
//                itemParameterHolder.mapMiles = "";
//            }

            this.itemParameterHolder = itemParameterHolder;
        }
    }
}

