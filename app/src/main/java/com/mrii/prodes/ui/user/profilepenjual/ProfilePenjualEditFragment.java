package com.mrii.prodes.ui.user.profilepenjual;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mrii.prodes.MainActivity;
import com.mrii.prodes.R;
import com.mrii.prodes.binding.FragmentDataBindingComponent;
import com.mrii.prodes.databinding.FragmentProfileEditBinding;
import com.mrii.prodes.databinding.FragmentProfilePenjualEditBinding;
import com.mrii.prodes.ui.common.DataBoundListAdapter;
import com.mrii.prodes.ui.common.PSFragment;
import com.mrii.prodes.ui.item.map.MapActivity;
import com.mrii.prodes.utils.AutoClearedValue;
import com.mrii.prodes.utils.Constants;
import com.mrii.prodes.utils.PSDialogMsg;
import com.mrii.prodes.utils.Utils;
import com.mrii.prodes.viewmodel.user.UserViewModel;
import com.mrii.prodes.viewobject.User;

/**
 * ProfileEditFragment
 */
public class ProfilePenjualEditFragment extends PSFragment  implements DataBoundListAdapter.DiffUtilDispatchedInterface  {

    //region Variables
    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);
    private UserViewModel userViewModel;
    private PSDialogMsg psDialogMsg;
    private User user;
    @VisibleForTesting
    private AutoClearedValue<FragmentProfilePenjualEditBinding> binding;
    private AutoClearedValue<ProgressDialog> prgDialog;

    //endregion

    //region Override Methods
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        FragmentProfilePenjualEditBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile_penjual_edit, container, false, dataBindingComponent);

        binding = new AutoClearedValue<>(this, dataBinding);
        setHasOptionsMenu(true);

        return binding.get().getRoot();
    }

    @Override
    protected void initViewModels() {
        userViewModel = new ViewModelProvider(this, viewModelFactory).get(UserViewModel.class);
    }

    @Override
    protected void initAdapters() {

    }

    @Override
    protected void initData() {

        userViewModel.getLoginUser().observe(this, listResource -> {
            // we don't need any null checks here for the adapter since LiveData guarantees that
            // it won't call us if fragment is stopped or not started.
            if (listResource != null && listResource.size() > 0) {
                Utils.psLog("Got Data");

                user = listResource.get(0).user;

                //fadeIn Animation
                fadeIn(binding.get().getRoot());

                binding.get().setUser(listResource.get(0).user);
                userViewModel.user = listResource.get(0).user;
                Utils.psLog("Photo : " + listResource.get(0).user.userProfilePhoto);
            } else {
                //noinspection Constant Conditions

                user = null;

                Utils.psLog("Empty Data");

            }
        });

    }

    @Override
    protected void initUIAndActions() {

        userViewModel.latValue = selectedLat;
        userViewModel.lngValue = selectedLng;

        psDialogMsg = new PSDialogMsg(getActivity(), false);

        if (getContext() != null) {
            binding.get().userNameEditText.setHint(R.string.edit_profile__user_name);
            binding.get().userEmailEditText.setHint(R.string.edit_profile__email);
            binding.get().userPhoneEditText.setHint(R.string.edit_profile__phone);
            binding.get().userProvisiEditText.setHint(R.string.provinsi);
            binding.get().userKabupatenEditText.setHint(R.string.kabupaten);
            binding.get().userKecamatanEditText.setHint(R.string.kecamatan);
            binding.get().userKelurahanEditText.setHint(R.string.kelurahan);
            binding.get().userAddressEditText.setHint(R.string.edit_profile__address);

            binding.get().userNameEditText.setEnabled(false);
            binding.get().userPhoneEditText.setEnabled(false);
            binding.get().userProvisiEditText.setEnabled(false);
            binding.get().userKabupatenEditText.setEnabled(false);
            binding.get().userKecamatanEditText.setEnabled(false);
            binding.get().userKelurahanEditText.setEnabled(false);
            binding.get().userAddressEditText.setEnabled(false);


            binding.get().nameTextView.setText(getContext().getString(R.string.edit_profile__user_name));
            binding.get().emailTextView.setText(getContext().getString(R.string.edit_profile__email));
            binding.get().phoneTextView.setText(getContext().getString(R.string.edit_profile__phone));
            binding.get().userProvinsiTextView.setText(getContext().getString(R.string.provinsi));
            binding.get().userKabupatenTextView.setText(getContext().getString(R.string.kabupaten));
            binding.get().userKecamatanTextView.setText(getContext().getString(R.string.kecamatan));
            binding.get().userKelurahanTextView.setText(getContext().getString(R.string.kelurahan));
            binding.get().addressTextView.setText(getContext().getString(R.string.edit_profile__address));

        }

        // Init Dialog
        prgDialog = new AutoClearedValue<>(this, new ProgressDialog(getActivity()));

        prgDialog.get().setMessage((Utils.getSpannableString(getContext(), getString(R.string.message__please_wait), Utils.Fonts.MM_FONT)));
        prgDialog.get().setCancelable(false);

        binding.get().profileImageView.setOnClickListener(view -> {

            if (connectivity.isConnected()) {
                try {

                    if (Utils.isStoragePermissionGranted(getActivity())) {
                        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(i, Constants.RESULT_LOAD_IMAGE);
                    }
                } catch (Exception e) {
                    Utils.psErrorLog("Error in Image Gallery.", e);
                }
            } else {

                psDialogMsg.showWarningDialog(getString(R.string.no_internet_error), getString(R.string.app__ok));
                psDialogMsg.show();

            }

        });

        binding.get().saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProfilePenjualEditFragment.this.editProfileData();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {

            if (requestCode == Constants.RESULT_LOAD_IMAGE && resultCode == Constants.RESULT_OK && null != data) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                if (getActivity() != null && selectedImage != null) {
                    Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);

                    if (cursor != null) {
                        cursor.moveToFirst();

                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        userViewModel.profileImagePath = cursor.getString(columnIndex);
                        cursor.close();

                        uploadImage(selectedImage);
                    }

                }

            }

            else if (requestCode == Constants.RESULT_CODE__TO_MAP_VIEW && resultCode == Constants.RESULT_CODE__FROM_MAP_VIEW) {

                userViewModel.user.city = data.getStringExtra(Constants.USER_ADDRESS);
                userViewModel.latValue = data.getStringExtra(Constants.LAT);
                userViewModel.lngValue = data.getStringExtra(Constants.LNG);

                }


        } catch (Exception e) {
            Utils.psErrorLog("Error in load image.", e);
        }
    }


    //endregion


    //region Private Methods


    private void editProfileData() {

        if (!connectivity.isConnected()) {

            psDialogMsg.showWarningDialog(getString(R.string.no_internet_error), getString(R.string.app__ok));
            psDialogMsg.show();
            return;
        }

        String userName = binding.get().userNameEditText.getText().toString();
        if (userName.equals("")) {

            psDialogMsg.showWarningDialog(getString(R.string.error_message__blank_name), getString(R.string.app__ok));
            psDialogMsg.show();
            return;
        }

//        String userEmail = binding.get().userEmailEditText.getText().toString();
//        if (userEmail.equals("")) {
//
//            psDialogMsg.showWarningDialog(getString(R.string.error_message__blank_email), getString(R.string.app__ok));
//            psDialogMsg.show();
//            return;
//        }

        if (!checkToUpdateProfile()) {
            updateUserProfile();
        }

        updateUserProfile();

        userViewModel.getUpdateUserData().observe(this, listResource -> {

            if (listResource != null) {

                Utils.psLog("Got Data Update" + listResource.message + listResource.toString());

                switch (listResource.status) {
                    case LOADING:
                        // Loading State
                        // Data are from Local DB
//                            if(listResource.data != null){
//                                fadeIn(binding.get().getRoot());
//                            }
                        break;
                    case SUCCESS:
                        // Success State
                        // Data are from Server

                        if (listResource.data != null) {

                            Toast.makeText(getActivity(), "Berhasil Diupdate", Toast.LENGTH_SHORT).show();
                            pref.edit().remove(Constants.USER_TYPE).apply();
                            pref.edit().putString(Constants.USER_TYPE, Constants.SELLER).apply();
                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            intent.putExtra("FromProfilePenjualEditFragment", "2");
                            Thread timer = new Thread(){
                                public void run(){
                                    try {
                                        sleep(1000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    finally {
                                        startActivity(intent);
                                        //Utils.navigateOnUserVerificationFragment(pref,user,navigationController,getActivity());
                                        getActivity().finish();
                                    }
                                }
                            };
                            timer.start();

//                            psDialogMsg.showSuccessDialog(listResource.data.message, getString(R.string.app__ok));
//                            psDialogMsg.show();
//                            psDialogMsg.okButton.setOnClickListener(
//                                    new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View v) {
//                                            psDialogMsg.cancel();
//                                            getActivity().finish();
//                                            // Utils.navigateOnUserVerificationFragment(pref,user,navigationController,getActivity());
//                                        }
//                                    }
//                               );
                        }
                        userViewModel.setLoadingState(false);
                        prgDialog.get().cancel();

                        break;
                    case ERROR:
                        // Error State

                        psDialogMsg.showErrorDialog(listResource.message, getString(R.string.app__ok));
                        psDialogMsg.show();
                        prgDialog.get().cancel();

                        userViewModel.setLoadingState(false);
                        break;
                    default:


                        break;
                }

            } else {

                // Init Object or Empty Data
                Utils.psLog("Empty Data");
            }


        });
    }


    private void uploadImage(Uri uri) {

        prgDialog.get().show();
        Utils.psLog("Uploading Image.");

        userViewModel.uploadImage(getContext(), userViewModel.profileImagePath, uri, loginUserId).observe(this, listResource -> {
            // we don't need any null checks here for the SubCategoryAdapter since LiveData guarantees that
            // it won't call us if fragment is stopped or not started.
            if (listResource != null && listResource.data != null) {
                Utils.psLog("Got Data" + listResource.message + listResource.toString());


//                if (listResource.message != null && !listResource.message.equals("")) {
//                    prgDialog.get().cancel();
//                } else {
//                    // Update the data
//                    prgDialog.get().cancel();
//                }
                prgDialog.get().cancel();

            } else if (listResource != null && listResource.message != null) {
                Utils.psLog("Message from server.");

                psDialogMsg.showInfoDialog(listResource.message, getString(R.string.app__ok));
                psDialogMsg.show();

                prgDialog.get().cancel();
            } else {
                //noinspection Constant Conditions
                Utils.psLog("Empty Data");

            }

        });
    }

    private boolean checkToUpdateProfile() {

        return binding.get().userNameEditText.getText().toString().equals(userViewModel.user.userName) &&
                binding.get().userEmailEditText.getText().toString().equals(userViewModel.user.userEmail) &&
                binding.get().userPhoneEditText.getText().toString().equals(userViewModel.user.userPhone) &&
                binding.get().userAddressEditText.getText().toString().equals(userViewModel.user.userAddress) &&
                binding.get().userCityEditText.getText().toString().equals(userViewModel.user.city);
    }

    private void updateUserProfile() {
        User user = new User(
                userViewModel.user.userId, userViewModel.user.userIsSysAdmin, userViewModel.user.isCityAdmin, userViewModel.user.facebookId, userViewModel.user.phoneId,
                userViewModel.user.googleId, binding.get().userNameEditText.getText().toString(), binding.get().userEmailEditText.getText().toString(), binding.get().userPhoneEditText.getText().toString(), binding.get().userAddressEditText.getText().toString(),
                userViewModel.user.city, userViewModel.user.userEmail, userViewModel.user.userAboutMe, userViewModel.user.userCoverPhoto, userViewModel.user.userProfilePhoto,
                userViewModel.user.roleId, userViewModel.user.status, userViewModel.user.isBanned, userViewModel.user.addedDate, userViewModel.user.deviceToken,
                userViewModel.user.code, userViewModel.user.overallRating, userViewModel.user.whatsapp, userViewModel.user.messenger, userViewModel.user.followerCount,
                userViewModel.user.followingCount, userViewModel.user.isFollowed, userViewModel.user.added_date_str, userViewModel.user.verifyTypes, userViewModel.user.emailVerify,
                userViewModel.user.facebookVerify, userViewModel.user.googleVerify, userViewModel.user.phoneVerify, userViewModel.user.ratingCount, userViewModel.user.namaUmkm,
                userViewModel.user.namaPemilik, userViewModel.user.kodeAkses, userViewModel.user.nomorWa, userViewModel.user.alamat, userViewModel.user.provinsi,
                userViewModel.user.kabupaten, userViewModel.user.kecamatan, userViewModel.user.kelurahan, userViewModel.user.namaKades, userViewModel.user.noHpKades,
                "penjual", userViewModel.user.lat, userViewModel.user.lng, userViewModel.user.ratingDetails);

        userViewModel.setUpdateUserObj(user);

        prgDialog.get().show();
    }

    @Override
    public void onDispatched() {

    }




    //endregion
}
