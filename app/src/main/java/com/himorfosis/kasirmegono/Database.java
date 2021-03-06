package com.himorfosis.kasirmegono;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.himorfosis.kasirmegono.Kasir.BeliClassData;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteOpenHelper {

    private static final String id_produk = "id_produk";
    private static final String jumlah_produk = "jumlah_produk";
    private static final String harga_produk = "harga_produk";
    private static final String bayar = "bayar";
    private static final String nama_produk = "nama_produk";
    private static final String gambar = "gambar";

    private static final String DatabaseName = "Kasir";
    private static final int DatabaseVersion = 2;

    public Database(Context context) {
        super(context, DatabaseName, null, DatabaseVersion);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE tabelbeli ( id_produk INTEGER, jumlah_produk INTEGER, harga_produk INTEGER, bayar INTEGER, nama_produk VARCHAR, gambar VARCHAR ); ");

    }

    @Override

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS tabelbeli");

        onCreate(db);
    }

    public void addBeli(BeliClassData classData) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(id_produk, classData.getId_produk());
        cv.put(jumlah_produk, classData.getJumlah_produk());
        cv.put(harga_produk, classData.getHarga_produk());
        cv.put(bayar, classData.getBayar());
        cv.put(nama_produk, classData.getNama_produk());
        cv.put(gambar, classData.getGambar());

        Log.e("nama_produkaddbeli", "" +classData.getNama_produk() );
        db.insert("tabelbeli", null, cv);
        db.close();

    }

    public void delBeli(String id) {

        SQLiteDatabase db = this.getWritableDatabase();
        String[] args = {id};
        ContentValues cv = new ContentValues();

        db.delete("tabelbeli", "id_produk=?", args);
        db.close();
    }


    public void updateBeli (BeliClassData classData) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(jumlah_produk, classData.getJumlah_produk());
        cv.put(harga_produk, classData.getHarga_produk());

        cv.put(bayar, classData.getBayar());
        cv.put(nama_produk, classData.getNama_produk());
        cv.put(gambar, classData.getGambar());

        db.update("tabelbeli", cv, id_produk+ " = ?", new String[]{String.valueOf(classData.getId_produk())});
        db.close();

    }

    public List<BeliClassData> getBeli() {
        List<BeliClassData> dataArray = new ArrayList<BeliClassData>();
        String query = "SELECT * FROM tabelbeli";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {

                BeliClassData datalist = new BeliClassData(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2), cursor.getInt(3), cursor.getString(4), cursor.getString(5));

                dataArray.add(datalist);
                Log.e("datalist", "" +datalist );

            } while (cursor.moveToNext());
        }
        return dataArray;
    }

    public void delAllData(String tabel) {

        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("delete from "+ tabel);
        db.close();
    }

}
