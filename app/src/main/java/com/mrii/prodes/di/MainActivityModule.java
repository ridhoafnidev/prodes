package com.mrii.prodes.di;


import com.mrii.prodes.MainActivity;
import com.mrii.prodes.ui.apploading.AppLoadingActivity;
import com.mrii.prodes.ui.apploading.AppLoadingFragment;
import com.mrii.prodes.ui.banner.BannerDetailActivity;
import com.mrii.prodes.ui.banner.BannerDetailFragment;
import com.mrii.prodes.ui.blog.detail.BlogDetailActivity;
import com.mrii.prodes.ui.blog.detail.BlogDetailFragment;
import com.mrii.prodes.ui.blog.list.BlogListActivity;
import com.mrii.prodes.ui.blog.list.BlogListFragment;
import com.mrii.prodes.ui.blog.list.BlogListMainFragment;
import com.mrii.prodes.ui.category.categoryfilter.CategoryFilterFragment;
import com.mrii.prodes.ui.category.categoryfilter.CategoryFilterHomeFragment;
import com.mrii.prodes.ui.category.list.CategoryListActivity;
import com.mrii.prodes.ui.category.list.CategoryListFragment;
import com.mrii.prodes.ui.chat.chat.ChatActivity;
import com.mrii.prodes.ui.chat.chat.ChatFragment;
import com.mrii.prodes.ui.chat.chatimage.ChatImageFullScreenActivity;
import com.mrii.prodes.ui.chat.chatimage.ChatImageFullScreenFragment;
import com.mrii.prodes.ui.chathistory.BuyerChatFragment;
import com.mrii.prodes.ui.chathistory.BuyerFragment;
import com.mrii.prodes.ui.chathistory.MessageFragment;
import com.mrii.prodes.ui.chathistory.SellerFragment;
import com.mrii.prodes.ui.city.menu.CityMenuFragment;
import com.mrii.prodes.ui.city.selectedcity.SelectedCityActivity;
import com.mrii.prodes.ui.city.selectedcity.SelectedCityFragment;
import com.mrii.prodes.ui.contactus.ContactUsFragment;
import com.mrii.prodes.ui.cs.CallCenterActivity;
import com.mrii.prodes.ui.cs.CallCenterFragment;
import com.mrii.prodes.ui.customcamera.CameraActivity;
import com.mrii.prodes.ui.customcamera.CameraFragment;
import com.mrii.prodes.ui.customcamera.setting.CameraSettingActivity;
import com.mrii.prodes.ui.customcamera.setting.CameraSettingFragment;
import com.mrii.prodes.ui.dashboard.DashBoardSearchCategoryFragment;
import com.mrii.prodes.ui.dashboard.DashBoardSearchFragment;
import com.mrii.prodes.ui.dashboard.DashBoardSearchSubCategoryFragment;
import com.mrii.prodes.ui.dashboard.DashboardSearchByCategoryActivity;
import com.mrii.prodes.ui.forceupdate.ForceUpdateActivity;
import com.mrii.prodes.ui.forceupdate.ForceUpdateFragment;
import com.mrii.prodes.ui.gallery.GalleryActivity;
import com.mrii.prodes.ui.gallery.GalleryFragment;
import com.mrii.prodes.ui.gallery.detail.GalleryDetailActivity;
import com.mrii.prodes.ui.gallery.detail.GalleryDetailFragment;
import com.mrii.prodes.ui.infocovid.InfoCovidListActivity;
import com.mrii.prodes.ui.infocovid.InfoCovidListFragment;
import com.mrii.prodes.ui.infocovid.detail.DetailInfoCovidActivity;
import com.mrii.prodes.ui.infocovid.detail.DetailInfoCovidFragment;
import com.mrii.prodes.ui.infodevelopment.InfoDevelopmentActivity;
import com.mrii.prodes.ui.item.detail.ItemActivity;
import com.mrii.prodes.ui.item.detail.ItemFragment;
import com.mrii.prodes.ui.item.detail.ItemRelatedDetailActivity;
import com.mrii.prodes.ui.item.detail.ItemRelatedDetailFragment;
import com.mrii.prodes.ui.item.edit.ItemEditActivity;
import com.mrii.prodes.ui.item.edit.ItemEditFragment;
import com.mrii.prodes.ui.item.entry.ItemEntryActivity;
import com.mrii.prodes.ui.item.entry.ItemEntryFragment;
import com.mrii.prodes.ui.item.favourite.FavouriteListActivity;
import com.mrii.prodes.ui.item.favourite.FavouriteListFragment;
import com.mrii.prodes.ui.item.history.HistoryFragment;
import com.mrii.prodes.ui.item.history.UserHistoryListActivity;
import com.mrii.prodes.ui.item.itemcondition.ItemConditionFragment;
import com.mrii.prodes.ui.item.itemcurrency.ItemCurrencyTypeFragment;
import com.mrii.prodes.ui.item.itemdealoption.ItemDealOptionTypeFragment;
import com.mrii.prodes.ui.item.itemfromfollower.ItemFromFollowerListActivity;
import com.mrii.prodes.ui.item.itemfromfollower.ItemFromFollowerListFragment;
import com.mrii.prodes.ui.item.itemlocation.ItemLocationFragment;
import com.mrii.prodes.ui.item.itempricetype.ItemPriceTypeFragment;
import com.mrii.prodes.ui.item.itemtype.ItemTypeFragment;
import com.mrii.prodes.ui.item.itemtype.SearchViewActivity;
import com.mrii.prodes.ui.item.loginUserItem.LoginUserItemFragment;
import com.mrii.prodes.ui.item.loginUserItem.LoginUserItemListActivity;
import com.mrii.prodes.ui.item.loginUserItem.LoginUserPaidItemFragment;
import com.mrii.prodes.ui.item.map.MapActivity;
import com.mrii.prodes.ui.item.map.MapFragment;
import com.mrii.prodes.ui.item.map.PickMapFragment;
import com.mrii.prodes.ui.item.map.mapFilter.MapFilteringActivity;
import com.mrii.prodes.ui.item.map.mapFilter.MapFilteringFragment;
import com.mrii.prodes.ui.item.promote.ItemPromoteActivity;
import com.mrii.prodes.ui.item.promote.ItemPromoteFragment;
import com.mrii.prodes.ui.item.rating.RatingListActivity;
import com.mrii.prodes.ui.item.rating.RatingListFragment;
import com.mrii.prodes.ui.item.readmore.ReadMoreActivity;
import com.mrii.prodes.ui.item.readmore.ReadMoreFragment;
import com.mrii.prodes.ui.item.search.searchlist.SearchListActivity;
import com.mrii.prodes.ui.item.search.searchlist.SearchListFragment;
import com.mrii.prodes.ui.item.search.specialfilterbyattributes.FilteringActivity;
import com.mrii.prodes.ui.item.search.specialfilterbyattributes.FilteringFragment;
import com.mrii.prodes.ui.item.searchlistCategory.SearchListCategoryActivity;
import com.mrii.prodes.ui.item.searchlistCategory.SearchListCategoryFragment;
import com.mrii.prodes.ui.language.LanguageFragment;
import com.mrii.prodes.ui.location.LocationActivity;
import com.mrii.prodes.ui.notification.detail.NotificationActivity;
import com.mrii.prodes.ui.notification.detail.NotificationFragment;
import com.mrii.prodes.ui.notification.list.NotificationListActivity;
import com.mrii.prodes.ui.notification.list.NotificationListFragment;
import com.mrii.prodes.ui.notification.setting.NotificationSettingActivity;
import com.mrii.prodes.ui.notification.setting.NotificationSettingFragment;
import com.mrii.prodes.ui.privacypolicy.PrivacyPolicyActivity;
import com.mrii.prodes.ui.privacypolicy.PrivacyPolicyFragment;
import com.mrii.prodes.ui.safetytip.SafetyTipFragment;
import com.mrii.prodes.ui.safetytip.SafetyTipsActivity;
import com.mrii.prodes.ui.setting.SettingActivity;
import com.mrii.prodes.ui.setting.SettingFragment;
import com.mrii.prodes.ui.setting.appinfo.AppInfoActivity;
import com.mrii.prodes.ui.setting.appinfo.AppInfoFragment;
import com.mrii.prodes.ui.stripe.StripeActivity;
import com.mrii.prodes.ui.stripe.StripeFragment;
import com.mrii.prodes.ui.subcategory.SubCategoryActivity;
import com.mrii.prodes.ui.subcategory.SubCategoryFragment;
import com.mrii.prodes.ui.user.PasswordChangeActivity;
import com.mrii.prodes.ui.user.PasswordChangeFragment;
import com.mrii.prodes.ui.user.ProfileEditActivity;
import com.mrii.prodes.ui.user.ProfileEditFragment;
import com.mrii.prodes.ui.user.ProfileFragment;
import com.mrii.prodes.ui.user.UserFBRegisterActivity;
import com.mrii.prodes.ui.user.UserFBRegisterFragment;
import com.mrii.prodes.ui.user.UserForgotPasswordActivity;
import com.mrii.prodes.ui.user.UserForgotPasswordFragment;
import com.mrii.prodes.ui.user.UserLoginActivity;
import com.mrii.prodes.ui.user.UserLoginFragment;
import com.mrii.prodes.ui.user.UserRegisterActivity;
import com.mrii.prodes.ui.user.UserRegisterFragment;
import com.mrii.prodes.ui.user.loginchat.UserLoginChatActivity;
import com.mrii.prodes.ui.user.loginchat.UserLoginChatFragment;
import com.mrii.prodes.ui.user.mapstest.MapsTestActivity;
import com.mrii.prodes.ui.user.mapstest.MapsTestFragment;
import com.mrii.prodes.ui.user.phonelogin.PhoneLoginActivity;
import com.mrii.prodes.ui.user.phonelogin.PhoneLoginFragment;
import com.mrii.prodes.ui.user.profilepembeli.ProfilePembeliFragment;
import com.mrii.prodes.ui.user.profilepenjual.ProfilePenjualEditActivity;
import com.mrii.prodes.ui.user.profilepenjual.ProfilePenjualEditFragment;
import com.mrii.prodes.ui.user.userlist.UserListActivity;
import com.mrii.prodes.ui.user.userlist.UserListFragment;
import com.mrii.prodes.ui.user.userlist.detail.UserDetailActivity;
import com.mrii.prodes.ui.user.userlist.detail.UserDetailFragment;
import com.mrii.prodes.ui.user.verifyemail.VerifyEmailActivity;
import com.mrii.prodes.ui.user.verifyemail.VerifyEmailFragment;
import com.mrii.prodes.ui.user.verifyphone.VerifyMobileActivity;
import com.mrii.prodes.ui.user.verifyphone.VerifyMobileFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

