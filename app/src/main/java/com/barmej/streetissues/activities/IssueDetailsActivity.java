package com.barmej.streetissues.activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.barmej.streetissues.R;
import com.barmej.streetissues.data.Issue;
import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class IssueDetailsActivity extends AppCompatActivity implements OnMapReadyCallback {
    /*
     String key to get data fro intent
     */
    public static final String ISSUE_DATA = "issue_data";
    private static final String MAPVIEW_BUNDLE_KEY = "mapViewBundleKey";

    Issue issue;
    /*
     Define required variables
     */
    private ImageView mIssueImageView;
    private TextView mIssueTitleTextView;
    private TextView mIssueDescriptionTextView;
    private Toolbar mToolbar;

    private MapView mMapView;
    private GoogleMap mGoogleMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_issue_details);
        /*
          Set tool bar
         */
        mToolbar = findViewById( R.id.toolBar_home);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /*
         Find views by id's
         */
        mIssueImageView = findViewById(R.id.image_view__issue_photo);
        mIssueTitleTextView = findViewById(R.id.text_view_issue_title);
        mIssueDescriptionTextView = findViewById(R.id.text_view_issue_description);
        mMapView = findViewById( R.id.map_view );
        mMapView.onCreate( savedInstanceState );
        Bundle mapViewBundle = null;
        if(savedInstanceState != null){
            mapViewBundle = savedInstanceState.getBundle( MAPVIEW_BUNDLE_KEY );

        }

        mMapView = findViewById( R.id.map_view );
        mMapView.onCreate( mapViewBundle );
        mMapView.getMapAsync( this );

        /*
          getIntent to get dat from Issue object
         */
        if(getIntent() != null && getIntent().getExtras() != null){
            issue = getIntent().getExtras().getParcelable(ISSUE_DATA);
            if(issue!= null){
                getSupportActionBar().setTitle(issue.getTitle());
                mIssueDescriptionTextView.setText(issue.getDescription());
                Glide.with( this).load(issue.getPhoto()).into(mIssueImageView);

            }
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState( outState );
        Bundle mapViewBundle = outState.getBundle( MAPVIEW_BUNDLE_KEY );
        if(mapViewBundle == null){
            mapViewBundle = new Bundle();
            outState.putBundle( MAPVIEW_BUNDLE_KEY, mapViewBundle );
        }
        mMapView.onSaveInstanceState( mapViewBundle );
    }

/*
  Displaying Issue geopoint location on the map
 */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mGoogleMap = googleMap;
        LatLng latLng = new LatLng(issue.getLocation().getLatitude(), issue.getLocation().getLongitude());
        googleMap.addMarker( new MarkerOptions()
                .position( latLng ));
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom( latLng, 16  );
        googleMap.moveCamera( cameraUpdate );

    }
    /*
     Map Lifecycle mehtods
     */

    @Override
    protected void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }
}