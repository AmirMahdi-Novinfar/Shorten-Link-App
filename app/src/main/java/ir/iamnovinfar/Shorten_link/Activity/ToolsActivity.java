package ir.iamnovinfar.Shorten_link.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.adivery.sdk.Adivery;
import com.adivery.sdk.AdiveryAdListener;
import com.adivery.sdk.AdiveryBannerAdView;
import com.andreseko.SweetAlert.SweetAlertDialog;
import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


import ir.iamnovinfar.Shorten_link.R;
import saman.zamani.persiandate.PersianDate;

public class ToolsActivity extends AppCompatActivity {
    TextView shorten_txt_url;
    ImageView copy, share;
    File dir;
    ImageView imageview;
    RelativeLayout open_link, generate_qrcode,imageview_qrcode_lay;
    SweetAlertDialog sweetAlertDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tools);


        SetupViews();

        Adivery.configure(getApplication(),"fdde4967-d1b8-42af-996b-4fde9b15ee4d");
        Adivery.prepareInterstitialAd(this, "88db1f31-c7c1-48b3-b62d-9ffdbd8bafd5");
        Adivery.showAd("88db1f31-c7c1-48b3-b62d-9ffdbd8bafd5");
        String link = getIntent().getStringExtra("finaldata");
        shorten_txt_url.setText(link + "");
        showMessage();

        registerReceiver(broadcastReceiver,new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));



        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("link", link);
                clipboard.setPrimaryClip(clip);

                Toast.makeText(ToolsActivity.this, "لینک کپی شد...", Toast.LENGTH_SHORT).show();

            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                /*This will be the actual content you wish you share.*/
                intent.setType("text/plain");
                /*Applying information Subject and Body.*/
                intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "لینک کوتاه شده:");
                intent.putExtra(android.content.Intent.EXTRA_TEXT, link);
                /*Fire!*/
                startActivity(Intent.createChooser(intent, "انتخاب کنید..."));
            }
        });

        open_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(link));
                startActivity(i);
            }
        });

        generate_qrcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    imageview_qrcode_lay.setBackground(getResources().getDrawable(R.drawable.amir2));
                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                    Bitmap bitmap = barcodeEncoder.encodeBitmap(link, BarcodeFormat.QR_CODE, 400, 400);
                    imageview = (ImageView) findViewById(R.id.imageview_qrcode);
                    imageview.setImageBitmap(bitmap);

                    //   SaveImage(bitmap, getApplicationContext());


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        SaveQRImageForAPI24Upper();
                    } else {
                        SaveQRImage();
                    }
                } catch (Exception e) {

                }
            }
        });
    }

    private void showMessage() {

        String timeCreate = getIntent().getStringExtra("timeCreate");
        String status = getIntent().getStringExtra("status");


        int year = Integer.parseInt(timeCreate.substring(0, timeCreate.indexOf("-")));
        int month = Integer.parseInt(timeCreate.substring(5, 7));
        int day = Integer.parseInt(timeCreate.substring(8, 10));


        PersianDate persianDate = new PersianDate();
        persianDate.gregorian_to_jalali(year, month, day);
        com.andreseko.SweetAlert.SweetAlertDialog sweetAlertDialog;


        if (status.contains("New link Successfully Added !")) {
            sweetAlertDialog=new com.andreseko.SweetAlert.SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE);
            sweetAlertDialog.setTitle("تبریک");
            sweetAlertDialog.setContentText("لینک درتاریخ " + persianDate.getShYear() + "/" + persianDate.getShMonth() + "/" + persianDate.getShDay() + " " + "کوتاه شد...");
            sweetAlertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog) {
                    TextView text = (TextView) sweetAlertDialog.findViewById(R.id.content_text);
                    text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                    text.setGravity(Gravity.RIGHT);
                    final Typeface typeface = ResourcesCompat.getFont(ToolsActivity.this, R.font.byekan);
                    text.setTypeface(typeface);
                    TextView text2 = (TextView) sweetAlertDialog.findViewById(R.id.title_text);
                    text2.setTypeface(typeface);
                }
            });

            sweetAlertDialog.setConfirmButton("باشه", new com.andreseko.SweetAlert.SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(com.andreseko.SweetAlert.SweetAlertDialog sweetAlertDialog) {
               sweetAlertDialog.dismiss();
                }
            });
            sweetAlertDialog.setCancelable(true);
            sweetAlertDialog.show();

        } else if (status.contains("Link already Exist !")) {


            sweetAlertDialog=new com.andreseko.SweetAlert.SweetAlertDialog(this, com.andreseko.SweetAlert.SweetAlertDialog.WARNING_TYPE);
            sweetAlertDialog.setTitle("هشدار!");
            sweetAlertDialog.setContentText("لینک درتاریخ" + persianDate.getShYear() + "/" + persianDate.getShMonth() + "/" + persianDate.getShDay()  + "کوتاه شده بود");
            sweetAlertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog) {
                    TextView text = (TextView) sweetAlertDialog.findViewById(R.id.content_text);
                    text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                    text.setGravity(Gravity.RIGHT);
                    final Typeface typeface = ResourcesCompat.getFont(ToolsActivity.this, R.font.byekan);
                    text.setTypeface(typeface);
                    TextView text2 = (TextView) sweetAlertDialog.findViewById(R.id.title_text);
                    text2.setTypeface(typeface);
                }
            });
            sweetAlertDialog.setConfirmButton("باشه", new com.andreseko.SweetAlert.SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(com.andreseko.SweetAlert.SweetAlertDialog sweetAlertDialog) {
                    sweetAlertDialog.dismiss();
                }
            });
            sweetAlertDialog.setCancelable(true);
            sweetAlertDialog.show();

        }


    }


    private void SetupViews() {

        shorten_txt_url = findViewById(R.id.shorten_txt_url);
        copy = findViewById(R.id.btn_copy_clip);
        share = findViewById(R.id.btn_share);
        open_link = findViewById(R.id.open_link2);
        generate_qrcode = findViewById(R.id.generate_qrcode2);
        imageview_qrcode_lay=findViewById(R.id.imageview_qrcode_lay);


    }

