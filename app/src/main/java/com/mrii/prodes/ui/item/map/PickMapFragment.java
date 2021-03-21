package com.mrii.prodes.ui.item.map;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.mrii.prodes.Config;
import com.mrii.prodes.R;
import com.mrii.prodes.binding.FragmentDataBindingComponent;
import com.mrii.prodes.databinding.FragmentPickMapBinding;
import com.mrii.prodes.ui.common.PSFragment;
import com.mrii.prodes.utils.AutoClearedValue;
import com.mrii.prodes.utils.Constants;
import com.mrii.prodes.utils.Utils;
import com.skyfishjy.library.RippleBackground;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class PickMapFragment extends PSFragment {

    private String latValue = "-0.989818";
    private String lngValue = "113.915863";

    Geocoder geocoder;
    List<Address> addresses;
    private String address;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private PlacesClient placesClient;
    private List<AutocompletePrediction> predictionList;

    private Location mLastKnownLocation;
    private LocationCallback locationCallback;

    private View mapView;
    private Button btnFind;

    private RippleBackground rippleBackground;

    private final float DEFAULT_ZOOM = 18;

    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);
    private GoogleMap map;
    private MarkerOptions markerOptions = new MarkerOptions();

    @VisibleForTesting
    private AutoClearedValue<FragmentPickMapBinding> binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        FragmentPickMapBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_pick_map, container, false, dataBindingComponent);

        binding = new AutoClearedValue<>(this, dataBinding);

        setHasOptionsMenu(true);

        initializeMap(savedInstanceState);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        Places.initialize(getActivity(), "AIzaSyAfelC6EvUF3dMkbR1pvr2LIq7FZL9XGrk");
        placesClient = Places.createClient(getActivity());
        final AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();
        binding.get().btnFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final LatLng currentMarkerLocation = map.getCameraPosition().target;
                final Double lat_add = currentMarkerLocation.latitude;
                final Double lng_add = currentMarkerLocation.longitude;
                latValue = String.valueOf(lat_add);
                lngValue = String.valueOf(lng_add);
                geocoder = new Geocoder(getActivity(), Locale.getDefault());
                try {
                    addresses = geocoder.getFromLocation(lat_add,lng_add,1);
                    address = addresses.get(0).getAddressLine(0);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                navigationController.navigateBackFromMapView(getActivity(), latValue, lngValue, address);

                binding.get().rippleBg.startRippleAnimation();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        binding.get().rippleBg.stopRippleAnimation();
                        getActivity().finish();
                    }
                }, 2000);

                System.out.println("cek"+currentMarkerLocation.latitude+" "+currentMarkerLocation.longitude);

            }
        });
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

        binding.get().mapView.onCreate(savedInstanceState);
        binding.get().mapView.onResume();

        binding.get().mapView.getMapAsync(googleMap -> {
            map = googleMap;
            map.setMyLocationEnabled(true);
            map.getUiSettings().setMyLocationButtonEnabled(true);

            map.setMyLocationEnabled(true);
            map.getUiSettings().setMyLocationButtonEnabled(true);

            if (mapView != null && mapView.findViewById(Integer.parseInt("1")) != null){
                View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
                layoutParams.setMargins(0, 0, 40, 180);
            }

            //cek apakah gps diaktifkan atau tidak
            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setInterval(1000);
            locationRequest.setFastestInterval(5000);
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);

            SettingsClient settingsClient = LocationServices.getSettingsClient(getActivity());
            Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(builder.build());

            task.addOnSuccessListener(getActivity(), new OnSuccessListener<LocationSettingsResponse>() {
                @Override
                public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                    getDeviceLocation();
                }
            });

            task.addOnFailureListener(getActivity(), new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    if (e instanceof ResolvableApiException) {
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        try {
                            resolvable.startResolutionForResult(getActivity(), 51);
                        } catch (IntentSender.SendIntentException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            });

//            map.addMarker(new MarkerOptions()
//                    .position(new LatLng(Double.valueOf(latValue), Double.valueOf(lngValue)))
//                    .title("Nama Tempat"));

            //zoom
            if (!latValue.isEmpty() && !lngValue.isEmpty()) {
                int zoomlevel = 15;
                // Animating to the touched position
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(latValue), Double.parseDouble(lngValue)), zoomlevel));
            }
            /*
            map.setOnMapClickListener(latLng -> {
                // Creating a marker
                // Setting the position for the marker
                map.clear();

                markerOptions.position(latLng);

                // Setting the title for the marker.
                // This will be displayed on taping the marker
                markerOptions.title(latLng.latitude + " : " + latLng.longitude);

                markerOptions.draggable(true);

                latValue = String.valueOf(latLng.latitude);
                lngValue = String.valueOf(latLng.longitude);

                // Clears the previously touched position
                map.clear();

                // Animating to the touched position
                map.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                // Placing a marker
                // the touched position
                map.addMarker(markerOptions);
            });
             */

        });


    }

    private void getDeviceLocation() {
        mFusedLocationProviderClient.getLastLocation()
                .addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            mLastKnownLocation = task.getResult();
                            if (mLastKnownLocation != null) {
                                map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                            } else {
                                final LocationRequest locationRequest = LocationRequest.create();
                                locationRequest.setInterval(10000);
                                locationRequest.setFastestInterval(5000);
                                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                                locationCallback = new LocationCallback() {
                                    @Override
                                    public void onLocationResult(LocationResult locationResult) {
                                        super.onLocationResult(locationResult);
                                        if (locationResult == null) {
                                            return;
                                        }
                                        mLastKnownLocation = locationResult.getLastLocation();
                                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                                        mFusedLocationProviderClient.removeLocationUpdates(locationCallback);
                                    }
                                };
                                mFusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);

                            }
                        } else {
                            Toast.makeText(getActivity(), "unable to get last location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

//    @Override
//    public void onCreateOptionsMenu(@NonNull Menu menu,@NonNull MenuInflater inflater) {
//        inflater.inflate(R.menu.menu_pick_location, menu);
//        super.onCreateOptionsMenu(menu, inflater);
//    }

//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//
//        if (item.getItemId() == R.id.pickButton) {
//            Utils.psLog("I am here for ok Button");
//
//            navigationController.navigateBackFromMapView(getActivity(), latValue, lngValue);
//
//            if (getActivity() != null) {
//                getActivity().finish();
//            }
//        }
//
//        return super.onOptionsItemSelected(item);
//    }


    @Override
    public void onDestroyView() {

        super.onDestroyView();
    }

    @Override
    public void onDestroy() {

        if(binding != null) {
            if (binding.get() != null) {
                if (binding.get().mapView != null) {

                    binding.get().mapView.onDestroy();

                    if (map != null) {
                        map.clear();
                    }

                }
            }
        }
        super.onDestroy();

    }

    @Override
    public void onLowMemory() {
        binding.get().mapView.onLowMemory();
        super.onLowMemory();

    }

    @Override
    public void onPause() {
        binding.get().mapView.onPause();
        super.onPause();
    }

    @Override
    public void onResume() {
        binding.get().mapView.onResume();
        super.onResume();
    }

    @Override
    protected void initUIAndActions() {

    }

    @Override
    protected void initViewModels() {

    }

    @Override
    protected void initAdapters() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 51){
            if (resultCode == getActivity().RESULT_OK){
                getDeviceLocation();
            }
        }
    }

    private void initVermisionLocation() {
        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        }
    }

    @Override
    protected void initData() {

        initVermisionLocation();

        if (getActivity() != null) {
            latValue = getActivity().getIntent().getStringExtra(Constants.LAT);
            //latValue = "-6.200000";
            lngValue = getActivity().getIntent().getStringExtra(Constants.LNG);
            //lngValue = "106.816666";
        }

    }

}
