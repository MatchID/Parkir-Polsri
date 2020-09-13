package id.my.match.parkir.utility.spinner;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import id.my.match.parkir.R;

/**
 * Created by admin on 18/03/2018.
 */

public class SpinnerKampus extends BaseAdapter {
    private ArrayList<HashMap<String, String>> lstData;
    private Activity activity;
    private LayoutInflater inflater;

    public SpinnerKampus(ArrayList<HashMap<String, String>> lstData, Activity activity){
        this.lstData = lstData;
        this.activity = activity;
        this.inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return lstData.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(convertView == null)
            view = inflater.inflate(R.layout.spinner_items,null);

        final TextView JUDUL = view.findViewById(R.id.isi);
        final TextView ID = view.findViewById(R.id.kode);

        HashMap<String, String> artikel = new HashMap<String, String>();
        artikel = lstData.get(position);

        JUDUL.setText(artikel.get("JUDUL"));
        ID.setText(artikel.get("ID"));

        return view;
    }

}
