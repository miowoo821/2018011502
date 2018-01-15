package com.example.student.a2018011502;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    ImageView img;
    TextView tv;
    TextView tv2;
    TextView tv3;
    ProgressBar pb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        img=(ImageView)findViewById(R.id.imageView);
        tv=(TextView)findViewById(R.id.textView);
        tv2=(TextView)findViewById(R.id.textView2);
        tv3=(TextView)findViewById(R.id.textView3);
        pb=(ProgressBar)findViewById(R.id.progressBar);
    }
    public void click1(View v){

        //先寫一個另外執行緒，才能在下載同時做其他事情
        new Thread(){
            @Override
            public void run() {//執行緒必覆寫的方法
                super.run();
                String str_url="http://www.lolpix.com/_pics/Funny_Pictures_743/Funny_Pictures_7435.jpg";
                URL url;//新增一個網址型態的變數準備接收網址字串
                try {
                    url = new URL(str_url);//URL有try catch
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();//要catch
                    conn.setRequestMethod("GET");//GET 跟 POST?
                    conn.connect();
                    InputStream inputStream=conn.getInputStream();
                    ByteArrayOutputStream bos=new ByteArrayOutputStream();
                    byte[] buf=new byte[1024];//做一個位元組陣列，大小1024(要幹嘛?)

                    final int totallength=conn.getContentLength();//把conn的總大小放進名為totallength的int型態的變數中
                    int sum=0;//新增一個名為sum的int變數

                    int length;//新增一個名為length的int變數
                    while ((length=inputStream.read(buf))!=-1){//當把每次從buf讀到的東西丟進length中，並判斷length會不會等於-1(沒有資料的時候讀到的就是-1，有資料的時候讀到的是buf的1024陣列)
                       // sum += length;//sum=sum+length的意思
                        sum=sum+length;

                        final int tmp=sum;//把sum變數丟進tmp中

                        bos.write(buf,0,length);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tv.setText(String.valueOf(tmp));
                                pb.setProgress(100 * tmp / totallength);
                                img.setVisibility(View.VISIBLE);
                            }
                        });
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

    //AsyncTask<>泛型，傳入值，回報值，結果值
    class MyTask extends AsyncTask<Integer,Integer,String> {

        @Override
        protected String doInBackground(Integer... integers) {//這個方法可以在背景執行內含的程式碼；"..."，可以輸入不確定數量的陣列，一定要擺最後面程式才分得出來誰是誰

            int i;
            for (i=0;i<=integers[0]; i++)
            {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.d("TASK", "doInBackground, i:" + i);
                publishProgress(i);//這個回傳的資料會被protected void onProgressUpdate(Integer... values)抓到
            }

            return "完成";//這個回傳的資料會被下面那個方法(onPostExecute)抓到(進s)
        }

        @Override
        protected void onPostExecute(String s) {//程式執行完會啟動
            tv3.setText(s);
            super.onPostExecute(s);
        }

        @Override
        protected void onPreExecute() {//程式執行前會啟動
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {//程式進度有更新時會啟動
            super.onProgressUpdate(values);
            tv2.setText(String.valueOf(values[0]));
        }


    }

    public void click2(View v){
        MyTask task = new MyTask();
        task.execute(10);
    }


    //重新寫一個AsyncTask來試試
    class MyImageTask extends AsyncTask<String,Integer,Bitmap>{//第一個泛型傳入值：字串(因為要傳入網址解析，所以網址是字串)；第二個泛型回傳值：Interger(因為)；第三個泛型為結果值：BitMap(因為是圖片下載，最後得到的是BitMap
        @Override
        protected Bitmap doInBackground(String... strings) {

            String str_url=strings[0];//接收第一個泛型的傳入值
            URL url;//新增一個網址型態的變數準備接收網址字串
            try {
                url = new URL(str_url);//URL有try catch
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();//要catch
                conn.setRequestMethod("GET");//GET 跟 POST?
                conn.connect();
                InputStream inputStream=conn.getInputStream();
                ByteArrayOutputStream bos=new ByteArrayOutputStream();
                byte[] buf=new byte[1024];//做一個位元組陣列，大小1024(要幹嘛?)

                final int totallength=conn.getContentLength();//把conn的總大小放進名為totallength的int型態的變數中
                int sum=0;//新增一個名為sum的int變數

                int length;//新增一個名為length的int變數
                while ((length=inputStream.read(buf))!=-1){//當把每次從buf讀到的東西丟進length中，並判斷length會不會等於-1(沒有資料的時候讀到的就是-1，有資料的時候讀到的是buf的1024陣列)
                    // sum += length;//sum=sum+length的意思
                    sum=sum+length;
                    final int tmp=sum;//把sum變數丟進tmp中
                    bos.write(buf,0,length);
                }

                byte[] results=bos.toByteArray();
                final Bitmap bmp= BitmapFactory.decodeByteArray(results,0,results.length);
                return bmp;


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            img.setImageBitmap(bitmap);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }
    }

    public void click3(View v){
        MyImageTask task = new MyImageTask();
        task.execute("http://www.lolpix.com/_pics/Funny_Pictures_743/Funny_Pictures_7435.jpg");

    }
}
