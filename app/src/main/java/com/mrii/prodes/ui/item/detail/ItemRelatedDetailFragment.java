package com.mrii.prodes.ui.item.detail;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.mrii.prodes.Config;
import com.mrii.prodes.R;
import com.mrii.prodes.binding.FragmentDataBindingComponent;
import com.mrii.prodes.databinding.FragmentItemBinding;
import com.mrii.prodes.databinding.FragmentItemRelatedBinding;
import com.mrii.prodes.ui.common.DataBoundListAdapter;
import com.mrii.prodes.ui.common.PSFragment;
import com.mrii.prodes.ui.gallery.adapter.GalleryAdapter;
import com.mrii.prodes.ui.item.adapter.ImageCoverItemDetailAdapter;
import com.mrii.prodes.ui.item.adapter.ItemHorizontalListAdapter;
import com.mrii.prodes.utils.AutoClearedValue;
import com.mrii.prodes.utils.Constants;
import com.mrii.prodes.utils.PSDialogMsg;
import com.mrii.prodes.utils.Utils;
import com.mrii.prodes.viewmodel.aboutus.AboutUsViewModel;
import com.mrii.prodes.viewmodel.image.ImageViewModel;
import com.mrii.prodes.viewmodel.item.FavouriteViewModel;
import com.mrii.prodes.viewmodel.item.ItemViewModel;
import com.mrii.prodes.viewmodel.item.PopularItemViewModel;
import com.mrii.prodes.viewmodel.item.SpecsViewModel;
import com.mrii.prodes.viewmodel.item.TouchCountViewModel;
import com.mrii.prodes.viewmodel.rating.RatingViewModel;
import com.mrii.prodes.viewobject.Image;
import com.mrii.prodes.viewobject.Item;
import com.mrii.prodes.viewobject.common.Resource;
import com.mrii.prodes.viewobject.common.Status;

import java.net.URLEncoder;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 */
public class ItemRelatedDetailFragment extends PSFragment implements DataBoundListAdapter.DiffUtilDispatchedInterface {

    private ImageViewModel imageViewModel;

    @VisibleForTesting
    //private AutoClearedValue<FragmentGalleryBinding> binding;
    private AutoClearedValue<GalleryAdapter> adapter;

    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    private ItemViewModel itemViewModel;
    private TouchCountViewModel touchCountViewModel;
    private AboutUsViewModel aboutUsViewModel;
    private FavouriteViewModel favouriteViewModel;
    private SpecsViewModel specsViewModel;
    private RatingViewModel ratingViewModel;
    private PSDialogMsg psDialogMsg;
    private ImageView imageView;
    private AutoClearedValue<ViewPager2> viewPager2;
    private Runnable update;
    private boolean touched = false;
    private boolean searchKeywordOnFocus = false;
    private int currentPage = 0;
    private GoogleMap map;
    private ImageView[] dots;
    private Handler handler = new Handler();
    private AutoClearedValue<LinearLayout> pageIndicatorLayout;
    private Timer unTouchedTimer;


    private PopularItemViewModel relatedItemViewModel;


    private AutoClearedValue<ItemHorizontalListAdapter> relatedItemListAdapter;

    @VisibleForTesting
    private AutoClearedValue<FragmentItemRelatedBinding> binding;
    //private AutoClearedValue<ItemPopulerHorizontalListAdapter> relatedItemListAdapter;
    private AutoClearedValue<ProgressDialog> prgDialog;
    private AutoClearedValue<ImageCoverItemDetailAdapter> imageCoverDetailListAdapter;
    //endregion

    //region Override Methods
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        FragmentItemRelatedBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_item_related, container, false, dataBindingComponent);

        binding = new AutoClearedValue<>(this, dataBinding);
        initializeMap(savedInstanceState);
        // imageView = binding.get().coverUserImageView;

        return binding.get().getRoot();
    }

    private void initializeMap(Bundle savedInstanceState) {
        try {
            if (this.getActivity() != null) {
                MapsInitializer.initialize(this.getActivity());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        binding.get().mapViewItem.onCreate(savedInstanceState);
        bindMap(selectedLat, selectedLng);

    }

    private void bindMap(String latValue, String lngValue) {
        binding.get().mapViewItem.onResume();

        binding.get().mapViewItem.getMapAsync(googleMap -> {
            map = googleMap;

            try {
                map.addMarker(new MarkerOptions()
                        .position(new LatLng(Double.valueOf(latValue), Double.valueOf(lngValue)))
                        .title("City Name"));

                //zoom
                if (!latValue.isEmpty() && !lngValue.isEmpty()) {
                    int zoomlevel = 15;
                    // Animating to the touched position
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(latValue), Double.parseDouble(lngValue)), zoomlevel));
                }
            } catch (Exception e) {
                Utils.psErrorLog("", e);
            }

        });
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void initUIAndActions() {

        itemViewModel.latValue = selectedLat;
        itemViewModel.lngValue = selectedLng;

        psDialogMsg = new PSDialogMsg(getActivity(), false);
        psDialogMsg.showInfoDialog(getString(R.string.error_message__login_first), getString(R.string.app__ok));

        prgDialog = new AutoClearedValue<>(this, new ProgressDialog(getActivity()));
        viewPager2 = new AutoClearedValue<>(this, binding.get().viewPagerCoverUserImageView);

        prgDialog.get().setMessage((Utils.getSpannableString(getContext(), getString(R.string.message__please_wait), Utils.Fonts.MM_FONT)));
        prgDialog.get().setCancelable(false);

        if (Config.SHOW_ADMOB && connectivity.isConnected()) {
            AdRequest adRequest = new AdRequest.Builder()
                    .build();
            // binding.get().adView.loadAd(adRequest);
        } else {
            // binding.get().adView.setVisibility(View.GONE);
        }

        if (viewPager2.get() != null && viewPager2.get() != null && viewPager2.get() != null) {

            viewPager2.get().registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    //super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                }

                @Override
                public void onPageSelected(int position) {
                    currentPage = position;

//                    if (pageIndicatorLayout.get() != null) {f
//
//                        setupSliderPagination();
//                    }
//
//                    for (ImageView dot : dots) {
//                        if (dots != null) {
//                            dot.setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));
//                        }
//                    }
//
//                    if (dots != null && dots.length > position) {
//                        dots[position].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));
//                    }

                    touched = true;

                    handler.removeCallbacks(update);

                    setUnTouchedTimer();
                }

            });
        }

