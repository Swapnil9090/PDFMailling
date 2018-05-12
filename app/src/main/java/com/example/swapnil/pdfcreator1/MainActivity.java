package com.example.swapnil.pdfcreator1;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;

import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Patterns;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

import javax.mail.Session;

public class MainActivity extends AppCompatActivity {

    EditText edit, edit1;
    Bitmap bitmap, bt2, bt3;
    Button butt1;
    Session session = null;
    TextView emailstatus;
    String emailid = "";
    static final Integer WRITE_EXST = 0x3;
    static final Integer READ_EXST = 0x4;
    String[] permissions;

    String pathname, filename;
    private static final String sub = "Ifest Registration";
    private static String mess;
    private int Regno;
    private AwesomeValidation awesomeValidation;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        permissions= new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE};
        edit = findViewById(R.id.edit);
        emailstatus = findViewById(R.id.tv1);
        Button btn = findViewById(R.id.btn);
        butt1 = findViewById(R.id.button);
        edit1 = findViewById(R.id.edit1);
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        Context context = getApplicationContext();
        Drawable vector = ResourcesCompat.getDrawable(context.getResources(), R.drawable.daiict, null);
        bitmap = ((BitmapDrawable) vector).getBitmap();
        vector = ResourcesCompat.getDrawable(context.getResources(), R.drawable.ifest, null);
        bt2 = ((BitmapDrawable) vector).getBitmap();
        vector = ResourcesCompat.getDrawable(context.getResources(), R.drawable.ieee, null);
        bt3 = ((BitmapDrawable) vector).getBitmap();
        Regno = registratiion_id();
        askForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, WRITE_EXST);
        askForPermission(Manifest.permission.READ_EXTERNAL_STORAGE, READ_EXST);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edit.getText().toString();
                if (name.equals("")) {
                    Toast.makeText(MainActivity.this, "Enter your name", Toast.LENGTH_LONG).show();
                } else {
                    generate_pdf();
                }
            }
        });
        awesomeValidation.addValidation(MainActivity.this, R.id.edit, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.nameerr);
        awesomeValidation.addValidation(MainActivity.this, R.id.edit1, Patterns.EMAIL_ADDRESS, R.string.emailerr);
//        butt1.setOnClickListener((View.OnClickListener) this);
        butt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                emailid = edit1.getText().toString();
                if (emailid.equals("")) {
                    Toast.makeText(MainActivity.this, "Enter a valid email-id", Toast.LENGTH_LONG).show();
                }
                String name = edit.getText().toString();
                if (name.equals("")) {
                    Toast.makeText(MainActivity.this, "Enter your name", Toast.LENGTH_LONG).show();
                } else {
                    //
                    if (awesomeValidation.validate()) {
                        generate_pdf();
                        sending();
                        mess = "Hi " + edit.getText().toString() + ",\nYou have sucessfully registered for the Ifest 2018 and your Receipt is attached below.\nYour Registration number is " + Regno + "\nPlease download the below Receipt.\nRegards,\nIfest-Team 2018";
                        new Thread(new Runnable() {
                            public void run() {
                                try {
                                    GMailSender sender = new GMailSender(
                                            "swapniljethwa22@gmail.com",
                                            "PASS");
                                    sender.addAttachment(Environment.getExternalStorageDirectory().getPath() + "/IEEE-Ifest" + "/" + edit.getText().toString() + ".pdf", Regno);
                                    sender.sendMail(sub, mess,
                                            "swapniljethwa22@gmail.com",
                                            edit1.getText().toString());
                                    success();
                                } catch (Exception e) {
                                    success();
                                    // Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
                                }
                                String myf = Environment.getExternalStorageDirectory().getPath() + "/IEEE-Ifest";
                                File dir = new File(myf);
                                if (dir.isDirectory()) {
                                    String[] child = dir.list();
                                    for (int i = 0; i < child.length; i++) {
                                        new File(dir, child[i]).delete();
                                    }
                                }
                                boolean delete = dir.delete();
                            }
                        }).start();
                    }
                }//
            }
        });
    }

    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCode);
            }
        } else {
           // Toast.makeText(this, "" + permission + " is already granted.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
        butt1.setEnabled(true);
        }
    else
        {
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            butt1.setEnabled(false);
        }
    }
    protected void sending()
    {
        emailstatus.setText("EMAIL STATUS:- SENDING ");
    }
    protected void success()
    {
        emailstatus.setText("EMAIL STATUS:- EMAIL SEND ");
    }
    private void generate_pdf()
    {
        File file = new File(Environment.getExternalStorageDirectory() + "/IEEE-Ifest");
        Log.v("Folder", file.getName());
        pathname=Environment.getExternalStorageDirectory().toString()+"/IEEE-Ifest";
        //  Toast.makeText(MainActivity.this,pathname, Toast.LENGTH_LONG).show();
        if (!file.exists()) file.mkdirs();
        try {
            final File pdf = new File(file, edit.getText().toString() + ".pdf");
            filename=edit.getText().toString()+".pdf";
            pdf.createNewFile();
            Toast.makeText(getApplicationContext(), file.getName(), Toast.LENGTH_SHORT).show();
            Log.v("File", pdf.getName());
            FileOutputStream fout = new FileOutputStream(pdf);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {

                WindowManager wm = (WindowManager) getSystemService(getApplication().WINDOW_SERVICE);
                Display display = wm.getDefaultDisplay();
                DisplayMetrics displaymetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
                float height = displaymetrics.heightPixels;
                float width = displaymetrics.widthPixels;

                int convertHeight = (int) height;
                int convertWidth = (int) width;
                PdfDocument document = new PdfDocument();

                PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(210, 297, 1).create();
                PdfDocument.Page page = document.startPage(pageInfo);

                Canvas canvas = page.getCanvas();
                Paint paint = new Paint();
                paint.setColor(Color.WHITE);
                canvas.drawPaint(paint);
                bitmap = Bitmap.createScaledBitmap(bitmap, 100, 97, true);
                bt2 = Bitmap.createScaledBitmap(bt2, 20, 20, true);
                bt3 = Bitmap.createScaledBitmap(bt3, 69, 80, true);
                canvas.drawBitmap(bitmap, 0, 0, null);
                canvas.drawBitmap(bt2, 40, 0, null);
                canvas.drawBitmap(bt3, 70, 0, null);
                paint.setColor(Color.BLACK);
                canvas.drawText(edit.getText().toString(), 40, 30, paint);
                document.finishPage(page);
                document.writeTo(fout);
                document.close();
            }
        } catch (IOException e) {
            Log.v("Error", "");
        }
    }
    public int registratiion_id()
    {
        Random rand=new Random();
        int n=rand.nextInt(50)+1;
        return n;
    }
}