//import com.panaceasoft.psbuyandsell.ui.followinguser.FollowingUserActivity;
//import com.panaceasoft.psbuyandsell.ui.followinguser.FollowingUserFragment;
//import com.panaceasoft.psbuyandsell.ui.followinguser.detail.FollowingUserDetailActivity;
//import com.panaceasoft.psbuyandsell.ui.followinguser.detail.FollowingUserDetailFragment;

/**
 * Created by Panacea-Soft on 11/15/17.
 * Contact Email : teamps.is.cool@gmail.com
 */


@Module
abstract class MainActivityModule {

    @ContributesAndroidInjector(modules = MainModule.class)
    abstract MainActivity contributeMainActivity();

    @ContributesAndroidInjector(modules = FavouriteListModule.class)
    abstract FavouriteListActivity contributeFavouriteListActivity();

    @ContributesAndroidInjector(modules = UserHistoryModule.class)
    abstract UserHistoryListActivity contributeUserHistoryListActivity();

    @ContributesAndroidInjector(modules = InfoDevelopmentModule.class)
    abstract InfoDevelopmentActivity contributeInfoDevelopmentActivity();

    @ContributesAndroidInjector(modules = UserRegisterModule.class)
    abstract UserRegisterActivity contributeUserRegisterActivity();

    @ContributesAndroidInjector(modules = UserFBRegisterModule.class)
    abstract UserFBRegisterActivity contributeUserFBRegisterActivity();

