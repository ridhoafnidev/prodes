package com.mrii.prodes.ui.city.selectedcity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.core.view.ViewCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.gms.ads.AdRequest;
import com.mrii.prodes.Config;
import com.mrii.prodes.MainActivity;
import com.mrii.prodes.R;
import com.mrii.prodes.api.ApiCLientTest;
import com.mrii.prodes.api.ApiClient;
import com.mrii.prodes.api.PSApiService;
import com.mrii.prodes.binding.FragmentDataBindingComponent;
import com.mrii.prodes.databinding.FragmentSelectedCityBinding;
import com.mrii.prodes.db.ItemDao;
import com.mrii.prodes.repository.test.ItemsTest;
import com.mrii.prodes.ui.category.adapter.CityCategoryAdapter;
import com.mrii.prodes.ui.category.list.CategoryListFragment;
import com.mrii.prodes.ui.common.DataBoundListAdapter;
import com.mrii.prodes.ui.common.PSFragment;
import com.mrii.prodes.ui.dashboard.adapter.DashBoardViewPagerAdapter;
import com.mrii.prodes.ui.item.adapter.ItemHorizontalListAdapter;
import com.mrii.prodes.ui.item.adapter.ItemPopulerHorizontalListAdapter;
import com.mrii.prodes.ui.item.search.searchlist.SearchListFragment;
import com.mrii.prodes.ui.subcategory.SubCategoryFragment;
import com.mrii.prodes.ui.user.ProfileFragment;
import com.mrii.prodes.utils.AutoClearedValue;
import com.mrii.prodes.utils.Constants;
import com.mrii.prodes.utils.PSDialogMsg;
import com.mrii.prodes.utils.Tools;
import com.mrii.prodes.utils.Utils;
import com.mrii.prodes.utils.ZoomOutPageTransformer;
import com.mrii.prodes.viewmodel.BannerViewModel;
import com.mrii.prodes.viewmodel.apploading.PSAPPLoadingViewModel;
import com.mrii.prodes.viewmodel.clearalldata.ClearAllDataViewModel;
import com.mrii.prodes.viewmodel.item.ItemViewModel;
import com.mrii.prodes.viewmodel.item.NewItemViewModel;
import com.mrii.prodes.viewmodel.item.PopularItemViewModel;
import com.mrii.prodes.viewmodel.item.RecentItemViewModel;
import com.mrii.prodes.viewmodel.itemcategory.ItemCategoryViewModel;
import com.mrii.prodes.viewmodel.itemfromfollower.ItemFromFollowerViewModel;
import com.mrii.prodes.viewmodel.user.UserViewModel;
import com.mrii.prodes.viewobject.Banner;
import com.mrii.prodes.viewobject.Blog;
import com.mrii.prodes.viewobject.Item;
import com.mrii.prodes.viewobject.ItemCategory;
import com.mrii.prodes.viewobject.User;
import com.mrii.prodes.viewobject.common.Resource;
import com.mrii.prodes.viewobject.holder.ItemParameterHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectedCityFragment extends PSFragment implements DataBoundListAdapter.DiffUtilDispatchedInterface {

    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    PSApiService apiService;
    private ItemCategoryViewModel itemCategoryViewModel;
    private PopularItemViewModel popularItemViewModel;
    private ItemViewModel itemViewModel;
    private RecentItemViewModel recentItemViewModel;
    private NewItemViewModel newItemViewModel;
    private BannerViewModel bannerViewModel;
    private ImageView[] dots;
    private boolean layoutDone = false;
    private ImageView[] dotsPopuler;
    private int loadingCount = 0;
    public ItemDao itemDao;
    private PSDialogMsg psDialogMsg;
    private PSAPPLoadingViewModel psappLoadingViewModel;
    // private PSAppInfoViewModel psAppInfoViewModel;
    private ClearAllDataViewModel clearAllDataViewModel;
    private ItemFromFollowerViewModel itemFromFollowerViewModel;
    private UserViewModel userViewModel;
    private ItemParameterHolder searchItemParameterHolder = new ItemParameterHolder().getRecentItem();
    private ItemParameterHolder itemParameterHolder = new ItemParameterHolder();

    Drawable notificationIconDrawable = null;

    private Runnable update;
    String notificationCount = "0";
    String address, addressDetail;
    private Runnable updatePopuler;
    private int NUM_PAGES = 5;
    private int NUM_PAGES_BLOG = 5;
    private int currentPage = 0;
    private int currentPagePopuler = 0;
    private boolean touched = false;
    private boolean touchedPopuler = false;
    private Timer unTouchedTimerPopuler;
    private Timer unTouchedTimer;
    private Handler handler = new Handler();
    private Handler handlerPopuler = new Handler();
    private boolean searchKeywordOnFocus = false;
    private TextView notificationTextView;
    private ImageView notificationIconImageView;
    @VisibleForTesting
    private AutoClearedValue<FragmentSelectedCityBinding> binding;
    private AutoClearedValue<ItemPopulerHorizontalListAdapter> popularItemListAdapter;
    private AutoClearedValue<ItemHorizontalListAdapter> recentItemListAdapter;
    private AutoClearedValue<ItemHorizontalListAdapter> followerItemListAdapter;
    private AutoClearedValue<DashBoardViewPagerAdapter> dashBoardViewPagerAdapter;
    private AutoClearedValue<CityCategoryAdapter> cityCategoryAdapter;
    private AutoClearedValue<ViewPager> viewPager;
    private AutoClearedValue<ViewPager2> viewPager2;
    private AutoClearedValue<LinearLayout> pageIndicatorLayout;
    private AutoClearedValue<LinearLayout> pagePopulerIndicatorLayout;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        FragmentSelectedCityBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_selected_city, container, false, dataBindingComponent);

        binding = new AutoClearedValue<>(this, dataBinding);

        binding.get().setLoadingMore(connectivity.isConnected());

        Tools.setSystemBarColor(getActivity(), R.color.global__primary_dark);

        // In Activity's onCreate() for instance
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            Window w = getActivity().getWindow();
//            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//        }

        return binding.get().getRoot();
    }

    Menu menu = null;
    private int toolbarIconColor = Color.GRAY;

    public void updateToolbarIconColor(int color) {
        toolbarIconColor = color;
        updateMenuIconColor(menu, color);
    }

    private void updateMenuIconColor(Menu menu, int color) {
        if(menu != null) {
            for (int i = 0; i < menu.size(); i++) {
                ImageView notiImageView = menu.getItem(i).getActionView().findViewById(R.id.notiImageView);
                if (notiImageView != null) {
                    notiImageView.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
                }
            }
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getActivity().getMenuInflater().inflate(R.menu.notification_menu, menu);

        this.menu = menu;
        updateMenuIconColor(menu, toolbarIconColor);

        for(int i = 0; i< menu.size(); i++) {
            notificationIconDrawable = menu.getItem(i).getIcon();
            notificationIconDrawable.setColorFilter(toolbarIconColor, PorterDuff.Mode.SRC_ATOP);
        }
        View itemView = menu.getItem(0).getActionView();

        notificationTextView = itemView.findViewById(R.id.txtCount);

        notificationTextView.setText(notificationCount);

        if (notificationCount.equals("0")) {
            notificationTextView.setVisibility(View.GONE);
        } else {
            notificationTextView.setVisibility(View.VISIBLE);

            int count = Integer.valueOf(notificationCount);
            if (count > 9) {
                notificationTextView.setText("9+");
            }
        }
        notificationIconImageView = itemView.findViewById(R.id.notiImageView);
        notificationIconImageView.setOnClickListener(
                v -> navigationController.navigateToNotificationList(getActivity())
        );

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_notification) {
            navigationController.navigateToNotificationList(getActivity());
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void initUIAndActions() {

        if (getActivity() instanceof MainActivity) {
            //((MainActivity) this.getActivity()).binding.toolbar.setBackgroundColor(getResources().getColor(R.color.global__primary));
            ((MainActivity) getActivity()).updateToolbarIconColor(Color.GRAY);
            ((MainActivity) getActivity()).updateMenuIconGrey();
            ((MainActivity) getActivity()).refreshPSCount();
        }

        getIntentData();

        if (Config.SHOW_ADMOB && connectivity.isConnected()) {
            AdRequest adRequest2 = new AdRequest.Builder()
                    .build();
            //binding.get().adView2.loadAd(adRequest2);
            AdRequest adRequest3 = new AdRequest.Builder()
                    .build();
            //binding.get().adView3.loadAd(adRequest3);
        }
        else {
            //binding.get().adView2.setVisibility(View.GONE);
            //binding.get().adView3.setVisibility(View.GONE);
        }

        psDialogMsg = new PSDialogMsg(getActivity(), false);

        viewPager = new AutoClearedValue<>(this, binding.get().blogViewPager);

        viewPager2 = new AutoClearedValue<>(this, binding.get().viewPagerRekomendasi);

        pageIndicatorLayout = new AutoClearedValue<>(this, binding.get().pagerIndicator);

        pagePopulerIndicatorLayout = new AutoClearedValue<>(this, binding.get().pagerPopulerIndicator);

        binding.get().popularViewAllTextView.setOnClickListener(v -> navigationController.navigateToHomeFilteringActivity(getActivity(), popularItemViewModel.popularItemParameterHolder, getString(R.string.selected_city_popular_item), selectedCityLat, selectedCityLng, Constants.MAP_MILES));

       binding.get().recentItemViewAllTextView.setOnClickListener(v -> navigationController.navigateToHomeFilteringActivity(getActivity(), recentItemViewModel.recentItemParameterHolder, getString(R.string.selected_city_recent), selectedCityLat, selectedCityLng, Constants.MAP_MILES));

       /* make condition to set text in header from user "penjual" and "pembeli" */
       if (pref.getString(Constants.USER_TYPE, Constants.EMPTY_STRING).equals("pembeli")){
           binding.get().tvAddress.setText("Hi, ");
           binding.get().tvDetailAddress.setText(pref.getString(Constants.USER_NAME, Constants.EMPTY_STRING));
       }else if(pref.getString(Constants.USER_TYPE, Constants.EMPTY_STRING).equals("penjual")){
           binding.get().tvAddress.setText("Hi, ");
           binding.get().tvDetailAddress.setText(pref.getString(Constants.NAMA_UMKM, Constants.EMPTY_STRING));
       }else{
           binding.get().tvAddress.setText("Hi, ");
           binding.get().tvDetailAddress.setText("Prodesian");
       }
       /*end*/

       binding.get().categoryViewAllFB.setOnClickListener(
                v ->
                  navigationController.navigateToCategoryActivity(getActivity())
       );

        binding.get().blogViewPager.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                binding.get().searchBoxEditText.clearFocus();
            }
        });

        binding.get().searchBoxEditText.setOnFocusChangeListener((v, hasFocus) -> {
            searchKeywordOnFocus = hasFocus;
            Utils.psLog("Focus " + hasFocus);
        });

        binding.get().searchBoxEditText.setOnKeyListener((v, keyCode, event) -> {
            if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                binding.get().searchBoxEditText.clearFocus();
                searchKeywordOnFocus = false;
                callSearchList();
                Utils.psLog("Down");
                return false;
            } else if (event.getAction() == KeyEvent.ACTION_UP) {
                Utils.psLog("Up");
            }
            return false;
        });

        binding.get().searchImageButton.setOnClickListener(v -> SelectedCityFragment.this.callSearchList());

        if (viewPager2.get() != null && viewPager2.get() != null && viewPager2.get() != null) {

            viewPager2.get().registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                    if (searchKeywordOnFocus) {
                        binding.get().searchBoxEditText.clearFocus();
                    }
                }

                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);
                    currentPagePopuler = position;

                    if (pagePopulerIndicatorLayout.get() != null) {
                        setupSliderPopulerPagination();
                    }

                    for (ImageView dotP : dotsPopuler) {
                        if (dotsPopuler != null) {
                            dotP.setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));
                        }
                    }

                    if (dotsPopuler != null && dotsPopuler.length > position) {
                        dotsPopuler[position].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));
                    }

                    touchedPopuler = true;

                    handlerPopuler.removeCallbacks(updatePopuler);

                    setUnTouchedTimerPopuler();
                }

            });
        }

        startPagerAutoSwipePopuler();

        if (viewPager.get() != null && viewPager.get() != null && viewPager.get() != null) {

            viewPager.get().setPageTransformer(true, new ZoomOutPageTransformer());

            viewPager.get().addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    if (searchKeywordOnFocus) {
                        binding.get().searchBoxEditText.clearFocus();
                    }
                }


                @Override
                public void onPageSelected(int position) {

                    currentPage = position;

                    if (pageIndicatorLayout.get() != null) {

                        setupSliderPagination();
                    }

                    for (ImageView dot : dots) {
                        if (dots != null) {
                            dot.setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));
                        }
                    }

                    if (dots != null && dots.length > position) {
                        dots[position].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));
                    }

                    touched = true;

                    handler.removeCallbacks(update);

                    setUnTouchedTimer();

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }

        startPagerAutoSwipe();

        if (force_update) {
            navigationController.navigateToForceUpdateActivity(this.getActivity(), force_update_title, force_update_msg);
        }
    }

    private void callSearchList() {
        searchItemParameterHolder.keyword = binding.get().searchBoxEditText.getText().toString();
        navigationController.navigateToHomeFilteringActivity(getActivity(), searchItemParameterHolder, searchItemParameterHolder.keyword, selectedCityLat, selectedCityLng, Constants.MAP_MILES);
    }

    @Override
    protected void initViewModels() {
        itemCategoryViewModel = new ViewModelProvider(this, viewModelFactory).get(ItemCategoryViewModel.class);
        recentItemViewModel = new ViewModelProvider(this, viewModelFactory).get(RecentItemViewModel.class);
        newItemViewModel = new ViewModelProvider(this, viewModelFactory).get(NewItemViewModel.class);
        popularItemViewModel = new ViewModelProvider(this, viewModelFactory).get(PopularItemViewModel.class);
        bannerViewModel = new ViewModelProvider(this, viewModelFactory).get(BannerViewModel.class);
        itemFromFollowerViewModel = new ViewModelProvider(this, viewModelFactory).get(ItemFromFollowerViewModel.class);
        userViewModel = new ViewModelProvider(this, viewModelFactory).get(UserViewModel.class);
        itemViewModel = new ViewModelProvider(this, viewModelFactory).get(ItemViewModel.class);
        clearAllDataViewModel = new ViewModelProvider(this, viewModelFactory).get(ClearAllDataViewModel.class);
    }

    @Override
    protected void initAdapters() {

        DashBoardViewPagerAdapter nvAdapter3 = new DashBoardViewPagerAdapter(dataBindingComponent, banner -> navigationController.navigateToBannerDetailActivity(SelectedCityFragment.this.getActivity(), banner.id));

        this.dashBoardViewPagerAdapter = new AutoClearedValue<>(this, nvAdapter3);
        viewPager.get().setAdapter(dashBoardViewPagerAdapter.get());

        binding.get().categoryViewAllWisata.setOnClickListener(v -> {
            navigationController.navigateToSubCategoryActivity(getActivity(), "cat445639833db3eff8b6cdb5510aa39faa", "Wisata");
        });

        binding.get().categoryViewAllKuliner.setOnClickListener(v -> {
            itemParameterHolder.cat_id = "catfa070dd5cc2a2c9c6196159f85480ff7";
            itemParameterHolder.isPaid = Constants.PAIDITEMFIRST;
            itemParameterHolder.location_id = selected_location_id;
            itemParameterHolder.lat = selectedLat;
            itemParameterHolder.lng = selectedLng;

            navigationController.navigateToHomeFilteringActivity(SelectedCityFragment.this.getActivity(), itemParameterHolder, "Kuliner", selectedCityLat, selectedCityLng, Constants.MAP_MILES);

        });

        binding.get().categoryViewAllKriya.setOnClickListener(v -> {
            // navigationController.navigateToSubCategoryActivity(getActivity(), "cat5dfc0138547b926dccc5ee269b1cd042", "Kriya");
            itemParameterHolder.cat_id = "cat5dfc0138547b926dccc5ee269b1cd042";
            itemParameterHolder.isPaid = Constants.PAIDITEMFIRST;
            itemParameterHolder.location_id = selected_location_id;
            itemParameterHolder.lat = selectedLat;
            itemParameterHolder.lng = selectedLng;

            navigationController.navigateToHomeFilteringActivity(SelectedCityFragment.this.getActivity(), itemParameterHolder, "Kriya", selectedCityLat, selectedCityLng, Constants.MAP_MILES);

        });

        CityCategoryAdapter cityCategoryAdapter = new CityCategoryAdapter(dataBindingComponent,
                category -> navigationController.navigateToSubCategoryActivity(getActivity(), category.id, category.name), this);
        this.cityCategoryAdapter = new AutoClearedValue<>(this, cityCategoryAdapter);

        //binding.get().cityCategoryRecyclerView.setAdapter(cityCategoryAdapter);

        //ItemHorizontalListAdapter followerItemListAdapter = new ItemHorizontalListAdapter(dataBindingComponent, item -> navigationController.navigateToItemDetailActivity(SelectedCityFragment.this.getActivity(), item.id, item.title), this);
        //this.followerItemListAdapter = new AutoClearedValue<>(this, followerItemListAdapter);
        //binding.get().followerRecyclerView.setAdapter(followerItemListAdapter);

        ItemPopulerHorizontalListAdapter popularAdapter = new ItemPopulerHorizontalListAdapter(dataBindingComponent, item -> navigationController.navigateToItemDetailActivity(SelectedCityFragment.this.getActivity(), item.id, item.title, item.catId, item.subCatId), this);

        //viewPager2.get().setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);

        viewPager2.get().setAdapter(popularAdapter);
        viewPager2.get().setClipToPadding(false);
        viewPager2.get().setClipChildren(false);
        viewPager2.get().setOffscreenPageLimit(20);

        viewPager2.get().getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
            CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
            compositePageTransformer.addTransformer(new MarginPageTransformer(20));
            compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
                @Override
                public void transformPage(@NonNull View page, float position) {
                    float r = 1 - Math.abs(position);
                    page.setScaleY(0.85f + r * 0.10f);
                }
            });

        viewPager2.get().setPageTransformer(compositePageTransformer);

