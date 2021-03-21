package com.mrii.prodes;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.facebook.login.LoginManager;
import com.google.ads.consent.ConsentForm;
import com.google.ads.consent.ConsentFormListener;
import com.google.ads.consent.ConsentInfoUpdateListener;
import com.google.ads.consent.ConsentInformation;
import com.google.ads.consent.ConsentStatus;
import com.google.ads.consent.DebugGeography;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.mrii.prodes.databinding.ActivityMainBinding;
import com.mrii.prodes.ui.blog.list.BlogListMainFragment;
import com.mrii.prodes.ui.city.selectedcity.SelectedCityFragment;
import com.mrii.prodes.ui.common.NavigationController;
import com.mrii.prodes.ui.common.PSAppCompactActivity;
import com.mrii.prodes.ui.user.alert.AlertFragment;
import com.mrii.prodes.utils.AppLanguage;
import com.mrii.prodes.utils.Constants;
import com.mrii.prodes.utils.MyContextWrapper;
import com.mrii.prodes.utils.PSDialogMsg;
import com.mrii.prodes.utils.Utils;
import com.mrii.prodes.viewmodel.common.NotificationViewModel;
import com.mrii.prodes.viewmodel.pscount.PSCountViewModel;
import com.mrii.prodes.viewmodel.user.UserViewModel;
import com.mrii.prodes.viewobject.PSCount;
import com.mrii.prodes.viewobject.User;
import com.mrii.prodes.viewobject.common.Resource;
import com.mrii.prodes.viewobject.holder.ItemParameterHolder;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.inject.Inject;

public class MainActivity extends PSAppCompactActivity {


    //region Variables

    @Inject
    SharedPreferences pref;

    @Inject
    AppLanguage appLanguage;
    private Boolean notificationSetting = false;
    private String token = "";
    private UserViewModel userViewModel;
    private NotificationViewModel notificationViewModel;
    private User user;
    private PSDialogMsg psDialogMsg;
    public boolean isLogout = false;
    Drawable notificationIconDrawable = null;
    ActionBarDrawerToggle drawerToggle;
    public String selectedLocationId, selectedLocationName, selected_lat, selected_lng;
    private String loginUserId;
    public String userType;
    public String address_detail;
    public String nama_umkm;
    public String address;
    private String locationId;
    private String locationName;
    public String notificationItemId, notificationBuyerId, notificationSellerId, notificationMsg, notificationSenderName, notificationSenderUrl, userId;
    String receiverId = Constants.EMPTY_STRING;
    String receiverName = Constants.EMPTY_STRING;
    String receiverUrl = Constants.EMPTY_STRING;
    int requestCode = 0;
    String flag = Constants.EMPTY_STRING;
    private ConsentForm form;
    private String userIdToVerify;
    private GoogleSignInClient mGoogleSignInClient;
    public User a;
    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Inject
    NavigationController navigationController;

    public ActivityMainBinding binding;
    private PSCountViewModel psCountViewModel;
    private TextView messageNotificationTextView;
    private TextView notificationTextView;
    private ImageView notificationIconImageView;
    AlertFragment alertFragment;

    String notificationCount = "0";
    private int toolbarIconColor = Color.GRAY;
    //endregion


    //region Override Methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Base_PSTheme);
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        initUIAndActions();

        initModels();

        initData();

        checkConsentStatus();

