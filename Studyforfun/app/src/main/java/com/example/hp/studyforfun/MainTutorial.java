package com.example.hp.studyforfun;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class MainTutorial extends AppCompatActivity {
    SharedPreferences s;
    private TextView descr,name,up,down,subs,views;
    private ImageView file,profile;
    String name1,descr1,up1,down1,subs1,file1,profile1,id,up_stat,down_stat,views1,sub_stat,email1,time1;
    private VideoView v1;
    private RelativeLayout video;
    private ImageButton u1;
    private ImageButton u2;
    private ImageButton d1;
    private ImageButton d2;
    String email2;
    private Button join,joined;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tutorial);


        s = getSharedPreferences("Info", Context.MODE_PRIVATE);
        email2 = s.getString("email","");

        descr = (TextView)findViewById(R.id.descr);
       name = (TextView)findViewById(R.id.name);
     up = (TextView)findViewById(R.id.up);
     down = (TextView)findViewById(R.id.down);
     subs = (TextView)findViewById(R.id.subs);
     views = (TextView)findViewById(R.id.views);

     file = (ImageView)findViewById(R.id.file);
     profile = (ImageView)findViewById(R.id.profile);
     v1 = (VideoView)findViewById(R.id.v);
     video = (RelativeLayout)findViewById(R.id.video);

     u1 = (ImageButton)findViewById(R.id.up1);
     u2 = (ImageButton)findViewById(R.id.up2);
     d1 = (ImageButton)findViewById(R.id.down1);
     d2 = (ImageButton)findViewById(R.id.down2);
     join = (Button)findViewById(R.id.join);
     joined = (Button)findViewById(R.id.joined);

      u1.setVisibility(View.GONE);
      u2.setVisibility(View.GONE);
      d1.setVisibility(View.GONE);
      d2.setVisibility(View.GONE);
      joined.setVisibility(View.GONE);
      join.setVisibility(View.GONE);






     video.setVisibility(View.GONE);
     file.setVisibility(View.GONE);
     v1.setVisibility(View.GONE);

     id = getIntent().getStringExtra("id");
     name1 = getIntent().getStringExtra("name");
     descr1 = getIntent().getStringExtra("descr");
     up1 = getIntent().getStringExtra("up");
     down1 = getIntent().getStringExtra("down");
     subs1 = getIntent().getStringExtra("subs");
     file1 = getIntent().getStringExtra("file");
     profile1 = getIntent().getStringExtra("profile");
     up_stat = getIntent().getStringExtra("up_stat");
     down_stat = getIntent().getStringExtra("down_stat");
     views1 = getIntent().getStringExtra("views");
     sub_stat = getIntent().getStringExtra("sub_stat");
     email1 = getIntent().getStringExtra("email");
     time1 = getIntent().getStringExtra("time");



        Stats s = new Stats();
        s.execute(email2,id,"","","","","","","");



        if(up_stat.equals("1")){
         u2.setVisibility(View.VISIBLE);

     }
     if(up_stat.equals("0")){
         u1.setVisibility(View.VISIBLE);
     }
     if(down_stat.equals("1")){
         d2.setVisibility(View.VISIBLE);
     }
     if(down_stat.equals("0")){
         d1.setVisibility(View.VISIBLE);
     }
     if(sub_stat.equals("0")){
       join.setVisibility(View.VISIBLE);
     }
     if(sub_stat.equals("1")){
         joined.setVisibility(View.VISIBLE);
     }

     join.setOnClickListener(
             new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {

                     join.setVisibility(View.GONE);
                     joined.setVisibility(View.VISIBLE);
                     Stats s = new Stats();
                     s.execute(email2,"","","","","",email2,"",email1);

                 }
             }
     );
     joined.setOnClickListener(
             new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {
                     joined.setVisibility(View.GONE);
                     join.setVisibility(View.VISIBLE);

                     Stats s = new Stats();
                     s.execute(email2,"","","","","","",email2,email1);

                 }
             }
     );

     u1.setOnClickListener(
             new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {
                     u1.setVisibility(View.GONE);
                     u2.setVisibility(View.VISIBLE);
                    up1 = String.valueOf(Integer.parseInt(up1) + 1);

                    if(d2.getVisibility() == View.VISIBLE){
                        d2.setVisibility(View.GONE);
                        down1 = String.valueOf(Integer.parseInt(down1) - 1);
                        d1.setVisibility(View.VISIBLE);
                        Stats s = new Stats();
                        s.execute(email2,id,id,"","",id,"","","");

                    }

                     Stats s = new Stats();
                     s.execute(email2,id,id,"","","","","","");
                     setVal();
                 }
             }
     );
     u2.setOnClickListener(
             new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {
                     u2.setVisibility(View.GONE);
                     u1.setVisibility(View.VISIBLE);
                     up1 = String.valueOf(Integer.parseInt(up1) - 1);
                     Stats s = new Stats();
                     s.execute(email2,id,"",id,"","","","","");
                     setVal();
                 }
             }
     );
     d1.setOnClickListener(
             new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {
                     d1.setVisibility(View.GONE);
                     d2.setVisibility(View.VISIBLE);

                     down1 = String.valueOf(Integer.parseInt(down1) + 1);
                     if(u2.getVisibility() == View.VISIBLE){
                         u2.setVisibility(View.GONE);
                         up1 = String.valueOf(Integer.parseInt(up1) - 1);
                         u1.setVisibility(View.VISIBLE);
                         Stats s = new Stats();
                         s.execute(email2,id,"",id,id,"","","","");

                     }

                     Stats s = new Stats();
                     s.execute(email2,id,"","",id,"","","","");
                     setVal();
                 }
             }
     );
     d2.setOnClickListener(
             new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {
                     d2.setVisibility(View.GONE);
                     d1.setVisibility(View.VISIBLE);
                     down1 = String.valueOf(Integer.parseInt(down1) - 1);
                     Stats s = new Stats();
                     s.execute(email2,id,"","","",id,"","","");
                     setVal();
                 }
             }
     );



     String ext = file1.substring(file1.lastIndexOf(".")+1);
        if (ext.equals("png") || ext.equals("jpg") || ext.equals("jpeg")) {
            try {
                file.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage(file1, file);

            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }
            catch (RuntimeException e){
                e.printStackTrace();
            }
        }
        else if (ext.equals("mp4") || ext.equals("3gp") || ext.equals("wav")) {
            try {
                video.setVisibility(View.VISIBLE);
                 v1.setVisibility(View.VISIBLE);
                v1.setVideoPath(file1);
                v1.setMediaController(new MediaController(MainTutorial.this));
                v1.start();
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }
            catch (RuntimeException e){
                e.printStackTrace();
            }

        }
        else if (ext.equals("pdf") || ext.equals("doc") || ext.equals("word")) {
            try {
                file.setVisibility(View.VISIBLE);
               file.setImageResource(R.drawable.doc);

                   file.setOnClickListener(
                           new View.OnClickListener() {
                               @Override
                               public void onClick(View view) {
                                   Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(file1));
                                   startActivity(i);
                               }
                           }
                   );


            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }
            catch (RuntimeException e){
                e.printStackTrace();
            }

        }


        try {
            ImageLoader.getInstance().displayImage(profile1, profile);

        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        catch (RuntimeException e){
            e.printStackTrace();
        }

        setVal();

    }



    public void setVal(){
        descr.setText(descr1);
        name.setText(name1);
        up.setText(up1);
        down.setText(down1);
        subs.setText(subs1);
        views.setText(views1);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(MainTutorial.this,Tutorial.class);
        startActivity(i);
    }

    class Stats extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection x = null;
            BufferedReader br = null;
            String page_url = "http://studyforfun.000webhostapp.com/main_tutorial.php";
            try {

                String email = strings[0];
                String id = strings[1];
                String id1 = strings[2];
                String id2 = strings[3];
                String id3 = strings[4];
                String id4 = strings[5];
                String subs1 = strings[6];
                String subs2 = strings[7];
                String email1 = strings[8];

                URL url = new URL(page_url);
                x = (HttpURLConnection) url.openConnection();
                x.setRequestMethod("POST");
                x.setDoOutput(true);
                x.setDoInput(true);
                OutputStream outputStream = x.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8") + "&" +
                        URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8")
                        + "&" +
                        URLEncoder.encode("id1", "UTF-8") + "=" + URLEncoder.encode(id1, "UTF-8")
                        + "&" +
                        URLEncoder.encode("id2", "UTF-8") + "=" + URLEncoder.encode(id2, "UTF-8")
                        + "&" +
                        URLEncoder.encode("id3", "UTF-8") + "=" + URLEncoder.encode(id3, "UTF-8")
                        + "&" +
                        URLEncoder.encode("id4", "UTF-8") + "=" + URLEncoder.encode(id4, "UTF-8")
                        + "&" +
                        URLEncoder.encode("subs1", "UTF-8") + "=" + URLEncoder.encode(subs1, "UTF-8")
                        + "&" +
                        URLEncoder.encode("email1", "UTF-8") + "=" + URLEncoder.encode(email1, "UTF-8")
                         + "&" +
                        URLEncoder.encode("subs2", "UTF-8") + "=" + URLEncoder.encode(subs2, "UTF-8");

                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = x.getInputStream();
                br = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                StringBuffer buffer = new StringBuffer();
                String line = "";
                while ((line = br.readLine()) != null) {
                    buffer.append(line);
                }
                br.close();
                inputStream.close();
                x.disconnect();
                String res = buffer.toString();


                return res;


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            } finally {
                if (x != null) {
                    x.disconnect();
                }
                try {
                    if (br != null) {
                        br.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }


            return null;
        }


}


    public void home1(View v){

    }



    }



