package id.my.match.parkir.activity.admin;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import id.my.match.parkir.R;
import id.my.match.parkir.activity.MapsActivityBukanParkiran;
import id.my.match.parkir.activity.pengguna.Activity_buat_laporan;
import id.my.match.parkir.utility.restapi.ApiClient;
import id.my.match.parkir.utility.sqlite.DBHelper;

public class Activity_detail_laporan extends AppCompatActivity {
    DBHelper dbh;
    ApiClient ApiClient = new ApiClient();

    ImageView gmbr;
    TextView laporan, parkir, nama, nama_kampus, parkir_koor, plat, jenis_kendaraan, ket_kendaraan;
    Button lokasi, send, send_benar, send_salah, bukan_parkir;

    String id, status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_laporan);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Detail Laporan");

        Bundle cek_data = getIntent().getExtras();
        id = cek_data.getString("id");
        status = cek_data.getString("status");

        dbh = new DBHelper(this);

        gmbr = findViewById(R.id.gmbr);

        laporan = findViewById(R.id.laporan);
        parkir = findViewById(R.id.parkir);
        nama = findViewById(R.id.nama);
        nama_kampus = findViewById(R.id.nama_kampus);
        parkir_koor = findViewById(R.id.parkir_koor);
        plat = findViewById(R.id.plat);
        jenis_kendaraan = findViewById(R.id.jenis_kendaraan);
        ket_kendaraan = findViewById(R.id.ket_kendaraan);
        bukan_parkir = findViewById(R.id.bukan_parkir);

        lokasi = findViewById(R.id.lokasi);
        send = findViewById(R.id.send);
        send_benar = findViewById(R.id.send_benar);
        send_salah = findViewById(R.id.send_salah);

        send.setVisibility(View.GONE);
        send_benar.setVisibility(View.GONE);
        send_salah.setVisibility(View.GONE);

        ApiClient.detail_laporan(this, id, status, gmbr, laporan, parkir, nama, nama_kampus, parkir_koor, send, send_benar, send_salah, plat, jenis_kendaraan, ket_kendaraan);

        lokasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String label = parkir.getText().toString();
                String uriBegin = "geo:" + parkir_koor.getText().toString().trim();
                String query = parkir_koor.getText().toString().trim() + "(" + label + ")";
                String encodedQuery = Uri.encode(query);
                String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
                Uri uri = Uri.parse(uriString);
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(status.equals("N")){
                    ApiClient.proses_update_laporan(id, "TL1", Activity_detail_laporan.this);
                }else if(status.equals("TL2")){
                    ApiClient.proses_update_laporan(id, "TL4", Activity_detail_laporan.this);
                }
            }
        });

        send_benar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApiClient.proses_update_laporan(id, "TL2", Activity_detail_laporan.this);
            }
        });

        send_salah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApiClient.proses_update_laporan(id, "TL3", Activity_detail_laporan.this);
            }
        });

        bukan_parkir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent map = new Intent(Activity_detail_laporan.this, MapsActivityBukanParkiran.class);
                startActivity(map);
            }
        });

        gmbr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent map = new Intent(Activity_detail_laporan.this, Activity_detail_laporan_gambar.class);
                map.putExtra("id", id);
                startActivity(map);
            }
        });

//        ApiClient.bukan_parkir(this);
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
