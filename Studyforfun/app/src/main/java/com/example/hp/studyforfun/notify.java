package com.example.hp.studyforfun;

import android.content.Context;
import android.os.AsyncTask;

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

/**
 * Created by hp on 22-01-2018.
 */

public class notify extends AsyncTask<String,Void,String> {
    private Context context;

    notify (Context ctx) {
        context = ctx;
    }
    @Override
    protected String doInBackground(String... params) {

        String notify_url = "http://studyforfun.000webhostapp.com/notify.php";

        try {
            String name1 = params[0];
            String email1 = params[1];
            String name2 = params[2];
            String email2 = params[3];
            String message = params[4];
            URL url = new URL(notify_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String post_data = URLEncoder.encode("name1","UTF-8")+"="+URLEncoder.encode(name1,"UTF-8")+"&"
                    +URLEncoder.encode("email1","UTF-8")+"="+URLEncoder.encode(email1,"UTF-8")+"&"
                    +URLEncoder.encode("name2","UTF-8")+"="+URLEncoder.encode(name2,"UTF-8")+"&"
                    +URLEncoder.encode("email2","UTF-8")+"="+URLEncoder.encode(email2,"UTF-8")+"&"
                    +URLEncoder.encode("message","UTF-8")+"="+URLEncoder.encode(message,"UTF-8");
            bufferedWriter.write(post_data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
            String result="";
            String line="";
            while((line = bufferedReader.readLine())!= null) {
                result += line;
            }
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();
            return result;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected void onPostExecute(String result) {

    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}



