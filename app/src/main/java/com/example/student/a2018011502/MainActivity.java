package com.example.student.a2018011502;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        img=(ImageView)findViewById(R.id.imageView);
    }
    public void click1(View v){

        //先寫一個另外執行緒，才能在下載同時做其他事情
        new Thread(){
            @Override
            public void run() {//執行緒必覆寫的方法
                super.run();
                String str_url="http://www.lolpix.com/_pics/Funny_Pictures_743/Funny_Pictures_7435.jpg";
                URL url;
                try {
                    url = new URL(str_url);//URL有try catch
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();//要catch
                    conn.setRequestMethod("GET");//GET 跟 POST?
                    conn.connect();
                    InputStream inputStream=conn.getInputStream();
                    ByteArrayOutputStream bos=new ByteArrayOutputStream();
                    byte[] buf=new byte[1024];
                    int length;
                    while ((length=inputStream.read(buf))!=-1){
                        bos.write(buf,0,length);
                    }

                    byte[] results=bos.toByteArray();
                    final Bitmap bmp= BitmapFactory.decodeByteArray(results,0,results.length);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                                img.setImageBitmap(bmp);

                        }
                    });

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }.start();//不要忘了開始

    }
}
