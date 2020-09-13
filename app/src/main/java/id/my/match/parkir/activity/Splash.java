package id.my.match.parkir.activity;

/**
 * Created by ebnim on 10 Apr 2019.
 */

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import java.util.HashMap;

import id.my.match.parkir.R;
import id.my.match.parkir.activity.admin.MainActivity;
import id.my.match.parkir.activity.pengguna.MainActivityUsr;
import id.my.match.parkir.utility.sqlite.DBHelper;

public class Splash extends AppCompatActivity {
    public static String versionName;
    public static int versionCode;
    public static String PACKAGE_NAME;
    DBHelper dbh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        dbh = new DBHelper(Splash.this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        PACKAGE_NAME = this.getPackageName();

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {

                        if (Build.VERSION.SDK_INT > 22){
                            if ( (!checkIfAlreadyhavePermission()) ) {
                                requestForSpecificPermission();
                            }else if (!checkIfAlreadyhavePermissionGPS()) {
                                requestForSpecificPermissionGPS();
                            }else if (!checkIfAlreadyhavePermissionSDCARD()) {
                                requestForSpecificPermissionSDCARD();
                            }else{
                                if (cek_koneksi(Splash.this)) {
                                    lanjut();
                                } else {
                                    Toast.makeText(Splash.this, "Koneksi Gangguan", Toast.LENGTH_LONG).show();
                                    finish();
                                }
                            }
                        }else{
                            if (cek_koneksi(Splash.this)) {
                                lanjut();
                            } else {
                                Toast.makeText(Splash.this, "Koneksi Gangguan", Toast.LENGTH_LONG).show();
                                finish();
                            }
                        }

                    }
                }, 1000);
    }

    private boolean cek_koneksi(Context cek) {
        // TODO Auto-generated method stub
        ConnectivityManager cm = (ConnectivityManager) cek.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();

        if (info != null && info.isConnected()){
            return true;
        }else{
            return false;
        }
    }

    private boolean checkIfAlreadyhavePermission() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int result2 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int result5 = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE);

//        if ((result == PackageManager.PERMISSION_GRANTED) && (result2 == PackageManager.PERMISSION_GRANTED) && (result3 == PackageManager.PERMISSION_GRANTED) && (result4 == PackageManager.PERMISSION_GRANTED) && (result5 == PackageManager.PERMISSION_GRANTED)) {
        if ((result == PackageManager.PERMISSION_GRANTED) && (result2 == PackageManager.PERMISSION_GRANTED) && (result5 == PackageManager.PERMISSION_GRANTED)) {
            return true;
        } else {
            return false;
        }
    }

    private boolean checkIfAlreadyhavePermissionGPS() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private boolean checkIfAlreadyhavePermissionSDCARD() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestForSpecificPermission() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,}, 101);
    }

    private void requestForSpecificPermissionGPS() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, 101);
    }

    private void requestForSpecificPermissionSDCARD() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 101:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //granted
                    lanjut();
                } else {
                    //not granted
                    lanjut();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void lanjut() {
        PackageManager manager = this.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            versionCode = info.versionCode;
            versionName = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
        }

        HashMap<String, String> cek = dbh.getUserDetails();

        if(cek.size() > 0){
            Log.d("CEKDATA", cek.get("status_akun"));
            if(cek.get("status_akun").equals("adm")){
                Intent adm = new Intent(this, MainActivity.class);
                startActivity(adm);
            }else if(cek.get("status_akun").equals("user")){
                Intent usr = new Intent(this, MainActivityUsr.class);
                startActivity(usr);
            }
        }else{
            Intent login = new Intent(this, Activity_Login.class);
            startActivity(login);
        }
        finish();

    }
}
