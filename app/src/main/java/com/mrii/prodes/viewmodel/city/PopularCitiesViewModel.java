package com.mrii.prodes.viewmodel.city;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.mrii.prodes.repository.city.CityRepository;
import com.mrii.prodes.utils.AbsentLiveData;
import com.mrii.prodes.viewmodel.common.PSViewModel;
import com.mrii.prodes.viewobject.City;
import com.mrii.prodes.viewobject.common.Resource;
import com.mrii.prodes.viewobject.holder.CityParameterHolder;

import java.util.List;

import javax.inject.Inject;

public class PopularCitiesViewModel extends PSViewModel {

    private final LiveData<Resource<List<City>>> popularCityListData;
    private MutableLiveData<PopularCitiesViewModel.CityListTmpDataHolder> cityListObj = new MutableLiveData<>();

    private final LiveData<Resource<Boolean>> nextPagepopularCityListData;
    private MutableLiveData<PopularCitiesViewModel.CityListTmpDataHolder> nextPageCityListObj = new MutableLiveData<>();

    public CityParameterHolder popularCitiesParameterHolder = new CityParameterHolder().getPopularCities();

    @Inject
    PopularCitiesViewModel(CityRepository repository) {

        //region Getting City List

        popularCityListData = Transformations.switchMap(cityListObj, obj -> {

            if (obj == null) {
                return AbsentLiveData.create();
            }

            return repository.getCityListByValue(obj.limit, obj.offset, obj.parameterHolder);

        });

        nextPagepopularCityListData = Transformations.switchMap(nextPageCityListObj, obj -> {

            if (obj == null) {
                return AbsentLiveData.create();
            }

            return repository.getNextPageCityList(obj.limit, obj.offset, obj.parameterHolder);

        });

        //endregion

    }


    //region Getting City List

    public void setPopularCityListObj(String limit, String offset, CityParameterHolder parameterHolder) {
        PopularCitiesViewModel.CityListTmpDataHolder tmpDataHolder = new PopularCitiesViewModel.CityListTmpDataHolder(limit, offset, parameterHolder);

        this.cityListObj.setValue(tmpDataHolder);
    }

    public LiveData<Resource<List<City>>> getPopularCityListData() {
        return popularCityListData;
    }

    public void setNextPagePopularCityListObj(String limit, String offset, CityParameterHolder parameterHolder) {
        PopularCitiesViewModel.CityListTmpDataHolder tmpDataHolder = new PopularCitiesViewModel.CityListTmpDataHolder(limit, offset, parameterHolder);

        this.nextPageCityListObj.setValue(tmpDataHolder);
    }

    public LiveData<Resource<Boolean>> getNextPagePopularCityListData() {
        return nextPagepopularCityListData;
    }

    //endregion


    class CityListTmpDataHolder {

        private String limit, offset;
        private CityParameterHolder parameterHolder;

        public CityListTmpDataHolder(String limit, String offset, CityParameterHolder parameterHolder) {
            this.limit = limit;
            this.offset = offset;
            this.parameterHolder = parameterHolder;
        }
    }
}
