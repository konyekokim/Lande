package com.konye.lande;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.services.android.ui.geocoder.GeocoderAutoCompleteView;
import com.mapbox.services.api.geocoding.v5.GeocodingCriteria;
import com.mapbox.services.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.services.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.services.api.geocoding.v5.models.GeocodingResponse;
import com.mapbox.services.commons.models.Position;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MapActivity extends AppCompatActivity {
    private MapView mapView;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private double userLocationLat, userLocationLong, searchedLatitude, searchedLongitude;
    private double camLat, camLong;
    MapboxGeocoding mapboxGeocoding;
    CameraPosition position;
    String searchLocation, areaLocationString, areaLatitudeString, areaLongitudeString;
    Position userPosition, searchedPosition,secondPosition;
    TextView latTextView, longTextView;
    Dialog areaCoordinatesDialog, paymentDialog, makePaymentDialog, addCardDialog, choiceDialog, locationDetailsDialog, formTypeDialog;
    EditText areaLocation, areaLatitude, areaLongitude;
    Button areaConfirmButton, payNowButton, addCardButton, makePaymentPayButton,
            addCardConfirmButton, addCardSkipButton, verificationCFO, freshLandApplication, individualBtn, organisationBtn;
    TextView makePaymentPriceTextView, makePaymentCardNumber, validityDropText, ownershipDropText, landDisputeDropText, governmentHistoryDropText;
    RelativeLayout validityLayout, ownershipLayout, landDisputeLayout, governmentHistoryLayout;
    boolean validityExpanded, ownershipExpanded, landDisputeExpanded, governmentHistoryExpanded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, "pk.eyJ1Ijoia29ueWVrb2tpbSIsImEiOiJjajVtcGN4Z2gzMngxMndvZGE4ZmhraXF0In0.X7pzRKamw6Yp_tnoQuXncA");
        setContentView(R.layout.activity_map);

        latTextView = (TextView) findViewById(R.id.latitude_textview);
        longTextView = (TextView) findViewById(R.id.longitude_textview);

        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);


        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("helvetica_font_normal.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        /*GeocoderAutoCompleteView autoComplete = (GeocoderAutoCompleteView) findViewById(R.id.autoCompleteWidget);
        autoComplete.setAccessToken(Mapbox.getAccessToken());
        autoComplete.setType(GeocodingCriteria.TYPE_POI);
        autoComplete.setOnFeatureListener(new GeocoderAutoCompleteView.OnFeatureListener() {
            @Override
            public void onFeatureClick(CarmenFeature feature) {
                secondPosition = feature.asPosition();
                // using the position you can drop a marker or move the map's camera.
            }
        });*/

        /*CameraPosition camPosition = new CameraPosition.Builder()
                .target(new LatLng(40.0000,-10.0000))
                .zoom(10)
                .tilt(10)
                .build();*/

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final MapboxMap mapboxMap) {

                // Toast instructing user to tap on the map
                Toast.makeText(
                        MapActivity.this,
                        "tap on map",
                        Toast.LENGTH_LONG
                ).show();

                // When user clicks the map, animate to new camera location
                mapboxMap.setOnMapClickListener(new MapboxMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(@NonNull LatLng point) {
                        //configureButton();
                        if(searchedPosition != null){
                            position = new CameraPosition.Builder()
                                    .target(new LatLng(searchedPosition.getLatitude(),searchedPosition.getLongitude()))
                                    .zoom(16)
                                    .tilt(0)
                                    .build();
                            mapboxMap.animateCamera(CameraUpdateFactory
                                    .newCameraPosition(position), 5000);

                            mapboxMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(searchedPosition.getLatitude(),searchedPosition.getLongitude()))
                                    .title("searched Location")
                            );
                        }else{
                            camLat = userLocationLat;
                            camLong = userLocationLong;
                            position = new CameraPosition.Builder()
                                    .target(new LatLng(camLat, camLong)) // Sets the new camera position
                                    .zoom(15) // Sets the zoom
                                    .tilt(0) // Set the camera tilt
                                    .build(); // Creates a CameraPosition from the builder

                            mapboxMap.animateCamera(CameraUpdateFactory
                                    .newCameraPosition(position), 5000);
                            latTextView.setText(Double.toString(userLocationLat));
                            longTextView.setText(Double.toString(userLocationLong));

                            mapboxMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(userLocationLat, userLocationLong))
                                    .title("User Location")
                            );
                        }
                    }
                });
            }
        });


        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                userLocationLat = location.getLatitude();
                userLocationLong = location.getLongitude();
                userPosition = Position.fromCoordinates(userLocationLat, userLocationLong);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 0, locationListener);
        }

        AreaCoordinatesDialog();
        areaConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                areaLocationString = areaLocation.getText().toString().trim();
                areaLatitudeString = areaLatitude.getText().toString().trim();
                areaLongitudeString = areaLongitude.getText().toString().trim();
                if(!TextUtils.isEmpty(areaLocationString) || (!TextUtils.isEmpty(areaLatitudeString)
                        && !TextUtils.isEmpty(areaLongitudeString))){
                    searchLocation = areaLocationString;
                    //searchedLatitude = Double.parseDouble(areaLatitudeString);
                    //searchedLongitude = Double.parseDouble(areaLongitudeString);
                    getSearchedLocation();
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
                                    ChoiceDialog();
                                    verificationCFO.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            //do stuff
                                            choiceDialog.cancel();
                                            LocationDetailsDialog();
                                            locationDetailsDialog.show();
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
                                            ChoiceDialog();
                                            verificationCFO.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    //do stuff
                                                    choiceDialog.cancel();
                                                    LocationDetailsDialog();
                                                    locationDetailsDialog.show();
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
                                    });
                                    makePaymentDialog.show();
                                }
                            });
                            addCardDialog.show();
                        }
                    });
                    paymentDialog.show();
                }else{
                    Toast.makeText(MapActivity.this,"please put in location or coordinates",Toast.LENGTH_LONG).show();
                }
            }
        });
        areaCoordinatesDialog.show();

    }

    private void getSearchedLocation(){
        mapboxGeocoding = new MapboxGeocoding.Builder()
                .setAccessToken(Mapbox.getAccessToken())
                .setLocation(searchLocation)
                .build();

        mapboxGeocoding.enqueueCall(new Callback<GeocodingResponse>() {
            @Override
            public void onResponse(Call<GeocodingResponse> call, Response<GeocodingResponse> response) {
                List<CarmenFeature> results = response.body().getFeatures();
                if (results.size() > 0) {
                    // Log the first results position.
                    searchedPosition = results.get(0).asPosition();
                    //Log.d("yes result", "onResponse: " + firstResultPos.toString());
                } else {
                    // No result for your request were found.
                    //Log.d("no result", "onResponse: No result found");
                }
            }

            @Override
            public void onFailure(Call<GeocodingResponse> call, Throwable throwable) {
                throwable.printStackTrace();
            }
        });
    }
    private void AreaCoordinatesDialog(){
        areaCoordinatesDialog = new Dialog(MapActivity.this);
        areaCoordinatesDialog.setContentView(R.layout.area_coordinates_dialog);
        areaCoordinatesDialog.setCancelable(true);
        //set up widgets in dialog layout
        areaLocation = (EditText) areaCoordinatesDialog.findViewById(R.id.area_cod_location);
        areaLatitude = (EditText) areaCoordinatesDialog.findViewById(R.id.area_cod_latitude);
        areaLongitude = (EditText) areaCoordinatesDialog.findViewById(R.id.area_cod_longitude);
        areaConfirmButton = (Button) areaCoordinatesDialog.findViewById(R.id.area_confirm_butn);

    }

    private void PaymentDialog(){
        paymentDialog = new Dialog(MapActivity.this);
        paymentDialog.setContentView(R.layout.payment_dialog);
        paymentDialog.setCancelable(true);
        //set up widgets in dialog layout
        payNowButton = (Button) paymentDialog.findViewById(R.id.pay_now_butn);
        addCardButton = (Button) paymentDialog.findViewById(R.id.add_card_butn);
    }

    private void MakePaymentDialog(){
        makePaymentDialog = new Dialog(MapActivity.this);
        makePaymentDialog.setContentView(R.layout.make_payment_dialog);
        makePaymentDialog.setCancelable(true);
        //set up widgets in dialog layout
        makePaymentCardNumber = (TextView) makePaymentDialog.findViewById(R.id.make_payment_cardNumber);
        makePaymentPriceTextView = (TextView) makePaymentDialog.findViewById(R.id.make_payment_price_textView);
        makePaymentPayButton = (Button) makePaymentDialog.findViewById(R.id.make_payment_pay_butn);
    }

    private void ChoiceDialog(){
        choiceDialog = new Dialog(MapActivity.this);
        choiceDialog.setContentView(R.layout.choice_dialog);
        choiceDialog.setCancelable(true);
        //set up the widgets in the dialog layout
        verificationCFO = (Button) choiceDialog.findViewById(R.id.choice_verification_button);
        freshLandApplication = (Button) choiceDialog.findViewById(R.id.choice_fresh_land_app_button);
    }

    private void LocationDetailsDialog(){
        locationDetailsDialog = new Dialog(MapActivity.this);
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
        formTypeDialog = new Dialog(MapActivity.this);
        formTypeDialog.setContentView(R.layout.form_type_dialog);
        formTypeDialog.setCancelable(true);
        //set up the widgets in the dialog layout
        individualBtn = (Button) formTypeDialog.findViewById(R.id.form_type_ind_button);
        organisationBtn = (Button) formTypeDialog.findViewById(R.id.form_type_org_button);
    }

    private void AddCardDialog(){
        addCardDialog = new Dialog(MapActivity.this);
        addCardDialog.setContentView(R.layout.add_card_dialog);
        addCardDialog.setCancelable(true);
        //set up widgets in dialog layout
        addCardConfirmButton = (Button) addCardDialog.findViewById(R.id.add_card_confirm_butn);
        addCardSkipButton = (Button) addCardDialog.findViewById(R.id.add_card_skipButton);
        addCardSkipButton.setVisibility(View.GONE);
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Cancel the directions API request
        if (mapboxGeocoding != null) {
            mapboxGeocoding.cancelCall();
        mapView.onDestroy();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
