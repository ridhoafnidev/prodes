package com.mrii.prodes.ui.common;


import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.mrii.prodes.Config;
import com.mrii.prodes.MainActivity;
import com.mrii.prodes.R;
import com.mrii.prodes.UserSelectedType;
import com.mrii.prodes.ui.apploading.AppLoadingActivity;
import com.mrii.prodes.ui.banner.BannerDetailActivity;
import com.mrii.prodes.ui.blog.detail.BlogDetailActivity;
import com.mrii.prodes.ui.blog.list.BlogListActivity;
import com.mrii.prodes.ui.blog.list.BlogListFragment;
import com.mrii.prodes.ui.blog.list.BlogListMainFragment;
import com.mrii.prodes.ui.category.list.CategoryListActivity;
import com.mrii.prodes.ui.category.list.CategoryListFragment;
import com.mrii.prodes.ui.chat.chat.ChatActivity;
import com.mrii.prodes.ui.chat.chatimage.ChatImageFullScreenActivity;
import com.mrii.prodes.ui.chathistory.BuyerChatFragment;
import com.mrii.prodes.ui.chathistory.MessageFragment;
import com.mrii.prodes.ui.city.menu.CityMenuFragment;
import com.mrii.prodes.ui.city.selectedcity.SelectedCityActivity;
import com.mrii.prodes.ui.city.selectedcity.SelectedCityFragment;
import com.mrii.prodes.ui.contactus.ContactUsFragment;
import com.mrii.prodes.ui.cs.CallCenterFragment;
import com.mrii.prodes.ui.customcamera.CameraActivity;
import com.mrii.prodes.ui.customcamera.setting.CameraSettingActivity;
import com.mrii.prodes.ui.dashboard.DashBoardSearchFragment;
import com.mrii.prodes.ui.dashboard.DashboardSearchByCategoryActivity;
import com.mrii.prodes.ui.forceupdate.ForceUpdateActivity;
import com.mrii.prodes.ui.gallery.GalleryActivity;
import com.mrii.prodes.ui.gallery.detail.GalleryDetailActivity;
import com.mrii.prodes.ui.infocovid.InfoCovidListActivity;
import com.mrii.prodes.ui.infocovid.detail.DetailInfoCovidActivity;
import com.mrii.prodes.ui.infodevelopment.InfoDevelopmentActivity;
import com.mrii.prodes.ui.intro.IntroActivity;
import com.mrii.prodes.ui.item.detail.ItemActivity;
import com.mrii.prodes.ui.item.detail.ItemRelatedDetailActivity;
import com.mrii.prodes.ui.item.edit.ItemEditActivity;
import com.mrii.prodes.ui.item.entry.ItemEntryActivity;
import com.mrii.prodes.ui.item.entry.ItemEntryFragment;
import com.mrii.prodes.ui.item.favourite.FavouriteListActivity;
import com.mrii.prodes.ui.item.favourite.FavouriteListFragment;
import com.mrii.prodes.ui.item.history.HistoryFragment;
import com.mrii.prodes.ui.item.itemfromfollower.ItemFromFollowerListActivity;
import com.mrii.prodes.ui.item.itemtype.SearchViewActivity;
import com.mrii.prodes.ui.item.loginUserItem.LoginUserItemListActivity;
import com.mrii.prodes.ui.item.loginUserItem.LoginUserPaidItemFragment;
import com.mrii.prodes.ui.item.map.MapActivity;
import com.mrii.prodes.ui.item.map.mapFilter.MapFilteringActivity;
import com.mrii.prodes.ui.item.promote.ItemPromoteActivity;
import com.mrii.prodes.ui.item.rating.RatingListActivity;
import com.mrii.prodes.ui.item.search.searchlist.SearchListActivity;
import com.mrii.prodes.ui.item.search.searchlist.SearchListFragment;
import com.mrii.prodes.ui.item.search.specialfilterbyattributes.FilteringActivity;
import com.mrii.prodes.ui.item.searchlistCategory.SearchListCategoryActivity;
import com.mrii.prodes.ui.language.LanguageFragment;
import com.mrii.prodes.ui.location.LocationActivity;
import com.mrii.prodes.ui.notification.detail.NotificationActivity;
import com.mrii.prodes.ui.notification.list.NotificationListActivity;
import com.mrii.prodes.ui.notification.setting.NotificationSettingActivity;
import com.mrii.prodes.ui.privacypolicy.PrivacyPolicyActivity;
import com.mrii.prodes.ui.privacypolicy.PrivacyPolicyFragment;
import com.mrii.prodes.ui.safetytip.SafetyTipsActivity;
import com.mrii.prodes.ui.setting.SettingActivity;
import com.mrii.prodes.ui.setting.SettingFragment;
import com.mrii.prodes.ui.setting.appinfo.AppInfoActivity;
import com.mrii.prodes.ui.stripe.StripeActivity;
import com.mrii.prodes.ui.subcategory.SubCategoryActivity;
import com.mrii.prodes.ui.user.PasswordChangeActivity;
import com.mrii.prodes.ui.user.ProfileEditActivity;
import com.mrii.prodes.ui.user.ProfileFragment;
import com.mrii.prodes.ui.user.UserForgotPasswordActivity;
import com.mrii.prodes.ui.user.UserForgotPasswordFragment;
import com.mrii.prodes.ui.user.UserLoginActivity;
import com.mrii.prodes.ui.user.UserLoginFragment;
import com.mrii.prodes.ui.user.UserRegisterActivity;
import com.mrii.prodes.ui.user.UserRegisterFragment;
import com.mrii.prodes.ui.user.loginchat.UserLoginChatActivity;
import com.mrii.prodes.ui.user.mapstest.MapsTestActivity;
import com.mrii.prodes.ui.user.phonelogin.PhoneLoginActivity;
import com.mrii.prodes.ui.user.phonelogin.PhoneLoginFragment;
import com.mrii.prodes.ui.user.profilepembeli.ProfilePembeliFragment;
import com.mrii.prodes.ui.user.profilepenjual.ProfilePenjualEditActivity;
import com.mrii.prodes.ui.user.userlist.UserListActivity;
import com.mrii.prodes.ui.user.userlist.detail.UserDetailActivity;
import com.mrii.prodes.ui.user.verifyemail.VerifyEmailActivity;
import com.mrii.prodes.ui.user.verifyemail.VerifyEmailFragment;
import com.mrii.prodes.ui.user.verifyphone.VerifyMobileActivity;
import com.mrii.prodes.ui.user.verifyphone.VerifyMobileFragment;
import com.mrii.prodes.ui.user.verifyumkm.VerifyUmkmActivity;
import com.mrii.prodes.utils.Constants;
import com.mrii.prodes.utils.Utils;
import com.mrii.prodes.viewobject.Noti;
import com.mrii.prodes.viewobject.User;
import com.mrii.prodes.viewobject.holder.ItemParameterHolder;
import com.mrii.prodes.viewobject.holder.UserParameterHolder;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;

//import com.panaceasoft.psbuyandsell.ui.followinguser.FollowingUserActivity;
//import com.panaceasoft.psbuyandsell.ui.followinguser.detail.FollowingUserDetailActivity;

//import com.panaceasoft.psbuyandsell.ui.city.selectedCity.SelectedCityFragment;

/**
 * Created by Panacea-Soft on 11/17/17.
 * Contact Email : teamps.is.cool@gmail.com
 */

public class NavigationController {

    //region Variables

    private final int containerId;
    private RegFragments currentFragment;
    public Uri photoURI;

    //endregion


    //region Constructor
    @Inject
    public NavigationController() {

        // This setup is for MainActivity
        this.containerId = R.id.content_frame;
    }

    //endregion


    //region default navigation

    public void navigateToUserLogin(MainActivity mainActivity) {
        if (checkFragmentChange(RegFragments.HOME_USER_LOGIN)) {
            try {
                UserLoginFragment fragment = new UserLoginFragment();
                mainActivity.getSupportFragmentManager().beginTransaction()
                        .replace(containerId, fragment)
                        .commitAllowingStateLoss();
            } catch (Exception e) {
                Utils.psErrorLog("Error! Can't replace fragment.", e);
            }
        }
    }