    @ContributesAndroidInjector(modules = UserForgotPasswordModule.class)
    abstract UserForgotPasswordActivity contributeUserForgotPasswordActivity();

    @ContributesAndroidInjector(modules = UserLoginModule.class)
    abstract UserLoginActivity contributeUserLoginActivity();

    @ContributesAndroidInjector(modules = UserLoginChatModule.class)
    abstract UserLoginChatActivity contributeUserChatActivity();

   @ContributesAndroidInjector(modules = CallCenterModule.class)
    abstract CallCenterActivity contributeCallCenterActivity();

    @ContributesAndroidInjector(modules = PasswordChangeModule.class)
    abstract PasswordChangeActivity contributePasswordChangeActivity();

    @ContributesAndroidInjector(modules = FilteringModule.class)
    abstract FilteringActivity filteringActivity();

    @ContributesAndroidInjector(modules = SubCategoryActivityModule.class)
    abstract SubCategoryActivity subCategoryActivity();

    @ContributesAndroidInjector(modules = NotificationModule.class)
    abstract NotificationListActivity notificationActivity();

    @ContributesAndroidInjector(modules = CameraSettingActivityModule.class)
    abstract CameraSettingActivity cameraSettingActivity();

   @ContributesAndroidInjector(modules = PhoneLoginActivityModule.class)
    abstract PhoneLoginActivity contributePhoneLoginActivity();

