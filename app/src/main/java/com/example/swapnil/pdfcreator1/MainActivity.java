package com.example.swapnil.pdfcreator1;

import android.content.Context;
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
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.mail.Session;

public class MainActivity extends AppCompatActivity {

    EditText edit,edit1;
    Bitmap bitmap,bt2,bt3;
    Button butt1;
    Session session =null;
    TextView emailstatus;
    String emailid="";
    String rec="swapniljethwa22@gmail.com";
    String from=rec;
    String pathname,filename;
    String sub="IEEE IFEST REGISTRATION";
    String mess="";
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edit = findViewById(R.id.edit);
        emailstatus=findViewById(R.id.tv1);
        Button btn = findViewById(R.id.btn);
        butt1 = findViewById(R.id.button);
        edit1=findViewById(R.id.edit1);
        Context context = getApplicationContext();
        Drawable vector = ResourcesCompat.getDrawable(context.getResources(), R.drawable.daiict, null);
        bitmap = ((BitmapDrawable) vector).getBitmap();
        vector = ResourcesCompat.getDrawable(context.getResources(), R.drawable.ifest, null);
        bt2 = ((BitmapDrawable) vector).getBitmap();
        vector = ResourcesCompat.getDrawable(context.getResources(), R.drawable.ieee, null);
        bt3 = ((BitmapDrawable) vector).getBitmap();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=edit.getText().toString();
                if (name.equals(""))
                {
                    Toast.makeText(MainActivity.this,"Enter your name", Toast.LENGTH_LONG).show();
                }
                else {
                    generate_pdf();
                }
            }
        });
        butt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                emailid=edit1.getText().toString();
                if(emailid.equals(""))
                {
                    Toast.makeText(MainActivity.this,"Enter a valid email-id", Toast.LENGTH_LONG).show();
                }
                String name=edit.getText().toString();
                if (name.equals(""))
                {
                    Toast.makeText(MainActivity.this,"Enter your name", Toast.LENGTH_LONG).show();
                }
                else {
                    generate_pdf();
                    emailstatus.setText("SENDING...");
                    mess = "This is to inform you that " + name + " have successfully registered for the Ifest-2019. You can find the attached pdf as the Registration proof." + "\n" + "From Ifest 2019 Team";
                    // TODO Auto-generated method stub

                    new Thread(new Runnable() {

                        public void run() {

                            try {

                                GMailSender sender = new GMailSender(
                                        "swapniljethwa22@gmail.com",
                                        "suramya22");
                                sender.addAttachment(Environment.getExternalStorageDirectory().getPath() + "/IEEE-Ifest/"+edit.getText().toString()+ ".pdf");
                                sender.sendMail(sub, mess,
                                        "swapniljethwa22@gmail.com",
                                        emailid);
                                //Toast.makeText(MainActivity.this,"email send", Toast.LENGTH_LONG).show();
                                try{
                                    emailstatus.setText("EMAIL SEND!!");
                                }catch(Exception e)
                                {
                                    success();
                                }
                                String myf = Environment.getExternalStorageDirectory().getPath() + "/IEEE-Ifest";
                                File dir = new File(myf);
                                if (dir.isDirectory()) {
                                    String[] child = dir.list();
                                    for (int i = 0; i < child.length; i++) {
                                        new File(dir, child[i]).delete();
                                    }
                                }
                                boolean delete= dir.delete();
                            } catch (Exception e) {

                                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                            }
                        }
                    }).start();
                }
            }
        });
    }
    protected void success()
    {
        emailstatus.setText("EMAIL SEND ");
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
}
