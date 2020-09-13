package id.my.match.parkir.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import id.my.match.parkir.R;
import id.my.match.parkir.activity.admin.MainActivity;
import id.my.match.parkir.activity.pengguna.MainActivityUsr;
import id.my.match.parkir.utility.restapi.ApiClient;

/**
 * Created by ebnim on 10 Apr 2019.
 */

public class Activity_Login extends AppCompatActivity {
    static ProgressDialog progressDialog;
    EditText _emailText, _passwordText;
    static Button _loginButton;
    public static String versionName;
    public static int versionCode;
    public String kode_id, kode_pass;
    ApiClient ApiClient = new ApiClient();
    TextView register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);

        PackageManager manager = this.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            versionCode = info.versionCode;
            versionName = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
        }

        register = findViewById(R.id.register);

        _emailText = findViewById(R.id.userid);
        _passwordText = findViewById(R.id.password);
        _loginButton = findViewById(R.id.btn_login);

        _loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (cek_koneksi(Activity_Login.this)) {
                    login();
                } else {
                    Toast.makeText(Activity_Login.this, "Koneksi Gangguan", Toast.LENGTH_LONG).show();
                }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent register =new Intent(Activity_Login.this, Register.class);
                startActivity(register);
            }
        });
    }

    public void login() {
        if (!validate()) {
            onLoginValidasiSalah();
            return;
        }

        _loginButton.setEnabled(false);

        kode_id = _emailText.getText().toString().trim();
        kode_pass = _passwordText.getText().toString().trim();

        ApiClient.login(kode_id, kode_pass, this, _loginButton);
    }

    public static boolean cek_koneksi(Context cek) {
        ConnectivityManager cm = (ConnectivityManager) cek.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();

        if (info != null && info.isConnected())
        {
            return true;
        }
        else
        {
            return false;
        }

    }

    public void onLoginValidasiBenar(Context context, String status) {
        Intent login;
        if(status.equals("adm")){
            login = new Intent(context, MainActivity.class);
        }else{
            login = new Intent(context, MainActivityUsr.class);
        }
        context.startActivity(login);
        finish();
    }

    public void onLoginValidasiSalah() {
        Toast.makeText(Activity_Login.this, "Login gagal", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
        finish();
        System.exit(0);
    }


    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        /** untuk mengecek email
         if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
         _emailText.setError("enter a valid email address");
         valid = false;
         } else {
         _emailText.setError(null);
         }
         */

        if (email.isEmpty() || email.trim().length() < 1) {
            String isi = "Username Kosong";
            ForegroundColorSpan fgcspan = new ForegroundColorSpan(getResources().getColor(R.color.warning_error));
            SpannableStringBuilder ssbuilder = new SpannableStringBuilder(isi);
            ssbuilder.setSpan(fgcspan, 0,isi.length(), 0);
            _emailText.setError(ssbuilder);
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.trim().length() < 1) {
            String isi = "Password Kosong";
            ForegroundColorSpan fgcspan = new ForegroundColorSpan(getResources().getColor(R.color.warning_error));
            SpannableStringBuilder ssbuilder = new SpannableStringBuilder(isi);
            ssbuilder.setSpan(fgcspan, 0, isi.length(), 0);
            _passwordText.setError(ssbuilder);
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
}
