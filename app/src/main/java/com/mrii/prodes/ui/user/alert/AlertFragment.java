package com.mrii.prodes.ui.user.alert;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.LoggingBehavior;
import com.google.firebase.auth.FirebaseAuth;
import com.mrii.prodes.MainActivity;
import com.mrii.prodes.R;
import com.mrii.prodes.binding.FragmentDataBindingComponent;
import com.mrii.prodes.databinding.FragmentAlertBinding;
import com.mrii.prodes.databinding.FragmentPhoneLoginBinding;
import com.mrii.prodes.ui.common.PSFragment;
import com.mrii.prodes.utils.AutoClearedValue;
import com.mrii.prodes.utils.PSDialogMsg;
import com.mrii.prodes.utils.Utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class AlertFragment extends Fragment {

    //region Variables

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alert, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

}
//endregion