package id.my.match.parkir.activity.admin;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import id.my.match.parkir.activity.Activity_Login;
import id.my.match.parkir.R;
import id.my.match.parkir.activity.pengguna.MainActivityUsr;
import id.my.match.parkir.utility.listview.ListAdapter;
import id.my.match.parkir.utility.restapi.ApiClient;
import id.my.match.parkir.utility.sqlite.DBHelper;

public class MainActivity extends AppCompatActivity {
    TextView welcome, laporan_baru_txt;
    DBHelper dbh;
    RecyclerView mRecyclerView;
    ArrayList<HashMap<String, String>> tl_list = new ArrayList<HashMap<String, String>>();
    ListAdapter adapter_tl;
    LinearLayoutManager mLayoutManager;
    ApiClient ApiClient = new ApiClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dbh = new DBHelper(MainActivity.this);

        welcome = findViewById(R.id.welcome);

        laporan_baru_txt = findViewById(R.id.laporan_baru_txt);
        mRecyclerView = findViewById(R.id.recyclerViewBaru);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        welcome.setText(welcome.getText().toString() + dbh.getUserDetails().get("nama_lengkap"));

        laporan_baru_txt.setVisibility(View.GONE);

        list_baru();

    }

    private void list_baru(){
        tl_list.clear();
        adapter_tl=new ListAdapter(Activity_detail_laporan.class, tl_list);
        mRecyclerView.setAdapter(adapter_tl);

        ApiClient.laporan_list_adm(this, mRecyclerView, null, Activity_detail_laporan.class, "N", laporan_baru_txt);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            DBHelper dbh = new DBHelper(this);
            dbh.deleteUsers();

            Intent Login = new Intent(this, Activity_Login.class);
            startActivity(Login);
            finish();
        }else if(id == R.id.action_tl){
            Intent buka = new Intent(this, Activity_tindaklanjut.class);
            startActivity(buka);
        }else if(id == R.id.action_histori){
            Intent buka = new Intent(this, Activity_histori_tl.class);
            startActivity(buka);
        }else if(id == R.id.action_vr){
            Intent buka = new Intent(this, Activity_verifikasi.class);
            startActivity(buka);
        }else if(id == R.id.action_sl){
            Intent buka = new Intent(this, Activity_salah.class);
            startActivity(buka);
        }

        return super.onOptionsItemSelected(item);
    }
}
