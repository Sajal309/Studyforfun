package com.example.hp.studyforfun;

import android.content.SharedPreferences;
import android.media.Image;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class Panel extends AppCompatActivity {

    TextView name,post,views,up,down,popluarity,ranking;
    ImageView profile;
    Button subs;
    String name1,post1,views1,up1,down1,popularity1,ranking1,subs1,profile1;
    LinearLayout main;
    ProgressBar bar1;
    SharedPreferences s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel);
        s = getSharedPreferences("Info",MODE_PRIVATE);
        String email = s.getString("email","");
        name = (TextView)findViewById(R.id.name);
        post = (TextView)findViewById(R.id.post);
        views = (TextView)findViewById(R.id.views);
        up = (TextView)findViewById(R.id.up);
        down = (TextView)findViewById(R.id.down);
        popluarity = (TextView)findViewById(R.id.popular);
        ranking = (TextView)findViewById(R.id.rank);
        profile  = (ImageView)findViewById(R.id.profile);
        subs = (Button)findViewById(R.id.subs);
        main = (LinearLayout)findViewById(R.id.main);
        bar1 = (ProgressBar)findViewById(R.id.bar1);
        main.setVisibility(View.GONE);
        PanelTask t = new PanelTask();
        t.execute(email);


    }

    class PanelTask extends AsyncTask<String, Void, Void> {


        @Override
        protected Void doInBackground(String... strings) {
            HttpURLConnection x = null;
            BufferedReader br = null;
            String page_url = "http://studyforfun.000webhostapp.com/panel.php";
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
                post1 = o2.getString("post");
                views1 = o2.getString("views");
                up1 = o2.getString("up");
                down1 = o2.getString("down");
                subs1 = o2.getString("subs");
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
           name.setText(name1);
           subs.setText(subs1);
           post.setText(post1);
           views.setText(views1);
           up.setText(up1);
           down.setText(down1);
          main.setVisibility(View.VISIBLE);
          bar1.setVisibility(View.GONE);

            ImageLoader.getInstance().displayImage(profile1, profile);

        }
    }
}
