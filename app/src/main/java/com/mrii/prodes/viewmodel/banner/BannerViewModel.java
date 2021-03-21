package com.mrii.prodes.viewmodel.banner;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.mrii.prodes.Config;
import com.mrii.prodes.repository.banner.BannerRepository;
import com.mrii.prodes.repository.blog.BlogRepository;
import com.mrii.prodes.utils.AbsentLiveData;
import com.mrii.prodes.viewmodel.blog.BlogViewModel;
import com.mrii.prodes.viewmodel.common.PSViewModel;
import com.mrii.prodes.viewobject.Banner;
import com.mrii.prodes.viewobject.Blog;
import com.mrii.prodes.viewobject.common.Resource;

import java.util.List;

import javax.inject.Inject;

public class BannerViewModel extends PSViewModel {

    private final LiveData<Resource<List<Banner>>> newsBannerData;
    private MutableLiveData<BannerViewModel.TmpDataHolder> newsBannerObj = new MutableLiveData<>();

    private final LiveData<Resource<Boolean>> nextPageNewsBannerData;
    private MutableLiveData<BannerViewModel.TmpDataHolder> nextPageNewsBannerObj = new MutableLiveData<>();

    private final LiveData<Resource<Banner>> bannerByIdData;
    private MutableLiveData<BannerViewModel.BannerByIdTmpDataHolder> bannerByIdObj = new MutableLiveData<>();

    public String cityId;

    @Inject
    BannerViewModel(BannerRepository repository) {

        newsBannerData = Transformations.switchMap(newsBannerObj, obj -> {

            if (obj == null) {
                return AbsentLiveData.create();
            }

            return repository.getNewsBannerList(obj.limit, obj.offset);

        });

        nextPageNewsBannerData = Transformations.switchMap(nextPageNewsBannerObj, obj -> {

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
        BannerViewModel.TmpDataHolder tmpDataHolder = new BannerViewModel.TmpDataHolder(limit, offset);

        this.newsBannerObj.setValue(tmpDataHolder);
    }

    public LiveData<Resource<List<Banner>>> getNewsBannerData() {
        return newsBannerData;
    }


    public void setNextPageNewsFeedObj(String limit, String offset) {
        BannerViewModel.TmpDataHolder tmpDataHolder = new BannerViewModel.TmpDataHolder(limit, offset);

        this.nextPageNewsBannerObj.setValue(tmpDataHolder);
    }

    public LiveData<Resource<Boolean>> getNextPageNewsBannerData() {
        return nextPageNewsBannerData;
    }

    public void setBlogByIdObj(String id) {
        BannerViewModel.BannerByIdTmpDataHolder bannerByIdTmpDataHolder = new BannerViewModel.BannerByIdTmpDataHolder(id);

        this.bannerByIdObj.setValue(bannerByIdTmpDataHolder);
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
