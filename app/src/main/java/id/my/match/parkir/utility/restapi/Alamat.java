package id.my.match.parkir.utility.restapi;

/**
 * Created by MatchID on 28/11/2016.
 */
public class Alamat {
    //    String encrypted = Phass_Class.source("Logout");
    //    Log.e("Link", encrypted + " <=> " + decrypted);

    String link_decrypted = "";

    String server_cek_decrypted = "";
    String data_cek_decrypted = "";

    public String link = "http://parkir.s1creative.com/apix/",
            link_pengguna_user_cek = "pengguna/user.php",
            link_pengguna_laporan_data = "pengguna/laporan.php",
            link_adm_laporan_data = "tindak_lanjut/laporan.php",
            link_adm_user_cek = "tindak_lanjut/user.php";


    public String alamat, login_cek, data_cek, kampus_data, list_laporan_adm, list_laporan_usr, send_laporan;

    public Alamat() {
        this.alamat = link;
        this.login_cek = link_pengguna_user_cek;
        this.kampus_data = link_pengguna_laporan_data;
        this.list_laporan_adm = link_adm_laporan_data;
        this.list_laporan_usr = link_pengguna_laporan_data;
        this.send_laporan = link_pengguna_laporan_data;
    }

    public String getAlamat () {
        return this.alamat;
    }

    public String getLogin() {
        return this.login_cek;
    }

    public String getKampus() {
        return this.kampus_data;
    }

    public String getLaporanAdm_list() {
        return this.list_laporan_adm;
    }

    public String getLaporanAdm_proses() {
        return this.list_laporan_adm;
    }

    public String sendLaporan() {
        return this.send_laporan;
    }

    public String getLaporanUsr_list() {
        return this.list_laporan_usr;
    }

    public String getData_cekktp() {
        return this.data_cek;
    }
}

