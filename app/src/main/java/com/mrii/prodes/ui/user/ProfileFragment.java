package com.mrii.prodes.ui.user;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.mrii.prodes.Config;
import com.mrii.prodes.MainActivity;
import com.mrii.prodes.R;
import com.mrii.prodes.binding.FragmentDataBindingComponent;
import com.mrii.prodes.databinding.FragmentProfileBinding;
import com.mrii.prodes.databinding.FragmentProfilePembeliBinding;
import com.mrii.prodes.ui.common.DataBoundListAdapter;
import com.mrii.prodes.ui.common.PSFragment;
import com.mrii.prodes.ui.item.adapter.ItemHorizontalListAdapter;
import com.mrii.prodes.ui.item.adapter.ItemVerticalListAdapter;
import com.mrii.prodes.ui.item.promote.adapter.ItemPromoteHorizontalListAdapter;
import com.mrii.prodes.ui.user.userlist.detail.UserDetailFragment;
import com.mrii.prodes.utils.AutoClearedValue;
import com.mrii.prodes.utils.Constants;
import com.mrii.prodes.utils.PSDialogMsg;
import com.mrii.prodes.utils.Tools;
import com.mrii.prodes.utils.Utils;
import com.mrii.prodes.viewmodel.ItemPaidHistoryViewModel.ItemPaidHistoryViewModel;
import com.mrii.prodes.viewmodel.item.DisabledViewModel;
import com.mrii.prodes.viewmodel.item.ItemViewModel;
import com.mrii.prodes.viewmodel.item.PendingViewModel;
import com.mrii.prodes.viewmodel.item.RejectedViewModel;
import com.mrii.prodes.viewmodel.user.UserViewModel;
import com.mrii.prodes.viewobject.Item;
import com.mrii.prodes.viewobject.ItemPaidHistory;
import com.mrii.prodes.viewobject.User;
import com.mrii.prodes.viewobject.common.Resource;
import com.mrii.prodes.viewobject.holder.UserParameterHolder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * ProfileFragment
 */
public class ProfileFragment extends PSFragment implements DataBoundListAdapter.DiffUtilDispatchedInterface {


    //region Variables

    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    private ItemViewModel itemViewModel;
    private DisabledViewModel disabledViewModel;
    private RejectedViewModel rejectedViewModel;
    private PendingViewModel pendingViewModel;
    private UserViewModel userViewModel;
    private ItemPaidHistoryViewModel itemPaidHistoryViewModel;
    public PSDialogMsg psDialogMsg;

    @VisibleForTesting
    private AutoClearedValue<FragmentProfileBinding> binding;
    private AutoClearedValue<ItemVerticalListAdapter> adapter;
    private AutoClearedValue<ItemHorizontalListAdapter> pendingAdapter;
    private AutoClearedValue<ItemHorizontalListAdapter> rejectedAdapter;
    private AutoClearedValue<ItemHorizontalListAdapter> disabledAdapter;
    private AutoClearedValue<ItemPromoteHorizontalListAdapter> itemPromoteHorizontalListAdapter;
    //endregion