    public void navigateToUserLogin(VerifyEmailActivity mainActivity) {
        if (checkFragmentChange(RegFragments.HOME_USER_LOGIN)) {
            try {
                UserLoginFragment fragment = new UserLoginFragment();
                mainActivity.getSupportFragmentManager().beginTransaction()
                        .replace(containerId, fragment)
                        .commitAllowingStateLoss();
            } catch (Exception e) {
                Utils.psErrorLog("Error! Can't replace fragment.", e);
            }
        }
    }

    public void navigateToVerifyEmail(MainActivity mainActivity) {
        if (checkFragmentChange(RegFragments.HOME_USER_EMAIL_VERIFY)) {
            try {
                VerifyEmailFragment fragment = new VerifyEmailFragment();
                mainActivity.getSupportFragmentManager().beginTransaction()
                        .replace(containerId, fragment)
                        .commitAllowingStateLoss();
            } catch (Exception e) {
                Utils.psErrorLog("Error! Can't replace fragment.", e);
            }
        }
    }


    public void navigateToCategoryFragment(SelectedCityActivity selectedCityActivity) {
        if (checkFragmentChange(RegFragments.HOME_CATEGORY)) {
            try {
                CategoryListFragment fragment = new CategoryListFragment();
                selectedCityActivity.getSupportFragmentManager().beginTransaction()
                        .replace(containerId, fragment)
                        .commitAllowingStateLoss();
            } catch (Exception e) {
                Utils.psErrorLog("Error! Can't replace fragment.", e);
            }
        }
    }

    public void navigateToHomeFragment(SelectedCityActivity selectedCityActivity) {
        if (checkFragmentChange(RegFragments.HOME_HOME)) {
            try {
                SelectedCityFragment fragment = new SelectedCityFragment();
                selectedCityActivity.getSupportFragmentManager().beginTransaction()
                        .replace(containerId, fragment)
                        .commitAllowingStateLoss();
            } catch (Exception e) {
                Utils.psErrorLog("Error! Can't replace fragment.", e);
            }
        }
    }

    public void navigateToFilteringFragment(FragmentActivity activity) {
        if (checkFragmentChange(RegFragments.HOME_FILTER)) {
            try {
                DashBoardSearchFragment fragment = new DashBoardSearchFragment();
                activity.getSupportFragmentManager().beginTransaction()
                        .replace(containerId, fragment)
                        .commitAllowingStateLoss();
            } catch (Exception e) {
                Utils.psErrorLog("Error! Can't replace fragment.", e);
            }
        }
    }

    public void navigateToUserProfileTest(MainActivity mainActivity, User user) {
        if (checkFragmentChange(RegFragments.HOME_USER_LOGIN)) {
            try {
                ProfileFragment fragment = new ProfileFragment();
                mainActivity.getSupportFragmentManager().beginTransaction()
                        .replace(containerId, fragment)
                        .commitAllowingStateLoss();
            } catch (Exception e) {
                Utils.psErrorLog("Error! Can't replace fragment.", e);
            }
        }
    }

    public void navigateToUserProfilePembeli(MainActivity mainActivity, User user) {
        if (checkFragmentChange(RegFragments.HOME_USER_LOGIN)) {
            try {
                ProfilePembeliFragment fragment = new ProfilePembeliFragment();
                mainActivity.getSupportFragmentManager().beginTransaction()
                        .replace(containerId, fragment)
                        .commitAllowingStateLoss();
            } catch (Exception e) {
                Utils.psErrorLog("Error! Can't replace fragment.", e);
            }
        }
    }

    public void navigateToUserProfile(MainActivity mainActivity) {
        if (checkFragmentChange(RegFragments.HOME_USER_LOGIN)) {
            try {
                ProfileFragment fragment = new ProfileFragment();
                mainActivity.getSupportFragmentManager().beginTransaction()
                        .replace(containerId, fragment)
                        .commitAllowingStateLoss();
            } catch (Exception e) {
                Utils.psErrorLog("Error! Can't replace fragment.", e);
            }
        }
    }

    public void navigateToUserUpdateProfile(Activity activity) {
        Intent intent = new Intent(activity, ProfileEditActivity.class);
        activity.startActivity(intent);
    }


    public void navigateToFavourite(MainActivity mainActivity) {
        if (checkFragmentChange(RegFragments.HOME_FAVOURITE)) {
            try {
                FavouriteListFragment fragment = new FavouriteListFragment();
                mainActivity.getSupportFragmentManager().beginTransaction()
                        .replace(containerId, fragment)
                        .commitAllowingStateLoss();
            } catch (Exception e) {
                Utils.psErrorLog("Error! Can't replace fragment.", e);
            }
        }
    }

    public void navigateToTransactions(MainActivity mainActivity) {
        if (checkFragmentChange(RegFragments.HOME_TRANSACTION)) {
            try {
                LoginUserPaidItemFragment fragment = new LoginUserPaidItemFragment();
                mainActivity.getSupportFragmentManager().beginTransaction()
                        .replace(containerId, fragment)
                        .commitAllowingStateLoss();
            } catch (Exception e) {
                Utils.psErrorLog("Error! Can't replace fragment.", e);
            }
        }
    }


    public void navigateToCityMenu(SelectedCityActivity mainActivity) {
        if (checkFragmentChange(RegFragments.HOME_CITY_MENU)) {
            try {
                CityMenuFragment fragment = new CityMenuFragment();
                mainActivity.getSupportFragmentManager().beginTransaction()
                        .replace(containerId, fragment)
                        .commitAllowingStateLoss();
            } catch (Exception e) {
                Utils.psErrorLog("Error! Can't replace fragment.", e);
            }
        }
    }

    //    public void navigateToTransaction(MainActivity mainActivity) {
//        if (checkFragmentChange(RegFragments.HOME_TRANSACTION)) {
//            try {
//                TransactionListFragment fragment = new TransactionListFragment();
//                mainActivity.getSupportFragmentManager().beginTransaction()
//                        .replace(containerId, fragment)
//                        .commitAllowingStateLoss();
//            } catch (Exception e) {
//                Utils.psErrorLog("Error! Can't replace fragment.", e);
//            }
//        }
//    }
    public void navigateToContactUs(MainActivity mainActivity) {
        if (checkFragmentChange(RegFragments.HOME_CONTACTUS)) {
            try {
                ContactUsFragment fragment = new ContactUsFragment();
                mainActivity.getSupportFragmentManager().beginTransaction()
                        .replace(containerId, fragment)
                        .commitAllowingStateLoss();
            } catch (Exception e) {
                Utils.psErrorLog("Error! Can't replace fragment.", e);
            }
        }
    }

    public void navigateToPrivacyPolicy(MainActivity mainActivity) {
        if (checkFragmentChange(RegFragments.HOME_PRIVACY_POLICY
        )) {
            try {
                PrivacyPolicyFragment fragment = new PrivacyPolicyFragment();
                mainActivity.getSupportFragmentManager().beginTransaction()
                        .replace(containerId, fragment)
                        .commitAllowingStateLoss();
            } catch (Exception e) {
                Utils.psErrorLog("Error! Can't replace fragment.", e);
            }
        }
    }


    public void navigateToHistory(MainActivity mainActivity) {
        if (checkFragmentChange(RegFragments.HOME_HISTORY)) {
            try {
                HistoryFragment fragment = new HistoryFragment();
                mainActivity.getSupportFragmentManager().beginTransaction()
                        .replace(containerId, fragment)
                        .commitAllowingStateLoss();
            } catch (Exception e) {
                Utils.psErrorLog("Error! Can't replace fragment.", e);
            }
        }
    }


    public void navigateToUserRegister(MainActivity mainActivity) {
        if (checkFragmentChange(RegFragments.HOME_USER_REGISTER)) {
            try {
                UserRegisterFragment fragment = new UserRegisterFragment();
                mainActivity.getSupportFragmentManager().beginTransaction()
                        .replace(containerId, fragment)
                        .commitAllowingStateLoss();
            } catch (Exception e) {
                Utils.psErrorLog("Error! Can't replace fragment.", e);
            }
        }
    }

