package com.mrii.prodes.ui.user.verifyphone;


import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.mrii.prodes.MainActivity;
import com.mrii.prodes.R;
import com.mrii.prodes.binding.FragmentDataBindingComponent;
import com.mrii.prodes.databinding.FragmentVerifyMobileBinding;
import com.mrii.prodes.ui.common.PSFragment;
import com.mrii.prodes.utils.AutoClearedValue;
import com.mrii.prodes.utils.Constants;
import com.mrii.prodes.utils.PSDialogMsg;
import com.mrii.prodes.utils.Utils;
import com.mrii.prodes.viewmodel.user.UserViewModel;

import java.lang.reflect.Array;
import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 */
public class VerifyMobileFragment extends PSFragment {


    //region Variables

    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    private UserViewModel userViewModel;

    private PSDialogMsg psDialogMsg;

    private String userPhoneNo, userName, phoneUserId, token;
    private FirebaseAuth mAuth;
    private String mVerificationId;
    String userType;
    @VisibleForTesting
    private AutoClearedValue<FragmentVerifyMobileBinding> binding;

    private AutoClearedValue<ProgressDialog> prgDialog;

    //endregion


    //region Override Methods

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        // Inflate the layout for this fragment
        FragmentVerifyMobileBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_verify_mobile, container, false, dataBindingComponent);

        binding = new AutoClearedValue<>(this, dataBinding);

        if (getActivity() != null && getActivity().getIntent() != null) {
            userPhoneNo = getActivity().getIntent() .getStringExtra(Constants.USER_PHONE);
            userName = getActivity().getIntent() .getStringExtra(Constants.USER_NAME);

            if (getArguments() != null) {
                userPhoneNo = getArguments().getString(Constants.USER_PHONE);
                userName = getArguments().getString(Constants.USER_NAME);
            }

        }

        sendVerificationCode(userPhoneNo);

        return binding.get().getRoot();
    }

    private void sendVerificationCode(String no) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                no,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            //Getting the code sent by SMS
            String code = phoneAuthCredential.getSmsCode();

            //sometime the code is not detected automatically
            //in this case the code will be null
            //so user has to manually enter the code
            if (code != null) {
//                if (binding.get().enterCodeEditText != null) {
//                    binding.get().enterCodeEditText.setText(code);
//                }
//                String[] digit=code.split("");
//                for (int i=0;i<code.length();i++){

//                    if (binding.get().digit1!=null){
//                        binding.get().digit1.setText(digit[0]);
//                    }


//                }
                //verifying the code
//                binding.get().digit1.setText(digit[0]);
//                binding.get().digit2.setText(digit[1]);
//                binding.get().digit3.setText(digit[2]);
//                binding.get().digit4.setText(digit[3]);
//                binding.get().digit5.setText(digit[4]);
//                binding.get().digit6.setText(digit[5]);

                verifyVerificationCode(code);
                //verifyVerificationCode(digit[i].toString());

            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            //storing the verification id that is sent to the user
            mVerificationId = s;
        }


    };

    @Override
    protected void initUIAndActions() {

        if (getActivity() instanceof MainActivity) {
            //((MainActivity) getActivity()).setToolbarText(((MainActivity) getActivity()).binding.toolbar, getString(R.string.verify_phone__title));
            //((MainActivity) this.getActivity()).binding.toolbar.setBackgroundColor(getResources().getColor(R.color.global__primary));
            //((MainActivity) getActivity()).updateMenuIconWhite();
            ((MainActivity) getActivity()).updateToolbarIconColor(Color.WHITE);

        }

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        userType = pref.getString(Constants.USER_TYPE, Constants.EMPTY_STRING);

        psDialogMsg = new PSDialogMsg(getActivity(), false);

        prgDialog = new AutoClearedValue<>(this, new ProgressDialog(getActivity()));
        prgDialog.get().setMessage((Utils.getSpannableString(getContext(), getString(R.string.message__please_wait), Utils.Fonts.MM_FONT)));
        prgDialog.get().setCancelable(false);

        binding.get().phoneTextView.setText(userPhoneNo);

        binding.get().notSendCodeButton.setOnClickListener(v -> {
            navigationController.navigateToPhoneLoginActivity(getActivity());
        });

        binding.get().verificationButton.setOnClickListener(v -> {

            String code = binding.get().enterCodeEditText.getText().toString().trim();
            if (code.isEmpty() || code.length() < 6) {
                binding.get().enterCodeEditText.setError("Masukan kode yang benar");
                binding.get().enterCodeEditText.requestFocus();
                return;
            }

//                verifying the code entered manually
            verifyVerificationCode(code);

//            String code1 = binding.get().digit1.getText().toString().trim();
//            String code2 = binding.get().digit2.getText().toString().trim();
//            String code3 = binding.get().digit3.getText().toString().trim();
//            String code4 = binding.get().digit4.getText().toString().trim();
//            String code5 = binding.get().digit5.getText().toString().trim();
//            String code6 = binding.get().digit6.getText().toString().trim();

//            if (code1.isEmpty() || code1.length() < 1) {
//                Toast.makeText(getContext(),"Enter Valid Code",Toast.LENGTH_SHORT).show();
//                //binding.get().enterCodeEditText.setError("Enter valid code");
//                binding.get().digit1.requestFocus();
//                return;
//            }
//            else if (code2.isEmpty() || code2.length() < 1) {
//                Toast.makeText(getContext(),"Enter Valid Code",Toast.LENGTH_SHORT).show();
//                //binding.get().enterCodeEditText.setError("Enter valid code");
//                binding.get().digit2.requestFocus();
//                return;
//            }
//            else if (code3.isEmpty() || code3.length() < 1) {
//                Toast.makeText(getContext(),"Enter Valid Code",Toast.LENGTH_SHORT).show();
//                //binding.get().enterCodeEditText.setError("Enter valid code");
//                binding.get().digit3.requestFocus();
//                return;
//            }
//            else if (code4.isEmpty() || code4.length() < 1) {
//                Toast.makeText(getContext(),"Enter Valid Code",Toast.LENGTH_SHORT).show();
//                //binding.get().enterCodeEditText.setError("Enter valid code");
//                binding.get().digit4.requestFocus();
//                return;
//            }
//            else if (code5.isEmpty() || code5.length() < 1) {
//                Toast.makeText(getContext(),"Enter Valid Code",Toast.LENGTH_SHORT).show();
//                //binding.get().enterCodeEditText.setError("Enter valid code");
//                binding.get().digit5.requestFocus();
//                return;
//            }
//            else if (code6.isEmpty() || code6.length() < 1) {
//                Toast.makeText(getContext(),"Enter Valid Code",Toast.LENGTH_SHORT).show();
//                //binding.get().enterCodeEditText.setError("Enter valid code");
//                binding.get().digit6.requestFocus();
//                return;
//            }

//                verifying the code entered manually
//            if(code1!=null&&code2!=null&&code3!=null&&code4!=null&&code5!=null&&code6!=null){
//                verifyVerificationCode(code1+code2+code3+code4+code5+code6);
//            }

        });
    }

    private void verifyVerificationCode(String code) {
        //creating the credential
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);

        //signing the user
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        if (getActivity() != null) {
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(getActivity(), task -> {
                        if (task.isSuccessful()) {
                            try {
                                //verification successful we will start the profile activity
                                if (task.getResult() != null && task.getResult().getUser() != null) {
                                    phoneUserId = task.getResult().getUser().getUid();
                                    userViewModel.setPhoneLoginUser(phoneUserId, userPhoneNo, token);
                                }
                            } catch (Exception e1) {
                                // Error occurred while creating the File
                                Toast.makeText(getContext(),e1.getMessage(),Toast.LENGTH_LONG).show();
                            }
                        }

                    });
        }
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
        token = pref.getString(Constants.NOTI_TOKEN, Constants.USER_NO_DEVICE_TOKEN);
        userType = pref.getString(Constants.USER_TYPE, Constants.EMPTY_STRING);

        userViewModel.getPhoneLoginData().observe(this, listResource -> {

            if (listResource != null) {

                Utils.psLog("Got Data" + listResource.message + listResource.toString());

                switch (listResource.status) {
                    case LOADING:
                        // Loading State
                        // Data are from Local DB

                        prgDialog.get().show();

                        break;
                    case SUCCESS:
                        // Success State
                        // Data are from Server

                        if (listResource.data != null) {
                            try {
                                Utils.updateUserLoginData(pref, listResource.data.user);
                                userType = listResource.data.user.userType;
                                if ( userType.equalsIgnoreCase(Constants.BUYER)){
                                    Utils.navigateAfterUserLogin(getActivity(), navigationController);
                                } else if ( userType.equalsIgnoreCase(Constants.SELLER) ){
                                    Utils.navigateAfterUserLogin(getActivity(), navigationController);
                                }else{
                                    Utils.navigateAfterUserLoginCodeUmkm(getActivity(),navigationController, userPhoneNo);
                                }
                            } catch (NullPointerException ne) {
                                Utils.psErrorLog("Null Pointer Exception.", ne);
                            } catch (Exception e) {
                                Utils.psErrorLog("Error in getting notification flag data.", e);
                            }

                            userViewModel.isLoading = false;
                            prgDialog.get().cancel();

                        }

                        break;
                    case ERROR:
                        // Error State

                        userViewModel.isLoading = false;
                        prgDialog.get().cancel();

                        psDialogMsg.showErrorDialog(listResource.message, getString(R.string.app__ok));
                        psDialogMsg.show();

                        break;
                    default:
                        // Default

                        break;
                }

            } else {

                // Init Object or Empty Data
                Utils.psLog("Empty Data");

            }

        });

    }
}