//        viewPager2.setPageTransformer(new ViewPager2.PageTransformer() {
//            @Override
//            public void transformPage(@NonNull View page, float position) {
//
//                float r = 1 - Math.abs(position);
//
//                float myOffset =  0.85f + r * 0.15f;
//                if (viewPager2.getOrientation() == ViewPager2.ORIENTATION_HORIZONTAL) {
//                    if (ViewCompat.getLayoutDirection(viewPager2) == ViewCompat.LAYOUT_DIRECTION_RTL) {
//                        page.setTranslationX(-myOffset);
//                    } else {
//                        page.setTranslationX(myOffset);
//                    }
//                } else {
//                    page.setTranslationY(myOffset);
//                }
//            }
//        });

        this.popularItemListAdapter = new AutoClearedValue<>(this, popularAdapter);
        //->binding.get().popularItemRecyclerView.setAdapter(popularAdapter);

        ItemHorizontalListAdapter recentAdapter = new ItemHorizontalListAdapter(dataBindingComponent, item ->
                navigationController.navigateToItemDetailActivity(this.getActivity(), item.id, item.title, item.catId, item.subCatId), this);

        this.recentItemListAdapter = new AutoClearedValue<>(this, recentAdapter);
        binding.get().recentItemRecyclerView.setAdapter(recentAdapter);


    }

    private void replaceItemFromFollowerList(List<Item> itemList) {
        //this.followerItemListAdapter.get().replace(itemList);
        binding.get().executePendingBindings();
    }

    private void replaceRecentItemList(List<Item> itemList) {
        this.recentItemListAdapter.get().replace(itemList);
        binding.get().executePendingBindings();
    }

    private void replacePopularItemList(List<Item> itemList) {
        this.popularItemListAdapter.get().replace(itemList);
        binding.get().executePendingBindings();
    }

    private void replaceCityCategory(List<ItemCategory> categories) {
        cityCategoryAdapter.get().replace(categories);
        binding.get().executePendingBindings();
    }


    @Override
    protected void initData() {

        //rshowItemFromFollower();

        userViewModel.getLoginUser().observe(this, data -> {

            if (data != null) {

                if (data.size() > 0) {
                    userViewModel.user = data.get(0).user;
                    String address_detail = data.get(0).user.kecamatan+" "+data.get(0).user.kabupaten+" "+data.get(0).user.provinsi;
                    pref.edit().putString(Constants.USER_ADDRESS, data.get(0).user.alamat).apply();
                    pref.edit().putString(Constants.USER_DETAIL_ADDRESS, address_detail).apply();
                    pref.edit().putString(Constants.USER_TYPE, data.get(0).user.userType).apply();
                    pref.edit().putString(Constants.USER_ID, data.get(0).user.userId).apply();
                    pref.edit().putString(Constants.USER_NAME, data.get(0).user.userName).apply();
                    pref.edit().putString(Constants.USER_EMAIL, data.get(0).user.userEmail).apply();
                    pref.edit().putString(Constants.USER_PASSWORD, data.get(0).user.userEmail).apply();
                    pref.edit().putString(Constants.LAT, data.get(0).user.lat).apply();
                    pref.edit().putString(Constants.LNG, data.get(0).user.lng).apply();

                }
            }

        });

        clearAllDataViewModel.getDeleteAllDataData().observe(this, result -> {

            if (result != null) {
                switch (result.status) {

                    case ERROR:
                        break;

                    case SUCCESS:
                        break;
                }
            }
        });

        loadProducts();

    }