    public void navigateToPhoneLoginFragment(MainActivity mainActivity) {
        if (checkFragmentChange(RegFragments.HOME_PHONE_LOGIN)) {
            try {
                PhoneLoginFragment fragment = new PhoneLoginFragment();
                mainActivity.getSupportFragmentManager().beginTransaction()
                        .replace(containerId, fragment)
                        .commitAllowingStateLoss();
            } catch (Exception e) {
                Utils.psErrorLog("Error! Can't replace fragment.", e);
            }
        }
    }


    public void navigateToPhoneVerifyFragment(MainActivity mainActivity, String number) {
        if (checkFragmentChange(RegFragments.HOME_PHONE_VERIFY)) {
            try {
                VerifyMobileFragment fragment = new VerifyMobileFragment();
                mainActivity.getSupportFragmentManager().beginTransaction()
                        .replace(containerId, fragment)
                        .commitAllowingStateLoss();

                Bundle args = new Bundle();
                args.putString(Constants.USER_PHONE, number);
                //args.putString(Constants.USER_NAME, userName);
                fragment.setArguments(args);
            } catch (Exception e) {
                Utils.psErrorLog("Error! Can't replace fragment.", e);
            }
        }
    }

    public void navigateToUserForgotPassword(MainActivity mainActivity) {
        if (checkFragmentChange(RegFragments.HOME_USER_FOGOT_PASSWORD)) {
            try {
                UserForgotPasswordFragment fragment = new UserForgotPasswordFragment();
                mainActivity.getSupportFragmentManager().beginTransaction()
                        .replace(containerId, fragment)
                        .commitAllowingStateLoss();
            } catch (Exception e) {
                Utils.psErrorLog("Error! Can't replace fragment.", e);
            }
        }
    }

    public void navigateToSetting(MainActivity mainActivity) {
        if (checkFragmentChange(RegFragments.HOME_SETTING)) {
            try {
                SettingFragment fragment = new SettingFragment();
                mainActivity.getSupportFragmentManager().beginTransaction()
                        .replace(containerId, fragment)
                        .commitAllowingStateLoss();
            } catch (Exception e) {
                Utils.psErrorLog("Error! Can't replace fragment.", e);
            }
        }
    }


//    public void navigateToLanguageSetting(MainActivity mainActivity) {
//        if (checkFragmentChange(RegFragments.HOME_LANGUAGE_SETTING)) {
//            try {
//                LanguageFragment fragment = new LanguageFragment();
//                mainActivity.getSupportFragmentManager().beginTransaction()
//                        .replace(containerId, fragment)
//                        .commitAllowingStateLoss();
//            } catch (Exception e) {
//                Utils.psErrorLog("Error! Can't replace fragment.", e);
//            }
//        }
//    }

    public void navigateToHome(MainActivity mainActivity, boolean forceReplace, String address, String address_detail, String locationId, String locationName, boolean afterLogout) {
        if (checkFragmentChange(RegFragments.HOME_HOME) || forceReplace || afterLogout) {
            try {
                SelectedCityFragment fragment = new SelectedCityFragment();
                mainActivity.getSupportFragmentManager().beginTransaction()
                        .replace(containerId, fragment)
                        .commitAllowingStateLoss();

                Bundle args = new Bundle();
                args.putString(Constants.SELECTED_LOCATION_ID, locationId);
                args.putString(Constants.SELECTED_LOCATION_NAME, locationName);
                args.putString(Constants.USER_ADDRESS, address);
                args.putString(Constants.USER_DETAIL_ADDRESS, address_detail);
                fragment.setArguments(args);

            } catch (Exception e) {
                Utils.psErrorLog("Error! Can't replace fragment.", e);
            }
        }
    }


    public void navigateToMessage(MainActivity mainActivity) {
        if (checkFragmentChange(RegFragments.HOME_MESSAGE)) {
            try {
                MessageFragment fragment = new MessageFragment();
                mainActivity.getSupportFragmentManager().beginTransaction()
                        .replace(containerId, fragment)
                        .commitAllowingStateLoss();
            } catch (Exception e) {
                Utils.psErrorLog("Error! Can't replace fragment.", e);
            }
        }
    }

    public void navigateToBuyerMessage(MainActivity mainActivity) {
        if (checkFragmentChange(RegFragments.HOME_MESSAGE)) {
            try {
                BuyerChatFragment fragment = new BuyerChatFragment();
                mainActivity.getSupportFragmentManager().beginTransaction()
                        .replace(containerId, fragment)
                        .commitAllowingStateLoss();
            } catch (Exception e) {
                Utils.psErrorLog("Error! Can't replace fragment.", e);
            }
        }
    }

    public void navigateToInterest(MainActivity mainActivity) {
        if (checkFragmentChange(RegFragments.HOME_BLOG)) {
            try {
                BlogListMainFragment fragment = new BlogListMainFragment();
                mainActivity.getSupportFragmentManager().beginTransaction()
                        .replace(containerId, fragment)
                        .commitAllowingStateLoss();
            } catch (Exception e) {
                Utils.psErrorLog("Error! Can't replace fragment.", e);
            }
        }
    }

    public void navigateToPronews(MainActivity mainActivity) {
        if (checkFragmentChange(RegFragments.HOME_BLOG)) {
            try {
                BlogListMainFragment fragment = new BlogListMainFragment();
                mainActivity.getSupportFragmentManager().beginTransaction()
                        .replace(containerId, fragment)
                        .commitAllowingStateLoss();
            } catch (Exception e) {
                Utils.psErrorLog("Error! Can't replace fragment.", e);
            }
        }
    }

   public void navigateToCallCenter(MainActivity mainActivity) {
        if (checkFragmentChange(RegFragments.HOME_CS)) {
            try {
                CallCenterFragment fragment = new CallCenterFragment();
                mainActivity.getSupportFragmentManager().beginTransaction()
                        .replace(containerId, fragment)
                        .commitAllowingStateLoss();
            } catch (Exception e) {
                Utils.psErrorLog("Error! Can't replace fragment.", e);
            }
        }
    }


    public void navigateToFilter(MainActivity mainActivity) {
        if (checkFragmentChange(RegFragments.HOME_FILTER)) {
            try {
                DashBoardSearchFragment fragment = new DashBoardSearchFragment();
                mainActivity.getSupportFragmentManager().beginTransaction()
                        .replace(containerId, fragment)
                        .commitAllowingStateLoss();
            } catch (Exception e) {
                Utils.psErrorLog("Error! Can't replace fragment.", e);
            }
        }
    }

    public void navigateToCityList(MainActivity mainActivity) {
        if (checkFragmentChange(RegFragments.HOME_CITY_LIST)) {
            try {
                SelectedCityFragment fragment = new SelectedCityFragment();
                mainActivity.getSupportFragmentManager().beginTransaction()
                        .replace(containerId, fragment)
                        .commitAllowingStateLoss();
            } catch (Exception e) {
                Utils.psErrorLog("Error! Can't replace fragment.", e);
            }
        }
    }

    public void navigateToGalleryActivity(Activity activity, String imgType, String imgParentId) {
        Intent intent = new Intent(activity, GalleryActivity.class);

        if (!imgType.equals("")) {
            intent.putExtra(Constants.IMAGE_TYPE, imgType);
        }

        if (!imgParentId.equals("")) {
            intent.putExtra(Constants.IMAGE_PARENT_ID, imgParentId);
        }

        activity.startActivity(intent);

    }

    public void navigateToDetailGalleryActivity(Activity activity, String imgType, String newsId, String imgId) {
        Intent intent = new Intent(activity, GalleryDetailActivity.class);

        if (!imgType.equals("")) {
            intent.putExtra(Constants.IMAGE_TYPE, imgType);
        }

        if (!newsId.equals("")) {
            intent.putExtra(Constants.ITEM_ID, newsId);
        }

        if (!imgId.equals("")) {
            intent.putExtra(Constants.IMAGE_ID, imgId);
        }

        activity.startActivity(intent);

    }

