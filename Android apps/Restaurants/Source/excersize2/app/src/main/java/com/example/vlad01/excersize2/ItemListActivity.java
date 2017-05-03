package com.example.vlad01.excersize2;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


import com.example.vlad01.excersize2.places.PlacesContent;

import java.util.List;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ItemDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class ItemListActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    //private boolean mTwoPane;

    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    long updateInterval = 10000;
    //convert to meters
    double radius = 500; //in feet

     //measured in feet
    //boolean blockLocationUpdate = false;
    final int RQS_GooglePlayServices = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CharSequence muTitle = getTitle();
        toolbar.setTitle(muTitle);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

         RecyclerView recyclerView = (RecyclerView)findViewById(R.id.item_list);
         //recyclerView.addItemDecoration(new RecyclerView.ItemDecoration());
//        assert recyclerView != null;
//        setupRecyclerView((RecyclerView) recyclerView);

//        if (findViewById(R.id.item_detail_container) != null) {
//            // The detail container view will be present only in the
//            // large-screen layouts (res/values-w900dp).
//            // If this view is present, then the
//            // activity should be in two-pane mode.
//            mTwoPane = true;
//        }
        //locations
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .enableAutoManage(this, 0 , this)
                    .addApi(Places.GEO_DATA_API)
                    .addApi(Places.PLACE_DETECTION_API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    boolean blockPick = true ;
    @Override
    protected void onResume() {
        super.onResume();

        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());

        if (resultCode == ConnectionResult.SUCCESS) {
//            Toast.makeText(getApplicationContext(),
//                    "isGooglePlayServicesAvailable SUCCESS",
//                    Toast.LENGTH_LONG).show();
        }else{
            GooglePlayServicesUtil.getErrorDialog(resultCode, this, RQS_GooglePlayServices);
        }

        AppCompatSpinner mySpinner = (AppCompatSpinner) findViewById(R.id.spinner);
        if (mySpinner != null) {
          mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
              @Override
              public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
              {
                 if (blockPick)
                 {
                     blockPick = false;
                     return;

                 }
                 radius = Integer.parseInt((String) parent.getSelectedItem());
                  onLocationChanged(); //not really but we say so
              };

              @Override
              public void onNothingSelected(AdapterView<?> parent) {

              }
          });
        }
    }

    public final void onLocationChanged()
    {

        if (dontBother)
        {
            dontBother = false;
            return;
        }
        if (mLastLocation != null)
            onFindPlaces();


    }
    public void onFindPlaces()
    {
        if (mGoogleApiClient != null) {
            //use web serices for Ploace Searc
            int radiusInMeter = (int) (radius / 3.28);
            double lat = mLastLocation.getLatitude();
            double lng = mLastLocation.getLongitude();
            String type = "bar|cafe|meal_take_away|restaurant";
            StringBuilder strb = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=")
                    .append(lat).append(",").append(lng).append("&radius=").append(radiusInMeter).append("&type=").append(type).append("&key=").append(this.getString(R.string.google_api_key));

            String urlPlaces = strb.toString();
            new GetJson(this).execute(urlPlaces, "places");
        }
        else{
            Toast.makeText(ItemListActivity.this, "No GoogleApiClient", Toast.LENGTH_SHORT).show();
        }
    }

    boolean dontBother = false;
    //implement interfaces
    @Override
    public void onConnected(Bundle bundle) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        onLocationChanged();
        dontBother = true;
        //monitor location changes
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        LocationListener listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                if (mLastLocation == null || (mLastLocation.getLatitude() != location.getLatitude()) || (mLastLocation.getLongitude() != location.getLongitude()))
                {
                    mLastLocation = location;
                    ItemListActivity.this.onLocationChanged();
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };


        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 60000, 100, listener);

    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }


    public static void showAlert(String message, Context context) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        // set title
        alertDialogBuilder.setTitle("");

        // set dialog message
        alertDialogBuilder
                .setMessage(message)
                .setCancelable(false)
                .setNegativeButton("Close",new DialogInterface.OnClickListener()
                   {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });


        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }


}

