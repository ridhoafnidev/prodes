package com.mrii.prodes.repository.itemcondition;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.mrii.prodes.AppExecutors;
import com.mrii.prodes.Config;
import com.mrii.prodes.api.ApiResponse;
import com.mrii.prodes.api.PSApiService;
import com.mrii.prodes.db.ItemConditionDao;
import com.mrii.prodes.db.PSCoreDb;
import com.mrii.prodes.repository.common.NetworkBoundResource;
import com.mrii.prodes.repository.common.PSRepository;
import com.mrii.prodes.utils.Utils;
import com.mrii.prodes.viewobject.ItemCondition;
import com.mrii.prodes.viewobject.common.Resource;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ItemConditionRepository extends PSRepository {
    private ItemConditionDao itemConditionDao;

    @Inject
    ItemConditionRepository(PSApiService psApiService, AppExecutors appExecutors, PSCoreDb db, ItemConditionDao itemConditionDao) {

        super(psApiService, appExecutors, db);
        this.itemConditionDao = itemConditionDao;
    }

    public LiveData<Resource<List<ItemCondition>>> getAllItemConditionList(String limit, String offset) {

        return new NetworkBoundResource<List<ItemCondition>, List<ItemCondition>>(appExecutors) {

            @Override
            protected void saveCallResult(@NonNull List<ItemCondition> itemTypeList) {
                Utils.psLog("SaveCallResult of getAllCategoriesWithUserId");

                try {
                    db.runInTransaction(() -> {
                        itemConditionDao.deleteAllItemCondition();

                        itemConditionDao.insertAll(itemTypeList);

                    });
                } catch (Exception ex) {
                    Utils.psErrorLog("Error at ", ex);
                }
            }


            @Override
            protected boolean shouldFetch(@Nullable List<ItemCondition> data) {

                return connectivity.isConnected();
            }

            @NonNull
            @Override
            protected LiveData<List<ItemCondition>> loadFromDb() {

                Utils.psLog("Load From Db (All Categories)");

                return itemConditionDao.getAllItemCondition();

            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<ItemCondition>>> createCall() {
                Utils.psLog("Call Get All Categories webservice.");

                return psApiService.getItemConditionTypeList(Config.API_KEY,limit, offset);
            }

            @Override
            protected void onFetchFailed(String message) {
                Utils.psLog("Fetch Failed of About Us");
            }

        }.asLiveData();
    }

    public LiveData<Resource<Boolean>> getNextPageItemConditionList( String limit, String offset) {

        final MediatorLiveData<Resource<Boolean>> statusLiveData = new MediatorLiveData<>();
        LiveData<ApiResponse<List<ItemCondition>>> apiResponse = psApiService.getItemConditionTypeList(Config.API_KEY, limit, offset);

        statusLiveData.addSource(apiResponse, response -> {

            statusLiveData.removeSource(apiResponse);

            //noinspection Constant Conditions
            if (response.isSuccessful()) {

                appExecutors.diskIO().execute(() -> {

                    try {
                        db.runInTransaction(() -> {
                            if (apiResponse.getValue() != null) {

                                itemConditionDao.insertAll(apiResponse.getValue().body);
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