    @ContributesAndroidInjector(modules = SearchActivityModule.class)
    abstract SearchListActivity contributeSearchListActivity();

    @ContributesAndroidInjector(modules = SearchListCategoryActivityModule.class)
    abstract SearchListCategoryActivity contributeSearchListCategoryActivity();

    @ContributesAndroidInjector(modules = CameraActivityModule.class)
    abstract CameraActivity contributeCameraActivity();

    @ContributesAndroidInjector(modules = ItemEntryActivityModule.class)
    abstract ItemEntryActivity contributeItemEntryActivity();

    @ContributesAndroidInjector(modules = MapsTestActivityModule.class)
    abstract MapsTestActivity contributeMapsTestActivity();

    @ContributesAndroidInjector(modules = ItemEditActivityModule.class)
    abstract ItemEditActivity contributeItemEditActivity();

    @ContributesAndroidInjector(modules = ItemPromoteEntryActivityModule.class)
    abstract ItemPromoteActivity contributeItemPromoteEntryActivity();

    @ContributesAndroidInjector(modules = NotificationDetailModule.class)
    abstract NotificationActivity notificationDetailActivity();

    @ContributesAndroidInjector(modules = ItemActivityModule.class)
    abstract ItemActivity itemActivity();

    @ContributesAndroidInjector(modules = ItemRelatedDetailActivityModule.class)
    abstract ItemRelatedDetailActivity itemRelatedActivity();

    @ContributesAndroidInjector(modules = SafetyTipsActivityModule.class)
    abstract SafetyTipsActivity safetyTipsActivity();

    @ContributesAndroidInjector(modules = GalleryDetailActivityModule.class)
    abstract GalleryDetailActivity galleryDetailActivity();

    @ContributesAndroidInjector(modules = GalleryActivityModule.class)
    abstract GalleryActivity galleryActivity();

    @ContributesAndroidInjector(modules = SearchByCategoryActivityModule.class)
    abstract DashboardSearchByCategoryActivity searchByCategoryActivity();

    @ContributesAndroidInjector(modules = readMoreActivityModule.class)
    abstract ReadMoreActivity readMoreActivity();

    @ContributesAndroidInjector(modules = EditSettingModule.class)
    abstract SettingActivity editSettingActivity();

    @ContributesAndroidInjector(modules = LanguageChangeModule.class)
    abstract NotificationSettingActivity languageChangeActivity();

    @ContributesAndroidInjector(modules = ProfileEditModule.class)
    abstract ProfileEditActivity contributeProfileEditActivity();

    @ContributesAndroidInjector(modules = ProfilePenjualEditModule.class)
    abstract ProfilePenjualEditActivity contributeProfilePenjualEditActivity();

    @ContributesAndroidInjector(modules = AppInfoModule.class)
    abstract AppInfoActivity AppInfoActivity();

    @ContributesAndroidInjector(modules = CategoryListActivityAppInfoModule.class)
    abstract CategoryListActivity categoryListActivity();

    @ContributesAndroidInjector(modules = RatingListActivityModule.class)
    abstract RatingListActivity ratingListActivity();

    @ContributesAndroidInjector(modules = SelectedCityModule.class)
    abstract SelectedCityActivity selectedShopActivity();

    @ContributesAndroidInjector(modules = SelectedShopListBlogModule.class)
    abstract BlogListActivity selectedShopListBlogActivity();

    @ContributesAndroidInjector(modules = SelectedInfoCovidListModule.class)
    abstract InfoCovidListActivity selectedInfoCovidListActivity();

    @ContributesAndroidInjector(modules = BlogDetailModule.class)
    abstract BlogDetailActivity blogDetailActivity();

    @ContributesAndroidInjector(modules = DetailInfoCovidlModule.class)
    abstract DetailInfoCovidActivity detailInfoCovidActivity();

    @ContributesAndroidInjector(modules = BannerDetailModule.class)
    abstract BannerDetailActivity bannerDetailActivity();

    @ContributesAndroidInjector(modules = MapActivityModule.class)
    abstract MapActivity mapActivity();

    @ContributesAndroidInjector(modules = forceUpdateModule.class)
    abstract ForceUpdateActivity forceUpdateActivity();