//    private static void SaveImage(Bitmap finalBitmap,Context context) {
//
//        String root = Environment.getExternalStorageDirectory().getAbsolutePath();
//        File myDir = new File(root + "/LinkShortener");
//        myDir.mkdirs();
//
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.US);
//        Date now = new Date();
//        String time = formatter.format(now);
//
//        String fname = time +".jpg";
//        File file = new File (myDir, fname);
//        if (file.exists ()) file.delete ();
//        try {
//            FileOutputStream out = new FileOutputStream(file);
//            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
//            out.flush();
//            out.close();
//
//            Toast.makeText(context, "بارکد در حافظه شما ذخیره شد...", Toast.LENGTH_SHORT).show();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//

    private void SaveQRImage() {
        if (imageview.getDrawable() != null) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) imageview.getDrawable();
            Bitmap bitmap = bitmapDrawable.getBitmap();

            FileOutputStream outputStream = null;
            File sdcard = Environment.getExternalStorageDirectory();
            dir = new File(sdcard.getAbsolutePath() + "/LinkShortener");

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.US);
            Date now = new Date();
            String time = formatter.format(now);
            dir.mkdir();


            String filename = String.format("%s.jpg", time);
            File outfile = new File(dir, filename);
            try {
                outputStream = new FileOutputStream(outfile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                outputStream.flush();
                outputStream.close();
                String path = dir.getPath();
                Toast.makeText(ToolsActivity.this, "فایل در" + path + " /" + filename + "سیو شد", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(ToolsActivity.this, "مشکلی در سیو کردن عکس وجود دارد لطفا بعدا امتحان کنید", Toast.LENGTH_SHORT).show();

            }
        }
    }

    private void SaveQRImageForAPI24Upper() {
        OutputStream outputStream;
        ContentResolver resolver = this.getContentResolver();
        ContentValues contentValues = new ContentValues();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.US);
        Date now = new Date();
        String time = formatter.format(now);


        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, time + ".jpg");
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg");
        contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, "DCIM" + "/LinkShortener");
        Uri uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        BitmapDrawable bitmapDrawable = (BitmapDrawable) imageview.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();
        try {
            outputStream = ToolsActivity.this.getContentResolver().openOutputStream(uri);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.close();
            Toast.makeText(ToolsActivity.this, "فایل در" + " DCIM " + "/ " + "Link Shortener/ " + time + ".jpg " + "سیو شد", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(ToolsActivity.this, "مشکلی در سیو کردن عکس وجود دارد لطفا بعدا امتحان کنید", Toast.LENGTH_SHORT).show();
        }


    }




    private void setupBannerAd(){
        AdiveryBannerAdView bannerAd = findViewById(R.id.tools_banner1);

        bannerAd.setBannerAdListener(new AdiveryAdListener() {
            @Override
            public void onAdLoaded() {

            }

            @Override
            public void onError(String reason){
            }

            @Override
            public void onAdClicked(){
                // کاربر روی بنر کلیک کرده
            }
        });


        bannerAd.setPlacementId("678fc408-1fac-48d6-8d84-66b5efd1125d");
        bannerAd.loadAd();



    }






    public BroadcastReceiver broadcastReceiver=new BroadcastReceiver() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onReceive(Context context, Intent intent) {

            if (isNetworkConnected(ToolsActivity.this)){
                setupBannerAd();
            }else{
                sweetAlertDialog = new SweetAlertDialog(ToolsActivity.this, SweetAlertDialog.ERROR_TYPE);
                sweetAlertDialog.setTitle("به اینترنت وصل نیستید!!!");
                sweetAlertDialog.setCancelable(false);
                sweetAlertDialog.setConfirmButton("باشه", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                    }
                });
                sweetAlertDialog.show();
            }

        }
    };

    @RequiresApi(api = Build.VERSION_CODES.M)
    private static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }



}