package id.my.match.parkir.utility.restapi;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import id.my.match.parkir.activity.Activity_Login;
import id.my.match.parkir.activity.admin.MainActivity;
import id.my.match.parkir.utility.listview.ListAdapter;
import id.my.match.parkir.utility.spinner.SpinnerKampus;
import id.my.match.parkir.utility.spinner.SpinnerParkir;
import id.my.match.parkir.utility.sqlite.DBHelper;

public class ApiClient {
    DBHelper dbh;
    Alamat alamat = new Alamat();
    JSONArray server = null, server1 = null;
    Map<String, String> params = new Hashtable<String, String>();
    ArrayList<HashMap<String, String>> kampus_list = new ArrayList<HashMap<String, String>>();

    private String link_url;
    ProgressDialog loading;
    Context context;
    HashMap<String, String> user;
    JSONObject json;
    VolleyRequest mVolleyService;
    VolleyRequest.IResult mResultCallback = null;

    public void sendFIDToServer(Context context) {
        dbh = new DBHelper(context);
        user = dbh.getUserDetails();
        if (user.size() > 0) {
            link_url = alamat.getLogin();

            params.put("akun_fungsi", user.get("akun_fungsi"));
            params.put("id", user.get("username"));

            mVolleyService = new VolleyRequest(null,context);
            mVolleyService.RestFull(context, Request.Method.POST, link_url, params, null, null);
        }
    }

    public void lastseen(Context cnxt, String nip, String email, String vn, String vc, String vs, String hp, String awal) {
        context = cnxt;
//        link_url = alamat.getLastSeen();

        params.put("username", nip);
        params.put("email", email);
        params.put("versiapp", vn);
        params.put("kodeapp", vc);
        params.put("jenisos", "Android");
        params.put("versios", vs);
        params.put("tipehp", hp);
        params.put("koordinat", awal);

        mVolleyService = new VolleyRequest(null,context);
        mVolleyService.RestFull(context, Request.Method.PUT, link_url, params, null, null);
    }

    public void codeversion(String codeversion) {
        link_url = alamat.getLogin();

        params.put("codeversion", codeversion);

        mVolleyService = new VolleyRequest(null,context);
        mVolleyService.RestFull(context, Request.Method.PUT, link_url, params, null, null);
    }

