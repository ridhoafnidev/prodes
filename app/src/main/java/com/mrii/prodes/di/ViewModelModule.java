package com.mrii.prodes.di;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.mrii.prodes.viewmodel.BannerViewModel;
import com.mrii.prodes.viewmodel.ItemPaidHistoryViewModel.ItemPaidHistoryViewModel;
import com.mrii.prodes.viewmodel.aboutus.AboutUsViewModel;
import com.mrii.prodes.viewmodel.apploading.PSAPPLoadingViewModel;
import com.mrii.prodes.viewmodel.blog.BlogViewModel;
import com.mrii.prodes.viewmodel.chat.ChatViewModel;
import com.mrii.prodes.viewmodel.chathistory.ChatHistoryViewModel;
import com.mrii.prodes.viewmodel.city.CityViewModel;
import com.mrii.prodes.viewmodel.city.FeaturedCitiesViewModel;
import com.mrii.prodes.viewmodel.city.PopularCitiesViewModel;
import com.mrii.prodes.viewmodel.city.RecentCitiesViewModel;
import com.mrii.prodes.viewmodel.clearalldata.ClearAllDataViewModel;
import com.mrii.prodes.viewmodel.common.NotificationViewModel;
import com.mrii.prodes.viewmodel.common.PSNewsViewModelFactory;
import com.mrii.prodes.viewmodel.contactus.ContactUsViewModel;
import com.mrii.prodes.viewmodel.homelist.HomeTrendingCategoryListViewModel;
import com.mrii.prodes.viewmodel.image.ImageViewModel;
import com.mrii.prodes.viewmodel.infocovid.InfoCovidViewModel;
import com.mrii.prodes.viewmodel.item.DisabledViewModel;
import com.mrii.prodes.viewmodel.item.FavouriteViewModel;
import com.mrii.prodes.viewmodel.item.HistoryViewModel;
import com.mrii.prodes.viewmodel.item.NewItemViewModel;
import com.mrii.prodes.viewmodel.item.PendingViewModel;
import com.mrii.prodes.viewmodel.item.PopularItemViewModel;
import com.mrii.prodes.viewmodel.item.RecentItemViewModel;
import com.mrii.prodes.viewmodel.item.RejectedViewModel;
import com.mrii.prodes.viewmodel.item.SpecsViewModel;
import com.mrii.prodes.viewmodel.item.TouchCountViewModel;
import com.mrii.prodes.viewmodel.itemcategory.ItemCategoryViewModel;
import com.mrii.prodes.viewmodel.itemcondition.ItemConditionViewModel;
import com.mrii.prodes.viewmodel.itemcurrency.ItemCurrencyViewModel;
import com.mrii.prodes.viewmodel.itemdealoption.ItemDealOptionViewModel;
import com.mrii.prodes.viewmodel.itemfromfollower.ItemFromFollowerViewModel;
import com.mrii.prodes.viewmodel.itemlocation.ItemLocationViewModel;
import com.mrii.prodes.viewmodel.itempricetype.ItemPriceTypeViewModel;
import com.mrii.prodes.viewmodel.itemsubcategory.ItemSubCategoryViewModel;
import com.mrii.prodes.viewmodel.itemtype.ItemTypeViewModel;
import com.mrii.prodes.viewmodel.notification.NotificationsViewModel;
import com.mrii.prodes.viewmodel.paypal.PaypalViewModel;
import com.mrii.prodes.viewmodel.pscount.PSCountViewModel;
import com.mrii.prodes.viewmodel.rating.RatingViewModel;
import com.mrii.prodes.viewmodel.user.UserViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

/**
 * Created by Panacea-Soft on 11/16/17.
 * Contact Email : teamps.is.cool@gmail.com
 */

@Module
abstract class ViewModelModule {

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(PSNewsViewModelFactory factory);

