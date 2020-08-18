package coom.vapestore.Sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.io.IOException;

import coom.vapestore.Util.DB_Adapter;

public class DB_VAPE extends DB_Adapter {
    private Context context;

    public DB_VAPE(Context context) throws IOException {
        super(context);
        this.context = context;
    }

    public void insertBarang(String namaBarang, int qty, String desc, double harga, String imgPath){
        ContentValues val = new ContentValues();

        val.put(VAPE.BARANG_NAMA, namaBarang);
        val.put(VAPE.BARANG_QTY, qty);
        val.put(VAPE.BARANG_DESKRIPSI, desc);
        val.put(VAPE.BARANG_HARGA, harga);
        val.put(VAPE.BARANG_IMAGE, imgPath);

        db.insert(VAPE.TABLE_BARANG, val);
    }

    public Cursor getBarang(){
        return db.fetch(VAPE.TABLE_BARANG);
    }

    public void updateBarang(String namaBarang, int qty, String desc, double harga, String image, int id){
        ContentValues val = new ContentValues();

        val.put(VAPE.BARANG_NAMA, namaBarang);
        val.put(VAPE.BARANG_QTY, qty);
        val.put(VAPE.BARANG_DESKRIPSI, desc);
        val.put(VAPE.BARANG_HARGA, harga);
        val.put(VAPE.BARANG_IMAGE, image);

        db.update(VAPE.TABLE_BARANG, val, VAPE.BARANG_ID + " = " +id);
    }

    public void register(String userId, String password, String secKey, String nama, String noHp){
        ContentValues val = new ContentValues();

        val.put(VAPE.USER_USER_ID, userId);
        val.put(VAPE.USER_PASSWORD, password);
        val.put(VAPE.USER_SECKEY, secKey);
        val.put(VAPE.NAMA, nama);
        val.put(VAPE.USER_NO_HP, noHp);

       db.insert(VAPE.TABLE_USER, val);
    }

    public void changePass(String newPass, String newSecKey, int userId){
        ContentValues val = new ContentValues();

        val.put(VAPE.USER_PASSWORD, newPass);
        val.put(VAPE.USER_SECKEY, newSecKey);

        db.update(VAPE.TABLE_USER, val, VAPE.USER_ID + " = " + userId);
    }

    public int login(String secKey){
        Cursor csr = db.rawQuery("SELECT * FROM USER WHERE SEC_KEY = '"+ secKey +"'");
        int login = csr.getCount();
        return login;
    }

    public Cursor getUserDetail(String secKey){
        Cursor csr = db.rawQuery("SELECT * FROM USER WHERE SEC_KEY = '"+ secKey +"'");
//        int idUser = Integer.parseInt(csr.getString(0));
        return csr;
    }

    public Cursor getUser(){
        return db.fetch(VAPE.TABLE_USER);
    }

    public Cursor getDetailBarang(int idBarang){
        Cursor csr = db.rawQuery("SELECT * FROM BARANG WHERE ID = "+idBarang);

        return csr;
    }

    public void saveTrx(int trxUserId, String trxUserName, String trxNamaBarang, String jumlah, double total, String alamat, int barangID){
        ContentValues val = new ContentValues();

        val.put(VAPE.TRANSACTION_USER_ID, trxUserId);
        val.put(VAPE.TRANSACTION_USER_NAME, trxUserName);
        val.put(VAPE.TRANSACTION_NAMA_BARANG, trxNamaBarang);
        val.put(VAPE.TRANSACTION_JUMLAH, jumlah);
        val.put(VAPE.TRANSACTION_TOTAL, total);
        val.put(VAPE.TRANSACTION_ALAMAT, alamat);
        val.put(VAPE.TRANSACTION_STATUS, 0);
        val.put(VAPE.TRANSACTION_BARANG_ID, barangID);

        db.insert(VAPE.TABLE_TRX, val);
    }

    public void updateBarang(int qty, int barangId){
        ContentValues val = new ContentValues();

        val.put(VAPE.BARANG_QTY, qty);

        db.update(VAPE.TABLE_BARANG, val, VAPE.BARANG_ID + " = " + barangId);
    }

    public Cursor getUncorfimedTrx(int userId){
        Cursor csr = db.rawQuery("SELECT " +
                "TRX.ID, " +                //0
                "TRX.NAMA_BARANG, " +       //1
                "TRX.JUMLAH, " +            //2
                "TRX.TOTAL, " +             //3
                "TRX.ALAMAT, " +            //4
                "TRX.STATUS, " +            //5
                "TRX.USER_ID," +            //6
                "TRX.USER_NAME, " +          //7
                "TRX.BARANG_ID," +          //8
                "TRX.IMAGE_TRX, " +         //9
                "BARANG.IMAGE_PATH " +       //10
                "FROM TRX " +
                "inner join BARANG on BARANG.ID = TRX.BARANG_ID" +
                " WHERE TRX.STATUS IN (0, 2) AND TRX.USER_ID = "+userId);

        return csr;
    }

    public Cursor getHistoryUser(int userId){
        Cursor csr = db.rawQuery("SELECT " +
                "TRX.ID, " +                //0
                "TRX.NAMA_BARANG, " +       //1
                "TRX.JUMLAH, " +            //2
                "TRX.TOTAL, " +             //3
                "TRX.ALAMAT, " +            //4
                "TRX.STATUS, " +            //5
                "TRX.USER_ID," +            //6
                "TRX.USER_NAME, " +          //7
                "TRX.BARANG_ID," +          //8
                "TRX.IMAGE_TRX, " +         //9
                "BARANG.IMAGE_PATH " +       //10
                "FROM TRX " +
                "inner join BARANG on BARANG.ID = TRX.BARANG_ID" +
                " WHERE TRX.STATUS NOT IN (0, 2) AND TRX.USER_ID = "+userId);

        return csr;
    }

    public Cursor getOrder(){
        Cursor csr = db.rawQuery("SELECT " +
                "TRX.ID, " +                //0
                "TRX.NAMA_BARANG, " +       //1
                "TRX.JUMLAH, " +            //2
                "TRX.TOTAL, " +             //3
                "TRX.ALAMAT, " +            //4
                "TRX.STATUS, " +            //5
                "TRX.USER_ID," +            //6
                "TRX.USER_NAME, " +          //7
                "TRX.BARANG_ID," +          //8
                "TRX.IMAGE_TRX, " +         //9
                "BARANG.IMAGE_PATH " +       //10
                "FROM TRX " +
                "inner join BARANG on BARANG.ID = TRX.BARANG_ID");

        return csr;
    }

    public void confirmTrx(String path, int trxId){
        ContentValues val = new ContentValues();

        val.put(VAPE.TRANSACTION_IMAGE, path);
        val.put(VAPE.TRANSACTION_STATUS, 1);

        db.update(VAPE.TABLE_TRX, val, VAPE.TRANSACTION_ID + " = " + trxId);
    }

    public void confirmTrxAdmibn(int trxId){
        ContentValues val = new ContentValues();

        val.put(VAPE.TRANSACTION_STATUS, 3);

        db.update(VAPE.TABLE_TRX, val, VAPE.TRANSACTION_ID + " = " + trxId);
    }

    public void rejectTrxAdmin(int trxId){
        ContentValues val = new ContentValues();

        val.put(VAPE.TRANSACTION_STATUS, 2);

        db.update(VAPE.TABLE_TRX, val, VAPE.TRANSACTION_ID + " = " + trxId);
    }
}
