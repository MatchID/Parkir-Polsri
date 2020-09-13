package id.my.match.parkir.activity.admin;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;

import id.my.match.parkir.R;
import id.my.match.parkir.utility.listview.ListAdapter;
import id.my.match.parkir.utility.restapi.ApiClient;
import id.my.match.parkir.utility.sqlite.DBHelper;

public class Activity_histori_tl extends AppCompatActivity {
    TextView data_kosong;

    ApiClient ApiClient = new ApiClient();

    LinearLayoutManager mLayoutManager;
    SwipeRefreshLayout list_tl;
    RecyclerView mRecyclerView;
    ArrayList<HashMap<String, String>> tl_list = new ArrayList<HashMap<String, String>>();
    ListAdapter adapter_tl;
    DBHelper dbh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_laporan);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Histori Tindak Lanjut");

        dbh = new DBHelper(this);

        data_kosong = findViewById(R.id.data_kosong);
        mRecyclerView = findViewById(R.id.recyclerViewAcc);
        list_tl = findViewById(R.id.pullToRefreshAcc);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        list_tl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                list_tl();
            }
        });

        list_tl();

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK){
            if(data.getStringExtra("asal").equals("refresh")){
                list_tl();
            }
        }
    }

    private void list_tl(){
        tl_list.clear();
        adapter_tl=new ListAdapter(Activity_detail_laporan.class, tl_list);
        mRecyclerView.setAdapter(adapter_tl);
        list_tl.setRefreshing(false);

        ApiClient.laporan_list_adm(this, mRecyclerView, list_tl, Activity_detail_laporan.class, "TL4", data_kosong);
    }

    @Override
    public void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver((mMessageReceiver),
                new IntentFilter("Notif")
        );
    }

    @Override
    public void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String status_notif = intent.getStringExtra("status");
            if (status_notif.equals("refresh")) {
                list_tl();
            }
        }
    };
}
