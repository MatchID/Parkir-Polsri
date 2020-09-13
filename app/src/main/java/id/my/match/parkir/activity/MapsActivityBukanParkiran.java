package id.my.match.parkir.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

import id.my.match.parkir.R;
import id.my.match.parkir.activity.pengguna.Activity_buat_laporan;
import id.my.match.parkir.utility.restapi.ApiClient;

public class MapsActivityBukanParkiran extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static final int DIALOG_REQUEST = 9001;
    ApiClient api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        api = new ApiClient();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (servicesOK() /*&& initMap()*/) {
            try {

                /*MarkerOptions options = new MarkerOptions()
                        .title(getString(R.string.landon_hotel) + ", " + city)
                        .position(new LatLng(lat, lng));
                mMap.addMarker(options);*/
                //onMapReady(mMap);


                LatLng zoom = new LatLng(-2.982996, 104.732918);

//                mMap.addMarker(new MarkerOptions().position(zoom).title("Marker in Palembang"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

                mMap.getUiSettings().setAllGesturesEnabled(true);
                mMap.getUiSettings().setRotateGesturesEnabled(false);
                mMap.getUiSettings().setTiltGesturesEnabled(false);
                mMap.getUiSettings().setCompassEnabled(true);
//
                mMap.setTrafficEnabled(true);

                Log.i("CEKDATA","BEGINNING");
                mMap.moveCamera(CameraUpdateFactory.newLatLng(zoom));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(14), 2000, null);
                Log.i("CEKDATA","Finishing");

            } /*catch (IOException e) {
                Toast.makeText(this, getString(R.string.error_finding_hotel), Toast.LENGTH_SHORT).show();
            }*/ catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("Check this->", e.getMessage());
            }


            api.bukan_parkir(googleMap, this);

        }
    }

    public boolean servicesOK() {
        int result = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        if (result == ConnectionResult.SUCCESS) {
            return true;
        } else if (GooglePlayServicesUtil.isUserRecoverableError(result)) {
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(result, this, DIALOG_REQUEST);
            dialog.show();
        } else {
            Toast.makeText(this, "Error Connect to Service", Toast.LENGTH_LONG).show();
        }
        return false;
    }
}