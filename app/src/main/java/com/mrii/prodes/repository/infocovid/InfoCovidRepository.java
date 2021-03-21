package com.mrii.prodes.repository.infocovid;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.mrii.prodes.AppExecutors;
import com.mrii.prodes.Config;
import com.mrii.prodes.api.ApiResponse;
import com.mrii.prodes.api.PSApiService;
import com.mrii.prodes.db.BlogDao;
import com.mrii.prodes.db.InfoCovidDao;
import com.mrii.prodes.db.PSCoreDb;
import com.mrii.prodes.repository.common.NetworkBoundResource;
import com.mrii.prodes.repository.common.PSRepository;
import com.mrii.prodes.utils.Utils;
import com.mrii.prodes.viewobject.Blog;
import com.mrii.prodes.viewobject.InfoCovid;
import com.mrii.prodes.viewobject.common.Resource;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class InfoCovidRepository extends PSRepository {

    private final InfoCovidDao infoCovidDao;

    @Inject
    InfoCovidRepository(PSApiService psApiService, AppExecutors appExecutors, PSCoreDb db, InfoCovidDao infoCovidDao) {
        super(psApiService, appExecutors, db);
        this.infoCovidDao = infoCovidDao;
    }

    // function return live data to get three data feeds from SelectedCityFragment.java
    public LiveData<Resource<List<InfoCovid>>> getFiveNewsInfoCovidList(String limit, String offset) {
        return new NetworkBoundResource<List<InfoCovid>, List<InfoCovid>>(appExecutors) {

            @Override
            protected void saveCallResult(@NonNull List<InfoCovid> itemList) {
                Utils.psLog("SaveCallResult of getNewsFeedList.");

//                db.beginTransaction();

//                try {
//                    blogDao.deleteAll();
//                    blogDao.insertAll(itemList);
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
                        infoCovidDao.deleteAll();
                        infoCovidDao.insertAll(itemList);
                    });
                } catch (Exception ex) {
                    Utils.psErrorLog("Error at getNewsFeedList", ex);
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable List<InfoCovid> data) {
                // Recent news always load from server
                return connectivity.isConnected();
            }

            @NonNull
            @Override
            protected LiveData<List<InfoCovid>> loadFromDb() {
                Utils.psLog("Load getNewsFeedList From Db");
                if (limit.equals(String.valueOf(Config.LIST_NEW_FEED_COUNT_PAGER))) {
                    return infoCovidDao.getAllNewsInfoCovidByLimit(limit);
                } else {
                    return infoCovidDao.getAllNewsInfoCovid();
                }
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<InfoCovid>>> createCall() {
                Utils.psLog("Call API Service to getNewsFeedList.");

                return psApiService.getAllNewsInfoCovid(Config.API_KEY, limit, offset);
            }

            @Override
            protected void onFetchFailed(String message) {
                Utils.psLog("Fetch Failed (getNewsInfoCovidList) : " + message);
            }
        }.asLiveData();
    }

    public LiveData<Resource<List<InfoCovid>>> getNewsInfoCovidList(String limit, String offset) {
        return new NetworkBoundResource<List<InfoCovid>, List<InfoCovid>>(appExecutors) {

            @Override
            protected void saveCallResult(@NonNull List<InfoCovid> itemList) {
                Utils.psLog("SaveCallResult of getNewsFeedList.");

//                db.beginTransaction();

//                try {
//                    blogDao.deleteAll();
//                    blogDao.insertAll(itemList);
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
                        infoCovidDao.deleteAll();
                        infoCovidDao.insertAll(itemList);
                    });
                } catch (Exception ex) {
                    Utils.psErrorLog("Error at getNewsFeedList", ex);
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable List<InfoCovid> data) {
                // Recent news always load from server
                return connectivity.isConnected();
            }

            @NonNull
            @Override
            protected LiveData<List<InfoCovid>> loadFromDb() {
                Utils.psLog("Load getNewsFeedList From Db");
                if (limit.equals(String.valueOf(Config.LIST_NEW_FEED_COUNT_PAGER))) {
                    return infoCovidDao.getAllNewsInfoCovidByLimit(limit);
                } else {
                    return infoCovidDao.getAllNewsInfoCovid();
                }
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<InfoCovid>>> createCall() {
                Utils.psLog("Call API Service to getNewsFeedList.");

                return psApiService.getAllNewsInfoCovid(Config.API_KEY, limit, offset);
            }

            @Override
            protected void onFetchFailed(String message) {
                Utils.psLog("Fetch Failed (getNewsInfoCovidList) : " + message);
            }
        }.asLiveData();
    }

    public LiveData<Resource<Boolean>> getNextPageNewsInfoCovidList(String apiKey, String limit, String offset) {

        final MediatorLiveData<Resource<Boolean>> statusLiveData = new MediatorLiveData<>();

        LiveData<ApiResponse<List<InfoCovid>>> apiResponse = psApiService.getAllNewsInfoCovid(apiKey, limit, offset);

        statusLiveData.addSource(apiResponse, response -> {

            statusLiveData.removeSource(apiResponse);

            //noinspection Constant Conditions
            if (response.isSuccessful()) {

                appExecutors.diskIO().execute(() -> {

                    try {
                        db.runInTransaction(() -> {
                            db.infoCovidDao().insertAll(response.body);
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

    public LiveData<Resource<InfoCovid>> getInfoCovidById(String id) {
        return new NetworkBoundResource<InfoCovid, InfoCovid>(appExecutors) {

            @Override
            protected void saveCallResult(@NonNull InfoCovid infoCovid) {
                Utils.psLog("SaveCallResult of getBlogById.");

//                db.beginTransaction();
//
//                try {
//
//                    blogDao.insert(blog);
//
//                    db.setTransactionSuccessful();
//
//                } catch (Exception e) {
//                    Utils.psErrorLog("Error in doing transaction of getBlogById.", e);
//                } finally {
//                    db.endTransaction();
//                }

                try {
                    db.runInTransaction(() -> infoCovidDao.insert(infoCovid));
                } catch (Exception ex) {
                    Utils.psErrorLog("Error at ", ex);
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable InfoCovid infoCovid) {

                // Recent news always load from server
                return connectivity.isConnected();

            }

            @NonNull
            @Override
            protected LiveData<InfoCovid> loadFromDb() {

                Utils.psLog("Load getInfoCovidById From Db");
                return infoCovidDao.getInfoCovidById(id);

            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<InfoCovid>> createCall() {

                Utils.psLog("Call API Service to getInfoCovidById.");
                return psApiService.getInfoCovidById(Config.API_KEY, id);

            }

            @Override
            protected void onFetchFailed(String message) {
                Utils.psLog("Fetch Failed (getInfoCovidById) : " + message);
            }
        }.asLiveData();
    }

}