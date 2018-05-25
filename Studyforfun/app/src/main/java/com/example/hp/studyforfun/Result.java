package com.example.hp.studyforfun;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class Result extends AppCompatActivity {

    WebView web5;
    String table;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
       table = getIntent().getStringExtra("table");
        String email = getIntent().getStringExtra("email");
        web5 = (WebView)findViewById(R.id.web5);
        WebSettings settings = web5.getSettings();
        settings.setJavaScriptEnabled(true);
        web5.setWebChromeClient(new WebChromeClient());
        web5.loadUrl("http://studyforfun.000webhostapp.com/result1.php?email="+email+"&table="+table);
    }

    public void back(View v){
        Intent i = new Intent(this,QuizActivity.class);
        i.putExtra("table",table);
        startActivity(i);
    }
}
