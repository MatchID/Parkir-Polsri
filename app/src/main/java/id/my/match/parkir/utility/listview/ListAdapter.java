package id.my.match.parkir.utility.listview;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import id.my.match.parkir.R;
import id.my.match.parkir.utility.restapi.Alamat;
import id.my.match.parkir.utility.sqlite.DBHelper;

import static id.my.match.parkir.utility.datetime.DateTime_Format.DateTime_Format;

/**
 * Created by Indra Maulana on 23/04/2019.
 */

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.MyViewHolder>{

    ArrayList<HashMap<String, String>> mBerkasListAcc;
    Class activity;
    Alamat alamat = new Alamat();

    public ListAdapter(Class a, ArrayList<HashMap<String, String>> BerkasList) {
        mBerkasListAcc = BerkasList;
        activity = a;
    }


    @Override
    public ListAdapter.MyViewHolder onCreateViewHolder (ViewGroup parent, int viewType){
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_listusr, parent, false);
        ListAdapter.MyViewHolder mViewHolder = new ListAdapter.MyViewHolder(mView);

        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        HashMap<String, String> artikel = new HashMap<String, String>();
        artikel = mBerkasListAcc.get(position);
        final String id = artikel.get("kd_laporan");
        final String status = artikel.get("status_laporan");

        Alamat alamat = new Alamat();
        final String link_url = alamat.getAlamat();

        holder.mTextViewJudul.setText("" + artikel.get("nama_parkir"));
        holder.mTextViewTracking.setText("" + artikel.get("lokasi_parkir"));
        if(artikel.get("laporan").length() > 50){
            holder.mTextViewAlamat.setText("" + artikel.get("laporan").substring(0, 50).replace("\n", " ")+"...");
        }else{
            holder.mTextViewAlamat.setText("" + artikel.get("laporan").replace("\n", " "));
        }
        holder.mTextTanggal.setText("" + DateTime_Format(artikel.get("waktu_lapor")));

        if(status.equals("N")){
            holder.mTextViewStatus.setText("Laporan Baru");
        }else if(status.equals("TL1")){
            holder.mTextViewStatus.setText("Laporan Sedang Diverifikasi");
        }else if(status.equals("TL2")){
            holder.mTextViewStatus.setText("Laporan Sedang Ditindak Lanjuti");
        }else if(status.equals("TL3")){
            holder.mTextViewStatus.setText("Laporan Ditolak");
        }else if(status.equals("TL4")){
            holder.mTextViewStatus.setText("Laporan Selesai");
        }

        Picasso.with(holder.mTextViewGambar.getContext()).load(link_url + "../" + artikel.get("gambar")).into(holder.mTextViewGambar);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent = new Intent(view.getContext(), activity);
                mIntent.putExtra("id", id);
                mIntent.putExtra("status", status);

                ((Activity) view.getContext()).startActivityForResult(mIntent, 0);
                System.out.print(mIntent.getExtras());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mBerkasListAcc.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextViewID, mTextViewJudul, mTextViewTracking, mTextTanggal,
                mTextViewAlamat, mTextViewStatus;
        public ImageView mTextViewGambar;

        public MyViewHolder(View itemView) {
            super(itemView);
            mTextViewID = itemView.findViewById(R.id.kode);
            mTextViewJudul = itemView.findViewById(R.id.judul);
            mTextViewTracking = itemView.findViewById(R.id.tracking);
            mTextTanggal = itemView.findViewById(R.id.tgl);
            mTextViewAlamat = itemView.findViewById(R.id.nama_kategori);
            mTextViewGambar = itemView.findViewById(R.id.gambar);
            mTextViewStatus = itemView.findViewById(R.id.status);
        }
    }
}
