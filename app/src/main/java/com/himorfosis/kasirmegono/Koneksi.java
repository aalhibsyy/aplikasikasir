package com.himorfosis.kasirmegono;

public class Koneksi {

//    public static final String URL = "http://156.67.221.226/";
    public static final String URL = "http://192.168.80.183/";

    public static final String login = URL + "api_pos_rxking/api/auth";

    public static final String produk_api = URL + "api_pos_rxking/api/produk";
    public static final String produk_tambah = URL + "api_pos_rxking/api/produk/add";
    public static final String produk_update = URL + "api_pos_rxking/api/produk/update";
    public static final String produk_delete = URL + "api_pos_rxking/api/produk/delete";


    public static final String pengguna_api = URL + "api_pos_rxking/api/pengguna";
    public static final String pengguna_update = URL + "api_pos_rxking/api/pengguna/update";
    public static final String pengguna_delete = URL + "api_pos_rxking/api/pengguna/delete";

    public static final String pelanggan_api = URL + "api_pos_rxking/api/pelanggan";
    public static final String pelanggan_update = URL + "api_pos_rxking/api/pelanggan/update";
    public static final String pelanggan_delete = URL + "api_pos_rxking/api/pelanggan/delete";

    public static final String kasir = URL + "api_pos_rxking/api/pengguna";
    public static final String kasir_post = URL + "api_pos_rxking/api/kasirpost.php";
    public static final String kasir_update = URL + "api_pos_rxking/api/kasirupdate.php";

    public static final String pemesanan_post = URL + "api_pos_rxking/api/pemesanan/post";
    public static final String pemesanan_api = URL + "api_pos_rxking/api/pemesanan";

    public static final String gambar = URL + "api_pos_rxking/upload/";

}