    @ContributesAndroidInjector(modules = MapFilteringModule.class)
    abstract MapFilteringActivity mapFilteringActivity();

    @ContributesAndroidInjector(modules = SearchViewActivityModule.class)
    abstract SearchViewActivity searchViewActivity();

    @ContributesAndroidInjector(modules = LoginUserItemListActivityModule.class)
    abstract LoginUserItemListActivity loginUserItemListActivity();

    @ContributesAndroidInjector(modules = chatActivityModule.class)
    abstract ChatActivity chatActivity();

    @ContributesAndroidInjector(modules = ImageFullScreenModule.class)
    abstract ChatImageFullScreenActivity imageFullScreenActivity();

//    @ContributesAndroidInjector(modules = LoginUserItemModule.class)
//    abstract LoginUserItemListActivity contributeLoginUserItemListActivity();

    @ContributesAndroidInjector(modules = FollowerUserModule.class)
    abstract UserListActivity contributeFollowerUserListActivity();

    @ContributesAndroidInjector(modules = VerifyEmailModule.class)
    abstract VerifyEmailActivity contributeVerifyEmailActivity();

    @ContributesAndroidInjector(modules = VerifyMobileModule.class)
    abstract VerifyMobileActivity contributeVerifyMobileActivity();

    @ContributesAndroidInjector(modules = FollowerUserDetailModule.class)
    abstract UserDetailActivity contributeFollowerUserDetailActivity();

    @ContributesAndroidInjector(modules = AppLoadingActivityModule.class)
    abstract AppLoadingActivity appLoadingActivity();

    @ContributesAndroidInjector(modules = ItemFromFollowerListModule.class)
    abstract ItemFromFollowerListActivity itemFromFollowerListActivity();

    @ContributesAndroidInjector(modules = LocationActivityModule.class)
    abstract LocationActivity locationActivity();

    @ContributesAndroidInjector(modules = PrivacyAndPolicyActivityModule.class)
    abstract PrivacyPolicyActivity privacyPolicyActivity();

    @ContributesAndroidInjector(modules = StripeModule.class)
    abstract StripeActivity stripeActivity();
}


@Module
abstract class MainModule {

    @ContributesAndroidInjector
    abstract ContactUsFragment contributeContactUsFragment();

    @ContributesAndroidInjector
    abstract VerifyMobileFragment contributeVerifyMobileFragment();

    @ContributesAndroidInjector
    abstract PhoneLoginFragment contributePhoneLoginFragment();

    @ContributesAndroidInjector
    abstract BuyerFragment contributeBuyerFragment();

    @ContributesAndroidInjector
    abstract SellerFragment contributeSellerFragment();

    @ContributesAndroidInjector
    abstract UserLoginFragment contributeUserLoginFragment();

    @ContributesAndroidInjector
    abstract CallCenterFragment contributeUCallCenterFragment();

    @ContributesAndroidInjector
    abstract UserForgotPasswordFragment contributeUserForgotPasswordFragment();

    @ContributesAndroidInjector
    abstract UserRegisterFragment contributeUserRegisterFragment();

    @ContributesAndroidInjector
    abstract UserFBRegisterFragment contributeUserFBRegisterFragment();

    @ContributesAndroidInjector
    abstract NotificationSettingFragment contributeNotificationSettingFragment();

    @ContributesAndroidInjector
    abstract ProfileFragment contributeProfileFragment();

    @ContributesAndroidInjector
    abstract ProfilePembeliFragment contributeProfilePembeliFragment();

    @ContributesAndroidInjector
    abstract LanguageFragment contributeLanguageFragment();

    @ContributesAndroidInjector
    abstract FavouriteListFragment contributeFavouriteListFragment();

    @ContributesAndroidInjector
    abstract LoginUserPaidItemFragment contributeLoginUserPaidItemFragment();

    @ContributesAndroidInjector
    abstract SettingFragment contributEditSettingFragment();

    @ContributesAndroidInjector
    abstract HistoryFragment historyFragment();

    @ContributesAndroidInjector
    abstract ItemEntryFragment itemEntryFragment();

    @ContributesAndroidInjector
    abstract ProfileEditFragment profileEditFragment();

    @ContributesAndroidInjector
    abstract NotificationListFragment contributeNotificationFragment();

    @ContributesAndroidInjector
    abstract AppInfoFragment contributeAppInfoFragment();

    @ContributesAndroidInjector
    abstract SelectedCityFragment contributeSelectedCityFragment();

    @ContributesAndroidInjector
    abstract SearchListFragment contributeSearchListFragment();