//    private void oke(){
//            Utils.psLog("yuyu test");
//
////        System.out.println("test limit :"+limit);
////                    System.out.println("test offset :"+offset);
//                    System.out.println("test loginUserId :"+loginUserId);
//                    System.out.println("test itemParameterHolder.keyword :"+itemParameterHolder.keyword);
//                    System.out.println("test itemParameterHolder.item_id :"+itemParameterHolder.item_id);
//                    System.out.println("test itemParameterHolder.cat_id :"+itemParameterHolder.cat_id);
//                    System.out.println("test itemParameterHolder.sub_cat_id :"+itemParameterHolder.sub_cat_id);
//                    System.out.println("test itemParameterHolder.location_id :"+itemParameterHolder.location_id);
//                    System.out.println("test itemParameterHolder.order_by :"+itemParameterHolder.order_by);
//                    System.out.println("test itemParameterHolder.order_type :"+itemParameterHolder.order_type);
//                    System.out.println("test itemParameterHolder.type_id :"+itemParameterHolder.type_id);
//                    System.out.println("test itemParameterHolder.price_type_id :"+itemParameterHolder.price_type_id);
//                    System.out.println("test itemParameterHolder.currency_id :"+itemParameterHolder.currency_id);
//                    System.out.println("test itemParameterHolder.min_price :"+itemParameterHolder.min_price);
//                    System.out.println("test itemParameterHolder.lat :"+itemParameterHolder.lat);
//                    System.out.println("test itemParameterHolder.lng :"+itemParameterHolder.lng);
//                    System.out.println("test itemParameterHolder.mapMiles :"+itemParameterHolder.mapMiles);
//                    System.out.println("test itemParameterHolder.userId :"+itemParameterHolder.userId);
//                    System.out.println("test itemParameterHolder.isPaid :"+itemParameterHolder.isPaid);
//                    System.out.println("test itemParameterHolder.status :"+itemParameterHolder.status);
//                    System.out.println("test itemParameterHolder.isPaid :"+itemParameterHolder.isPaid);
//                    System.out.println("test itemParameterHolder.kabupaten_kota_id :"+itemParameterHolder.kabupaten_kota_id);
//                    System.out.println("test itemParameterHolder.kecamatan_id :"+itemParameterHolder.kecamatan_id);
//                    System.out.println("test itemParameterHolder.kelurahan_desa_id :"+itemParameterHolder.kelurahan_desa_id);
//                    System.out.println("test API_URL :"+Config.APP_API_URL);
//
//            //loading = ProgressDialog.show(context, null, "harap tunggu...", true, false);
//            apiService = ApiCLientTest.getClient(Config.APP_API_URL).create(PSApiService.class);
//                apiService.searchItemTest("teampsisthebest", "10", "0", "nologinuser", itemParameterHolder.keyword, itemParameterHolder.search_type, itemParameterHolder.item_id, itemParameterHolder.cat_id, itemParameterHolder.sub_cat_id,
//                    itemParameterHolder.order_by, itemParameterHolder.order_type, itemParameterHolder.type_id, itemParameterHolder.price_type_id, itemParameterHolder.currency_id,
//                    itemParameterHolder.location_id, itemParameterHolder.deal_option_id, itemParameterHolder.condition_id, itemParameterHolder.max_price,
//                    itemParameterHolder.min_price, "", itemParameterHolder.lat, itemParameterHolder.lng, itemParameterHolder.mapMiles, itemParameterHolder.userId,itemParameterHolder.isPaid,
//                    itemParameterHolder.status, itemParameterHolder.kabupaten_kota_id, itemParameterHolder.kecamatan_id, itemParameterHolder.kelurahan_desa_id).enqueue(new Callback<List<ItemsTest>> () {
//                @Override
//                public void onResponse(Call<List<ItemsTest>> call, Response<List<ItemsTest>> response) {
//                    Utils.psLog("yuyu response:"+response);
//                    List<ItemsTest> items = new ArrayList<>();
//                    for (Integer x = 0 ; x < response.body().size(); x++ ){
//
//                        Utils.psLog("yuyu response body: "+response.body().get(x).getTitle());
//                    }
//
//                }
//
//                @Override
//                public void onFailure(Call<List<ItemsTest>> call, Throwable t) {
//                    Utils.psLog("yuyu error:"+t.getLocalizedMessage());
//                }
//            });
////            apiService.masterMatpelFindAll().enqueue(new Callback<ResponseMasterMatpel>() {
////                @Override
////                public void onResponse(Call<ResponseMasterMatpel> call, Response<ResponseMasterMatpel> response) {
////                    if (response.isSuccessful()) {
////                        //loading.dismiss();
////                        List<MasterMatpel> semuadosenItems = response.body().getMaster();
////                        List<String> listSpinner = new ArrayList<String>();
////
////                        for (int i = 0; i < semuadosenItems.size(); i++){
////                            listSpinner.add(semuadosenItems.get(i).getNama());
////                        }
////
////                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
////                                android.R.layout.simple_spinner_item, listSpinner);
////                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
////                        spinnerMasterMatpel.setAdapter(adapter);
////
////                        int id_matpel = Integer.parseInt(_select_matpel);
////                        // find matpel
////                        apiService.getMatpelById(id_matpel).enqueue(new Callback<ResponseDataMatpel>() {
////                            @Override
////                            public void onResponse(Call<ResponseDataMatpel> call, Response<ResponseDataMatpel> response) {
////                                System.out.println("Responnya :"+response);
////                                if (response.isSuccessful()) {
////                                    List<DataMatpel> datamatpels = response.body().getMaster();
////                                    for (int i = 0; i < datamatpels.size(); i++){
////                                        int position = Integer.parseInt(datamatpels.get(i).getMatpel());
////                                        Log.e("Error", ""+position);
////                                        int x = position - 1;
////                                        spinnerMasterMatpel.setSelection(x);
////                                    }
////                                } else {
////                                }
////                            }
////
////                            @Override
////                            public void onFailure(Call<ResponseDataMatpel> call, Throwable t) {
////                            }
////                        });
////
////                        spinnerMasterMatpel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
////                            @Override
////                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
////                                String selectedName = parent.getItemAtPosition(position).toString();
////                                for (MasterMatpel semuamastermatpel : semuadosenItems){
////                                    if (semuamastermatpel.getNama().equals(spinnerMasterMatpel.getSelectedItem().toString())){
////                                        loadDataMatpel(semuamastermatpel.getIdMasterMatpel());
////                                        idMatpel = semuamastermatpel.getIdMasterMatpel();
////                                    }
////                                }
////                            }
////
////                            @Override
////                            public void onNothingSelected(AdapterView<?> parent) {
////
////                            }
////                        });
////                    } else {
////                        //loading.dismiss();
////                        Toast.makeText(context, "Gagal mengambil mata pelajaran", Toast.LENGTH_SHORT).show();
////                    }
////                }
////
////                @Override
////                public void onFailure(Call<ResponseMasterMatpel> call, Throwable t) {
////                    //loading.dismiss();
////                    Toast.makeText(context, "Koneksi internet bermasalah", Toast.LENGTH_SHORT).show();
////                }
////            });
//
//
//    }

    private void showItemFromFollower() {
        if (loginUserId.isEmpty()) {
            hideForFollower();
        } else {
            showForFollower();
        }
    }

    private void showForFollower() {
        //binding.get().followerConstraintLayout.setVisibility(View.VISIBLE);
        //binding.get().followerTitleTextView.setVisibility(View.VISIBLE);
        //binding.get().followerViewAllTextView.setVisibility(View.VISIBLE);
        //binding.get().followerDescTextView.setVisibility(View.VISIBLE);
        //binding.get().followerRecyclerView.setVisibility(View.VISIBLE);
    }

    private void hideForFollower() {
        //binding.get().followerConstraintLayout.setVisibility(View.GONE);
        //binding.get().followerTitleTextView.setVisibility(View.GONE);
        //binding.get().followerViewAllTextView.setVisibility(View.GONE);
        //binding.get().followerDescTextView.setVisibility(View.GONE);
        //binding.get().followerRecyclerView.setVisibility(View.GONE);
    }

    private void getIntentData() {

        if (getActivity() != null) {
            recentItemViewModel.locationId = getActivity().getIntent().getStringExtra(Constants.SELECTED_LOCATION_ID);
            recentItemViewModel.locationName = getActivity().getIntent().getStringExtra(Constants.SELECTED_LOCATION_NAME);
            address = getActivity().getIntent().getStringExtra(Constants.USER_ADDRESS);
            addressDetail = getActivity().getIntent().getStringExtra(Constants.USER_DETAIL_ADDRESS);

            if (getArguments() != null) {
                recentItemViewModel.locationId = getArguments().getString(Constants.SELECTED_LOCATION_ID);
                recentItemViewModel.locationName = getArguments().getString(Constants.SELECTED_LOCATION_NAME);
                recentItemViewModel.locationLat = getArguments().getString(Constants.LAT);
                recentItemViewModel.locationLng = getArguments().getString(Constants.LNG);
                address = getActivity().getIntent().getStringExtra(Constants.USER_ADDRESS);
                addressDetail = getActivity().getIntent().getStringExtra(Constants.USER_DETAIL_ADDRESS);
            }

            recentItemViewModel.locationId = selected_location_id;
            recentItemViewModel.locationName = selected_location_name;
            recentItemViewModel.locationLat = selectedLat;
            recentItemViewModel.locationLng = selectedLng;

            recentItemViewModel.recentItemParameterHolder.location_id = recentItemViewModel.locationId;
            popularItemViewModel.popularItemParameterHolder.location_id = recentItemViewModel.locationId;
            searchItemParameterHolder.location_id = recentItemViewModel.locationId;

            //binding.get().locationTextView.setText(recentItemViewModel.locationName);

        }
    }

    private void loadProducts() {

        // Blog
        /* Set request 3 (three) data news feeds from remote and local database used in (bannerViewModel.java) */
        bannerViewModel.setNewsBannerObj(String.valueOf(Config.LIST_NEW_FIVE_FEED_COUNT_PAGER), String.valueOf(bannerViewModel.offset));

        /* Get response LiveData 3 data news from setThreeNewsFeedObj() function used in (bannerViewModel) */
        bannerViewModel.getNewsBannerData().observe(this, result -> {

            if (result != null) {
                switch (result.status) {
                    case SUCCESS:
                        replaceNewsFeedList(result.data);
                        bannerViewModel.setLoadingState(false);
                        break;

                    case LOADING:
                        replaceNewsFeedList(result.data);
                        break;

                    case ERROR:

                        bannerViewModel.setLoadingState(false);
                        break;
                }
            }

        });

        // City Category
        itemCategoryViewModel.setCategoryListObj(String.valueOf(Config.LIST_CATEGORY_COUNT), Constants.ZERO);

        itemCategoryViewModel.getCategoryListData().observe(this, listResource -> {

            if (listResource != null) {

                switch (listResource.status) {
                    case SUCCESS:

                        if (listResource.data != null) {

                            if (listResource.data.size() > 0) {
                                replaceCityCategory(listResource.data);
                            }

                        }
                        itemCategoryViewModel.setLoadingState(false);

                        break;

                    case LOADING:

                        if (listResource.data != null) {

                            if (listResource.data.size() > 0) {
                                replaceCityCategory(listResource.data);
                            }

                        }

                        break;

                    case ERROR:
                        itemCategoryViewModel.setLoadingState(false);
                        break;
                }
            }
        });

        // Popular OR Rekomendation Item
        /* Set request 5 (five) data rekomendation item form (PopulerItemViewModel.java)*/
        popularItemViewModel.setFiveREkomendationItemListByKeyObj(Utils.checkUserId(loginUserId), Config.LIMIT_FIVE_FROM_DB_COUNT, Constants.ZERO, popularItemViewModel.popularItemParameterHolder);

        /* Get LiveData populer item from (PopularItemViewModel) */
        popularItemViewModel.getFiveRekomendationItemListByKeyData().observe(this, listResource -> {

            if (listResource != null) {
                switch (listResource.status) {
                    case SUCCESS:

                        if (listResource.data != null) {
                            if (listResource.data.size() > 0) {
                                replacePopularItemList(listResource.data);
                            }
                        }

                        break;

                    case LOADING:

                        if (listResource.data != null) {
                            if (listResource.data.size() > 0) {
                                replacePopularItemList(listResource.data);
                            }
                        }

                        break;

                    case ERROR:
                        break;
                }
            }
        });

       // Recent OR New Item
        /* Set function 10 terbaru item from (RecentItemViewModel.java) */
        recentItemViewModel.setRecentItemListByKeyObj(Utils.checkUserId(loginUserId), Config.LIMIT_FIVE_FROM_DB_COUNT, Constants.ZERO, newItemViewModel.popularItemParameterHolder);

        //itemDao = new ItemDao();

        /*Get function 10 rekomendation item from (RecentItemViewModel.java)*/
        recentItemViewModel.getRecentItemListByKeyData().observe(this, listResource -> {

            if (listResource != null) {
                switch (listResource.status) {
                    case SUCCESS:

                        if (listResource.data != null) {
                            if (listResource.data.size() > 0) {
                                SelectedCityFragment.this.replaceRecentItemList(listResource.data);
                            }
                        }

                        break;

                    case LOADING:

                        if (listResource.data != null) {
                            if (listResource.data.size() > 0) {
                                SelectedCityFragment.this.replaceRecentItemList(listResource.data);
                            }
                        }
                        recentItemViewModel.setLoadingState(false);
                        break;

                    case ERROR:
                        break;
                }
            }
        });

        // Item from follower
        itemFromFollowerViewModel.setItemFromFollowerListObj(Utils.checkUserId(loginUserId), Config.LIMIT_FROM_DB_COUNT, Constants.ZERO);

        itemFromFollowerViewModel.getItemFromFollowerListData().observe(this, listResource -> {

            if (listResource != null) {
                switch (listResource.status) {
                    case LOADING:

                        if (listResource.data != null) {
                            if (listResource.data.size() > 0) {
                                replaceItemFromFollowerList(listResource.data);
                            }
                        }

                        break;
                    case SUCCESS:

                        if (listResource.data != null) {
                            if (listResource.data.size() > 0) {
                                replaceItemFromFollowerList(listResource.data);
                                showForFollower();
                            }
                        } else {
                            hideForFollower();
                        }
                        itemFromFollowerViewModel.setLoadingState(false);
                        break;

                    case ERROR:
                        break;
                }
            }
        });

        //endregion

        viewPager.get().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (binding.get() != null && viewPager.get() != null) {
                    if (viewPager.get().getChildCount() > 0) {
                        layoutDone = true;
                        loadingCount++;
                        hideLoading();
                        viewPager.get().getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                }
            }
        });
    }

    @Override
    public void onDispatched() {

//        if (homeLatestProductViewModel.loadingDirection == Utils.LoadingDirection.top) {
//
//            LinearLayoutManager layoutManager = (LinearLayoutManager)
//                    binding.get().productList.getLayoutManager();
//
//            if (layoutManager != null) {
//                layoutManager.scrollToPosition(0);
//            }
//
//        }
//
//        if (homeSearchProductViewModel.loadingDirection == Utils.LoadingDirection.top) {
//
//            GridLayoutManager layoutManager = (GridLayoutManager)
//                    binding.get().discountList.getLayoutManager();
//
//            if (layoutManager != null) {
//                layoutManager.scrollToPosition(0);
//            }
//
//        }
//
//        if (homeTrendingProductViewModel.loadingDirection == Utils.LoadingDirection.top) {
//
//            GridLayoutManager layoutManager = (GridLayoutManager)
//                    binding.get().trendingList.getLayoutManager();
//
//            if (layoutManager != null) {
//                layoutManager.scrollToPosition(0);
//            }
//
//        }
    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    private void setupSliderPopulerPagination() {

        int dotsCountPopuler = popularItemListAdapter.get().getItemCount();

        if (dotsCountPopuler > 0 && dotsPopuler == null) {

            dotsPopuler = new ImageView[dotsCountPopuler];

            if (binding.get() != null) {
                if (pagePopulerIndicatorLayout.get().getChildCount() > 0) {
                    pagePopulerIndicatorLayout.get().removeAllViewsInLayout();
                }
            }

            for (int i = 0; i < dotsCountPopuler; i++) {
                dotsPopuler[i] = new ImageView(getContext());
                dotsPopuler[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));

                LinearLayout.LayoutParams paramsPopuler = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

                paramsPopuler.setMargins(4, 0, 4, 0);

                pagePopulerIndicatorLayout.get().addView(dotsPopuler[i], paramsPopuler);
            }

            dotsPopuler[0].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));

        }

    }

    private void setupSliderPagination() {

        int dotsCount = dashBoardViewPagerAdapter.get().getCount();

        if (dotsCount > 0 && dots == null) {

            dots = new ImageView[dotsCount];

            if (binding.get() != null) {
                if (pageIndicatorLayout.get().getChildCount() > 0) {
                    pageIndicatorLayout.get().removeAllViewsInLayout();
                }
            }

            for (int i = 0; i < dotsCount; i++) {
                dots[i] = new ImageView(getContext());
                dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

                params.setMargins(2, 0, 2, 0);

                pageIndicatorLayout.get().addView(dots[i], params);
            }

            dots[0].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));

        }

    }

    private void hideLoading() {

        if (loadingCount == 3 && layoutDone) {

            // binding.get().loadingView.setVisibility(View.GONE);
            binding.get().loadHolder.setVisibility(View.GONE);
        }
    }

    private void startPagerAutoSwipePopuler() {

        updatePopuler = () -> {
            if (!touchedPopuler) {
                if (currentPagePopuler == NUM_PAGES) {
                    currentPagePopuler = 0;
                }

                if (viewPager2.get() != null) {
                    viewPager2.get().setCurrentItem( currentPagePopuler++, true);
                }

            }
        };

        Timer swipeTimerPopuler = new Timer();
        swipeTimerPopuler.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!searchKeywordOnFocus) {
                    handlerPopuler.post(updatePopuler);
                }
            }
        }, 1000, 3000);
    }

    private void startPagerAutoSwipe() {

        update = () -> {
            if (!touched) {
                if (currentPage == NUM_PAGES_BLOG) {
                    currentPage = 0;
                }

                if (viewPager.get() != null) {
                    viewPager.get().setCurrentItem(currentPage++, true);
                }

            }
        };

        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!searchKeywordOnFocus) {
                    handler.post(update);
                }
            }
        }, 1000, 3000);
    }

    private void setUnTouchedTimerPopuler() {
        if (unTouchedTimerPopuler == null)
        {
            unTouchedTimerPopuler = new Timer();
            unTouchedTimerPopuler.schedule(new TimerTask() {
                @Override
                public void run() {
                    touchedPopuler = false;
                    if (!searchKeywordOnFocus) {
                        handlerPopuler.post(updatePopuler);
                    }
                }
            }, 3000, 6000);
        }
        else
        {
            unTouchedTimerPopuler.cancel();
            unTouchedTimerPopuler.purge();

            unTouchedTimerPopuler = new Timer();
            unTouchedTimerPopuler.schedule(new TimerTask() {
                @Override
                public void run() {
                    touchedPopuler = false;
                    if (!searchKeywordOnFocus) {
                        handlerPopuler.post(updatePopuler);
                    }
                }
            }, 3000, 6000);
        }
    }

    private void setUnTouchedTimer() {

        if (unTouchedTimer == null) {
            unTouchedTimer = new Timer();
            unTouchedTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    touched = false;
                    if (!searchKeywordOnFocus) {
                        handler.post(update);
                    }
                }
            }, 3000, 6000);
        } else {
            unTouchedTimer.cancel();
            unTouchedTimer.purge();

            unTouchedTimer = new Timer();
            unTouchedTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    touched = false;
                    if (!searchKeywordOnFocus) {
                        handler.post(update);
                    }
                }
            }, 3000, 6000);
        }
    }

    private void replaceNewsFeedList(List<Banner> banners) {
        this.dashBoardViewPagerAdapter.get().replaceNewsBannerList(banners);
        binding.get().executePendingBindings();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            if (requestCode == Constants.REQUEST_CODE__SELECTED_CITY_FRAGMENT
                    && resultCode == Constants.RESULT_CODE__SEARCH_WITH_ITEM_LOCATION_TYPE) {

                recentItemViewModel.locationId = data.getStringExtra(Constants.ITEM_LOCATION_TYPE_ID);
                recentItemViewModel.locationName = data.getStringExtra(Constants.ITEM_LOCATION_TYPE_NAME);
                recentItemViewModel.locationLat = data.getStringExtra(Constants.LAT);
                recentItemViewModel.locationLng = data.getStringExtra(Constants.LNG);

                pref.edit().putString(Constants.SELECTED_LOCATION_ID, recentItemViewModel.locationId).apply();
                pref.edit().putString(Constants.SELECTED_LOCATION_NAME, recentItemViewModel.locationName).apply();
                pref.edit().putString(Constants.LAT, recentItemViewModel.locationLat).apply();
                pref.edit().putString(Constants.LNG, recentItemViewModel.locationLng).apply();

                if (getActivity() != null) {
                    navigationController.navigateToHome((MainActivity) getActivity(), true, address, addressDetail, recentItemViewModel.locationId,
                            recentItemViewModel.locationName,false);
                }
            }
        }
    }

    @Override
    public void onResume() {
        loadLoginUserId();
        super.onResume();
    }

}
