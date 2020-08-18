package coom.vapestore.model;

public class TrxModel {

    public int trxId;
    public String namBarang;
    public String jumlahBarang;
    public Double totalHarga;
    public String alamat;
    public String status;
    public int userId;
    public String userName;
    public String trxImage;
    public String barangImage;


    public int getTrxId() {
        return trxId;
    }

    public void setTrxId(int trxId) {
        this.trxId = trxId;
    }

    public String getNamBarang() {
        return namBarang;
    }

    public void setNamBarang(String namBarang) {
        this.namBarang = namBarang;
    }

    public String getJumlahBarang() {
        return jumlahBarang;
    }

    public void setJumlahBarang(String jumlahBarang) {
        this.jumlahBarang = jumlahBarang;
    }

    public Double getTotalHarga() {
        return totalHarga;
    }

    public void setTotalHarga(Double totalHarga) {
        this.totalHarga = totalHarga;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTrxImage() {
        return trxImage;
    }

    public void setTrxImage(String trxImage) {
        this.trxImage = trxImage;
    }

    public String getBarangImage() {
        return barangImage;
    }

    public void setBarangImage(String barangImage) {
        this.barangImage = barangImage;
    }
}
