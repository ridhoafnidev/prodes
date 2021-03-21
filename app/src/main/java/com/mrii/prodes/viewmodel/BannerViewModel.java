package com.mrii.prodes.viewmodel;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.mrii.prodes.Config;
import com.mrii.prodes.repository.banner.BannerRepository;
import com.mrii.prodes.repository.blog.BlogRepository;
import com.mrii.prodes.utils.AbsentLiveData;
import com.mrii.prodes.viewmodel.common.PSViewModel;
import com.mrii.prodes.viewobject.Banner;
import com.mrii.prodes.viewobject.Blog;
import com.mrii.prodes.viewobject.common.Resource;

import java.util.List;

import javax.inject.Inject;

public class BannerViewModel extends PSViewModel {

    private final LiveData<Resource<List<Banner>>> bannerData;
    private MutableLiveData<BannerViewModel.TmpDataHolder> bannerObj = new MutableLiveData<>();

    private final LiveData<Resource<Boolean>> nextPageBannerData;
    private MutableLiveData<BannerViewModel.TmpDataHolder> nextPageBannerObj = new MutableLiveData<>();

    private final LiveData<Resource<Banner>> bannerByIdData;
    private MutableLiveData<BannerByIdTmpDataHolder> bannerByIdObj = new MutableLiveData<>();

    public String cityId;

    @Inject
    BannerViewModel(BannerRepository repository) {
        bannerData = Transformations.switchMap(bannerObj, obj -> {

            if (obj == null) {
                return AbsentLiveData.create();
            }

            return repository.getNewsBannerList(obj.limit, obj.offset);

        });

        nextPageBannerData = Transformations.switchMap(nextPageBannerObj, obj -> {

            if (obj == null) {
                return AbsentLiveData.create();
            }

            return repository.getNextPageNewsFeedList(Config.API_KEY, obj.limit, obj.offset);

        });

        bannerByIdData = Transformations.switchMap(bannerByIdObj, obj -> {

            if (obj == null) {
                return AbsentLiveData.create();
            }

            return repository.getBannerById(obj.id);

        });

    }

    public void setNewsBannerObj(String limit, String offset) {
        TmpDataHolder tmpDataHolder = new BannerViewModel.TmpDataHolder(limit, offset);

        this.bannerObj.setValue(tmpDataHolder);
    }

    public LiveData<Resource<List<Banner>>> getNewsBannerData() {
        return bannerData;
    }

    public void setNextPageNewsFeedObj(String limit, String offset) {
        TmpDataHolder tmpDataHolder = new TmpDataHolder(limit, offset);

        this.nextPageBannerObj.setValue(tmpDataHolder);
    }

    public LiveData<Resource<Boolean>> getNextPageNewsFeedData() {
        return nextPageBannerData;
    }

    public void setBannerByIdObj(String id) {
        BannerByIdTmpDataHolder blogByIdTmpDataHolder = new BannerByIdTmpDataHolder(id);

        this.bannerByIdObj.setValue(blogByIdTmpDataHolder);
    }

    public LiveData<Resource<Banner>> getBannerByIdData() {
        return bannerByIdData;
    }

    class TmpDataHolder {

        String  limit, offset;

        public TmpDataHolder(String limit, String offset) {
            this.limit = limit;
            this.offset = offset;
        }
    }

    class BannerByIdTmpDataHolder {

        String id;

        private BannerByIdTmpDataHolder(String id) {
            this.id = id;
        }
    }
}
