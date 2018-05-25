package com.example.hp.studyforfun;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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


public class ConfirmActivity extends AppCompatActivity {
    private EditText e3;
    private TextView t2;
    SharedPreferences s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);
        e3 = (EditText)findViewById(R.id.e3);
         t2  = (TextView)findViewById(R.id.t2);

        String name = getIntent().getStringExtra("name");
        String email = getIntent().getStringExtra("email");
        ConfirmTask t = new ConfirmTask();
        t.execute(name,email);

    }





    String res;
    class ConfirmTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String name = strings[0];
            String email = strings[1];
            try {
                URL url = new URL("http://studyforfun.000webhostapp.com/auth2.php");
                HttpURLConnection x = (HttpURLConnection)url.openConnection();
                x.setRequestMethod("POST");
                x.setDoInput(true);
                x.setDoInput(true);


                OutputStream os = x.getOutputStream();
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
                String data =  URLEncoder.encode("name","UTF-8")+"="+URLEncoder.encode(name,"UTF-8")+"&"
                        +URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(email,"UTF-8");
                bw.write(data);
                bw.flush();
                bw.close();
                os.close();
                InputStream is = x.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"));
                StringBuffer buffer = new StringBuffer();
                String line="";
                while((line = bufferedReader.readLine())!= null) {
                   buffer.append(line);
                }
                bufferedReader.close();
                is.close();
                x.disconnect();
                String res = buffer.toString();
                JSONObject o1 = new JSONObject(res);
                JSONArray a1 = o1.getJSONArray("detail");
                JSONObject o2  = a1.getJSONObject(0);
                String otp = o2.getString("otp");
                return otp;



            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(final  String s) {
            super.onPostExecute(s);
            res = s;






        }
    }
    public void confirm(View view) {


        String otp = e3.getText().toString();

        if(otp.equals(res)){

            String name = getIntent().getStringExtra("name");
            String email = getIntent().getStringExtra("email");
            SharedPreferences.Editor edit = getSharedPreferences("Info",MODE_PRIVATE).edit();
            edit.putString("name",name);
            edit.putString("email",email);
            edit.apply();

            t2.setText("Successfull");

            Intent i = new Intent(ConfirmActivity.this,MainActivity.class);

            i.putExtra("name",name);
            i.putExtra("email",email);
            startActivity(i);
        }
        else {
            t2.setText("Wrong OTP...Enter again");


        }
    }
}
