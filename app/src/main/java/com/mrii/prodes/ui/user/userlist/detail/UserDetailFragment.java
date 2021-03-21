package com.mrii.prodes.ui.user.userlist.detail;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.mrii.prodes.Config;
import com.mrii.prodes.MainActivity;
import com.mrii.prodes.R;
import com.mrii.prodes.binding.FragmentDataBindingComponent;
import com.mrii.prodes.databinding.FragmentUserDetailBinding;
import com.mrii.prodes.ui.common.DataBoundListAdapter;
import com.mrii.prodes.ui.common.PSFragment;
import com.mrii.prodes.ui.item.adapter.ItemVerticalListAdapter;
import com.mrii.prodes.ui.item.detail.ItemFragment;
import com.mrii.prodes.utils.AutoClearedValue;
import com.mrii.prodes.utils.Constants;
import com.mrii.prodes.utils.PSDialogMsg;
import com.mrii.prodes.utils.Utils;
import com.mrii.prodes.viewmodel.item.ItemViewModel;
import com.mrii.prodes.viewmodel.user.UserViewModel;
import com.mrii.prodes.viewobject.Item;
import com.mrii.prodes.viewobject.User;
import com.mrii.prodes.viewobject.common.Status;

import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class UserDetailFragment extends PSFragment implements DataBoundListAdapter.DiffUtilDispatchedInterface {


    //region Variables

    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    private ItemViewModel itemViewModel;
    private UserViewModel userViewModel;
    public PSDialogMsg psDialogMsg;
    private static final int REQUEST_CALL = 1;


    @VisibleForTesting
    private AutoClearedValue<FragmentUserDetailBinding> binding;
    private AutoClearedValue<ItemVerticalListAdapter> adapter;

    //endregion

    //region Override Methods

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        FragmentUserDetailBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_detail, container, false, dataBindingComponent);


        binding = new AutoClearedValue<>(this, dataBinding);


        return binding.get().getRoot();
    }


    @Override
    protected void initUIAndActions() {


        if (getActivity() instanceof MainActivity) {
//            ((MainActivity) this.getActivity()).binding.toolbar.setBackgroundColor(getResources().getColor(R.color.global__primary));
            ((MainActivity) getActivity()).updateMenuIconWhite();

        }

        psDialogMsg = new PSDialogMsg(getActivity(), false);

        binding.get().userOwnItemList.setNestedScrollingEnabled(false);
        binding.get().seeAllTextView.setOnClickListener(view ->
                navigationController.navigateToItemListActivity(getActivity(), userViewModel.otherUserId,Constants.FLAGNOPAID, Constants.ONE, getString(R.string.profile__listing)));

        binding.get().followBtn.setOnClickListener(v -> userViewModel.setUserFollowPostObj(loginUserId, userViewModel.otherUserId));

        binding.get().chatBtn.setOnClickListener(v -> {
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

        binding.get().phoneTextView.setOnClickListener(v -> {
            String number = binding.get().phoneTextView.getText().toString();
                if(!(number.trim().isEmpty() || number.trim().equals("-"))){
                    Utils.callPhone(this,number);
                }
        });

        binding.get().ratingBarInformation.setOnClickListener(v -> navigationController.navigateToRatingList(UserDetailFragment.this.getActivity(),binding.get().getUser().userId));
        binding.get().ratingBarInformation.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                navigationController.navigateToRatingList(UserDetailFragment.this.getActivity(),binding.get().getUser().userId);
            }
            return true;
        });
    }

    @Override
    protected void initViewModels() {
        itemViewModel = new ViewModelProvider(this, viewModelFactory).get(ItemViewModel.class);
        userViewModel = new ViewModelProvider(this, viewModelFactory).get(UserViewModel.class);
    }

    @Override
    protected void initAdapters() {

        ItemVerticalListAdapter nvAdapter = new ItemVerticalListAdapter(dataBindingComponent,
                item -> navigationController.navigateToItemDetailActivity(UserDetailFragment.this.getActivity(), item.id, item.title, item.catId, item.subCatId), this);
        this.adapter = new AutoClearedValue<>(this, nvAdapter);
        binding.get().userOwnItemList.setAdapter(nvAdapter);
    }

    @Override
    protected void initData() {

        try {
            if (getActivity() != null) {
                if (getActivity().getIntent() != null) {
                    if (getActivity().getIntent().getExtras() != null) {
                        userViewModel.otherUserId = getActivity().getIntent().getExtras().getString(Constants.OTHER_USER_ID);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (userViewModel.otherUserId != null) {
            if (userViewModel.otherUserId.equals(loginUserId) || loginUserId.equals("")) {
                binding.get().followBtn.setVisibility(View.GONE);
            } else {
                binding.get().followBtn.setVisibility(View.VISIBLE);
            }
        }

        //User
        userViewModel.getOtherUser(loginUserId, userViewModel.otherUserId).observe(this, listResource -> {

            if (listResource != null) {

                Utils.psLog("Got Data Test test" + listResource.message + listResource.toString());

                switch (listResource.status) {
                    case LOADING:
                        // Loading State
                        // Data are from Local DB

                        if (listResource.data != null) {

                            //fadeIn Animation
                            fadeIn(binding.get().getRoot());

                            binding.get().setUser(listResource.data);
                            Utils.psLog("Photo : " + listResource.data.userProfilePhoto);

                            replaceUserData(listResource.data);

                        }

                        break;
                    case SUCCESS:
                        // Success State
                        // Data are from Server

                        if (listResource.data != null) {

                            //Toast.makeText(getActivity(), "Haha", Toast.LENGTH_SHORT).show();

                            //fadeIn Animation
                            //fadeIn(binding.get().getRoot());

                            binding.get().setUser(listResource.data);
                            Utils.psLog("Photo : " + listResource.data.userProfilePhoto);

                            replaceUserData(listResource.data);

                            if (listResource.data.isFollowed != null) {

                                userViewModel.isFollowed = listResource.data.isFollowed;

                            }

                        }

                        break;
                    case ERROR:
                        // Error State

                        psDialogMsg.showErrorDialog(listResource.message, "HAHA");
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
        });

        //Item
        itemViewModel.holder.userId = userViewModel.otherUserId;

        itemViewModel.setItemListByKeyObj(loginUserId, String.valueOf(Config.LOGIN_USER_APPROVED_ITEM_COUNT), Constants.ZERO, itemViewModel.holder);

        itemViewModel.getItemListByKeyData().observe(this, listResource -> {

            if (listResource != null) {
                switch (listResource.status) {
                    case SUCCESS:

                        if (listResource.data != null) {
                            if (listResource.data.size() > 0) {
                                itemReplaceData(listResource.data);
                            }
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
                        break;
                }
            }
        });

        //For user follow post
        userViewModel.getUserFollowPostData().observe(this, result -> {
            if (result != null) {
                if (result.status == Status.SUCCESS) {
                    if (this.getActivity() != null) {
                        Utils.psLog(result.status.toString());
                        userViewModel.setLoadingState(false);
                        userViewModel.getOtherUser(loginUserId, userViewModel.otherUserId);
                    }

                } else if (result.status == Status.ERROR) {
                    if (this.getActivity() != null) {
                        Utils.psLog("Test follow" +result.status.toString());
                        userViewModel.setLoadingState(false);
                    }
                }
            }
        });
    }

    @Override
    public void onDispatched() {

    }


    private void replaceUserData(User user) {

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

        binding.get().nameUmkmTextView.setText(user.namaUmkm);
        binding.get().nameTextView.setText(user.userName);
        if(user.kelurahan != null){
            binding.get().namaKelurahanTextView.setText(Utils.capitalizeFirstLetter(user.kelurahan.toLowerCase()) );
        }
        binding.get().overAllRatingTextView.setText(user.overallRating);
        binding.get().ratingBarInformation.setRating(user.ratingDetails.totalRatingValue);
        String ratingCount = "( " + user.ratingCount + " )";
        binding.get().ratingCountTextView.setText(ratingCount);
        binding.get().phoneTextView.setText(user.userPhone.isEmpty() ? " - " : user.userPhone);

//        Toast.makeText(getActivity(), " Ha :"+user.kelurahan.toLowerCase() , Toast.LENGTH_SHORT).show();

//        if (user.emailVerify.equals("1")) {
//            binding.get().emailImage.setVisibility(View.VISIBLE);
//        }else {
//            binding.get().emailImage.setVisibility(View.GONE);
//        }
//
//        if (user.facebookVerify.equals("1")) {
//            binding.get().facebookImage.setVisibility(View.VISIBLE);
//        }else {
//            binding.get().facebookImage.setVisibility(View.GONE);
//        }
//
//        if (user.phoneVerify.equals("1")) {
//            binding.get().phoneImage.setVisibility(View.VISIBLE);
//        }else {
//            binding.get().phoneImage.setVisibility(View.GONE);
//        }
//
//        if (user.googleVerify.equals("1")) {
//            binding.get().googleImage.setVisibility(View.VISIBLE);
//        }else {
//            binding.get().googleImage.setVisibility(View.GONE);
//        }

    }

    private void itemReplaceData(List<Item> itemList) {
        adapter.get().replace(itemList);
        binding.get().executePendingBindings();
    }

}
