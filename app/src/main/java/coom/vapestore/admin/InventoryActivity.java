package coom.vapestore.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import coom.vapestore.R;
import coom.vapestore.Sqlite.DB_VAPE;
import coom.vapestore.Util.DateUtil;
import coom.vapestore.adapter.BarangAdapter;
import coom.vapestore.model.BarangModel;
import coom.vapestore.model.UserModel;

public class InventoryActivity extends AppCompatActivity {

    RecyclerView rvListBarang;
    DB_VAPE db;
    Cursor csr;
    Context context;
    BarangAdapter adapter;
    List<BarangModel> list = new ArrayList<>();
    Button btnPdfInv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        rvListBarang = findViewById(R.id.rv_listBarang);
        btnPdfInv = findViewById(R.id.btnPdfInventory);
        adapter = new BarangAdapter(this, list);

        try{
            db = new DB_VAPE(this);
        } catch (Exception e){
            Toast.makeText(this, e.getCause().getMessage(), Toast.LENGTH_SHORT).show();
        }

        initUI();
        btnPdfInv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                write();
            }
        });
    }

    private void initUI(){
        list.clear();
        try{
            db.open();
            csr = db.getBarang();
            for (int i= 0; i<csr.getCount(); i++){
                csr.moveToPosition(i);
                BarangModel item = new BarangModel();
                item.setBarangId(Integer.parseInt(csr.getString(0)));
                item.setNamaBarang(csr.getString(1));
                item.setQty(csr.getString(2));
                item.setDesc(csr.getString(3));
                item.setHarga(csr.getString(4));
                item.setImagePath(csr.getString(5));
                list.add(item);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }

        adapter.notifyDataSetChanged();
        rvListBarang.setLayoutManager(new LinearLayoutManager(this));
        rvListBarang.setHasFixedSize(true);
        rvListBarang.setAdapter(adapter);
    }

    public Boolean write (){
        try {
            PdfWriter writer;
            //Create file path for Pdf
            String fpath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
                    + File.separator + "Inventory - "+DateUtil.indonesiaFormat()+".pdf";
            Log.i("pdf path", fpath);
            File file = new File(fpath);
            if (!file.exists()) {
                file.createNewFile();
            }
            // To customise the text of the pdf
            // we can use FontFamily
            Font bfBold12 = new Font(Font.FontFamily.TIMES_ROMAN,
                    15, Font.BOLD, new BaseColor(0, 0, 0));
            Font bf12 = new Font(Font.FontFamily.TIMES_ROMAN,
                    12);
            Font bf10 = new Font(Font.FontFamily.TIMES_ROMAN,
                    10);
            // create an instance of itext document
            Document document = new Document();
            writer = PdfWriter.getInstance(document,
                    new FileOutputStream(file.getAbsoluteFile()));
            document.open();
            //using add method in document to insert a paragraph
//          document.add(new Paragraph("My First Pdf !"));
//          document.add(new Paragraph("Hello World"));
            //Header
            Paragraph companyName = new Paragraph("Vape Ech");
            Drawable d = getResources ().getDrawable (R.drawable.logoreport);
            Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
            Bitmap btmp = Bitmap.createScaledBitmap(bitmap, 50, 50, false);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            btmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] bitmapData = stream.toByteArray();
            Image image = Image.getInstance(bitmapData);
            companyName.add (new Chunk(image, -265, -40, true));
            companyName.setAlignment(Element.ALIGN_CENTER);
            companyName.setFont(bfBold12);
            document.add(companyName);

            Paragraph address = new Paragraph("Jl. Cipinang Besar, RT.5/RW.6, Kecamatan Jatinegara, Kota Jakarta Timur");
            address.setAlignment(Element.ALIGN_CENTER);
            address.setFont(bf12);
            document.add(address);

            Paragraph city = new Paragraph("Daerah Khusus Ibukota Jakarta");
            city.setAlignment(Element.ALIGN_CENTER);
            city.setFont(bf12);
            document.add(city);

            document.add( Chunk.NEWLINE );

            // Creating a table

            LineSeparator line = new LineSeparator();
            document.add(line);

            PdfPTable table = new PdfPTable(5);
            PdfPCell cellOne = new PdfPCell(new Phrase("Nomor", bf10));
            PdfPCell cellTwo = new PdfPCell(new Phrase("Nama Barang", bf10));
            PdfPCell cellThree = new PdfPCell(new Phrase("Qty",bf10));
            PdfPCell cellFour = new PdfPCell(new Phrase("Deskripsi", bf10));
            PdfPCell cellFive = new PdfPCell(new Phrase("Harga", bf10));

            cellOne.setBorder(Rectangle.BOX);
            cellTwo.setBorder(Rectangle.BOX);
            cellThree.setBorder(Rectangle.BOX);
            cellFour.setBorder(Rectangle.BOX);
            cellFive.setBorder(Rectangle.BOX);

            table.addCell(cellOne);
            table.addCell(cellTwo);
            table.addCell(cellThree);
            table.addCell(cellFour);
            table.addCell(cellFive);


            document.add(table);

            for (BarangModel item : list){
                Log.i("size list", String.valueOf(list.size()));
                PdfPTable table2 = new PdfPTable(5);
                PdfPCell id = new PdfPCell(new Phrase(String.valueOf(item.getBarangId()), bf10));
                PdfPCell namaBarang = new PdfPCell(new Phrase(item.getNamaBarang(), bf10));
                PdfPCell qty = new PdfPCell(new Phrase(item.getQty(),bf10));
                PdfPCell desc = new PdfPCell(new Phrase(item.getDesc(),bf10));
                PdfPCell harga = new PdfPCell(new Phrase(item.getHarga(),bf10));

                id.setBorder(Rectangle.BOX);
                namaBarang.setBorder(Rectangle.BOX);
                qty.setBorder(Rectangle.BOX);
                desc.setBorder(Rectangle.BOX);
                harga.setBorder(Rectangle.BOX);

                table2.addCell(id);
                table2.addCell(namaBarang);
                table2.addCell(qty);
                table2.addCell(desc);
                table2.addCell(harga);

                document.add(table2);
//                Log.i("user", gson.toJson(item));
            }

            document.add( Chunk.NEWLINE );
            document.add( Chunk.NEWLINE );
            document.add( Chunk.NEWLINE );
            document.add( Chunk.NEWLINE );
            document.add( Chunk.NEWLINE );

            PdfPTable tableSignature = new PdfPTable(1);
            tableSignature.setHorizontalAlignment(Element.ALIGN_RIGHT);
            PdfPCell dateNow = new PdfPCell(new Phrase("Jakarta, "+ DateUtil.indonesiaFormat(), bf10));
            dateNow.setBorder(Rectangle.NO_BORDER);
            dateNow.setHorizontalAlignment(Element.ALIGN_RIGHT);
            PdfPCell owner = new PdfPCell(new Phrase("Ikbal", bf10));
            owner.setBorder(Rectangle.NO_BORDER);
            owner.setHorizontalAlignment(Element.ALIGN_RIGHT);
            PdfPCell job = new PdfPCell(new Phrase("Kepala Toko", bf10));
            job.setBorder(Rectangle.NO_BORDER);
            job.setHorizontalAlignment(Element.ALIGN_RIGHT);
            PdfPCell space = new PdfPCell(new Phrase("", bf10));
            space.setBorder(Rectangle.NO_BORDER);

            tableSignature.addCell(dateNow);
            tableSignature.addCell(space);
            tableSignature.addCell(space);
            tableSignature.addCell(space);
            tableSignature.addCell(space);
            tableSignature.addCell(space);
            tableSignature.addCell(owner);
            tableSignature.addCell(job);



            document.add(tableSignature);

//            Paragraph date = new Paragraph("Jakarta, "+ DateUtil.indonesiaFormat());
//            date.setAlignment(Element.ALIGN_RIGHT);
//            date.setFont(bf12);
//            document.setMargins(180, 108, 72, 36);
//            document.add(date);

            // close document
            document.close();
            Toast.makeText(InventoryActivity.this, "Report Inventory sudah didownload dalam bentuk PDF", Toast.LENGTH_SHORT).show();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (DocumentException e) {
            e.printStackTrace();
            return false;
        }
    }
}