        handleIntent(getIntent());
    }

    private void handleIntent(Intent intent) {

        // ATTENTION: This was auto-generated to handle app links.
        String appLinkAction = intent.getAction();
        Uri appLinkData = intent.getData();
        if (Intent.ACTION_VIEW.equals(appLinkAction) && appLinkData != null){
            String recipeId = appLinkData.getLastPathSegment();
            String[] segment = appLinkData.getPath().split("/");
            String param = segment[segment.length - 2];
            if (param.equalsIgnoreCase("item")) {
                 navigationController.navigateToItemDetailActivity(this, recipeId, Constants.EMPTY_STRING, Constants.EMPTY_STRING, Constants.EMPTY_STRING);
            } else if (param.equalsIgnoreCase("akun")) {
                navigationController.navigateToUserDetail(this, receiverId, Constants.EMPTY_STRING);
            }else if(param.equalsIgnoreCase("pronews")){
                navigationController.navigateToBlogDetailActivity(this, recipeId);
            }
            Uri appData = Uri.parse("content://com.recipe_app/recipe/").buildUpon().appendPath(recipeId).build();
        }

        String dataUserSelectedActivity = intent.getStringExtra("FromUserSelectedType");
        String dataItemEntryFragment = intent.getStringExtra("FromItemEntryFragment");
        String dataVerifyMobileFragment = intent.getStringExtra("FromVerifyMobileFragment");
        String dataProfileEditFragment = intent.getStringExtra("FromProfileEditFragment");
        String dataProfilePenjualEditFragment = intent.getStringExtra("FromProfilePenjualEditFragment");

        if (dataItemEntryFragment != null && dataItemEntryFragment.contentEquals("2")){
            chanegeColorsTinyStore();
            navigationController.navigateToHome(MainActivity.this, false, address, address_detail, selectedLocationId, selectedLocationName,false);
        }

        if (dataVerifyMobileFragment != null && dataVerifyMobileFragment.contentEquals("2")){
            chanegeColorsTinyStore();
            navigationController.navigateToHome(MainActivity.this, false, address, address_detail, selectedLocationId, selectedLocationName,false);
        }

        if (dataProfileEditFragment != null && dataProfileEditFragment.contentEquals("2")){
            chanegeColorsTinyStore();
            navigationController.navigateToHome(MainActivity.this, false, address, address_detail, selectedLocationId, selectedLocationName,false);
        }

        if (dataProfilePenjualEditFragment != null && dataProfilePenjualEditFragment.contentEquals("2")){
            pref.edit().putString(Constants.USER_TYPE, Constants.SELLER).apply();

           // userType = pref.getString(Constants.USER_TYPE,Constants.EMPTY_STRING);

            userType = "penjual";

            chanegeColorsTinyStore();
            Utils.navigateOnUserPenjualVerificationFragment(pref,user,navigationController,this);
            //Utils.navigateOnUserVerificationFragment(pref,user,navigationController,this);
            navigationController.navigateToHome(MainActivity.this, false, address, address_detail, selectedLocationId, selectedLocationName,false);
        }
        if (dataUserSelectedActivity != null && dataUserSelectedActivity.contentEquals("1")){
            chanegeColorsTinyStore();
            navigationController.navigateToHome(MainActivity.this, false, address, address_detail, selectedLocationId, selectedLocationName,false);
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(newBase);
        String CURRENT_LANG_CODE = preferences.getString(Constants.LANGUAGE_CODE, Config.DEFAULT_LANGUAGE);
        String CURRENT_LANG_COUNTRY_CODE = preferences.getString(Constants.LANGUAGE_COUNTRY_CODE, Config.DEFAULT_LANGUAGE_COUNTRY_CODE);
        super.attachBaseContext(MyContextWrapper.wrap(newBase, CURRENT_LANG_CODE, CURRENT_LANG_COUNTRY_CODE, true));
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshUserData();
    }

    public void refreshPSCount() {
        if (!loginUserId.isEmpty()) {
            psCountViewModel.setPsCountObj(loginUserId, token);
        }
    }

    public void refreshUserData() {
        try {
            loginUserId = pref.getString(Constants.USER_ID, Constants.EMPTY_STRING);
            userType = pref.getString(Constants.USER_TYPE,Constants.EMPTY_STRING);
            address = pref.getString(Constants.USER_ADDRESS, Constants.EMPTY_STRING);
            address_detail = pref.getString(Constants.USER_DETAIL_ADDRESS, Constants.EMPTY_STRING);
        } catch (Exception e) {
            Utils.psErrorLog("", e);
        }

        refreshPSCount();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
            if(fragment != null)
            {
                if(fragment instanceof SelectedCityFragment)
                {
                    String message = getBaseContext().getString(R.string.message__want_to_quit);
                    String okStr =getBaseContext().getString(R.string.message__ok_close);
                    String cancelStr = getBaseContext().getString(R.string.message__cancel_close);

                    showBasicComfirmDialog(getApplicationContext(), message, okStr,cancelStr );

//                    psDialogMsg.show();
//
//                    psDialogMsg.okButton.setOnClickListener(view -> {
//                        psDialogMsg.cancel();
//                        MainActivity.this.finish();
//                        System.exit(0);
//                    });
//                    psDialogMsg.cancelButton.setOnClickListener(view -> psDialogMsg.cancel());
                }else {
//                    setSelectMenu(R.id.nav_home);
                    showBottomNavigation();
                    //binding.bottomNavigationView.setSelectedItemId(R.id.home_menu);
                    //Utils.addToolbarScrollFlag(binding.toolbar);
                    //binding.addItemButton.setVisibility(View.VISIBLE);
                    //setToolbarText(binding.toolbar, Constants.EMPTY_STRING);
                    navigationController.navigateToHome(MainActivity.this, false, address, address_detail, selectedLocationId, selectedLocationName,false);

                }

            }
        }
        return  true;
    }

    public void showBasicComfirmDialog(Context context, String description, String oke, String  cancel){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Informasi");
        builder.setMessage(description);
        builder.setPositiveButton(oke, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MainActivity.this.finish();
                System.exit(0);
            }
        });
        builder.setNegativeButton(cancel, null);
        builder.show();
    }

    //endregion

    //region Private Methods

    /**
     * Initialize Models
     */
    private void initModels() {
        userViewModel = new ViewModelProvider(this, viewModelFactory).get(UserViewModel.class);
        notificationViewModel = new ViewModelProvider(this, viewModelFactory).get(NotificationViewModel.class);
    }

    /**
     * Show alert message to user.
     *
     * @param msg Message to show to user
     */
    private void showAlertMessage(String msg) {

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();

        View view = inflater.inflate(R.layout.ps_dialog, null);

        builder.setView(view)
                .setPositiveButton(getString(R.string.app__ok), null);

        TextView message = view.findViewById(R.id.messageTextView);

        message.setText(msg);

        builder.create();

        builder.show();

    }

    /**
     * This function will initialize UI and Event Listeners
     */
    private void initUIAndActions() {

        psDialogMsg = new PSDialogMsg(this, false);

        userIdToVerify = pref.getString(Constants.USER_ID_TO_VERIFY, Constants.EMPTY_STRING);

        navigationController.navigateToCityList(this);

        showBottomNavigation();

        setSelectMenu(R.id.nav_home);

        getIntentData();

//        BottomNavigationMenuView bottomNavigationMenuView =
//                (BottomNavigationMenuView) binding.bottomNavigationView.getChildAt(0);
//        View bTMView = bottomNavigationMenuView.getChildAt(3);
//        BottomNavigationItemView itemView = (BottomNavigationItemView) bTMView;
//
//        View badgeView = LayoutInflater.from(this)
//                .inflate(R.layout.notification_badge, itemView, true);
//
//        messageNotificationTextView = badgeView.findViewById(R.id.notifications_badge);
//        messageNotificationTextView.setVisibility(View.GONE);


//        binding.bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
//
//            switch (item.getItemId()) {
//                case R.id.home_menu:
//                    //layout_scrollFlags
//                    //Utils.addToolbarScrollFlag(binding.toolbar);
//                    //binding.addItemButton.setVisibility(View.VISIBLE);
//                    navigationController.navigateToHome(MainActivity.this, false, address, address_detail, selectedLocationId, selectedLocationName,false);
//                    //setToolbarText(binding.toolbar, Constants.EMPTY_STRING);
//
//                    break;
//                case R.id.message_menu:
//                    //Utils.addToolbarScrollFlag(binding.toolbar);
//                    //binding.addItemButton.setVisibility(View.GONE);
//
//                    Utils.navigateOnUserVerificationAndMessageFragment(pref,user,navigationController,this);
//
//                    break;
//
//                case R.id.pronews_menu:
//                    //Utils.addToolbarScrollFlag(binding.toolbar);
//                    //binding.addItemButton.setVisibility(View.GONE);
//                    //navigationController.navigateToInterest(MainActivity.this);
//                    navigationController.navigateToPronews(MainActivity.this);
//                    //setToolbarText(binding.toolbar, getString(R.string.menu__interest));
//
//                    break;
//
//                case R.id.add_product_menu:
//                    // Utils.addToolbarScrollFlag(binding.toolbar);
//                    //binding.addItemButton.setVisibility(View.GONE);
//
//
//                    //setToolbarText(binding.toolbar, getString(R.string.menu__search));
//
//                    if (userType.toLowerCase().equals("penjual")){
//
//                        //Utils.addToolbarScrollFlag(binding.toolbar);
//                        //binding.addItemButton.setVisibility(View.GONE);
//                        Utils.navigateOnUserVerificationActivity(userIdToVerify, loginUserId, psDialogMsg, this, navigationController, () -> {
//                            try {
//                                locationId = pref.getString(Constants.SELECTED_LOCATION_ID, Constants.EMPTY_STRING);
//                                locationName = pref.getString(Constants.SELECTED_LOCATION_NAME, Constants.EMPTY_STRING);
//
//                            } catch (Exception e) {
//                                Utils.psErrorLog("", e);
//                            }
//
//                            navigationController.navigateToItemEntryFragment(MainActivity.this, Constants.ADD_NEW_ITEM, locationId, locationName);
//                        });
//
//                        //setToolbarText(binding.toolbar, getString(R.string.menu__search));
//                    }
//                    else{
////                        navigationController.navigateToAlertActivity(MainActivity.this);
//                        alertFragment = new AlertFragment();
//                        loadFragment(alertFragment);
//                        Toast.makeText(this, "Fitur ini hanya untuk penjual", Toast.LENGTH_SHORT).show();
//                    }
//
//
//                    break;
//
//                case R.id.me_menu:
//                    // Utils.addToolbarScrollFlag(binding.toolbar);
//                    //binding.addItemButton.setVisibility(View.GONE);
//
//                    Utils.navigateOnUserVerificationFragment(pref,user,navigationController,this);
//
//                    break;
//
//                default:
//
//                    break;
//            }
//
//            return true;
//        });

//        binding.addItemButton.setTypeface(Utils.getTypeFace(this, Utils.Fonts.ROBOTO));

//        binding.addItemButton.setOnClickListener(v -> {
//
//            Utils.navigateOnUserVerificationActivity(userIdToVerify, loginUserId, psDialogMsg, this, navigationController, () -> {
//                try {
//                    locationId = pref.getString(Constants.SELECTED_LOCATION_ID, Constants.EMPTY_STRING);
//                    locationName = pref.getString(Constants.SELECTED_LOCATION_NAME, Constants.EMPTY_STRING);
//
//                } catch (Exception e) {
//                    Utils.psErrorLog("", e);
//                }
//
//                navigationController.navigateToItemEntryActivity(MainActivity.this, Constants.ADD_NEW_ITEM, locationId, locationName);
//
//            });
//
//        });

        // Configure Google Sign In

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);
    }

    private void setSelectMenu(int nav_home) {
        chanegeColorsTinyStore();
        navigationController.navigateToHome(MainActivity.this, false, address, address_detail, selectedLocationId, selectedLocationName,false);
    }

    public void clickAction(View view) {
        int id = view.getId();
        switch (userType) {
            case "penjual":
                binding.addButton.setVisibility(View.VISIBLE);
                binding.btnCallCenter.setVisibility(View.GONE);
                switch (id) {
                    case R.id.menu_store:
                        chanegeColorsTinyStore();
                        navigationController.navigateToHome(MainActivity.this, false, address, address_detail, selectedLocationId, selectedLocationName,false);
                        break;
                    case R.id.menu_pronews:
                        chanegeColorsTinyPronews();
                        navigationController.navigateToPronews(MainActivity.this);
                        break;
                    case R.id.add_button:
                        chanegeColorsTinyAddProduct();
                        if (userType.toLowerCase().equals("penjual")){
                            Utils.navigateOnUserVerificationActivity(userIdToVerify, loginUserId, psDialogMsg, this, navigationController, () -> {
                                try {
                                    locationId = pref.getString(Constants.SELECTED_LOCATION_ID, Constants.EMPTY_STRING);
                                    locationName = pref.getString(Constants.SELECTED_LOCATION_NAME, Constants.EMPTY_STRING);

                                } catch (Exception e) {
                                    Utils.psErrorLog("", e);
                                }
                                navigationController.navigateToItemEntryFragment(MainActivity.this, Constants.ADD_NEW_ITEM, locationId, locationName);
                            });

                        }else{
                            alertFragment = new AlertFragment();
                            loadFragment(alertFragment);
                        }
                        break;
                    case R.id.menu_riwayat:
                        chanegeColorsTinyAddProduct();
                        Utils.navigateOnUserVerificationAndMessageFragment(pref,user,navigationController,this);
                        break;
                    case R.id.menu_akun:
                        chanegeColorsTinyAkun();
                        Utils.navigateOnUserPenjualVerificationFragment(pref,user,navigationController,this);
                        break;
                }

                break;

            case "pembeli":
                chanegeColorsTinyPronews();
                binding.addButton.setVisibility(View.GONE);
                binding.btnCallCenter.setVisibility(View.VISIBLE);
                switch (id) {
                    case R.id.menu_store:
                        chanegeColorsTinyStore();
                        navigationController.navigateToHome(MainActivity.this, false, address, address_detail, selectedLocationId, selectedLocationName,false);
                        break;
                    case R.id.menu_pronews:
                        chanegeColorsTinyPronews();
                        navigationController.navigateToPronews(MainActivity.this);
                        break;
                    case R.id.btn_call_center:
                        chanegeColorsTinyCallCenter();
                        navigationController.navigateToCallCenter(MainActivity.this);
                        break;
                    case R.id.menu_riwayat:
                        chanegeColorsTinyRiwayat();
                        Utils.navigateOnUserVerificationAndMessageFragment(pref,user,navigationController,this);
                        break;
                    case R.id.menu_akun:
                        chanegeColorsTinyAkun();
                        Utils.navigateOnUserPembeliVerificationFragment(pref,user,navigationController,this);
                        break;
                }
                break;

            default:
                switch (id) {
                    case R.id.menu_store:
                        chanegeColorsTinyStore();
                        navigationController.navigateToHome(MainActivity.this, false, address, address_detail, selectedLocationId, selectedLocationName,false);
                        break;
                    case R.id.menu_pronews:
                        chanegeColorsTinyPronews();
                        navigationController.navigateToPronews(MainActivity.this);
                        break;
                    case R.id.btn_call_center:
                        chanegeColorsTinyCallCenter();
                        navigationController.navigateToCallCenter(MainActivity.this);
                        break;
                    case R.id.menu_riwayat:
                        chanegeColorsTinyRiwayat();
                        Utils.navigateOnUserVerificationAndMessageFragment(pref,user,navigationController,this);
                        break;
                    case R.id.menu_akun:
                        chanegeColorsTinyAkun();
                        Utils.navigateOnUserVerificationFragment(pref,user,navigationController,this);
                        break;
                }

        }

    }

    private void chanegeColorsTinyCallCenter() {
        // Change ImageView
        binding.ivStore.setColorFilter(getResources().getColor(R.color.grey_40),
                PorterDuff.Mode.SRC_IN);
        binding.ivPronews.setColorFilter(getResources().getColor(R.color.grey_40),
                PorterDuff.Mode.SRC_IN);
        binding.ivRiwayat.setColorFilter(getResources().getColor(R.color.grey_40),
                PorterDuff.Mode.SRC_IN);
        binding.ivAkun.setColorFilter(getResources().getColor(R.color.grey_40),
                PorterDuff.Mode.SRC_IN);

        // Change TextView
        binding.tvStore.setTextColor(getResources().getColor(R.color.grey_40));
        binding.tvPronews.setTextColor(getResources().getColor(R.color.grey_40));
        binding.tvRiwayat.setTextColor(getResources().getColor(R.color.grey_40));
        binding.tvAkun.setTextColor(getResources().getColor(R.color.grey_40));
    }

    private void chanegeColorsTinyAddProduct() {
        // Change ImageView
        binding.ivStore.setColorFilter(getResources().getColor(R.color.grey_40),
                PorterDuff.Mode.SRC_IN);
        binding.ivPronews.setColorFilter(getResources().getColor(R.color.grey_40),
                PorterDuff.Mode.SRC_IN);
        binding.ivRiwayat.setColorFilter(getResources().getColor(R.color.grey_40),
                PorterDuff.Mode.SRC_IN);
        binding.ivAkun.setColorFilter(getResources().getColor(R.color.grey_40),
                PorterDuff.Mode.SRC_IN);

        // Change TextView
        binding.tvStore.setTextColor(getResources().getColor(R.color.grey_40));
        binding.tvPronews.setTextColor(getResources().getColor(R.color.grey_40));
        binding.tvRiwayat.setTextColor(getResources().getColor(R.color.grey_40));
        binding.tvAkun.setTextColor(getResources().getColor(R.color.grey_40));
    }

    private void chanegeColorsTinyStore() {
        // Change ImageView
        binding.ivStore.setColorFilter(getResources().getColor(R.color.global__primary),
                PorterDuff.Mode.SRC_IN);
        binding.ivPronews.setColorFilter(getResources().getColor(R.color.grey_40),
                PorterDuff.Mode.SRC_IN);
        binding.ivRiwayat.setColorFilter(getResources().getColor(R.color.grey_40),
                PorterDuff.Mode.SRC_IN);
        binding.ivAkun.setColorFilter(getResources().getColor(R.color.grey_40),
                PorterDuff.Mode.SRC_IN);

        // Change TextView
        binding.tvStore.setTextColor(getResources().getColor(R.color.global__primary));
        binding.tvPronews.setTextColor(getResources().getColor(R.color.grey_40));
        binding.tvRiwayat.setTextColor(getResources().getColor(R.color.grey_40));
        binding.tvAkun.setTextColor(getResources().getColor(R.color.grey_40));
    }

    private void chanegeColorsTinyPronews() {
        // Change ImageView
        binding.ivStore.setColorFilter(getResources().getColor(R.color.grey_40),
                PorterDuff.Mode.SRC_IN);
        binding.ivPronews.setColorFilter(getResources().getColor(R.color.global__primary),
                PorterDuff.Mode.SRC_IN);
        binding.ivRiwayat.setColorFilter(getResources().getColor(R.color.grey_40),
                PorterDuff.Mode.SRC_IN);
        binding.ivAkun.setColorFilter(getResources().getColor(R.color.grey_40),
                PorterDuff.Mode.SRC_IN);

        // Change TextView
        binding.tvStore.setTextColor(getResources().getColor(R.color.grey_40));
        binding.tvPronews.setTextColor(getResources().getColor(R.color.global__primary));
        binding.tvRiwayat.setTextColor(getResources().getColor(R.color.grey_40));
        binding.tvAkun.setTextColor(getResources().getColor(R.color.grey_40));
    }

    private void chanegeColorsTinyRiwayat() {
        // Change ImageView
        binding.ivStore.setColorFilter(getResources().getColor(R.color.grey_40),
                PorterDuff.Mode.SRC_IN);
        binding.ivPronews.setColorFilter(getResources().getColor(R.color.grey_40),
                PorterDuff.Mode.SRC_IN);
        binding.ivRiwayat.setColorFilter(getResources().getColor(R.color.global__primary),
                PorterDuff.Mode.SRC_IN);
        binding.ivAkun.setColorFilter(getResources().getColor(R.color.grey_40),
                PorterDuff.Mode.SRC_IN);

        // Change TextView
        binding.tvStore.setTextColor(getResources().getColor(R.color.grey_40));
        binding.tvPronews.setTextColor(getResources().getColor(R.color.grey_40));
        binding.tvRiwayat.setTextColor(getResources().getColor(R.color.global__primary));
        binding.tvAkun.setTextColor(getResources().getColor(R.color.grey_40));
    }

    private void chanegeColorsTinyAkun() {
        // Change ImageView
        binding.ivStore.setColorFilter(getResources().getColor(R.color.grey_40),
                PorterDuff.Mode.SRC_IN);
        binding.ivPronews.setColorFilter(getResources().getColor(R.color.grey_40),
                PorterDuff.Mode.SRC_IN);
        binding.ivRiwayat.setColorFilter(getResources().getColor(R.color.grey_40),
                PorterDuff.Mode.SRC_IN);
        binding.ivAkun.setColorFilter(getResources().getColor(R.color.global__primary),
                PorterDuff.Mode.SRC_IN);

        // Change TextView
        binding.tvStore.setTextColor(getResources().getColor(R.color.grey_40));
        binding.tvPronews.setTextColor(getResources().getColor(R.color.grey_40));
        binding.tvRiwayat.setTextColor(getResources().getColor(R.color.grey_40));
        binding.tvAkun.setTextColor(getResources().getColor(R.color.global__primary));
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void checkUserId() {
        if (!userId.isEmpty()) {
            if (userId.equals(notificationBuyerId)) {
                receiverId = notificationSellerId;
                receiverName = notificationSenderName;
                receiverUrl = notificationSenderUrl;
                requestCode = Constants.REQUEST_CODE__SELLER_CHAT_FRAGMENT;
                flag = Constants.CHAT_FROM_SELLER;
            }
            if (userId.equals(notificationSellerId)) {
                receiverId = notificationBuyerId;
                receiverName = notificationSenderName;
                receiverUrl = notificationSenderUrl;
                requestCode = Constants.REQUEST_CODE__BUYER_CHAT_FRAGMENT;
                flag = Constants.CHAT_FROM_BUYER;
            }
        }
    }

    private void getIntentData() {
        loginUserId = pref.getString(Constants.USER_ID, Constants.EMPTY_STRING);
        userType = pref.getString(Constants.USER_TYPE,Constants.EMPTY_STRING);
        address = pref.getString(Constants.USER_ADDRESS,Constants.EMPTY_STRING);
        nama_umkm = pref.getString(Constants.NAMA_UMKM,Constants.EMPTY_STRING);
        address_detail = pref.getString(Constants.USER_DETAIL_ADDRESS,Constants.EMPTY_STRING);

        selectedLocationId = getIntent().getStringExtra(Constants.SELECTED_LOCATION_ID);
        selectedLocationName = getIntent().getStringExtra(Constants.SELECTED_LOCATION_NAME);
        selected_lat = getIntent().getStringExtra(Constants.LAT);
        selected_lng = getIntent().getStringExtra(Constants.LNG);

        pref.edit().putString(Constants.USER_ADDRESS, address).apply();
        pref.edit().putString(Constants.USER_DETAIL_ADDRESS, address_detail).apply();
        pref.edit().putString(Constants.NAMA_UMKM, nama_umkm).apply();

        pref.edit().putString(Constants.SELECTED_LOCATION_NAME, selectedLocationName).apply();
        pref.edit().putString(Constants.LAT, selected_lat).apply();
        pref.edit().putString(Constants.LNG, selected_lng).apply();

        notificationItemId = getIntent().getStringExtra(Constants.NOTI_ITEM_ID);
        notificationMsg = getIntent().getStringExtra(Constants.NOTI_MSG);
        notificationBuyerId = getIntent().getStringExtra(Constants.NOTI_BUYER_ID);
        notificationSellerId = getIntent().getStringExtra(Constants.NOTI_SELLER_ID);
        notificationSenderName = getIntent().getStringExtra(Constants.NOTI_SENDER_NAME);
        notificationSenderUrl = getIntent().getStringExtra(Constants.NOTI_SENDER_URL);

        userId = pref.getString(Constants.USER_ID, Constants.EMPTY_STRING);

        // frist change icon penjual and pembeli
        if (userType.equals("penjual")){
            binding.addButton.setVisibility(View.VISIBLE);
            binding.btnCallCenter.setVisibility(View.GONE);
        }else if(userType.equals("pembeli")){
            binding.addButton.setVisibility(View.GONE);
            binding.btnCallCenter.setVisibility(View.VISIBLE);
        }else{
            binding.addButton.setVisibility(View.GONE);
            binding.btnCallCenter.setVisibility(View.VISIBLE);
        }

        checkUserId();

        if (notificationItemId != null) {
            if (loginUserId.isEmpty()) {

                psDialogMsg.showInfoDialog(getString(R.string.error_message__login_first), getString(R.string.app__ok));
                psDialogMsg.show();
                psDialogMsg.okButton.setOnClickListener(v1 -> {
                    psDialogMsg.cancel();
                    navigationController.navigateToPhoneLoginActivity(MainActivity.this);
                });

            } else {
                navigationController.navigateToChatActivity(MainActivity.this, notificationItemId, receiverId, receiverName, "", "", "",
                        "", "", flag, notificationSenderUrl, requestCode);
            }

        }

        if ((notificationItemId == null || notificationItemId.isEmpty()) && (notificationMsg != null)) {
            showAlertMessage(notificationMsg);
        }

    }

    private void initNavigationView() {

//        if (binding.navView != null) {
//
//            // Updating Custom Fonts
//            Menu m = binding.navView.getMenu();
//            try {
//                if (m != null) {
//
//                    for (int i = 0; i < m.size(); i++) {
//                        MenuItem mi = m.getItem(i);
//
//                        //for applying a font to subMenu ...
//                        SubMenu subMenu = mi.getSubMenu();
//                        if (subMenu != null && subMenu.size() > 0) {
//                            for (int j = 0; j < subMenu.size(); j++) {
//                                MenuItem subMenuItem = subMenu.getItem(j);
//
//                                subMenuItem.setTitle(subMenuItem.getTitle());
//                                // update font
//
//                                subMenuItem.setTitle(Utils.getSpannableString(getBaseContext(), subMenuItem.getTitle().toString(), Utils.Fonts.ROBOTO));
//
//                            }
//                        }
//
//                        mi.setTitle(mi.getTitle());
//                        // update font
//
//                        mi.setTitle(Utils.getSpannableString(getBaseContext(), mi.getTitle().toString(), Utils.Fonts.ROBOTO));
//                    }
//                }
//            } catch (Exception e) {
//                Utils.psErrorLog("Error in Setting Custom Font", e);
//            }
//
////            binding.navView.setNavigationItemSelectedListener(menuItem -> {
////                navigationMenuChanged(menuItem);
////                return true;
////            });
//
//        }

//        if (binding.bottomNavigationView != null) {
//
//            // Updating Custom Fonts
//            Menu m = binding.bottomNavigationView.getMenu();
//            try {
//
//                for (int i = 0; i < m.size(); i++) {
//                    MenuItem mi = m.getItem(i);
//
//                    //for applying a font to subMenu ...
//                    SubMenu subMenu = mi.getSubMenu();
//                    if (subMenu != null && subMenu.size() > 0) {
//                        for (int j = 0; j < subMenu.size(); j++) {
//                            MenuItem subMenuItem = subMenu.getItem(j);
//
//                            subMenuItem.setTitle(subMenuItem.getTitle());
//                            // update font
//
//                            subMenuItem.setTitle(Utils.getSpannableString(getBaseContext(), subMenuItem.getTitle().toString(), Utils.Fonts.ROBOTO));
//
//                        }
//                    }
//
//                    mi.setTitle(mi.getTitle());
//                    // update font
//
//                    mi.setTitle(Utils.getSpannableString(getBaseContext(), mi.getTitle().toString(), Utils.Fonts.ROBOTO));
//                }
//            } catch (Exception e) {
//                Utils.psErrorLog("Error in Setting Custom Font", e);
//            }
//
////            binding.navView.setNavigationItemSelectedListener(menuItem -> {
////                navigationMenuChanged(menuItem);
////                return true;
////            });
//
//        }

    }

    public void hideBottomNavigation() {
        //binding.bottomNavigationView.setVisibility(View.GONE);
        //binding.addItemButton.setVisibility(View.GONE);

        //Utils.removeToolbarScrollFlag(binding.toolbar);

    }

    private void showBottomNavigation() {
        //binding.bottomNavigationView.setVisibility(View.VISIBLE);
        //binding.addItemButton.setVisibility(View.GONE);
        //Utils.addToolbarScrollFlag(binding.toolbar);

    }

    private void navigationMenuChanged(MenuItem menuItem) {
        //openFragment(menuItem.getItemId());

        if (menuItem.getItemId() != R.id.nav_logout_login) {
            menuItem.setChecked(true);
            //binding.drawerLayout.closeDrawers();
        }
    }

    //public void setSelectMenu(int id) {
//        binding.navView.setCheckedItem(id);
//    }

    private int menuId = 0;

    /**
     * Initialize Data
     */
    private void initData() {

        try {
            notificationSetting = pref.getBoolean(Constants.NOTI_SETTING, false);
            token = pref.getString(Constants.NOTI_TOKEN, "");
        } catch (NullPointerException ne) {
            Utils.psErrorLog("Null Pointer Exception.", ne);
        } catch (Exception e) {
            Utils.psErrorLog("Error in getting notification flag data.", e);
        }

        try {
            loginUserId = pref.getString(Constants.USER_ID, Constants.EMPTY_STRING);
        } catch (Exception e) {
            Utils.psErrorLog("", e);
        }

        userViewModel.getLoginUser().observe(this, data -> {

            if (data != null) {

                if (data.size() > 0) {

                    user = data.get(0).user;
                    address = user.alamat;
                    nama_umkm = user.namaUmkm;
                    address_detail = user.kecamatan+" "+user.kabupaten+" "+user.provinsi;
                    userType = user.userType;

                    pref.edit().putString(Constants.USER_ADDRESS, user.alamat).apply();
                    pref.edit().putString(Constants.USER_DETAIL_ADDRESS, address_detail).apply();
                    pref.edit().putString(Constants.NAMA_UMKM, nama_umkm).apply();
                    pref.edit().putString(Constants.USER_TYPE, user.userType).apply();
                    pref.edit().putString(Constants.USER_ID, user.userId).apply();
                    pref.edit().putString(Constants.USER_NAME, user.userName).apply();
                    pref.edit().putString(Constants.USER_EMAIL, user.userEmail).apply();
                    pref.edit().putString(Constants.USER_PASSWORD, user.userEmail).apply();

                } else {
                    user = null;

                    pref.edit().remove(Constants.USER_ADDRESS).apply();
                    pref.edit().remove(Constants.USER_DETAIL_ADDRESS).apply();
                    pref.edit().remove(Constants.NAMA_UMKM).apply();
                    pref.edit().remove(Constants.USER_TYPE).apply();
                    pref.edit().remove(Constants.USER_ID).apply();
                    pref.edit().remove(Constants.USER_NAME).apply();
                    pref.edit().remove(Constants.USER_EMAIL).apply();
                    pref.edit().remove(Constants.USER_PASSWORD).apply();

                }

            }
            else {

                user = null;
                pref.edit().remove(Constants.USER_ADDRESS).apply();
                pref.edit().remove(Constants.USER_DETAIL_ADDRESS).apply();
                pref.edit().remove(Constants.USER_TYPE).apply();
                pref.edit().remove(Constants.NAMA_UMKM).apply();
                pref.edit().remove(Constants.USER_ID).apply();
                pref.edit().remove(Constants.USER_NAME).apply();
                pref.edit().remove(Constants.USER_EMAIL).apply();
                pref.edit().remove(Constants.USER_PASSWORD).apply();

            }

            if(userType.toLowerCase().equals("penjual")) {
                binding.addButton.setVisibility(View.VISIBLE);
                binding.btnCallCenter.setVisibility(View.GONE);
            }else{
                binding.addButton.setVisibility(View.GONE);
                binding.btnCallCenter.setVisibility(View.VISIBLE);
            }

            if (isLogout) {
                navigationController.navigateToHome(MainActivity.this, false, address, address_detail, selectedLocationId, selectedLocationName,true);
                showBottomNavigation();
                refreshUserData();
                isLogout = false;
                FirebaseAuth.getInstance().signOut();
                mGoogleSignInClient.revokeAccess()
                        .addOnCompleteListener(this, task -> {
                            // ...
                        });

                LoginManager.getInstance().logOut();

            }

        });

        registerNotificationToken(); // Just send "" because don't have token to sent. It will get token itself.

        psCountViewModel = new ViewModelProvider(this, viewModelFactory).get(PSCountViewModel.class);

        LiveData<Resource<PSCount>> chatHistoryListData = psCountViewModel.getPSCount();

        if (chatHistoryListData != null) {
            chatHistoryListData.observe(this, psCountResource -> {
                if (psCountResource != null) {

                    Utils.psLog("Got Data" + psCountResource.message + psCountResource.toString());

                    switch (psCountResource.status) {
                        case LOADING:
                            // Loading State
                            // Data are from Local DB

//                            if (psCountResource.data != null) {
//                                // Update the data
//                                // Notification
//                                notificationCount = psCountResource.data.blogNotiUnreadCount;
////                                if (notificationTextView != null) {
////                                    if (notificationCount.equals("0")) {
////                                        notificationTextView.setVisibility(View.GONE);
////                                    } else {
////                                        notificationTextView.setVisibility(View.VISIBLE);
////
////                                        int count = Integer.valueOf(notificationCount);
////                                        if (count > 9) {
////                                            notificationTextView.setText("9+");
////                                        } else {
////                                            notificationTextView.setText(String.valueOf(count));
////                                        }
////                                    }
////                                }
//
//
//                                // Message
//                                int sellerCount = Integer.valueOf(psCountResource.data.sellerUnreadCount);
//                                int buyerCount = Integer.valueOf(psCountResource.data.buyerUnreadCount);
//                                int totalCount = sellerCount + buyerCount;
//                                if (totalCount == 0) {
//                                    // messageNotificationTextView.setVisibility(View.GONE);
//                                } else {
//                                    // messageNotificationTextView.setVisibility(View.VISIBLE);
//
//                                    if (totalCount > 9) {
//                                        // messageNotificationTextView.setText("9+");
//                                    } else {
//                                        // messageNotificationTextView.setText(String.valueOf(totalCount));
//                                    }
//                                }
//
//                            } else {
//                                // messageNotificationTextView.setVisibility(View.GONE);
//                                if (notificationTextView != null) {
//                                    // notificationTextView.setVisibility(View.GONE);
//                                }
//                            }

                            break;

                        case SUCCESS:
                            // Success State
                            // Data are from Server

//                            if (psCountResource.data != null) {
//                                // Update the data
//                                // Notification
//                                notificationCount = psCountResource.data.blogNotiUnreadCount;
//                                if (notificationTextView != null) {
//                                    if (notificationCount.equals("0")) {
//                                        notificationTextView.setVisibility(View.GONE);
//                                    } else {
//                                        notificationTextView.setVisibility(View.VISIBLE);
//
//                                        int count = Integer.valueOf(notificationCount);
//                                        if (count > 9) {
//                                            notificationTextView.setText("9+");
//                                        } else {
//                                            notificationTextView.setText(String.valueOf(count));
//                                        }
//                                    }
//                                }
//
//                                // Message
//                                int sellerCount = Integer.valueOf(psCountResource.data.sellerUnreadCount);
//                                int buyerCount = Integer.valueOf(psCountResource.data.buyerUnreadCount);
//                                int totalCount = sellerCount + buyerCount;
//                                if (totalCount == 0) {
//                                    messageNotificationTextView.setVisibility(View.GONE);
//                                } else {
//                                    messageNotificationTextView.setVisibility(View.VISIBLE);
//
//                                    if (totalCount > 9) {
//                                        messageNotificationTextView.setText("9+");
//                                    } else {
//                                        messageNotificationTextView.setText(String.valueOf(totalCount));
//                                    }
//                                }
//
//                            } else {
//                                messageNotificationTextView.setVisibility(View.GONE);
//                                if (notificationTextView != null) {
//                                    notificationTextView.setVisibility(View.GONE);
//                                }
//                            }

                            psCountViewModel.setLoadingState(false);

                            break;

                        case ERROR:
                            // Error State
//                            messageNotificationTextView.setVisibility(View.GONE);
//                            if (notificationTextView != null) {
//                                notificationTextView.setVisibility(View.GONE);
//                            }

                            psCountViewModel.setLoadingState(false);

                            break;
                        default:
                            // Default

                            break;
                    }

                } else {

                    // Init Object or Empty Data
                    Utils.psLog("Empty Data");

                    if (psCountViewModel.offset > 1) {
                        // No more data for this list
                        // So, Block all future loading
                        psCountViewModel.forceEndLoading = true;
                    }

                }

            });
        }
    }

    /**
     * This function will change the menu based on the user is logged in or not.
     */

//    private void updateMenu() {
//
//        if (user == null) {
//
//            binding.navView.getMenu().setGroupVisible(R.id.group_before_login, true);
//            binding.navView.getMenu().setGroupVisible(R.id.group_after_login, false);
//
//            setSelectMenu(R.id.nav_home);
//
//        } else {
//            binding.navView.getMenu().setGroupVisible(R.id.group_after_login, true);
//            binding.navView.getMenu().setGroupVisible(R.id.group_before_login, false);
//
//            if (menuId == R.id.nav_profile) {
//                setSelectMenu(R.id.nav_profile_login);
//            } else if (menuId == R.id.nav_profile_login) {
//                setSelectMenu(R.id.nav_profile_login);
//            } else {
//                setSelectMenu(R.id.nav_home_login);
//            }
//
//        }
//
//
//    }

    private void registerNotificationToken() {
        /*
         * Register Notification
         */

        // Check already submit or not
        // If haven't, submit to server
        if (!notificationSetting) {

            if (this.token.equals("")) {

                FirebaseInstanceId.getInstance().getInstanceId()
                        .addOnCompleteListener(task -> {
                            if (!task.isSuccessful()) {

                                return;
                            }

                            // Get new Instance ID token
                            if (task.getResult() != null) {
                                token = task.getResult().getToken();
                            }

                            notificationViewModel.registerNotification(getBaseContext(), Constants.PLATFORM, token,loginUserId);
                        });


            }
        } else {
            Utils.psLog("Notification Token is already registered. Notification Setting : true.");
        }
    }

    //endregion

    Menu menu = null;
    public void updateToolbarIconColor(int color) {
        toolbarIconColor = color;
        //updateMenuIconColor(menu, color);
    }

//    private void updateMenuIconColor(Menu menu, int color) {
//        if(menu != null) {
//            for (int i = 0; i < menu.size(); i++) {
//                ImageView notiImageView = menu.getItem(i).getActionView().findViewById(R.id.notiImageView);
//                if (notiImageView != null) {
//                    notiImageView.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
//                }
//            }
//        }
//    }

    public void updateMenuIconWhite() {
        //drawerToggle.setHomeAsUpIndicator(R.drawable.baseline_menu_white_24);
    }

    public void updateMenuIconGrey() {
        //drawerToggle.setHomeAsUpIndicator(R.drawable.baseline_menu_grey_24);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // getMenuInflater().inflate(R.menu.notification_menu, menu);

//        this.menu = menu;
//        // updateMenuIconColor(menu, toolbarIconColor);
//
//        for(int i = 0; i< menu.size(); i++) {
//            notificationIconDrawable = menu.getItem(i).getIcon();
//            notificationIconDrawable.setColorFilter(toolbarIconColor, PorterDuff.Mode.SRC_ATOP);
//        }
//        View itemView = menu.getItem(0).getActionView();
//
//        notificationTextView = itemView.findViewById(R.id.txtCount);
//
//        notificationTextView.setText(notificationCount);
//
//        if (notificationCount.equals("0")) {
//            notificationTextView.setVisibility(View.GONE);
//        } else {
//            notificationTextView.setVisibility(View.VISIBLE);
//
//            int count = Integer.valueOf(notificationCount);
//            if (count > 9) {
//                notificationTextView.setText("9+");
//            }
//        }
//        notificationIconImageView = itemView.findViewById(R.id.notiImageView);
//        notificationIconImageView.setOnClickListener(
//                v -> navigationController.navigateToNotificationList(this)
//        );

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
//        else if (item.getItemId() == R.id.action_notification) {
//            navigationController.navigateToNotificationList(this);
//        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }

    }

    private void checkConsentStatus() {

        // For Testing Open this
        ConsentInformation.getInstance(this).
                setDebugGeography(DebugGeography.DEBUG_GEOGRAPHY_EEA);

        ConsentInformation consentInformation = ConsentInformation.getInstance(this);
        String[] publisherIds = {getString(R.string.adview_publisher_key)};
        consentInformation.requestConsentInfoUpdate(publisherIds, new ConsentInfoUpdateListener() {
            @Override
            public void onConsentInfoUpdated(ConsentStatus consentStatus) {
                // User's consent status successfully updated.

                Utils.psLog(consentStatus.name());

                if (!consentStatus.name().equals(pref.getString(Config.CONSENTSTATUS_CURRENT_STATUS, Config.CONSENTSTATUS_CURRENT_STATUS)) || consentStatus.name().equals(Config.CONSENTSTATUS_UNKNOWN)) {
                    collectConsent();
                }
            }

            @Override
            public void onFailedToUpdateConsentInfo(String errorDescription) {
                // User's consent status failed to update.
                Utils.psLog("Failed to update");
            }
        });
    }

    private void collectConsent() {
        URL privacyUrl = null;
        try {
            privacyUrl = new URL(Config.POLICY_URL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            // Handle error.
        }

        form = new ConsentForm.Builder(this, privacyUrl)
                .withListener(new ConsentFormListener() {
                    @Override
                    public void onConsentFormLoaded() {
                        // Consent form loaded successfully.

                        Utils.psLog("Form loaded");

                        if (form != null) {
                            form.show();
                        }
                    }

                    @Override
                    public void onConsentFormOpened() {
                        // Consent form was displayed.

                        Utils.psLog("Form Opened");
                    }

                    @Override
                    public void onConsentFormClosed(
                            ConsentStatus consentStatus, Boolean userPrefersAdFree) {
                        // Consent form was closed.

                        pref.edit().putString(Config.CONSENTSTATUS_CURRENT_STATUS, consentStatus.name()).apply();
                        pref.edit().putBoolean(Config.CONSENTSTATUS_IS_READY_KEY, true).apply();
                        Utils.psLog("Form Closed");
                    }

                    @Override
                    public void onConsentFormError(String errorDescription) {
                        // Consent form error.

                        pref.edit().putBoolean(Config.CONSENTSTATUS_IS_READY_KEY, false).apply();
                        Utils.psLog("Form Error " + errorDescription);
                    }
                })
                .withPersonalizedAdsOption()
                .withNonPersonalizedAdsOption()
                .withAdFreeOption()
                .build();

        form.load();

    }
    //endregion

    //endregion
    public boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    public User getA(){
        return this.a;
    }
}
