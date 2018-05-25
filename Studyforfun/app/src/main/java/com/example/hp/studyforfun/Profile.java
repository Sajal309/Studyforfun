package com.example.hp.studyforfun;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import java.util.ArrayList;
import java.util.List;

public class Profile extends AppCompatActivity {

    TextView name,hobby,city,institute,status,connection,post,like,reaction;
    ImageView profile,setting;
    Button connect;


    SharedPreferences s;
    String email1,email2;
    String name1,hobby1,city1,institute1,status1,connection1,post1,like1,reaction1,profile1;
    LinearLayout bar_l;
    ProgressBar bar1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        s = getSharedPreferences("Info",MODE_PRIVATE);

        email1 = s.getString("email","");
        email2 = getIntent().getStringExtra("email");



        name = (TextView)findViewById(R.id.name);
        hobby = (TextView)findViewById(R.id.hobby);
       institute = (TextView)findViewById(R.id.institute);
       status = (TextView)findViewById(R.id.status);
       city = (TextView)findViewById(R.id.city);
       connection = (TextView)findViewById(R.id.connection);
       post = (TextView)findViewById(R.id.post);
       like = (TextView)findViewById(R.id.like);
       reaction = (TextView)findViewById(R.id.reaction);
       profile = (ImageView)findViewById(R.id.profile);
       connect = (Button)findViewById(R.id.connect);
        setting = (ImageView)findViewById(R.id.setting);

        bar_l = (LinearLayout)findViewById(R.id.bar_l);
        bar1 = (ProgressBar)findViewById(R.id.bar1);

       ProfileTask t = new ProfileTask();
       t.execute(email2);
      if(email1.equals(email2)){
          connect.setVisibility(View.GONE);
          setting.setVisibility(View.VISIBLE);
      }

      else{
          connect.setVisibility(View.VISIBLE);
          setting.setVisibility(View.GONE);
      }

     connect.setOnClickListener(
             new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {
                     Intent i = new Intent(Profile.this,chat.class);
                     i.putExtra("email",email2);
                     i.putExtra("name",name1);
                     i.putExtra("profile",profile1);
                     startActivity(i);
                 }
             }
     );


    }
    class ProfileTask extends AsyncTask<String, Void, Void> {


        @Override
        protected Void doInBackground(String... strings) {
            HttpURLConnection x = null;
            BufferedReader br = null;
            String page_url = "http://studyforfun.000webhostapp.com/profile1.php";
            try {

                String email = strings[0];
                URL url = new URL(page_url);
                x = (HttpURLConnection) url.openConnection();
                x.setRequestMethod("POST");
                x.setDoOutput(true);
                x.setDoInput(true);
                OutputStream outputStream = x.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8");

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

                JSONObject o1 = new JSONObject(res);
                JSONArray a1 = o1.getJSONArray("details");

                    JSONObject o2 = a1.getJSONObject(0);
                    name1 = o2.getString("name");
                    hobby1 = o2.getString("hobby");
                    city1 = o2.getString("city");
                    institute1 = o2.getString("institute");
                    status1 = o2.getString("status");
                    connection1 = o2.getString("connection");
                    post1 = o2.getString("posts");
                    like1 = o2.getString("likes");
                    reaction1 = o2.getString("reactions");
                    profile1 = o2.getString("profile");


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
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

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            hobby.setText(hobby1);
            name.setText(name1);
            city.setText(city1);
            institute.setText(institute1);
            connection.setText(connection1);
            post.setText(post1);
            like.setText(like1);
            reaction.setText(reaction1);
            status.setText(status1);

            ImageLoader.getInstance().displayImage(profile1, profile);

            bar_l.setVisibility(View.GONE);
            bar1.setVisibility(View.GONE);


        }
    }
}