    @ContributesAndroidInjector
    abstract CategoryListFragment contributeCategoryListFragment();

    @ContributesAndroidInjector
    abstract BlogListFragment contributeBlogListFragment();

    @ContributesAndroidInjector
    abstract BlogListMainFragment contributeBlogListMainFragment();

    @ContributesAndroidInjector
    abstract MessageFragment contributeMessageFragment();

    @ContributesAndroidInjector
    abstract BuyerChatFragment contributeBuyerChatFragment();

    @ContributesAndroidInjector
    abstract DashBoardSearchFragment contributeDashBoardSearchFragment();

    @ContributesAndroidInjector
    abstract VerifyEmailFragment contributeVerifyEmailFragment();

    @ContributesAndroidInjector
    abstract PrivacyPolicyFragment contributePrivacyPolicyFragment();
}

@Module
abstract class ProfileEditModule {
    @ContributesAndroidInjector
    abstract ProfileEditFragment contributeProfileEditFragment();
}

@Module
abstract class ProfilePenjualEditModule {
    @ContributesAndroidInjector
    abstract ProfilePenjualEditFragment contributeProfilePenjualEditFragment();
}

@Module
abstract class UserFBRegisterModule {
    @ContributesAndroidInjector
    abstract UserFBRegisterFragment contributeUserFBRegisterFragment();
}

@Module
abstract class ItemActivityModule {
    @ContributesAndroidInjector
    abstract ItemFragment contributeItemFragment();
}

@Module
abstract class ItemRelatedDetailActivityModule {
    @ContributesAndroidInjector
    abstract ItemRelatedDetailFragment contributeItemFragment();
}

@Module
abstract class SafetyTipsActivityModule {
    @ContributesAndroidInjector
    abstract SafetyTipFragment contributeSafetyTipFragment();
}

@Module
abstract class FavouriteListModule {
    @ContributesAndroidInjector
    abstract FavouriteListFragment contributeFavouriteFragment();
}


@Module
abstract class UserRegisterModule {
    @ContributesAndroidInjector
    abstract UserRegisterFragment contributeUserRegisterFragment();
}

@Module
abstract class InfoDevelopmentModule {
    @ContributesAndroidInjector
    abstract UserRegisterFragment contributeUserRegisterFragment();
}

@Module
abstract class UserForgotPasswordModule {
    @ContributesAndroidInjector
    abstract UserForgotPasswordFragment contributeUserForgotPasswordFragment();
}

@Module
abstract class UserLoginModule {
    @ContributesAndroidInjector
    abstract UserLoginFragment contributeUserLoginFragment();
}

@Module
abstract class UserLoginChatModule {
    @ContributesAndroidInjector
    abstract UserLoginChatFragment contributeUserLoginChatFragment();
}

@Module
abstract class CallCenterModule {
    @ContributesAndroidInjector
    abstract CallCenterFragment contributeUserLoginFragment();
}

@Module
abstract class PasswordChangeModule {
    @ContributesAndroidInjector
    abstract PasswordChangeFragment contributePasswordChangeFragment();
}


@Module
abstract class NotificationModule {
    @ContributesAndroidInjector
    abstract NotificationListFragment notificationFragment();
}

@Module
abstract class CameraSettingActivityModule {
    @ContributesAndroidInjector
    abstract CameraSettingFragment cameraSettingFragment();
}

@Module
abstract class PhoneLoginActivityModule {
    @ContributesAndroidInjector
    abstract PhoneLoginFragment cameraPhoneLoginFragment();
}

@Module
abstract class NotificationDetailModule {
    @ContributesAndroidInjector
    abstract NotificationFragment notificationDetailFragment();
}

@Module
abstract class UserHistoryModule {
    @ContributesAndroidInjector
    abstract HistoryFragment contributeHistoryFragment();
}

@Module
abstract class AppInfoModule {
    @ContributesAndroidInjector
    abstract AppInfoFragment contributeAppInfoFragment();
}

@Module
abstract class CategoryListActivityAppInfoModule {
    @ContributesAndroidInjector
    abstract CategoryListFragment contributeCategoryFragment();

}

@Module
abstract class RatingListActivityModule {
    @ContributesAndroidInjector
    abstract RatingListFragment contributeRatingListFragment();
}

@Module
abstract class readMoreActivityModule {
    @ContributesAndroidInjector
    abstract ReadMoreFragment contributeReadMoreFragment();
}

@Module
abstract class EditSettingModule {
    @ContributesAndroidInjector
    abstract SettingFragment EditSettingFragment();
}