    public void splash_lanjut(String versionName, final int versionCode, Context cnxt) {
        context = cnxt;
        dbh = new DBHelper(context);
        user = dbh.getUserDetails();

        if (user.size() > 0) {
//            idadmin = user.get("username");
        } else {
//            idadmin = "000";
        }

        link_url = alamat.getLogin();

//        params.put("username", idadmin);

        mResultCallback = new VolleyRequest.IResult() {
            @Override
            public void notifySuccess(String response) {
                try {
                    json = new JSONObject(response);
                    if (json.getString("Status").equals("true")) {
                        Intent b = new Intent(context, MainActivity.class);
                        context.startActivity(b);
                    } else {
                        Toast.makeText(context, "Server Dalam Perbaikan", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(context, "Terdapat Gangguan Server", Toast.LENGTH_SHORT).show();
                }
                ((Activity)(context)).finish();
            }

            @Override
            public void notifyError(VolleyError error) {
                if (error instanceof TimeoutError) {
                    Toast.makeText(context, "Request Time Out", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Terdapat Gangguan Server", Toast.LENGTH_SHORT).show();
                }
                ((Activity)(context)).finish();
            }
        };

        VolleyRequest mVolleyService = new VolleyRequest(mResultCallback,context);
        mVolleyService.RestFull(context, Request.Method.GET, link_url, params, null, null);
    }

    public void login(final String kode_id, final String kode_pass, final Context cnxt,
                      final Button _loginButton) {

        final Activity_Login activity_login = new Activity_Login();
        context = cnxt;
        dbh = new DBHelper(context);

        params.put("data", "login");
        params.put("uname", kode_id);
        params.put("upass", kode_pass);

        link_url = alamat.getLogin();

        mResultCallback = new VolleyRequest.IResult() {
            @Override
            public void notifySuccess(String response) {
                try {
                    json = new JSONObject(response);
                    if(json.getString("status_data").equals("true")){
                        JSONObject ar1 = json.getJSONObject("data");
                        if (ar1.getString("status_akun").equals("true")){
                            JSONObject ar = ar1.getJSONObject("data_akun");

                            dbh = new DBHelper(context);

                            if(ar.getString("status").equals("adm")){
                                dbh.setUserDetails(ar.getString("id_user"), ar.getString("username"),
                                        ar.getString("username"), ar.getString("nama_user"),
                                        "-", ar.getString("status"), ar.getString("kd_kampus"), "-");
                            }else{
                                dbh.setUserDetails(ar.getString("id_user"), ar.getString("email_pengguna"),
                                        ar.getString("email_pengguna"), ar.getString("nama_pengguna"),
                                        ar.getString("tlpn_pengguna"), ar.getString("status"), ar.getString("kd_kampus"), ar.getString("nprm"));
                            }

                            _loginButton.setEnabled(true);
                            activity_login.onLoginValidasiBenar(context, ar.getString("status"));
                            ((Activity)(context)).finish();
                        }else if (json.getString("Login").equals("Pass Error")){
                            _loginButton.setEnabled(true);
                            Toast.makeText(context, "Akun tidak aktif", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        _loginButton.setEnabled(true);
                        Toast.makeText(context, "Data Login Salah", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    _loginButton.setEnabled(true);
                    Toast.makeText(context, "Terdapat Gangguan Server", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void notifyError(VolleyError error) {
                if (error instanceof TimeoutError) {
                    Toast.makeText(context, "Request Time Out", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Terdapat Gangguan Server", Toast.LENGTH_SHORT).show();
                }
                ((Activity)(context)).finish();
            }
        };

        loading = ProgressDialog.show(context,null,"Loading",false,false);
        VolleyRequest mVolleyService = new VolleyRequest(mResultCallback,context);
        mVolleyService.RestFull(context, Request.Method.GET, link_url, params, loading, null);
    }

    public void register(final String email, final String kode_pass, final String nama, final String nprm,
                         final String tlpn, final String id_kampus, final Context cnxt, final Button _registerButton) {

        context = cnxt;
        dbh = new DBHelper(context);

        params.put("data", "register");
        params.put("email", email);
        params.put("pass", kode_pass);
        params.put("nama", nama);
        params.put("nprm", nprm);
        params.put("tlp", tlpn);
        params.put("kampus", id_kampus);

        link_url = alamat.getLogin();

        mResultCallback = new VolleyRequest.IResult() {
            @Override
            public void notifySuccess(String response) {
                try {
                    json = new JSONObject(response);
                    if(json.getString("status_data").equals("true")){
                        Toast.makeText(context, "Akun Berhasil Didaftarkan", Toast.LENGTH_SHORT).show();
                        ((Activity)(context)).finish();
                    }else if(json.getString("status_data").equals("0")){
                        _registerButton.setEnabled(true);
                        Toast.makeText(context, "Registrasi Gagal, Silahkan Coba Kembali", Toast.LENGTH_SHORT).show();
                    }else{
                        _registerButton.setEnabled(true);
                        Toast.makeText(context, "Akun Telah Terdaftar", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    _registerButton.setEnabled(true);
                    Toast.makeText(context, "Terdapat Gangguan Server", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void notifyError(VolleyError error) {
                if (error instanceof TimeoutError) {
                    Toast.makeText(context, "Request Time Out", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Terdapat Gangguan Server", Toast.LENGTH_SHORT).show();
                }
                ((Activity)(context)).finish();
            }
        };

        loading = ProgressDialog.show(context,null,"Loading",false,false);
        VolleyRequest mVolleyService = new VolleyRequest(mResultCallback,context);
        mVolleyService.RestFull(context, Request.Method.POST, link_url, params, loading, null);
    }

    public void bukan_parkir(final GoogleMap googleMap, final Context cnxt) {

        context = cnxt;
        dbh = new DBHelper(context);
        dbh.deleteBukan();

        params.put("data", "list_bukan_parkir");

        link_url = alamat.getKampus();

        mResultCallback = new VolleyRequest.IResult() {
            @Override
            public void notifySuccess(String response) {
                try {
                    json = new JSONObject(response);
                    if(json.getString("status_data").equals("true")){
                        server = json.getJSONArray("data");

                        for(int i = 0; i < server.length(); i++) {
                            JSONObject ar = server.getJSONObject(i);

//                                createMarker(googleMap, markersArray.get(i).getLatitude(), markersArray.get(i).getLongitude(), markersArray.get(i).getTitle(), markersArray.get(i).getSnippet(), markersArray.get(i).getIconResID());
                            String[] a = ar.getString("koordinat").split(",");

                            googleMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(Double.parseDouble(a[0]), Double.parseDouble(a[1])))
                                    .anchor(0.5f, 0.5f)
                                    .title(ar.getString("nama_lokasi"))
                                    .snippet(ar.getString("lokasi")));

                            dbh.setBukan(ar.getString("kd_bukan_parkir"),ar.getString("nama_lokasi"),
                                    ar.getString("lokasi"),ar.getString("koordinat"));
                        }
                    }else{
                        Toast.makeText(context, "Data Lokasi Bukan Parkiran Tidak Ada", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(context, "Data Lokasi Bukan Parkiran Gagal Dimuat", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void notifyError(VolleyError error) {
                if (error instanceof TimeoutError) {
                    Toast.makeText(context, "Request Time Out Load Data Lokasi Bukan Parkiran", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Data Lokasi Bukan Parkiran Gagal Dimuat", Toast.LENGTH_SHORT).show();
                }
            }
        };

        VolleyRequest mVolleyService = new VolleyRequest(mResultCallback,context);
        mVolleyService.RestFull(context, Request.Method.GET, link_url, params, null, null);
    }

    public void kampus(final Spinner kampus, final Context cnxt, final Activity act) {

        context = cnxt;
        dbh = new DBHelper(context);

        params.put("data", "kampus");

        link_url = alamat.getKampus();

        mResultCallback = new VolleyRequest.IResult() {
            @Override
            public void notifySuccess(String response) {
                try {
                    json = new JSONObject(response);
                    if(json.getString("status_data").equals("true")){
                        server = json.getJSONArray("data");

                        HashMap<String, String> map_start = new HashMap<String, String>();

                        map_start.put("JUDUL", "Pilih Lokasi Kampus");
                        map_start.put("ID", "-");

                        kampus_list.add(map_start);

                        for(int i = 0; i < server.length(); i++) {
                            JSONObject ar = server.getJSONObject(i);
                            HashMap<String, String> map = new HashMap<String, String>();

                            map.put("JUDUL", ar.getString("nama_kampus"));
                            map.put("ID", ar.getString("id_kampus"));

                            kampus_list.add(map);
                        }

                        HashMap<String, String> map_end = new HashMap<String, String>();

                        map_end.put("JUDUL", "Umum");
                        map_end.put("ID", "0");

                        kampus_list.add(map_end);

                        SpinnerKampus kampus_spinner = new SpinnerKampus(kampus_list, act);
                        kampus.setAdapter(kampus_spinner);
                        kampus.setDropDownVerticalOffset(100);
                    }else{
                        Toast.makeText(context, "Data Kampus Tidak Ada", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(context, "Data Kampus Gagal Dimuat", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void notifyError(VolleyError error) {
                if (error instanceof TimeoutError) {
                    Toast.makeText(context, "Request Time Out Load Data Kampus", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Data Kampus Gagal Dimuat", Toast.LENGTH_SHORT).show();
                }
            }
        };

        VolleyRequest mVolleyService = new VolleyRequest(mResultCallback,context);
        mVolleyService.RestFull(context, Request.Method.GET, link_url, params, null, null);
    }

    public void parkir(final Spinner parkir, final Context cnxt, final Activity act) {

        context = cnxt;
        dbh = new DBHelper(context);

        params.put("data", "parkir");

        link_url = alamat.getKampus();

        mResultCallback = new VolleyRequest.IResult() {
            @Override
            public void notifySuccess(String response) {
                try {
                    json = new JSONObject(response);
                    if(json.getString("status_data").equals("true")){
                        server = json.getJSONArray("data");

                        HashMap<String, String> map_start = new HashMap<String, String>();

                        map_start.put("JUDUL", "Pilih Lokasi Parkir");
                        map_start.put("LOKASI", "-");
                        map_start.put("ID", "-");

                        kampus_list.add(map_start);

                        for(int i = 0; i < server.length(); i++) {
                            JSONObject ar = server.getJSONObject(i);
                            HashMap<String, String> map = new HashMap<String, String>();

                            map.put("JUDUL", ar.getString("nama_parkir") + " (" + ar.getString("nama_kampus") + ")");

                            if(ar.getString("jenis_parkir").equals("MTD")){
                                map.put("LOKASI",  "(Motor Dosen) " + ar.getString("lokasi_parkir"));
                            }else if(ar.getString("jenis_parkir").equals("MBD")){
                                map.put("LOKASI",  "(Mobil Dosen) " + ar.getString("lokasi_parkir"));
                            }else if(ar.getString("jenis_parkir").equals("MTM")){
                                map.put("LOKASI",  "(Motor Mahasiswa/Umum) " + ar.getString("lokasi_parkir"));
                            }else if(ar.getString("jenis_parkir").equals("MBM")){
                                map.put("LOKASI",  "(Mobil Mahasiswa/Umum) " + ar.getString("lokasi_parkir"));
                            }

                            map.put("ID", ar.getString("id_parkir"));
                            map.put("KOOR", ar.getString("koordinat_parkir"));

                            kampus_list.add(map);
                        }

                        SpinnerParkir kampus_spinner = new SpinnerParkir(kampus_list, act);
                        parkir.setAdapter(kampus_spinner);
                        parkir.setDropDownVerticalOffset(100);
                    }else{
                        Toast.makeText(context, "Data Lokasi Tidak Ada", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(context, "Data Lokasi Gagal Dimuat", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void notifyError(VolleyError error) {
                if (error instanceof TimeoutError) {
                    Toast.makeText(context, "Request Time Out Load Data Lokasi", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Data Lokasi Gagal Dimuat", Toast.LENGTH_SHORT).show();
                }
            }
        };

        VolleyRequest mVolleyService = new VolleyRequest(mResultCallback,context);
        mVolleyService.RestFull(context, Request.Method.GET, link_url, params, null, null);
    }

    public void laporan_list_adm(Context ctx, final RecyclerView mRecyclerView, final SwipeRefreshLayout list_tl,
                                 final Class clas, final String status, final TextView data_kosong){
        context = ctx;

        link_url = alamat.getLaporanAdm_list();

        params.put("data", "list");
        params.put("status_laporan", status);

        mResultCallback = new VolleyRequest.IResult() {
            @Override
            public void notifySuccess(String response) {
                try {
                    JSONObject json = new JSONObject(response);
                        if (json.getString("status_data").equals("true")) {
                            server = json.getJSONArray("data");
                            ArrayList<HashMap<String, String>> tl_list =
                                    new ArrayList<HashMap<String, String>>();
                            for(int i = 0; i < server.length(); i++){
                                JSONObject ar = server.getJSONObject(i);

                                HashMap<String, String> map = new HashMap<String, String>();
                                map.put("kd_laporan", ar.getString("kd_laporan"));
                                map.put("visibilitas", ar.getString("visibilitas"));
                                map.put("laporan", ar.getString("laporan"));
                                map.put("gambar", ar.getString("gambar"));
                                map.put("koordinat", ar.getString("koordinat"));
                                map.put("status_laporan", ar.getString("status_laporan"));
                                map.put("waktu_lapor", ar.getString("waktu_lapor"));
                                map.put("nama_parkir", ar.getString("nama_parkir"));
                                map.put("lokasi_parkir", ar.getString("lokasi_parkir"));
                                map.put("koordinat_parkir", ar.getString("koordinat_parkir"));
                                map.put("jenis_parkir", ar.getString("jenis_parkir"));
                                map.put("nama_kampus", ar.getString("nama_kampus"));
                                map.put("nama_pengguna", ar.getString("nama_pengguna"));
                                map.put("kampus_pengguna", ar.getString("kampus_pengguna"));

                                tl_list.add(map);
                            }

                            ListAdapter adapter_tl = new ListAdapter(clas, tl_list);
                            mRecyclerView.setAdapter(adapter_tl);
                            if(list_tl != null){
                                list_tl.setRefreshing(false);
                                data_kosong.setVisibility(View.GONE);
                            }else{
                                data_kosong.setVisibility(View.VISIBLE);
                            }
                        }else{
                            Toast.makeText(context, "Data Kosong", Toast.LENGTH_SHORT).show();
                        }
                } catch (JSONException e) {
                    Toast.makeText(context, "Terdapat Gangguan Server", Toast.LENGTH_SHORT).show();
                    ((Activity)(context)).finish();
                }
            }
            @Override
            public void notifyError(VolleyError error) {
                if (error instanceof TimeoutError) {
                    Toast.makeText(context, "Request Time Out", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Terdapat Gangguan Server", Toast.LENGTH_SHORT).show();
                }
                ((Activity)(context)).finish();
            }
        };

        loading = ProgressDialog.show(context, null, "Loading", false, false);
        VolleyRequest mVolleyService = new VolleyRequest(mResultCallback,context);
        mVolleyService.RestFull(context, Request.Method.GET, link_url, params, loading, null);
    }

    public void laporan_list_usr(Context ctx, final RecyclerView mRecyclerView, final SwipeRefreshLayout list_tl,
                                 final Class clas, final TextView data_kosong){
        context = ctx;
        dbh = new DBHelper(context);
        user = dbh.getUserDetails();

        link_url = alamat.getLaporanUsr_list();

        params.put("email", user.get("email"));
        params.put("data", "list");

        mResultCallback = new VolleyRequest.IResult() {
            @Override
            public void notifySuccess(String response) {
                try {
                    JSONObject json = new JSONObject(response);
                    if (json.getString("status_data").equals("true")) {
                        server = json.getJSONArray("data");
                        ArrayList<HashMap<String, String>> tl_list =
                                new ArrayList<HashMap<String, String>>();
                        for(int i = 0; i < server.length(); i++){
                            JSONObject ar = server.getJSONObject(i);

                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("kd_laporan", ar.getString("kd_laporan"));
                            map.put("visibilitas", ar.getString("visibilitas"));
                            map.put("laporan", ar.getString("laporan"));
                            map.put("gambar", ar.getString("gambar"));
                            map.put("koordinat", ar.getString("koordinat"));
                            map.put("status_laporan", ar.getString("status_laporan"));
                            map.put("waktu_lapor", ar.getString("waktu_lapor"));
                            map.put("nama_parkir", ar.getString("nama_parkir"));
                            map.put("lokasi_parkir", ar.getString("lokasi_parkir"));
                            map.put("koordinat_parkir", ar.getString("koordinat_parkir"));
                            map.put("jenis_parkir", ar.getString("jenis_parkir"));
                            map.put("nama_kampus", ar.getString("nama_kampus"));
                            map.put("nama_pengguna", ar.getString("nama_pengguna"));
                            map.put("kampus_pengguna", ar.getString("kampus_pengguna"));

                            tl_list.add(map);
                        }

                        ListAdapter adapter_tl = new ListAdapter(clas, tl_list);
                        mRecyclerView.setAdapter(adapter_tl);
                        list_tl.setRefreshing(false);
                        data_kosong.setVisibility(View.GONE);
                    }else{
                        Toast.makeText(context, "Data Kosong", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(context, "Terdapat Gangguan Server", Toast.LENGTH_SHORT).show();
                    ((Activity)(context)).finish();
                }
            }
            @Override
            public void notifyError(VolleyError error) {
                if (error instanceof TimeoutError) {
                    Toast.makeText(context, "Request Time Out", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Terdapat Gangguan Server", Toast.LENGTH_SHORT).show();
                }
                ((Activity)(context)).finish();
            }
        };

        loading = ProgressDialog.show(context, null, "Loading", false, false);
        VolleyRequest mVolleyService = new VolleyRequest(mResultCallback,context);
        mVolleyService.RestFull(context, Request.Method.GET, link_url, params, loading, null);
    }

    public void proses_upload_laporan(final String gambar, final String parkir, final String status,
                                     final String isi, final String koordinat, final Context context,
                                      final String ket_kendaraan, final String status_kendaraan, final String plat) {
        link_url = alamat.sendLaporan();

        dbh = new DBHelper(context);
        user = dbh.getUserDetails();

        params.put("kd_pengguna", user.get("id_user"));
        params.put("data", "upload");
        params.put("kd_parkir", parkir);
        params.put("visibilitas", status);
        params.put("laporan", isi);
        params.put("koordinat", koordinat);
        params.put("gambar", gambar);
        params.put("plat", plat);
        params.put("jenis_kendaraan", status_kendaraan);
        params.put("ket_kendaraan", ket_kendaraan);

        mResultCallback = new VolleyRequest.IResult() {
            @Override
            public void notifySuccess(String response) {
                Log.d("CEKDATA", response);
                try {
                    json = new JSONObject(response);
                    if (json.getString("status_data").equals("true")) {
                        Toast.makeText(context, "Pengiriman Laporan Berhasil", Toast.LENGTH_SHORT).show();
                        ((Activity)(context)).finish();
                    }else{
                        Toast.makeText(context, "Pengiriman Laporan Gagal", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(context, "Terdapat Gangguan Server", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void notifyError(VolleyError error) {
                Log.d("CEKDATAERROR", error.toString());
                if (error instanceof TimeoutError) {
                    Toast.makeText(context, "Request Time Out", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Terdapat Gangguan Server", Toast.LENGTH_SHORT).show();
                }
            }
        };

        loading = ProgressDialog.show(context,null,"Loading",false,false);
        VolleyRequest mVolleyService = new VolleyRequest(mResultCallback,context);
        mVolleyService.RestFull(context, Request.Method.POST, link_url, params, loading, null);
    }

    public void detail_laporan(Context ctx, String id, final String status, final ImageView gmbr, final TextView laporan,
                               final TextView parkir, final TextView nama, final TextView nama_kampus,
                               final TextView parkir_koor, final TextView send, final TextView send_benar, final TextView send_salah,
                               final TextView plat, final TextView jenis_kendaraan, final TextView ket_kendaraan){
        context = ctx;

        link_url = alamat.getLaporanAdm_list();

        params.put("data", "detail");
        params.put("kd_laporan", id);

        mResultCallback = new VolleyRequest.IResult() {
            @Override
            public void notifySuccess(String response) {
                try {
                    JSONObject json = new JSONObject(response);
                    if (json.getString("status_data").equals("true")) {
                        JSONObject ar = json.getJSONObject("data");

                        Picasso.with(context).load(alamat.getAlamat() + "../" + ar.getString("gambar")).into(gmbr);
                        laporan.setText(ar.getString("laporan"));
                        if(ar.getString("jenis_parkir").equals("MTD")){
                            parkir.setText(ar.getString("nama_parkir") + " (" + ar.getString("lokasi_parkir") + ") " + "Motor Dosen");
                        }else if(ar.getString("jenis_parkir").equals("MBD")){
                            parkir.setText(ar.getString("nama_parkir") + " (" + ar.getString("lokasi_parkir") + ") " + "Mobil Dosen");
                        }else if(ar.getString("jenis_parkir").equals("MTM")){
                            parkir.setText(ar.getString("nama_parkir") + " (" + ar.getString("lokasi_parkir") + ") " + "Motor Mahasiswa/Umum");
                        }else if(ar.getString("jenis_parkir").equals("MBM")){
                            parkir.setText(ar.getString("nama_parkir") + " (" + ar.getString("lokasi_parkir") + ") " + "Mobil Mahasiswa/Umum");
                        }
                        nama.setText(ar.getString("nama_pengguna"));
                        if(!ar.getString("kampus_pengguna").equals("-")){
                            nama_kampus.setText(ar.getString("kampus_pengguna"));
                        }else{
                            nama_kampus.setText("-");
                        }
                        parkir_koor.setText(ar.getString("koordinat_parkir"));

                        if(status.equals("N")){
                            send.setVisibility(View.VISIBLE);
                        }else if(status.equals("TL1")){
                            send_benar.setVisibility(View.VISIBLE);
                            send_salah.setVisibility(View.VISIBLE);
                        }else if(status.equals("TL2")){
                            send.setVisibility(View.VISIBLE);
                        }

                        if(ar.getString("jenis_kendaraan").equals("MB")){
                            jenis_kendaraan.setText("Mobil");
                        }else{
                            jenis_kendaraan.setText("Motor");
                        }

                        ket_kendaraan.setText(ar.getString("ket_kendaraan"));
                        plat.setText(ar.getString("plat"));

                    }else{
                        Toast.makeText(context, "Data Kosong", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(context, "Terdapat Gangguan Server", Toast.LENGTH_SHORT).show();
                    ((Activity)(context)).finish();
                }
            }
            @Override
            public void notifyError(VolleyError error) {
                if (error instanceof TimeoutError) {
                    Toast.makeText(context, "Request Time Out", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Terdapat Gangguan Server", Toast.LENGTH_SHORT).show();
                }
                ((Activity)(context)).finish();
            }
        };

        loading = ProgressDialog.show(context, null, "Loading", false, false);
        VolleyRequest mVolleyService = new VolleyRequest(mResultCallback,context);
        mVolleyService.RestFull(context, Request.Method.GET, link_url, params, loading, null);
    }

    public void detail_laporan_gambar(Context ctx, String id, final ImageView gmbr){
        context = ctx;

        link_url = alamat.getLaporanAdm_list();

        params.put("data", "detail");
        params.put("kd_laporan", id);

        mResultCallback = new VolleyRequest.IResult() {
            @Override
            public void notifySuccess(String response) {
                try {
                    JSONObject json = new JSONObject(response);
                    if (json.getString("status_data").equals("true")) {
                        JSONObject ar = json.getJSONObject("data");

                        Picasso.with(context).load(alamat.getAlamat() + "../" + ar.getString("gambar")).into(gmbr);

                    }else{
                        Toast.makeText(context, "Data Kosong", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(context, "Terdapat Gangguan Server", Toast.LENGTH_SHORT).show();
                    ((Activity)(context)).finish();
                }
            }
            @Override
            public void notifyError(VolleyError error) {
                if (error instanceof TimeoutError) {
                    Toast.makeText(context, "Request Time Out", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Terdapat Gangguan Server", Toast.LENGTH_SHORT).show();
                }
                ((Activity)(context)).finish();
            }
        };

        loading = ProgressDialog.show(context, null, "Loading", false, false);
        VolleyRequest mVolleyService = new VolleyRequest(mResultCallback,context);
        mVolleyService.RestFull(context, Request.Method.GET, link_url, params, loading, null);
    }

    public void detail_laporanUsr(Context ctx, String id, final ImageView gmbr, final TextView laporan,
                               final TextView parkir, final TextView nama, final TextView nama_kampus,
                               final TextView parkir_koor, final TextView plat, final TextView jenis_kendaraan,
                                  final TextView ket_kendaraan){
        context = ctx;

        dbh = new DBHelper(context);
        user = dbh.getUserDetails();

        link_url = alamat.getLaporanUsr_list();

        params.put("data", "detail");
        params.put("kd_laporan", id);
        params.put("email", user.get("email"));

        mResultCallback = new VolleyRequest.IResult() {
            @Override
            public void notifySuccess(String response) {
                try {
                    JSONObject json = new JSONObject(response);
                    if (json.getString("status_data").equals("true")) {
                        JSONObject ar = json.getJSONObject("data");

                        Picasso.with(context).load(alamat.getAlamat() + "../" + ar.getString("gambar")).into(gmbr);
                        laporan.setText(ar.getString("laporan"));
                        if(ar.getString("jenis_parkir").equals("MTD")){
                            parkir.setText(ar.getString("nama_parkir") + " (" + ar.getString("lokasi_parkir") + ") " + "Motor Dosen");
                        }else if(ar.getString("jenis_parkir").equals("MBD")){
                            parkir.setText(ar.getString("nama_parkir") + " (" + ar.getString("lokasi_parkir") + ") " + "Mobil Dosen");
                        }else if(ar.getString("jenis_parkir").equals("MTM")){
                            parkir.setText(ar.getString("nama_parkir") + " (" + ar.getString("lokasi_parkir") + ") " + "Motor Mahasiswa/Umum");
                        }else if(ar.getString("jenis_parkir").equals("MBM")){
                            parkir.setText(ar.getString("nama_parkir") + " (" + ar.getString("lokasi_parkir") + ") " + "Mobil Mahasiswa/Umum");
                        }
                        nama.setText(ar.getString("nama_pengguna"));
                        if(!ar.getString("kampus_pengguna").equals("-")){
                            nama_kampus.setText(ar.getString("kampus_pengguna"));
                        }else{
                            nama_kampus.setText("-");
                        }
                        parkir_koor.setText(ar.getString("koordinat_parkir"));

                        if(ar.getString("jenis_kendaraan").equals("MB")){
                            jenis_kendaraan.setText("Mobil");
                        }else{
                            jenis_kendaraan.setText("Motor");
                        }

                        ket_kendaraan.setText(ar.getString("plat"));
                        plat.setText(ar.getString("jenis_kendaraan"));

                    }else{
                        Toast.makeText(context, "Data Kosong", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(context, "Terdapat Gangguan Server", Toast.LENGTH_SHORT).show();
                    ((Activity)(context)).finish();
                }
            }
            @Override
            public void notifyError(VolleyError error) {
                if (error instanceof TimeoutError) {
                    Toast.makeText(context, "Request Time Out", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Terdapat Gangguan Server", Toast.LENGTH_SHORT).show();
                }
                ((Activity)(context)).finish();
            }
        };

        loading = ProgressDialog.show(context, null, "Loading", false, false);
        VolleyRequest mVolleyService = new VolleyRequest(mResultCallback,context);
        mVolleyService.RestFull(context, Request.Method.GET, link_url, params, loading, null);
    }

    public void proses_update_laporan(final String id, final String status, final Context context) {
        link_url = alamat.getLaporanAdm_proses();

        dbh = new DBHelper(context);
        user = dbh.getUserDetails();

        params.put("kd_laporan", id);
        params.put("data", "update");
        params.put("status_laporan", status);

        mResultCallback = new VolleyRequest.IResult() {
            @Override
            public void notifySuccess(String response) {
                try {
                    json = new JSONObject(response);
                    if (json.getString("status_data").equals("true")) {
                        Toast.makeText(context, "Laporan Berhasil Diproses", Toast.LENGTH_SHORT).show();
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("status", "refresh");
                        ((Activity)(context)).setResult(Activity.RESULT_OK, returnIntent);
                        ((Activity)(context)).finish();
                    }else{
                        Toast.makeText(context, "Laporan Gagal Diproses", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(context, "Terdapat Gangguan Server", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void notifyError(VolleyError error) {
                if (error instanceof TimeoutError) {
                    Toast.makeText(context, "Request Time Out", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Terdapat Gangguan Server", Toast.LENGTH_SHORT).show();
                }
            }
        };

        loading = ProgressDialog.show(context,null,"Loading",false,false);
        VolleyRequest mVolleyService = new VolleyRequest(mResultCallback,context);
        mVolleyService.RestFull(context, Request.Method.GET, link_url, params, loading, null);
    }

    public void proses_update_akun_usr(final String nama, final String nprm, final String tlp, final String Pass, final Context context) {
        link_url = alamat.getLogin();

        dbh = new DBHelper(context);
        user = dbh.getUserDetails();

        params.put("uname", user.get("email"));
        params.put("data", "update");
        params.put("nprm", nprm);
        params.put("upass", Pass);
        params.put("nama_user", nama);
        params.put("tlp", tlp);

        mResultCallback = new VolleyRequest.IResult() {
            @Override
            public void notifySuccess(String response) {
                Log.d("CEKDATA", response);
                try {
                    json = new JSONObject(response);
                    if (json.getString("status_data").equals("true")) {
                        Toast.makeText(context, "Update Data Akun Berhasil", Toast.LENGTH_SHORT).show();
                        dbh.deleteUsers();

                        Intent Login = new Intent(context, Activity_Login.class);
                        ((Activity)(context)).startActivity(Login);
                        ((Activity)(context)).finish();
                    }else{
                        Toast.makeText(context, "Update Data Gagal", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(context, "Terdapat Gangguan Server", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void notifyError(VolleyError error) {
                if (error instanceof TimeoutError) {
                    Toast.makeText(context, "Request Time Out", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Terdapat Gangguan Server", Toast.LENGTH_SHORT).show();
                }
            }
        };

        loading = ProgressDialog.show(context,null,"Loading",false,false);
        VolleyRequest mVolleyService = new VolleyRequest(mResultCallback,context);
        mVolleyService.RestFull(context, Request.Method.GET, link_url, params, loading, null);
    }
//
//    public void cari_ktp(String nik, Context cnxt, final ScrollView data_pencarian, final TextView nik_s,
//                         final TextView jk, final TextView tgl, final TextView tgl2, final TextView prov, final TextView kab, final TextView kec,
//                         final TextView pos, final TextView kel, final TextView usia, final TextView zodiak, final TextView generasi,
//                         final ShimmerFrameLayout mShimmerViewContainer, final ImageView img_ktp) {
//        context = cnxt;
//        dbh = new DBHelper(context);
//
//        link_url = alamat.getData_cekktp();
//
//        params.put("nik_data", nik);
//
//        mResultCallback = new VolleyRequest.IResult() {
//            @Override
//            public void notifySuccess(String response) {
//                try {
//                    json = new JSONObject(response);
//                    if (json.getString("Status").equals("true")) {
//                        JSONObject Data = json.getJSONObject("Data");
//
//                        if(Data.getString("status").equals("200")){
//                            DateTime_Format date = new DateTime_Format();
//                            Generasi_Format generasi_set = new Generasi_Format();
//
//                            JSONObject message = Data.getJSONObject("message");
//                            JSONObject data_nik = message.getJSONObject("data");
//                            nik_s.setText(data_nik.getString("nik"));
//                            jk.setText(data_nik.getString("jk"));
//                            tgl.setText(date.DateTimeDay_Format(data_nik.getString("tgl")));
//                            tgl2.setText(data_nik.getString("tgl"));
//                            prov.setText(data_nik.getString("prov"));
//                            kab.setText(data_nik.getString("kab"));
//                            kec.setText(data_nik.getString("kec"));
//                            if(data_nik.getString("pos").equals("null")){
//                                pos.setText("-");
//                            }else{
//                                pos.setText(data_nik.getString("pos"));
//                            }
//                            kel.setText(data_nik.getString("kel"));
//                            usia.setText(date.Age_Format(data_nik.getString("tgl")) + " Tahun");
//                            zodiak.setText(generasi_set.Horoscop(data_nik.getString("tgl")));
//                            generasi.setText(generasi_set.Generasi(data_nik.getString("tgl")));
//
//                            data_pencarian.setVisibility(View.VISIBLE);
//                        }else{
//                            img_ktp.setVisibility(View.VISIBLE);
//                            FancyToast.makeText(context, Data.getString("message").toString(), FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
//                        }
//                    } else {
//                        img_ktp.setVisibility(View.VISIBLE);
//                        FancyToast.makeText(context, context.getString(R.string.server_maintenance), FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show();
//                    }
//                } catch (JSONException e) {
//                    img_ktp.setVisibility(View.VISIBLE);
//                    FancyToast.makeText(context, context.getString(R.string.server_error), FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
//                }
//                mShimmerViewContainer.setVisibility(View.GONE);
//                mShimmerViewContainer.stopShimmer();
//            }
//
//            @Override
//            public void notifyError(VolleyError error) {
//                if (error instanceof TimeoutError) {
//                    FancyToast.makeText(context, context.getString(R.string.server_rto), FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
//                } else {
//                    FancyToast.makeText(context, context.getString(R.string.server_error), FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
//                }
//            }
//        };
//
//        VolleyRequest mVolleyService = new VolleyRequest(mResultCallback,context);
//        mVolleyService.RestFull(context, Request.Method.GET, link_url, params, null, null);
//    }
//
//    public void ktp_histori(Context cnxt, final RecyclerView recyclerView, TextView datanull) {
//        context = cnxt;
//        dbh = new DBHelper(context);
//
//        HashMap<String, String> HistoriToken = dbh.getHistoriKTP(context, recyclerView);
//
//        if(HistoriToken.size()>0){
//            datanull.setVisibility(View.INVISIBLE);
//        }else{
//            datanull.setVisibility(View.VISIBLE);
//        }
//    }
}