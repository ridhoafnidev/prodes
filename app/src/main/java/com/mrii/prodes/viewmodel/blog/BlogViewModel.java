package com.mrii.prodes.viewmodel.blog;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.mrii.prodes.Config;
import com.mrii.prodes.repository.blog.BlogRepository;
import com.mrii.prodes.utils.AbsentLiveData;
import com.mrii.prodes.viewmodel.common.PSViewModel;
import com.mrii.prodes.viewobject.Blog;
import com.mrii.prodes.viewobject.common.Resource;

import java.util.List;

import javax.inject.Inject;

public class BlogViewModel extends PSViewModel {

    private final LiveData<Resource<List<Blog>>> newsFeedData;
    private MutableLiveData<BlogViewModel.TmpDataHolder> newsFeedObj = new MutableLiveData<>();

    // LiveData three news feed in SelectedCityFragment.java
    private final LiveData<Resource<List<Blog>>> newsThreeFeedData;
    // MutableLiveData three news feed in SelectedCityFragment.java
    private MutableLiveData<BlogViewModel.TmpDataHolder> newsThreeFeedObj = new MutableLiveData<>();

    private final LiveData<Resource<Boolean>> nextPageNewsFeedData;
    private MutableLiveData<BlogViewModel.TmpDataHolder> nextPageNewsFeedObj = new MutableLiveData<>();

    private final LiveData<Resource<Blog>> blogByIdData;
    private MutableLiveData<BlogViewModel.BlogByIdTmpDataHolder> blogByIdObj = new MutableLiveData<>();

    public String cityId;

    @Inject
    BlogViewModel(BlogRepository repository) {

        newsFeedData = Transformations.switchMap(newsFeedObj, obj -> {

            if (obj == null) {
                return AbsentLiveData.create();
            }

            return repository.getNewsFeedList(obj.limit, obj.offset);

        });

        // constructor DI to get three data feed in SelectedCityFragment.java
        newsThreeFeedData = Transformations.switchMap(newsThreeFeedObj, obj -> {

            if (obj == null) {
                return AbsentLiveData.create();
            }

            return repository.getThreeNewsFeedList(obj.limit, obj.offset);

        });

        nextPageNewsFeedData = Transformations.switchMap(nextPageNewsFeedObj, obj -> {

            if (obj == null) {
                return AbsentLiveData.create();
            }

            return repository.getNextPageNewsFeedList(Config.API_KEY, obj.limit, obj.offset);

        });

        blogByIdData = Transformations.switchMap(blogByIdObj, obj -> {

            if (obj == null) {
                return AbsentLiveData.create();
            }

            return repository.getBlogById(obj.id);

        });

    }

    public void setNewsFeedObj(String limit, String offset) {
        TmpDataHolder tmpDataHolder = new TmpDataHolder(limit, offset);

        this.newsFeedObj.setValue(tmpDataHolder);
    }

    public LiveData<Resource<List<Blog>>> getNewsFeedData() {
        return newsFeedData;
    }

    // function set three feed in SelectedCityFragment
    public void setThreeNewsFeedObj(String limit, String offset) {
        TmpDataHolder tmpDataHolder = new TmpDataHolder(limit, offset);

        this.newsThreeFeedObj.setValue(tmpDataHolder);
    }

    // function get three feed in SelectedCityFragment LiveData
    public LiveData<Resource<List<Blog>>> getThreeNewsFeedData() {
        return newsThreeFeedData;
    }

    public void setNextPageNewsFeedObj(String limit, String offset) {
        TmpDataHolder tmpDataHolder = new TmpDataHolder(limit, offset);

        this.nextPageNewsFeedObj.setValue(tmpDataHolder);
    }

    public LiveData<Resource<Boolean>> getNextPageNewsFeedData() {
        return nextPageNewsFeedData;
    }

    public void setBlogByIdObj(String id) {
        BlogByIdTmpDataHolder blogByIdTmpDataHolder = new BlogByIdTmpDataHolder(id);

        this.blogByIdObj.setValue(blogByIdTmpDataHolder);
    }

    public LiveData<Resource<Blog>> getBlogByIdData() {
        return blogByIdData;
    }

    class TmpDataHolder {

        String  limit, offset;

        public TmpDataHolder(String limit, String offset) {
            this.limit = limit;
            this.offset = offset;
        }
    }

    class BlogByIdTmpDataHolder {

        String id;

        private BlogByIdTmpDataHolder(String id) {
            this.id = id;
        }
    }
}