@Module
abstract class LanguageChangeModule {
    @ContributesAndroidInjector
    abstract NotificationSettingFragment notificationSettingFragment();
}

@Module
abstract class EditProfileModule {
    @ContributesAndroidInjector
    abstract ProfileFragment ProfileFragment();
}

@Module
abstract class EditProfilePembeliModule {
    @ContributesAndroidInjector
    abstract ProfilePembeliFragment ProfilePembeliFragment();
}

@Module
abstract class SubCategoryActivityModule {
    @ContributesAndroidInjector
    abstract SubCategoryFragment contributeSubCategoryFragment();

}

@Module
abstract class FilteringModule {

    @ContributesAndroidInjector
    abstract CategoryFilterHomeFragment contributeTypeFilterHomeFragment();

    @ContributesAndroidInjector
    abstract CategoryFilterFragment contributeTypeFilterFragment();

    @ContributesAndroidInjector
    abstract FilteringFragment contributeSpecialFilteringFragment();

}

@Module
abstract class SearchActivityModule {
    @ContributesAndroidInjector
    abstract SearchListFragment contributefeaturedProductFragment();

    @ContributesAndroidInjector
    abstract CategoryListFragment contributeCategoryFragment();

    @ContributesAndroidInjector
    abstract CategoryFilterFragment contributeTypeFilterFragment();

    @ContributesAndroidInjector
    abstract CategoryFilterHomeFragment contributeTypeFilterHomeFragment();
}

@Module
abstract class SearchListCategoryActivityModule {
    @ContributesAndroidInjector
    abstract SearchListCategoryFragment contributefeaturedSeachListCategoryFragment();

    @ContributesAndroidInjector
    abstract CategoryListFragment contributeCategoryFragment();

    @ContributesAndroidInjector
    abstract CategoryFilterFragment contributeTypeFilterFragment();

    @ContributesAndroidInjector
    abstract CategoryFilterHomeFragment contributeTypeFilterHomeFragment();
}

@Module
abstract class CameraActivityModule {
    @ContributesAndroidInjector
    abstract CameraFragment contributeCameraFragment();
}

@Module
abstract class ItemEntryActivityModule {
    @ContributesAndroidInjector
    abstract ItemEntryFragment contributeItemEntryFragment();
}

@Module
abstract class MapsTestActivityModule {
    @ContributesAndroidInjector
    abstract MapsTestFragment contributeMapsTestFragment();
}

@Module
abstract class ItemEditActivityModule {
    @ContributesAndroidInjector
    abstract ItemEditFragment contributeItemEditFragment();
}

@Module
abstract class ItemPromoteEntryActivityModule {
    @ContributesAndroidInjector
    abstract ItemPromoteFragment contributeItemPromoteFragment();
}

@Module
abstract class GalleryDetailActivityModule {
    @ContributesAndroidInjector
    abstract GalleryDetailFragment contributeGalleryDetailFragment();
}

@Module
abstract class GalleryActivityModule {
    @ContributesAndroidInjector
    abstract GalleryFragment contributeGalleryFragment();
}

@Module
abstract class SearchByCategoryActivityModule {
    @ContributesAndroidInjector
    abstract DashBoardSearchCategoryFragment contributeDashBoardSearchCategoryFragment();

    @ContributesAndroidInjector
    abstract DashBoardSearchSubCategoryFragment contributeDashBoardSearchSubCategoryFragment();
}

@Module
abstract class SelectedCityModule {
    @ContributesAndroidInjector
    abstract SearchListFragment contributefeaturedProductFragment();

    @ContributesAndroidInjector
    abstract CategoryListFragment categoryListFragment();

    @ContributesAndroidInjector
    abstract SelectedCityFragment contributeSelectedCityFragment();

    @ContributesAndroidInjector
    abstract CategoryFilterFragment contributeTypeFilterFragment();

    @ContributesAndroidInjector
    abstract CategoryFilterHomeFragment contributeTypeFilterHomeFragment();

    @ContributesAndroidInjector
    abstract CityMenuFragment contributeCityMenuFragment();

    @ContributesAndroidInjector
    abstract DashBoardSearchFragment contributeDashBoardSearchFragment();
}

@Module
abstract class SelectedShopListBlogModule {
    @ContributesAndroidInjector
    abstract BlogListFragment contributeSelectedShopListBlogFragment();
}

@Module
abstract class SelectedInfoCovidListModule {
    @ContributesAndroidInjector
    abstract InfoCovidListFragment contributeSelectedInfoCovidListFragment();
}

