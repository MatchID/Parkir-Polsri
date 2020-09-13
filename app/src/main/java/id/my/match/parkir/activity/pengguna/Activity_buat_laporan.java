package id.my.match.parkir.activity.pengguna;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import id.my.match.parkir.R;
import id.my.match.parkir.activity.MapsActivityBukanParkiran;
import id.my.match.parkir.activity.Register;
import id.my.match.parkir.utility.gps.GpsService;
import id.my.match.parkir.utility.restapi.ApiClient;
import id.my.match.parkir.utility.sqlite.DBHelper;

public class Activity_buat_laporan extends AppCompatActivity {
    DBHelper dbh;
    private ImageView imageView;
    private int PICK_IMAGE_REQUEST = 1;
    private Bitmap bitmap;
    String koordinat, status_gambar = "-", latitude, longitude, txt_parkir="-", txt_parkir_nama="-",
            txt_status_laporan="A", txt_status_kendaraan="MB";
    GpsService gps;
    Spinner parkir, visibilitas, jenis_kendaraan;
    Button send;
    EditText laporan, ket_kendaraan, plat;
    ApiClient api;
    TextView lihat_lokasi, lihat_lokasi_koor, lihat_bukan_lokasi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buat_laporan);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Buat Laporan");

        api = new ApiClient();
//        api.bukan_parkir(this);

        dbh = new DBHelper(this);

        imageView = findViewById(R.id.gmbr);
        parkir = findViewById(R.id.parkir);
        visibilitas = findViewById(R.id.visibilitas);
        laporan = findViewById(R.id.laporan);
        plat = findViewById(R.id.plat);
        ket_kendaraan = findViewById(R.id.ket_kendaraan);
        jenis_kendaraan = findViewById(R.id.jenis_kendaraan);

        lihat_lokasi = findViewById(R.id.lihat_lokasi);
        lihat_lokasi_koor = findViewById(R.id.lihat_lokasi_koor);
        lihat_bukan_lokasi = findViewById(R.id.lihat_bukan_lokasi);

        send = findViewById(R.id.send);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(laporan.getText().toString().trim().equals("") || status_gambar.equals("-") || txt_status_laporan.equals("-") || plat.getText().toString().trim().equals("")){
                    Toast.makeText(Activity_buat_laporan.this, "Data Laporan Masih Belum Lengkap", Toast.LENGTH_LONG).show();
                }else{
                    uploadImage();
                }
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });

        gps = new GpsService(Activity_buat_laporan.this);
        if (gps.canGetLocation()) {

            latitude = String.valueOf(gps.getLatitude());
            longitude = String.valueOf(gps.getLongitude());
            koordinat = latitude + "," + longitude;

        } else {
            gps.showSettingAlert();
        }

        parkir.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final TextView ID = view.findViewById(R.id.kode);
                final TextView koor = view.findViewById(R.id.koor);
                final TextView lokasi = view.findViewById(R.id.lokasi);
                txt_parkir_nama = lokasi.getText().toString();
                txt_parkir = ID.getText().toString();
                lihat_lokasi_koor = koor;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        jenis_kendaraan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    txt_status_kendaraan = "MB";
                }else{
                    txt_status_kendaraan = "MT";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        visibilitas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    txt_status_laporan = "A";
                }else{
                    txt_status_laporan = "P";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        lihat_lokasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!txt_parkir_nama.equals("-")) {
                    String label = txt_parkir_nama;
                    String uriBegin = "geo:" + lihat_lokasi_koor.getText().toString();
                    String query = lihat_lokasi_koor.getText().toString() + "(" + label + ")";
                    String encodedQuery = Uri.encode(query);
                    String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
                    Uri uri = Uri.parse(uriString);
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
                    startActivity(intent);
//                lihat_lokasi_koor
                }
            }
        });

        lihat_bukan_lokasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent map = new Intent(Activity_buat_laporan.this, MapsActivityBukanParkiran.class);
                startActivity(map);
            }
        });

        api.parkir(parkir, this, Activity_buat_laporan.this);

    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
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
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //Setting the Bitmap to ImageView
                imageView.setImageBitmap(bitmap);
                status_gambar = "ok";

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void uploadImage(){
        String image = getStringImage(bitmap);
        api.proses_upload_laporan(image, txt_parkir, txt_status_laporan, laporan.getText().toString().trim(), koordinat, this, ket_kendaraan.getText().toString().trim(), txt_status_kendaraan, plat.getText().toString().trim());
    }
}