//        binding.get().phoneTextView.setOnClickListener(v -> {
//            String number = binding.get().phoneTextView.getText().toString();
//            if (!(number.trim().isEmpty() || number.trim().equals("-"))) {
//                try {
//                    Uri uri = Uri.parse("smsto:" + number);
//                    Intent sendIntent = new Intent(Intent.ACTION_SENDTO, uri);
//                    sendIntent.putExtra(Intent.EXTRA_TEXT, "Hai Good Morning");
//                    sendIntent.setPackage("com.whatsapp");
//                    startActivity(sendIntent);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    Toast.makeText(getActivity(), "Ada kesalahan...", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//        });

        binding.get().waButton.setOnClickListener(v -> {
            String number = binding.get().phoneTextView.getText().toString();
                if (!(number.trim().isEmpty() || number.trim().equals("-"))) {
                try {
                    String text = "Halo, kami dari prodeser...";
                    String textEncoded = URLEncoder.encode(text, "UTF-8");
                    String url = "https://api.whatsapp.com/send?phone="+number+"&text="+textEncoded;
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setPackage("com.whatsapp");
                    i.setData(Uri.parse(url));
                    startActivity(i);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Aplikasi Whatsapp belum terpasang di device kamu. Harap install terlebih dahulu.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.get().mapViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationController.navigateToMapActivity(getActivity(), itemViewModel.itemContainer.lng, itemViewModel.itemContainer.lat, Constants.MAP);
            }
        });

        // binding.get().safetyTipButton.setOnClickListener(v -> navigationController.navigateToSafetyTipsActivity(getActivity()));

        // ini ya
        binding.get().meetTheSellerConstraintLayout.setOnClickListener(v -> navigationController.navigateToUserDetail(getActivity(), itemViewModel.otherUserId, itemViewModel.otherUserName));

        // binding.get().userNameActiveHourTextView.setOnClickListener(v -> navigationController.navigateToUserDetail(getActivity(), itemViewModel.otherUserId, itemViewModel.otherUserName));

        binding.get().imageViewShare.setOnClickListener(v -> {

            try {
                String uri = "http://www.ngebugcode.com/prodes/item/"+itemViewModel.itemId;
                shareImageUri(uri);
            } catch (Exception e) {
                Utils.psErrorLog("Error ItemFragment 288 shared", e);
            }

        });

        binding.get().countPhotoConstraint.setOnClickListener(v -> navigationController.navigateToGalleryActivity(ItemRelatedDetailFragment.this.getActivity(), Constants.IMAGE_TYPE_PRODUCT, itemViewModel.itemId));

        //binding.get().coverUserImageView.setOnClickListener(v -> navigationController.navigateToGalleryActivity(ItemFragment.this.getActivity(), Constants.IMAGE_TYPE_PRODUCT, itemViewModel.itemId));

        binding.get().editButton.setOnClickListener(v -> navigationController.navigateToItemEditActivity(getActivity(), itemViewModel.itemId, itemViewModel.locationId, itemViewModel.locationName));

        binding.get().soldTextView.setOnClickListener(v -> {
            if (binding.get().soldTextView.getText().equals(getResources().getString(R.string.item_detail__mark_sold))) {
                psDialogMsg.showConfirmDialog(getString(R.string.item_detail__confirm_sold_out), getString(R.string.app__ok), getString(R.string.message__cancel_close));
                psDialogMsg.show();

                psDialogMsg.okButton.setOnClickListener(v12 -> {
                    itemViewModel.setMarkAsSoldOutItemObj(itemViewModel.itemId, loginUserId);

                    psDialogMsg.cancel();
                });

                psDialogMsg.cancelButton.setOnClickListener(v1 -> psDialogMsg.cancel());

            }
        });

        binding.get().deleteButton.setOnClickListener(v -> {
            psDialogMsg.showConfirmDialog(getString(R.string.item_detail__confirm_delete), getString(R.string.app__ok), getString(R.string.message__cancel_close));
            psDialogMsg.show();

            psDialogMsg.okButton.setOnClickListener(v12 -> {
                itemViewModel.setDeleteItemObj(itemViewModel.itemId, loginUserId);

                psDialogMsg.cancel();
            });

            psDialogMsg.cancelButton.setOnClickListener(v1 -> psDialogMsg.cancel());

        });

        binding.get().ratingBarInformation.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                navigationController.navigateToRatingList(ItemRelatedDetailFragment.this.getActivity(), binding.get().getItem().user.userId);
            }
            return true;
        });

        binding.get().backImageView.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().finish();
            }
        });

        /* ini ya
        binding.get().statisticDownImageView.setOnClickListener(v -> {
            boolean show = Utils.toggleUpDownWithAnimation(v);
            if (show) {
                ViewAnimationUtil.expand(binding.get().viewConstraintLayout);
                ViewAnimationUtil.expand(binding.get().reviewConstraintLayout);
            } else {
                ViewAnimationUtil.collapse(binding.get().viewConstraintLayout);
                ViewAnimationUtil.collapse(binding.get().reviewConstraintLayout);
            }
        });

        binding.get().statisticTextView.setOnClickListener(v -> {
            boolean show = Utils.toggleUpDownWithAnimation(binding.get().statisticDownImageView);
            if (show) {
                ViewAnimationUtil.expand(binding.get().viewConstraintLayout);
                ViewAnimationUtil.expand(binding.get().reviewConstraintLayout);
            } else {
                ViewAnimationUtil.collapse(binding.get().viewConstraintLayout);
                ViewAnimationUtil.collapse(binding.get().reviewConstraintLayout);
            }
        });

        binding.get().locationTitleDownImageView.setOnClickListener(v -> {
            boolean show = Utils.toggleUpDownWithAnimation(v);
            if (show) {
                //if add more field ,wrap with constraint
                ViewAnimationUtil.expand(binding.get().viewOnMapTextView);
            } else {
                ViewAnimationUtil.collapse(binding.get().viewOnMapTextView);

            }
        });

        binding.get().locationTitleTextView.setOnClickListener(v -> {
            boolean show = Utils.toggleUpDownWithAnimation(binding.get().locationTitleDownImageView);
            if (show) {
                ViewAnimationUtil.expand(binding.get().viewOnMapTextView);
            } else {
                ViewAnimationUtil.collapse(binding.get().viewOnMapTextView);
            }
        });

        binding.get().meetTheSellerDownImageView.setOnClickListener(v -> {
            boolean show = Utils.toggleUpDownWithAnimation(v);
            if (show) {
                ViewAnimationUtil.expand(binding.get().meetTheSellerConstraintLayout);
            } else {
                ViewAnimationUtil.collapse(binding.get().meetTheSellerConstraintLayout);
            }
        });

        binding.get().meetTheSellerTextView.setOnClickListener(v -> {
            boolean show = Utils.toggleUpDownWithAnimation(binding.get().meetTheSellerDownImageView);
            if (show) {
                ViewAnimationUtil.expand(binding.get().meetTheSellerConstraintLayout);
            } else {
                ViewAnimationUtil.collapse(binding.get().meetTheSellerConstraintLayout);
            }
        });

//        binding.get().promoteDownImageView.setOnClickListener(v -> {
//            boolean show = Utils.toggleUpDownWithAnimation(v);
//            if (show) {
//                ViewAnimationUtil.expand(binding.get().itemPromoteConstraintLayout);
//            } else {
//                ViewAnimationUtil.collapse(binding.get().itemPromoteConstraintLayout);
//            }
//        });

//        binding.get().promoteTitleTextView.setOnClickListener(v -> {
//            boolean show = Utils.toggleUpDownWithAnimation(binding.get().promoteDownImageView);
//            if (show) {
//                ViewAnimationUtil.expand(binding.get().itemPromoteConstraintLayout);
//            } else {
//                ViewAnimationUtil.collapse(binding.get().itemPromoteConstraintLayout);
//            }
//        });

//        binding.get().gettingThisDownImageView.setOnClickListener(v -> {
//            boolean show = Utils.toggleUpDownWithAnimation(v);
//            if (show) {
//                ViewAnimationUtil.expand(binding.get().meetTextView);
//                ViewAnimationUtil.expand(binding.get().addressTextView);
//                ViewAnimationUtil.expand(binding.get().imageView25);
//            } else {
//                ViewAnimationUtil.collapse(binding.get().meetTextView);
//                ViewAnimationUtil.collapse(binding.get().addressTextView);
//                ViewAnimationUtil.collapse(binding.get().imageView25);
//            }
//        });
//
        binding.get().safetyTipsDownImageView.setOnClickListener(v -> {
            boolean show = Utils.toggleUpDownWithAnimation(v);
            if (show) {
                // ViewAnimationUtil.expand(binding.get().safetyTipButton);
                ViewAnimationUtil.expand(binding.get().imageView23);
                ViewAnimationUtil.expand(binding.get().categoryAndSubCategoryTextView);
                ViewAnimationUtil.expand(binding.get().informationTextView);
                ViewAnimationUtil.expand(binding.get().imageView30);

            } else {
                // ViewAnimationUtil.collapse(binding.get().safetyTipButton);
                ViewAnimationUtil.collapse(binding.get().imageView23);
                ViewAnimationUtil.collapse(binding.get().categoryAndSubCategoryTextView );
                ViewAnimationUtil.collapse(binding.get().informationTextView );
                ViewAnimationUtil.collapse(binding.get().imageView30 );
            }
        });
//
//        binding.get().gettingThisTextView.setOnClickListener(v -> {
//            boolean show = Utils.toggleUpDownWithAnimation(binding.get().gettingThisDownImageView);
//            if (show) {
//                ViewAnimationUtil.expand(binding.get().meetTextView);
//                ViewAnimationUtil.expand(binding.get().addressTextView);
//                ViewAnimationUtil.expand(binding.get().imageView25);
//            } else {
//                ViewAnimationUtil.collapse(binding.get().meetTextView);
//                ViewAnimationUtil.collapse(binding.get().addressTextView);
//                ViewAnimationUtil.collapse(binding.get().imageView25);
//            }
//        });
//
//        binding.get().safetyTitleTextView.setOnClickListener(v -> {
//            boolean show = Utils.toggleUpDownWithAnimation(binding.get().safetyTipsDownImageView);
//            if (show) {
//                ViewAnimationUtil.expand(binding.get().safetyTipButton);
//                ViewAnimationUtil.expand(binding.get().safetyTextView);
//
//            } else {
//                ViewAnimationUtil.collapse(binding.get().safetyTipButton);
//                ViewAnimationUtil.collapse(binding.get().safetyTextView);
//
//            }
//        });

/*
        end ini ya */

        binding.get().favouriteImageView.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {

                Item item = binding.get().getItem();
                if (item != null) {
                    favFunction(item, likeButton);
                }

            }

            @Override
            public void unLiked(LikeButton likeButton) {

                Item item = binding.get().getItem();
                if (item != null) {
                    unFavFunction(item, likeButton);
                }
            }
        });

        binding.get().chatButton.setOnClickListener(v -> {

            Utils.navigateOnUserVerificationActivity(userIdToVerify, loginUserId, psDialogMsg, ItemRelatedDetailFragment.this.getActivity(), navigationController, new Utils.NavigateOnUserVerificationActivityCallback() {
                @Override
                public void onSuccess() {

                    if (itemViewModel.currentItem.user.userId.isEmpty()) {
                        psDialogMsg.showWarningDialog(getString(R.string.item_entry_user_not_exit), getString(R.string.app__ok));
                        psDialogMsg.show();
                    } else {
                        navigationController.navigateToChatActivity(getActivity(),
                                itemViewModel.currentItem.id,
                                itemViewModel.currentItem.user.userId,
                                itemViewModel.currentItem.user.userName,
                                itemViewModel.currentItem.defaultPhoto.imgPath,
                                itemViewModel.currentItem.title,
                                itemViewModel.currentItem.itemCurrency.currencySymbol,
                                itemViewModel.currentItem.price,
                                itemViewModel.currentItem.itemCondition.name,
                                Constants.CHAT_FROM_SELLER,
                                itemViewModel.currentItem.user.userProfilePhoto,
                                0
                        );
                    }
                }
            });

            if (userIdToVerify.isEmpty()) {
                if (loginUserId.equals("")) {
                    navigationController.navigateToUserLoginChatActivity(getActivity());
                } else if (itemViewModel.currentItem.user.userId.isEmpty()) {
                    psDialogMsg.showWarningDialog(getString(R.string.item_entry_user_not_exit), getString(R.string.app__ok));
//                    psDialogMsg.show();
                } else {
                    navigationController.navigateToChatActivity(getActivity(),
                            itemViewModel.currentItem.id,
                            itemViewModel.currentItem.user.userId,
                            itemViewModel.currentItem.user.userName,
                            itemViewModel.currentItem.defaultPhoto.imgPath,
                            itemViewModel.currentItem.title,
                            itemViewModel.currentItem.itemCurrency.currencySymbol,
                            itemViewModel.currentItem.price,
                            itemViewModel.currentItem.itemCondition.name,
                            Constants.CHAT_FROM_SELLER,
                            itemViewModel.currentItem.user.userProfilePhoto,
                            0
                    );
                }
            } else {

                navigationController.navigateToVerifyEmailActivity(getActivity());
            }

        });

        binding.get().ratingBarInformation.setOnClickListener(v -> navigationController.navigateToRatingList(ItemRelatedDetailFragment.this.getActivity(), binding.get().getItem().user.userId));

