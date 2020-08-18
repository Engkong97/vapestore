package coom.vapestore.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.List;

import coom.vapestore.DetailActivity;
import coom.vapestore.R;
import coom.vapestore.Sqlite.DB_VAPE;
import coom.vapestore.Util.DateUtil;
import coom.vapestore.Util.ImageUtil;
import coom.vapestore.model.BarangModel;
import coom.vapestore.model.TrxModel;
import coom.vapestore.profile.DetailConfirmTrxActivity;

public class TrxAdapter extends RecyclerView.Adapter<TrxAdapter.ViewHolder> {
    Context context;
    List<TrxModel> list;
    DB_VAPE db;

    public TrxAdapter(Context context, List<TrxModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public TrxAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_trx, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TrxAdapter.ViewHolder holder, final int position) {
        holder.nama.setText(list.get(position).getUserName());
        holder.namaBarang.setText(list.get(position).getNamBarang());
        holder.harga.setText((String.valueOf(list.get(position).getTotalHarga() / Double.valueOf(list.get(position).getJumlahBarang()))));
        holder.jumlah.setText(list.get(position).getJumlahBarang());
        holder.total.setText(String.valueOf(list.get(position).getTotalHarga()));
        holder.status.setText(String.valueOf(list.get(position).getStatus()));
        holder.img.setImageBitmap(ImageUtil.getImagefromPath(list.get(position).getBarangImage()));
        holder.trxCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list.get(position).getStatus().equals("Belum Melakukan Pembayaran")||list.get(position).getStatus().equals("Reject, Pembayaran Tidak Valid")) {
                    Intent intent = new Intent(context, DetailConfirmTrxActivity.class);
                    intent.putExtra("TRXID", String.valueOf(list.get(position).getTrxId()));
                    context.startActivity(intent);
                } else if (list.get(position).getStatus().equals("Sedang Di Proses")){
                    Toast.makeText(context, "Nothing cant be processed", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        PdfWriter writer;
                        //Create file path for Pdf
                        String fpath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
                                + File.separator + "Invoice - "+ DateUtil.indonesiaFormat()+".pdf";
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

                        //Header
                        Paragraph companyName = new Paragraph("Vape Ech");
                        Drawable d = context.getResources ().getDrawable (R.drawable.logoreport);
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

                        Paragraph title = new Paragraph("Invoice");
                        title.setAlignment(Element.ALIGN_CENTER);
                        title.setFont(bf12);
                        document.add(title);

                        document.add( Chunk.NEWLINE );
                        document.add( Chunk.NEWLINE );

                        Paragraph custName = new Paragraph("Nama Customer   : "+ list.get(position).getUserName());
                        custName.setAlignment(Element.ALIGN_LEFT);
                        custName.setFont(bf12);
                        document.add(custName);

                        Paragraph alamat = new Paragraph("Alamat                  : "+ list.get(position).getAlamat());
                        alamat.setAlignment(Element.ALIGN_LEFT);
                        alamat.setFont(bf12);
                        document.add(alamat);

                        document.add( Chunk.NEWLINE );

                        PdfPTable table = new PdfPTable(5);
                        PdfPCell cellOne = new PdfPCell(new Phrase("Nomor", bf10));
                        PdfPCell cellTwo = new PdfPCell(new Phrase("Nama Barang", bf10));
                        PdfPCell cellThree = new PdfPCell(new Phrase("Jumlah Barang", bf10));
                        PdfPCell cellFour = new PdfPCell(new Phrase("Harga", bf10));
                        PdfPCell cellFive = new PdfPCell(new Phrase("Total Harga", bf10));

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

                        PdfPTable table1 = new PdfPTable(5);
                        PdfPCell cellOne1 = new PdfPCell(new Phrase(String.valueOf(list.get(position).getTrxId()), bf10));
                        PdfPCell cellTwo1 = new PdfPCell(new Phrase(list.get(position).getNamBarang(), bf10));
                        PdfPCell cellThree1 = new PdfPCell(new Phrase(list.get(position).getJumlahBarang(),bf10));
                        PdfPCell cellFour1 = new PdfPCell(new Phrase(String.valueOf(list.get(position).getTotalHarga()/Double.valueOf(list.get(position).getJumlahBarang())), bf10));
                        PdfPCell cellFive1 = new PdfPCell(new Phrase(String.valueOf(list.get(position).getTotalHarga()), bf10));

                        cellOne1.setBorder(Rectangle.BOX);
                        cellTwo1.setBorder(Rectangle.BOX);
                        cellThree1.setBorder(Rectangle.BOX);
                        cellFour1.setBorder(Rectangle.BOX);
                        cellFive1.setBorder(Rectangle.BOX);

                        table1.addCell(cellOne1);
                        table1.addCell(cellTwo1);
                        table1.addCell(cellThree1);
                        table1.addCell(cellFour1);
                        table1.addCell(cellFive1);


                        document.add(table1);


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
                        Toast.makeText(context, "Invoice sudah didownload dalam bentuk PDF", Toast.LENGTH_SHORT).show();


                        // close document
                        document.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    }
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView nama,namaBarang, harga, jumlah, total, status;
        ImageView img;
        CardView trxCard;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nama = itemView.findViewById(R.id.nama_pemesan);
            namaBarang = itemView.findViewById(R.id.nama_barang_trx);
            harga = itemView.findViewById(R.id.harga_barang_trx);
            jumlah = itemView.findViewById(R.id.jmlh_barang_trx);
            total = itemView.findViewById(R.id.total_harga_trx);
            status = itemView.findViewById(R.id.status_trx);
            img = itemView.findViewById(R.id.img_uncorfm_trx);
            trxCard = itemView.findViewById(R.id.card_trx);

        }
    }
}
