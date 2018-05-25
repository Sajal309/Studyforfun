package com.example.hp.studyforfun;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

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

public class HomeActivity extends AppCompatActivity {
    private EditText e1, e2;
    private TextView t1;
    SharedPreferences s;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);
        e1 = (EditText) findViewById(R.id.e1);
        e2 = (EditText) findViewById(R.id.e2);
        t1 = (TextView)findViewById(R.id.t1);



    }

    @Override
    protected void onResume() {


        s = getSharedPreferences("Info",Context.MODE_PRIVATE);
        String name = s.getString("name","");
        String email = s.getString("email","");
        if(!(name.isEmpty())){

            Intent intent = new Intent(HomeActivity.this,MainActivity.class);
            intent.putExtra("name",name);
            intent.putExtra("email",email);
            startActivity(intent);
        }
        super.onResume();




    }

    public void login(View view) {


        BackgroundTask b = new BackgroundTask(this   );
        String name = e2.getText().toString();
        String email = e1.getText().toString();
        b.execute(name, email);

        Intent i = new Intent(this,ConfirmActivity.class);
        i.putExtra("name",name);
        i.putExtra("email",email);
        startActivity(i);

    }

}