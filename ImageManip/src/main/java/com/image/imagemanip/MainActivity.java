package com.image.imagemanip;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.Buffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import javax.xml.datatype.Duration;

public class MainActivity extends Activity {

    private Bitmap image;
    private ImageView imageView;
    private Button greyImageBtn;
    private Button gammaImageBtn;
    private Button hightlightImageBtn;
    private Button invertImageBtn;
    private Button sepiaBtn;
    private Button contrastBtn;
    private Button blurBtn;
    private Button sharpenBtn;
    private Button smoothBtn;
    private Button embossBtn;
    private Button engraveBtn;
    private Button watermarkBtn;
    private Button mirrorBtn;
    private Button flipVerticalBtn;
    private Button tintBtn;
    private Button reflectionBtn;
    private Button saveBtn;

    private ImageManipulator imageManipulator;

    private String imagename;
    private String imageorigname;

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
        sepiaBtn = (Button) findViewById(R.id.SepiaBtn);
        contrastBtn = (Button) findViewById(R.id.ContrastBtn);
        blurBtn = (Button) findViewById(R.id.BlurBtn);
        sharpenBtn = (Button) findViewById(R.id.BlurBtn);
        smoothBtn = (Button) findViewById(R.id.SmoothBtn);
        embossBtn = (Button) findViewById(R.id.EmbossBtn);
        engraveBtn = (Button) findViewById(R.id.EngraveBtn);
        watermarkBtn = (Button) findViewById(R.id.EngraveBtn);
        mirrorBtn = (Button) findViewById(R.id.MirrorBtn);
        flipVerticalBtn = (Button) findViewById(R.id.FlipVerticalBtn);
        tintBtn = (Button) findViewById(R.id.TintBtn);
        reflectionBtn = (Button) findViewById(R.id.ReflectionBtn);
        saveBtn = (Button) findViewById(R.id.SaveBtn);

        imageManipulator = new ImageManipulator();
        imagename = "";
        imageorigname = "";

        imageView.setOnClickListener(LoadImageFromGallery);
        greyImageBtn.setOnClickListener(OnGreyBtnClick);
        gammaImageBtn.setOnClickListener(OnGammaBtnClick);
        hightlightImageBtn.setOnClickListener(OnHighlightImageBtnClick);
        invertImageBtn.setOnClickListener(OnInvertImageBtnClick);
        sepiaBtn.setOnClickListener(OnSepiaBtnClick);
        contrastBtn.setOnClickListener(OnContrastBtnClick);
        blurBtn.setOnClickListener(OnBlurBtnClick);
        sharpenBtn.setOnClickListener(OnSharpenBtnClick);
        smoothBtn.setOnClickListener(OnSmoothBtnClick);
        embossBtn.setOnClickListener(OnEmbossBtnClick);
        engraveBtn.setOnClickListener(OnEngraveBtnClick);
        watermarkBtn.setOnClickListener(OnWatermarkBtnClick);
        mirrorBtn.setOnClickListener(OnMirrorBtnClick);
        flipVerticalBtn.setOnClickListener(OnflipVerticalBtnClick);
        tintBtn.setOnClickListener(OnTintBtnClick);
        reflectionBtn.setOnClickListener(OnReflectionBtnClick);
        saveBtn.setOnClickListener(OnSaveBtnClick);
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

            String[] splitPath = picturePath.split("/");
            imageorigname = splitPath[splitPath.length - 1];
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
            imagename = "Gamma";
            imageView.setImageBitmap(res);
        }
    };

    private View.OnClickListener OnHighlightImageBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Bitmap res = imageManipulator.doHighlightImage(image);
            imagename = "Highlighted";
            imageView.setImageBitmap(res);
        }
    };

    private View.OnClickListener OnInvertImageBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Bitmap res = imageManipulator.doInvert(image);
            imagename = "Inverted";
            imageView.setImageBitmap(res);
        }
    };

    private View.OnClickListener OnSepiaBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Bitmap res = imageManipulator.createSepiaToningEffect(image, 100, 1, 1, 0);
            imagename = "Sepia";
            imageView.setImageBitmap(res);
        }
    };

    private View.OnClickListener OnContrastBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Bitmap res = imageManipulator.createContrast(image, 0.5);
            imagename = "Contrast";
            imageView.setImageBitmap(res);
        }
    };

    private View.OnClickListener OnBlurBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Bitmap res = imageManipulator.applyGaussianBlur(image);
            imagename = "Blur";
            imageView.setImageBitmap(res);
        }
    };

    private View.OnClickListener OnSharpenBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Bitmap res = imageManipulator.sharpen(image, 0.8);
            imagename = "Sharp";
            imageView.setImageBitmap(res);
        }
    };

    private View.OnClickListener OnSmoothBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Bitmap res = imageManipulator.smooth(image, 0.8);
            imagename = "Smooth";
            imageView.setImageBitmap(res);
        }
    };

    private View.OnClickListener OnEmbossBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Bitmap res = imageManipulator.emboss(image);
            imagename = "Emboss";
            imageView.setImageBitmap(res);
        }
    };

    private View.OnClickListener OnEngrossBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Bitmap res = imageManipulator.emboss(image);
            imagename = "Engross";
            imageView.setImageBitmap(res);
        }
    };

    private View.OnClickListener OnEngraveBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Bitmap res = imageManipulator.engrave(image);
            imagename = "Engrave";
            imageView.setImageBitmap(res);
        }
    };

    private View.OnClickListener OnWatermarkBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Point p = new Point(20, 20);
            Bitmap res = imageManipulator.watermark(image, "WATER MARKER", p, Color.BLACK, 1, 5, true);
            imagename = "Watermark";
            imageView.setImageBitmap(res);
        }
    };

    private View.OnClickListener OnMirrorBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Bitmap res = imageManipulator.flip(image, ImageManipulator.FLIP_HORIZONTAL);
            imagename = "Mirror";
            imageView.setImageBitmap(res);
        }
    };

    private View.OnClickListener OnflipVerticalBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Bitmap res = imageManipulator.flip(image, ImageManipulator.FLIP_VERTICAL);
            imagename = "Flip";
            imageView.setImageBitmap(res);
        }
    };

    private View.OnClickListener OnTintBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            PopupTextForResult("Tint", "Set Tint value");
            Bitmap res = imageManipulator.tintImage(image, 60);
            imagename = "Tint";
            imageView.setImageBitmap(res);
        }
    };

    private View.OnClickListener OnReflectionBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Bitmap res = imageManipulator.applyReflection(image);
            imagename = "Reflection";
            imageView.setImageBitmap(res);
        }
    };

    private View.OnClickListener OnSaveBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Bitmap outImage = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            String fileName = imageorigname + "_" + imagename;
        }
    };

    private void WriteToFile(String filename, Bitmap bmp)
    {
        String path = Environment.getExternalStorageDirectory().toString();
        FileOutputStream f = null;
        path += "/ImageManip/" + filename + ".png";
        File file = new File(path);
        try
        {
            f = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, f);
            f.close();
            MediaStore.Images.Media.insertImage(getContentResolver(),file.getAbsolutePath(),file.getName(),file.getName());
        }
        catch (IOException ex)
        {
            Log.i("ImageManip", "failed to save the image");
            Toast.makeText(this, "Failed to save image to storage card", Toast.LENGTH_LONG);
        }
    }

    private void PopupTextForResult(String title, String message)
    {
        String result = "";

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(title);
        alert.setMessage(message);

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String output = input.getText().toString();
                // Do something with value!
            }
        });

    }
}
