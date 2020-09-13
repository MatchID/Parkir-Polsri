package id.my.match.parkir.activity.pengguna;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import id.my.match.parkir.R;
import id.my.match.parkir.activity.Activity_Login;
import id.my.match.parkir.utility.restapi.ApiClient;
import id.my.match.parkir.utility.sqlite.DBHelper;

public class Activity_akun_pengguna extends AppCompatActivity {
    TextView welcome, email;
    EditText nama, nprm, tlp, pass;
    Button send;
    DBHelper dbh;
    ApiClient api = new ApiClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_akun_usr);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Akun");

        dbh = new DBHelper(Activity_akun_pengguna.this);

        email = findViewById(R.id.email);
        nama = findViewById(R.id.nama);
        nprm = findViewById(R.id.nprm);
        tlp = findViewById(R.id.tlp);
        send = findViewById(R.id.send);
        pass = findViewById(R.id.pass);

        nama.setText(dbh.getUserDetails().get("nama_lengkap"));
        email.setText(dbh.getUserDetails().get("email"));
        nprm.setText(dbh.getUserDetails().get("nprm"));
        tlp.setText(dbh.getUserDetails().get("tlp"));

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!nama.getText().toString().trim().equals("") && !tlp.getText().toString().trim().equals("")) {
                    api.proses_update_akun_usr(nama.getText().toString().trim(), nprm.getText().toString().trim(), tlp.getText().toString().trim(), pass.getText().toString().trim(),
                            Activity_akun_pengguna.this);
                }else{
                    Toast.makeText(Activity_akun_pengguna.this, "Data Nama dan Telepon tidak Boleh Kosong", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
