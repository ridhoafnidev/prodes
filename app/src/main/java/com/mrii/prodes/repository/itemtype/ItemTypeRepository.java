package com.mrii.prodes.repository.itemtype;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.mrii.prodes.AppExecutors;
import com.mrii.prodes.Config;
import com.mrii.prodes.api.ApiResponse;
import com.mrii.prodes.api.PSApiService;
import com.mrii.prodes.db.ItemTypeDao;
import com.mrii.prodes.db.PSCoreDb;
import com.mrii.prodes.repository.common.NetworkBoundResource;
import com.mrii.prodes.repository.common.PSRepository;
import com.mrii.prodes.utils.Utils;
import com.mrii.prodes.viewobject.ItemType;
import com.mrii.prodes.viewobject.common.Resource;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ItemTypeRepository extends PSRepository {
    private ItemTypeDao itemTypeDao;

    @Inject
    ItemTypeRepository(PSApiService psApiService, AppExecutors appExecutors, PSCoreDb db, ItemTypeDao itemTypeDao) {

        super(psApiService, appExecutors, db);
        this.itemTypeDao = itemTypeDao;
    }

    public LiveData<Resource<List<ItemType>>> getAllItemTypeList(String limit, String offset) {

        return new NetworkBoundResource<List<ItemType>, List<ItemType>>(appExecutors) {

            @Override
            protected void saveCallResult(@NonNull List<ItemType> itemTypeList) {
                Utils.psLog("SaveCallResult of getAllCategoriesWithUserId");

                try {
                    db.runInTransaction(() -> {
                        itemTypeDao.deleteAllItemType();

                        itemTypeDao.insertAll(itemTypeList);
                    });
                } catch (Exception ex) {
                    Utils.psErrorLog("Error at ", ex);
                }
            }


            @Override
            protected boolean shouldFetch(@Nullable List<ItemType> data) {

                return connectivity.isConnected();
            }

            @NonNull
            @Override
            protected LiveData<List<ItemType>> loadFromDb() {

                Utils.psLog("Load From Db (All Categories)");

                return itemTypeDao.getAllItemType();

            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<ItemType>>> createCall() {
                Utils.psLog("Call Get All Categories webservice.");

                return psApiService.getItemTypeList(Config.API_KEY, limit, offset);
            }

            @Override
            protected void onFetchFailed(String message) {
                Utils.psLog("Fetch Failed of About Us");
            }

        }.asLiveData();
    }

    public LiveData<Resource<Boolean>> getNextPageItemTypeList( String limit, String offset) {

        final MediatorLiveData<Resource<Boolean>> statusLiveData = new MediatorLiveData<>();
        LiveData<ApiResponse<List<ItemType>>> apiResponse = psApiService.getItemTypeList(Config.API_KEY, limit, offset);

        statusLiveData.addSource(apiResponse, response -> {

            statusLiveData.removeSource(apiResponse);

            //noinspection Constant Conditions
            if (response.isSuccessful()) {

                appExecutors.diskIO().execute(() -> {

                    try {
                        db.runInTransaction(() -> {
                            if (apiResponse.getValue() != null) {

                                itemTypeDao.insertAll(apiResponse.getValue().body);
                            }
                        });
                    } catch (Exception ex) {
                        Utils.psErrorLog("Error at ", ex);
                    }

                    statusLiveData.postValue(Resource.success(true));

                });
            } else {
                statusLiveData.postValue(Resource.error(response.errorMessage, null));
            }

        });

        return statusLiveData;

    }
}