    public void navigateToCamera(Activity activity, String flag) {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = Utils.createImageFile(activity);
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, System.currentTimeMillis() + ".jpg");
                values.put(MediaStore.Images.Media.MIME_TYPE, "image/*");
//                photoURI = FileProvider.getUriForFile(activity, activity.getPackageName() + Config.AUTHORITYFILE, photoFile);
                photoURI = activity.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

//                // Authority name
//                String name  = activity.getPackageName()+Config.AUTHORITYFILE;
//                Utils.psLog("*********************   "+name);

                switch (flag) {
                    case Constants.ONE:    //case Constants.ONE:
                        activity.startActivityForResult(takePictureIntent, Constants.REQUEST_CODE__FIRST_CAMERA);
                        break;
                    case Constants.TWO:    //case Constants.TWO:
                        activity.startActivityForResult(takePictureIntent, Constants.REQUEST_CODE__SEC_CAMERA);
                        break;
                    case Constants.THREE: //case Constants.THREE:
                        activity.startActivityForResult(takePictureIntent, Constants.REQUEST_CODE__THIRD_CAMERA);
                        break;
                    case Constants.FOUR: //case Constants.FOUR:
                        activity.startActivityForResult(takePictureIntent, Constants.REQUEST_CODE__FOURTH_CAMERA);
                        break;
                    case Constants.FIVE: //case Constants.FIVE:
                        activity.startActivityForResult(takePictureIntent, Constants.REQUEST_CODE__FIFTH_CAMERA);
                        break;
                }

            }
        }
    }

    public void navigateToCustomCamera(Activity activity, String flag) {
        Intent intent = new Intent(activity, CameraActivity.class);
        switch (flag) {
            case Constants.ONE:
                activity.startActivityForResult(intent, Constants.REQUEST_CODE__FIRST_CUSTOM_CAMERA);
                break;
            case Constants.TWO:
                activity.startActivityForResult(intent, Constants.REQUEST_CODE__SEC_CUSTOM_CAMERA);
                break;
            case Constants.THREE:
                activity.startActivityForResult(intent, Constants.REQUEST_CODE__THIRD_CUSTOM_CAMERA);
                break;
            case Constants.FOUR:
                activity.startActivityForResult(intent, Constants.REQUEST_CODE__FOURTH_CUSTOM_CAMERA);
                break;
            case Constants.FIVE:
                activity.startActivityForResult(intent, Constants.REQUEST_CODE__FIFTH_CUSTOM_CAMERA);
                break;
        }
    }

    public void navigateToGallery(Activity activity, String flag) {
//        Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        Intent pickPhoto = new Intent();
        pickPhoto.setType("image/*");
        pickPhoto.setAction(Intent.ACTION_OPEN_DOCUMENT);
        pickPhoto.addCategory(Intent.CATEGORY_OPENABLE);

        switch (flag) {
            case Constants.ONE:
                activity.startActivityForResult(pickPhoto, Constants.REQUEST_CODE__FIRST_GALLERY);
                break;
            case Constants.TWO:
                activity.startActivityForResult(pickPhoto, Constants.REQUEST_CODE__SEC_GALLERY);
                break;
            case Constants.THREE:
                activity.startActivityForResult(pickPhoto, Constants.REQUEST_CODE__THIRD_GALLERY);
                break;
            case Constants.FOUR:
                activity.startActivityForResult(pickPhoto, Constants.REQUEST_CODE__FOURTH_GALLERY);
                break;
            case Constants.FIVE:
                activity.startActivityForResult(pickPhoto, Constants.REQUEST_CODE__FIFTH_GALLERY);
                break;
        }
    }

    public void navigateToSettingActivity(Activity activity) {
        Intent intent = new Intent(activity, SettingActivity.class);
        activity.startActivityForResult(intent, Constants.REQUEST_CODE__PROFILE_FRAGMENT);
    }

    public void navigateToNotificationSettingActivity(Activity activity) {
        Intent intent = new Intent(activity, NotificationSettingActivity.class);
        activity.startActivity(intent);
    }

    public void navigateToCameraSettingActivity(Activity activity) {
        Intent intent = new Intent(activity, CameraSettingActivity.class);
        activity.startActivity(intent);
    }

    public void navigateToAppInfoActivity(Activity activity) {
        Intent intent = new Intent(activity, AppInfoActivity.class);
        activity.startActivity(intent);
    }

    public void navigateToProfileEditActivity(Activity activity) {
        Intent intent = new Intent(activity, ProfileEditActivity.class);
        intent.putExtra("PROFILE_PEMBELI", "profile_pembeli_kedua");
        activity.startActivity(intent);
    }

    public void navigateToProfilePenjualEditActivity(Activity activity) {
        Intent intent = new Intent(activity, ProfilePenjualEditActivity.class);
        intent.putExtra("PROFILE_PENJUAL", "profile_penjual_kedua");
        activity.startActivity(intent);
    }

    public void navigateToMapsTestActivity(Activity activity) {
        Intent intent = new Intent(activity, MapsTestActivity.class);
        activity.startActivity(intent);
    }

    public void navigateToUserLoginActivity(Activity activity) {
        Intent intent = new Intent(activity, UserLoginActivity.class);
        activity.startActivity(intent);
    }

    public void navigateToUserLoginChatActivity(Activity activity) {
        Intent intent = new Intent(activity, UserLoginChatActivity.class);
        activity.startActivity(intent);
    }

    public void navigateToVerifyEmailActivity(Activity activity) {
        Intent intent = new Intent(activity, VerifyEmailActivity.class);
        activity.startActivity(intent);
    }

    public void navigateToPhoneLoginActivity(Activity activity) {
        Intent intent = new Intent(activity, PhoneLoginActivity.class);
        activity.startActivity(intent);
    }

    public void navigateToPhoneVerifyActivity(Activity activity,String number) {
        Intent intent = new Intent(activity, VerifyMobileActivity.class);
        intent.putExtra(Constants.USER_PHONE, number);
        activity.startActivity(intent);
    }

    public void navigateToUserRegisterActivity(Activity activity) {
        Intent intent = new Intent(activity, UserRegisterActivity.class);
        activity.startActivity(intent);
    }

    public void navigateToUserForgotPasswordActivity(Activity activity) {
        Intent intent = new Intent(activity, UserForgotPasswordActivity.class);
        activity.startActivity(intent);
    }

    public void navigateToPasswordChangeActivity(Activity activity) {
        Intent intent = new Intent(activity, PasswordChangeActivity.class);
        activity.startActivity(intent);
    }

    public void navigateToNotificationList(Activity activity) {
        Intent intent = new Intent(activity, NotificationListActivity.class);
        activity.startActivity(intent);
    }

    public void navigateToPrivacyPolicyActivity(Activity activity) {
        Intent intent = new Intent(activity, PrivacyPolicyActivity.class);
        activity.startActivity(intent);
    }

    public void navigateToSafetyTipsActivity(Activity activity) {
        Intent intent = new Intent(activity, SafetyTipsActivity.class);
        activity.startActivity(intent);
    }

    public void navigateToRatingList(Activity activity, String userId) {
        Intent intent = new Intent(activity, RatingListActivity.class);
        intent.putExtra(Constants.ITEM_USER_ID, userId);
//        intent.putExtra(Constants.ITEM_ID, item.id);
        activity.startActivity(intent);
    }


    public void navigateToNotificationDetail(Activity activity, Noti noti, String token) {
        Intent intent = new Intent(activity, NotificationActivity.class);
        intent.putExtra(Constants.NOTI_ID, noti.id);
        intent.putExtra(Constants.NOTI_TOKEN, token);
        activity.startActivityForResult(intent, Constants.REQUEST_CODE__NOTIFICATION_LIST_FRAGMENT);
    }

    public void navigateToItemListActivity(Activity activity, String userId, String flagPaidOrNot, String status, String title) {
        Intent intent = new Intent(activity, LoginUserItemListActivity.class);
        intent.putExtra(Constants.USER_ID, userId);
        intent.putExtra(Constants.FLAGPAIDORNOT, flagPaidOrNot);
        intent.putExtra(Constants.STATUS, status);
        intent.putExtra(Constants.ITEM_LIST_TITLE, title);
        activity.startActivity(intent);
    }

    public void navigateToUserListActivity(Activity activity, UserParameterHolder userParameterHolder) {
        Intent intent = new Intent(activity, UserListActivity.class);
        intent.putExtra(Constants.USER_PARAM_HOLDER_KEY, userParameterHolder);
        activity.startActivity(intent);
    }

    public void navigateToFavouriteActivity(Activity activity) {
        Intent intent = new Intent(activity, FavouriteListActivity.class);
        activity.startActivity(intent);
    }

    public void navigateToCategoryActivity(Activity activity) {
        Intent intent = new Intent(activity, CategoryListActivity.class);
        activity.startActivity(intent);
    }

    public void navigateToItemEntryActivity(Activity activity, String itemId, String locationId, String locationName) {
        Intent intent = new Intent(activity, ItemEntryActivity.class);
        intent.putExtra(Constants.ITEM_ID, itemId);
        intent.putExtra(Constants.SELECTED_LOCATION_ID, locationId);
        intent.putExtra(Constants.SELECTED_LOCATION_NAME, locationName);

        activity.startActivity(intent);
    }

    public void navigateToItemEditActivity(Activity activity, String itemId, String locationId, String locationName) {
        Intent intent = new Intent(activity, ItemEditActivity.class);
        intent.putExtra(Constants.ITEM_ID, itemId);
        intent.putExtra(Constants.SELECTED_LOCATION_ID, locationId);
        intent.putExtra(Constants.SELECTED_LOCATION_NAME, locationName);

        activity.startActivity(intent);
    }

    public void navigateToItemEntryFragment(MainActivity activity, String itemId, String locationId, String locationName) {
//        Intent intent = new Intent(activity, ItemEntryActivity.class);
//        intent.putExtra(Constants.ITEM_ID, itemId);
//        intent.putExtra(Constants.SELECTED_LOCATION_ID, locationId);
//        intent.putExtra(Constants.SELECTED_LOCATION_NAME, locationName);
//
//        activity.startActivity(intent);

        if (checkFragmentChange(RegFragments.HOME_ADD_ITEM)) {
            try {
                ItemEntryFragment fragment = new ItemEntryFragment();
                activity.getSupportFragmentManager().beginTransaction()
                        .replace(containerId, fragment)
                        .commitAllowingStateLoss();
            } catch (Exception e) {
                Utils.psErrorLog("Error! Can't replace fragment.", e);
            }
        }

    }

    public void navigateToMapsTestFragment(MainActivity activity, String itemId, String locationId, String locationName) {
//        Intent intent = new Intent(activity, ItemEntryActivity.class);
//        intent.putExtra(Constants.ITEM_ID, itemId);
//        intent.putExtra(Constants.SELECTED_LOCATION_ID, locationId);
//        intent.putExtra(Constants.SELECTED_LOCATION_NAME, locationName);
//
//        activity.startActivity(intent);

        if (checkFragmentChange(RegFragments.HOME_ADD_ITEM)) {
            try {
                ItemEntryFragment fragment = new ItemEntryFragment();
                activity.getSupportFragmentManager().beginTransaction()
                        .replace(containerId, fragment)
                        .commitAllowingStateLoss();
            } catch (Exception e) {
                Utils.psErrorLog("Error! Can't replace fragment.", e);
            }
        }

    }

    public void navigateToInfoDevelopmentActivity(Activity activity, String title) {
        Intent intent = new Intent(activity, InfoDevelopmentActivity.class);
        intent.putExtra(Constants.NAME_MENU_DEVELOPMENT, title);
        activity.startActivity(intent);
    }

    public void navigateToSubCategoryActivity(Activity activity, String catId, String catName) {
        Intent intent = new Intent(activity, SubCategoryActivity.class);
        intent.putExtra(Constants.CATEGORY_ID, catId);
        intent.putExtra(Constants.CATEGORY_NAME, catName);
        activity.startActivity(intent);
    }

    public void navigateToMapActivity(Activity activity, String LNG, String LAT, String flag) {
        Intent intent = new Intent(activity, MapActivity.class);
        intent.putExtra(Constants.LNG, LNG);
        intent.putExtra(Constants.LAT, LAT);
        intent.putExtra(Constants.MAP_FLAG, flag);
        activity.startActivityForResult(intent, Constants.RESULT_CODE__TO_MAP_VIEW); // requestCode
    }

    public void navigateBackFromMapView(Activity activity, String lat, String lng, String address) {
        Intent intent = new Intent();
        intent.putExtra(Constants.LAT, lat);
        intent.putExtra(Constants.LNG, lng);
        intent.putExtra(Constants.USER_ADDRESS, address);

        activity.setResult(Constants.RESULT_CODE__FROM_MAP_VIEW, intent); // resultCode
    }

    public void navigateToTypeFilterCatFragment(FragmentActivity mainActivity, String
            catId, String subCatId, ItemParameterHolder itemParameterHolder, String name) {

        if (name.equals(Constants.FILTERING_CATEGORY_FILTER)) {
            Toast.makeText(mainActivity, "Test", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(mainActivity, FilteringActivity.class);

            intent.putExtra(Constants.CATEGORY_ID, catId);
            if (subCatId == null || subCatId.equals("")) {
                subCatId = Constants.ZERO;
            }

            intent.putExtra(Constants.SUBCATEGORY_ID, subCatId);
            intent.putExtra(Constants.FILTERING_FILTER_NAME, name);

            mainActivity.startActivity(intent);
        }

    }

    public void navigateToTypeFilterFragment(FragmentActivity mainActivity, String
            catId, String subCatId, ItemParameterHolder itemParameterHolder, String name) {

        if (name.equals(Constants.FILTERING_TYPE_FILTER)) {
            Intent intent = new Intent(mainActivity, FilteringActivity.class);

            intent.putExtra(Constants.CATEGORY_ID, catId);
            if (subCatId == null || subCatId.equals("")) {
                subCatId = Constants.ZERO;
            }
            intent.putExtra(Constants.SUBCATEGORY_ID, subCatId);
            intent.putExtra(Constants.FILTERING_FILTER_NAME, name);

            mainActivity.startActivityForResult(intent, Constants.REQUEST_CODE__ITEM_LIST_FRAGMENT);
        }

        else if (name.equals(Constants.FILTERING_SPECIAL_FILTER)) {
            Intent intent = new Intent(mainActivity, FilteringActivity.class);

            intent.putExtra(Constants.FILTERING_HOLDER, itemParameterHolder);
            intent.putExtra(Constants.FILTERING_FILTER_NAME, name);

            mainActivity.startActivityForResult(intent, Constants.REQUEST_CODE__ITEM_LIST_FRAGMENT);
        }

        else if (name.equals(Constants.FILTERING_CATEGORY_FILTER)) {
            Intent intent = new Intent(mainActivity, FilteringActivity.class);

            intent.putExtra(Constants.CATEGORY_ID, catId);
            if (subCatId == null || subCatId.equals("")) {
                subCatId = Constants.ZERO;
            }

            intent.putExtra(Constants.SUBCATEGORY_ID, subCatId);
            intent.putExtra(Constants.FILTERING_FILTER_NAME, name);

            mainActivity.startActivityForResult(intent, Constants.REQUEST_CODE__ITEM_LIST_FRAGMENT);
        }

    }

    public void navigateToTypeFilterCategoryFragment(FragmentActivity mainActivity, String
            catId, String subCatId) {

            Intent intent = new Intent(mainActivity, SearchListActivity.class);

            intent.putExtra(Constants.CATEGORY_ID, catId);

            if (subCatId == null || subCatId.equals("")) {
                subCatId = Constants.ZERO;
            }

            intent.putExtra(Constants.SUBCATEGORY_ID, subCatId);

            mainActivity.startActivity(intent);

    }

    public void navigateBackFromNotiList(Activity activity) {
        Intent intent = new Intent();

        activity.setResult(Constants.RESULT_CODE__REFRESH_NOTIFICATION, intent);
    }


    public void navigateBackToHomeFeaturedFragment(FragmentActivity mainActivity, String
            catId, String subCatId) {
        Intent intent = new Intent(mainActivity,SearchListActivity.class);

        intent.putExtra(Constants.CATEGORY_ID, catId);
        intent.putExtra(Constants.SUBCATEGORY_ID, subCatId);

        // mainActivity.startActivityForResult(intent,Constants.RESULT_CODE__CATEGORY_FILTER);
        mainActivity.setResult(Constants.RESULT_CODE__CATEGORY_FILTER, intent);
    }

    public void navigateBackToHomeFeaturedCatFragment(FragmentActivity mainActivity, String
            catId, String subCatId) {
        Toast.makeText(mainActivity, "Nav...", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(mainActivity,SearchListActivity.class);

        intent.putExtra(Constants.CATEGORY_ID, catId);
        intent.putExtra(Constants.SUBCATEGORY_ID, subCatId);

        mainActivity.startActivity(intent);
        //mainActivity.startActivityForResult(intent,Constants.RESULT_CODE__CAT_FILTER);
        //mainActivity.setResult(Constants.RESULT_CODE__CAT_FILTER, intent);
    }

    public void navigateBackToHomeFeaturedFragmentFromFiltering(FragmentActivity mainActivity, ItemParameterHolder itemParameterHolder) {
        Intent intent = new Intent();
        intent.putExtra(Constants.FILTERING_HOLDER, itemParameterHolder);

        mainActivity.setResult(Constants.RESULT_CODE__SPECIAL_FILTER, intent);
    }

    public void navigateToCategory(MainActivity mainActivity) {
        if (checkFragmentChange(RegFragments.HOME_CATEGORY)) {
            try {
                CategoryListFragment fragment = new CategoryListFragment();
                Bundle bundle = new Bundle();
                fragment.setArguments(bundle);
                mainActivity.getSupportFragmentManager().beginTransaction()
                        .replace(containerId, fragment)
                        .commitAllowingStateLoss();
            } catch (Exception e) {
                Utils.psErrorLog("Error! Can't replace fragment.", e);
            }
        }
    }

    public void navigateToHomeLatestFiltering(MainActivity mainActivity, ItemParameterHolder itemParameterHolder) {
        if (checkFragmentChange(RegFragments.HOME_LATEST_PRODUCTS)) {
            try {
                SearchListFragment fragment = new SearchListFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.ITEM_PARAM_HOLDER_KEY, itemParameterHolder);
                fragment.setArguments(bundle);
                mainActivity.getSupportFragmentManager().beginTransaction()
                        .replace(containerId, fragment)
                        .commitAllowingStateLoss();
            } catch (Exception e) {
                Utils.psErrorLog("Error! Can't replace fragment.", e);
            }
        }
    }


    public void navigateToHomePopularFiltering(MainActivity mainActivity, ItemParameterHolder itemParameterHolder) {
        if (checkFragmentChange(RegFragments.HOME_POPULAR_CITIES)) {
            try {
                SearchListFragment fragment = new SearchListFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.ITEM_PARAM_HOLDER_KEY, itemParameterHolder);
                fragment.setArguments(bundle);
                mainActivity.getSupportFragmentManager().beginTransaction()
                        .replace(containerId, fragment)
                        .commitAllowingStateLoss();
            } catch (Exception e) {
                Utils.psErrorLog("Error! Can't replace fragment.", e);
            }
        }
    }

    public void navigateToHomeFilteringActivity(FragmentActivity mainActivity, ItemParameterHolder itemParameterHolder, String titleName, String itemLat, String itemLng, String mapMiles) {

        if (itemLat != null) {
            itemParameterHolder.lat = itemLat;
            itemParameterHolder.lng = itemLng;
            itemParameterHolder.mapMiles = mapMiles;
        }

        Intent intent = new Intent(mainActivity, SearchListActivity.class);

        intent.putExtra(Constants.ITEM_NAME, titleName);
        intent.putExtra(Constants.ITEM_PARAM_HOLDER_KEY, itemParameterHolder);

        mainActivity.startActivity(intent);
    }

    public void navigateToAllInfoCovidActivity(FragmentActivity mainActivity) {

        Intent intent = new Intent(mainActivity, InfoCovidListActivity.class);

        mainActivity.startActivity(intent);
    }

    public void navigateToHomeFilteringCategoryActivity(FragmentActivity mainActivity, ItemParameterHolder itemParameterHolder, String titleName, String itemLat, String itemLng, String mapMiles) {

        if (itemLat != null) {
            itemParameterHolder.lat = itemLat;
            itemParameterHolder.lng = itemLng;
            itemParameterHolder.mapMiles = mapMiles;
        }

        Intent intent = new Intent(mainActivity, SearchListCategoryActivity.class);

        intent.putExtra(Constants.ITEM_NAME, titleName);
        intent.putExtra(Constants.ITEM_PARAM_HOLDER_KEY, itemParameterHolder);

        mainActivity.startActivity(intent);
    }

    public void navigateToSearchActivityCategoryFragment(FragmentActivity fragmentActivity, String fragName, String catId, String subCatId) {
        Intent intent = new Intent(fragmentActivity, DashboardSearchByCategoryActivity.class);
        intent.putExtra(Constants.CATEGORY_FLAG, fragName);

        if (!catId.equals(Constants.NO_DATA)) {
            intent.putExtra(Constants.CATEGORY_ID, catId);
        }

        if (!subCatId.equals(Constants.NO_DATA)) {
            intent.putExtra(Constants.SUBCATEGORY_ID, subCatId);
        }

        fragmentActivity.startActivityForResult(intent, Constants.REQUEST_CODE__SEARCH_FRAGMENT);
    }

    public void navigateToSearchViewActivity(FragmentActivity fragmentActivity, String fragName, String typeId, String priceTypeId, String conditionId, String dealOptionId, String currencyId, String locationId) {
        Intent intent = new Intent(fragmentActivity, SearchViewActivity.class);
        intent.putExtra(Constants.ITEM_TYPE_FLAG, fragName);

        intent.putExtra(Constants.ITEM_TYPE_ID, typeId);
        intent.putExtra(Constants.ITEM_PRICE_TYPE_ID, priceTypeId);
        intent.putExtra(Constants.ITEM_CONDITION_TYPE_ID, conditionId);
        intent.putExtra(Constants.ITEM_OPTION_TYPE_ID, dealOptionId);
        intent.putExtra(Constants.ITEM_CURRENCY_TYPE_ID, currencyId);

        intent.putExtra(Constants.ITEM_LOCATION_TYPE_ID, locationId);
        intent.putExtra(Constants.LOCATION_FLAG, Constants.LOCATION_WITH_CLEAR_ICON);

        fragmentActivity.startActivityForResult(intent, Constants.REQUEST_CODE__SEARCH_VIEW_FRAGMENT);
    }

    public void navigateBackToSearchFragment(FragmentActivity fragmentActivity, String catId, String cat_Name) {
        Intent intent = new Intent();
        intent.putExtra(Constants.CATEGORY_NAME, cat_Name);
        intent.putExtra(Constants.CATEGORY_ID, catId);

        fragmentActivity.setResult(Constants.RESULT_CODE__SEARCH_WITH_CATEGORY, intent);
    }

    public void navigateBackToSearchFragmentFromSubCategory(FragmentActivity fragmentActivity, String sub_id, String sub_Name) {
        Intent intent = new Intent();
        intent.putExtra(Constants.SUBCATEGORY_NAME, sub_Name);
        intent.putExtra(Constants.SUBCATEGORY_ID, sub_id);

        fragmentActivity.setResult(Constants.RESULT_CODE__SEARCH_WITH_SUBCATEGORY, intent);
    }

    public void navigateBackToItemTypeFragment(FragmentActivity fragmentActivity, String typeId, String typeName) {
        Intent intent = new Intent();
        intent.putExtra(Constants.ITEM_TYPE_NAME, typeName);
        intent.putExtra(Constants.ITEM_TYPE_ID, typeId);

        fragmentActivity.setResult(Constants.RESULT_CODE__SEARCH_WITH_ITEM_TYPE, intent);
    }

    public void navigateBackToItemPriceTypeFragment(FragmentActivity fragmentActivity, String priceTypeId, String priceTypeName) {
        Intent intent = new Intent();
        intent.putExtra(Constants.ITEM_PRICE_TYPE_NAME, priceTypeName);
        intent.putExtra(Constants.ITEM_PRICE_TYPE_ID, priceTypeId);

        fragmentActivity.setResult(Constants.RESULT_CODE__SEARCH_WITH_ITEM_PRICE_TYPE, intent);
    }

    public void navigateBackToItemConditionFragment(FragmentActivity fragmentActivity, String priceTypeId, String priceTypeName) {
        Intent intent = new Intent();
        intent.putExtra(Constants.ITEM_CONDITION_TYPE_NAME, priceTypeName);
        intent.putExtra(Constants.ITEM_CONDITION_TYPE_ID, priceTypeId);

        fragmentActivity.setResult(Constants.RESULT_CODE__SEARCH_WITH_ITEM_CONDITION_TYPE, intent);
    }

    public void navigateBackToItemLocationFragment(FragmentActivity fragmentActivity, String locationId, String locationName, String locationLat, String locationLng) {
        Intent intent = new Intent();
        intent.putExtra(Constants.ITEM_LOCATION_TYPE_NAME, locationName);
        intent.putExtra(Constants.ITEM_LOCATION_TYPE_ID, locationId);
        intent.putExtra(Constants.LAT, locationLat);
        intent.putExtra(Constants.LNG, locationLng);

        fragmentActivity.setResult(Constants.RESULT_CODE__SEARCH_WITH_ITEM_LOCATION_TYPE, intent);
    }

    public void navigateBackToItemCurrencyTypeFragment(FragmentActivity fragmentActivity, String currencyId, String currencySymbol) {
        Intent intent = new Intent();
        intent.putExtra(Constants.ITEM_CURRENCY_TYPE_NAME, currencySymbol);
        intent.putExtra(Constants.ITEM_CURRENCY_TYPE_ID, currencyId);

        fragmentActivity.setResult(Constants.RESULT_CODE__SEARCH_WITH_ITEM_CURRENCY_TYPE, intent);
    }

    public void navigateBackToItemDealOptionTypeFragment(FragmentActivity fragmentActivity, String optionId, String optionName) {
        Intent intent = new Intent();
        intent.putExtra(Constants.ITEM_OPTION_TYPE_NAME, optionName);
        intent.putExtra(Constants.ITEM_OPTION_TYPE_ID, optionId);

        fragmentActivity.setResult(Constants.RESULT_CODE__SEARCH_WITH_ITEM_OPTION_TYPE, intent);
    }

    public void navigateBackToItemEntryFromCustomCamera(FragmentActivity fragmentActivity, String filePath, Uri uri) {
        Intent intent = new Intent();
        intent.putExtra(Constants.IMAGE_PATH, filePath);
        intent.putExtra(Constants.IMAGE_URI, uri.toString());

        fragmentActivity.setResult(Constants.RESULT_CODE__ITEM_ENTRY_WITH_CUSTOM_CAMERA, intent);
    }

    public void navigateBackToProfileFragment(FragmentActivity fragmentActivity) {
        Intent intent = new Intent();

        fragmentActivity.setResult(Constants.RESULT_CODE__LOGOUT_ACTIVATED, intent);
    }

    public void navigateBackToChatHistoryListFragment(FragmentActivity fragmentActivity) {

        fragmentActivity.setResult(Constants.RESULT_CODE__CHAT_FRAGMENT, new Intent());
    }

    public void navigateToItemDetailFromHistoryListOnly(Activity activity, String itemId, String itemName) {
        Intent intent = new Intent(activity, ItemActivity.class);
        intent.putExtra(Constants.ITEM_ID, itemId);
        intent.putExtra(Constants.ITEM_NAME, itemName);
        intent.putExtra(Constants.HISTORY_FLAG, Constants.ZERO);

        activity.startActivity(intent);
    }

    public void navigateToItemDetailActivity(FragmentActivity fragmentActivity, String itemId, String itemName, String categoryId, String subCategoryId) {

        Intent intent = new Intent(fragmentActivity, ItemActivity.class);

        intent.putExtra(Constants.HISTORY_FLAG, Constants.ONE);
        intent.putExtra(Constants.ITEM_ID, itemId);
        intent.putExtra(Constants.ITEM_NAME, itemName);
        intent.putExtra(Constants.ITEM_CATEGORY_ID, categoryId);
        intent.putExtra(Constants.ITEM_SUB_CATEGORY_ID, subCategoryId);

        //fragmentActivity.onBackPressed();

        fragmentActivity.startActivity(intent);
    }

    public void navigateToRelatedItemDetailActivity(FragmentActivity fragmentActivity, String itemId, String itemName, String categoryId, String subCategoryId) {

        Intent intent = new Intent(fragmentActivity, ItemRelatedDetailActivity.class);

        intent.putExtra(Constants.HISTORY_FLAG, Constants.ONE);
        intent.putExtra(Constants.ITEM_ID, itemId);
        intent.putExtra(Constants.ITEM_NAME, itemName);
        intent.putExtra(Constants.ITEM_CATEGORY_ID, categoryId);
        intent.putExtra(Constants.ITEM_SUB_CATEGORY_ID, subCategoryId);

        //fragmentActivity.onBackPressed();

        fragmentActivity.startActivity(intent);
    }


    public void navigateToBlogList(FragmentActivity fragmentActivity) {
        Intent intent = new Intent(fragmentActivity, BlogListActivity.class);
        fragmentActivity.startActivity(intent);
    }

    public void navigateToAppLoading(FragmentActivity fragmentActivity) {
        Intent intent = new Intent(fragmentActivity, AppLoadingActivity.class);
        fragmentActivity.startActivity(intent);
    }

    public void navigateToItemListFromFollower(Activity activity) {

        Intent intent = new Intent(activity, ItemFromFollowerListActivity.class);
        activity.startActivity(intent);
    }

    public void navigateToBlogDetailActivity(FragmentActivity fragmentActivity, String blogId) {

        Intent intent = new Intent(fragmentActivity, BlogDetailActivity.class);

        intent.putExtra(Constants.BLOG_ID, blogId);

        fragmentActivity.startActivity(intent);
    }

    public void navigateToInfoCovidDetailActivity(FragmentActivity fragmentActivity, String infoCovidId) {

        Intent intent = new Intent(fragmentActivity, DetailInfoCovidActivity.class);

        intent.putExtra(Constants.INFO_COVID_ID, infoCovidId);

        fragmentActivity.startActivity(intent);
    }

    public void navigateToBannerDetailActivity(FragmentActivity fragmentActivity, String bannerId) {

        Intent intent = new Intent(fragmentActivity, BannerDetailActivity.class);

        intent.putExtra(Constants.BANNER_ID, bannerId);

        fragmentActivity.startActivity(intent);
    }

    public void navigateToMainActivity(Activity activity, String selectedLocationId, String selectedLocationName, String lat, String lng) {
        Intent intent = new Intent(activity, MainActivity.class);
        intent.putExtra(Constants.SELECTED_LOCATION_ID, selectedLocationId);
        intent.putExtra(Constants.SELECTED_LOCATION_NAME, selectedLocationName);
        intent.putExtra(Constants.LAT, lat);
        intent.putExtra(Constants.LNG, lng);
        activity.startActivityForResult(intent, Constants.REQUEST_CODE__SELECTED_CITY_FRAGMENT);
    }

    public void navigateBackToMainActivity(FragmentActivity activity, String selectedLocationId, String selectedLocationName, String lat, String lng) {
        Intent intent = new Intent(); //activity, MainActivity.class);
        intent.putExtra(Constants.ITEM_LOCATION_TYPE_ID, selectedLocationId);
        intent.putExtra(Constants.ITEM_LOCATION_TYPE_NAME, selectedLocationName);
        intent.putExtra(Constants.LAT, lat);
        intent.putExtra(Constants.LNG, lng);
        activity.setResult(Constants.RESULT_CODE__SEARCH_WITH_ITEM_LOCATION_TYPE, intent);
    }