//        binding.get().promoteButton.setOnClickListener(v -> {
//
//            Utils.navigateOnUserVerificationActivity(userIdToVerify, loginUserId, psDialogMsg, getActivity(), navigationController, new Utils.NavigateOnUserVerificationActivityCallback() {
//                @Override
//                public void onSuccess() {
//                    navigationController.navigateToItemPromoteActivity(ItemFragment.this.getActivity(), Constants.ADD_NEW_ITEM);
//                }
//            });
//
//        });

//        binding.get().promoteButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                navigationController.navigateToItemPromoteActivity(ItemFragment.this.getActivity(), itemViewModel.itemId);
//            }
//        });
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

    private void setupSliderPagination() {

        int dotsCount = imageCoverDetailListAdapter.get().getItemCount();

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

                params.setMargins(4, 0, 4, 0);

                pageIndicatorLayout.get().addView(dots[i], params);
            }

            dots[0].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        String number = binding.get().phoneTextView.getText().toString();
        if (!(number.trim().isEmpty() || number.trim().equals("-"))) {
            Utils.phoneCallPermissionResult(requestCode, grantResults, this, number);
        }
    }

    @Override
    protected void initViewModels() {
        relatedItemViewModel = new ViewModelProvider(this, viewModelFactory).get(PopularItemViewModel.class);
        imageViewModel = new ViewModelProvider(this, viewModelFactory).get(ImageViewModel.class);
        itemViewModel = new ViewModelProvider(this, viewModelFactory).get(ItemViewModel.class);
        ratingViewModel = new ViewModelProvider(this, viewModelFactory).get(RatingViewModel.class);
        specsViewModel = new ViewModelProvider(this, viewModelFactory).get(SpecsViewModel.class);
        favouriteViewModel = new ViewModelProvider(this, viewModelFactory).get(FavouriteViewModel.class);
        touchCountViewModel = new ViewModelProvider(this, viewModelFactory).get(TouchCountViewModel.class);
        aboutUsViewModel = new ViewModelProvider(this, viewModelFactory).get(AboutUsViewModel.class);

    }

    @Override
    protected void initAdapters() {

        GalleryAdapter nvAdapter = new GalleryAdapter(dataBindingComponent, this::imageClicked, this, getActivity());
        this.adapter = new AutoClearedValue<>(this, nvAdapter);

        //ImageCoverItemDetailAdapter itemCoverDetailAdapter = new ImageCoverItemDetailAdapter(dataBindingComponent, item -> navigationController.navigateToItemDetailActivity(ItemFragment.this.getActivity(), item.id, item.title), this);
        viewPager2.get().setAdapter(nvAdapter);
        viewPager2.get().setClipToPadding(false);
        viewPager2.get().setClipChildren(false);
        viewPager2.get().setOffscreenPageLimit(5);

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

        ItemHorizontalListAdapter relatedAdapter = new ItemHorizontalListAdapter(dataBindingComponent, item ->
                navigationController.navigateToItemDetailActivity(this.getActivity(), item.id, item.title, item.catId, item.subCatId), this);

        this.relatedItemListAdapter = new AutoClearedValue<>(this, relatedAdapter);
        binding.get().relatedItemRecyclerView.setAdapter(relatedAdapter);

    }

    private void imageClicked(Image image) {
        navigationController.navigateToDetailGalleryActivity(getActivity(), Constants.IMAGE_TYPE_PRODUCT, itemViewModel.itemId, image.imgId);
    }

    @Override
    protected void initData() {

        getIntentData();

        getCoverImageDetailItem();

        getItemDetail();

        getMarkAsSoldOutData();

        callTouchCount();

        getTouchCount();

        getFavData();

        getFavData();

        getReportItemStatus();

        getDeleteItemStatus();

        getAboutUsData();
        
        getRelatedItem();
    }

    private void getRelatedItem() {
        /* Set request 5 (five) data rekomendation item form (PopulerItemViewModel.java)*/
        relatedItemViewModel.setFiveREkomendationItemListByKeyObj(Utils.checkUserId(loginUserId), Config.LIMIT_FIVE_FROM_DB_COUNT, Constants.ZERO, relatedItemViewModel.popularItemParameterHolder);

        /* Get LiveData populer item from (PopularItemViewModel) */
        relatedItemViewModel.getFiveRekomendationItemListByKeyData().observe(this, listResource -> {

            if (listResource != null) {
                switch (listResource.status) {
                    case SUCCESS:

                        if (listResource.data != null) {
                            if (listResource.data.size() > 0) {
                                binding.get().viewEmptySpace.setVisibility(View.GONE);
                                binding.get().relatedItemRecyclerView.setVisibility(View.VISIBLE);
                                binding.get().tvRelatedProduct.setVisibility(View.VISIBLE);
                                replaceRelatedItemList(listResource.data);
                            }else{
                                binding.get().viewEmptySpace.setVisibility(View.VISIBLE);
                                binding.get().relatedItemRecyclerView.setVisibility(View.GONE);
                                binding.get().tvRelatedProduct.setVisibility(View.GONE);
                            }
                        }else{

                        }

                        break;

                    case LOADING:

                        if (listResource.data != null) {
                            if (listResource.data.size() > 0) {
                                replaceRelatedItemList(listResource.data);
                            }
                        }

                        break;

                    case ERROR:
                        break;
                }
            }
        });
    }

    private void replaceRelatedItemList(List<Item> itemList) {
        this.relatedItemListAdapter.get().replace(itemList);
        binding.get().executePendingBindings();
    }

    private void getCoverImageDetailItem() {
        imageViewModel.setImageParentId(Constants.IMAGE_TYPE_PRODUCT, itemViewModel.itemId);
        LiveData<Resource<List<Image>>> imageListLiveData = imageViewModel.getImageListLiveData();
        imageListLiveData.observe(this, listResource -> {
            // we don't need any null checks here for the adapter since LiveData guarantees that
            // it won't call us if fragment is stopped or not started.
            if (listResource != null && listResource.data != null) {
                Utils.psLog("Got Data");

                //fadeIn Animation
                fadeIn(binding.get().getRoot());
                // Update the data
                this.adapter.get().replace(listResource.data);
                this.binding.get().executePendingBindings();

            } else {
                //noinspection Constant Conditions
                Utils.psLog("Empty Data");
            }
        });
    }

    private void getAboutUsData() {
        aboutUsViewModel.setAboutUsObj("about us");
        aboutUsViewModel.getAboutUsData().observe(this, resource -> {

            if (resource != null) {

                switch (resource.status) {
                    case LOADING:
                        // Loading State
                        // Data are from Local DB

                        if (resource.data != null) {

                            fadeIn(binding.get().getRoot());

                        }
                        break;
                    case SUCCESS:
                        // Success State
                        // Data are from Server

                        if (resource.data != null) {

                            // binding.get().safetyTextView.setText(resource.data.safetyTips);
                        }

                        break;
                    case ERROR:
                        // Error State

                        break;
                    default:
                        // Default

                        break;
                }

            } else {

                // Init Object or Empty Data
                Utils.psLog("Empty Data");

            }


            // we don't need any null checks here for the adapter since LiveData guarantees that
            // it won't call us if fragment is stopped or not started.
            if (resource != null) {
                Utils.psLog("Got Data Of About Us.");


            } else {
                //noinspection Constant Conditions
                Utils.psLog("No Data of About Us.");
            }
        });
    }

    private void getDeleteItemStatus() {
        itemViewModel.getDeleteItemStatus().observe(this, result -> {

            if (result != null) {
                switch (result.status) {
                    case SUCCESS:

                        //add offer text
                        Toast.makeText(getContext(), "Berhasil menghapus data", Toast.LENGTH_SHORT).show();
                        if (getActivity() != null) {
                            getActivity().finish();
                        }

                        break;

                    case ERROR:
                        Toast.makeText(getContext(), "Gagal menghapus data", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    private void getMarkAsSoldOutData() {
        LiveData<Resource<Item>> itemDetail = itemViewModel.getMarkAsSoldOutItemData();
        if (itemDetail != null) {
            itemDetail.observe(this, listResource -> {
                if (listResource != null) {

                    switch (listResource.status) {
                        case LOADING:
                            // Loading State
                            // Data are from Local DB

                            if (listResource.data != null) {
                                //fadeIn Animation

                                fadeIn(binding.get().getRoot());

                            }

                            break;

                        case SUCCESS:
                            // Success State
                            // Data are from Server

                            if (listResource.data != null) {

                                Toast.makeText(getContext(), "Berhasil, barang sudah terjual", Toast.LENGTH_SHORT).show();

                            }

                            itemViewModel.setLoadingState(false);

                            break;

                        case ERROR:

                            // Error State
                            itemViewModel.setLoadingState(false);
//                            binding.get().markAsSoldButton.setVisibility(View.VISIBLE);

                            break;

                        default:
                            // Default

                            break;
                    }

                } else {

                    itemViewModel.setLoadingState(false);

                }
            });
        }
    }

    private void getReportItemStatus() {

        itemViewModel.getReportItemStatusData().observe(this, result -> {

            if (result != null) {
                switch (result.status) {
                    case SUCCESS:

                        //add offer text
                        Toast.makeText(getContext(), "Success Report this Item", Toast.LENGTH_SHORT).show();

                        break;

                    case ERROR:
                        Toast.makeText(getContext(), "Fail Report this item", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    private void getTouchCount() {

        //get touch count post method
        touchCountViewModel.getTouchCountPostData().observe(this, result -> {
            if (result != null) {
                if (result.status == Status.SUCCESS) {
                    if (ItemRelatedDetailFragment.this.getActivity() != null) {
                        Utils.psLog(result.status.toString());
                    }

                } else if (result.status == Status.ERROR) {
                    if (ItemRelatedDetailFragment.this.getActivity() != null) {
                        Utils.psLog(result.status.toString());
                    }
                }
            }
        });
    }

    private void getFavData() {
        //get favourite post method
        favouriteViewModel.getFavouritePostData().observe(this, result -> {
            if (result != null) {
                if (result.status == Status.SUCCESS) {
                    if (this.getActivity() != null) {
                        Utils.psLog(result.status.toString());
                        favouriteViewModel.setLoadingState(false);
                        itemViewModel.setItemDetailObj(itemViewModel.itemId, itemViewModel.historyFlag, loginUserId);
                    }

                } else if (result.status == Status.ERROR) {
                    if (this.getActivity() != null) {
                        Utils.psLog(result.status.toString());
                        favouriteViewModel.setLoadingState(false);
                    }
                }
            }
        });
    }

    private void getIntentData() {
        try {
            if (getActivity() != null) {
                if (getActivity().getIntent().getExtras() != null) {
                    itemViewModel.itemId = getActivity().getIntent().getExtras().getString(Constants.ITEM_ID);
                    itemViewModel.catId = getActivity().getIntent().getExtras().getString(Constants.ITEM_CATEGORY_ID);
                    itemViewModel.subCatId = getActivity().getIntent().getExtras().getString(Constants.ITEM_SUB_CATEGORY_ID);
                    itemViewModel.itemName = getActivity().getIntent().getExtras().getString(Constants.ITEM_NAME);
                    relatedItemViewModel.itemId = itemViewModel.itemId;
                    relatedItemViewModel.catId = itemViewModel.catId;
                    relatedItemViewModel.subCatId = itemViewModel.subCatId;
                    relatedItemViewModel.itemName = itemViewModel.itemName;

                    relatedItemViewModel.popularItemParameterHolder.cat_id = relatedItemViewModel.catId;
                    relatedItemViewModel.popularItemParameterHolder.item_id = relatedItemViewModel.itemId;
                    relatedItemViewModel.popularItemParameterHolder.sub_cat_id = relatedItemViewModel.subCatId;
                    relatedItemViewModel.popularItemParameterHolder.order_by = "added_date";
                    relatedItemViewModel.popularItemParameterHolder.order_type = "paid_item_first";
                    relatedItemViewModel.popularItemParameterHolder.search_type = "related_item";

                    itemViewModel.historyFlag = getActivity().getIntent().getExtras().getString(Constants.HISTORY_FLAG);

                }
            }
        } catch (Exception e) {
            Utils.psErrorLog("", e);
        }
    }

    private void shareImageUri(String uri) {

        new Thread(() -> {
            try {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, uri);
                intent.setType("text/plain");
                Intent shareIntent = Intent.createChooser(intent, null);

                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(shareIntent);
                }

                // startActivity(shareIntent);

                // Objects.requireNonNull(getContext()).startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private Bitmap getBitmapFromView(ImageView view) {
        Drawable drawable = view.getDrawable();

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    private ImageView getCurrentImageView() {
        return imageView;
    }

    private void getItemDetail() {

        itemViewModel.setItemDetailObj(itemViewModel.itemId, itemViewModel.historyFlag, loginUserId);

        LiveData<Resource<Item>> itemDetail = itemViewModel.getItemDetailData();
        if (itemDetail != null) {
            itemDetail.observe(this, listResource -> {
                if (listResource != null) {

                    switch (listResource.status) {
                        case LOADING:
                            // Loading State
                            // Data are from Local DB

                            if (listResource.data != null) {
                                //fadeIn Animation
                                fadeIn(binding.get().getRoot());

                                itemViewModel.itemContainer = listResource.data;
                                specsViewModel.setSpecsListObj(itemViewModel.itemId);
                                itemViewModel.userId = listResource.data.user.userId;
                                itemViewModel.latValue = listResource.data.lat;
                                itemViewModel.lngValue = listResource.data.lng;
                                replaceItemData(listResource.data);
                                showOrHide(listResource.data);
                                bindingRatingData(listResource.data);

                                // ini ya
                                // bindingCountData(listResource.data);
                                bindingFavoriteData(listResource.data);
                                bindingCategoryNameAndSubCategoryName(listResource.data);
                                bindingPriceWithCurrencySymbol(listResource.data);
                                bindingPhoneNo(listResource.data);
                                bindingFavouriteCount(listResource.data);
                                bindingSoldData(listResource.data);
                                bindindAddedDateUserName(listResource.data);
                                bindingBusinessMode(listResource.data);
                                bindingBottomConstraintLayout(listResource.data);
                                bindingPhotoCount(listResource.data);
                                bindingVerifiedData(listResource.data);
                                bindingItemDialOption(listResource.data);
                                bindingPromoteConstraintLayout(listResource.data);
                                bindMap(itemViewModel.latValue, itemViewModel.lngValue);
                                bindingUserAddress(listResource.data);
                                // bindingPaidStatus(listResource.data);
                            }

                            break;

                        case SUCCESS:
                            // Success State
                            // Data are from Server

                            if (listResource.data != null) {

                                specsViewModel.setSpecsListObj(itemViewModel.itemId);

                                itemViewModel.itemContainer = listResource.data;

                                // Update the data
                                replaceItemData(listResource.data);
                                showOrHide(listResource.data);
                                itemViewModel.userId = listResource.data.user.userId;
//                                if (itemViewModel.userId != null){
//                                callTouchCount();
//                                }
                                bindingRatingData(listResource.data);

                                // ini ya
                                // bindingCountData(listResource.data);
                                bindingFavoriteData(listResource.data);
                                bindingCategoryNameAndSubCategoryName(listResource.data);
                                bindingPriceWithCurrencySymbol(listResource.data);
                                bindingPhoneNo(listResource.data);
                                bindingFavouriteCount(listResource.data);
                                bindingSoldData(listResource.data);
                                bindindAddedDateUserName(listResource.data);
                                bindingBusinessMode(listResource.data);
                                bindingBottomConstraintLayout(listResource.data);
                                bindingPhotoCount(listResource.data);
                                bindingVerifiedData(listResource.data);
                                bindingItemDialOption(listResource.data);
                                itemViewModel.locationId = listResource.data.itemLocation.id;
                                itemViewModel.locationName = listResource.data.itemLocation.name;
                                itemViewModel.otherUserId = listResource.data.user.userId;
                                itemViewModel.otherUserName = listResource.data.user.userName;
                                bindingPromoteConstraintLayout(listResource.data);
                                bindMap(itemViewModel.latValue, itemViewModel.lngValue);
                                // bindingPaidStatus(listResource.data);
//                                checkText(listResource.data);

                            }

                            itemViewModel.setLoadingState(false);

                            break;

                        case ERROR:

                            // Error State
                            itemViewModel.setLoadingState(false);

                            break;

                        default:
                            // Default

                            break;
                    }

                } else {

                    itemViewModel.setLoadingState(false);

                }
            });
        }


        //get rating post method
        ratingViewModel.getRatingPostData().observe(this, result -> {
            if (result != null) {
                if (result.status == Status.SUCCESS) {
                    if (ItemRelatedDetailFragment.this.getActivity() != null) {
                        Utils.psLog(result.status.toString());
                        ratingViewModel.setLoadingState(false);
                        prgDialog.get().dismiss();
                        prgDialog.get().cancel();
                        navigationController.navigateToRatingList(ItemRelatedDetailFragment.this.getActivity(), binding.get().getItem().user.userId);
                    }

                } else if (result.status == Status.ERROR) {
                    if (ItemRelatedDetailFragment.this.getActivity() != null) {
                        Utils.psLog(result.status.toString());
                        ratingViewModel.setLoadingState(false);
                        prgDialog.get().dismiss();
                        prgDialog.get().cancel();
                    }
                }
            }
        });

    }

    private void bindingUserAddress(Item data) {
        if (data.umkm.kelurahan != null){
            binding.get().addressUserTextView.setText(Utils.capitalizeFirstLetter(data.umkm.kelurahan.toLowerCase()));
        }
    }

    private void callTouchCount() {
        if (!loginUserId.equals(itemViewModel.userId)) {
            if (connectivity.isConnected()) {
                touchCountViewModel.setTouchCountPostDataObj(loginUserId, itemViewModel.itemId);
            }
        }
    }

    private void replaceItemData(Item item) {
        itemViewModel.currentItem = item;
        binding.get().setItem(item);

    }

    // ini ya
//    private void bindingCountData(Item item) {
//         binding.get().favouriteCountTextView.setText(getString(R.string.item_detail__fav_count, item.favouriteCount));
//         binding.get().viewCountTextView.setText(getString(R.string.item_detail__view_count, item.touchCount));
//    }

    private void bindingCategoryNameAndSubCategoryName(Item item) {
        String categoryName = item.category.name;
        String subCategoryName = item.subCategory.name;

        // ini ya
        if (categoryName.equals("")) {
            binding.get().categoryAndSubCategoryTextView.setText(subCategoryName);
            binding.get().categoriAddressTextView.setText(subCategoryName);
        } else if (subCategoryName.equals("")) {
            binding.get().categoryAndSubCategoryTextView.setText(categoryName);
            binding.get().categoriAddressTextView.setText(categoryName);
        } else {
            String name = categoryName + " / " + subCategoryName;
            binding.get().categoryAndSubCategoryTextView.setText(name);
            binding.get().categoriAddressTextView.setText(name);
        }

    }

    private void bindingPriceWithCurrencySymbol(Item item) {
        String currencySymbol = "Rp. ";
        String price;
        try {
            price = Utils.format(Double.parseDouble(item.price));
        } catch (Exception e) {
            price = item.price;
        }

        String currencyPrice;
        if (Config.SYMBOL_SHOW_FRONT) {
            currencyPrice = currencySymbol + " " + price;
        } else {
            currencyPrice = price + " " + currencySymbol;
        }
        binding.get().priceTextView.setText(currencyPrice);
    }

    private void bindingPhoneNo(Item item) {
        if (item.user.userPhone.trim().isEmpty()) {
            binding.get().waButton.setVisibility(View.VISIBLE);
        } else {
            binding.get().waButton.setVisibility(View.VISIBLE);
        }
    }

    private void bindingRatingData(Item item) {

        if (item.user.overallRating.isEmpty()) {
            binding.get().ratingCountTextView.setText(getString(R.string.item_detail__rating));
        } else {
            binding.get().ratingCountTextView.setText(item.user.overallRating);
        }

        if (!item.user.overallRating.isEmpty()) {
            binding.get().ratingBarInformation.setRating(item.user.ratingDetails.totalRatingValue);
        }

        String ratingCount = "( " + item.user.ratingCount + " )";

        // binding.get().ratingInfoTextView.setText(ratingCount);

    }

    private void bindingVerifiedData(Item item) {

        if (item.user.emailVerify.equals("1")) {
            // binding.get().mailImageView.setVisibility(View.VISIBLE);
        } else {
            //binding.get().mailImageView.setVisibility(View.GONE);
        }

        if (item.user.facebookVerify.equals("1")) {
            // binding.get().facebookImageView.setVisibility(View.VISIBLE);
        } else {
            //binding.get().facebookImageView.setVisibility(View.GONE);
        }

        if (item.user.phoneVerify.equals("1")) {
            //binding.get().phoneImage.setVisibility(View.VISIBLE);
        } else {
            //binding.get().phoneImage.setVisibility(View.GONE);
        }

        if (item.user.googleVerify.equals("1")) {
            //binding.get().googleImage.setVisibility(View.VISIBLE);
        } else {
            //binding.get().googleImage.setVisibility(View.GONE);
        }
    }

    private void bindingFavouriteCount(Item item) {
        String favouriteCount = item.favouriteCount + " " + getString(R.string.item_detail__like);
        //binding.get().likesTextView.setText(favouriteCount);

    }

    private void bindingFavoriteData(Item item) {
        if (item.isFavourited.equals(Constants.ONE)) {
            binding.get().favouriteImageView.setLiked(true);
        } else {
            binding.get().favouriteImageView.setLiked(false);
        }
    }

    private void bindingItemDialOption(Item item) {
        if (item.dealOptionId.equals("1")) {
            // binding.get().meetTextView.setText(getString(R.string.item_detail__meetup));
        } else {
            // binding.get().meetTextView.setText(getString(R.string.item_detail__mailing_on_delivery));
        }
    }

    private void bindingSoldData(Item item) {
        if (item.isSoldOut.equals(Constants.ONE)) {
            binding.get().soldTextView.setText(getString(R.string.item_detail__sold));
        } else {
            if (item.addedUserId.equals(loginUserId)) {
                binding.get().soldTextView.setText(R.string.item_detail__mark_sold);
            } else {
                binding.get().soldTextView.setVisibility(View.GONE);
            }
        }
    }

    /*
    private void bindingPaidStatus(Item item) {
        switch (item.paidStatus) {
            case Constants.ADSPROGRESS:
                binding.get().adsCheckingTextView.setText(getString(R.string.paid__ads_in_progress));
                binding.get().adsCheckingTextView.setBackgroundColor(getResources().getColor(R.color.paid_ad));
                break;
            case Constants.ADSFINISHED:
                binding.get().adsCheckingTextView.setText(getString(R.string.paid__ads_in_completed));
                binding.get().adsCheckingTextView.setBackgroundColor(getResources().getColor(R.color.paid_ad_completed));
                break;
            case Constants.ADSNOTYETSTART:
                binding.get().adsCheckingTextView.setText(getString(R.string.paid__ads_is_not_yet_start));
                binding.get().adsCheckingTextView.setBackgroundColor(getResources().getColor(R.color.paid_ad_is_not_start));
                break;
            default:
                binding.get().adsCheckingTextView.setVisibility(View.GONE);
                break;
        }
    }
    */

    private void bindingPhotoCount(Item item) {
        if (item.photoCount.equals("1")) {
            String photoCount = item.photoCount + " " + getString(R.string.item_detail__photo);
            binding.get().photoCountTextView.setText(photoCount);
        } else {
            String photoCount = item.photoCount + " " + getString(R.string.item_detail__photos);
            binding.get().photoCountTextView.setText(photoCount);
        }
    }

    private void bindingBusinessMode(Item item) {
        if (item.businessMode.equals("0")) {
//            binding.get().orderTextView.setText(getString(R.string.item_detail__order_not_more_than_one));
        }
        if (item.businessMode.equals("1")) {
//            binding.get().orderTextView.setText(getString(R.string.item_detail__order_more_than_one));
        }
    }

    private void bindindAddedDateUserName(Item item) {
        // binding.get().activeHourTextView.setText(item.addedDate);
        binding.get().activeHourTextView.setText("Diunggah "+Utils.covertTimeToText(item.addedDate));
        // binding.get().userNameActiveHourTextView.setText(item.user.userName);
    }


    private void bindingBottomConstraintLayout(Item item) {
        if (item.isOwner.equals(Constants.ONE)) {
            binding.get().itemOwnerConstraintLayout.setVisibility(View.VISIBLE);
            binding.get().itemSupplierConstraintLayout.setVisibility(View.GONE);
        } else {
            binding.get().itemSupplierConstraintLayout.setVisibility(View.VISIBLE);
            binding.get().itemOwnerConstraintLayout.setVisibility(View.GONE);
        }
    }

    private void bindingPromoteConstraintLayout(Item item) {
        if (item.isOwner.equals(Constants.ONE) && (item.paidStatus.equals(Constants.ADSFINISHED) || item.paidStatus.equals(Constants.ADSNOTAVAILABLE))) {
            // binding.get().itemPromoteCardView.setVisibility(View.VISIBLE);
        } else {
            // binding.get().itemPromoteCardView.setVisibility(View.GONE);
        }
    }

    private void unFavFunction(Item item, LikeButton likeButton) {

        Utils.navigateOnUserVerificationActivityFromFav(userIdToVerify, loginUserId, psDialogMsg, getActivity(), navigationController, likeButton, () -> {

            if (!favouriteViewModel.isLoading) {
                favouriteViewModel.setFavouritePostDataObj(item.id, loginUserId);
                likeButton.setLikeDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_favorite_white, null));
            }

        });

    }

    private void favFunction(Item item, LikeButton likeButton) {

        Utils.navigateOnUserVerificationActivityFromFav(userIdToVerify, loginUserId, psDialogMsg, getActivity(), navigationController, likeButton, () -> {

            if (!favouriteViewModel.isLoading) {
                favouriteViewModel.setFavouritePostDataObj(item.id, loginUserId);
                likeButton.setLikeDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_favorite_red, null));
            }

        });

    }

    private void showOrHide(Item item) {

        if (item != null && item.itemPriceType != null && item.itemPriceType.name.equals("")) {
            //binding.get().priceTypeTextView.setVisibility(View.GONE);
        } else {
            //binding.get().priceTypeTextView.setVisibility(View.VISIBLE);
        }
        if (item != null && item.addedDateStr != null && item.addedDateStr.equals("")) {
            binding.get().activeHourTextView.setVisibility(View.GONE);
            /// binding.get().imageView16.setVisibility(View.GONE);
        } else {
            binding.get().activeHourTextView.setVisibility(View.VISIBLE);
            // binding.get().imageView16.setVisibility(View.VISIBLE);
        }
        if (item != null && item.price != null && item.price.equals("")) {
            String currencySymbol = "Rp.";
            String price;
            try {
                price = Utils.format(Double.parseDouble(item.price));
            } catch (Exception e) {
                price = item.price;
            }

            String currencyPrice;
            if (Config.SYMBOL_SHOW_FRONT) {
                currencyPrice = currencySymbol + " " + price;
            } else {
                currencyPrice = price + " " + currencySymbol;
            }

            binding.get().priceTextView.setText(currencyPrice);

            binding.get().priceTextView.setVisibility(View.GONE);
            //binding.get().imageView17.setVisibility(View.GONE);
        } else {
            binding.get().priceTextView.setVisibility(View.VISIBLE);
            //binding.get().imageView17.setVisibility(View.VISIBLE);
        }
        if (item != null && item.favouriteCount != null && item.favouriteCount.equals("")) {
            //binding.get().likesTextView.setVisibility(View.GONE);
            //binding.get().imageView22.setVisibility(View.GONE);
        } else {
            //binding.get().likesTextView.setVisibility(View.VISIBLE);
            //binding.get().imageView22.setVisibility(View.VISIBLE);
        }
        if (item != null && item.itemCondition.name != null && item.itemCondition.name.equals("")) {
            //binding.get().newTextView.setVisibility(View.GONE);
            //binding.get().imageView18.setVisibility(View.GONE);
        } else {
            //binding.get().newTextView.setVisibility(View.VISIBLE);
            //binding.get().imageView18.setVisibility(View.VISIBLE);
        }

        if (item != null && item.category.name != null && item.subCategory.name != null && item.category.name.equals("") && item.subCategory.name.equals("")) {
           // binding.get().categoryAndSubCategoryTextView.setVisibility(View.GONE);
            //binding.get().imageView23.setVisibility(View.GONE);
        } else {
            //binding.get().categoryAndSubCategoryTextView.setVisibility(View.VISIBLE);
            //binding.get().imageView23.setVisibility(View.VISIBLE);
        }
        if (item != null && item.highlightInfo != null && item.highlightInfo.equals("")) {
            //binding.get().highlightInfoTextView.setVisibility(View.GONE);
           // binding.get().imageView26.setVisibility(View.GONE);
        } else {
            //binding.get().highlightInfoTextView.setVisibility(View.VISIBLE);
           // binding.get().imageView26.setVisibility(View.VISIBLE);
        }

        if (item != null && item.brand != null && item.brand.equals("")) {
            //binding.get().brandTextView.setVisibility(View.GONE);
            //binding.get().imageView24.setVisibility(View.GONE);
        } else {
            //binding.get().brandTextView.setVisibility(View.VISIBLE);
            //binding.get().imageView24.setVisibility(View.VISIBLE);
        }

//        if (item != null && item.itemType.name != null && item.itemType.name.equals("")) {
//            binding.get().saleBuyTextView.setVisibility(View.GONE);
//            binding.get().imageView27.setVisibility(View.GONE);
//        } else {
//            binding.get().saleBuyTextView.setVisibility(View.VISIBLE);
//            binding.get().imageView27.setVisibility(View.VISIBLE);
//        }

        if (item != null && item.businessMode != null && item.businessMode.equals("1") || item != null && item.businessMode != null && item.businessMode.equals("0")) {
            //binding.get().orderTextView.setVisibility(View.VISIBLE);
            //binding.get().imageView29.setVisibility(View.VISIBLE);
        } else {
            //binding.get().orderTextView.setVisibility(View.GONE);
            //binding.get().imageView29.setVisibility(View.GONE);
        }
//      ini ya
//        if (item != null && item.description != null && item.description.equals("")) {
//            binding.get().informationTextView.setVisibility(View.GONE);
//            binding.get().imageView30.setVisibility(View.GONE);
//        } else {
//            binding.get().informationTextView.setVisibility(View.VISIBLE);
//            binding.get().imageView30.setVisibility(View.VISIBLE);
//        }

        if (item != null && item.addedUserId != null && item.addedUserId.equals(loginUserId)) {
//            if(item.isSoldOut.equals(Constants.ONE)){
//                binding.get().markAsSoldButton.setVisibility(View.GONE);
//            }else {
//                binding.get().markAsSoldButton.setVisibility(View.VISIBLE);
//            }
            binding.get().editButton.setVisibility(View.VISIBLE);
            binding.get().deleteButton.setVisibility(View.VISIBLE);
            binding.get().itemSupplierConstraintLayout.setVisibility(View.GONE);
            // binding.get().adsCheckingTextView.setVisibility(View.VISIBLE);

        } else {
            binding.get().editButton.setVisibility(View.GONE);
            binding.get().deleteButton.setVisibility(View.GONE);
//            binding.get().markAsSoldButton.setVisibility(View.GONE);
            binding.get().itemSupplierConstraintLayout.setVisibility(View.VISIBLE);
            // binding.get().adsCheckingTextView.setVisibility(View.GONE);
        }

    }

    @Override
    public void onResume() {
        super.onResume();

        loadLoginUserId();
        if (loginUserId != null) {
            itemViewModel.setItemDetailObj(itemViewModel.itemId, itemViewModel.historyFlag, loginUserId);
        }
        psDialogMsg.cancel();
//        binding.get().rating.setRating(0);
    }


    @Override
    public void onDispatched() {

    }
}
