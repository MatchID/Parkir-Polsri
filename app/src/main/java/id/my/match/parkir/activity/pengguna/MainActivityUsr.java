package id.my.match.parkir.activity.pengguna;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import id.my.match.parkir.activity.Activity_Login;
import id.my.match.parkir.R;
import id.my.match.parkir.activity.Splash;
import id.my.match.parkir.activity.admin.MainActivity;
import id.my.match.parkir.utility.sqlite.DBHelper;

public class MainActivityUsr extends AppCompatActivity {
    TextView welcome;
    DBHelper dbh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_usr);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dbh = new DBHelper(MainActivityUsr.this);

        welcome = findViewById(R.id.welcome);

        welcome.setText(welcome.getText().toString() + dbh.getUserDetails().get("nama_lengkap"));

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Buat Laporan", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent buat  = new Intent(MainActivityUsr.this, Activity_buat_laporan.class);
                startActivity(buat);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_usr, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            DBHelper dbh = new DBHelper(this);
            dbh.deleteUsers();

            Intent Login = new Intent(this, Activity_Login.class);
            startActivity(Login);
            finish();
        }else if(id == R.id.action_histori){
            Intent buka = new Intent(this, Activity_histori_laporan.class);
            startActivity(buka);
        }else if(id == R.id.action_akun){
            Intent buka = new Intent(this, Activity_akun_pengguna.class);
            startActivity(buka);
        }

        return super.onOptionsItemSelected(item);
    }
}
