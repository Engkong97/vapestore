package coom.vapestore.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
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
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import coom.vapestore.R;
import coom.vapestore.Sqlite.DB_VAPE;
import coom.vapestore.Util.DateUtil;
import coom.vapestore.adapter.BarangAdapter;
import coom.vapestore.adapter.UserAdapter;
import coom.vapestore.model.BarangModel;
import coom.vapestore.model.UserModel;

import static android.graphics.BitmapFactory.*;

public class ListUserActivity extends AppCompatActivity {
    RecyclerView rvListUser;
    DB_VAPE db;
    Cursor csr;
    Context context;
    UserAdapter adapter;
    List<UserModel> list = new ArrayList<>();
    Button btnPdfUser;
    Gson gson;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_user);

        rvListUser = findViewById(R.id.rv_listUser);
        btnPdfUser = findViewById(R.id.btnPdfUser);
        adapter = new UserAdapter(this, list);
        gson = new Gson();

        try{
            db = new DB_VAPE(this);
        } catch (Exception e){
            Toast.makeText(this, e.getCause().getMessage(), Toast.LENGTH_SHORT).show();
        }

        initUI();

        btnPdfUser.setOnClickListener(new View.OnClickListener() {
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
            csr = db.getUser();
            Log.i("list sixe", String.valueOf(csr.getCount()));
            for (int i= 0; i<csr.getCount(); i++){
                csr.moveToPosition(i);
                UserModel item = new UserModel();
                item.setUserId(Integer.parseInt(csr.getString(0)));
                item.setNama(csr.getString(4));
                item.setUserName(csr.getString(1));
                item.setNoHp(csr.getString(5));
                list.add(item);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }

        adapter.notifyDataSetChanged();
        rvListUser.setLayoutManager(new LinearLayoutManager(this));
        rvListUser.setHasFixedSize(true);
        rvListUser.setAdapter(adapter);
    }

    public Boolean write (){
        try {
            PdfWriter writer;
            //Create file path for Pdf
            String fpath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
                    + File.separator + "User - "+DateUtil.indonesiaFormat()+".pdf";
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

            PdfPTable table = new PdfPTable(4);
            PdfPCell cellOne = new PdfPCell(new Phrase("Nomor", bf10));
            PdfPCell cellTwo = new PdfPCell(new Phrase("User ID", bf10));
            PdfPCell cellThree = new PdfPCell(new Phrase("Nama",bf10));
            PdfPCell cellFour = new PdfPCell(new Phrase("Nomor Hp", bf10));

            cellOne.setBorder(Rectangle.BOX);
            cellTwo.setBorder(Rectangle.BOX);
            cellThree.setBorder(Rectangle.BOX);
            cellFour.setBorder(Rectangle.BOX);

            table.addCell(cellOne);
            table.addCell(cellTwo);
            table.addCell(cellThree);
            table.addCell(cellFour);

            document.add(table);

            for (UserModel item : list){
                Log.i("size list", String.valueOf(list.size()));
                PdfPTable table2 = new PdfPTable(4);
                PdfPCell id = new PdfPCell(new Phrase(String.valueOf(item.getUserId()), bf10));
                PdfPCell userId = new PdfPCell(new Phrase(item.getUserName(), bf10));
                PdfPCell nama = new PdfPCell(new Phrase(item.getNama(),bf10));
                PdfPCell noHp = new PdfPCell(new Phrase(item.getNoHp(),bf10));

                id.setBorder(Rectangle.BOX);
                userId.setBorder(Rectangle.BOX);
                nama.setBorder(Rectangle.BOX);
                noHp.setBorder(Rectangle.BOX);

                table2.addCell(id);
                table2.addCell(userId);
                table2.addCell(nama);
                table2.addCell(noHp);

                document.add(table2);
                Log.i("user", gson.toJson(item));
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
            Toast.makeText(ListUserActivity.this, "Report User sudah didownload dalam bentuk PDF", Toast.LENGTH_SHORT).show();
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