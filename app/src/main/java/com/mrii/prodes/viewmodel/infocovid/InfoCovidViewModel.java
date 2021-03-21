package com.mrii.prodes.viewmodel.infocovid;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.mrii.prodes.Config;
import com.mrii.prodes.repository.blog.BlogRepository;
import com.mrii.prodes.repository.infocovid.InfoCovidRepository;
import com.mrii.prodes.utils.AbsentLiveData;
import com.mrii.prodes.viewmodel.common.PSViewModel;
import com.mrii.prodes.viewobject.Blog;
import com.mrii.prodes.viewobject.InfoCovid;
import com.mrii.prodes.viewobject.common.Resource;

import java.util.List;

import javax.inject.Inject;

public class InfoCovidViewModel extends PSViewModel {

    private final LiveData<Resource<List<InfoCovid>>> newsInfoCovidData;
    private MutableLiveData<InfoCovidViewModel.TmpDataHolder> newsInfoCovidObj = new MutableLiveData<>();

    // LiveData three news InfoCovid in SelectedCityFragment.java
    private final LiveData<Resource<List<InfoCovid>>> newsFiveInfoCovidData;
    // MutableLiveData three news InfoCovid in SelectedCityFragment.java
    private MutableLiveData<InfoCovidViewModel.TmpDataHolder> newsFiveInfoCovidObj = new MutableLiveData<>();

    private final LiveData<Resource<Boolean>> nextPageNewsInfoCovidData;
    private MutableLiveData<InfoCovidViewModel.TmpDataHolder> nextPageNewsInfoCovidObj = new MutableLiveData<>();

    private final LiveData<Resource<InfoCovid>> infoCovidByIdData;
    private MutableLiveData<InfoCovidViewModel.InfoCovidByIdTmpDataHolder> infoCovidByIdObj = new MutableLiveData<>();

    public String cityId;

    @Inject
    InfoCovidViewModel(InfoCovidRepository repository) {

        newsInfoCovidData = Transformations.switchMap(newsInfoCovidObj, obj -> {

            if (obj == null) {
                return AbsentLiveData.create();
            }

            return repository.getNewsInfoCovidList(obj.limit, obj.offset);

        });

        // constructor DI to get three data InfoCovid in SelectedCityFragment.java
        newsFiveInfoCovidData = Transformations.switchMap(newsFiveInfoCovidObj, obj -> {

            if (obj == null) {
                return AbsentLiveData.create();
            }

            return repository.getFiveNewsInfoCovidList(obj.limit, obj.offset);

        });

        nextPageNewsInfoCovidData = Transformations.switchMap(nextPageNewsInfoCovidObj, obj -> {

            if (obj == null) {
                return AbsentLiveData.create();
            }

            return repository.getNextPageNewsInfoCovidList(Config.API_KEY, obj.limit, obj.offset);

        });

        infoCovidByIdData = Transformations.switchMap(infoCovidByIdObj, obj -> {

            if (obj == null) {
                return AbsentLiveData.create();
            }

            return repository.getInfoCovidById(obj.id);

        });

    }

    public void setNewsInfoCovidObj(String limit, String offset) {
        TmpDataHolder tmpDataHolder = new TmpDataHolder(limit, offset);

        this.newsInfoCovidObj.setValue(tmpDataHolder);
    }

    public LiveData<Resource<List<InfoCovid>>> getNewsInfoCovidData() {
        return newsInfoCovidData;
    }

    public void setNextPageNewsInfoCovidObj(String limit, String offset) {
        TmpDataHolder tmpDataHolder = new TmpDataHolder(limit, offset);

        this.nextPageNewsInfoCovidObj.setValue(tmpDataHolder);
    }

    public LiveData<Resource<Boolean>> getNextPageNewsInfoCovidData() {
        return nextPageNewsInfoCovidData;
    }

    public void setInfoCovidByIdObj(String id) {
        InfoCovidByIdTmpDataHolder infoCovidByIdTmpDataHolder = new InfoCovidByIdTmpDataHolder(id);

        this.infoCovidByIdObj.setValue(infoCovidByIdTmpDataHolder);
    }

    public LiveData<Resource<InfoCovid>> getInfoCovidByIdData() {
        return infoCovidByIdData;
    }

    class TmpDataHolder {

        String  limit, offset;

        public TmpDataHolder(String limit, String offset) {
            this.limit = limit;
            this.offset = offset;
        }
    }

    class InfoCovidByIdTmpDataHolder {

        String id;

        private InfoCovidByIdTmpDataHolder(String id) {
            this.id = id;
        }
    }
}
