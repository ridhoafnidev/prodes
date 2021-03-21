package com.mrii.prodes.ui.cs;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.LoggingBehavior;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.mrii.prodes.Config;
import com.mrii.prodes.MainActivity;
import com.mrii.prodes.R;
import com.mrii.prodes.binding.FragmentDataBindingComponent;
import com.mrii.prodes.databinding.FragmentCallCenterBinding;
import com.mrii.prodes.databinding.FragmentUserLoginBinding;
import com.mrii.prodes.ui.common.PSFragment;
import com.mrii.prodes.utils.AutoClearedValue;
import com.mrii.prodes.utils.Constants;
import com.mrii.prodes.utils.PSDialogMsg;
import com.mrii.prodes.utils.Utils;
import com.mrii.prodes.viewmodel.user.UserViewModel;
import com.mrii.prodes.viewobject.User;

import java.net.URLEncoder;


/**
 * UserLoginFragment
 */
public class CallCenterFragment extends PSFragment {

    //region Variables
    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    @VisibleForTesting
    private AutoClearedValue<FragmentCallCenterBinding> binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        FragmentCallCenterBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_call_center, container, false, dataBindingComponent);

        binding = new AutoClearedValue<>(this, dataBinding);
        return binding.get().getRoot();
    }

    @Override
    protected void initUIAndActions() {
        if (getActivity() instanceof MainActivity) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
            ((MainActivity) getActivity()).updateMenuIconWhite();
            Toolbar toolbar = (Toolbar) binding.get().toolbar;
            ((MainActivity)getActivity()).setToolbarText(toolbar, getString(R.string.call__center));
            setHasOptionsMenu(true);
        }
        //end

        binding.get().phoneLoginButton.setOnClickListener(v -> {
            String number = "+6282249167841";
            if (!(number.trim().isEmpty() || number.trim().equals("-"))) {
                try {
                    String text = "Halo Prodesian";
                    String textEncoded = URLEncoder.encode(text, "UTF-8");
                    String url = "https://api.whatsapp.com/send?phone="+number+"&text="+textEncoded;
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setPackage("com.whatsapp");
                    i.setData(Uri.parse(url));
                    startActivity(i);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Whatsapp belum terpasang di device kamu", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void initViewModels() {
    }

    @Override
    protected void initAdapters() {
    }

    @Override
    protected void initData() {
    }

}

//endregion

