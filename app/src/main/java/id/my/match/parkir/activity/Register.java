package id.my.match.parkir.activity;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import id.my.match.parkir.R;
import id.my.match.parkir.utility.restapi.ApiClient;

/**
 * Created by ebnim on 10 Apr 2019.
 */

public class Register extends AppCompatActivity {
    EditText _emailText, _passwordText, _nama, _tlpn, _nprm;
    static Button _registerButton;
    public String id_kampus="-";
    ApiClient ApiClient = new ApiClient();
    TextView register;
    Spinner kampus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);

        register = findViewById(R.id.register);

        _emailText = findViewById(R.id.userid);
        _passwordText = findViewById(R.id.password);
        _nama = findViewById(R.id.nama);
        _nprm = findViewById(R.id.nprm);
        _tlpn = findViewById(R.id.tlp);

        _registerButton = findViewById(R.id.btn_register);

        kampus = findViewById(R.id.kampus);

        _registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (cek_koneksi(Register.this)) {
                    register();
                } else {
                    Toast.makeText(Register.this, "Koneksi Gangguan", Toast.LENGTH_LONG).show();
                }
            }
        });

        ApiClient.kampus(kampus, this, Register.this);

        kampus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final TextView ID = view.findViewById(R.id.kode);
                id_kampus = ID.getText().toString();
//                Toast.makeText(Register.this, ID.getText().toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void register() {

        if(_emailText.getText().toString().trim().equals("") || _passwordText.getText().toString().trim().equals("") ||
                _nama.getText().toString().trim().equals("") || _tlpn.getText().toString().trim().equals("") ||
                id_kampus.equals("-")){
                Toast.makeText(Register.this, "Data Masih Ada Yang Kosong", Toast.LENGTH_LONG).show();
        }else{
            if(id_kampus.equals("0")){
                _registerButton.setEnabled(false);

                ApiClient.register(_emailText.getText().toString().trim(), _passwordText.getText().toString().trim(),
                        _nama.getText().toString().trim(), "-",
                        _tlpn.getText().toString().trim(), id_kampus, this, _registerButton);
            }else{
                if(_nprm.getText().toString().trim().equals("")){
                    Toast.makeText(Register.this, "Data Masih Ada Yang Kosong", Toast.LENGTH_LONG).show();
                }else{
                    _registerButton.setEnabled(false);

                    ApiClient.register(_emailText.getText().toString().trim(), _passwordText.getText().toString().trim(),
                            _nama.getText().toString().trim(), _nprm.getText().toString().trim(),
                            _tlpn.getText().toString().trim(), id_kampus, this, _registerButton);
                }
            }
        }
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
}
