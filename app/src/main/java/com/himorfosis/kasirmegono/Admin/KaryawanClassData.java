package com.himorfosis.kasirmegono.Admin;

public class KaryawanClassData {

    private Integer id_karyawan;
    private String nama;
    private String email;
    private String handphone;
    private String alamat;
    private String password;


    public Integer getId_karyawan() {
        return id_karyawan;
    }

    public void setId_karyawan(Integer id_karyawan) {
        this.id_karyawan = id_karyawan;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHandphone() {
        return handphone;
    }

    public void setHandphone(String handphone) {
        this.handphone = handphone;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }
}
