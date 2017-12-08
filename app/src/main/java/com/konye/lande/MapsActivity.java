package com.konye.lande;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mapbox.services.commons.models.Position;

import java.io.IOException;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private final static int MY_PERMISSION_FINE_LOCATION = 101;

    Dialog areaCoordinatesDialog, paymentDialog, makePaymentDialog, addCardDialog, choiceDialog, locationDetailsDialog, formTypeDialog;
    EditText areaLocation, areaLatitude, areaLongitude;
    Button areaConfirmButton, payNowButton, addCardButton, makePaymentPayButton,
            addCardConfirmButton, addCardSkipButton, verificationCFO, freshLandApplication, individualBtn, organisationBtn;
    TextView makePaymentPriceTextView, makePaymentCardNumber, validityDropText, ownershipDropText, landDisputeDropText, governmentHistoryDropText;
    RelativeLayout validityLayout, ownershipLayout, landDisputeLayout, governmentHistoryLayout;
    DrawerLayout mDrawerLayout;
    RelativeLayout historyLayout, settingsLayout, verificationLayout, applicationLayout, navDrawerLayout;
    ImageView topMenuIcon;
    boolean validityExpanded, ownershipExpanded, landDisputeExpanded, governmentHistoryExpanded;
    String searchLocation, areaLocationString, areaLatitudeString, areaLongitudeString, countryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        changeWidgetsFont();
        populateSideMenuWidgets();
        //this shows dialogs
        //showDialogs();
        showDialogsWell();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        /*try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.styles_json));

            if (!success) {
                Log.e("style parsing", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("cant find style", "Can't find style. Error: ", e);
        }*/
        //set up map
        setUpMap();

    }

    private void setUpMap() {
        // Add a marker to users current location
        //mMap.addMarker(new MarkerOptions().position(new LatLng(userLocationLat, userLocationLong)).title("Your current Location"));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_FINE_LOCATION);
            }
        }
        /*camPosition = new CameraPosition.Builder()
                .target(userPosition)
                .zoom(20)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(camPosition));*/
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSION_FINE_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        mMap.setMyLocationEnabled(true);
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "This app requires location permission", Toast.LENGTH_LONG).show();
                    finish();
                }
                break;
        }
    }

    private void populateSideMenuWidgets(){
        mDrawerLayout =(DrawerLayout) findViewById(R.id.maps_drawer_layout);
        navDrawerLayout = (RelativeLayout) findViewById(R.id.navigation_drawer_layout);
        historyLayout = (RelativeLayout) findViewById(R.id.history_side_layout);
        verificationLayout = (RelativeLayout) findViewById(R.id.cofo_side_layout);
        applicationLayout = (RelativeLayout) findViewById(R.id.freshlandapp_side_layout);
        settingsLayout = (RelativeLayout) findViewById(R.id.settings_side_layout);
        topMenuIcon = (ImageView) findViewById(R.id.top_menu_icon);
        topMenuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(navDrawerLayout);
            }
        });
        historyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HistoryActivity.class);
                startActivity(intent);
                finish();
            }
        });
        verificationLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),VerificationsActivity.class);
                startActivity(intent);
                finish();
            }
        });
        applicationLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ApplicationsActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void AreaCoordinatesDialog(){
        areaCoordinatesDialog = new Dialog(MapsActivity.this);
        areaCoordinatesDialog.setContentView(R.layout.area_coordinates_dialog);
        areaCoordinatesDialog.setCancelable(true);
        //set up widgets in dialog layout
        areaLocation = (EditText) areaCoordinatesDialog.findViewById(R.id.area_cod_location);
        areaLatitude = (EditText) areaCoordinatesDialog.findViewById(R.id.area_cod_latitude);
        areaLongitude = (EditText) areaCoordinatesDialog.findViewById(R.id.area_cod_longitude);
        areaConfirmButton = (Button) areaCoordinatesDialog.findViewById(R.id.area_confirm_butn);

    }

    private void PaymentDialog(){
        paymentDialog = new Dialog(MapsActivity.this);
        paymentDialog.setContentView(R.layout.payment_dialog);
        paymentDialog.setCancelable(true);
        //set up widgets in dialog layout
        payNowButton = (Button) paymentDialog.findViewById(R.id.pay_now_butn);
        addCardButton = (Button) paymentDialog.findViewById(R.id.add_card_butn);
    }

    private void MakePaymentDialog(){
        makePaymentDialog = new Dialog(MapsActivity.this);
        makePaymentDialog.setContentView(R.layout.make_payment_dialog);
        makePaymentDialog.setCancelable(true);
        //set up widgets in dialog layout
        makePaymentCardNumber = (TextView) makePaymentDialog.findViewById(R.id.make_payment_cardNumber);
        makePaymentPriceTextView = (TextView) makePaymentDialog.findViewById(R.id.make_payment_price_textView);
        makePaymentPayButton = (Button) makePaymentDialog.findViewById(R.id.make_payment_pay_butn);
    }

    private void ChoiceDialog(){
        choiceDialog = new Dialog(MapsActivity.this);
        choiceDialog.setContentView(R.layout.choice_dialog);
        choiceDialog.setCancelable(true);
        //set up the widgets in the dialog layout
        verificationCFO = (Button) choiceDialog.findViewById(R.id.choice_verification_button);
        freshLandApplication = (Button) choiceDialog.findViewById(R.id.choice_fresh_land_app_button);
    }

    private void LocationDetailsDialog(){
        locationDetailsDialog = new Dialog(MapsActivity.this);
        locationDetailsDialog.setContentView(R.layout.location_details_dialog);
        locationDetailsDialog.setCancelable(true);
        validityLayout = (RelativeLayout) locationDetailsDialog.findViewById(R.id.validity_layout);
        ownershipLayout = (RelativeLayout) locationDetailsDialog.findViewById(R.id.ownership_layout);
        landDisputeLayout = (RelativeLayout) locationDetailsDialog.findViewById(R.id.land_dispute_layout);
        governmentHistoryLayout = (RelativeLayout) locationDetailsDialog.findViewById(R.id.government_history_layout);
        validityDropText = (TextView) locationDetailsDialog.findViewById(R.id.validity_drop_text);
        ownershipDropText = (TextView) locationDetailsDialog.findViewById(R.id.ownership_drop_text);
        landDisputeDropText = (TextView) locationDetailsDialog.findViewById(R.id.land_dispute_drop_text);
        governmentHistoryDropText = (TextView) locationDetailsDialog.findViewById(R.id.government_drop_text);

        validityLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validityExpanded = !validityExpanded;
                validityDropText.setVisibility(validityExpanded ? View.VISIBLE : View.GONE);
            }
        });
        ownershipLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ownershipExpanded = !ownershipExpanded;
                ownershipDropText.setVisibility(ownershipExpanded ? View.VISIBLE : View.GONE);
            }
        });
        landDisputeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                landDisputeExpanded = !landDisputeExpanded;
                landDisputeDropText.setVisibility(landDisputeExpanded ? View.VISIBLE : View.GONE);
            }
        });
        governmentHistoryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                governmentHistoryExpanded = !governmentHistoryExpanded;
                governmentHistoryDropText.setVisibility(governmentHistoryExpanded ? View.VISIBLE : View.GONE);
            }
        });
    }

    private void FormTypeDialog(){
        formTypeDialog = new Dialog(MapsActivity.this);
        formTypeDialog.setContentView(R.layout.form_type_dialog);
        formTypeDialog.setCancelable(true);
        //set up the widgets in the dialog layout
        individualBtn = (Button) formTypeDialog.findViewById(R.id.form_type_ind_button);
        organisationBtn = (Button) formTypeDialog.findViewById(R.id.form_type_org_button);
    }

    private void AddCardDialog(){
        addCardDialog = new Dialog(MapsActivity.this);
        addCardDialog.setContentView(R.layout.add_card_dialog);
        addCardDialog.setCancelable(true);
        //set up widgets in dialog layout
        addCardConfirmButton = (Button) addCardDialog.findViewById(R.id.add_card_confirm_butn);
        addCardSkipButton = (Button) addCardDialog.findViewById(R.id.add_card_skipButton);
        addCardSkipButton.setVisibility(View.GONE);
    }

    private void showDialogsWell(){
        ChoiceDialog();
        verificationCFO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //do stuff
                choiceDialog.cancel();
                AreaCoordinatesDialog();
                areaConfirmButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        areaLocationString = areaLocation.getText().toString().trim();
                        areaLatitudeString = areaLatitude.getText().toString().trim();
                        areaLongitudeString = areaLongitude.getText().toString().trim();
                        if (!TextUtils.isEmpty(areaLocationString) || (!TextUtils.isEmpty(areaLatitudeString)
                                && !TextUtils.isEmpty(areaLongitudeString))) {
                            searchLocation = areaLocationString;
                            //searchedLatitude = Double.parseDouble(areaLatitudeString);
                            //searchedLongitude = Double.parseDouble(areaLongitudeString);
                            try {
                                getSearchedLocation();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            areaCoordinatesDialog.cancel();
                            PaymentDialog();
                            payNowButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    paymentDialog.cancel();
                                    MakePaymentDialog();
                                    makePaymentPayButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            makePaymentDialog.cancel();
                                            LocationDetailsDialog();
                                            locationDetailsDialog.show();
                                        }
                                    });
                                    makePaymentDialog.show();
                                }
                            });

                            addCardButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    paymentDialog.cancel();
                                    AddCardDialog();
                                    addCardConfirmButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            addCardDialog.cancel();
                                            MakePaymentDialog();
                                            makePaymentPayButton.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    makePaymentDialog.cancel();
                                                    LocationDetailsDialog();
                                                    locationDetailsDialog.show();
                                                }
                                            });
                                            makePaymentDialog.show();
                                        }
                                    });
                                    addCardDialog.show();
                                }
                            });
                            paymentDialog.show();
                        } else {
                            Toast.makeText(MapsActivity.this, "please put in location or coordinates", Toast.LENGTH_LONG).show();
                        }
                    }
            });
                areaCoordinatesDialog.show();
            }
        });
        freshLandApplication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //do stuff
                choiceDialog.cancel();
                FormTypeDialog();
                individualBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), IndividualFormActivity.class);
                        startActivity(intent);
                    }
                });
                organisationBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // pass intent here
                    }
                });
                formTypeDialog.show();
            }
        });

        choiceDialog.show();
    }

    private void getSearchedLocation() throws IOException {
        Geocoder geocoder = new Geocoder(this);
        List<Address> addressList = geocoder.getFromLocationName(searchLocation + "Nigeria", 1);
        Address addresses = addressList.get(0);
        LatLng searchedLatLng = new LatLng(addresses.getLatitude(),addresses.getLongitude());
        mMap.addMarker(new MarkerOptions().position(searchedLatLng).title("searched Location"));
        mMap.animateCamera(CameraUpdateFactory.newLatLng(searchedLatLng));

    }

    private void changeWidgetsFont(){
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("helvetica_font_normal.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }



}
