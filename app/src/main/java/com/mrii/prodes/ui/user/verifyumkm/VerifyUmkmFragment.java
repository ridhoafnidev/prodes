package com.mrii.prodes.ui.user.verifyumkm;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.VisibleForTesting;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mrii.prodes.MainActivity;
import com.mrii.prodes.R;
import com.mrii.prodes.binding.FragmentDataBindingComponent;
import com.mrii.prodes.databinding.FragmentVerifyMobileBinding;
import com.mrii.prodes.databinding.FragmentVerifyUmkmBinding;
import com.mrii.prodes.ui.common.PSFragment;
import com.mrii.prodes.utils.AutoClearedValue;
import com.mrii.prodes.utils.Constants;
import com.mrii.prodes.utils.PSDialogMsg;
import com.mrii.prodes.utils.Utils;
import com.mrii.prodes.viewmodel.user.UserViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class VerifyUmkmFragment extends PSFragment {

    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);


    private UserViewModel userViewModel;

    private PSDialogMsg psDialogMsg;

    private String userPhoneNo;

    @VisibleForTesting
    private AutoClearedValue<FragmentVerifyUmkmBinding> binding;

    private AutoClearedValue<ProgressDialog> prgDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        // Inflate the layout for this fragment
        FragmentVerifyUmkmBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_verify_umkm, container, false, dataBindingComponent);

        binding = new AutoClearedValue<>(this, dataBinding);

        if (getActivity() != null && getActivity().getIntent() != null) {
            userPhoneNo = getActivity().getIntent() .getStringExtra(Constants.USER_PHONE);

            if (getArguments() != null) {
                userPhoneNo = getArguments().getString(Constants.USER_PHONE);
            }

        }

        // sendVerificationCode(userPhoneNo);

        return binding.get().getRoot();
    }

    @Override
    protected void initUIAndActions() {
        if (getActivity() instanceof MainActivity) {
            //((MainActivity) getActivity()).setToolbarText(((MainActivity) getActivity()).binding.toolbar, getString(R.string.verify_umkm__title));
            //((MainActivity) this.getActivity()).binding.toolbar.setBackgroundColor(getResources().getColor(R.color.global__primary));
            ((MainActivity) getActivity()).updateMenuIconWhite();
            ((MainActivity) getActivity()).updateToolbarIconColor(Color.WHITE);

        }

        psDialogMsg = new PSDialogMsg(getActivity(), false);

        prgDialog = new AutoClearedValue<>(this, new ProgressDialog(getActivity()));
        prgDialog.get().setMessage((Utils.getSpannableString(getContext(), getString(R.string.message__please_wait), Utils.Fonts.MM_FONT)));
        prgDialog.get().setCancelable(false);

        // binding.get().phoneTextView.setText(userPhoneNo);

        binding.get().submitButton.setOnClickListener(v -> {
            String umkmCode = binding.get().enterCodeEditText.getText().toString().trim();
            if (umkmCode.isEmpty() || umkmCode.length() < 3) {
                binding.get().enterCodeEditText.setError("Masukan kode yang benar");
                binding.get().enterCodeEditText.requestFocus();
                return;
            }
            // verifying the code entered manually
            verifyVerificationCode(umkmCode);
        });
    }

    private void verifyVerificationCode(String umkmCode) {
        // userViewModel.setPhoneLoginUser(phoneUserId, userName, userPhoneNo, token);

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

    }
}
