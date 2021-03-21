package com.mrii.prodes.viewmodel.item;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.mrii.prodes.repository.item.ItemRepository;
import com.mrii.prodes.utils.AbsentLiveData;
import com.mrii.prodes.utils.Constants;
import com.mrii.prodes.viewmodel.common.PSViewModel;
import com.mrii.prodes.viewobject.Item;
import com.mrii.prodes.viewobject.common.Resource;
import com.mrii.prodes.viewobject.holder.ItemParameterHolder;

import java.util.List;

import javax.inject.Inject;

public class PopularItemViewModel extends PSViewModel {

    // Variable LiveData to get 10(teen) populer items from remote and local database used in SelectedCityFragment.java
    private final LiveData<Resource<List<Item>>> fiveRekomendationItemListByKeyData;
    // Variable MutableLiveData to set reguest 10(teen) populer items from remote database used in SelectedCityFragment.java
    private final MutableLiveData<PopularItemViewModel.ItemTmpDataHolder> fiveItemRekomendationListByKeyObj = new MutableLiveData<>();

    private final LiveData<Resource<Boolean>> nextPagepopularItemListByKeyData;
    private final MutableLiveData<PopularItemViewModel.ItemTmpDataHolder> nextPageItemListByKeyObj = new MutableLiveData<>();

    public ItemParameterHolder popularItemParameterHolder = new ItemParameterHolder().getPopularItem();

    public String itemId = Constants.EMPTY_STRING;
    public String itemName = Constants.EMPTY_STRING;
    public String catId = Constants.EMPTY_STRING;
    public String subCatId = Constants.EMPTY_STRING;

    @Inject
    PopularItemViewModel(ItemRepository repository)
    {

        // Devedency Injection(DI) to inject constructor class ItemRepository to get 10 (teen) populer items used in (SelectedCituFragment.java)
        fiveRekomendationItemListByKeyData = Transformations.switchMap(fiveItemRekomendationListByKeyObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            return repository.getRekomendationItemListByKey(obj.loginUserId, obj.limit, obj.offset, obj.itemParameterHolder);
        });

        nextPagepopularItemListByKeyData = Transformations.switchMap(nextPageItemListByKeyObj, obj -> {

            if (obj == null) {
                return AbsentLiveData.create();
            }

            return repository.getNextPageProductListByKey(obj.itemParameterHolder, obj.loginUserId, obj.limit, obj.offset);

        });
    }

    // region getItemList

        // Function set request 5 (five) populer/rekomendation items from remote database in fragment (SelectedCityFragment.java)
        public void setFiveREkomendationItemListByKeyObj(String loginUserId, String limit, String offset, ItemParameterHolder parameterHolder) {
            PopularItemViewModel.ItemTmpDataHolder tmpDataHolder = new PopularItemViewModel.ItemTmpDataHolder(limit, offset, loginUserId, parameterHolder);
            this.fiveItemRekomendationListByKeyObj.setValue(tmpDataHolder);
        }

        // Function get request 5 (five) populer/rekomendation items from remote database in fragment (SelectedCityFragment.java)
        public LiveData<Resource<List<Item>>> getFiveRekomendationItemListByKeyData() {
            return fiveRekomendationItemListByKeyData;
        }

        public void setNextPagePopularItemListByKeyObj(String limit, String offset, String loginUserId, ItemParameterHolder parameterHolder) {

            PopularItemViewModel.ItemTmpDataHolder tmpDataHolder = new PopularItemViewModel.ItemTmpDataHolder(limit, offset, loginUserId, parameterHolder);

            this.nextPageItemListByKeyObj.setValue(tmpDataHolder);
        }

        public LiveData<Resource<Boolean>> getNextPagePopularItemListByKeyData() {
            return nextPagepopularItemListByKeyData;
        }

    //endregion

    class ItemTmpDataHolder {

        private String limit, offset, loginUserId;
        private ItemParameterHolder itemParameterHolder;

        public ItemTmpDataHolder(String limit, String offset, String loginUserId, ItemParameterHolder itemParameterHolder) {
            this.limit = limit;
            this.offset = offset;
            this.loginUserId = loginUserId;
            this.itemParameterHolder = itemParameterHolder;
        }
    }
}

