package coom.vapestore.Util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;

public class ImageUtil {

    public static Bitmap getImagefromPath(String path){
        Bitmap btmp = null;
        File imgFile = new File(path);

        if(imgFile.exists()){

            btmp = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

        }
        return btmp;
    }
}
