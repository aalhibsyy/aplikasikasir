package com.himorfosis.kasirmegono;

public class Koneksi {

    public static final String URL = "http://192.168.100.222/";
//    public static final String URL = "http://orproject.web.id/";

    public static final String login = URL + "kasir/api/login.php";

    public static final String kasir = URL + "kasir/api/kasir.php";
    public static final String kasir_post = URL + "kasir/api/kasirpost.php";
    public static final String kasir_update = URL + "kasir/api/kasirupdate.php";

    public static final String mitra = URL + "kasir/api/mitra.php";
    public static final String mitra_post = URL + "kasir/api/mitrapost.php";
    public static final String mitra_update = URL + "kasir/api/mitraupdate.php";

    public static final String produk_api = URL + "kasir/api/produk.php";
    public static final String produk_tambah = URL + "kasir/api/produktambah.php";
    public static final String produk_update = URL + "kasir/api/produkupdate.php";
    public static final String produk_hapus = URL + "kasir/api/produkhapus.php";

    public static final String pemesanan_api = URL + "kasir/api/pemesanan.php";
    public static final String pemesanan_post = URL + "kasir/api/pemesananpost.php";

    public static final String reward_cek = URL + "kasir/api/rewardcek.php";
    public static final String reward_update = URL + "kasir/api/rewardpost.php";
    public static final String reward = URL + "kasir/api/reward.php";


    public static final String gambar = URL + "kasir/gambar/produk/";

}

