
 package com.barmej.streetissues.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.barmej.streetissues.R;
import com.barmej.streetissues.data.Issue;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class AddNewIssueActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final String TAG = AddNewIssueActivity.class.getSimpleName();
    /*
     int Constants required for permissins
     */
    private static final int PERMISSION_REQUEST_ACCESS_LOCATION = 1;
    private static final int PERMISSION_REQUEST_READ_STORAGE = 2;
    private static final int REQUEST_GET_PHOTO = 3;

    /*
     Boolean Variables location and read storage permissin granted
     */
    private boolean mLocationPermissionGranted;
    private boolean mReadStoragePermissionGranted;
    /*
     Lating constatnt for default location
     */
    private static final LatLng DEFAULT_LOCATION = new LatLng( 29.3760641, 47.9643571 );

    /*
     Image uri
     */
    private Uri mIssuePhotoUri;

    /*
     FusedLocatoinProviderClient to get last location
     */
    private FusedLocationProviderClient mLocationProviderClaint;

    /*
     Last location variable
     */
    private Location mlastKnownLocation;

    /*
     Lating variable
     */
    private LatLng mSelectedLating;

    /*
     Google map variable
     */
    private GoogleMap mGoogleMap;

    /*
     ProgressDialog variable
     */
    private ProgressDialog mDialog;

    /*
     Required variables to joint the views to the activity
     */
    private CoordinatorLayout mCoordinatorLayout;
    private TextInputLayout mIssueTitleTextInputLayout;
    private TextInputLayout mIssuedescriptionTextInputLayout;
    private TextInputEditText mIssueTilteTextInputEditText;
    private TextInputEditText mIssueDescriptionTextInputEditText;
    private ImageView mIssueImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_add_new_issue );

        /*
          Set the tool bar
         */
        Toolbar mToolbar = findViewById( R.id.toolBar_home );
        setSupportActionBar( mToolbar );
        getSupportActionBar().setDisplayHomeAsUpEnabled( true );

        /*
         SupportMapFragment to find Map by id
         */
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById( R.id.map );
        mapFragment.getMapAsync( this );

        /*
          Find views by Id's
         */
        mCoordinatorLayout = findViewById( R.id.add_issue_coord_layout );
        mIssueTitleTextInputLayout = findViewById( R.id.input_layout_title );
        mIssuedescriptionTextInputLayout = findViewById( R.id.input_layout_description );
        mIssueTilteTextInputEditText = findViewById( R.id.edit_text_title );
        mIssueDescriptionTextInputEditText = findViewById( R.id.edit_text_description );
        mIssueImageView = findViewById( R.id.image_view_photo );
        Button mChoosePhotoButton = findViewById( R.id.button_choose_photo );
        Button mAddIssueButton = findViewById( R.id.button_add_issue );

        /*
          Call requestLocationPermission() and requestStoragePermission() methods
         */
        requestLocationPermission();
        requestStoragePermission();

        /*
         MchoosePhotoButton setOnclickListener to lunch Gallery within device or emulator by intent
         */
        mChoosePhotoButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lanchGalleryIntent();
            }
        } );

        /*
          AddIssueButton setOnclickListener to add a new Issue information to the firebase
         */
        mAddIssueButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIssueTitleTextInputLayout.setError( null );
                mIssuedescriptionTextInputLayout.setError( null );
                if (TextUtils.isEmpty( mIssueTilteTextInputEditText.getText() )) {
                    mIssueTitleTextInputLayout.setError( getString( R.string.error_msg_title ) );
                } else {
                    if (TextUtils.isEmpty( mIssueDescriptionTextInputEditText.getText() )) {
                        mIssuedescriptionTextInputLayout.setError( getString( R.string.error_msg_description ) );
                    } else {
                        if (mIssuePhotoUri != null) {
                            addIssueToFirebase();

                        }
                    }
                }
            }
        } );

        // get last location
        mLocationProviderClaint = LocationServices.getFusedLocationProviderClient( this );

    }

    /*
      click on location to add issue information to the firebase
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        if (mLocationPermissionGranted) {

            requestDeviceCurrentLocation();
        }
        mGoogleMap.setOnMapClickListener( new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mSelectedLating = latLng;
                mGoogleMap.clear();
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position( mSelectedLating );
                markerOptions.icon( BitmapDescriptorFactory.defaultMarker( BitmapDescriptorFactory.HUE_BLUE ) );
                mGoogleMap.addMarker( markerOptions );
            }
        } );
    }

    /*
      AddIssueTo firebase method
     */
    private void addIssueToFirebase() {
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReference();
        final StorageReference photoStorageReference = storageReference.child( UUID.randomUUID().toString() );
        final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        mDialog = new ProgressDialog( this );
        mDialog.setIndeterminate( true );
        mDialog.setTitle( R.string.app_name );
        mDialog.setMessage( getString( R.string.uploading_photo ) );
        mDialog.show();
        photoStorageReference.putFile( mIssuePhotoUri ).addOnCompleteListener( new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    photoStorageReference.getDownloadUrl().addOnCompleteListener( new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                final Issue journey = new Issue();
                                journey.setTitle( mIssueTilteTextInputEditText.getText().toString() );
                                journey.setDescription( mIssueDescriptionTextInputEditText.getText().toString() );
                                journey.setPhoto( task.getResult().toString() );
                                journey.setLocation( new GeoPoint( mSelectedLating.latitude, mSelectedLating.longitude ) );
                                firebaseFirestore.collection( "issues" ).add( journey ).addOnCompleteListener( new OnCompleteListener<DocumentReference>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentReference> task) {
                                        if (task.isSuccessful()) {
                                            Snackbar.make( mCoordinatorLayout, R.string.add_issue_success, Snackbar.LENGTH_SHORT ).addCallback( new Snackbar.Callback() {
                                                @Override
                                                public void onDismissed(Snackbar transientBottomBar, int event) {
                                                    super.onDismissed( transientBottomBar, event );
                                                    mDialog.dismiss();
                                                    finish();
                                                }
                                            } ).show();
                                        } else {
                                            Snackbar.make( mCoordinatorLayout, R.string.add_issue_failed, Snackbar.LENGTH_LONG ).show();
                                            mDialog.dismiss();
                                        }
                                    }
                                } );
                            } else {
                                Snackbar.make( mCoordinatorLayout, R.string.uploaded_task_failed, Snackbar.LENGTH_LONG ).show();
                                mDialog.dismiss();

                            }
                        }
                    } );
                } else {
                    Snackbar.make( mCoordinatorLayout, R.string.add_issue_failed, Snackbar.LENGTH_LONG );
                    mDialog.dismiss();
                }
            }
        } );
    }

    /*
      Request permission to access location by google map
     */
    private void requestLocationPermission() {
        mLocationPermissionGranted = false;
        if (ContextCompat.checkSelfPermission( getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION ) == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;

        } else {

            ActivityCompat.requestPermissions( this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_ACCESS_LOCATION );
        }
    }

    /*
      Request permission to read external storage
     */
    private void requestStoragePermission() {
        mReadStoragePermissionGranted = false;
        if (ContextCompat.checkSelfPermission( getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE ) == PackageManager.PERMISSION_GRANTED) {
            mReadStoragePermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions( this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_READ_STORAGE );

        }
    }

    /*
      requestDeviceCurrentLocation method to access current user location
     */
    private void requestDeviceCurrentLocation() {

        if (ActivityCompat.checkSelfPermission( this, Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission( this, Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }


        Task<Location> locationResult = mLocationProviderClaint.getLastLocation();
        locationResult.addOnSuccessListener( new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    mlastKnownLocation = location;
                    mSelectedLating = new LatLng( mlastKnownLocation.getLatitude(), mlastKnownLocation.getLongitude() );
                    mGoogleMap.moveCamera( CameraUpdateFactory.newLatLngZoom( mSelectedLating, 15 ) );
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position( mSelectedLating );
                    markerOptions.icon( BitmapDescriptorFactory.defaultMarker( BitmapDescriptorFactory.HUE_BLUE ) );

                } else {

                    mGoogleMap.moveCamera( CameraUpdateFactory.newLatLngZoom( DEFAULT_LOCATION, 15 ) );
                }
            }
        } );
    }

    /*
      onRequestPermissionResult method
     */

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_ACCESS_LOCATION:
                mLocationPermissionGranted = false;
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                    requestDeviceCurrentLocation();
                }
                break;
            case PERMISSION_REQUEST_READ_STORAGE:
                mReadStoragePermissionGranted = false;
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mReadStoragePermissionGranted = true;
                }
                break;
        }
    }

    /*
     onActivityResult method to get result when access device media file
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult( requestCode, resultCode, data );

        if (requestCode == REQUEST_GET_PHOTO) {
            if (resultCode == RESULT_OK) {
                try {
                    mIssuePhotoUri = data.getData();
                    mIssueImageView.setImageURI( mIssuePhotoUri );
                } catch (Exception e) {
                    Snackbar.make( mCoordinatorLayout, R.string.photo_selected_error, Snackbar.LENGTH_LONG ).show();
                }
            }
        }
    }

    /*
     LunchGalleryIntent to access device media and choose image
     */

    private void lanchGalleryIntent() {

        Intent intent = new Intent( Intent.ACTION_GET_CONTENT );
        intent.addCategory( Intent.CATEGORY_OPENABLE );
        intent.setType( "image/*" );
        startActivityForResult( Intent.createChooser( intent, getString( R.string.choose_photo ) ), REQUEST_GET_PHOTO );

    }
}