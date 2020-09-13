package id.my.match.parkir.activity.admin;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import id.my.match.parkir.R;
import id.my.match.parkir.activity.MapsActivityBukanParkiran;
import id.my.match.parkir.utility.restapi.ApiClient;
import id.my.match.parkir.utility.sqlite.DBHelper;

public class Activity_detail_laporan_gambar extends AppCompatActivity {
    DBHelper dbh;
    ApiClient ApiClient = new ApiClient();

    ImageView gmbr;
    TextView laporan, parkir, nama, nama_kampus, parkir_koor, plat, jenis_kendaraan, ket_kendaraan;
    Button lokasi, send, send_benar, send_salah, bukan_parkir;

    String id, status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_laporan_gambar);

        Bundle cek_data = getIntent().getExtras();
        id = cek_data.getString("id");
        status = cek_data.getString("status");

        dbh = new DBHelper(this);

        gmbr = findViewById(R.id.gmbr);

        ApiClient.detail_laporan_gambar(this, id, gmbr);

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