    @Binds
    @IntoMap
    @ViewModelKey(UserViewModel.class)
    abstract ViewModel bindUserViewModel(UserViewModel userViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(AboutUsViewModel.class)
    abstract ViewModel bindAboutUsViewModel(AboutUsViewModel aboutUsViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(InfoCovidViewModel.class)
    abstract ViewModel bindInfoCovidViewModel(InfoCovidViewModel infoCovidViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ItemLocationViewModel.class)
    abstract ViewModel bindItemLocationViewModel(ItemLocationViewModel itemLocationViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ItemDealOptionViewModel.class)
    abstract ViewModel bindItemDealOptionViewModel(ItemDealOptionViewModel itemDealOptionViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ItemConditionViewModel.class)
    abstract ViewModel bindItemConditionViewModel(ItemConditionViewModel itemConditionViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ImageViewModel.class)
    abstract ViewModel bindImageViewModel(ImageViewModel imageViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ItemTypeViewModel.class)
    abstract ViewModel bindItemTypeViewModel(ItemTypeViewModel itemTypeViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(RatingViewModel.class)
    abstract ViewModel bindRatingViewModel(RatingViewModel ratingViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(NotificationViewModel.class)
    abstract ViewModel bindNotificationViewModel(NotificationViewModel notificationViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ItemFromFollowerViewModel.class)
    abstract ViewModel bindItemFromFollowerViewModel(ItemFromFollowerViewModel itemFromFollowerViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ItemPriceTypeViewModel.class)
    abstract ViewModel bindItemPriceTypeViewModel(ItemPriceTypeViewModel itemPriceTypeViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ItemCurrencyViewModel.class)
    abstract ViewModel bindItemCurrencyViewModel(ItemCurrencyViewModel itemCurrencyViewModel);


    @Binds
    @IntoMap
    @ViewModelKey(ContactUsViewModel.class)
    abstract ViewModel bindContactUsViewModel(ContactUsViewModel contactUsViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(DisabledViewModel.class)
    abstract ViewModel bindDisabledViewModel(DisabledViewModel disabledViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(RejectedViewModel.class)
    abstract ViewModel bindRejectedViewModel(RejectedViewModel rejectedViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(PendingViewModel.class)
    abstract ViewModel bindPendingViewModel(PendingViewModel pendingViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(FavouriteViewModel.class)
    abstract ViewModel bindFavouriteViewModel(FavouriteViewModel favouriteViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(TouchCountViewModel.class)
    abstract ViewModel bindTouchCountViewModel(TouchCountViewModel touchCountViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(SpecsViewModel.class)
    abstract ViewModel bindProductSpecsViewModel(SpecsViewModel specsViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(HistoryViewModel.class)
    abstract ViewModel bindHistoryProductViewModel(HistoryViewModel historyViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ItemCategoryViewModel.class)
    abstract ViewModel bindCityCategoryViewModel(ItemCategoryViewModel itemCategoryViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(NotificationsViewModel.class)
    abstract ViewModel bindNotificationListViewModel(NotificationsViewModel notificationListViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(HomeTrendingCategoryListViewModel.class)
    abstract ViewModel bindHomeTrendingCategoryListViewModel(HomeTrendingCategoryListViewModel transactionListViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(BlogViewModel.class)
    abstract ViewModel bindNewsFeedViewModel(BlogViewModel blogViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(BannerViewModel.class)
    abstract ViewModel bindNewsBannerViewModel(BannerViewModel bannerViewModel);

//    @Binds
//    @IntoMap
//    @ViewModelKey(PSAppInfoViewModel.class)
//    abstract ViewModel bindPSAppInfoViewModel(PSAppInfoViewModel psAppInfoViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ClearAllDataViewModel.class)
    abstract ViewModel bindClearAllDataViewModel(ClearAllDataViewModel clearAllDataViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(CityViewModel.class)
    abstract ViewModel bindCityViewModel(CityViewModel cityViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(com.mrii.prodes.viewmodel.item.ItemViewModel.class)
    abstract ViewModel bindItemViewModel(com.mrii.prodes.viewmodel.item.ItemViewModel itemViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(PopularItemViewModel.class)
    abstract ViewModel bindPopularItemViewModel(PopularItemViewModel popularItemViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(NewItemViewModel.class)
    abstract ViewModel bindNewItemViewModel(NewItemViewModel newItemViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(RecentItemViewModel.class)
    abstract ViewModel bindRecentItemViewModel(RecentItemViewModel recentItemViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(PSAPPLoadingViewModel.class)
    abstract ViewModel bindPSAPPLoadingViewModel(PSAPPLoadingViewModel psappLoadingViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(PopularCitiesViewModel.class)
    abstract ViewModel bindPopularCitiesViewModel(PopularCitiesViewModel popularCitiesViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(FeaturedCitiesViewModel.class)
    abstract ViewModel bindFeaturedCitiesViewModel(FeaturedCitiesViewModel featuredCitiesViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(RecentCitiesViewModel.class)
    abstract ViewModel bindRecentCitiesViewModel(RecentCitiesViewModel recentCitiesViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ItemSubCategoryViewModel.class)
    abstract ViewModel bindItemSubCategoryViewModel(ItemSubCategoryViewModel itemSubCategoryViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ChatViewModel.class)
    abstract ViewModel bindChatViewModel(ChatViewModel chatViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ChatHistoryViewModel.class)
    abstract ViewModel bindSellerViewModel(ChatHistoryViewModel chatHistoryViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(PSCountViewModel.class)
    abstract ViewModel bindPSCountViewModel(PSCountViewModel psCountViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(PaypalViewModel.class)
    abstract ViewModel bindPaypalViewModel(PaypalViewModel paypalViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ItemPaidHistoryViewModel.class)
    abstract ViewModel bindItemPaidHistoryViewModel(ItemPaidHistoryViewModel itemPaidHistoryViewModel);
}


