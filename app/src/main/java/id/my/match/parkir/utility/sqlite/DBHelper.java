package id.my.match.parkir.utility.sqlite;

/**
 * Created by admin on 20/07/2018.
 */

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

public class DBHelper extends SQLiteOpenHelper {

    private static final String db_name ="parkir";
    private static final int db_version=3;
    private SQLiteDatabase db = this.getReadableDatabase();

    public DBHelper(Context context) {
        super(context, db_name, null, db_version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table IF NOT EXISTS tb_akun (id TEXT, id_user TEXT, username TEXT, email TEXT, " +
                "nama_lengkap TEXT, tlp TEXT, status_akun TEXT, kampus TEXT, nprm TEXT)");
        db.execSQL("create table IF NOT EXISTS tb_bukan_parkir (id TEXT, nama_lokasi TEXT, lokasi TEXT, koordinat TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        updateDatabase(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        updateDatabase(db);
    }

    private void updateDatabase(SQLiteDatabase db){
        db.execSQL("DROP TABLE IF EXISTS tb_akun ");
        db.execSQL("DROP TABLE IF EXISTS tb_bukan_parkir ");
        onCreate(db);
    }

    public HashMap<String, String> getUserDetails() {
        db = this.getReadableDatabase();
        HashMap<String, String> user = new HashMap<>();
        String selectQuery = "SELECT  * FROM  tb_akun";
        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put("id_user", cursor.getString(1));
            user.put("username", cursor.getString(2));
            user.put("email", cursor.getString(3));
            user.put("nama_lengkap", cursor.getString(4));
            user.put("tlp", cursor.getString(5));
            user.put("status_akun", cursor.getString(6));
            user.put("kampus", cursor.getString(7));
            user.put("nprm", cursor.getString(8));
        }
        cursor.close();
        db.close();

        return user;
    }

    public HashMap<String, String> getBukan() {
        db = this.getReadableDatabase();
        HashMap<String, String> user = new HashMap<>();
        String selectQuery = "SELECT  * FROM  tb_bukan_parkir";
        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put("id", cursor.getString(0));
            user.put("nama", cursor.getString(1));
            user.put("lokasi", cursor.getString(2));
            user.put("koordinat", cursor.getString(3));
        }
        cursor.close();
        db.close();

        return user;
    }

    public void setUserDetails(String conId, String username, String email, String nama_lengkap,
                               String tlp, String status_akun, String kampus, String nprm){

        db = this.getReadableDatabase();
        db.execSQL("insert into tb_akun values( '1','" + conId + "','" +
                username + "','" + email + "','" + nama_lengkap + "','" + tlp + "','" +
                status_akun + "','" + kampus + "','" + nprm + "')");
    }

    public void setBukan(String conId, String nama, String lokasi, String koordinat){

        db = this.getReadableDatabase();
        db.execSQL("insert into tb_bukan_parkir values( '" + conId + "','" +
                nama + "','" + lokasi + "','" + koordinat + "')");
    }

    public void deleteUsers() {
        db = this.getReadableDatabase();
        db.delete("tb_akun", null, null);
        db.close();
    }

    public void deleteBukan() {
        db = this.getReadableDatabase();
        db.delete("tb_bukan_parkir", null, null);
        db.close();
    }
}
