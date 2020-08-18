package coom.vapestore.Sqlite;

public class VAPE {
    public static final String TABLE_BARANG = "BARANG";
    public static final String TABLE_USER = "USER";
    public static final String TABLE_TRX = "TRX";

    public static final String[] BARANG = {
            "ID",               //0
            "NAMA_BARANG",           //1
            "QTY",       //2
            "DESKRIPSI",         //3
            "HARGA",
            "IMAGE_PATH"
    };

    public static final String BARANG_ID                = BARANG[0];
    public static final String BARANG_NAMA              = BARANG[1];
    public static final String BARANG_QTY               = BARANG[2];
    public static final String BARANG_DESKRIPSI         = BARANG[3];
    public static final String BARANG_HARGA             = BARANG[4];
    public static final String BARANG_IMAGE             = BARANG[5];

    public static final String[] USER = {
            "ID",               //0
            "USER_ID",           //1
            "PASSWORD",       //2
            "SEC_KEY" ,        //3
            "NAMA",             //4
            "NO_HP"
    };

    public static final String USER_ID                  = USER[0];
    public static final String USER_USER_ID             = USER[1];
    public static final String USER_PASSWORD            = USER[2];
    public static final String USER_SECKEY              = USER[3];
    public static final String NAMA                     = USER[4];
    public static final String USER_NO_HP               = USER[5];

    public static final String[] TRANSACTION = {
            "ID",               //0
            "NAMA_BARANG",           //1
            "JUMLAH",       //2
            "TOTAL" ,        //3
            "ALAMAT",             //4
            "STATUS",       //5
            "USER_ID",      //6
            "USER_NAME",     //7
            "BARANG_ID",
            "IMAGE_TRX"
    };

    public static final String TRANSACTION_ID                        = TRANSACTION[0];
    public static final String TRANSACTION_NAMA_BARANG               = TRANSACTION[1];
    public static final String TRANSACTION_JUMLAH                    = TRANSACTION[2];
    public static final String TRANSACTION_TOTAL                     = TRANSACTION[3];
    public static final String TRANSACTION_ALAMAT                    = TRANSACTION[4];
    public static final String TRANSACTION_STATUS                    = TRANSACTION[5];
    public static final String TRANSACTION_USER_ID                   = TRANSACTION[6];
    public static final String TRANSACTION_USER_NAME                 = TRANSACTION[7];
    public static final String TRANSACTION_BARANG_ID                 = TRANSACTION[8];
    public static final String TRANSACTION_IMAGE                     = TRANSACTION[9];
}