@Module
abstract class SelectedShopListBlogMainModule {
    @ContributesAndroidInjector
    abstract BlogListMainFragment contributeSelectedShopListBlogMainFragment();
}

@Module
abstract class BlogDetailModule {
    @ContributesAndroidInjector
    abstract BlogDetailFragment contributeBlogDetailFragment();
}

@Module
abstract class DetailInfoCovidlModule {
    @ContributesAndroidInjector
    abstract DetailInfoCovidFragment contributeDetailInfoCovidFragment();
}

@Module
abstract class BannerDetailModule {
    @ContributesAndroidInjector
    abstract BannerDetailFragment contributeBannerDetailFragment();
}

@Module
abstract class MapActivityModule {
    @ContributesAndroidInjector
    abstract MapFragment contributeMapFragment();

    @ContributesAndroidInjector
    abstract PickMapFragment contributePickMapFragment();
}

@Module
abstract class forceUpdateModule {
    @ContributesAndroidInjector
    abstract ForceUpdateFragment contributeForceUpdateFragment();
}

@Module
abstract class MapFilteringModule {

    @ContributesAndroidInjector
    abstract MapFilteringFragment contributeMapFilteringFragment();
}

@Module
abstract class SearchViewActivityModule {

    @ContributesAndroidInjector
    abstract ItemCurrencyTypeFragment contributeItemConditionTypeFragment();

    @ContributesAndroidInjector
    abstract ItemConditionFragment contributeItemConditionFragment();

    @ContributesAndroidInjector
    abstract ItemLocationFragment contributeItemLocationFragment();

    @ContributesAndroidInjector
    abstract ItemDealOptionTypeFragment contributeItemDealOptionTypeFragment();

    @ContributesAndroidInjector
    abstract ItemPriceTypeFragment contributeItemPriceTypeFragment();

    @ContributesAndroidInjector
    abstract ItemTypeFragment contributeItemTypeFragment();


}


@Module
abstract class LoginUserItemListActivityModule {

    @ContributesAndroidInjector
    abstract  LoginUserItemFragment contributeLoginUserItemFragment();

    @ContributesAndroidInjector
    abstract  LoginUserPaidItemFragment contributeLoginUserPaidItemFragment();

}

@Module
abstract class chatActivityModule {

    @ContributesAndroidInjector
    abstract ChatFragment contributeChatFragment();

    @ContributesAndroidInjector
    abstract BuyerChatFragment contributeBuyerChatFragment();
}

@Module
abstract class ImageFullScreenModule {

    @ContributesAndroidInjector
    abstract ChatImageFullScreenFragment contributeImageFullScreenFragment();

}

//@Module
//abstract class LoginUserItemModule {
//    @ContributesAndroidInjector
//    abstract LoginUserItemFragment contributeLoginUserItemFragment();
//}
//
//@Module
//abstract class LoginUserPaidItemModule {
//    @ContributesAndroidInjector
//    abstract LoginUserPaidItemFragment contributeLoginUserPaidItemFragment();
//}

@Module
abstract class FollowerUserModule {
    @ContributesAndroidInjector
    abstract UserListFragment contributeFollowerUserFragment();
}

@Module
abstract class VerifyEmailModule {
    @ContributesAndroidInjector
    abstract VerifyEmailFragment contributeVerifyEmailFragment();

}

@Module
abstract class VerifyMobileModule {
    @ContributesAndroidInjector
    abstract VerifyMobileFragment contributeVerifyMobileFragment();
}

@Module
abstract class FollowerUserDetailModule {
    @ContributesAndroidInjector
    abstract UserDetailFragment contributeFollowerUserDetailFragment();
}

@Module
abstract class AppLoadingActivityModule {

    @ContributesAndroidInjector
    abstract AppLoadingFragment contributeAppLoadingFragment();
}

@Module
abstract class ItemFromFollowerListModule {

    @ContributesAndroidInjector
    abstract ItemFromFollowerListFragment contributeItemFromFollowerListFragment();
}

@Module
abstract class LocationActivityModule {

    @ContributesAndroidInjector
    abstract ItemLocationFragment contributeItemLocationFragment();

}

@Module
abstract class PrivacyAndPolicyActivityModule {

    @ContributesAndroidInjector
    abstract PrivacyPolicyFragment contributePrivacyPolicyFragment();

}

@Module
abstract class StripeModule {

    @ContributesAndroidInjector
    abstract StripeFragment contributeStripeFragment();

}