    //region Override Methods
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        FragmentProfileBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false, dataBindingComponent);

        binding = new AutoClearedValue<>(this, dataBinding);

        setHasOptionsMenu(true);

        return binding.get().getRoot();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_profile, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getActivity().finish();
        } else if(item.getItemId() == R.id.action_share) {

                try {
                    String uri = "http://www.ngebugcode.com/prodes/akun/"+userViewModel.user.userId;
                    shareImageUri(uri);
                } catch (Exception e) {
                    Utils.psErrorLog("Error ItemFragment 288 shared", e);
                }

        }else{
             navigationController.navigateToSettingActivity(getActivity());
        }
        return false;
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

    @Override
    protected void initUIAndActions() {

        if (getActivity() instanceof MainActivity) {

            Toolbar toolbar = (Toolbar) binding.get().toolbar;
            ((MainActivity)getActivity()).setSupportActionBar(toolbar);
            ((MainActivity)getActivity()).setToolbarText(toolbar, getString(R.string.menu__profile));
            setHasOptionsMenu(true);
            ((MainActivity) getActivity()).updateToolbarIconColor(Color.WHITE);
            ((MainActivity) getActivity()).refreshPSCount();
        }

        psDialogMsg = new PSDialogMsg(getActivity(), false);

        /* make condition to set text in header from user "penjual" and "pembeli" */
        if (pref.getString(Constants.USER_TYPE, Constants.EMPTY_STRING).equals("pembeli")){

            binding.get().editTextView.setOnClickListener(view -> navigationController.navigateToProfileEditActivity(getActivity()));

            binding.get().llUserCount.setVisibility(View.GONE);
            binding.get().historyTextView.setVisibility(View.GONE);
            binding.get().seeAllTextView.setVisibility(View.GONE);
            binding.get().overAllRatingTextView.setVisibility(View.GONE);
            binding.get().ratingBarInformation.setVisibility(View.GONE);
            binding.get().ratingCountTextView.setVisibility(View.GONE);
            binding.get().kelurahanTextView.setVisibility(View.GONE);
            binding.get().approvedListingRecyclerView.setVisibility(View.GONE);
            binding.get().view27.setVisibility(View.VISIBLE);
            binding.get().nameTextView.setVisibility(View.INVISIBLE);
            binding.get().namaUmkmTextView.setVisibility(View.VISIBLE);
        }

        if(pref.getString(Constants.USER_TYPE, Constants.EMPTY_STRING).equals("penjual")){

            binding.get().editTextView.setOnClickListener(view -> navigationController.navigateToProfilePenjualEditActivity(getActivity()));

            binding.get().llUserCount.setVisibility(View.VISIBLE);
            binding.get().historyTextView.setVisibility(View.VISIBLE);
            binding.get().seeAllTextView.setVisibility(View.VISIBLE);
            binding.get().overAllRatingTextView.setVisibility(View.VISIBLE);
            binding.get().ratingBarInformation.setVisibility(View.VISIBLE);
            binding.get().ratingCountTextView.setVisibility(View.VISIBLE);
            binding.get().kelurahanTextView.setVisibility(View.VISIBLE);
            binding.get().approvedListingRecyclerView.setVisibility(View.VISIBLE);
            binding.get().view27.setVisibility(View.VISIBLE);
            binding.get().nameTextView.setVisibility(View.VISIBLE);
            binding.get().namaUmkmTextView.setVisibility(View.VISIBLE);
        }
        /*end*/

        binding.get().approvedListingRecyclerView.setNestedScrollingEnabled(true);
        binding.get().seeAllTextView.setOnClickListener(view -> navigationController.navigateToItemListActivity(getActivity(), loginUserId, Constants.FLAGNOPAID, Constants.ONE, getString(R.string.profile__listing)));
        binding.get().followingUserTextView.setOnClickListener(view -> navigationController.navigateToUserListActivity(ProfileFragment.this.getActivity(), new UserParameterHolder().getFollowingUsers()));
        binding.get().followUserTextView.setOnClickListener(view -> navigationController.navigateToUserListActivity(ProfileFragment.this.getActivity(), new UserParameterHolder().getFollowerUsers()));

    }

    @Override
    protected void initViewModels() {
        itemViewModel = new ViewModelProvider(this, viewModelFactory).get(ItemViewModel.class);
        userViewModel = new ViewModelProvider(this, viewModelFactory).get(UserViewModel.class);
        itemPaidHistoryViewModel = new ViewModelProvider(this, viewModelFactory).get(ItemPaidHistoryViewModel.class);
        disabledViewModel = new ViewModelProvider(this, viewModelFactory).get(DisabledViewModel.class);
        rejectedViewModel = new ViewModelProvider(this, viewModelFactory).get(RejectedViewModel.class);
        pendingViewModel = new ViewModelProvider(this, viewModelFactory).get(PendingViewModel.class);
    }

    @Override
    protected void initAdapters() {

        ItemVerticalListAdapter nvAdapter = new ItemVerticalListAdapter(dataBindingComponent,
                item -> navigationController.navigateToItemDetailActivity(ProfileFragment.this.getActivity(), item.id, item.title, item.catId, item.subCatId), this);

        this.adapter = new AutoClearedValue<>(this, nvAdapter);
        binding.get().approvedListingRecyclerView.setAdapter(nvAdapter);

        ItemHorizontalListAdapter pendingAdapter = new ItemHorizontalListAdapter(dataBindingComponent, new ItemHorizontalListAdapter.NewsClickCallback() {
            @Override
            public void onClick(Item item) {
                navigationController.navigateToItemDetailActivity(ProfileFragment.this.getActivity(), item.id, item.title, item.catId, item.subCatId);
            }
        }, this);
        this.pendingAdapter = new AutoClearedValue<>(this, pendingAdapter);
        // binding.get().pendingRecyclerView.setAdapter(pendingAdapter);

        ItemHorizontalListAdapter rejectedAdapter = new ItemHorizontalListAdapter(dataBindingComponent, new ItemHorizontalListAdapter.NewsClickCallback() {
            @Override
            public void onClick(Item item) {
                navigationController.navigateToItemDetailActivity(ProfileFragment.this.getActivity(), item.id, item.title, item.catId, item.subCatId);
            }
        }, this);
        this.rejectedAdapter = new AutoClearedValue<>(this, rejectedAdapter);
        // binding.get().rejectedRecyclerView.setAdapter(rejectedAdapter);

        ItemHorizontalListAdapter disabledAdapter = new ItemHorizontalListAdapter(dataBindingComponent, new ItemHorizontalListAdapter.NewsClickCallback() {
            @Override
            public void onClick(Item item) {
                navigationController.navigateToItemDetailActivity(ProfileFragment.this.getActivity(), item.id, item.title, item.catId, item.subCatId);
            }
        }, this);
        this.disabledAdapter = new AutoClearedValue<>(this, disabledAdapter);
        // binding.get().disabledRecyclerView.setAdapter(disabledAdapter);

//        ItemPromoteHorizontalListAdapter itemPromoteAdapter = new ItemPromoteHorizontalListAdapter(dataBindingComponent, new ItemPromoteHorizontalListAdapter.NewsClickCallback() {
//            @Override
//            public void onClick(ItemPaidHistory itemPaidHistory) {
//                navigationController.navigateToItemDetailActivity(ProfileFragment.this.getActivity(), itemPaidHistory.item.id, itemPaidHistory.item.title, itemPaidHistory.catId, item.subCatId);
//            }
//        }, this);
//        this.itemPromoteHorizontalListAdapter = new AutoClearedValue<>(this, itemPromoteAdapter);
        // binding.get().userPaidItemRecyclerView.setAdapter(itemPromoteAdapter);

    }

    @Override
    protected void initData() {

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

        //User
        userViewModel.setUserObj(loginUserId);
        userViewModel.getUserData().observe(this, new Observer<Resource<User>>() {
            @Override
            public void onChanged(Resource<User> listResource) {

                if (listResource != null) {

                    Utils.psLog("Got Data" + listResource.message + listResource.toString());

                    switch (listResource.status) {
                        case LOADING:
                            // Loading State
                            // Data are from Local DB

                            if (listResource.data != null) {
                                //fadeIn Animation
                                ProfileFragment.this.fadeIn(binding.get().getRoot());

                                binding.get().setUser(listResource.data);
                                Utils.psLog("Photo : " + listResource.data.userProfilePhoto);

                                ProfileFragment.this.replaceUserData(listResource.data);

                            }

                            break;
                        case SUCCESS:
                            // Success State
                            // Data are from Server
                                User x = ((MainActivity)getActivity()).getA();

                            if (listResource.data != null) {
                                //fadeIn Animation
                                //fadeIn(binding.get().getRoot());
                                binding.get().setUser(listResource.data);

                                ProfileFragment.this.replaceUserData(listResource.data);
                            }

                            break;
                        case ERROR:
                            // Error State

                            psDialogMsg.showErrorDialog(listResource.message, ProfileFragment.this.getString(R.string.app__ok));
                            psDialogMsg.show();

                            userViewModel.isLoading = false;

                            break;
                        default:
                            // Default
                            userViewModel.isLoading = false;

                            break;
                    }

                } else {

                    // Init Object or Empty Data
                    Utils.psLog("Empty Data");

                }

                // we don't need any null checks here for the SubCategoryAdapter since LiveData guarantees that
                // it won't call us if fragment is stopped or not started.
                if (listResource != null && listResource.data != null) {
                    Utils.psLog("Got Data");


                } else {
                    //noinspection Constant Conditions
                    Utils.psLog("Empty Data");

                }
            }
        });

        //delete user
        userViewModel.getDeleteUserStatus().observe(this, result -> {

            if (result != null) {
                switch (result.status) {
                    case SUCCESS:

                        //add offer text
                        Toast.makeText(getContext(), "Success Delete user", Toast.LENGTH_SHORT).show();

                        logout();

                        break;

                    case ERROR:
                        Toast.makeText(getContext(), "Fail Delete this user", Toast.LENGTH_SHORT).show();

                        break;
                }
            }
        });

        //Approved Item Listing
        itemViewModel.holder.userId = loginUserId;
        itemViewModel.holder.status = Constants.ONE;

        itemViewModel.setItemListByKeyObj(Utils.checkUserId(loginUserId),
                String.valueOf(Config.LOGIN_USER_APPROVED_ITEM_COUNT), Constants.ZERO, itemViewModel.holder);

        itemViewModel.getItemListByKeyData().observe(this, listResource -> {

            if (listResource != null) {
                switch (listResource.status) {
                    case SUCCESS:

                        if (listResource.data != null) {
                            if (listResource.data.size() > 0) {
                                hideApprovedList(false);
                                itemReplaceData(listResource.data);
                            }else {
                                hideApprovedList(true);
                            }
                            itemViewModel.setLoadingState(false);
                        }

                        break;

                    case LOADING:

                        if (listResource.data != null) {
                            if (listResource.data.size() > 0) {
                                itemReplaceData(listResource.data);
                            }

                        }

                        break;

                    case ERROR:

                        hideApprovedList(true);
                        itemViewModel.setLoadingState(false);
                        break;
                }
            }
        });

        //end

        //pending listing
        pendingViewModel.holder.userId = loginUserId;
        pendingViewModel.holder.status = Constants.ZERO;

        pendingViewModel.setItemListByKeyObj(Utils.checkUserId(loginUserId),
                String.valueOf(Config.LOGIN_USER_PENDING_ITEM_COUNT), Constants.ZERO, pendingViewModel.holder);

        pendingViewModel.getItemListByKeyData().observe(this, listResource -> {

            if (listResource != null) {
                switch (listResource.status) {
                    case SUCCESS:

                        if (listResource.data != null) {
                            if (listResource.data.size() > 0) {
                                hidePendingList(false);
                                replacePendingListData(listResource.data);
                            }else {
                                hidePendingList(true);
                            }
                            pendingViewModel.setLoadingState(false);
                        }

                        break;

                    case LOADING:

                        if (listResource.data != null) {
                            if (listResource.data.size() > 0) {
                                replacePendingListData(listResource.data);
                            }

                        }

                        break;

                    case ERROR:

                        hidePendingList(true);
                        pendingViewModel.setLoadingState(false);
                        break;
                }
            }
        });
        // end

        //rejected listing
        rejectedViewModel.holder.userId = loginUserId;
        rejectedViewModel.holder.status = Constants.THREE;

        rejectedViewModel.setItemListByKeyObj(Utils.checkUserId(loginUserId),
                String.valueOf(Config.LOGIN_USER_REJECTED_ITEM_COUNT), Constants.ZERO, rejectedViewModel.holder);

        rejectedViewModel.getItemListByKeyData().observe(this, listResource -> {

            if (listResource != null) {
                switch (listResource.status) {
                    case SUCCESS:

                        if (listResource.data != null) {
                            if (listResource.data.size() > 0) {
                                hideRejectedList(false);
                                replaceRejectedListData(listResource.data);
                            }else{
                                hideRejectedList(true);
                            }
                            rejectedViewModel.setLoadingState(false);
                        }

                        break;

                    case LOADING:

                        if (listResource.data != null) {
                            if (listResource.data.size() > 0) {
                                replaceRejectedListData(listResource.data);
                            }
                        }

                        break;

                    case ERROR:

                        hideRejectedList(true);
                        rejectedViewModel.setLoadingState(false);
                        break;
                }
            }
        });
        // end

        //disabled listing

        disabledViewModel.holder.userId = loginUserId;
        disabledViewModel.holder.status = Constants.TWO;

        disabledViewModel.setItemListByKeyObj(Utils.checkUserId(loginUserId),
                String.valueOf(Config.LOGIN_USER_DISABLED_ITEM_COUNT), Constants.ZERO, disabledViewModel.holder);

        disabledViewModel.getItemListByKeyData().observe(this, listResource -> {

            if (listResource != null) {
                switch (listResource.status) {
                    case SUCCESS:

                        if (listResource.data != null) {
                            if (listResource.data.size() > 0) {
                                hideDisabledList(false);
                                replaceDisabledListData(listResource.data);
                            }else {
                                hideDisabledList(true);
                            }
                            disabledViewModel.setLoadingState(false);
                        }

                        break;

                    case LOADING:

                        if (listResource.data != null) {
                            if (listResource.data.size() > 0) {
                                replaceDisabledListData(listResource.data);
                            }

                        }

                        break;

                    case ERROR:

                        hideDisabledList(true);
                        disabledViewModel.setLoadingState(false);
                        break;
                }
            }
        });
        // end

        itemViewModel.getItemListFromDbByKeyData().observe(this, listResource -> {

            if (listResource != null) {

                itemReplaceData(listResource);

            }
        });

        // Get Paid Item History List
        itemPaidHistoryViewModel.setPaidItemHistory(Utils.checkUserId(loginUserId), String.valueOf(Config.PAID_ITEM_COUNT), String.valueOf(itemPaidHistoryViewModel.offset));

        itemPaidHistoryViewModel.getPaidItemHistory().observe(this, result -> {

            if (result != null) {
                switch (result.status) {
                    case SUCCESS:

                        if (result.data != null) {
                            hidePaidItemList(false);
                            //replacePaidItemHistoryList(result.data);
                        }else {
                            hidePaidItemList(true);
                        }
                        itemPaidHistoryViewModel.setLoadingState(false);
                        break;

                    case LOADING:

                        if (result.data != null) {
                            //replacePaidItemHistoryList(result.data);
                        }

                        break;
                    case ERROR:
                        hidePaidItemList(true);
                        itemPaidHistoryViewModel.setLoadingState(false);
                        break;

                        default:
                            break;
                }
            }

        });
    }

    private void hidePaidItemList(boolean isTrue) {
        if(isTrue) {
//            binding.get().paidAdTextView.setVisibility(View.GONE);
//            binding.get().paidAdViewAllTextView.setVisibility(View.GONE);
//            binding.get().userPaidItemRecyclerView.setVisibility(View.GONE);
        }else {
//            binding.get().paidAdTextView.setVisibility(View.VISIBLE);
//            binding.get().paidAdViewAllTextView.setVisibility(View.VISIBLE);
//            binding.get().userPaidItemRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void hideApprovedList(boolean isTrue) {
        if(isTrue) {
            binding.get().approvedListingRecyclerView.setVisibility(View.GONE);
            binding.get().historyTextView.setVisibility(View.GONE);
            binding.get().seeAllTextView.setVisibility(View.GONE);
        }else{
            binding.get().approvedListingRecyclerView.setVisibility(View.VISIBLE);
            binding.get().historyTextView.setVisibility(View.VISIBLE);
            binding.get().seeAllTextView.setVisibility(View.VISIBLE);
        }
    }

    private void hidePendingList(boolean isTrue) {
        if(isTrue) {
//            binding.get().pendingTitleTextView.setVisibility(View.GONE);
//            binding.get().pendingSeeAllTextView.setVisibility(View.GONE);
//            binding.get().pendingRecyclerView.setVisibility(View.GONE);
        }else {
//            binding.get().pendingTitleTextView.setVisibility(View.VISIBLE);
//            binding.get().pendingSeeAllTextView.setVisibility(View.VISIBLE);
//            binding.get().pendingRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void hideRejectedList(boolean isTrue) {
        if(isTrue) {
//            binding.get().rejectedTitleTextView.setVisibility(View.GONE);
//            binding.get().rejectedSeeAllTextView.setVisibility(View.GONE);
//            binding.get().rejectedRecyclerView.setVisibility(View.GONE);
        }else {
//            binding.get().rejectedTitleTextView.setVisibility(View.VISIBLE);
//            binding.get().rejectedSeeAllTextView.setVisibility(View.VISIBLE);
//            binding.get().rejectedRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void hideDisabledList(boolean isTrue) {
        if(isTrue) {
//            binding.get().disabledTitleTextView.setVisibility(View.GONE);
//            binding.get().disabledSeeAllTextView.setVisibility(View.GONE);
//            binding.get().disabledRecyclerView.setVisibility(View.GONE);
        }else{
//            binding.get().disabledTitleTextView.setVisibility(View.VISIBLE);
//            binding.get().disabledSeeAllTextView.setVisibility(View.VISIBLE);
//            binding.get().disabledRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void logout() {

        if ((MainActivity) getActivity() != null) {
            ((MainActivity) getActivity()).hideBottomNavigation();

            userViewModel.deleteUserLogin(userViewModel.user).observe(this, status -> {
                if (status != null) {
//                    this.menuId = 0;

                    //((MainActivity) getActivity()).setToolbarText(((MainActivity) getActivity()).binding.toolbar, getString(R.string.app__app_name));

                    ((MainActivity) getActivity()).isLogout = true;

                    FacebookSdk.sdkInitialize(((MainActivity) getActivity()).getApplicationContext());
                    LoginManager.getInstance().logOut();

                    if (getFragmentManager() != null) {
                        getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        //User
                        userViewModel.setUserObj(loginUserId);
                        userViewModel.getUserData().observe(this, new Observer<Resource<User>>() {
                            @Override
                            public void onChanged(Resource<User> listResource) {

                                if (listResource != null) {

                                    Utils.psLog("Got Data" + listResource.message + listResource.toString());

                                    switch (listResource.status) {
                                        case LOADING:
                                            // Loading State
                                            // Data are from Local DB

                                            if (listResource.data != null) {
                                                //fadeIn Animation
                                                ProfileFragment.this.fadeIn(binding.get().getRoot());
                                                binding.get().setUser(listResource.data);
                                                Utils.psLog("Photo : " + listResource.data.userProfilePhoto);
                                                ProfileFragment.this.replaceUserData(listResource.data);
                                            }

                                            break;
                                        case SUCCESS:
                                            // Success State
                                            // Data are from Server
                                            User x = ((MainActivity)getActivity()).getA();

                                            if (listResource.data != null) {
                                                //fadeIn Animation
                                                //fadeIn(binding.get().getRoot());
                                                binding.get().setUser(listResource.data);

                                                ProfileFragment.this.replaceUserData(listResource.data);
                                            }

                                            break;
                                        case ERROR:
                                            // Error State

                                            psDialogMsg.showErrorDialog(listResource.message, ProfileFragment.this.getString(R.string.app__ok));
                                            psDialogMsg.show();

                                            userViewModel.isLoading = false;

                                            break;
                                        default:
                                            // Default
                                            userViewModel.isLoading = false;
                                            break;
                                    }

                                } else {
                                    // Init Object or Empty Data
                                    Utils.psLog("Empty Data");
                                }

                                // we don't need any null checks here for the SubCategoryAdapter since LiveData guarantees that
                                // it won't call us if fragment is stopped or not started.
                                if (listResource != null && listResource.data != null) {
                                    Utils.psLog("Got Data");


                                } else {
                                    //noinspection Constant Conditions
                                    Utils.psLog("Empty Data");

                                }
                            }
                        });
                    }
                }
            });
        }
    }

    @Override
    public void onDispatched() {

    }


    private void replaceUserData(User user) {

        /* make condition to set text in header from user "penjual" and "pembeli" */
        if (pref.getString(Constants.USER_TYPE, Constants.EMPTY_STRING).equals("pembeli")){
            binding.get().llUserCount.setVisibility(View.GONE);
            binding.get().historyTextView.setVisibility(View.GONE);
            binding.get().seeAllTextView.setVisibility(View.GONE);
            binding.get().overAllRatingTextView.setVisibility(View.GONE);
            binding.get().ratingBarInformation.setVisibility(View.GONE);
            binding.get().ratingCountTextView.setVisibility(View.GONE);
            binding.get().kelurahanTextView.setVisibility(View.GONE);
            binding.get().approvedListingRecyclerView.setVisibility(View.GONE);
            binding.get().view27.setVisibility(View.VISIBLE);
            binding.get().nameTextView.setVisibility(View.INVISIBLE);
            binding.get().namaUmkmTextView.setVisibility(View.VISIBLE);
            binding.get().namaUmkmTextView.setText(user.userName);
        }

        if(pref.getString(Constants.USER_TYPE, Constants.EMPTY_STRING).equals("penjual")){
            binding.get().llUserCount.setVisibility(View.VISIBLE);
            binding.get().historyTextView.setVisibility(View.VISIBLE);
            binding.get().seeAllTextView.setVisibility(View.VISIBLE);
            binding.get().overAllRatingTextView.setVisibility(View.VISIBLE);
            binding.get().ratingBarInformation.setVisibility(View.VISIBLE);
            binding.get().ratingCountTextView.setVisibility(View.VISIBLE);
            binding.get().kelurahanTextView.setVisibility(View.VISIBLE);
            binding.get().approvedListingRecyclerView.setVisibility(View.VISIBLE);
            binding.get().view27.setVisibility(View.VISIBLE);
            binding.get().nameTextView.setVisibility(View.VISIBLE);
            binding.get().namaUmkmTextView.setVisibility(View.VISIBLE);
            binding.get().namaUmkmTextView.setText(user.namaUmkm);
        }
        /*end*/
        binding.get().historyTextView.setText(binding.get().historyTextView.getText().toString());
        binding.get().seeAllTextView.setText(binding.get().seeAllTextView.getText().toString());

        String strCurrentDate = user.addedDate;
        java.text.SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        Date inputDate ;
        try {
            inputDate = inputFormat.parse(strCurrentDate);
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
            if(inputDate != null) {
                String outputDateString = "Bergabung Sejak "+outputFormat.format(inputDate.getTime());
                binding.get().joinedDateTextView.setText(outputDateString);// user.addedDate);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        binding.get().nameTextView.setText(user.userName);
        binding.get().overAllRatingTextView.setText(user.overallRating);
        binding.get().ratingBarInformation.setRating(user.ratingDetails.totalRatingValue);

        String ratingCount = "( " + user.ratingCount + " )";
        String followerCount = " ( " + user.followerCount + " )";
        String followingCount = " ( " + user.followingCount + " )";

        binding.get().ratingCountTextView.setText(ratingCount);
        binding.get().followUserTextView.setText(followerCount);
        binding.get().followingUserTextView.setText(followingCount);
        if (user.kelurahan != null){
            String kelurahan_lowcase = user.kelurahan.toLowerCase();
            binding.get().kelurahanTextView.setText(" - "+capitalizeFirstLetter(kelurahan_lowcase) );
        }

        if (user.emailVerify.equals("1")) {
//            binding.get().emailImage.setVisibility(View.VISIBLE);
        } else {
//            binding.get().emailImage.setVisibility(View.GONE);
        }

        if (user.facebookVerify.equals("1")) {
//            binding.get().facebookImage.setVisibility(View.VISIBLE);
        } else {
//            binding.get().facebookImage.setVisibility(View.GONE);
        }

        if (user.phoneVerify.equals("1")) {
//            binding.get().phoneImage.setVisibility(View.VISIBLE);
        } else {
//            binding.get().phoneImage.setVisibility(View.GONE);
        }

        if (user.googleVerify.equals("1")) {
//            binding.get().googleImage.setVisibility(View.VISIBLE);
        } else {
//            binding.get().googleImage.setVisibility(View.GONE);
        }
    }

    public String capitalizeFirstLetter(String original) {
        if (original == null || original.length() == 0) {
            return original;
        }
        StringBuilder capitalizedString = new StringBuilder();
        String[] splited = original.trim().split("\\s+");
        for (String string : splited) {
            String s1 = string.substring(0, 1).toUpperCase();
            String nameCapitalized = s1 + string.substring(1);

            capitalizedString.append(nameCapitalized);
            capitalizedString.append(" ");
        }
        return capitalizedString.toString().trim();
//        return original.substring(0, 1).toUpperCase() + original.substring(1);
    }

    private void itemReplaceData(List<Item> itemList) {
        /* make condition to set text in header from user "penjual" and "pembeli" */
        if (pref.getString(Constants.USER_TYPE, Constants.EMPTY_STRING).equals("pembeli")){
            binding.get().llUserCount.setVisibility(View.GONE);
            binding.get().historyTextView.setVisibility(View.GONE);
            binding.get().seeAllTextView.setVisibility(View.GONE);
            binding.get().overAllRatingTextView.setVisibility(View.GONE);
            binding.get().ratingBarInformation.setVisibility(View.GONE);
            binding.get().ratingCountTextView.setVisibility(View.GONE);
            binding.get().kelurahanTextView.setVisibility(View.GONE);
            binding.get().approvedListingRecyclerView.setVisibility(View.GONE);
            binding.get().view27.setVisibility(View.VISIBLE);
            binding.get().nameTextView.setVisibility(View.INVISIBLE);
            binding.get().namaUmkmTextView.setVisibility(View.VISIBLE);
        }

        if(pref.getString(Constants.USER_TYPE, Constants.EMPTY_STRING).equals("penjual")){
            binding.get().llUserCount.setVisibility(View.VISIBLE);
            binding.get().historyTextView.setVisibility(View.VISIBLE);
            binding.get().seeAllTextView.setVisibility(View.VISIBLE);
            binding.get().overAllRatingTextView.setVisibility(View.VISIBLE);
            binding.get().ratingBarInformation.setVisibility(View.VISIBLE);
            binding.get().ratingCountTextView.setVisibility(View.VISIBLE);
            binding.get().kelurahanTextView.setVisibility(View.VISIBLE);
            binding.get().approvedListingRecyclerView.setVisibility(View.VISIBLE);
            binding.get().view27.setVisibility(View.VISIBLE);
            binding.get().nameTextView.setVisibility(View.VISIBLE);
            binding.get().namaUmkmTextView.setVisibility(View.VISIBLE);
        }
        /*end*/
        binding.get().countProductTextView.setText("( "+itemList.size()+" )");
        adapter.get().replace(itemList);
        binding.get().executePendingBindings();
    }

    private void replacePendingListData(List<Item> itemList) {
        pendingAdapter.get().replace(itemList);
        binding.get().executePendingBindings();
    }

    private void replaceRejectedListData(List<Item> itemList) {
        rejectedAdapter.get().replace(itemList);
        binding.get().executePendingBindings();
    }

    private void replaceDisabledListData(List<Item> itemList) {
        disabledAdapter.get().replace(itemList);
        binding.get().executePendingBindings();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.REQUEST_CODE__PROFILE_FRAGMENT
                && resultCode == Constants.RESULT_CODE__LOGOUT_ACTIVATED) {
            if (getActivity() instanceof MainActivity) {
                pref.edit().remove(Constants.USER_ADDRESS).apply();
                pref.edit().remove(Constants.USER_DETAIL_ADDRESS).apply();
                pref.edit().remove(Constants.USER_TYPE).apply();
                pref.edit().remove(Constants.NAMA_UMKM).apply();
                pref.edit().remove(Constants.USER_ID).apply();
                pref.edit().remove(Constants.USER_NAME).apply();
                pref.edit().remove(Constants.USER_EMAIL).apply();
                pref.edit().remove(Constants.USER_PASSWORD).apply();
                navigationController.navigateToUserLogin((MainActivity) getActivity());
                // navigationController.navigateToAppLoading(getActivity());
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        userViewModel.setUserObj(loginUserId);
        itemViewModel.setItemListByKeyObj(Utils.checkUserId(loginUserId), String.valueOf(Config.LOGIN_USER_APPROVED_ITEM_COUNT), Constants.ZERO, itemViewModel.holder);

    }
}
