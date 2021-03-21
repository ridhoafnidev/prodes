package com.mrii.prodes.repository.banner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.mrii.prodes.AppExecutors;
import com.mrii.prodes.Config;
import com.mrii.prodes.api.ApiResponse;
import com.mrii.prodes.api.PSApiService;
import com.mrii.prodes.db.BannerDao;
import com.mrii.prodes.db.PSCoreDb;
import com.mrii.prodes.repository.common.NetworkBoundResource;
import com.mrii.prodes.repository.common.PSRepository;
import com.mrii.prodes.utils.Utils;
import com.mrii.prodes.viewobject.Banner;
import com.mrii.prodes.viewobject.Banner;
import com.mrii.prodes.viewobject.common.Resource;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class BannerRepository extends PSRepository {

    private final BannerDao bannerDao;

    @Inject
    protected BannerRepository(PSApiService psApiService, AppExecutors appExecutors, PSCoreDb db, BannerDao bannerDao) {
        super(psApiService, appExecutors, db);
        this.bannerDao = bannerDao;
    }

    // function return live data to get three data feeds from SelectedCityFragment.java
    public LiveData<Resource<List<Banner>>> getNewsBannerList(String limit, String offset) {
        return new NetworkBoundResource<List<Banner>, List<Banner>>(appExecutors) {

            @Override
            protected void saveCallResult(@NonNull List<Banner> itemList) {
                Utils.psLog("SaveCallResult of getNewsFeedList.");

//                db.beginTransaction();

//                try {
//                    bannerDao.deleteAll();
//                    bannerDao.insertAll(itemList);
//
//                    db.setTransactionSuccessful();
//
//                } catch (Exception e) {
//                    Utils.psErrorLog("Error in doing transaction of getNewsFeedList.", e);
//                } finally {
//                    db.endTransaction();
//                }

                try {
                    db.runInTransaction(() -> {
                        bannerDao.deleteAll();
                        bannerDao.insertAll(itemList);
                    });
                } catch (Exception ex) {
                    Utils.psErrorLog("Error at getNewsFeedList", ex);
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable List<Banner> data) {
                // Recent news always load from server
                return connectivity.isConnected();
            }

            @NonNull
            @Override
            protected LiveData<List<Banner>> loadFromDb() {
                Utils.psLog("Load getNewsFeedList From Db");
                if (limit.equals(String.valueOf(Config.LIST_NEW_FEED_COUNT_PAGER))) {
                    return bannerDao.getAllNewsBannerByLimit(limit);
                } else {
                    return bannerDao.getAllNewsBanner();
                }
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<Banner>>> createCall() {
                Utils.psLog("Call API Service to getNewsBannerList.");

                return psApiService.getAllNewsBanner(Config.API_KEY, limit, offset);
            }

            @Override
            protected void onFetchFailed(String message) {
                Utils.psLog("Fetch Failed (getNewsFeedList) : " + message);
            }
        }.asLiveData();
    }

    public LiveData<Resource<Boolean>> getNextPageNewsFeedList(String apiKey, String limit, String offset) {

        final MediatorLiveData<Resource<Boolean>> statusLiveData = new MediatorLiveData<>();

        LiveData<ApiResponse<List<Banner>>> apiResponse = psApiService.getAllNewsBanner(apiKey, limit, offset);

        statusLiveData.addSource(apiResponse, response -> {

            statusLiveData.removeSource(apiResponse);

            //noinspection Constant Conditions
            if (response.isSuccessful()) {

                appExecutors.diskIO().execute(() -> {

                    try {
                        db.runInTransaction(() -> {
                            db.bannerDao().insertAll(response.body);
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

    public LiveData<Resource<Banner>> getBannerById(String id) {
        return new NetworkBoundResource<Banner, Banner>(appExecutors) {

            @Override
            protected void saveCallResult(@NonNull Banner Banner) {
                Utils.psLog("SaveCallResult of getBannerById.");

//                db.beginTransaction();
//
//                try {
//
//                    BannerDao.insert(Banner);
//
//                    db.setTransactionSuccessful();
//
//                } catch (Exception e) {
//                    Utils.psErrorLog("Error in doing transaction of getBannerById.", e);
//                } finally {
//                    db.endTransaction();
//                }

                try {
                    db.runInTransaction(() -> bannerDao.insert(Banner));
                } catch (Exception ex) {
                    Utils.psErrorLog("Error at ", ex);
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable Banner Banner) {

                // Recent news always load from server
                return connectivity.isConnected();

            }

            @NonNull
            @Override
            protected LiveData<Banner> loadFromDb() {

                Utils.psLog("Load getBannerById From Db");
                return bannerDao.getBannerById(id);

            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<Banner>> createCall() {

                Utils.psLog("Call API Service to getBannerById.");
                return psApiService.getBannersById(Config.API_KEY, id);

            }

            @Override
            protected void onFetchFailed(String message) {
                Utils.psLog("Fetch Failed (getBannerById) : " + message);
            }
        }.asLiveData();
    }


}
