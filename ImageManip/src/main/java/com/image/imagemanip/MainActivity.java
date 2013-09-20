package com.image.imagemanip;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.Buffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private Bitmap image;
    private ImageView imageView;
    private Button greyImageBtn;
    private Button gammaImageBtn;
    private Button hightlightImageBtn;
    private Button invertImageBtn;

    private ImageManipulator imageManipulator;

    private static int RESULT_LOAD_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.imageView);
        greyImageBtn = (Button) findViewById(R.id.GreyImageBtn);
        gammaImageBtn = (Button) findViewById(R.id.GammaImageBtn);
        hightlightImageBtn = (Button) findViewById(R.id.HighlightImageBtn);
        invertImageBtn = (Button) findViewById(R.id.InvertImageBtn);

        imageManipulator = new ImageManipulator();

        imageView.setOnClickListener(LoadImageFromGallery);
        greyImageBtn.setOnClickListener(OnGreyBtnClick);
        gammaImageBtn.setOnClickListener(OnGammaBtnClick);
        hightlightImageBtn.setOnClickListener(OnHighlightImageBtnClick);
        invertImageBtn.setOnClickListener(OnInvertImageBtnClick);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private View.OnClickListener LoadImageFromGallery = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, RESULT_LOAD_IMAGE);
        }
    };

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;
            options.inPurgeable = true;
            image = Bitmap.createBitmap(BitmapFactory.decodeFile(picturePath, options));
            imageView.setImageBitmap(image);
        }
    }

    private View.OnClickListener OnGreyBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Bitmap res= imageManipulator.doGrayscale(image);
            imageView.setImageBitmap(res);
        }
    };

    private View.OnClickListener OnGammaBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Bitmap res = imageManipulator.doGamma(image, 0.6, 0.6, 0.6);
            imageView.setImageBitmap(res);
        }
    };

    private View.OnClickListener OnHighlightImageBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Bitmap res = imageManipulator.doHighlightImage(image);
            imageView.setImageBitmap(res);
        }
    };

    private View.OnClickListener OnInvertImageBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Bitmap res = imageManipulator.doInvert(image);
            imageView.setImageBitmap(res);
        }
    };

}