//    public void navigateToLocationActivity(Activity activity, String flag, String locationId) {
//        Intent intent = new Intent(activity, LocationActivity.class);
//        intent.putExtra(Constants.LOCATION_FLAG, flag);
//        intent.putExtra(Constants.ITEM_LOCATION_TYPE_ID, locationId);
//        activity.startActivityForResult(intent, Constants.REQUEST_CODE__SELECTED_CITY_FRAGMENT);
//    }

    public void navigateToIntroActivity(Activity activity, String flag, String locationId) {
        Intent intent = new Intent(activity, IntroActivity.class);
        intent.putExtra(Constants.LOCATION_FLAG, flag);
        intent.putExtra(Constants.ITEM_LOCATION_TYPE_ID, locationId);
        activity.startActivityForResult(intent, Constants.REQUEST_CODE__SELECTED_CITY_FRAGMENT);
    }

    public void navigateToForceUpdateActivity(FragmentActivity fragmentActivity, String title, String msg) {

        Intent intent = new Intent(fragmentActivity, ForceUpdateActivity.class);

        intent.putExtra(Constants.APPINFO_FORCE_UPDATE_MSG, msg);
        intent.putExtra(Constants.APPINFO_FORCE_UPDATE_TITLE, title);

        fragmentActivity.startActivity(intent);
    }

    public void navigateToPlayStore(FragmentActivity fragmentActivity) {
//        try {
//            fragmentActivity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Config.PLAYSTORE_MARKET_URL)));
//        } catch (android.content.ActivityNotFoundException anfe) {
//            fragmentActivity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Config.PLAYSTORE_HTTP_URL)));
//        }
//    }
        Uri uri = Uri.parse(Config.PLAYSTORE_MARKET_URL_FIX + fragmentActivity.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            fragmentActivity.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            fragmentActivity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Config.PLAYSTORE_HTTP_URL_FIX + fragmentActivity.getPackageName())));
        }
    }

    public void navigateToMapFiltering(FragmentActivity activity, ItemParameterHolder itemParameterHolder) {
        Intent intent = new Intent(activity, MapFilteringActivity.class);

        intent.putExtra(Constants.ITEM_HOLDER, itemParameterHolder);

        activity.startActivityForResult(intent, Constants.REQUEST_CODE__MAP_FILTERING);
    }

    public void navigateBackToSearchFromMapFiltering(FragmentActivity activity, ItemParameterHolder itemParameterHolder) {
        Intent intent = new Intent();

        intent.putExtra(Constants.ITEM_HOLDER, itemParameterHolder);

        activity.setResult(Constants.RESULT_CODE__MAP_FILTERING, intent);

        activity.finish();
    }

    public void navigateToUserDetail(FragmentActivity activity, String otherUserId, String otherUserName) {

        Intent intent = new Intent(activity, UserDetailActivity.class);

        intent.putExtra(Constants.OTHER_USER_ID, otherUserId);
        intent.putExtra(Constants.OTHER_USER_NAME, otherUserName);

        activity.startActivity(intent);
    }

    public void navigateToChatActivity(FragmentActivity activity,
                                       String itemId,
                                       String receivedUserId,
                                       String receiverName,
                                       String itemImagePath,
                                       String itemName,
                                       String itemCurrency,
                                       String itemPrice,
                                       String itemConditionName,
                                       String flag,
                                       String receiveUserImage,
                                       int request_code) {

        Intent intent = new Intent(activity, ChatActivity.class);

        intent.putExtra(Constants.ITEM_ID, itemId);
        intent.putExtra(Constants.RECEIVE_USER_ID, receivedUserId);
        intent.putExtra(Constants.RECEIVE_USER_NAME, receiverName);
        intent.putExtra(Constants.RECEIVE_USER_IMG_URL, receiveUserImage);
        intent.putExtra(Constants.IMAGE_PATH, itemImagePath);
        intent.putExtra(Constants.ITEM_NAME, itemName);
        intent.putExtra(Constants.ITEM_PRICE, itemPrice);
        intent.putExtra(Constants.ITEM_CURRENCY, itemCurrency);
        intent.putExtra(Constants.ITEM_CONDITION_TYPE_NAME, itemConditionName);
        intent.putExtra(Constants.CHAT_FLAG, flag);

        activity.startActivityForResult(intent, request_code);
    }

    public void navigateToImageFullScreen(FragmentActivity activity, String path) {
        Intent intent = new Intent(activity, ChatImageFullScreenActivity.class);

        intent.putExtra(Constants.IMAGE, path);

        activity.startActivity(intent);
    }

    public void getImageFromGallery(Activity activity) {

        if (Utils.isStoragePermissionGranted(activity)) {

            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

            activity.startActivityForResult(Intent.createChooser(intent, "Pilih Foto"), Constants.RESULT_CODE__IMAGE_CATEGORY);
        }

    }
    public void navigateToItemPromoteActivity(Activity activity, String itemId) {
        Intent intent = new Intent(activity, ItemPromoteActivity.class);
        intent.putExtra(Constants.ITEM_ID, itemId);
        activity.startActivity(intent);
    }

    public void navigateBackToCheckoutFragment(Activity activity, String stripeToken) {
        Intent intent = new Intent();

        intent.putExtra(Constants.PAYMENT_TOKEN, stripeToken);

        activity.setResult(Constants.RESULT_CODE__STRIPE_ACTIVITY, intent);
    }


    //region Private methods
    private Boolean checkFragmentChange(RegFragments regFragments) {
        if (currentFragment != regFragments) {
            currentFragment = regFragments;
            return true;
        }

        return false;
    }

    public void navigateToStripeActivity(Activity fragmentActivity, String stripePublishableKey) {
        Intent intent = new Intent(fragmentActivity, StripeActivity.class);
        intent.putExtra(Constants.STRIPEPUBLISHABLEKEY, stripePublishableKey);
        fragmentActivity.startActivityForResult(intent, Constants.REQUEST_CODE__STRIPE_ACTIVITY);
    }

    /**
     * Remark : This enum is only for MainActivity,
     * For the other fragments, no need to register here
     **/
    private enum RegFragments {
        HOME_ADD_ITEM,
        HOME_FRAGMENT,
        HOME_USER_LOGIN,
        HOME_USER_EMAIL_VERIFY,
        HOME_FB_USER_REGISTER,
        HOME_BASKET,
        HOME_USER_REGISTER,
        HOME_PHONE_VERIFY,
        HOME_PHONE_LOGIN,
        HOME_USER_FOGOT_PASSWORD,
        HOME_ABOUTUS,
        HOME_CONTACTUS,
        HOME_NOTI_SETTING,
        HOME_APP_INFO,
        HOME_LANGUAGE_SETTING,
        HOME_LATEST_PRODUCTS,
        HOME_DISCOUNT,
        HOME_FEATURED_PRODUCTS,
        HOME_CATEGORY,
        HOME_MESSAGE,
        HOME_SUBCATEGORY,
        HOME_HOME,
        HOME_TRENDINGPRODUCTS,
        HOME_COMMENTLISTS,
        HOME_SEARCH,
        HOME_NOTIFICATION,
        HOME_PRODUCT_COLLECTION,
        HOME_TRANSACTION,
        HOME_HISTORY,
        HOME_SETTING,
        HOME_FAVOURITE,
        HOME_CITY_LIST,
        HOME_CITY_MENU,
        HOME_FILTER,
        HOME_CS,
        HOME_CITIES,
        HOME_BLOG,
        HOME_POPULAR_CITIES,
        HOME_RECOMMENDED_CITIES,
        HOME_PRIVACY_POLICY

    }
}
